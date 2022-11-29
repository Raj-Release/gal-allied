package com.shaic.reimbursement.paymentprocess.stoppaymenttracking.request;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.tables.UploadDocumentGridForm;
import com.shaic.claim.rod.wizard.tables.UploadedDocumentsTable;
import com.shaic.domain.MasterService;
import com.shaic.ims.carousel.RevisedCarousel;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.server.Sizeable;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component.Listener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;

public class StopPaymentRequestUI extends ViewComponent {


	private VerticalLayout mainPanel;
	private StopPaymentRequestDto dtoBean;
	private StringBuilder errMsg = new StringBuilder();

	private TextField txtUtrNumber;
	private TextField txtIntimationNo;
	private ComboBox cmbReasonforStopPayment;
	private TextArea txtComments;
	private TextField txtOtherReamrks;
	
	private TextField forCaption;
	
	private OptionGroup optPaymentMode;
	
	private VerticalLayout uploadDocMainLayout;
	
    private Panel uploadDocsPanel;
	
	private Panel uploadedDocsPanel;
	
	private HorizontalLayout hor;
	
	private Window popup;
	@Inject
	private RevisedCarousel revisedCarousel;
	
	@EJB
	private MasterService masterService;

	@Inject
	private UploadDocumentGridFormForStopPayReq uploadDocsTable;
	
	private List<UploadDocumentDTO> uploadedTblList = new ArrayList<UploadDocumentDTO>();
	
	@Inject
	private ReceiptOfDocumentsDTO bean;
	
	
	@Inject
	private UploadedDocumentsTableForStopPayReq uploadedDocsTable;
	
	protected Map<String, Object> referenceData = new HashMap<String, Object>();
	

	
	@PostConstruct
	public void init() {

	}
	
	public void initView(StopPaymentRequestDto result) {
		this.dtoBean=result;
		this.popup=popup;
		mainPanel = new VerticalLayout();
		mainPanel.removeAllComponents();
		
		revisedCarousel.init(this.dtoBean.getPreauthDto()/*.getNewIntimationDTO()*/, "");
		Panel p=new Panel();
		VerticalLayout forSpace= new VerticalLayout();
		VerticalLayout vertical=new VerticalLayout();
		Label caption = new Label("<b style='font-size:x-large;padding-left:40px'>Stop Payment Request</b>",ContentMode.HTML);

		HorizontalLayout forCap = new HorizontalLayout();
		forCap.addComponent(caption);
		forCap.setComponentAlignment(caption, Alignment.TOP_LEFT);
		vertical.addComponent(forCap);
		
		uploadDocsPanel = new Panel();
		uploadedDocsPanel = new Panel();
		
		
		VerticalLayout uploadedDocLayout = new VerticalLayout();
		uploadedDocLayout.setCaption("Uploaded Documents");
		uploadedDocLayout.setSpacing(true);
		uploadedDocLayout.setMargin(true);
		uploadedDocLayout.setWidth("100%");
		
		
		
		uploadDocsTable.init(bean.getUploadDocumentsDTO(), "Stop Payment Request");
		BeanItemContainer<SelectValue> beanContainer = masterService.getStopPaymentRequestCategoryTypes();
		Map<String, Object> referenceDataMap = new HashMap<String, Object>();
		uploadDocsTable.setFileTypeValues(beanContainer);
		
		
		VerticalLayout uploadLayout = new VerticalLayout();
		uploadLayout.setSpacing(true);
		uploadLayout.setMargin(true);
		uploadLayout.setWidth("100%");
		uploadLayout.addComponent(uploadDocsTable);
		uploadDocsPanel.setContent(uploadLayout);
		if(uploadedDocsTable != null){
			uploadedDocsTable.removeRow();
		}
		uploadedDocsTable.init("Uploaded Documents", false);
		uploadedDocsTable.setReference(this.referenceData);
		uploadedDocLayout.addComponent(uploadedDocsTable);
		uploadedDocsPanel.setContent(uploadedDocLayout);
		
		

		mainPanel.addComponent(revisedCarousel);
		mainPanel.addComponent(p);
		mainPanel.addComponent(forSpace);
		mainPanel.addComponent(vertical);
		mainPanel.addComponent(commonTextLayout());
		//mainPanel.addComponent(uploadDocsTable);
		mainPanel.addComponent(uploadDocsPanel);
		mainPanel.addComponent(uploadedDocsPanel);
		mainPanel.addComponent(addFooterButtons());
		mainPanel.setSizeFull();		

		setCompositionRoot(mainPanel);	
		addListener();
		
	}

	@SuppressWarnings("deprecation")
	private void addListener() {
		cmbReasonforStopPayment.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty().getValue();
				if(value != null && value.getValue() != null && !value.getValue().isEmpty() && "Others".equals(value.getValue())){
					txtOtherReamrks.setVisible(true);
					txtOtherReamrks.setRequired(true);
				}else{
					txtOtherReamrks.setVisible(false);
				}
			}
		});
		
     optPaymentMode.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				Boolean value = (Boolean) event.getProperty().getValue();
				if(null != value){
					if(value){
//						dtoBean.setPaymentMode(Boolean.TRUE);
					dtoBean.setReIssuingPaymentMode("Cheque/DD");
					}else{
//						dtoBean.setPaymentMode(Boolean.FALSE);
						dtoBean.setReIssuingPaymentMode("Bank Transfer");
					}
				}
			}

		});
		
	}


	private Component addFooterButtons() {

		HorizontalLayout buttonsLayout = new HorizontalLayout();

		Button	cancelButton = new Button("Cancel");
		cancelButton.addStyleName(ValoTheme.BUTTON_DANGER);
		cancelButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(PopupStopPaymentRequestWizardPresenter.POPUP_CANCEL_REQUEST,null);
			}
		});

		Button	submitButton = new Button("Submit");
		submitButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				
				List<UploadDocumentDTO> uploadedDocs = getUploadedDocsTableValues();
				if(uploadedDocs != null && !uploadedDocs.isEmpty()){
					dtoBean.setUploadedDocsTableList(uploadedDocs);
				}
				if(!validatePage()){
					dtoBean.setResonForStopPayment((SelectValue)cmbReasonforStopPayment.getValue());
					dtoBean.setStopPaymentReqRemarks(txtComments.getValue());
					if(cmbReasonforStopPayment.getValue()!= null && cmbReasonforStopPayment.getValue().toString().equals("Others")){
						dtoBean.setOtherRemarks(txtOtherReamrks.getValue());
						SelectValue val = (SelectValue)cmbReasonforStopPayment.getValue();
						dtoBean.setReasonForStopPaymentKey(val.getId());
						dtoBean.setReasonForStopPaymentValue(val.getValue());
					}
					if(cmbReasonforStopPayment.getValue()!= null && cmbReasonforStopPayment.getValue().toString().equals("Stale DD")){
						SelectValue val = (SelectValue)cmbReasonforStopPayment.getValue();
						dtoBean.setReasonForStopPaymentKey(val.getId());
						dtoBean.setReasonForStopPaymentValue(val.getValue());
					}
					if(cmbReasonforStopPayment.getValue()!= null && cmbReasonforStopPayment.getValue().toString().equals("Name Correction")){
						SelectValue val = (SelectValue)cmbReasonforStopPayment.getValue();
						dtoBean.setReasonForStopPaymentKey(val.getId());
						dtoBean.setReasonForStopPaymentValue(val.getValue());
					}
					
					fireViewEvent(PopupStopPaymentRequestWizardPresenter.POPUP_SUBMIT_REQUEST,dtoBean);
				}else{
					showErrorMessage(errMsg.toString());
				}				
			}
		});

		buttonsLayout.addComponents(submitButton,cancelButton);
		buttonsLayout.setSpacing(true);

		AbsoluteLayout submit_layout =  new AbsoluteLayout();
		submit_layout.addComponent(buttonsLayout, "left: 40%; top: 10%;");
		submit_layout.setWidth("100%");
		submit_layout.setHeight("50px");

		return submit_layout;
	
	}
	
	@SuppressWarnings("static-access")
	private void showErrorMessage(String eMsg) {
		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);
	}



	private Component commonTextLayout() {


		txtUtrNumber = new TextField("Cheque Number");
		txtUtrNumber.setValue(this.dtoBean.getUtrNumber());
		txtUtrNumber.setReadOnly(true);

		txtIntimationNo = new TextField("Intimation Number");
		txtIntimationNo.setValue(this.dtoBean.getIntimationNo());
		txtIntimationNo.setReadOnly(true);

		cmbReasonforStopPayment = new ComboBox("Reason for Stop Payment");

		BeanItemContainer<SelectValue> masterData = masterService.getMasterValueByCode("DD stop payment");
		cmbReasonforStopPayment.setContainerDataSource(masterData);
		cmbReasonforStopPayment.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbReasonforStopPayment.setItemCaptionPropertyId("value");
		cmbReasonforStopPayment.setRequired(true);

		txtComments = new TextArea("Stop Payments Request Remarks");
		txtComments.setDescription("Click the Text Box and Press F8 for Detail Popup");
		txtComments.setWidth(14, Unit.EM);
		txtComments.setMaxLength(500);
		txtComments.setRequired(true);
		handleTextAreaPopup(txtComments,null);
		
		txtOtherReamrks = new TextField("Others Remark");
		txtOtherReamrks.setVisible(false);

		
		
		
		FormLayout firstForm1=new FormLayout();
		firstForm1 = new FormLayout(txtIntimationNo,txtUtrNumber,cmbReasonforStopPayment,txtComments);
		
		
		optPaymentMode=new OptionGroup("Re_issuing Payment mode");
		optPaymentMode.setRequired(true);
		optPaymentMode.addItems(getReadioButtonOptions());
		optPaymentMode.setItemCaption(true, "Cheque/DD");
		optPaymentMode.setItemCaption(false, "Bank Transfer");
		optPaymentMode.setStyleName("horizontal");
		
		TextField txt1=new TextField("");
		txt1.setReadOnly(true);
		txt1.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		TextField txt2=new TextField("");
		txt2.setReadOnly(true);
		txt2.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		FormLayout firstForm2=new FormLayout(optPaymentMode,txt2,txtOtherReamrks);
		FormLayout firstForm3=new FormLayout(/*txt1,txt2,txt1*/);
		

		HorizontalLayout hLayout1 = new HorizontalLayout(firstForm1,firstForm3,firstForm2);
		//hLayout1.setSizeFull();
		hLayout1.setMargin(true);
		hLayout1.setSpacing(true);

		AbsoluteLayout searchlumen_layout =  new AbsoluteLayout();
		searchlumen_layout.addComponent(hLayout1, "left: 5%; top: 0%;");		
		searchlumen_layout.setWidth("100%");
		searchlumen_layout.setHeight("253px");
		return searchlumen_layout;
	
	}
	
	private void optPaymentModeListener() {

		optPaymentMode.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				Boolean value = (Boolean) event.getProperty().getValue();
				if(null != value){
					if(value){
					dtoBean.setReIssuingPaymentMode("Cheque/DD");
					}else{
						dtoBean.setReIssuingPaymentMode("Bank Transfer");
					}
				}
			}

		});
	
		
	}

	protected Collection<Boolean> getReadioButtonOptions() {
		
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		return coordinatorValues;
	}

	@SuppressWarnings("deprecation")
	private boolean validatePage() {
		Boolean hasError = false;
		errMsg.setLength(0);
		if(((cmbReasonforStopPayment!= null && cmbReasonforStopPayment.getValue() == null || (cmbReasonforStopPayment.getValue() != null && cmbReasonforStopPayment.getValidators().toString().length() ==0))|| 
		(txtComments!=null && txtComments.getValue()==null && txtComments.getValue().equalsIgnoreCase("")|| (txtComments.getValue() != null/* && txtComments.getValue().length() ==0 */&& txtComments.getValue().toString().trim().equals(""))) ||
		(null != this.optPaymentMode && null == this.optPaymentMode.getValue())) ||
		((cmbReasonforStopPayment != null && cmbReasonforStopPayment.getValue() != null && (cmbReasonforStopPayment.getValue()!= null &&  cmbReasonforStopPayment.getValue().toString().equals("Others"))) && 
		(txtOtherReamrks!=null && txtOtherReamrks.getValue()==null || (txtOtherReamrks.getValue() != null && txtOtherReamrks.getValue().toString().trim().equals("")/*txtOtherReamrks.getValue().length() ==0*/))) ){
			hasError = true;
			errMsg.append("Please enter all the fields </br>");
		}
		return hasError;
	}
	


	public  void handleTextAreaPopup(TextArea searchField, final  Listener listener) {

		ShortcutListener enterShortCut = new ShortcutListener(
				"ShortcutForRedraftRemarks", ShortcutAction.KeyCode.F8, null) {

			private static final long serialVersionUID = 1L;
			@Override
			public void handleAction(Object sender, Object target) {
				((ShortcutListener) listener).handleAction(sender, target);
			}
		};
		handleShortcutForRedraft(searchField, getShortCutListenerForRemarks(searchField));

	}

	@SuppressWarnings("deprecation")
	public  void handleShortcutForRedraft(final TextArea textField, final ShortcutListener shortcutListener) {
		textField.addFocusListener(new FocusListener() {

			@Override
			public void focus(FocusEvent event) {
				textField.addShortcutListener(shortcutListener);

			}
		});
		textField.addBlurListener(new BlurListener() {

			@Override
			public void blur(BlurEvent event) {

				textField.removeShortcutListener(shortcutListener);

			}
		});
	}
	private ShortcutListener getShortCutListenerForRemarks(final TextArea txtFld)
	{
		ShortcutListener listener =  new ShortcutListener("Redraft Remarks",KeyCodes.KEY_F8,null) {

			private static final long serialVersionUID = 1L;

			@SuppressWarnings({ "static-access", "deprecation" })
			@Override
			public void handleAction(Object sender, Object target) {
				VerticalLayout vLayout =  new VerticalLayout();

				vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				final TextArea txtArea = new TextArea();
				txtArea.setStyleName("Boldstyle"); 
				txtArea.setValue(txtFld.getValue());
				txtArea.setNullRepresentation("");
				txtArea.setSizeFull();
				txtArea.setWidth("100%");
				txtArea.setMaxLength(500);
				txtArea.setReadOnly(false);
				txtArea.setRows(25);
				txtArea.addValueChangeListener(new ValueChangeListener() {

					@Override
					public void valueChange(ValueChangeEvent event) {
							txtFld.setValue(((TextArea)event.getProperty()).getValue());		
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);

				final Window dialog = new Window();

				String strCaption = "Stop Payment Request remarks";

				dialog.setCaption(strCaption);

				dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
				dialog.setWidth("45%");
				dialog.setClosable(true);

				dialog.setContent(vLayout);
				dialog.setResizable(true);
				dialog.setModal(true);
				dialog.setDraggable(true);
				dialog.setData(txtFld);

				dialog.addCloseListener(new Window.CloseListener() {
					@Override
					public void windowClose(CloseEvent e) {
						dialog.close();
					}
				});

				if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
					dialog.setPositionX(350);
					dialog.setPositionY(50);
				}
				getUI().getCurrent().addWindow(dialog);
				okBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;
					@Override
					public void buttonClick(ClickEvent event) {
						dialog.close();
					}
				});	
			}
		};

		return listener;
	}

	public void loadUploadedDocsTableValues(UploadDocumentDTO uploadDocsDTO) {
		if(null != this.uploadedDocsTable){
			uploadDocsDTO.setFileTypeValue(uploadDocsDTO.getFileType().getValue());
			uploadDocsDTO.setFileName(uploadDocsDTO.getFileName());
			if(null != this.dtoBean.getRodNumber())
			{
				uploadDocsDTO.setRodNo(this.dtoBean.getRodNumber());
			}

			uploadedTblList.add(uploadDocsDTO);
			setTableValues();
		}
		
	}

	private void setTableValues() {

		BeanItemContainer<SelectValue> beanContainer = masterService.getStopPaymentRequestCategoryTypes();
		if(null != this.uploadDocsTable)	{
			this.uploadDocsTable.init(new UploadDocumentDTO()/*,bean*/,"Stop Payment Request");
			uploadDocsTable.setFileTypeValues(beanContainer);		
		}
		
		if(null != this.uploadedDocsTable)	{
			uploadedDocsTable.removeRow();
			if(null != uploadedTblList && !uploadedTblList.isEmpty()) {
				 this.uploadedDocsTable.setTableList(uploadedTblList);
			}
		}
	
		
	}

	public void deleteUploadDocumentDetails(UploadDocumentDTO dto) {
		
		if(null != this.uploadDocsTable){
			this.uploadedDocsTable.removeRow(dto);
			this.uploadedTblList.remove(dto);
			uploadedTblList.remove(dto);
			}
	}

	public void editUploadedDocumentDetails(UploadDocumentDTO uploadDTO) {

		BeanItemContainer<SelectValue> beanContainer = masterService.getStopPaymentRequestCategoryTypes();
		if(null != this.uploadDocsTable){
			uploadDocsTable.init(uploadDTO/*,bean*/,"Stop Payment Request");
			uploadDocsTable.setFileTypeValues(beanContainer);
			uploadDocsTable.setValueFromTable(beanContainer, uploadDTO.getFileTypeValue());
			uploadDocsTable.disableFileUploadComponent();
		}
	
		
	}
	
	public List<UploadDocumentDTO>  getUploadedDocsTableValues(){
		List<UploadDocumentDTO> listUploadedDocs = new ArrayList<UploadDocumentDTO>(); 
		if(uploadedDocsTable.getValues() != null && !uploadedDocsTable.getValues().isEmpty()){
			listUploadedDocs = uploadedDocsTable.getValues();
		}
		return listUploadedDocs;
	}

	public void clearReference(){
		SHAUtils.setClearReferenceData(referenceData);
	}



}

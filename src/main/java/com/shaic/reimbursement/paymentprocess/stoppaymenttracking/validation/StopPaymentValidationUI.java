package com.shaic.reimbursement.paymentprocess.stoppaymenttracking.validation;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.PreauthWizardPresenter;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.TmpEmployee;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.carousel.RevisedCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.reimbursement.paymentprocess.stoppaymenttracking.request.StopPaymentRequestDto;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.server.Sizeable;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.BorderStyle;
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
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;

@SuppressWarnings("serial")
public class StopPaymentValidationUI extends ViewComponent {


	private VerticalLayout mainPanel;
	private StopPaymentRequestDto dtoBean;
	private StringBuilder errMsg = new StringBuilder();

	private ComboBox cmbActionTaken;
	private TextArea txtComments;
	private TextField forCaption;
	private Button forTracking;
	private TextField txtUtrNumber;
	private TextField txtIntimationNo;
	private ComboBox cmbReasonforStopPayment;
	private TextArea txtRequestRemarks;
	private OptionGroup optPaymentMode;
	
	private TextField txtRequestedBy;
	private TextField requeestedDate;
	
	private DateField paymentCreditDate;
	
	private DateField paidDate;
	
	private Button viewDocumnet;
	private Window popup;
	
	@EJB
	private MasterService masterService;
	
	private HorizontalLayout submitButtonLayout;
	
	@Inject
	private RevisedCarousel revisedCarousel;
	
	@Inject
	private StopPaymentTrackingTable trailForStopPayment;
	
	@EJB
	private IntimationService intimationService;
	
	
	@PostConstruct
	public void init() {

	}
	
	
	public void initView(StopPaymentRequestDto result) {
		this.dtoBean=result;
		this.popup=popup;
		mainPanel = new VerticalLayout();
		submitButtonLayout = new HorizontalLayout();
		mainPanel.removeAllComponents();
		
		revisedCarousel.init(this.dtoBean.getPreauthDto()/*.getNewIntimationDTO()*/, "");
		Panel p=new Panel();
		VerticalLayout forSpace= new VerticalLayout();
		VerticalLayout vertical=new VerticalLayout();
		Label caption = new Label("<b style='font-size:x-large;padding-left:9px'>Stop Payment Validation</b>",ContentMode.HTML);
		
		VerticalLayout verticalForReq=new VerticalLayout();
		Label captionForReq = new Label("<b style='font-size:x-large;padding-left:40px'>Stop Payment Request</b>",ContentMode.HTML);

		HorizontalLayout forCapReq = new HorizontalLayout();
		forCapReq.addComponent(captionForReq);
		forCapReq.setComponentAlignment(captionForReq, Alignment.TOP_LEFT);
		verticalForReq.addComponent(forCapReq);
		
		
		HorizontalLayout forCap = new HorizontalLayout();
		forCap.addComponent(caption);
		forCap.setComponentAlignment(caption, Alignment.TOP_LEFT);
		vertical.addComponent(forCap);
		
		forTracking=new Button("Stop Payment Tracking");
		viewDocumnet=new Button("View Documents");
		HorizontalLayout forButtons=new HorizontalLayout(forTracking,viewDocumnet);
		forButtons.setSpacing(true);
		AbsoluteLayout sPlayout =  new AbsoluteLayout();
		sPlayout.addComponent(/*forTracking*/forButtons, "left: 1%; top: 20%;");
		sPlayout.setWidth("100%");
		sPlayout.setHeight("50px");
		
		
		mainPanel.addComponent(revisedCarousel);
		mainPanel.addComponent(p);
		mainPanel.addComponent(forSpace);
		mainPanel.addComponent(forSpace);
		mainPanel.addComponent(vertical);
		mainPanel.addComponent(sPlayout);
		mainPanel.addComponent(forSpace);
		mainPanel.addComponent(verticalForReq);
		mainPanel.addComponent(commonTextLayout());
		mainPanel.addComponent(addFooterButtons());
		mainPanel.setSizeFull();		

		setCompositionRoot(mainPanel);	
		addListener();
		
	}

	
	private HorizontalLayout buildSubmitAndCancelBtnLayout(
			StopPaymentRequestDto result) {
		// TODO Auto-generated method stub
		return null;
	}


	private void addListener() {
	    forTracking.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {

				        trailForStopPayment.init("", false, false);
						fireViewEvent(PopupStopPaymentValidationWizardPresenter.GET_STOP_PAYMENT_TRACKING_TRIALS, dtoBean);								
					
			}
		});
	    
	    viewDocumnet.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {

				        if(dtoBean.getIntimationNo() != null){
				        	viewUploadedDocumentDetails(dtoBean.getIntimationNo());
				        }
					
			}
		});
	    

	    cmbActionTaken.addValueChangeListener(new ValueChangeListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 2697682747976915503L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				
				SelectValue value = (SelectValue) event.getProperty().getValue();
				if(value != null) {
					if(value.toString().equals("Approve")){
						paymentCreditDate.setRequired(true);
						paidDate.setRequired(false);
					}
					else if(value.toString().equals("Reject")){
						paidDate.setRequired(true);
						paymentCreditDate.setRequired(false);
					}
				}
				else {
					paymentCreditDate.setRequired(false);
					paidDate.setRequired(false);
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
				fireViewEvent(PopupStopPaymentValidationWizardPresenter.POPUP_CANCEL_VALIDATION_REQUEST,null);
			}
		});

		Button	submitButton = new Button("Submit");
		submitButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if(!validatePage()){
					if(cmbActionTaken.getValue()!= null && cmbActionTaken.getValue().toString().equals("Approve")){
						dtoBean.setForActionTaken("A");
					}
					if(cmbActionTaken.getValue()!= null && cmbActionTaken.getValue().toString().equals("Reject")){
						dtoBean.setForActionTaken("R");
					}
					
					if(paymentCreditDate.getValue()!= null){
						dtoBean.setPaymentCreditDate(paymentCreditDate.getValue());
					}
					
					if(paidDate.getValue()!= null){
						dtoBean.setPaidDate(paidDate.getValue());
					}
					
					dtoBean.setStopPaymentResReamrks(txtComments.getValue());
					
					fireViewEvent(PopupStopPaymentValidationWizardPresenter.POPUP_SUBMIT_VALIDATION_REQUEST,dtoBean);
				}else{
					showErrorMessage(errMsg.toString());
				}				
			}
		});

		buttonsLayout.addComponents(submitButton,cancelButton);
		buttonsLayout.setSpacing(true);

		AbsoluteLayout submit_layout =  new AbsoluteLayout();
		submit_layout.addComponent(buttonsLayout, "left: 30%; top: 10%;");
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



	@SuppressWarnings("deprecation")
	private Component commonTextLayout() {
		
		txtUtrNumber = new TextField("Cheque Number");
		txtUtrNumber.setValue(this.dtoBean.getUtrNumber());
		txtUtrNumber.setReadOnly(true);
		//txtUtrNumber.setStyleName("style = background-color: gray");

		txtIntimationNo = new TextField("Intimation Number");
		txtIntimationNo.setValue(this.dtoBean.getIntimationNo());
		txtIntimationNo.setReadOnly(true);
		//txtIntimationNo.setStyleName("style = background-color: gray");

		cmbReasonforStopPayment = new ComboBox("Reason for Stop Payment");
		
		
		BeanItemContainer<SelectValue> masterData = masterService.getMasterValueByCode("DD stop payment");
		cmbReasonforStopPayment.setReadOnly(false);
		cmbReasonforStopPayment.setContainerDataSource(masterData);
		cmbReasonforStopPayment.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbReasonforStopPayment.setItemCaptionPropertyId("value");
		cmbReasonforStopPayment.setRequired(true);
		for(int i = 0 ; i<masterData.size() ; i++)
	 	{
			if ((this.dtoBean.getReasonForStopPaymentValue()).equalsIgnoreCase(masterData.getIdByIndex(i).getValue()))
			{
				this.cmbReasonforStopPayment.setValue(masterData.getIdByIndex(i));
			}
		}
		
		//cmbReasonforStopPayment.setStyleName("style = background-color: gray");
		cmbReasonforStopPayment.setReadOnly(true);

		txtRequestRemarks = new TextArea("Stop Payments Request Remarks");
		txtRequestRemarks.setDescription("Click the Text Box and Press F8 for Detail Popup");
		txtRequestRemarks.setWidth(14, Unit.EM);
		txtRequestRemarks.setMaxLength(500);
		txtRequestRemarks.setRequired(true);
		txtRequestRemarks.setValue(this.dtoBean.getStopPaymentReqRemarks());
		txtRequestRemarks.setReadOnly(true);
		handleTextAreaPopupRequest(txtRequestRemarks,null);
		//txtRequestRemarks.setStyleName("style = background-color: gray");

		cmbActionTaken = new ComboBox("Action to be Taken");

		BeanItemContainer<SelectValue> actionContainer = getReasonForStopPaymentType();
		cmbActionTaken.setContainerDataSource(actionContainer);
		cmbActionTaken.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbActionTaken.setItemCaptionPropertyId("value");
		cmbActionTaken.setRequired(true);

		txtComments = new TextArea("Stop Payment Response remarks");
		txtComments.setDescription("Click the Text Box and Press F8 for Detail Popup");
		txtComments.setWidth(14, Unit.EM);
		txtComments.setMaxLength(500);
		txtComments.setRequired(true);
		handleTextAreaPopup(txtComments,null);
		
		optPaymentMode=new OptionGroup("Re_issuing Payment mode");
		optPaymentMode.setRequired(true);
		optPaymentMode.addItems(getReadioButtonOptions());
		optPaymentMode.setItemCaption(true, "Cheque/DD");
		optPaymentMode.setItemCaption(false, "Bank Transfer");
		optPaymentMode.setStyleName("horizontal");
		if(dtoBean.getReIssuingPaymentMode() != null && (dtoBean.getReIssuingPaymentMode().equalsIgnoreCase("Cheque/DD") )){
			optPaymentMode.setValue(true);
		}else{
			optPaymentMode.setValue(false);
		}
		optPaymentMode.setReadOnly(true);
		
		TextField txtField = new TextField();
		txtField.setReadOnly(true);
		txtField.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		TmpEmployee userObj = null;
		userObj = masterService.getEmployeeName(this.dtoBean.getRequestBy());
		if(userObj != null && !userObj.equals("")){
			txtRequestedBy = new TextField("Requested By");
			String name=userObj.getLoginId().toUpperCase() + "-" + userObj.getEmpFirstName();
			txtRequestedBy.setValue(name);
			txtRequestedBy.setReadOnly(true);
		}
		
		requeestedDate=new TextField("Requested Date");
		requeestedDate.setValue(SHAUtils.formatDate(this.dtoBean.getRequestedDate()));
		requeestedDate.setReadOnly(true);
		
		paymentCreditDate=new DateField("Payment Credit Date");
		//requeestedDate.setValue(SHAUtils.formatDate(this.dtoBean.getRequestedDate()));
		paymentCreditDate.setReadOnly(false);
		
		addPaymentCreditDateChangeListener();
		
		paidDate=new DateField("Paid Date");
		//requeestedDate.setValue(SHAUtils.formatDate(this.dtoBean.getRequestedDate()));
		paidDate.setReadOnly(false);
		
		addPaidDateChangeListener();
		
		FormLayout firstForm1 = new FormLayout(txtIntimationNo,txtUtrNumber,cmbReasonforStopPayment,txtRequestRemarks,txtField,cmbActionTaken,txtComments);
		FormLayout firstForm2 = new FormLayout(optPaymentMode,txtRequestedBy,requeestedDate,paymentCreditDate,paidDate);
		FormLayout firstForm3=new FormLayout();
		

		

		HorizontalLayout hLayout1 = new HorizontalLayout(firstForm1,firstForm2,firstForm3);
		//hLayout1.setSizeFull();
		hLayout1.setMargin(true);
		hLayout1.setSpacing(true);

		AbsoluteLayout searchlumen_layout =  new AbsoluteLayout();
		searchlumen_layout.addComponent(hLayout1, "left: 5%; top: 0%;");		
		searchlumen_layout.setWidth("100%");
		searchlumen_layout.setHeight("410px");
		return searchlumen_layout;
	
	}
	
      private void handleTextAreaPopupRequest(TextArea searchField, final  Listener listener) {


  		ShortcutListener enterShortCut = new ShortcutListener(
  				"ShortcutForRedraftRemarks", ShortcutAction.KeyCode.F8, null) {

  			private static final long serialVersionUID = 1L;
  			@Override
  			public void handleAction(Object sender, Object target) {
  				((ShortcutListener) listener).handleAction(sender, target);
  			}
  		};
  		handleShortcutForRedraft(searchField, getShortCutListenerForRequestRemarks(searchField));

  	
		
	}


	private ShortcutListener getShortCutListenerForRequestRemarks(
			TextArea txtFld) {

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

				String strCaption = "Stop Payment Request-Remarks";

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
		
		if((cmbActionTaken!=null && cmbActionTaken.getValue()==null || (cmbActionTaken.getValue() != null && cmbActionTaken.getValidators().toString().length() ==0)) ||
				(txtComments!=null && txtComments.getValue()==null || (txtComments.getValue() != null && txtComments.getValue().toString().trim().equals("")))){
			hasError = true;
			errMsg.append("Please enter all the fields </br>");
		}
		
		if(cmbActionTaken!=null && cmbActionTaken.getValue() != null && cmbActionTaken.getValue().toString().equals("Approve")){
			if(paymentCreditDate.getValue() == null)
			{
				hasError = true;
				errMsg.append("Please enter Payment Credit Date field </br>");
			}
		}
		if(cmbActionTaken!=null && cmbActionTaken.getValue() != null && cmbActionTaken.getValue().toString().equals("Reject")){
			if(paidDate.getValue() == null)
			{
				hasError = true;
				errMsg.append("Please enter Paid Date field </br>");
			}
		}
		return hasError;
	}
	
	@SuppressWarnings("deprecation")
	private BeanItemContainer<SelectValue> getReasonForStopPaymentType() {
		
		BeanItemContainer<SelectValue> container = new BeanItemContainer<SelectValue>(SelectValue.class);
		SelectValue selectValue2 = new SelectValue();
		selectValue2.setId(2l);
		selectValue2.setValue("Approve");
		SelectValue selectValue3 = new SelectValue();
		selectValue3.setId(3l);
		selectValue3.setValue("Reject");
		container.addBean(selectValue2);
		container.addBean(selectValue3);
		
		container.sort(new Object[] {"value"}, new boolean[] {true});
		
		return container;
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

				String strCaption = "Stop Payment Response-Remarks";

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
	
	public void showTrackingTrails(
			List<StopPaymentTrackingTableDTO> viewStopPaymentTrackingTableList) {
		
		trailForStopPayment.setTableList(viewStopPaymentTrackingTableList);
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("Stop Payment Tracking-Detailed View");
		popup.setWidth("75%");
		popup.setHeight("75%");
		popup.setContent(trailForStopPayment);				
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	
		
	

	}
	
	protected void viewUploadedDocumentDetails(String intimationNo) {

		BPMClientContext bpmClientContext = new BPMClientContext();
		Map<String,String> tokenInputs = new HashMap<String, String>();
		 tokenInputs.put("intimationNo", intimationNo);
		 String intimationNoToken = null;
		  try {
			  intimationNoToken = intimationService.createJWTTokenForClaimStatusPages(tokenInputs);
		  } catch (NoSuchAlgorithmException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
		  } catch (ParseException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
		  }
		  tokenInputs = null; 
		String url = bpmClientContext.getGalaxyDMSUrl() + intimationNoToken;

		
		getUI().getPage().open(url, "_blank",1500,800,BorderStyle.NONE);
		

	

	
		
	}

	protected void addPaymentCreditDateChangeListener()
	{
		//admissionDate.setValue(this.bean.getPreauthDataExtractionDetails().getAdmissionDate());

		paymentCreditDate.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				//paymentCreditDate.setValue(paymentCreditDate.getValue());

				if(paymentCreditDate.getValue() != null){
					Date currentSystemDate = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					String date1=sdf.format(paymentCreditDate.getValue());
					String currentSystemDate1 = sdf.format(currentSystemDate);
					if((paymentCreditDate.getValue()).after(currentSystemDate))
					{
						paymentCreditDate.setValue(null);
						showErrorMessage("Please Enter Valid Date.");
					}

				}
			}
		});

	}
	
	protected void addPaidDateChangeListener()
	{
		//admissionDate.setValue(this.bean.getPreauthDataExtractionDetails().getAdmissionDate());

		paidDate.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				//paymentCreditDate.setValue(paymentCreditDate.getValue());

				if(paidDate.getValue() != null){
					Date currentSystemDate = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					String date1=sdf.format(paidDate.getValue());
					String currentSystemDate1 = sdf.format(currentSystemDate);
					if((paidDate.getValue()).after(currentSystemDate))
					{
						paidDate.setValue(null);
						showErrorMessage("Please Enter Valid Date.");
					}

				}
			}
		});

	}




}

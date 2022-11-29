package com.shaic.paclaim.addAdditionalDocPaymentInfo.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.wizard.dto.AddAddlDocsPaymentInfoDTO;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.viewEarlierRodDetails.EarlierRodDetailsViewImpl;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.ims.carousel.RevisedCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.ui.themes.Reindeer;

public class AddAditionalDocumentsPaymentInfoPageUI extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7121667116259763884L;
	private VerticalLayout mainPanel;
	
	@Inject
	private ViewDetails viewDetails;
	
	@Inject
	private SelectRODtoAddAddlDocsPaymentInfoTable selectRODtoAddAdditionalDocumentsTable;
	
	@Inject
	private AddAddlDocsPaymentInfoPage uploadDocumentView;
	
	@Inject
	private Instance<RevisedCarousel> commonCarouselInstance;
	
	@Inject
	private ReceiptOfDocumentsDTO bean;
	
	@Inject
	private PaymentInformationUI paymentInformationObj;
	
	private BeanFieldGroup <AddAddlDocsPaymentInfoDTO> binder;
	
	
	
	protected Button btnSubmit;
	protected Button btnCancel;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	@Inject
	private Instance<EarlierRodDetailsViewImpl> earlierRodDetailsViemImplInstance;

	private EarlierRodDetailsViewImpl earlierRodDetailsViewObj;
	
	
	public void init(ReceiptOfDocumentsDTO bean){
//		initBinder();
		this.bean = bean;
		
		//this.bean = new ReceiptOfDocumentsDTO();
//		IntimationDetailsCarousel intimationDetailsCarousel = commonCarouselInstance
//				.get();
//		intimationDetailsCarousel.initOMPCarousal(this.bean.getNewIntimationDto(), "Process OMP Claim -Processor");
		mainPanel = new VerticalLayout();
//		mainPanel.addComponent(intimationDetailsCarousel);
//		mainPanel.addComponent(viewDetailsButtonLayout());
		
		btnSubmit=new Button("Submit");
		btnCancel=new Button("Cancel");
		HorizontalLayout hlaout=new HorizontalLayout(btnSubmit, btnCancel);
		
		RevisedCarousel intimationDetailsCarousel = commonCarouselInstance
				.get();
		intimationDetailsCarousel.init(this.bean.getClaimDTO()
				.getNewIntimationDto(), this.bean.getClaimDTO(),
				"Add Additional documents",this.bean.getDiagnosis());
		mainPanel.addComponent(intimationDetailsCarousel);
		
//		PaymentInformationUI PaymentInformationDetails = paymentInformationInstance.get();
//		PaymentInformationDetails.initView(bean);
//		mainPanel.addComponent(PaymentInformationDetails);
		
		//View details
		//VerticalLayout wizardLayout1 = new VerticalLayout();
		
		Panel panel1 = new Panel();
		panel1.setContent(commonButtonsLayout());
		panel1.setHeight("50px");
		VerticalLayout wizardLayout2 = new VerticalLayout(panel1);
		wizardLayout2.setSpacing(true);
		mainPanel.addComponent(wizardLayout2);
		
		selectRODtoAddAdditionalDocumentsTable.init("", false, true, this.bean);
		//uploadDocumentView = uploadDocumentsViemImpl.get();
		uploadDocumentView.init(this.bean);
		VerticalLayout vlaout=new VerticalLayout();
		vlaout.addComponent(selectRODtoAddAdditionalDocumentsTable);
		HorizontalLayout dummyhLayout = new HorizontalLayout();
		dummyhLayout.setSpacing(true);
		dummyhLayout.setMargin(true);
		mainPanel.addComponent(vlaout);
		mainPanel.addComponent(uploadDocumentView.getContent());
		fireViewEvent(AddAditionalDocumentsPaymentInfoPagePresenter.PAYMENT_INFO_SETUP_DROPDOWN_VALUES, bean);
//		mainPanel.addComponent(buildChequePaymentLayout());
		mainPanel.addComponent(hlaout);
		mainPanel.setComponentAlignment(hlaout,Alignment.BOTTOM_CENTER);
		mainPanel.setSizeFull();
//		buildChequePaymentLayout();
		addListener();
		
		setCompositionRoot(mainPanel);
		
	}
	
//	public void initBinder() {
//		this.binder = new BeanFieldGroup<AddAddlDocsPaymentInfoDTO>(
//				AddAddlDocsPaymentInfoDTO.class);
//		this.binder.setItemDataSource(this.bean.getAddAddlDocsPaymentInfoDetails());
//	}
//	
//	private void buildChequePaymentLayout() 
//	{
//		optPaymentMode = (OptionGroup) binder.buildAndBind("Payment Mode" , "paymentMode" , OptionGroup.class);
//		if(null != optPaymentMode && optPaymentMode.getValue() != null && (boolean) optPaymentMode.getValue())
//		{
//			buildBankTransferLayout();
//		}
//		paymentModeListener();
//		optPaymentMode.setRequired(true);
//		optPaymentMode.addItems(getReadioButtonOptions());
//		optPaymentMode.setItemCaption(true, "Cheque/DD");
//		optPaymentMode.setItemCaption(false, "Bank Transfer");
//		optPaymentMode.setStyleName("horizontal");
//		//Vaadin8-setImmediate() optPaymentMode.setImmediate(true);
//		
//		cmbPayeeName = (ComboBox) binder.buildAndBind("Payee Name", "payeeName" , ComboBox.class);
//		cmbPayeeName.setEnabled(true);
//		
//		if(null == this.bean.getDocumentDetails().getPaymentMode())
//		{
//			if(null != this.bean.getClaimDTO() && 
//					((ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) || ((ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue())))
//					&&  ("Hospital").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()))
//			{
//				if(null != this.bean.getDocumentDetails() && null != this.bean.getDocumentDetails().getPaymentModeFlag() &&
//						(ReferenceTable.PAYMENT_MODE_CHEQUE_DD).equals(this.bean.getDocumentDetails().getPaymentModeFlag()))
//				{
//					
//					
//					if(optPaymentMode != null){
//						optPaymentMode.setValue(true);
//					}
//				}
//				else
//				{
//					if(optPaymentMode != null){
//						optPaymentMode.setValue(false);
//					}
//				}
//
//				if((ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
//				{
//					if(optPaymentMode != null){
//						 optPaymentMode.setReadOnly(false);
//						 optPaymentMode.setEnabled(true);
//					}
//					 btnIFCSSearch.setEnabled(true);
//					
//				}
//				else
//				{
//					if(optPaymentMode != null){
//						optPaymentMode.setReadOnly(true);
//						optPaymentMode.setEnabled(false);
//					}
//				 btnIFCSSearch.setEnabled(false);
//				}
//			}
//			
//			else
//			{
//				optPaymentMode.setValue(true);
//
//			}
//		}
//		else if(null != this.bean.getDocumentDetails().getPaymentMode())
//		{
//			
//			Boolean val = this.bean.getDocumentDetails().getPaymentMode();
//			if(val)
//			{
//				optPaymentMode.setValue(false);
//			}
//			else 
//			{
//				optPaymentMode.setValue(true);
//			}
//			optPaymentMode.setValue(val);
//		}
//		
//		if(null != this.bean.getDocumentDetails().getPayableAt()){
//		txtPayableAt.setValue(this.bean.getDocumentDetails().getPayableAt());	
//		}
//	}
//	private HorizontalLayout buildBankTransferLayout()
//	{
//		txtAccntNo = (TextField)binder.buildAndBind("Account No" , "accountNo", TextField.class);
//		txtAccntNo.setRequired(true);
//		txtAccntNo.setNullRepresentation("");
//		txtAccntNo.setEnabled(true);
//		txtAccntNo.setMaxLength(30);
//		
//		CSValidator accntNoValidator = new CSValidator();
//		accntNoValidator.extend(txtAccntNo);
//		accntNoValidator.setRegExp("^[a-zA-Z 0-9 ]*$");
//		accntNoValidator.setPreventInvalidTyping(true);
//		
//		
//		
//		txtIfscCode = (TextField) binder.buildAndBind("IFSC Code", "ifscCode", TextField.class);
//		txtIfscCode.setRequired(true);
//		txtIfscCode.setNullRepresentation("");
//		txtIfscCode.setEnabled(false);
//		txtIfscCode.setMaxLength(15);
//		CSValidator ifscCodeValidator = new CSValidator();
//		ifscCodeValidator.extend(txtIfscCode);
//		ifscCodeValidator.setRegExp("^[a-zA-Z 0-9]*$");
//		ifscCodeValidator.setPreventInvalidTyping(true);
//		
//		
//		
//		
//		txtBranch = (TextField) binder.buildAndBind("Branch", "branch", TextField.class);
//		txtBranch.setNullRepresentation("");
//		txtBranch.setEnabled(false);
//		txtBranch.setMaxLength(100);
//		
//		
//		
//		txtBankName = (TextField) binder.buildAndBind("Bank Name", "bankName", TextField.class);
//		txtBankName.setNullRepresentation("");
//		txtBankName.setEnabled(false);
//		txtBankName.setMaxLength(100);
//
//		
//		
//		txtCity = (TextField) binder.buildAndBind("City", "city", TextField.class);
//		txtCity.setNullRepresentation("");
//		txtCity.setEnabled(false);
//		
//		
//		
//		
//		if(null != this.bean.getClaimDTO() && (ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) && 
//				("Hospital").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()))
//			
//		{
//			if(null != bean.getDocumentDetails().getAccountNo() && !("").equals(bean.getDocumentDetails().getAccountNo()))
//			{
//				txtAccntNo.setReadOnly(true);
//				txtAccntNo.setEnabled(true);
//			}
//			else
//			{
//				txtAccntNo.setReadOnly(false);
//				txtAccntNo.setEnabled(false);
//			}
//			if(null != bean.getDocumentDetails().getIfscCode() && !("").equals(bean.getDocumentDetails().getIfscCode()))
//			{
//				txtIfscCode.setReadOnly(true);
//				//txtIfscCode.setEnabled(true);
//			}
//			else
//			{
//				txtIfscCode.setReadOnly(false);
//			}
//			
//			txtBranch.setReadOnly(true);
//			txtBranch.setEnabled(false);
//			
//			txtBankName.setReadOnly(true);
//			txtBankName.setEnabled(false);
//			
//			
//			txtCity.setReadOnly(true);
//			txtCity.setEnabled(false);
//			
//		}
//	else if(null != this.bean.getClaimDTO() && (ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) || ((ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue())) && 
//			("Insured").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()))
//	{
//	
//			txtAccntNo.setReadOnly(false);
//			txtAccntNo.setValue(null);
//			txtAccntNo.setNullRepresentation("");
//			
//			txtIfscCode.setReadOnly(false);
//			txtIfscCode.setValue(null);
//			txtIfscCode.setNullRepresentation("");
//			
//			txtBranch.setReadOnly(false);
//			txtBranch.setEnabled(false);
//			txtBranch.setValue(null);;
//			
//			txtBankName.setReadOnly(false);
//			txtBankName.setEnabled(false);
//			txtBankName.setValue(null);
//			txtBankName.setNullRepresentation("");
//			
//			txtCity.setReadOnly(false);
//			txtCity.setEnabled(false);
//			txtCity.setValue(null);
//			txtCity.setNullRepresentation("");
//			
//		}
//	if(("Insured").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()))
//	{
//			if(null != this.bean.getDocumentDetails().getAccountNo())
//			{
//				txtAccntNo.setValue(this.bean.getDocumentDetails().getAccountNo());
//			}
//			if(null != this.bean.getDocumentDetails().getIfscCode())
//			{
//				txtIfscCode.setValue(this.bean.getDocumentDetails().getIfscCode());
//			}
//			if(null != this.bean.getDocumentDetails().getBranch())
//			{
//				txtBranch.setValue(this.bean.getDocumentDetails().getBranch());
//			}
//			if(null != this.bean.getDocumentDetails().getBankName())
//			{
//				txtBankName.setValue(this.bean.getDocumentDetails().getBankName());
//			}
//			if(null != this.bean.getDocumentDetails().getCity())
//			{
//				txtCity.setValue(this.bean.getDocumentDetails().getCity());
//			}
//		}
//		
//		
//		HorizontalLayout lyutIFCS = new HorizontalLayout(txtIfscCode, btnIFCSSearch);
//		btnIFCSSearch.setStyleName(ValoTheme.BUTTON_LINK);
//		btnIFCSSearch.setIcon(new ThemeResource("images/search.png"));
//		lyutIFCS.setComponentAlignment(btnIFCSSearch, Alignment.BOTTOM_CENTER);
//		lyutIFCS.setWidth("88%");
//		lyutIFCS.setCaption("IFSC Code");
//		
//		TextField dField5 = new TextField();
//		dField5.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//		dField5.setReadOnly(true);
//		dField5.setWidth(2,Unit.CM);
//		
//		
//		FormLayout btnLayout = new FormLayout(dField5, btnIFCSSearch);
//		
//		FormLayout bankTransferLayout1 = new FormLayout(txtAccntNo,txtIfscCode,txtBankName,txtCity);
//		bankTransferLayout1.setComponentAlignment(txtAccntNo, Alignment.MIDDLE_RIGHT);
//		TextField dField = new TextField();
//		dField.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//		dField.setReadOnly(true);
//		FormLayout bankTransferLayout2 = new FormLayout(dField,txtBranch);
//		
//		HorizontalLayout hLayout = new HorizontalLayout(bankTransferLayout1 , btnLayout , bankTransferLayout2);
//		hLayout.setSpacing(true);
//		hLayout.setMargin(false);
//		hLayout.addStyleName("gridBorder");
//		if(null != txtAccntNo)
//		{
//			mandatoryFields.add(txtAccntNo);
//			setRequiredAndValidation(txtAccntNo);
//		}
//		if(null != txtIfscCode)
//		{
//			mandatoryFields.add(txtIfscCode);
//			setRequiredAndValidation(txtIfscCode);
//		}
//		return hLayout;
//	}
//	
//	@SuppressWarnings({ "serial", "deprecation" })
//	private void paymentModeListener()
//	{
//		optPaymentMode.addValueChangeListener(new Property.ValueChangeListener() {
//			
//			private static final long serialVersionUID = -1774887765294036092L;
//
//			@Override
//			public void valueChange(ValueChangeEvent event) {
//				//TODO
//				Boolean value = (Boolean) event.getProperty().getValue();
//				
//				if (null != paymentDetailsLayout && paymentDetailsLayout.getComponentCount() > 0) 
//				{
//					paymentDetailsLayout.removeAllComponents();
//				}
//				if(value)
//				{
//					
//					unbindField(getListOfPaymentFields());
//					
//					/*if(null != txtAccntNo)
//					{
//						bean.getDocumentDetails().setAccountNo(txtAccntNo.getValue());
//					}
//					if(null != txtIfscCode)
//					{
//						bean.getDocumentDetails().setIfscCode(txtIfscCode.getValue());
//					}
//					if(null != txtBranch)
//					{
//						bean.getDocumentDetails().setBranch(txtBranch.getValue());
//					}
//					if(null != txtBankName)
//					{
//						bean.getDocumentDetails().setBankName(txtBankName.getValue());
//					}
//					if(null != txtCity)
//					{
//						bean.getDocumentDetails().setCity(txtCity.getValue());
//					}
//					*/
//					
//					unbindField(getListOfPaymentFields());
//					
////					paymentDetailsLayout.addComponent(buildChequePaymentLayout());
//					buildChequePaymentLayout();
//					if(null != txtPayableAt)
//					{
//						mandatoryFields.add(txtPayableAt);
//						setRequiredAndValidation(txtPayableAt);
//						txtPayableAt.setRequired(true);
//						showOrHideValidation(false);
//					}
//					if(null != txtAccntNo)
//						{
//							mandatoryFields.remove(txtAccntNo);
//						}
//						if(null != txtIfscCode)
//						{
//							mandatoryFields.remove(txtIfscCode);
//						}
//				//	bean.getDocumentDetails().setPaymentModeFlag(ReferenceTable.PAYMENT_MODE_CHEQUE_DD);
//						bean.getDocumentDetails().setPaymentModeChangeFlag(ReferenceTable.PAYMENT_MODE_CHEQUE_DD);
//					
//				}
//				else 
//				{
//					if(null != txtPayableAt)
//					{
//						bean.getDocumentDetails().setPayableAt(txtPayableAt.getValue());
//					}
//
//					unbindField(getListOfPaymentFields());
//					HorizontalLayout bankLayout = buildBankTransferLayout();
//					txtAccntNo.setEnabled(true);
//					bankLayout.setMargin(false);
//					txtBankName.setEnabled(false);
//					txtCity.setEnabled(false);
//					btnIFCSSearch.setVisible(true);
//					txtBranch.setEnabled(false);
//					paymentDetailsLayout.addComponent(btnPopulatePreviousAccntDetails);
//					paymentDetailsLayout.setComponentAlignment(btnPopulatePreviousAccntDetails, Alignment.TOP_RIGHT);
////					paymentDetailsLayout.addComponent(buildChequePaymentLayout());
//					buildChequePaymentLayout();
//					paymentDetailsLayout.addComponent(bankLayout);
//					paymentDetailsLayout.setMargin(false);
//					unbindField(txtPayableAt);
//					mandatoryFields.remove(txtPayableAt);
//					
//					bean.getDocumentDetails().setPaymentModeChangeFlag(ReferenceTable.PAYMENT_MODE_BANK_TRANSFER);
//				}
//				
///*				if(cmbReconsiderationRequest != null  || bean.getIsQueryReplyReceived()) {
//					if((bean.getDocumentDetails().getReconsiderationRequestValue() != null && bean.getDocumentDetails().getReconsiderationRequestValue().equalsIgnoreCase("yes"))
//							|| bean.getIsQueryReplyReceived()) {
//						
//						if(null != bean.getDocumentDetails().getPaymentModeFlag() && !(bean.getDocumentDetails().getPaymentModeFlag().equals(bean.getDocumentDetails().getPaymentModeChangeFlag()))){
//							
//							mandatoryFields.add(txtPayModeChangeReason);
//							txtPayModeChangeReason.setValue("");
//							showOrHideValidation(false);
//						}
//						else
//						{
//							mandatoryFields.remove(txtPayModeChangeReason);
//						}
//					}
//				}	*/			
//				
//			}
//		});
//		
//	}
//	
//	protected Collection<Boolean> getReadioButtonOptions() {
//		
//		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
//		coordinatorValues.add(true);
//		coordinatorValues.add(false);
//		return coordinatorValues;
//	}
//	
//	@SuppressWarnings("unused")
//	private List<Field<?>> getListOfPaymentFields() {
//		List<Field<?>> fieldList = new ArrayList<Field<?>>();
//		fieldList.add(cmbPayeeName);
//		fieldList.add(txtEmailId);
//		fieldList.add(txtReasonForChange);
//		fieldList.add(txtPanNo);
//		fieldList.add(txtLegalHeirFirstName);
//		fieldList.add(txtLegalHeirMiddleName);
//		fieldList.add(txtLegalHeirLastName);
//		fieldList.add(txtAccntNo);
//		fieldList.add(txtIfscCode);
//		fieldList.add(txtBranch);
//		fieldList.add(txtBankName);
//		fieldList.add(txtCity);
//		fieldList.add(txtPayableAt);
//		return fieldList;
//	}
	
	private HorizontalLayout commonButtonsLayout() {

		/*
		 * TextField acknowledgementNumber = new
		 * TextField("Acknowledgement Number");
		 * acknowledgementNumber.setValue(String
		 * .valueOf(this.bean.getAcknowledgementNumber()));
		 * //Vaadin8-setImmediate() acknowledgementNumber.setImmediate(true);
		 * acknowledgementNumber.setWidth("250px");
		 * acknowledgementNumber.setHeight("20px");
		 * acknowledgementNumber.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		 * acknowledgementNumber.setReadOnly(true);
		 * acknowledgementNumber.setEnabled(false);
		 * acknowledgementNumber.setNullRepresentation(""); FormLayout hLayout =
		 * new FormLayout (acknowledgementNumber);
		 * hLayout.setComponentAlignment(acknowledgementNumber,
		 * Alignment.MIDDLE_LEFT);
		 */

		Button viewEarlierRODDetails = new Button("View Earlier ROD Details");
		viewEarlierRODDetails.addClickListener(new ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				// viewDetails.getTranslationMiscRequest(bean.getNewIntimationDTO().getIntimationId());
				Window popup = new com.vaadin.ui.Window();
				popup.setCaption("");
				popup.setWidth("75%");
				popup.setHeight("85%");
				earlierRodDetailsViewObj = earlierRodDetailsViemImplInstance
						.get();
				ViewDocumentDetailsDTO documentDetails = new ViewDocumentDetailsDTO();
				documentDetails.setClaimDto(bean.getClaimDTO());
				earlierRodDetailsViewObj.init(bean.getClaimDTO().getKey(), bean
						.getDocumentDetails().getRodKey());
				popup.setContent(earlierRodDetailsViewObj);
				popup.setClosable(true);
				popup.center();
				popup.setResizable(false);
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
		});

		//FormLayout viewEarlierRODLayout = new FormLayout();

		/*FormLayout viewDetailsForm = new FormLayout();
		//Vaadin8-setImmediate() viewDetailsForm.setImmediate(true);
		viewDetailsForm.setWidth("-1px");
		viewDetailsForm.setHeight("-1px");
		viewDetailsForm.setMargin(false);
		viewDetailsForm.setSpacing(true);*/
		// ComboBox viewDetailsSelect = new ComboBox()
		viewDetails.initView(bean.getClaimDTO().getNewIntimationDto()
				.getIntimationId(), bean.getDocumentDetails().getRodKey(),
				ViewLevels.PREAUTH_MEDICAL,"");
		//viewDetailsForm.addComponent(viewDetails);

		// viewDetailsForm.addComponent(viewDetailsSelect);
		// Button goButton = new Button("GO");
		/*
		 * HorizontalLayout horizontalLayout1 = new HorizontalLayout(
		 * viewDetailsForm, goButton);
		 */
		/*
		 * HorizontalLayout horizontalLayout1 = new HorizontalLayout(
		 * viewDetailsForm); horizontalLayout1.setSizeUndefined();
		 * //Vaadin8-setImmediate() horizontalLayout1.setImmediate(true);
		 * horizontalLayout1.setSpacing(true);
		 */

		 HorizontalLayout viewEarlierRODDetailsHLayout = new HorizontalLayout(viewEarlierRODDetails);

		HorizontalLayout componentsHLayout = new HorizontalLayout(viewEarlierRODDetailsHLayout, viewDetails);
		
		VerticalLayout vLayout = new VerticalLayout(componentsHLayout);
		// HorizontalLayout alignmentHLayout = new
		// HorizontalLayout(componentsHLayout);
		componentsHLayout.setWidth("100%");
		
		componentsHLayout.setComponentAlignment(viewDetails,
				Alignment.TOP_RIGHT);
		
		HorizontalLayout hLayout = new HorizontalLayout(vLayout);
		//return componentsHLayout;
		
		hLayout.setWidth("90%");
		hLayout.setSizeUndefined();
		hLayout.setSizeFull();
		
		return hLayout;
	}
	
	public void setFileTypeValues(Map<String, Object> referenceData)
	{
		uploadDocumentView.setFileTypeValues(referenceData);
	}
	
	public void loadUploadedDocsTableValues(UploadDocumentDTO uploadDocsDTO)
	{
		uploadDocumentView.loadUploadedDocsTableValues(uploadDocsDTO);
	}
	
	public void deleteUploadDocumentDetails(UploadDocumentDTO dto) {
		uploadDocumentView.deleteUploadDocumentDetails(dto);
	}
	
	public void editUploadedDocumentDetails(UploadDocumentDTO dto){
		uploadDocumentView.editUploadedDocumentDetails(dto);
	}
	
	public void reset()
	{
		uploadDocumentView.reset();	
	}
	
	public void updateBean(ReceiptOfDocumentsDTO bean, Map<String, Object> referenceData){
		this.bean = bean;
		if(uploadDocumentView != null){
			uploadDocumentView.updateBean(bean);
		}
		if(paymentInformationObj != null){
			paymentInformationObj.initView(bean);
			//paymentInformationObj.loadContainerDataSources(referenceData);
			
			if(mainPanel != null){
				mainPanel.removeComponent(paymentInformationObj);
				mainPanel.addComponent(paymentInformationObj,4);
			}
		}
	}
	
	private void unbindField(List<Field<?>> field) {
		if(null != field && !field.isEmpty())
		{
			for (Field<?> field2 : field) {
				if (field2 != null ) {
					Object propertyId = this.binder.getPropertyId(field2);
					//if (field2!= null && field2.isAttached() && propertyId != null) {
					if (field2!= null  && propertyId != null) {
						this.binder.unbind(field2);
					}
				}
			}
		}
	}
	@SuppressWarnings("unused")
	private void setRequiredAndValidation(Component component) {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		AbstractField<Field> field = (AbstractField<Field>) component;
		field.setRequired(true);
		field.setValidationVisible(false);
	}
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}
	
	private void unbindField(Field<?> field) {
		if (field != null ) {
			Object propertyId = this.binder.getPropertyId(field);
			if (field!= null && propertyId != null) {
				this.binder.unbind(field);
			}
		}
	}
	
	private void addListener()
	{
		btnSubmit.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(selectRODtoAddAdditionalDocumentsTable.isValid() && uploadDocumentView.validatePage() && paymentInformationObj.validatePage()){
					uploadDocumentView.setTableValuesToDTO();
					fireViewEvent(AddAditionalDocumentsPaymentInfoPagePresenter.SUBMIT_DETAILS,
							bean);
					
				}
				
			}
		});
	
		btnCancel.addClickListener(new ClickListener() {
		
		@Override
		public void buttonClick(ClickEvent event) {

			ConfirmDialog dialog = ConfirmDialog.show(getUI(), "Confirmation",
					"Are you sure you want to cancel ?", "Cancel", "Ok",
					new ConfirmDialog.Listener() {

						/**
						 * 
						 */
						private static final long serialVersionUID = 1L;

						public void onClose(ConfirmDialog dialog) {
							if (!dialog.isConfirmed()) {
								fireViewEvent(MenuItemBean.PA_ADD_ADDITIONAL_DOCUMENT_PAYMENT_INFORMATION, true);
								// fireViewEvent(MenuPresenter.SHOW_CREATE_ROD_WIZARD,
								// null);
								// Confirmed to continue
								// fireViewEvent(MenuItemBean.PROCESS_PRE_MEDICAL,
								// null);
								// fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER,
								// null);
							} else {
								// User did not confirm
								dialog.close();
							}
						}
					});

			dialog.setClosable(false);
			dialog.setStyleName(Reindeer.WINDOW_BLACK);

		
		}
	});
	}
	
	public void setUpPayableDetails(String name){
		paymentInformationObj.setUpPayableDetails(name);
	}
	
	public void setUpIFSCDetails(ViewSearchCriteriaTableDTO dto) {
		paymentInformationObj.setUpIFSCDetails(dto);
	}
	
	public void populatePreviousPaymentDetails(PreviousAccountDetailsDTO tableDTO){
		paymentInformationObj.populatePreviousPaymentDetails(tableDTO);
	}

}

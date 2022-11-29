package com.shaic.claim.OMPProcessNegotiation.pages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.IntimationDetailsCarousel;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPCalcualtionUI;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimCalculationViewTable;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimProcessorDTO;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPDocumentDetailListenerTable;
import com.shaic.claim.OMPreceiptofdocumentsbillentry.page.OMPEarlierRodDetailsViewImpl;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.ims.carousel.RevisedCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class OMPProcessNegotiationPageUI extends ViewComponent{
	



//	private static final long serialVersionUID = -6992464970175605792L;
	

	@Inject
	private OMPClaimProcessorDTO bean;
	
	@Inject
	private Instance<OMPCalcualtionUI> calculationInstance;
	
	private OMPCalcualtionUI calculationInstanceObj;
	
	
	@Inject
	private Instance<OMPClaimCalculationViewTable> ompClaimCalcViewTableInstance;
	
	private OMPClaimCalculationViewTable ompClaimCalcViewTableObj;
	
	private VerticalLayout wholeLayout;


	private BeanFieldGroup<OMPClaimProcessorDTO> binder;
	
	//private BeanFieldGroup<PreauthDataExtaractionDTO> paymentbinder;

	
    public ComboBox cmbPayeeName;
	
	private TextField txtEmailId;
	
	private TextField txtPanNo;
	
	@Inject
	private Instance<OMPDocumentDetailListenerTable> documentDetailsViewTableInstance;
	
	private OMPDocumentDetailListenerTable documentDetailsViewObj;
	
	Map<String, Object> referenceDataMap = new HashMap<String, Object>();
	
	Map<String, Object> referenceDataMapCategory = new HashMap<String, Object>();
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	@Inject
	private Instance<RevisedCarousel> carouselInstance;

	private RevisedCarousel revisedCarousel;
	
	private VerticalLayout  mainLayout;
	
	@Inject
	private ViewDetails viewDetails;
	
	@Inject
	private Instance<OMPEarlierRodDetailsViewImpl> earlierRodDetailsViemImplInstance;
	
	private OMPEarlierRodDetailsViewImpl earlierRodDetailsViewObj;
	
	@Inject
	private Instance<OMPProcessNegotiationPageDetailTable> ompProcessNegotiationTableInstance;
	
	private OMPProcessNegotiationPageDetailTable ompProcessNegotiationTableObj;
	
	private Button btnViewDocument;
	
	private ComboBox cmbEventCode;
	
	private DateField lossDateField;
	
	private TextField txtAilmentLoss;
	
	private TextField txtDelayHrs;
	
	private ComboBox cmbDocType;

	private TextField txtHospName;

	private TextField txtHospCity;

	private ComboBox cmbHospCountry;

	private TextField txtPlaceOfVisit;

	private TextField txtreasonForReconsider;

	private ComboBox cmbModeOfReceipt;

	private DateField documentsReceivedDate;

	private ComboBox cmbDocumentsReceivedFrom;

	private ComboBox cmbClassification;

	private ComboBox cmbSubClassification;

	private TextField txtConversionValue;

	private TextField txtCurrencyRate;

	private ComboBox cmbCurrencyType;

	private ComboBox cmbPaymentMode;

	private TextField txtPayeeNameStr;

	private ComboBox cmbPaymentTo;

	private TextField txtPayableAt;

	private Button suggestforApproval;

	private Button suggestforRejectionBtn;

	private Button sendtoNegotiatorBtn;

	private ComboBox txtNameofNegotiator;

	private com.vaadin.v7.ui.TextArea txtReasonOfNegotiation;

	private com.vaadin.v7.ui.TextArea txtReasonOfRejection;


	private TextArea txtReasonOfApproval;
	
	private TextArea txtRemark;
	private TextArea txtRemarkApprover;
	protected Button btnSave;
	protected Button btnSubmit;
	protected Button btnCancel;

	private TextField txtDoctorName;

	private TextField txtInvestigatorName;

	private TextField txtAdvocateName;

	private TextField txtAuditorName;

	private TextField txtNegotiatorName;
	
	private OptionGroup claimTypeOption;
	
	private OptionGroup hospitalOption;
	
	private BeanItemContainer<SelectValue> documentRecievedFrom;
	
	@Inject
	private Instance<IntimationDetailsCarousel> commonCarouselInstance;

	
	@Override
	public String getCaption() {
		return "Bill Hospitalization";
	}

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {

	}
	
	
	public void init(OMPClaimProcessorDTO bean, BeanItemContainer<SelectValue> classification, BeanItemContainer<SelectValue> subClassification, 
			BeanItemContainer<SelectValue> paymentTo, BeanItemContainer<SelectValue> paymentMode, BeanItemContainer<SelectValue> eventCode, 
			BeanItemContainer<SelectValue> currencyValue, BeanItemContainer<SelectValue> negotiatorName, BeanItemContainer<SelectValue> modeOfReciept, 
			BeanItemContainer<SelectValue> documentRecievedFrom, BeanItemContainer<SelectValue> documentType, BeanItemContainer<SelectValue> country) {
//		initBinder();
		this.bean = bean;
		this.documentRecievedFrom = documentRecievedFrom;
	/*	revisedCarousel = carouselInstance.get();
		revisedCarousel.init(bean.getNewIntimationDto());*/
		IntimationDetailsCarousel intimationDetailsCarousel = commonCarouselInstance
				.get();
		intimationDetailsCarousel.initOMPCarousal(this.bean.getNewIntimationDto(), "Process OMP Claim -Processor");
		FormLayout viewDetailsForm = new FormLayout();
		//Vaadin8-setImmediate() viewDetailsForm.setImmediate(true);
		viewDetailsForm.setWidth("-1px");
		viewDetailsForm.setHeight("-1px");
		viewDetailsForm.setMargin(false);
		viewDetailsForm.setSpacing(true);
		// ComboBox viewDetailsSelect = new ComboBox();
		// viewDetailsForm.addComponent(viewDetailsSelect);
		viewDetails.initView(bean.getIntimationId(), bean.getRodKey(),  ViewLevels.OMP ,null);
		viewDetailsForm.addComponent(viewDetails);
		VerticalLayout vlayout = new VerticalLayout();
		vlayout.addComponent(/*revisedCarousel*/intimationDetailsCarousel);
		vlayout.setStyleName("policygridinfo");
		Label txtRodNumber = new Label(this.bean.getRodNumber());
		HorizontalLayout wizardLayout1 = new HorizontalLayout (viewButtonsLayout(),viewDetailsForm);
		wizardLayout1.setComponentAlignment(viewDetailsForm, Alignment.TOP_RIGHT);
		wizardLayout1.setSpacing(true);
		wizardLayout1.setSizeFull();
		
		HorizontalLayout wizardLayout2 = new HorizontalLayout (eventlayout(documentType), hospitalLayout(country));
		//wizardLayout2.setComponentAlignment(viewDetailsForm, Alignment.TOP_RIGHT);
		wizardLayout2.setSpacing(true);
		wizardLayout2.setSizeFull();
		
		
		VerticalLayout wizardLayout4 = new VerticalLayout (classificationLayout(modeOfReciept, documentRecievedFrom, negotiatorName), currencyLayout(currencyValue));
		wizardLayout4.setSpacing(true);
		wizardLayout4.setSizeFull();
		
		ConfirmDialog confirmDialog = new ConfirmDialog();
		HorizontalLayout wizardLayout5 = new HorizontalLayout (getSaveButtonWithListener(confirmDialog) ,getSaveNexitButtonWithListener(confirmDialog) ,getCancelButton(confirmDialog));
		wizardLayout5.setCaption("");
		wizardLayout5.setSpacing(true);
		wizardLayout5.setSizeFull();
		wizardLayout5.setWidth("50%");
		mainLayout = new VerticalLayout(vlayout, wizardLayout1 , txtRodNumber, wizardLayout2);
		documentTypeListener(mainLayout);
		//mainLayout.addComponent(wizardLayout4);
		mainLayout.addComponent(getContent(negotiatorName,wizardLayout4));
		mainLayout.addComponent(getNegotitaion());
		mainLayout.addComponent(wizardLayout5);
		
				
		mainLayout.setComponentAlignment(wizardLayout5, Alignment.MIDDLE_CENTER);
		mainLayout.setMargin(true);
		mainLayout.setCaption("");
		mainLayout.setSpacing(true);
		mainLayout.setSizeFull();
		setCmbValues(classification,subClassification,paymentMode,paymentTo,eventCode);
		cmbEventCode.setReadOnly(Boolean.FALSE);
		cmbEventCode.setValue(bean.getEventCode());
		//cmbEventCode.setReadOnly(Boolean.TRUE);
		cmbClassification.setReadOnly(Boolean.FALSE);
		cmbClassification.setValue(bean.getClassification());
		cmbClassification.setReadOnly(Boolean.TRUE);
		cmbSubClassification.setReadOnly(Boolean.FALSE);
		cmbSubClassification.setValue(bean.getSubClassification());
		cmbSubClassification.setReadOnly(Boolean.TRUE);
		cmbDocType.setReadOnly(Boolean.FALSE);
		cmbDocType.setValue(bean.getDocType());
		cmbDocType.setReadOnly(Boolean.TRUE);
		cmbEventCode.setValidationVisible(true);
//		cmbDocumentsReceivedFrom.setValue(bean.getDocumentsReceivedFrom());
//		cmbModeOfReceipt.setValue(bean.getModeOfReceipt());
//		cmbPayeeName.setValue(bean.getPayeeNameStr());
//		cmbCurrencyType.setValue(bean.getCurrencyType());
		cmbHospCountry.setReadOnly(Boolean.FALSE);
		cmbHospCountry.setValue(bean.getHospCountry());
		cmbHospCountry.setReadOnly(Boolean.TRUE);
		if(bean.getIsMutipleRod()){
			cmbEventCode.setReadOnly(Boolean.TRUE);
		}
		addListenerForCurrency();
		setCompositionRoot(mainLayout);
	}

	private HorizontalLayout getDocumentDetail() {
		documentDetailsViewObj = documentDetailsViewTableInstance.get();
		documentDetailsViewObj.initPresenter(SHAConstants.ACKNOWLEDGE_DOC_RECEIVED);
		documentDetailsViewObj.init();
		documentDetailsViewObj.setCaption("Select Earlier Request to be Reconsidered");
		HorizontalLayout wizardLayout3 = new HorizontalLayout (documentDetailsViewObj);
		wizardLayout3.setComponentAlignment(documentDetailsViewObj, Alignment.TOP_CENTER);
		wizardLayout3.setSpacing(true);
		wizardLayout3.setSizeFull();
		return wizardLayout3;
	}
	
	private void setCmbValues(BeanItemContainer<SelectValue> classification,BeanItemContainer<SelectValue> subClassification, BeanItemContainer<SelectValue> paymentMode, 
			BeanItemContainer<SelectValue> paymentTo, BeanItemContainer<SelectValue> eventCode) {
		cmbClassification.setContainerDataSource(classification);
		cmbClassification.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbClassification.setItemCaptionPropertyId("value");
		cmbClassification.setReadOnly(true);
		
		cmbSubClassification.setContainerDataSource(subClassification);
		cmbSubClassification.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbSubClassification.setItemCaptionPropertyId("value");
		cmbSubClassification.setReadOnly(true);
//		cmbPaymentTo.setContainerDataSource(paymentTo);
//		cmbPaymentTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
//		cmbPaymentTo.setItemCaptionPropertyId("value");
//		
//		cmbPaymentMode.setContainerDataSource(paymentMode);
//		cmbPaymentMode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
//		cmbPaymentMode.setItemCaptionPropertyId("value");
		
		cmbEventCode.setContainerDataSource(eventCode);
		cmbEventCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbEventCode.setItemCaptionPropertyId("value");
	}

	public void initBinder() {
		this.binder = new BeanFieldGroup<OMPClaimProcessorDTO>(
				OMPClaimProcessorDTO.class);
		this.binder.setItemDataSource(bean);
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		//this.paymentbinder = new BeanFieldGroup<PreauthDataExtaractionDTO>(
			//	PreauthDataExtaractionDTO.class);
		//this.paymentbinder.setItemDataSource(this.bean.getPreauthDataExtractioDto());
		//this.paymentbinder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
	}

	
	private HorizontalLayout viewButtonsLayout()
	{
		Button viewEarlierRODDetails = new Button("View Earlier ROD Details");
		viewEarlierRODDetails.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				earlierRodDetailsViewObj = earlierRodDetailsViemImplInstance.get();
				ViewDocumentDetailsDTO documentDetails =  new ViewDocumentDetailsDTO();
				documentDetails.setClaimDto(bean.getClaimDto());
				earlierRodDetailsViewObj.init(bean.getClaimDto().getKey(),bean.getRodKey());
				showPopup(new VerticalLayout(earlierRodDetailsViewObj));
			}
			
		});
		
		btnViewDocument = new Button("View Document");
		btnViewDocument.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		btnViewDocument.addStyleName(ValoTheme.BUTTON_LINK);
		addListener();
		HorizontalLayout alignmentHLayout = new HorizontalLayout(btnViewDocument, viewEarlierRODDetails);
		return alignmentHLayout;
	}
	
	
	private HorizontalLayout eventlayout(BeanItemContainer<SelectValue> documentType)
	{
		initBinder();
	/*	OptionGroup  optionGroup = new OptionGroup ();
		optionGroup.setNullSelectionAllowed(true);
		optionGroup.addItems("Cashless","Reimbursement");
		optionGroup.setStyleName("horizontal");*/
		
		txtRemark = binder.buildAndBind("Remark (Processor)", "remarks",TextArea.class);
		txtRemark.setWidth("40%");
		txtRemark.setReadOnly(true);
		
		claimTypeOption = binder.buildAndBind("","claimType",OptionGroup.class);
		claimTypeOption.setNullSelectionAllowed(true);
		claimTypeOption.addItems(getReadioButtonOptions());
		claimTypeOption.setItemCaption(true, "Cashless");
		claimTypeOption.setItemCaption(false, "Reimbursement");
		claimTypeOption.setStyleName("horizontal");
		claimTypeOption.setReadOnly(true);
		
		cmbEventCode = binder.buildAndBind("Event Code" , "eventCode", ComboBox.class);
		//cmbEventCode.setReadOnly(true);
		mandatoryFields.add(cmbEventCode);
		cmbEventCode.setValue(bean.getEventCode());
		lossDateField= binder.buildAndBind("Loss Date" , "lossDate", DateField.class);
		mandatoryFields.add(lossDateField);
		lossDateField.setReadOnly(true);
		txtAilmentLoss= binder.buildAndBind("Ailment/Loss" , "ailmentLoss", TextField.class);
		txtAilmentLoss.setReadOnly(true);
		txtDelayHrs= binder.buildAndBind("Delay (in Hours)" , "delayHrs", TextField.class);
		txtDelayHrs.setReadOnly(true);
		cmbDocType = binder.buildAndBind("Document Type" , "docType", ComboBox.class);
		
		cmbDocType.setContainerDataSource(documentType);
		cmbDocType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbDocType.setItemCaptionPropertyId("value");
		cmbDocType.setReadOnly(true);
		cmbEventCode.setEnabled(false);
		FormLayout optionLayout = new FormLayout(txtRemark,claimTypeOption,cmbEventCode,lossDateField,txtAilmentLoss,txtDelayHrs, cmbDocType);
		addListenerForCashless();
		HorizontalLayout alignmentHLayout = new HorizontalLayout(optionLayout);
		alignmentHLayout.setSpacing(true);
		alignmentHLayout.setSizeFull();
		return alignmentHLayout;
		
	}
	
	private HorizontalLayout hospitalLayout(BeanItemContainer<SelectValue> country)
	{
		
	/*	OptionGroup  optionGroup = new OptionGroup ();
		optionGroup.setNullSelectionAllowed(true);
		optionGroup.addItems("Hospital","Non Hospital");
		optionGroup.setStyleName("horizontal");*/
		
		txtRemarkApprover = binder.buildAndBind("Remark (Approver)", "remarksApprover",TextArea.class);
		txtRemarkApprover.setReadOnly(true);
		txtRemarkApprover.setWidth("40%");
		
		hospitalOption = binder.buildAndBind("","hospTypeBooleanval",OptionGroup.class);
		hospitalOption.setStyleName("horizontal");
		//Vaadin8-setImmediate() hospitalOption.setImmediate(false);
		hospitalOption.setNullSelectionAllowed(true);
		hospitalOption.addItems(getReadioButtonOptions());
		hospitalOption.setItemCaption(true, "Hospitalisation");
		hospitalOption.setItemCaption(false, "Non Hospitalisation");
		hospitalOption.setReadOnly(true);
		
		txtHospName= binder.buildAndBind("Hospital Name" , "hospName", TextField.class);
		txtHospName.setReadOnly(true);
		txtHospName.setValidationVisible(false);
		txtHospCity= binder.buildAndBind("Hospital City" , "hospCity", TextField.class);
		txtHospCity.setReadOnly(true);
		txtHospCity.setValidationVisible(false);
		cmbHospCountry = binder.buildAndBind("Hospital Country" , "hospCountry", ComboBox.class);
//		cmbHospCountry.setReadOnly(true);
		cmbHospCountry.setValidationVisible(false);
		txtPlaceOfVisit = binder.buildAndBind("Place of Visit" , "placeOfVisit", TextField.class);
		txtPlaceOfVisit.setReadOnly(true);
		txtreasonForReconsider = binder.buildAndBind("Reason For Reconsider" , "reasonForReconsider", TextField.class);		
		txtreasonForReconsider.setReadOnly(true);
		
		cmbHospCountry.setContainerDataSource(country);
		cmbHospCountry.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbHospCountry.setItemCaptionPropertyId("value");
		
		FormLayout optionLayout = new FormLayout(txtRemarkApprover,hospitalOption,txtHospName,txtHospCity,cmbHospCountry,txtPlaceOfVisit,txtreasonForReconsider);
		
		HorizontalLayout alignmentHLayout = new HorizontalLayout(optionLayout);
		return alignmentHLayout;
		
	}
	
	private VerticalLayout documentDetailLayout(BeanItemContainer<SelectValue> modeOfReciept, BeanItemContainer<SelectValue> documentRecievedFrom){
		
		cmbDocumentsReceivedFrom = binder.buildAndBind("Documents Recieved From", "documentsReceivedFrom",ComboBox.class);
		cmbDocumentsReceivedFrom.setValue(bean.getDocumentsReceivedFrom());
		documentsReceivedDate = binder.buildAndBind("Documents Recieved Date","documentsReceivedDate", DateField.class);
		documentsReceivedDate.setReadOnly(true);
		cmbModeOfReceipt = binder.buildAndBind("Mode Of Receipt","modeOfReceipt", ComboBox.class);
		
		cmbDocumentsReceivedFrom.setContainerDataSource(documentRecievedFrom);
		cmbDocumentsReceivedFrom.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbDocumentsReceivedFrom.setItemCaptionPropertyId("value");
		cmbDocumentsReceivedFrom.setValue(bean.getDocumentsReceivedFrom());
		cmbDocumentsReceivedFrom.setReadOnly(true);
		cmbModeOfReceipt.setContainerDataSource(modeOfReciept);
		cmbModeOfReceipt.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbModeOfReceipt.setItemCaptionPropertyId("value");
		cmbModeOfReceipt.setValue(bean.getModeOfReceipt());
		cmbModeOfReceipt.setReadOnly(true);
		VerticalLayout alignmentHLayout = new VerticalLayout(cmbDocumentsReceivedFrom, documentsReceivedDate, cmbModeOfReceipt);
		return alignmentHLayout;
	}
	
	private VerticalLayout classificationLayout(BeanItemContainer<SelectValue> modeOfReciept, BeanItemContainer<SelectValue> documentRecievedFrom ,BeanItemContainer<SelectValue> negotiatorName){
		
		cmbClassification = binder.buildAndBind("Classification", "classification",ComboBox.class);
		
		cmbSubClassification = binder.buildAndBind("Sub Classification", "subClassification",ComboBox.class);
		
		VerticalLayout alignmentHLayout = new VerticalLayout(cmbClassification,cmbSubClassification);
		
		alignmentHLayout = subClassificationListener(alignmentHLayout , modeOfReciept, documentRecievedFrom, negotiatorName);
		alignmentHLayout.setSpacing(true);
		addListenerForClassification();
		return alignmentHLayout;
		
	}
	
	private List<Field<?>> getListOfSubclassifcationFields()
	{
		List<Field<?>>  fieldList = new ArrayList<Field<?>>();
		fieldList.add(txtDoctorName);
		fieldList.add(txtInvestigatorName);
		fieldList.add(txtAdvocateName);
		fieldList.add(txtAuditorName);
		fieldList.add(txtNegotiatorName);
		fieldList.add(cmbDocumentsReceivedFrom);
		fieldList.add(documentsReceivedDate);
		fieldList.add(cmbModeOfReceipt);
		return fieldList;
	}
	
	private VerticalLayout currencyLayout(BeanItemContainer<SelectValue> currencyValue){
		
		cmbCurrencyType = binder.buildAndBind("Currency Type", "currencyType",ComboBox.class);
		txtCurrencyRate = binder.buildAndBind("Currency Rate(in to USD)", "currencyRate",TextField.class);
		txtCurrencyRate.setReadOnly(true);
		txtConversionValue = binder.buildAndBind("Conversion Value(USD to INR)", "conversionValue",TextField.class);
		txtConversionValue.setReadOnly(true);
		
		cmbCurrencyType.setContainerDataSource(currencyValue);
		cmbCurrencyType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCurrencyType.setItemCaptionPropertyId("value");
		cmbCurrencyType.setValue(bean.getCurrencyType());
		cmbCurrencyType.setReadOnly(true);
		VerticalLayout alignmentHLayout = new VerticalLayout(cmbCurrencyType,txtCurrencyRate,txtConversionValue);
		addListenerForCurrency();
		return alignmentHLayout;
		
	}
	
	
	private void addListenerForCurrency()
	{
		if(txtCurrencyRate != null) {
			txtCurrencyRate.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 7455756225751111662L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					String totalValue = (String) event.getProperty().getValue();
					if(totalValue != null && ompClaimCalcViewTableObj!=null) {
						ompClaimCalcViewTableObj.dummyField.setValue(totalValue);
					}
					
				}
			});
		}
	}
	
	public Component getContent(BeanItemContainer<SelectValue> negotiatorName, VerticalLayout wizardLayout4) {
		fireViewEvent(OMPProcessNegotiationWizardPresenter.OMP_NEGOTATION_PARTICULARS, referenceDataMap);
		ompClaimCalcViewTableObj =  ompClaimCalcViewTableInstance.get();
		ompClaimCalcViewTableObj.init(bean);
		ompClaimCalcViewTableObj.setReferenceData(referenceDataMap);
		if(bean.getClaimCalculationViewTable() != null){
			ompClaimCalcViewTableObj.setTableList(bean.getClaimCalculationViewTable());
			}
		 ompClaimCalcViewTableObj.setEnabled(false);
		 wholeLayout = new VerticalLayout(wizardLayout4, ompClaimCalcViewTableObj, getCalculationLayout()/*,getPaymentDetailsLayout() buildButtons(bean,negotiatorName)*/);
		 wholeLayout.setComponentAlignment(ompClaimCalcViewTableObj, Alignment.TOP_CENTER);
		 wholeLayout.setSpacing(true);
		 wholeLayout.setSizeFull();
		 return wholeLayout;
	}
	
	public Component getNegotitaion(){
		

		ompProcessNegotiationTableObj =  ompProcessNegotiationTableInstance.get();
		//ompProcessNegotiationTableObj.init(bean);
		ompProcessNegotiationTableObj.setCaption("Negotiation Details");
		if(bean.getNegotiationDetailsDTOs() != null){
			ompProcessNegotiationTableObj.setTableList(bean.getNegotiationDetailsDTOs());
		}
		 wholeLayout = new VerticalLayout(ompProcessNegotiationTableObj);
		 wholeLayout.setComponentAlignment(ompProcessNegotiationTableObj, Alignment.TOP_CENTER);
		 wholeLayout.setSpacing(true);
		 wholeLayout.setSizeFull();
		 return wholeLayout;
	}
	
	private Button getCancelButton(final ConfirmDialog dialog) {
		btnCancel = new Button("Exit");
		btnCancel.setStyleName(ValoTheme.BUTTON_DANGER);
		btnCancel.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				releaseWorkFlowTask();
//				fireViewEvent(MenuItemBean.OMP_PROCESS_OMP_NEGOTIATION, null);
			}
		});
		return btnCancel;
	}
	
	private Button getSaveNexitButtonWithListener(final ConfirmDialog dialog) {
		btnSubmit = new Button("Save & Exit");
		btnSubmit.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSubmit.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				String eMsg = "";
				if(!validatePage()) {
					dialog.close();
					fireViewEvent(OMPProcessNegotiationWizardPresenter.OMP_PROCESS_NEGOTIATION_SUBMIT, bean);
					
					
					//wizard.getNextButton().setEnabled(false);
					//wizard.getFinishButton().setEnabled(true);
					//bean.setIsFirstPageSubmit(true);
				} else {/*
					List<String> errors = getErrors();
					for (String error : errors) {
						eMsg += error+"</br>";
					}
					showErrorPopup(eMsg);
				*/}
			}

			

			
		});
		return btnSubmit;
	}

	private Button getSaveButtonWithListener(final ConfirmDialog dialog) {
		btnSave = new Button("Save");
		btnSave.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSave.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				String eMsg = "";
				if(!validatePage()) {
					dialog.close();
					releaseWorkFlowTask();
					fireViewEvent(OMPProcessNegotiationWizardPresenter.OMP_PROCESS_NEGOTIATION_SAVE, bean);
					//wizard.getNextButton().setEnabled(false);
					//wizard.getFinishButton().setEnabled(true);
					//bean.setIsFirstPageSubmit(true);
				} else {/*
					List<String> errors = getErrors();
					for (String error : errors) {
						eMsg += error+"</br>";
					}
					showErrorPopup(eMsg);
				*/}
			}

			
		});
		return btnSave;
	}

	private boolean isValid() {
		// TODO Auto-generated method stub
		return false;
	}
	
	private VerticalLayout getCalculationLayout(){
		
		
		calculationInstanceObj = calculationInstance.get();
		calculationInstanceObj.initView(this.bean);
		calculationInstanceObj.setEnabled(Boolean.FALSE);
		VerticalLayout verticalLayout = new VerticalLayout(calculationInstanceObj);
		
		return verticalLayout;
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	public void setupReferences(Map<String, Object> referenceData) {
		
	}
	
	
	
	public boolean validatePage() {
		Boolean hasError = false;
		showOrHideValidation(true);
		String eMsg = "";	
		
		
//		if(!this.binder.isValid()) {
//			for (Field<?> field : this.binder.getFields()) {
//				ErrorMessage errMsg = ((AbstractField<?>) field)
//						.getErrorMessage();
//				if (errMsg != null) {
//					eMsg += errMsg.getFormattedHtmlMessage();
//				}
//				hasError = true;
//			}
//		}
		
		List<String> errors = ompProcessNegotiationTableObj.validateCalculation();
		if(null != errors && !errors.isEmpty()){
			for (String error : errors) {
				eMsg += error + "</br>";
				
			}
			if (eMsg != null) {
				hasError = true;
			}
		}
		
		if(!hasError) {
			
		}
		
		
	
		if (hasError) {
			setRequired(true);
			Label label = new Label(eMsg, ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);

			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Errors");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);

			hasError = true;
			return hasError;
		} else {
			try {
				this.binder.commit();
				bindValues();
			} catch (CommitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			return false;
		}
	}
	@SuppressWarnings("unused")
	private void setRequired(Boolean isRequired) {
	
		if (!mandatoryFields.isEmpty()) {
			for (int i = 0; i < mandatoryFields.size(); i++) {
				AbstractField<?> field = (AbstractField<?>) mandatoryFields
						.get(i);
				field.setRequired(isRequired);
			}
		}
	}

	private void bindValues() {
		if(this.calculationInstanceObj != null) {
			this.calculationInstanceObj.isValid();
		}
		if(this.ompProcessNegotiationTableObj != null) {
			bean.setNegotiationDetailsDTOs(this.ompProcessNegotiationTableObj.getValues());
		}
	}


	
	private VerticalLayout getPaymentDetailsLayout()
	{
		cmbPaymentTo = (ComboBox) binder.buildAndBind("Payment To" , "paymentTo" , ComboBox.class);
		txtPayeeNameStr = (TextField) binder.buildAndBind("Payee Name" , "payeeNameStr" , TextField.class);
		cmbPaymentMode = (ComboBox) binder.buildAndBind("Payment Mode" , "payMode" , ComboBox.class);
		txtPayableAt =  (TextField) binder.buildAndBind("Payable At" , "payableAt" , TextField.class);
		FormLayout formLayout = new FormLayout(cmbPaymentTo, txtPayeeNameStr, cmbPaymentMode, txtPayableAt);
		formLayout.setSpacing(true);
		txtPanNo =  (TextField) binder.buildAndBind("Pan No" , "panNo" , TextField.class);
		txtEmailId =  (TextField) binder.buildAndBind("Email ID" , "emailId" , TextField.class);
		FormLayout formLayout1 = new FormLayout(txtPanNo, txtEmailId);
		formLayout1.setSpacing(true);
		HorizontalLayout horizontalLayout1 = new HorizontalLayout(formLayout,formLayout1);
		horizontalLayout1.setSpacing(true);
		VerticalLayout mainpaymentLayout = new VerticalLayout(horizontalLayout1);
		mainpaymentLayout.setCaption("Payment Details");
		mainpaymentLayout.setSpacing(true);
		mainpaymentLayout.setSizeFull();
		return mainpaymentLayout;
	}
	
	public HorizontalLayout buildButtons(OMPClaimProcessorDTO bean, BeanItemContainer<SelectValue> negotiatorName) {
		this.bean = bean;
		sendtoNegotiatorBtn = new Button("Send to Negotiator");
		suggestforRejectionBtn = new Button("Suggest for Rejection");
		suggestforApproval = new Button("Approve (Send to Payment)");
		
		
		
		HorizontalLayout buttonHLayout = new HorizontalLayout(sendtoNegotiatorBtn, suggestforRejectionBtn,suggestforApproval);
		buttonHLayout.setSpacing(true);
		
		HorizontalLayout alignmentHLayout = new HorizontalLayout(buttonHLayout);
		alignmentHLayout.setWidth("100%");
		alignmentHLayout.setComponentAlignment(buttonHLayout, Alignment.MIDDLE_CENTER);
//		addListenerButton(buttonHLayout, negotiatorName);
	
		
		
		return alignmentHLayout;
	}
	
	private VerticalLayout subClassificationListener(final VerticalLayout alignmentHLayout, final BeanItemContainer<SelectValue> modeOfReciept, final BeanItemContainer<SelectValue> documentRecievedFrom, final BeanItemContainer<SelectValue> negotiatorName){
		cmbSubClassification.addValueChangeListener(new ValueChangeListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 2697682747976915503L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				
				SelectValue selectValue = (SelectValue) event.getProperty().getValue();
				
				if(selectValue!=null){
					
					List<Field<?>> listOfSubclassifcationFields = getListOfSubclassifcationFields();
					unbindField(listOfSubclassifcationFields);
					
					if(selectValue.getValue()!=null ){
						
						if(alignmentHLayout.getComponentCount()>2){
							alignmentHLayout.removeComponent(alignmentHLayout.getComponent(alignmentHLayout.getComponentCount() - 1));
						}
						if(selectValue.getValue().equalsIgnoreCase("Doctors opinion")){
							txtDoctorName = binder.buildAndBind("Doctor Name(Opinion)", "doctorName",TextField.class);
							txtDoctorName.setReadOnly(true);
							alignmentHLayout.addComponent(txtDoctorName);
						}
						if(selectValue.getValue().equalsIgnoreCase("Investigation charges")){
							txtInvestigatorName = binder.buildAndBind("Investigator Name", "investigatorName",TextField.class);
							txtInvestigatorName.setReadOnly(true);
							alignmentHLayout.addComponent(txtInvestigatorName);
						}
						if(selectValue.getValue().equalsIgnoreCase("Advocate Fee")){
							txtAdvocateName = binder.buildAndBind("Advocate Name", "advocateName",TextField.class);
							txtAdvocateName.setReadOnly(true);
							alignmentHLayout.addComponent(txtAdvocateName);
						}
						if(selectValue.getValue().equalsIgnoreCase("Auditor Fee")){
							txtAuditorName = binder.buildAndBind("Auditor Name", "auditorName",TextField.class);
							txtAuditorName.setReadOnly(true);
							alignmentHLayout.addComponent(txtAuditorName);
						}
						if(selectValue.getValue().equalsIgnoreCase("Negotiator Fee")){
							txtNameofNegotiator = binder.buildAndBind("Negotiator Name", "negotiatorName",ComboBox.class);
							txtNameofNegotiator.setReadOnly(true);
							alignmentHLayout.addComponent(txtNameofNegotiator);
							txtNameofNegotiator.setContainerDataSource(negotiatorName);
							txtNameofNegotiator.setItemCaptionMode(ItemCaptionMode.PROPERTY);
							txtNameofNegotiator.setItemCaptionPropertyId("value");
							txtNameofNegotiator.setValue(bean.getNegotiatorName());
						}
						if(selectValue.getValue().equalsIgnoreCase("OMP Claim Related")){
							alignmentHLayout.addComponent(documentDetailLayout(modeOfReciept,documentRecievedFrom));
						}
					}
				}
			}
		});
		return alignmentHLayout;
	}
	
	
	private VerticalLayout documentTypeListener(final VerticalLayout mainLayout){
		cmbDocType.addValueChangeListener(new ValueChangeListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 2697682747976915503L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				
				SelectValue selectValue = (SelectValue) event.getProperty().getValue();
				
				if(selectValue!=null){
					
					if(selectValue.getValue()!=null ){
						int componentCount = mainLayout.getComponentCount();
						if(componentCount>7){
							mainLayout.removeComponent(mainLayout.getComponent(4));
						}
						System.out.println(componentCount);
						if(selectValue.getValue().equalsIgnoreCase("Reconsideration")){
							mainLayout.addComponent(getDocumentDetail(),4);
						}else{
							int componentCount2 = mainLayout.getComponentCount();
							if(componentCount2>7){
								mainLayout.removeComponent(mainLayout.getComponent(4));
							}
						}
					}
				}
			}
		});
		return mainLayout;
	}
	
	
//	protected void addListenerButton(final HorizontalLayout buttonHLayout, final BeanItemContainer<SelectValue> negotiatorName){
//		sendtoNegotiatorBtn.addClickListener(new ClickListener() {
//			
//			/**
//			 * 
//			 */
//			private static final long serialVersionUID = 2679764179795985945L;
//
//			@Override
//			public void buttonClick(ClickEvent event) {
//				fireViewEvent(OMPProcessClaimApproverWizardPresenter.OMP_NEGOTIATE, buttonHLayout,negotiatorName);
//			}
//		});
//		
//		suggestforRejectionBtn.addClickListener(new ClickListener() {
//			
//			/**
//			 * 
//			 */
//			private static final long serialVersionUID = -1545640032342015257L;
//
//			@Override
//			public void buttonClick(ClickEvent event) {
//				fireViewEvent(OMPProcessClaimApproverWizardPresenter.OMP_REJECTION, buttonHLayout);
//			}
//		});
//		suggestforApproval.addClickListener(new ClickListener() {
//			
//			/**
//			 * 
//			 */
//			private static final long serialVersionUID = 1274221814969702338L;
//
//			@Override
//			public void buttonClick(ClickEvent event) {
//				fireViewEvent(OMPProcessClaimApproverWizardPresenter.OMP_APPROVAL, buttonHLayout);
//			}
//		});
//	}	
		
	
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
			//field.setValidationVisible(false);
		}
	}
	
	@SuppressWarnings("unused")
	private void setRequiredAndValidation(Component component) {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		AbstractField<Field> field = (AbstractField<Field>) component;
		field.setRequired(true);
		field.setValidationVisible(false);
	}
	
	
	private List<Field<?>> getListOfButtonFields()
	{
	List<Field<?>> buttonField = new ArrayList<Field<?>>();
	buttonField.add(txtNameofNegotiator);
	buttonField.add(txtReasonOfNegotiation);
	buttonField.add(txtReasonOfApproval);
	buttonField.add(txtReasonOfRejection);
	return buttonField;
	}
	
	private void unbindField(Field<?> field) {
		if (field != null ) {
			Object propertyId = this.binder.getPropertyId(field);
			if (field!= null  && propertyId != null) {
				field.setValue(null);
				this.binder.unbind(field);
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
	
	
	protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		
		return coordinatorValues;
	}

	
	private void showPopup(Layout layout) {
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("");
		popup.setWidth("75%");
		popup.setHeight("85%");
		popup.setContent(layout);
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
	
	public void addListener(){
    	
    	btnViewDocument.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				
				if(bean.getIntimationId() != null){
					viewDetails.viewUploadedDocumentDetails(bean.getIntimationId());
				}
				
			}
		});
	}

	
	
	public void generateNegotiate(HorizontalLayout buttonLayout, BeanItemContainer<SelectValue> negotiatorName) {
		unbindField(getListOfButtonFields());
		txtNameofNegotiator = binder.buildAndBind("Name of Negotiator" , "negotiatorName", ComboBox.class);
		txtNameofNegotiator.setValue("AGADA");
		txtReasonOfNegotiation = binder.buildAndBind("Reason for Negotiation*" , "reasonForNegotiation", TextArea.class);
		
		txtNameofNegotiator.setContainerDataSource(negotiatorName);
		txtNameofNegotiator.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		txtNameofNegotiator.setItemCaptionPropertyId("value");
		bean.setOutCome(SHAConstants.OUTCOME_FOR_OMP_APPROVER_NEGO);
		VerticalLayout layout = new VerticalLayout(txtNameofNegotiator, txtReasonOfNegotiation);
		if(wholeLayout.getComponentCount()>5){
			wholeLayout.removeComponent(wholeLayout.getComponent(wholeLayout.getComponentCount() - 1));
			wholeLayout.addComponent(layout);
		}else{
			
			wholeLayout.addComponent(layout);
		}
	}

	public void generateRejection(HorizontalLayout buttonLayout) {
		unbindField(getListOfButtonFields());
		txtReasonOfRejection = binder.buildAndBind("Reason for Rejection" , "reasonForRejection", TextArea.class);
		VerticalLayout layout = new VerticalLayout(txtReasonOfRejection);
		bean.setOutCome(SHAConstants.OUTCOME_FOR_OMP_PROCESSOR);
		if(wholeLayout.getComponentCount()>5){
			wholeLayout.removeComponent(wholeLayout.getComponent(wholeLayout.getComponentCount() - 1));
			wholeLayout.addComponent(layout);
		}else{
			
			wholeLayout.addComponent(layout);
		}
		
	}

	public void generateApproval(HorizontalLayout buttonLayout) {
		unbindField(getListOfButtonFields());
		txtReasonOfApproval = binder.buildAndBind("Reason for sugesst  approval" , "reasonForApproval", TextArea.class);
		VerticalLayout layout = new VerticalLayout(txtReasonOfApproval);
		bean.setOutCome(SHAConstants.OUTCOME_FOR_OMP_APPROVER);
		if(wholeLayout.getComponentCount()>5){
			wholeLayout.removeComponent(wholeLayout.getComponent(wholeLayout.getComponentCount() - 1));
			wholeLayout.addComponent(layout);
		}else{
			
			wholeLayout.addComponent(layout);
		}
		
	}

	public void setReferenceDate(Map<String, Object> referenceDataMap) {
		this.referenceDataMap = referenceDataMap;
		this.referenceDataMapCategory = referenceDataMap;
		
	}
	
	private void addListenerForCashless()
	{
		if(claimTypeOption != null) {
			claimTypeOption.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 7455756225751111662L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					OptionGroup totalValue = (OptionGroup) event.getProperty();
					if(totalValue!=null && totalValue.getValue()!=null){
						Boolean value = (Boolean) totalValue.getValue();
						if(value!=null){
							manipulateDocumentRecievedFrom(value);
						}
					}
					
				}

				private void manipulateDocumentRecievedFrom(Boolean value) {
					if(!value){
						if(cmbDocumentsReceivedFrom!=null){
							List<SelectValue> itemIds = documentRecievedFrom.getItemIds();
							List<SelectValue> itemIdsList = new ArrayList<SelectValue>();
							SelectValue selectValue1 = null;
							for (SelectValue selectValue : itemIds) {
								if(!selectValue.getId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)){
									itemIdsList.add(selectValue);
								}
							}
						
							BeanItemContainer<SelectValue> documentRecievedFrom = new BeanItemContainer<SelectValue>(SelectValue.class);
							documentRecievedFrom.addAll(itemIdsList);
							System.out.println(itemIds);
							cmbDocumentsReceivedFrom.setContainerDataSource(documentRecievedFrom);
							cmbDocumentsReceivedFrom.setItemCaptionMode(ItemCaptionMode.PROPERTY);
							cmbDocumentsReceivedFrom.setItemCaptionPropertyId("value");
						}
						
					}else{
						if(cmbDocumentsReceivedFrom!=null){
						cmbDocumentsReceivedFrom.setContainerDataSource(documentRecievedFrom);
						cmbDocumentsReceivedFrom.setItemCaptionMode(ItemCaptionMode.PROPERTY);
						cmbDocumentsReceivedFrom.setItemCaptionPropertyId("value");
						}
					}
				}

				
			});
		}
	}
	
	private void addListenerForClassification()
	{
		if(cmbClassification != null) {
			cmbClassification.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 7455756225751111662L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					ComboBox classification = (ComboBox) event.getProperty();
					if(classification!=null && classification.getValue()!=null){
						SelectValue value2 = (SelectValue) classification.getValue();
						if(value2!=null){
							//Boolean enableORdisableNegobutton = enableORdisableNegobutton();
							/*if(sendtoNegotiatorBtn!=null){
								sendtoNegotiatorBtn.setEnabled(enableORdisableNegobutton);
								if(!enableORdisableNegobutton){
									if(wholeLayout.getComponentCount()>5){
										wholeLayout.removeComponent(wholeLayout.getComponent(wholeLayout.getComponentCount() - 1));
									}
								}*/
							}
							if(value2.getValue().equals("OMP Other Expenses")){
								ompClaimCalcViewTableObj.classification.setValue("Other Exp");
								referenceDataMapCategory.clear();
								fireViewEvent(OMPProcessNegotiationWizardPresenter.OMP_NEGOTATION_PARTICULARS, referenceDataMapCategory, SHAConstants.OMPCLM_OTHR);
								ompClaimCalcViewTableObj.setReferenceData(referenceDataMapCategory);
								if(ompClaimCalcViewTableObj.dropDownDummy!=null){
									ompClaimCalcViewTableObj.dropDownDummy.setValue("YEAH");
								}
							}
							if(value2.getValue().equals("OMP Claim Related")){
								ompClaimCalcViewTableObj.classification.setValue("OMP Claim Related");
								referenceDataMapCategory.clear();
								fireViewEvent(OMPProcessNegotiationWizardPresenter.OMP_NEGOTATION_PARTICULARS, referenceDataMapCategory, SHAConstants.OMPCLM_REL);
								ompClaimCalcViewTableObj.setReferenceData(referenceDataMapCategory);
								if(ompClaimCalcViewTableObj.dropDownDummy!=null){
									ompClaimCalcViewTableObj.dropDownDummy.setValue("dummy");
								}
								
							}
							if(value2.getValue().equals("Negotiator Fee")){
								ompClaimCalcViewTableObj.classification.setValue("Negotiator Fee");
								referenceDataMapCategory.clear();
								fireViewEvent(OMPProcessNegotiationWizardPresenter.OMP_NEGOTATION_PARTICULARS, referenceDataMapCategory, SHAConstants.OMPCLM_NEGO);
								ompClaimCalcViewTableObj.setReferenceData(referenceDataMapCategory);
								if(ompClaimCalcViewTableObj.dropDownDummy!=null){
									ompClaimCalcViewTableObj.dropDownDummy.setValue("TEST");
								}
							
							}
							
							}
					
				}
			});
		}
	}
	
	private void releaseWorkFlowTask(){
		VaadinSession session = getSession();
		Long wrkFlowKey=(Long)session.getAttribute(SHAConstants.WK_KEY);
		DBCalculationService dbService = new DBCalculationService();
 		if(wrkFlowKey != null) {
 			dbService.callOMPUnlockProcedure(wrkFlowKey);
 			getSession().setAttribute(SHAConstants.WK_KEY, null);
 		}
	}
	
	
}

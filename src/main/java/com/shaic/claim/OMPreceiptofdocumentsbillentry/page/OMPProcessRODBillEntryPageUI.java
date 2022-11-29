package com.shaic.claim.OMPreceiptofdocumentsbillentry.page;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimCalculationViewTable;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimCalculationViewTableDTO;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimProcessorDTO;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPDocumentDetailListenerTable;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.tables.DocumentCheckListValidationListenerTable;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.domain.ReferenceTable;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.server.ErrorMessage;
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
import com.vaadin.ui.Notification;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class OMPProcessRODBillEntryPageUI extends ViewComponent implements WizardStep<OMPClaimProcessorDTO>{

//	private static final long serialVersionUID = -6992464970175605792L;
	

	@Inject
	private OMPClaimProcessorDTO bean;
	
	Map<String, Object> referenceDataMap = new HashMap<String, Object>();
	
	Map<String, Object> referenceDataMapCategory = new HashMap<String, Object>();
	
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
	
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	@Inject
	private ViewDetails viewDetails;
	
	private VerticalLayout  mainLayout;
	
	@Inject
	private Instance<OMPEarlierRodDetailsViewImpl> earlierRodDetailsViemImplInstance;
	
	@Inject
	private Instance<DocumentCheckListValidationListenerTable> documentCheckListValidationObj;
	
	private DocumentCheckListValidationListenerTable documentCheckListValidation;
	
	private OMPEarlierRodDetailsViewImpl earlierRodDetailsViewObj;
	
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
	
	//private TextArea txtRemark;
	
	protected Button btnSave;
	protected Button btnSubmit;
	protected Button btnCancel;

	private TextField txtDoctorName;

	private TextField txtInvestigatorName;

	private TextField txtAdvocateName;

	private TextField txtAuditorName;

	private ComboBox txtNegotiatorName;
	
	private OptionGroup claimTypeOption;
	
	private OptionGroup hospitalOption;

	private BeanItemContainer<SelectValue> documentType;

	private BeanItemContainer<SelectValue> country;

	private BeanItemContainer<SelectValue> modeOfReciept;

	private BeanItemContainer<SelectValue> documentRecievedFrom;

	private BeanItemContainer<SelectValue> currencyValue;

	private BeanItemContainer<SelectValue> negotiatorName;

	private BeanItemContainer<SelectValue> classification;

	private BeanItemContainer<SelectValue> subClassification;

	private BeanItemContainer<SelectValue> paymentMode;

	private BeanItemContainer<SelectValue> paymentTo;

	private BeanItemContainer<SelectValue> eventCode;

	
	@Override
	public String getCaption() {
		return "Documents & Bill Entry";
	}

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		
	}
	
	
	public void init(OMPClaimProcessorDTO bean, BeanItemContainer<SelectValue> classification, BeanItemContainer<SelectValue> subClassification, 
			BeanItemContainer<SelectValue> paymentTo, BeanItemContainer<SelectValue> paymentMode, BeanItemContainer<SelectValue> eventCode, 
			BeanItemContainer<SelectValue> currencyValue, BeanItemContainer<SelectValue> negotiatorName, BeanItemContainer<SelectValue> modeOfReciept, 
			BeanItemContainer<SelectValue> documentRecievedFrom, BeanItemContainer<SelectValue> documentType, BeanItemContainer<SelectValue> country) {
		this.bean = bean;
		this.classification = classification;
		this.subClassification = subClassification;
		this.paymentTo = paymentTo;
		this.paymentMode =paymentMode;
		this.eventCode=eventCode;
		this.currencyValue = currencyValue;
		this.negotiatorName = negotiatorName;
		this.modeOfReciept = modeOfReciept;
		this.documentRecievedFrom= documentRecievedFrom;
		this.documentType = documentType;
		this.country = country;
		initBinder();
		
		/*FormLayout viewDetailsForm = new FormLayout();
		//Vaadin8-setImmediate() viewDetailsForm.setImmediate(true);
		viewDetailsForm.setWidth("-1px");
		viewDetailsForm.setHeight("-1px");
		viewDetailsForm.setMargin(false);
		viewDetailsForm.setSpacing(true);*/
		// ComboBox viewDetailsSelect = new ComboBox();
		// viewDetailsForm.addComponent(viewDetailsSelect);
		//viewDetails.initView(bean.getIntimationId(), ViewLevels.INTIMATION);
		//viewDetailsForm.addComponent(viewDetails);
		//VerticalLayout vlayout = new VerticalLayout();
		//vlayout.addComponent(revisedCarousel);
		//vlayout.setStyleName("policygridinfo");
		
		/*HorizontalLayout wizardLayout1 = new HorizontalLayout (viewButtonsLayout());
		wizardLayout1.setSpacing(true);
		wizardLayout1.setSizeFull();
		
		HorizontalLayout wizardLayout2 = new HorizontalLayout (eventlayout(documentType), hospitalLayout(country));
		//wizardLayout2.setComponentAlignment(viewDetailsForm, Alignment.TOP_RIGHT);
		wizardLayout2.setSpacing(true);
		wizardLayout2.setSizeFull();
		
		
		VerticalLayout wizardLayout4 = new VerticalLayout (classificationLayout(modeOfReciept, documentRecievedFrom), currencyLayout(currencyValue));
		wizardLayout4.setSpacing(true);
		wizardLayout4.setSizeFull();
		
		ConfirmDialog confirmDialog = new ConfirmDialog();
		//HorizontalLayout wizardLayout5 = new HorizontalLayout (getSaveButtonWithListener(confirmDialog) ,getSaveNexitButtonWithListener(confirmDialog) ,getCancelButton(confirmDialog));
		//wizardLayout5.setCaption("");
		//wizardLayout5.setSpacing(true);
		//wizardLayout5.setSizeFull();
		//wizardLayout5.setWidth("50%");
		mainLayout = new VerticalLayout(wizardLayout1 , wizardLayout2);
		documentTypeListener(mainLayout);
		mainLayout.addComponent(wizardLayout4);
		mainLayout.addComponent(getContent(negotiatorName));
		//mainLayout.addComponent(wizardLayout5);
		
				
		//mainLayout.setComponentAlignment(wizardLayout5, Alignment.MIDDLE_CENTER);
		mainLayout.setMargin(true);
		mainLayout.setCaption("");
		mainLayout.setSpacing(true);
		mainLayout.setSizeFull();
		setCmbValues(classification,subClassification,paymentMode,paymentTo,eventCode);
		setCompositionRoot(mainLayout);*/
		showOrHideValidation(true);
	}

	private HorizontalLayout getDocumentDetail() {
		documentDetailsViewObj = documentDetailsViewTableInstance.get();
		documentDetailsViewObj.initPresenter(SHAConstants.ACKNOWLEDGE_DOC_RECEIVED);
		documentDetailsViewObj.init();
		documentDetailsViewObj.setCaption("Select Earlier Request to be Reconsidered");
		documentDetailsViewObj.setTableList(this.bean.getReconsiderRodRequestList());
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
		cmbClassification.setValue(bean.getClassification());		
		
		cmbSubClassification.setContainerDataSource(subClassification);
		cmbSubClassification.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbSubClassification.setItemCaptionPropertyId("value");
		cmbSubClassification.setValue(bean.getSubClassification());
		
		cmbPaymentTo.setContainerDataSource(paymentTo);
		cmbPaymentTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPaymentTo.setItemCaptionPropertyId("value");
		cmbPaymentTo.setValue(bean.getPaymentTo());
		
		cmbPaymentMode.setContainerDataSource(paymentMode);
		cmbPaymentMode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPaymentMode.setItemCaptionPropertyId("value");
		cmbPaymentMode.setValue(bean.getPayMode());
		
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
				WeakHashMap<String, Object> Masreference = new WeakHashMap<String, Object>();
				Masreference.put("category", referenceDataMap.get("category")); 
				
				earlierRodDetailsViewObj.setReferenceData(Masreference);
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
		
	/*	OptionGroup  optionGroup = new OptionGroup ();
		optionGroup.setNullSelectionAllowed(true);
		optionGroup.addItems("Cashless","Reimbursement");
		optionGroup.setStyleName("horizontal");*/
		claimTypeOption = binder.buildAndBind("","claimType",OptionGroup.class);
//		claimTypeOption = new OptionGroup("");
		claimTypeOption.setNullSelectionAllowed(true);
		claimTypeOption.addItems(getReadioButtonOptions());
		claimTypeOption.setItemCaption(true, "Cashless");
		claimTypeOption.setItemCaption(false, "Reimbursement");
		claimTypeOption.setStyleName("horizontal");
		
		cmbEventCode = binder.buildAndBind("Event Code" , "eventCode", ComboBox.class);
		cmbEventCode.setValidationVisible(false);
//		cmbEventCode.setRequired(true);
		cmbEventCode.setValue(bean.getEventCode());
		lossDateField= binder.buildAndBind("Loss Date" , "lossDate", DateField.class);
		lossDateField.setValidationVisible(false);
		txtAilmentLoss= binder.buildAndBind("Ailment/Loss" , "ailmentLoss", TextField.class);
		txtAilmentLoss.setValidationVisible(false);
		txtDelayHrs= binder.buildAndBind("Delay (in Hours)" , "delayHrs", TextField.class);
		cmbDocType = binder.buildAndBind("Document Type" , "docType", ComboBox.class);
		cmbDocType.setValidationVisible(false);
		cmbDocType.setContainerDataSource(documentType);
		cmbDocType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbDocType.setItemCaptionPropertyId("value");
		cmbDocType.setValue(bean.getDocType());
		
		FormLayout optionLayout = new FormLayout(claimTypeOption,cmbEventCode,lossDateField,txtAilmentLoss,txtDelayHrs, cmbDocType);
		mandatoryFields.add(cmbEventCode);
		mandatoryFields.add(lossDateField);
		mandatoryFields.add(txtAilmentLoss);
		mandatoryFields.add(cmbDocType);
		addListenerForCashless();
		
		HorizontalLayout alignmentHLayout = new HorizontalLayout(optionLayout);
		alignmentHLayout.setSpacing(true);
		alignmentHLayout.setSizeFull();
		return alignmentHLayout;		
	}
	
	private HorizontalLayout hospitalLayout(BeanItemContainer<SelectValue> country)
	{
		
		/*OptionGroup  optionGroup = new OptionGroup ();
		optionGroup.setNullSelectionAllowed(true);
		optionGroup.addItems("Hospital","Non Hospital");
		optionGroup.setStyleName("horizontal");*/
	
		hospitalOption = binder.buildAndBind("","hospTypeBooleanval",OptionGroup.class);
		hospitalOption.setStyleName("horizontal");
		//Vaadin8-setImmediate() hospitalOption.setImmediate(false);
		hospitalOption.setNullSelectionAllowed(true);
		hospitalOption.addItems(getReadioButtonOptions());
		hospitalOption.setItemCaption(true, "Hospitalisation");
		hospitalOption.setItemCaption(false, "Non Hospitalisation");
		
		txtHospName= binder.buildAndBind("Hospital Name" , "hospName", TextField.class);
		txtHospName.setValidationVisible(false);
		txtHospCity= binder.buildAndBind("Hospital City" , "hospCity", TextField.class);
		txtHospCity.setValidationVisible(false);
		cmbHospCountry = binder.buildAndBind("Hospital Country" , "hospCountry", ComboBox.class);
		cmbHospCountry.setValidationVisible(false);
		txtPlaceOfVisit = binder.buildAndBind("Place of Visit" , "placeOfVisit", TextField.class);
		txtPlaceOfVisit.setValidationVisible(false);
		
		txtreasonForReconsider = binder.buildAndBind("Reason For Reconsider" , "reasonForReconsider", TextField.class);		
		
		cmbHospCountry.setContainerDataSource(country);
		cmbHospCountry.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbHospCountry.setItemCaptionPropertyId("value");
		cmbHospCountry.setValue(bean.getHospCountry());
				
		FormLayout optionLayout = new FormLayout(hospitalOption,txtHospName,txtHospCity,cmbHospCountry,txtPlaceOfVisit,txtreasonForReconsider);
		
//		mandatoryFields.add(txtHospName);
//		mandatoryFields.add(txtHospCity);
//		mandatoryFields.add(cmbHospCountry);
		if(bean.getClaimType() && bean.getHospTypeBooleanval()){
			mandatoryFields.add(txtHospName);
			mandatoryFields.add(txtHospCity);
			mandatoryFields.add(cmbHospCountry);
		}
		mandatoryFields.add(txtPlaceOfVisit);
		
		HorizontalLayout alignmentHLayout = new HorizontalLayout(optionLayout);
		return alignmentHLayout;
		
	}
	
	private VerticalLayout documentDetailLayout(BeanItemContainer<SelectValue> modeOfReciept, BeanItemContainer<SelectValue> documentRecievedFrom){
		
		cmbDocumentsReceivedFrom = binder.buildAndBind("Documents Recieved From", "documentsReceivedFrom",ComboBox.class);
//		cmbDocumentsReceivedFrom.setValidationVisible(false);
		documentsReceivedDate = binder.buildAndBind("Documents Recieved Date","documentsReceivedDate", DateField.class);
//		documentsReceivedDate.setValidationVisible(false);
		cmbModeOfReceipt = binder.buildAndBind("Mode Of Receipt","modeOfReceipt", ComboBox.class);
//		cmbModeOfReceipt.setValidationVisible(false);
		if(claimTypeOption!=null && claimTypeOption.getValue()!=null){
			Boolean value = (Boolean) claimTypeOption.getValue();
			manipulateDocumentRecievedFrom(value);
		}
		/*cmbDocumentsReceivedFrom.setContainerDataSource(documentRecievedFrom);
		cmbDocumentsReceivedFrom.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbDocumentsReceivedFrom.setItemCaptionPropertyId("value");
		cmbDocumentsReceivedFrom.setValue(bean.getDocumentsReceivedFrom());*/
				
		cmbModeOfReceipt.setContainerDataSource(modeOfReciept);
		cmbModeOfReceipt.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbModeOfReceipt.setItemCaptionPropertyId("value");
		cmbModeOfReceipt.setValue(bean.getModeOfReceipt());
		
		
		VerticalLayout alignmentHLayout = new VerticalLayout(cmbDocumentsReceivedFrom, documentsReceivedDate, cmbModeOfReceipt);
		mandatoryFields.add(cmbDocumentsReceivedFrom);
		mandatoryFields.add(documentsReceivedDate);
		mandatoryFields.add(cmbModeOfReceipt);
		cmbDocumentsReceivedFrom.setRequired(true);
		documentsReceivedDate.setRequired(true);
		cmbModeOfReceipt.setRequired(true);
		addListenerForDocumentRecievedFrom();
		return alignmentHLayout;
	}
	
	private VerticalLayout classificationLayout(BeanItemContainer<SelectValue> modeOfReciept, BeanItemContainer<SelectValue> documentRecievedFrom){
		
		cmbClassification = binder.buildAndBind("Classification", "classification",ComboBox.class);
		cmbClassification.setValidationVisible(false);
		cmbSubClassification = binder.buildAndBind("Sub Classification", "subClassification",ComboBox.class);
		cmbSubClassification.setValidationVisible(false);
		VerticalLayout alignmentHLayout = new VerticalLayout(cmbClassification,cmbSubClassification);
		mandatoryFields.add(cmbClassification);
		mandatoryFields.add(cmbSubClassification);
		alignmentHLayout = subClassificationListener(alignmentHLayout , modeOfReciept, documentRecievedFrom,negotiatorName);
		alignmentHLayout.setSpacing(true);
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
		mandatoryFields.remove(txtDoctorName);
		mandatoryFields.remove(txtInvestigatorName);
		mandatoryFields.remove(txtAdvocateName);
		mandatoryFields.remove(txtAuditorName);
		mandatoryFields.remove(txtNegotiatorName);
		mandatoryFields.remove(cmbDocumentsReceivedFrom);
		mandatoryFields.remove(documentsReceivedDate);
		mandatoryFields.remove(cmbModeOfReceipt);
		return fieldList;
	}
	
	private VerticalLayout currencyLayout(BeanItemContainer<SelectValue> currencyValue){
		
		cmbCurrencyType = binder.buildAndBind("Currency Type", "currencyType",ComboBox.class);
		cmbCurrencyType.setValidationVisible(false);
		txtCurrencyRate = binder.buildAndBind("Currency Rate(in to USD)", "currencyRate",TextField.class);
		txtCurrencyRate.setValidationVisible(false);
		txtConversionValue = binder.buildAndBind("Conversion Value(USD to INR)", "conversionValue",TextField.class);
		txtConversionValue.setValidationVisible(false);
		txtCurrencyRate.setNullRepresentation("");
		txtConversionValue.setNullRepresentation("");
		validateNumber(txtCurrencyRate);
		validateNumber(txtConversionValue);
		cmbCurrencyType.setContainerDataSource(currencyValue);
		cmbCurrencyType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCurrencyType.setItemCaptionPropertyId("value");
		cmbCurrencyType.setValue(bean.getCurrencyType());
		
		VerticalLayout alignmentHLayout = new VerticalLayout(cmbCurrencyType,txtCurrencyRate,txtConversionValue);
		mandatoryFields.add(cmbCurrencyType);
		mandatoryFields.add(txtCurrencyRate);
		mandatoryFields.add(txtConversionValue);
		return alignmentHLayout;
		
	}

	private void validateNumber(TextField txtCurrencyRate2) {
		CSValidator inrConvRateTxtValidator = new CSValidator();
		inrConvRateTxtValidator.extend(txtCurrencyRate2);
		inrConvRateTxtValidator.setRegExp("^[0-9.]*$");
		inrConvRateTxtValidator.setPreventInvalidTyping(true);
	}
	
	public Component getContent(BeanItemContainer<SelectValue> negotiatorName) {
		
		fireViewEvent(OMPRODBillEntryPagePresenter.OMP_PARTICULARS, referenceDataMap);
		ompClaimCalcViewTableObj =  ompClaimCalcViewTableInstance.get();
		ompClaimCalcViewTableObj.init(bean);
		ompClaimCalcViewTableObj.setReferenceData(referenceDataMap);
		if(bean.getClaimCalculationViewTable() != null){
		ompClaimCalcViewTableObj.setTableList(bean.getClaimCalculationViewTable());
		}
		ReceiptOfDocumentsDTO rodDTO  = new ReceiptOfDocumentsDTO();
		rodDTO.setClaimDTO(bean.getClaimDto());
		//txtRemark = binder.buildAndBind("Remark", "remarks",TextArea.class);
		//txtRemark.setWidth("40%");
		DocumentDetailsDTO documentDetails = bean.getDocumentDetails();
		documentCheckListValidation = documentCheckListValidationObj.get();
		documentCheckListValidation.initPresenter(SHAConstants.OMP_ROD);
		documentCheckListValidation.init();
		documentCheckListValidation.setReferenceData(referenceDataMap);
		documentCheckListValidation.setCaption("Document Checklist");
		
		if(bean.getDocumentDetails() != null && bean.getDocumentDetails().getDocumentCheckList() != null){
			documentCheckListValidation.setTableList(bean.getDocumentDetails().getDocumentCheckList());
		}	
		
		 wholeLayout = new VerticalLayout(documentCheckListValidation,currencyLayout(currencyValue),ompClaimCalcViewTableObj, getPaymentDetailsLayout());
		 wholeLayout.setComponentAlignment(ompClaimCalcViewTableObj, Alignment.TOP_CENTER);
		 wholeLayout.setSpacing(true);
		 wholeLayout.setSizeFull();
		 showOrHideValidation(false);
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
				fireViewEvent(MenuItemBean.OMP_PROCESS_OMP_CLAIM_PROCESSOR, null);
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
					fireViewEvent(OMPRODBillEntryPagePresenter.OMP_ROD_CLAIM_SUBMIT, bean);
					
					
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
					fireViewEvent(OMPRODBillEntryPagePresenter.OMP_ROD_CLAIM_SUBMIT, bean);
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
	
	/*private VerticalLayout getCalculationLayout(){
		
		
		calculationInstanceObj = calculationInstance.get();
		calculationInstanceObj.initView(this.bean);
		VerticalLayout verticalLayout = new VerticalLayout(calculationInstanceObj);
		
		return verticalLayout;
	}*/
	
	
	
	
	@SuppressWarnings("unchecked")
	public void setupReferences(Map<String, Object> referenceData) {
		
	}
	
	
	
	public boolean validatePage() {
		Boolean hasError = false;
		showOrHideValidation(true);
		String eMsg = "";	
		
		
		if(!this.binder.isValid()) {
			for (Field<?> field : this.binder.getFields()) {
				ErrorMessage errMsg = ((AbstractField<?>) field)
						.getErrorMessage();
				if (errMsg != null && field.isEnabled()){
					
					eMsg += errMsg.getFormattedHtmlMessage();
					hasError = true;
				}
			}
		}
		
		List<String> errors = ompClaimCalcViewTableObj.validateCalculation();
		if(null != errors && !errors.isEmpty()){
			for (String error : errors) {
				eMsg += error + "</br>";
				hasError = true;
			}
			
		}
		
		if(null != this.documentCheckListValidation)
		{
			Boolean isValid = documentCheckListValidation.validatePageForAckScreen();
			if (!isValid) {
				hasError = true;
				List<String> errors1 = this.documentCheckListValidation.getErrors();
				for (String error : errors1) {
					eMsg += error + "</br>";
					hasError = true;
				}
			}
		}
		
//		if(txtDelayHrs.getValue() != null){
////			hasError = true;
//			Notification.show("Message",
//	                  "Check the number of hours entered confirms the policy conditions",
//	                  Notification.Type.WARNING_MESSAGE);
////			eMsg += "Check the number of hours entered confirms the policy conditions</br>";
//		}
		
//		if(hospitalOption.getValue() != null && (boolean)hospitalOption.getValue() ){
		if(hospitalOption.getValue() != null && hospitalOption.getValue().equals(true) && claimTypeOption.getValue()!= null && claimTypeOption.getValue().equals(true) ){
			
			if(txtHospName.getValue() == null || txtHospName.getValue().isEmpty()){
				hasError = true;
				eMsg += "Please enter Hospital Name </br>";
			}
			if(txtHospCity.getValue() == null || txtHospCity.getValue().isEmpty()){
				hasError = true;
				eMsg += "Please enter Hospital City </br>";
			}
			if(cmbHospCountry.getValue() == null){
				hasError = true;
				eMsg += "Please Select County Value</br>";
			}
//			if(txtAilmentOrLoss.getValue() == null || txtAilmentOrLoss.getValue().isEmpty()){
//				hasError = true;
//				eMsg += "Please enter Ailment / Loss </br>";
//			}
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
				List<OMPClaimCalculationViewTableDTO> claimCalcViewTableList = ompClaimCalcViewTableObj.getValues();
				if(claimCalcViewTableList!=null){
					bean.setClaimCalculationViewTable(claimCalcViewTableList);
				}
				List<DocumentCheckListDTO> objDocCheckList = this.documentCheckListValidation.getValues();
				if(objDocCheckList!=null){
					this.bean.getDocumentDetails().setDocumentCheckList(objDocCheckList);
				}
				//bindValues();
			} catch (CommitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			return hasError;
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

	/*private void bindValues() {
		if(this.calculationInstanceObj != null) {
			this.calculationInstanceObj.isValid();
		}
	}*/


	
	private VerticalLayout getPaymentDetailsLayout()
	{
		cmbPaymentTo = (ComboBox) binder.buildAndBind("Payment To" , "paymentTo" , ComboBox.class);
		cmbPaymentTo.setValidationVisible(false);
		txtPayeeNameStr = (TextField) binder.buildAndBind("Payee Name" , "payeeNameStr" , TextField.class);
		txtPayeeNameStr.setValidationVisible(false);
		cmbPaymentMode = (ComboBox) binder.buildAndBind("Payment Mode" , "payMode" , ComboBox.class);
		txtPayableAt =  (TextField) binder.buildAndBind("Payable At" , "payableAt" , TextField.class);
		txtPayableAt.setValidationVisible(false);
		cmbPaymentTo.removeAllValidators();
		txtPayableAt.removeAllValidators();
		txtPayeeNameStr.removeAllValidators();
		//txtPayableAt.setRequired(true);
		FormLayout formLayout = new FormLayout(cmbPaymentTo, txtPayeeNameStr, cmbPaymentMode, txtPayableAt);
		formLayout.setSpacing(true);
		txtPanNo =  (TextField) binder.buildAndBind("Pan No" , "panNo" , TextField.class);
		txtEmailId =  (TextField) binder.buildAndBind("Email ID" , "emailId" , TextField.class);
		FormLayout formLayout1 = new FormLayout(txtPanNo, txtEmailId);
//		mandatoryFields.add(txtPayeeNameStr);
//		mandatoryFields.add(cmbPaymentTo);
//		mandatoryFields.add(txtPayableAt);
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
		suggestforApproval = new Button("Suggest for Approval");
		
		
		
		HorizontalLayout buttonHLayout = new HorizontalLayout(sendtoNegotiatorBtn, suggestforRejectionBtn,suggestforApproval);
		buttonHLayout.setSpacing(true);
		
		HorizontalLayout alignmentHLayout = new HorizontalLayout(buttonHLayout);
		alignmentHLayout.setWidth("100%");
		alignmentHLayout.setComponentAlignment(buttonHLayout, Alignment.MIDDLE_CENTER);
		addListenerButton(buttonHLayout, negotiatorName);
	
		
		
		return alignmentHLayout;
	}
	
	private VerticalLayout subClassificationListener(final VerticalLayout alignmentHLayout, final BeanItemContainer<SelectValue> modeOfReciept, final BeanItemContainer<SelectValue> documentRecievedFrom ,final BeanItemContainer<SelectValue> negotiatorName){
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
							txtDoctorName.setValidationVisible(false);
							mandatoryFields.add(txtDoctorName);
							txtDoctorName.setRequired(true);
							alignmentHLayout.addComponent(txtDoctorName);
						}
						if(selectValue.getValue().equalsIgnoreCase("Investigation charges")){
							txtInvestigatorName = binder.buildAndBind("Investigator Name", "investigatorName",TextField.class);
							txtInvestigatorName.setValidationVisible(false);
							mandatoryFields.add(txtInvestigatorName);
							txtInvestigatorName.setRequired(true);
							alignmentHLayout.addComponent(txtInvestigatorName);
						}
						if(selectValue.getValue().equalsIgnoreCase("Advocate Fee")){
							txtAdvocateName = binder.buildAndBind("Advocate Name", "advocateName",TextField.class);
							txtAdvocateName.setValidationVisible(false);
							mandatoryFields.add(txtAdvocateName);
							txtAdvocateName.setRequired(true);
							alignmentHLayout.addComponent(txtAdvocateName);
						}
						if(selectValue.getValue().equalsIgnoreCase("Auditor Fee")){
							txtAuditorName = binder.buildAndBind("Auditor Name", "auditorName",TextField.class);
							txtAuditorName.setValidationVisible(false);
							mandatoryFields.add(txtAuditorName);
							txtAuditorName.setRequired(true);
							alignmentHLayout.addComponent(txtAuditorName);
						}
						if(selectValue.getValue().equalsIgnoreCase("Negotiator Fee")){
							unbindField(txtNegotiatorName);
							mandatoryFields.remove(txtNegotiatorName);
							txtNegotiatorName = binder.buildAndBind("Negotiator Name", "negotiatorName",ComboBox.class);
							txtNegotiatorName.setValidationVisible(false);
							mandatoryFields.add(txtNegotiatorName);
							txtNegotiatorName.setRequired(true);
							alignmentHLayout.addComponent(txtNegotiatorName);
							
							txtNegotiatorName.setContainerDataSource(negotiatorName);
							txtNegotiatorName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
							txtNegotiatorName.setItemCaptionPropertyId("value");
							txtNegotiatorName.setValue(bean.getNegotiatorName());
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
						if(componentCount>=5){
							mainLayout.removeComponent(mainLayout.getComponent(2));
						}
						System.out.println(componentCount);
						if(selectValue.getValue().equalsIgnoreCase("Reconsideration")){
							mainLayout.addComponent(getDocumentDetail(),2);
						}else{
							int componentCount2 = mainLayout.getComponentCount();
							if(componentCount2>=5){
								mainLayout.removeComponent(mainLayout.getComponent(2));
							}
						}
					}
				}
			}
		});
		return mainLayout;
	}
	
	
	protected void addListenerButton(final HorizontalLayout buttonHLayout, final BeanItemContainer<SelectValue> negotiatorName){
		sendtoNegotiatorBtn.addClickListener(new ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 2679764179795985945L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(OMPRODBillEntryPagePresenter.OMP_NEGOTIATE, buttonHLayout,negotiatorName);
			}
		});
		
		suggestforRejectionBtn.addClickListener(new ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = -1545640032342015257L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(OMPRODBillEntryPagePresenter.OMP_REJECTION, buttonHLayout);
			}
		});
		suggestforApproval.addClickListener(new ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1274221814969702338L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(OMPRODBillEntryPagePresenter.OMP_APPROVAL, buttonHLayout);
			}
		});
	}	
		
	
	
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
		txtReasonOfNegotiation = binder.buildAndBind("Reason for Negotiation*" , "reasonForNegotiation", TextArea.class);
		
		txtNameofNegotiator.setContainerDataSource(negotiatorName);
		txtNameofNegotiator.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		txtNameofNegotiator.setItemCaptionPropertyId("value");
		
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
		if(wholeLayout.getComponentCount()>5){
			wholeLayout.removeComponent(wholeLayout.getComponent(wholeLayout.getComponentCount() - 1));
			wholeLayout.addComponent(layout);
		}else{
			
			wholeLayout.addComponent(layout);
		}
		
	}

	@Override
	public void init(OMPClaimProcessorDTO bean) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Component getContent() {
		
		initBinder();
		HorizontalLayout wizardLayout1 = new HorizontalLayout (viewButtonsLayout());
		wizardLayout1.setSpacing(true);
		wizardLayout1.setSizeFull();
		
		HorizontalLayout wizardLayout2 = new HorizontalLayout (eventlayout(documentType), hospitalLayout(country));
		//wizardLayout2.setComponentAlignment(viewDetailsForm, Alignment.TOP_RIGHT);
		wizardLayout2.setSpacing(true);
		wizardLayout2.setSizeFull();
		
		
		VerticalLayout wizardLayout4 = new VerticalLayout (classificationLayout(modeOfReciept, documentRecievedFrom));
		wizardLayout4.setSpacing(true);
		wizardLayout4.setSizeFull();
		
		mainLayout = new VerticalLayout(wizardLayout1 , wizardLayout2);
		documentTypeListener(mainLayout);
		mainLayout.addComponent(wizardLayout4);
		mainLayout.addComponent(getContent(negotiatorName));
		
				
		mainLayout.setMargin(true);
		mainLayout.setCaption("");
		mainLayout.setSpacing(true);
		mainLayout.setSizeFull();
		setCmbValues(classification,subClassification,paymentMode,paymentTo,eventCode);
		cmbEventCode.setValue(bean.getEventCode());
		if(bean.getDocumentsReceivedFrom()!=null){
			cmbDocumentsReceivedFrom.setValue(bean.getDocumentsReceivedFrom());
		}
		//setCompositionRoot(mainLayout);
		addListenerForCurrency();
		addListenerForClassification();
		addListenerForCurrencyType();
		addListenerForDesibleHospDetails();
//		addListenerForReadOnlyClassification();
		//addListenerForReadOnlyClassification();
		if(bean.getIsMutipleRod()){
			cmbEventCode.setReadOnly(Boolean.TRUE);
			claimTypeOption.setReadOnly(Boolean.TRUE);
			hospitalOption.setReadOnly(Boolean.TRUE);
		}
		if(bean.getHospTypeBooleanval()!=null && bean.getHospTypeBooleanval()){
			enableDisableNonHospitalisationFields(true);
		}else{
			enableDisableNonHospitalisationFields(false);
		}
		addListenerForHospital();
		return mainLayout;
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
	
	private void addListenerForCurrencyType()
	{
		if(cmbCurrencyType != null) {
			cmbCurrencyType.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 7455756225751111662L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					SelectValue currencyRateSelectValue = (SelectValue) event.getProperty().getValue();
					if(currencyRateSelectValue != null && currencyRateSelectValue.getValue()!=null) {
						if(currencyRateSelectValue.getValue().equalsIgnoreCase("USD")){
							txtCurrencyRate.setValue("1");
							txtCurrencyRate.setEnabled(false);
							txtConversionValue.setEnabled(Boolean.TRUE);
						}else if(currencyRateSelectValue.getValue().equalsIgnoreCase("INR")){
							if(cmbClassification!=null && cmbClassification.getValue()!=null){
								SelectValue value2 = (SelectValue) cmbClassification.getValue();
								if(value2.getValue().equals("OMP Other Expenses")){
									txtCurrencyRate.setValue("1");
									txtConversionValue.setValue("1");
									txtCurrencyRate.setEnabled(Boolean.FALSE);
									txtConversionValue.setEnabled(Boolean.FALSE);
								}else{
									txtCurrencyRate.setValue("1");
									txtConversionValue.setValue("1");
									txtCurrencyRate.setEnabled(Boolean.TRUE);
									txtConversionValue.setEnabled(Boolean.TRUE);
								}
							}
						}
						else{
							txtCurrencyRate.setValue("1");
							txtCurrencyRate.setEnabled(Boolean.TRUE);
							txtConversionValue.setEnabled(Boolean.TRUE);
						}
						
					}
					
				}
			});
		}
	}
	private void addListenerForDesibleHospDetails()
	{
//		if(hospitalOption.getValue() != null){
//	hospitalOption.addValueChangeListener(new Property.ValueChangeListener() {
//		
//		@Override
//		public void valueChange(ValueChangeEvent event) {
//			String hospitalization = hospitalOption.getValue() != null ? hospitalOption.getValue().toString() : "";
//			String clmType = claimTypeOption.getValue() != null ? claimTypeOption.getValue().toString() : "";
//			if(!hospitalization.isEmpty() && ("Non Hospitalisation").equalsIgnoreCase(hospitalization) && !clmType.isEmpty() && ("Reimbursement").equalsIgnoreCase(clmType)){
//				enableDisableNonHospitalisationFields(false);
//			}
//			else{
//				enableDisableNonHospitalisationFields(true);
//			}
//		}
//	});
//		}
	}
	
	/*private void addListenerForReadOnlyClassification()
	{
		if(cmbClassification != null){
		cmbClassification.addValueChangeListener(new ValueChangeListener() {
		
			private static final long serialVersionUID = 2697682747976915503L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				
				SelectValue selectValue = (SelectValue) event.getProperty().getValue();
				
				if(selectValue!=null){
					
					//List<Field<?>> listOfSubclassifcationFields = getListOfSubclassifcationFields();
					//unbindField(listOfSubclassifcationFields);
					List<SelectValue> itemIds = subClassification.getItemIds();
					if(selectValue.getValue()!=null ){
						if(selectValue.getValue().equalsIgnoreCase("OMP Claim Related")){
							ompClaimCalcViewTableObj.classification.setValue("OMP Claim Related");
							
							if(itemIds != null && ompClaimCalcViewTableObj!= null){
								ompClaimCalcViewTableObj.dummyField.setValue(itemIds.toString());
							}
						}else if(selectValue.getValue().equalsIgnoreCase("Negotiator Fee")){
							ompClaimCalcViewTableObj.classification.setValue("Negotiator Fee");
							if(itemIds != null && ompClaimCalcViewTableObj!= null){
								ompClaimCalcViewTableObj.dummyField.setValue(itemIds.toString());
							}
						}else{
							ompClaimCalcViewTableObj.classification.setValue("Other Exp");
							if(itemIds!= null && ompClaimCalcViewTableObj!= null){
								ompClaimCalcViewTableObj.dummyField.setValue(itemIds.toString());
							}
						}
						
					}
				}
			}
		
		});
<<<<<<< HEAD
		}
	}
=======
	}*/
	
	
	@Override
	public boolean onAdvance() {
		return	!validatePage();
						
//			return true;
//		}
//		// TODO Auto-generated method stub
//		return false;
	}

	@Override
	public boolean onBack() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onSave() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setReferenceDate(Map<String, Object> referenceDataMap) {
		this.referenceDataMap = referenceDataMap;
		this.referenceDataMapCategory = referenceDataMap;
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
	
	public void enableDisableNonHospitalisationFields(boolean value){
		
		if(!value){
			unbindField(txtHospCity);
			unbindField(txtHospName);
			unbindField(cmbHospCountry);
			txtHospCity.setValue(null);
//			txtHospCity.setValidationVisible(false);
			txtHospName.setValue(null);
			cmbHospCountry.setValue(null);
		}
		txtHospCity.setEnabled(value);
		txtHospName.setEnabled(value);
		cmbHospCountry.setEnabled(value);
	}
	
	
	private void addListenerForHospital()
	{
		if(hospitalOption != null) {
			  hospitalOption.addBlurListener(new BlurListener() {
					
					@Override
					public void blur(BlurEvent event) {
						OptionGroup totalValue = (OptionGroup) event.getComponent();
						Boolean value = (Boolean) totalValue.getValue();
						if(totalValue.getValue()!=null && totalValue.getValue().equals(true) && claimTypeOption!= null && claimTypeOption.getValue() != null && claimTypeOption.getValue().equals(false) ){
							
							enableDisableNonHospitalisationFields(true);
//							mandatoryFields.remove(txtHospName);
//							mandatoryFields.remove(txtHospCity);
//							mandatoryFields.remove(cmbHospCountry);
//							txtHospName.removeAllValidators();
//							txtHospCity.removeAllValidators();
							cmbHospCountry.removeAllValidators();
						}
						
						if(totalValue.getValue()!=null && totalValue.getValue().equals(false) && claimTypeOption!= null && claimTypeOption.getValue() != null && claimTypeOption.getValue().equals(true)){
					
							enableDisableNonHospitalisationFields(true);
//							mandatoryFields.remove(txtHospName);
//							mandatoryFields.remove(txtHospCity);
//							mandatoryFields.remove(cmbHospCountry);
//							txtHospName.removeAllValidators();
//							txtHospCity.removeAllValidators();
							cmbHospCountry.removeAllValidators();
						
					}
						if(totalValue.getValue()!=null && totalValue.getValue().equals(false) && claimTypeOption!= null && claimTypeOption.getValue() != null && claimTypeOption.getValue().equals(false) ){
							
							enableDisableNonHospitalisationFields(false);
//							mandatoryFields.remove(txtHospName);
//							mandatoryFields.remove(txtHospCity);
//							mandatoryFields.remove(cmbHospCountry);
//							txtHospName.removeAllValidators();
//							txtHospCity.removeAllValidators();
//							cmbHospCountry.removeAllValidators();
						}
						
						if(totalValue.getValue()!=null && totalValue.getValue().equals(true) && claimTypeOption!= null && claimTypeOption.getValue() != null && claimTypeOption.getValue().equals(true) ){
							
							enableDisableNonHospitalisationFields(true);
//							mandatoryFields.add(txtHospName);
//							mandatoryFields.add(txtHospCity);
//							mandatoryFields.add(cmbHospCountry);
							
						}
					}
				});
			  }
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
//							claimTypeOption.addValueChangeListener(new Property.ValueChangeListener() {
								
//								@Override
//								public void valueChange(ValueChangeEvent event) {
							if(value != null && value.equals(true)){
								if(hospitalOption != null){
									hospitalOption.setValue(true);
								}
							}
								if(value != null && value.equals(false)){
									if(hospitalOption != null){
										hospitalOption.setValue(false);
									}
								}
							}
							
							if(value != null && value.equals(true)){
							
								enableDisableNonHospitalisationFields(true);
//								mandatoryFields.add(txtHospName);
//								mandatoryFields.add(txtHospCity);
//								mandatoryFields.add(cmbHospCountry);
							}else{
								enableDisableNonHospitalisationFields(false);
//								mandatoryFields.remove(txtHospName);
//								mandatoryFields.remove(txtHospCity);
//								mandatoryFields.remove(cmbHospCountry);
//								txtHospName.removeAllValidators();
//								txtHospCity.removeAllValidators();
//								cmbHospCountry.removeAllValidators();
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
		
		if(txtDelayHrs != null){
			txtDelayHrs.addValueChangeListener( new ValueChangeListener( ) {
	          
	          /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
	          public void valueChange( ValueChangeEvent event ) {
				
				if(txtDelayHrs.getValue()!= null){
					Notification.show("Alert",
			                  "Check the number of hours entered confirms the policy conditions",
			                  Notification.Type.ERROR_MESSAGE);
				}
	              
			}

	      } );
		  }
		
		 if(lossDateField != null){
			 lossDateField.addValueChangeListener( new ValueChangeListener( ) {
		          
		          /**
				 * 
				 */
				private static final long serialVersionUID = 1L;					

				@Override
		          public void valueChange( ValueChangeEvent event ) {
					
					String str1 = (lossDateField.getValue() != null && !SHAUtils.isOMPDateOfIntimationWithPolicyRange(bean.getNewIntimationDto().getPolicy().getPolicyFromDate(), bean.getNewIntimationDto().getPolicy().getPolicyToDate(), lossDateField.getValue()) ? "Loss Date-Time is not with in policy period.</br>" : "");
					
					if(!SHAUtils.isEmpty(str1)){
//						lossDateField.setValue(null);
						showErrorMessage(str1);
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
							BeanItemContainer<SelectValue> subClassification2 = new BeanItemContainer<SelectValue>(SelectValue.class);
							if(value2.getValue().equals("OMP Other Expenses")){
								
								SelectValue selectValue1 = new SelectValue();
								selectValue1.setId(177L);
								selectValue1.setValue("INR");
								cmbCurrencyType.setValue(selectValue1);
								txtCurrencyRate.setValue("1");
								txtCurrencyRate.setEnabled(Boolean.FALSE);
								txtConversionValue.setValue("1");
								txtConversionValue.setEnabled(Boolean.FALSE);
								ompClaimCalcViewTableObj.reset();
								ompClaimCalcViewTableObj.classification.setValue("Other Exp");
								referenceDataMapCategory.clear();
								fireViewEvent(OMPRODBillEntryPagePresenter.OMP_PARTICULARS, referenceDataMapCategory, SHAConstants.OMPCLM_OTHR);
								ompClaimCalcViewTableObj.setReferenceData(referenceDataMapCategory);
								if(ompClaimCalcViewTableObj.dropDownDummy!=null){
									ompClaimCalcViewTableObj.dropDownDummy.setValue("YEAH");
								}
								List<SelectValue> itemIds = subClassification.getItemIds();
								List<SelectValue> itemIdsList = new ArrayList<SelectValue>();
								SelectValue defaultValue= null;
								for (SelectValue selectValue : itemIds) {
									if(!selectValue.getValue().equalsIgnoreCase("OMP Claim Related") && !selectValue.getValue().equalsIgnoreCase("Negotiator Fee")){
										itemIdsList.add(selectValue);
									}
									if(selectValue.getValue().equalsIgnoreCase("Doctors opinion")){
										defaultValue =selectValue;
									}
								}
								subClassification2.addAll(itemIdsList);
								cmbSubClassification.setContainerDataSource(subClassification2);
								cmbSubClassification.setItemCaptionMode(ItemCaptionMode.PROPERTY);
								cmbSubClassification.setItemCaptionPropertyId("value");
								cmbSubClassification.setValue(defaultValue);
							}
							if(value2.getValue().equals("OMP Claim Related")){
								txtCurrencyRate.setEnabled(Boolean.TRUE);
								txtConversionValue.setEnabled(Boolean.TRUE);
								ompClaimCalcViewTableObj.reset();
								ompClaimCalcViewTableObj.classification.setValue("OMP Claim Related");
								referenceDataMapCategory.clear();
								fireViewEvent(OMPRODBillEntryPagePresenter.OMP_PARTICULARS, referenceDataMapCategory, SHAConstants.OMPCLM_REL);
								ompClaimCalcViewTableObj.setReferenceData(referenceDataMapCategory);
								if(ompClaimCalcViewTableObj.dropDownDummy!=null){
									ompClaimCalcViewTableObj.dropDownDummy.setValue("dummy");
								}
								SelectValue defaultValue= null;
								List<SelectValue> itemIds = subClassification.getItemIds();
								List<SelectValue> itemIdsList = new ArrayList<SelectValue>();
								for (SelectValue selectValue : itemIds) {
									if(selectValue.getValue().equalsIgnoreCase("OMP Claim Related")){
										itemIdsList.add(selectValue);
										defaultValue =selectValue;
									}
								}
								subClassification2.addAll(itemIdsList);
								cmbSubClassification.setContainerDataSource(subClassification2);
								cmbSubClassification.setItemCaptionMode(ItemCaptionMode.PROPERTY);
								cmbSubClassification.setItemCaptionPropertyId("value");
								cmbSubClassification.setValue(defaultValue);
							}
							if(value2.getValue().equals("Negotiator Fee")){
								SelectValue selectValue1 = new SelectValue();
								selectValue1.setId(48L);
								selectValue1.setValue("USD");
								cmbCurrencyType.setValue(selectValue1);
								txtCurrencyRate.setValue("1");
								txtCurrencyRate.setEnabled(Boolean.FALSE);
								txtConversionValue.setEnabled(Boolean.TRUE);
								ompClaimCalcViewTableObj.reset();
								ompClaimCalcViewTableObj.classification.setValue("Negotiator Fee");
								referenceDataMapCategory.clear();
								fireViewEvent(OMPRODBillEntryPagePresenter.OMP_PARTICULARS, referenceDataMapCategory, SHAConstants.OMPCLM_NEGO);
								ompClaimCalcViewTableObj.setReferenceData(referenceDataMapCategory);
								if(ompClaimCalcViewTableObj.dropDownDummy!=null){
									ompClaimCalcViewTableObj.dropDownDummy.setValue("TEST");
								}
								SelectValue defaultValue= null;
								List<SelectValue> itemIds = subClassification.getItemIds();
								List<SelectValue> itemIdsList = new ArrayList<SelectValue>();
								for (SelectValue selectValue : itemIds) {
									if(selectValue.getValue().equalsIgnoreCase("Negotiator Fee")){
										itemIdsList.add(selectValue);
										defaultValue =selectValue;
									}
								}
								subClassification2.addAll(itemIdsList);
								cmbSubClassification.setContainerDataSource(subClassification2);
								cmbSubClassification.setItemCaptionMode(ItemCaptionMode.PROPERTY);
								cmbSubClassification.setItemCaptionPropertyId("value");
								cmbSubClassification.setValue(defaultValue);
							}
							
							}
						}
					
				}
			});
		}
	}
	
	private void addListenerForDocumentRecievedFrom()
	{
		if(cmbDocumentsReceivedFrom != null) {
			cmbDocumentsReceivedFrom.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 7455756225751111662L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					SelectValue totalValue = (SelectValue) event.getProperty().getValue();
					if(totalValue != null && totalValue.getValue()!=null) {
						String value = totalValue.getValue();
						if(value!=null && value.equalsIgnoreCase(SHAConstants.DOC_RECEIVED_FROM_HOSPITAL)){
							if(bean.getIsDocRecHospital()){
								showErrorMessage("ROD with document received from hospital is already exists");
								cmbDocumentsReceivedFrom.setValue(null);
							}
						}
						if(value!=null && value.equalsIgnoreCase(SHAConstants.DOC_RECEIVED_FROM_INSURED)){
							if(bean.getIsDocRecInsured()){
								showErrorMessage("ROD with document received from insured is already exists");
								cmbDocumentsReceivedFrom.setValue(null);
							}
						}
						
					}
					
				}
			});
		}
	}
	
	private void showErrorMessage(String eMsg) {
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
	}
	
}

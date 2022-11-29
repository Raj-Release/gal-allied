package com.shaic.claim.OMPProcessOmpClaimApprover.pages;

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
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.IntimationDetailsCarousel;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPCalcualtionUI;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimCalculationViewTable;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimCalculationViewTableDTO;
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
import com.vaadin.server.ErrorMessage;
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
import com.vaadin.ui.Notification;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class OMPProcessOmpClaimApproverPageUI extends ViewComponent{

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
	
	private TextArea txtRemarkApprver;
	
	private BeanItemContainer<SelectValue> documentRecievedFrom ;
	
	BeanItemContainer<SelectValue> subClassification;
	@Inject
	private Instance<IntimationDetailsCarousel> commonCarouselInstance;
	
	Map<String, Object> referenceDataMapCategory = new HashMap<String, Object>();

	
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
		this.subClassification = subClassification;
		this.documentRecievedFrom=documentRecievedFrom; 
	/*	revisedCarousel = carouselInstance.get();
		revisedCarousel.init(bean.getNewIntimationDto());*/
		IntimationDetailsCarousel intimationDetailsCarousel = commonCarouselInstance
				.get();
		intimationDetailsCarousel.initOMPCarousal(this.bean.getNewIntimationDto(), "Process OMP Claim - Approver");
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
		mainLayout = new VerticalLayout(vlayout, wizardLayout1 , txtRodNumber,wizardLayout2);
		documentTypeListener(mainLayout);
		mainLayout.addComponent(wizardLayout4);
		mainLayout.addComponent(getContent(negotiatorName));
		
		mainLayout.addComponent(wizardLayout5);
		
				
		mainLayout.setComponentAlignment(wizardLayout5, Alignment.MIDDLE_CENTER);
		mainLayout.setMargin(true);
		mainLayout.setCaption("");
		mainLayout.setSpacing(true);
		mainLayout.setSizeFull();
		setCmbValues(classification,subClassification,paymentMode,paymentTo,eventCode);
		//cmbEventCode.setValue(bean.getEventCode());
		cmbClassification.setValue(bean.getClassification());
		cmbSubClassification.setValue(bean.getSubClassification());
		cmbPaymentMode.setValue(bean.getPayMode());
		cmbPaymentTo.setValue(bean.getPaymentTo());
		cmbDocType.setValue(bean.getDocType());
		cmbHospCountry.setValue(bean.getHospCountry());
		cmbEventCode.setValidationVisible(false);
//		cmbDocumentsReceivedFrom.setValue(bean.getDocumentsReceivedFrom());
//		cmbModeOfReceipt.setValue(bean.getModeOfReceipt());
//		cmbPayeeName.setValue(bean.getPayeeNameStr());
//		cmbCurrencyType.setValue(bean.getCurrencyType());
//		cmbHospCountry.setValue(bean.getCountry());
		//ompClaimCalcViewTableObj.dummyField.setValue(bean.getCurrencyRate().toString());
		
		if(bean.getHospTypeBooleanval()!=null && bean.getHospTypeBooleanval()){
			enableDisableNonHospitalisationFields(true);
		}else{
			enableDisableNonHospitalisationFields(false);
		}
		addListenerForCurrency();
		addListenerForCurrencyType();
		addListenerForConversionRate();
		addListenerForTotal();
		addListenerForTotalExp();
		addListenerForpayable();
		addListenerForEventCode();
		cmbEventCode.setValue(null);
		cmbEventCode.setValue(bean.getEventCode());
		if(bean.getIsMutipleRod()){
			cmbEventCode.setReadOnly(Boolean.TRUE);
			claimTypeOption.setReadOnly(Boolean.TRUE);
			hospitalOption.setReadOnly(Boolean.TRUE);
		}
		if(bean.getCurrencyRate()!=null){
			txtCurrencyRate.setValue(bean.getCurrencyRate().toString());
		}
		setCompositionRoot(mainLayout);
		setSizeFull();
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
		
		
		cmbSubClassification.setContainerDataSource(subClassification);
		cmbSubClassification.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbSubClassification.setItemCaptionPropertyId("value");
		
		cmbPaymentTo.setContainerDataSource(paymentTo);
		cmbPaymentTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPaymentTo.setItemCaptionPropertyId("value");
		
		cmbPaymentMode.setContainerDataSource(paymentMode);
		cmbPaymentMode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPaymentMode.setItemCaptionPropertyId("value");
		
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
		claimTypeOption = binder.buildAndBind("","claimType",OptionGroup.class);
		claimTypeOption.setNullSelectionAllowed(true);
		claimTypeOption.addItems(getReadioButtonOptions());
		claimTypeOption.setItemCaption(true, "Cashless");
		claimTypeOption.setItemCaption(false, "Reimbursement");
		claimTypeOption.setStyleName("horizontal");
		
		cmbEventCode = binder.buildAndBind("Event Code" , "eventCode", ComboBox.class);
		cmbEventCode.setValidationVisible(false);
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
		
	/*	OptionGroup  optionGroup = new OptionGroup ();
		optionGroup.setNullSelectionAllowed(true);
		optionGroup.addItems("Hospital","Non Hospital");
		optionGroup.setStyleName("horizontal");*/
		
		hospitalOption = binder.buildAndBind("","hospTypeBooleanval",OptionGroup.class);
		hospitalOption.setStyleName("horizontal");
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
		
		FormLayout optionLayout = new FormLayout(hospitalOption,txtHospName,txtHospCity,cmbHospCountry,txtPlaceOfVisit,txtreasonForReconsider);
		if(bean.getClaimType() && bean.getHospTypeBooleanval()){
			mandatoryFields.add(txtHospName);
			mandatoryFields.add(txtHospCity);
			mandatoryFields.add(cmbHospCountry);
		}
		mandatoryFields.add(txtPlaceOfVisit);
		addListenerForHospital();
		HorizontalLayout alignmentHLayout = new HorizontalLayout(optionLayout);
		return alignmentHLayout;
		
	}
	
	private VerticalLayout documentDetailLayout(BeanItemContainer<SelectValue> modeOfReciept, BeanItemContainer<SelectValue> documentRecievedFrom){
		
		cmbDocumentsReceivedFrom = binder.buildAndBind("Documents Recieved From", "documentsReceivedFrom",ComboBox.class);

		documentsReceivedDate = binder.buildAndBind("Documents Recieved Date","documentsReceivedDate", DateField.class);

		cmbModeOfReceipt = binder.buildAndBind("Mode Of Receipt","modeOfReceipt", ComboBox.class);
		
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
		addListenerForDocument();
		
		cmbDocumentsReceivedFrom.setValue(bean.getDocumentsReceivedFrom());
		cmbDocumentsReceivedFrom.setNullSelectionAllowed(Boolean.TRUE);
		
		return alignmentHLayout;
	}
	
	private VerticalLayout classificationLayout(BeanItemContainer<SelectValue> modeOfReciept, BeanItemContainer<SelectValue> documentRecievedFrom,BeanItemContainer<SelectValue> negotiatorName){
		
		cmbClassification = binder.buildAndBind("Classification", "classification",ComboBox.class);
		cmbClassification.setValidationVisible(false);
		cmbSubClassification = binder.buildAndBind("Sub Classification", "subClassification",ComboBox.class);
		cmbSubClassification.setValidationVisible(false);
		VerticalLayout alignmentHLayout = new VerticalLayout(cmbClassification,cmbSubClassification);
		mandatoryFields.add(cmbClassification);
		mandatoryFields.add(cmbSubClassification);
		alignmentHLayout = subClassificationListener(alignmentHLayout , modeOfReciept, documentRecievedFrom, negotiatorName);
		alignmentHLayout.setSpacing(true);
		addListenerForClassification();
		addListenerForSubClassification();
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
		
		cmbCurrencyType.setContainerDataSource(currencyValue);
		cmbCurrencyType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCurrencyType.setItemCaptionPropertyId("value");
		cmbCurrencyType.setValue(bean.getCurrencyType());
		VerticalLayout alignmentHLayout = new VerticalLayout(cmbCurrencyType,txtCurrencyRate,txtConversionValue);
		mandatoryFields.add(cmbCurrencyType);
		mandatoryFields.add(txtCurrencyRate);
		mandatoryFields.add(txtConversionValue);
		addListenerForCurrency();
		if(cmbCurrencyType!=null && cmbCurrencyType.getValue()!=null ){
			SelectValue value = (SelectValue) cmbCurrencyType.getValue();
			if(value!=null && value.getValue().equalsIgnoreCase("USD")){
				txtCurrencyRate.setValue("1");
				txtCurrencyRate.setEnabled(false);
			}
			if(value!=null && value.getValue().equalsIgnoreCase("INR")){
				txtCurrencyRate.setValue("1");
				txtCurrencyRate.setEnabled(Boolean.FALSE);
				txtConversionValue.setEnabled(Boolean.FALSE);
			}
		}
		
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
						calculationInstanceObj.dummyCurrency.setValue(totalValue);
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
	
	private void addListenerForEventCode()
	{
		if(cmbEventCode != null) {
			cmbEventCode.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 7455756225751111662L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					SelectValue currencyRateSelectValue = (SelectValue) event.getProperty().getValue();
					if(cmbClassification!=null && cmbClassification.getValue()!=null){
					SelectValue value2 = (SelectValue) cmbClassification.getValue();
					if(!value2.getValue().equals("OMP Other Expenses")){
					if(currencyRateSelectValue != null && currencyRateSelectValue.getValue()!=null) {
						fireViewEvent(OMPProcessClaimApproverWizardPresenter.OMP_Approver_BSI, bean, currencyRateSelectValue);
						if(txtCurrencyRate!=null){
							String value = txtCurrencyRate.getValue();
							ompClaimCalcViewTableObj.dummyField.setValue("0");
							ompClaimCalcViewTableObj.dummyField.setValue(value);
							calculationInstanceObj.dummyCurrency.setValue("0");
							calculationInstanceObj.dummyCurrency.setValue(value);
							
						}
					}}}
				}
			});
		}
	}
	
	private void addListenerForConversionRate()
	{
		if(txtConversionValue != null) {
			txtConversionValue.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 7455756225751111662L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					String totalValue = (String) event.getProperty().getValue();
					if(totalValue != null && ompClaimCalcViewTableObj!=null) {
						calculationInstanceObj.dummyConversionRate.setValue(totalValue);
					}
					
				}
			});
		}
	}
	
	
	
	private void addListenerForTotal()
	{
		if(ompClaimCalcViewTableObj.dummyFieldBillAmt != null) {
			ompClaimCalcViewTableObj.dummyFieldBillAmt.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 7455756225751111662L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					String totalValue = (String) event.getProperty().getValue();
					if(totalValue != null && calculationInstanceObj!=null) {
						calculationInstanceObj.dummyField.setValue(totalValue);
						if(totalValue.equalsIgnoreCase("0") || totalValue.equalsIgnoreCase("0.0")){
							suggestforApproval.setEnabled(false);
						}else{
							suggestforApproval.setEnabled(true);
						}
						if(txtConversionValue.getValue()!=null){
							//calculationInstanceObj.dummyCurrency.setValue(txtConversionValue.getValue());
							calculationInstanceObj.dummyConversionRate.setValue(null);
							calculationInstanceObj.dummyConversionRate.setValue(txtConversionValue.getValue());
						}
						/*if(txtCurrencyRate.getValue()!=null){
							calculationInstanceObj.dummyCurrency.setValue(null);
							calculationInstanceObj.dummyCurrency.setValue(txtCurrencyRate.getValue());
							//SUAN
						}*/
					}
					
				}
			});
		}
	}
	
	private void addListenerForpayable()
	{
		if(ompClaimCalcViewTableObj.dummyFieldINRTotal != null) {
			ompClaimCalcViewTableObj.dummyFieldINRTotal.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 7455756225751111662L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					String totalValue = (String) event.getProperty().getValue();
					if(totalValue != null && calculationInstanceObj!=null) {
						calculationInstanceObj.dummyPayableAmt.setValue(totalValue);
						if(totalValue.equalsIgnoreCase("0.0")){
							suggestforApproval.setEnabled(false);
						}else{
							suggestforApproval.setEnabled(true);
						}
					}
					
				}
			});
		}
	}
	
	private void addListenerForTotalExp()
	{
		if(ompClaimCalcViewTableObj.dummyFieldExpTotal != null) {
			ompClaimCalcViewTableObj.dummyFieldExpTotal.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 7455756225751111662L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					String totalValue = (String) event.getProperty().getValue();
					if(totalValue != null && calculationInstanceObj!=null) {
						calculationInstanceObj.dummyPayableAmtNego.setValue(totalValue);
						if(totalValue.equalsIgnoreCase("0.0")){
							suggestforApproval.setEnabled(false);
						}else{
							suggestforApproval.setEnabled(true);
						}
						if(txtConversionValue.getValue()!=null){
							//calculationInstanceObj.dummyCurrency.setValue(txtConversionValue.getValue());
							calculationInstanceObj.dummyConversionRate.setValue(null);
							calculationInstanceObj.dummyConversionRate.setValue(txtConversionValue.getValue());
						}
					}
					
				}
			});
		}
	}
	
	public Component getContent(BeanItemContainer<SelectValue> negotiatorName) {
		
		fireViewEvent(OMPProcessClaimApproverWizardPresenter.OMP_APPROVER_PARTICULARS, referenceDataMap);
		ompClaimCalcViewTableObj =  ompClaimCalcViewTableInstance.get();
		ompClaimCalcViewTableObj.init(bean);
		ompClaimCalcViewTableObj.setReferenceData(referenceDataMap);
		if(bean.getClaimCalculationViewTable() != null){
			ompClaimCalcViewTableObj.setTableList(bean.getClaimCalculationViewTable());
			}
		txtRemark = binder.buildAndBind("Remarks (Processor)", "remarks",TextArea.class);
		txtRemark.setMaxLength(4000);
		txtRemark.setReadOnly(true);
		
		txtRemarkApprver = binder.buildAndBind("Remark(Approver)", "remarksApprover",TextArea.class);
		txtRemarkApprver.setMaxLength(4000);
		
		 wholeLayout = new VerticalLayout(ompClaimCalcViewTableObj, getCalculationLayout(),txtRemark,txtRemarkApprver, getPaymentDetailsLayout());
		 HorizontalLayout buildButtons = buildButtons(bean,negotiatorName);
		 wholeLayout.addComponent(buildButtons);
		 wholeLayout.setComponentAlignment(ompClaimCalcViewTableObj, Alignment.TOP_CENTER);
		 wholeLayout.setSpacing(true);
		 wholeLayout.setSizeFull();
		 return wholeLayout;
	}
	
	private Button getCancelButton(final ConfirmDialog dialog) {
		btnCancel = new Button("Cancel");
		btnCancel.setStyleName(ValoTheme.BUTTON_DANGER);
		btnCancel.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				releaseWorkFlowTask();
				fireViewEvent(MenuItemBean.OMP_PROCESS_OMP_CLAIM_APPROVER, null);
			}
		});
		return btnCancel;
	}
	
	private Button getSaveNexitButtonWithListener(final ConfirmDialog dialog) {
		btnSubmit = new Button("Submit");
		btnSubmit.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSubmit.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				String eMsg = "";
				if(!validatePage(Boolean.FALSE)) {
					dialog.close();
					fireViewEvent(OMPProcessClaimApproverWizardPresenter.OMP_CLAIM_APPROVER_SUBMIT, bean);
					
					
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
				if(!validatePage(Boolean.TRUE)) {
					dialog.close();
					releaseWorkFlowTask();
					fireViewEvent(OMPProcessClaimApproverWizardPresenter.OMP_CLAIM_APPROVER_SAVE, bean);
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
		VerticalLayout verticalLayout = new VerticalLayout(calculationInstanceObj);
		
		return verticalLayout;
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	public void setupReferences(Map<String, Object> referenceData) {
		
	}
	
	
	
	public boolean validatePage(Boolean true1) {
		Boolean hasError = false;
		showOrHideValidation(true);
		String eMsg = "";	
		
		
		if(!this.binder.isValid()) {
			for (Field<?> field : this.binder.getFields()) {
				ErrorMessage errMsg = ((AbstractField<?>) field)
						.getErrorMessage();
				if (errMsg != null  && field.isEnabled()) {
					eMsg += errMsg.getFormattedHtmlMessage();
					hasError = true;
				}
			}
		}
		
		if(!hasError) {
			
		}
		List<String> errors = ompClaimCalcViewTableObj.validateCalculation();
		if(null != errors && !errors.isEmpty()){
			for (String error : errors) {
				eMsg += error + "</br>";
				hasError = true;
			}
			
		}
		if(!true1 && !this.bean.getButtonflag()){
			hasError = true;
			eMsg+=("Please do any one of the actions Send to Negotiator, Suggest Rejection, Suggest Approval </br> ");
		}
		
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
	
		if((cmbClassification.getValue()!= null && cmbClassification.getValue().toString().equalsIgnoreCase("OMP Claim Related"))
				&& (cmbDocumentsReceivedFrom.getValue() != null && cmbDocumentsReceivedFrom.getValue().toString().equalsIgnoreCase("Hospital"))){
			if(bean.getCalculationSheetDTO()!= null){
				this.calculationInstanceObj.isValid();
				if(bean.getCalculationSheetDTO().getDeductiblerecoveredfrominsured() == null){
					hasError = true;
					eMsg+= "Please select Yes or No Deductible recovered from insured </br>";
				}
				if(bean.getCalculationSheetDTO().getDeductiblerecoveredfrominsured()!= null && bean.getCalculationSheetDTO().getDeductiblerecoveredfrominsured().equalsIgnoreCase("Y")){
					if(bean.getCalculationSheetDTO().getDateOfRecovery() == null){
						hasError = true;
						eMsg+= "Please select date of recovery </br>";
					}
					if(bean.getCalculationSheetDTO().getAmtRecoveredINR() == null){
						hasError = true;
						eMsg+= "Please enter recovered Amt Inr </br>";
					}
					if(bean.getCalculationSheetDTO().getAmtRecoveredDollar() == null){
						hasError = true;
						eMsg+= "Please enter recovery Amt Dollar </br>";
					}
					if(bean.getCalculationSheetDTO().getRemarks() == null){
						hasError = true;
						eMsg+= "Please enter remarks </br>";
					}
				}
			}
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
				List<OMPClaimCalculationViewTableDTO> claimCalcViewTableList = ompClaimCalcViewTableObj.getValues();
				if(claimCalcViewTableList!=null){
					bean.setClaimCalculationViewTable(claimCalcViewTableList);
				}
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
	}


	
	private VerticalLayout getPaymentDetailsLayout()
	{
		cmbPaymentTo = (ComboBox) binder.buildAndBind("Payment To" , "paymentTo" , ComboBox.class);
		cmbPaymentTo.setValidationVisible(false);
		txtPayeeNameStr = (TextField) binder.buildAndBind("Payee Name" , "payeeNameStr" , TextField.class);
		txtPayeeNameStr.setValidationVisible(false);
//		cmbPayeeName.setValue(bean.getPayeeNameStr());
		cmbPaymentMode = (ComboBox) binder.buildAndBind("Payment Mode" , "payMode" , ComboBox.class);
		txtPayableAt =  (TextField) binder.buildAndBind("Payable At" , "payableAt" , TextField.class);
		txtPayableAt.setValidationVisible(false);
		FormLayout formLayout = new FormLayout(cmbPaymentTo, txtPayeeNameStr, cmbPaymentMode, txtPayableAt);
		
		mandatoryFields.add(txtPayeeNameStr);
		mandatoryFields.add(cmbPaymentTo);
		mandatoryFields.add(txtPayableAt);
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
		suggestforRejectionBtn = new Button("Rejection");
		suggestforApproval = new Button("Approve (Send to Payment)");
		
		
		
		HorizontalLayout buttonHLayout = new HorizontalLayout(sendtoNegotiatorBtn, suggestforRejectionBtn,suggestforApproval);
		buttonHLayout.setSpacing(true);
		
		HorizontalLayout alignmentHLayout = new HorizontalLayout(buttonHLayout);
		alignmentHLayout.setWidth("100%");
		alignmentHLayout.setComponentAlignment(buttonHLayout, Alignment.MIDDLE_CENTER);
		addListenerButton(buttonHLayout, negotiatorName);
	
		
		
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
							unbindField(txtNameofNegotiator);
							mandatoryFields.remove(txtNameofNegotiator);
							txtNameofNegotiator = binder.buildAndBind("Negotiator Name", "negotiatorName",ComboBox.class);
							txtNameofNegotiator.setValidationVisible(false);
							mandatoryFields.add(txtNameofNegotiator);
							txtNameofNegotiator.setRequired(true);
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
	
	
	protected void addListenerButton(final HorizontalLayout buttonHLayout, final BeanItemContainer<SelectValue> negotiatorName){
		sendtoNegotiatorBtn.addClickListener(new ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 2679764179795985945L;

			@Override
			public void buttonClick(ClickEvent event) {
				bean.setButtonflag(true);
				fireViewEvent(OMPProcessClaimApproverWizardPresenter.OMP_NEGOTIATE, buttonHLayout,negotiatorName);
			}
		});
		
		suggestforRejectionBtn.addClickListener(new ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = -1545640032342015257L;

			@Override
			public void buttonClick(ClickEvent event) {
				bean.setButtonflag(true);
				fireViewEvent(OMPProcessClaimApproverWizardPresenter.OMP_REJECTION, buttonHLayout);
			}
		});
		suggestforApproval.addClickListener(new ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1274221814969702338L;

			@Override
			public void buttonClick(ClickEvent event) {
				bean.setButtonflag(true);
				fireViewEvent(OMPProcessClaimApproverWizardPresenter.OMP_APPROVAL, buttonHLayout);
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
	mandatoryFields.remove(txtNameofNegotiator);
	mandatoryFields.remove(txtReasonOfNegotiation);
	mandatoryFields.remove(txtReasonOfApproval);
	mandatoryFields.remove(txtReasonOfRejection);
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
		txtNameofNegotiator = binder.buildAndBind("Name of Negotiator" , "sendToNegotiatorName", ComboBox.class);
		txtNameofNegotiator.setValue(bean.getNegotiatorName());
		txtReasonOfNegotiation = binder.buildAndBind("Reason for Negotiation*" , "reasonForNegotiation", TextArea.class);
		txtReasonOfNegotiation.setMaxLength(4000);
		txtNameofNegotiator.setContainerDataSource(negotiatorName);
		txtNameofNegotiator.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		txtNameofNegotiator.setItemCaptionPropertyId("value");
		bean.setOutCome(SHAConstants.OUTCOME_FOR_OMP_APPROVER_NEGO);
		mandatoryFields.add(txtNameofNegotiator);
		mandatoryFields.add(txtReasonOfNegotiation);
		txtNameofNegotiator.setRequired(true);
		txtReasonOfNegotiation.setRequired(true);
		VerticalLayout layout = new VerticalLayout(txtNameofNegotiator, txtReasonOfNegotiation);
		if(wholeLayout.getComponentCount()>6){
			wholeLayout.removeComponent(wholeLayout.getComponent(wholeLayout.getComponentCount() - 1));
			wholeLayout.addComponent(layout);
		}else{
			
			wholeLayout.addComponent(layout);
		}
	}

	public void generateRejection(HorizontalLayout buttonLayout) {
		unbindField(getListOfButtonFields());
		txtReasonOfRejection = binder.buildAndBind("Reason for Rejection" , "reasonForRejection", TextArea.class);
		txtReasonOfRejection.setMaxLength(4000);
		VerticalLayout layout = new VerticalLayout(txtReasonOfRejection);
		bean.setOutCome(SHAConstants.OUTCOME_FOR_OMP_APPROVERREJECTION);
		mandatoryFields.add(txtReasonOfRejection);
		txtReasonOfRejection.setRequired(true);
		if(wholeLayout.getComponentCount()>6){
			wholeLayout.removeComponent(wholeLayout.getComponent(wholeLayout.getComponentCount() - 1));
			wholeLayout.addComponent(layout);
		}else{
			
			wholeLayout.addComponent(layout);
		}
		
	}

	public void generateApproval(HorizontalLayout buttonLayout) {
		unbindField(getListOfButtonFields());
		txtReasonOfApproval = binder.buildAndBind("Reason for sugesst  approval" , "reasonForApproval", TextArea.class);
		txtReasonOfApproval.setMaxLength(4000);
		VerticalLayout layout = new VerticalLayout(txtReasonOfApproval);
		bean.setOutCome(SHAConstants.OUTCOME_FOR_OMP_APPROVER);
		mandatoryFields.add(txtReasonOfApproval);
		txtReasonOfApproval.setRequired(true);
		if(wholeLayout.getComponentCount()>6){
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
	
public void enableDisableNonHospitalisationFields(boolean value){
		
		if(!value){
			txtHospCity.setValue(null);
			txtHospName.setValue(null);
			cmbHospCountry.setValue(null);
		}
		txtHospCity.setEnabled(value);
		txtHospName.setEnabled(value);
		cmbHospCountry.setEnabled(value);
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
							Boolean enableORdisableNegobutton = enableORdisableNegobutton();
							if(sendtoNegotiatorBtn!=null){
								sendtoNegotiatorBtn.setEnabled(enableORdisableNegobutton);
								if(!enableORdisableNegobutton){
									if(wholeLayout.getComponentCount()>6){
										wholeLayout.removeComponent(wholeLayout.getComponent(wholeLayout.getComponentCount() - 1));
									}
								}
							}
							manipulateDocumentRecievedFrom(value);
						
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
	private void addListenerForHospital()
	{
		if(hospitalOption != null) {
			hospitalOption.addValueChangeListener(new ValueChangeListener() {

				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					OptionGroup totalValue = (OptionGroup) event.getProperty();
					if(totalValue!=null && totalValue.getValue()!=null){
						Boolean value = (Boolean) totalValue.getValue();
						if(value!=null){
							if(totalValue.getValue()!=null && totalValue.getValue().equals(true) && claimTypeOption!= null && claimTypeOption.getValue() != null && claimTypeOption.getValue().equals(false)){
//								mandatoryFields.remove(txtHospName);
//								mandatoryFields.remove(txtHospCity);
//								mandatoryFields.remove(cmbHospCountry);
//								txtHospName.removeAllValidators();
//								txtHospCity.removeAllValidators();
//								cmbHospCountry.removeAllValidators();
								enableDisableNonHospitalisationFields(true);
							}
							if(totalValue.getValue()!=null && totalValue.getValue().equals(false) && claimTypeOption!= null && claimTypeOption.getValue() != null && claimTypeOption.getValue().equals(true)){
//								mandatoryFields.remove(txtHospName);
//								mandatoryFields.remove(txtHospCity);
//								mandatoryFields.remove(cmbHospCountry);
//								txtHospName.removeAllValidators();
//								txtHospCity.removeAllValidators();
//								cmbHospCountry.removeAllValidators();
								enableDisableNonHospitalisationFields(true);
							}
							
							if(totalValue.getValue()!=null && totalValue.getValue().equals(false) && claimTypeOption!= null && claimTypeOption.getValue() != null && claimTypeOption.getValue().equals(false) ){
//								mandatoryFields.remove(txtHospName);
//								mandatoryFields.remove(txtHospCity);
//								mandatoryFields.remove(cmbHospCountry);
//								txtHospName.removeAllValidators();
//								txtHospCity.removeAllValidators();
//								cmbHospCountry.removeAllValidators();
								enableDisableNonHospitalisationFields(false);
								
							}
							
							if(totalValue.getValue()!=null && totalValue.getValue().equals(true) && claimTypeOption!= null && claimTypeOption.getValue() != null && claimTypeOption.getValue().equals(true) ){
//								mandatoryFields.add(txtHospName);
//								mandatoryFields.add(txtHospCity);
//								mandatoryFields.add(cmbHospCountry);
								
								enableDisableNonHospitalisationFields(true);
								
							}
							Boolean enableORdisableNegobutton = enableORdisableNegobutton();
							if(sendtoNegotiatorBtn!=null){
								sendtoNegotiatorBtn.setEnabled(enableORdisableNegobutton);
								if(!enableORdisableNegobutton){
									if(wholeLayout.getComponentCount()>6){
										wholeLayout.removeComponent(wholeLayout.getComponent(wholeLayout.getComponentCount() - 1));
									}
								}
							}
							
						}
					}
					
				}
			});
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
							Boolean enableORdisableNegobutton = enableORdisableNegobutton();
							if(sendtoNegotiatorBtn!=null){
								sendtoNegotiatorBtn.setEnabled(enableORdisableNegobutton);
								if(!enableORdisableNegobutton){
									if(wholeLayout.getComponentCount()>6){
										wholeLayout.removeComponent(wholeLayout.getComponent(wholeLayout.getComponentCount() - 1));
									}
								}
							}
							BeanItemContainer<SelectValue> subClassification2 = new BeanItemContainer<SelectValue>(SelectValue.class);
							if(value2.getValue().equals("OMP Other Expenses")){
								if(cmbDocumentsReceivedFrom!=null){
									cmbDocumentsReceivedFrom.setValue(null);
									cmbDocumentsReceivedFrom.setNullSelectionItemId(null);
								}else{
									calculationInstanceObj.dummyFieldHospital.setValue("dummy");
								}
								SelectValue selectValue1 = new SelectValue();
								selectValue1.setId(177L);
								selectValue1.setValue("INR");
								cmbCurrencyType.setValue(selectValue1);
								txtCurrencyRate.setValue("1");
								txtCurrencyRate.setEnabled(Boolean.FALSE);
								txtConversionValue.setEnabled(Boolean.FALSE);
								if(ompClaimCalcViewTableObj.classification!=null 
										&& !ompClaimCalcViewTableObj.classification.getValue().equalsIgnoreCase("")){
										ompClaimCalcViewTableObj.reset();
				
								}
								calculationInstanceObj.classification.setValue("Other Exp");
								ompClaimCalcViewTableObj.classification.setValue("Other Exp");
								referenceDataMapCategory.clear();
								fireViewEvent(OMPProcessClaimApproverWizardPresenter.OMP_APPROVER_PARTICULARS, referenceDataMapCategory, SHAConstants.OMPCLM_OTHR);
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
								enableDeductiblesRecovered();
								if(cmbCurrencyType!=null && cmbCurrencyType.getValue()!=null ){
									SelectValue currencyRateSelectValue = (SelectValue) cmbCurrencyType.getValue();
									if(currencyRateSelectValue.getValue().equalsIgnoreCase("USD")){
										txtCurrencyRate.setValue("1");
										txtCurrencyRate.setEnabled(false);
										txtConversionValue.setEnabled(Boolean.TRUE);
									}else if(currencyRateSelectValue.getValue().equalsIgnoreCase("INR")){
										txtCurrencyRate.setValue("1");
										txtConversionValue.setValue("1");
										txtCurrencyRate.setEnabled(Boolean.FALSE);
										txtConversionValue.setEnabled(Boolean.FALSE);
									}
									else{
										txtCurrencyRate.setValue("1");
										txtCurrencyRate.setEnabled(Boolean.TRUE);
										txtConversionValue.setEnabled(Boolean.TRUE);
									}
								}
								if(ompClaimCalcViewTableObj.classification!=null 
										&& !ompClaimCalcViewTableObj.classification.getValue().equalsIgnoreCase("")){
										ompClaimCalcViewTableObj.reset();
				
								}
								calculationInstanceObj.classification.setValue("OMP Claim Related");
								ompClaimCalcViewTableObj.classification.setValue("OMP Claim Related");
								referenceDataMapCategory.clear();
								fireViewEvent(OMPProcessClaimApproverWizardPresenter.OMP_APPROVER_PARTICULARS, referenceDataMapCategory, SHAConstants.OMPCLM_REL);
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
								if(cmbDocumentsReceivedFrom!=null){
									cmbDocumentsReceivedFrom.setValue(null);
									cmbDocumentsReceivedFrom.setNullSelectionItemId(null);
								}else{
									calculationInstanceObj.dummyFieldHospital.setValue("dummy");
								}
								SelectValue selectValue1 = new SelectValue();
								selectValue1.setId(48L);
								selectValue1.setValue("USD");
								cmbCurrencyType.setValue(selectValue1);
								txtCurrencyRate.setValue("1");
								txtCurrencyRate.setEnabled(Boolean.FALSE);
								txtConversionValue.setEnabled(Boolean.TRUE);
								if(ompClaimCalcViewTableObj.classification!=null 
										&& !ompClaimCalcViewTableObj.classification.getValue().equalsIgnoreCase("")){
										ompClaimCalcViewTableObj.reset();
				
								}
								calculationInstanceObj.classification.setValue("Negotiator Fee");
								ompClaimCalcViewTableObj.classification.setValue("Negotiator Fee");
								referenceDataMapCategory.clear();
								fireViewEvent(OMPProcessClaimApproverWizardPresenter.OMP_APPROVER_PARTICULARS, referenceDataMapCategory, SHAConstants.OMPCLM_NEGO);
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
				private void enableDeductiblesRecovered() {
					if(cmbDocumentsReceivedFrom!= null && cmbDocumentsReceivedFrom.getValue()!=null){
						SelectValue value = (SelectValue) cmbDocumentsReceivedFrom.getValue();
						if(value.getId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)){
							calculationInstanceObj.dummyFieldHospital.setValue(ReferenceTable.RECEIVED_FROM_HOSPITAL.toString());
						}else{
							calculationInstanceObj.dummyFieldHospital.setValue("dummy");
						}
					}
				}
			});
		}
	}
	
	private void addListenerForSubClassification()
	{
		if(cmbSubClassification != null) {
			cmbSubClassification.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 7455756225751111662L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					ComboBox classification = (ComboBox) event.getProperty();
					if(classification!=null && classification.getValue()!=null){
						SelectValue value2 = (SelectValue) classification.getValue();
						if(value2!=null){
							Boolean enableORdisableNegobutton = enableORdisableNegobutton();
							if(sendtoNegotiatorBtn!=null){
								sendtoNegotiatorBtn.setEnabled(enableORdisableNegobutton);
								if(!enableORdisableNegobutton){
									if(wholeLayout.getComponentCount()>6){
										wholeLayout.removeComponent(wholeLayout.getComponent(wholeLayout.getComponentCount() - 1));
									}
								}
							}
							}
						}
					
				}
			});
		}
	}
	
	private void addListenerForDocument()
	{
		if(cmbDocumentsReceivedFrom != null) {
			cmbDocumentsReceivedFrom.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 7455756225751111662L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					ComboBox classification = (ComboBox) event.getProperty();
					if(classification!=null && classification.getValue()!=null){
						SelectValue value2 = (SelectValue) classification.getValue();
						if(value2!=null){
							Boolean enableORdisableNegobutton = enableORdisableNegobutton();
							if(sendtoNegotiatorBtn!=null){
								sendtoNegotiatorBtn.setEnabled(enableORdisableNegobutton);
								if(!enableORdisableNegobutton){
									if(wholeLayout.getComponentCount()>6){
										wholeLayout.removeComponent(wholeLayout.getComponent(wholeLayout.getComponentCount() - 1));
									}
								}
							}
							if(classification.getValue()!=null){
								SelectValue value = (SelectValue) classification.getValue();
								if(value.getId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)){
									if(bean.getIsDocRecHospital()){
										showErrorMessage("ROD with document received from hospital is already exists");
										cmbDocumentsReceivedFrom.setValue(null);
									}else{
										calculationInstanceObj.dummyFieldHospital.setValue(ReferenceTable.RECEIVED_FROM_HOSPITAL.toString());
									}
								}else if(value.getId().equals(ReferenceTable.RECEIVED_FROM_INSURED)){
									if(bean.getIsDocRecInsured()){
										showErrorMessage("ROD with document received from insured is already exists");
										cmbDocumentsReceivedFrom.setValue(null);
									}else{
										calculationInstanceObj.dummyFieldHospital.setValue("dummy");
									}
								}else{
									calculationInstanceObj.dummyFieldHospital.setValue("dummy");
								}
							}else{
								calculationInstanceObj.dummyFieldHospital.setValue("dummy");
							}
							
							}
						}else if(classification!=null && classification.getValue()==null){
							calculationInstanceObj.dummyFieldHospital.setValue("dummy");
						}
					
				}
			});
		}
	}
	
	private Boolean enableORdisableNegobutton() {
		Boolean enable = Boolean.FALSE;
		Boolean claimType = Boolean.FALSE;
		Boolean hosipal = Boolean.FALSE;
		Boolean classification = Boolean.FALSE;
		Boolean subClassification = Boolean.FALSE;
		Boolean document = Boolean.FALSE;
		if(claimTypeOption!=null && claimTypeOption.getValue()!=null){
			Boolean hospital = (Boolean) claimTypeOption.getValue();
			if(hospital){
				 claimType = Boolean.TRUE;
			}
		}
		if(hospitalOption!=null && hospitalOption.getValue()!=null){
			Boolean hospital = (Boolean) hospitalOption.getValue();
			if(hospital){
				 hosipal = Boolean.TRUE;
			}
		}
		if(cmbClassification!=null && cmbClassification.getValue()!=null){
			SelectValue value2 = (SelectValue) cmbClassification.getValue();
			if(value2!=null){
				String value3 = value2.getValue();
				if(value3.equalsIgnoreCase("OMP Claim Related")){
					 classification = Boolean.TRUE;
				}
			}
		}
		if(cmbSubClassification!=null && cmbSubClassification.getValue()!=null){
			SelectValue value2 = (SelectValue) cmbSubClassification.getValue();
			if(value2!=null){
				String value3 = value2.getValue();
				if(value3.equalsIgnoreCase("OMP Claim Related")){
					subClassification = Boolean.TRUE;
				}
			}
		}
		if(cmbDocumentsReceivedFrom!=null && cmbDocumentsReceivedFrom.getValue()!=null){
			document = Boolean.TRUE;
			/*
			SelectValue value2 = (SelectValue) cmbDocumentsReceivedFrom.getValue();
			if(value2!=null){
				String value3 = value2.getValue();
				if(value3.equalsIgnoreCase("Hospital")){
					 document = Boolean.TRUE;
				}
			}
		*/}
		if(claimType && hosipal && classification && document && subClassification){
			return Boolean.TRUE;
		}
		return enable;
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
	
	
}//last

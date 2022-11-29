package com.shaic.claim.outpatient.registerclaim.wizard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;

import com.alert.util.ButtonOption;
import com.alert.util.ButtonType;
import com.alert.util.MessageBox;
import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.google.gwt.aria.client.SelectedValue;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.validation.ValidatorUtils;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.intimation.create.dto.HospitalDto;
import com.shaic.claim.outpatient.registerclaim.dto.OutPatientDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.dataextraction.ClaimRequestDataExtractionPagePresenter;
import com.shaic.domain.CityTownVillage;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Insured;
import com.shaic.domain.InsuredService;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.State;
import com.shaic.domain.UnFreezHospitals;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.data.validator.EmailValidator;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteEvents;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteQuery;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestion;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteTextField;
import eu.maxschuster.vaadin.autocompletetextfield.provider.CollectionSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.provider.MatchMode;
import eu.maxschuster.vaadin.autocompletetextfield.shared.ScrollBehavior;

public class OPRegisterClaimView extends ViewComponent{

	private static final long serialVersionUID = 84500210556813854L;
	
	private static final String stateCaption="State";
	private static final String cityCaption="City";
	private static final String hospitalNameCaption="Hospital Name";

	private OutPatientDTO bean;

	private BeanFieldGroup<OutPatientDTO> binder;

	private VerticalLayout opRegisterPageLayout;
	private Panel claimDetailsPanel;
//	private Panel hosDetailsPanel;

//	private ComboBox cmbClaimType;
	private ComboBox cmbTreatmentType;
	private ComboBox cmbInsuredPatientName;
	private ComboBox cmbModeOfReceipt;
	private PopupDateField checkupDate;
	private PopupDateField billReceivedDate;
	private TextField emailIdTxt;
	private TextField amountClaimedTxt;
//	private TextField provisionAmtTxt;
//	private ComboBox cmbDOPVisitReason;
//	private TextField OPVisitRemarks;
	private TextArea reasonforConsultation;
	private TextField hospitalPhone;
	private TextField hospitalDocName;
	private TextArea remarksForEmergencyAccident;
	private CheckBox chkEmergency;
	private CheckBox chkAccident;
	private TextField balanceSumInsuredtTxt;
	private ComboBox cmbConsultationType;
	
	FormLayout chxEmergencylayout;
	FormLayout chxAccidentlayout;
	HorizontalLayout chxLayout;
	FormLayout claimDetailslayout_1;
	FormLayout claimDetailslayout_2;
	VerticalLayout verticalLayout;
	HorizontalLayout fieldLayout;

	//	private ComboBox cmbState;
	//	private ComboBox cmbCity;
	//	private TextField hospitalName;

	/*private AutocompleteField<State> cmbState;
	private AutocompleteField<CityTownVillage> cmbCity;
	private AutocompleteField<HospitalDto> hospitalName;*/
	
//	private AutocompleteTextField cmbState;
//	private AutocompleteTextField cmbCity;
//	private AutocompleteTextField hospitalName;

//	private TextArea hospitalAddress;
//	private TextField hospitalPhone;
//	private TextField hospitalFaxNo;
//	private TextField hospitalDocName;

	private State selectedState;
	private CityTownVillage selectedCity;
	private HospitalDto selectedHospital;

	@Inject
	private MasterService masterService;

	@EJB
	private InsuredService insuredService;

	@EJB
	private HospitalService hospitalService;
	
	@EJB
	private DBCalculationService dbService;

	/*@Inject
	private Instance<DiganosisDetailsListenerForOP> diagnosisListnerTable;

	private DiganosisDetailsListenerForOP diagnosisListenerTableObj;*/

	private Map<String, Object> referenceData;
	
	private Integer anhHospitalCount;


	private StringBuilder errMsg = new StringBuilder();
	
	private ViewDetails viewDetails;
	
	public ViewDetails getViewDetails() {
		return viewDetails;
	}

	public void setViewDetails(ViewDetails viewDetails) {
		this.viewDetails = viewDetails;
	}

	public void init(OutPatientDTO bean) {
		this.bean = bean;
//		initBinder();
		opRegisterPageLayout = new VerticalLayout();
		claimDetailsPanel = new Panel();
//		hosDetailsPanel = new Panel();
		buildRegisterClaimLayout();
		setCompositionRoot(opRegisterPageLayout);
	}

	public void initBinder() {
		this.binder = new BeanFieldGroup<OutPatientDTO>(OutPatientDTO.class);
		
		BeanItem<OutPatientDTO> item = new BeanItem<OutPatientDTO>(bean);
		item.addNestedProperty("documentDetails");
		item.addNestedProperty("documentDetails.claimType");
		item.addNestedProperty("documentDetails.insuredPatientName");
		item.addNestedProperty("documentDetails.OPCheckupDate");
		item.addNestedProperty("documentDetails.billReceivedDate");
		item.addNestedProperty("documentDetails.amountClaimed");
		item.addNestedProperty("documentDetails.provisionAmt");
		item.addNestedProperty("documentDetails.reasonForOPVisit");
		item.addNestedProperty("documentDetails.remarksForOpVisit");
		this.binder.setItemDataSource(item);
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	@SuppressWarnings("deprecation")
	public void buildRegisterClaimLayout(){
		// Claim Details Panel
		cmbTreatmentType =  new ComboBox("Treatment Type <b style= 'color: red'>*</b>"); //binder.buildAndBind("Claim Type *", "documentDetails.claimType", ComboBox.class);
		cmbTreatmentType.setCaptionAsHtml(true);
		
		cmbInsuredPatientName =  new ComboBox("Insured Patient Name <b style= 'color: red'>*</b>"); //binder.buildAndBind("Insured Patient Name *", "documentDetails.insuredPatientName", ComboBox.class);
		cmbInsuredPatientName.setCaptionAsHtml(true);
		
		cmbModeOfReceipt =  new ComboBox("Mode Of Receipt <b style= 'color: red'>*</b>"); //binder.buildAndBind("Insured Patient Name *", "documentDetails.insuredPatientName", ComboBox.class);
		cmbModeOfReceipt.setCaptionAsHtml(true);
		
		checkupDate =  new PopupDateField("OP Consultation Date <b style= 'color: red'>*</b>"); //binder.buildAndBind("OP Check-up Date *", "documentDetails.OPCheckupDate", PopupDateField.class);
		checkupDate.setCaptionAsHtml(true);
		checkupDate.setDateFormat(("dd/MM/yyyy"));
		checkupDate.setRangeEnd(new Date());

		
		billReceivedDate =  new PopupDateField("Documents Received Date <b style= 'color: red'>*</b>"); //binder.buildAndBind("Bill Received Date *", "documentDetails.billReceivedDate", PopupDateField.class);
		billReceivedDate.setCaptionAsHtml(true);
		billReceivedDate.setDateFormat(("dd/MM/yyyy"));
		billReceivedDate.setRangeEnd(new Date());

		emailIdTxt = new TextField("Email Id"); //binder.buildAndBind("Amount Claimed *","documentDetails.amountClaimed",TextField.class);
//		emailIdTxt.addValidator(new EmailValidator("Invalid e-mail address {0}"));
		CSValidator emailIdTxtValidator = new CSValidator();
		emailIdTxtValidator.extend(emailIdTxt);
		emailIdTxtValidator.setRegExp("^[a-z A-Z 0-9 @ . _]*$");
		emailIdTxtValidator.setPreventInvalidTyping(true);
		
		amountClaimedTxt = new TextField("Amount Claimed <b style= 'color: red'>*</b>"); //binder.buildAndBind("Amount Claimed *","documentDetails.amountClaimed",TextField.class);
		amountClaimedTxt.setCaptionAsHtml(true);
		amountClaimedTxt.setConverter(Double.class);
		CSValidator amountClaimedTxtValidator = new CSValidator();
		amountClaimedTxtValidator.extend(amountClaimedTxt);
		amountClaimedTxtValidator.setRegExp("^[0-9]*$");
		amountClaimedTxtValidator.setPreventInvalidTyping(true);
		amountClaimedTxt.setNullRepresentation("");
		
		balanceSumInsuredtTxt = new TextField("Available OP SI"); //binder.buildAndBind("Amount Claimed *","documentDetails.amountClaimed",TextField.class);
		balanceSumInsuredtTxt.setCaptionAsHtml(true);
		balanceSumInsuredtTxt.setEnabled(false);
		CSValidator balanceSumInsuredtTxtValidator = new CSValidator();
		balanceSumInsuredtTxtValidator.extend(balanceSumInsuredtTxt);
		balanceSumInsuredtTxtValidator.setRegExp("^[0-9]*$");
		balanceSumInsuredtTxtValidator.setPreventInvalidTyping(true);
		
		/*provisionAmtTxt = new TextField("Provision Amount <b style= 'color: red'>*</b>"); //binder.buildAndBind("Provision Amount *","documentDetails.provisionAmt",TextField.class);
		provisionAmtTxt.setCaptionAsHtml(true);*/
		
		/*cmbDOPVisitReason =  new ComboBox("Reason for Consultation <b style= 'color: red'>*</b>"); //binder.buildAndBind("Reason for OP Check-up Visit *", "documentDetails.reasonForOPVisit", ComboBox.class);
		cmbDOPVisitReason.setCaptionAsHtml(true);*/
		
//		OPVisitRemarks = new TextField("Remarks for OP Check-up Visit"); //binder.buildAndBind("Remarks for OP Check-up Visit ","documentDetails.remarksForOpVisit",TextField.class);

		chkEmergency = new CheckBox("Emergency "); 
		
		chkAccident = new CheckBox("Accident "); 
		
		reasonforConsultation = new TextArea("Reason for Consultation <b style= 'color: red'>*</b>"); 
		reasonforConsultation.setCaptionAsHtml(true);
		CSValidator reasonforConsultationValidator = new CSValidator();
		reasonforConsultationValidator.extend(reasonforConsultation);
		reasonforConsultationValidator.setRegExp("^[a-zA-Z 0-9 . , ' -]*$");
		reasonforConsultationValidator.setPreventInvalidTyping(true);
		
		remarksForEmergencyAccident = new TextArea("Remarks for Emergency/Accident <b style= 'color: red'>*</b>"); 
		remarksForEmergencyAccident.setCaptionAsHtml(true); 
		CSValidator remarksForEmergencyAccidentValidator = new CSValidator();
		remarksForEmergencyAccidentValidator.extend(remarksForEmergencyAccident);
		remarksForEmergencyAccidentValidator.setRegExp("^[a-zA-Z 0-9 . , ' -]*$");
		remarksForEmergencyAccidentValidator.setPreventInvalidTyping(true);
		
		hospitalDocName =  new TextField("Name (Doc. Submitted Name) <b style= 'color: red'>*</b>"); 
		hospitalDocName.setCaptionAsHtml(true);
		CSValidator doctorNameValidator = new CSValidator();
		doctorNameValidator.extend(hospitalDocName);
		doctorNameValidator.setRegExp("^[a-zA-Z . ]*$");
		doctorNameValidator.setPreventInvalidTyping(true);
		
		hospitalPhone = new TextField("Contact No (Doc. Submitted Name) <b style= 'color: red'>*</b>"); 
		hospitalPhone.setCaptionAsHtml(true);
		hospitalPhone.setMaxLength(12);
		CSValidator hospitalPhoneValidator = new CSValidator();
		hospitalPhoneValidator.extend(hospitalPhone);
		hospitalPhoneValidator.setRegExp("^[0-9]*$");
		hospitalPhoneValidator.setPreventInvalidTyping(true);
		

		cmbConsultationType = new ComboBox("Cover Section <b style = 'color: red'>*</b>");
		cmbConsultationType.setCaptionAsHtml(true);
		


		claimDetailsPanel.setCaption("Claim Details");
		
		chxEmergencylayout = new FormLayout(chkEmergency);
		chxEmergencylayout.setSpacing(false);
		chxAccidentlayout = new FormLayout(chkAccident);
		chxAccidentlayout.setSpacing(false);

		chxLayout = new HorizontalLayout(chxEmergencylayout, chxAccidentlayout);
		chxLayout.setSpacing(false);
		
		claimDetailslayout_1 = new FormLayout(cmbInsuredPatientName, checkupDate, billReceivedDate, cmbModeOfReceipt, hospitalPhone, reasonforConsultation);
		claimDetailslayout_2 = new FormLayout(balanceSumInsuredtTxt, cmbTreatmentType,cmbConsultationType, hospitalDocName, emailIdTxt, amountClaimedTxt);
		claimDetailslayout_2.setMargin(false);

		verticalLayout = new VerticalLayout(chxLayout, claimDetailslayout_2);
		verticalLayout.setSpacing(false);
		
		fieldLayout = new HorizontalLayout(claimDetailslayout_1, verticalLayout);

		fieldLayout.setSpacing(true);
		fieldLayout.setMargin(true);
		fieldLayout.setWidth("100%");

		claimDetailsPanel.setContent(fieldLayout);
		
		addListener();

		// Hospital Details Panel

		//		private ComboBox cmbState;
		//		private ComboBox cmbCity;
		//		private TextField hospitalName;
		//		private TextField hospitalAddress;
		//		private TextField hospitalPhone;
		//		private TextField hospitalFaxNo;
		//		private TextField hospitalDocName;

		/*cmbState = new AutocompleteTextField();
		cmbState.setCaption("State");
		cmbState.setWidth("-1px");		
		//Vaadin8-setImmediate() cmbState.setImmediate(true);
		cmbState.setRequiredError("Please select a valid state");
		cmbState.setValidationVisible(false);
		cmbState.setMinimumQueryCharacters(2);

		cmbCity = new AutocompleteTextField();
		//Vaadin8-setImmediate() cmbCity.setImmediate(true);
		cmbCity.setCaption("City");
		cmbCity.setWidth("-1px");
		//Vaadin8-setImmediate() cmbCity.setImmediate(true);
		cmbCity.setRequiredError("Please select a valid city");
		cmbCity.setValidationVisible(false);
		cmbCity.setMinimumQueryCharacters(2);*/
		
		AutocompleteSuggestionProvider suggestionProvider = new CustomSuggestionProvider();

		
		/*cmbState = new AutocompleteTextField();		
		cmbState.setCaption(stateCaption);		
		cmbState.setCaptionAsHtml(true);		
		cmbState.setItemAsHtml(false);		
		cmbState.setMinChars(2);		

		cmbState.setScrollBehavior(ScrollBehavior.NONE);		
		cmbState.setSuggestionLimit(0);		
		//cmbStates.setSizeFull();		
		cmbState.setRequiredIndicatorVisible(true);		
		cmbState.setSuggestionProvider(suggestionProvider);		
		cmbState.addSelectListener(this::onAutocompleteStateSelect);
		
		cmbCity = new AutocompleteTextField();		
		cmbCity.setCaption(cityCaption);		
		cmbCity.setCaptionAsHtml(true);		
		cmbCity.setItemAsHtml(false);		
		cmbCity.setMinChars(2);		
		cmbCity.setScrollBehavior(ScrollBehavior.NONE);		
		cmbCity.setSuggestionLimit(0);		

		cmbCity.setSuggestionProvider(suggestionProvider);		
		cmbCity.addSelectListener(this::onAutocompleteCitySelect);
		
		hospitalName = new AutocompleteTextField();		
		hospitalName.setCaption(hospitalNameCaption);		
		hospitalName.setCaptionAsHtml(true);		
		hospitalName.setItemAsHtml(false);		
		hospitalName.setMinChars(2);		
		hospitalName.setScrollBehavior(ScrollBehavior.NONE);		
		hospitalName.setSuggestionLimit(0);		

		hospitalName.setSuggestionProvider(suggestionProvider);		
		hospitalName.addSelectListener(this::onAutocompleteHospitalSelect);		
		hospitalName.setRequiredIndicatorVisible(true);*/

		/*hospitalName = new AutocompleteTextField();
		//Vaadin8-setImmediate() hospitalName.setImmediate(true);
		hospitalName.setRequiredError("Please select a valid Hospital");
		hospitalName.setWidth("-1px");
		hospitalName.setValidationVisible(false);
		hospitalName.setCaption("Hospital Name");
		hospitalName.setRequired(true);
		hospitalName.setRequiredError("Hosptial Name is mandatory please select hosptial Name");*/


		/*hospitalAddress = new TextArea("Hospital Address");
		//		hospitalAddress.setWidth("-1px");

		hospitalPhone = new TextField("Hospital Phone");
		//		hospitalPhone.setWidth("-1px");

		hospitalFaxNo = new TextField("Hospital Fax No");
		//		hospitalFaxNo.setWidth("-1px");

		hospitalDocName =  new TextField("Hospital Doctor Name");
		//		hospitalDocName.setWidth("-1px");


//		hosDetailsPanel.setCaption("Hospital Details");

		FormLayout hosDetailslayout_1 = new FormLayout(cmbState, hospitalName, hospitalPhone, hospitalFaxNo);
		FormLayout hosDetailslayout_2 = new FormLayout(cmbCity, hospitalAddress, hospitalDocName);

		HorizontalLayout field2Layout = new HorizontalLayout(hosDetailslayout_1, hosDetailslayout_2);

		field2Layout.setSpacing(true);
		field2Layout.setMargin(true);
		field2Layout.setWidth("100%");*/

//		hosDetailsPanel.setContent(field2Layout);

		opRegisterPageLayout.addComponent(claimDetailsPanel);
//		opRegisterPageLayout.addComponent(hosDetailsPanel);

		/*setUpAutoState(cmbState);
		setUpAutoCity(cmbCity);
		setUpAutoHospital(hospitalName);*/

		//addListeners();
		setDropDownValues();		

		/*this.diagnosisListenerTableObj = diagnosisListnerTable.get();
		this.diagnosisListenerTableObj.init("diagnosisDetailsOP"); //new PreauthDTO(), 
*/
		referenceData = new HashMap<String, Object>();

		referenceData.put("icdChapter", masterService.getSelectValuesForICDChapter());
		referenceData.put("icdBlock", masterService.getSelectValuesForICDBlock());
		referenceData.put("icdCode", masterService.getSelectValuesForICDCode());

		/*this.diagnosisListenerTableObj.setReferenceData(referenceData);


		opRegisterPageLayout.addComponent(diagnosisListenerTableObj);*/

		opRegisterPageLayout.addComponent(addFooterButtons());
	}

	@SuppressWarnings("deprecation")
	public void setDropDownValues(){
		/*BeanItemContainer<SelectValue> claimTypes = masterService.getOPClaimTypeSelectValueContainer(ReferenceTable.CLAIM_TYPE);
		cmbClaimType.setContainerDataSource(claimTypes);
		cmbClaimType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbClaimType.setItemCaptionPropertyId("value");
		cmbClaimType.setValue(claimTypes.getItemIds().get(1));
		cmbClaimType.setEnabled(false);*/
		/*SelectValue clmType = null ;
		if(bean.getOutpatientFlag()){
			clmType = (SelectValue) cmbClaimType.getContainerDataSource().getItemIds().toArray()[1];
		}else{
			clmType = (SelectValue) cmbClaimType.getContainerDataSource().getItemIds().toArray()[0];
		}
		cmbClaimType.setValue(clmType);*/
		//cmbClaimType.setReadOnly(true);

		BeanItemContainer<Insured> insuredList = insuredService.getInsuredByPolicyNoForOP(bean.getPolicyDto().getPolicyNumber());
		cmbInsuredPatientName.setContainerDataSource(insuredList);
		cmbInsuredPatientName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbInsuredPatientName.setItemCaptionPropertyId("insuredName");
		
		BeanItemContainer<SelectValue> receiptMode = masterService.getMastersValuebyTypeCodeOnStaatus(ReferenceTable.OP_RECMODE);
		cmbModeOfReceipt.setContainerDataSource(receiptMode);
		cmbModeOfReceipt.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbModeOfReceipt.setItemCaptionPropertyId("value");
		cmbModeOfReceipt.setValue(receiptMode);
		
		BeanItemContainer<SelectValue> treatmentType = masterService.getMastersValuebyTypeCodeOnStaatus(ReferenceTable.OP_TRTMNT_TYPE);
		cmbTreatmentType.setContainerDataSource(treatmentType);
		cmbTreatmentType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbTreatmentType.setItemCaptionPropertyId("value");
		cmbTreatmentType.setValue(treatmentType);
		
		BeanItemContainer<SelectValue> consultationType = masterService.getConsulationDetails(bean.getPolicyDto().getProduct().getKey());
		cmbConsultationType.setContainerDataSource(consultationType);
		cmbConsultationType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbConsultationType.setItemCaptionPropertyId("value");
		if(consultationType !=null && consultationType.size() > 1){
			cmbConsultationType.setValue(consultationType);
		} else {
			List<SelectValue> secDtls = consultationType.getItemIds();
			cmbConsultationType.setValue(secDtls.get(0));
			cmbConsultationType.setEnabled(false);
		}

		/*BeanItemContainer<SelectValue> reasonTypes = masterService.getOPReason();
		cmbDOPVisitReason.setContainerDataSource(reasonTypes);
		cmbDOPVisitReason.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbDOPVisitReason.setItemCaptionPropertyId("value");*/

		//		BeanItemContainer<SelectValue> lumenStatusTypes = lumenService.getLevelOneStatus();
		//		cmbStatus.setContainerDataSource(lumenStatusTypes);
		//		cmbStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		//		cmbStatus.setItemCaptionPropertyId("value");
	}

	/*public void addListeners(){
		cmbState.setSuggestionPickedListener(new AutocompleteSuggestionPickedListener<State>() {
			@Override
			public void onSuggestionPicked(State state) {
				if (state != null && cmbState.getText() != null
						&& !cmbState.getText().equals("")) {
					handleStateSelection(state);
				} else {
					Notification.show("Please Select a Valid State");
				}

				cmbCity.setValue("");
				cmbCity.setValidationVisible(false);
				hospitalName.setText("");
				hospitalName.setValue(null);
				hospitalName.setValidationVisible(false);
				selectedCity = null;
				selectedHospital = null;
				clearHospitalDetails();

			}
		});

		cmbCity.setSuggestionPickedListener(new AutocompleteSuggestionPickedListener<CityTownVillage>() {
			@Override
			public void onSuggestionPicked(CityTownVillage city) {
				if (city != null && cmbCity.getText() != null
						&& !cmbCity.getText().equals("")) {
					handleCitySelection(city);
				} else {
					Notification.show("Please Select a Valid City");
				}

				hospitalName.setText("");
				hospitalName.setValue(null);
				hospitalName.setValidationVisible(false);
				selectedHospital = null;
				clearHospitalDetails();
			}
		});
		
		

		hospitalName.setSuggestionPickedListener(new AutocompleteSuggestionPickedListener<HospitalDto>() {
			@Override
			public void onSuggestionPicked(HospitalDto suggestion) {
				if (suggestion != null) {
					clearHospitalDetails();
					selectedHospital = suggestion;
					hospitalAddress.setReadOnly(false);
					hospitalAddress.setValue(suggestion.getAddress());
					hospitalAddress.setReadOnly(true);
					
					hospitalPhone.setReadOnly(false);
					hospitalPhone.setValue(suggestion.getPhoneNumber());
					hospitalPhone.setReadOnly(true);
					
					hospitalFaxNo.setReadOnly(false);
					hospitalFaxNo.setValue(suggestion.getFax());
					hospitalFaxNo.setReadOnly(true);
					
					hospitalDocName.setReadOnly(false);
					hospitalDocName.setValue(suggestion.getName());
					hospitalDocName.setReadOnly(true);
				} else {
					Notification.show("Please Select a Hospital Name or Enter Hospital Details");
				}
			}
		});
		
		cmbInsuredPatientName.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -7112401803518306056L;
			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				Insured insuredObject = (Insured)event.getProperty().getValue();
				getViewDetails().setInsuredSelected(insuredObject);
			}
		});
	}*/

	protected void handleStateSelection(State state) {
		selectedState = state;
	}

	protected void handleCitySelection(CityTownVillage city) {
		selectedCity = city;
	}

	public void clearHospitalDetails() {
		/*hospitalAddress.setReadOnly(false);
		hospitalAddress.setValue("");
		hospitalAddress.setReadOnly(true);*/
		
		hospitalPhone.setReadOnly(false);
		hospitalPhone.setValue("");
		hospitalPhone.setReadOnly(true);
		
		/*hospitalFaxNo.setReadOnly(false);
		hospitalFaxNo.setValue("");
		hospitalFaxNo.setReadOnly(true);*/
		
		hospitalDocName.setReadOnly(false);
		hospitalDocName.setValue("");
		hospitalDocName.setReadOnly(true);
	}

	/*private void setUpAutoState(AutocompleteField<State> search) {
		search.setQueryListener(new AutocompleteQueryListener<State>() {
			@Override
			public void handleUserQuery(AutocompleteField<State> field,
					String query) {
				selectedState = null;
				selectedCity = null;
				handleStateSearchQuery(field, query);

			}
		});
	}

	private void setUpAutoCity(AutocompleteField<CityTownVillage> search) {
		search.setQueryListener(new AutocompleteQueryListener<CityTownVillage>() {
			@Override
			public void handleUserQuery(
					AutocompleteField<CityTownVillage> field, String query) {
				if (cmbState.getText() != null
						&& !cmbState.getText().equals("")
						&& !ValidatorUtils.isNull(selectedState)) {
					handleCitySearchQuery(field, query);
				} else {
					Notification.show("Please Select a State");
				}
			}
		});
	}*/

	private void handleStateSearchQuery(Collection<AutocompleteSuggestion> suggestions, AutocompleteQuery query) {

		List<State> stateSearch = masterService
				.stateSearch(query.getTerm().toLowerCase());
		

		if (stateSearch != null && !stateSearch.isEmpty()) {
			for (State state : stateSearch) {
				AutocompleteSuggestion suggestioner=new AutocompleteSuggestion(state.getValue());
				suggestioner.setData(state);
				suggestions.add(suggestioner);
			}
		} else {
			Notification.show("Please Select Valid State");
			selectedState = null;
			selectedCity = null;
		}
		selectedCity = null;
		/*cmbCity.setValue("");
		cmbCity.setData(null);
		hospitalName.setValue("");
		hospitalName.setData(null);*/
		selectedHospital = null;
		clearHospitalDetails();
		
	}

	private void handleCitySearchQuery(
			Collection<AutocompleteSuggestion> suggestions, AutocompleteQuery query) {
		if (selectedState == null) {
			Notification.show("Please Select Valid State");
		} else {
			List<CityTownVillage> citySearch = masterService.citySearch(
					query.getTerm().toLowerCase(), selectedState);

			if (citySearch != null && !citySearch.isEmpty()) {
				for (CityTownVillage city : citySearch) {
					AutocompleteSuggestion suggestioner=new AutocompleteSuggestion(city.getValue());	
					suggestioner.setData(city);		
					suggestions.add(suggestioner);
					//field.addSuggestion(city, city.getValue());
				}
			} else {
				Notification.show("Please Select Valid City");
				selectedCity = null;
			}

			/*hospitalName.setData(null);
			hospitalName.setValue("");*/
			selectedHospital = null;
			clearHospitalDetails();
		}
	}

	/*private void setUpAutoHospital(AutocompleteField<HospitalDto> search) {
		search.setQueryListener(new AutocompleteQueryListener<HospitalDto>() {
			@Override
			public void handleUserQuery(AutocompleteField<HospitalDto> field, String query) {
				if (cmbState.getText() != null
						&& !cmbState.getText().equals("")
						&& !ValidatorUtils.isNull(selectedState)) {
					handleHospitalSearchQuery(field, query);
				} else {
					Notification.show("State and City both are mandatory for Hospital Selection, Please Select State and City");
				}
			}
		});
	}*/
	private void handleHospitalSearchQuery(
			Collection<AutocompleteSuggestion> suggestions, AutocompleteQuery query) {
		List<UnFreezHospitals> hospitalSearch = null;
	
			hospitalSearch = hospitalService
					.UnFreezHospitalNameCriteriaSearch(query.getTerm(), selectedState, selectedCity);
		

		if (!hospitalSearch.isEmpty()) {
			anhHospitalCount = hospitalService.getANHHospitalCountCityWise(
					selectedState, selectedCity);
			anhHospitalCount = anhHospitalCount != null && anhHospitalCount > 0 ? anhHospitalCount - 1
					: 0;
			List<HospitalDto> hospitalDtoList = new ArrayList<HospitalDto>();
			for (UnFreezHospitals hospital : hospitalSearch) {

				HospitalDto resultHospitalDto = new HospitalDto(hospital);
				hospitalDtoList.add(resultHospitalDto);
			}
			
//			field.clearChoices();
			suggestions.clear();

			for (HospitalDto hospitalDto : hospitalDtoList) {
				/*field.addSuggestion(hospitalDto, hospitalDto.getName()+","+(hospitalDto.getAddress() != null ? hospitalDto.getAddress() : "")
						+","+(hospitalDto.getState() != null ? hospitalDto.getState() : "") +","+(hospitalDto.getCity() != null ? hospitalDto.getCity() : "")  +","+(hospitalDto.getPincode() != null ? hospitalDto.getPincode() : "")
						+","+hospitalDto.getHospitalTypeValue());*/
				AutocompleteSuggestion suggestioner=new AutocompleteSuggestion(hospitalDto.getName()+","+(hospitalDto.getAddress() != null ? hospitalDto.getAddress() : "")
						+","+(hospitalDto.getState() != null ? hospitalDto.getState() : "") +","+(hospitalDto.getCity() != null ? hospitalDto.getCity() : "")  +","+(hospitalDto.getPincode() != null ? hospitalDto.getPincode() : "")
						+","+hospitalDto.getHospitalTypeValue());
				suggestioner.setData(hospitalDto);
				suggestions.add(suggestioner);
			}
			
		} else {
			
			clearHospitalDetails();
		
		}
	}
	
	@SuppressWarnings("deprecation")
	protected void addListener() {
		
		chkEmergency.addValueChangeListener(new Property.ValueChangeListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						Boolean emergencyCheckValue = chkEmergency.getValue();
						Boolean accidentCheckValue = chkAccident.getValue();

						if (emergencyCheckValue.equals(Boolean.TRUE) || accidentCheckValue.equals(Boolean.TRUE)) {
							claimDetailslayout_2.addComponentAsFirst(remarksForEmergencyAccident);
						}else {
							claimDetailslayout_2.removeComponent(remarksForEmergencyAccident);
						}
						
					}
				});
		
		chkAccident.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Boolean emergencyCheckValue = chkEmergency.getValue();
				Boolean accidentCheckValue = chkAccident.getValue();

				if (emergencyCheckValue.equals(Boolean.TRUE) || accidentCheckValue.equals(Boolean.TRUE)) {
					claimDetailslayout_2.addComponentAsFirst(remarksForEmergencyAccident);
				}else {
					claimDetailslayout_2.removeComponent(remarksForEmergencyAccident);
				}
				
			}
		});
		
		cmbInsuredPatientName.addValueChangeListener(new Property.ValueChangeListener() {

			public void valueChange(ValueChangeEvent valueChangeEvent) {
				Insured insuredName = (Insured) valueChangeEvent
						.getProperty().getValue();
				if(insuredName != null & cmbConsultationType.getValue() != null){
					bean.getDocumentDetails().setInsuredPatientName(insuredName);
					bean.getDocumentDetails().setConsultationType((SelectValue)cmbConsultationType.getValue());
					fireViewEvent(OPRegisterClaimWizardPresenter.OP_BALANCE_SUM_INSURED_EVENT, bean);
					SelectValue seletedVlaue = (SelectValue)cmbConsultationType.getValue();
					if(seletedVlaue != null && (seletedVlaue.getId().equals(ReferenceTable.OUT_PATIENT_BENEFIT_COVER)
							|| seletedVlaue.getId().equals(ReferenceTable.OUT_PATIENT_BENEFIT_VACINATION_COVER))){
						System.out.println(String.format("OP Available SI amount [%s]", bean.getAvailableSI()));
						alertMessageForAvailableOPSILimit();
					}
					
				}
				
				/*if(insuredName != null && insuredName.getInsuredGender() != null){
					MastersValue mastersValue = insuredName.getInsuredGender();
					if(mastersValue != null && !mastersValue.getKey().equals(ReferenceTable.FEMALE_GENDER)){
						System.out.println("Insured Patient Name is Not FEMALE");
						alertMessageForInsuredNameNotChoosenFemale();
						alertMessageForAvailableOPSILimit();
					}
				}*/
			}
		});
		
		cmbConsultationType.addValueChangeListener(new Property.ValueChangeListener() {

			public void valueChange(ValueChangeEvent valueChangeEvent) {
				SelectValue consulatationType = (SelectValue) valueChangeEvent
						.getProperty().getValue();
				if(consulatationType != null){
					
//					bean.getDocumentDetails().setInsuredPatientName(insuredName);
					if(cmbInsuredPatientName.getValue() != null){
						bean.getDocumentDetails().setInsuredPatientName((Insured)cmbInsuredPatientName.getValue());
						bean.getDocumentDetails().setConsultationType(consulatationType);
						fireViewEvent(OPRegisterClaimWizardPresenter.OP_BALANCE_SUM_INSURED_EVENT, bean);
						
						SelectValue seletedVlaue = (SelectValue)cmbConsultationType.getValue();
						if(seletedVlaue != null && seletedVlaue.getId().equals(ReferenceTable.DENTAL_AND_OPTHALMIC_TREATMENT)){
							alertMessageForDentalAndOpthalmic();
						}
						if(seletedVlaue != null && (seletedVlaue.getId().equals(ReferenceTable.OUT_PATIENT_BENEFIT_COVER)
								|| seletedVlaue.getId().equals(ReferenceTable.OUT_PATIENT_BENEFIT_VACINATION_COVER))){
							System.out.println(String.format("OP Available SI amount [%s]", bean.getAvailableSI()));
							alertMessageForAvailableOPSILimit();
						}
						if(seletedVlaue != null && seletedVlaue.getId().equals(ReferenceTable.OUT_PATIENT_VACCINATION_BENEFIT)){
							alertMessageForAvailableOPSILimit();
							alertMessageVaccinationBenefit();
							alertMessageForCoverSectionVaccinationBenefit();
							
						}
						if((bean.getPolicyDto().getProduct().getKey().equals(ReferenceTable.STAR_DIABETIC_SAFE_FLOATER_REVISED)
								|| bean.getPolicyDto().getProduct().getKey().equals(ReferenceTable.STAR_DIABETIC_SAFE_INDIVIDUAL_REVISED)) 
								&& seletedVlaue != null && seletedVlaue.getId().equals(ReferenceTable.HbA1C_TREATMENT)){
							SHAUtils.showMessageBoxWithCaption(SHAConstants.HbA1C_ALERT_MSG, "INFORMATION");
							alertMessageForAvailableOPSILimitHbA1C();
						}
						/*if(bean != null && bean.getPolicyDto() != null && bean.getPolicyDto().getProduct() != null && bean.getPolicyDto().getProduct().getKey() != null &&
								(bean.getPolicyDto().getProduct().getKey().equals(ReferenceTable.COMPREHENSIVE_78_PRODUCT_FLOATER)
										|| bean.getPolicyDto().getProduct().getKey().equals(ReferenceTable.COMPREHENSIVE_78_PRODUCT_INDIVIDUAL)) &&
								seletedVlaue != null && seletedVlaue.getId().equals(ReferenceTable.OUT_PATIENT_VACCINATION_BENEFIT)
								&& bean != null && bean.getDocumentDetails() != null && bean.getDocumentDetails().getInsuredPatientName() != null
							    && bean.getDocumentDetails().getInsuredPatientName().getInsuredAge() != null 
							    && (bean.getDocumentDetails().getInsuredPatientName().getInsuredAge() > ReferenceTable.OUT_PATIENT_AGE_LIMIT)
								&& !bean.getDocumentDetails().getInsuredPatientName().getInsuredGender().getKey().equals(ReferenceTable.FEMALE_GENDER)){
							//alertMessageForInsuredNameNotChoosenFemale();
							showErrorMessage("Insured Patient Name should be female - Mother’s Name to be mentioned");
						}*/
					}
				}
			}
		});
		
	}


	@SuppressWarnings("serial")
	public AbsoluteLayout addFooterButtons(){
		HorizontalLayout buttonsLayout = new HorizontalLayout();

		Button	cancelButton = new Button("Cancel");
		cancelButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(OPRegisterClaimWizardPresenter.OP_CANCEL_EVENT,null);
			}
		});

		Button	submitButton = new Button("Register");
		submitButton.addClickListener(new Button.ClickListener() 	{
			@Override
			public void buttonClick(ClickEvent event) {
				try {
					if(!validatePage()){
						bean.getDocumentDetails().setTreatmentType((SelectValue)cmbTreatmentType.getValue());
						bean.getDocumentDetails().setInsuredPatientName((Insured)cmbInsuredPatientName.getValue());
						bean.getDocumentDetails().setModeOfReceipt((SelectValue)cmbModeOfReceipt.getValue());
						bean.getDocumentDetails().setOPCheckupDate(checkupDate.getValue());
						bean.getDocumentDetails().setBillReceivedDate(billReceivedDate.getValue());
						bean.getDocumentDetails().setDocEmailId(emailIdTxt.getValue());
						bean.getDocumentDetails().setAmountClaimed(Double.valueOf(SHAUtils.getIntegerFromStringWithComma(amountClaimedTxt.getValue().toString())));
						bean.getDocumentDetails().setReasonforConsultation(reasonforConsultation.getValue());
						bean.getDocumentDetails().setDocSubmittedContactNo(Long.parseLong(hospitalPhone.getValue()));
						bean.getDocumentDetails().setDocSubmittedName(hospitalDocName.getValue());
						bean.getDocumentDetails().setRemarksForEmergencyAccident(remarksForEmergencyAccident.getValue());
						bean.getDocumentDetails().setEmergencyFlag(chkEmergency.getValue().toString());
						bean.getDocumentDetails().setAccidentFlag(chkAccident.getValue().toString());
						bean.getDocumentDetails().setConsultationType((SelectValue)cmbConsultationType.getValue());
//						bean.getDocumentDetails().setProvisionAmt(Double.parseDouble(provisionAmtTxt.getValue()));
//						bean.getDocumentDetails().setReasonForOPVisit((SelectValue)cmbDOPVisitReason.getValue());
//						bean.getDocumentDetails().setRemarksForOpVisit(OPVisitRemarks.getValue());
						
//						bean.setSelectedHospital(selectedHospital);
//						bean.setDiagnosisListenerTableList(diagnosisListenerTableObj.getValues());
						fireViewEvent(OPRegisterClaimWizardPresenter.OP_SUBMITTED_EVENT, bean);
					}else{
						showErrorMessage(errMsg.toString());
					}	
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		buttonsLayout.addComponents(submitButton,cancelButton);
		buttonsLayout.setSpacing(true);

		AbsoluteLayout submit_layout =  new AbsoluteLayout();
		submit_layout.addComponent(buttonsLayout, "left: 40%; top: 20%;");
		submit_layout.setWidth("100%");
		submit_layout.setHeight("50px");

		return submit_layout;
	}
	
	public void cancelIntimation(){/*
		ConfirmDialog dialog = ConfirmDialog.show(getUI(), "Confirmation",
				"Are you sure you want to cancel ?",
				"No", "Yes", new ConfirmDialog.Listener() {
					private static final long serialVersionUID = 1L;
					public void onClose(ConfirmDialog dialog) {
						if (dialog.isCanceled() && !dialog.isConfirmed()) {
							// YES
							fireViewEvent(MenuItemBean.REGISTER_CLAIM_OP, null);
						}
					}
				});
		
		dialog.setStyleName(Reindeer.WINDOW_BLACK);
	*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "Yes");
		buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "No");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createConfirmationbox("Are you sure you want to cancel ?", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES.toString());
		Button cancelButton = messageBoxButtons.get(GalaxyButtonTypesEnum.NO.toString());
		homeButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				fireViewEvent(MenuItemBean.REGISTER_CLAIM_OP, null);
			}
		});	
	}

	@SuppressWarnings("deprecation")
	private boolean validatePage() {
		Boolean hasError = false;
		errMsg.setLength(0);
		if(cmbTreatmentType.getValue() == null){
			hasError = true;
			errMsg.append("Please select Treatment Type </br>");
		}
		if(cmbInsuredPatientName.getValue() == null){
			hasError = true;
			errMsg.append("Please select any Insured Patient Name </br>");
		}
		
		// commented jira GALAXYMAIN-14988
		/*Date opDate = checkupDate.getValue();
		Date policyFromDate = bean.getPolicy().getPolicyFromDate();
		Date policyToDate = bean.getPolicy().getPolicyToDate();*/
		
		Date opDate = SHAUtils.removeTime(checkupDate.getValue());
		Date policyFromDate = SHAUtils.removeTime(bean.getPolicy().getPolicyFromDate());
		Date policyToDate = SHAUtils.removeTime(bean.getPolicy().getPolicyToDate());

		if(opDate == null){
			hasError = true;
			errMsg.append("Please select OP Consultation Date </br>");
		}

		if(opDate != null && (opDate.after(policyToDate) || opDate.before(policyFromDate))){
			hasError = true;
			errMsg.append("OP Checkup date must be between the policy start date and policy end date </br>");
		}
		
		if(cmbModeOfReceipt.getValue() == null){
			hasError = true;
			errMsg.append("Please select any Mode of Receipt </br>");
		}
		
		if(cmbConsultationType.getValue() == null){
			hasError = true;
			errMsg.append("Please select Consultation Type </br>");
		}

		if(StringUtils.isBlank(hospitalPhone.getValue())){
			hasError = true;
			errMsg.append("Please enter Contact No (Doc. Submitted Name) </br>");
		}

		if(StringUtils.isBlank(hospitalDocName.getValue())){
			hasError = true;
			errMsg.append("Please enter Name (Doc. Submitted Name) </br>");
		}

		if(StringUtils.isBlank(reasonforConsultation.getValue())){
			hasError = true;
			errMsg.append("Please enter Reason for Consultation </br>");
		}

		if((chkEmergency.getValue().equals(Boolean.TRUE) || chkAccident.getValue().equals(Boolean.TRUE)) 
				&& StringUtils.isBlank(remarksForEmergencyAccident.getValue())){
			hasError = true;
			errMsg.append("Please enter Remarks for Emergency/Accident </br>");
		}

		if(billReceivedDate.getValue() == null){
			hasError = true;
			errMsg.append("Please select Documents Received Date </br>");
		}

		if(StringUtils.isBlank(amountClaimedTxt.getValue())){
			hasError = true;
			errMsg.append("Please fill the Amount Claimed </br>");
		}
		
	
		if(null != this.emailIdTxt && null != this.emailIdTxt.getValue() && !("").equalsIgnoreCase(this.emailIdTxt.getValue()))
		{
			if(!isValidEmail(this.emailIdTxt.getValue()))
			{
				hasError = true;
				errMsg.append("Please enter a valid email </br>");
			}
		}
		if(cmbConsultationType != null && cmbConsultationType.getValue() != null){
			SelectValue selectValue = (SelectValue)cmbConsultationType.getValue();
			/*if(selectValue != null ){
				if(bean != null && bean.getPolicyDto() != null && bean.getPolicyDto().getProduct() != null && bean.getPolicyDto().getProduct().getKey() != null &&
						(bean.getPolicyDto().getProduct().getKey().equals(ReferenceTable.COMPREHENSIVE_78_PRODUCT_FLOATER)
								|| bean.getPolicyDto().getProduct().getKey().equals(ReferenceTable.COMPREHENSIVE_78_PRODUCT_INDIVIDUAL)) &&
								selectValue.getId().equals(ReferenceTable.OUT_PATIENT_VACCINATION_BENEFIT)){
					if(bean != null && bean.getDocumentDetails() != null && bean.getDocumentDetails().getInsuredPatientName() != null
							&& bean.getDocumentDetails().getInsuredPatientName().getInsuredGender() != null 
							&& (bean.getDocumentDetails().getInsuredPatientName().getInsuredAge() > ReferenceTable.OUT_PATIENT_AGE_LIMIT)
							&& !bean.getDocumentDetails().getInsuredPatientName().getInsuredGender().equals(ReferenceTable.FEMALE_GENDER)){
						hasError = true;
						errMsg.append("Insured Patient Name should be female - Mother’s Name to be mentioned </br>");
					} 
				}
			}*/
		}
		/*if(cmbConsultationType != null && 
				cmbConsultationType.getValue().equals(ReferenceTable.OUT_PATIENT_VACCINATION_BENEFIT_VALUE)
				&& bean != null && bean.getDocumentDetails() != null && bean.getDocumentDetails().getInsuredPatientName() != null
			    && bean.getDocumentDetails().getInsuredPatientName().getInsuredGender() != null 
			    && !bean.getDocumentDetails().getInsuredPatientName().getInsuredGender().equals(ReferenceTable.FEMALE_GENDER)){
			hasError = true;
			errMsg.append("Insured Patient Name should be female - Mother’s Name to be mentioned </br>");
		}*/

		/*if(cmbDOPVisitReason.getValue() == null){
			hasError = true;
			errMsg.append("Please select any Reason for OP checkup Visit </br>");
		}*/

		/*if(StringUtils.isBlank(provisionAmtTxt.getValue())){
			hasError = true;
			errMsg.append("Please fill the Provision Amount </br>");
		}*/
		
		/*if(selectedHospital == null){
			hasError = true;
			errMsg.append("Please fill the hospital details </br>");
		}*/
		/*if(StringUtils.isBlank(hospitalName.getValue())){
			hasError = true;
			errMsg.append("Please fill the hospital details </br>");
		}*/

		/*if(!diagnosisListenerTableObj.isValid()){
			hasError = true;
			List<String> errList = diagnosisListenerTableObj.getErrors();
			if(errList != null && errList.size() > 0){
				for(String err : errList){
					errMsg.append(err+" </br>");
				}
			}
		}*/
		return hasError;
	}

	@SuppressWarnings("static-access")
	private void showErrorMessage(String eMsg) {
		/*Label label = new Label(eMsg, ContentMode.HTML);
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
		dialog.show(getUI().getCurrent(), null, true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);
	}
	/*private void onAutocompleteStateSelect(AutocompleteEvents.SelectEvent event) {		
		AutocompleteSuggestion suggestion = event.getSuggestion();		
		String caption = "Suggestion selected: " + suggestion.getValue();		
		State state = (State) suggestion.getData();		

		if (state != null && cmbState.getValue() != null		
				&& !cmbState.getValue().equals("")) {		
			handleStateSelection(state);	
		//	bean.getNewIntimationDTO().setState(state);
		} else {		
			Notification.show("Pealse Select a Valid State");		
		}		
		cmbCity.setValue("");		
		cmbCity.setData(null);		
			
		hospitalName.setValue("");		
		hospitalName.setData(null);		
		
		selectedCity = null;		
		selectedHospital = null;		
		clearHospitalDetails();		


	}*/		

	/*private void onAutocompleteCitySelect(AutocompleteEvents.SelectEvent event) {		
		AutocompleteSuggestion suggestion = event.getSuggestion();		
		String caption = "Suggestion selected: " + suggestion.getValue();		
		CityTownVillage city = (CityTownVillage) suggestion.getData();		


		if (city != null && cmbCity.getValue() != null		
				&& !cmbCity.getValue().equals("")) {		
			handleCitySelection(city);	
		//	bean.getNewIntimationDTO().setCity(city);
		} else {		
			Notification.show("Pealse Select a Valid City");		
		}		
			
		hospitalName.setValue("");		
		hospitalName.setData(null);		
			
		selectedHospital = null;		
		clearHospitalDetails();		
	}*/

	/*private void onAutocompleteHospitalSelect(AutocompleteEvents.SelectEvent event) {		
		AutocompleteSuggestion suggestion = event.getSuggestion();		
		String caption = "Suggestion selected: " + suggestion.getValue();		
		HospitalDto hospitalData = (HospitalDto) suggestion.getData();		
		if (hospitalData != null) {		
			bean.setHospitalType(hospitalData.getHospitalType());		
			bean.setHospitalTypeValue(hospitalData		
					.getHospitalTypeValue());		
			bean.setHospitalDto(hospitalData);		
			bindBeanToUI();		
			handleHospitalSelection(hospitalData);		

		}
	}*/

	private void bindBeanToUI() {
		HospitalDto hospitalDto = this.bean.getSelectedHospital();

		/*State id = bean.getNewIntimationDTO().getState();
		if (!ValidatorUtils.isNull(id)) {
			cmbState.setValue(id.getValue());
			cmbState.setData(id);
			selectedState = id;
		}

		CityTownVillage city = bean.getNewIntimationDTO().getCity();
		if (!ValidatorUtils.isNull(city)) {
			cmbCity.setValue(city.getValue());
			cmbCity.setData(city);
			selectedCity = city;
		}*/

		/*if (!ValidatorUtils.isNull(hospitalDto)
				&& hospitalDto.getName() != null) {
			hospitalName.setValue(hospitalDto.getName());
			hospitalName.setData(hospitalDto);
			

		}*/

		

		if (hospitalDto != null && hospitalDto.getKey() != null) {
			getAndFillHospitalDetails(hospitalDto);
		}
	}

	private void getAndFillHospitalDetails(HospitalDto hospitalDto) {
		Long key = hospitalDto.getKey();
		if (!ValidatorUtils.isNull(key)) {

					UnFreezHospitals hospital = hospitalDto.getRegistedUnFreezHospitals();
					
					
					String hospAddress = (hospital.getAddress() != null ? hospital.getAddress() : "") + ","+
							(hospital.getCity() != null ? hospital.getCity() : "") + ","+ 
							(hospital.getState() != null ? hospital.getState(): "")  +"-"+
							(hospital.getPincode() != null ? hospital.getPincode(): "");
					
//						hospitalAddress.setValue(hospAddress != null ? hospAddress : "");
						
					hospitalPhone
							.setValue(hospital.getPhoneNumber() != null ? hospital
									.getPhoneNumber() : "");
					
					/*hospitalFaxNo
							.setValue(hospital.getFax() != null ? hospital
									.getFax() : "");
					
					hospitalName.setValue(hospital.getName());*/
					
					hospitalDocName.setReadOnly(false);
					hospitalDocName.setValue(hospital.getRepresentativeName() !=null ? hospital.getRepresentativeName() : "" );
					hospitalDocName.setReadOnly(true);

				
		}
	}

	private void handleHospitalSelection(HospitalDto suggestion) {
		if (suggestion != null) {
			selectedHospital = suggestion;

			if (suggestion.getKey() != null) {
				UnFreezHospitals registedHospitals = suggestion.getRegistedUnFreezHospitals();
				if (registedHospitals != null) {
					/*this.txtHospitalAddress.setValue(registedHospitals
							.getAddress());*/
					String hospAddress = (registedHospitals.getAddress() != null ? registedHospitals.getAddress() : "") + ","+
							(registedHospitals.getCity() != null ? registedHospitals.getCity() : "") + ","+ 
							(registedHospitals.getState() != null ? registedHospitals.getState(): "")  +"-"+
							(registedHospitals.getPincode() != null ? registedHospitals.getPincode(): "");
					
//					this.hospitalAddress.setReadOnly(false);
					this.hospitalPhone.setReadOnly(false);
//					this.hospitalFaxNo.setReadOnly(false);
					
//					this.hospitalAddress.setValue(hospAddress != null ? hospAddress : "");					
					this.hospitalPhone.setValue(registedHospitals
							.getPhoneNumber());
					/*this.hospi.setValue(registedHospitals
							.getPincode());*/
//					this.hospitalFaxNo.setValue(registedHospitals.getFax());
					/*this.txtHospitalEmailId.setValue(registedHospitals
							.getEmailId());
					this.txtHospitalMobileNumber.setValue(registedHospitals
							.getMobileNumber());
					this.hospitalCodeIrdaTxt.setValue(registedHospitals
							.getHospitalIrdaCode());
					this.hospitalCodeTxt.setValue(registedHospitals
							.getHospitalCode());*/
					
//					this.hospitalAddress.setReadOnly(true);
					this.hospitalPhone.setReadOnly(true);
//					this.hospitalFaxNo.setReadOnly(true);

					setHospitalFieldsEditable(false);

					SelectValue hospitalTypeValue = new SelectValue();
					hospitalTypeValue.setId(registedHospitals.getHospitalType()
							.getKey());
					hospitalTypeValue.setValue(registedHospitals
							.getHospitalType().getValue());

					
					} 
					//IMSSUPPOR-27813 - added for doctor name
					hospitalDocName.setReadOnly(false);
					hospitalDocName.setValue(registedHospitals.getRepresentativeName() !=null ? registedHospitals.getRepresentativeName() : "" );
					hospitalDocName.setReadOnly(true);

				
			}
		} else {
			setHospitalFieldsEditable(true);
			//bindTempHospitalTypeToUI();
		}

	}
	
		private void setHospitalFieldsEditable(boolean isEditable) {

//		this.hospitalAddress.setEnabled(isEditable);
		this.hospitalPhone.setEnabled(isEditable);
//		this.hospitalFaxNo.setEnabled(isEditable);
		
	}
	class CustomSuggestionProvider extends CollectionSuggestionProvider {		
		public CustomSuggestionProvider() {		
			super(Arrays.asList(new String[] {		
					"Java",		
					"JavaScript",		
					"Join Java",		
					"JavaFX Script"		
			}), MatchMode.CONTAINS, true);		
		}		
		public Collection<AutocompleteSuggestion> querySuggestions(AutocompleteQuery query) {		
			String caption = query.getExtension().getParent().getCaption();		
			Collection<AutocompleteSuggestion> suggestions = super.querySuggestions(query);		
			if(caption.equalsIgnoreCase(stateCaption)){		

				suggestions.clear();		
				handleStateSearchQuery(suggestions,query);		
			}		
			else if(caption.equalsIgnoreCase(cityCaption)){		
				handleCitySearchQuery(suggestions,query);		
			}else{		
				handleHospitalSearchQuery(suggestions,query);		
			}		
			return suggestions;		

		}		
	}
	
	private Boolean isValidEmail(String strEmail)
	{
		String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern validEmailPattern = Pattern.compile(emailPattern);
		Matcher validMatcher = validEmailPattern.matcher(strEmail);
		return validMatcher.matches();
	}
	
	public void setValeBalanceSumInsured(Integer opBalanceSumInsured){
		/*if(opBalanceSumInsured != null && opBalanceSumInsured.get("balanceSumInsured") != null ){
			Double bsi = Double.valueOf(opBalanceSumInsured.get("balanceSumInsured").toString());
			balanceSumInsuredtTxt.setConverter(Double.class);
			balanceSumInsuredtTxt.setValue(String.valueOf(bsi));
		}*/
		balanceSumInsuredtTxt.setValue(opBalanceSumInsured.toString());
	/*	final MessageBox showInfoMessageBox = showInfoMessageBox("Available OP SI Limit : "+);
		Button homeButton = showInfoMessageBox.getButton(ButtonType.OK);
		
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				showInfoMessageBox.close();
			}
		});*/
		
	}
	public void alertMessageForDentalAndOpthalmic() {
   		
		final MessageBox showInfoMessageBox = showInfoMessageBox("This Cover Section has block of   3 continous years");
		Button homeButton = showInfoMessageBox.getButton(ButtonType.OK);
		
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				showInfoMessageBox.close();
			}
		});
		
	}
	
	public void alertMessageForAvailableOPSILimit() {
		
		String opSI= bean.getAvailableSI();
		
		Double opSIValue=0.0;
		if(opSI != null && !(opSI.isEmpty())){
			
			opSIValue=Double.valueOf(opSI);
		}
		
		final MessageBox showInfoMessageBox = showInfoMessageBox("Available OP SI Limit : "+opSIValue );
		Button homeButton = showInfoMessageBox.getButton(ButtonType.OK);
		
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				showInfoMessageBox.close();
			}
		});
		
	}
	
	public MessageBox showInfoMessageBox(String message){
		final MessageBox msgBox = MessageBox
			    .createInfo()
			    .withCaptionCust("Information")
			    .withHtmlMessage(message)
			    .withOkButton(ButtonOption.caption(ButtonType.OK.name()))
			    .open();
		
		return msgBox;
      }
	
	public void alertMessageForInsuredNameNotChoosenFemale() {

		final MessageBox showInfoMessageBox = showInfoMessageBox("Insured Patient Name should be female - Mother’s Name to be mentioned");
		Button homeButton = showInfoMessageBox.getButton(ButtonType.OK);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				showInfoMessageBox.close();
			}
		});

	}
	public void alertMessageForCoverSectionVaccinationBenefit() {

		final MessageBox showInfoMessageBox = showInfoMessageBox("Benefit is subject to a waiting period of 24 months");
		Button homeButton = showInfoMessageBox.getButton(ButtonType.OK);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				showInfoMessageBox.close();
			}
		});

	}
	
	public void alertMessageVaccinationBenefit() {

		final MessageBox showInfoMessageBox = showInfoMessageBox("Please verify the insured details before processing");
		Button homeButton = showInfoMessageBox.getButton(ButtonType.OK);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				showInfoMessageBox.close();
			}
		});

	}
	
public void alertMessageForAvailableOPSILimitHbA1C() {
		
		String opSI= bean.getAvailableSI();
		
		Double opSIValue=0.0;
		if(opSI != null && !(opSI.isEmpty())){
			
			opSIValue=Double.valueOf(opSI);
		}
		
		final MessageBox showInfoMessageBox = showInfoMessageBox("Available OP SI Limit (HbA1C): "+opSIValue );
		Button homeButton = showInfoMessageBox.getButton(ButtonType.OK);
		
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				showInfoMessageBox.close();
			}
		});
		
	}
}

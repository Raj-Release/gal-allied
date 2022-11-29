package com.shaic.claim.outpatient.processOPpages;

import java.util.ArrayList;
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

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jcajce.provider.symmetric.Shacal2;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;

import com.alert.util.ButtonOption;
import com.alert.util.ButtonType;
import com.alert.util.MessageBox;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.validation.ValidatorUtils;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.intimation.create.dto.HospitalDto;
import com.shaic.claim.outpatient.registerclaim.dto.OutPatientDTO;
import com.shaic.claim.outpatient.registerclaim.wizard.DiagnosisDetailsOPTableDTO;
import com.shaic.claim.outpatient.registerclaim.wizard.DiganosisDetailsListenerForOP;
import com.shaic.claim.outpatient.registerclaim.wizard.OPRegisterClaimWizardPresenter;
import com.shaic.domain.CityTownVillage;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Insured;
import com.shaic.domain.InsuredService;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.State;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.event.FieldEvents.TextChangeEvent;
import com.vaadin.v7.event.FieldEvents.TextChangeListener;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.zybnet.autocomplete.server.AutocompleteField;
import com.zybnet.autocomplete.server.AutocompleteQueryListener;
import com.zybnet.autocomplete.server.AutocompleteSuggestionPickedListener;

import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteTextField;

public class ProcessOPClaimDetailsPage extends ViewComponent{

	private static final long serialVersionUID = -5289304019062349766L;

	private OutPatientDTO bean;

	private GWizard wizard;

	private VerticalLayout opRegisterPageLayout;
	private Panel claimDetailsPanel;
//	private Panel hosDetailsPanel;
//	private Panel docDetailsPanel;

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
	
	private OptionGroup physicalDocsReceived;
	private PopupDateField physicalDocsReceivedDate;
	
	private CheckBox chkConsultation;
	private CheckBox chkDiagnositics;
	private CheckBox chkPhysiotherapy;
	private CheckBox chkMedicine;
	
	private ComboBox cmbConsultationType;
	
	
	FormLayout chxEmergencylayout;
	FormLayout chxAccidentlayout;
	HorizontalLayout chxLayout;
	FormLayout claimDetailslayout_1;
	FormLayout claimDetailslayout_2;
	VerticalLayout verticalLayout;
	HorizontalLayout fieldLayout;

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
	
//	private ComboBox docReceivedFrom;
//	private TextField docSubmittedName;
//	private PopupDateField docReceivedDate;
//	private ComboBox docModeOfReceipt;
//	private TextField docContactNo;
//	private TextField docEmailId;

	@Inject
	private MasterService masterService;

	@EJB
	private InsuredService insuredService;

	@EJB
	private HospitalService hospitalService;
	
	@Inject
	private Instance<ConsultationTabPage> consultationTabPageInst;
	
	private ConsultationTabPage consultationTabPage;
	
	@Inject
	private Instance<DiagnosticTabChange> diagnosticTabChangeInst;
	
	private DiagnosticTabChange diagnosticTabChange;
	
	@Inject
	private Instance<PhysioTherapyTabPage> physioTherapyTabPageInst;
	
	private PhysioTherapyTabPage physioTherapyTabPage;
	
	@Inject
	private Instance<MedicineTabPage> medicineTabPageInst;
	
	private MedicineTabPage medicineTabPage;
	
	private TabSheet benefitsAvailedTab = new TabSheet();
	
	private Panel benefitsPanel;

	/*@Inject
	private Instance<DiganosisDetailsListenerForOP> diagnosisListnerTable;

	private DiganosisDetailsListenerForOP diagnosisListenerTableObj;*/

	private Map<String, Object> referenceData;

	private StringBuilder errMsg = new StringBuilder();
	
	public StringBuilder getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(StringBuilder errMsg) {
		this.errMsg = errMsg;
	}

	private ViewDetails viewDetails;

	public ViewDetails getViewDetails() {
		return viewDetails;
	}

	public void setViewDetails(ViewDetails viewDetails) {
		this.viewDetails = viewDetails;
	}

	public void init(OutPatientDTO bean, GWizard wizard) {
		this.bean = bean;
		this.wizard = wizard;
		opRegisterPageLayout = new VerticalLayout();
		claimDetailsPanel = new Panel();
		benefitsPanel = new Panel();
//		hosDetailsPanel = new Panel();
//		docDetailsPanel = new Panel();
	}

	@SuppressWarnings("deprecation")
	public Component getContent(){
		wizard.getNextButton().setEnabled(false);
		
		opRegisterPageLayout.removeAllComponents();
		
		if((bean.getPolicyDto().getProduct().getKey().equals(ReferenceTable.STAR_DIABETIC_SAFE_FLOATER_REVISED)
				|| bean.getPolicyDto().getProduct().getKey().equals(ReferenceTable.STAR_DIABETIC_SAFE_INDIVIDUAL_REVISED)) 
				&& bean.getDocumentDetails().getConsultationType() != null && bean.getDocumentDetails().getConsultationType().getId() != null 
				&& bean.getDocumentDetails().getConsultationType().getId().equals(ReferenceTable.HbA1C_TREATMENT)){
			if(bean.getAvailableSI()!=null){
				alertMessageForAvailableOPSILimitHbA1C();
			}
		}
		else{
			if(bean.getAvailableSI()!=null){
				alertMessageForAvailableOPSILimit();
			}
		}
		

		/*cmbClaimType =  new ComboBox("Claim Type <b style= 'color: red'>*</b>");
		cmbClaimType.setCaptionAsHtml(true);*/
		
		cmbTreatmentType =  new ComboBox("Treatment Type <b style= 'color: red'>*</b>"); //binder.buildAndBind("Claim Type *", "documentDetails.claimType", ComboBox.class);
		cmbTreatmentType.setCaptionAsHtml(true);
		cmbTreatmentType.setValue(bean.getDocumentDetails().getTreatmentType());
		
		cmbInsuredPatientName =  new ComboBox("Insured Patient Name <b style= 'color: red'>*</b>");
		cmbInsuredPatientName.setCaptionAsHtml(true);
		cmbInsuredPatientName.setValue(bean.getDocumentDetails().getInsuredPatientName());
		cmbInsuredPatientName.setNullSelectionAllowed(false);
		
		cmbModeOfReceipt =  new ComboBox("Mode Of Receipt <b style= 'color: red'>*</b>"); //binder.buildAndBind("Insured Patient Name *", "documentDetails.insuredPatientName", ComboBox.class);
		cmbModeOfReceipt.setCaptionAsHtml(true);
		cmbModeOfReceipt.setValue(bean.getDocumentDetails().getModeOfReceipt());
		
		cmbConsultationType = new ComboBox("Cover Section <b style= 'color: red'>*</b>");
		cmbConsultationType.setCaptionAsHtml(true);
		cmbConsultationType.setValue(bean.getDocumentDetails().getConsultationType());

		checkupDate =  new PopupDateField("OP Check-up Date <b style= 'color: red'>*</b>");
		checkupDate.setCaptionAsHtml(true);
		checkupDate.setDateFormat(("dd/MM/yyyy"));
		checkupDate.setRangeEnd(new Date());
		checkupDate.setValue(bean.getDocumentDetails().getOPCheckupDate());


		billReceivedDate =  new PopupDateField("Bill Received Date <b style= 'color: red'>*</b>");
		billReceivedDate.setCaptionAsHtml(true);
		billReceivedDate.setDateFormat(("dd/MM/yyyy"));
		billReceivedDate.setRangeEnd(new Date());
		billReceivedDate.setValue(bean.getDocumentDetails().getBillReceivedDate());

		emailIdTxt = new TextField("Email Id"); 
		CSValidator emailIdTxtValidator = new CSValidator();
		emailIdTxtValidator.extend(emailIdTxt);
		emailIdTxtValidator.setRegExp("^[a-z A-Z 0-9 @ . _]*$");
		emailIdTxtValidator.setPreventInvalidTyping(true);
		emailIdTxt.setValue(bean.getDocumentDetails().getDocEmailId());
		emailIdTxt.setNullRepresentation("");

		amountClaimedTxt = new TextField("Amount Claimed <b style= 'color: red'>*</b>");
		amountClaimedTxt.setCaptionAsHtml(true);
		CSValidator amountClaimedTxtValidator = new CSValidator();
		amountClaimedTxtValidator.extend(amountClaimedTxt);
		amountClaimedTxtValidator.setRegExp("^[0-9]*$");
		amountClaimedTxt.setConverter(Double.class);
		amountClaimedTxtValidator.setPreventInvalidTyping(true);
		amountClaimedTxt.setValue(String.valueOf(bean.getDocumentDetails().getAmountClaimed().longValue()));
		amountClaimedTxt.setNullRepresentation("");

		chkEmergency = new CheckBox("Emergency "); 
		if (bean.getDocumentDetails() != null && bean.getDocumentDetails().getEmergencyFlag() != null && 
				bean.getDocumentDetails().getEmergencyFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)) {
			chkEmergency.setValue(Boolean.TRUE);
		}

		chkAccident = new CheckBox("Accident ");
		if (bean.getDocumentDetails() != null && bean.getDocumentDetails().getAccidentFlag() != null && 
				bean.getDocumentDetails().getAccidentFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)) {
			chkAccident.setValue(Boolean.TRUE);
		}
		
		reasonforConsultation = new TextArea("Reason for Consultation <b style= 'color: red'>*</b>"); 
		reasonforConsultation.setCaptionAsHtml(true);
		CSValidator reasonforConsultationValidator = new CSValidator();
		reasonforConsultationValidator.extend(reasonforConsultation);
		reasonforConsultationValidator.setRegExp("^[a-zA-Z 0-9 . , ' -]*$");
		reasonforConsultationValidator.setPreventInvalidTyping(true);
		reasonforConsultation.setValue(bean.getDocumentDetails().getReasonforConsultation());
		reasonforConsultation.setNullRepresentation("");
		
		remarksForEmergencyAccident = new TextArea("Remarks for Emergency/Accident <b style= 'color: red'>*</b>"); 
		remarksForEmergencyAccident.setCaptionAsHtml(true);  
		CSValidator remarksForEmergencyAccidentValidator = new CSValidator();
		remarksForEmergencyAccidentValidator.extend(remarksForEmergencyAccident);
		remarksForEmergencyAccidentValidator.setRegExp("^[a-zA-Z 0-9 . , ' -]*$");
		remarksForEmergencyAccidentValidator.setPreventInvalidTyping(true);
		remarksForEmergencyAccident.setValue(bean.getDocumentDetails().getRemarksForEmergencyAccident());
		remarksForEmergencyAccident.setNullRepresentation("");
		
		hospitalDocName =  new TextField("Name (Doc. Submitted Name) <b style= 'color: red'>*</b>"); 
		hospitalDocName.setCaptionAsHtml(true);
		CSValidator doctorNameValidator = new CSValidator();
		doctorNameValidator.extend(hospitalDocName);
		doctorNameValidator.setRegExp("^[a-zA-Z . ]*$");
		doctorNameValidator.setPreventInvalidTyping(true);
		hospitalDocName.setValue(bean.getDocumentDetails().getDocSubmittedName());
		hospitalDocName.setNullRepresentation("");
		
		hospitalPhone = new TextField("Contact No (Doc. Submitted No) <b style= 'color: red'>*</b>"); 
		hospitalPhone.setCaptionAsHtml(true);
		hospitalPhone.setMaxLength(10);
		CSValidator hospitalPhoneValidator = new CSValidator();
		hospitalPhoneValidator.extend(hospitalPhone);
		hospitalPhoneValidator.setRegExp("^[0-9]*$");
		hospitalPhoneValidator.setPreventInvalidTyping(true);
		hospitalPhone.setValue(bean.getDocumentDetails().getDocSubmittedContactNo() != null ? 
				bean.getDocumentDetails().getDocSubmittedContactNo().toString() : "");
		
		
		physicalDocsReceived = new OptionGroup();
		physicalDocsReceived.addItems(getReadioButtonOptions());
		physicalDocsReceived.setItemCaption(true, "Yes");
		physicalDocsReceived.setItemCaption(false, "No");
		physicalDocsReceived.setStyleName("horizontal");
		physicalDocsReceived.setCaption("Physical Documents Received <b style= 'color: red'>*</b>"); 
		physicalDocsReceived.setCaptionAsHtml(true);
//		physicalDocsReceived.setValue(true);
		
		if (bean.getDocumentDetails() != null && bean.getDocumentDetails().getPhysicalDocsReceivedFlag() != null && 
				bean.getDocumentDetails().getPhysicalDocsReceivedFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)) {
			physicalDocsReceived.setValue(true);
		}
		
		physicalDocsReceivedDate =  new PopupDateField("Physical Documents<br>Received Date<b style= 'color: red'> *</b>");
		physicalDocsReceivedDate.setCaptionAsHtml(true);
		physicalDocsReceivedDate.setDateFormat(("dd/MM/yyyy"));
		physicalDocsReceivedDate.setRangeEnd(new Date());
		if(bean.getDocumentDetails().getPhysicalDocsReceivedDate() != null){
			physicalDocsReceivedDate.setValue(bean.getDocumentDetails().getPhysicalDocsReceivedDate());
		}
		
		if(bean.getDocumentDetails().getModeOfReceipt().getValue() != null && bean.getDocumentDetails().getModeOfReceipt().getValue().equalsIgnoreCase("Online")){
			physicalDocsReceived.setVisible(true);
			physicalDocsReceivedDate.setVisible(true);
		}else {
			physicalDocsReceived.setVisible(false);
			physicalDocsReceivedDate.setVisible(false);
		}

		/*provisionAmtTxt = new TextField("Provision Amount <b style= 'color: red'>*</b>");
		provisionAmtTxt.setCaptionAsHtml(true);
//		provisionAmtTxt.setValue(String.valueOf(bean.getDocumentDetails().getProvisionAmt().longValue()));


		cmbDOPVisitReason =  new ComboBox("Reason for OP Check-up Visit <b style= 'color: red'>*</b>");
		cmbDOPVisitReason.setCaptionAsHtml(true);

		OPVisitRemarks = new TextField("Remarks for OP Check-up Visit");*/
//		OPVisitRemarks.setValue(bean.getDocumentDetails().getRemarksForOpVisit());

		claimDetailsPanel.setCaption("Claim Details");
		benefitsPanel.setCaption("");

//		FormLayout claimDetailslayout_1 = new FormLayout(cmbClaimType, checkupDate, amountClaimedTxt, cmbDOPVisitReason);
//		FormLayout claimDetailslayout_2 = new FormLayout(cmbInsuredPatientName, billReceivedDate, provisionAmtTxt, OPVisitRemarks);
		
		chxEmergencylayout = new FormLayout(chkEmergency);
		chxEmergencylayout.setSpacing(false);
		chxAccidentlayout = new FormLayout(chkAccident);
		chxAccidentlayout.setSpacing(false);

		chxLayout = new HorizontalLayout(chxEmergencylayout, chxAccidentlayout);
		chxLayout.setSpacing(false);
		
		if ((bean.getDocumentDetails() != null && bean.getDocumentDetails().getEmergencyFlag() != null 
				&& bean.getDocumentDetails().getEmergencyFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)) 
				|| (bean.getDocumentDetails() != null && bean.getDocumentDetails().getAccidentFlag() != null 
						&& bean.getDocumentDetails().getAccidentFlag().equalsIgnoreCase(SHAConstants.YES_FLAG))) {
			claimDetailslayout_1 = new FormLayout(cmbInsuredPatientName, checkupDate, billReceivedDate, cmbModeOfReceipt, hospitalPhone, reasonforConsultation,physicalDocsReceived);
			claimDetailslayout_2 = new FormLayout(remarksForEmergencyAccident,cmbTreatmentType,cmbConsultationType, hospitalDocName, emailIdTxt, amountClaimedTxt,physicalDocsReceivedDate);
			claimDetailslayout_2.setMargin(false);
		}else {
			claimDetailslayout_1 = new FormLayout(cmbInsuredPatientName, checkupDate, billReceivedDate, cmbModeOfReceipt, hospitalPhone, reasonforConsultation,physicalDocsReceived);
			claimDetailslayout_2 = new FormLayout(cmbTreatmentType, cmbConsultationType,hospitalDocName, emailIdTxt, amountClaimedTxt,physicalDocsReceivedDate);
			claimDetailslayout_2.setMargin(false);
		}

		verticalLayout = new VerticalLayout(chxLayout, claimDetailslayout_2);
		verticalLayout.setSpacing(false);
		
		fieldLayout = new HorizontalLayout(claimDetailslayout_1, verticalLayout);

		fieldLayout.setSpacing(true);
		fieldLayout.setMargin(true);
		fieldLayout.setWidth("100%");
		
		TextField chkBoxLayout = new TextField("Benefits Availed");
		chkBoxLayout.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		chkBoxLayout.setEnabled(false);
		chkConsultation = new CheckBox("Consultation");
		chkDiagnositics = new CheckBox("Diagnostics");
		chkPhysiotherapy = new CheckBox("Physiotherapy");
		chkMedicine = new CheckBox("Medicine");
		
		chkConsultation.setValue(true);
		chkConsultation.setEnabled(false);
		
		if(bean.getDiagnosis()){
			chkDiagnositics.setValue(true);
		}
		if(bean.getPhysiotherapy()){
			chkPhysiotherapy.setValue(true);
		}
		if(bean.getMedicine()){
			chkMedicine.setValue(true);
		}
		
		HorizontalLayout chkBoxes = new HorizontalLayout(chkConsultation,chkDiagnositics,chkPhysiotherapy,chkMedicine);
		chkBoxes.setSpacing(true);
		HorizontalLayout benefitsAvailed = new HorizontalLayout(chkBoxLayout,chkBoxes);
		
		VerticalLayout vLayout = new VerticalLayout(fieldLayout);
		
		FormLayout formLayout = new FormLayout(benefitsAvailed);
		
		VerticalLayout vbenfitLayout = new VerticalLayout(formLayout,buildBenefitsAvailedTabSheet());
		vbenfitLayout.setSpacing(false);
		vbenfitLayout.setMargin(true);

		claimDetailsPanel.setContent(vLayout);
		benefitsPanel.setContent(vbenfitLayout);
		
		addListener();
		
		benefitsAvailedValueChangeListner();

		/*cmbState = new AutocompleteTextField();
		cmbState.setCaption("State");
		cmbState.setWidth("-1px");		
		//Vaadin8-setImmediate() cmbState.setImmediate(true);
		cmbState.setRequiredIndicatorVisible(true);		
		//cmbState.setValidationVisible(false);
		cmbState.setMinChars(2);
		cmbState.setReadOnly(false);
//		cmbState.setValue(bean.getNewIntimationDTO().getHospitalDto().getState());
		cmbState.setReadOnly(true);
		cmbState.setEnabled(false);


		cmbCity = new AutocompleteTextField();
		//Vaadin8-setImmediate() cmbCity.setImmediate(true);
		cmbCity.setCaption("City");
		cmbCity.setWidth("-1px");
		//Vaadin8-setImmediate() cmbCity.setImmediate(true);
		cmbCity.setRequiredIndicatorVisible(true);	
		//cmbCity.setValidationVisible(false);
		cmbCity.setMinChars(2);
		cmbCity.setReadOnly(false);
//		cmbCity.setValue(bean.getNewIntimationDTO().getHospitalDto().getCity());
		cmbCity.setReadOnly(true);
		cmbCity.setEnabled(false);

		hospitalName = new AutocompleteTextField();
		//Vaadin8-setImmediate() hospitalName.setImmediate(true);
		hospitalName.setRequiredIndicatorVisible(true);	
		hospitalName.setWidth("-1px");
		hospitalName.setCaption("Hospital Name");
		hospitalName.setReadOnly(false);
//		hospitalName.setValue(bean.getNewIntimationDTO().getHospitalDto().getName());
		hospitalName.setReadOnly(true);
		hospitalName.setEnabled(false);

		hospitalAddress = new TextArea("Hospital Address");
		hospitalAddress.setReadOnly(false);
//		hospitalAddress.setValue(bean.getNewIntimationDTO().getHospitalDto().getAddress());
		hospitalAddress.setReadOnly(true);*/

		
//		hospitalPhone = new TextField("Hospital Phone");
//		hospitalPhone.setReadOnly(false);
//		hospitalPhone.setValue(bean.getNewIntimationDTO().getHospitalDto().getPhoneNumber());
//		hospitalPhone.setReadOnly(true);
		
		/*hospitalFaxNo = new TextField("Hospital Fax No");
		hospitalFaxNo.setReadOnly(false);
//		hospitalFaxNo.setValue(bean.getNewIntimationDTO().getHospitalDto().getFax());
		hospitalFaxNo.setReadOnly(true);*/
		
//		hospitalDocName =  new TextField("Hospital Doctor Name");
//		hospitalDocName.setValue(bean.getNewIntimationDTO().getHospitalDto().getName());
//		hospitalDocName.setReadOnly(true);

//		hosDetailsPanel.setCaption("Hospital Details");

//		FormLayout hosDetailslayout_1 = new FormLayout(cmbState, hospitalName, hospitalPhone, hospitalFaxNo);
//		FormLayout hosDetailslayout_2 = new FormLayout(cmbCity, hospitalAddress, hospitalDocName);

//		HorizontalLayout field2Layout = new HorizontalLayout(hosDetailslayout_1, hosDetailslayout_2);

//		field2Layout.setSpacing(true);
//		field2Layout.setMargin(true);
//		field2Layout.setWidth("100%");

//		hosDetailsPanel.setContent(field2Layout);
		
		VerticalLayout panelLayout = new VerticalLayout(claimDetailsPanel,benefitsPanel);

		opRegisterPageLayout.addComponent(panelLayout);
//		opRegisterPageLayout.addComponent(hosDetailsPanel);

		/*setUpAutoState(cmbState);
		setUpAutoCity(cmbCity);
		setUpAutoHospital(hospitalName);*/


		/*this.diagnosisListenerTableObj = diagnosisListnerTable.get();
		this.diagnosisListenerTableObj.init("diagnosisDetailsOP");*/

		referenceData = new HashMap<String, Object>();

		referenceData.put("icdChapter", masterService.getSelectValuesForICDChapter());
		referenceData.put("icdBlock", masterService.getSelectValuesForICDBlock());
		referenceData.put("icdCode", masterService.getSelectValuesForICDCode());

		/*this.diagnosisListenerTableObj.setReferenceData(referenceData);
		
		for(DiagnosisDetailsOPTableDTO rec : bean.getDiagnosisListenerTableList()){
			diagnosisListenerTableObj.addBeanToList(rec);
		}

		opRegisterPageLayout.addComponent(diagnosisListenerTableObj);
		
		// Document Details 
		docReceivedFrom =   new ComboBox("Document Received From <b style= 'color: red'>*</b>");
		docReceivedFrom.setCaptionAsHtml(true);
		
		docSubmittedName =  new TextField("Name(Doc. Submitted Name) <b style= 'color: red'>*</b>");
		docSubmittedName.setCaptionAsHtml(true);
		
		docReceivedDate =   new PopupDateField("Documents Received Date <b style= 'color: red'>*</b>");
		docReceivedDate.setCaptionAsHtml(true);
		docReceivedDate.setDateFormat(("dd/MM/yyyy"));

		
		docContactNo =  new TextField("Contact No (Doc.Submitted Name) <b style= 'color: red'>*</b>");
		docContactNo.setCaptionAsHtml(true);
		
		docModeOfReceipt =   new ComboBox("Mode of Receipt <b style= 'color: red'>*</b>");
		docModeOfReceipt.setCaptionAsHtml(true);
		
		docEmailId =  new TextField("Email ID <b style= 'color: red'>*</b>");
		docEmailId.setCaptionAsHtml(true);*/
		
		setDropDownValues();
		addListeners();
		
		/*if(!StringUtils.isBlank(bean.getDocumentDetails().getDocSubmittedName())){
			docSubmittedName.setValue(bean.getDocumentDetails().getDocSubmittedName());
		}else{
			docSubmittedName.setValue("");
		}
		

		if(bean.getDocumentDetails().getDocSubmittedDate() != null){
			docReceivedDate.setValue(bean.getDocumentDetails().getDocSubmittedDate());
		}else{
			docReceivedDate.setValue(null);
		}
		

		if(bean.getDocumentDetails().getDocSubmittedContactNo() != null){
			docContactNo.setValue(String.valueOf(bean.getDocumentDetails().getDocSubmittedContactNo()));
		}else{
			docContactNo.setValue("");
		}
		

		if(bean.getDocumentDetails().getModeOfReceipt() != null){
			docModeOfReceipt.setValue(bean.getDocumentDetails().getModeOfReceipt());
		}else{
			docModeOfReceipt.setValue(null);
		}
		

		if(!StringUtils.isBlank(bean.getDocumentDetails().getDocEmailId())){
			docEmailId.setValue(bean.getDocumentDetails().getDocEmailId());
		}else{
			docEmailId.setValue("");
		}*/
		
//		FormLayout docDetailslayout_1 = new FormLayout(docReceivedFrom, docReceivedDate, docModeOfReceipt);
//		FormLayout docDetailslayout_2 = new FormLayout(docSubmittedName, docContactNo, docEmailId);
//		
//		HorizontalLayout field3Layout = new HorizontalLayout(docDetailslayout_1, docDetailslayout_2);
//
//		field3Layout.setSpacing(true);
//		field3Layout.setMargin(true);
//		field3Layout.setWidth("100%");
		
//		docDetailsPanel.setCaption("Document Details");
//		docDetailsPanel.setContent(field3Layout);
		

		
//		opRegisterPageLayout.addComponent(docDetailsPanel);
//		opRegisterPageLayout.addComponent(addFooterButtons());
		return opRegisterPageLayout;
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

	@SuppressWarnings("deprecation")
	public void setDropDownValues(){
		/*BeanItemContainer<SelectValue> claimTypes = masterService.getOPClaimTypeSelectValueContainer(ReferenceTable.CLAIM_TYPE);
		cmbClaimType.setContainerDataSource(claimTypes);
		cmbClaimType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbClaimType.setItemCaptionPropertyId("value");

		cmbClaimType.setValue(bean.getClaimDTO().getClaimType());
		cmbClaimType.setReadOnly(true);*/		


		BeanItemContainer<Insured> insuredList = insuredService.getInsuredByPolicyNoForOP(bean.getPolicyDto().getPolicyNumber());
		cmbInsuredPatientName.setContainerDataSource(insuredList);
		cmbInsuredPatientName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbInsuredPatientName.setItemCaptionPropertyId("insuredName");
		cmbInsuredPatientName.setValue(bean.getDocumentDetails().getInsuredPatientName());

//		cmbInsuredPatientName.setValue(bean.getNewIntimationDTO().getInsuredPatient());
		
		BeanItemContainer<SelectValue> receiptMode = masterService.getMastersValuebyTypeCodeOnStaatus(ReferenceTable.OP_RECMODE);
		cmbModeOfReceipt.setContainerDataSource(receiptMode);
		cmbModeOfReceipt.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbModeOfReceipt.setItemCaptionPropertyId("value");
//		cmbModeOfReceipt.setValue(receiptMode);
		cmbModeOfReceipt.setValue(bean.getDocumentDetails().getModeOfReceipt());
		
//		cmbModeOfReceipt.setValue(bean.getNewIntimationDTO().getInsuredPatient());
		
		BeanItemContainer<SelectValue> treatmentType = masterService.getMastersValuebyTypeCodeOnStaatus(ReferenceTable.OP_TRTMNT_TYPE);
		cmbTreatmentType.setContainerDataSource(treatmentType);
		cmbTreatmentType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbTreatmentType.setItemCaptionPropertyId("value");
//		cmbTreatmentType.setValue(treatmentType);
		cmbTreatmentType.setValue(bean.getDocumentDetails().getTreatmentType());
		
		BeanItemContainer<SelectValue> consultationType = masterService.getConsulationDetails(bean.getPolicyDto().getProduct().getKey());
		cmbConsultationType.setContainerDataSource(consultationType);
		cmbConsultationType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbConsultationType.setItemCaptionPropertyId("value");
		if(bean.getDocumentDetails().getConsultationType() != null){
			cmbConsultationType.setValue(bean.getDocumentDetails().getConsultationType());
			if(!bean.getDocumentDetails().getConsultationType().getId().equals(ReferenceTable.DENTAL_AND_OPTHALMIC_TREATMENT)){
				cmbConsultationType.setEnabled(false);
			}
		} else {
			if(consultationType !=null && consultationType.size() > 1){
				cmbConsultationType.setValue(consultationType);
			} else {
				List<SelectValue> secDtls = consultationType.getItemIds();
				cmbConsultationType.setValue(secDtls.get(0));
				cmbConsultationType.setEnabled(false);
			}
		}
		
//		cmbTreatmentType.setValue(bean.getNewIntimationDTO().getInsuredPatient());

		/*BeanItemContainer<SelectValue> reasonTypes = masterService.getOPReason();
		cmbDOPVisitReason.setContainerDataSource(reasonTypes);
		cmbDOPVisitReason.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbDOPVisitReason.setItemCaptionPropertyId("value");

		cmbDOPVisitReason.setValue(bean.getDocumentDetails().getReasonForOPVisit());*/
		
		// Document Received From 
		/*BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		SelectValue docRec = new SelectValue();
		docRec.setId(1541L);
		docRec.setValue("Insured");
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		selectValuesList.add(docRec);
		selectValueContainer.addAll(selectValuesList);
		
		docReceivedFrom.setContainerDataSource(selectValueContainer);
		docReceivedFrom.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		docReceivedFrom.setItemCaptionPropertyId("value");
		
		SelectValue docReceivedType = (SelectValue) docReceivedFrom.getContainerDataSource().getItemIds().toArray()[0];
		docReceivedFrom.setValue(docReceivedType);
		docReceivedFrom.setReadOnly(true);	*/	
		//---------------------------------------------------
		
//		Mode Of Receipt
//		1781 In Person
//		1782 Courier
		
		BeanItemContainer<SelectValue> modeValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		SelectValue mode1 = new SelectValue();
		mode1.setId(1781L);
		mode1.setValue("In Person");
		SelectValue mode2 = new SelectValue();
		mode2.setId(1782L);
		mode2.setValue("Courier");
		List<SelectValue> modeValuesList = new ArrayList<SelectValue>();
		modeValuesList.add(mode1);
		modeValuesList.add(mode2);
		modeValueContainer.addAll(modeValuesList);
		
		/*docModeOfReceipt.setContainerDataSource(modeValueContainer);
		docModeOfReceipt.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		docModeOfReceipt.setItemCaptionPropertyId("value");*/
	}
	
	protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> physicalDocsValues = new ArrayList<Boolean>(2);
		physicalDocsValues.add(true);
		physicalDocsValues.add(false);
		
		return physicalDocsValues;
	}
	
	@SuppressWarnings("serial")
	public AbsoluteLayout addFooterButtons(){
		HorizontalLayout buttonsLayout = new HorizontalLayout();

		Button	cancelButton = new Button("Cancel");
		cancelButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(MenuItemBean.PORCESS_CLAIM_OP, null);

			}
		});

		Button	submitButton = new Button("Submit");
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
						bean.getDocumentDetails().setAmountClaimed(Double.parseDouble(amountClaimedTxt.getValue()));
						bean.getDocumentDetails().setReasonforConsultation(reasonforConsultation.getValue());
						bean.getDocumentDetails().setDocSubmittedContactNo(Long.parseLong(hospitalPhone.getValue()));
						bean.getDocumentDetails().setDocSubmittedName(hospitalDocName.getValue());
						bean.getDocumentDetails().setRemarksForEmergencyAccident(remarksForEmergencyAccident.getValue());
						if(chkEmergency != null && chkEmergency.getValue() !=null && chkEmergency.getValue().toString().equals(Boolean.TRUE)){
							bean.getDocumentDetails().setEmergencyFlag(SHAConstants.YES_FLAG);
						}
						if(chkAccident != null && chkAccident.getValue() !=null && chkAccident.getValue().toString().equals(Boolean.TRUE)){
							bean.getDocumentDetails().setAccidentFlag(SHAConstants.YES_FLAG);
						}
						bean.getDocumentDetails().setConsultationType((SelectValue)cmbConsultationType.getValue());
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
	}

	@SuppressWarnings("deprecation")
	public void addListeners(){
		
		/*cmbClaimType.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 6251822616467768479L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue val = (SelectValue) event.getProperty().getValue();
				if(val != null){
					bean.getClaimDTO().setClaimType((SelectValue) cmbClaimType.getValue());
				}else{
					bean.getClaimDTO().setClaimType(null);
				}
			}
		});*/
		
		cmbInsuredPatientName.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 6251822616467768479L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				Insured Insval = (Insured) event.getProperty().getValue();
				if(Insval != null){
					bean.getNewIntimationDTO().setInsuredPatient(Insval);
//					getViewDetails().setInsuredSelected(Insval);
				}else{
					bean.getNewIntimationDTO().setInsuredPatient(null);
//					getViewDetails().setInsuredSelected(null);
				}
			}
		});
		
		checkupDate.addValueChangeListener(new ValueChangeListener() {
			 private static final long serialVersionUID = 1L;
             @Override
             public void valueChange(com.vaadin.v7.data.Property.ValueChangeEvent event) {
            	Date chkUpDateval = (Date) event.getProperty().getValue();
 				if(chkUpDateval != null){
 					bean.getDocumentDetails().setOPCheckupDate((Date) checkupDate.getValue());
 				}else{
 					bean.getDocumentDetails().setOPCheckupDate(null);
 				}
             }
         });
		
		billReceivedDate.addValueChangeListener(new ValueChangeListener() {
			 private static final long serialVersionUID = 1L;
            @Override
            public void valueChange(com.vaadin.v7.data.Property.ValueChangeEvent event) {
           	Date billReceivedDateval = (Date) event.getProperty().getValue();
				if(billReceivedDateval != null){
					bean.getDocumentDetails().setBillReceivedDate((Date) billReceivedDate.getValue());
				}else{
					bean.getDocumentDetails().setBillReceivedDate(null);
				}
            }
        });
		
		amountClaimedTxt.addListener(new ValueChangeListener() {
			private static final long serialVersionUID = 2347471274081835280L;
			@Override
			public void valueChange(ValueChangeEvent  event) {
				if(event.getProperty().getValue() != null){
					Long claimedAmt = Long.valueOf(SHAUtils.getIntegerFromStringWithComma(event.getProperty().getValue().toString())).longValue();
					bean.getDocumentDetails().setAmountClaimed(Double.valueOf(claimedAmt));
				}else{
					bean.getDocumentDetails().setAmountClaimed(null);
				}
			}
		});
		
		/*provisionAmtTxt.addListener(new ValueChangeListener() {
			private static final long serialVersionUID = -5287933026127527806L;
			@Override
			public void valueChange(ValueChangeEvent  event) {
				if(event.getProperty().getValue() != null){
					bean.getDocumentDetails().setProvisionAmt(Double.parseDouble(event.getProperty().getValue().toString()));
				}else{
					bean.getDocumentDetails().setProvisionAmt(null);
				}
			}
		});
		
		cmbDOPVisitReason.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -7139516156542770560L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue val = (SelectValue) event.getProperty().getValue();
				if(val != null){
					bean.getDocumentDetails().setReasonForOPVisit((SelectValue) cmbDOPVisitReason.getValue());
				}else{
					bean.getDocumentDetails().setReasonForOPVisit(null);
				}
			}
		});
		
		OPVisitRemarks.addListener(new ValueChangeListener() {
			private static final long serialVersionUID = 3833098879729893630L;
			@Override
			public void valueChange(ValueChangeEvent  event) {
				if(event.getProperty().getValue() != null){
					bean.getDocumentDetails().setRemarksForOpVisit(String.valueOf(event.getProperty().getValue()));
				}else{
					bean.getDocumentDetails().setRemarksForOpVisit(null);
				}
			}
		});
		
		
		docSubmittedName.addListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(event.getProperty().getValue() != null){
					bean.getDocumentDetails().setDocSubmittedName(String.valueOf(event.getProperty().getValue()));
				}else{
					bean.getDocumentDetails().setDocSubmittedName(null);
				}
			}
		});
		
		docReceivedDate.addValueChangeListener(new ValueChangeListener() {
			 private static final long serialVersionUID = 1L;
	            @Override
	            public void valueChange(com.vaadin.v7.data.Property.ValueChangeEvent event) {
	           	Date docReceivedDateval = (Date) event.getProperty().getValue();
					if(docReceivedDateval != null){
						bean.getDocumentDetails().setDocSubmittedDate((Date) billReceivedDate.getValue());
					}else{
						bean.getDocumentDetails().setDocSubmittedDate(null);
					}
	            }
	        });
		
		docContactNo.addListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(ValueChangeEvent  event) {
				if(event.getProperty().getValue() != null){
					bean.getDocumentDetails().setDocSubmittedContactNo(Long.valueOf(event.getProperty().getValue().toString()));
				}else{
					bean.getDocumentDetails().setDocSubmittedContactNo(null);
				}
			}
		});
	
		docModeOfReceipt.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue val = (SelectValue) event.getProperty().getValue();
				if(val != null){
					bean.getDocumentDetails().setModeOfReceipt(val);
				}else{
					bean.getDocumentDetails().setModeOfReceipt(null);
				}
			}
		});
		
		docEmailId.addListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(ValueChangeEvent  event) {
				if(event.getProperty().getValue() != null){
					bean.getDocumentDetails().setDocEmailId(String.valueOf(event.getProperty().getValue()));
				}else{
					bean.getDocumentDetails().setDocEmailId(null);
				}
			}
		});*/
		
		/*cmbState.setSuggestionPickedListener(new AutocompleteSuggestionPickedListener<State>() {
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
					hospitalAddress.setValue(suggestion.getAddress());
					hospitalAddress.setReadOnly(true);
					hospitalPhone.setValue(suggestion.getPhoneNumber());
					hospitalPhone.setReadOnly(true);
					hospitalFaxNo.setValue(suggestion.getFax());
					hospitalFaxNo.setReadOnly(true);
					hospitalDocName.setValue(suggestion.getName());
					hospitalDocName.setReadOnly(true);
				} else {
					Notification.show("Please Select a Hospital Name or Enter Hospital Details");
				}
			}
		});*/

	}

	protected void handleStateSelection(State state) {
		selectedState = state;
	}

	protected void handleCitySelection(CityTownVillage city) {
		selectedCity = city;
	}

	/*public void clearHospitalDetails() {
		hospitalAddress.setValue("");
		hospitalPhone.setValue("");
		hospitalFaxNo.setValue("");
		hospitalDocName.setValue("");
	}*/

/*	private void setUpAutoState(AutocompleteField<State> search) {
		search.setQueryListener(new AutocompleteQueryListener<State>() {
			@Override
			public void handleUserQuery(AutocompleteField<State> field, String query) {
				selectedState = null;
				selectedCity = null;
				handleStateSearchQuery(field, query);
			}
		});
	}*/
/*
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
	}

	private void handleStateSearchQuery(AutocompleteField<State> field, String query) {

		List<State> stateSearch = masterService
				.stateSearch(query.toLowerCase());

		if (stateSearch != null && !stateSearch.isEmpty()) {
			for (State state : stateSearch) {
				field.addSuggestion(state, state.getValue());
			}
		} else {
			Notification.show("Please Select Valid State");
			selectedState = null;
			selectedCity = null;
		}
		selectedCity = null;
		cmbCity.setValue(null);
		cmbCity.setText("");
		hospitalName.setValue(null);
		hospitalName.setText("");
		selectedHospital = null;
		clearHospitalDetails();
	}
*/
/*	private void handleCitySearchQuery(
			AutocompleteField<CityTownVillage> field, String query) {
		if (selectedState == null) {
			Notification.show("Please Select Valid State");
		} else {
			List<CityTownVillage> citySearch = masterService.citySearch(
					query.toLowerCase(), selectedState);

			if (citySearch != null && !citySearch.isEmpty()) {
				for (CityTownVillage city : citySearch) {
					field.addSuggestion(city, city.getValue());
				}
			} else {
				Notification.show("Please Select Valid City");
				selectedCity = null;
			}

			hospitalName.setValue(null);
			hospitalName.setText("");
			selectedHospital = null;
			clearHospitalDetails();
		}
	}*/

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

	private void handleHospitalSearchQuery(AutocompleteField<HospitalDto> field, String query) {

		List<Hospitals> hospitalSearch = hospitalService.hospitalNameCriteriaSearch(query, selectedState, selectedCity);

		if (!hospitalSearch.isEmpty()) {
			List<HospitalDto> hospitalDtoList = new ArrayList<HospitalDto>();
			for (Hospitals hospital : hospitalSearch) {
				HospitalDto resultHospitalDto = new HospitalDto(hospital);
				hospitalDtoList.add(resultHospitalDto);
			}

			for (HospitalDto hospitalDto : hospitalDtoList) {
				field.addSuggestion(hospitalDto, hospitalDto.getName());
			}
		} else {

		}

	}
	
	private TabSheet buildBenefitsAvailedTabSheet(){
		
		benefitsAvailedTab.removeAllComponents();
		
		benefitsAvailedTab.setSizeFull();
		benefitsAvailedTab.setStyleName(ValoTheme.TABSHEET_FRAMED);
		
		TabSheet consultationTab = getConsultationTab();
		benefitsAvailedTab.setHeight("100.0%");
		benefitsAvailedTab.addTab(consultationTab, "Consultation Details", null);
		if(bean.getDiagnosis()){
			TabSheet diagnosticsTab = getDiagnosticsTab();
			benefitsAvailedTab.setHeight("100.0%");
			benefitsAvailedTab.addTab(diagnosticsTab, "Diagnostics Details", null);
		}
		if(bean.getPhysiotherapy()){
			TabSheet physiotherapyTab = getPhysiotherapyTab();
			benefitsAvailedTab.setHeight("100.0%");
			benefitsAvailedTab.addTab(physiotherapyTab, "Physiotherapy Details", null);
		}
		if(bean.getMedicine()){
			TabSheet medcineTab = getMedicineTab();;
			benefitsAvailedTab.setHeight("100.0%");
			benefitsAvailedTab.addTab(medcineTab, "Medicine Details", null);
		}

		
		return benefitsAvailedTab;
	}
	
	private TabSheet getConsultationTab(){
		TabSheet consultationTab = new TabSheet();
		consultationTab.hideTabs(true);
		consultationTab.setWidth("100%");
		consultationTab.setHeight("100%");
		consultationTab.setSizeFull();
		
		consultationTabPage = consultationTabPageInst.get();
		consultationTabPage.initView(bean);
		
		VerticalLayout consultLayout = new VerticalLayout(consultationTabPage);
		consultationTab.addComponent(consultLayout);
		
		return consultationTab;
	}
	
	private TabSheet getDiagnosticsTab(){
		TabSheet diagnosticTab = new TabSheet();
		diagnosticTab.hideTabs(true);
		diagnosticTab.setWidth("100%");
		diagnosticTab.setHeight("100%");
		diagnosticTab.setSizeFull();
		
		diagnosticTabChange = diagnosticTabChangeInst.get();
		diagnosticTabChange.initView(bean);
		
		VerticalLayout diagnosticLayout = new VerticalLayout(diagnosticTabChange);
		diagnosticTab.addComponent(diagnosticLayout);
		
		return diagnosticTab;
	}
	
	private TabSheet getPhysiotherapyTab(){
		TabSheet physiotherapyTab = new TabSheet();
		physiotherapyTab.hideTabs(true);
		physiotherapyTab.setWidth("100%");
		physiotherapyTab.setHeight("100%");
		physiotherapyTab.setSizeFull();
		
		physioTherapyTabPage = physioTherapyTabPageInst.get();
		physioTherapyTabPage.initView(bean);
		
		VerticalLayout physiotherapyLayout = new VerticalLayout(physioTherapyTabPage);
		physiotherapyTab.addComponent(physiotherapyLayout);
		
		return physiotherapyTab;
	}
	
	private TabSheet getMedicineTab(){
		TabSheet medicineTab = new TabSheet();
		medicineTab.hideTabs(true);
		medicineTab.setWidth("100%");
		medicineTab.setHeight("100%");
		medicineTab.setSizeFull();
		
		medicineTabPage = medicineTabPageInst.get();
		medicineTabPage.initView(bean);
		
		VerticalLayout medicineLayout = new VerticalLayout(medicineTabPage);
		medicineTab.addComponent(medicineLayout);
		
		return medicineTab;
	}
	
	@SuppressWarnings("deprecation")
	public void benefitsAvailedValueChangeListner(){
		chkDiagnositics
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue()){
					Boolean value = (Boolean) event.getProperty().getValue();
					if(value){
						TabSheet diagnosticsTab = getDiagnosticsTab();
						benefitsAvailedTab.setHeight("100.0%");
						benefitsAvailedTab.addTab(diagnosticsTab, "Diagnostics Details", null);
						benefitsAvailedTab.setSelectedTab(diagnosticsTab);
					} else {
						
						if(chkConsultation != null && chkConsultation.getValue()){
							consultationTabPage.setValues();
						}
						if(chkPhysiotherapy != null && chkPhysiotherapy.getValue()){
							physioTherapyTabPage.setValues();
						}
						if(chkMedicine != null && chkMedicine.getValue()){
							medicineTabPage.setValues();
						}
						diagnosticTabChange.resetValues();
						benefitsAvailedTab.removeAllComponents();
						TabSheet consultationTab = getConsultationTab();
						TabSheet diagnosticsTab = getDiagnosticsTab();
						TabSheet physiotherapyTab = getPhysiotherapyTab();
						TabSheet medicineTab = getMedicineTab();
						benefitsAvailedTab.setHeight("100.0%");
						benefitsAvailedTab.addTab(consultationTab, "Consultation Details", null);
						if(chkDiagnositics != null && chkDiagnositics.getValue()){
							benefitsAvailedTab.addTab(diagnosticsTab, "Diagnostics Details", null);
						}
						if(chkPhysiotherapy != null && chkPhysiotherapy.getValue()){
							benefitsAvailedTab.addTab(physiotherapyTab, "Physiotherapy Details", null);
						}
						if(chkMedicine != null && chkMedicine.getValue()){
							benefitsAvailedTab.addTab(medicineTab, "Medicine Details", null);
						}
						/*if(chkDiagnositics != null && chkDiagnositics.getValue()){
							TabSheet consultationTab = getConsultationTab();
							TabSheet diagnosticsTab = getDiagnosticsTab();
							benefitsAvailedTab.setHeight("100.0%");
							benefitsAvailedTab.addTab(consultationTab, "Consultation Details", null);
							benefitsAvailedTab.addTab(diagnosticsTab, "Diagnostics Details", null);
						} else if(chkPhysiotherapy != null && chkPhysiotherapy.getValue()){
							TabSheet physiotherapyTab = getPhysiotherapyTab();
							TabSheet consultationTab = getConsultationTab();
							benefitsAvailedTab.setHeight("100.0%");
							benefitsAvailedTab.addTab(consultationTab, "Consultation Details", null);
							benefitsAvailedTab.addTab(physiotherapyTab, "Physiotherapy", null);
						} else if(chkMedicine != null && chkMedicine.getValue()){
							TabSheet medcineTab = getMedicineTab();
							TabSheet consultationTab = getConsultationTab();
							benefitsAvailedTab.setHeight("100.0%");
							benefitsAvailedTab.addTab(consultationTab, "Consultation Details", null);
							benefitsAvailedTab.addTab(medcineTab, "Medicine Details", null);
						} else {
							TabSheet consultationTab = getConsultationTab();
							benefitsAvailedTab.setHeight("100.0%");
							benefitsAvailedTab.addTab(consultationTab, "Consultation Details", null);
						}*/
					}
				}
			}
		});
		
		chkPhysiotherapy
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue()){
					Boolean value = (Boolean) event.getProperty().getValue();
					if(value){
						TabSheet physiotherapyTab = getPhysiotherapyTab();
						benefitsAvailedTab.setHeight("100.0%");
						benefitsAvailedTab.addTab(physiotherapyTab, "Physiotherapy Details", null);
						benefitsAvailedTab.setSelectedTab(physiotherapyTab);
					} else {
						if(chkConsultation != null && chkConsultation.getValue()){
							consultationTabPage.setValues();
						}
						if(chkDiagnositics != null && chkDiagnositics.getValue()){
							diagnosticTabChange.setValues();
						}
						if(chkMedicine != null && chkMedicine.getValue()){
							medicineTabPage.setValues();
						}
						physioTherapyTabPage.resetValues();
						benefitsAvailedTab.removeAllComponents();
						TabSheet consultationTab = getConsultationTab();
						TabSheet diagnosticsTab = getDiagnosticsTab();
						TabSheet physiotherapyTab = getPhysiotherapyTab();
						TabSheet medicineTab = getMedicineTab();
						benefitsAvailedTab.setHeight("100.0%");
						benefitsAvailedTab.addTab(consultationTab, "Consultation Details", null);
						if(chkDiagnositics != null && chkDiagnositics.getValue()){
							benefitsAvailedTab.addTab(diagnosticsTab, "Diagnostics Details", null);
						}
						if(chkPhysiotherapy != null && chkPhysiotherapy.getValue()){
							benefitsAvailedTab.addTab(physiotherapyTab, "Physiotherapy Details", null);
						}
						if(chkMedicine != null && chkMedicine.getValue()){
							benefitsAvailedTab.addTab(medicineTab, "Medicine Details", null);
						}
						/*if(chkDiagnositics != null && chkDiagnositics.getValue()){
							TabSheet consultationTab = getConsultationTab();
							TabSheet diagnosticsTab = getDiagnosticsTab();
							TabSheet physiotherapyTab = getPhysiotherapyTab();
							TabSheet medicineTab = getMedicineTab();
							benefitsAvailedTab.setHeight("100.0%");
							benefitsAvailedTab.addTab(consultationTab, "Consultation Details", null);
							benefitsAvailedTab.addTab(diagnosticsTab, "Diagnostics Details", null);
							if(chkPhysiotherapy != null && chkPhysiotherapy.getValue()){
								benefitsAvailedTab.addTab(physiotherapyTab, "Physiotherapy", null);
							}
							if(chkMedicine != null && chkMedicine.getValue()){
								benefitsAvailedTab.addTab(medicineTab, "Medicine Details", null);
							}
						} else if(chkPhysiotherapy != null && chkPhysiotherapy.getValue()){
							TabSheet physiotherapyTab = getPhysiotherapyTab();
							TabSheet consultationTab = getConsultationTab();
							benefitsAvailedTab.setHeight("100.0%");
							benefitsAvailedTab.addTab(consultationTab, "Consultation Details", null);
							benefitsAvailedTab.addTab(physiotherapyTab, "Physiotherapy", null);
						} else if(chkMedicine != null && chkMedicine.getValue()){
							TabSheet physiotherapyTab = getMedicineTab();
							TabSheet consultationTab = getConsultationTab();
							benefitsAvailedTab.setHeight("100.0%");
							benefitsAvailedTab.addTab(consultationTab, "Consultation Details", null);
							benefitsAvailedTab.addTab(physiotherapyTab, "Medicine Details", null);
//							benefitsAvailedTab.addTab(consultationTab, "Consultation Details", null);
						} else {
							TabSheet consultationTab = getConsultationTab();
							benefitsAvailedTab.setHeight("100.0%");
							benefitsAvailedTab.addTab(consultationTab, "Consultation Details", null);
						}*/
					}
				}
			}
		});
		
		chkMedicine
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue()){
					Boolean value = (Boolean) event.getProperty().getValue();
					if(value){
						TabSheet medicineTab = getMedicineTab();
						benefitsAvailedTab.setHeight("100.0%");
						benefitsAvailedTab.addTab(medicineTab, "Medicine Details", null);
						benefitsAvailedTab.setSelectedTab(medicineTab);
					} else {
						if(chkConsultation != null && chkConsultation.getValue()){
							consultationTabPage.setValues();
						}
						if(chkDiagnositics != null && chkDiagnositics.getValue()){
							diagnosticTabChange.setValues();
						}
						if(chkPhysiotherapy != null && chkPhysiotherapy.getValue()){
							physioTherapyTabPage.setValues();
						}
						medicineTabPage.resetValues();
						benefitsAvailedTab.removeAllComponents();
						TabSheet consultationTab = getConsultationTab();
						TabSheet diagnosticsTab = getDiagnosticsTab();
						TabSheet physiotherapyTab = getPhysiotherapyTab();
						TabSheet medicineTab = getMedicineTab();
						benefitsAvailedTab.setHeight("100.0%");
						benefitsAvailedTab.addTab(consultationTab, "Consultation Details", null);
						if(chkDiagnositics != null && chkDiagnositics.getValue()){
							benefitsAvailedTab.addTab(diagnosticsTab, "Diagnostics Details", null);
						}
						if(chkPhysiotherapy != null && chkPhysiotherapy.getValue()){
							benefitsAvailedTab.addTab(physiotherapyTab, "Physiotherapy Details", null);
						}
						if(chkMedicine != null && chkMedicine.getValue()){
							benefitsAvailedTab.addTab(medicineTab, "Medicine Details", null);
						}
						/*if(chkDiagnositics != null && chkDiagnositics.getValue()){
							TabSheet consultationTab = getConsultationTab();
							TabSheet diagnosticsTab = getDiagnosticsTab();
							TabSheet physiotherapyTab = getPhysiotherapyTab();
							TabSheet medicineTab = getMedicineTab();
							benefitsAvailedTab.setHeight("100.0%");
							benefitsAvailedTab.addTab(consultationTab, "Consultation Details", null);
							benefitsAvailedTab.addTab(diagnosticsTab, "Diagnostics Details", null);
							if(chkPhysiotherapy != null && chkPhysiotherapy.getValue()){
								benefitsAvailedTab.addTab(physiotherapyTab, "Physiotherapy", null);
							}
							if(chkMedicine != null && chkMedicine.getValue()){
								benefitsAvailedTab.addTab(medicineTab, "Medicine Details", null);
							}
						} else if(chkPhysiotherapy != null && chkPhysiotherapy.getValue()){
							TabSheet physiotherapyTab = getPhysiotherapyTab();
							TabSheet consultationTab = getConsultationTab();
							TabSheet diagnosticsTab = getDiagnosticsTab();
							TabSheet medicineTab = getMedicineTab();
							benefitsAvailedTab.setHeight("100.0%");
							benefitsAvailedTab.addTab(consultationTab, "Consultation Details", null);
							if(chkDiagnositics != null && chkDiagnositics.getValue()){
								benefitsAvailedTab.addTab(diagnosticsTab, "Diagnostics Details", null);
							}
							benefitsAvailedTab.addTab(physiotherapyTab, "Physiotherapy", null);
							if(chkMedicine != null && chkMedicine.getValue()){
								benefitsAvailedTab.addTab(medicineTab, "Medicine Details", null);
							}
						} else if(chkMedicine != null && chkMedicine.getValue()){
							TabSheet medicineTab = getMedicineTab();
							TabSheet consultationTab = getConsultationTab();
							TabSheet diagnosticsTab = getDiagnosticsTab();
							TabSheet physiotherapyTab = getPhysiotherapyTab();
							benefitsAvailedTab.setHeight("100.0%");
							benefitsAvailedTab.addTab(consultationTab, "Consultation Details", null);
							if(chkDiagnositics != null && chkDiagnositics.getValue()){
								benefitsAvailedTab.addTab(diagnosticsTab, "Diagnostics Details", null);
							}
							if(chkPhysiotherapy != null && chkPhysiotherapy.getValue()){
								benefitsAvailedTab.addTab(physiotherapyTab, "Physiotherapy", null);
							}
							benefitsAvailedTab.addTab(medicineTab, "Medicine Details", null);
						} else {
							TabSheet consultationTab = getConsultationTab();
							benefitsAvailedTab.setHeight("100.0%");
							benefitsAvailedTab.addTab(consultationTab, "Consultation Details", null);
						}*/
					
					}
				}
			}
		});
		
		chkConsultation
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue()){
					Boolean value = (Boolean) event.getProperty().getValue();
					if(value){
						TabSheet consultationTab = getConsultationTab();
						benefitsAvailedTab.setHeight("100.0%");
						benefitsAvailedTab.addTab(consultationTab, "Consultation Details", null);
					} else {

						benefitsAvailedTab.removeAllComponents();
						if(chkDiagnositics != null && chkDiagnositics.getValue()){
							TabSheet consultationTab = getConsultationTab();
							TabSheet diagnosticsTab = getDiagnosticsTab();
							benefitsAvailedTab.setHeight("100.0%");
							benefitsAvailedTab.addTab(diagnosticsTab, "Diagnostics Details", null);
							benefitsAvailedTab.addTab(consultationTab, "Consultation Details", null);
						} else if(chkPhysiotherapy != null && chkPhysiotherapy.getValue()){
							TabSheet physiotherapyTab = getPhysiotherapyTab();
							TabSheet consultationTab = getConsultationTab();
							benefitsAvailedTab.setHeight("100.0%");
							benefitsAvailedTab.addTab(physiotherapyTab, "Physiotherapy Details", null);
							benefitsAvailedTab.addTab(consultationTab, "Consultation Details", null);
						} else if(chkMedicine != null && chkMedicine.getValue()){
							TabSheet medicineTab = getMedicineTab();
							TabSheet consultationTab = getConsultationTab();
							benefitsAvailedTab.setHeight("100.0%");
							benefitsAvailedTab.addTab(medicineTab, "Medicine Details", null);
							benefitsAvailedTab.addTab(consultationTab, "Consultation Details", null);
						} else {
							TabSheet consultationTab = getConsultationTab();
							benefitsAvailedTab.setHeight("100.0%");
							benefitsAvailedTab.addTab(consultationTab, "Consultation Details", null);
						}
					
					}
				}
			}
		});
	}

	public boolean validatePage() {
		Boolean hasError = false;
		errMsg.setLength(0);
		
		/*if(cmbClaimType.getValue() == null){
			hasError = true;
			errMsg.append("Please select any Claim Type </br>");
		}*/
		
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

		if(StringUtils.isBlank(hospitalPhone.getValue())){
			hasError = true;
			errMsg.append("Please enter Contact No (Doc. Submitted No) </br>");
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
		
		if(cmbModeOfReceipt != null && cmbModeOfReceipt.getValue() != null && cmbModeOfReceipt.getValue().toString().equalsIgnoreCase("Online")){
			if(physicalDocsReceived.getValue() == null){
				hasError = true;
				errMsg.append("Please enter Physical Documents Received </br>");
			}
			
			if(physicalDocsReceivedDate.getValue() == null){
				hasError = true;
				errMsg.append("Please enter Physical Documents Received Date </br>");
			}
		}
		
			if(consultationTabPage.validatePage()){
				hasError = true;
				errMsg.append(consultationTabPage.getErrMsg().toString());
			}
		
		if(chkDiagnositics != null && chkDiagnositics.getValue().equals(Boolean.TRUE)){
			if(diagnosticTabChange.validatePage()){
				hasError = true;
				errMsg.append(diagnosticTabChange.getErrMsg().toString());
			}
		}
		
		if(chkPhysiotherapy != null && chkPhysiotherapy.getValue().equals(Boolean.TRUE)){
			if(physioTherapyTabPage.validatePage()){
				hasError = true;
				errMsg.append(physioTherapyTabPage.getErrMsg().toString());
			}
		}
		
		if(chkMedicine != null && chkMedicine.getValue().equals(Boolean.TRUE)){
			if(medicineTabPage.validatePage()){
				hasError = true;
				errMsg.append(medicineTabPage.getErrMsg().toString());
			}
		}

		/*if(cmbDOPVisitReason == null){
			hasError = true;
			errMsg.append("Please select any Reason for OP checkup Visit </br>");
		}*/

		/*if(StringUtils.isBlank(provisionAmtTxt.getValue())){
			hasError = true;
			errMsg.append("Please fill the Provision Amount </br>");
		}*/

		/*if(selectedHospital == null){ selectedHospital has to be set on binding value....
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
		
//		private TextField docSubmittedName;
//		private PopupDateField docReceivedDate;
//		private ComboBox docModeOfReceipt;
//		private TextField docContactNo;
//		private TextField docEmailId;
		
		/*if(StringUtils.isBlank(docSubmittedName.getValue())){
			hasError = true;
			errMsg.append("Please provide Doc.Submitted Name </br>");
		}
		
		if(docReceivedDate.getValue() == null){
			hasError = true;
			errMsg.append("Please provide Documents Received Date </br>");
		}
		
		if(docModeOfReceipt.getValue() == null){
			hasError = true;
			errMsg.append("Please provide Mode of Receipt </br>");
		}
		
		if(StringUtils.isBlank(docContactNo.getValue())){
			hasError = true;
			errMsg.append("Please provide Contact No </br>");
		}
		
		if(StringUtils.isBlank(docEmailId.getValue())){
			hasError = true;
			errMsg.append("Please provide Email ID </br>");
		}
		
		if(!hasError){
			if(diagnosisListenerTableObj.isValid()){
				List<DiagnosisDetailsOPTableDTO> listOfDiagnosis = diagnosisListenerTableObj.getValues();
				if(diagnosisListenerTableObj.deletedDTO != null && diagnosisListenerTableObj.deletedDTO.size() > 0){
					for(DiagnosisDetailsOPTableDTO rec : diagnosisListenerTableObj.deletedDTO){
						listOfDiagnosis.add(rec);
					}
				}

				bean.setDeletedDiagnosisListenerTableList(diagnosisListenerTableObj.deletedDTO);
				bean.setDiagnosisListenerTableList(listOfDiagnosis);
			}
		}*/	
		
		if(!hasError){
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
			// OP Online Portal Intimations section Issue
			bean.getDocumentDetails().setConsultationType((SelectValue)cmbConsultationType.getValue());
			if(chkEmergency != null && chkEmergency.getValue() !=null && chkEmergency.getValue().equals(Boolean.TRUE)){
				bean.getDocumentDetails().setEmergencyFlag(SHAConstants.YES_FLAG);
			}
			if(chkAccident != null && chkAccident.getValue() !=null && chkAccident.getValue().equals(Boolean.TRUE)){
				bean.getDocumentDetails().setAccidentFlag(SHAConstants.YES_FLAG);
			}
			if(physicalDocsReceived != null && physicalDocsReceived.getValue() != null){
				bean.getDocumentDetails().setPhysicalDocsReceivedFlag(physicalDocsReceived.getValue().equals(Boolean.TRUE) ? "Y" : "N");
			}
			if(physicalDocsReceivedDate != null && physicalDocsReceivedDate.getValue() != null){
				bean.getDocumentDetails().setPhysicalDocsReceivedDate(physicalDocsReceivedDate.getValue());
			}

			consultationTabPage.setValues();
			if(chkConsultation != null && chkConsultation.getValue().equals(Boolean.TRUE)){
				bean.setConsulation(Boolean.TRUE);

			}
			if(chkDiagnositics != null && chkDiagnositics.getValue().equals(Boolean.TRUE)){
				bean.setDiagnosis(Boolean.TRUE);
				diagnosticTabChange.setValues();
			
			} else if(chkDiagnositics != null && chkDiagnositics.getValue().equals(Boolean.FALSE)){
				bean.setDiagnosis(Boolean.FALSE);
				if(diagnosticTabChange != null){
					diagnosticTabChange.resetValues();
				}
			}
			if(chkPhysiotherapy != null && chkPhysiotherapy.getValue().equals(Boolean.TRUE)){
				bean.setPhysiotherapy(Boolean.TRUE);
				physioTherapyTabPage.setValues();
			} else if(chkPhysiotherapy != null && chkPhysiotherapy.getValue().equals(Boolean.FALSE)){
				bean.setPhysiotherapy(Boolean.FALSE);
				if(physioTherapyTabPage != null){
					physioTherapyTabPage.resetValues();
				}
			}
			if(chkMedicine != null && chkMedicine.getValue().equals(Boolean.TRUE)){
				bean.setMedicine(Boolean.TRUE);
				medicineTabPage.setValues();
			} else if(chkMedicine != null && chkMedicine.getValue().equals(Boolean.FALSE)){
				bean.setMedicine(Boolean.FALSE);
				if(medicineTabPage != null){
					medicineTabPage.resetValues();
				}
			}
		}
		return hasError;
	}

	@SuppressWarnings("static-access")
	public void showErrorMessage(String eMsg) {
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
	
	private Boolean isValidEmail(String strEmail)
	{
		String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern validEmailPattern = Pattern.compile(emailPattern);
		Matcher validMatcher = validEmailPattern.matcher(strEmail);
		return validMatcher.matches();
	}
	
	public void setTabSheetValues(){
		
		/*if(chkConsultation != null && chkConsultation.getValue() != null && chkConsultation.getValue().equals(Boolean.TRUE)){
			consultationTabPage.setValues();
		}
		
		if(chkDiagnositics != null && chkDiagnositics.getValue() != null && chkDiagnositics.getValue().equals(Boolean.TRUE)){
			diagnosticTabChange.setValues();
		}
		
		if(chkPhysiotherapy != null && chkPhysiotherapy.getValue() != null && chkPhysiotherapy.getValue().equals(Boolean.TRUE)){
			physioTherapyTabPage.setValues();
		}
		
		if(chkMedicine != null && chkMedicine.getValue() != null && chkMedicine.getValue().equals(Boolean.TRUE)){
			medicineTabPage.setValues();
		}*/
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

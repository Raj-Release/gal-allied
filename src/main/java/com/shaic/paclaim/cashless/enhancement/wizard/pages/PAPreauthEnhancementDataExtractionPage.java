package com.shaic.paclaim.cashless.enhancement.wizard.pages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.utils.StarCommonUtils;
import com.shaic.claim.preauth.PreauthCoordinatorView;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDataExtaractionDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.preauth.wizard.dto.SpecialityDTO;
import com.shaic.claim.preauth.wizard.pages.SpecialityTable;
import com.shaic.claim.premedical.dto.NoOfDaysCell;
import com.shaic.domain.InsuredPedDetails;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Policy;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.State;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.paclaim.cashless.enhancement.wizard.wizardfiles.PAPreauthEnhancemetWizardPresenter;
import com.shaic.paclaim.cashless.listenertables.PADiganosisDetailsListenerForPremedical;
import com.shaic.paclaim.cashless.listenertables.PANewClaimedAmountTable;
import com.shaic.paclaim.cashless.listenertables.PAProcedureListenerTableForPremedical;
import com.shaic.paclaim.cashless.listenertables.PASectionDetailsListenerTable;
import com.shaic.paclaim.health.reimbursement.listenertable.PANewProcedureTableList;
import com.vaadin.v7.data.Property;
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
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class PAPreauthEnhancementDataExtractionPage extends ViewComponent implements WizardStep<PreauthDTO> {

	private static final long serialVersionUID = 7875660032093740973L;

	@Inject
	private PreauthDTO bean;
	
	private GWizard wizard;

	@EJB
	private MasterService masterService;
	
	@EJB
	private DBCalculationService dbCalculationService;

	@Inject
	private Instance<PreauthCoordinatorView> preauthCoordinatorView;
	
	@Inject
	private Instance<PAProcedureListenerTableForPremedical> procedureListenerTable;

	@Inject
	private Instance<SpecialityTable> specialityTableList;
	
	
	@Inject
	private Instance<PADiganosisDetailsListenerForPremedical> diagnosisListnerTable;
	
	@Inject
	private Instance<PANewClaimedAmountTable> claimedAmountDetailsTable;

	@Inject
	private Instance<PANewProcedureTableList> newProcedureTableList;
	
	@Inject
	private Instance<PASectionDetailsListenerTable> sectionDetailsListenerTable;
	
	private PASectionDetailsListenerTable sectionDetailsListenerTableObj;

	private PAProcedureListenerTableForPremedical procedureTableObj;

	private PANewProcedureTableList newProcedurdTableObj;

	private PreauthCoordinatorView preauthCoordinatorViewInstance;
	
	private PANewClaimedAmountTable claimedDetailsTableObj;

	private BeanFieldGroup<PreauthDataExtaractionDTO> binder;

	private TextField referenceNoTxt;

	private TextField reasonForAdmissionTxt;
	
	private OptionGroup workOrNonWorkSpace;

	private DateField admissionDate;

	private TextField noOfDaysTxt;

	private ComboBox cmbNatureOfTreatment;

	private PopupDateField firstConsultantDate;

	private CheckBox corpBufferChk;

	private TextField automaticRestorationTxt;

	private CheckBox criticalIllnessChk;

	private ComboBox cmbSpecifyIllness;

	private ComboBox cmbRoomCategory;
	
	private OptionGroup ventilatorSupportOption;

	private ComboBox cmbTreatmentType;

	private TextArea treatmentRemarksTxt;

	private ComboBox cmbIllness;

	private ComboBox cmbPatientStatus;

	private DateField deathDate;

	private TextField txtReasonForDeath;

	private ComboBox cmbTerminateCover;
	
	private OptionGroup interimOfFinalEnhancement;
	
	private TextField changeOfDOA;
	
	private TextField preauthApprovedAmt;
	
	// -------------- Newly added fields --------------//
	
		private ComboBox cmbCauseOfInjury;
		
		private DateField diseaseDetectedDate;
		
		private DateField deliveryDate;
		
		private ComboBox cmbHospitalisationDueTo;
		
		private TextField firNumberTxt;

		private OptionGroup policeReportedAttached;
		
		private DateField injuryDate;
		
		private OptionGroup medicalLegalCase;
		
		private OptionGroup reportedToPolice;
		
		private ComboBox cmbTypeOfDelivery;
		
		private ComboBox cmbSection;
		
		//private ComboBox cmbCatastrophicLoss;
		
		private ComboBox cmbNatureOfLoss;
				
		private ComboBox cmbCauseOfLoss;
		
		// -------------- Newly added fields --------------//

	private FormLayout firstFLayout;

	private FormLayout secondFLayout;
	
	private FormLayout gpaLayout1;
	
	private FormLayout gpaLayout2;

	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();

	private Map<String, Object> referenceData;

	VerticalLayout wholeVLayout;

	VerticalLayout tableVLayout;

	private SpecialityTable specialityTableObj;

	private PADiganosisDetailsListenerForPremedical diagnosisListenerTableObj;

	private Map<String, Property.ValueChangeListener> listenerMap = new HashMap<String, Property.ValueChangeListener>();

	private HorizontalLayout dynamicElementsHLayout;

	private FormLayout treatmentFLayout;

	private FormLayout patientStatusFLayout;
	
	private OptionGroup changeInDiagnosis;
	
	private TextField field;  

	private BeanItemContainer<State> container = new BeanItemContainer<State>(
			State.class);

	private DateField dateofDischarge;
	
	private Integer changeIndiaganosisRows = 0;
	
	private Boolean changeInDiagSelected = false;
	
	private Boolean isValid = false;
	
	private TextField organisationName;
	
	private TextField sumInsured;
	
	private TextField parentName;
	
	private DateField dateOfBirth;
	
	private TextField riskName;
	
	private TextField age;
	
	private TextField sectionOrclass;
	
	private ComboBox cmbCategory;
	
	private DateField riskDOB;
	
	private TextField riskAge;
	
	private DateField dateOfAccident;
	
	private DateField dateOfDeath;
	
	private DateField dateOfDisablement;
	
	///R1006
	private TextField txtAmtClaimed;
	
	private TextField txtDiscntHospBill;
	
	private TextField txtNetAmt;
	

	@Override
	public String getCaption() {
		return "Data Extraction";
	}

	public void init(PreauthDTO bean, GWizard wizard ) {
		this.bean = bean;
		this.wizard = wizard;
	}

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {

	}

	public void initBinder() {
		this.binder = new BeanFieldGroup<PreauthDataExtaractionDTO>(
				PreauthDataExtaractionDTO.class);
		this.binder.setItemDataSource(this.bean
				.getPreauthDataExtractionDetails());
	}
	
//	protected Collection<Boolean> getReadioButtonOptions() {
//		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
//		coordinatorValues.add(true);
//		coordinatorValues.add(false);
//		
//		return coordinatorValues;
//	}
	
	public Boolean alertMessageForPED() {
   		Label successLabel = new Label(
				"<b style = 'color: red;'>" + SHAConstants.PED_RAISE_MESSAGE + "</b>",
				ContentMode.HTML);
   		final Boolean isClicked = false;
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
//		dialog.setCaption("Alert");
		dialog.setClosable(false);
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				bean.setIsPEDInitiated(false);
				if(bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				} else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY) && bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE) && bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE)) {
					StarCommonUtils.alertMessage(getUI(), SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
				}
			}
		});
		return true;
	}
	
	public Boolean alertMessageForAutoRestroation() {
   		Label successLabel = new Label(
				"<b style = 'color: red;'>" + SHAConstants.AUTO_RESTORATION_MESSAGE + "</b>",
				ContentMode.HTML);
   		final Boolean isClicked = false;
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
//		dialog.setCaption("Alert");
		dialog.setClosable(false);
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				bean.setIsAutoRestorationDone(true);
				if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY) && bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE) && bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE)) {
						StarCommonUtils.alertMessage(getUI(), SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
				}
			}
		});
		return true;
	}

	@Override
	public Component getContent() {
		DBCalculationService dbService = new DBCalculationService();
		String memberType = dbService.getCMDMemberType(this.bean.getPolicyKey());
		if(null != memberType && !memberType.isEmpty() && memberType.equalsIgnoreCase(SHAConstants.CMD)){
			showCMDAlert();
		}
		else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
			poupMessageForProduct();
		}else if(!bean.getIsPopupMessageOpened() && this.bean.getClaimCount() >1){
				alertMessageForClaimCount(this.bean.getClaimCount());
		}
		else if(!bean.getSuspiciousPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()){
			suspiousPopupMessage();
		} else if(bean.getIsPEDInitiated()) {
			alertMessageForPED();
		} else if(bean.getIsAutoRestorationDone()) {
			alertMessageForAutoRestroation();
		} else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY) && bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE) && bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE)) {
			StarCommonUtils.alertMessage(getUI(), SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
		}
		if(bean.getNewIntimationDTO().getHospitalDto()	 != null 
				&& bean.getNewIntimationDTO().getHospitalDto().getDiscount() != null
				&& SHAConstants.CAPS_YES.equalsIgnoreCase(bean.getNewIntimationDTO().getHospitalDto().getDiscount())) {
			String hsptRemark = bean.getNewIntimationDTO().getHospitalDto().getDiscountRemark();
			if(hsptRemark != null){
				SHAUtils.showMessageBoxWithCaption(SHAConstants.HOSPITAL_DISCOUNT_ALERT_MSG + hsptRemark,"Information - Hospital Discount");
			}
		}
		initBinder();
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());

		referenceNoTxt = (TextField) binder.buildAndBind("Reference No",
				"referenceNo", TextField.class);
		referenceNoTxt.setWidth("210px");
		referenceNoTxt.setEnabled(false);
		reasonForAdmissionTxt = (TextField) binder.buildAndBind(
				"Reason For Admission", "reasonForAdmission", TextField.class);
		reasonForAdmissionTxt.setEnabled(false);
		admissionDate = (DateField) binder.buildAndBind(
				"Date of Admission", "admissionDate", DateField.class);
		noOfDaysTxt = (TextField) binder.buildAndBind("No of Days", "noOfDays",
				TextField.class);
		noOfDaysTxt.setMaxLength(4);
		/**
		 * Adding validation for noOfDaysTxt field.
		 * */
		
		workOrNonWorkSpace = (OptionGroup) binder.buildAndBind(
				"Accident", "workOrNonWorkPlace", OptionGroup.class);
		workOrNonWorkSpace.addItems(getReadioButtonOptions());
		workOrNonWorkSpace.setItemCaption(true, "WorkPlace");
		workOrNonWorkSpace.setItemCaption(false, "NonWorkPlace");
		workOrNonWorkSpace.setStyleName("horizontal");
		workOrNonWorkSpace.setEnabled(false);
		
		
		CSValidator noOfDaysValidator = new CSValidator();
		
		noOfDaysValidator.extend(noOfDaysTxt);
		noOfDaysValidator.setRegExp("^[0-9]*$");
		noOfDaysValidator.setPreventInvalidTyping(true);
		cmbNatureOfTreatment = (ComboBox) binder.buildAndBind(
				"Nature of Treatment", "natureOfTreatment", ComboBox.class);
		cmbNatureOfTreatment.setEnabled(true);
		firstConsultantDate = (PopupDateField) binder.buildAndBind(
				"1st Consultation Date", "firstConsultantDate",
				PopupDateField.class);
		corpBufferChk = (CheckBox) binder.buildAndBind("", "corpBuffer",
				CheckBox.class);
		
		//Added for corp buffer checkbox validation.
		editCorpBufferChk();
		
		criticalIllnessChk = (CheckBox) binder.buildAndBind("Critical Illness",
				"criticalIllness", CheckBox.class);
		criticalIllnessChk.setEnabled(false);
		cmbRoomCategory = (ComboBox) binder.buildAndBind("Room Category",
				"roomCategory", ComboBox.class);
		
		ventilatorSupportOption = (OptionGroup) binder.buildAndBind(
				"Ventilator Support", "ventilatorSupport", OptionGroup.class);
		ventilatorSupportOption.addItems(getReadioButtonOptions());
		ventilatorSupportOption.setItemCaption(true, "Yes");
		ventilatorSupportOption.setItemCaption(false, "No");
		ventilatorSupportOption.setStyleName("horizontal");
		ventilatorSupportOption.setEnabled(false);
		
		cmbSpecifyIllness = (ComboBox) binder.buildAndBind("Specify",
				"specifyIllness", ComboBox.class);
		cmbSpecifyIllness.setReadOnly(true);
		cmbTreatmentType = (ComboBox) binder.buildAndBind("Treatment Type",
				"treatmentType", ComboBox.class);
		cmbPatientStatus = (ComboBox) binder.buildAndBind("Patient Status",
				"patientStatus", ComboBox.class);
		automaticRestorationTxt = (TextField) binder.buildAndBind(
				"Automatic Restoration", "autoRestoration", TextField.class);
		automaticRestorationTxt.setEnabled(false);
		cmbIllness = (ComboBox) binder.buildAndBind("Illness", "illness",
				ComboBox.class);
		
		if((SHAConstants.AUTO_RESTORATION_NOTAPPLICABLE).equals(automaticRestorationTxt.getValue()) || (SHAConstants.AUTO_RESTORATION_NOTDONE).equals(automaticRestorationTxt.getValue()))
		{
			cmbIllness.setEnabled(false);
		}
		
		preauthApprovedAmt = (TextField) binder.buildAndBind(
				"Total Approved Amt", "preauthTotalApprAmt", TextField.class);
		preauthApprovedAmt.setEnabled(false);
		
		//dateofDischarge = new DateField("Date Of Discharge");
		//dateofDischarge.setEnabled(false);
//		dateofDischarge = (DateField) binder.buildAndBind(
//				"Date of Discharge", "dischargeDate", DateField.class);
		
		dateofDischarge = new DateField("Date of Discharge");
		dateofDischarge.setDateFormat("dd/MM/yyyy");
		dateofDischarge.setEnabled(false);
		dateofDischarge.setValidationVisible(false);
		//if(null != this.bean.getPreauthDataExtractionDetails() && null != this.bean.getPreauthDataExtractionDetails().getDischargeDate())
		/*if((null != this.bean.getPreauthDataExtractionDetails() && null != this.bean.getPreauthDataExtractionDetails().getDischargeDate())
				&& ("F").equalsIgnoreCase(this.bean.getPreauthDataExtractionDetails().getInterimOrFinalEnhancementFlag())) 
		{
			dateofDischarge.setValue(this.bean.getPreauthDataExtractionDetails().getDischargeDate());
			dateofDischarge.setEnabled(true);
			dateofDischarge.setRequired(true);
		}
		else
		{
			dateofDischarge.setEnabled(false);
			dateofDischarge.setRequired(false);
		}*/
		
		/*if(null != this.bean.getPreauthDataExtractionDetails() && null != this.bean.getPreauthDataExtractionDetails().getDischargeDate())
		{
			dateofDischarge.setValue(this.bean.getPreauthDataExtractionDetails().getDischargeDate());
		}*/
		
		interimOfFinalEnhancement = (OptionGroup) binder.buildAndBind( "", "interimOrFinalEnhancement", OptionGroup.class);
		interimOfFinalEnhancement.addItems(getReadioButtonOptions());
		interimOfFinalEnhancement.setItemCaption(true, "Interim Enhancement");
		interimOfFinalEnhancement.setItemCaption(false, "Final Enhancement");
		interimOfFinalEnhancement.setStyleName("horizontal");
		interimOfFinalEnhancement.setCaption("Enhancement Type");
		
		
		
		
		changeInDiagnosis = new OptionGroup("Change In Diagnosis");
		changeInDiagnosis.addItems(getReadioButtonOptions());
		changeInDiagnosis.setItemCaption(true, "Yes");
		changeInDiagnosis.setItemCaption(false, "No");
		changeInDiagnosis.setStyleName("horizontal");
		
		field  = new TextField("Treatment Remarks");
		
		changeOfDOA = new TextField("Reason For Change in DOA");
		changeOfDOA.setNullRepresentation("");
		//changeOfDOA.setEnabled(false);
		changeOfDOA.setEnabled(true);
		changeOfDOA.setRequired(true);
		
		changeOfDOA.setVisible(false);
		if(null != this.bean.getPreauthDataExtractionDetails() && null != this.bean.getPreauthDataExtractionDetails().getChangeOfDOA() 
				&& !("").equalsIgnoreCase(this.bean.getPreauthDataExtractionDetails().getChangeOfDOA()))
		{
			changeOfDOA.setValue(this.bean.getPreauthDataExtractionDetails().getChangeOfDOA());
			changeOfDOA.setVisible(true);

		}
		
		
		FormLayout illnessFLayout = new FormLayout(criticalIllnessChk);
		illnessFLayout.setCaption("Critical Illness");
		illnessFLayout.setMargin(false);
		FormLayout bufferFLayout = new FormLayout(corpBufferChk);
		bufferFLayout.setCaption("Corp Buffer");
		bufferFLayout.setMargin(false);
		
		cmbHospitalisationDueTo = (ComboBox) binder.buildAndBind(
				"Hospitalisation Due to", "hospitalisationDueTo", ComboBox.class);
		cmbHospitalisationDueTo.setEnabled(false);
		
		cmbSection = (ComboBox) binder.buildAndBind(
				"Section", "section", ComboBox.class);
		
		/*cmbCatastrophicLoss = (ComboBox) binder.buildAndBind(
				"Catastrophe Loss", "catastrophicLoss", ComboBox.class);*/
		
		cmbNatureOfLoss = (ComboBox) binder.buildAndBind(
				"Nature Of Loss", "natureOfLoss", ComboBox.class);
		
		cmbCauseOfLoss = (ComboBox) binder.buildAndBind(
				"Cause Of Loss", "causeOfLoss", ComboBox.class);
		
		if(this.bean.getNewIntimationDTO() != null && !ReferenceTable.getSectionKeys().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) ) {
			cmbSection.setEnabled(false);
		}
		
		OptionGroup accDeath  = (OptionGroup) binder.buildAndBind("Accident / Death",
				"accidentOrDeath", OptionGroup.class);
		
		Collection<Boolean> accDeathValues = new ArrayList<Boolean>(2);
		accDeathValues.add(true);
		accDeathValues.add(false);
		
		accDeath.addItems(accDeathValues);
		accDeath.setStyleName("horizontal");
		
		accDeath.setItemCaption(true, "Accident");
		accDeath.setItemCaption(false, "Death");
		
		
		DateField dtOfAccDeath = (DateField) binder.buildAndBind("Date of Accident / Death",
				"dateOfDeathAcc", DateField.class);
		
		
		organisationName = (TextField) binder.buildAndBind("Organisation Name", "organisationName", TextField.class);
		organisationName.setEnabled(false);
		
		sumInsured = (TextField) binder.buildAndBind("Sum Insured", "paSumInsured", TextField.class);
		sumInsured.setEnabled(false);
		
		parentName = (TextField) binder.buildAndBind("Parent Name", "parentName", TextField.class);
		//parentName.setEnabled(false);
		
		dateOfBirth = (DateField) binder.buildAndBind("Date Of Birth", "dateOfBirth", DateField.class);
	//	dateOfBirth.setEnabled(false);
		
		riskName = (TextField) binder.buildAndBind("Risk Name", "riskName", TextField.class);
	//	riskName.setEnabled(false);
		
		age = (TextField) binder.buildAndBind("Age", "age", TextField.class);
	//	age.setEnabled(false);
		
		riskDOB = (DateField) binder.buildAndBind("Risk(DOB)", "gpaRiskDOB", DateField.class);
		
		riskAge = (TextField) binder.buildAndBind("Risk(Age)", "gpaRiskAge", TextField.class);
		
		sectionOrclass = (TextField) binder.buildAndBind("Section/Class", "gpaSection", TextField.class);
		sectionOrclass.setEnabled(false);
		
		cmbCategory = (ComboBox) binder.buildAndBind("Category", "gpaCategory", ComboBox.class);	
		cmbCategory.setEnabled(false);
		BeanItemContainer<SelectValue> selectValueForCategory = dbCalculationService.getGPACategory(bean.getNewIntimationDTO().getPolicy().getKey());
		setDropDownValues(selectValueForCategory);
		
		dateOfAccident = (DateField) binder.buildAndBind("Date of Accident",
				"dateOfAccident", DateField.class);
		
		dateOfDeath = (DateField) binder.buildAndBind("Date of Death",
				"dateOfDeath", DateField.class);
		
		dateOfDisablement = (DateField) binder.buildAndBind("Date of Disablement",
				"dateOfDisablement", DateField.class);


		firstFLayout = new FormLayout(accDeath,dateOfDisablement,interimOfFinalEnhancement, dateofDischarge, referenceNoTxt, admissionDate, changeOfDOA,
				cmbRoomCategory,ventilatorSupportOption,illnessFLayout, cmbSpecifyIllness, preauthApprovedAmt,cmbHospitalisationDueTo);
		firstFLayout.setSpacing(true);
		firstFLayout.setMargin(false);
		secondFLayout = new FormLayout(dateOfAccident,dateOfDeath,reasonForAdmissionTxt,workOrNonWorkSpace, noOfDaysTxt,
				cmbNatureOfTreatment, firstConsultantDate, bufferFLayout,
				automaticRestorationTxt, cmbIllness, cmbSection,/*cmbCatastrophicLoss,*/cmbNatureOfLoss,cmbCauseOfLoss);
		secondFLayout.setMargin(false);
		
		gpaLayout1 = new FormLayout(organisationName,sumInsured,sectionOrclass,cmbCategory,parentName,dateOfBirth,age);
		gpaLayout1.setSpacing(true);
		gpaLayout1.setMargin(false);
		gpaLayout2 = new FormLayout(riskName,riskDOB,riskAge);
		
		this.diagnosisListenerTableObj =   diagnosisListnerTable.get();
		this.diagnosisListenerTableObj.init(this.bean, "preauthEnhancement");

		HorizontalLayout formHLayout = new HorizontalLayout(firstFLayout,
				secondFLayout);
//		formHLayout.setWidth("100%");
		
		HorizontalLayout gpaLayout = new HorizontalLayout(gpaLayout1,gpaLayout2);
		gpaLayout.setCaption("UNNAMED RISK DETAILS");
		
		PreauthCoordinatorView preauthCoordinatorViewInstance = preauthCoordinatorView
				.get();
		preauthCoordinatorViewInstance.setWizard(wizard,SHAConstants.PRE_AUTH_ENHANCEMENT);
		this.preauthCoordinatorViewInstance = preauthCoordinatorViewInstance;
		preauthCoordinatorViewInstance.init(this.bean);
		

		tableVLayout = new VerticalLayout();
		tableVLayout.setSpacing(true);
		treatmentFLayout = new FormLayout(cmbTreatmentType);
		treatmentFLayout.setMargin(false);
		patientStatusFLayout = new FormLayout(cmbPatientStatus);
		patientStatusFLayout.setMargin(false);
		dynamicElementsHLayout = new HorizontalLayout(treatmentFLayout,
				patientStatusFLayout);
		dynamicElementsHLayout.setWidth("100%");
		dynamicElementsHLayout.setMargin(true);

		claimedDetailsTableObj = claimedAmountDetailsTable.get();
		claimedDetailsTableObj.initView(this.bean,SHAConstants.PROCESS_ENHANCEMENT);
		
		this.sectionDetailsListenerTableObj = sectionDetailsListenerTable.get();
		this.sectionDetailsListenerTableObj.init(this.bean, SHAConstants.PRE_AUTH_ENHANCEMENT);
		if(this.referenceData != null) {
			this.sectionDetailsListenerTableObj.setReferenceData(referenceData);
		}
		this.sectionDetailsListenerTableObj.addBeanToList(bean.getPreauthDataExtractionDetails().getSectionDetailsDTO());
		
		//R1006
		txtAmtClaimed = binder.buildAndBind("Amount Claimed" , "amtClaimed",TextField.class);
		txtAmtClaimed.setNullRepresentation("");
		txtAmtClaimed.setMaxLength(15);

		CSValidator ClaimedAmtValidator = new CSValidator();
		ClaimedAmtValidator.extend(txtAmtClaimed);
		ClaimedAmtValidator.setRegExp("^[0-9]*$");
		ClaimedAmtValidator.setPreventInvalidTyping(true);
		
		txtAmtClaimed.addBlurListener(getclaimedAmtListener());

		txtDiscntHospBill = binder.buildAndBind("Discount in Hospital Bill" , "disCntHospBill",TextField.class);
		txtDiscntHospBill.setNullRepresentation("");
		txtDiscntHospBill.setMaxLength(15);

		CSValidator disCntHospBillAmtValidator = new CSValidator();
		disCntHospBillAmtValidator.extend(txtDiscntHospBill);
		disCntHospBillAmtValidator.setRegExp("^[0-9]*$");
		disCntHospBillAmtValidator.setPreventInvalidTyping(true);

		txtNetAmt = binder.buildAndBind("Claimed amount after Discount" , "netAmt",TextField.class);
		txtNetAmt.setNullRepresentation("");
		txtNetAmt.setMaxLength(15);
		
		txtNetAmt.setEnabled(false);

		CSValidator netAmtValidator = new CSValidator();
		netAmtValidator.extend(txtNetAmt);
		netAmtValidator.setRegExp("^[0-9]*$");
		netAmtValidator.setPreventInvalidTyping(true);

		txtDiscntHospBill.addBlurListener(getHospDiscountBillListener());

		FormLayout claimedAmtFLayout = new FormLayout(txtAmtClaimed);
		claimedAmtFLayout.setMargin(false);
		FormLayout disCntFLayout = new FormLayout(txtDiscntHospBill);
		disCntFLayout.setMargin(false);
		FormLayout netAmtFLayout = new FormLayout(txtNetAmt);
		netAmtFLayout.setMargin(false);
		HorizontalLayout hospDiscountHLayout = new HorizontalLayout(claimedAmtFLayout,
				disCntFLayout, netAmtFLayout);
		hospDiscountHLayout.setWidth("100%");
		
		VerticalLayout lLayout = new VerticalLayout();
		lLayout.addComponent(hospDiscountHLayout);
		lLayout.addComponent(claimedDetailsTableObj);
		lLayout.setMargin(true);
		
		HorizontalLayout insuranceDiagnosis = new HorizontalLayout();
		insuranceDiagnosis.addComponent(getInsuranceDiagnosisDetailsPanel());
		
		 FormLayout changeInDiagnosisform =new FormLayout(changeInDiagnosis) ;
		 changeInDiagnosisform.setMargin(false);
		//wholeVLayout = new VerticalLayout(diagnosisListenerTableObj, formHLayout, new FormLayout(changeInDiagnosis) , diagnosisListenerTableObj,  dynamicElementsHLayout, tableVLayout, claimedDetailsTableObj ,  preauthCoordinatorViewInstance);
		/*As per satish sir instruction change in diagnosis removed*/
		 wholeVLayout = new VerticalLayout( formHLayout,sectionDetailsListenerTableObj,insuranceDiagnosis,/*changeInDiagnosisform,*/ diagnosisListenerTableObj,  dynamicElementsHLayout, tableVLayout, lLayout);
		
		if(null != bean && null != bean.getNewIntimationDTO() && null != bean.getNewIntimationDTO().getPolicy() &&
				null != bean.getNewIntimationDTO().getPolicy().getProduct() && null != bean.getNewIntimationDTO().getPolicy().getProduct().getKey() &&
				(ReferenceTable.getGPAProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) &&
				(SHAConstants.GPA_UN_NAMED_POLICY_TYPE.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getGpaPolicyType()))){
			
			wholeVLayout = new VerticalLayout( formHLayout,gpaLayout,sectionDetailsListenerTableObj,insuranceDiagnosis,/*changeInDiagnosisform,*/ diagnosisListenerTableObj,  dynamicElementsHLayout, tableVLayout, lLayout);
		}
		wholeVLayout.setSpacing(false);
		wholeVLayout.setMargin(false);
		
		addListener();
		addDiagnosisNameChangeListener();
		mandatoryFields.add(interimOfFinalEnhancement);
		mandatoryFields.add(admissionDate);
		mandatoryFields.add(cmbRoomCategory);
		mandatoryFields.add(cmbTreatmentType);
		mandatoryFields.add(cmbPatientStatus);

		showOrHideValidation(false);
		return wholeVLayout;
	}
	
	private void alertMessageForClaimCount(Long claimCount){
		
		String msg = SHAConstants.CLAIM_COUNT_MESSAGE+claimCount;
		
		String additionalMessage = SHAConstants.ADDITIONAL_MESSAGE;
		
   		Label successLabel = new Label(
				"<b style = 'color: black;'>"+msg+"</b>",
				ContentMode.HTML);
		
   		if(this.bean.getClaimCount() >2){
	   		successLabel = new Label(
					"<b style = 'color: black;'>"+msg+"<br>"
							+ additionalMessage+"</b>",
					ContentMode.HTML);
   		}
//   		successLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
//   		successLabel.addStyleName(ValoTheme.LABEL_BOLD);
   		successLabel.addStyleName(ValoTheme.LABEL_H3);
   		Button homeButton = new Button("ok");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
   		VerticalLayout firstForm = new VerticalLayout(successLabel,homeButton);
   		firstForm.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
//   		firstForm.setHeight("1003px");
		Panel panel = new Panel();
		panel.setContent(firstForm);
		
		if(this.bean.getClaimCount() > 1 && this.bean.getClaimCount() <=2){
			panel.addStyleName("girdBorder1");
		}else if(this.bean.getClaimCount() >2){
			panel.addStyleName("girdBorder2");
		}
		
//		panel.setHeight("143px");
		panel.setSizeFull();
		
		
		final Window popup = new com.vaadin.ui.Window();
		popup.setWidth("45%");
		popup.setHeight("20%");
//		popup.setCaption("Alert");
//		popup.setContent( viewDocumentDetailsPage);
		popup.setContent(panel);
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
		
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				bean.setIsPopupMessageOpened(true);
				popup.close();
				
				if(!bean.getSuspiciousPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()){
					suspiousPopupMessage();
				} else if(bean.getIsPEDInitiated()) {
					alertMessageForPED();
				} else if(bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				} else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY) && bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE) && bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE)) {
					StarCommonUtils.alertMessage(getUI(), SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
				}
				
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
		
		//admissionDate.setValue(this.bean.getDateOfAdmission());
		admissionDate.setValue(this.bean.getPreauthDataExtractionDetails().getAdmissionDate());
		reasonForAdmissionTxt.setValue(this.bean.getReasonForAdmission());
		reasonForAdmissionTxt.setEnabled(false);
		BeanItemContainer<SelectValue> treatementType = (BeanItemContainer<SelectValue>) referenceData
				.get("treatmentType");
		BeanItemContainer<SelectValue> roomCategory = (BeanItemContainer<SelectValue>) referenceData
				.get("roomCategory");
		BeanItemContainer<SelectValue> natureOfTreatment = (BeanItemContainer<SelectValue>) referenceData
				.get("natureOfTreatment");
		BeanItemContainer<SelectValue> patientStatus = (BeanItemContainer<SelectValue>) referenceData
				.get("patientStatus");
		BeanItemContainer<SelectValue> illness = (BeanItemContainer<SelectValue>) referenceData
				.get("illness");
		
		BeanItemContainer<SelectValue> criticalIllness = (BeanItemContainer<SelectValue>) referenceData
				.get("criticalIllness");
		
		BeanItemContainer<SelectValue> hospitalizationDueTo = (BeanItemContainer<SelectValue>) referenceData
				.get("hospitalisationDueTo");
		
		BeanItemContainer<SelectValue> section = (BeanItemContainer<SelectValue>) referenceData
				.get("section");
		
		BeanItemContainer<SelectValue> catastrophicLoss = (BeanItemContainer<SelectValue>) referenceData
				.get("catastrophicLoss");
		
		BeanItemContainer<SelectValue> natureOfLoss = (BeanItemContainer<SelectValue>) referenceData
				.get("natureOfLoss");
		
		BeanItemContainer<SelectValue> causeOfLoss = (BeanItemContainer<SelectValue>) referenceData
				.get("causeOfLoss");
		
		this.bean.getPolicyDto().setAdmissionDate(admissionDate.getValue());
		this.bean.getPolicyDto().setClaimKey(this.bean.getClaimKey());
		
		if(cmbSection != null && cmbSection.getValue() != null){
			SelectValue sectionValue = (SelectValue) cmbSection.getValue();
			this.bean.getPreauthDataExtractionDetails().setSection(sectionValue);
		}
		
		
		fireViewEvent(PAPreauthEnhancemetWizardPresenter.SET_DB_DETAILS_TO_REFERENCE_DATA, this.bean);
		
		cmbTreatmentType.setContainerDataSource(treatementType);
		cmbTreatmentType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbTreatmentType.setItemCaptionPropertyId("value");

		cmbRoomCategory.setContainerDataSource(roomCategory);
		cmbRoomCategory.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbRoomCategory.setItemCaptionPropertyId("value");

		if(this.bean.getPolicyDto().getProduct().getNonAllopathicFlag() != null && this.bean.getPolicyDto().getProduct().getNonAllopathicFlag().toLowerCase().equalsIgnoreCase("n")) {
			List<SelectValue> itemIds2 = natureOfTreatment.getItemIds();
			List<SelectValue> allopathicValues = new ArrayList<SelectValue>();
			for (SelectValue selectValue : itemIds2) {
				if(!selectValue.getValue().toString().toLowerCase().contains("non")) {
					allopathicValues.add(selectValue);
				}
			}
			natureOfTreatment.removeAllItems();
			natureOfTreatment.addAll(allopathicValues);
		}
		
		cmbNatureOfTreatment.setContainerDataSource(natureOfTreatment);
		cmbNatureOfTreatment.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbNatureOfTreatment.setItemCaptionPropertyId("value");

		cmbPatientStatus.setContainerDataSource(patientStatus);
		cmbPatientStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPatientStatus.setItemCaptionPropertyId("value");

		cmbIllness.setContainerDataSource(illness);
		cmbIllness.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbIllness.setItemCaptionPropertyId("value");
		cmbIllness.setNullSelectionAllowed(false);
		
		cmbSpecifyIllness.setReadOnly(false);
		cmbSpecifyIllness.setContainerDataSource(criticalIllness);
		cmbSpecifyIllness.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbSpecifyIllness.setItemCaptionPropertyId("value");
		
		cmbSpecifyIllness.setReadOnly(true);
		
		cmbHospitalisationDueTo.setContainerDataSource(hospitalizationDueTo);
		cmbHospitalisationDueTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbHospitalisationDueTo.setItemCaptionPropertyId("value");
		
		cmbSection.setContainerDataSource(section);
		cmbSection.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbSection.setItemCaptionPropertyId("value");
		
		/*cmbCatastrophicLoss.setContainerDataSource(catastrophicLoss);
		cmbCatastrophicLoss.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCatastrophicLoss.setItemCaptionPropertyId("value");*/
		
		cmbNatureOfLoss.setContainerDataSource(natureOfLoss);
		cmbNatureOfLoss.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbNatureOfLoss.setItemCaptionPropertyId("value");
		
		cmbCauseOfLoss.setContainerDataSource(causeOfLoss);
		cmbCauseOfLoss.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCauseOfLoss.setItemCaptionPropertyId("value");
		
		if (this.bean.getPreauthDataExtractionDetails().getSection() != null) {
			this.cmbSection.setValue(this.bean
					.getPreauthDataExtractionDetails().getSection());
		}
		
		/*if(this.bean.getPreauthDataExtractionDetails().getCatastrophicLoss() != null) {
			this.cmbCatastrophicLoss.setValue(this.bean.getPreauthDataExtractionDetails().getCatastrophicLoss());
			}*/
			
		
		if(this.bean.getPreauthDataExtractionDetails().getNatureOfLoss() != null) {
			
			this.cmbNatureOfLoss.setValue(this.bean.getPreauthDataExtractionDetails().getNatureOfLoss());
		}
		
		if(this.bean.getPreauthDataExtractionDetails().getCauseOfLoss() != null) {
			this.cmbCauseOfLoss.setValue(this.bean.getPreauthDataExtractionDetails().getCauseOfLoss());
		}
		
		if (this.bean.getPreauthDataExtractionDetails().getHospitalisationDueTo() != null) {
			this.cmbHospitalisationDueTo.setValue(this.bean.getPreauthDataExtractionDetails().getHospitalisationDueTo());
		}
		
		List<SelectValue> natureOfTreatmentIDS = natureOfTreatment.getItemIds();
		for (SelectValue selectValue : natureOfTreatmentIDS) {
			if(!selectValue.getId().equals(ReferenceTable.NON_ALLOPATHIC_ID)) {
				this.bean.getPreauthDataExtractionDetails().setNatureOfTreatment(selectValue);
			}
		}
		cmbNatureOfTreatment.setEnabled(false);
		
		if (this.bean.getPreauthDataExtractionDetails().getNatureOfTreatment() != null) {
			this.cmbNatureOfTreatment.setValue(this.bean
					.getPreauthDataExtractionDetails().getNatureOfTreatment());
		}

		if (this.bean.getPreauthDataExtractionDetails().getRoomCategory() != null) {
			this.cmbRoomCategory.setValue(this.bean
					.getPreauthDataExtractionDetails().getRoomCategory());
		}
		
		if (this.bean.getPreauthDataExtractionDetails().getVentilatorSupport()!= null) {
			System.out.println(String.format("Ventilator Support value already exist: [%s]", this.bean.getPreauthDataExtractionDetails().getVentilatorSupport()));
			this.ventilatorSupportOption.setValue(this.bean.getPreauthDataExtractionDetails().getVentilatorSupport()? true : false);
			ventilatorSupportOption.setEnabled(true);
			ventilatorSupportOption.setRequired(true);
		}
		
		if (this.bean.getPreauthDataExtractionDetails().getSpecifyIllness() != null) {
			this.cmbSpecifyIllness.setReadOnly(false);
			this.cmbSpecifyIllness.setValue(this.bean
					.getPreauthDataExtractionDetails().getSpecifyIllness());
		}

		if (this.bean.getPreauthDataExtractionDetails().getTreatmentType() != null) {
			this.cmbTreatmentType.setValue(this.bean
					.getPreauthDataExtractionDetails().getTreatmentType());
		}

		if (this.bean.getPreauthDataExtractionDetails().getPatientStatus() != null) {
			this.cmbPatientStatus.setValue(this.bean
					.getPreauthDataExtractionDetails().getPatientStatus());
		}

		if (this.bean.getPreauthDataExtractionDetails().getIllness() != null) {
			this.cmbIllness.setValue(this.bean
					.getPreauthDataExtractionDetails().getIllness());
		}

		
		if(null != this.bean.getPreauthDataExtractionDetails().getInterimOrFinalEnhancementFlag() && this.bean.getPreauthDataExtractionDetails().getInterimOrFinalEnhancementFlag().equalsIgnoreCase("I") ) {
			interimOfFinalEnhancement.select(true);
			setInterimOrFinalEnhancement(true);
		} else if(null != this.bean.getPreauthDataExtractionDetails().getInterimOrFinalEnhancementFlag() && this.bean.getPreauthDataExtractionDetails().getInterimOrFinalEnhancementFlag().equalsIgnoreCase("F")) {
			interimOfFinalEnhancement.select(false);
			setInterimOrFinalEnhancement(false);
		}
		
		this.preauthCoordinatorViewInstance.setUpReference(referenceData);
//		this.claimedDetailsTableObj.setDBCalculationValues((Map<String, Object>)referenceData.get("claimDBDetails"));
		
		this.diagnosisListenerTableObj.setReferenceData(referenceData);
		
		if(this.procedureTableObj != null) {
			this.procedureTableObj.setReferenceData(referenceData);
		}
		
		if(cmbNatureOfTreatment != null) {
			cmbNatureOfTreatment.setEnabled(false);
		}
		
		this.sectionDetailsListenerTableObj.setReferenceData(referenceData);
		setTableValues();
	}

	@Override
	public boolean onAdvance() {
		setTableValuesToDTO();
		
		if (validatePage()) {
			if (this.bean.getIsReferTOFLP()) {
				return true;
			}

			if (this.bean.getAlertMessageOpened()) {
				this.bean.setAlertMessageOpened(false);
				if (bean.getIsDialysis() && !bean.getDialysisOpened()) {
					alertNoOfSittings();
					return false;
				}
				return true;
			}
			alertMessage();
			return false;

		}else{
				return false;
			}
		 
//		return validatePage();
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

	@SuppressWarnings("static-access")
	public boolean validatePage() {
		Boolean hasError = false;
		showOrHideValidation(true);
		String eMsg = "";
		this.bean.setAmountConsidered(this.claimedDetailsTableObj != null ? this.claimedDetailsTableObj.getTotalPayableAmt().toString() : "0");
		if (!this.binder.isValid()) {

			for (Field<?> field : this.binder.getFields()) {
				ErrorMessage errMsg = ((AbstractField<?>) field)
						.getErrorMessage();
				if (errMsg != null) {
					eMsg += errMsg.getFormattedHtmlMessage();
				}
				hasError = true;
			}
		}
		
//		if(interimOfFinalEnhancement.getValue()== null){
//			hasError = true;
//			eMsg +="Please Choose Interim or Final Enhancement";
//		}

		try {
			if(! this.preauthCoordinatorViewInstance.isValid()){
				hasError = true;
			List<String> errors = this.preauthCoordinatorViewInstance.getErrors();
			for (String error : errors) {
				eMsg += error + "</br>";
			   }
			}
		} catch (CommitException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		/*Below Condition removed as per satish sir instruction - change in diagnosis removed
		if(changeInDiagSelected && changeIndiaganosisRows.equals(this.diagnosisListenerTableObj.getValues().size())) {
			hasError = true;
			eMsg += "Please Delete Atleast one Diagnosis Details</br>"; 
		}*/
		
		if(null != this.cmbTreatmentType && null == this.cmbTreatmentType.getValue())
		{
			hasError = true;
			eMsg += "Please Select Treatment Type. </br>";
		}
		
		if(this.diagnosisListenerTableObj != null && this.diagnosisListenerTableObj.getValues().isEmpty()) {
			hasError = true;
			eMsg += "Please Add Atleast one Diagnosis Details to Proceed Further. </br>"; 
		}
		
		if(this.claimedDetailsTableObj != null && this.claimedDetailsTableObj.getTotalPayableAmt() <= 0){
			hasError = true;
			eMsg += "Claimed Payable amount should not be Zero. </br>";
		}
		if(interimOfFinalEnhancement.getValue() != null && interimOfFinalEnhancement.getValue().toString() == "false") {
			if(dateofDischarge.getValue() == null) {
				hasError = true;
				eMsg += "Please Select Discharge Date. </br>"; 
			} else {
				bean.getPreauthDataExtractionDetails().setDischargeDate(dateofDischarge.getValue());
			}
		}
		/*if(interimOfFinalEnhancement.getValue() != null && interimOfFinalEnhancement.getValue().toString() == "false") {
			if(cmbCatastrophicLoss.getValue() == null) {
				hasError = true;
				eMsg += "Catastrophe Loss is Mandatory. </br>"; 
			}
		}*/
			if(interimOfFinalEnhancement.getValue() != null && interimOfFinalEnhancement.getValue().toString() == "false") {
				if(cmbNatureOfLoss.getValue() == null) {
					hasError = true;
					eMsg += "Nature Of Loss is Manadatory. </br>"; 
				}
			}
				if(interimOfFinalEnhancement.getValue() != null && interimOfFinalEnhancement.getValue().toString() == "false") {
					if(cmbCauseOfLoss.getValue() == null) {
						hasError = true;
						eMsg += "Cause Of Loss is Manadatory. </br>"; 
					}
				}
		if(this.claimedDetailsTableObj != null && !claimedDetailsTableObj.isValid((interimOfFinalEnhancement.getValue() != null && interimOfFinalEnhancement.getValue().toString() == "false"))) {
			hasError = true;
			List<String> errors = this.claimedDetailsTableObj.getErrors();
			for (String error : errors) {
				eMsg += error + "</br>";
			}
		} else {
			Float enteredDays = SHAUtils.convertFloatToString(noOfDaysTxt.getValue());
			if(this.claimedDetailsTableObj != null  && this.claimedDetailsTableObj.getTotalNoOfDays() < (enteredDays)) {
				hasError = true;
				eMsg += "The total of number of days entered against Room Rent and ICU should be lesser or equal to no. of days. </br> Claim Table Value is " + this.claimedDetailsTableObj.getTotalNoOfDays() + " and Data Extraction Value is " + enteredDays + "</br>";
			}
		}
		
//		if(this.claimedDetailsTableObj != null && !claimedDetailsTableObj.isValid()) {
//			hasError = true;
//			eMsg += "Please Enter <b>'Room Rent  or ICU Charges or ANH Package or Composite Package' </b> On Amount Claimed Details Table. </br>";
//		}
		
		if(this.changeOfDOA.isEnabled() && this.changeOfDOA.isVisible() 
				&& !(null != this.changeOfDOA && (null!= this.changeOfDOA.getValue() && !("").equalsIgnoreCase(this.changeOfDOA.getValue()))))
		{
			hasError = true;
			eMsg += "Please enter Reason For Change in DOA to Proceed Further. </br>";
					
		}
		
		
		
		if((this.procedureTableObj != null && this.procedureTableObj.getValues().isEmpty()) && (this.newProcedurdTableObj != null && this.newProcedurdTableObj.getValues().isEmpty())) {
			hasError = true;
			eMsg += "Please Add Atleast one Procedure List Details to Proceed Further. </br>"; 
		}
		
		if(ventilatorSupportOption != null && ventilatorSupportOption.isEnabled() && (ventilatorSupportOption.getValue() == null))
		{
			System.out.println(String.format("Ventilator Support value in validate: [%s]",this.ventilatorSupportOption.getValue()));
			hasError = true;
			eMsg += "Please Select Ventilator Support. </br>";
		}
		
		if(this.procedureTableObj != null && !this.procedureTableObj.getValues().isEmpty()) {
			List<DiagnosisDetailsTableDTO> diagnosisList = this.diagnosisListenerTableObj.getValues();
			List<ProcedureDTO> procedureList = this.procedureTableObj.getValues();
			for (ProcedureDTO procedureDTO : procedureList) {
				SelectValue consider = new SelectValue();
				consider.setId(1l);
				consider.setValue("yes");
				procedureDTO.setConsiderForPayment(consider);
			}
			Boolean isError = false;
			for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisList) {
				for (ProcedureDTO procedureDTO : procedureList) {
					if(procedureDTO.getSublimitName() != null && diagnosisDetailsTableDTO.getSublimitName() != null && procedureDTO.getSublimitName().getLimitId().equals(diagnosisDetailsTableDTO.getSublimitName().getLimitId())) {
						if((null != diagnosisDetailsTableDTO.getConsiderForPayment() && diagnosisDetailsTableDTO.getConsiderForPayment().getId().equals(ReferenceTable.COMMONMASTER_YES)) && (null != procedureDTO.getConsiderForPayment() && procedureDTO.getConsiderForPayment().getId().equals(ReferenceTable.COMMONMASTER_YES))) {
							hasError = true;
							isError = true;
							eMsg += "Same Sublimit is Selected for both Diagnosis and Procedure. </br> Consider For Payment cannot be yes for all the entries for which Same Sublimit is seleced .  </br>";
							break;
						}
					}
				}
				if(isError) {
					break;
				}
			}
		}
		
		Boolean diagnosisIsConsiderForPayment = false;
		Boolean procedureIsConsiderForPayment = false;
		if(this.diagnosisListenerTableObj != null && !this.diagnosisListenerTableObj.getValues().isEmpty()) {
			List<DiagnosisDetailsTableDTO> diagnosisList = this.diagnosisListenerTableObj.getValues();
			for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisList) {
						if((null != diagnosisDetailsTableDTO.getConsiderForPayment() && diagnosisDetailsTableDTO.getConsiderForPayment().getId().equals(ReferenceTable.COMMONMASTER_YES))) {
							diagnosisIsConsiderForPayment =false;
							break;
						} else {
							diagnosisIsConsiderForPayment = true;
						}
					
				
			}
		}
		if(this.procedureTableObj != null && !this.procedureTableObj.getValues().isEmpty()) {
			List<ProcedureDTO> procedureList = this.procedureTableObj.getValues();
			for (ProcedureDTO procedureDTO : procedureList) {
				if(null != procedureDTO.getConsiderForPayment() && procedureDTO.getConsiderForPayment().getId().equals(ReferenceTable.COMMONMASTER_YES)) {
					procedureIsConsiderForPayment = false;
					break;
				} else {
					procedureIsConsiderForPayment = true;
				}
			}
		} else {
				procedureIsConsiderForPayment =true;
		}
		
//		if (diagnosisIsConsiderForPayment && procedureIsConsiderForPayment){
//			hasError = true;
//			eMsg += SHAConstants.DIAGNOSIS_ERROR_MSG +"</br>";
//		}
		
		String strMsg = validateProcedureAndDiagnosisName();
		if(null != strMsg && !("").equalsIgnoreCase(strMsg))
		{
			eMsg += strMsg;
			hasError = true;
		}
		
		if (this.diagnosisListenerTableObj != null){
			boolean isValid = this.diagnosisListenerTableObj.isValid();
			if(!isValid){
				hasError = true;
				List<String> errors = this.diagnosisListenerTableObj.getErrors();
				for (String error : errors) {
					eMsg += error + "</br>";
				}
			}
		}
		
		
		if (this.procedureTableObj != null) {
			boolean isValid = this.procedureTableObj.isValidPA();
			if (!isValid) {
				hasError = true;
				List<String> errors = this.procedureTableObj.getErrors();
				for (String error : errors) {
					eMsg += error + "</br>";
				}
			}
		}

		if (this.newProcedurdTableObj != null) {
			boolean isValid = this.newProcedurdTableObj.isValid();
			boolean isValidProcedure = this.newProcedurdTableObj.isValidProcedure();
			if (!isValid) {
				hasError = true;
				List<String> errors = this.newProcedurdTableObj.getErrors();
				for (String error : errors) {
					eMsg += error + "</br>";
				}
			}
			
			if (!isValidProcedure) {
				hasError = true;
				List<String> errors = this.newProcedurdTableObj.getProcedureErrors();
				for (String error : errors) {
					eMsg += error + "</br>";
				}
			}
		}
		
//		String value = (this.noOfDaysTxt.getValue() != null && this.noOfDaysTxt.getValue() != "") ? this.noOfDaysTxt.getValue() : "0";
//		
//		if(this.claimedDetailsTableObj != null && !claimedDetailsTableObj.isANHOrCompositeSelected && this.claimedDetailsTableObj.getNoOfDaysValues().compareTo(SHAUtils.getFloatFromString(value)) != 0) {
//			hasError = true;
//			eMsg += "The total of number of days entered against Room Rent and ICU should be equal to no. of days. </br> Claim Table Value is " + this.claimedDetailsTableObj.getNoOfDaysValues() + " and Data Extraction Value is " + value + "</br>";
//		}
//		

		if (this.specialityTableObj != null) {
			boolean isValid = this.specialityTableObj.isValid();
			if (!isValid) {
				hasError = true;
				List<String> errors = this.specialityTableObj.getErrors();
				for (String error : errors) {
					eMsg += error + "</br>";
				}
			}
		}
		
		// Section details included for Comprehensive product. Remaining products, the Hospitalization will be the default value.........
		if(this.sectionDetailsListenerTableObj != null && !sectionDetailsListenerTableObj.isValid(true)) {
			hasError = true;
			List<String> errors = this.sectionDetailsListenerTableObj.getErrors();
			for (String error : errors) {
				eMsg += error + "</br>";
			}
		}
		
		//R1006
		if(this.txtAmtClaimed != null && (this.txtAmtClaimed.getValue() == null || this.txtAmtClaimed.getValue().equalsIgnoreCase("")) ){
			txtAmtClaimed.setValidationVisible(true);
			hasError = true;
			eMsg +="Enter Claimed Amount. </br>";
		}

		if(this.txtDiscntHospBill != null && (this.txtDiscntHospBill.getValue() == null || this.txtDiscntHospBill.getValue().equalsIgnoreCase("")) ){
			txtDiscntHospBill.setValidationVisible(true);
			hasError = true;
			eMsg +="Enter Discount in Hospital Bill. </br>";
		}

		if(this.txtNetAmt != null && this.txtNetAmt.getValue() != null && !this.txtNetAmt.getValue().equalsIgnoreCase("") && this.claimedDetailsTableObj != null){
			Integer totalNetAmtforAmtconsd = claimedDetailsTableObj.getTotalClaimedAmt();
			Integer netAmt = Integer.valueOf(txtNetAmt.getValue());
			if(totalNetAmtforAmtconsd != null && !(totalNetAmtforAmtconsd.equals(netAmt))){
				hasError = true;
				eMsg+="Claimed amount after discount to be equal to Claimed amount. </br>";
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
			return !hasError;
		} else {
			try {
				this.binder.commit();
				this.bean.setAmountConsidered(this.claimedDetailsTableObj.getTotalPayableAmt().toString());
				this.bean.setAmountRequested(this.claimedDetailsTableObj.getTotalClaimedAmt().toString());
				this.bean.setTotalClaimedAmt(Double.valueOf(this.claimedDetailsTableObj.getTotalClaimedAmt()));
				this.bean.setToatlNonPayableAmt(Double.valueOf(this.claimedDetailsTableObj.getTotalDeductableAmount()));
				this.bean.setTotalDeductibleAmount(String.valueOf(this.claimedDetailsTableObj.getTotalDeductibleAmount()));
				this.bean.setEntitlmentNoOfDays(this.claimedDetailsTableObj.getTotalEntitlementNoOfDays());
				if(this.bean.getDeletedDiagnosis().isEmpty()) {
					this.bean.setDeletedDiagnosis(this.diagnosisListenerTableObj.deletedDTO);
				} else {
					List<DiagnosisDetailsTableDTO> deletedDTO = this.diagnosisListenerTableObj.deletedDTO;
					for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : deletedDTO) {
						if(!this.bean.getDeletedDiagnosis().contains(diagnosisDetailsTableDTO)) {
							this.bean.getDeletedDiagnosis().add(diagnosisDetailsTableDTO);
						}
						
					}
				}
			
				if(admissionDate != null && admissionDate.getValue() != null){
					bean.setDateOfAdmission(admissionDate.getValue());
				}
				Date policyFromDate = bean.getNewIntimationDTO().getPolicy().getPolicyFromDate();
				Long diffDays = SHAUtils.getDiffDays(policyFromDate, admissionDate.getValue());
                MastersValue policyType = bean.getNewIntimationDTO().getPolicy().getPolicyType();
				
                if((diffDays != 0 && diffDays > 90) || (policyType != null && policyType.getKey().equals(ReferenceTable.RENEWAL_POLICY)) || !bean.getAdmissionDatePopup()){
					this.bean.setAlertMessageOpened(true);
				}
				
				if(SHAUtils.getDialysisDiagnosisDTO(bean.getPreauthDataExtractionDetails().getDiagnosisTableList()) != null || SHAUtils.getDialysisProcedureDTO(bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList()) != null) {
					this.bean.setIsDialysis(true);
				}
				
				if(this.bean.getPreauthMedicalDecisionDetails().getMedicalDecisionTableDTO() != null 
						&& ! this.bean.getPreauthMedicalDecisionDetails().getMedicalDecisionTableDTO().isEmpty()){
					List<DiagnosisProcedureTableDTO> medicalDecisionTableDTO = new ArrayList<DiagnosisProcedureTableDTO>();
					this.bean.getPreauthMedicalDecisionDetails().setMedicalDecisionTableDTO(medicalDecisionTableDTO);
				}
				
				if(this.bean.getPreauthDataExtractionDetails().getNoOfDays() == null || (this.bean.getPreauthDataExtractionDetails().getNoOfDays() != null
						&& this.bean.getPreauthDataExtractionDetails().getNoOfDays().equalsIgnoreCase(""))){
					this.bean.getPreauthDataExtractionDetails().setNoOfDays("0");
				}
				
				List<NoOfDaysCell> values = this.claimedDetailsTableObj.getValues();
				
				this.bean.setAmbulanceAmountConsidered(this.bean.getAmountConsidered());
				
				if(ReferenceTable.getSeniorCitizenKeys().containsKey(this.bean.getPolicyDto().getProduct().getKey())){
					Integer ambulanceAmount = SHAUtils.isAmbulanceAvailable(values);
					if(ambulanceAmount != null){
						this.bean.setIsAmbulanceApplicable(true);
						this.bean.setAmbulanceLimitAmount(Double.valueOf(ambulanceAmount));
						 Double totalAmountConsidered = SHAUtils.getDoubleFromStringWithComma(this.bean.getAmountConsidered());
					      totalAmountConsidered -= ambulanceAmount;
					      this.bean.setAmbulanceAmountConsidered(totalAmountConsidered.toString());
						
					}else{
						this.bean.setAmbulanceLimitAmount(0d);
						this.bean.setIsAmbulanceApplicable(false);
					}
	        		
	        	}

			} catch (CommitException e) {
				e.printStackTrace();
			}
			showOrHideValidation(false);
			return true;
		}
	}
	
	private String validateProcedureAndDiagnosisName()
	{
		String eMsg = "";
		StringBuffer strBuf = new StringBuffer();
		List<DiagnosisDetailsTableDTO> diagList = this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList();
		List<ProcedureDTO> procList = this.bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList();
		if((null != diagList && !diagList.isEmpty()) && (null != procList && !procList.isEmpty()))
		{
			for(DiagnosisDetailsTableDTO diagObj : diagList)
			{
				String strDiagName = diagObj.getDiagnosis();
				
				for(ProcedureDTO procObj : procList)
				{
					String strProcName = procObj.getProcedureNameValue();
					if(strDiagName.equals(strProcName))
					{
						strBuf.append("Diagnosis and Procedure are same. Diagnosis"+" "+ strDiagName).append("\n");
					}
				}
			}
		}
		
		if(null != strBuf && strBuf.length() > 0)
		{
			eMsg = strBuf.toString();
		}
		
		return eMsg;
	}
	
	private PedDetailsTableDTO setPEDDetailsToDTO(DiagnosisDetailsTableDTO diagnosisDetailsTableDTO,
			InsuredPedDetails pedList) {
		PedDetailsTableDTO dto = new PedDetailsTableDTO();
		dto.setDiagnosisName(diagnosisDetailsTableDTO.getDiagnosis());
		dto.setEnableOrDisable(diagnosisDetailsTableDTO.getEnableOrDisable());
		dto.setPolicyAgeing(diagnosisDetailsTableDTO.getPolicyAgeing());
		if(pedList == null) {
			dto.setPedCode("");
			dto.setPedName("");
		} else {
			dto.setPedCode(pedList.getPedCode());
			dto.setPedName(pedList.getPedDescription());
		}
		return dto;
	}

	@SuppressWarnings({ "static-access", "unchecked" })
	public void setTableValuesToDTO() {
		this.bean.getPreauthDataExtractionDetails().setSpecialityList(
				this.specialityTableObj != null ? this.specialityTableObj
						.getValues() : new ArrayList<SpecialityDTO>());
		this.bean.getPreauthDataExtractionDetails().setNewProcedureList(
				this.newProcedurdTableObj != null ?  getProcedureVariationList(this.newProcedurdTableObj.getValues(), 1l)  : new ArrayList<ProcedureDTO>());
		this.bean.getPreauthDataExtractionDetails().setProcedureList(this.procedureTableObj != null ? getProcedureVariationList(this.procedureTableObj.getValues(), 0l) : new ArrayList<ProcedureDTO>());
		
		this.bean.getPreauthDataExtractionDetails().setDiagnosisTableList(this.diagnosisListenerTableObj.getValues());
		
		List<ProcedureDTO> wholeProcedureList = new ArrayList<ProcedureDTO>();
		wholeProcedureList.addAll(this.bean.getPreauthDataExtractionDetails().getProcedureList());
		wholeProcedureList.addAll(this.bean.getPreauthDataExtractionDetails().getNewProcedureList());
		
		this.bean.getPreauthMedicalProcessingDetails().setProcedureExclusionCheckTableList(wholeProcedureList);
		
		List<DiagnosisDetailsTableDTO> diagnosisList = this.diagnosisListenerTableObj.getValues();
		List<InsuredPedDetails> tmpPEDList = (List<InsuredPedDetails>) referenceData.get("insuredPedList");
		String policyAgeing = (String) referenceData.get("policyAgeing");
		for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisList) {
			if(diagnosisDetailsTableDTO.getDiagnosisName() != null && diagnosisDetailsTableDTO.getDiagnosisName().getValue() != null) {
				diagnosisDetailsTableDTO.setDiagnosis(diagnosisDetailsTableDTO.getDiagnosisName().getValue());
			}
			if(diagnosisDetailsTableDTO.getDiagnosisName() != null) {
				diagnosisDetailsTableDTO.setDiagnosisId(diagnosisDetailsTableDTO.getDiagnosisName().getId());
			}
			diagnosisDetailsTableDTO.setPolicyAgeing(policyAgeing);
			List<PedDetailsTableDTO> list = new ArrayList<PedDetailsTableDTO>();
			if(diagnosisDetailsTableDTO.getPedList().isEmpty()) {
				if(!tmpPEDList.isEmpty()) {
					for (InsuredPedDetails insuredPEDDetails : tmpPEDList) {
						PedDetailsTableDTO setPEDDetailsToDTO = setPEDDetailsToDTO(diagnosisDetailsTableDTO, insuredPEDDetails);
						list.add(setPEDDetailsToDTO);
					}
					
				}  else {
					PedDetailsTableDTO setPEDDetailsToDTO = setPEDDetailsToDTO(diagnosisDetailsTableDTO, null);
					list.add(setPEDDetailsToDTO);
				}
				diagnosisDetailsTableDTO.setPedList(list);
			}
		}
		
		this.bean.getPreauthDataExtractionDetails().setDiagnosisTableList(diagnosisList);

		List<NoOfDaysCell> values = this.claimedDetailsTableObj.getValues();
		List<NoOfDaysCell> claimedDetailsList = this.bean.getPreauthDataExtractionDetails().getClaimedDetailsList();
		
		if(!claimedDetailsList.isEmpty()) {
			for (NoOfDaysCell oldNoOfCell : claimedDetailsList) {
				for (NoOfDaysCell newNoOfDaysCell : values) {
					if(oldNoOfCell.getBenefitId() == newNoOfDaysCell.getBenefitId()) {
						newNoOfDaysCell.setKey(oldNoOfCell.getKey());
					}
				}
			}
		}
		this.bean.setDeletedClaimedAmountIds(this.claimedDetailsTableObj.getDeletedItems());
		this.bean.getPreauthDataExtractionDetails().setClaimedDetailsList(this.claimedDetailsTableObj != null ?  values: new ArrayList<NoOfDaysCell>());
	}

	private List<ProcedureDTO> getProcedureVariationList(List<ProcedureDTO> procedureDTOList, Long isNewProcedure)  {
		if(!procedureDTOList.isEmpty()) {
			for (ProcedureDTO procedureDTO : procedureDTOList) {
				if(isNewProcedure == 0) {
					procedureDTO.setProcedureNameValue(procedureDTO.getProcedureName() != null ? procedureDTO.getProcedureName().getValue() : null);
					procedureDTO.setProcedureCodeValue(procedureDTO.getProcedureCode() != null ? procedureDTO.getProcedureCode().getValue() : null);
				} else {
					SelectValue procedureName = new SelectValue();
					procedureName.setValue(procedureDTO.getProcedureNameValue());
					procedureDTO.setProcedureName(procedureName);
					SelectValue procedureCode = new SelectValue();
					procedureCode.setValue(procedureDTO.getProcedureCodeValue());
					procedureDTO.setProcedureName(procedureCode);
				}
				procedureDTO.setNewProcedureFlag(isNewProcedure);
				procedureDTO.setPolicyAging(referenceData.containsKey("policyAgeing") ? (String)referenceData.get("policyAgeing") : null);
			}
		}
		return procedureDTOList;
	}
	
	private void setTableValues() {
		
		if(this.specialityTableObj != null) {
			List<SpecialityDTO> specialityList = this.bean.getPreauthDataExtractionDetails().getSpecialityList();
			for (SpecialityDTO specialityDTO : specialityList) {
				//specialityDTO.setStatusFlag(true);
				if(null != specialityDTO && null != specialityDTO.getEnableOrDisable() && !specialityDTO.getEnableOrDisable())
				{
					specialityDTO.setEnableOrDisable(false);
				}
				else
				{
					specialityDTO.setEnableOrDisable(true);
				}
				specialityDTO.setEnableOrDisable(true);
				specialityDTO.setStatusFlag(true);
				this.specialityTableObj.addBeanToList(specialityDTO);
			}
		}
		
		List<ProcedureDTO> procedureExclusionCheckTableList = this.bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList();
		List<ProcedureDTO> oldProcedureDTOList = new ArrayList<ProcedureDTO>();
	
		List<ProcedureDTO> newProcedureDTOList = new ArrayList<ProcedureDTO>();
		if(!procedureExclusionCheckTableList.isEmpty()) {
			for (ProcedureDTO procedureDTO : procedureExclusionCheckTableList) {
				if(null != procedureDTO && null != procedureDTO.getEnableOrDisable() && !procedureDTO.getEnableOrDisable())
				{
					procedureDTO.setEnableOrDisable(false);
				}
				else
				{
					procedureDTO.setEnableOrDisable(true);
				}
				procedureDTO.setStatusFlag(true);
				if(procedureDTO.getNewProcedureFlag() == 1) {
					newProcedureDTOList.add(procedureDTO);
				} else {
					oldProcedureDTOList.add(procedureDTO);
				}
			}
		}
		this.bean.getPreauthDataExtractionDetails().setProcedureList(oldProcedureDTOList);
		this.bean.getPreauthDataExtractionDetails().setNewProcedureList(newProcedureDTOList);
		if(this.newProcedurdTableObj != null) {
			List<ProcedureDTO> newProcedureList = this.bean.getPreauthDataExtractionDetails().getNewProcedureList();
			for (ProcedureDTO newProcedureTableDTO : newProcedureList) {
				newProcedureTableDTO.setEnableOrDisable(true);
				this.newProcedurdTableObj.addBeanToList(newProcedureTableDTO);
			}
		}
		
		if(this.procedureTableObj != null) {
			List<ProcedureDTO> procedureList = this.bean.getPreauthDataExtractionDetails().getProcedureList();
			for (ProcedureDTO procedureTableDTO : procedureList) {
				if(null != procedureTableDTO && null != procedureTableDTO.getEnableOrDisable() && !procedureTableDTO.getEnableOrDisable())
				{
					procedureTableDTO.setEnableOrDisable(false);
				}
				else
				{
					procedureTableDTO.setEnableOrDisable(true);
				}
				procedureTableDTO.setEnableOrDisable(true);
				this.procedureTableObj.addBeanToList(procedureTableDTO);
			}
		}
		
		if(this.diagnosisListenerTableObj != null) {
			List<DiagnosisDetailsTableDTO> diagnosisList = this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList();
			for (DiagnosisDetailsTableDTO diagnosisDTO : diagnosisList) {
				//iagnosisDTO.setEnableOrDisable(false);
				if(null != diagnosisDTO && null != diagnosisDTO.getEnableOrDisable() && !diagnosisDTO.getEnableOrDisable())
				{
					diagnosisDTO.setEnableOrDisable(false);
				}
				else
				{
					diagnosisDTO.setEnableOrDisable(true);
				}
				this.diagnosisListenerTableObj.addBeanToList(diagnosisDTO);
			}
		}
		
		if(this.claimedDetailsTableObj != null) {
			this.claimedDetailsTableObj.setValues(this.bean.getPreauthDataExtractionDetails().getClaimedDetailsList(), false);
		}
		
		if(this.bean.getDeletedClaimedAmountIds() != null && !this.bean.getDeletedClaimedAmountIds().isEmpty()) {
			this.claimedDetailsTableObj.setDeletedItems(this.bean.getDeletedClaimedAmountIds());
		}
		
		//Set Amount Requested and Amount Considered to PreauthDTO.
		/**
		 * Amount requested should be the pre auth request amount as per
		 * sathish Sir. Hence the data population from the amount
		 * claims table is commented . The amount requested is directly
		 * populated in the presenter. The same is set medical processing page. 
		 * */
//		this.bean.setAmountRequested(this.claimedDetailsTableObj.getTotalAmountByPropertyName("Amount Requested"));
//		this.bean.setAmountConsidered(this.claimedDetailsTableObj.getTotalAmountByPropertyName("Amount Considered"));
		
		this.bean.setAmountRequested(this.claimedDetailsTableObj.getTotalClaimedAmt().toString());
		this.bean.setAmountConsidered(this.claimedDetailsTableObj.getTotalPayableAmt().toString());
	}

	@Override
	public boolean onBack() {
		return true;
	}

	@Override
	public boolean onSave() {
		setTableValuesToDTO();
		return validatePage();
	}

	public void editSpecifyVisible(Boolean checkValue) {
		if (checkValue) {
			cmbSpecifyIllness.setReadOnly(false);
		} else {
			//cmbSpecifyIllness.setValue("");
			cmbSpecifyIllness.setValue(null);
			cmbSpecifyIllness.setNullSelectionAllowed(true);
			cmbSpecifyIllness.setReadOnly(true);
		}
	}

	protected void addListener() {

		criticalIllnessChk
				.addValueChangeListener(new Property.ValueChangeListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						Boolean checkValue = criticalIllnessChk.getValue();
						fireViewEvent(
								PAPreauthEnhancemetWizardPresenter.CHECK_CRITICAL_ILLNESS,
								checkValue, true);
					}
				});
		UI ui = getUI();
		cmbTreatmentType.setData(ui);
		cmbTreatmentType.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 2697682747976915503L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty().getValue();
				if(value != null && cmbTreatmentType.getValue().toString().toLowerCase()
						.contains("medical")) {
					if(!bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList().isEmpty()) {
						alertMessageForChangeOfTreatmentType();
					} else {
						fireViewEvent(
								PAPreauthEnhancemetWizardPresenter.PREAUTH_TREATMENT_TYPE_CHANGED,
								null);
					}
				} else {
					fireViewEvent(
							PAPreauthEnhancemetWizardPresenter.PREAUTH_TREATMENT_TYPE_CHANGED,
							null);
				}
			}
		});
		
		 cmbRoomCategory.addValueChangeListener(new ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					
					SelectValue value = (SelectValue) event.getProperty().getValue();
					bean.getNewIntimationDTO().setRoomCategory(value);
					
					if(value != null && value.getValue() != null){

						System.out.println(String.format("Room Category Value : [%s]",value.getValue()));
					}

					if(value != null && value.getValue() != null && value.getValue().equalsIgnoreCase("ICU/NICU/ICCU/CCU/PICU")) {
						ventilatorSupportOption.setEnabled(true);
						ventilatorSupportOption.setRequired(true);
						//ventilatorSupportOption.setVisible(true);
						mandatoryFields.add(ventilatorSupportOption);
						//bean.getPreauthDataExtractionDetails().setVentilatorSupport(true);
					} 
					else{
						ventilatorSupportOption.setValue(null);
						//unbindField(ventilatorSupportOption);
						ventilatorSupportOption.setEnabled(false);
						ventilatorSupportOption.setRequired(false);
						mandatoryFields.remove(ventilatorSupportOption);
					}
					
					
					List<ProcedureDTO> procedureList = new ArrayList<ProcedureDTO>();
					
					if(procedureTableObj != null){
//						List<ProcedureDTO> values = procedureTableObj.getValues();
//						procedureList.addAll(values);
//						procedureTableObj.removeRow();
//						for (ProcedureDTO procedureDTO : procedureList) {
////							procedureDTO.setProcedureCode(null);
//							procedureTableObj.addBeanToList(procedureDTO);s
//						}
						
						List<ProcedureDTO> procedureDTO = new ArrayList<ProcedureDTO>();
	  					
	  					procedureDTO = procedureTableObj.getValues();

	  					procedureTableObj.init(bean.getHospitalCode(), "preauthEnhancement", bean);
	  					procedureTableObj.setReferenceData(referenceData);
	  					
	                     for (ProcedureDTO procedureDTO2 : procedureDTO) {
								procedureTableObj.addBeanToList(procedureDTO2);
						}
					}
				
				}
			});
		 
		 /*cmbCatastrophicLoss.addValueChangeListener(new ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					SelectValue value = (SelectValue) event.getProperty().getValue();
					
				}
			});*/
			  
			  cmbNatureOfLoss.addValueChangeListener(new ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
						SelectValue value = (SelectValue) event.getProperty().getValue();
						
					}
				});
			  
			  cmbCauseOfLoss.addValueChangeListener(new ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
						SelectValue value = (SelectValue) event.getProperty().getValue();
						
					}
				});
			  
         cmbSection.addValueChangeListener(new ValueChangeListener() {
  			
  			@Override
  			public void valueChange(ValueChangeEvent event) {
  				
  				if(cmbSection != null && cmbSection.getValue() != null){
  					SelectValue sectionValue = (SelectValue) cmbSection.getValue();
  					bean.getPreauthDataExtractionDetails().setSection(sectionValue);
  				}
  				
  				fireViewEvent(PAPreauthEnhancemetWizardPresenter.SUBLIMIT_CHANGED_BY_SECTION, bean);
  				

  				List<DiagnosisDetailsTableDTO> diagnosisList = new ArrayList<DiagnosisDetailsTableDTO>();
  				
  				diagnosisList = diagnosisListenerTableObj.getValues();
  				
  				
  				diagnosisListenerTableObj.init(bean,"preauthEnhancement");
  				diagnosisListenerTableObj.setReferenceData(referenceData);
  				
  				for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisList) {
  					
  					diagnosisListenerTableObj.addBeanToList(diagnosisDetailsTableDTO);
						
					}

  				if(procedureTableObj != null) {
  					
  					List<ProcedureDTO> procedureDTO = new ArrayList<ProcedureDTO>();
  					
  					procedureDTO = procedureTableObj.getValues();
  				
  					procedureTableObj.init(bean.getHospitalCode(), "preauthEnhancement", bean);
  					procedureTableObj.setReferenceData(referenceData);
  					
                     for (ProcedureDTO procedureDTO2 : procedureDTO) {
							procedureTableObj.addBeanToList(procedureDTO2);
						}
  				}
  				
  				if(claimedDetailsTableObj != null){
 					
 					List<NoOfDaysCell> claimedAmountValues = new ArrayList<NoOfDaysCell>();
 					
 					claimedAmountValues = claimedDetailsTableObj.getValues();
 					
// 					claimedDetailsTableObj.initView(bean, SHAConstants.PROCESS_ENHANCEMENT);
 					
 					Map<Integer,Object> values = (Map<Integer,Object>)referenceData.get("claimDBDetails");
 					claimedDetailsTableObj.setDBCalculationValues(values);
 					for (NoOfDaysCell noOfDaysCell : claimedAmountValues) {
						if(noOfDaysCell.getBenefitId() != null && values.containsKey(noOfDaysCell.getBenefitId().intValue())) {
							noOfDaysCell.setPolicyPerDayPayment(((Double)values.get(noOfDaysCell.getBenefitId().intValue())).intValue());
						}
					}
 					claimedDetailsTableObj.setValuesForSectionChange(claimedAmountValues, false);
 					
 				}
  				
//  				List<ProcedureDTO> procedureList = new ArrayList<ProcedureDTO>();
//  				
//  				if(procedureTableObj != null){
//  					List<ProcedureDTO> values = procedureTableObj.getValues();
//  					procedureList.addAll(values);
//  					procedureTableObj.removeRow();
//  					for (ProcedureDTO procedureDTO : procedureList) {
  				
////  						procedureDTO.setProcedureCode(null);
//  						procedureTableObj.addBeanToList(procedureDTO);
//  					}
//  				}
  			
  			}
  		});
		
		cmbNatureOfTreatment.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 2697682747976915503L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty().getValue();
				if(value != null && value.getValue() != null && value.getValue().toString().toLowerCase().contains("non")) {
					Collection<?> itemIds = cmbTreatmentType.getContainerDataSource().getItemIds();
					cmbTreatmentType.setValue(itemIds.toArray()[0]);
					cmbTreatmentType.setEnabled(false);
				} else {
					cmbTreatmentType.setEnabled(true);
				}
			}
		});

		cmbPatientStatus.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -8435623803385270083L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				fireViewEvent(
						PAPreauthEnhancemetWizardPresenter.PREAUTH_PATIENT_STATUS_CHANGED,
						null);
			}
		});
		
		admissionDate.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -8435623803385270083L;

			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {
				Date enteredDate = (Date) event.getProperty().getValue();
				changeOfDOA.setEnabled(true);
				unbindField(changeOfDOA);
				binder.bind(changeOfDOA, "changeOfDOA");
				changeOfDOA.setRequired(true);
				changeOfDOA.setValidationVisible(false);
				changeOfDOA.setVisible(true);
				mandatoryFields.add(changeOfDOA);
				if(enteredDate != null) {
					Date policyFromDate = bean.getPolicyDto().getPolicyFromDate();
					Date policyToDate = bean.getPolicyDto().getPolicyToDate();
					
					final ConfirmDialog dialog = new ConfirmDialog();
					dialog.setClosable(false);
					dialog.setResizable(false);
					
					if (!(enteredDate.after(policyFromDate)  || enteredDate.compareTo(policyFromDate) == 0) || !(enteredDate.before(policyToDate) || enteredDate.compareTo(policyToDate) == 0)) {
						
						 Button okButton = new Button("OK");
						 okButton.addClickListener(new ClickListener() {
							private static final long serialVersionUID = -7148801292961705660L;

							@Override
							public void buttonClick(ClickEvent event) {
								dialog.close();
							}
						});
						HorizontalLayout hLayout = new HorizontalLayout(okButton);
						hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
						hLayout.setMargin(true);
						VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color: red;'>Admission Date is not in range between Policy From Date and Policy To Date. </b>", ContentMode.HTML));
						layout.setMargin(true);
						layout.setSpacing(true);
						dialog.setContent(layout);
						dialog.setCaption("Error");
						dialog.setClosable(true);
						dialog.show(getUI().getCurrent(), null, true);
						
						event.getProperty().setValue(null);
					}
				}
				
				if(admissionDate.getValue() != null && bean.getNewIntimationDTO().getAdmissionDate() != null && (bean.getNewIntimationDTO().getAdmissionDate().compareTo(admissionDate.getValue()))==0){
					
					changeOfDOA.setValue(null);
					unbindField(changeOfDOA);
					changeOfDOA.setVisible(false);
					changeOfDOA.setRequired(false);
					mandatoryFields.remove(changeOfDOA);
					}
				
			}
		});
		
		interimOfFinalEnhancement.addValueChangeListener(new Property.ValueChangeListener() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Boolean isFinalChecked = false ;
				if(event.getProperty() != null && event.getProperty().getValue().toString() == "true") {
					isFinalChecked = true;
				}
				setInterimOrFinalEnhancement(isFinalChecked);
			}

			
		});
		
		dateofDischarge.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(event.getProperty() != null) {
					final ConfirmDialog dialog = new ConfirmDialog();
					dialog.setClosable(true);
					dialog.setResizable(false);
					DateField property = (DateField) event.getProperty();
					if(property.getValue() != null && admissionDate.getValue() != null && !SHAUtils.validateDischargeDate(property.getValue(), admissionDate.getValue())) {

						 Button okButton = new Button("OK");
						 okButton.addClickListener(new ClickListener() {
							private static final long serialVersionUID = -7148801292961705660L;

							@Override
							public void buttonClick(ClickEvent event) {
								dialog.close();
							}
						});
						HorizontalLayout hLayout = new HorizontalLayout(okButton);
						hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
						hLayout.setMargin(true);
						VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color: red;'>Please select valid Discharge Date.Date of Discharge should not be before Date of Admisssion.</b>", ContentMode.HTML));
						layout.setMargin(true);
						layout.setSpacing(true);
						dialog.setContent(layout);
						dialog.setCaption("Error");
						dialog.setClosable(true);
						dialog.show(getUI().getCurrent(), null, true);
						
						event.getProperty().setValue(null);
					
					}
				}
				
			}
		});
		
		changeInDiagnosis.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Boolean isChecked = false;
				if(event.getProperty() != null && event.getProperty().getValue().toString() == "true") {
					diagnosisListenerTableObj.enableOrDisableDeleteButton(true);
					changeInDiagSelected = true;
					changeIndiaganosisRows = diagnosisListenerTableObj.getValues().size();
					if(procedureTableObj != null) {
						procedureTableObj.enableOrDisableDeleteButton(true);
					}
				} else {
					changeInDiagSelected = false;
					diagnosisListenerTableObj.enableOrDisableDeleteButton(false);
					if(procedureTableObj != null) {
						procedureTableObj.enableOrDisableDeleteButton(false);
					}
					
				}
			}
		});
		if(this.diagnosisListenerTableObj != null) {
			this.diagnosisListenerTableObj.listenerField.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					TextField field = (TextField) event.getProperty();
					if(field.getValue() != null && field.getValue().equalsIgnoreCase("true")) {
						changeInDiagSelected = false;
					}
				}
			});
		}
		
		cmbHospitalisationDueTo.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -2577540521492098375L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty().getValue();
//				if(value != null) {
					fireViewEvent(
							PAPreauthEnhancemetWizardPresenter.PREAUTH_ENHANCEMENT_HOSPITALISATION_DUE_TO,
							value);
//				}
				
			}
		});
		
		this.sectionDetailsListenerTableObj.dummySubCoverField.addValueChangeListener(new ValueChangeListener() {
			
			private static final long serialVersionUID = -7831804284490287934L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				TextField property = (TextField) event.getProperty();
				String value = property.getValue();
				if(value != null) {
					fireViewEvent(PAPreauthEnhancemetWizardPresenter.SET_DB_DETAILS_TO_REFERENCE_DATA, bean);
					if(diagnosisListenerTableObj != null && !diagnosisListenerTableObj.getValues().isEmpty()) {
						diagnosisListenerTableObj.changeSublimitValues();
					} 
					if(procedureTableObj != null && !procedureTableObj.getValues().isEmpty()) {
						procedureTableObj.changeSublimitValues();
					}
				}
			}
		});
		

		dateOfAccident.addValueChangeListener(new Property.ValueChangeListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {
				Policy policy = (Policy) ((DateField) event
						.getProperty()).getData();

				Date enteredDate = (Date) ((DateField) event
						.getProperty()).getValue();
				if (enteredDate != null) {

					try {
						dateOfAccident.validate();
						enteredDate = (Date) event.getProperty()
								.getValue();
					} catch (Exception e) {
						dateOfAccident.setValue(null);
						showErrorMessage("Please Enter a valid Accident Date");
						// Notification.show("Please Enter a valid Date");
						return;
					}
				}

				Date currentDate = new Date();
				Date policyFrmDate = null;
				Date policyToDate = null;
				if (policy != null) {
					policyFrmDate = policy.getPolicyFromDate();
					policyToDate = policy.getPolicyToDate();
				}
				if (enteredDate != null && policyFrmDate != null
						&& policyToDate != null) {
					if (!enteredDate.after(policyFrmDate)
							|| enteredDate.compareTo(policyToDate) > 0) {
						event.getProperty().setValue(null);
					
						showErrorMessage("Accident Date is not in range between Policy From Date and Policy To Date.");
					}
				}
			}
		});		
		
		dateOfDeath.addValueChangeListener(new Property.ValueChangeListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {
				Policy policy = (Policy) ((DateField) event
						.getProperty()).getData();

				Date enteredDate = (Date) ((DateField) event
						.getProperty()).getValue();
				if (enteredDate != null) {

					try {
						dateOfDeath.validate();
						enteredDate = (Date) event.getProperty()
								.getValue();
					} catch (Exception e) {
						dateOfDeath.setValue(null);
						showErrorMessage("Please Enter a valid Death Date");
						// Notification.show("Please Enter a valid Date");
						return;
					}
				}

				Date currentDate = new Date();
				Date policyFrmDate = null;
				Date policyToDate = null;
				if (policy != null) {
					policyFrmDate = policy.getPolicyFromDate();
					policyToDate = policy.getPolicyToDate();
				}
				/*if (enteredDate != null && policyFrmDate != null
						&& policyToDate != null) {
					if (!enteredDate.after(policyFrmDate)
							|| enteredDate.compareTo(policyToDate) > 0) {
						event.getProperty().setValue(null);
					
						showErrorMessage("Death Date is not in range between Policy From Date and Policy To Date.");
					}
				}*/
				if(null != enteredDate)
				{
					Date accidentDate = new Date();
					if(null != dateOfAccident.getValue()){
						accidentDate = dateOfAccident.getValue();
					}
					if (accidentDate != null && null != enteredDate) {
						
						Long diffDays = SHAUtils.getDaysBetweenDate(accidentDate,enteredDate);
						
						if(null != diffDays && diffDays>365)
						{
							showErrorMessage("The date of death captured is beyond 12 months from the date of accident");
						}
					}
				}
				
				
				
			}
		});		
		
		dateOfDisablement.addValueChangeListener(new Property.ValueChangeListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {
				Policy policy = (Policy) ((DateField) event
						.getProperty()).getData();

				Date enteredDate = (Date) ((DateField) event
						.getProperty()).getValue();
				if (enteredDate != null) {

					try {
						dateOfDisablement.validate();
						enteredDate = (Date) event.getProperty()
								.getValue();
					} catch (Exception e) {
						dateOfDisablement.setValue(null);
						showErrorMessage("Please Enter a valid Disablement Date");
						// Notification.show("Please Enter a valid Date");
						return;
					}
				}

				Date accidentDate = new Date();
				if(null != dateOfAccident.getValue()){
					accidentDate = dateOfAccident.getValue();
				}
				if (accidentDate != null && null != enteredDate) {
					
					Long diffMonths = SHAUtils.getDaysBetweenDate(accidentDate,enteredDate);
					
					if(null != diffMonths && diffMonths>365)
					{
						showErrorMessage("The date of disablement captured is beyond 12 months from the date of accident");
					}
				}
				
			}
		});				
	
		
	}
	
	
	 public Boolean alertMessage() {
			

	   		Label successLabel = new Label(
					"<b style = 'color: red;'> Close Proximity Claim</b>",
					ContentMode.HTML);

			// Label noteLabel = new
			// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
			// ContentMode.HTML);

			Button homeButton = new Button("ok");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
			layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
			layout.setSpacing(true);
			layout.setMargin(true);
			HorizontalLayout hLayout = new HorizontalLayout(layout);
			hLayout.setMargin(true);
			hLayout.setStyleName("borderLayout");

			final ConfirmDialog dialog = new ConfirmDialog();
//			dialog.setCaption("Alert");
			dialog.setClosable(false);
			dialog.setContent(hLayout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);

			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					
				    isValid = true;
				    bean.setAlertMessageOpened(true);
				    if(!bean.getIsDialysis()){
				    	wizard.next();
				    }
				    dialog.close();

				}
			});

			return isValid;

		}
	
	private void claimDetailsNoOfDaysListener() {
		BlurListener listener = new BlurListener() {
			private static final long serialVersionUID = -6214266810135299292L;

			@Override
			public void blur(BlurEvent event) {
//				fireViewEvent(PreMedicalPreauthWizardPresenter, primaryParameter, secondaryParameters);
			}
		};

	}
	
	
	private void setInterimOrFinalEnhancement(Boolean isFinalChecked) {
		if(isFinalChecked) {
		//	dateofDischarge.setValue(null);
			dateofDischarge.setEnabled(false);
			dateofDischarge.setRequired(false);
			dateofDischarge.setValue(null);
			unbindField(changeOfDOA);
			unbindField(dateofDischarge);
			mandatoryFields.remove(changeOfDOA);
			mandatoryFields.remove(dateofDischarge);
			bean.getPreauthDataExtractionDetails().setDischargeDate(null);
			fireViewEvent(PAPreauthEnhancemetWizardPresenter.SHOW_FVR_DETAILS, null);


		} else {
			dateofDischarge.setEnabled(true);
			unbindField(changeOfDOA);
			/**
			 * During the time of declaration, 
			 * the dateOfDischarge is binded with DTO property
			 * to avoid inconsistent observed in date format.
			 * Hence commenting the below line.
			 * */
			//binder.bind(dateofDischarge, "dischargeDate");
			dateofDischarge.setRequired(true);
			dateofDischarge.setValidationVisible(false);
			mandatoryFields.add(dateofDischarge);
			//cmbCatastrophicLoss.setRequired(true);
			//mandatoryFields.add(cmbCatastrophicLoss);
			cmbNatureOfLoss.setRequired(true);
			mandatoryFields.add(cmbNatureOfLoss);
			cmbCauseOfLoss.setRequired(true);
			mandatoryFields.add(cmbCauseOfLoss);
			if(bean.getPreauthDataExtractionDetails().getDischargeDate() != null) {
				dateofDischarge.setValue(bean.getPreauthDataExtractionDetails().getDischargeDate());
			}
			fireViewEvent(PAPreauthEnhancemetWizardPresenter.HIDE_FVR_DETAILS, null);
		}
	}
	
	/*private void setInterimOrFinalEnhancement(Boolean isFinalChecked) {
		if(isFinalChecked) {
		//	dateofDischarge.setValue(null);
			dateofDischarge.setEnabled(false);
			dateofDischarge.setRequired(false);
			dateofDischarge.setValue(null);
			unbindField(changeOfDOA);
			unbindField(dateofDischarge);
			mandatoryFields.remove(changeOfDOA);
			mandatoryFields.remove(dateofDischarge);

		} else {
			dateofDischarge.setEnabled(true);
			unbindField(changeOfDOA);
			*//**
			 * During the time of declaration, 
			 * the dateOfDischarge is binded with DTO property
			 * to avoid inconsistent observed in date format.
			 * Hence commenting the below line.
			 * *//*
			//binder.bind(dateofDischarge, "dischargeDate");
			dateofDischarge.setRequired(true);
			dateofDischarge.setValidationVisible(false);
			mandatoryFields.add(dateofDischarge);
			
		}
	}
*/
	private void unbindField(Field<?> field) {
		if (field != null ) {
			Object propertyId = this.binder.getPropertyId(field);
			if (field!= null && propertyId != null) {
				this.binder.unbind(field);
			}
		}
	}

	public void generatedFieldsBasedOnTreatment() {
		if (cmbTreatmentType.getValue() != null) {
			Boolean isMedicalSeclted = true;
			if (cmbTreatmentType.getValue().toString().toLowerCase()
					.contains("medical")) {
			//	unbindField(treatmentRemarksTxt);
				treatmentRemarksTxt = (TextArea) binder.buildAndBind(
						"Treatment Remarks", "enhancementTreatmentRemarks",
						TextArea.class);
				
				
				treatmentRemarksTxt.setNullRepresentation("");
				treatmentRemarksTxt.setMaxLength(4000);
//				treatmentRemarksTxt.setRequired(true);
//				treatmentRemarksTxt.setValidationVisible(false);
				
//				CSValidator validator = new CSValidator();
//				treatmentRemarksTxt.setMaxLength(100);
//				validator.extend(treatmentRemarksTxt);
//				validator.setRegExp("^[a-zA-Z 0-9]*$");
//				validator.setPreventInvalidTyping(true);
				
//				treatmentFLayout.addComponent(field);
				treatmentFLayout.addComponent(treatmentRemarksTxt);
//				mandatoryFields.add(treatmentRemarksTxt);
				
				Table table = new Table();
				table.setWidth("80%");
				table.addContainerProperty("S.No", String.class, null);
				table.addContainerProperty("Remarks",  String.class, null);
				table.setStyleName(ValoTheme.TABLE_NO_HEADER);
				if(this.bean.getTreatmentRemarksList() != null && !this.bean.getTreatmentRemarksList().isEmpty()) {
					for(int i = 0; i < this.bean.getTreatmentRemarksList().size(); i++) {
						if(i != this.bean.getTreatmentRemarksList().size()-1){
						table.addItem(new Object[]{"Treatment Remarks " + (i+1), this.bean.getTreatmentRemarksList().get(i)}, i+1);
						}
					}
				}
				table.setPageLength(2);
				treatmentFLayout.addComponent(table);
				
				if(this.bean.getTreatmentRemarksList() != null && ! this.bean.getTreatmentRemarksList().isEmpty()){
					treatmentRemarksTxt.setValue(this.bean.getTreatmentRemarksList().get(this.bean.getTreatmentRemarksList().size() -1));
				}

				if (tableVLayout != null
						&& tableVLayout.getComponentCount() > 0) {
					tableVLayout.removeAllComponents();
				}
				this.procedureTableObj = null;
				this.newProcedurdTableObj = null;
				

			} else {
				this.bean.getPreauthDataExtractionDetails().setEnhancementTreatmentRemarks("");
				if (treatmentRemarksTxt != null) {
					
					unbindField(treatmentRemarksTxt);					
					/**
					 * 
					 * Fix for the issue 773.
					 * If table is added in layout, then it is removed. If in case table is not
					 * added in layout and when trying to remove a component which is not added
					 * it may lead to null pointer exception. Hence the layout is first
					 * iterated and then if instance of check will happen, where in if table
					 * instance is present, we would remove the same.
					 * 
					 * */
					Iterator<Component> treatmentCompIterator = treatmentFLayout.iterator();
					while(treatmentCompIterator.hasNext())
					{
						Component treatmentComp = treatmentCompIterator.next();
						if(treatmentComp instanceof Table)
						{
							Table table = (Table) treatmentComp;
							treatmentFLayout.removeComponent(table);
						}
					}
					treatmentFLayout.removeComponent(treatmentRemarksTxt);
//					mandatoryFields.remove(treatmentRemarksTxt);
				}
				isMedicalSeclted = false;
				List<DiagnosisDetailsTableDTO> diagList = null;
				if(null != diagnosisListenerTableObj)
				{
					diagList = diagnosisListenerTableObj.getValues();
				}
				PAProcedureListenerTableForPremedical procedureTableInstance = procedureListenerTable.get();
				//procedureTableInstance.init(bean.getHospitalKey(), "preauthEnhancement",diagList);
				procedureTableInstance.init(bean.getHospitalCode(), "preauthEnhancement", bean);
				this.procedureTableObj = procedureTableInstance;
				this.procedureTableObj.setReferenceData(referenceData);

				PANewProcedureTableList newProcedureTableListInstance = newProcedureTableList
						.get();
				newProcedureTableListInstance.init("Add New Procedure", true);
				newProcedureTableListInstance.setReference(referenceData);
				this.newProcedurdTableObj = newProcedureTableListInstance;
				this.newProcedurdTableObj.setPreauthDTO(bean);

				if (tableVLayout != null
						&& tableVLayout.getComponentCount() > 0) {
					tableVLayout.removeAllComponents();
				}
				tableVLayout.addComponent(procedureTableObj);
				tableVLayout.addComponent(newProcedurdTableObj);
				
				List<ProcedureDTO> procedureExclusionCheckTableList = this.bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList();
				List<ProcedureDTO> oldProcedureDTOList = new ArrayList<ProcedureDTO>();
			
				List<ProcedureDTO> newProcedureDTOList = new ArrayList<ProcedureDTO>();
				if(!procedureExclusionCheckTableList.isEmpty()) {
					for (ProcedureDTO procedureDTO : procedureExclusionCheckTableList) {
						if(null != procedureDTO && null != procedureDTO.getEnableOrDisable() && !procedureDTO.getEnableOrDisable())
						{
							procedureDTO.setEnableOrDisable(false);
						}
						else
						{
							procedureDTO.setEnableOrDisable(true);
						}
						procedureDTO.setStatusFlag(true);
						if(procedureDTO.getNewProcedureFlag() == 1) {
							newProcedureDTOList.add(procedureDTO);
						} else {
							oldProcedureDTOList.add(procedureDTO);
						}
					}
				}
				this.bean.getPreauthDataExtractionDetails().setProcedureList(oldProcedureDTOList);
				this.bean.getPreauthDataExtractionDetails().setNewProcedureList(newProcedureDTOList);
				if(this.newProcedurdTableObj != null) {
					List<ProcedureDTO> newProcedureList = this.bean.getPreauthDataExtractionDetails().getNewProcedureList();
					for (ProcedureDTO newProcedureTableDTO : newProcedureList) {
						newProcedureTableDTO.setEnableOrDisable(true);
						this.newProcedurdTableObj.addBeanToList(newProcedureTableDTO);
					}
				}
				
				if(this.procedureTableObj != null) {
					List<ProcedureDTO> procedureList = this.bean.getPreauthDataExtractionDetails().getProcedureList();
					for (ProcedureDTO procedureTableDTO : procedureList) {
						if(null != procedureTableDTO && null != procedureTableDTO.getEnableOrDisable() && !procedureTableDTO.getEnableOrDisable())
						{
							procedureTableDTO.setEnableOrDisable(false);
						}
						else
						{
							procedureTableDTO.setEnableOrDisable(true);
						}
						procedureTableDTO.setEnableOrDisable(true);
						this.procedureTableObj.addBeanToList(procedureTableDTO);
					}
				}

			}
			SpecialityTable specialityTableInstance = specialityTableList.get();
			specialityTableInstance.init("Speciality", true);
			referenceData.put("specialityType",
					referenceData.get("medicalSpeciality"));
			if (!isMedicalSeclted) {
				referenceData.put("specialityType",
						referenceData.get("surgicalSpeciality"));
			}
			
			specialityTableInstance.setReference(referenceData);
			this.specialityTableObj = specialityTableInstance;
			tableVLayout.addComponent(specialityTableObj);
		} else {
			if (treatmentRemarksTxt != null) {
				unbindField(treatmentRemarksTxt);
				treatmentFLayout.removeComponent(treatmentRemarksTxt);
//				mandatoryFields.remove(treatmentRemarksTxt);
			}

			if (tableVLayout != null && tableVLayout.getComponentCount() > 0) {
				tableVLayout.removeAllComponents();
			}
			this.procedureTableObj = null;
			this.newProcedurdTableObj = null;
		}
	}

	@SuppressWarnings("unused")
	private void setRequiredAndValidation(Component component) {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		AbstractField<Field> field = (AbstractField<Field>) component;
		field.setRequired(true);
		field.setValidationVisible(false);
	}

	public void generateFieldsBasedOnPatientStatus() {
		if (cmbPatientStatus.getValue() != null) {
			if (this.cmbPatientStatus.getValue().toString().toLowerCase()
					.contains("deceased")) {
				deathDate = (PopupDateField) binder.buildAndBind(
						"Date Of Death", "deathDate", PopupDateField.class);
				
				//validation added for death date.
				addDeathDateValueChangeListener();
				
				txtReasonForDeath = (TextField) binder.buildAndBind(
						"Reason For Death", "reasonForDeath", TextField.class);
				txtReasonForDeath.setMaxLength(100);
				cmbTerminateCover = (ComboBox) binder.buildAndBind(
						"Terminate Cover", "terminateCover", ComboBox.class);
				
				@SuppressWarnings("unchecked")
				BeanItemContainer<SelectValue> terminateCover = (BeanItemContainer<SelectValue>) referenceData
						.get("terminateCover");

				cmbTerminateCover.setContainerDataSource(terminateCover);
				cmbTerminateCover.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				cmbTerminateCover.setItemCaptionPropertyId("value");
				
				if(this.bean.getPreauthDataExtractionDetails().getTerminateCoverFlag() != null && this.bean.getPreauthDataExtractionDetails().getTerminateCoverFlag().toLowerCase().equalsIgnoreCase("y")){
					cmbTerminateCover.setValue(this.bean.getPreauthDataExtractionDetails().getTerminateCover());
				}

				setRequiredAndValidation(deathDate);
				setRequiredAndValidation(txtReasonForDeath);
				setRequiredAndValidation(cmbTerminateCover);

				mandatoryFields.add(deathDate);
				mandatoryFields.add(txtReasonForDeath);
				mandatoryFields.add(cmbTerminateCover);

				patientStatusFLayout.addComponent(deathDate);
				patientStatusFLayout.addComponent(txtReasonForDeath);
				patientStatusFLayout.addComponent(cmbTerminateCover);
			} else {
				removePatientStatusGeneratedFields();
			}

		} else {
			removePatientStatusGeneratedFields();
		}

	}
	
	/**
	 * Method to validate death date.
	 * 
	 * */
	private void addDeathDateValueChangeListener()
	{
		deathDate.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -8435623803385270083L;

			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {
				Date enteredDate = (Date) event.getProperty().getValue();
				Date currentSystemDate = new Date();
				if(null != enteredDate && null != admissionDate && null != admissionDate.getValue())
					if(!SHAUtils.validateDeathDate(enteredDate,admissionDate.getValue()))
					{
						
						showError();
						event.getProperty().setValue(null);
					}
				
				if(enteredDate != null && dateofDischarge != null && dateofDischarge.getValue() != null){
					if(enteredDate.after(dateofDischarge.getValue()) || enteredDate.after(currentSystemDate)){
						showError();
						event.getProperty().setValue(null);
					}
				}
			}
		});
	}
	
	private void showError(){
		
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setClosable(false);
		dialog.setResizable(false);
		 Button okButton = new Button("OK");
		 okButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -7148801292961705660L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
			}
		});
		HorizontalLayout hLayout = new HorizontalLayout(okButton);
		hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
		hLayout.setMargin(true);
		VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color: red;'>Please select valid death date. Death date is not in range between admission date and Discharge date.</b>", ContentMode.HTML));
		layout.setMargin(true);
		layout.setSpacing(true);
		dialog.setContent(layout);
		dialog.setCaption("Error");
		dialog.setClosable(true);
		dialog.show(getUI().getCurrent(), null, true);
		
	}

	private void removePatientStatusGeneratedFields() {
		if (deathDate != null && txtReasonForDeath != null
				&& cmbTerminateCover != null) {
			unbindField(deathDate);
			unbindField(txtReasonForDeath);
			unbindField(cmbTerminateCover);
			mandatoryFields.remove(deathDate);
			mandatoryFields.remove(txtReasonForDeath);
			mandatoryFields.remove(cmbTerminateCover);
			patientStatusFLayout.removeComponent(deathDate);
			patientStatusFLayout.removeComponent(txtReasonForDeath);
			patientStatusFLayout.removeComponent(cmbTerminateCover);
		}
	}

	@Override
	public void init(PreauthDTO bean) {

	}

	public void setIcdBlock(BeanItemContainer<SelectValue> icdBlockContainer){
		this.diagnosisListenerTableObj.setIcdBlock(icdBlockContainer);
		
	}
	
	public void setIcdCode(BeanItemContainer<SelectValue> icdCodeContainer){
		this.diagnosisListenerTableObj.setIcdCode(icdCodeContainer);
	}

	public void setPackageRateForProcedure(Map<String, String> mappedValues) {
		this.procedureTableObj.setPackageRate(mappedValues);
	}
	
	/**
	 * If the policy type is "Group Policy" , then the corp buffer check box
	 * will be enabled. Else it will be disabled. The below method
	 * does the validation for the same.
	 * */
	
	public void editCorpBufferChk()
	{
		/**
		 * The string "Group Policy" needs to be replaced, after checkwith
		 * saravana/Sathish the valid values for Group Policy.For time being
		 * the below string is used. 
		 */

		if (null != this.bean.getNewIntimationDTO() && null != this.bean.getNewIntimationDTO().getPolicy() 
				&& null != this.bean.getNewIntimationDTO().getPolicy().getProductType() && ("Group").equalsIgnoreCase(this.bean.getNewIntimationDTO().getPolicy().getProductType().getValue()))
		//if (("Group").equalsIgnoreCase(this.bean.getNewIntimationDTO().getPolicy().getProductType().getValue()))
		{
			corpBufferChk.setReadOnly(false);
		}
		else
		{
			corpBufferChk.setReadOnly(true);
		}
	}
	
	public void setHospitalizationDetails(
			Map<Integer, Object> hospitalizationDetails) {
		this.claimedDetailsTableObj.setDBCalculationValues(hospitalizationDetails);
		
//		if(hospitalizationDetails != null && hospitalizationDetails.get(15) != null){
//			this.bean.setAmbulanceLimitAmount((Double)hospitalizationDetails.get(15));
//		}
		
	}
	
	protected void addDiagnosisNameChangeListener()
	{
		this.diagnosisListenerTableObj.dummyField.addValueChangeListener(new ValueChangeListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != procedureTableObj)
				{
					String diagnosisValue = (String)event.getProperty().getValue();
					if(!procedureTableObj.diagnosisList.contains(diagnosisValue))
					{
						procedureTableObj.diagnosisList.add(diagnosisValue);
					}
				System.out.println("---the diagnosisListValue----"+procedureTableObj.diagnosisList);
				}
			}
			
			
		});
		
	}
	
	  public void alertNoOfSittings() {
		   	 DiagnosisDetailsTableDTO dialysisDiagnosisDTO = SHAUtils.getDialysisDiagnosisDTO(bean.getPreauthDataExtractionDetails().getDiagnosisTableList());
		   	 ProcedureDTO dialysisProcedureDTO = SHAUtils.getDialysisProcedureDTO(bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList());
		   	 if(dialysisDiagnosisDTO != null || dialysisProcedureDTO != null) { 
						final ConfirmDialog dialog = new ConfirmDialog();
						dialog.setCaption("");
						dialog.setClosable(false);
						dialog.setResizable(false);
						dialog.setModal(true);
						
						final TextField sittingsField = new TextField("No. Of sittings");
						
						CSValidator validator = new CSValidator();
						validator.extend(sittingsField);
						validator.setRegExp("^[0-9]*$");
						validator.setPreventInvalidTyping(true);
						
						sittingsField.setData(dialysisDiagnosisDTO != null ? dialysisDiagnosisDTO : (dialysisProcedureDTO != null ? dialysisProcedureDTO : null ));
						if(dialysisDiagnosisDTO != null) {
							if(dialysisDiagnosisDTO.getSittingsInput() != null) {
								sittingsField.setValue(dialysisDiagnosisDTO.getSittingsInput() );
							}
						} else if(dialysisProcedureDTO != null) {
							if(dialysisProcedureDTO.getSittingsInput() != null) {
								sittingsField.setValue(dialysisProcedureDTO.getSittingsInput() );
							}
						}
						
						Button okButton = new Button("OK");
						okButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
						
						okButton.setData(dialysisDiagnosisDTO != null ? dialysisDiagnosisDTO : (dialysisProcedureDTO != null ? dialysisProcedureDTO : null ));
						okButton.addClickListener(new ClickListener() {
							private static final long serialVersionUID = 7396240433865727954L;
		
							@Override
							public void buttonClick(ClickEvent event) {
								if(event.getButton().getData() != null) {
									if((event.getButton().getData()) instanceof DiagnosisDetailsTableDTO) {
										DiagnosisDetailsTableDTO dto = (DiagnosisDetailsTableDTO) event.getButton().getData();
										dto.setSittingsInput(sittingsField.getValue() != null ? sittingsField.getValue() : "0");
									} else if((event.getButton().getData()) instanceof ProcedureDTO) {
										ProcedureDTO dto = (ProcedureDTO) event.getButton().getData();
										dto.setSittingsInput(sittingsField.getValue() != null ? sittingsField.getValue() : "0");
									}
									bean.setDialysisOpened(true);
								}
								wizard.next();
								dialog.close();
							}
						});
						HorizontalLayout hLayout = new HorizontalLayout(okButton);
						hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
						hLayout.setMargin(true);
						VerticalLayout layout = new VerticalLayout(sittingsField, hLayout);
						layout.setMargin(true);
						dialog.setContent(layout);
						dialog.show(getUI().getCurrent(), null, true);
		   	 } else {
		   		 bean.setIsDialysis(false);
		   		 bean.setDialysisOpened(false);
		   	 }
	    }
	  
	  public void alertMessageForChangeOfTreatmentType() {
		  final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setClosable(false);
			dialog.setResizable(false);
			dialog.setModal(true);
			
			Button okButton = new Button("Yes");
			Button cancelButton = new Button("No");
			okButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			
			okButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					List<ProcedureDTO> procedureExclusionCheckTableList = bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList();
                	for (ProcedureDTO procedureDTO : procedureExclusionCheckTableList) {
                		if(!bean.getDeletedProcedure().contains(procedureDTO) && procedureDTO.getKey() != null) {
	        				bean.getDeletedProcedure().add(procedureDTO);
	        			}
					}
                	
                	bean.getPreauthMedicalProcessingDetails().setProcedureExclusionCheckTableList(new ArrayList<ProcedureDTO>());
                	fireViewEvent(
							PAPreauthEnhancemetWizardPresenter.PREAUTH_TREATMENT_TYPE_CHANGED,
							null);
					dialog.close();
				}
			});
			
			cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
			
			cancelButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					cmbTreatmentType.setValue(cmbTreatmentType.getItemIds().toArray()[1]);
					dialog.close();
				}
			});
			HorizontalLayout hLayout = new HorizontalLayout(okButton, cancelButton);
			hLayout.setSpacing(true);
			hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
			hLayout.setMargin(true);
			VerticalLayout layout = new VerticalLayout(new Label("Entered Procedure Will be marked as deleted one. Do you want to proceed further ?"), hLayout);
			layout.setMargin(true);
			dialog.setContent(layout);
			dialog.show(getUI().getCurrent(), null, true);
		}
	  
	  public void poupMessageForProduct() {
		  final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setClosable(true);
			dialog.setResizable(false);
			dialog.setModal(true);
			
			Button okButton = new Button("OK");
			okButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			
			okButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					
					bean.setIsPopupMessageOpened(true);
					dialog.close();
					if(!bean.getIsPopupMessageOpened()){
						if(bean.getClaimCount() > 1){
							alertMessageForClaimCount(bean.getClaimCount());
						}
					} else if(!bean.getSuspiciousPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()){
						suspiousPopupMessage();
					} else if(bean.getIsPEDInitiated()) {
						alertMessageForPED();
					} else if(bean.getIsAutoRestorationDone()) {
						alertMessageForAutoRestroation();
					} else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY) && bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE) && bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE)) {
						StarCommonUtils.alertMessage(getUI(), SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
					}
				}
			});
			
			HorizontalLayout hLayout = new HorizontalLayout(okButton);
			hLayout.setSpacing(true);
			hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
			hLayout.setMargin(true);
			VerticalLayout layout = new VerticalLayout();
			Map<String, String> popupMap = bean.getPopupMap();
			for (Map.Entry<String, String> entry : popupMap.entrySet()) {
				layout.addComponent(new Label(entry.getValue(), ContentMode.HTML));
				 layout.addComponent(new Label(entry.getKey(), ContentMode.HTML));
			}
			layout.addComponent(okButton);
			layout.setMargin(true);
			dialog.setContent(layout);
			dialog.setWidth("35%");
			bean.setIsPopupMessageOpened(true);
			dialog.show(getUI().getCurrent(), null, true);
		}
	  
	  public void showCMDAlert() {	 
			 
			 Label successLabel = new Label(
						"<b style = 'color: red;'>" + SHAConstants.CMD_ALERT + "</b>",
						ContentMode.HTML);
				Button homeButton = new Button("OK");
				homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
				layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
				layout.setSpacing(true);
				layout.setMargin(true);
				layout.setStyleName("borderLayout");

				final ConfirmDialog dialog = new ConfirmDialog();
				dialog.setClosable(false);
				dialog.setContent(layout);
				dialog.setResizable(false);
				dialog.setModal(true);
				dialog.show(getUI().getCurrent(), null, true);

				homeButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						dialog.close();
						if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
							poupMessageForProduct();
						}else if(!bean.getIsPopupMessageOpened() && bean.getClaimCount() >1){
								alertMessageForClaimCount(bean.getClaimCount());
						}
						else if(!bean.getSuspiciousPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()){
							suspiousPopupMessage();
						} else if(bean.getIsPEDInitiated()) {
							alertMessageForPED();
						} else if(bean.getIsAutoRestorationDone()) {
							alertMessageForAutoRestroation();
						} else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY) && bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE) && bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE)) {
							StarCommonUtils.alertMessage(getUI(), SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
						}	
					}
				});
			}
	  
	    private void unbindAndRemoveFromLayout() {
			unbindField(injuryDate);
			unbindField(cmbCauseOfInjury);
			unbindField(medicalLegalCase);
			unbindField(reportedToPolice);
			unbindField(deliveryDate);
			unbindField(diseaseDetectedDate);
			unbindField(firNumberTxt);
			unbindField(policeReportedAttached);
			unbindField(cmbTypeOfDelivery);
			
			
			if(injuryDate != null && cmbCauseOfInjury != null && medicalLegalCase != null && reportedToPolice != null  ) {
				firstFLayout.removeComponent(medicalLegalCase);
				firstFLayout.removeComponent(reportedToPolice);
				firstFLayout.removeComponent(injuryDate);
				secondFLayout.removeComponent(cmbCauseOfInjury);
				
			}
			
			if(firNumberTxt != null && policeReportedAttached != null) {
				firstFLayout.removeComponent(firNumberTxt);
				secondFLayout.removeComponent(policeReportedAttached);
			}
			
			if(deliveryDate != null ) {
				firstFLayout.removeComponent(deliveryDate);
				mandatoryFields.remove(deliveryDate);
			}
			
			if(cmbTypeOfDelivery != null) {
				firstFLayout.removeComponent(cmbTypeOfDelivery);
			}
			
			if(diseaseDetectedDate != null) {
				firstFLayout.removeComponent(diseaseDetectedDate);
			}
		}
	    
	    protected Collection<Boolean> getReadioButtonOptions() {
			Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
			coordinatorValues.add(true);
			coordinatorValues.add(false);
			
			return coordinatorValues;
		}
	    
	    
	    public void generatedFieldsBasedOnHospitalisationDueTo(SelectValue value) {
			if (null != value) {
				if(value.getId() != null && ReferenceTable.INJURY_MASTER_ID.equals(value.getId())) {
					unbindAndRemoveFromLayout();
					injuryDate = (DateField) binder.buildAndBind(
							"Date of Injury", "injuryDate", DateField.class);
					cmbCauseOfInjury = (ComboBox) binder.buildAndBind(
							"Cause of Injury", "causeOfInjury", ComboBox.class);
					
					@SuppressWarnings("unchecked")
					BeanItemContainer<SelectValue> terminateCover = (BeanItemContainer<SelectValue>) referenceData
							.get("causeOfInjury");

					cmbCauseOfInjury.setContainerDataSource(terminateCover);
					cmbCauseOfInjury.setItemCaptionMode(ItemCaptionMode.PROPERTY);
					cmbCauseOfInjury.setItemCaptionPropertyId("value");
					
					if(this.bean.getPreauthDataExtractionDetails().getCauseOfInjury() != null) {
						cmbCauseOfInjury.setValue(this.bean.getPreauthDataExtractionDetails().getCauseOfInjury());
					}
					
					medicalLegalCase = (OptionGroup) binder.buildAndBind(
							"Medical Legal Case", "medicalLegalCase", OptionGroup.class);
					medicalLegalCase.addItems(getReadioButtonOptions());
					medicalLegalCase.setItemCaption(true, "Yes");
					medicalLegalCase.setItemCaption(false, "No");
					medicalLegalCase.setStyleName("horizontal");
					
					reportedToPolice = (OptionGroup) binder.buildAndBind(
							"Reported to Police", "reportedToPolice", OptionGroup.class);
					reportedToPolice.addItems(getReadioButtonOptions());
					reportedToPolice.setItemCaption(true, "Yes");
					reportedToPolice.setItemCaption(false, "No");
					reportedToPolice.setStyleName("horizontal");
					reportedToPolice.addValueChangeListener(new Property.ValueChangeListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void valueChange(ValueChangeEvent event) {
							Boolean isChecked = false;
							if(event.getProperty() != null && event.getProperty().getValue() != null && event.getProperty().getValue().toString() == "true") {
								isChecked = true;
							} 
							if(event.getProperty() != null && event.getProperty().getValue() != null) {
								fireViewEvent(PAPreauthEnhancemetWizardPresenter.PREAUTH_ENHANCEMENT_REPORTED_TO_POLICE, isChecked);
							}
						}
					});
					
					
					firstFLayout.addComponent(injuryDate);
					firstFLayout.addComponent(medicalLegalCase);
					firstFLayout.addComponent(reportedToPolice);
					
					secondFLayout.addComponent(cmbCauseOfInjury);
					if(this.bean.getPreauthDataExtractionDetails().getReportedToPolice() != null && this.bean.getPreauthDataExtractionDetails().getReportedToPolice()) {
						generatedFieldsBasedOnReportedToPolice(true);
					}
					
				} else if(value.getId() != null && ReferenceTable.ILLNESS_MASTER_ID.equals(value.getId())) {
					unbindAndRemoveFromLayout();
					diseaseDetectedDate = (DateField) binder.buildAndBind(
							"Date Disease First Detected", "diseaseFirstDetectedDate", DateField.class);
					firstFLayout.addComponent(diseaseDetectedDate);
				} else if(value.getId() != null && ReferenceTable.MATERNITY_MASTER_ID.equals(value.getId())) {
					if(firNumberTxt != null){
						firstFLayout.removeComponent(firNumberTxt);
						}
					if(policeReportedAttached != null){
						secondFLayout.removeComponent(policeReportedAttached);
					}
					unbindAndRemoveFromLayout();
					if(bean.getNewIntimationDTO().getInsuredPatient() != null && bean.getNewIntimationDTO().getInsuredPatient().getInsuredGender() != null && bean.getNewIntimationDTO().getInsuredPatient().getInsuredGender().getKey().equals(ReferenceTable.FEMALE_GENDER)){
					deliveryDate = (DateField) binder.buildAndBind(
							"Date of Delivery", "deliveryDate", DateField.class);
					mandatoryFields.add(deliveryDate);
					showOrHideValidation(false);
					cmbTypeOfDelivery = (ComboBox) binder.buildAndBind(
							"Type of Delivery", "typeOfDelivery", ComboBox.class);
					
					@SuppressWarnings("unchecked")
					BeanItemContainer<SelectValue> terminateCover = (BeanItemContainer<SelectValue>) referenceData
							.get("typeOfDelivery");

					cmbTypeOfDelivery.setContainerDataSource(terminateCover);
					cmbTypeOfDelivery.setItemCaptionMode(ItemCaptionMode.PROPERTY);
					cmbTypeOfDelivery.setItemCaptionPropertyId("value");
					
					if(this.bean.getPreauthDataExtractionDetails().getTypeOfDelivery() != null) {
						cmbTypeOfDelivery.setValue(this.bean.getPreauthDataExtractionDetails().getTypeOfDelivery());
					}
					
					firstFLayout.addComponent(deliveryDate);
					firstFLayout.addComponent(cmbTypeOfDelivery);
				}else{
					getErrorMessage("Selected insured is Male.Hence Maternity is not applicable for the selected insured");
					cmbHospitalisationDueTo.setValue(null);
					//Vaadin8-setImmediate() cmbHospitalisationDueTo.setImmediate(true);
				}
				}
					
			} else {
				unbindAndRemoveFromLayout();
			}
		}
	    
	    public void generatedFieldsBasedOnReportedToPolice(Boolean isChecked) {
			if (isChecked) {
					unbindAndRemovePoliceReportFromLayout();
					firNumberTxt = (TextField) binder.buildAndBind(
							"FIR NO", "firNumber", TextField.class);
					
					policeReportedAttached = (OptionGroup) binder.buildAndBind(
							"MLC Report & Police Report Attached", "policeReportAttached", OptionGroup.class);
					policeReportedAttached.addItems(getReadioButtonOptions());
					policeReportedAttached.setItemCaption(true, "Yes");
					policeReportedAttached.setItemCaption(false, "No");
					policeReportedAttached.setStyleName("horizontal");
					
					firstFLayout.addComponent(firNumberTxt);
					
					secondFLayout.addComponent(policeReportedAttached);
			} else {
				unbindAndRemovePoliceReportFromLayout();
			}
		}
	    
	    private void unbindAndRemovePoliceReportFromLayout() {
			unbindField(firNumberTxt);
			unbindField(policeReportedAttached);
			
			if(firNumberTxt != null && policeReportedAttached != null) {
				firstFLayout.removeComponent(firNumberTxt);
				secondFLayout.removeComponent(policeReportedAttached);
			}
			
		}
	    
	    public void suspiousPopupMessage() {
			  final ConfirmDialog dialog = new ConfirmDialog();
				dialog.setCaption("");
				dialog.setClosable(true);
				dialog.setResizable(false);
				dialog.setModal(true);
				
				Button okButton = new Button("OK");
				okButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				
				okButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						bean.setIsPopupMessageOpened(true);
						dialog.close();
						if(bean.getIsPEDInitiated()) {
							alertMessageForPED();
						} else if(bean.getIsAutoRestorationDone()) {
							alertMessageForAutoRestroation();
						} else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY) && bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE) && bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE)) {
							StarCommonUtils.alertMessage(getUI(), SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
						}
					}
				});
				
				HorizontalLayout hLayout = new HorizontalLayout(okButton);
				hLayout.setSpacing(true);
				hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
				hLayout.setMargin(true);
				VerticalLayout layout = new VerticalLayout();
				Map<String, String> popupMap = bean.getSuspiciousPopupMap();
				for (Map.Entry<String, String> entry : popupMap.entrySet()) {
				   layout.addComponent(new Label(entry.getValue(), ContentMode.HTML));
				   layout.addComponent(new Label(entry.getKey(), ContentMode.HTML));
				}
				layout.addComponent(okButton);
				layout.setMargin(true);
				layout.setSpacing(true);
				dialog.setContent(layout);
				dialog.setWidth("30%");
				this.bean.setIsPopupMessageOpened(true);
				dialog.show(getUI().getCurrent(), null, true);
			}
	    
	    public void getErrorMessage(String eMsg){
			
			Label label = new Label(eMsg, ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);

			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Error");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
		}
	    
	    
	    public void setCoverList(BeanItemContainer<SelectValue> coverContainer) {
	    	// TODO Auto-generated method stub
	    	sectionDetailsListenerTableObj.setCoverList(coverContainer);
	    	
	    }

	    public void setSubCoverList(BeanItemContainer<SelectValue> subCoverContainer) {
	    	
	    	sectionDetailsListenerTableObj.setSubCoverList(subCoverContainer);
	    	
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
	    
	    public void setDropDownValues(BeanItemContainer<SelectValue> category) 
	    {	

	    	cmbCategory.setContainerDataSource(category);
	    	cmbCategory.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	    	cmbCategory.setItemCaptionPropertyId("value");		

	    	if(null != category){
	    		SelectValue defaultCategory = category.getIdByIndex(0);
	    		cmbCategory.setValue(defaultCategory);
	    	}

	    }
	    
	    private BlurListener getHospDiscountBillListener() {
			BlurListener listener = new BlurListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void blur(BlurEvent event) {
					TextField value = (TextField) event.getComponent();
					if(value != null && value.getValue() != null && !value.getValue().isEmpty()){
						
						if(txtAmtClaimed != null && txtAmtClaimed.getValue() != null && !txtAmtClaimed.getValue().isEmpty()){
							Integer discount = Integer.valueOf(value.getValue());
							Integer claimedAmt = Integer.valueOf(txtAmtClaimed.getValue());
							Integer netAmt = claimedAmt - discount;
							
							if(txtNetAmt != null){
								txtNetAmt.setValue(netAmt.toString());
							}
							
						}
						
						Integer discount = Integer.valueOf(value.getValue());
						discount = discount*= -1;
						
						if(claimedDetailsTableObj != null){
	     					
	     					List<NoOfDaysCell> claimedAmountValues = new ArrayList<NoOfDaysCell>();
	     					
	     					claimedAmountValues.addAll(claimedDetailsTableObj.getValues());
	     					
	     					NoOfDaysCell noOfDaysCell1 = null;
	     					for (NoOfDaysCell noOfDaysCell : claimedAmountValues) {
								if(noOfDaysCell.getBenefitId() != null && noOfDaysCell.getBenefitId().equals(21L)) {
									noOfDaysCell.setClaimedBillAmount(discount);
									noOfDaysCell.setNetAmount(discount);
									noOfDaysCell.setPaybleAmount(discount);
									noOfDaysCell1 = noOfDaysCell;
									break;
								}
							}
	     					claimedDetailsTableObj.setValuesForHospDiscount(noOfDaysCell1);
	     					
	     				}
						
						
					}
						
				}
			};
			return listener;
		}
	    
	    private BlurListener getclaimedAmtListener() {
			BlurListener listener = new BlurListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void blur(BlurEvent event) {
					TextField value = (TextField) event.getComponent();
					if(value != null && value.getValue() != null && !value.getValue().isEmpty()){
						if(txtDiscntHospBill != null && txtDiscntHospBill.getValue() != null && !txtDiscntHospBill.getValue().isEmpty()){
							Integer claimedAmt = Integer.valueOf(value.getValue());
							Integer discount = Integer.valueOf(txtDiscntHospBill.getValue());
							Integer netAmt = claimedAmt - discount;
							
							if(txtNetAmt != null){
								txtNetAmt.setValue(netAmt.toString());
							}
							
						}
						
					}
						
				}
			};
			return listener;
		}
	    
	    private Table getInsuranceDiagnosisDetailsPanel(){
			
			Table table = new Table();
			table.setWidth("100%");
			table.addContainerProperty("diagnosisName", String.class, null);
			table.addContainerProperty("icdCode",  String.class, null);
			table.setCaption("Insurance Diagnosis");
			
			int i=0;
			if(this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList() != null && !this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList().isEmpty()) {
				for (DiagnosisDetailsTableDTO pedDetails : this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList()) {
					if(pedDetails.getIcdCode() != null){
						table.addItem(new Object[]{pedDetails.getDiagnosis(), pedDetails.getIcdCode().getValue()}, i+1);
						i++;
					}
				}
			}
			
			table.setPageLength(2);
			table.setColumnHeader("diagnosisName", "Hospital Diagnosis");
			table.setColumnHeader("icdCode", "Insurance Diagnosis");
			table.setColumnWidth("diagnosisName", 250);
			table.setColumnWidth("icdCode", 245);
			table.setWidth("500px");

			return table;
		}
}

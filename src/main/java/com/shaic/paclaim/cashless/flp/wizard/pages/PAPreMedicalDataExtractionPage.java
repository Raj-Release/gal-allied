package com.shaic.paclaim.cashless.flp.wizard.pages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.lang.time.DateUtils;
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
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDataExtaractionDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.preauth.wizard.dto.SpecialityDTO;
import com.shaic.claim.preauth.wizard.pages.SpecialityTable;
import com.shaic.claim.premedical.dto.NoOfDaysCell;
import com.shaic.claim.rod.wizard.dto.SectionDetailsTableDTO;
import com.shaic.domain.InsuredPedDetails;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Policy;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.State;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.paclaim.cashless.flp.wizard.wizardFiles.PAPreMedicalPreauthWizardPresenter;
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
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class PAPreMedicalDataExtractionPage extends ViewComponent implements
		WizardStep<PreauthDTO> {
	private static final long serialVersionUID = -4185722564638053875L;

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
	private Instance<PASectionDetailsListenerTable> sectionDetailsListenerTable;
	
	@Inject
	private Instance<PANewClaimedAmountTable> claimedAmountDetailsTable;

	@Inject
	private Instance<PANewProcedureTableList> newProcedureTableList;

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

	private ComboBox cmbTreatmentType;

	private TextArea treatmentRemarksTxt;

	private ComboBox cmbIllness;

	private ComboBox cmbPatientStatus;

	private DateField deathDate;

	private TextField txtReasonForDeath;

	private ComboBox cmbTerminateCover;
	
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
	
	private PASectionDetailsListenerTable sectionDetailsListenerTableObj;

	private Map<String, Property.ValueChangeListener> listenerMap = new HashMap<String, Property.ValueChangeListener>();

	private HorizontalLayout dynamicElementsHLayout;

	private FormLayout treatmentFLayout;

	private FormLayout patientStatusFLayout;
	
	//Added for reason for date change.
	private TextField reasonForChangeInDOA;
	
	private BeanItemContainer<SelectValue> roomCategory ;
	
	private List<String> diagnosisList = new ArrayList<String>();
	
	private Boolean isValid = false;
	
	private BeanItemContainer<State> container = new BeanItemContainer<State>(
			State.class);

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
	
	private TextField txtAmtClaimed;
	
	private TextField txtDiscntHospBill;
	
	private TextField txtNetAmt;


	@Override
	public String getCaption() {
		return "Data Extraction";
	}

	public void init(PreauthDTO bean, GWizard wizard) {
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
					//alertMessageForAutoRestroation();
				} else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
					poupMessageForProduct();
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
				if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
					poupMessageForProduct();
				}else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()){
					if(bean.getClaimCount() >1){
						alertMessageForClaimCount(bean.getClaimCount());
					}
				}
			}
		});
		return true;
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

//					bean.setIsPopupMessageOpened(true);
					dialog.close();
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
					bean.setIsPEDInitiated(false);
					if(bean.getIsPEDInitiated()) {
						alertMessageForPED();
					} else if(bean.getIsAutoRestorationDone()) {
						//alertMessageForAutoRestroation();
					} else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
						poupMessageForProduct();
					}	
				}
			});
		}

	@Override
	public Component getContent() {

		DBCalculationService dbService = new DBCalculationService();
		String memberType = dbService.getCMDMemberType(this.bean.getPolicyKey());
		if(null != memberType && !memberType.isEmpty() && memberType.equalsIgnoreCase(SHAConstants.CMD)){
			showCMDAlert();
		}
		else if(bean.getIsPEDInitiated()) {
			alertMessageForPED();
		} else if(bean.getIsAutoRestorationDone()) {
			//alertMessageForAutoRestroation();
		} else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
			poupMessageForProduct();
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
		reasonForAdmissionTxt.setMaxLength(101);
		
		workOrNonWorkSpace = (OptionGroup) binder.buildAndBind(
				"Accident", "workOrNonWorkPlace", OptionGroup.class);
		workOrNonWorkSpace.addItems(getReadioButtonOptions());
		workOrNonWorkSpace.setItemCaption(true, "WorkPlace");
		workOrNonWorkSpace.setItemCaption(false, "NonWorkPlace");
		workOrNonWorkSpace.setStyleName("horizontal");
		
		
		admissionDate = (DateField) binder.buildAndBind(
				"Date of Admission", "admissionDate", DateField.class);
		reasonForChangeInDOA = (TextField)binder.buildAndBind(
				"Reason For Change in DOA", "changeOfDOA", TextField.class);
		reasonForChangeInDOA.setVisible(false);
		reasonForChangeInDOA.setMaxLength(100);
		CSValidator validator = new CSValidator();
//		validator.extend(reasonForChangeInDOA);
//		validator.setRegExp("^[a-zA-Z 0-9/]*$");
//		validator.setPreventInvalidTyping(true);
		
		if(null != this.bean.getPreauthDataExtractionDetails().getChangeOfDOA() && !("").equals(this.bean.getPreauthDataExtractionDetails().getChangeOfDOA()))
		{
			admissionDate.setValue(this.bean.getPreauthDataExtractionDetails().getAdmissionDate());
			reasonForChangeInDOA.setValue(this.bean.getPreauthDataExtractionDetails().getChangeOfDOA());
			reasonForChangeInDOA.setVisible(true);;
			reasonForChangeInDOA.setRequired(true);
		}
		
		noOfDaysTxt = (TextField) binder.buildAndBind("No of Days", "noOfDays",
				TextField.class);
		noOfDaysTxt.setMaxLength(4);
		/**
		 * Adding validation for noOfDaysTxt field.
		 * */
		
		CSValidator noOfDaysValidator = new CSValidator();
		
		noOfDaysValidator.extend(noOfDaysTxt);
		noOfDaysValidator.setRegExp("^[0-9]*$");
		noOfDaysValidator.setPreventInvalidTyping(true);
		
		
		cmbNatureOfTreatment = (ComboBox) binder.buildAndBind(
				"Nature of Treatment", "natureOfTreatment", ComboBox.class);
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
		/*
		if (null != bean.getNewIntimationDTO().getRoomCategory())
		{
			for(int i = 0 ; i<roomCategory.size() ; i++)
			{
				if ((bean.getNewIntimationDTO().getRoomCategory().getValue()).equalsIgnoreCase(roomCategory.getIdByIndex(i).getValue()))
				{
					this.cmbRoomCategory.setValue(roomCategory.getIdByIndex(i));
				}
				
			}
		}*/
		
		/*if(null != this.bean.getPreauthDataExtractionDetails() && null != this.bean.getPreauthDataExtractionDetails().getRoomCategory() 
				&& null != this.bean.getPreauthDataExtractionDetails().getRoomCategory().getValue())
				{
					if(null != roomCategory)
					for(int i =0 ; i<roomCategory.size() ; i++)
					{
						if((this.bean.getPreauthDataExtractionDetails().getRoomCategory().getValue()).equalsIgnoreCase(roomCategory.getIdByIndex(i).getValue()))
						{
							this.cmbRoomCategory.setValue(roomCategory.getIdByIndex(i).getValue());
						}
					}
//					/cmbRoomCategory.setValue(this.bean.getPreauthDataExtractionDetails().getRoomCategory().getValue());
				}
					*/
		
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
		
		organisationName = (TextField) binder.buildAndBind("Organisation Name", "organisationName", TextField.class);
		organisationName.setEnabled(false);
		
		sumInsured = (TextField) binder.buildAndBind("Sum Insured", "paSumInsured", TextField.class);
		sumInsured.setEnabled(false);
		
		parentName = (TextField) binder.buildAndBind("Parent Name", "parentName", TextField.class);
		//parentName.setEnabled(false);
		
		dateOfBirth = (DateField) binder.buildAndBind("Parent(DOB)", "dateOfBirth", DateField.class);
		//dateOfBirth.setEnabled(false);
		
		riskName = (TextField) binder.buildAndBind("Risk Name", "riskName", TextField.class);
		//riskName.setEnabled(false);
		
		age = (TextField) binder.buildAndBind("Parent(Age)", "age", TextField.class);
		//age.setEnabled(false);
		
		riskDOB = (DateField) binder.buildAndBind("Risk(DOB)", "gpaRiskDOB", DateField.class);
		
		riskAge = (TextField) binder.buildAndBind("Risk(Age)", "gpaRiskAge", TextField.class);
		
		sectionOrclass = (TextField) binder.buildAndBind("Section/Class", "gpaSection", TextField.class);
		sectionOrclass.setEnabled(false);
		
		cmbCategory = (ComboBox) binder.buildAndBind("Category", "gpaCategory", ComboBox.class);	
		cmbCategory.setEnabled(false);
		BeanItemContainer<SelectValue> selectValueForCategory = dbCalculationService.getGPACategory(bean.getNewIntimationDTO().getPolicy().getKey());
		setDropDownValues(selectValueForCategory);
		
		if((SHAConstants.AUTO_RESTORATION_NOTAPPLICABLE).equals(automaticRestorationTxt.getValue()) || (SHAConstants.AUTO_RESTORATION_NOTDONE).equals(automaticRestorationTxt.getValue()))
		{
			cmbIllness.setEnabled(false);
		}
		
		FormLayout illnessFLayout = new FormLayout(criticalIllnessChk);
		illnessFLayout.setCaption("Critical Illness");
		illnessFLayout.setMargin(false);
		FormLayout bufferFLayout = new FormLayout(corpBufferChk);
		bufferFLayout.setCaption("Corp Buffer");
		bufferFLayout.setMargin(false);
		
		cmbHospitalisationDueTo = (ComboBox) binder.buildAndBind(
				"Hospitalisation Due to", "hospitalisationDueTo", ComboBox.class);
		
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
		
		dateOfAccident = (DateField) binder.buildAndBind("Date of Accident",
				"dateOfAccident", DateField.class);
		
		dateOfDeath = (DateField) binder.buildAndBind("Date of Death",
				"dateOfDeath", DateField.class);
		
		dateOfDisablement = (DateField) binder.buildAndBind("Date of Disablement",
				"dateOfDisablement", DateField.class);

		firstFLayout = new FormLayout(accDeath,dateOfDisablement,referenceNoTxt, admissionDate,reasonForChangeInDOA,
				cmbRoomCategory, illnessFLayout, cmbSpecifyIllness, cmbHospitalisationDueTo);
		firstFLayout.setSpacing(true);
		firstFLayout.setMargin(false);
		secondFLayout = new FormLayout(dateOfAccident,dateOfDeath,reasonForAdmissionTxt,workOrNonWorkSpace,noOfDaysTxt,
				cmbNatureOfTreatment, firstConsultantDate, bufferFLayout,
				automaticRestorationTxt, cmbIllness, cmbSection,/*cmbCatastrophicLoss,*/cmbNatureOfLoss,cmbCauseOfLoss);
		secondFLayout.setMargin(false);
		
		gpaLayout1 = new FormLayout(organisationName,sumInsured,sectionOrclass,cmbCategory,parentName,dateOfBirth,age);
		gpaLayout1.setSpacing(true);
		gpaLayout1.setMargin(false);
		gpaLayout2 = new FormLayout(riskName,riskDOB,riskAge);
		
		//Fix for issue 692 -- starts
		//List<ProcedureDTO> procList = (null != procedureTableObj ? procedureTableObj.getValues() : null);
		this.diagnosisListenerTableObj =   diagnosisListnerTable.get();
		//this.diagnosisListenerTableObj.init("premedicalPreauth",procList);
		this.diagnosisListenerTableObj.init(this.bean, "premedicalPreauth");
		
		this.sectionDetailsListenerTableObj = sectionDetailsListenerTable.get();
		this.sectionDetailsListenerTableObj.init(this.bean, SHAConstants.PRE_MEDICAL_PRE_AUTH);
		if(this.referenceData != null) {
			this.sectionDetailsListenerTableObj.setReferenceData(referenceData);
		}
		this.sectionDetailsListenerTableObj.addBeanToList(bean.getPreauthDataExtractionDetails().getSectionDetailsDTO());

		HorizontalLayout formHLayout = new HorizontalLayout(firstFLayout,
				secondFLayout);
		formHLayout.setWidth("100%");
		
		HorizontalLayout gpaLayout = new HorizontalLayout(gpaLayout1,gpaLayout2);
		gpaLayout.setWidth("100%");
		gpaLayout.setCaption("UNNAMED RISK DETAILS");
		
		PreauthCoordinatorView preauthCoordinatorViewInstance = preauthCoordinatorView
				.get();
		preauthCoordinatorViewInstance.setWizard(wizard,SHAConstants.PRE_MEDICAL_PRE_AUTH);
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
//		dynamicElementsHLayout.setMargin(false);

		claimedDetailsTableObj = claimedAmountDetailsTable.get();
		claimedDetailsTableObj.initView(this.bean, SHAConstants.PRE_MEDICAL_PRE_AUTH);
		
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
		
		//Added for test.
		Label amtClaimedDetailsLbl = new Label("Amount Claimed Details");
		/*VerticalLayout lableLayout = new VerticalLayout();
		lableLayout.addComponent(amtClaimedDetailsLbl);
		lableLayout.setHeight("5px");
		lableLayout.setMargin(true);*/

		VerticalLayout lLayout = new VerticalLayout();
		lLayout.addComponent(amtClaimedDetailsLbl);
		lLayout.addComponent(hospDiscountHLayout);
		lLayout.addComponent(claimedDetailsTableObj);
//		lLayout.setMargin(false);
		//lLayout.setHeight("375px");
	//	wholeVLayout = new VerticalLayout(diagnosisListenerTableObj, formHLayout, diagnosisListenerTableObj,  dynamicElementsHLayout, tableVLayout, claimedDetailsTableObj ,  preauthCoordinatorViewInstance);
		wholeVLayout = new VerticalLayout(diagnosisListenerTableObj, formHLayout, sectionDetailsListenerTableObj, diagnosisListenerTableObj,dynamicElementsHLayout, tableVLayout, lLayout);
		if(null != bean && null != bean.getNewIntimationDTO() && null != bean.getNewIntimationDTO().getPolicy() &&
				null != bean.getNewIntimationDTO().getPolicy().getProduct() && null != bean.getNewIntimationDTO().getPolicy().getProduct().getKey() &&
				(ReferenceTable.getGPAProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) &&
				(SHAConstants.GPA_UN_NAMED_POLICY_TYPE.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getGpaPolicyType()))){
			
			wholeVLayout = new VerticalLayout(diagnosisListenerTableObj, formHLayout,gpaLayout, sectionDetailsListenerTableObj, diagnosisListenerTableObj,dynamicElementsHLayout, tableVLayout, lLayout);
		}
		
		
		wholeVLayout.setSpacing(false);
		wholeVLayout.setMargin(true);
		
		addListener();
		addAdmissionDateChangeListener();
		addDiagnosisNameChangeListener();
		mandatoryFields.add(admissionDate);
		
		
		mandatoryFields.add(cmbRoomCategory);
		mandatoryFields.add(cmbTreatmentType);
		mandatoryFields.add(cmbPatientStatus);

		showOrHideValidation(false);
		return wholeVLayout;
	}
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}
	
	private void alertMessageForClaimCount(Long claimCount){
		
		String msg = SHAConstants.CLAIM_COUNT_MESSAGE+claimCount;
		
		
   		Label successLabel = new Label(
				"<b style = 'color: black;'>"+msg+"</b>",
				ContentMode.HTML);
//   		successLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
//   		successLabel.addStyleName(ValoTheme.LABEL_BOLD);
   		successLabel.addStyleName(ValoTheme.LABEL_H3);
   		Button homeButton = new Button("ok");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		HorizontalLayout mainHor = new HorizontalLayout(successLabel);
		TextField dummyField = new TextField();
		dummyField.setEnabled(false);
		dummyField.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
   		VerticalLayout firstForm = new VerticalLayout(dummyField,mainHor,homeButton);
   		
		Panel panel = new Panel(firstForm);
		
		if(this.bean.getClaimCount() > 1 && this.bean.getClaimCount() <=2){
			panel.addStyleName("girdBorder1");
		}else if(this.bean.getClaimCount() >2){
			panel.addStyleName("girdBorder2");
		}
		
		panel.setHeight("135px");
//		panel.setSizeFull();
		
		
		final Window popup = new com.vaadin.ui.Window();
		popup.setWidth("30%");
		popup.setHeight("20%");
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
				
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
		
		
		
		reasonForAdmissionTxt.setValue(this.bean.getReasonForAdmission());
		//Setting this feild readOnly true, since this needs to be editable as per sathish sir.
		reasonForAdmissionTxt.setReadOnly(true);
		reasonForAdmissionTxt.setEnabled(false);
		
		//reasonForChangeInDOA.setNullRepresentation(reasonForAdmissionTxt.getValue());
		
		BeanItemContainer<SelectValue> treatementType = (BeanItemContainer<SelectValue>) referenceData
				.get("treatmentType");
		roomCategory = (BeanItemContainer<SelectValue>) referenceData
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
		
		BeanItemContainer<SelectValue> natureOfLoss = (BeanItemContainer<SelectValue>) referenceData
				.get("natureOfLoss");
		
		BeanItemContainer<SelectValue> catastrophicLoss = (BeanItemContainer<SelectValue>) referenceData
				.get("catastrophicLoss");
		
		BeanItemContainer<SelectValue> causeOfLoss = (BeanItemContainer<SelectValue>) referenceData
				.get("causeOfLoss");
		
		this.bean.getPolicyDto().setAdmissionDate(admissionDate.getValue());
		//fireViewEvent(PreMedicalPreauthWizardPresenter.SET_DB_DETAILS_TO_REFERENCE_DATA, this.bean.getPolicyDto());
		/**
		 * In menu presenter , the below observer method uses preauthDTO for further processing. But since 
		 * here policyDto is passed, class cast exception was thrown. Hence to overcome the same, preauthDTO is passed
		 * instead of policyDTO. Moreover, from preauthDTO, policyDTO can be obtained. Hence there will be
		 * no impact due to the below change.
		 * */
		if(cmbSection != null && cmbSection.getValue() != null){
			SelectValue sectionValue = (SelectValue) cmbSection.getValue();
			this.bean.getPreauthDataExtractionDetails().setSection(sectionValue);
		}
		
		fireViewEvent(PAPreMedicalPreauthWizardPresenter.SET_DB_DETAILS_TO_REFERENCE_DATA, this.bean);
		
		cmbTreatmentType.setContainerDataSource(treatementType);
		cmbTreatmentType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbTreatmentType.setItemCaptionPropertyId("value");

		cmbRoomCategory.setContainerDataSource(roomCategory);
		cmbRoomCategory.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbRoomCategory.setItemCaptionPropertyId("value");

		if(this.bean.getPolicyDto().getProduct().getNonAllopathicFlag() != null && this.bean.getPolicyDto().getProduct().getNonAllopathicFlag().toLowerCase().equalsIgnoreCase("n")) {
			List<SelectValue> itemIds2 = natureOfTreatment.getItemIds();
			List<SelectValue> allopathicValues = new ArrayList<SelectValue>() ; 
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
		
		cmbHospitalisationDueTo.setContainerDataSource(hospitalizationDueTo);
		cmbHospitalisationDueTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbHospitalisationDueTo.setItemCaptionPropertyId("value");
		cmbHospitalisationDueTo.setEnabled(false);
		
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
			for(SelectValue val : catastrophicLoss.getItemIds()) {
				if(this.bean.getPreauthDataExtractionDetails().getCatastrophicLoss().getValue().equalsIgnoreCase(val.getValue())) {
					cmbCatastrophicLoss.setValue(val.getValue());
					break;
				}
			}
			
		}else if(cmbCatastrophicLoss.getValue() == null) {
			SelectValue sectionValue = (SelectValue) cmbCatastrophicLoss.getValue();
			this.bean.getPreauthDataExtractionDetails().setCatastrophicLoss(sectionValue);
		}*/
		
		if(this.bean.getPreauthDataExtractionDetails().getNatureOfLoss() != null) {
			for(SelectValue val : natureOfLoss.getItemIds()) {
				if(this.bean.getPreauthDataExtractionDetails().getNatureOfLoss().getValue().equalsIgnoreCase(val.getValue())) {
					cmbNatureOfLoss.setValue(val);
					break;
				}
			}
			
		}else if(cmbNatureOfLoss.getValue() == null) {
			SelectValue sectionValue = (SelectValue) cmbNatureOfLoss.getValue();
			this.bean.getPreauthDataExtractionDetails().setNatureOfLoss(sectionValue);
		}
		
		if(this.bean.getPreauthDataExtractionDetails().getCauseOfLoss() != null) {
			for(SelectValue val : causeOfLoss.getItemIds()) {
				if(this.bean.getPreauthDataExtractionDetails().getCauseOfLoss().getValue().equalsIgnoreCase(val.getValue())) {
					cmbCauseOfLoss.setValue(val);
					break;
				}
			}
		}else if(cmbCauseOfLoss.getValue() == null) {
			SelectValue sectionValue = (SelectValue) cmbCauseOfLoss.getValue();
			this.bean.getPreauthDataExtractionDetails().setCauseOfLoss(sectionValue);
		}
		
		List<SelectValue> itemIds = hospitalizationDueTo.getItemIds();
		for (SelectValue selectValue : itemIds) {
			if(selectValue.getId().equals(ReferenceTable.INJURY_MASTER_ID)) {
				this.bean.getPreauthDataExtractionDetails().setHospitalisationDueTo(selectValue);
			}
		}
		
		if (this.bean.getPreauthDataExtractionDetails().getHospitalisationDueTo() != null) {
			this.cmbHospitalisationDueTo.setValue(this.bean.getPreauthDataExtractionDetails().getHospitalisationDueTo());
		}
		
		cmbSpecifyIllness.setReadOnly(true);
		
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
		
		
		
		/**
		 * Room category mentioned at intimation level 
		 * will be set into new intimation DTO . Since preAuthDataExtraction
		 * details().getRoomCategory() was null, this approach was followed.
		 * */

		if (null != bean.getNewIntimationDTO().getRoomCategory())
		{
			for(int i = 0 ; i<roomCategory.size() ; i++)
			{
				if ((bean.getNewIntimationDTO().getRoomCategory().getValue()).equalsIgnoreCase(roomCategory.getIdByIndex(i).getValue()))
				{
					this.cmbRoomCategory.setValue(roomCategory.getIdByIndex(i));
				}
				
			}
		}
		if(null != this.bean.getPreauthDataExtractionDetails() && null != this.bean.getPreauthDataExtractionDetails().getRoomCategory() 
				&& null != this.bean.getPreauthDataExtractionDetails().getRoomCategory().getValue())
				{
					if(null != roomCategory)
					for(int i =0 ; i<roomCategory.size() ; i++)
					{
						if((this.bean.getPreauthDataExtractionDetails().getRoomCategory().getValue()).equalsIgnoreCase(roomCategory.getIdByIndex(i).getValue()))
						{
							this.cmbRoomCategory.setValue(roomCategory.getIdByIndex(i));
						}
					}
				//cmbRoomCategory.setValue(this.bean.getPreauthDataExtractionDetails().getRoomCategory().getValue());
				}
					
		/*if (this.bean.getPreauthDataExtractionDetails().getRoomCategory() != null) {
		//	System.out.println("---the value----"+);
			this.cmbRoomCategory.setValue(this.bean
					.getPreauthDataExtractionDetails().getRoomCategory());
		}*/
		
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

		this.preauthCoordinatorViewInstance.setUpReference(referenceData);
//		this.claimedDetailsTableObj.setDBCalculationValues((Map<String, Double>)referenceData.get("claimDBDetails"));
		
		this.diagnosisListenerTableObj.setReferenceData(referenceData);
		
		this.sectionDetailsListenerTableObj.setReferenceData(referenceData);
		
		if(this.procedureTableObj != null) {
			this.procedureTableObj.setReferenceData(referenceData);
		}
		
		
		setTableValues();
	}

	@Override
	public boolean onAdvance() {
		setTableValuesToDTO();
        if(validatePage()) {
			
			if(this.bean.getAlertMessageOpened()){
				this.bean.setAlertMessageOpened(false);
				if(bean.getIsDialysis() && !bean.getDialysisOpened()) {
					alertNoOfSittings();
					return false;
				}
				bean.setDialysisOpened(false);
				setTableValuesToDTO();
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
		
		boolean diagValues = true;
		
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

		try {
			if(!this.preauthCoordinatorViewInstance.isValid()) {
				hasError = true;
				List<String> errors = this.preauthCoordinatorViewInstance.getErrors();
				for (String error : errors) {
					eMsg += error;
				 }
			}
		} catch (CommitException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(null != this.cmbTreatmentType && null == this.cmbTreatmentType.getValue())
		{
			hasError = true;
			eMsg += "Please Select Treatment Type. </br>";
		}
		
		if(this.diagnosisListenerTableObj != null && this.diagnosisListenerTableObj.getValues().isEmpty()) {
			hasError = true;
			eMsg += "Please Add Atleast one Diagnosis Details to Proceed Further. </br>"; 
		}
		
		if(this.reasonForChangeInDOA.isVisible() && !(null != this.reasonForChangeInDOA && null!= this.reasonForChangeInDOA.getValue()))
			
		{
			hasError = true;
			eMsg += "Please enter Reason For Change in DOA to Proceed Further. </br>";
					
		}
		
	
		
		if((this.procedureTableObj != null && this.procedureTableObj.getValues().isEmpty()) && (this.newProcedurdTableObj != null && this.newProcedurdTableObj.getValues().isEmpty())) {
			hasError = true;
			eMsg += "Please Add Atleast one Procedure List Details to Proceed Further. </br>"; 
		}
		
		/*String strMsg = validateProcedureAndDiagnosisName();
		if(null != strMsg && !("").equalsIgnoreCase(strMsg))
		{
			eMsg += strMsg;
			hasError = true;
		}*/
		
		if(this.claimedDetailsTableObj != null && this.claimedDetailsTableObj.getTotalPayableAmt() <= 0){
			hasError = true;
			eMsg += "Claimed Payable amount should not be Zero. </br>";
		}
		
		if(this.claimedDetailsTableObj != null && !claimedDetailsTableObj.isValid(false)) {
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
				if(admissionDate != null && admissionDate.getValue() != null){
					bean.setDateOfAdmission(admissionDate.getValue());
				}
				
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
				
				Date policyFromDate = bean.getNewIntimationDTO().getPolicy().getPolicyFromDate();
				
				MastersValue policyType = bean.getNewIntimationDTO().getPolicy().getPolicyType();
//				bean.getPolicyDto().getPolicyType();
				Long diffDays = SHAUtils.getDiffDays(policyFromDate, admissionDate.getValue());
				if((diffDays != 0 && diffDays > 90) || (policyType != null && policyType.getKey().equals(ReferenceTable.RENEWAL_POLICY)) || !bean.getAdmissionDatePopup()){
					this.bean.setAlertMessageOpened(true);
				}
				if(SHAUtils.getDialysisDiagnosisDTO(bean.getPreauthDataExtractionDetails().getDiagnosisTableList()) != null || SHAUtils.getDialysisProcedureDTO(bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList()) != null) {
					this.bean.setIsDialysis(true);
				}
				
				if(this.bean.getPreauthDataExtractionDetails().getNoOfDays() == null || (this.bean.getPreauthDataExtractionDetails().getNoOfDays() != null
						&& this.bean.getPreauthDataExtractionDetails().getNoOfDays().equalsIgnoreCase(""))){
					this.bean.getPreauthDataExtractionDetails().setNoOfDays("0");
					
				}
				
			} catch (CommitException e) {
				e.printStackTrace();
			}
			showOrHideValidation(false);
			return true;
		}
	}
	
	/*private String validateProcedureAndDiagnosisName()
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
	*/
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
			} else {
				List<PedDetailsTableDTO> pedList = diagnosisDetailsTableDTO.getPedList();
				for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {
					pedDetailsTableDTO.setDiagnosisName(diagnosisDetailsTableDTO.getDiagnosis());
				}
			}
		}
		
		this.bean.getPreauthDataExtractionDetails().setDiagnosisTableList(diagnosisList);
		
		List<SectionDetailsTableDTO> sectionDetailsList = this.sectionDetailsListenerTableObj.getValues();
		this.bean.getPreauthDataExtractionDetails().setSectionDetailsDTO(sectionDetailsList.get(0));
		
		List<ProcedureDTO> wholeProcedureList = new ArrayList<ProcedureDTO>();
		wholeProcedureList.addAll(this.bean.getPreauthDataExtractionDetails().getProcedureList());
		wholeProcedureList.addAll(this.bean.getPreauthDataExtractionDetails().getNewProcedureList());
		
		this.bean.getPreauthMedicalProcessingDetails().setProcedureExclusionCheckTableList(wholeProcedureList);
		
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
					SelectValue considerForDayCare = new SelectValue();
					considerForDayCare.setId(ReferenceTable.COMMONMASTER_NO);
					considerForDayCare.setValue(SHAConstants.No);
					procedureDTO.setConsiderForDayCare(considerForDayCare);
				} else {
					SelectValue procedureName = new SelectValue();
					procedureName.setValue(procedureDTO.getProcedureNameValue());
					procedureDTO.setProcedureName(procedureName);
					SelectValue procedureCode = new SelectValue();
					procedureCode.setValue(procedureDTO.getProcedureCodeValue());
					procedureDTO.setProcedureCode(procedureCode);
				}
				
			//	procedureDTOList
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
				if(null != specialityDTO && null != specialityDTO.getEnableOrDisable() && !specialityDTO.getEnableOrDisable())
				{
					specialityDTO.setStatusFlag(false);
					specialityDTO.setEnableOrDisable(false);
				}
				else
				{
					specialityDTO.setStatusFlag(true);
					specialityDTO.setEnableOrDisable(true);
				}
				this.specialityTableObj.addBeanToList(specialityDTO);
			}
		}
		
		List<ProcedureDTO> procedureExclusionCheckTableList = this.bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList();
		List<ProcedureDTO> oldProcedureDTOList = new ArrayList<ProcedureDTO>();
	
		List<ProcedureDTO> newProcedureDTOList = new ArrayList<ProcedureDTO>();
		if(!procedureExclusionCheckTableList.isEmpty()) {
			for (ProcedureDTO procedureDTO : procedureExclusionCheckTableList) {
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
				this.newProcedurdTableObj.addBeanToList(newProcedureTableDTO);
			}
		}
		
		if(this.procedureTableObj != null) {
			List<ProcedureDTO> procedureList = this.bean.getPreauthDataExtractionDetails().getProcedureList();
			for (ProcedureDTO procedureTableDTO : procedureList) {
				this.procedureTableObj.addBeanToList(procedureTableDTO);
			}
		}
		
		if(this.diagnosisListenerTableObj != null) {
			List<DiagnosisDetailsTableDTO> diagnosisList = this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList();
			for (DiagnosisDetailsTableDTO diagnosisDTO : diagnosisList) {
				this.diagnosisListenerTableObj.addBeanToList(diagnosisDTO);
			}
		}
		
		if(this.claimedDetailsTableObj != null) {
			this.claimedDetailsTableObj.setValues(this.bean.getPreauthDataExtractionDetails().getClaimedDetailsList(), false);
		}
		
		if(this.bean.getDeletedClaimedAmountIds() != null && !this.bean.getDeletedClaimedAmountIds().isEmpty()) {
			this.claimedDetailsTableObj.setDeletedItems(this.bean.getDeletedClaimedAmountIds());
		}
	}

	@Override
	public boolean onBack() {
		//isBackClicked = true;
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
			
			cmbSpecifyIllness.setValue(null);
			cmbSpecifyIllness.setNullSelectionAllowed(true);
		
			cmbSpecifyIllness.setReadOnly(true);
		}
	}
	
	
	protected void addAdmissionDateChangeListener()
	{
		
			admissionDate.setValue(this.bean.getDateOfAdmission());

		admissionDate.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				admissionDate.setValue(admissionDate.getValue());
				
				if(admissionDate.getValue() != null && (bean.getNewIntimationDTO().getAdmissionDate().compareTo(admissionDate.getValue()))!=0){
				
				reasonForChangeInDOA.setVisible(true);
				reasonForChangeInDOA.setRequired(true);
				/**
				 * If admission Date is changed, then reasonForChangeInDOA 
				 * is mandatory
				 * */
				mandatoryFields.add(reasonForChangeInDOA);
				}
				else{
					reasonForChangeInDOA.setValue(null);
					unbindField(reasonForChangeInDOA);
					reasonForChangeInDOA.setVisible(false);
					reasonForChangeInDOA.setRequired(false);
					mandatoryFields.remove(reasonForChangeInDOA);
				}
				
				// TODO Auto-generated method stub
				
			}
		});
	}

	protected void addListener() {
		
		

		criticalIllnessChk
				.addValueChangeListener(new Property.ValueChangeListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						Boolean checkValue = criticalIllnessChk.getValue();
						fireViewEvent(
								PAPreMedicalPreauthWizardPresenter.CHECK_CRITICAL_ILLNESS,
								checkValue, true);
					}
				});

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
								PAPreMedicalPreauthWizardPresenter.PREMEDICAL_TREATMENT_TYPE_CHANGED,
								null);
					}
				} else {
					fireViewEvent(
							PAPreMedicalPreauthWizardPresenter.PREMEDICAL_TREATMENT_TYPE_CHANGED,
							null);
				}
				
				
			}
		});
		
             cmbRoomCategory.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				SelectValue value = (SelectValue) event.getProperty().getValue();
				bean.getNewIntimationDTO().setRoomCategory(value);
				
//				List<ProcedureDTO> procedureList = new ArrayList<ProcedureDTO>();
//				
//				if(procedureTableObj != null){
//					List<ProcedureDTO> values = procedureTableObj.getValues();
//					procedureList.addAll(values);
//					procedureTableObj.removeRow();
//					for (ProcedureDTO procedureDTO : procedureList) {
////						procedureDTO.setProcedureCode(null);
//						procedureTableObj.addBeanToList(procedureDTO);
//					}
//				}
			
			}
		});
             
             cmbSection.addValueChangeListener(new ValueChangeListener() {
     			
     			@Override
     			public void valueChange(ValueChangeEvent event) {
     				
     				if(cmbSection != null && cmbSection.getValue() != null){
     					SelectValue sectionValue = (SelectValue) cmbSection.getValue();
     					bean.getPreauthDataExtractionDetails().setSection(sectionValue);
     				}
     				
     				fireViewEvent(PAPreMedicalPreauthWizardPresenter.SUBLIMIT_CHANGED_BY_SECTION, bean);
     				

     				List<DiagnosisDetailsTableDTO> diagnosisList = new ArrayList<DiagnosisDetailsTableDTO>();
     				
     				diagnosisList = diagnosisListenerTableObj.getValues();
     				
     				
     				diagnosisListenerTableObj.init(bean,"premedicalPreauth");
     				diagnosisListenerTableObj.setReferenceData(referenceData);
     				
     				for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisList) {
     					
     					diagnosisListenerTableObj.addBeanToList(diagnosisDetailsTableDTO);
						
					}
 
     				if(procedureTableObj != null) {
     					
     					List<ProcedureDTO> procedureDTO = new ArrayList<ProcedureDTO>();
     					
     					procedureDTO = procedureTableObj.getValues();
     				
     					procedureTableObj.init(bean.getHospitalCode(), "premedicalPreauth", bean);
     					procedureTableObj.setReferenceData(referenceData);
     					
                        for (ProcedureDTO procedureDTO2 : procedureDTO) {
							procedureTableObj.addBeanToList(procedureDTO2);
						}
     				}
     				
     				if(claimedDetailsTableObj != null){
     					
     					List<NoOfDaysCell> claimedAmountValues = new ArrayList<NoOfDaysCell>();
     					
     					claimedAmountValues.addAll(claimedDetailsTableObj.getValues());
     					
//     					claimedDetailsTableObj.initView(bean, SHAConstants.PRE_MEDICAL_PRE_AUTH);
     					Map<Integer,Object> values = (Map<Integer,Object>)referenceData.get("claimDBDetails");
     					claimedDetailsTableObj.setDBCalculationValues(values);
     					for (NoOfDaysCell noOfDaysCell : claimedAmountValues) {
							if(noOfDaysCell.getBenefitId() != null && values.containsKey(noOfDaysCell.getBenefitId().intValue())) {
								noOfDaysCell.setPolicyPerDayPayment(((Double)values.get(noOfDaysCell.getBenefitId().intValue())).intValue());
							}
						}
     					claimedDetailsTableObj.setValuesForSectionChange(claimedAmountValues, false);
     					
     				}
     				
//     				List<ProcedureDTO> procedureList = new ArrayList<ProcedureDTO>();
//     				
//     				if(procedureTableObj != null){
//     					List<ProcedureDTO> values = procedureTableObj.getValues();
//     					procedureList.addAll(values);
//     					procedureTableObj.removeRow();
//     					for (ProcedureDTO procedureDTO : procedureList) {
     				
////     						procedureDTO.setProcedureCode(null);
//     						procedureTableObj.addBeanToList(procedureDTO);
//     					}
//     				}
     			
     			}
     		});
             
             /*cmbCatastrophicLoss.addValueChangeListener(new ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					SelectValue value = (SelectValue) event.getProperty().getValue();
					bean.getPreauthDataExtractionDetails().setCatastrophicLoss(value);
				}
			});*/
             
             cmbNatureOfLoss.addValueChangeListener(new ValueChangeListener() {
 				
 				@Override
 				public void valueChange(ValueChangeEvent event) {
 					SelectValue value = (SelectValue) event.getProperty().getValue();
 					bean.getPreauthDataExtractionDetails().setNatureOfLoss(value);
 				}
 			});
             
             cmbCauseOfLoss.addValueChangeListener(new ValueChangeListener() {
 				
 				@Override
 				public void valueChange(ValueChangeEvent event) {
 					SelectValue value = (SelectValue) event.getProperty().getValue();
 					bean.getPreauthDataExtractionDetails().setCauseOfLoss(value);
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
						PAPreMedicalPreauthWizardPresenter.PREMEDICAL_PATIENT_STATUS_CHANGED,
						null);
			}
		});
		
		admissionDate.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -8435623803385270083L;

			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {
				Date enteredDate = (Date) event.getProperty().getValue();
				if(enteredDate != null) {
					Date policyFromDate = bean.getPolicyDto().getPolicyFromDate();
					Date policyToDate = bean.getPolicyDto().getPolicyToDate();
					
					final ConfirmDialog dialog = new ConfirmDialog();
					dialog.setClosable(false);
					dialog.setResizable(false);
					
					if (!( DateUtils.isSameDay(enteredDate, policyFromDate) || enteredDate.after(policyFromDate)) || !(DateUtils.isSameDay(enteredDate, policyToDate) || enteredDate.before(policyToDate) )) {
						
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
				
			}
		});
		
		cmbHospitalisationDueTo.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -2577540521492098375L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty().getValue();
//				if(value != null) {
					fireViewEvent(
							PAPreMedicalPreauthWizardPresenter.PREMEDICAL_HOSPITALISATION_DUE_TO,
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
					fireViewEvent(PAPreMedicalPreauthWizardPresenter.SET_DB_DETAILS_TO_REFERENCE_DATA, bean);
					if(diagnosisListenerTableObj != null && !diagnosisListenerTableObj.getValues().isEmpty()) {
						diagnosisListenerTableObj.changeSublimitValues();
					} 
					/*if(procedureTableObj != null && !procedureTableObj.getValues().isEmpty()) {
						procedureTableObj.changeSublimitValues();
					}*/
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
				final ConfirmDialog dialog = new ConfirmDialog();
				dialog.setClosable(false);
				dialog.setResizable(false);
				Date enteredDate = (Date) event.getProperty().getValue();
				if(null != enteredDate && null != admissionDate && null != admissionDate.getValue())
					if(!SHAUtils.validateDeathDate(enteredDate,admissionDate.getValue()))
					{
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
						VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color: red;'>Please select valid death date. Death date is not in range between admission date and current date.</b>", ContentMode.HTML));
						layout.setMargin(true);
						layout.setSpacing(true);
						dialog.setContent(layout);
						dialog.setCaption("Error");
						dialog.setClosable(true);
						dialog.show(getUI().getCurrent(), null, true);
						
						event.getProperty().setValue(null);
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
				
			    isValid = true;
			    bean.setAlertMessageOpened(true);
			    if(!bean.getIsDialysis()) {
			    	wizard.next();
			    }
			    dialog.close();

			}
		});

		return isValid;

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
							PAPreMedicalPreauthWizardPresenter.PREMEDICAL_TREATMENT_TYPE_CHANGED,
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

	
	private void claimDetailsNoOfDaysListener() {
		BlurListener listener = new BlurListener() {
			private static final long serialVersionUID = -6214266810135299292L;

			@Override
			public void blur(BlurEvent event) {
//				fireViewEvent(PreMedicalPreauthWizardPresenter, primaryParameter, secondaryParameters);
			}
		};

	}

	private void unbindField(Field<?> field) {
		if (field != null ) {
			Object propertyId = this.binder.getPropertyId(field);
			if (field!= null && field.isAttached() && propertyId != null) {
				this.binder.unbind(field);
			}
		}
	}

	public void generatedFieldsBasedOnTreatment() {
		if (cmbTreatmentType.getValue() != null) {
			Boolean isMedicalSeclted = true;
			if (cmbTreatmentType.getValue().toString().toLowerCase()
					.contains("medical")) {
				unbindField(treatmentRemarksTxt);
				treatmentRemarksTxt = (TextArea) binder.buildAndBind(
						"Treatment Remarks", "treatmentRemarks",
						TextArea.class);
				treatmentRemarksTxt.setWidth("400px");
				treatmentFLayout.addComponent(treatmentRemarksTxt);
//				treatmentRemarksTxt.setRequired(true);
//				treatmentRemarksTxt.setValidationVisible(false);
				treatmentRemarksTxt.setMaxLength(4000);
//				CSValidator validator = new CSValidator();
//				treatmentRemarksTxt.setMaxLength(100);
//				validator.extend(treatmentRemarksTxt);
//				validator.setRegExp("^[a-zA-Z 0-9]*$");
//				validator.setPreventInvalidTyping(true);
//				mandatoryFields.add(treatmentRemarksTxt);

				if (tableVLayout != null
						&& tableVLayout.getComponentCount() > 0) {
					tableVLayout.removeAllComponents();
				}
				this.procedureTableObj = null;
				this.newProcedurdTableObj = null;

			} else {
				if (treatmentRemarksTxt != null) {
					unbindField(treatmentRemarksTxt);
					treatmentFLayout.removeComponent(treatmentRemarksTxt);
//					mandatoryFields.remove(treatmentRemarksTxt);

				}
				isMedicalSeclted = false;
				PAProcedureListenerTableForPremedical procedureTableInstance = procedureListenerTable.get();
				
				List<DiagnosisDetailsTableDTO> diagList = null;
				if(null != diagnosisListenerTableObj)
				{
					diagList = diagnosisListenerTableObj.getValues();
				}
				
				//procedureTableInstance.init(bean.getHospitalKey(), "premedicalPreauth", diagList);
				procedureTableInstance.init(bean.getHospitalCode(), "premedicalPreauth", bean);
				this.procedureTableObj = procedureTableInstance;
				this.procedureTableObj.diagnosisList = diagnosisList;
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
		// TODO Auto-generated method stub

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
		
	}
	
	public void setCustomDiagValue(SelectValue selValue,ComboBox cmbBox)
	{
		diagnosisListenerTableObj.setCustomDiagValueToContainer(selValue,cmbBox);
	}
	/*public void setCustomDiagValue(BeanItemContainer<SelectValue> selectValue,ComboBox cmbBox,Long key)
	{
		diagnosisListenerTableObj.setCustomDiagValueToContainer(selectValue,cmbBox , key);
	}*/
	
	protected void addDiagnosisNameChangeListener()
	{
		this.diagnosisListenerTableObj.dummyField.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				String diagnosisValue = (String)event.getProperty().getValue();
				if(null != procedureTableObj)
				{
					if(!procedureTableObj.diagnosisList.contains(diagnosisValue))
					{
						procedureTableObj.diagnosisList.add(diagnosisValue);
					}
				System.out.println("---the diagnosisListValue----"+procedureTableObj.diagnosisList);
				}
				else
				{
					diagnosisList = new ArrayList<String>();
					if(!diagnosisList.contains(diagnosisValue))
						diagnosisList.add(diagnosisValue);
				}
				/*if(null != newProcedurdTableObj)
				{
					String diagnosisValue = (String)event.getProperty().getValue();
					if(!newProcedurdTableObj.diagnosisList.contains(diagnosisValue))
					{
						newProcedurdTableObj.diagnosisList.add(diagnosisValue);
					}
				System.out.println("---the diagnosisListValue----"+newProcedurdTableObj.diagnosisList);
				}*/
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
							fireViewEvent(PAPreMedicalPreauthWizardPresenter.PRE_MEDICAL_REPORTED_TO_POLICE, isChecked);
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
				} else {
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

}

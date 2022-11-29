package com.shaic.claim.viewEarlierRodDetails.Page;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ClaimMapper;
import com.shaic.claim.intimation.create.dto.PolicyDto;
import com.shaic.claim.preauth.wizard.dto.CoordinatorDTO;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDataExtaractionDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthMedicalDecisionDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.preauth.wizard.dto.SpecialityDTO;
import com.shaic.claim.preauth.wizard.dto.TreatingDoctorDTO;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.shaic.claim.premedical.mapper.PreMedicalMapper;
import com.shaic.claim.reimbursement.dto.OtherClaimDiagnosisDTO;
import com.shaic.claim.reimbursement.dto.ZonalReviewUpdateHospitalDetailsDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto.MedicalVerificationDTO;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.mapper.ZonalMedicalReviewMapper;
import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.claim.scoring.HospitalScoringDetailView;
import com.shaic.claim.viewEarlierRodDetails.Table.ViewDiagnosisTable;
import com.shaic.claim.viewEarlierRodDetails.Table.ViewMedicalVerificationTable;
import com.shaic.claim.viewEarlierRodDetails.Table.ViewProcedureListTable;
import com.shaic.claim.viewEarlierRodDetails.Table.ViewSpecialityTable;
import com.shaic.claim.viewEarlierRodDetails.Table.ViewTreatementQualityVerificationTable;
import com.shaic.claim.viewEarlierRodDetails.Table.ViewTreatingDoctorTabel;
import com.shaic.domain.AssignedInvestigatiorDetails;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.FieldVisitRequestService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.IntimationService;
import com.shaic.domain.Investigation;
import com.shaic.domain.InvestigationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.PEDValidationService;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.PreviousClaimedHistory;
import com.shaic.domain.PreviousClaimedHospitalization;
import com.shaic.domain.RODBillDetails;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.ReimbursementQuery;
import com.shaic.domain.ReimbursementQueryService;
import com.shaic.domain.ReimbursementRejection;
import com.shaic.domain.ReimbursementRejectionService;
import com.shaic.domain.SublimitFunObject;
import com.shaic.domain.TmpInvestigation;
import com.shaic.domain.UpdateHospital;
import com.shaic.domain.ViewTmpClaim;
import com.shaic.domain.preauth.ClaimLimit;
import com.shaic.domain.preauth.DiagnosisPED;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.PreauthEscalate;
import com.shaic.domain.preauth.Speciality;
import com.shaic.domain.preauth.TreatingDoctorDetails;
import com.shaic.domain.reimbursement.MedicalApprover;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.domain.PolicyMapper;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.paclaim.reimbursement.medicalapproval.processclaimrequest.pages.dataextraction.PAInvestigationReviewRemarksTable;
import com.shaic.paclaim.rod.wizard.dto.PABenefitsDTO;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class ViewPAMedicalSummaryPage extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private PreauthDTO bean;

	private Window popup;

	@Inject
	private Instance<ViewMedicalSummaryMoreDetailsPage> moreDetailsPageInstance;
	@Inject
	private ViewMedicalSummaryMoreDetailsPage moreDetailsPageObj;

	@EJB
	private ReimbursementService reimbursementService;

	@EJB
	private IntimationService intimationService;

	@EJB
	private ClaimService claimService;
	
	@EJB
	private PolicyService policyService;

	@EJB
	private HospitalService hosptialService;

	@EJB
	private PEDValidationService pedValidationService;

	@EJB
	private PreauthService preauthService;

	@EJB
	private HospitalService hospitalService;

	@EJB
	private ReimbursementQueryService queryService;

	@EJB
	private ReimbursementRejectionService rejectionService;

	@EJB
	private FieldVisitRequestService fvrService;

	@Inject
	private Instance<ViewDiagnosisTable> viewDiagnosisTableInstance;

	@Inject
	private ViewDiagnosisTable viewDiagnosisTableObj;

	private BeanFieldGroup<PreauthDataExtaractionDTO> binder;

	@Inject
	private Instance<ViewProcedureListTable> viewProcedureListTableInstance;

	@Inject
	private ViewProcedureListTable viewProcedureListTableObj;

	private Map<String, Object> referenceData = new HashMap<String, Object>();

	@Inject
	private Instance<ViewMedicalVerificationTable> medicalVerificationTableInstance;

	@Inject
	private ViewMedicalVerificationTable medicalVerificationTableObj;

	@Inject
	private Instance<ViewTreatementQualityVerificationTable> treatementVerificationInstance;

	@Inject
	private ViewTreatementQualityVerificationTable treatementVerificationObj;

	@Inject
	private Instance<ViewSpecialityTable> viewSpecialityTableInstance;

	@Inject
	private ViewSpecialityTable viewSpecialityTableObj;
	
	
	private String spleTable;

	@EJB
	private CreateRODService rodService;

	private Button viewMoreDetailsBtn;
	
	private Button btnHospitalScroing;
	
	@Inject
	private HospitalScoringDetailView hospitalScoringView;

	private TextField txtStatus;

	private TextField txtApprovedAmount;

	private TextArea txtMedicalRemarks;

	private TextArea txtRemarks;

	private TextField txtDoctorNote;

	private TextField txtDateOfAdmission;

	private TextField ReasonForChangeAdmission;

	private TextField txtRoomCategory;
	
	private OptionGroup ventilatorSupportRadio;

	private TextField txtSpecify;

	private TextField txtDateOfDischarge;

	private TextField txtSystemMedicine;

	private TextField txtHospitalisationDueTo;

	private TextField txtDateOfInjury;

	private OptionGroup medicoLegalCase;

	private OptionGroup reportedToPolice;

	private TextField txtRelapseOfIllness;

	private TextField txtReasonForAdmission;

	private TextField txtNoOfDays;

	private TextField txtNatureOfTreatment;

	private TextField txtFirstConsultaionDate;

	private CheckBox chkCorpBuffer;

	private TextField txtAutoRestoration;

	private TextField txtIllness;

	private TextField txtPatientStatus;

	private TextField txtCauseOfInjury;

	private TextField txtTreatementType;

	/*private CheckBox chkInvestigationReviewed;

	private TextField txtInvestigatorName;

	private TextField txtInvestigationReviewRemarks;*/

	private Reimbursement reimbursement;

	@EJB
	private AcknowledgementDocumentsReceivedService documentService;


	private List<SpecialityDTO> specialityList;

	private List<ProcedureDTO> procedureList;

	private List<MedicalVerificationDTO> medicalVerification;

	private List<MedicalVerificationDTO> treatmentVerification;

	private List<DiagnosisDetailsTableDTO> diagnosisList = new ArrayList<DiagnosisDetailsTableDTO>();

	@EJB
	private MasterService masterService;

	@EJB
	private InvestigationService investigationService;
	
	private OptionGroup acctDeath;
	
	private TextField dateAcctDeath;
	
	private TextField benefit;
	
	@EJB
	private CreateRODService createRODService;

	@Inject
	private Instance<PAOptionalCoverTableForView> optionCoverTableInstance;
	
	@Inject
	private Instance<PAAddOnCoverTableForView> addOnCoverTableInstance;
	
	@Inject
	private PAOptionalCoverTableForView optionalCover;
	
	@Inject
	private PAAddOnCoverTableForView addOnCover;
	
	
	private Instance<PABenefitsTableForView> paBenefitsTableObj;
	
	@Inject
	private PABenefitsTableForView paBenefitsTable;
	
	/*@Inject
	private PABenefitsListenerTable paBenefitsTable;*/
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	@Inject
	private Instance<ViewTreatingDoctorTabel> treatingDoctorTableList;
	
	private ViewTreatingDoctorTabel treatingDoctorTableObj;
	
	private List<TreatingDoctorDTO> treatingDoctorList;
	
	@Inject
	private Instance<PAInvestigationReviewRemarksTable> invsReviewRemarksTableInstance;
	
    private PAInvestigationReviewRemarksTable invsReviewRemarksTableObj;
	
	public void init(Long rodKey) {

		Reimbursement reimbursement = null;
		if (null != rodKey) {
			reimbursement = documentService.getReimbursement(rodKey);
		}
		this.reimbursement = reimbursement;
		
		List<Speciality> specialityList = new ArrayList<Speciality>();

		if (null != reimbursement) {
			specialityList = documentService
					.getSpecialityListByClaim(reimbursement.getClaim().getKey());
		}

		List<ProcedureDTO> procedureList = new ArrayList<ProcedureDTO>();
		List<DiagnosisDetailsTableDTO> diagnosisList = new ArrayList<DiagnosisDetailsTableDTO>();

		if (rodKey != null) {
			// ZonalMedicalReviewMapper reimbursementMapper = new
			// ZonalMedicalReviewMapper();
			// PreauthDTO reimbursementDTO = reimbursementMapper
			// .getReimbursementDTO(reimbursement);
			procedureList = documentService.getProcedureDto(rodKey);
			diagnosisList = documentService.getDiagnosisList(rodKey);
			// List<PedValidation> findPedValidationByPreauthKey =
			// preauthService
			// .findPedValidationByPreauthKey(reimbursement.getKey());
			// diagnosisList = reimbursementMapper
			// .getNewPedValidationTableListDto(findPedValidationByPreauthKey);
			this.diagnosisList = diagnosisList;
		}

		// Preauth preauth =
		// getPreviousPreauth(reimbursement.getClaim().getKey());
		//
		// if(preauth != null){
		// procedureList = documentService.getProcedureDto(preauth.getKey());
		// diagnosisList = documentService.getDiagnosisList(preauth.getKey());
		// }

		BeanItemContainer<SelectValue> selectValueContainer = masterService
				.getSelectValueContainer(ReferenceTable.MEDICAL_VERIFICATION);

		BeanItemContainer<SelectValue> treatmentContainer = masterService
				.getSelectValueContainer(ReferenceTable.TREATMENT_QUALITY_VERIFICATION);

		List<MedicalVerificationDTO> medicalVerification = new ArrayList<MedicalVerificationDTO>();
		List<MedicalVerificationDTO> treatmentVerification = new ArrayList<MedicalVerificationDTO>();

		if (rodKey != null) {
			medicalVerification = documentService.getMedicalVerification(
					selectValueContainer, rodKey);
			this.medicalVerification = medicalVerification;

			treatmentVerification = documentService.getMedicalVerification(
					treatmentContainer, rodKey);
			this.treatmentVerification = treatmentVerification;
		}

		this.procedureList = procedureList;

		List<SpecialityDTO> speciality = new ArrayList<SpecialityDTO>();

		for (Speciality list : specialityList) {
			SpecialityDTO dto = new SpecialityDTO();
			if (null != list.getSpecialityType()) {
				SelectValue selected = new SelectValue();
				selected.setId(list.getSpecialityType().getKey());
				selected.setValue(list.getSpecialityType().getValue());
				dto.setSpecialityType(selected);
			}
			dto.setRemarks(list.getRemarks());
			speciality.add(dto);
		}

		this.specialityList = speciality;

		// PreauthDTO preauthDto = mapper.getReimbursementDTO(reimbursement);

		PreauthDTO preauthDto = getPreauthDto(rodKey);

		this.bean = preauthDto;

		List<PAcoverTableViewDTO> optionalCoversList = createRODService.getOptionalCoversValueBasedOnRODForView(rodKey);
		
		List<PAcoverTableViewDTO> addOnCoversList = createRODService.getAdditionalCoversValueBasedOnRODForView(rodKey);
		
		if (this.bean.getPreauthDataExtractionDetails().getAdmissionDate() != null) {
			Date tempDate = this.bean.getPreauthDataExtractionDetails()
					.getAdmissionDate();
			this.bean.getPreauthDataExtractionDetails().setAdmissionDateStr(
					SHAUtils.formatDate(tempDate));
		}

		if (this.bean.getPreauthDataExtractionDetails().getDischargeDate() != null) {
			Date tempDate = this.bean.getPreauthDataExtractionDetails()
					.getDischargeDate();
			this.bean.getPreauthDataExtractionDetails().setDischargeDateStr(
					SHAUtils.formatDate(tempDate));
		}

		if (null == this.bean) {
			this.bean = new PreauthDTO();
		}

		this.binder = new BeanFieldGroup<PreauthDataExtaractionDTO>(
				PreauthDataExtaractionDTO.class);
		this.binder.setItemDataSource(this.bean
				.getPreauthDataExtractionDetails());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());

		txtApprovedAmount = (TextField) binder.buildAndBind("Approved Amt",
				"totalApprAmt", TextField.class);

		if (null != reimbursement) {
			if (null != reimbursement.getApprovedAmount()) {
				txtApprovedAmount.setValue(reimbursement.getApprovedAmount()
						.toString());
			}
		}
		txtStatus = (TextField) binder.buildAndBind("Status", "statusValue",
				TextField.class);
		if (null != reimbursement) {
			if (null != reimbursement.getStatus()) {
				txtStatus.setValue(reimbursement.getStatus().getProcessValue());
			}
		}
		// txtMedicalRemarks = (TextField)
		// binder.buildAndBind("Medical Remarks", "medicalRemarks",
		// TextField.class);
		txtMedicalRemarks = new TextArea("Medical Remarks");
		txtMedicalRemarks.setValue(reimbursement.getMedicalRemarks());
		// txtRemarks = (TextField) binder.buildAndBind("Remarks",
		// "totalApprAmt", TextField.class);
		txtRemarks = new TextArea("Remarks");
		txtRemarks.setWidth("70px");
		// txtDoctorNote = (TextField)
		// binder.buildAndBind("Doctor Note (Internal remarks)", "totalApprAmt",
		// TextField.class);
		txtDoctorNote = new TextField("Doctor Note (Internal remarks)");
		txtDoctorNote.setValue(reimbursement.getDoctorNote());
		txtDateOfAdmission = (TextField) binder.buildAndBind(
				"Date of Admission ", "admissionDateStr", TextField.class);
		txtDateOfAdmission.setValidationVisible(false);
		ReasonForChangeAdmission = (TextField) binder.buildAndBind(
				"Reason For Change in DOA", "changeOfDOA", TextField.class);
		txtRoomCategory = new TextField("Room Category");
		if (null != reimbursement) {
			if (null != reimbursement.getRoomCategory()) {
				txtRoomCategory.setValue(reimbursement.getRoomCategory()
						.getValue());
			}
			ventilatorSupportRadio =new OptionGroup("");
			
			if(reimbursement.getRoomCategory() != null 
					&& reimbursement.getRoomCategory().getValue() != null 
					&& reimbursement.getRoomCategory().getValue().equalsIgnoreCase("ICU/NICU/ICCU/CCU/PICU")){
				
				System.out.println(String.format("Ventilator Support value in View Details in PA medical Summary: [%s]",reimbursement.getVentilatorSupport()));
				ventilatorSupportRadio =new OptionGroup("Ventilator Support");
				ventilatorSupportRadio.addItems(getReadioButtonOptions());
				ventilatorSupportRadio.setItemCaption(true, "Yes");
				ventilatorSupportRadio.setItemCaption(false, "No");
				ventilatorSupportRadio.setStyleName("horizontal");
				//ventilatorSupportRadio.setValue(this.bean.getPreauthDataExtractionDetails().getVentilatorSupport()? true : false);
				if(reimbursement.getVentilatorSupport()!=null){
				ventilatorSupportRadio.setValue(reimbursement.getVentilatorSupport().equalsIgnoreCase("Y")? true : false);
				}
				ventilatorSupportRadio.setReadOnly(true);
				ventilatorSupportRadio.setEnabled(false);
			}

			// txtMedicalRemarks.setValue(reimbursement.getTreatmentRemarks());
			txtRemarks.setValue(reimbursement.getApprovalRemarks());
			ReasonForChangeAdmission.setValue(reimbursement
					.getDoaChangeReason());
		}

		updateRemarksBasedOnStatus(reimbursement);

		txtRoomCategory.setValidationVisible(false);
		txtSpecify = new TextField("Specify");

		txtDateOfDischarge = (TextField) binder.buildAndBind(
				"Date of Discharge", "dischargeDateStr", TextField.class);
		txtDateOfDischarge.setValidationVisible(false);

		txtSystemMedicine = (TextField) binder.buildAndBind(
				"System of Medicine", "systemOfMedicine", TextField.class);
		// txtHospitalisationDueTo = (TextField)
		// binder.buildAndBind("Hospitalisation Due to", "totalApprAmt",
		// TextField.class);
		txtHospitalisationDueTo = new TextField("Hospitalisation Due to");

		txtDateOfInjury = (TextField) binder.buildAndBind("Date of Injury",
				"injuryDate", TextField.class);
		txtDateOfInjury.setValidationVisible(false);

		txtRelapseOfIllness = (TextField) binder.buildAndBind(
				"Relapse of Illness", "relapseIllness", TextField.class);
		txtReasonForAdmission = (TextField) binder.buildAndBind(
				"Reason For Admission", "reasonForAdmission", TextField.class);
		txtReasonForAdmission.setValue(reimbursement.getClaim().getIntimation()
				.getAdmissionReason());
		txtNoOfDays = (TextField) binder.buildAndBind("Length of Stay", "noOfDays",
				TextField.class);

		// txtNatureOfTreatment = (TextField)
		// binder.buildAndBind("Nature of Treatment", "totalApprAmt",
		// TextField.class);
		txtNatureOfTreatment = new TextField("Nature of Treatment");

		txtFirstConsultaionDate = (TextField) binder
				.buildAndBind("1st Consultation Date", "firstConsultantDate",
						TextField.class);
		txtFirstConsultaionDate.setValidationVisible(false);

		chkCorpBuffer = (CheckBox) binder.buildAndBind("Corp Buffer",
				"corpBuffer", CheckBox.class);
		chkCorpBuffer.setEnabled(false);

		txtAutoRestoration = (TextField) binder.buildAndBind(
				"Automatic Restoration", "autoRestoration", TextField.class);
		// txtIllness = (TextField) binder.buildAndBind("Illness",
		// "totalApprAmt", TextField.class);

		txtIllness = new TextField("Illness");
		txtPatientStatus = new TextField("Patient Status");

		if (null != reimbursement) {
			txtSystemMedicine.setValue(reimbursement.getSystemOfMedicine());
			txtRelapseOfIllness.setValue(reimbursement.getRelapseRemarks());
			if (null != reimbursement.getNumberOfDays()) {
				txtNoOfDays
						.setValue(reimbursement.getNumberOfDays().toString());
			}
			if (null != reimbursement.getInjuryDate()) {
				txtDateOfInjury.setValue(SHAUtils.formatDate(reimbursement
						.getInjuryDate()));
			}
			if (null != reimbursement.getHopsitaliztionDueto()) {
				txtHospitalisationDueTo.setValue(reimbursement
						.getHopsitaliztionDueto().getValue());
			}
			if (null != reimbursement.getNatureOfTreatment()) {
				txtNatureOfTreatment.setValue(reimbursement
						.getNatureOfTreatment().getValue());
			}
			if (null != reimbursement.getConsultationDate()) {
				txtFirstConsultaionDate.setValue(SHAUtils
						.formatDate(reimbursement.getConsultationDate()));
			}
			if (null != reimbursement.getCorporateBufferFlag()) {
				if (1 == reimbursement.getCorporateBufferFlag()) {
					chkCorpBuffer.setValue(true);
				}
			}
			if (null != reimbursement.getAutomaticRestoration()) {
				if (reimbursement.getAutomaticRestoration().equals("Y")) {
					txtAutoRestoration.setValue("Yes");
				} else {
					txtAutoRestoration.setValue("No");
				}
			}
			if (reimbursement.getIllness() != null) {
				txtIllness.setValue(reimbursement.getIllness().getValue());
			}
			if (reimbursement.getPatientStatus() != null) {
				txtPatientStatus.setValue(reimbursement.getPatientStatus()
						.getValue());
				if (reimbursement.getPatientStatus().getKey()
						.equals(ReferenceTable.PATIENT_STATUS_ADMITTED)) {
				}
			}

		}

		// txtPatientStatus = (TextField) binder.buildAndBind("Patient Status",
		// "totalApprAmt", TextField.class);

		// txtCauseOfInjury = (TextField) binder.buildAndBind("Cause of Injury",
		// "totalApprAmt", TextField.class);

		txtCauseOfInjury = new TextField("Cause of Injury");
		if (this.bean.getPreauthDataExtractionDetails().getCauseOfInjury() != null) {
			txtCauseOfInjury.setValue(this.bean
					.getPreauthDataExtractionDetails().getCauseOfInjury()
					.getValue());
		}

		medicoLegalCase = (OptionGroup) binder.buildAndBind(
				"Medico Legal Case", "medicalLegalCase", OptionGroup.class);

		reportedToPolice = (OptionGroup) binder.buildAndBind(
				"Reported to Police", "reportedToPolice", OptionGroup.class);

		medicoLegalCase.addItems(getReadioButtonOptions());
		medicoLegalCase.setItemCaption(true, "Yes");
		medicoLegalCase.setItemCaption(false, "No");
		medicoLegalCase.setStyleName("horizontal");
		medicoLegalCase.select(false);
		medicoLegalCase.setValue(this.bean.getPreauthDataExtractionDetails()
				.getMedicalLegalCase());
		medicoLegalCase.setReadOnly(true);

		reportedToPolice.addItems(getReadioButtonOptions());
		reportedToPolice.setItemCaption(true, "Yes");
		reportedToPolice.setItemCaption(false, "No");
		reportedToPolice.setStyleName("horizontal");
		reportedToPolice.select(false);
		reportedToPolice.setValue(this.bean.getPreauthDataExtractionDetails()
				.getReportedToPolice());
		reportedToPolice.setReadOnly(true);
		txtTreatementType = new TextField("Treatment Type");
		if (null != reimbursement) {
			if (null != reimbursement.getTreatmentType()) {
				txtTreatementType.setValue(reimbursement.getTreatmentType()
						.getValue());
			}
		}
		
		benefit = new TextField("Benefits");
		
		if(null != reimbursement)
		{
			if(null != reimbursement.getBenefitsId())
			{
				benefit.setValue(reimbursement.getBenefitsId().getValue());
			}
		}
		
		acctDeath = new OptionGroup("Accident / Death");
		acctDeath.addItem(SHAConstants.ACCIDENT);
		acctDeath.addItem(SHAConstants.DEATH);
		acctDeath.addStyleName("horizontal");
		acctDeath.setEnabled(false);
		
		dateAcctDeath = new TextField("Date of Accident / Death");
		
		
				if(bean.getClaimDTO().getIncidenceDate() != null)
		    	{
		    		dateAcctDeath.setValue(SHAUtils.formatDate(bean.getClaimDTO().getIncidenceDate()));
		    	}
		    	
		    	
		    	if(bean.getClaimDTO().getIncidenceFlag())
		    	{
		    		acctDeath.setValue(SHAConstants.ACCIDENT);
		    	}
		    	else if(!bean.getClaimDTO().getIncidenceFlag())
		    	{
		    		acctDeath.setValue(SHAConstants.DEATH);
		    	}
		    	/*List<PABenefitsDTO> paBenefitsValueList = new ArrayList<PABenefitsDTO>();
		    	if(this.bean.getPreauthDataExtractionDetails().getPaBenefits() != null)
				  {
		    		paBenefitsTable.initReferenceId(this.bean.getPreauthDataExtractionDetails().getPaBenefits().getId());
		    		paBenefitsValueList = dbCalculationService.getBenefitCoverValues(this.bean.getNewIntimationDTO().getInsuredPatient().getKey(), this.bean.getPreauthDataExtractionDetails().getPaBenefits().getId());
		    		
				  }
		    	
		    	paBenefitsTable.init();
		    	paBenefitsTable.removeAllItems();
		    	paBenefitsTable.setTableData(paBenefitsValueList);*/
				
		    	
		    	 // paBenefitsTable = paBenefitsTableObj.get();
	
		List<PABenefitsDTO> paBenefitsValueList = new ArrayList<PABenefitsDTO>();
				  
		if (this.bean.getPreauthDataExtractionDetails().getPaBenefits() != null) {
			paBenefitsTable.initReferenceId(this.bean
					.getPreauthDataExtractionDetails().getPaBenefits().getId());
			paBenefitsValueList = dbCalculationService
					.getBenefitCoverValues(this.bean.getNewIntimationDTO()
							.getInsuredPatient().getKey(), this.bean
							.getPreauthDataExtractionDetails().getPaBenefits()
							.getId());
			
		}
		
		paBenefitsTable.init("",false,false);
		
		if (paBenefitsValueList != null) {
			paBenefitsTable.setTableList(paBenefitsValueList);
		}
				  
				  
		//viewDiagnosisTableObj = viewDiagnosisTableInstance.get();
		viewDiagnosisTableObj.init("", false, false);
		viewDiagnosisTableObj.setVisibleColumn();
		viewDiagnosisTableObj.setTableList(diagnosisList);
		viewDiagnosisTableObj.setCaption("Diagnosis Details");
		/*
		 * int serialNumber = 1; for (DiagnosisDetailsTableDTO
		 * diagnosisDetailsTableDTO : diagnosisList) {
		 * diagnosisDetailsTableDTO.setSerialNumber(serialNumber); serialNumber
		 * ++; viewDiagnosisTableObj.addBeanToList(diagnosisDetailsTableDTO); }
		 */
		FormLayout benefitsForm = new FormLayout(benefit);
		benefitsForm.setSpacing(false);
		benefitsForm.setMargin(false);
		setReadOnly(benefitsForm, true);
		VerticalLayout tableBenefits = new VerticalLayout();
		tableBenefits.addComponent(benefitsForm);
		tableBenefits.addComponent(paBenefitsTable);
		tableBenefits.setCaption("Part I-Table Benefits");
		tableBenefits.setSpacing(false);

		FormLayout firstForm = new FormLayout(txtStatus, txtApprovedAmount,
				txtMedicalRemarks, acctDeath, txtDateOfAdmission,
				ReasonForChangeAdmission, txtRoomCategory, ventilatorSupportRadio,txtSpecify,
				txtDateOfDischarge, txtSystemMedicine, txtHospitalisationDueTo,
				txtDateOfInjury, medicoLegalCase, reportedToPolice,
				txtTreatementType);
		firstForm.setSpacing(true);

		setReadOnly(firstForm, true);

		FormLayout secondForm = new FormLayout(txtRemarks, txtDoctorNote,
				txtRelapseOfIllness, dateAcctDeath, txtReasonForAdmission, txtNoOfDays,
				txtNatureOfTreatment, txtFirstConsultaionDate, chkCorpBuffer,
				txtAutoRestoration, txtIllness, txtPatientStatus,
				txtCauseOfInjury);
		secondForm.setSpacing(true);
		setReadOnly(secondForm, true);

		HorizontalLayout firstHor = new HorizontalLayout(firstForm, secondForm);
		firstHor.setSpacing(true);

		//this.viewProcedureListTableObj = viewProcedureListTableInstance.get();
		this.viewProcedureListTableObj.init("", false, false);
		this.viewProcedureListTableObj.setCaption("Procedure List");

		this.viewProcedureListTableObj.setTableList(procedureList);
		/*
		 * for (ProcedureDTO procedureDTO : procedureList) {
		 * viewProcedureListTableObj.addBeanToList(procedureDTO); }
		 */

		//this.viewSpecialityTableObj = viewSpecialityTableInstance.get();
		this.viewSpecialityTableObj.init("", false, false);
		this.viewSpecialityTableObj.setCaption("Speciality");

		for (SpecialityDTO specialityDTO : speciality) {
			viewSpecialityTableObj.addBeanToList(specialityDTO);
		}

		/*this.medicalVerificationTableObj = medicalVerificationTableInstance
				.get();
		*/this.medicalVerificationTableObj.init("", false,
				false);
		
		this.medicalVerificationTableObj.setCaption("Medical Verification");
		this.medicalVerificationTableObj.setTableList(medicalVerification);
		/*
		 * for (MedicalVerificationDTO medicalVerificationDTO :
		 * medicalVerification) {
		 * this.medicalVerificationTableObj.addBeanToList(
		 * medicalVerificationDTO); }
		 */

		/*if (null != reimbursement) {
			Investigation investigation = investigationService
					.getInvestigationByClaimKey(reimbursement.getClaim()
							.getKey());
			if (reimbursement.getInvestigatorCode() != null) {
				TmpInvestigation tmpInvestigationByInvestigatorCode = investigationService
						.getTmpInvestigationByInactiveInvestigatorCode(reimbursement
								.getInvestigatorCode());
				if (tmpInvestigationByInvestigatorCode != null) {
					this.bean
							.setInvestigatorName(tmpInvestigationByInvestigatorCode
									.getInvestigatorName());
				}

			}

			this.bean.setInvestigationReviewRemarks(reimbursement
					.getInvestigatorRemarks());

			if (reimbursement.getInvestigationReportReview() != null
					&& reimbursement.getInvestigationReportReview()
							.equalsIgnoreCase("Y")) {
				this.bean.setReportReviewed(true);
			}

		}

		chkInvestigationReviewed = new CheckBox("Investigation Report Reviewed");
		chkInvestigationReviewed.setValue(this.bean.getReportReviewed());
		chkInvestigationReviewed.setEnabled(false);
		txtInvestigatorName = new TextField("Investigator Name");
		txtInvestigatorName.setValue(this.bean.getInvestigatorName());
		txtInvestigationReviewRemarks = new TextField(
				"Investigation Review Remarks");
		txtInvestigationReviewRemarks.setValue(this.bean
				.getInvestigationReviewRemarks());

		FormLayout form1 = new FormLayout(chkInvestigationReviewed,
				txtInvestigatorName, txtInvestigationReviewRemarks);
		form1.setSpacing(true);

		setReadOnly(form1, true);*/

		List<AssignedInvestigatiorDetails> invsReviewRemarksList = reimbursementService.getInvsReviewRemarksDetailsByClaimKey(this.bean.getClaimKey());
	    this.bean.setPreauthMedicalDecisionDetails(new PreauthMedicalDecisionDTO());
		bean.getPreauthMedicalDecisionDetails().setInvsReviewRemarksTableList(invsReviewRemarksList);
		
		invsReviewRemarksTableObj = invsReviewRemarksTableInstance.get();
		invsReviewRemarksTableObj.init();
		//fireViewEvent(ClaimRequestMedicalDecisionPagePresenter.INVS_REVIEW_REMARKS_LIST, this.bean);
		invsReviewRemarksTableObj.setTableList(bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList());
	//	HorizontalLayout specialistHLayout = new HorizontalLayout(investigationFLayout);
		
		HorizontalLayout specialistHLayout = new HorizontalLayout(invsReviewRemarksTableObj);
		specialistHLayout.setWidth("100%");
		specialistHLayout.setSpacing(true);
		
		HorizontalLayout investigationHor = new HorizontalLayout(
				medicalVerificationTableObj/*, form1*/);
		investigationHor.setSpacing(true);

		if (null == this.treatementVerificationObj) {
			/*this.treatementVerificationObj = treatementVerificationInstance
					.get();*/
			this.treatementVerificationObj.init(
					"", false, false);
			this.treatementVerificationObj.setCaption("Treatment Quality Verification");
		}
		this.treatementVerificationObj.setTableList(treatmentVerification);
		
		/*
		 * for (MedicalVerificationDTO medicalVerificationDTO :
		 * treatmentVerification) {
		 * this.treatementVerificationObj.addBeanToList(medicalVerificationDTO);
		 * }
		 */

		viewMoreDetailsBtn = new Button("View Medical Summary (Detailed)");
		btnHospitalScroing = new Button("Hospital Scoring");
		/*if(reimbursement.getClaim().getIntimation().getHospitalType().getKey() == ReferenceTable.NETWORK_HOSPITAL_TYPE_ID){
			btnHospitalScroing.setVisible(true);
		}else{
			btnHospitalScroing.setVisible(false);
		}*/
		
		/*this.optionalCover = optionCoverTableInstance
				.get();
		*/this.optionalCover.init("", false,
				false);
		
		this.optionalCover.setCaption("Part II-Optional Covers");
		
		if(optionalCoversList != null)
		{
			int i=1;
			for(PAcoverTableViewDTO paCoverTableViewDTO :optionalCoversList)
			{
				paCoverTableViewDTO.setsNo(i);
				this.optionalCover.addBeanToList(paCoverTableViewDTO);
				i++;
			}
		}
		
		/*this.addOnCover = addOnCoverTableInstance
				.get();
		*/this.addOnCover.init("", false,
				false);
		
		this.addOnCover.setCaption("Part III-Add on Covers");
		
		if(addOnCoversList != null)
		{
			int i=1;
			for(PAcoverTableViewDTO paCoverTableViewDTO :addOnCoversList)
			{
				paCoverTableViewDTO.setsNo(i);
				this.addOnCover.addBeanToList(paCoverTableViewDTO);
				i++;
			}
		}
		
		/*Treating Doctor Details View CR2019211
		//List<TreatingDoctorDetails> findDoctorDetailsByClaimKey = preauthService.findTreatingDoctorDetailsByClaimKey(reimbursement.getClaim().getKey());
		//Testing 
		List<TreatingDoctorDetails> findDoctorDetailsByClaimKey = preauthService.findTreatingDoctorDetails();
		ZonalMedicalReviewMapper mapper = ZonalMedicalReviewMapper.getInstance();
		if(findDoctorDetailsByClaimKey !=null && !findDoctorDetailsByClaimKey.isEmpty()){
			List<TreatingDoctorDTO> treatingDoctorDTOList = mapper.gettreatingDoctorDTOList(findDoctorDetailsByClaimKey);
			this.treatingDoctorList = treatingDoctorDTOList; 
		}
		this.treatingDoctorTableObj.init("Treating Doctor Deatils", false,false);	
		if(this.treatingDoctorTableObj != null) {
			int i= 1;
			for (TreatingDoctorDTO treatingDoctorDTO : treatingDoctorList) {		
				treatingDoctorDTO.setSerialNo(i++);
				this.treatingDoctorTableObj.addBeanToList(treatingDoctorDTO);
			}
		}
		this.treatingDoctorTableObj.setEnabled(false);	*/
		HorizontalLayout dummyHor = new HorizontalLayout();
		dummyHor.setHeight("100px");
		
		HorizontalLayout dummyver = new HorizontalLayout(treatementVerificationObj);
		HorizontalLayout buttonHor = new HorizontalLayout(btnHospitalScroing, viewMoreDetailsBtn);
		buttonHor.setSpacing(true);
		

		VerticalLayout verticalMain = new VerticalLayout(buttonHor,
				firstHor, tableBenefits, this.viewDiagnosisTableObj,
				this.viewProcedureListTableObj,/*this.treatingDoctorTableObj,*/ this.viewSpecialityTableObj,optionalCover,addOnCover,
				investigationHor,specialistHLayout, dummyHor, dummyver);
		verticalMain.setComponentAlignment(buttonHor, Alignment.TOP_RIGHT);
		verticalMain.setSpacing(true);
		verticalMain.setMargin(true);

		Panel mainPanel = new Panel(verticalMain);
		mainPanel.setStyleName("girdBorder");
		mainPanel.setCaption("Medical Summary");

		addListener();

		setCompositionRoot(mainPanel);
	}

	public void addListener() {

		viewMoreDetailsBtn.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				popup = new com.vaadin.ui.Window();
				popup.setCaption("");
				popup.setWidth("75%");
				popup.setHeight("95%");
				if (txtRemarks != null && null != txtRemarks.getValue()) {
					bean.setRemarks(txtRemarks.getValue());
				}
				//moreDetailsPageObj = moreDetailsPageInstance.get();
				moreDetailsPageObj.init(bean, reimbursement, procedureList,
						specialityList, medicalVerification,
						treatmentVerification, diagnosisList);
				popup.setContent(moreDetailsPageObj);
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
		
		btnHospitalScroing.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				showScoringView();
			}
		});
	}

	private Preauth getPreviousPreauth(Long claimKey) {
		List<Preauth> preauthByClaimKey = preauthService
				.getPreauthByClaimKey(claimKey);
		Preauth previousPreauth = null;
		if (!preauthByClaimKey.isEmpty()) {
			previousPreauth = preauthByClaimKey.get(0);
			String[] split = previousPreauth.getPreauthId().split("/");
			String defaultNumber = split[split.length - 1];
			Integer nextReferenceNo = Integer.valueOf(defaultNumber);
			for (Preauth preauth : preauthByClaimKey) {
				if (preauth.getPreauthId() != null) {
					String[] splitNumber = preauth.getPreauthId().split("/");
					String number = splitNumber[splitNumber.length - 1];

					if (Integer.valueOf(number) > Integer
							.valueOf(defaultNumber)) {
						previousPreauth = preauth;
						nextReferenceNo = Integer.valueOf(number);
					}
				}

			}
		}

		return previousPreauth;
	}

	public List<ViewTmpClaim> getPreviousClaimForPreviousPolicy(String policyNumber, List<ViewTmpClaim> generatedList) {
		try {
			Policy renewalPolNo = policyService.getByPolicyNumber(policyNumber);
			if(renewalPolNo != null) {
				List<ViewTmpClaim> previousPolicyPreviousClaims = claimService.getViewTmpClaimsByPolicyNumber(renewalPolNo.getPolicyNumber());
				if(previousPolicyPreviousClaims != null && !previousPolicyPreviousClaims.isEmpty()) {
					for (ViewTmpClaim viewTmpClaim : previousPolicyPreviousClaims) {
						if(!generatedList.contains(viewTmpClaim)) {
							generatedList.add(viewTmpClaim);
						}
					}
				}
				if(renewalPolNo != null && renewalPolNo.getRenewalPolicyNumber() != null ) {
					getPreviousClaimForPreviousPolicy(renewalPolNo.getRenewalPolicyNumber(), generatedList);
				} else {
					return generatedList;
				}
			}
		} catch(Exception e) {
			
		}
		return generatedList;
	}
	
	public PreauthDTO getPreauthDto(Long rodkey) {

		Reimbursement reimbursementObjectByKey = rodService
				.getReimbursementObjectByKey(rodkey);

		
		ZonalMedicalReviewMapper mapper = ZonalMedicalReviewMapper.getInstance();
//		ZonalMedicalReviewMapper.getAllMapValues();

		PreauthDTO reimbursementDTO = mapper
				.getReimbursementDTO(reimbursementObjectByKey);

		Claim claimByKey = reimbursementObjectByKey.getClaim();

		Reimbursement previousLatestROD = getPreviousLatestROD(
				claimByKey.getKey(), reimbursementObjectByKey);
		NewIntimationDto newIntimationDto = new NewIntimationDto();

		if (claimByKey != null) {
			setClaimValuesToDTO(reimbursementDTO, claimByKey);
			newIntimationDto = intimationService.getIntimationDto(claimByKey
					.getIntimation());
			// newIntimationDto.getPolicy().getProduct().getAutoRestoration()
			ClaimDto claimDTO = ClaimMapper.getInstance().getClaimDto(claimByKey);
			reimbursementDTO.setNewIntimationDTO(newIntimationDto);
			reimbursementDTO.setClaimDTO(claimDTO);
		}
		/*String policyNumber = reimbursementDTO.getPolicyDto().getPolicyNumber();
		List<ViewTmpClaim> previousclaimsList = new ArrayList<ViewTmpClaim>();
		List<ViewTmpClaim> claimsByPolicyNumber = claimService
				.getViewTmpClaimsByPolicyNumber(policyNumber);
		Policy byPolicyNumber = policyService.getByPolicyNumber(policyNumber);
		previousclaimsList.addAll(claimsByPolicyNumber);
		
		previousclaimsList = getPreviousClaimForPreviousPolicy(byPolicyNumber.getRenewalPolicyNumber(), previousclaimsList);*/

		// List<PreviousClaimsTableDTO> previousClaimDTOList = SHAUtils
		// .getPreviousClaims(claimsByPolicyNumber,
		// claimByKey.getClaimId(), pedValidationService,
		// masterService);
		//

		/*List<PreviousClaimsTableDTO> previousClaimDTOList = preauthService
				.getPreviousClaims(previousclaimsList,
						claimByKey.getClaimId());*/
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		List<PreviousClaimsTableDTO> previousClaimDTOList = dbCalculationService.getPreviousClaims(claimByKey.getKey(), claimByKey.getIntimation().getPolicy().getKey(), 
				claimByKey.getIntimation().getInsured().getKey(), SHAConstants.POLICY_WISE_SEARCH_TYPE);

		// List<PreviousClaimsTableDTO> previousClaimDTOList = new
		// PreviousClaimMapper().getPreviousClaimDTOList(claimsByPolicyNumber);

		// List<PreviousClaimsTableDTO> previousClaimDTOList = new
		// PreviousClaimMapper().getPreviousClaimDTOList(claimsByPolicyNumber);

		reimbursementDTO.setPreviousClaimsList(previousClaimDTOList);

		//DBCalculationService dbCalculationService = new DBCalculationService();
		Double insuredSumInsured = 0d;
		
		if((ReferenceTable.getGPAProducts().containsKey(reimbursementObjectByKey.getClaim().getIntimation().getPolicy().getProduct().getKey()))){
            insuredSumInsured = dbCalculationService.getGPAInsuredSumInsured(
            		reimbursementObjectByKey.getClaim().getIntimation().getInsured()
                                            .getInsuredId().toString(), reimbursementObjectByKey.getClaim().getIntimation().getPolicy()
                                            .getKey());
		}else{
            insuredSumInsured = dbCalculationService.getInsuredSumInsured(
            		reimbursementObjectByKey.getClaim().getIntimation().getInsured()
                    .getInsuredId().toString(), reimbursementObjectByKey.getClaim().getIntimation().getPolicy()
                    .getKey(),reimbursementObjectByKey.getClaim().getIntimation().getInsured().getLopFlag());
		}
		Integer sumInsured = preauthService.getSumInsured(reimbursementDTO
				.getPolicyDto().getProduct().getKey(),
				(insuredSumInsured == 0) ? reimbursementDTO.getPolicyDto()
						.getTotalSumInsured() : insuredSumInsured);
		reimbursementDTO.getPolicyDto().setInsuredSumInsured(insuredSumInsured);
		String strAutoRestorationFlg = newIntimationDto.getPolicy()
				.getProduct().getAutoRestorationFlag();
		if (("Y").equalsIgnoreCase(strAutoRestorationFlg)) {
			if (sumInsured != null && sumInsured.intValue() > 0) {
				reimbursementDTO.getPreauthDataExtractionDetails()
						.setAutoRestoration(
								SHAConstants.AUTO_RESTORATION_NOTDONE);
			} else if (null != sumInsured && 0 == sumInsured.intValue()) {
				reimbursementDTO.getPreauthDataExtractionDetails()
						.setAutoRestoration(SHAConstants.AUTO_RESTORATION_DONE);
			}
		} else {
			reimbursementDTO.getPreauthDataExtractionDetails()
					.setAutoRestoration(
							SHAConstants.AUTO_RESTORATION_NOTAPPLICABLE);
		}

		if (previousLatestROD != null) {
			setReimbursmentTOPreauthDTO(mapper, claimByKey,
					reimbursementObjectByKey, reimbursementDTO, true);
			Hospitals hospitalById = hospitalService.getHospitalById(claimByKey
					.getIntimation().getHospital());
			ZonalReviewUpdateHospitalDetailsDTO updateHospitalDetails = reimbursementDTO
					.getPreauthDataExtractionDetails()
					.getUpdateHospitalDetails();
			updateHospitalDetails.setHospitalCity(hospitalById.getCity());
			updateHospitalDetails.setHospitalCity(hospitalById.getCity());
			updateHospitalDetails.setHospitalState(hospitalById.getState());
			updateHospitalDetails.setHospitalCode(hospitalById
					.getHospitalCode());

			reimbursementDTO
					.setReconsiderationList(getReconsiderRODRequest(claimByKey));
		} else if (claimByKey.getClaimType() != null
				&& claimByKey.getClaimType().getKey()
						.equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
			PreMedicalMapper premedicalMapper=PreMedicalMapper.getInstance();
//			PreMedicalMapper.getAllMapValues();
			List<Preauth> preauthByClaimKey = preauthService
					.getPreauthByClaimKey(claimByKey.getKey());
			Preauth previousPreauth = null;
			if (!preauthByClaimKey.isEmpty()) {
				previousPreauth = preauthByClaimKey.get(0);
				String[] split = previousPreauth.getPreauthId().split("/");
				String defaultNumber = split[split.length - 1];
				Integer nextReferenceNo = Integer.valueOf(defaultNumber);
				for (Preauth preauth : preauthByClaimKey) {
					if (preauth.getPreauthId() != null) {
						String[] splitNumber = preauth.getPreauthId()
								.split("/");
						String number = splitNumber[splitNumber.length - 1];

						if (Integer.valueOf(number) > Integer
								.valueOf(defaultNumber)) {
							previousPreauth = preauth;
							nextReferenceNo = Integer.valueOf(number);
						}
					}

				}
			}

			Long lobId = 0l;
			if(claimByKey.getLobId() != null)
			{
				lobId = claimByKey.getLobId();
			}
			
			if (previousPreauth != null && !(ReferenceTable.PA_LOB_KEY).equals(lobId)) {
				reimbursementDTO = premedicalMapper
						.getPreauthDTO(previousPreauth);
				// setpreauthTOPreauthDTO(premedicalMapper, claimByKey,
				// previousPreauth, reimbursementDTO, true);
				reimbursementDTO.setIsCashlessType(true);
				setReimbursementValues(reimbursementObjectByKey,
						reimbursementDTO);

			}

			Hospitals hospitalById = hospitalService.getHospitalById(claimByKey
					.getIntimation().getHospital());
			setHospitalDetailsToDTO(hospitalById, reimbursementDTO);

		} else {
			Hospitals hospitalById = hospitalService.getHospitalById(claimByKey
					.getIntimation().getHospital());
			setHospitalDetailsToDTO(hospitalById, reimbursementDTO);
		}

		DocAcknowledgement docAcknowLedgement = reimbursementObjectByKey
				.getDocAcknowLedgement();
		List<UploadDocumentDTO> rodSummaryDetails = rodService
				.getRODSummaryDetails(reimbursementObjectByKey.getKey());
		List<Long> documentSummaryKeys = new ArrayList<Long>();
		for (UploadDocumentDTO uploadDocumentDTO : rodSummaryDetails) {
			documentSummaryKeys.add(uploadDocumentDTO.getDocSummaryKey());
			uploadDocumentDTO.setStatus(true);
			List<RODBillDetails> billEntryDetails = rodService
					.getBillEntryDetails(uploadDocumentDTO.getDocSummaryKey());
			List<BillEntryDetailsDTO> dtoList = new ArrayList<BillEntryDetailsDTO>();
			if (billEntryDetails != null && !billEntryDetails.isEmpty()) {
				for (RODBillDetails billEntryDetailsDO : billEntryDetails) {
					dtoList.add(getBillDetailsDTO(billEntryDetailsDO,
							uploadDocumentDTO));
				}
			}
			uploadDocumentDTO.setBillEntryDetailList(dtoList);
		}
		Double totalBilledAmount = reimbursementService
				.getTotalBilledAmount(documentSummaryKeys);
		reimbursementDTO.setUploadDocumentDTO(rodSummaryDetails);
		reimbursementDTO.setKey(reimbursementObjectByKey.getKey());
		reimbursementDTO.setAmountConsidered(totalBilledAmount != null ? String
				.valueOf(totalBilledAmount.intValue()) : "0");

		reimbursementDTO.setRodNumber(reimbursementObjectByKey.getRodNumber());
		reimbursementDTO.getPreauthDataExtractionDetails()
				.setReasonForAdmission(
						claimByKey.getIntimation().getAdmissionReason());

		if (reimbursementObjectByKey.getDocAcknowLedgement()
				.getHospitalisationFlag() != null
				&& reimbursementObjectByKey.getDocAcknowLedgement()
						.getHospitalisationFlag().toLowerCase()
						.equalsIgnoreCase("y")) {
			reimbursementDTO.setHospitalizaionFlag(true);
		}
		if (reimbursementObjectByKey.getDocAcknowLedgement()
				.getPreHospitalisationFlag() != null
				&& reimbursementObjectByKey.getDocAcknowLedgement()
						.getPreHospitalisationFlag().toLowerCase()
						.equalsIgnoreCase("y")) {
			reimbursementDTO.setHospitalizaionFlag(true);
		}
		if (reimbursementObjectByKey.getDocAcknowLedgement()
				.getPostHospitalisationFlag() != null
				&& reimbursementObjectByKey.getDocAcknowLedgement()
						.getPostHospitalisationFlag().toLowerCase()
						.equalsIgnoreCase("y")) {
			reimbursementDTO.setHospitalizaionFlag(true);
		}

		return reimbursementDTO;

	}

	// public void setpreauthTOPreauthDTO(PreMedicalMapper premedicalMapper,
	// Claim claimByKey, Preauth previousPreauth, PreauthDTO preauthDTO,
	// Boolean isEnabled) {
	// if (claimByKey != null) {
	// setClaimValuesToDTO(preauthDTO, claimByKey);
	// NewIntimationDto newIntimationDto = intimationService
	// .getIntimationDto(claimByKey.getIntimation());
	// ClaimDto claimDTO = new ClaimMapper().getClaimDto(claimByKey);
	// preauthDTO.setNewIntimationDTO(newIntimationDto);
	// preauthDTO.setClaimDTO(claimDTO);
	// }
	// String policyNumber = preauthDTO.getPolicyDto().getPolicyNumber();
	// List<Claim> claimsByPolicyNumber = claimService
	// .getClaimsByPolicyNumber(policyNumber);
	//
	// List<PreviousClaimsTableDTO> previousClaimDTOList = SHAUtils
	// .getPreviousClaims(claimsByPolicyNumber,
	// claimByKey.getClaimId(), pedValidationService,
	// masterService);
	//
	// // List<PreviousClaimsTableDTO> previousClaimDTOList = new
	// // PreviousClaimMapper().getPreviousClaimDTOList(claimsByPolicyNumber);
	//
	// preauthDTO.setPreviousClaimsList(previousClaimDTOList);
	//
	// if (previousPreauth.getCoordinatorFlag().equalsIgnoreCase("y")) {
	//
	// CoordinatorDTO coordinatorDTO = premedicalMapper
	// .getCoordinatorDTO(preauthService
	// .findCoordinatorByClaimKey(previousPreauth
	// .getClaim().getKey()));
	// coordinatorDTO.setRefertoCoordinator(true);
	// preauthDTO.setCoordinatorDetails(coordinatorDTO);
	// }
	//
	// List<SpecialityDTO> specialityDTOList = premedicalMapper
	// .getSpecialityDTOList(preauthService
	// .findSpecialityByClaimKey(previousPreauth.getClaim().getKey()));
	// for (SpecialityDTO specialityDTO : specialityDTOList) {
	// specialityDTO.setEnableOrDisable(isEnabled);
	// }
	// preauthDTO.getPreauthDataExtractionDetails().setSpecialityList(
	// specialityDTOList);
	//
	// List<ProcedureDTO> procedureMainDTOList = premedicalMapper
	// .getProcedureMainDTOList(preauthService
	// .findProcedureByPreauthKey(previousPreauth.getKey()));
	// for (ProcedureDTO procedureDTO : procedureMainDTOList) {
	// procedureDTO.setEnableOrDisable(isEnabled);
	// if (procedureDTO.getSublimitName() != null) {
	// ClaimLimit limit = claimService.getClaimLimitByKey(procedureDTO
	// .getSublimitName().getLimitId());
	// procedureDTO.setSublimitName(getSublimitFunctionObj(limit));
	// procedureDTO.setSublimitDesc(limit.getLimitDescription());
	// procedureDTO.setSublimitAmount(limit.getMaxPerPolicyPeriod()
	// .toString());
	// }
	// }
	//
	// preauthDTO.getPreauthMedicalProcessingDetails()
	// .setProcedureExclusionCheckTableList(procedureMainDTOList);
	//
	// List<PedValidation> findPedValidationByPreauthKey = preauthService
	// .findPedValidationByPreauthKey(previousPreauth.getKey());
	// List<DiagnosisDetailsTableDTO> newPedValidationTableListDto =
	// premedicalMapper
	// .getNewPedValidationTableListDto(findPedValidationByPreauthKey);
	//
	// // Fix for issue 732 starts.
	// DBCalculationService dbCalculationService = new DBCalculationService();
	//
	// Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(
	// preauthDTO.getNewIntimationDTO().getInsuredPatient()
	// .getInsuredId().toString(), preauthDTO.getPolicyDto()
	// .getKey());
	//
	// Double balanceSI = dbCalculationService.getBalanceSI(
	// preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
	// preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),
	// preauthDTO.getClaimKey(), insuredSumInsured).get(
	// SHAConstants.TOTAL_BALANCE_SI);
	// List<Double> copayValue = dbCalculationService.getProductCoPay(
	// preauthDTO.getNewIntimationDTO().getPolicy().getProduct()
	// .getKey(), preauthDTO.getNewIntimationDTO()
	// .getInsuredPatient().getKey());
	// preauthDTO.setBalanceSI(balanceSI);
	// preauthDTO.setProductCopay(copayValue);
	//
	// if (preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()
	// .equals(ReferenceTable.SUPER_SURPLUS_INDIVIDUAL)
	// || preauthDTO.getNewIntimationDTO().getPolicy().getProduct()
	// .getKey().equals(ReferenceTable.SUPER_SURPLUS_FLOATER)) {
	// //
	// preauthDTO.setSpecificProductDeductibleDTO(getPreviousClaimsSuperSurplusTable(claimsByPolicyNumber
	// // ,claimByKey.getClaimId(), preauthDTO));
	// }
	//
	// Map<Long, SublimitFunObject> sublimitFunObjMap = getSublimitFunObjMap(
	// preauthDTO.getPolicyDto().getProduct().getKey(),
	// insuredSumInsured, preauthDTO.getNewIntimationDTO()
	// .getInsuredPatient().getInsuredAge());
	// // Fix for issue 732 ends
	//
	// for (DiagnosisDetailsTableDTO pedValidationTableDTO :
	// newPedValidationTableListDto) {
	// pedValidationTableDTO.setEnableOrDisable(isEnabled);
	// if (pedValidationTableDTO.getDiagnosisName() != null) {
	// String diagnosis = masterService
	// .getDiagnosis(pedValidationTableDTO.getDiagnosisName()
	// .getId());
	// pedValidationTableDTO.setDiagnosis(diagnosis);
	// pedValidationTableDTO.getDiagnosisName().setValue(diagnosis);
	// }
	//
	// if (pedValidationTableDTO.getSublimitName() != null) {
	// // Fix for issue 732 starts.
	// SublimitFunObject objSublimitFun = sublimitFunObjMap
	// .get(pedValidationTableDTO.getSublimitName()
	// .getLimitId());
	// // ClaimLimit limit =
	// //
	// claimService.getClaimLimitByKey(pedValidationTableDTO.getSublimitName().getLimitId());
	// if (objSublimitFun != null) {
	// pedValidationTableDTO.setSublimitName(objSublimitFun);
	// pedValidationTableDTO.setSublimitAmt(String
	// .valueOf(objSublimitFun.getAmount()));
	// }
	// // Fix for issue 732 ends
	// }
	//
	// if (pedValidationTableDTO.getSumInsuredRestriction() != null) {
	// MastersValue master = masterService
	// .getMaster(pedValidationTableDTO
	// .getSumInsuredRestriction().getId());
	// pedValidationTableDTO.getSumInsuredRestriction().setValue(
	// master.getValue());
	//
	// }
	// List<DiagnosisPED> pedDiagnosisByPEDValidationKey = preauthService
	// .getPEDDiagnosisByPEDValidationKey(pedValidationTableDTO
	// .getKey());
	// List<PedDetailsTableDTO> dtoList = new ArrayList<PedDetailsTableDTO>();
	// for (DiagnosisPED diagnosisPED : pedDiagnosisByPEDValidationKey) {
	// PedDetailsTableDTO dto = new PedDetailsTableDTO();
	// // Added for disabling the procedure that is coming from
	// // preauth.
	// dto.setEnableOrDisable(isEnabled);
	// dto.setDiagnosisName(pedValidationTableDTO.getDiagnosis());
	// dto.setPolicyAgeing(pedValidationTableDTO.getPolicyAgeing());
	// dto.setKey(diagnosisPED.getKey());
	// dto.setPedCode(diagnosisPED.getPedCode());
	// dto.setPedName(diagnosisPED.getPedName());
	//
	// if (diagnosisPED.getDiagonsisImpact() != null) {
	// SelectValue value = new SelectValue();
	// value.setId(diagnosisPED.getDiagonsisImpact().getKey());
	// value.setValue(diagnosisPED.getDiagonsisImpact().getValue());
	// dto.setPedExclusionImpactOnDiagnosis(value);
	// }
	//
	// if (diagnosisPED.getExclusionDetails() != null) {
	// SelectValue exclusionValue = new SelectValue();
	// exclusionValue.setId(diagnosisPED.getExclusionDetails()
	// .getKey());
	// exclusionValue.setValue(diagnosisPED.getExclusionDetails()
	// .getExclusion());
	// dto.setExclusionDetails(exclusionValue);
	// }
	//
	// dto.setRemarks(diagnosisPED.getDiagnosisRemarks());
	// dtoList.add(dto);
	// }
	// pedValidationTableDTO.setPedList(dtoList);
	// }
	//
	// // TODO: Need to change this behaviour..
	// preauthDTO.getPreauthDataExtractionDetails().setDiagnosisTableList(
	// newPedValidationTableListDto);
	//
	// //
	// preauthDTO.getPreauthMedicalProcessingDetails().setPedValidationTableList(newPedValidationTableListDto);
	//
	// List<ClaimAmountDetails> findClaimAmountDetailsByPreauthKey =
	// preauthService
	// .findClaimAmountDetailsByPreauthKey(previousPreauth.getKey());
	// preauthDTO
	// .getPreauthDataExtractionDetails()
	// .setClaimedDetailsList(
	// premedicalMapper
	// .getClaimedAmountDetailsDTOList(findClaimAmountDetailsByPreauthKey));
	// Integer sumInsured = preauthService.getSumInsured(preauthDTO
	// .getPolicyDto().getProduct().getKey(),
	// (insuredSumInsured == 0) ? preauthDTO.getPolicyDto()
	// .getTotalSumInsured() : insuredSumInsured);
	// preauthDTO.getPolicyDto().setInsuredSumInsured(insuredSumInsured);
	// String strAutoRestorationFlg = preauthDTO.getNewIntimationDTO()
	// .getPolicy().getProduct().getAutoRestorationFlag();
	//
	// if (("Y").equalsIgnoreCase(strAutoRestorationFlg)) {
	// if (sumInsured != null && sumInsured.intValue() > 0) {
	// preauthDTO.getPreauthDataExtractionDetails()
	// .setAutoRestoration(
	// SHAConstants.AUTO_RESTORATION_NOTDONE);
	// } else if (null != sumInsured && 0 == sumInsured.intValue()) {
	// preauthDTO.getPreauthDataExtractionDetails()
	// .setAutoRestoration(SHAConstants.AUTO_RESTORATION_DONE);
	// }
	// } else {
	// preauthDTO.getPreauthDataExtractionDetails().setAutoRestoration(
	// SHAConstants.AUTO_RESTORATION_NOTAPPLICABLE);
	// }
	// }

	
	private PreauthDTO setReimbursmentTOPreauthDTO(
			ZonalMedicalReviewMapper reimbursementMapper, Claim claimByKey,
			Reimbursement reimbursement, PreauthDTO preauthDTO,
			Boolean isEnabled) {
		if (claimByKey != null) {
			setClaimValuesToDTO(preauthDTO, claimByKey);
			NewIntimationDto newIntimationDto = intimationService
					.getIntimationDto(claimByKey.getIntimation());
			ClaimDto claimDTO =  ClaimMapper.getInstance().getClaimDto(claimByKey);
			preauthDTO.setNewIntimationDTO(newIntimationDto);
			preauthDTO.setClaimDTO(claimDTO);
			preauthDTO.getClaimDTO().setNewIntimationDto(newIntimationDto);
		}
		preauthDTO.setKey(reimbursement.getKey());
		/*String policyNumber = preauthDTO.getPolicyDto().getPolicyNumber();
		List<ViewTmpClaim> previousclaimsList = new ArrayList<ViewTmpClaim>();
		List<ViewTmpClaim> claimsByPolicyNumber = claimService
				.getViewTmpClaimsByPolicyNumber(policyNumber);
		Policy byPolicyNumber = policyService.getByPolicyNumber(policyNumber);
		previousclaimsList.addAll(claimsByPolicyNumber);
		
		previousclaimsList = getPreviousClaimForPreviousPolicy(byPolicyNumber.getRenewalPolicyNumber(), previousclaimsList);*/

		// List<PreviousClaimsTableDTO> previousClaimDTOList = SHAUtils
		// .getPreviousClaims(claimsByPolicyNumber,
		// claimByKey.getClaimId(), pedValidationService,
		// masterService);
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		List<PreviousClaimsTableDTO> previousClaimDTOList = dbCalculationService.getPreviousClaims(claimByKey.getKey(), claimByKey.getIntimation().getPolicy().getKey(), 
				claimByKey.getIntimation().getInsured().getKey(), SHAConstants.POLICY_WISE_SEARCH_TYPE);

		/*List<PreviousClaimsTableDTO> previousClaimDTOList = preauthService
				.getPreviousClaims(previousclaimsList,
						claimByKey.getClaimId());*/

		// List<PreviousClaimsTableDTO> previousClaimDTOList = new
		// PreviousClaimMapper().getPreviousClaimDTOList(claimsByPolicyNumber);

		preauthDTO.setPreviousClaimsList(previousClaimDTOList);

		if (null != reimbursement.getCoordinatorFlag()
				&& reimbursement.getCoordinatorFlag().equalsIgnoreCase("y")) {

			CoordinatorDTO coordinatorDTO = reimbursementMapper
					.getCoordinatorDTO(preauthService
							.findCoordinatorByClaimKey(reimbursement.getClaim()
									.getKey()));
			coordinatorDTO.setRefertoCoordinator(true);
			preauthDTO.setCoordinatorDetails(coordinatorDTO);
		}

		UpdateHospital updateHospitalByReimbursementKey = reimbursementService
				.updateHospitalByReimbursementKey(reimbursement.getKey());
		if (updateHospitalByReimbursementKey != null) {
			ZonalReviewUpdateHospitalDetailsDTO updateHospitalDTO = reimbursementMapper
					.getUpdateHospitalDTO(updateHospitalByReimbursementKey);
			preauthDTO.getPreauthDataExtractionDetails()
					.setUpdateHospitalDetails(updateHospitalDTO);
		}

		PreviousClaimedHistory claimedHistoryByTransactionKey = reimbursementService
				.getClaimedHistoryByTransactionKey(reimbursement.getKey());
		if (claimedHistoryByTransactionKey != null) {
			preauthDTO.getPreauthDataExtractionDetails()
					.setCoveredPreviousClaim(true);
			preauthDTO
					.getPreauthDataExtractionDetails()
					.setOtherClaimDetails(
							reimbursementMapper
									.getClaimedHistoryDTO(claimedHistoryByTransactionKey));

			List<PreviousClaimedHospitalization> claimedHospitalizationByClaimedHistoryKey = reimbursementService
					.getClaimedHospitalizationByClaimedHistoryKey(claimedHistoryByTransactionKey
							.getKey());
			List<OtherClaimDiagnosisDTO> otherClaimDiagnosisDTOList = reimbursementMapper
					.getOtherClaimDiagnosisDTOList(claimedHospitalizationByClaimedHistoryKey);

			preauthDTO.getPreauthDataExtractionDetails()
					.setOtherClaimDetailsList(otherClaimDiagnosisDTOList);
		}

		List<SpecialityDTO> specialityDTOList = reimbursementMapper
				.getSpecialityDTOList(preauthService
						.findSpecialityByClaimKey(reimbursement.getClaim()
								.getKey()));
		for (SpecialityDTO specialityDTO : specialityDTOList) {
			specialityDTO.setEnableOrDisable(isEnabled);
		}
		preauthDTO.getPreauthDataExtractionDetails().setSpecialityList(
				specialityDTOList);

		List<ProcedureDTO> procedureMainDTOList = reimbursementMapper
				.getProcedureMainDTOList(preauthService
						.findProcedureByPreauthKey(reimbursement.getKey()));
		for (ProcedureDTO procedureDTO : procedureMainDTOList) {
			procedureDTO.setEnableOrDisable(isEnabled);
			if (procedureDTO.getSublimitName() != null) {
				ClaimLimit limit = claimService.getClaimLimitByKey(procedureDTO
						.getSublimitName().getLimitId());
				procedureDTO.setSublimitName(getSublimitFunctionObj(limit));
				procedureDTO.setSublimitDesc(limit.getLimitDescription());
				procedureDTO.setSublimitAmount(limit.getMaxPerPolicyPeriod()
						.toString());
			}
			if (preauthDTO.getIsPostHospitalization()) {
				procedureDTO.setAmountConsideredAmount(0d);
			}

		}

		preauthDTO.getPreauthMedicalProcessingDetails()
				.setProcedureExclusionCheckTableList(procedureMainDTOList);

		List<PedValidation> findPedValidationByPreauthKey = preauthService
				.findPedValidationByPreauthKey(reimbursement.getKey());
		List<DiagnosisDetailsTableDTO> newPedValidationTableListDto = reimbursementMapper
				.getNewPedValidationTableListDto(findPedValidationByPreauthKey);

		// Fix for issue 732 starts.
		//DBCalculationService dbCalculationService = new DBCalculationService();

		Double insuredSumInsured = 0d;
		
		if((ReferenceTable.getGPAProducts().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
	            insuredSumInsured = dbCalculationService.getGPAInsuredSumInsured(
	                            preauthDTO.getNewIntimationDTO().getInsuredPatient()
	                                            .getInsuredId().toString(), preauthDTO.getPolicyDto()
	                                            .getKey());
	    }else{
	            insuredSumInsured = dbCalculationService.getInsuredSumInsured(
	                            preauthDTO.getNewIntimationDTO().getInsuredPatient()
	                                            .getInsuredId().toString(), preauthDTO.getPolicyDto()
	                                            .getKey(),preauthDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
	    }

		Double balanceSI = dbCalculationService.getBalanceSI(
				preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
				preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),
				preauthDTO.getClaimKey(), insuredSumInsured,
				preauthDTO.getNewIntimationDTO().getKey()).get(
				SHAConstants.TOTAL_BALANCE_SI);
		List<Double> copayValue = dbCalculationService.getProductCoPay(
				preauthDTO.getNewIntimationDTO().getPolicy().getProduct()
						.getKey(), preauthDTO.getNewIntimationDTO()
						.getInsuredPatient().getKey(), preauthDTO
						.getNewIntimationDTO().getInsuredPatient()
						.getInsuredId(),preauthDTO.getNewIntimationDTO());
		preauthDTO.setBalanceSI(balanceSI);
		preauthDTO.setProductCopay(copayValue);

		if (preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()
				.equals(ReferenceTable.SUPER_SURPLUS_INDIVIDUAL)
				|| preauthDTO.getNewIntimationDTO().getPolicy().getProduct()
						.getKey().equals(ReferenceTable.SUPER_SURPLUS_FLOATER)) {
			// preauthDTO.setSpecificProductDeductibleDTO(getPreviousClaimsSuperSurplusTable(claimsByPolicyNumber
			// ,claimByKey.getClaimId(), preauthDTO));
		}

		Map<Long, SublimitFunObject> sublimitFunObjMap = getSublimitFunObjMap(
				preauthDTO.getPolicyDto().getProduct().getKey(),
				insuredSumInsured, preauthDTO.getNewIntimationDTO()
						.getInsuredPatient().getInsuredAge(), preauthDTO);
		// Fix for issue 732 ends

		for (DiagnosisDetailsTableDTO pedValidationTableDTO : newPedValidationTableListDto) {
			pedValidationTableDTO.setEnableOrDisable(isEnabled);
			if (pedValidationTableDTO.getDiagnosisName() != null) {
				String diagnosis = masterService
						.getDiagnosis(pedValidationTableDTO.getDiagnosisName()
								.getId());
				pedValidationTableDTO.setDiagnosis(diagnosis);
				pedValidationTableDTO.getDiagnosisName().setValue(diagnosis);
				if (preauthDTO.getIsPostHospitalization()) {
					pedValidationTableDTO.setAmountConsideredAmount(0d);
				}
			}

			if (pedValidationTableDTO.getSublimitName() != null) {
				// Fix for issue 732 starts.
				SublimitFunObject objSublimitFun = sublimitFunObjMap
						.get(pedValidationTableDTO.getSublimitName()
								.getLimitId());
				// ClaimLimit limit =
				// claimService.getClaimLimitByKey(pedValidationTableDTO.getSublimitName().getLimitId());
				if (objSublimitFun != null) {
					pedValidationTableDTO.setSublimitName(objSublimitFun);
					pedValidationTableDTO.setSublimitAmt(String
							.valueOf(objSublimitFun.getAmount()));
				}
				// Fix for issue 732 ends
			}

			if (pedValidationTableDTO.getSumInsuredRestriction() != null) {
				MastersValue master = masterService
						.getMaster(pedValidationTableDTO
								.getSumInsuredRestriction().getId());
				pedValidationTableDTO.getSumInsuredRestriction().setValue(
						master.getValue());
			}
			List<DiagnosisPED> pedDiagnosisByPEDValidationKey = preauthService
					.getPEDDiagnosisByPEDValidationKey(pedValidationTableDTO
							.getKey());
			List<PedDetailsTableDTO> dtoList = new ArrayList<PedDetailsTableDTO>();
			for (DiagnosisPED diagnosisPED : pedDiagnosisByPEDValidationKey) {
				PedDetailsTableDTO dto = new PedDetailsTableDTO();
				// Added for disabling the procedure that is coming from
				// preauth.
				dto.setEnableOrDisable(isEnabled);
				dto.setDiagnosisName(pedValidationTableDTO.getDiagnosis());
				dto.setPolicyAgeing(pedValidationTableDTO.getPolicyAgeing());
				dto.setKey(diagnosisPED.getKey());
				dto.setPedCode(diagnosisPED.getPedCode());
				dto.setPedName(diagnosisPED.getPedName());

				if (diagnosisPED.getDiagonsisImpact() != null) {
					SelectValue value = new SelectValue();
					value.setId(diagnosisPED.getDiagonsisImpact().getKey());
					value.setValue(diagnosisPED.getDiagonsisImpact().getValue());
					dto.setPedExclusionImpactOnDiagnosis(value);
					BeanItemContainer<ExclusionDetails> contaniner = masterService
							.getExclusionDetailsByImpactKey(diagnosisPED
									.getDiagonsisImpact().getKey());
					dto.setExclusionAllDetails(contaniner.getItemIds());
				}

				if (diagnosisPED.getExclusionDetails() != null) {
					SelectValue exclusionValue = new SelectValue();
					exclusionValue.setId(diagnosisPED.getExclusionDetails()
							.getKey());
					exclusionValue.setValue(diagnosisPED.getExclusionDetails()
							.getExclusion());
					dto.setExclusionDetails(exclusionValue);
				}

				dto.setRemarks(diagnosisPED.getDiagnosisRemarks());
				dtoList.add(dto);
			}
			pedValidationTableDTO.setPedList(dtoList);
		}

		// TODO: Need to change this behaviour..
		preauthDTO.getPreauthDataExtractionDetails().setDiagnosisTableList(
				newPedValidationTableListDto);

		// preauthDTO.getPreauthMedicalProcessingDetails().setPedValidationTableList(newPedValidationTableListDto);

		Integer sumInsured = preauthService.getSumInsured(preauthDTO
				.getPolicyDto().getProduct().getKey(),
				(insuredSumInsured == 0) ? preauthDTO.getPolicyDto()
						.getTotalSumInsured() : insuredSumInsured);
		preauthDTO.getPolicyDto().setInsuredSumInsured(insuredSumInsured);
		String strAutoRestorationFlg = preauthDTO.getNewIntimationDTO()
				.getPolicy().getProduct().getAutoRestorationFlag();

		if (("Y").equalsIgnoreCase(strAutoRestorationFlg)) {
			if (sumInsured != null && sumInsured.intValue() > 0) {
				preauthDTO.getPreauthDataExtractionDetails()
						.setAutoRestoration(
								SHAConstants.AUTO_RESTORATION_NOTDONE);
			} else if (null != sumInsured && 0 == sumInsured.intValue()) {
				preauthDTO.getPreauthDataExtractionDetails()
						.setAutoRestoration(SHAConstants.AUTO_RESTORATION_DONE);
			}
		} else {
			preauthDTO.getPreauthDataExtractionDetails().setAutoRestoration(
					SHAConstants.AUTO_RESTORATION_NOTAPPLICABLE);
		}
		return preauthDTO;
	}

	private List<ReconsiderRODRequestTableDTO> getReconsiderRODRequest(
			Claim claim) {
		List<ReconsiderRODRequestTableDTO> reconsiderRODList = documentService
				.getReconsiderRequestTableValues(claim);
		return reconsiderRODList;
	}

	private Map<Long, SublimitFunObject> getSublimitFunObjMap(Long productKey,
			Double insuredSumInsured, Double insuredAge, PreauthDTO preauthDTO) {

//		Reimbursement reimbursment = null;
//
//		if (preauthDTO.getKey() != null) {
//			reimbursment = reimbursementService
//					.getReimbursementByKey(preauthDTO.getKey());
//
//		}
//
//		DBCalculationService dbCalculationService = new DBCalculationService();
		Map<Long, SublimitFunObject> sublimitFunMap = new HashMap<Long, SublimitFunObject>();
//
//		List<SublimitFunObject> sublimitList = new ArrayList<SublimitFunObject>();
//
//		if (reimbursment != null && reimbursment.getSectionCategory() != null) {
//
//			sublimitList = dbCalculationService
//					.getClaimedAmountDetailsForSection(
//							productKey,
//							insuredSumInsured,
//							insuredAge,
//							reimbursment.getSectionCategory(),
//							preauthDTO.getPolicyDto().getPolicyPlan() != null ? preauthDTO
//
//									.getPolicyDto().getPolicyPlan() : "0", "");
//		} else {
//			sublimitList = dbCalculationService
//					.getClaimedAmountDetailsForSection(productKey,
//							insuredSumInsured, insuredAge, 0l, "0", "");
//
//									.getPolicyDto().getPolicyPlan() : "0", null);
//		} else {
//			sublimitList = dbCalculationService
//					.getClaimedAmountDetailsForSection(productKey,
//							insuredSumInsured, insuredAge, 0l, "0", null);
//
//		}
//
//		if (null != sublimitList && !sublimitList.isEmpty()) {
//			for (SublimitFunObject sublimitFunObj : sublimitList) {
//				sublimitFunMap.put(sublimitFunObj.getLimitId(), sublimitFunObj);
//			}
//		}
		return sublimitFunMap;
//		return

	}

	protected Reimbursement getPreviousLatestROD(Long claimKey,
			Reimbursement currentReimbursement) {
		Reimbursement previousROD = null;
		String[] split = currentReimbursement.getRodNumber().split("/");
		String defaultNumber = split[5];
		Integer nextRODNo = Integer.valueOf(defaultNumber);
		Integer previousNumber = nextRODNo - 1;
		List<Reimbursement> previousRODByClaimKey = reimbursementService
				.getPreviousRODByClaimKey(claimKey);
		if (previousRODByClaimKey != null && !previousRODByClaimKey.isEmpty()) {
			for (Reimbursement reimbursement : previousRODByClaimKey) {
				String[] eachSplit = reimbursement.getRodNumber().split("/");
				String eachSplitNo = eachSplit[5];
				Integer eachRODNo = Integer.valueOf(eachSplitNo);
				if (previousNumber.equals(eachRODNo)) {
					previousROD = reimbursement;
				}
			}
		}

		return previousROD;
	}

	private void setReimbursementValues(Reimbursement reimbursement,
			PreauthDTO reimbursmentDTO) {
		reimbursmentDTO.setKey(reimbursement.getKey());
		reimbursmentDTO.setStatusKey(reimbursement.getStatus().getKey());
		reimbursmentDTO.setStageKey(reimbursement.getStage().getKey());

		/**
		 * Bank id as been changed from Long to MAS_BANK type. hence below line
		 * as been changed accordingly
		 * */
		reimbursmentDTO.setBankId(reimbursement.getBankId());
		reimbursmentDTO.setAccountNumber(reimbursement.getAccountNumber());
		reimbursmentDTO.setPayableAt(reimbursement.getPayableAt());
		reimbursmentDTO.setPayeeEmailId(reimbursement.getPayeeEmailId());
		reimbursmentDTO.setPanNumber(reimbursement.getPanNumber());
		reimbursmentDTO.setPayeeName(reimbursement.getPayeeName());
		reimbursmentDTO.setPaymentModeId(reimbursement.getPaymentModeId());
		reimbursmentDTO.setRodNumber(reimbursement.getRodNumber());
		PreauthDataExtaractionDTO dataExtraction = reimbursmentDTO
				.getPreauthDataExtractionDetails();
		dataExtraction.setDocAckknowledgement(reimbursement
				.getDocAcknowLedgement());
		dataExtraction.setDocAcknowledgementKey(reimbursement
				.getDocAcknowLedgement() != null ? reimbursement
				.getDocAcknowLedgement().getKey() : null);
		dataExtraction.setPaymentModeFlag(reimbursement.getPaymentModeId());
		dataExtraction.setPanNo(reimbursement.getPanNumber());
		dataExtraction.setChangeInReasonDOA(reimbursement.getReasonForChange());
		dataExtraction.setLegalFirstName(reimbursement.getLegalHeirFirstName());
		dataExtraction.setLegalLastName(reimbursement.getLegalHeirLastName());
		dataExtraction.setLegalMiddleName(reimbursement
				.getLegalHeirMiddleName());
		dataExtraction.setAccountNo(reimbursement.getAccountNumber());
		// dataExtraction.setPayeeName(reimbursement.getPayeeName());
		dataExtraction.setPayableAt(reimbursement.getPayableAt());
	}

	private void setClaimValuesToDTO(PreauthDTO preauthDTO, Claim claimByKey) {
		PolicyDto policyDto = new PolicyMapper().getPolicyDto(claimByKey
				.getIntimation().getPolicy());
		preauthDTO.setHospitalKey(claimByKey.getIntimation().getHospital());
		Long hospital = claimByKey.getIntimation().getHospital();
		Hospitals hospitalById = hosptialService.getHospitalById(hospital);
		preauthDTO.setHospitalCode(hospitalById.getHospitalCode());
		preauthDTO.setClaimNumber(claimByKey.getClaimId());
		preauthDTO.setPolicyDto(policyDto);
		preauthDTO.setDateOfAdmission(claimByKey.getIntimation()
				.getAdmissionDate());
		preauthDTO.setReasonForAdmission(claimByKey.getIntimation()
				.getAdmissionReason());
		preauthDTO.setIntimationKey(claimByKey.getIntimation().getKey());
		preauthDTO
				.setPolicyKey(claimByKey.getIntimation().getPolicy().getKey());
		preauthDTO.setClaimKey(claimByKey.getKey());
	}

	private BillEntryDetailsDTO getBillDetailsDTO(RODBillDetails billDetails,
			UploadDocumentDTO uploadDocumentDTO) {
		BillEntryDetailsDTO dto = new BillEntryDetailsDTO();
		dto.setItemName(billDetails.getItemName());
		dto.setKey(billDetails.getKey());
		SelectValue classificationValue = new SelectValue();
		classificationValue.setId(billDetails.getBillClassification().getKey());
		classificationValue.setValue(billDetails.getBillClassification()
				.getValue());
		dto.setClassification(classificationValue);
		dto.setItemNo(billDetails.getItemNumber());
		dto.setZonalRemarks(uploadDocumentDTO.getZonalRemarks());
		dto.setCorporateRemarks(uploadDocumentDTO.getCorporateRemarks());
		dto.setNoOfDays(billDetails.getNoOfDaysBills() != null ? billDetails
				.getNoOfDaysBills().doubleValue() : 0d);
		dto.setPerDayAmt(billDetails.getPerDayAmountBills());
		dto.setBillValue(billDetails.getTotalAmount());
		dto.setItemValue(billDetails.getTotalAmount());
		SelectValue billCategoryvalue = new SelectValue();
		billCategoryvalue.setId(billDetails.getBillCategory().getKey());
		billCategoryvalue.setValue(billDetails.getBillCategory().getValue());
		dto.setCategory(billCategoryvalue);
		return dto;
	}

	private void setHospitalDetailsToDTO(Hospitals hospital, PreauthDTO dto) {
		ZonalReviewUpdateHospitalDetailsDTO updateHospitalDetails = dto
				.getPreauthDataExtractionDetails().getUpdateHospitalDetails();
		updateHospitalDetails.setHospitalCity(hospital.getCity());
		updateHospitalDetails.setHospitalCityId(hospital.getCityId());
		updateHospitalDetails.setHospitalState(hospital.getState());
		updateHospitalDetails.setHospitalStateId(hospital.getStateId());
		updateHospitalDetails.setHospitalAddress1(hospital.getAddress());
		updateHospitalDetails.setHospitalName(hospital.getName());
		updateHospitalDetails.setHospitalCode(hospital.getHospitalCode());
		updateHospitalDetails.setPanNumber(hospital.getPanNumber());
		updateHospitalDetails.setHospitalTypeId(hospital.getHospitalType()
				.getKey());
		updateHospitalDetails.setHospitalPhoneNo(hospital.getPhoneNumber());
		updateHospitalDetails.setHopitalRegNumber(hospital
				.getRegistrationNumber());
		updateHospitalDetails.setHospitalPincode(hospital.getPincode());
		updateHospitalDetails.setHospitalId(hospital.getKey());
		updateHospitalDetails.setOtFacilityFlag(hospital.getOtFacilityFlag());
		updateHospitalDetails.setIcuFacilityFlag(hospital.getIcuFacilityFlag());
		updateHospitalDetails
				.setInpatientBeds(hospital.getInpatientBeds() != null ? hospital
						.getInpatientBeds().toString() : "");
	}

	private void updateRemarksBasedOnStatus(Reimbursement reimbursement) {

		if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)) {
			txtRemarks.setValue(reimbursement.getApprovalRemarks());
		} else if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.CLAIM_REQUEST_QUERY_STATUS)) {
			ReimbursementQuery reimbursementyQueryByRodKey = queryService
					.getReimbursementyQueryByRodKey(reimbursement.getKey());
			if (reimbursementyQueryByRodKey != null) {
				txtRemarks.setValue(reimbursementyQueryByRodKey
						.getQueryRemarks());
			}
		} else if (reimbursement
				.getStatus()
				.getKey()
				.equals(ReferenceTable.PROCESS_CLAIM_REQUEST_DRAFT_QUERY_STATUS)
				|| reimbursement
						.getStatus()
						.getKey()
						.equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_DRAFT_QUERY_STATUS)) {
			ReimbursementQuery reimbursementyQueryByRodKey = queryService
					.getReimbursementyQueryByRodKey(reimbursement.getKey());
			if (reimbursementyQueryByRodKey != null) {
				txtRemarks.setValue(reimbursementyQueryByRodKey
						.getQueryLetterRemarks());
			}
		} else if (reimbursement
				.getStatus()
				.getKey()
				.equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REJECT_STATUS)
				|| reimbursement
						.getStatus()
						.getKey()
						.equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_REJECT_STATUS)) {
			ReimbursementQuery reimbursementyQueryByRodKey = queryService
					.getReimbursementyQueryByRodKey(reimbursement.getKey());
			if (reimbursementyQueryByRodKey != null) {
				txtRemarks.setValue(reimbursementyQueryByRodKey
						.getRejectionRemarks());
			}
		} else if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.CLAIM_REQUEST_REJECT_STATUS)) {
			ReimbursementRejection reimbursementRejection = rejectionService
					.getRejectionKeyByReimbursement(reimbursement.getKey());
			if (reimbursementRejection != null) {
				txtRemarks.setValue(reimbursementRejection
						.getRejectionRemarks());
			}
		} else if (reimbursement
				.getStatus()
				.getKey()
				.equals(ReferenceTable.PROCESS_CLAIM_REQUEST_DRAFT_REJECT_STATUS)
				|| reimbursement
						.getStatus()
						.getKey()
						.equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_DRAFT_REJECT_STATUS)) {
			ReimbursementRejection reimbursementRejection = rejectionService
					.getRejectionKeyByReimbursement(reimbursement.getKey());
			if (reimbursementRejection != null) {
				txtRemarks.setValue(reimbursementRejection
						.getRejectionLetterRemarks());
			}
		} else if (reimbursement
				.getStatus()
				.getKey()
				.equals(ReferenceTable.PROCESS_CLAIM_REQUEST_DISAPPROVE_REJECT_STATUS)
				|| reimbursement
						.getStatus()
						.getKey()
						.equals(ReferenceTable.PROCESS_CLAIM_REQUEST_DISAPPROVE_REJECT_STATUS)) {
			ReimbursementRejection reimbursementRejection = rejectionService
					.getRejectionKeyByReimbursement(reimbursement.getKey());
			if (reimbursementRejection != null) {
				txtRemarks.setValue(reimbursementRejection
						.getDisapprovedRemarks());
			}
		} else if (reimbursement
				.getStatus()
				.getKey()
				.equals(ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS)) {
			List<FieldVisitRequest> fieldVisitByClaimKey = fvrService
					.getFieldVisitByClaimKey(reimbursement.getClaim().getKey());
			for (FieldVisitRequest fieldVisitRequest : fieldVisitByClaimKey) {
				txtRemarks.setValue(fieldVisitRequest.getFvrTriggerPoints());
			}
		} else if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.CLAIM_REQUEST_ESCALATION_STATUS)) {
			PreauthEscalate escalateByClaimKey = reimbursementService
					.getEscalateByClaimKey(reimbursement.getClaim().getKey());
			if (escalateByClaimKey != null) {
				txtRemarks.setValue(escalateByClaimKey.getEscalateRemarks());
			}
		} else if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.CLAIM_REQUEST_ESCALATION_REPLY_STATUS)) {
			PreauthEscalate escalateByClaimKey = reimbursementService
					.getEscalateByClaimKey(reimbursement.getClaim().getKey());
			if (escalateByClaimKey != null) {
				txtRemarks
						.setValue(escalateByClaimKey.getEsclateReplyRemarks());
			}
		} else if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_STATUS)
				|| reimbursement.getStatus().getKey().equals(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_FA_STATUS)) {
			MedicalApprover medicalApprover = reimbursementService
					.getMedicalApprover(reimbursement.getKey());
			if (medicalApprover != null) {
				txtRemarks.setValue(medicalApprover.getApproverReply());
			}
		} else if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER)) {
			txtRemarks.setValue(reimbursement.getBillingRemarks());
		} else if (reimbursement.getStatus().getKey()
				.equals(ReferenceTable.BILLING_REFER_TO_MEDICALA_APPROVER)) {
			MedicalApprover medicalApprover = reimbursementService
					.getMedicalApprover(reimbursement.getKey());
			if (medicalApprover != null) {
				txtRemarks
						.setValue(medicalApprover.getReferringRemarks() != null ? medicalApprover
								.getReferringRemarks() : medicalApprover
								.getApproverReply());
			}
		} else if (reimbursement
				.getStatus()
				.getKey()
				.equals(ReferenceTable.CLAIM_REQUEST_REFER_TO_COORDINATOR_STATUS)) {

		} else {
			txtRemarks.setValue(reimbursement.getApprovalRemarks());
		}
	}

	private SublimitFunObject getSublimitFunctionObj(ClaimLimit limit) {
		SublimitFunObject obj = new SublimitFunObject();
		obj.setLimitId(limit.getKey());
		obj.setAmount(Double.valueOf(limit.getMaxPerClaimAmount()));
		obj.setName(limit.getLimitName());
		obj.setDescription(limit.getLimitDescription());
		return obj;
	}

	protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);

		return coordinatorValues;
	}

	@SuppressWarnings({ "deprecation" })
	private void setReadOnly(FormLayout a_formLayout, boolean readOnly) {
		Iterator<Component> formLayoutLeftComponent = a_formLayout
				.getComponentIterator();
		while (formLayoutLeftComponent.hasNext()) {
			Component c = formLayoutLeftComponent.next();
			if (c instanceof TextField) {
				TextField field = (TextField) c;
				field.setWidth("300px");
				field.setNullRepresentation("-");
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			} else if (c instanceof TextArea) {
				TextArea field = (TextArea) c;
				field.setWidth("350px");
				field.setNullRepresentation("-");
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			}
		}
	}
	
	public void showScoringView(){
		hospitalScoringView.init(bean.getNewIntimationDTO().getIntimationId(), false);	
		hospitalScoringView.setScreenName("ViewPage");
		VerticalLayout misLayout = new VerticalLayout(hospitalScoringView);
		Window popup = new com.vaadin.ui.Window();
		popup.setWidth("45%");
//		popup.setHeight("92%");
		popup.setContent(misLayout);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);
		popup.addCloseListener(new Window.CloseListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});
		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}
	
	 public void setClearReferenceData(){
	    	SHAUtils.setClearReferenceData(referenceData);
	    	SHAUtils.setClearPreauthDTO(bean);
	    	
	    	if(this.treatingDoctorTableObj!=null){
	    		this.treatingDoctorTableObj.removeRow();
//	    		this.treatingDoctorTableObj=null;
	    	}
	    	if(this.invsReviewRemarksTableObj!=null){
//	    		this.invsReviewRemarksTableObj.remover
//	    		this.invsReviewRemarksTableObj=null;
	    	}
	    	if(this.viewSpecialityTableObj!=null){
	    		this.viewSpecialityTableObj.removeRow();
//	    		this.viewSpecialityTableObj=null;
	    	}
	    	/*if(this.OpinionDetailsRolesTableTableObj!=null){
	    		this.OpinionDetailsRolesTableTableObj.removeRow();
	    		this.OpinionDetailsRolesTableTableObj=null;
	    	}
	    	if(this.cancelledFvrInvsQueryObj!=null){
	    		this.cancelledFvrInvsQueryObj.removeRow();
	    		this.cancelledFvrInvsQueryObj=null;
	    	}*/
	    	if(this.moreDetailsPageObj!=null){
	    		this.moreDetailsPageObj.setClearReferenceData();
//	    		this.moreDetailsPageObj = null;
	    	}
	    	if(this.medicalVerificationTableObj!=null){
	    		this.medicalVerificationTableObj.removeRow();
//	    		this.medicalVerificationTableObj=null;
	    	}
	    	if(this.viewProcedureListTableObj!=null){
	    		this.viewProcedureListTableObj.removeRow();
//	    		this.viewProcedureListTableObj=null;
	    	}
	    	if(this.viewDiagnosisTableObj!=null){
	    		this.viewDiagnosisTableObj.removeRow();
//	    		this.viewDiagnosisTableObj=null;
	    	}
	    	if(this.treatementVerificationObj!=null){
	    		this.treatementVerificationObj.removeRow();
//	    		this.treatementVerificationObj=null;
	    	}
	    	this.binder=null;
	    }

}

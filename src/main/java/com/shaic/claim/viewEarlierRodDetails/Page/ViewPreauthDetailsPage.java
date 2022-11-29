package com.shaic.claim.viewEarlierRodDetails.Page;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ClaimMapper;
import com.shaic.claim.intimation.create.dto.PolicyDto;
import com.shaic.claim.medical.opinion.OpinionValidationMapper;
import com.shaic.claim.medical.opinion.OpinionValidationService;
import com.shaic.claim.medical.opinion.OpinionValidationTableDTO;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.dto.ResidualAmountDTO;
import com.shaic.claim.preauth.view.DiagnosisService;
import com.shaic.claim.preauth.view.ViewPostCashlessRemarkTable;
import com.shaic.claim.preauth.wizard.dto.CoordinatorDTO;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDataExtaractionDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthMedicalDecisionDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthPreviousClaimsDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.preauth.wizard.dto.SpecialityDTO;
import com.shaic.claim.preauth.wizard.dto.TreatingDoctorDTO;
import com.shaic.claim.preauth.wizard.dto.ViewPccRemarksDTO;
import com.shaic.claim.preauth.wizard.pages.AmountConsideredTable;
import com.shaic.claim.premedical.dto.PreauthMedicalProcessingDTO;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.shaic.claim.premedical.mapper.PreMedicalMapper;
import com.shaic.claim.reports.negotiationreport.NegotiationAmountDetails;
import com.shaic.claim.reports.negotiationreport.NegotiationAmountDetailsDTO;
import com.shaic.claim.scoring.HospitalScoringDetailView;
import com.shaic.claim.scoring.ppcoding.ScoringAndPPDetailView;
import com.shaic.claim.viewEarlierRodDetails.Table.RevisedDiagnosisPreauthViewTable;
import com.shaic.domain.AssignedInvestigatiorDetails;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.OpinionValidation;
import com.shaic.domain.PEDValidationService;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.SublimitFunObject;
import com.shaic.domain.TmpEmployee;
import com.shaic.domain.ViewTmpClaim;
import com.shaic.domain.preauth.ClaimAmountDetails;
import com.shaic.domain.preauth.ClaimLimit;
import com.shaic.domain.preauth.DiagnosisPED;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.ResidualAmount;
import com.shaic.domain.preauth.TreatingDoctorDetails;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.domain.PolicyMapper;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.paclaim.reimbursement.medicalapproval.processclaimrequest.pages.dataextraction.PAInvestigationReviewRemarksTable;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class ViewPreauthDetailsPage extends AbstractMVPView implements
		PreauthView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private PreauthDTO bean;

	private BeanFieldGroup<PreauthDataExtaractionDTO> binder;

	private BeanFieldGroup<PreauthPreviousClaimsDTO> previousClaimBinder;

	private BeanFieldGroup<PreauthMedicalDecisionDTO> medicalDecisionBinder;

	private BeanFieldGroup<PreauthMedicalProcessingDTO> medicalProcessingBinder;

	// @Inject
	// private Instance<DiagnosisPreauthViewTable>
	// diagnosisListenerTableInstance;
	//
	// private DiagnosisPreauthViewTable diagnosisListenerTableObj;

	@Inject
	private Instance<RevisedDiagnosisPreauthViewTable> diagnosisListenerTableInstance;

	private RevisedDiagnosisPreauthViewTable diagnosisListenerTableObj;

	@Inject
	private Instance<AmountConsideredTable> amountConsideredTableInstance;
	
	@EJB
	private HospitalService hosptialService;

	private AmountConsideredTable amountConsideredTable;

	private BeanItemContainer<ExclusionDetails> exlusionContainer;

	private TextField approvedAmtField;

	private String diagnosisName;

	private Map<String, Object> sublimitCalculatedValues;

	private TextField txtPreauthStatus;

	private TextField txtPreauthApprovedAmt;

	private TextArea txtMedicalRemarks;

	private TextArea txtPreauthRemarks;

	private TextArea txtDoctorNote;

	private TextField txtDateOfAdmission;

	private TextField txtReasonForChangeAdmisson;

	private TextField txtRoomCategory;
	
	private OptionGroup ventilatorSupportRadio;

	private CheckBox chkCriticalIllness;

	private TextField txtSpecify;

	private TextField txtTreatementType;

	private TextField txtPatientStatus;

	private TextField txtDateOfDeath;

	private TextField txtReasonForDeath;

	private TextField txtTerminateCover;

	private TextField txtReasonForAdmission;

	private TextField txtNoOfDays;

	private TextField txtNatureOfTreatement;

	private TextField txtConsultaionDate;

	private CheckBox chkCorpBuffer;

	private TextField txtAutomaticRestoration;

	private TextField txtIllness;

	private TextField txtRelapseOfIllness;

	private TextField txtRelapseRemark;

	private TextField txtAttachPreviousClaim;

	private OptionGroup initiateFieldVisitRequestRadio;

	private OptionGroup specialistOpinionTaken;

	private ComboBox cmbFvrNotRequiredRemarks;

	private ComboBox cmbAllocationTo;

	private TextField txtFvrTriggerPoints;

	private ComboBox cmbSpecialistType;

	private ComboBox cmbSpecialistConsultant;

	private TextField txtRemarksBySpecialist;

	private TextField txtInvestigatorName;

	private TextField txtInvestigatorRemarks;

	private TextField txtCPURemarks;

	private TextField txtPreauthReceivedDate;

	private TextField txtPreauthResponseTime;

	private TextField txtPreauthReferenceNo;

	private TextField txtPreauthapproField;
	
	private OptionGroup acctDeath;
	
	private TextField dateAcctDeath;
	
	private TextArea txtWithdrawInternalRemarks;
	
	private TextArea txtNegotiationWith;
	
	private TextField txtPreAuthType;
	
	private TextArea txtTimeBasedIcdCodeExclusion;
	
	private TextArea txtDecisionChangeReason;
	
	private TextArea txtDecisionChangeRemarks;
	
	
	@Inject
	private Instance<OpinionDetailsRolesTable> opinionDetailsRolesTable;
	
	private OpinionDetailsRolesTable OpinionDetailsRolesTableTableObj;
	
	
	
	@EJB
	private OpinionValidationService searchService;
	private List<DiagnosisProcedureTableDTO> diagnosisProcedureValues = new ArrayList<DiagnosisProcedureTableDTO>();

	private Button viewPreauthButton;
	
	private Button btnHospitalScroing;
	
	@Inject
	private HospitalScoringDetailView hospitalScoringView;

	@EJB
	private PreauthService preauthService;

	@EJB
	private DBCalculationService dbCalculationService;

	@EJB
	private DiagnosisService diagnosisService;

	@EJB
	private IntimationService intimationService;

	@EJB
	private ClaimService claimService;
	
	@EJB
	private PolicyService policyService;

	@EJB
	private MasterService masterService;

	@EJB
	private PEDValidationService pedValidationService;
	
	@EJB
	private ReimbursementService reimbursementService;

	@Inject
	private Instance<ViewPreauthMoreDetailsPage> viewPreauthMoreDetailsPageInstance;

	private ViewPreauthMoreDetailsPage viewPreauthMoreDetailsPageObj;
	
	/*@Inject
	private Instance<ViewInvestigationRemarksTable> viewInvestigationReviewRemarksInstance;
	
	private ViewInvestigationRemarksTable viewInvestigationRemarksTable;*/

	////private static Window popup;

	private Preauth preauth;
	
	private Preauth previousPreauth;
	
	private Button btnPCCRemarkView;
	
	private TextField txtTypeOfAdmission;
	
	@Inject
	private ViewPostCashlessRemarkTable viewPostCashlessRemark;
	
	@Inject
	private ScoringAndPPDetailView scoringAndPPDetailView;
	
	@Inject
	private Instance<PAInvestigationReviewRemarksTable> invsReviewRemarksTableInstance;
	
    private PAInvestigationReviewRemarksTable invsReviewRemarksTableObj;

	public void init(/* String intimationId */Long key) {

		// Intimation intimation = intimationService
		// .searchbyIntimationNo(intimationId);
		//
		// // String intimationNo ="I/2015/0000890"; //need to implements;
		//
		// // Intimation intimation2 =
		// // intimationService.getIntimationByNo(intimationNo);
		try {
			PreMedicalMapper premedicalMapper=PreMedicalMapper.getInstance();
//			PreMedicalMapper.getAllMapValues();
			//
			// List<Preauth> preauthList = preauthService
			// .getPreauthByIntimationKey(intimation.getKey());
			//
			// List<Long> preauthKey = new ArrayList<Long>();
			// for (Preauth singlePreauth : preauthList) {
			// if(singlePreauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_APPROVE_STATUS)
			// ||
			// singlePreauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS))
			// preauthKey.add(singlePreauth.getKey());
			// }
			//
			// Long key = Collections.min(preauthKey);

			Preauth preauth = preauthService.getPreauthById(key);
			this.preauth = preauth;
			
			previousPreauth = getPreivousPreauth(preauth.getClaim().getKey(),key);

			Intimation intimation = preauth.getIntimation();

			PreauthDTO preauthDTO = premedicalMapper.getPreauthDTO(preauth);
			setpreauthTOPreauthDTO(premedicalMapper, preauth.getClaim(),
					preauth, preauthDTO, false);

			getResidualAmount(preauth, preauthDTO);

			Double insuredSumInsured = dbCalculationService
					.getInsuredSumInsured(preauthDTO.getNewIntimationDTO()
							.getInsuredPatient().getInsuredId().toString(),
							preauthDTO.getNewIntimationDTO().getPolicy()
									.getKey(),preauthDTO.getNewIntimationDTO()
									.getInsuredPatient().getLopFlag());
			
			Double balanceSI = 0d;
			List<Double> copayValue = new ArrayList<Double>();
			if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				balanceSI = dbCalculationService.getBalanceSIForGMC(
						preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
						preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),
						preauthDTO.getClaimKey());
				copayValue = dbCalculationService.getProductCoPayForGMC(
						preauthDTO.getNewIntimationDTO().getPolicy().getKey(),preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey());
			}else{
				balanceSI = dbCalculationService.getBalanceSI(
						preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
						preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),
						preauthDTO.getClaimKey(), insuredSumInsured,
						preauthDTO.getNewIntimationDTO().getKey()).get(
						SHAConstants.TOTAL_BALANCE_SI);
				 copayValue = dbCalculationService.getProductCoPay(
						preauthDTO.getNewIntimationDTO().getPolicy().getProduct()
								.getKey(), preauthDTO.getNewIntimationDTO()
								.getInsuredPatient().getKey(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(),preauthDTO.getNewIntimationDTO());
			}
			preauthDTO.setBalanceSI(balanceSI);
			
//			String policyAgeing = dbCalculationService.getPolicyAgeing(
//					preauthDTO.getDateOfAdmission(), preauthDTO.getPolicyDto()
//							.getPolicyNumber());
			
			String policyAgeing = preauthDTO.getNewIntimationDTO().getPolicyYear();
			
			preauthDTO.setProductCopay(copayValue);
			
			approvedAmtField = new TextField();
			approvedAmtField.setEnabled(false);

			txtPreauthReferenceNo = new TextField();
			// txtPreauthReferenceNo.setValue(preauth.getPreauthId());
			txtPreauthReferenceNo.setWidth("500px");
			txtPreauthReferenceNo.setReadOnly(true);
			txtPreauthReferenceNo.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

			FormLayout referenceNoForm = new FormLayout(txtPreauthReferenceNo);

			viewPreauthButton = new Button("View Pre-auth (Detailed)");
			btnHospitalScroing = new Button("Hospital Scoring/PP Coding");
			btnPCCRemarkView= new Button("PCC Remark");
			/*if(intimation.getHospitalType().getKey() == ReferenceTable.NETWORK_HOSPITAL_TYPE_ID){
				btnHospitalScroing.setVisible(true);
			}else{
				btnHospitalScroing.setVisible(false);
			}*/

			preauthDTO.getPreauthMedicalProcessingDetails().setMedicalRemarks(
					preauthDTO.getMedicalRemarks());

			this.bean = preauthDTO;

			if (bean != null && bean.getDateOfAdmission() != null) {
				bean.setDateOfAdmissionStr(SHAUtils.formatDate(preauthDTO
						.getDateOfAdmission()));
			}

			this.binder = new BeanFieldGroup<PreauthDataExtaractionDTO>(
					PreauthDataExtaractionDTO.class);
			this.binder.setItemDataSource(this.bean
					.getPreauthDataExtractionDetails());

			this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());

			this.medicalDecisionBinder = new BeanFieldGroup<PreauthMedicalDecisionDTO>(
					PreauthMedicalDecisionDTO.class);
			this.medicalDecisionBinder.setItemDataSource(this.bean
					.getPreauthMedicalDecisionDetails());
			this.medicalDecisionBinder
					.setFieldFactory(new EnhancedFieldGroupFieldFactory());

			this.medicalProcessingBinder = new BeanFieldGroup<PreauthMedicalProcessingDTO>(
					PreauthMedicalProcessingDTO.class);
			this.medicalProcessingBinder.setItemDataSource(this.bean
					.getPreauthMedicalProcessingDetails());
			this.medicalProcessingBinder
					.setFieldFactory(new EnhancedFieldGroupFieldFactory());

			this.previousClaimBinder = new BeanFieldGroup<PreauthPreviousClaimsDTO>(
					PreauthPreviousClaimsDTO.class);
			this.previousClaimBinder.setItemDataSource(this.bean
					.getPreauthPreviousClaimsDetails());
			this.previousClaimBinder
					.setFieldFactory(new EnhancedFieldGroupFieldFactory());

			if (bean != null
					&& bean.getPreauthDataExtractionDetails()
							.getAdmissionDate() != null) {
				// Date tempDate =
				// SHAUtils.formatTimestamp(bean.getPreauthDataExtractionDetails().getAdmissionDate());
				bean.getPreauthDataExtractionDetails().setAdmissionDateStr(
						(SHAUtils.formatDate(bean
								.getPreauthDataExtractionDetails()
								.getAdmissionDate())));
			}

			txtPreauthReceivedDate = new TextField(
					"Pre-auth Recieved Date & Time");
			
			String receivedDate = SHAUtils.formateDateForHistory(preauth.getCreatedDate());
			txtPreauthReceivedDate.setValue(receivedDate);
			
			txtPreauthResponseTime = new TextField(
					"Pre-auth Response Date & Time");
			
			String responseDate = SHAUtils.formateDateForHistory(preauth.getModifiedDate());
			txtPreauthResponseTime.setValue(responseDate);
		

			txtPreauthStatus = new TextField("Status");
			txtPreauthStatus.setValue(this.bean.getStatusValue());
			txtPreauthApprovedAmt = (TextField) binder.buildAndBind(
					"Pre-auth Approved Amt", "totalApprAmt", TextField.class);
			txtMedicalRemarks = (TextArea) medicalProcessingBinder
					.buildAndBind("Medical Remarks", "medicalRemarks",
							TextArea.class);
			txtMedicalRemarks.setWidth("50%");
			txtMedicalRemarks.setRequired(false);
			
			txtWithdrawInternalRemarks = (TextArea) medicalDecisionBinder
					.buildAndBind("Withdrawal Internal Remarks", "withdrawInternalRemarks",
							TextArea.class);
			txtWithdrawInternalRemarks.setWidth("50%");
			txtWithdrawInternalRemarks.setRequired(false);
			
			txtPreauthRemarks = (TextArea) medicalDecisionBinder.buildAndBind(
					"Remarks", "approvalRemarks", TextArea.class);
			txtPreauthRemarks.setValidationVisible(false);
			
			String preauthRemarks = preauth.getRemarks();
			
			txtPreauthRemarks.setValue(preauthRemarks);
			txtPreauthRemarks.setDescription(preauthRemarks);
			txtDoctorNote = new TextArea("Doctor Note (Internal remarks)");
			txtDoctorNote.setValue(this.bean.getDoctorNote());
			txtDoctorNote.setDescription(this.bean.getDoctorNote());

			txtDateOfAdmission = (TextField) binder.buildAndBind(
					"Date of Admission", "admissionDateStr", TextField.class);
			txtReasonForChangeAdmisson = (TextField) binder.buildAndBind(
					"Reason For Change in DOA", "changeOfDOA", TextField.class);

			acctDeath = new OptionGroup("Accident / Death");
			acctDeath.addItem(SHAConstants.ACCIDENT);
			acctDeath.addItem(SHAConstants.DEATH);
			acctDeath.addStyleName("horizontal");
			acctDeath.setEnabled(false);
		    
			dateAcctDeath = new TextField("Date of Accident / Death");
			
			
			 if(intimation.getProcessClaimType() != null && intimation.getProcessClaimType().equalsIgnoreCase("P"))
			    {
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
			    	acctDeath.setVisible(true);
			    	dateAcctDeath.setVisible(true);
			    }
			    else
			    {
			    	acctDeath.setVisible(false);
			    	dateAcctDeath.setVisible(false);
			    }
				
					
					
			txtRoomCategory = new TextField("Room Category");
			txtRoomCategory.setValue(this.bean
					.getPreauthDataExtractionDetails().getRoomCategory()
					.getValue());
			
			ventilatorSupportRadio =new OptionGroup("");
			
			if(this.bean.getPreauthDataExtractionDetails().getRoomCategory() != null 
					&& this.bean.getPreauthDataExtractionDetails().getRoomCategory().getValue() != null 
					&& this.bean.getPreauthDataExtractionDetails().getRoomCategory().getValue().equalsIgnoreCase("ICU/NICU/ICCU/CCU/PICU")){
				
				System.out.println(String.format("Ventilator Support value in View Details: [%s]", this.bean.getPreauthDataExtractionDetails().getVentilatorSupport()));
				ventilatorSupportRadio =new OptionGroup("Ventilator Support");
				ventilatorSupportRadio.addItems(getReadioButtonOptions());
				ventilatorSupportRadio.setItemCaption(true, "Yes");
				ventilatorSupportRadio.setItemCaption(false, "No");
				ventilatorSupportRadio.setStyleName("horizontal");
				ventilatorSupportRadio.setValue(this.bean.getPreauthDataExtractionDetails().getVentilatorSupport()? true : false);
				ventilatorSupportRadio.setReadOnly(true);
				ventilatorSupportRadio.setEnabled(false);
			}

			chkCriticalIllness = (CheckBox) binder.buildAndBind(
					"Critical Illness", "criticalIllness", CheckBox.class);
			FormLayout illnessLayout = new FormLayout(chkCriticalIllness);
			illnessLayout.setCaption("Critical Illness");
			illnessLayout.setMargin(false);
			
			txtSpecify = new TextField("Specify");
			if (null != this.bean.getPreauthDataExtractionDetails()
					.getSpecifyIllness()) {
				txtSpecify.setValue(this.bean.getPreauthDataExtractionDetails()
						.getSpecifyIllness().getValue());
			}

			txtTreatementType = new TextField("Treatment Type");
			txtTreatementType.setValue(this.bean
					.getPreauthDataExtractionDetails().getTreatmentType()
					.getValue());

			txtPatientStatus = new TextField("Patient Status");
			txtPatientStatus.setValue(this.bean
					.getPreauthDataExtractionDetails().getPatientStatus()
					.getValue());

			txtDateOfDeath = (TextField) binder.buildAndBind("Date of Death",
					"deathDate", TextField.class);
			txtDateOfDeath.setValidationVisible(false);
			txtReasonForDeath = (TextField) binder.buildAndBind(
					"Reason for Death", "reasonForDeath", TextField.class);
			txtReasonForDeath.setValidationVisible(false);
			
			txtTerminateCover = new TextField("Terminate Cover");
			txtTerminateCover.setValue(preauth.getTerminatorCover());
			
			//removed as requested for PA
			/*if (this.bean.getPreauthDataExtractionDetails().getPatientStatus() != null) {
				if (this.bean.getPreauthDataExtractionDetails()
						.getPatientStatus().getId()
						.equals(ReferenceTable.PATIENT_STATUS_ADMITTED)) {
					txtDateOfDeath.setEnabled(true);
					txtReasonForDeath.setEnabled(true);
					txtTerminateCover.setEnabled(true);
				}
			}*/
			
			String medicalRemarks = preauth.getMedicalRemarks();
			
			txtMedicalRemarks.setValue(medicalRemarks);
			if(medicalRemarks != null){
				txtMedicalRemarks.setDescription(medicalRemarks);
			}
			
			//added for withdraw CR R1180
            String withdrawInternalRemarks = preauth.getWithdrawInternalRemarks();
			
            txtWithdrawInternalRemarks.setValue(withdrawInternalRemarks);
			if(medicalRemarks != null){
				txtWithdrawInternalRemarks.setDescription(withdrawInternalRemarks);
			}

			txtReasonForAdmission = (TextField) binder.buildAndBind(
					"Reason For Admission", "reasonForAdmission",
					TextField.class);
			txtReasonForAdmission.setValue(intimation.getAdmissionReason());
			txtNoOfDays = (TextField) binder.buildAndBind("Length of Stay",
					"noOfDays", TextField.class);

			txtNatureOfTreatement = new TextField("Nature of Treatment");
			if (null != this.bean.getPreauthDataExtractionDetails()
					.getNatureOfTreatment()) {
				txtNatureOfTreatement.setValue(this.bean
						.getPreauthDataExtractionDetails()
						.getNatureOfTreatment().getValue());
			}

			txtConsultaionDate = (TextField) binder.buildAndBind(
					"1st Consultation Date", "firstConsultantDate",
					TextField.class);
			
			chkCorpBuffer = (CheckBox) binder.buildAndBind("Corp Buffer",
					"corpBuffer", CheckBox.class);
			FormLayout bufferFLayout = new FormLayout(chkCorpBuffer);
			bufferFLayout.setCaption("Corp Buffer");
			bufferFLayout.setMargin(false);
			txtAutomaticRestoration = (TextField) binder
					.buildAndBind("Automatic Restoration", "autoRestoration",
							TextField.class);

			txtIllness = new TextField("Illness");
			if (this.bean.getPreauthDataExtractionDetails().getIllness() != null) {
				txtIllness.setValue(this.bean.getPreauthDataExtractionDetails()
						.getIllness().getValue());
			}

			txtRelapseOfIllness = (TextField) binder.buildAndBind(
					"Relapse of Illness", "relapseIllness", TextField.class);
			txtRelapseRemark = new TextField("Remarks (Relapse)");

			txtAttachPreviousClaim = (TextField) previousClaimBinder
					.buildAndBind("Attach to Previous Claim",
							"attachToPreviousClaim", TextField.class);

			cmbFvrNotRequiredRemarks = (ComboBox) medicalDecisionBinder
					.buildAndBind("FVR Not Required Remarks",
							"fvrNotRequiredRemarks", ComboBox.class);
			cmbAllocationTo = (ComboBox) medicalDecisionBinder.buildAndBind(
					"Allocation to", "allocationTo", ComboBox.class);
			txtFvrTriggerPoints = (TextField) medicalDecisionBinder
					.buildAndBind("FVR Trigger Points", "fvrTriggerPoints",
							TextField.class);
			txtInvestigatorName = (TextField) medicalDecisionBinder
					.buildAndBind("Investigator Name", "investigatorNameValue",
							TextField.class);
			txtInvestigatorRemarks = (TextField) medicalDecisionBinder
					.buildAndBind("Investigation Review Remarks",
							"investigationReviewRemarks", TextField.class);
			txtInvestigatorRemarks.setValidationVisible(false);
			txtInvestigatorRemarks.setWidth("50px");
			
			cmbSpecialistType = (ComboBox) medicalDecisionBinder.buildAndBind(
					"Specialist Type", "specialistType", ComboBox.class);
			
			
			
			cmbSpecialistConsultant = (ComboBox) medicalDecisionBinder
					.buildAndBind("Specialist Consulted",
							"specialistConsulted", ComboBox.class);
			txtRemarksBySpecialist = (TextField) medicalDecisionBinder
					.buildAndBind("Remarks by Specialist",
							"remarksBySpecialist", TextField.class);
			txtCPURemarks = (TextField) medicalDecisionBinder.buildAndBind(
					"Remarks for CPU", "remarksForCPU", TextField.class);

			initiateFieldVisitRequestRadio = (OptionGroup) medicalDecisionBinder
					.buildAndBind("Initiate Field Visit Request",
							"initiateFieldVisitRequestFlag", OptionGroup.class);

			initiateFieldVisitRequestRadio.addItems(getReadioButtonOptions());
			initiateFieldVisitRequestRadio.setItemCaption(true, "Yes");
			initiateFieldVisitRequestRadio.setItemCaption(false, "No");
			initiateFieldVisitRequestRadio.setStyleName("horizontal");
			initiateFieldVisitRequestRadio.select(this.bean
					.getPreauthMedicalDecisionDetails()
					.getInitiateFieldVisitRequestFlag());
			initiateFieldVisitRequestRadio.setReadOnly(true);
			initiateFieldVisitRequestRadio.setEnabled(false);

			specialistOpinionTaken = (OptionGroup) medicalDecisionBinder
					.buildAndBind("Specialist Opinion Taken",
							"specialistOpinionTakenFlag", OptionGroup.class);
			specialistOpinionTaken.addItems(getReadioButtonOptions());
			specialistOpinionTaken.setItemCaption(true, "Yes");
			specialistOpinionTaken.setItemCaption(false, "No");
			specialistOpinionTaken.setStyleName("horizontal");
			specialistOpinionTaken.setEnabled(false);
			
			specialistOpinionTaken.select(this.bean
					.getPreauthMedicalDecisionDetails()
					.getSpecialistOpinionTakenFlag());
			specialistOpinionTaken.setReadOnly(true);
			
			
			txtNegotiationWith = (TextArea) medicalDecisionBinder
					.buildAndBind("Negotiated With",
							"negotiationWith", TextArea.class);
			txtNegotiationWith.setEnabled(true);
			
			txtTypeOfAdmission = new TextField("Type Of Admission");
			if( txtTypeOfAdmission != null && preauth != null && preauth.getTypeOfAdmission() != null ){
				txtTypeOfAdmission.setValue(masterService.getMaster(preauth.getTypeOfAdmission()).getValue());
			}
//			txtNegotiationWith.setValue(bean.getPreauthMedicalDecisionDetails().getNegotiationWith());
			
			//R1162
			List<OpinionValidationTableDTO> tableDTO= new ArrayList<OpinionValidationTableDTO>();
			List<OpinionValidation> opinionValidation =   searchService.getOpinionObjectBYIntimationNO(intimation.getIntimationId());
			if(opinionValidation!=null){
				tableDTO = OpinionValidationMapper.getInstance().getOpinionValidationTableObjects(opinionValidation);
			}
			
			/*this.viewInvestigationRemarksTable = this.viewInvestigationReviewRemarksInstance.get();
			this.viewInvestigationRemarksTable.init(SHAConstants.PRE_AUTH);
			
			List<AssignedInvestigatiorDetails> invsReviewRemarksList = reimbursementService.getRepiedInvestigationDetails(preauthDTO.getClaimDTO().getKey() != null ? preauthDTO.getClaimDTO().getKey():0);
			if(invsReviewRemarksList != null){
				List<AssignedInvestigatiorDetails> invsRepliedRemarksList = new ArrayList<AssignedInvestigatiorDetails>();
				for (AssignedInvestigatiorDetails assignedInvestigatiorDetails : invsReviewRemarksList) {
					if(assignedInvestigatiorDetails.getReportReviewed() != null && assignedInvestigatiorDetails.getReportReviewed().equalsIgnoreCase(SHAConstants.YES_FLAG)){
						invsRepliedRemarksList.add(assignedInvestigatiorDetails);
					}
				}
				
				this.viewInvestigationRemarksTable.setTableList(invsRepliedRemarksList);
			}*/
			
			List<AssignedInvestigatiorDetails> invsReviewRemarksList = reimbursementService.getInvsReviewRemarksDetailsByClaimKey(this.bean.getClaimKey());
			invsReviewRemarksTableObj = invsReviewRemarksTableInstance.get();
			invsReviewRemarksTableObj.init();
			invsReviewRemarksTableObj.setTableList(invsReviewRemarksList);
			
			HorizontalLayout specialistHLayout = new HorizontalLayout(invsReviewRemarksTableObj);
			specialistHLayout.setWidth("100%");
			specialistHLayout.setSpacing(true);
			
			this.OpinionDetailsRolesTableTableObj = this.opinionDetailsRolesTable.get();
			this.OpinionDetailsRolesTableTableObj.init("",false,false);
			
			
			
			if(tableDTO!=null && !tableDTO.isEmpty()){
				for(OpinionValidationTableDTO roleDTO:tableDTO){
					String roleNam= masterService.getOpninonRole(roleDTO.getAssignedRoleBy());
					roleDTO.setAssignedRoleBy(roleNam);
					

					TmpEmployee tmpEmployee = claimService.getEmployeeName(roleDTO.getAssignedDocName());
					if(tmpEmployee != null && tmpEmployee.getEmpFirstName()!=null) {
						roleDTO.setAssignedDocName(tmpEmployee.getEmpFirstName());
						}
					
				OpinionDetailsRolesTableTableObj.setTableList(tableDTO);
				OpinionDetailsRolesTableTableObj.tablesize();
			}
			
			}
			
			/**
			 * ***
			 * code changed for GLX2020021
			 */
			txtPreAuthType = new TextField("Pre-Auth Type");
			if(preauth != null && preauth.getStpProcessLevel() != null){
				MastersValue preauthType=masterService.getMaster(preauth.getStpProcessLevel());
				txtPreAuthType.setValue(preauthType.getValue());
			}
			
			txtTimeBasedIcdCodeExclusion = new TextArea("Time based excluded ICD Reason");
			if(preauth != null && preauth.getReasonForIcdExclusion()!= null){
				txtTimeBasedIcdCodeExclusion.setValue(preauth.getReasonForIcdExclusion());
			}
			
			txtDecisionChangeReason = new TextArea("Decision Change Reason");
			if(this.bean.getPreauthMedicalDecisionDetails() != null && this.bean.getPreauthMedicalDecisionDetails().getDecisionChangeReason() != null){
				txtDecisionChangeReason.setValue(this.bean.getPreauthMedicalDecisionDetails().getDecisionChangeReason().getValue());
			}
			
			txtDecisionChangeRemarks = new TextArea("Decision Change Remarks");
			if(preauth != null && preauth.getDecisionChangeRemarks()!= null){
				txtDecisionChangeRemarks.setValue(preauth.getDecisionChangeRemarks());
			}
			
			

			FormLayout firstForm = new FormLayout(txtPreauthReceivedDate,
					txtPreauthStatus, txtPreauthApprovedAmt, txtMedicalRemarks,txtWithdrawInternalRemarks,
					txtDateOfAdmission, acctDeath, txtReasonForChangeAdmisson,
					txtRoomCategory, ventilatorSupportRadio,illnessLayout, txtSpecify,
					txtTreatementType,txtTypeOfAdmission, txtPatientStatus, txtDateOfDeath,
					txtReasonForDeath, txtTerminateCover,
					initiateFieldVisitRequestRadio, txtPreAuthType,txtTimeBasedIcdCodeExclusion,txtDecisionChangeReason,txtDecisionChangeRemarks/*, txtInvestigatorName,
					txtInvestigatorRemarks*/);
			VerticalLayout vLeftLayout = new VerticalLayout();
			vLeftLayout.addComponents(firstForm,specialistHLayout);;
			firstForm.setSpacing(true);
			setReadOnly(firstForm, true);
			
			FormLayout secondForm = new FormLayout(txtPreauthResponseTime,
					txtPreauthRemarks, txtDoctorNote, dateAcctDeath, txtReasonForAdmission,
					txtNoOfDays, txtNatureOfTreatement, txtConsultaionDate,
					bufferFLayout, txtAutomaticRestoration, txtIllness,
					txtRelapseOfIllness, txtRelapseRemark,
					txtAttachPreviousClaim, specialistOpinionTaken, txtNegotiationWith );//change2
			VerticalLayout vLayout = new VerticalLayout();
			vLayout.addComponents(secondForm,OpinionDetailsRolesTableTableObj);
			FormLayout finalLayout = new FormLayout();
			finalLayout.addComponent(vLayout);
			secondForm.setSpacing(true);
			setReadOnly(secondForm, true);
			
			HorizontalLayout firstHor = new HorizontalLayout(vLeftLayout,
					secondForm);
			firstHor.setSpacing(true);
		//	firstHor.setWidth("110%");

			Panel panel = new Panel(firstHor);
			panel.setCaption("Preauth Details");
			panel.addStyleName("girdBorder");
			panel.setHeight("1000px");

			this.diagnosisListenerTableObj = this.diagnosisListenerTableInstance
					.get();
			// View PreauthDetails Multilple Data Issue
			this.diagnosisListenerTableObj.removeAllItems();
			this.diagnosisListenerTableObj.init(this.bean,approvedAmtField);

			setValueToMedicalDecisionValues();

			Label amtClaimedDetailsLbl = new Label("A) Amount Considered");
			HorizontalLayout viewLayout1 = new HorizontalLayout(
					amtClaimedDetailsLbl);
			viewLayout1.setSpacing(true);

			Label balanceSumInsuredLbl = new Label("B) Balance Sum Insured");

			HorizontalLayout viewLayout2 = new HorizontalLayout(
					balanceSumInsuredLbl);
			viewLayout2.setSpacing(true);

			
			
			HorizontalLayout approvedFormLayout = new HorizontalLayout(
					new Label(
							"C) Sub limits, </br> Package &  </br> SI Restriction Amount",
							ContentMode.HTML), approvedAmtField);

			this.amountConsideredTable = this.amountConsideredTableInstance
					.get();
			this.amountConsideredTable.initTable(this.bean, viewLayout1,
					viewLayout2, approvedFormLayout, true, false);

			if (this.bean.getPreauthMedicalDecisionDetails()
					.getInitialApprovedAmt() != null) {
//				approvedAmtField.setValue(this.bean
//						.getPreauthMedicalDecisionDetails()
//						.getInitialApprovedAmt().toString());
//				this.amountConsideredTable.setApprovedAmtForViews(this.bean
//						.getPreauthMedicalDecisionDetails()
//						.getInitialApprovedAmt().toString(), preauth.getTotalApprovalAmount() != null ? preauth.getTotalApprovalAmount().toString() : " "); 
			}

			HorizontalLayout horizontalLayout = new HorizontalLayout(
					referenceNoForm, viewPreauthButton,btnHospitalScroing,btnPCCRemarkView);
			horizontalLayout.setComponentAlignment(viewPreauthButton, Alignment.TOP_RIGHT);
			horizontalLayout.setComponentAlignment(btnHospitalScroing, Alignment.TOP_RIGHT);
			

			txtPreauthapproField = new TextField("Pre-auth Approved Amt");
			txtPreauthapproField.setValue(preauth.getTotalApprovalAmount() != null ? preauth.getTotalApprovalAmount()
					.toString() : " ");
			txtPreauthapproField.setReadOnly(true);

			FormLayout form1 = new FormLayout(txtPreauthapproField);

			VerticalLayout mainVertical = new VerticalLayout(horizontalLayout,
					panel, this.diagnosisListenerTableObj,
					this.amountConsideredTable, form1);

			mainVertical.setSpacing(true);
			mainVertical.setMargin(true);

			addListener();
			
			

			setCompositionRoot(mainVertical);
		} catch (Exception e) {
			// System.out.println("No preauth for this intimation");
			e.printStackTrace();
			Notification.show("No preauth for this intimation");
		}

	}
	
	public void setFieldValues(Preauth preauth){
		
		MastersValue specialistType = preauth.getSpecialistType();
		if(specialistType != null){
			BeanItemContainer<MastersValue> specialistContainer = new BeanItemContainer<MastersValue>(MastersValue.class);
			specialistContainer.addBean(specialistType);
			cmbSpecialistType.setReadOnly(false);
			cmbSpecialistType.setContainerDataSource(specialistContainer);
			cmbSpecialistType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbSpecialistType.setItemCaptionPropertyId("value");
			
			cmbSpecialistType.setValue(specialistType);
			cmbSpecialistType.setReadOnly(true);
		}
		
		Long specialistConsulted = preauth.getSpecialistConsulted();
		MastersValue specialistConsulted2 = masterService.getSpecialistConsulted(specialistConsulted);
		if(specialistConsulted2 != null){
			BeanItemContainer<MastersValue> consultated = new BeanItemContainer<MastersValue>(MastersValue.class);
			consultated.addBean(specialistConsulted2);
			
			cmbSpecialistConsultant.setReadOnly(false);
			cmbSpecialistConsultant.setContainerDataSource(consultated);
			cmbSpecialistConsultant.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbSpecialistConsultant.setItemCaptionPropertyId("value");
			
			cmbSpecialistConsultant.setValue(specialistType);
			cmbSpecialistConsultant.setReadOnly(true);
		}
		
	}

	public void addListener() {

		viewPreauthButton.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				Window popup = new com.vaadin.ui.Window();
				popup.setCaption("");
				popup.setWidth("75%");
				popup.setHeight("95%");
				viewPreauthMoreDetailsPageObj = viewPreauthMoreDetailsPageInstance
						.get();
				viewPreauthMoreDetailsPageObj.init(bean, preauth,
						diagnosisProcedureValues);

				popup.setContent(viewPreauthMoreDetailsPageObj);
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
		});
		
		btnHospitalScroing.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				showScoringView();
			}
		});
		
		btnPCCRemarkView.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				showPCCRemarkView();
			}
		});

	}

	protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);

		return coordinatorValues;
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
	
	public void setpreauthTOPreauthDTO(PreMedicalMapper premedicalMapper,
			Claim claimByKey, Preauth previousPreauth, PreauthDTO preauthDTO,
			Boolean isEnabled) {
		if (claimByKey != null) {
			setClaimValuesToDTO(preauthDTO, claimByKey);
			NewIntimationDto newIntimationDto = intimationService
					.getIntimationDto(claimByKey.getIntimation());
			ClaimDto claimDTO =  ClaimMapper.getInstance().getClaimDto(claimByKey);
			preauthDTO.setNewIntimationDTO(newIntimationDto);
			preauthDTO.setClaimDTO(claimDTO);
		}
		/*String policyNumber = preauthDTO.getPolicyDto().getPolicyNumber();
		List<ViewTmpClaim> previousclaimsList = new ArrayList<ViewTmpClaim>();
		List<ViewTmpClaim> claimsByPolicyNumber = claimService
				.getViewTmpClaimsByPolicyNumber(policyNumber);
		Policy byPolicyNumber = policyService.getByPolicyNumber(policyNumber);
		previousclaimsList.addAll(claimsByPolicyNumber);
		
		previousclaimsList = getPreviousClaimForPreviousPolicy(byPolicyNumber.getRenewalPolicyNumber(), previousclaimsList);*/

//		List<PreviousClaimsTableDTO> previousClaimDTOList = SHAUtils
//				.getPreviousClaims(claimsByPolicyNumber,
//						claimByKey.getClaimId(), pedValidationService,
//						masterService);
		
		/*List<PreviousClaimsTableDTO> previousClaimDTOList = preauthService
				.getPreviousClaims(previousclaimsList,
						claimByKey.getClaimId());  */
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		List<PreviousClaimsTableDTO> previousClaimDTOList = dbCalculationService.getPreviousClaims(claimByKey.getKey(), claimByKey.getIntimation().getPolicy().getKey(), 
				claimByKey.getIntimation().getInsured().getKey(), SHAConstants.POLICY_WISE_SEARCH_TYPE);

		// List<PreviousClaimsTableDTO> previousClaimDTOList = new
		// PreviousClaimMapper().getPreviousClaimDTOList(claimsByPolicyNumber);

		preauthDTO.setPreviousClaimsList(previousClaimDTOList);

		if (previousPreauth.getCoordinatorFlag().equalsIgnoreCase("y")) {

			CoordinatorDTO coordinatorDTO = premedicalMapper
					.getCoordinatorDTO(preauthService
							.findCoordinatorByClaimKey(previousPreauth
									.getClaim().getKey()));
			coordinatorDTO.setRefertoCoordinator(true);
			preauthDTO.setCoordinatorDetails(coordinatorDTO);
		}

		List<SpecialityDTO> specialityDTOList = premedicalMapper
				.getSpecialityDTOList(preauthService
						.findSpecialityByClaimKey(previousPreauth.getClaim()
								.getKey()));
		for (SpecialityDTO specialityDTO : specialityDTOList) {
			specialityDTO.setEnableOrDisable(isEnabled);
		}
		preauthDTO.getPreauthDataExtractionDetails().setSpecialityList(
				specialityDTOList);

		List<ProcedureDTO> procedureMainDTOList = premedicalMapper
				.getProcedureMainDTOList(preauthService
						.findProcedureByPreauthKey(previousPreauth.getKey()));
		for (ProcedureDTO procedureDTO : procedureMainDTOList) {
			procedureDTO.setEnableOrDisable(isEnabled);
			
			if(procedureDTO.getApprovedAmount() != null && procedureDTO.getNetApprovedAmount() != null && !procedureDTO.getApprovedAmount().equals(procedureDTO.getNetApprovedAmount())) {
				preauthDTO.setIsReverseAllocation(true);
			}
			
			if (procedureDTO.getSublimitName() != null) {
				ClaimLimit limit = claimService.getClaimLimitByKey(procedureDTO
						.getSublimitName().getLimitId());
				procedureDTO.setSublimitName(getSublimitFunctionObj(limit));
				procedureDTO.setSublimitDesc(limit.getLimitDescription());
				procedureDTO.setSublimitAmount(limit.getMaxPerPolicyPeriod()
						.toString());
			}
			
			procedureDTO.setNetApprovedAmount(procedureDTO.getNetApprovedAmount());
		}

		preauthDTO.getPreauthMedicalProcessingDetails()
				.setProcedureExclusionCheckTableList(procedureMainDTOList);

		List<PedValidation> findPedValidationByPreauthKey = preauthService
				.findPedValidationByPreauthKey(previousPreauth.getKey());
		List<DiagnosisDetailsTableDTO> newPedValidationTableListDto = premedicalMapper
				.getNewPedValidationTableListDto(findPedValidationByPreauthKey);

		// Fix for issue 732 starts.
		//DBCalculationService dbCalculationService = new DBCalculationService();
		Double insuredSumInsured = 0d;
		if(null != preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey() &&
				!(ReferenceTable.getGPAProducts().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
			if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				insuredSumInsured = dbCalculationService.getInsuredSumInsuredForGMC(preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
						preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),preauthDTO.getNewIntimationDTO().getPolicy().getSectionCode());
			}else{
				 insuredSumInsured = dbCalculationService.getInsuredSumInsured(
							preauthDTO.getNewIntimationDTO().getInsuredPatient()
									.getInsuredId().toString(), preauthDTO.getPolicyDto()
									.getKey(),preauthDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
			}
		
		}
		else
		{
			
			insuredSumInsured = dbCalculationService.getGPAInsuredSumInsured(
					preauthDTO.getNewIntimationDTO().getInsuredPatient()
							.getInsuredId().toString(), preauthDTO.getPolicyDto()
							.getKey());
		}
		

		Double balanceSI = 0d;
		List<Double> copayValue = new ArrayList<Double>();
		if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			balanceSI = dbCalculationService.getBalanceSIForGMC(
					preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
					preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),
					preauthDTO.getClaimKey());
			copayValue = dbCalculationService.getProductCoPayForGMC(
					preauthDTO.getNewIntimationDTO().getPolicy().getKey(),preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey());
		}else{
			balanceSI = dbCalculationService.getBalanceSI(
					preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
					preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),
					preauthDTO.getClaimKey(), insuredSumInsured,
					preauthDTO.getNewIntimationDTO().getKey()).get(
					SHAConstants.TOTAL_BALANCE_SI);
			 copayValue = dbCalculationService.getProductCoPay(
					preauthDTO.getNewIntimationDTO().getPolicy().getProduct()
							.getKey(), preauthDTO.getNewIntimationDTO()
							.getInsuredPatient().getKey(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(),
							preauthDTO.getNewIntimationDTO());
		}
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
						.getInsuredPatient().getInsuredAge(),preauthDTO);
		// Fix for issue 732 ends

		for (DiagnosisDetailsTableDTO pedValidationTableDTO : newPedValidationTableListDto) {
			pedValidationTableDTO.setEnableOrDisable(isEnabled);
			
			pedValidationTableDTO.setNetApprovedAmount(pedValidationTableDTO.getNetApprovedAmount());
			
			if (pedValidationTableDTO.getDiagnosisName() != null) {
				String diagnosis = masterService
						.getDiagnosis(pedValidationTableDTO.getDiagnosisName()
								.getId());
				pedValidationTableDTO.setDiagnosis(diagnosis);
				pedValidationTableDTO.getDiagnosisName().setValue(diagnosis);
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
		preauthDTO.getPreauthDataExtractionDetails().setDiagnosisTableList(
				newPedValidationTableListDto);

		// preauthDTO.getPreauthMedicalProcessingDetails().setPedValidationTableList(newPedValidationTableListDto);

		List<ClaimAmountDetails> findClaimAmountDetailsByPreauthKey = preauthService
				.findClaimAmountDetailsByPreauthKey(previousPreauth.getKey());
		Float amountConsider = 0.0f;
		if (findClaimAmountDetailsByPreauthKey != null) {
			for (ClaimAmountDetails claimAmountDetails : findClaimAmountDetailsByPreauthKey) {
				amountConsider += claimAmountDetails.getPaybleAmount();
			}
		}
		preauthDTO
				.setAmountConsidered(String.valueOf(amountConsider.intValue()));
		preauthDTO
				.getPreauthDataExtractionDetails()
				.setClaimedDetailsList(
						premedicalMapper
								.getClaimedAmountDetailsDTOList(findClaimAmountDetailsByPreauthKey));
		
		Integer sumInsured = preauthService.getSumInsured(preauthDTO
				.getPolicyDto().getProduct().getKey(),
				(insuredSumInsured == 0) ? preauthDTO.getPolicyDto()
						.getTotalSumInsured() : insuredSumInsured);
		
		
		preauthDTO.getPolicyDto().setInsuredSumInsured(insuredSumInsured);
		String strAutoRestorationFlg = preauthDTO.getNewIntimationDTO()
				.getPolicy().getProduct().getAutoRestorationFlag();
			
		
		/*TreatingDoctorDetails Populate CR2019211*/
		List<TreatingDoctorDetails> findDoctorDetailsByClaimKey = preauthService.findTreatingDoctorDetailsByTransactionKey(previousPreauth.getKey());
		if(findDoctorDetailsByClaimKey !=null && !findDoctorDetailsByClaimKey.isEmpty()){
			List<TreatingDoctorDTO> TreatingDoctorDTOList = premedicalMapper.gettreatingDoctorDTOList(findDoctorDetailsByClaimKey);
			preauthDTO.getPreauthDataExtractionDetails().setTreatingDoctorDTOs(TreatingDoctorDTOList);
		}
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
		
		/*Negotiation Details */
		if(previousPreauth.getKey() !=null){
			NegotiationAmountDetails negotiationAmountDetails = preauthService.getNegotiationAmountDetailsByTransactionKey(previousPreauth.getKey());
			if(negotiationAmountDetails !=null
					&& negotiationAmountDetails.getNegotiationWith() !=null){
				preauthDTO.getPreauthMedicalDecisionDetails().setNegotiationWith(negotiationAmountDetails.getNegotiationWith());
			}
		}


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

	private SublimitFunObject getSublimitFunctionObj(ClaimLimit limit) {
		SublimitFunObject obj = new SublimitFunObject();
		obj.setLimitId(limit.getKey());
		obj.setAmount(Double.valueOf(limit.getMaxPerClaimAmount()));
		obj.setName(limit.getLimitName());
		obj.setDescription(limit.getLimitDescription());
		return obj;
	}

	private Map<Long, SublimitFunObject> getSublimitFunObjMap(Long productKey,
			Double insuredSumInsured, Double insuredAge,PreauthDTO preauthDTO) {
		
		Preauth preauth = null;
		
		if(preauthDTO.getKey() != null){
			List<Preauth> preauthList = preauthService.getPreauthByKey(preauthDTO.getKey());
			if(preauthList != null && ! preauthList.isEmpty()){
				preauth = preauthList.get(0);
				}
		}
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		Map<Long, SublimitFunObject> sublimitFunMap = new HashMap<Long, SublimitFunObject>();
		
		List<SublimitFunObject> sublimitList = new ArrayList<SublimitFunObject>();
		
		if(preauth != null && preauth.getSectionCategory() != null){
			
			sublimitList = dbCalculationService
					.getClaimedAmountDetailsForSection(productKey, insuredSumInsured, 0l,
							insuredAge,preauth.getSectionCategory(),preauthDTO.getPolicyDto().getPolicyPlan() != null ? preauthDTO.getPolicyDto().getPolicyPlan() : "0", null,preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId());
		}else{
			
			sublimitList = dbCalculationService
					.getClaimedAmountDetailsForSection(productKey, insuredSumInsured, 0l,
							insuredAge,0l, preauthDTO.getPolicyDto().getPolicyPlan() != null ? preauthDTO.getPolicyDto().getPolicyPlan() : "0", null,preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId());
			
		}
		
		if (null != sublimitList && !sublimitList.isEmpty()) {
			for (SublimitFunObject sublimitFunObj : sublimitList) {
				sublimitFunMap.put(sublimitFunObj.getLimitId(), sublimitFunObj);
			}
		}
		return sublimitFunMap;

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
				field.setWidth("50%");
				field.setNullRepresentation("-");
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			} else if (c instanceof CheckBox) {
				CheckBox field = (CheckBox) c;
				field.setReadOnly(true);
			} else if (c instanceof OptionGroup) {
				OptionGroup field = (OptionGroup) c;
				field.setReadOnly(false);
			}
		}
	}

	public void setValueToMedicalDecisionValues() {
		Double sublimitPackageAmount = 0d;
		this.diagnosisProcedureValues.clear();
		List<DiagnosisProcedureTableDTO> filledDTO = this.bean
				.getPreauthMedicalDecisionDetails()
				.getMedicalDecisionTableDTO();

		List<DiagnosisProcedureTableDTO> medicalDecisionDTOList = new ArrayList<DiagnosisProcedureTableDTO>();
		if (filledDTO.isEmpty()) {
			List<DiagnosisDetailsTableDTO> pedValidationTableList = this.bean
					.getPreauthDataExtractionDetails().getDiagnosisTableList();
			for (DiagnosisDetailsTableDTO pedValidationTableDTO : pedValidationTableList) {
				DiagnosisProcedureTableDTO dto = new DiagnosisProcedureTableDTO();
				
				if(this.bean.getNewIntimationDTO().getPolicy().getProductType().getKey().intValue() != 2904 ||
						this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CRITICARE_OTHER_BANKS)){
					
					dto.setPedImpactOnDiagnosis(pedValidationTableDTO.getPedImpactOnDiagnosis() != null && pedValidationTableDTO.getPedImpactOnDiagnosis().getValue() != null ? pedValidationTableDTO.getPedImpactOnDiagnosis().getValue() : "");
					dto.setNotPayingReason(pedValidationTableDTO.getReasonForNotPaying() != null && pedValidationTableDTO.getReasonForNotPaying().getValue() != null ? pedValidationTableDTO.getReasonForNotPaying().getValue() : "");
					dto.setReasonForNotPaying(pedValidationTableDTO.getReasonForNotPaying() != null ? pedValidationTableDTO.getReasonForNotPaying() : null);
				}
				
				if (pedValidationTableDTO.getConsiderForPaymentFlag() != null) {
					Boolean isPaymentAvailable = pedValidationTableDTO
							.getConsiderForPaymentFlag().toLowerCase()
							.equalsIgnoreCase("y") ? true : false;
					if (isPaymentAvailable) {
						List<PedDetailsTableDTO> pedList = pedValidationTableDTO
								.getPedList();
						if (!pedList.isEmpty()) {
							
							for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {
                                if(pedDetailsTableDTO.getExclusionDetails() != null){
								exclusionValues(pedDetailsTableDTO
										.getExclusionDetails().getId());

								pedDetailsTableDTO
										.setExclusionAllDetails(this.exlusionContainer
												.getItemIds());

								List<ExclusionDetails> exclusionAllDetails = pedDetailsTableDTO
										.getExclusionAllDetails();
								String paymentFlag = "y";
								for (ExclusionDetails exclusionDetails : exclusionAllDetails) {
									if (null != pedDetailsTableDTO
											.getExclusionDetails()
											&& exclusionDetails
													.getKey()
													.equals(pedDetailsTableDTO
															.getExclusionDetails()
															.getId())) {
										paymentFlag = exclusionDetails
												.getPaymentFlag();
									}
								}

								if (paymentFlag.toLowerCase().equalsIgnoreCase(
										"n")) {
									isPaymentAvailable = false;
									break;
								}
							}
							}
						}
					}

					if (!isPaymentAvailable) {
						dto.setMinimumAmount(0);
						dto.setConsiderForPayment("No");
					} else {
						dto.setConsiderForPayment("Yes");
					}
					dto.setIsPaymentAvailable(isPaymentAvailable);
				}
				if (pedValidationTableDTO.getSumInsuredRestriction() == null) {
					dto.setRestrictionSI("NA");
				} else {
					dto.setRestrictionSI(SHAUtils.getIntegerFromString(
							pedValidationTableDTO.getSumInsuredRestriction()
									.getValue()).toString());
				}

				if (pedValidationTableDTO.getSublimitName() == null) {
					dto.setSubLimitAmount("NA");
				} else {
					dto.setSubLimitAmount(SHAUtils.getIntegerFromString(
							pedValidationTableDTO.getSublimitAmt()).toString());
				}
				dto.setPackageAmt("NA");
				dto.setDiagnosisDetailsDTO(pedValidationTableDTO);
				
				if(pedValidationTableDTO.getCopayPercentage() != null) {
					SelectValue value = new SelectValue();
					value.setId(pedValidationTableDTO.getCopayPercentage().longValue());
					value.setValue(pedValidationTableDTO.getCopayPercentage().toString());
					dto.setCoPayPercentage(value);
				}
				
				dto.setIsAmbChargeFlag(pedValidationTableDTO.getIsAmbChargeFlag());
				dto.setAmbulanceCharge(pedValidationTableDTO.getAmbulanceCharge());
				dto.setAmtWithAmbulanceCharge(pedValidationTableDTO.getAmtWithAmbulanceCharge());

				medicalDecisionDTOList.add(dto);
			}
			
		
			List<ProcedureDTO> procedureExclusionCheckTableList = this.bean
					.getPreauthMedicalProcessingDetails()
					.getProcedureExclusionCheckTableList();
			for (ProcedureDTO procedureDTO : procedureExclusionCheckTableList) {
				DiagnosisProcedureTableDTO dto = new DiagnosisProcedureTableDTO();
				dto.setProcedureDTO(procedureDTO);
				if (procedureDTO.getConsiderForPaymentFlag() != null) {
					Boolean isPaymentAvailable = procedureDTO
							.getConsiderForPaymentFlag().toLowerCase()
							.equalsIgnoreCase("y") ? true : false;
					if (!isPaymentAvailable) {
						dto.setMinimumAmount(0);
						dto.setConsiderForPayment("No");
					} else {
						dto.setConsiderForPayment("Yes");
					}
					dto.setIsPaymentAvailable(isPaymentAvailable);
				}
				dto.setRestrictionSI("NA");

				dto.setPackageAmt("NA");

				if (procedureDTO.getSublimitName() == null) {
					dto.setSubLimitAmount("NA");
				} else {
					dto.setSubLimitAmount(SHAUtils.getIntegerFromString(
							procedureDTO.getSublimitAmount()).toString());
				}
				
				if(procedureDTO.getCopayPercentage() != null) {
					SelectValue value = new SelectValue();
					value.setId(procedureDTO.getCopayPercentage().longValue());
					value.setValue(procedureDTO.getCopayPercentage().toString());
					dto.setCoPayPercentage(value);
				}
				
				dto.setIsAmbChargeFlag(procedureDTO.getIsAmbChargeFlag());
				dto.setAmbulanceCharge(procedureDTO.getAmbulanceCharge());
				dto.setAmtWithAmbulanceCharge(procedureDTO.getAmtWithAmbulanceCharge());
				
				medicalDecisionDTOList.add(dto);
			}
			
			

			Map<String, Object> caluculationInputValues = new HashMap<String, Object>();
			caluculationInputValues.put("policyNumber", this.bean
					.getPolicyDto().getPolicyNumber());
			// caluculationInputValues.put("insuredId",
			// this.bean.getPolicyDto().getInsuredId());
			caluculationInputValues.put("insuredId", this.bean
					.getNewIntimationDTO().getInsuredPatient().getInsuredId());

			DBCalculationService dbCalculationService = new DBCalculationService();
			Double insuredSumInsured = dbCalculationService
					.getInsuredSumInsured(this.bean.getNewIntimationDTO()
							.getInsuredPatient().getInsuredId().toString(),
							this.bean.getPolicyDto().getKey(),this.bean.getNewIntimationDTO()
							.getInsuredPatient().getLopFlag());
			caluculationInputValues.put(
					"policySI",
					insuredSumInsured != 0 ? String.valueOf(insuredSumInsured)
							: String.valueOf(this.bean.getPolicyDto()
									.getTotalSumInsured()));

			if (null != medicalDecisionDTOList
					&& medicalDecisionDTOList.size() > 0) {
				int diag = 1;
				int proc = 1;
				int diagCount = 0;
				int proCount = 0;
				for (int i = 0; i < medicalDecisionDTOList.size(); i++) {
					DiagnosisProcedureTableDTO medicalDecisionDto = medicalDecisionDTOList
							.get(i);
					medicalDecisionDto.setIsEnabled(false);
					// if(medicalDecisionDto.getDiagnosisDetailsDTO() != null) {
					// medicalDecisionDto.setDiagOrProcedure("Diag "+(diag++));
					// caluculationInputValues.put("restrictedSI",
					// medicalDecisionDto.getDiagnosisDetailsDTO().getSumInsuredRestriction()
					// != null ?
					// medicalDecisionDto.getDiagnosisDetailsDTO().getSumInsuredRestriction().getValue()
					// : null);
					// caluculationInputValues.put("sublimitId",
					// medicalDecisionDto.getDiagnosisDetailsDTO().getSublimitName()
					// != null ?
					// medicalDecisionDto.getDiagnosisDetailsDTO().getSublimitName().getLimitId()
					// : null);
					// caluculationInputValues.put("diagOrProcId",
					// medicalDecisionDto.getDiagnosisDetailsDTO().getKey() ==
					// null ? 0l :
					// medicalDecisionDto.getDiagnosisDetailsDTO().getKey());
					// caluculationInputValues.put("diagnosisId",
					// medicalDecisionDto.getDiagnosisDetailsDTO().getDiagnosisName().getId().toString());
					// caluculationInputValues.put("referenceFlag", "D" );
					// } else if(medicalDecisionDto.getProcedureDTO() != null) {
					// medicalDecisionDto.setDiagOrProcedure("Proc " +
					// (proc++));
					// caluculationInputValues.put("restrictedSI", null );
					// caluculationInputValues.put("sublimitId",
					// medicalDecisionDto.getProcedureDTO().getSublimitName() !=
					// null ?
					// medicalDecisionDto.getProcedureDTO().getSublimitName().getLimitId()
					// : null);
					// caluculationInputValues.put("diagOrProcId",
					// medicalDecisionDto.getProcedureDTO().getKey() == null ?
					// 0l : medicalDecisionDto.getProcedureDTO().getKey());
					// caluculationInputValues.put("referenceFlag", "P" );
					// }
					if (medicalDecisionDto.getDiagnosisDetailsDTO() != null) {
						medicalDecisionDto.setDiagOrProcedure("Diag "
								+ (diag++));
						caluculationInputValues
								.put("restrictedSI",
										medicalDecisionDto
										.getDiagnosisDetailsDTO()
										.getSumInsuredRestriction() != null ? medicalDecisionDto
										.getDiagnosisDetailsDTO()
										.getSumInsuredRestriction().getId()
										: null);
						caluculationInputValues
						.put("restrictedSIAmount",
								medicalDecisionDto
										.getDiagnosisDetailsDTO()
										.getSumInsuredRestriction() != null ? medicalDecisionDto
										.getDiagnosisDetailsDTO()
										.getSumInsuredRestriction().getValue()
										: null);
						caluculationInputValues
								.put("sublimitId",
										medicalDecisionDto
												.getDiagnosisDetailsDTO()
												.getSublimitName() != null ? medicalDecisionDto
												.getDiagnosisDetailsDTO()
												.getSublimitName().getLimitId()
												: null);
						caluculationInputValues.put("diagOrProcId",
								medicalDecisionDto.getDiagnosisDetailsDTO()
										.getDiagnosisName() == null ? 0l
										: medicalDecisionDto
												.getDiagnosisDetailsDTO()
												.getDiagnosisName().getId());
						caluculationInputValues.put("diagnosisId",
								medicalDecisionDto.getDiagnosisDetailsDTO()
										.getDiagnosisName() == null ? "0"
										: medicalDecisionDto
												.getDiagnosisDetailsDTO()
												.getDiagnosisName().getId()
												.toString());
						caluculationInputValues.put("referenceFlag", "D");
						if (caluculationInputValues.get("sublimitId") != null) {
							Long sublimitId = (Long) caluculationInputValues
									.get("sublimitId");
							ClaimLimit claimLimit = diagnosisService
									.getClaimLimit(sublimitId);
							medicalDecisionDto.setSublimitName(claimLimit
									.getLimitName());
							medicalDecisionDto.setSublimitApplicable("Yes");
						} else {
							medicalDecisionDto.setSublimitApplicable("No");
						}
						

					} else if (medicalDecisionDto.getProcedureDTO() != null) {
						medicalDecisionDto.setDiagOrProcedure("Proc "
								+ (proc++));
						caluculationInputValues.put("restrictedSI", null);
						caluculationInputValues.put("restrictedSIAmount", null);
						caluculationInputValues
								.put("sublimitId",
										medicalDecisionDto.getProcedureDTO()
												.getSublimitName() != null ? medicalDecisionDto
												.getProcedureDTO()
												.getSublimitName().getLimitId()
												: null);
						caluculationInputValues
								.put("diagOrProcId",
										medicalDecisionDto.getProcedureDTO()
												.getProcedureName() == null ? 0l
												: medicalDecisionDto
														.getProcedureDTO()
														.getProcedureName()
														.getId() == null ? 0l
														: medicalDecisionDto
																.getProcedureDTO()
																.getProcedureName()
																.getId());
						caluculationInputValues.put("referenceFlag", "P");
						if (null != this.bean
								.getPreauthMedicalProcessingDetails()
								.getProcedureExclusionCheckTableList()
								&& !this.bean
										.getPreauthMedicalProcessingDetails()
										.getProcedureExclusionCheckTableList()
										.isEmpty()) {
							Double amountConsidered = this.bean
									.getPreauthMedicalProcessingDetails()
									.getProcedureExclusionCheckTableList()
									.get(proCount).getAmountConsideredAmount();
							medicalDecisionDto
									.getProcedureDTO()
									.setAmountConsideredAmount(amountConsidered);
							medicalDecisionDto
									.setAmountConsidered(amountConsidered != null ? amountConsidered
											.intValue() : 0);
							
							
							
							
							
//							Double minimumAmount = this.bean
//									.getPreauthMedicalProcessingDetails()
//									.getProcedureExclusionCheckTableList()
//									.get(proCount).getApprovedAmount();
//							medicalDecisionDto.getProcedureDTO()
//									.setMinimumAmount(minimumAmount);
//							if (minimumAmount != null) {
//								sublimitPackageAmount += minimumAmount;
//								medicalDecisionDto
//										.setMinimumAmount(minimumAmount
//												.intValue());
//							}
							
							Double copayAmount = this.bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList().get(proCount).getCopayAmount();
							if(copayAmount != null){
							medicalDecisionDto.setCoPayAmount(copayAmount.intValue());
							}
							
							Double copayPercentage = this.bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList().get(proCount).getCopayPercentage();
							if(copayPercentage != null){
								medicalDecisionDto.setCopayPercentageAmount(copayPercentage.doubleValue());
							}

							
							Double netAmount = this.bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList().get(proCount).getNetAmount();
							if(netAmount != null){
								medicalDecisionDto.setNetAmount(netAmount.intValue());
							}
							
							Double netApprovedAmount = this.bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList().get(proCount).getNetApprovedAmount();
							medicalDecisionDto.setReverseAllocatedAmt(netApprovedAmount != null ? netApprovedAmount.intValue() : 0);
							
							Double approvedAmount = this.bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList().get(proCount).getApprovedAmount();
							if(approvedAmount != null){
								medicalDecisionDto.setMinimumAmount(approvedAmount.intValue());
								sublimitPackageAmount += approvedAmount;
							}
							
							medicalDecisionDto.setIsAmbChargeFlag(this.bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList().get(proCount).getIsAmbChargeFlag());
							medicalDecisionDto.setIsAmbChargeApplicable(this.bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList().get(proCount).getIsAmbChargeApplicable());
							medicalDecisionDto.setAmbulanceCharge(this.bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList().get(proCount).getAmbulanceCharge());
							medicalDecisionDto.setAmtWithAmbulanceCharge(this.bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList().get(proCount).getAmtWithAmbulanceCharge());

							proCount++;
						}
						if (caluculationInputValues.get("sublimitId") != null) {
							Long sublimitId = (Long) caluculationInputValues
									.get("sublimitId");
							ClaimLimit claimLimit = diagnosisService
									.getClaimLimit(sublimitId);
							medicalDecisionDto.setSublimitName(claimLimit
									.getLimitName());
							medicalDecisionDto.setSublimitApplicable("Yes");
						} else {
							medicalDecisionDto.setSublimitApplicable("No");
						}
					}
					if(previousPreauth != null){
					caluculationInputValues.put("preauthKey",
							/*previousPreauth.getKey()*/0l);
					}else{
						caluculationInputValues.put("preauthKey",
								this.bean.getPreviousPreauthKey());
					}
					
					caluculationInputValues.put(SHAConstants.CLAIM_KEY, this.bean.getClaimDTO().getKey());
					
					sumInsuredCalculation(caluculationInputValues);
					Map<String, Object> values = this.sublimitCalculatedValues;

					medicalDecisionDto.setRestrictionSI(caluculationInputValues
							.get("restrictedSIAmount") != null ? SHAUtils
							.getIntegerFromString(
									(String) caluculationInputValues
											.get("restrictedSIAmount")).toString()
							: "NA");
					if (values.get("restrictedAvailAmt") != null) {
						medicalDecisionDto.setAvailableAmout(((Double) values
								.get("restrictedAvailAmt")).intValue());
					}
					medicalDecisionDto.setUtilizedAmt(((Double) values
							.get("restrictedUtilAmt")).intValue());
					medicalDecisionDto.setSubLimitAmount(((Double) values
							.get("currentSL")).intValue() > 0 ? (String
							.valueOf(((Double) values.get("currentSL"))
									.intValue())) : "NA");
					medicalDecisionDto.setSubLimitUtilAmount(((Double) values
							.get("SLUtilAmt")).intValue());
					medicalDecisionDto.setSubLimitAvaliableAmt(((Double) values
							.get("SLAvailAmt")).intValue());
					medicalDecisionDto
							.setCoPayPercentageValues((List<String>) values
									.get("copay"));

					if (medicalDecisionDto.getDiagnosisDetailsDTO() != null) {
						medicalDecisionDto.getDiagnosisDetailsDTO()
								.setDiagnosis(this.diagnosisName);
						if (null != this.bean.getPreauthDataExtractionDetails()
								.getDiagnosisTableList()
								&& !this.bean.getPreauthDataExtractionDetails()
										.getDiagnosisTableList().isEmpty()) {
							Double amountConsidered = this.bean
									.getPreauthDataExtractionDetails()
									.getDiagnosisTableList().get(diagCount)
									.getAmountConsideredAmount();
							if(amountConsidered != null){
								medicalDecisionDto
										.getDiagnosisDetailsDTO()
										.setAmountConsideredAmount(amountConsidered);
								medicalDecisionDto
										.setAmountConsidered(amountConsidered
												.intValue());
							}
							
							
							Double copayAmount = this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList().get(diagCount).getCopayAmount();
							if(copayAmount != null){
							medicalDecisionDto.setCoPayAmount(copayAmount.intValue());
							
							}
							
							Double copayPercentage = this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList().get(diagCount).getCopayPercentage();
							if(copayPercentage != null){
								medicalDecisionDto.setCopayPercentageAmount(copayPercentage.doubleValue());
							}

							Double netAmount = this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList().get(diagCount).getNetAmount();
							if(netAmount != null){
								medicalDecisionDto.setNetAmount(netAmount.intValue());
							}
							
							Double netApprovedAmount = this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList().get(diagCount).getNetApprovedAmount();
							medicalDecisionDto.setReverseAllocatedAmt(netApprovedAmount != null ? netApprovedAmount.intValue() : 0);
							
							Double approvedAmount = this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList().get(diagCount).getApprovedAmount();
							if(approvedAmount != null){
								medicalDecisionDto.setMinimumAmount(approvedAmount.intValue());
								sublimitPackageAmount += approvedAmount;
								
							}
							
							medicalDecisionDto.setIsAmbChargeFlag(this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList().get(diagCount).getIsAmbChargeFlag());
							medicalDecisionDto.setIsAmbChargeApplicable(this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList().get(diagCount).getIsAmbChargeApplicable());
							medicalDecisionDto.setAmbulanceCharge(this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList().get(diagCount).getAmbulanceCharge());
							medicalDecisionDto.setAmtWithAmbulanceCharge(this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList().get(diagCount).getAmtWithAmbulanceCharge());

//							Double minimumAmount = this.bean
//									.getPreauthDataExtractionDetails()
//									.getDiagnosisTableList().get(diagCount)
//									.getMinimumAmount();
//							medicalDecisionDto.getDiagnosisDetailsDTO()
//									.setMinimumAmount(minimumAmount);
//							if (minimumAmount != null) {
//								sublimitPackageAmount += minimumAmount;
//								medicalDecisionDto
//										.setMinimumAmount(minimumAmount
//												.intValue());
//							}
							diagCount++;
						}
					}

					// need to implement in new medical listener table
					medicalDecisionDto.setIsEnabled(false);
					this.diagnosisListenerTableObj
							.addBeanToList(medicalDecisionDto);
					this.diagnosisProcedureValues.add(medicalDecisionDto);
				}
				DiagnosisProcedureTableDTO dto = new DiagnosisProcedureTableDTO();
				dto.setDiagOrProcedure("Residual Treatment / Procedure Amount");
				dto.setPackageAmt("NA");
				dto.setSubLimitAmount("NA");
				dto.setRestrictionSI("NA");
				if (null != this.bean.getResidualAmountDTO()) {
					if (null != this.bean.getResidualAmountDTO()
							.getAmountConsideredAmount()) {
						dto.setAmountConsidered(this.bean
								.getResidualAmountDTO()
								.getAmountConsideredAmount().intValue());
						if(this.bean.getResidualAmountDTO().getCopayAmount() != null){
						dto.setCoPayAmount(this.bean.getResidualAmountDTO().getCopayAmount().intValue());
						}
						
						if(this.bean.getResidualAmountDTO().getNetAmount() != null){
							dto.setNetAmount(this.bean.getResidualAmountDTO().getNetAmount().intValue());
						}
						
						if(this.bean.getResidualAmountDTO().getApprovedAmount() != null){
						dto.setMinimumAmount(this.bean.getResidualAmountDTO()
								.getApprovedAmount().intValue());
						sublimitPackageAmount += this.bean
								.getResidualAmountDTO().getApprovedAmount();
						}
						
						Double copayPercentage = this.bean.getResidualAmountDTO().getCopayPercentage();
						if(copayPercentage != null){
							dto.setCopayPercentageAmount(copayPercentage.doubleValue());
						}
						
						Double netApprovedAmount = this.bean.getResidualAmountDTO().getNetApprovedAmount();
						
						dto.setReverseAllocatedAmt(netApprovedAmount != null ? netApprovedAmount.intValue() : 0);
						
						if (this.bean.getResidualAmountDTO().getMinimumAmount() != null) {
//							sublimitPackageAmount += this.bean
//									.getResidualAmountDTO().getMinimumAmount();
						}
					}
				}
				dto.setIsEnabled(false);
				this.diagnosisListenerTableObj.addBeanToList(dto);
				this.diagnosisProcedureValues.add(dto);
			}
		} else {
			this.diagnosisListenerTableObj.addList(filledDTO);
		}

		this.bean.getPreauthMedicalDecisionDetails().setInitialApprovedAmt(
				sublimitPackageAmount);

	}

	public void exclusionValues(Long impactDiagnosisKey) {

		BeanItemContainer<ExclusionDetails> icdCodeContainer = masterService
				.getExclusionDetailsByImpactKey(impactDiagnosisKey);
		this.exlusionContainer = icdCodeContainer;

	}

	public void sumInsuredCalculation(Map<String, Object> values) {

		String diagnosis = null;
		if (values.containsKey("diagnosisId")) {
			diagnosis = masterService.getDiagnosis(Long.valueOf((String) values
					.get("diagnosisId")));
		}

		Map<String, Object> medicalDecisionTableValue = dbCalculationService
				.getMedicalDecisionTableValue(values,bean.getNewIntimationDTO());
		this.diagnosisName = diagnosis;
		this.sublimitCalculatedValues = medicalDecisionTableValue;

	}

	private void getResidualAmount(Preauth previousPreauth,
			PreauthDTO preauthDTO) {
		ResidualAmount residualAmtByPreauthKey = preauthService
				.getLatestResidualAmtByPreauthKey(previousPreauth.getKey());
		if (null != residualAmtByPreauthKey) {
			ResidualAmountDTO residualDTO = new ResidualAmountDTO();
			residualDTO.setKey(residualAmtByPreauthKey.getKey());
			residualDTO.setPreauthKey(previousPreauth.getKey());
			residualDTO.setAmountConsideredAmount(residualAmtByPreauthKey
					.getAmountConsideredAmount());
			residualDTO.setCopayAmount(residualAmtByPreauthKey.getCopayAmount());
			residualDTO.setMinimumAmount(residualAmtByPreauthKey
					.getMinimumAmount());
			residualDTO.setNetAmount(residualAmtByPreauthKey.getNetAmount());
			residualDTO.setApprovedAmount(residualAmtByPreauthKey
					.getApprovedAmount());
			residualDTO.setNetApprovedAmount(residualAmtByPreauthKey.getNetApprovedAmount());
			residualDTO.setRemarks(residualAmtByPreauthKey.getRemarks());
			if(null != preauthDTO.getNewIntimationDTO().getIsJioPolicy() && preauthDTO.getNewIntimationDTO().getIsJioPolicy()){
				if(null != residualAmtByPreauthKey.getCoPayTypeId()){		
					SelectValue copayTypeValue = new SelectValue();
					copayTypeValue.setValue(residualAmtByPreauthKey.getCoPayTypeId().getValue());
					copayTypeValue.setId(residualAmtByPreauthKey.getCoPayTypeId().getKey());
					residualDTO.setCoPayTypeId(copayTypeValue);
				}
			}
			preauthDTO.setResidualAmountDTO(residualDTO);
		}

	}
	
	public Preauth getPreivousPreauth(Long claimKey,Long currentPreauthKey){
		List<Preauth> preauthByClaimKey = preauthService
				.getPreauthByClaimKey(claimKey);
		Integer nextReferenceNo = 2;
		Preauth previousPreauth = null;
		if (!preauthByClaimKey.isEmpty()) {
			previousPreauth = preauthByClaimKey.get(0);

			String[] split = previousPreauth.getPreauthId().split("/");
			String defaultNumber = split[split.length - 1];
			nextReferenceNo = Integer.valueOf(defaultNumber);
			for (Preauth preauth : preauthByClaimKey) {
				if (preauth.getPreauthId() != null) {
					String[] splitNumber = preauth.getPreauthId()
							.split("/");
					String number = splitNumber[splitNumber.length - 1];

					if (Integer.valueOf(number) > Integer
							.valueOf(defaultNumber)) {
						if(! preauth.getKey().equals(currentPreauthKey)){
						previousPreauth = preauth;
						
						nextReferenceNo = Integer.valueOf(number);
						}
					}
				}

			}
		}
		
		return previousPreauth;

	}

	@Override
	public void resetView() {

	}

	@Override
	public void setApprovedAmountField(Integer approvedAmount) {
		approvedAmtField.setValue(approvedAmount.toString());
	}

	public void showScoringView(){
		
		scoringAndPPDetailView.setIntimationNumber(bean.getNewIntimationDTO().getIntimationId());
		scoringAndPPDetailView.setScreenName("ViewPage");
		scoringAndPPDetailView.setIsoldppscoreview(false);
		scoringAndPPDetailView.init(false);
		/*hospitalScoringView.init(bean.getNewIntimationDTO().getIntimationId(), false);		
		hospitalScoringView.setScreenName("ViewPage");*/
		VerticalLayout misLayout = new VerticalLayout(scoringAndPPDetailView);
		Window popup = new com.vaadin.ui.Window();
		popup.setWidth("50%");
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
	
	public void showPCCRemarkView(){

		List<ViewPccRemarksDTO> pccRemarksDTOHistoryDetails = null;
		if(preauth != null && preauth.getClaim() !=null && preauth.getClaim().getKey() != null){
			pccRemarksDTOHistoryDetails =  preauthService.getPccRemarksDTOHistoryDetails(preauth.getClaim().getKey());
		}
		viewPostCashlessRemark.init("", false, false);
		viewPostCashlessRemark.setTableList(pccRemarksDTOHistoryDetails);
		VerticalLayout misLayout = new VerticalLayout(viewPostCashlessRemark);
		Window popup = new com.vaadin.ui.Window();
		popup.setWidth("45%");
		popup.setCaption("PCC Remarks Details");
		popup.setWidth("75%");
		popup.setHeight("75%");
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

    	if(exlusionContainer != null){
    		exlusionContainer=null;
    	}
    	SHAUtils.setClearReferenceData(sublimitCalculatedValues);
    	SHAUtils.setClearPreauthDTO(bean);
    	if(this.diagnosisListenerTableObj!=null){
    		this.diagnosisListenerTableObj.removeAllItems();
    		this.diagnosisListenerTableObj = null;
    	}
    	if(this.amountConsideredTable!=null){
    		this.amountConsideredTable = null;
    	}
    	
    	if(this.OpinionDetailsRolesTableTableObj!=null){
    		this.OpinionDetailsRolesTableTableObj.removeRow();
    		this.OpinionDetailsRolesTableTableObj = null;
    	}
    	/*searchService=null;
    	preauthService=null;
    	dbCalculationService=null;
    	diagnosisService=null;
    	intimationService=null;
    	claimService=null;
    	policyService=null;
    	masterService=null;
    	reimbursementService=null;*/
    	binder=null;
    	previousClaimBinder=null;
    	medicalDecisionBinder=null;
    	medicalProcessingBinder=null;
    	/*if(this.diagnosisProcedureValues!=null){
    		this.diagnosisProcedureValues = null;
    	}*/
    	if(this.viewPreauthMoreDetailsPageObj!=null){
    		this.viewPreauthMoreDetailsPageObj.setClearReferenceData();
    		this.viewPreauthMoreDetailsPageObj = null;
    	}
    	if(this.invsReviewRemarksTableObj!=null){
    		this.invsReviewRemarksTableObj=null;
    	}
    }
}

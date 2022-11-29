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
import com.shaic.arch.table.NewClaimedAmountTable;
import com.shaic.claim.intimation.create.dto.PolicyDto;
import com.shaic.claim.medical.opinion.OpinionValidationMapper;
import com.shaic.claim.medical.opinion.OpinionValidationService;
import com.shaic.claim.medical.opinion.OpinionValidationTableDTO;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.pedvalidation.view.ViewPedValidationService;
import com.shaic.claim.preauth.pedvalidation.view.ViewPedValidationTable;
import com.shaic.claim.preauth.pedvalidation.view.ViewPedValidationTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDataExtaractionDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthMedicalDecisionDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthPreviousClaimsDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.preauth.wizard.dto.SpecialityDTO;
import com.shaic.claim.preauth.wizard.dto.TreatingDoctorDTO;
import com.shaic.claim.preauth.wizard.pages.AmountConsideredTable;
import com.shaic.claim.premedical.dto.NoOfDaysCell;
import com.shaic.claim.premedical.dto.PreauthMedicalProcessingDTO;
import com.shaic.claim.procedureexclusioncheck.view.ViewProcedureExclusionCheckService;
import com.shaic.claim.procedureexclusioncheck.view.ViewProcedureExclusionCheckTable;
import com.shaic.claim.reports.negotiationreport.NegotiationAmountDetails;
import com.shaic.claim.reports.negotiationreport.NegotiationAmountDetailsDTO;
import com.shaic.claim.scoring.HospitalScoringDetailView;
import com.shaic.claim.scoring.ppcoding.ScoringAndPPDetailView;
import com.shaic.claim.viewEarlierRodDetails.Table.RevisedDiagnosisPreauthViewTable;
import com.shaic.claim.viewEarlierRodDetails.Table.ViewProcedureTable;
import com.shaic.claim.viewEarlierRodDetails.Table.ViewSpecialityTable;
import com.shaic.claim.viewEarlierRodDetails.Table.ViewTreatingDoctorTabel;
import com.shaic.domain.AssignedInvestigatiorDetails;
import com.shaic.domain.ClaimService;
import com.shaic.domain.FieldVisitRequestService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.OpinionValidation;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.TmpEmployee;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.paclaim.reimbursement.medicalapproval.processclaimrequest.pages.dataextraction.PAInvestigationReviewRemarksTable;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class ViewPreauthMoreDetailsPage extends AbstractMVPView implements PreauthMoreDetailsView {

	
	private static final long serialVersionUID = 1L;
	
	private PreauthDTO bean;
	
	private BeanFieldGroup<PreauthDataExtaractionDTO> binder;
	
	private BeanFieldGroup<PreauthPreviousClaimsDTO> previousClaimBinder;
	
	private BeanFieldGroup<PreauthMedicalDecisionDTO> medicalDecisionBinder;
	
	private BeanFieldGroup<PreauthMedicalProcessingDTO> medicalProcessingBinder; 
	
	@Inject
	private Instance<RevisedDiagnosisPreauthViewTable> diagnosisListenerTableInstance;
	
	private RevisedDiagnosisPreauthViewTable diagnosisListenerTableObj;
	
	@Inject
	private Instance<AmountConsideredTable> amountConsideredTableInstance;

	private AmountConsideredTable amountConsideredTable;
	
	private Map<String, Object> referenceData = new HashMap<String, Object>(); 
	
	@Inject
	private Instance<NewClaimedAmountTable> claimedAmountDetailsTable;
	
	private NewClaimedAmountTable claimedDetailsTableObj;
	
	@Inject
	private Instance<ViewSpecialityTable> specialityTableList;
	
	private ViewSpecialityTable specialityTableObj;
	
	@Inject
	private ViewPedValidationTable viewPedValidationTable;
	
	@Inject
	private ViewPedValidationService viewPedValidationService;
	
	@Inject
	private ViewProcedureExclusionCheckTable viewProcedureExclusionCheckTable;

	@Inject
	private ViewProcedureExclusionCheckService viewProcedureExclusionService;
	@EJB
	private ClaimService claimService;
	
	/*@Inject
	private Instance<ViewInvestigationRemarksTable> viewInvestigationReviewRemarksInstance;
	
	private ViewInvestigationRemarksTable viewInvestigationRemarksTable;*/
	
	
	private BeanItemContainer<ExclusionDetails> exlusionContainer;
	
	private String diagnosisName;
	
	private Map<String, Object> sublimitCalculatedValues;
	
	private TextField approvedAmtField;
	
	private TextField txtPreauthStatus;
	
	private TextField txtPreauthApprovedAmt;
	
	private TextArea txtMedicalRemarks;
	
	private TextField txtPreauthRemarks;
	
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
	
	private TextField txtFvrNotRequiredRemarks;
	
	private TextField txtAllocationTo;
	
	private TextArea txtFvrTriggerPoints;
	
	private TextField txtSpecialistType;
	
	private TextField txtSpecialistConsultant;
	
	private TextArea txtRemarksBySpecialist;
	
	private TextField txtInvestigatorName;
	
	private TextArea txtInvestigatorRemarks;
	
	private TextArea txtCPURemarks;
	
	private TextField txtPreauthReceivedDate;
	
	private TextField txtPreauthResponseTime;
	
	private TextField txtPreauthReferenceNo;
	
	private TextField txtPreauthapproField;
	
	private TextArea txtTreatmentRemarks;
	
	private OptionGroup acctDeath;
	
	private TextField dateAcctDeath;
	
	private TextArea txtWithdrawInternallRemarks;
	
	private TextArea txtNegotiationWith;
	
	private TextField txtPreAuthType;
	
	private TextArea txtTimeBasedIcdCodeExclusion;
	
	private TextArea txtDecisionChangeReason;
	
	private TextArea txtDecisionChangeRemarks;
	
	
	@Inject
	private Instance<ViewProcedureTable> procedureExclusionCheckTable;


//	@Inject
//	private ViewProcedureTable procedureExclusionCheckTable;
	
	@Inject
	private ViewProcedureTable procedureExclusionCheckTableObj;
	
	
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private HospitalService hospitalService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	@EJB
	private FieldVisitRequestService fieldVisitRequestService;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	private Map<String, Object> instanceMapper;
	
	private TextField txtAmtClaimed;
	
	private TextField txtDiscntHospBill;
	
	private TextField txtNetAmt;
	
	@Inject
	private Instance<ViewPreauthMoreOpinionDetailsRolesTable> opinionDetailsRolesTable;
	
	private ViewPreauthMoreOpinionDetailsRolesTable OpinionDetailsRolesTableTableObj;
	@EJB
	private OpinionValidationService searchService;
	
	private Button btnHospitalScroing;
	
	@Inject
	private HospitalScoringDetailView hospitalScoringView;
	
	@Inject
	private Instance<ViewTreatingDoctorTabel> treatingDoctorTableList;
	
	private ViewTreatingDoctorTabel treatingDoctorTableObj;
	
	@Inject
	private ScoringAndPPDetailView scoringAndPPDetailView;
	private TextField txtTypeOfAdmission;
	
	@Inject
	private Instance<PAInvestigationReviewRemarksTable> invsReviewRemarksTableInstance;
	
    private PAInvestigationReviewRemarksTable invsReviewRemarksTableObj;
	
	public void init(PreauthDTO bean,Preauth preauth, List<DiagnosisProcedureTableDTO> diagnosisProcedureList){
		
		List<DiagnosisProcedureTableDTO> diagnosisProcedureValues = new ArrayList<DiagnosisProcedureTableDTO>();
		diagnosisProcedureValues.addAll(diagnosisProcedureList);
		
//		String policyAgeing = dbCalculationService.getPolicyAgeing(bean.getDateOfAdmission(), bean.getPolicyDto().getPolicyNumber());
		String policyAgeing = "";
		if(bean.getNewIntimationDTO() != null){
			policyAgeing = bean.getNewIntimationDTO().getPolicyYear();
		}
		/*Negotiation Details */
		if(bean.getKey() !=null){
			NegotiationAmountDetails negotiationAmountDetails = preauthService.getNegotiationAmountDetailsByTransactionKey(bean.getKey());
			if(negotiationAmountDetails !=null
					&& negotiationAmountDetails.getNegotiationWith() !=null){
				bean.getPreauthMedicalDecisionDetails().setNegotiationWith(negotiationAmountDetails.getNegotiationWith());
			}
		}
		
        this.bean = bean;
        
        txtPreauthReferenceNo = new TextField("Pre-auth Reference No");
		txtPreauthReferenceNo.setValue(preauth.getPreauthId());
		txtPreauthReferenceNo.setWidth("500px");
		txtPreauthReferenceNo.setReadOnly(true);
		
		FormLayout referenceNoForm = new FormLayout(txtPreauthReferenceNo);
		
		this.binder = new BeanFieldGroup<PreauthDataExtaractionDTO>(
				PreauthDataExtaractionDTO.class);
		this.binder.setItemDataSource(this.bean
				.getPreauthDataExtractionDetails());
		
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		this.medicalDecisionBinder = new BeanFieldGroup<PreauthMedicalDecisionDTO>(
				PreauthMedicalDecisionDTO.class);
		this.medicalDecisionBinder.setItemDataSource(this.bean
				.getPreauthMedicalDecisionDetails());
		this.medicalDecisionBinder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		this.medicalProcessingBinder = new BeanFieldGroup<PreauthMedicalProcessingDTO>(
				PreauthMedicalProcessingDTO.class);
		this.medicalProcessingBinder.setItemDataSource(this.bean
				.getPreauthMedicalProcessingDetails());
		this.medicalProcessingBinder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		this.previousClaimBinder = new BeanFieldGroup<PreauthPreviousClaimsDTO>(
				PreauthPreviousClaimsDTO.class);
		this.previousClaimBinder.setItemDataSource(this.bean
				.getPreauthPreviousClaimsDetails());
		this.previousClaimBinder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		setReferenceData();
		
		txtPreauthReceivedDate = new TextField("Pre-auth Recieved Date & Time");
		txtPreauthResponseTime = new TextField("Pre-auth Response Date & Time");
		
		txtPreauthStatus = new TextField("Status");
		txtPreauthStatus.setValue(this.bean.getStatusValue());
		txtPreauthApprovedAmt = (TextField) binder.buildAndBind("Pre-auth Approved Amt", "totalApprAmt", TextField.class);
		txtMedicalRemarks = (TextArea) medicalProcessingBinder.buildAndBind("Medical Remarks", "medicalRemarks", TextArea.class);
		txtMedicalRemarks.setValidationVisible(false);
		String medicalRemarks = preauth.getMedicalRemarks();
		
		txtMedicalRemarks.setValue(medicalRemarks);
		txtMedicalRemarks.setDescription(medicalRemarks);
		
		//added for CR R1180
		txtWithdrawInternallRemarks = (TextArea) medicalDecisionBinder.buildAndBind("Withdrawal Internal Remarks", "withdrawInternalRemarks", TextArea.class);
		txtWithdrawInternallRemarks.setValidationVisible(false);
		
         String withdrawInternallRemarks = preauth.getWithdrawInternalRemarks();
		
         txtWithdrawInternallRemarks.setValue(withdrawInternallRemarks);
         txtWithdrawInternallRemarks.setDescription(withdrawInternallRemarks);
		
		txtPreauthRemarks = (TextField) medicalDecisionBinder.buildAndBind("Remarks", "approvalRemarks", TextField.class);
		txtPreauthRemarks.setValidationVisible(false);
		
		
		String remarks = preauth.getRemarks();
		
		txtPreauthRemarks.setValue(remarks);
		
		txtDoctorNote = new TextArea("Doctor Note (Internal remarks)");
		txtDoctorNote.setValue(this.bean.getDoctorNote());
		txtDoctorNote.setDescription(this.bean.getDoctorNote());
		
		txtDateOfAdmission = (TextField) binder.buildAndBind("Date of Admission", "admissionDate", TextField.class);
		txtReasonForChangeAdmisson = (TextField) binder.buildAndBind("Reason For Change in DOA", "changeOfDOA", TextField.class);
		
		txtRoomCategory = new TextField("Room Category");
		if(this.bean.getPreauthDataExtractionDetails().getRoomCategory() != null){
			txtRoomCategory.setValue(this.bean.getPreauthDataExtractionDetails().getRoomCategory().getValue());
		}
		
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
		
		chkCriticalIllness = (CheckBox) binder.buildAndBind("Critical Illness", "criticalIllness", CheckBox.class);
		chkCriticalIllness.setEnabled(false);
		txtSpecify = new TextField("Specify");
		if(null != this.bean.getPreauthDataExtractionDetails().getSpecifyIllness()){
		txtSpecify.setValue(this.bean.getPreauthDataExtractionDetails().getSpecifyIllness().getValue());
		}
		
		txtTreatementType = new TextField("Treatment Type");
		txtTreatementType.setValue(this.bean.getPreauthDataExtractionDetails().getTreatmentType().getValue());
		
		txtTypeOfAdmission = new TextField("Type Of Admission");
		if( txtTypeOfAdmission != null && preauth != null && preauth.getTypeOfAdmission() != null ){
			txtTypeOfAdmission.setValue(masterService.getMaster(preauth.getTypeOfAdmission()).getValue());
		}
		
		txtTreatmentRemarks = new TextArea("Treatment  Remarks");
		txtTreatmentRemarks.setValue(this.bean.getPreauthDataExtractionDetails().getTreatmentRemarks());
		
		txtPatientStatus = new TextField("Patient Status");
		txtPatientStatus.setValue(this.bean.getPreauthDataExtractionDetails().getPatientStatus().getValue());
		
		txtDateOfDeath = (TextField) binder.buildAndBind("Date of Death", "deathDate", TextField.class);
		txtDateOfDeath.setValidationVisible(false);
		txtReasonForDeath = (TextField) binder.buildAndBind("Reason for Death", "reasonForDeath", TextField.class);
		txtReasonForDeath.setValidationVisible(false);
		
		txtTerminateCover = new TextField("Terminate Cover");
		txtTerminateCover.setValue(preauth.getTerminatorCover());
		
		txtReasonForAdmission = (TextField) binder.buildAndBind("Reason For Admission", "reasonForAdmission", TextField.class);
		txtReasonForAdmission.setValue(preauth.getIntimation().getAdmissionReason());
		txtNoOfDays = (TextField) binder.buildAndBind("Length of Stay", "noOfDays", TextField.class);
		
		txtNatureOfTreatement = new TextField("Nature of Treatment");
		if(null != this.bean.getPreauthDataExtractionDetails().getNatureOfTreatment()){
		txtNatureOfTreatement.setValue(this.bean.getPreauthDataExtractionDetails().getNatureOfTreatment().getValue());
		}
		
		txtConsultaionDate = (TextField) binder.buildAndBind("1st Consultation Date", "firstConsultantDate", TextField.class);
		chkCorpBuffer = (CheckBox) binder.buildAndBind("Corp Buffer", "corpBuffer", CheckBox.class);
		chkCorpBuffer.setEnabled(false);
		txtAutomaticRestoration = (TextField) binder.buildAndBind("Automatic Restoration", "autoRestoration", TextField.class);
		
		txtIllness = new TextField("Illness");
		if (this.bean.getPreauthDataExtractionDetails().getIllness() != null) {
			txtIllness.setValue(this.bean.getPreauthDataExtractionDetails()
					.getIllness().getValue());
		}
		
		txtRelapseOfIllness = (TextField) binder.buildAndBind("Relapse of Illness", "relapseIllness", TextField.class);
		txtRelapseRemark = new TextField("Remarks (Relapse)");
		
		txtAttachPreviousClaim = (TextField) previousClaimBinder.buildAndBind("Attach to Previous Claim", "attachToPreviousClaim", TextField.class);
		
		txtFvrNotRequiredRemarks = (TextField) medicalDecisionBinder.buildAndBind("FVR Not Required Remarks", "fvrNotRequiredRemarksValue", TextField.class);
		txtFvrNotRequiredRemarks.setValidationVisible(false);
		if(preauth.getFvrNotRequiredRemarks() != null){
			
			txtFvrNotRequiredRemarks.setValue(preauth.getFvrNotRequiredRemarks().getValue());
			
		}
		
		
		txtAllocationTo = (TextField) medicalDecisionBinder.buildAndBind("Allocation to", "allocationTo", TextField.class);
		txtAllocationTo.setValidationVisible(false);
		
		txtFvrTriggerPoints = (TextArea) medicalDecisionBinder.buildAndBind("FVR Trigger Points", "fvrTriggerPoints", TextArea.class);
		txtFvrTriggerPoints.setValidationVisible(false);
		
		FieldVisitRequest fieldVisitByPreauthKey = fieldVisitRequestService.getFieldVisitByPreauthKey(preauth.getKey());
		if(fieldVisitByPreauthKey != null){
			txtAllocationTo.setValue(fieldVisitByPreauthKey.getAllocationTo() != null ? fieldVisitByPreauthKey.getAllocationTo().getValue() : null);
			txtFvrTriggerPoints.setValue(fieldVisitByPreauthKey.getFvrTriggerPoints());
		}
		
		txtInvestigatorName = (TextField) medicalDecisionBinder.buildAndBind("Investigator Name", "investigatorNameValue", TextField.class);
		txtInvestigatorRemarks = (TextArea) medicalDecisionBinder.buildAndBind("Investigation Review Remarks", "investigationReviewRemarks", TextArea.class);
		txtInvestigatorRemarks.setValidationVisible(false);
		txtSpecialistType = (TextField) medicalDecisionBinder.buildAndBind("Specialist Type", "specialistType", TextField.class);
		txtSpecialistType.setValidationVisible(false);
		
		if(preauth.getSpecialistType() != null){
			txtSpecialistType.setValue(preauth.getSpecialistType().getValue());
		}
		
		txtSpecialistConsultant = (TextField) medicalDecisionBinder.buildAndBind("Specialist Consulted", "specialistConsulted", TextField.class);
		txtSpecialistConsultant.setValidationVisible(false);
		
		if(preauth.getSpecialistConsulted() != null){
			MastersValue specialistConsulted2 = masterService.getSpecialistConsulted(preauth.getSpecialistConsulted());
			if(specialistConsulted2 != null){
				txtSpecialistConsultant.setValue(specialistConsulted2.getValue());
			}
		}
		
		txtRemarksBySpecialist = (TextArea) medicalDecisionBinder.buildAndBind("Remarks by Specialist", "remarksBySpecialist", TextArea.class);
		txtRemarksBySpecialist.setValidationVisible(false);
		
		txtRemarksBySpecialist.setValue(preauth.getSpecialistRemarks());
		
		txtCPURemarks = (TextArea) medicalDecisionBinder.buildAndBind("Remarks for CPU", "remarksForCPU", TextArea.class);
		txtCPURemarks.setValidationVisible(false);
		
		initiateFieldVisitRequestRadio = (OptionGroup) medicalDecisionBinder.buildAndBind(
				"Initiate Field Visit Request",
				"initiateFieldVisitRequestFlag", OptionGroup.class);
		
		initiateFieldVisitRequestRadio.addItems(getReadioButtonOptions());
		initiateFieldVisitRequestRadio.setItemCaption(true, "Yes");
		initiateFieldVisitRequestRadio.setItemCaption(false, "No");
		initiateFieldVisitRequestRadio.setStyleName("horizontal");
		initiateFieldVisitRequestRadio.select(false);
		initiateFieldVisitRequestRadio.setEnabled(false);
		initiateFieldVisitRequestRadio.setReadOnly(true);
		
		if(preauth.getInitiateFvr() != null && preauth.getInitiateFvr().equals(1)){
			initiateFieldVisitRequestRadio.setReadOnly(false);
			initiateFieldVisitRequestRadio.select(true);
			initiateFieldVisitRequestRadio.setReadOnly(true);
		}
		
		specialistOpinionTaken = (OptionGroup) medicalDecisionBinder.buildAndBind(
				"Specialist Opinion Taken", "specialistOpinionTakenFlag",
				OptionGroup.class);
		specialistOpinionTaken.addItems(getReadioButtonOptions());
		specialistOpinionTaken.setItemCaption(true, "Yes");
		specialistOpinionTaken.setItemCaption(false, "No");
		specialistOpinionTaken.setStyleName("horizontal");
		specialistOpinionTaken.select(false);
		specialistOpinionTaken.setEnabled(false);
		if(preauth.getSpecialistOpinionTaken() != null && preauth.getSpecialistOpinionTaken().equals(1)){
			specialistOpinionTaken.select(true);
			
		}
		
		txtNegotiationWith = (TextArea) medicalDecisionBinder
				.buildAndBind("Negotiated With",
						"negotiationWith", TextArea.class);
		txtNegotiationWith.setEnabled(true);
		//txtNegotiationWith.setValue(this.bean.getPreauthMedicalDecisionDetails().getNegotiationWith());
		
		
		acctDeath = new OptionGroup("Accident / Death");
		acctDeath.addItem(SHAConstants.ACCIDENT);
		acctDeath.addItem(SHAConstants.DEATH);
		acctDeath.addStyleName("horizontal");
		acctDeath.setEnabled(false);
	    
		dateAcctDeath = new TextField("Date of Accident / Death");
		
		
		 if(preauth.getIntimation().getProcessClaimType() != null && preauth.getIntimation().getProcessClaimType().equalsIgnoreCase("P"))
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
		 
		 
		 
		 
		 List<OpinionValidationTableDTO> tableDTO= new ArrayList<OpinionValidationTableDTO>();
			List<OpinionValidation> opinionValidation =   searchService.getOpinionObjectBYIntimationNO(preauth.getIntimation().getIntimationId());
			if(opinionValidation!=null){
				tableDTO = OpinionValidationMapper.getInstance().getOpinionValidationTableObjects(opinionValidation);
			}
			
			
			if(instanceMapper != null){
				this.OpinionDetailsRolesTableTableObj = (ViewPreauthMoreOpinionDetailsRolesTable) instanceMapper.get(SHAConstants.OPINION_TABLE_OBJET);
			}else{
				this.OpinionDetailsRolesTableTableObj = this.opinionDetailsRolesTable.get();
			}
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
			if(txtDecisionChangeReason !=null && preauth != null && preauth.getDecisionChangeReason() != null){
                //MastersValue decisionChangeReason=masterService.getMaster(preauth.getDecisionChangeReason().getValue());
                txtDecisionChangeReason.setValue(preauth.getDecisionChangeReason().getValue());
			}

			
			
			txtDecisionChangeRemarks = new TextArea("Decision Change Remarks");
			if(preauth != null && preauth.getDecisionChangeRemarks()!= null){
				txtDecisionChangeRemarks.setValue(preauth.getDecisionChangeRemarks());
			}
		 
		
		FormLayout firstForm = new FormLayout(txtPreauthReceivedDate,txtPreauthStatus,txtPreauthApprovedAmt,txtMedicalRemarks,txtWithdrawInternallRemarks,acctDeath,txtDateOfAdmission,txtReasonForChangeAdmisson,txtRoomCategory
				,ventilatorSupportRadio,chkCriticalIllness,txtSpecify,txtTreatementType,txtTypeOfAdmission,txtPatientStatus,txtDateOfDeath,txtReasonForDeath,txtTerminateCover,txtPreAuthType,txtTimeBasedIcdCodeExclusion,txtDecisionChangeReason,txtDecisionChangeRemarks);
		firstForm.setSpacing(true);
		setReadOnly(firstForm, true);
		
		ViewProcedureTable procedureExclusionCheckTableInstance = null;
		
		if(instanceMapper != null){
			procedureExclusionCheckTableInstance = (ViewProcedureTable) instanceMapper.get(SHAConstants.PROCEDURE_TABLE_OBJECT);
		}else{
			procedureExclusionCheckTableInstance = procedureExclusionCheckTableObj;
		}
		
		procedureExclusionCheckTableInstance.init("Procedure Exclusion Check", false,false);
		this.procedureExclusionCheckTableObj = procedureExclusionCheckTableInstance;
//		this.procedureExclusionCheckTableObj.setReference(referenceData);
		
		List<ProcedureDTO> procedureExclusionCheckTableList = this.bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList();
		if(!procedureExclusionCheckTableList.isEmpty()) {
			for (ProcedureDTO procedureDTO : procedureExclusionCheckTableList) {
				procedureDTO.setStatusFlag(true);
				procedureDTO.setPolicyAging(policyAgeing);
				this.procedureExclusionCheckTableObj.addBeanToList(procedureDTO);
			}
		}
		
		
		if(viewPedValidationTable == null && instanceMapper != null){
			viewPedValidationTable = (ViewPedValidationTable)instanceMapper.get(SHAConstants.PED_VALIDATION_TABLE_OBJET);
		}
		
		viewPedValidationTable.init("Ped Validation", false, false);
		List<ViewPedValidationTableDTO> ViewPedValidationTableDTO = viewPedValidationService
				.search(preauth.getKey());
		for (ViewPedValidationTableDTO viewPedValidationTableDTO2 : ViewPedValidationTableDTO) {
			viewPedValidationTableDTO2.setPolicyAgeing(policyAgeing);
		}
		viewPedValidationTable.setTableList(ViewPedValidationTableDTO);
		
		if(viewProcedureExclusionCheckTable == null && instanceMapper != null){
			viewProcedureExclusionCheckTable = (ViewProcedureExclusionCheckTable) instanceMapper.get(SHAConstants.PROCEDURE_EXCLUSION_TABLE_OBJECT);
		}
		
		viewProcedureExclusionCheckTable.init("Procedure List",
				false, false);
		viewProcedureExclusionCheckTable
				.setTableList(viewProcedureExclusionService.search(preauth.getKey()));

		this.procedureExclusionCheckTableObj.setEnabled(false);
		
		if(instanceMapper != null){
			this.specialityTableObj = (ViewSpecialityTable) instanceMapper.get(SHAConstants.SPECIALIST_TABLE_OBJECT);
		}else{
			this.specialityTableObj = this.specialityTableList.get();
		}
		
		
		this.specialityTableObj.init("Speciality", false,false);
		
		if(this.specialityTableObj != null) {
			int i= 1;
			List<SpecialityDTO> specialityList = this.bean.getPreauthDataExtractionDetails().getSpecialityList();
			for (SpecialityDTO specialityDTO : specialityList) {
				specialityDTO.setStatusFlag(true);
				specialityDTO.setSerialNo(i++);
				this.specialityTableObj.addBeanToList(specialityDTO);
			}
		}
		this.specialityTableObj.setEnabled(false);
		
		/*this.viewInvestigationRemarksTable = this.viewInvestigationReviewRemarksInstance.get();
		this.viewInvestigationRemarksTable.init(SHAConstants.PRE_AUTH);
		
		List<AssignedInvestigatiorDetails> invsReviewRemarksList = reimbursementService.getRepiedInvestigationDetails(this.bean.getClaimDTO().getKey() !=null ? this.bean.getClaimDTO().getKey():0L);
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
	    this.bean.setPreauthMedicalDecisionDetails(new PreauthMedicalDecisionDTO());
		bean.getPreauthMedicalDecisionDetails().setInvsReviewRemarksTableList(invsReviewRemarksList);
		invsReviewRemarksTableObj = invsReviewRemarksTableInstance.get();
		invsReviewRemarksTableObj.init();
		invsReviewRemarksTableObj.setTableList(bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList());
		
		HorizontalLayout specialistHLayout = new HorizontalLayout(invsReviewRemarksTableObj);
		specialistHLayout.setWidth("100%");
		specialistHLayout.setSpacing(true);
		
		FormLayout secondForm = new FormLayout(txtPreauthResponseTime,txtPreauthRemarks,txtDoctorNote,dateAcctDeath,txtReasonForAdmission,txtNoOfDays,
				txtNatureOfTreatement,txtConsultaionDate,chkCorpBuffer,txtAutomaticRestoration,txtIllness,txtRelapseOfIllness,txtRelapseRemark,
				txtAttachPreviousClaim, txtNegotiationWith);
		secondForm.setSpacing(true);
		setReadOnly(secondForm, true);
		
		
		VerticalLayout vLayout = new VerticalLayout();
		vLayout.addComponents(secondForm,OpinionDetailsRolesTableTableObj);
		FormLayout finalLayout = new FormLayout();
		finalLayout.addComponent(vLayout);
		
		HorizontalLayout firstHor = new HorizontalLayout(firstForm,finalLayout);
		firstHor.setSpacing(true);
		firstHor.setWidth("110%");
		
		FormLayout thirdForm = new FormLayout(initiateFieldVisitRequestRadio,txtFvrNotRequiredRemarks,txtAllocationTo,txtFvrTriggerPoints/*,txtInvestigatorName,txtInvestigatorRemarks*/);;
		thirdForm.setSpacing(true);
		
		VerticalLayout vLeftLayout = new VerticalLayout();
		vLeftLayout.addComponents(thirdForm,specialistHLayout);
		
		FormLayout fourthForm = new FormLayout(specialistOpinionTaken,txtSpecialistType,txtSpecialistConsultant,txtRemarksBySpecialist,txtCPURemarks);
		fourthForm.setSpacing(true);
		
		setReadOnly(thirdForm,true);
		setReadOnly(fourthForm,true);
		
		HorizontalLayout secondHor = new HorizontalLayout(vLeftLayout,fourthForm);
		secondHor.setSpacing(true);
		secondHor.setWidth("100%");
		
		Panel panel = new Panel(firstHor);
		panel.setCaption("Preauth Details");
		panel.addStyleName("girdBorder");
		panel.setWidth("100%");
		panel.setHeight("557px");
		
		approvedAmtField = new TextField();
		approvedAmtField.setEnabled(false);
		
		if(instanceMapper != null){
			this.diagnosisListenerTableObj = (RevisedDiagnosisPreauthViewTable) instanceMapper.get(SHAConstants.DIAGNOSIS_TABLE_OBJECT);
		}else{
			this.diagnosisListenerTableObj=this.diagnosisListenerTableInstance.get();
		}
		
		// View PreauthDetails Multilple Data Issue
		this.diagnosisListenerTableObj.removeAllItems();
		this.diagnosisListenerTableObj.init(this.bean,approvedAmtField);
		
		for (DiagnosisProcedureTableDTO diagnosisProcedureTableDTO : diagnosisProcedureValues) {
			this.diagnosisListenerTableObj.addBeanToList(diagnosisProcedureTableDTO);		
		}	
		if(instanceMapper != null){
			claimedDetailsTableObj = (NewClaimedAmountTable) instanceMapper.get(SHAConstants.AMOUNT_CLAIMED_OBJECT);
			
			Hospitals hospitalById = hospitalService.getHospitalById(bean.getHospitalKey());
			PolicyDto policyDTO = bean.getPolicyDto();
			//String insuredAge = getInsuredAge(policyDTO.getInsuredDob());
			Double sumInsured = dbCalculationService.getInsuredSumInsured(bean.getNewIntimationDTO().getInsuredPatient().getInsuredId().toString(), bean.getPolicyDto().getKey()
					,bean.getNewIntimationDTO().getInsuredPatient().getLopFlag());
			
			if(ReferenceTable.getGPAProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				sumInsured = dbCalculationService.getGPAInsuredSumInsured(
						bean.getNewIntimationDTO().getInsuredPatient()
								.getInsuredId().toString(), bean.getPolicyDto()
								.getKey());
			}
			
			if (sumInsured == 0) {
				sumInsured = policyDTO.getTotalSumInsured();
			}
	         Map<Integer, Object> hospitalizationDetails = new HashMap<Integer, Object>();
			
			if(bean.getPreauthDataExtractionDetails().getSection() != null){
				try{
					if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
						sumInsured = dbCalculationService.getInsuredSumInsuredForGMC(bean.getNewIntimationDTO().getPolicy().getKey(),
								bean.getNewIntimationDTO().getInsuredPatient().getKey(),bean.getNewIntimationDTO().getPolicy().getSectionCode());
						hospitalizationDetails = dbCalculationService.getHospitalizationDetailsForGMC(policyDTO.getProduct().getKey(),
								sumInsured, hospitalById.getCityClass(), bean.getNewIntimationDTO().getInsuredPatient().getInsuredId(), 
								bean.getNewIntimationDTO().getKey(),bean.getPreauthDataExtractionDetails().getSection().getId(),"A");
					}else{
						hospitalizationDetails = dbCalculationService.getHospitalizationDetails(policyDTO.getProduct().getKey(),
								sumInsured, hospitalById.getCityClass(), bean.getNewIntimationDTO().getInsuredPatient().getInsuredId(), 
								bean.getNewIntimationDTO().getKey(),bean.getPreauthDataExtractionDetails().getSection().getId(),"A");
					}
				
				
				}catch(Exception e){
					e.printStackTrace();
				}
			}else{
				try{
					if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
						sumInsured = dbCalculationService.getInsuredSumInsuredForGMC(bean.getNewIntimationDTO().getPolicy().getKey(),
								bean.getNewIntimationDTO().getInsuredPatient().getKey(),bean.getNewIntimationDTO().getPolicy().getSectionCode());
						hospitalizationDetails = dbCalculationService.getHospitalizationDetailsForGMC(policyDTO.getProduct().getKey(),
								sumInsured, hospitalById.getCityClass(), bean.getNewIntimationDTO().getInsuredPatient().getInsuredId(), bean.getNewIntimationDTO().getKey(),0l,"0");
					}else{
						
						String policyPlan = bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null ? bean.getNewIntimationDTO().getPolicy().getPolicyPlan() : "0";
						
						/*if(ReferenceTable.MEDI_CLASSIC_BASIC_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) || ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {*/
						if(bean.getNewIntimationDTO().getPolicy().getProduct() != null 
								&& (((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
										SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))
										|| SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())
										|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))
								|| ((SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) || 
										SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))
									&& bean.getNewIntimationDTO().getPolicy().getProductType().getKey().equals(ReferenceTable.INDIVIDUAL_POLICY)))) {
							policyPlan = bean.getNewIntimationDTO().getInsuredPatient().getPolicyPlan() != null ? bean.getNewIntimationDTO().getInsuredPatient().getPolicyPlan() : "0";
						}
						
						hospitalizationDetails = dbCalculationService.getHospitalizationDetails(policyDTO.getProduct().getKey(),
								sumInsured, hospitalById.getCityClass(), bean.getNewIntimationDTO().getInsuredPatient().getInsuredId(), bean.getNewIntimationDTO().getKey(),0l,policyPlan);
					}
				
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			claimedDetailsTableObj.setDBCalculationValues(hospitalizationDetails);
			// View PreauthDetails Multilple Data Issue
			claimedDetailsTableObj.resetSerialNo();
			claimedDetailsTableObj.initView(this.bean, "view");
			
		}else{
			claimedDetailsTableObj =   claimedAmountDetailsTable.get();
			// View PreauthDetails Multilple Data Issue
			claimedDetailsTableObj.resetSerialNo();
			claimedDetailsTableObj.initView(this.bean, SHAConstants.VIEW_PREAUTH);
		}
		
		
		
		List<NoOfDaysCell> claimedDetailsList = this.bean.getPreauthDataExtractionDetails().getClaimedDetailsList();
		claimedDetailsTableObj.setValues(this.bean.getPreauthDataExtractionDetails().getClaimedDetailsList(), false);
//		this.bean.getPreauthDataExtractionDetails().setClaimedDetailsList(claimedDetailsList);
		this.bean.getPreauthDataExtractionDetails().setClaimedDetailsList(claimedDetailsTableObj.getValues());
		claimedDetailsTableObj.setEnabled(false);
		
		Label amtClaimedDetailsLbl = new Label("A) Amount Considered");
		HorizontalLayout viewLayout1 = new HorizontalLayout(
				amtClaimedDetailsLbl);
		viewLayout1.setSpacing(true);
		
		Label balanceSumInsuredLbl = new Label("B) Balance Sum Insured");
		
		HorizontalLayout viewLayout2 = new HorizontalLayout(
				balanceSumInsuredLbl);
		viewLayout2.setSpacing(true);
		
		
		HorizontalLayout approvedFormLayout = new HorizontalLayout(new Label(
				"C) Sub limits, </br> Package &  </br> SI Restriction Amount",
				ContentMode.HTML), approvedAmtField);
		
		if(instanceMapper != null){
			this.amountConsideredTable = (AmountConsideredTable) instanceMapper.get(SHAConstants.AMOUNT_CONSIDERED_OBJECT);
		}else{
			this.amountConsideredTable = this.amountConsideredTableInstance.get();
		}
		
		this.amountConsideredTable.initTable(this.bean, viewLayout1,
				viewLayout2,approvedFormLayout, true, false);
		
		Integer payableAmount = this.diagnosisListenerTableObj.getTotalPayableAmount();
		approvedAmtField.setValue(payableAmount.toString());
		if(this.bean.getPreauthMedicalDecisionDetails()
				.getInitialApprovedAmt() != null){
			
			approvedAmtField.setValue(this.bean.getPreauthMedicalDecisionDetails().getInitialApprovedAmt().toString());
			this.amountConsideredTable.setApprovedAmtForViews(this.bean
					.getPreauthMedicalDecisionDetails()
					.getInitialApprovedAmt().toString(), preauth.getTotalApprovalAmount() != null ? preauth.getTotalApprovalAmount().toString() : ""); 
			
		}
		
		HorizontalLayout horizontalLayout = new HorizontalLayout(referenceNoForm);
		horizontalLayout.setComponentAlignment(referenceNoForm, Alignment.MIDDLE_RIGHT);
		
		txtPreauthapproField = new TextField("Pre-auth Approved Amt");
		txtPreauthapproField.setValue(preauth.getTotalApprovalAmount() != null ? preauth.getTotalApprovalAmount().toString() : "");
		
		VerticalLayout mainVertical= null;
		
	    FormLayout treatementRemarksForm = new FormLayout(txtTreatmentRemarks);
	    //txtTreatmentRemarks.setReadOnly(true);
		setReadOnly(treatementRemarksForm, true);
		
		//R1006
		txtAmtClaimed = binder.buildAndBind("Amount Claimed" , "amtClaimed",TextField.class);
		txtAmtClaimed.setNullRepresentation("");
		
		txtDiscntHospBill = binder.buildAndBind("Discount in Hospital Bill" , "disCntHospBill",TextField.class);
		txtDiscntHospBill.setNullRepresentation("");
		
		txtNetAmt = binder.buildAndBind("Claimed amount after Discount" , "netAmt",TextField.class);
		txtNetAmt.setNullRepresentation("");
		

		btnHospitalScroing = new Button("Hospital Scoring/PP Coding");
		/*if(bean.getNewIntimationDTO().getHospitalDto().getHospitalType().getId() == ReferenceTable.NETWORK_HOSPITAL_TYPE_ID){
			btnHospitalScroing.setVisible(true);
		}else{
			btnHospitalScroing.setVisible(false);
		}*/
		btnHospitalScroing.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				showScoringView();
			}
		});
		
		if(instanceMapper != null){
			this.treatingDoctorTableObj = (ViewTreatingDoctorTabel) instanceMapper.get(SHAConstants.TREATING_DOCTOR_OBJECT);
		}else{
			this.treatingDoctorTableObj = this.treatingDoctorTableList.get();
		}
			
		this.treatingDoctorTableObj.init("Treating Doctor Deatils", false,false);	
		if(this.treatingDoctorTableObj != null) {
			int i= 1;
			List<TreatingDoctorDTO> treatingDoctorlist = this.bean.getPreauthDataExtractionDetails().getTreatingDoctorDTOs();
			for (TreatingDoctorDTO treatingDoctorDTO : treatingDoctorlist) {		
				treatingDoctorDTO.setSerialNo(i++);
				if(treatingDoctorDTO != null && treatingDoctorDTO.getTreatingDoctorSignature() != null
						&& treatingDoctorDTO.getTreatingDoctorSignature().getValue() != null){
					SelectValue selVal = new SelectValue();
					if(treatingDoctorDTO.getTreatingDoctorSignature().getValue().equalsIgnoreCase("Yes")){
						selVal.setId(1l);
						selVal.setValue("Yes");
						treatingDoctorDTO.setTreatingDoctorSignature(selVal);
					} else {
						selVal.setId(2l);
						selVal.setValue("No");
						treatingDoctorDTO.setTreatingDoctorSignature(selVal);
					}
				} else {
					SelectValue selVal = new SelectValue();
					selVal.setId(2l);
					selVal.setValue("No");
					treatingDoctorDTO.setTreatingDoctorSignature(selVal);
				}
				this.treatingDoctorTableObj.addBeanToList(treatingDoctorDTO);
			}
		}
		this.treatingDoctorTableObj.setEnabled(false);
		
		horizontalLayout.addComponent(btnHospitalScroing);
		horizontalLayout.setComponentAlignment(btnHospitalScroing, Alignment.MIDDLE_RIGHT);
		horizontalLayout.setSpacing(true);
		
	
		FormLayout claimedAmtFLayout = new FormLayout(txtAmtClaimed);
		claimedAmtFLayout.setMargin(false);
		setReadOnly(claimedAmtFLayout, true);
		FormLayout disCntFLayout = new FormLayout(txtDiscntHospBill);
		disCntFLayout.setMargin(false);
		setReadOnly(disCntFLayout, true);
		FormLayout netAmtFLayout = new FormLayout(txtNetAmt);
		netAmtFLayout.setMargin(false);
		setReadOnly(netAmtFLayout, true);
		HorizontalLayout hospDiscountHLayout = new HorizontalLayout(claimedAmtFLayout,
				disCntFLayout, netAmtFLayout);
		hospDiscountHLayout.setWidth("100%");
		
		
		if(this.bean.getPreauthDataExtractionDetails().getTreatmentType().getValue().equalsIgnoreCase("Surgical")){
		mainVertical = new VerticalLayout(horizontalLayout,panel,this.viewProcedureExclusionCheckTable,this.treatingDoctorTableObj/*,this.specialityTableObj*/,secondHor,this.diagnosisListenerTableObj,this.amountConsideredTable,hospDiscountHLayout,claimedDetailsTableObj,viewPedValidationTable,this.procedureExclusionCheckTableObj);
		}
		else{
			mainVertical = new VerticalLayout(horizontalLayout,panel,treatementRemarksForm,this.treatingDoctorTableObj,this.specialityTableObj,secondHor,this.diagnosisListenerTableObj,this.amountConsideredTable,hospDiscountHLayout,claimedDetailsTableObj,viewPedValidationTable,this.procedureExclusionCheckTableObj);
		}
		
		
		mainVertical.setSpacing(true);
		mainVertical.setMargin(true);
		
		setCompositionRoot(mainVertical);
		
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
			}
		}
	}
	
	protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);

		return coordinatorValues;
	}
	
	public void setTableInstances(Map<String,Object> instanceMapper){
		
		this.instanceMapper = instanceMapper;
	}
	
	public void setReferenceData(){
	
			BeanItemContainer<SelectValue> procedureListNames = preauthService.getProcedureListNames();
			referenceData.put("procedureName", procedureListNames);
			referenceData.put("procedureCode", procedureListNames);
			referenceData.put("medicalSpeciality", preauthService.getSpecialityType("M"));
			referenceData.put("surgicalSpeciality", preauthService.getSpecialityType("S"));
			referenceData.put("sublimitApplicable", masterService.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
			referenceData.put("considerForPayment", masterService.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
			referenceData.put("considerForDayCare", masterService.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
			referenceData.put("commonValues", masterService.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
			referenceData.put("pedExclusionImpactOnDiagnosis",masterService.getSelectValueContainer(ReferenceTable.IMPACT_ON_DIAGNOSI));
			referenceData.put("exclusionDetails",masterService.getSelectValueContainer(ReferenceTable.EXCLUSION_DETAILS));
			referenceData.put("procedureStatus",masterService.getSelectValueContainer(ReferenceTable.PROCEDURE_STATUS));
			//referenceData.put("pedList", masterService.getInsuredPedDetails());
	}
	
	
	
	

	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHospitalizationDetails(
			Map<Integer, Object> hospitalizationDetails) {
		this.claimedDetailsTableObj.setDBCalculationValues(hospitalizationDetails);
		
	}

	@Override
	public void setApprovedAmountField(Integer approvedAmount) {
		approvedAmtField.setValue(approvedAmount.toString());
	}

	public void showScoringView(){
		/*hospitalScoringView.init(bean.getNewIntimationDTO().getIntimationId(), false);	
		hospitalScoringView.setScreenName("ViewPage");*/
		
		scoringAndPPDetailView.setIntimationNumber(bean.getNewIntimationDTO().getIntimationId());
		scoringAndPPDetailView.setScreenName("ViewPage");
		scoringAndPPDetailView.setIsoldppscoreview(false);
		scoringAndPPDetailView.init(false);
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
	public void setClearReferenceData(){
    	SHAUtils.setClearPreauthDTO(this.bean);
    	SHAUtils.setClearReferenceData(referenceData);
    	SHAUtils.setClearReferenceData(instanceMapper);
		if(this.binder!=null){
			this.binder = null;
		}
		if(this.previousClaimBinder!=null){
			this.previousClaimBinder=null;
		}
		if(this.medicalDecisionBinder != null){
    		this.medicalDecisionBinder=null;
    	}
    	if(this.medicalProcessingBinder != null){
    		this.medicalProcessingBinder=null;
    	}
    	if(this.diagnosisListenerTableObj!=null){
    		this.diagnosisListenerTableObj=null;
    	}
    	if(this.amountConsideredTable!=null){
    		this.amountConsideredTable=null;
    	}
    	if(this.claimedDetailsTableObj!=null){
    		this.claimedDetailsTableObj=null;
    	}
    	if(this.specialityTableObj!=null){
    		this.specialityTableObj.removeRow();
    		this.specialityTableObj=null;
    	}
    	if(this.OpinionDetailsRolesTableTableObj!=null){
    		this.OpinionDetailsRolesTableTableObj.removeRow();
    		this.OpinionDetailsRolesTableTableObj=null;
    	}
    	if(this.treatingDoctorTableObj!=null){
    		this.treatingDoctorTableObj.removeRow();
    		this.treatingDoctorTableObj=null;
    	}
    	if(this.invsReviewRemarksTableObj!=null){
    		this.invsReviewRemarksTableObj=null;
    	}
//    	this.diagnosisDetailsTableObj.clearObject();
    	  	
    }

}

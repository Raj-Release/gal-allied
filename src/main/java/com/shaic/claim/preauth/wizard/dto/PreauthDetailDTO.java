package com.shaic.claim.preauth.wizard.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shaic.arch.SHAUtils;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.pedvalidation.view.ViewPedValidationTableDTO;
import com.shaic.claim.premedical.dto.NoOfDaysCell;
import com.shaic.domain.preauth.Preauth;

public class PreauthDetailDTO {

	private String createdDate;
	
	private String modifiedDate;
	
	private String remarks;
	
	private String medicalRemarks;
	
	private String approvedAmt;
	
	private String doctorNote;
	
	private String dateOfAdmisssion;
	
	private String changeDOA;
	
	private String admissionReason;
	
	/*private PreauthDTO preauthDTO;*/
	
	private List<DiagnosisProcedureTableDTO> diagnosisTableList = new ArrayList<DiagnosisProcedureTableDTO>();
	
	private HashMap<Integer,String> amountConsideredValues;
	
	private HashMap<Integer,String> balanceSumInsuredValues;
	
	private Integer minimumValue;
	
	private Integer balanceSumInsured;
	
	private Integer totalClaimedAmtforAmtconsd; 
	
	private Integer totalDeductAmtforAmtconsd; 
	
	private Integer totalNonPayableAmtforAmtconsd;
	
	private Integer totalPayableAmtforAmtconsd;
	
	private Integer totalProductAmtforAmtconsd; 
	
	private Integer totalNetAmtforAmtconsd;
	
	private List<NoOfDaysCell> claimedDetailsList = new ArrayList<NoOfDaysCell>();;
	
	private List<ViewPedValidationTableDTO> pedValidationList = new ArrayList<ViewPedValidationTableDTO>();
	
	private List<ProcedureDTO> procedureExclusionCheckTableList = new ArrayList<ProcedureDTO>();
	
	private Integer initiateFvr;
	
	private Integer specialistOpinionTaken;
	
	private String fvrNotRequiredRemarks;
	
	private String allocationTo;
	
	private String specialistType;
	
	private String specialistConsulted;
	
	private String fvrTriggeredPoints;
	
	private String specialistRemarks;
	
	private Integer totalDiagReverseAllocAmt;
	
	private String treatmentType;
	
	private String statusValue;
	
	private String specifyIllness;
	
	private String roomCategory;
	
	private String natureOfTreatment;
	
	private Integer totalDiagAllocAmt;
	
	private Integer totalDiagPayableAmt;
	
	private Integer totalDiagCopayAmt;
	
	private Integer totalDiagNetAmt;
	
	private String noOfDays;
	
	private String firstConsultedDate;
	
	private String criticalIllness;

	private String corpBuffer;
	
	private String patientStatus;
	
	private String autoRestore;
	
	private String illness;
	
	private String dateOfDeath;
	
	private String deathReason;
	
	private String illnessRelapse;
	
	private String relapseRemarks;
	
	private String investigatorName;
	
	private String terminateCover;
	
	private String treatmentRemarks;
	
	private String negotiationWith;
	
	public PreauthDetailDTO(Preauth preAuth, PreauthDTO preauthDTO) {
		this.createdDate = (preAuth.getCreatedDate() != null ? SHAUtils.formateDateForHistory(preAuth.getCreatedDate()) : "-");
		this.modifiedDate = (preAuth.getCreatedDate() != null ? SHAUtils.formateDateForHistory(preAuth.getModifiedDate()) : "-");
		this.firstConsultedDate = (preAuth.getConsultationDate() != null ? SHAUtils.formateDateForHistory(preAuth.getConsultationDate()) : "-");
        this.remarks = preAuth.getRemarks();
        //this.preauthDTO =  preauthDTO;
        this.medicalRemarks = (preAuth.getMedicalRemarks() != null ? preAuth.getMedicalRemarks() : "-");
        this.admissionReason = preAuth.getIntimation().getAdmissionReason();
        this.approvedAmt = preAuth.getTotalApprovalAmount() != null ? preAuth.getTotalApprovalAmount().toString() : "-";
        if(preauthDTO.getPreauthDataExtractionDetails() != null &&  preauthDTO.getPreauthDataExtractionDetails().getClaimedDetailsList() != null) {
        	this.claimedDetailsList.addAll(preauthDTO.getPreauthDataExtractionDetails().getClaimedDetailsList());
        }
        if(preauthDTO.getPreauthMedicalProcessingDetails() != null &&  preauthDTO.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList() != null) {
        	this.procedureExclusionCheckTableList = new ArrayList<ProcedureDTO>();
        	this.procedureExclusionCheckTableList.addAll(preauthDTO.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList());
        }
        
        this.initiateFvr = (preAuth.getInitiateFvr() != null ? preAuth.getInitiateFvr() : 0 );
        this.specialistOpinionTaken = preAuth.getSpecialistOpinionTaken(); 
        this.fvrNotRequiredRemarks = (preAuth.getFvrNotRequiredRemarks() != null ? preAuth.getFvrNotRequiredRemarks().getValue() : "-" ); 
        this.specialistConsulted = (preAuth.getSpecialistType() != null ? preAuth.getSpecialistType().getValue() : "-" ); 
        this.specialistRemarks = preAuth.getSpecialistRemarks(); 
        this.doctorNote = (preAuth.getDoctorNote() != null ? preAuth.getDoctorNote() : "-" );
        if(preauthDTO.getPreauthDataExtractionDetails() != null ) {
        	if(preauthDTO.getPreauthDataExtractionDetails().getSpecifyIllness() != null) {
        		this.specifyIllness = preauthDTO.getPreauthDataExtractionDetails().getSpecifyIllness().getValue();
        	}
        	if(preauthDTO.getPreauthDataExtractionDetails().getTreatmentType() != null) {
        		this.treatmentType = preauthDTO.getPreauthDataExtractionDetails().getTreatmentType().getValue();
        	}
        	
        	if(preauthDTO.getPreauthDataExtractionDetails().getRoomCategory() != null) {
        		this.roomCategory = preauthDTO.getPreauthDataExtractionDetails().getRoomCategory().getValue();
        	}
        	
        	if(preauthDTO.getPreauthDataExtractionDetails().getNatureOfTreatment() != null) {
        		this.natureOfTreatment = preauthDTO.getPreauthDataExtractionDetails().getNatureOfTreatment().getValue();
        	}
        	
        	if(preauthDTO.getPreauthDataExtractionDetails().getAdmissionDate() != null) {
        		this.dateOfAdmisssion = SHAUtils.formateDateForHistory(preauthDTO.getPreauthDataExtractionDetails().getAdmissionDate());
        	}
        
        	if(preauthDTO.getPreauthDataExtractionDetails().getChangeInReasonDOA() != null) {
        	   this.changeDOA = preauthDTO.getPreauthDataExtractionDetails().getChangeInReasonDOA();
        	}
        	
        	if(preauthDTO.getPreauthDataExtractionDetails().getCriticalIllness() != null && preauthDTO.getPreauthDataExtractionDetails().getCriticalIllness() == Boolean.TRUE) {
        		this.criticalIllness = "Y";
        	} else {
        		this.criticalIllness = "N";
        	}
        	
        	if(preauthDTO.getPreauthDataExtractionDetails().getCorpBuffer() != null && preauthDTO.getPreauthDataExtractionDetails().getCorpBuffer() == Boolean.TRUE) {
        		this.corpBuffer = "Y";
        	} else {
        		this.corpBuffer = "N";
        	}
        	this.noOfDays = preauthDTO.getPreauthDataExtractionDetails().getNoOfDays();
        	this.autoRestore = preauthDTO.getPreauthDataExtractionDetails().getAutoRestoration();
        	this.treatmentRemarks = (preauthDTO.getPreauthDataExtractionDetails().getTreatmentRemarks() != null ? preauthDTO.getPreauthDataExtractionDetails().getTreatmentRemarks() : "-");
        	
        }
        this.statusValue = preauthDTO.getStatusValue();
        this.patientStatus = (preAuth.getPatientStatus() != null ? preAuth.getPatientStatus().getValue() : "-");
        this.dateOfDeath = (preAuth.getDateOfDeath() != null ? SHAUtils.formateDateForHistory(preAuth.getDateOfDeath()) : "");
        this.relapseRemarks = (preAuth.getRelapseRemarks() != null ? preAuth.getRelapseRemarks()  : "-");
        this.deathReason = (preAuth.getDeathReason() != null ? preAuth.getDeathReason()  : "-");
        this.illness = (preAuth.getIllness() != null ? preAuth.getIllness().getValue()  : "-");
        this.investigatorName = (preAuth.getInvestigatorName() != null ? preAuth.getInvestigatorName() : "-");
        this.terminateCover = (preAuth.getTerminatorCover() != null ? preAuth.getTerminatorCover()  : "-");
        if(preauthDTO.getPreauthMedicalDecisionDetails() != null) {
            this.negotiationWith = (preauthDTO.getPreauthMedicalDecisionDetails().getNegotiationWith() != null ? preauthDTO.getPreauthMedicalDecisionDetails().getNegotiationWith()  : "-");
        }

	}
	
	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getMedicalRemarks() {
		return medicalRemarks;
	}

	public void setMedicalRemarks(String medicalRemarks) {
		this.medicalRemarks = medicalRemarks;
	}

	public String getDoctorNote() {
		return doctorNote;
	}

	public void setDoctorNote(String doctorNote) {
		this.doctorNote = doctorNote;
	}

	public String getDateOfAdmisssion() {
		return dateOfAdmisssion;
	}

	public void setDateOfAdmisssion(String dateOfAdmisssion) {
		this.dateOfAdmisssion = dateOfAdmisssion;
	}

	public String getChangeDOA() {
		return changeDOA;
	}

	public void setChangeDOA(String changeDOA) {
		this.changeDOA = changeDOA;
	}

	public String getAdmissionReason() {
		return admissionReason;
	}

	public void setAdmissionReason(String admissionReason) {
		this.admissionReason = admissionReason;
	}

	public String getApprovedAmt() {
		return approvedAmt;
	}

	public void setApprovedAmt(String approvedAmt) {
		this.approvedAmt = approvedAmt;
	}

	public List<DiagnosisProcedureTableDTO> getDiagnosisTableList() {
		return diagnosisTableList;
	}

	public void setDiagnosisTableList(
			List<DiagnosisProcedureTableDTO> diagnosisTableList) {
		this.diagnosisTableList = diagnosisTableList;
	}

	public HashMap<Integer, String> getAmountConsideredValues() {
		return amountConsideredValues;
	}

	public void setAmountConsideredValues(
			Map<Integer, String> amountConsideredValue) {
		this.amountConsideredValues = (HashMap<Integer, String>) amountConsideredValue;
	}

	public Integer getMinimumValue() {
		return minimumValue;
	}

	public void setMinimumValue(Integer minimumValue) {
		this.minimumValue = minimumValue;
	}

	public HashMap<Integer, String> getBalanceSumInsuredValues() {
		return balanceSumInsuredValues;
	}

	public void setBalanceSumInsuredValues(
			Map<Integer, String> balanceSumInsuredValues) {
		this.balanceSumInsuredValues = (HashMap<Integer, String>) balanceSumInsuredValues;
	}

	public Integer getBalanceSumInsured() {
		return balanceSumInsured;
	}

	public void setBalanceSumInsured(Integer balanceSumInsured) {
		this.balanceSumInsured = balanceSumInsured;
	}

	public Integer getTotalClaimedAmtforAmtconsd() {
		return totalClaimedAmtforAmtconsd;
	}

	public void setTotalClaimedAmtforAmtconsd(Integer totalClaimedAmtforAmtconsd) {
		this.totalClaimedAmtforAmtconsd = totalClaimedAmtforAmtconsd;
	}

	public Integer getTotalDeductAmtforAmtconsd() {
		return totalDeductAmtforAmtconsd;
	}

	public void setTotalDeductAmtforAmtconsd(Integer totalDeductAmtforAmtconsd) {
		this.totalDeductAmtforAmtconsd = totalDeductAmtforAmtconsd;
	}

	public Integer getTotalNonPayableAmtforAmtconsd() {
		return totalNonPayableAmtforAmtconsd;
	}

	public void setTotalNonPayableAmtforAmtconsd(
			Integer totalNonPayableAmtforAmtconsd) {
		this.totalNonPayableAmtforAmtconsd = totalNonPayableAmtforAmtconsd;
	}

	public Integer getTotalPayableAmtforAmtconsd() {
		return totalPayableAmtforAmtconsd;
	}

	public void setTotalPayableAmtforAmtconsd(Integer totalPayableAmtforAmtconsd) {
		this.totalPayableAmtforAmtconsd = totalPayableAmtforAmtconsd;
	}

	public Integer getTotalProductAmtforAmtconsd() {
		return totalProductAmtforAmtconsd;
	}

	public void setTotalProductAmtforAmtconsd(Integer totalProductAmtforAmtconsd) {
		this.totalProductAmtforAmtconsd = totalProductAmtforAmtconsd;
	}

	public Integer getTotalNetAmtforAmtconsd() {
		return totalNetAmtforAmtconsd;
	}

	public void setTotalNetAmtforAmtconsd(Integer totalNetAmtforAmtconsd) {
		this.totalNetAmtforAmtconsd = totalNetAmtforAmtconsd;
	}

	public void setAmountConsideredValues(
			HashMap<Integer, String> amountConsideredValues) {
		this.amountConsideredValues = amountConsideredValues;
	}

	public void setBalanceSumInsuredValues(
			HashMap<Integer, String> balanceSumInsuredValues) {
		this.balanceSumInsuredValues = balanceSumInsuredValues;
	}

	public List<NoOfDaysCell> getClaimedDetailsList() {
		return claimedDetailsList;
	}

	public void setClaimedDetailsList(List<NoOfDaysCell> claimedDetailsList) {
		this.claimedDetailsList = claimedDetailsList;
	}

	public List<ViewPedValidationTableDTO> getPedValidationList() {
		return pedValidationList;
	}

	public void setPedValidationList(
			List<ViewPedValidationTableDTO> pedValidationList) {
		this.pedValidationList = pedValidationList;
	}

	public List<ProcedureDTO> getProcedureExclusionCheckTableList() {
		return procedureExclusionCheckTableList;
	}

	public void setProcedureExclusionCheckTableList(
			List<ProcedureDTO> procedureExclusionCheckTableList) {
		this.procedureExclusionCheckTableList = procedureExclusionCheckTableList;
	}

	public Integer getInitiateFvr() {
		return initiateFvr;
	}

	public void setInitiateFvr(Integer initiateFvr) {
		this.initiateFvr = initiateFvr;
	}

	public Integer getSpecialistOpinionTaken() {
		return specialistOpinionTaken;
	}

	public void setSpecialistOpinionTaken(Integer specialistOpinionTaken) {
		this.specialistOpinionTaken = specialistOpinionTaken;
	}

	public String getFvrNotRequiredRemarks() {
		return fvrNotRequiredRemarks;
	}

	public void setFvrNotRequiredRemarks(String fvrNotRequiredRemarks) {
		this.fvrNotRequiredRemarks = fvrNotRequiredRemarks;
	}

	public String getAllocationTo() {
		return allocationTo;
	}

	public void setAllocationTo(String allocationTo) {
		this.allocationTo = allocationTo;
	}

	public String getSpecialistType() {
		return specialistType;
	}

	public void setSpecialistType(String specialistType) {
		this.specialistType = specialistType;
	}

	public String getSpecialistConsulted() {
		return specialistConsulted;
	}

	public void setSpecialistConsulted(String specialistConsulted) {
		this.specialistConsulted = specialistConsulted;
	}

	public String getFvrTriggeredPoints() {
		return fvrTriggeredPoints;
	}

	public void setFvrTriggeredPoints(String fvrTriggeredPoints) {
		this.fvrTriggeredPoints = fvrTriggeredPoints;
	}

	public String getSpecialistRemarks() {
		return specialistRemarks;
	}

	public void setSpecialistRemarks(String specialistRemarks) {
		this.specialistRemarks = specialistRemarks;
	}

	public Integer getTotalDiagReverseAllocAmt() {
		return totalDiagReverseAllocAmt;
	}

	public void setTotalDiagReverseAllocAmt(Integer totalDiagReverseAllocAmt) {
		this.totalDiagReverseAllocAmt = totalDiagReverseAllocAmt;
	}

	public String getTreatmentType() {
		return treatmentType;
	}

	public void setTreatmentType(String treatmentType) {
		this.treatmentType = treatmentType;
	}

	public String getStatusValue() {
		return statusValue;
	}

	public void setStatusValue(String statusValue) {
		this.statusValue = statusValue;
	}

	public String getSpecifyIllness() {
		return specifyIllness;
	}

	public void setSpecifyIllness(String specifyIllness) {
		this.specifyIllness = specifyIllness;
	}

	public String getRoomCategory() {
		return roomCategory;
	}

	public void setRoomCategory(String roomCategory) {
		this.roomCategory = roomCategory;
	}

	public String getNatureOfTreatment() {
		return natureOfTreatment;
	}

	public void setNatureOfTreatment(String natureOfTreatment) {
		this.natureOfTreatment = natureOfTreatment;
	}

	public Integer getTotalDiagAllocAmt() {
		return totalDiagAllocAmt;
	}

	public void setTotalDiagAllocAmt(Integer totalDiagAllocAmt) {
		this.totalDiagAllocAmt = totalDiagAllocAmt;
	}

	public Integer getTotalDiagPayableAmt() {
		return totalDiagPayableAmt;
	}

	public void setTotalDiagPayableAmt(Integer totalDiagPayableAmt) {
		this.totalDiagPayableAmt = totalDiagPayableAmt;
	}

	public Integer getTotalDiagCopayAmt() {
		return totalDiagCopayAmt;
	}

	public void setTotalDiagCopayAmt(Integer totalDiagCopayAmt) {
		this.totalDiagCopayAmt = totalDiagCopayAmt;
	}

	public Integer getTotalDiagNetAmt() {
		return totalDiagNetAmt;
	}

	public void setTotalDiagNetAmt(Integer totalDiagNetAmt) {
		this.totalDiagNetAmt = totalDiagNetAmt;
	}

	public String getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(String noOfDays) {
		this.noOfDays = noOfDays;
	}

	public String getFirstConsultedDate() {
		return firstConsultedDate;
	}

	public void setFirstConsultedDate(String firstConsultedDate) {
		this.firstConsultedDate = firstConsultedDate;
	}

	public String getCriticalIllness() {
		return criticalIllness;
	}

	public void setCriticalIllness(String criticalIllness) {
		this.criticalIllness = criticalIllness;
	}

	public String getCorpBuffer() {
		return corpBuffer;
	}

	public void setCorpBuffer(String corpBuffer) {
		this.corpBuffer = corpBuffer;
	}

	public String getPatientStatus() {
		return patientStatus;
	}

	public void setPatientStatus(String patientStatus) {
		this.patientStatus = patientStatus;
	}

	public String getAutoRestore() {
		return autoRestore;
	}

	public void setAutoRestore(String autoRestore) {
		this.autoRestore = autoRestore;
	}

	public String getIllness() {
		return illness;
	}

	public void setIllness(String illness) {
		this.illness = illness;
	}

	public String getDateOfDeath() {
		return dateOfDeath;
	}

	public void setDateOfDeath(String dateOfDeath) {
		this.dateOfDeath = dateOfDeath;
	}

	public String getDeathReason() {
		return deathReason;
	}

	public void setDeathReason(String deathReason) {
		this.deathReason = deathReason;
	}

	public String getIllnessRelapse() {
		return illnessRelapse;
	}

	public void setIllnessRelapse(String illnessRelapse) {
		this.illnessRelapse = illnessRelapse;
	}

	public String getRelapseRemarks() {
		return relapseRemarks;
	}

	public void setRelapseRemarks(String relapseRemarks) {
		this.relapseRemarks = relapseRemarks;
	}

	public String getInvestigatorName() {
		return investigatorName;
	}

	public void setInvestigatorName(String investigatorName) {
		this.investigatorName = investigatorName;
	}

	public String getTerminateCover() {
		return terminateCover;
	}

	public void setTerminateCover(String terminateCover) {
		this.terminateCover = terminateCover;
	}

	public String getTreatmentRemarks() {
		return treatmentRemarks;
	}

	public void setTreatmentRemarks(String treatmentRemarks) {
		this.treatmentRemarks = treatmentRemarks;
	}

	public String getNegotiationWith() {
		return negotiationWith;
	}

	public void setNegotiationWith(String negotiationWith) {
		this.negotiationWith = negotiationWith;
	}		
	
}

package com.shaic.claim.reimbursement.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.preauth.wizard.dto.SpecialityDTO;
import com.shaic.claim.premedical.dto.NoOfDaysCell;
import com.shaic.domain.ReferenceTable;

public class ReimbursementDataExtractionDTO implements Serializable {
	private static final long serialVersionUID = 4250016404942350745L;

	private String reasonForAdmission;
	
	//@NotNull(message = "Please Select Admission Date.")
	@NotNull(message = "Please Select Date of Admission")
	private Date admissionDate;
	
	@NotNull(message="Please Select Discharge Date")
	private Date dischargeDate;
	
	private String noOfDays;
	
	private SelectValue natureOfTreatment;
	
	@NotNull(message = "Please Enter atleast one Diagnosis.")
	private String diagnosis;
	
	private Date firstConsultantDate;
	
	private Boolean corpBuffer;

	private Long corporateBufferFlag;
	
	private Boolean criticalIllness;
	
	private Long criticalIllnessFlag;
	
	private SelectValue specifyIllness;
	
	private String terminateCoverFlag;
	
	private String autoRestoration;
	
	private SelectValue illness;
	
	@NotNull(message = "Please Choose Date of Death.")
	private Date deathDate;
	
	@NotNull(message = "Please Enter Reason for Death.")
	private String reasonForDeath;
	
	@NotNull(message = "Please Select Terminate Cover.")
	private SelectValue terminateCover;
	
	@NotNull(message = "Please Select Room Category.")
	private SelectValue roomCategory;
	
	@NotNull(message = "Please Select Treatment Type.")
	private SelectValue treatmentType;
	
	@NotNull(message = "Please Enter Treatment Remarks.")
	@Size(min = 1 , message = "Please Enter Treatment Remarks")
	private String treatmentRemarks;
	
	//New field for treatmentRemarks which is disabled.
	@NotNull(message = "Please Enter Treatment Remarks.")
	@Size(min = 1 , message = "Please Enter Treatment Remarks")
	private String enhancementTreatmentRemarks;
	
	@NotNull(message = "Please Enter patientStatus.")
	private SelectValue patientStatus;

	private Boolean enhancementType;
	
	private SelectValue sumInsured;
	
	private SelectValue sublimitSumInsured;
	
	private List<ProcedureDTO> procedureList;
	
	private List<ProcedureDTO> newProcedureList;
	
	private List<SpecialityDTO> specialityList;
	
	private List<DiagnosisDetailsTableDTO> diagnosisTableList;
	
	private List<NoOfDaysCell> claimedDetailsList;
	
	private String changeOfDOA;
	
	private String approvedAmount;
	
	private Double totalApprAmt;
	
	public ReimbursementDataExtractionDTO()
	{
	  procedureList = new ArrayList<ProcedureDTO>();
	  newProcedureList = new ArrayList<ProcedureDTO>();
	  specialityList = new ArrayList<SpecialityDTO>();
	  claimedDetailsList = new ArrayList<NoOfDaysCell>();
	  diagnosisTableList = new ArrayList<DiagnosisDetailsTableDTO>();
	}


	public String getReasonForAdmission() {
		return reasonForAdmission;
	}

	public void setReasonForAdmission(String reasonForAdmission) {
		this.reasonForAdmission = reasonForAdmission;
	}

	public Date getAdmissionDate() {
		return admissionDate;
	}

	public void setAdmissionDate(Date admissionDate) {
		this.admissionDate = admissionDate;
	}

	public String getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(String noOfDays) {
		this.noOfDays = noOfDays;
	}

	public SelectValue getNatureOfTreatment() {
		return natureOfTreatment;
	}


	
	public SelectValue getIllness() {
		return illness;
	}

	public void setIllness(SelectValue illness) {
		this.illness = illness;
	}

	public void setNatureOfTreatment(SelectValue natureOfTreatment) {
		this.natureOfTreatment = natureOfTreatment;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public Date getFirstConsultantDate() {
		return firstConsultantDate;
	}

	public void setFirstConsultantDate(Date firstConsultantDate) {
		this.firstConsultantDate = firstConsultantDate;
	}


	public Boolean getEnhancementType() {
		return enhancementType;
	}

	public void setEnhancementType(Boolean enhancementType) {
		this.enhancementType = enhancementType;
	}

	public Boolean getCorpBuffer() {
		return corpBuffer;
	}


	public void setCorpBuffer(Boolean corpBuffer) {
		this.corpBuffer = corpBuffer;
		this.corporateBufferFlag = this.corpBuffer != null && this.corpBuffer ? 1l : 0l;
	}

	public Boolean getCriticalIllness() {
		return criticalIllness;
	}

	public void setCriticalIllness(Boolean criticalIllness) {
		this.criticalIllness = criticalIllness;
		
		this.criticalIllnessFlag = this.criticalIllness != null && criticalIllness ? 1l : 0l ;
	}

	public SelectValue getSpecifyIllness() {
		return specifyIllness;
	}


	public List<NoOfDaysCell> getClaimedDetailsList() {
		return claimedDetailsList;
	}

	public void setClaimedDetailsList(List<NoOfDaysCell> claimedDetailsList) {
		this.claimedDetailsList = claimedDetailsList;
	}


	public void setSpecifyIllness(SelectValue specifyIllness) {
		this.specifyIllness = specifyIllness;
	}

	public SelectValue getRoomCategory() {
		return roomCategory;
	}

	public void setRoomCategory(SelectValue roomCategory) {
		this.roomCategory = roomCategory;
	}

	public SelectValue getTreatmentType() {
		return treatmentType;
	}

	public void setTreatmentType(SelectValue treatmentType) {
		this.treatmentType = treatmentType;
	}

	public SelectValue getPatientStatus() {
		return patientStatus;
	}

	public void setPatientStatus(SelectValue patientStatus) {
		this.patientStatus = patientStatus;
	}
	

	public List<SpecialityDTO> getSpecialityList() {
		return specialityList;
	}

	public void setSpecialityList(List<SpecialityDTO> specialityList) {
		this.specialityList = specialityList;
	}


	public String getTreatmentRemarks() {
		return treatmentRemarks;
	}

	public void setTreatmentRemarks(String treatmentRemarks) {
		this.treatmentRemarks = treatmentRemarks;
	}

	public SelectValue getSumInsured() {
		return sumInsured;
	}

	public void setSumInsured(SelectValue sumInsured) {
		this.sumInsured = sumInsured;
	}

	public List<DiagnosisDetailsTableDTO> getDiagnosisTableList() {
		return diagnosisTableList;
	}

	public void setDiagnosisTableList(List<DiagnosisDetailsTableDTO> diagnosisTableList) {
		this.diagnosisTableList = diagnosisTableList;
	}

	public SelectValue getSublimitSumInsured() {
		return sublimitSumInsured;
	}

	public void setSublimitSumInsured(SelectValue sublimitSumInsured) {
		this.sublimitSumInsured = sublimitSumInsured;
	}
	
	public Date getDischargeDate() {
		return dischargeDate;
	}

	public void setDischargeDate(Date dischargeDate) {
		this.dischargeDate = dischargeDate;
	}
	
	public Date getDeathDate() {
		return deathDate;
	}

	public void setDeathDate(Date deathDate) {
		this.deathDate = deathDate;
	}

	public List<ProcedureDTO> getProcedureList() {
		return procedureList;
	}

	public void setProcedureList(List<ProcedureDTO> procedureList) {
		this.procedureList = procedureList;
	}

	public List<ProcedureDTO> getNewProcedureList() {
		return newProcedureList;
	}

	public void setNewProcedureList(List<ProcedureDTO> newProcedureList) {
		this.newProcedureList = newProcedureList;
	}

	public String getReasonForDeath() {
		return reasonForDeath;
	}

	public void setReasonForDeath(String reasonForDeath) {
		this.reasonForDeath = reasonForDeath;
	}

	public SelectValue getTerminateCover() {
		return terminateCover;
	}

	public void setTerminateCover(SelectValue terminateCover) {
		this.terminateCover = terminateCover;
		this.terminateCoverFlag = this.terminateCover != null && this.terminateCover.getValue().toString().toLowerCase().contains("yes") ? "Y" : "N";
	}

	public String getAutoRestoration() {
		return autoRestoration;
	}

	public void setAutoRestoration(String autoRestoration) {
		this.autoRestoration = autoRestoration;
	}

	public Long getCriticalIllnessFlag() {
		return criticalIllnessFlag;
	}

	public void setCriticalIllnessFlag(Long criticalIllnessFlag) {
		this.criticalIllnessFlag = criticalIllnessFlag;
		if(this.criticalIllnessFlag != null && this.criticalIllnessFlag == 1) {
			this.criticalIllness = true;
		}
	}

	public Long getCorporateBufferFlag() {
		return corporateBufferFlag;
	}

	public void setCorporateBufferFlag(Long corporateBufferFlag) {
		this.corporateBufferFlag = corporateBufferFlag;
		if(this.corporateBufferFlag != null && this.corporateBufferFlag == 1) {
			this.corpBuffer = true;
		}
	}

	public String getTerminateCoverFlag() {
		return terminateCoverFlag;
	}

	public void setTerminateCoverFlag(String terminateCoverFlag) {
		this.terminateCoverFlag = terminateCoverFlag;
		this.terminateCover = new SelectValue();
		this.terminateCover.setId((this.terminateCoverFlag != null && this.terminateCoverFlag.toLowerCase().equalsIgnoreCase("y")) ? ReferenceTable.COMMONMASTER_YES : ReferenceTable.COMMONMASTER_NO);
	}

	public String getChangeOfDOA() {
		return changeOfDOA;
	}

	public void setChangeOfDOA(String changeOfDOA) {
		this.changeOfDOA = changeOfDOA;
	}

	public String getApprovedAmount() {
		return approvedAmount;
	}

	public void setApprovedAmount(String approvedAmount) {
		this.approvedAmount = approvedAmount;
	}


	public String getEnhancementTreatmentRemarks() {
		return enhancementTreatmentRemarks;
	}

	public void setEnhancementTreatmentRemarks(String enhancementTreatmentRemarks) {
		this.enhancementTreatmentRemarks = enhancementTreatmentRemarks;
	}

	public Double getTotalApprAmt() {
		return totalApprAmt;
	}

	public void setTotalApprAmt(Double totalApprAmt) {
		this.totalApprAmt = totalApprAmt;
	}

}

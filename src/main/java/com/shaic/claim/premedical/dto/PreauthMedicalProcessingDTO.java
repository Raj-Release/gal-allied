package com.shaic.claim.premedical.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.domain.ReferenceTable;

public class PreauthMedicalProcessingDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6218543431145963875L;
	
	@NotNull(message = "Please Enter Query Remarks")
	private String queryRemarks;
	
	@NotNull(message = "Please Enter Reason for Referring To Bill Entry")
	private String refBillEntyRsn;
	
	@NotNull(message = "Please Enter Rejection Remarks")
	private String rejectionRemarks;
	
//	@NotNull(message = "Please Enter Pre-Medical Remarks")
	private String medicalRemarks;
	
//	@NotNull(message = "Please Enter Approval Remarks")
	private String approvalRemarks;
	
	@NotNull(message = "Please Enter Billing Remarks")
	private String billingRemarks;
	
	@NotNull(message = "Please Select Category")
	private SelectValue category;
	
	private String zonalRemarks;
	
	private String corporateRemarks;
	
	private String rejectionRemarks2;
	
	private List<PedDetailsTableDTO> pedValidationTableList;
	
	private List<ProcedureDTO> procedureExclusionCheckTableList;
	
	private SelectValue changeInPreauth;
	
	private String changeInPreauthFlag;
	
	@NotNull(message = "Please Select Reason for Cancellation")
	private SelectValue cancellationReason;
	
	@NotNull(message = "Please Enter Cancellation Remarks")
	@Size(min = 1, message = "Please Enter Cancellation Remarks")
	private String cancelRemarks;
	
	private String vbPaymentStatus;
	
	private String vbApprovalStatus;
	
	private String vbApprovalRemark;
	private String pedDisabilityFlag;
	
	private Boolean pedDisability;
	
	private String pedDisabilityDetails;
	
	@NotNull(message = "Please Choose Allocation to.")
	private SelectValue allocationTo;
	
	@NotNull(message= "Please Choose Priority")
	private SelectValue priority;
	
	private String holdRemarks;
	
	public PreauthMedicalProcessingDTO()
	{
		pedValidationTableList = new ArrayList<PedDetailsTableDTO>();
		procedureExclusionCheckTableList = new ArrayList<ProcedureDTO>();
	}

	public String getQueryRemarks() {
		return queryRemarks;
	}

	public void setQueryRemarks(String queryRemarks) {
		this.queryRemarks = queryRemarks;
	}

	public String getRejectionRemarks() {
		return rejectionRemarks;
	}

	public void setRejectionRemarks(String rejectionRemarks) {
		this.rejectionRemarks = rejectionRemarks;
	}

	public String getMedicalRemarks() {
		return medicalRemarks;
	}

	public void setMedicalRemarks(String medicalRemarks) {
		this.medicalRemarks = medicalRemarks;
	}

	public SelectValue getCategory() {
		return category;
	}

	public void setCategory(SelectValue category) {
		this.category = category;
	}

	public List<PedDetailsTableDTO> getPedValidationTableList() {
		return pedValidationTableList;
	}

	public void setPedValidationTableList(
			List<PedDetailsTableDTO> pedValidationTableList) {
		this.pedValidationTableList = pedValidationTableList;
	}

	public List<ProcedureDTO> getProcedureExclusionCheckTableList() {
		return procedureExclusionCheckTableList;
	}

	public void setProcedureExclusionCheckTableList(
			List<ProcedureDTO> procedureExclusionCheckTableList) {
		this.procedureExclusionCheckTableList = procedureExclusionCheckTableList;
	}

	public SelectValue getChangeInPreauth() {
		return changeInPreauth;
	}

	public void setChangeInPreauth(SelectValue changeInPreauth) {
		this.changeInPreauth = changeInPreauth;
		this.changeInPreauthFlag = this.changeInPreauth != null && this.changeInPreauth.getValue() != null && this.changeInPreauth.getValue().toString().toLowerCase().contains("yes") ? "Y" : "N";
	}

	public String getChangeInPreauthFlag() {
		return changeInPreauthFlag;
	}

	public void setChangeInPreauthFlag(String changeInPreauthFlag) {
		this.changeInPreauthFlag = changeInPreauthFlag;
		this.changeInPreauth = new SelectValue();
		this.changeInPreauth.setId((this.changeInPreauthFlag != null && this.changeInPreauthFlag.equalsIgnoreCase("Y")) ? ReferenceTable.COMMONMASTER_YES : ReferenceTable.COMMONMASTER_NO);
	}

	public String getApprovalRemarks() {
		return approvalRemarks;
	}

	public void setApprovalRemarks(String approvalRemarks) {
		this.approvalRemarks = approvalRemarks;
	}

	public String getBillingRemarks() {
		return billingRemarks;
	}

	public void setBillingRemarks(String billingRemarks) {
		this.billingRemarks = billingRemarks;
	}

	public String getZonalRemarks() {
		return zonalRemarks;
	}

	public void setZonalRemarks(String zonalRemarks) {
		this.zonalRemarks = zonalRemarks;
	}

	public String getCorporateRemarks() {
		return corporateRemarks;
	}

	public void setCorporateRemarks(String corporateRemarks) {
		this.corporateRemarks = corporateRemarks;
	}

	public SelectValue getCancellationReason() {
		return cancellationReason;
	}

	public void setCancellationReason(SelectValue cancellationReason) {
		this.cancellationReason = cancellationReason;
	}

	public String getCancelRemarks() {
		return cancelRemarks;
	}

	public void setCancelRemarks(String cancelRemarks) {
		this.cancelRemarks = cancelRemarks;
	}

	

	public String getVbPaymentStatus() {
		return vbPaymentStatus;
	}

	public void setVbPaymentStatus(String vbPaymentStatus) {
		this.vbPaymentStatus = vbPaymentStatus;
	}

	public String getVbApprovalStatus() {
		return vbApprovalStatus;
	}

	public void setVbApprovalStatus(String vbApprovalStatus) {
		this.vbApprovalStatus = vbApprovalStatus;
	}

	public String getVbApprovalRemark() {
		return vbApprovalRemark;
	}

	public void setVbApprovalRemark(String vbApprovalRemark) {
		this.vbApprovalRemark = vbApprovalRemark;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getPedDisabilityFlag() {
		return pedDisabilityFlag;
	}

	public void setPedDisabilityFlag(String pedDisabilityFlag) {
		this.pedDisabilityFlag = pedDisabilityFlag;
		this.pedDisability = (pedDisabilityFlag != null && pedDisabilityFlag.equalsIgnoreCase("Y")) ? true : false;
	}

	public Boolean getPedDisability() {
		return pedDisability;
	}

	public void setPedDisability(Boolean pedDisability) {
		this.pedDisability = pedDisability;
		this.pedDisabilityFlag = (pedDisability != null && pedDisability) ? "Y" : "N";
	}

	public String getPedDisabilityDetails() {
		return pedDisabilityDetails;
	}

	public void setPedDisabilityDetails(String pedDisabilityDetails) {
		this.pedDisabilityDetails = pedDisabilityDetails;
	}

	public SelectValue getAllocationTo() {
		return allocationTo;
	}

	public void setAllocationTo(SelectValue allocationTo) {
		this.allocationTo = allocationTo;
	}

	public SelectValue getPriority() {
		return priority;
	}

	public void setPriority(SelectValue priority) {
		this.priority = priority;
	}

	public String getRefBillEntyRsn() {
		return refBillEntyRsn;
	}
	
	public void setRefBillEntyRsn(String refBillEntyRsn) {
		this.refBillEntyRsn = refBillEntyRsn;
	}

	public String getHoldRemarks() {
		return holdRemarks;
	}

	public void setHoldRemarks(String holdRemarks) {
		this.holdRemarks = holdRemarks;
	}

	public String getRejectionRemarks2() {
		return rejectionRemarks2;
	}

	public void setRejectionRemarks2(String rejectionRemarks2) {
		this.rejectionRemarks2 = rejectionRemarks2;
	}	
	
	
}

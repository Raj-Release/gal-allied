package com.shaic.claim.status.view;

import java.io.Serializable;

public class ViewStatusDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String referenceNo;
	private String diagnosis;
	private String procedure;
	private String reqAmt;
	private String status;
	private String approvedAmt;
	private String approvalRemarks;

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public String getProcedure() {
		return procedure;
	}

	public void setProcedure(String procedure) {
		this.procedure = procedure;
	}

	public String getReqAmt() {
		return reqAmt;
	}

	public void setReqAmt(String reqAmt) {
		this.reqAmt = reqAmt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getApprovedAmt() {
		return approvedAmt;
	}

	public void setApprovedAmt(String approvedAmt) {
		this.approvedAmt = approvedAmt;
	}

	public String getApprovalRemarks() {
		return approvalRemarks;
	}

	public void setApprovalRemarks(String approvalRemarks) {
		this.approvalRemarks = approvalRemarks;
	}

}

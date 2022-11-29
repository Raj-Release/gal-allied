package com.shaic.reimbursement.manageclaim.ClaimWiseAllowApprovalPages;

import java.io.Serializable;

import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;



public class ClaimWiseApprovalDto extends AbstractSearchDTO implements Serializable{

private String approvalComments;
	
	private String allowedApproval;
	
	private String intimationNo;
	
	private String preauthOrRodNo;
	
	private String documentReceivedFrom;
	
	private String billClassification;
	
	private Double amountClaimed;
	
	private String status;

	public String getApprovalComments() {
		return approvalComments;
	}

	public void setApprovalComments(String approvalComments) {
		this.approvalComments = approvalComments;
	}

	public String getAllowedApproval() {
		return allowedApproval;
	}

	public void setAllowedApproval(String allowedApproval) {
		this.allowedApproval = allowedApproval;
	}

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public String getPreauthOrRodNo() {
		return preauthOrRodNo;
	}

	public void setPreauthOrRodNo(String preauthOrRodNo) {
		this.preauthOrRodNo = preauthOrRodNo;
	}

	public String getDocumentReceivedFrom() {
		return documentReceivedFrom;
	}

	public void setDocumentReceivedFrom(String documentReceivedFrom) {
		this.documentReceivedFrom = documentReceivedFrom;
	}

	public String getBillClassification() {
		return billClassification;
	}

	public void setBillClassification(String billClassification) {
		this.billClassification = billClassification;
	}

	public Double getAmountClaimed() {
		return amountClaimed;
	}

	public void setAmountClaimed(Double amountClaimed) {
		this.amountClaimed = amountClaimed;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}  
}

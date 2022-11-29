package com.shaic.restservices.bancs.claimprovision;

public class ClaimProvisionServiceRequest {

	private String requestId;
	private String claimIntimationNumber;
	private String claimIntimationDate;
	private String lossDate;
	private String transactionDate;
	private String policyNumber;
	private String lob;
	private Integer provisionAmount;
	private Integer provisionReversalAmount;
	
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getClaimIntimationNumber() {
		return claimIntimationNumber;
	}
	public void setClaimIntimationNumber(String claimIntimationNumber) {
		this.claimIntimationNumber = claimIntimationNumber;
	}
	public String getClaimIntimationDate() {
		return claimIntimationDate;
	}
	public void setClaimIntimationDate(String claimIntimationDate) {
		this.claimIntimationDate = claimIntimationDate;
	}
	public String getLossDate() {
		return lossDate;
	}
	public void setLossDate(String lossDate) {
		this.lossDate = lossDate;
	}
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getPolicyNumber() {
		return policyNumber;
	}
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
	public String getLob() {
		return lob;
	}
	public void setLob(String lob) {
		this.lob = lob;
	}
	public Integer getProvisionAmount() {
		return provisionAmount;
	}
	public void setProvisionAmount(Integer provisionAmount) {
		this.provisionAmount = provisionAmount;
	}
	public Integer getProvisionReversalAmount() {
		return provisionReversalAmount;
	}
	public void setProvisionReversalAmount(Integer provisionReversalAmount) {
		this.provisionReversalAmount = provisionReversalAmount;
	}
	
}

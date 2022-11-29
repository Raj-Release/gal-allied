package com.shaic.restservices.bancs.claimprovision;

public class AdjustInstallmentAmountRequest {

	private String serviceTransactionId;
	private String businessChannel;
	private String userCode;
	private String roleCode;
	private String policyNumber;
	private String intimationNo;
	private Integer cpuCode;
	private Integer adjustmentAmount;
	private String adjustmentDate;
	
	public String getServiceTransactionId() {
		return serviceTransactionId;
	}
	public void setServiceTransactionId(String serviceTransactionId) {
		this.serviceTransactionId = serviceTransactionId;
	}
	public String getBusinessChannel() {
		return businessChannel;
	}
	public void setBusinessChannel(String businessChannel) {
		this.businessChannel = businessChannel;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getRoleCode() {
		return roleCode;
	}
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
	public String getPolicyNumber() {
		return policyNumber;
	}
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public Integer getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(Integer cpuCode) {
		this.cpuCode = cpuCode;
	}
	public Integer getAdjustmentAmount() {
		return adjustmentAmount;
	}
	public void setAdjustmentAmount(Integer adjustmentAmount) {
		this.adjustmentAmount = adjustmentAmount;
	}
	public String getAdjustmentDate() {
		return adjustmentDate;
	}
	public void setAdjustmentDate(String adjustmentDate) {
		this.adjustmentDate = adjustmentDate;
	}
	
}

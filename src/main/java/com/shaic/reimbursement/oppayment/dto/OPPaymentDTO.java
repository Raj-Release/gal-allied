package com.shaic.reimbursement.oppayment.dto;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

public class OPPaymentDTO extends AbstractTableDTO implements Serializable{
	
	private static final long serialVersionUID = 805207001992276413L;
	
	private Long key;
	private String lotNo;
	private String rodNo;
	private String intimationNo;
	private String claimNo;
	private Long riskId;
	private String policyNo;
	private String claimType;
	private String legalHeirName;
	private Long paymentCpuCode;
	private String cpuZone;
	private String productCode;
	private String paymentType;
	private Double approvedAmount;
	private Long cpuCode;
	private String ifscCode;
	private String accountNumber;
	private String branchName;
	private String payeeName;
	private String payableAt;
	private String panNumber;
	private String hospitalCode;
	private String emailId;
	private String bankCode;
	private Date chequeDDDate;
	private String chequeDDNumber;
	private String bankName;
	private Double netAmount;
	private Date faApprovalDate;
	private String reasonForChange;
	private Long stageId;
	private Long statusId;
	private Date createdDate;
	private String createdBy;
	private String modifiedBy;
	private Date modifiedDate;
	
	public Long getKey() {
		return key;
	}
	public void setKey(Long key) {
		this.key = key;
	}
	public String getLotNo() {
		return lotNo;
	}
	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}
	public String getRodNo() {
		return rodNo;
	}
	public void setRodNo(String rodNo) {
		this.rodNo = rodNo;
	}
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public String getClaimNo() {
		return claimNo;
	}
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}
	public Long getRiskId() {
		return riskId;
	}
	public void setRiskId(Long riskId) {
		this.riskId = riskId;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public String getLegalHeirName() {
		return legalHeirName;
	}
	public void setLegalHeirName(String legalHeirName) {
		this.legalHeirName = legalHeirName;
	}
	public Long getPaymentCpuCode() {
		return paymentCpuCode;
	}
	public void setPaymentCpuCode(Long paymentCpuCode) {
		this.paymentCpuCode = paymentCpuCode;
	}
	public String getCpuZone() {
		return cpuZone;
	}
	public void setCpuZone(String cpuZone) {
		this.cpuZone = cpuZone;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public Double getApprovedAmount() {
		return approvedAmount;
	}
	public void setApprovedAmount(Double approvedAmount) {
		this.approvedAmount = approvedAmount;
	}
	public Long getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(Long cpuCode) {
		this.cpuCode = cpuCode;
	}
	public String getIfscCode() {
		return ifscCode;
	}
	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getPayeeName() {
		return payeeName;
	}
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}
	public String getPayableAt() {
		return payableAt;
	}
	public void setPayableAt(String payableAt) {
		this.payableAt = payableAt;
	}
	public String getPanNumber() {
		return panNumber;
	}
	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}
	public String getHospitalCode() {
		return hospitalCode;
	}
	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public Date getChequeDDDate() {
		return chequeDDDate;
	}
	public void setChequeDDDate(Date chequeDDDate) {
		this.chequeDDDate = chequeDDDate;
	}
	public String getChequeDDNumber() {
		return chequeDDNumber;
	}
	public void setChequeDDNumber(String chequeDDNumber) {
		this.chequeDDNumber = chequeDDNumber;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public Double getNetAmount() {
		return netAmount;
	}
	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}
	public Date getFaApprovalDate() {
		return faApprovalDate;
	}
	public void setFaApprovalDate(Date faApprovalDate) {
		this.faApprovalDate = faApprovalDate;
	}
	public String getReasonForChange() {
		return reasonForChange;
	}
	public void setReasonForChange(String reasonForChange) {
		this.reasonForChange = reasonForChange;
	}
	public Long getStageId() {
		return stageId;
	}
	public void setStageId(Long stageId) {
		this.stageId = stageId;
	}
	public Long getStatusId() {
		return statusId;
	}
	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	

}

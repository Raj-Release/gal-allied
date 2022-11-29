package com.shaic.claim;

import com.shaic.arch.table.AbstractTableDTO;

public class ViewNegotiationDetailsDTO extends AbstractTableDTO {
	
	private String intimationStage;
	private Double negotiatedAmt;
	private Double savedAmt;
	private String negotiatedUpdateBy;
	private String updateDate;
	private String intimationNo;
	private String cashlessorReimNo;
	private String userId;
	private String cpucodeName;
	private Double claimApprovedAmt;
	private String referenceNo;
	private String stageValue;
	private String statusValue;
	private Boolean updateNegotiation = false;
	private Long intimationKey;
	private Long latestTransKey;
	private Long cpuKey;
	private String transactionFlag;
	
	
	public String getStageValue() {
		return stageValue;
	}
	public void setStageValue(String stageValue) {
		this.stageValue = stageValue;
	}
	public String getStatusValue() {
		return statusValue;
	}
	public void setStatusValue(String statusValue) {
		this.statusValue = statusValue;
	}
	public String getIntimationStage() {
		return intimationStage;
	}
	public void setIntimationStage(String intimationStage) {
		this.intimationStage = intimationStage;
	}
	public Double getNegotiatedAmt() {
		return negotiatedAmt;
	}
	public void setNegotiatedAmt(Double negotiatedAmt) {
		this.negotiatedAmt = negotiatedAmt;
	}
	public Double getSavedAmt() {
		return savedAmt;
	}
	public void setSavedAmt(Double savedAmt) {
		this.savedAmt = savedAmt;
	}
	public String getNegotiatedUpdateBy() {
		return negotiatedUpdateBy;
	}
	public void setNegotiatedUpdateBy(String negotiatedUpdateBy) {
		this.negotiatedUpdateBy = negotiatedUpdateBy;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public String getCashlessorReimNo() {
		return cashlessorReimNo;
	}
	public void setCashlessorReimNo(String cashlessorReimNo) {
		this.cashlessorReimNo = cashlessorReimNo;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCpucodeName() {
		return cpucodeName;
	}
	public void setCpucodeName(String cpucodeName) {
		this.cpucodeName = cpucodeName;
	}
	public Double getClaimApprovedAmt() {
		return claimApprovedAmt;
	}
	public void setClaimApprovedAmt(Double claimApprovedAmt) {
		this.claimApprovedAmt = claimApprovedAmt;
	}
	public String getReferenceNo() {
		return referenceNo;
	}
	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}
	public Boolean getUpdateNegotiation() {
		return updateNegotiation;
	}
	public void setUpdateNegotiation(Boolean updateNegotiation) {
		this.updateNegotiation = updateNegotiation;
	}
	public Long getIntimationKey() {
		return intimationKey;
	}
	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
	}
	public Long getLatestTransKey() {
		return latestTransKey;
	}
	public void setLatestTransKey(Long latestTransKey) {
		this.latestTransKey = latestTransKey;
	}
	public Long getCpuKey() {
		return cpuKey;
	}
	public void setCpuKey(Long cpuKey) {
		this.cpuKey = cpuKey;
	}
	public String getTransactionFlag() {
		return transactionFlag;
	}
	public void setTransactionFlag(String transactionFlag) {
		this.transactionFlag = transactionFlag;
	}
	
	

}

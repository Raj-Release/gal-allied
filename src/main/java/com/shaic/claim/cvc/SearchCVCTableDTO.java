package com.shaic.claim.cvc;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;

public class SearchCVCTableDTO extends AbstractTableDTO  implements Serializable{
	
	private Long intimationKey;
	private Long claimKey;
	private Long transactionKey;	
	private String claimType;
	private String auditRemarks;
	private String auditStatus;
	private Object team;
	private Object errorCategory;
	private String monetaryResult;
	private Object processor;
	private String remediationStatus;
	private String amountInvolved;
	private String remediationRemarks;
	private String clsQryRemarks;
	private String medicaQrylRemarks;
	private String billingFaQryRemarks;
	private PreauthDTO preauthDto;
	private RRCDTO rrcDto;
	private String claimedAmount;
	private String message;
	private String otherRemarks;
	private String auditReason;
	private Long stageKey;

	private String qryStatus;
	private String year;
	
	public Long getStageKey() {
		return stageKey;
	}
	public void setStageKey(Long stageKey) {
		this.stageKey = stageKey;
	}
	
	public Long getIntimationKey() {
		return intimationKey;
	}
	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
	}
	public Long getClaimKey() {
		return claimKey;
	}
	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}
	public Long getTransactionKey() {
		return transactionKey;
	}
	public void setTransactionKey(Long transactionKey) {
		this.transactionKey = transactionKey;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public String getAuditRemarks() {
		return auditRemarks;
	}
	public void setAuditRemarks(String auditRemarks) {
		this.auditRemarks = auditRemarks;
	}
	public PreauthDTO getPreauthDto() {
		return preauthDto;
	}
	public void setPreauthDto(PreauthDTO preauthDto) {
		this.preauthDto = preauthDto;
	}
	public RRCDTO getRrcDto() {
		return rrcDto;
	}
	public void setRrcDto(RRCDTO rrcDto) {
		this.rrcDto = rrcDto;
	}
	public String getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}
	public Object getTeam() {
		return team;
	}
	public void setTeam(Object team) {
		this.team = team;
	}
	public Object getErrorCategory() {
		return errorCategory;
	}
	public void setErrorCategory(Object errorCategory) {
		this.errorCategory = errorCategory;
	}
	public String getMonetaryResult() {
		return monetaryResult;
	}
	public void setMonetaryResult(String monetaryResult) {
		this.monetaryResult = monetaryResult;
	}
	public Object getProcessor() {
		return processor;
	}
	public void setProcessor(Object processor) {
		this.processor = processor;
	}
	public String getRemediationStatus() {
		return remediationStatus;
	}
	public void setRemediationStatus(String remediationStatus) {
		this.remediationStatus = remediationStatus;
	}
	public String getAmountInvolved() {
		return amountInvolved;
	}
	public void setAmountInvolved(String amountInvolved) {
		this.amountInvolved = amountInvolved;
	}
	public String getRemediationRemarks() {
		return remediationRemarks;
	}
	public void setRemediationRemarks(String remediationRemarks) {
		this.remediationRemarks = remediationRemarks;
	}
	public String getClaimedAmount() {
		return claimedAmount;
	}
	public void setClaimedAmount(String claimedAmount) {
		this.claimedAmount = claimedAmount;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getOtherRemarks() {
		return otherRemarks;
	}
	public void setOtherRemarks(String otherRemarks) {
		this.otherRemarks = otherRemarks;
	}
	public String getAuditReason() {
		return auditReason;
	}
	public void setAuditReason(String auditReason) {
		this.auditReason = auditReason;
	}
	public String getClsQryRemarks() {
		return clsQryRemarks;
	}
	public void setClsQryRemarks(String clsQryRemarks) {
		this.clsQryRemarks = clsQryRemarks;
	}
	public String getMedicaQrylRemarks() {
		return medicaQrylRemarks;
	}
	public void setMedicaQrylRemarks(String medicaQrylRemarks) {
		this.medicaQrylRemarks = medicaQrylRemarks;
	}
	public String getBillingFaQryRemarks() {
		return billingFaQryRemarks;
	}
	public void setBillingFaQryRemarks(String billingFaQryRemarks) {
		this.billingFaQryRemarks = billingFaQryRemarks;
	}
	public String getQryStatus() {
		return qryStatus;
	}
	public void setQryStatus(String qryStatus) {
		this.qryStatus = qryStatus;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	
}
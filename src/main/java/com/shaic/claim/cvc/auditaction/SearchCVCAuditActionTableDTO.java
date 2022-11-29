package com.shaic.claim.cvc.auditaction;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.cvc.auditqueryreplyprocessing.cashless.SearchCVCAuditClsQryTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.TextField;

/**
 * @author GokulPrasath.A
 *
 */
public class SearchCVCAuditActionTableDTO extends AbstractTableDTO  implements Serializable{
	
	private Long intimationKey;
	private Long claimKey;
	private Long transactionKey;	
	private String claimType;
	private String auditRemarks;
	private String clsQryRemarks;
	private String clsQryRplRemarks;
	private String medicaQrylRemarks;
	private String reimbQryRplRemarks;
	private String billingFAQryRemarks;
	private String billingFAQryRplRemarks;
	
	private String qryReplyBy;
	private String qryReplyRole;
	
	/*private String qryReplyBy;
	private String qryReplyRole;
	private Date queryRplDt;*/
	
	
	List<SearchCVCAuditClsQryTableDTO> clsQryList;
	List<SearchCVCAuditClsQryTableDTO> medicalQryList;
	List<SearchCVCAuditClsQryTableDTO> billingFaQryList;
	
	/*private String clsAdQryRemarks;
	private String clsAdQryRplRemarks;
	private String reimbAdQryRemarks;
	private String reimbAdQryRplRemarks;
	private String billingFAAdQryRemarks;
	private String billingFAAdQryRplRemarks;*/
	
	private String auditStatus;
	private String team;
	private String errorCategory;
	private String monetaryResult;
	private String processor;
	private String remediationStatus;
	private String amountInvolved;
	private String remediationRemarks;
	private PreauthDTO preauthDto;
	private RRCDTO rrcDto;
	private String intimationNumber;
	private String transactionNumber;
	private String auditBy;
	private String auditDate;
	private String auditFinalStatus;
	private String otherRemarks;
	private String status;
	private String claimedAmount;
	private Long auditKey;
	private String ageing;
	private String qryOutcome;
	private String satisUnSatisRemarks;
	private String completedReason;
	private String completedRemarks;
	private boolean clmAuditHeadUser;
	private String tabStatus;
	
	private String clsAuditQueryRaisedDt;
	private String clmAuditStatus;
	private String auditQueryRaisedDtStr;
	

	private String clsQryStatus;
	private String medicaQryStatus;
	private String billingFAQryStatus;
	
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
	public String getTeam() {
		return team;
	}
	public void setTeam(String team) {
		this.team = team;
	}
	public String getErrorCategory() {
		return errorCategory;
	}
	public void setErrorCategory(String errorCategory) {
		this.errorCategory = errorCategory;
	}
	public String getMonetaryResult() {
		return monetaryResult;
	}
	public void setMonetaryResult(String monetaryResult) {
		this.monetaryResult = monetaryResult;
	}
	public String getProcessor() {
		return processor;
	}
	public void setProcessor(String processor) {
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
	public String getIntimationNumber() {
		return intimationNumber;
	}
	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}
	public String getTransactionNumber() {
		return transactionNumber;
	}
	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}
	public String getAuditBy() {
		return auditBy;
	}
	public void setAuditBy(String auditBy) {
		this.auditBy = auditBy;
	}
	public String getAuditDate() {
		return auditDate;
	}
	public void setAuditDate(String auditDate) {
		this.auditDate = auditDate;
	}
	public String getAuditFinalStatus() {
		return auditFinalStatus;
	}
	public void setAuditFinalStatus(String auditFinalStatus) {
		this.auditFinalStatus = auditFinalStatus;
	}
	public String getOtherRemarks() {
		return otherRemarks;
	}
	public void setOtherRemarks(String otherRemarks) {
		this.otherRemarks = otherRemarks;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getClaimedAmount() {
		return claimedAmount;
	}
	public void setClaimedAmount(String claimedAmount) {
		this.claimedAmount = claimedAmount;
	}
	public Long getAuditKey() {
		return auditKey;
	}
	public void setAuditKey(Long auditKey) {
		this.auditKey = auditKey;
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
	public String getBillingFAQryRemarks() {
		return billingFAQryRemarks;
	}
	public void setBillingFAQryRemarks(String billingFAQryRemarks) {
		this.billingFAQryRemarks = billingFAQryRemarks;
	}
	public String getAgeing() {
		return ageing;
	}
	public void setAgeing(String ageing) {
		this.ageing = ageing;
	}
	public String getQryOutcome() {
		return qryOutcome;
	}
	public void setQryOutcome(String qryOutcome) {
		this.qryOutcome = qryOutcome;
	}
	public String getSatisUnSatisRemarks() {
		return satisUnSatisRemarks;
	}
	public void setSatisUnSatisRemarks(String satisUnSatisRemarks) {
		this.satisUnSatisRemarks = satisUnSatisRemarks;
	}
	public String getClsQryRplRemarks() {
		return clsQryRplRemarks;
	}
	public void setClsQryRplRemarks(String clsQryRplRemarks) {
		this.clsQryRplRemarks = clsQryRplRemarks;
	}
	public String getReimbQryRplRemarks() {
		return reimbQryRplRemarks;
	}
	public void setReimbQryRplRemarks(String reimbQryRplRemarks) {
		this.reimbQryRplRemarks = reimbQryRplRemarks;
	}
	public String getBillingFAQryRplRemarks() {
		return billingFAQryRplRemarks;
	}
	public void setBillingFAQryRplRemarks(String billingFAQryRplRemarks) {
		this.billingFAQryRplRemarks = billingFAQryRplRemarks;
	}
	/*public String getClsAdQryRemarks() {
		return clsAdQryRemarks;
	}
	public void setClsAdQryRemarks(String clsAdQryRemarks) {
		this.clsAdQryRemarks = clsAdQryRemarks;
	}
	public String getClsAdQryRplRemarks() {
		return clsAdQryRplRemarks;
	}
	public void setClsAdQryRplRemarks(String clsAdQryRplRemarks) {
		this.clsAdQryRplRemarks = clsAdQryRplRemarks;
	}
	public String getReimbAdQryRemarks() {
		return reimbAdQryRemarks;
	}
	public void setReimbAdQryRemarks(String reimbAdQryRemarks) {
		this.reimbAdQryRemarks = reimbAdQryRemarks;
	}
	public String getReimbAdQryRplRemarks() {
		return reimbAdQryRplRemarks;
	}
	public void setReimbAdQryRplRemarks(String reimbAdQryRplRemarks) {
		this.reimbAdQryRplRemarks = reimbAdQryRplRemarks;
	}
	public String getBillingFAAdQryRemarks() {
		return billingFAAdQryRemarks;
	}
	public void setBillingFAAdQryRemarks(String billingFAAdQryRemarks) {
		this.billingFAAdQryRemarks = billingFAAdQryRemarks;
	}
	public String getBillingFAAdQryRplRemarks() {
		return billingFAAdQryRplRemarks;
	}
	public void setBillingFAAdQryRplRemarks(String billingFAAdQryRplRemarks) {
		this.billingFAAdQryRplRemarks = billingFAAdQryRplRemarks;
	}
	public Long getQryKey() {
		return qryKey;
	}
	public void setQryKey(Long qryKey) {
		this.qryKey = qryKey;
	}
	public String getQryStatus() {
		return qryStatus;
	}
	public void setQryStatus(String qryStatus) {
		this.qryStatus = qryStatus;
	}
	
	public Date getQueryRplDt() {
		return queryRplDt;
	}
	public void setQueryRplDt(Date queryRplDt) {
		this.queryRplDt = queryRplDt;
	}*/
	
	public String getQryReplyBy() {
		return qryReplyBy;
	}
	public void setQryReplyBy(String qryReplyBy) {
		this.qryReplyBy = qryReplyBy;
	}
	public String getQryReplyRole() {
		return qryReplyRole;
	}
	public void setQryReplyRole(String qryReplyRole) {
		this.qryReplyRole = qryReplyRole;
	}
	public List<SearchCVCAuditClsQryTableDTO> getClsQryList() {
		return clsQryList;
	}
	public void setClsQryList(List<SearchCVCAuditClsQryTableDTO> clsQryList) {
		this.clsQryList = clsQryList;
	}
	public List<SearchCVCAuditClsQryTableDTO> getMedicalQryList() {
		return medicalQryList;
	}
	public void setMedicalQryList(List<SearchCVCAuditClsQryTableDTO> medicalQryList) {
		this.medicalQryList = medicalQryList;
	}
	public List<SearchCVCAuditClsQryTableDTO> getBillingFaQryList() {
		return billingFaQryList;
	}
	public void setBillingFaQryList(
			List<SearchCVCAuditClsQryTableDTO> billingFaQryList) {
		this.billingFaQryList = billingFaQryList;
	}
	public boolean isClmAuditHeadUser() {
		return clmAuditHeadUser;
	}
	public void setClmAuditHeadUser(boolean clmAuditHeadUser) {
		this.clmAuditHeadUser = clmAuditHeadUser;
	}

	public String getClsAuditQueryRaisedDt() {
		return clsAuditQueryRaisedDt;
	}
	public void setClsAuditQueryRaisedDt(String clsAuditQueryRaisedDt) {
		this.clsAuditQueryRaisedDt = clsAuditQueryRaisedDt;
	}
	public String getClmAuditStatus() {
		return clmAuditStatus;
	}
	public void setClmAuditStatus(String clmAuditStatus) {
		this.clmAuditStatus = clmAuditStatus;
	}
	public String getTabStatus() {
		return tabStatus;
	}
	public void setTabStatus(String tabStatus) {
		this.tabStatus = tabStatus;
	}
	public String getAuditQueryRaisedDtStr() {
		return auditQueryRaisedDtStr;
	}
	public void setAuditQueryRaisedDtStr(String auditQueryRaisedDtStr) {
		this.auditQueryRaisedDtStr = auditQueryRaisedDtStr;
	}
	public String getCompletedReason() {
		return completedReason;
	}
	public void setCompletedReason(String completedReason) {
		this.completedReason = completedReason;
	}
	public String getCompletedRemarks() {
		return completedRemarks;
	}
	public void setCompletedRemarks(String completedRemarks) {
		this.completedRemarks = completedRemarks;
	}
	public String getClsQryStatus() {
		return clsQryStatus;
	}
	public void setClsQryStatus(String clsQryStatus) {
		this.clsQryStatus = clsQryStatus;
	}
	public String getMedicaQryStatus() {
		return medicaQryStatus;
	}
	public void setMedicaQryStatus(String medicaQryStatus) {
		this.medicaQryStatus = medicaQryStatus;
	}
	public String getBillingFAQryStatus() {
		return billingFAQryStatus;
	}
	public void setBillingFAQryStatus(String billingFAQryStatus) {
		this.billingFAQryStatus = billingFAQryStatus;
	}
		
	
}
package com.shaic.domain.preauth;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.shaic.arch.fields.dto.AbstractEntity;

@Entity
@Table(name = "IMS_CLS_INTERNAL_INFO")
@NamedQueries({
	@NamedQuery(name = "InternalInfo.findByIntimationKey", query = "SELECT i FROM InternalInfo i WHERE i.intimationKey = :intimationKey AND i.stageId = :stageId ORDER BY i.createdDate DESC")
})

public class InternalInfo extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="STAGE_INFORMATION_KEY")
	private Long key;

	@Column(name = "INTIMATION_KEY")
	private Long intimationKey;

	@Column(name = "CLAIM_TYPE_ID")
	private Long claimTypeId;

	@Column(name = "STAGE_ID")
	private Long stageId;
	
	@Column(name = "STATUS_ID")
	private Long statusId;

	@Column(name = "STATUS_REMARKS")
	private String statusRemarks;
	
	@Column(name = "ACTIVE_STATUS")
	private Long activeRemarks;
	
	@Column(name = "OFFICE_CODE")
	private String officeCode;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "DM_CODE")
	private Long dmCode;

	@Column(name = "CLAIM_KEY")
	private Long claimKey;
	
	@Column(name = "CASHLESS_KEY")
	private Long cashlessKey;
	
	@Column(name = "REIMBURSEMENT_KEY")
	private Long reimbursementKey;
	
	@Column(name = "ALERT_FLAG")
	private String alertFlag;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "HISTORY_DATE")
	private Date historyDate;
	
	@Column(name = "DOC_ACKNOWLEDGEMENT_KEY")
	private Long docAcknowledgementKey;
	
	@Column(name = "SMS_SENT_FLAG")
	private String smsSentFlag;
	
	@Column(name = "EMAIL_SENT_FLAG")
	private String emailSentFlag;
	
	@Column(name = "SMS_SENT_TO")
	private String smsSentTo;
	
	@Column(name = "EMAIL_SENT_TO")
	private String emailSentTo;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SMS_SENT_DATE")
	private Date smsSentDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EMAIL_SENT_DATE")
	private Date emailSentDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BI_DATE")
	private Date biDate;
	
	@Column(name = "REF_NO")
	private String refNo;

	@Column(name = "REMARKS")
	private String remarks;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getIntimationKey() {
		return intimationKey;
	}

	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
	}

	public Long getClaimTypeId() {
		return claimTypeId;
	}

	public void setClaimTypeId(Long claimTypeId) {
		this.claimTypeId = claimTypeId;
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

	public String getStatusRemarks() {
		return statusRemarks;
	}

	public void setStatusRemarks(String statusRemarks) {
		this.statusRemarks = statusRemarks;
	}

	public Long getActiveRemarks() {
		return activeRemarks;
	}

	public void setActiveRemarks(Long activeRemarks) {
		this.activeRemarks = activeRemarks;
	}

	public String getOfficeCode() {
		return officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Long getDmCode() {
		return dmCode;
	}

	public void setDmCode(Long dmCode) {
		this.dmCode = dmCode;
	}

	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}

	public Long getCashlessKey() {
		return cashlessKey;
	}

	public void setCashlessKey(Long cashlessKey) {
		this.cashlessKey = cashlessKey;
	}

	public Long getReimbursementKey() {
		return reimbursementKey;
	}

	public void setReimbursementKey(Long reimbursementKey) {
		this.reimbursementKey = reimbursementKey;
	}

	public String getAlertFlag() {
		return alertFlag;
	}

	public void setAlertFlag(String alertFlag) {
		this.alertFlag = alertFlag;
	}

	public Date getHistoryDate() {
		return historyDate;
	}

	public void setHistoryDate(Date historyDate) {
		this.historyDate = historyDate;
	}

	public Long getDocAcknowledgementKey() {
		return docAcknowledgementKey;
	}

	public void setDocAcknowledgementKey(Long docAcknowledgementKey) {
		this.docAcknowledgementKey = docAcknowledgementKey;
	}

	public String getSmsSentFlag() {
		return smsSentFlag;
	}

	public void setSmsSentFlag(String smsSentFlag) {
		this.smsSentFlag = smsSentFlag;
	}

	public String getEmailSentFlag() {
		return emailSentFlag;
	}

	public void setEmailSentFlag(String emailSentFlag) {
		this.emailSentFlag = emailSentFlag;
	}

	public String getSmsSentTo() {
		return smsSentTo;
	}

	public void setSmsSentTo(String smsSentTo) {
		this.smsSentTo = smsSentTo;
	}

	public String getEmailSentTo() {
		return emailSentTo;
	}

	public void setEmailSentTo(String emailSentTo) {
		this.emailSentTo = emailSentTo;
	}

	public Date getSmsSentDate() {
		return smsSentDate;
	}

	public void setSmsSentDate(Date smsSentDate) {
		this.smsSentDate = smsSentDate;
	}

	public Date getEmailSentDate() {
		return emailSentDate;
	}

	public void setEmailSentDate(Date emailSentDate) {
		this.emailSentDate = emailSentDate;
	}

	public Date getBiDate() {
		return biDate;
	}

	public void setBiDate(Date biDate) {
		this.biDate = biDate;
	}

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	@Override
	public String toString() {
		return "InternalInfo [key=" + key + ", intimationKey=" + intimationKey
				+ ", claimTypeId=" + claimTypeId + ", stageId=" + stageId
				+ ", statusId=" + statusId + ", statusRemarks=" + statusRemarks
				+ ", activeRemarks=" + activeRemarks + ", officeCode="
				+ officeCode + ", createdBy=" + createdBy + ", createdDate="
				+ createdDate + ", dmCode=" + dmCode + ", claimKey=" + claimKey
				+ ", cashlessKey=" + cashlessKey + ", reimbursementKey="
				+ reimbursementKey + ", alertFlag=" + alertFlag
				+ ", historyDate=" + historyDate + ", docAcknowledgementKey="
				+ docAcknowledgementKey + ", smsSentFlag=" + smsSentFlag
				+ ", emailSentFlag=" + emailSentFlag + ", smsSentTo="
				+ smsSentTo + ", emailSentTo=" + emailSentTo + ", smsSentDate="
				+ smsSentDate + ", emailSentDate=" + emailSentDate
				+ ", biDate=" + biDate + ", refNo=" + refNo + "]";
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}

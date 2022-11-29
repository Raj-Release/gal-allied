package com.shaic.reimbursement.paymentprocess.stoppaymenttracking.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.shaic.domain.MastersValue;


@Entity
@Table(name=" IMS_CLS_STOP_PAYMENT_STG_INFO")
@NamedQueries({
	@NamedQuery(name ="StopPaymentTrails.findByrodNo",query="SELECT r FROM StopPaymentTrails r WHERE r.rodNo = :rodNo")
})
public class StopPaymentTrails {
	
	
	@Id
	@Column(name = "STAGE_INFORMATION_KEY")
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
	private Long activeStatus;
	
	@Column(name = "OFFICE_CODE")
	private String officeCode;
	
	@Column(name = "CREATED_BY")
	private String CreatedBy;
	
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
	
	@Column(name = "HISTORY_DATE")
	private Date historyDate;
	
	@Column(name = "DOC_ACKNOWLEDGEMENT_KEY")
	private Long  docAckKey;
	
	@Column(name = "BI_DATE")
	private Date biDate;
	
	@Column(name = "SMS_SENT_FLAG")
	private String smsSentFlag;
	
	@Column(name = "EMAIL_SENT_FLAG")
	private String emailSentFlag;
	
	@Column(name = "SMS_SENT_TO")
	private String smsSentTo;
	
	@Column(name = "EMAIL_SENT_TO")
	private String emailSentTo;
	
	@Column(name = "SMS_SENT_DATE")
	private Date smsSentDate;
	
	@Column(name = "EMAIL_SENT_DATE")
	private Date emailSentDate;
	
	@Column(name = "HOSPITAL_PORTAL_FLAG")
	private String hospitalPortalFlag;
	
	
	
	@Column(name = "HOSPITAL_PORTAL_DATE")
	private Date hospitalPortalDate;
	
	@Column(name = "PORTAL_RESPONSE")
	private String portalResponse;
	
	@Column(name = "PORTAL_RESPONSE_DATETIME")
	private Date portalResponseDateTime;
	
	@Column(name = "PORTAL_RESPONSE_REMARKS")
	private String portalResponseRemarks;
	
	@Column(name = "ROD_NUMBER")
	private String rodNo;
	
	@Column(name = "UTR_NUMBER")
	private String utrNumber;
	
	@Column(name = "INTIMATION_NUMBER")
	private String intimationNo;
	
/*	@Column(name="REQUEST_REASON")
	private Long rerquestReason;*/

	@Column(name="VALIDATE_ACTION")
	private String validaAction;
	
		
	@OneToOne
	@JoinColumn(name = "REQUEST_REASON", nullable=true)
	private MastersValue requestReason;
	
	

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

	public String getRodNo() {
		return rodNo;
	}

	public void setRodNo(String rodNo) {
		this.rodNo = rodNo;
	}



	public String getCreatedBy() {
		return CreatedBy;
	}

	public void setCreatedBy(String createdBy) {
		CreatedBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
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

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Date getHistoryDate() {
		return historyDate;
	}

	public void setHistoryDate(Date historyDate) {
		this.historyDate = historyDate;
	}

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public String getValidaAction() {
		return validaAction;
	}

	public void setValidaAction(String validaAction) {
		this.validaAction = validaAction;
	}

	/*public Long getRerquestReason() {
		return rerquestReason;
	}

	public void setRerquestReason(Long rerquestReason) {
		this.rerquestReason = rerquestReason;
	}*/

	public Long getClaimTypeId() {
		return claimTypeId;
	}

	public void setClaimTypeId(Long claimTypeId) {
		this.claimTypeId = claimTypeId;
	}

	public String getStatusRemarks() {
		return statusRemarks;
	}

	public void setStatusRemarks(String statusRemarks) {
		this.statusRemarks = statusRemarks;
	}

	public String getOfficeCode() {
		return officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
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

	public Long getDocAckKey() {
		return docAckKey;
	}

	public void setDocAckKey(Long docAckKey) {
		this.docAckKey = docAckKey;
	}

	public Date getBiDate() {
		return biDate;
	}

	public void setBiDate(Date biDate) {
		this.biDate = biDate;
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

	public String getHospitalPortalFlag() {
		return hospitalPortalFlag;
	}

	public void setHospitalPortalFlag(String hospitalPortalFlag) {
		this.hospitalPortalFlag = hospitalPortalFlag;
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

	public Date getHospitalPortalDate() {
		return hospitalPortalDate;
	}

	public void setHospitalPortalDate(Date hospitalPortalDate) {
		this.hospitalPortalDate = hospitalPortalDate;
	}

	public String getPortalResponse() {
		return portalResponse;
	}

	public void setPortalResponse(String portalResponse) {
		this.portalResponse = portalResponse;
	}

	public Date getPortalResponseDateTime() {
		return portalResponseDateTime;
	}

	public void setPortalResponseDateTime(Date portalResponseDateTime) {
		this.portalResponseDateTime = portalResponseDateTime;
	}

	public String getPortalResponseRemarks() {
		return portalResponseRemarks;
	}

	public void setPortalResponseRemarks(String portalResponseRemarks) {
		this.portalResponseRemarks = portalResponseRemarks;
	}

	public String getUtrNumber() {
		return utrNumber;
	}

	public void setUtrNumber(String utrNumber) {
		this.utrNumber = utrNumber;
	}

	public MastersValue getRequestReason() {
		return requestReason;
	}

	public void setRequestReason(MastersValue requestReason) {
		this.requestReason = requestReason;
	}

	

	
	

	
	
	
	

	

}

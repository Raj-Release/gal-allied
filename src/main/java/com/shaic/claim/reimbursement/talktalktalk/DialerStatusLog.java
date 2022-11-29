package com.shaic.claim.reimbursement.talktalktalk;

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
@Table(name="IMS_CLS_DIALER_STATUS_LOG")
@NamedQueries({
	@NamedQuery(name="DialerStatusLog.findAll", query="SELECT m FROM DialerStatusLog m"),
	@NamedQuery(name="DialerStatusLog.findByRefId", query="SELECT m FROM DialerStatusLog m where m.callReferenceId = :callReferenceId and m.conCallId = :conCallId"),
})
public class DialerStatusLog extends AbstractEntity{
	
	@Id
	@Column(name="DIALER_STATUS_KEY")
	private Long key;
	
	@Column(name="MOBILE_NUMBER")
	private String mobileNumber;
	
	@Column(name="TSO_CODE")
	private String tsoCode;
	
	@Column(name="CALL_MODE")
	private String callMode;
	
	@Column(name="CALL_STATUS")
	private String callStatus;
	
	@Column(name="CALL_DATE")
	private String callDate;
	
	@Column(name="CALL_HOUR")
	private String callHour;
	
	@Column(name="CALL_MIN")
	private String callMin;
	
	@Column(name="CALL_DURATION")
	private String callDuration;
	
	@Column(name="QUEUE_NAME")
	private String queueName;
	
	@Column(name="LIST_ID")
	private String listId;
	
	@Column(name="CON_CALL_ID")
	private String conCallId;
	
	@Column(name="CALL_REFERENCE_ID")
	private String callReferenceId;
	
	@Column(name="FILE_NAME")
	private String fileName;
	
	@Column(name="LEAD_ID")
	private String leadId;
	
	@Column(name="CONSUMER_NO")
	private String consumerNo;
	
	@Column(name="RING_DURATION")
	private String ringDuration;
	
	@Column(name="ACTIVE_STATUS")
	private String activeStatus;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	public Long getKey() {
		return this.key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getTsoCode() {
		return tsoCode;
	}

	public void setTsoCode(String tsoCode) {
		this.tsoCode = tsoCode;
	}

	public String getCallMode() {
		return callMode;
	}

	public void setCallMode(String callMode) {
		this.callMode = callMode;
	}

	public String getCallStatus() {
		return callStatus;
	}

	public void setCallStatus(String callStatus) {
		this.callStatus = callStatus;
	}

	public String getCallDate() {
		return callDate;
	}

	public void setCallDate(String callDate) {
		this.callDate = callDate;
	}

	public String getCallHour() {
		return callHour;
	}

	public void setCallHour(String callHour) {
		this.callHour = callHour;
	}

	public String getCallMin() {
		return callMin;
	}

	public void setCallMin(String callMin) {
		this.callMin = callMin;
	}

	public String getCallDuration() {
		return callDuration;
	}

	public void setCallDuration(String callDuration) {
		this.callDuration = callDuration;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public String getListId() {
		return listId;
	}

	public void setListId(String listId) {
		this.listId = listId;
	}

	public String getConCallId() {
		return conCallId;
	}

	public void setConCallId(String conCallId) {
		this.conCallId = conCallId;
	}

	public String getCallReferenceId() {
		return callReferenceId;
	}

	public void setCallReferenceId(String callReferenceId) {
		this.callReferenceId = callReferenceId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getLeadId() {
		return leadId;
	}

	public void setLeadId(String leadId) {
		this.leadId = leadId;
	}

	public String getConsumerNo() {
		return consumerNo;
	}

	public void setConsumerNo(String consumerNo) {
		this.consumerNo = consumerNo;
	}

	public String getRingDuration() {
		return ringDuration;
	}

	public void setRingDuration(String ringDuration) {
		this.ringDuration = ringDuration;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
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

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

}

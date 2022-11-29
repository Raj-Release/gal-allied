package com.shaic.claim.reimbursement.talktalktalk;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.shaic.arch.fields.dto.AbstractEntity;

@Entity
@Table(name="IMS_CLS_DIALER_CALL_REQUEST")
@NamedQueries({
	@NamedQuery(name="DialerCallRequest.findAll", query="SELECT m FROM DialerCallRequest m"),
	@NamedQuery(name="DialerCallRequest.findByKey", query="SELECT m FROM DialerCallRequest m where m.key = :key"),
	@NamedQuery(name="DialerCallRequest.findByIntimationNumber", query = "SELECT m FROM DialerCallRequest m where m.intimationNo = :intimationNo"),
})
public class DialerCallRequest extends AbstractEntity implements Serializable{
	
	@Id
	@SequenceGenerator(name="IMS_CLS_DIALER_CALL_REQUEST_KEY_GENERATOR", sequenceName = "SEQ_DIALER_REQUEST_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_DIALER_CALL_REQUEST_KEY_GENERATOR" ) 
	@Column(name="DIALER_REQUEST_KEY")
	private Long key;
	
	@Column(name="INTIMATION_NUMBER")
	private String intimationNo;
	
	@Column(name="REQUEST_ACTION")
	private String requestAction;
	
	@Column(name="REQUEST_USER")
	private String requestUser;
	
	@Column(name="REQUEST_PHONE_NUMBER")
	private String requestPhoneNo;
	
	@Column(name="REQUEST_REF_NUMBER")
	private String requestRefNo;
	
	@Column(name="RESPONSE_STATUS")
	private String responseStatus;
	
	@Column(name="RESPONSE_MESSAGE")
	private String responseMessage;
	
	@Column(name="RESPONSE_PHONE_NUMBER")
	private String responsePhoneNo;
	
	@Column(name="RESPONSE_PROCESS")
	private String responseProcess;
	
	@Column(name="RESPONSE_LEAD_ID")
	private String responseLeadId;
	
	@Column(name="RESPONSE_REF_NUMBER")
	private String responseRefNo;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name="ACTIVE_STATUS")
	private String activeStatus;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public String getRequestAction() {
		return requestAction;
	}

	public void setRequestAction(String requestAction) {
		this.requestAction = requestAction;
	}

	public String getRequestUser() {
		return requestUser;
	}

	public void setRequestUser(String requestUser) {
		this.requestUser = requestUser;
	}

	public String getRequestPhoneNo() {
		return requestPhoneNo;
	}

	public void setRequestPhoneNo(String requestPhoneNo) {
		this.requestPhoneNo = requestPhoneNo;
	}

	public String getRequestRefNo() {
		return requestRefNo;
	}

	public void setRequestRefNo(String requestRefNo) {
		this.requestRefNo = requestRefNo;
	}

	public String getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public String getResponsePhoneNo() {
		return responsePhoneNo;
	}

	public void setResponsePhoneNo(String responsePhoneNo) {
		this.responsePhoneNo = responsePhoneNo;
	}

	public String getResponseProcess() {
		return responseProcess;
	}

	public void setResponseProcess(String responseProcess) {
		this.responseProcess = responseProcess;
	}

	public String getResponseLeadId() {
		return responseLeadId;
	}

	public void setResponseLeadId(String responseLeadId) {
		this.responseLeadId = responseLeadId;
	}

	public String getResponseRefNo() {
		return responseRefNo;
	}

	public void setResponseRefNo(String responseRefNo) {
		this.responseRefNo = responseRefNo;
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

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}
}

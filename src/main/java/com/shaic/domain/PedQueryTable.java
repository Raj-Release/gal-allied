package com.shaic.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.shaic.domain.preauth.Stage;

@Entity
@Table(name="IMS_CLSB_PED_UW_QUERY")

@NamedQueries({
	
	@NamedQuery(name="PedQueryTable.findByKey", query="SELECT o FROM PedQueryTable o where o.key =:key"),
	@NamedQuery(name="PedQueryTable.findByworkItemID", query="SELECT o FROM PedQueryTable o where o.workItemId =:workItemId")
	
})

public class PedQueryTable {
	
	@Id
	@SequenceGenerator(name="IMS_CLSB_PED_UW_QUERY_KEY_GENERATOR", sequenceName = "SEQ_PED_UW_QUERY_KEY"  ,allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLSB_PED_UW_QUERY_KEY_GENERATOR") 

	@Column(name = "PED_UW_QUERY_KEY")
	private Long key;

	@Column(name = "POLICY_NUMBER")
	private String policyNumber;
	
	@Column(name = "MEMBER_CODE")
	private String memberCode;

	@Column(name = "CLAIM_NUMBER")
	private String claimNumber;
	
	@Column(name = "WORKITEM_ID")
	private Long workItemId;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="STATUS_ID")
	private Status status;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="STAGE_ID")
	private Stage stage;
	
	@Column(name = "ACTIVE_STATUS")
	private Long activeStatus;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;	
	
	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "BATCH_READ_FLAG")
	private String batchReadFlag;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BATCH_READ_DATE")
	private Date batchReadDate;
	
	@Column(name = "SERVICE_TRANSACTION_ID")
	private String serviceTransId;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getMemberCode() {
		return memberCode;
	}

	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}

	public String getClaimNumber() {
		return claimNumber;
	}

	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}

	public Long getWorkItemId() {
		return workItemId;
	}

	public void setWorkItemId(Long workItemId) {
		this.workItemId = workItemId;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
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

	public String getBatchReadFlag() {
		return batchReadFlag;
	}

	public void setBatchReadFlag(String batchReadFlag) {
		this.batchReadFlag = batchReadFlag;
	}

	public Date getBatchReadDate() {
		return batchReadDate;
	}

	public void setBatchReadDate(Date batchReadDate) {
		this.batchReadDate = batchReadDate;
	}

	public String getServiceTransId() {
		return serviceTransId;
	}

	public void setServiceTransId(String serviceTransId) {
		this.serviceTransId = serviceTransId;
	}


}

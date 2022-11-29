package com.shaic.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.shaic.arch.fields.dto.AbstractEntity;

@Entity
@Table(name="IMS_CLS_FVR_NOT_REQUIRED")
@NamedQueries({
@NamedQuery(name="FvrNotRequired.findAll", query="SELECT f FROM FvrNotRequired f"),
@NamedQuery(name="FvrNotRequired.findByIntimationKey", query="SELECT f FROM FvrNotRequired f WHERE f.intimationKey is not null and f.intimationKey = :intimationKey"),
@NamedQuery(name="FvrNotRequired.findByKey", query="SELECT f FROM FvrNotRequired f WHERE f.key = :fvrNotRequiredKey"),
})
public class FvrNotRequired  extends AbstractEntity{

	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="IMS_CLS_FVR_NOT_REQUIRED_GENERATOR", sequenceName = "SEQ_FVR_NOT_REQUIRED_KEY" , allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_FVR_NOT_REQUIRED_GENERATOR" )
	@Column(name="FVR_NOT_REQUIRED_KEY")
	private Long key;

	@Column(name="INTIMATION_NUMBER")
	private String intimationNumber;

	@Column(name="CLAIM_NUMBER")
	private String claimNumber;

	@Column(name="FVR_LOV_REMARKS")
	private String fvrLovRemarks;
	
	@Column(name="INTIMATION_KEY")
	private Long intimationKey;

	@Column(name="STATUS")
	private String status;

	@Column(name="ACTIVE_STATUS")
	private String activeStatus;
	
	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name="FVR_NOT_REQ_STAGE")
	private String fvrNotRequiredStage;



	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getIntimationNumber() {
		return intimationNumber;
	}

	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}

	public String getClaimNumber() {
		return claimNumber;
	}

	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}

	public String getFvrLovRemarks() {
		return fvrLovRemarks;
	}

	public void setFvrLovRemarks(String fvrLovRemarks) {
		this.fvrLovRemarks = fvrLovRemarks;
	}

	public Long getIntimationKey() {
		return intimationKey;
	}

	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getFvrNotRequiredStage() {
		return fvrNotRequiredStage;
	}

	public void setFvrNotRequiredStage(String fvrNotRequiredStage) {
		this.fvrNotRequiredStage = fvrNotRequiredStage;
	}
	
	
	
	}

package com.shaic.domain;

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

@Entity
@Table(name = "IMS_CLS_FVR_TRIGGER_POINT")
@NamedQueries({
	@NamedQuery(name = "FvrTriggerPoint.findByFvrKey", query = "SELECT o FROM FvrTriggerPoint o where o.fvrKey = :fvrKey order by o.key asc"),
	@NamedQuery(name = "FvrTriggerPoint.findByKey", query = "SELECT o FROM FvrTriggerPoint o where o.fvrKey = :fvrKey and (o.deleteFlag is null or o.deleteFlag = 'N') order by o.key asc")
})
public class FvrTriggerPoint implements Serializable {

	
	@Id
	@SequenceGenerator(name="IMS_CLS_FVR_TRIGGER_POINT_KEY_GENERATOR", sequenceName = "SEQ_SEQUENCE_FVRDTLS" , allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_FVR_TRIGGER_POINT_KEY_GENERATOR" )
	@Column(name="SEQUENCE_NUMBER")
	private Long key;
	
	@Column(name = "FVRDTLS_KEY")
	private Long fvrKey;
	
	@Column(name = "REMARKS")
	private String remarks;
	
	@Column(name = "GRADING")
	private String grading;
	
	@Column(name = "DELETED_FLAG")
	private String deleteFlag;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name = "TRIGGER_POINT_CHANGED")
	private String triggerPointsChanged;

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getGrading() {
		return grading;
	}

	public void setGrading(String grading) {
		this.grading = grading;
	}

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
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

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getFvrKey() {
		return fvrKey;
	}

	public void setFvrKey(Long fvrKey) {
		this.fvrKey = fvrKey;
	}

	public String getTriggerPointsChanged() {
		return triggerPointsChanged;
	}

	public void setTriggerPointsChanged(String triggerPointsChanged) {
		this.triggerPointsChanged = triggerPointsChanged;
	}
}

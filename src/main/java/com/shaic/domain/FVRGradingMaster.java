package com.shaic.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the MAS_FVR_GRADING_TYPE database table.
 * 
 */
@Entity
@Table(name = "MAS_FVR_GRADING_TYPE")
@NamedQueries({
		@NamedQuery(name = "FVRGradingMaster.findAll", query = "SELECT o FROM FVRGradingMaster o  where o.activeStatus = 1"),
		@NamedQuery(name = "FVRGradingMaster.findBySegment", query = "SELECT o FROM FVRGradingMaster o where Lower(o.segment) = :segment and o.activeStatus = 1")
})
public class FVRGradingMaster implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "\"FVR_GRADING_KEY\"")
	private Long key;

	@Column(name = "GRADING_TYPE")
	private String gradingType;

	@Column(name = "APPLICABILITY")
	private String applicability;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column(name="CREATED_BY")
	private String createdBy;  
			
	@Temporal(TemporalType.DATE)
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="SEGMENT")
	private String segment;
	
	@Column(name="ACTIVE_STATUS")
	private Integer	activeStatus;

	public FVRGradingMaster() {
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getGradingType() {
		return gradingType;
	}

	public void setGradingType(String gradingType) {
		this.gradingType = gradingType;
	}

	public String getApplicability() {
		return applicability;
	}

	public void setApplicability(String applicability) {
		this.applicability = applicability;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
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

	public String getSegment() {
		return segment;
	}

	public void setSegment(String segment) {
		this.segment = segment;
	}

	public Integer getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
	}
}
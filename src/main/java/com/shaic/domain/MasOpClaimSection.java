package com.shaic.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="MAS_OP_CLAIM_SECTION")
@NamedQueries({
	@NamedQuery(name ="MasOpClaimSection.findAll", query = "SELECT sec FROM MasOpClaimSection sec"),
	@NamedQuery(name = "MasOpClaimSection.findByKey",query = "select sec from MasOpClaimSection sec where sec.key = :key"),
})
public class MasOpClaimSection implements Serializable{
	
	@Id
	@Column(name="OP_SEC_KEY")
	private Long key;
	
	@Column(name="OP_DESCRIPTION")
	private String opDescription;
	
	@Column(name="SECTION_CODE")
	private String sectionCode;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getOpDescription() {
		return opDescription;
	}

	public void setOpDescription(String opDescription) {
		this.opDescription = opDescription;
	}

	public String getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(String sectionCode) {
		this.sectionCode = sectionCode;
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
	

}

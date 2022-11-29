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

import com.shaic.arch.fields.dto.AbstractEntity;
/**
 * @author karthikeyan.r
 * The persistent class for the MAS_HOSPITAL_SCORE database table.
 * 
 */

@SuppressWarnings("serial")
@Entity
@Table(name="MAS_HOSPITAL_SCORE")
@NamedQueries({
	@NamedQuery(name="HospitalCategory.findAll", query="SELECT m FROM HospitalCategory m"),
	@NamedQuery(name="HospitalCategory.findDistinctCategory", query="SELECT m FROM HospitalCategory m WHERE m.activeStatus = 'Y' AND (m.networkTypeID = :networkTypeID or m.networkTypeID is null) ORDER BY m.categoryKey ASC"),
	@NamedQuery(name="HospitalCategory.findByCategory", query="SELECT m FROM HospitalCategory m WHERE m.categoryDesc like :categoryDesc AND m.activeStatus = 'Y' AND (m.networkTypeID = :networkTypeID or m.networkTypeID is null)"),
	@NamedQuery(name="HospitalCategory.findByClaimType", query="SELECT m FROM HospitalCategory m WHERE m.claimType IN (:clmType) AND m.activeStatus = 'Y' ORDER BY m.categoryKey, m.subCategoryKey ASC"),
	@NamedQuery(name="HospitalCategory.findByKey", query="SELECT m FROM HospitalCategory m WHERE m.categoryKey= :cKey OR m.subCategoryKey = :scKey")
})
public class HospitalCategory extends AbstractEntity implements Serializable  {


	@Column(name = "SLNO")
	private Long key;
	
	@Column(name = "CATEGORY_KEY")
	private Long categoryKey;
	
	@Column(name = "CATEGORY_DESC")
	private String categoryDesc;
	
	@Id
	@Column(name = "SUB_CATEGORY_KEY")
	private Long subCategoryKey;
	
	@Column(name = "SUB_CATEGORY_DESC")
	private String subCategoryDesc;
	
	@Column(name = "ACTIVE_STATUS")
	private String activeStatus;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name = "CLAIM_TYPE")
	private String claimType;
	
	@Column(name = "HOSPITAL_TYPE")
	private String networkTypeID;
	
	@Override
	public Long getKey() {
		return key;
	}

	@Override
	public void setKey(Long key) {
		this.key = key;
	}

	public Long getCategoryKey() {
		return categoryKey;
	}

	public void setCategoryKey(Long categoryKey) {
		this.categoryKey = categoryKey;
	}

	public String getCategoryDesc() {
		return categoryDesc;
	}

	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}

	public Long getSubCategoryKey() {
		return subCategoryKey;
	}

	public void setSubCategoryKey(Long subCategoryKey) {
		this.subCategoryKey = subCategoryKey;
	}

	public String getSubCategoryDesc() {
		return subCategoryDesc;
	}

	public void setSubCategoryDesc(String subCategoryDesc) {
		this.subCategoryDesc = subCategoryDesc;
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

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	public String getNetworkTypeID() {
		return networkTypeID;
	}

	public void setNetworkTypeID(String networkTypeID) {
		this.networkTypeID = networkTypeID;
	}
	
}

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

import com.shaic.arch.fields.dto.AbstractEntity;

/**
 * @author GokulPrasath.A
 *
 */
@Entity
@Table(name = "CLAIM_MAGAZINE_GALLERY")
@NamedQueries({
		@NamedQuery(name = "ClaimMagazine.findAll", query = "SELECT m FROM ClaimMagazine m"),
		@NamedQuery(name = "ClaimMagazine.findByKey", query = "SELECT m FROM ClaimMagazine m where m.key = :key"),
		@NamedQuery(name = "ClaimMagazine.findByUserId", query = "SELECT o FROM ClaimMagazine o where Upper(o.userId) = :userId"),
		@NamedQuery(name = "ClaimMagazine.findByUserIdandCode", query = "SELECT o FROM ClaimMagazine o where Upper(o.userId) = :userId and o.magazineCode = :magazineCode") })
		
public class ClaimMagazine extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CLAIM_MAGAZINE_GALLERY_GENERATOR", sequenceName = "SEQ_GLX_CLM_MAG_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CLAIM_MAGAZINE_GALLERY_GENERATOR" ) 
	@Column(name="GLX_CLM_MAG_KEY")
	private Long key;

	@Column(name = "USER_ID")
	private String userId;
	
	@Column(name="CONFIRMATION_FLAG")
	private String confirmationFlag;

	@Column(name = "MAGAZINE_CODE")
	private String magazineCode;

	@Column(name = "MAGAZINE_CATEGORY")
	private String magazineCategory;
	
	@Column(name = "MAGAZINE_SUB_CATEGORY")
	private String magazineSubCategory;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_DATE")
	private Date startDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SUBMIT_DATE")
	private Date submitDate;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "ACTIVE_FLAG")
	private String activeFlag;
	
//	@Column(name = "MAGAZINE_DESC")
//	private String magazineDesc;
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getConfirmationFlag() {
		return confirmationFlag;
	}

	public void setConfirmationFlag(String confirmationFlag) {
		this.confirmationFlag = confirmationFlag;
	}

	public String getMagazineCode() {
		return magazineCode;
	}

	public void setMagazineCode(String magazineCode) {
		this.magazineCode = magazineCode;
	}

	public String getMagazineCategory() {
		return magazineCategory;
	}

	public void setMagazineCategory(String magazineCategory) {
		this.magazineCategory = magazineCategory;
	}

	public String getMagazineSubCategory() {
		return magazineSubCategory;
	}

	public void setMagazineSubCategory(String magazineSubCategory) {
		this.magazineSubCategory = magazineSubCategory;
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

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(Date submitDate) {
		this.submitDate = submitDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
}
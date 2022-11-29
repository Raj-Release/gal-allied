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
@Table(name="MAS_RAW_SUB_CATEGORY")
@NamedQueries({
	@NamedQuery(name="RawSubCategory.findAll", query="SELECT r FROM RawSubCategory r where r.key = :key "),
	@NamedQuery(name="RawSubCategory.findByRawCategoryKey", query="SELECT r FROM RawSubCategory r where r.rawCategoryKey = :rawCategoryKey")
})

public class RawSubCategory implements Serializable{

	@Id
	@SequenceGenerator(name="SEQ_RAW_SUB_CTGRY_KEY_GENERATOR", sequenceName = "SEQ_RAW_SUB_CTGRY_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_RAW_SUB_CTGRY_KEY_GENERATOR" )
	@Column(name = "RAW_SUB_CTGRY_KEY")
	private Long key;
	
	@Column(name="RAW_CATEGORY_KEY")
	private Long rawCategoryKey;
	
	@Column(name="SUB_CATEGORY_DESC")
	private String subCategoryDescription;
	
	@Column(name="ACTIVE_STATUS")
	private String activeStatus;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name="MODIFIED_BY")
	private String modifyby;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getSubCategoryDescription() {
		return subCategoryDescription;
	}

	public void setSubCategoryDescription(String subCategoryDescription) {
		this.subCategoryDescription = subCategoryDescription;
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

	public String getModifyby() {
		return modifyby;
	}

	public void setModifyby(String modifyby) {
		this.modifyby = modifyby;
	}

	public Long getRawCategoryKey() {
		return rawCategoryKey;
	}

	public void setRawCategoryKey(Long rawCategoryKey) {
		this.rawCategoryKey = rawCategoryKey;
	}
}

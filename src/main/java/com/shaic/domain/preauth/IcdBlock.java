package com.shaic.domain.preauth;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the MAS_ICD_BLOCK_T database table.
 * 
 */
@Entity
@Table(name="MAS_ICD_BLOCK")
@NamedQueries({
	@NamedQuery(name="IcdBlock.findAll", query="SELECT m FROM IcdBlock m where m.activeStatus is not null and m.activeStatus = 1 order by m.value asc"),
	@NamedQuery(name="IcdBlock.findByChapterKey", query="SELECT m FROM IcdBlock m WHERE (:chapterKey is null or m.icdChapterKey = :chapterKey) and m.activeStatus is not null and m.activeStatus = 1 order by m.value asc"),
	@NamedQuery(name="IcdBlock.findByKey",query="SELECT m FROM IcdBlock m WHERE m.key=:primaryKey"),
	@NamedQuery(name="IcdBlock.findByKeyList",query="SELECT m FROM IcdBlock m WHERE m.key in (:keyList)")
})

public class IcdBlock implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ICD_BLOCK_KEY")
	private Long key;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;

//	@Column(name="ACTIVE_STATUS_DATE")
//	private Timestamp activeStatusDate;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name="ICD_CHAPTER_KEY")
	private Long icdChapterKey;

	@Column(name="MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name="MODIFIED_DATE")
	private Long modifiedDate;

	@Column(name="VALUE")
	private String value;

//	@Column(name="VERSION")
//	private Long version;

	public IcdBlock() {
	}

	public Long getActiveStatus() {
		return this.activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}


	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Long getIcdChapterKey() {
		return this.icdChapterKey;
	}

	public void setIcdChapterKey(Long icdChapterKey) {
		this.icdChapterKey = icdChapterKey;
	}

	public Long getKey() {
		return this.key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Long getModifiedDate() {
		return this.modifiedDate;
	}

	public void setModifiedDate(Long modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
}
package com.shaic.domain.preauth;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * The persistent class for the MAS_ICD_CODE_T database table.
 * 
 */
@Entity
@Table(name="MAS_ICD_CODE")
@NamedQueries({ 
		@NamedQuery(name="IcdCode.findAll", query="SELECT m FROM IcdCode m where m.activeStatus is not null and m.activeStatus = 1 order by m.value asc"),
		@NamedQuery(name="IcdCode.findByChaptedId",query="SELECT o FROM IcdCode o where o.icdChapter.key =:icdChapterKey order by o.value asc"),
		@NamedQuery(name="IcdCode.findByBlockKey",query="SELECT o FROM IcdCode o where o.icdBlock is not null and  o.icdBlock.key =:blockKey and o.activeStatus is not null and o.activeStatus = 1 order by o.value asc "),
		@NamedQuery(name="IcdCode.findByKey",query="SELECT o FROM IcdCode o where o.key=:primaryKey"),
		@NamedQuery(name="IcdCode.findByKeyList",query="SELECT o FROM IcdCode o where o.key in (:keyList)")
})
		

public class IcdCode implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ICD_CODE_KEY")
	private Long key;

	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;

//	@Column(name="ACTIVE_STATUS_DATE")
//	private Timestamp activeStatusDate;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@OneToOne
	@JoinColumn(name="ICD_BLOCK_KEY")
	private IcdBlock icdBlock;

	@OneToOne
	@JoinColumn(name="ICD_CHAPTER_KEY")
	private IcdChapter icdChapter;
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Column(name="MODIFIED_DATE")
	private Long modifiedDate;

	@Column(name="VALUE")
	private String value;
	
	@Column(name = "DESCRIPTION")
	private String description;

//	@Column(name="VERSION")
//	private Long version;

	public IcdCode() {
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

	public IcdBlock getIcdBlock() {
		return this.icdBlock;
	}

	public void setIcdBlock(IcdBlock icdBlock) {
		this.icdBlock = icdBlock;
	}

	public IcdChapter getIcdChapter() {
		return this.icdChapter;
	}

	public void setIcdChapter(IcdChapter icdChapter) {
		this.icdChapter = icdChapter;
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

//	public void setModifiedBy(String modifiedBy) {
//		this.modifiedBy = modifiedBy;
//	}

//	public Long getModifiedDate() {
//		return this.modifiedDate;
//	}

//	public void setModifiedDate(Long modifiedDate) {
//		this.modifiedDate = modifiedDate;
//	}

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
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
@Table(name="MAS_RAW_CATEGORY")
@NamedQueries({
/*	@NamedQuery(name="RawCategory.findAll", query="SELECT r FROM RawCategory r where r.key = :key"),
	@NamedQuery(name="RawCategory.findByType", query="SELECT r FROM RawCategory r where r.categoryType = :categoryType")*/
	@NamedQuery(name="RawCategory.findAll", query="SELECT r FROM RawCategory r where r.key = :key "),
	@NamedQuery(name="RawCategory.findByType", query="SELECT r FROM RawCategory r where r.categoryType = :categoryType ")
})
public class RawCategory  implements Serializable{
	
	private static final long serialVersionUID = 1086997314154372927L;
	
	@Id
	@SequenceGenerator(name="SEQ_RAW_CATEGORY_KEY_GENERATOR", sequenceName = "SEQ_RAW_CATEGORY_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_RAW_CATEGORY_KEY_GENERATOR" )
	@Column(name = "RAW_CATEGORY_KEY ")
	private Long key;
	
	@Column(name="CATEGORY_TYPE")
	private String categoryType;
	
	@Column(name="CATEGORY_DESC")
	private String categoryDescription;
	
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

	public String getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}

	public String getCategoryDescription() {
		return categoryDescription;
	}

	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription = categoryDescription;
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
}

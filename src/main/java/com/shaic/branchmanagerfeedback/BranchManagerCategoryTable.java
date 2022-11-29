package com.shaic.branchmanagerfeedback;

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

import com.shaic.arch.fields.dto.AbstractEntity;
@Entity
@Table(name="MAS_SEC_BM_TYPE_CATEGORY")
@NamedQueries({
@NamedQuery(name="BranchManagerCategoryTable.findAll", query="SELECT m FROM BranchManagerCategoryTable m"),
@NamedQuery(name="BranchManagerCategoryTable.findByKey",query="SELECT m FROM BranchManagerCategoryTable m where m.key=:key")
		})
public class BranchManagerCategoryTable extends AbstractEntity implements Serializable{
	@Id
	@SequenceGenerator(name="MAS_SEC_BM_TYPE_CATEGORY_KEY_GENERATOR", sequenceName = "SEQ_BM_TYPE_CAT_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MAS_SEC_BM_TYPE_CATEGORY_KEY_GENERATOR" )
	@Column(name="BM_TYPE_CAT_KEY",updatable=false)
	private Long key;
	
	@Column(name="BM_MASTER_KEY")
	private Long fbMasterKey;
	
	@Column(name="BM_CATEGORY")
	private String feedbackCategory;
	
	@Column(name="ACTIVE_STATUS")
	private String activeStatus;
	
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}
	
	public String getFeedbackCategory() {
		return feedbackCategory;
	}

	public void setFeedbackCategory(String feedbackCategory) {
		this.feedbackCategory = feedbackCategory;
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

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Long getFbMasterKey() {
		return fbMasterKey;
	}

	public void setFbMasterKey(Long fbMasterKey) {
		this.fbMasterKey = fbMasterKey;
	}
}

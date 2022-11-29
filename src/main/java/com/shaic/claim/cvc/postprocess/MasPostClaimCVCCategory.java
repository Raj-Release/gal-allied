package com.shaic.claim.cvc.postprocess;

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

import com.shaic.arch.fields.dto.AbstractEntity;

@Entity
@Table(name = "MAS_POST_CLAIM_CVC_CATEGORY")
@NamedQueries({
@NamedQuery(name = "MasPostClaimCVCCategory.findAll", query = "SELECT i FROM MasPostClaimCVCCategory i where i.activeStatus = :activeStatus"),
@NamedQuery(name = "MasPostClaimCVCCategory.findByKey", query="SELECT i FROM MasPostClaimCVCCategory i where i.key = :key"),
})
public class MasPostClaimCVCCategory extends AbstractEntity{
	
	@Id
	@Column(name="POST_CLM_CVC_CTGRY_KEY", updatable=false)
	private Long key;
	
	@Column(name="CATEGORY")
	private String category;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name = "MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column(name="ACTIVE_STATUS")
	private String activeStatus;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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

	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}
	
	

}

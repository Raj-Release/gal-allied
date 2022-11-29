package com.shaic.claim.pcc.beans;

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
@Table(name="MAS_SEC_PCC_SUB_CATEGORY")
@NamedQueries({
	@NamedQuery(name = "PCCSubCategory.findAll", query = "SELECT i FROM PCCSubCategory i where i.activeStatus = 'Y'"),
	@NamedQuery(name="PCCSubCategory.findBypccCategory", query="SELECT i FROM PCCSubCategory i where i.pccCategorykey = :pccCategorykey and i.activeStatus = 'Y'")
})
public class PCCSubCategory extends AbstractEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PCC_SUB_CATEGORY_KEY")
	private Long key;
	
	@Column(name = "PCC_CATEGORY_KEY")
	private Long pccCategorykey;
	
	@Column(name = "PCC_SUB_CATEGORY_DESC")
	private String pccSubDesc;

	@Column(name = "ACTIVE_FLAG")
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
	private String modifiedBy;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getPccCategorykey() {
		return pccCategorykey;
	}

	public void setPccCategorykey(Long pccCategorykey) {
		this.pccCategorykey = pccCategorykey;
	}

	public String getPccSubDesc() {
		return pccSubDesc;
	}

	public void setPccSubDesc(String pccSubDesc) {
		this.pccSubDesc = pccSubDesc;
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
	
	
}

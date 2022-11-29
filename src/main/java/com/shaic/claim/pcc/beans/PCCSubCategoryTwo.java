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
@Table(name="MAS_SEC_PCC_SUB_CATEGORY_2")
@NamedQueries({
	@NamedQuery(name = "PCCSubCategoryTwo.findAll", query = "SELECT i FROM PCCSubCategoryTwo i where i.activeStatus = 'Y'"),
	@NamedQuery(name="PCCSubCategoryTwo.findBypccCategory", query="SELECT i FROM PCCSubCategoryTwo i where i.pccSubCategorykey = :pccSubCategorykey and i.activeStatus = 'Y'")
})
public class PCCSubCategoryTwo extends AbstractEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PCC_SUB_CATEGORY_KEY_2")
	private Long key;
	
	@Column(name = "PCC_SUB_CATEGORY_KEY")
	private Long pccSubCategorykey;
	
	@Column(name = "PCC_SUB_CATEGORY_DESC_2")
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

	public Long getPccSubCategorykey() {
		return pccSubCategorykey;
	}

	public void setPccSubCategorykey(Long pccSubCategorykey) {
		this.pccSubCategorykey = pccSubCategorykey;
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

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

@Entity
@Table(name="MAS_SEC_AA_GMC_ICD_CODE")
@NamedQueries({ 
		@NamedQuery(name="BillingAutoAllocationIcdCode.findAll", query="SELECT m FROM BillingAutoAllocationIcdCode m where m.activeStatus is not null and m.activeStatus = 'Y' order by m.key asc"),
		@NamedQuery(name="BillingAutoAllocationIcdCode.findByChaptedId",query="SELECT o FROM BillingAutoAllocationIcdCode o where o.autoAllocIcdCode =:autoAllocIcdCode order by o.key asc"),
		@NamedQuery(name="BillingAutoAllocationIcdCode.findByKey",query="SELECT o FROM BillingAutoAllocationIcdCode o where o.key=:primaryKey"),
		@NamedQuery(name="BillingAutoAllocationIcdCode.findByKeyList",query="SELECT o FROM BillingAutoAllocationIcdCode o where o.autoAllocIcdCodeKey in (:keyList) and o.activeStatus is not null and o.activeStatus = 'Y' ")
})

public class BillingAutoAllocationIcdCode implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="AA_GMC_ICD_KEY")
	private Long key;
	
	@Column(name="ICD_CODE_KEY")
	private Long autoAllocIcdCodeKey;

	@Column(name="AA_GMC_ICD_CODE")
	private String autoAllocIcdCode;
	
	@Column(name="AA_GMC_ICD_DESC")
	private String autoAllocIcdDesc;
	
	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Column(name="MODIFIED_DATE")
	private Long modifiedDate;
	
	@Column(name="ACTIVE_STATUS")
	private String activeStatus;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getAutoAllocIcdCodeKey() {
		return autoAllocIcdCodeKey;
	}

	public void setAutoAllocIcdCodeKey(Long autoAllocIcdCodeKey) {
		this.autoAllocIcdCodeKey = autoAllocIcdCodeKey;
	}
	
	public String getAutoAllocIcdCode() {
		return autoAllocIcdCode;
	}

	public void setAutoAllocIcdCode(String autoAllocIcdCode) {
		this.autoAllocIcdCode = autoAllocIcdCode;
	}

	public String getAutoAllocIcdDesc() {
		return autoAllocIcdDesc;
	}

	public void setAutoAllocIcdDesc(String autoAllocIcdDesc) {
		this.autoAllocIcdDesc = autoAllocIcdDesc;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Long getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Long modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}
}

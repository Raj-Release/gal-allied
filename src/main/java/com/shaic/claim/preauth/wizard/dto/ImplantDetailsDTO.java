package com.shaic.claim.preauth.wizard.dto;

import java.util.Date;

import com.shaic.arch.table.AbstractRowEnablerDTO;

public class ImplantDetailsDTO extends AbstractRowEnablerDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7456984404761561607L;

	private Long key;

	private Long claimKey;

	private Long transactionKey;

	private String implantName;

	private Double implantCost; 
	
	private String implantType; 

	private Long activeStatus;

	private String createdBy;

	private Date createDate;

	private String modifiedBy;

	private Date modifiedDate;

	private String oldImplantName;

	private Double oldImplantCost; 
	
	private String oldImplantType; 

	private String dcImplantFlag;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}

	public Long getTransactionKey() {
		return transactionKey;
	}

	public void setTransactionKey(Long transactionKey) {
		this.transactionKey = transactionKey;
	}

	public String getImplantName() {
		return implantName;
	}

	public void setImplantName(String implantName) {
		this.implantName = implantName;
	}

	public Double getImplantCost() {
		return implantCost;
	}

	public void setImplantCost(Double implantCost) {
		this.implantCost = implantCost;
	}

	public String getImplantType() {
		return implantType;
	}

	public void setImplantType(String implantType) {
		this.implantType = implantType;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getOldImplantName() {
		return oldImplantName;
	}

	public void setOldImplantName(String oldImplantName) {
		this.oldImplantName = oldImplantName;
	}

	public Double getOldImplantCost() {
		return oldImplantCost;
	}

	public void setOldImplantCost(Double oldImplantCost) {
		this.oldImplantCost = oldImplantCost;
	}

	public String getOldImplantType() {
		return oldImplantType;
	}

	public void setOldImplantType(String oldImplantType) {
		this.oldImplantType = oldImplantType;
	}

	public String getDcImplantFlag() {
		return dcImplantFlag;
	}

	public void setDcImplantFlag(String dcImplantFlag) {
		this.dcImplantFlag = dcImplantFlag;
	}
}

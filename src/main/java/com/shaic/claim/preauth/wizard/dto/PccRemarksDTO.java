package com.shaic.claim.preauth.wizard.dto;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

public class PccRemarksDTO extends AbstractTableDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private int pccRemarksKey;
	private int intimationKey;
	private int claimTypeId;
	private int stageId;
	private int statusId;
	private String pccRemarks;
	private int claimKey;
	private int cashlessKey;
	private int reimbursementKey;
	private Date biDate;
	private int activeStatus;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;

	public int getPccRemarksKey() {
		return pccRemarksKey;
	}

	public void setPccRemarksKey(int pccRemarksKey) {
		this.pccRemarksKey = pccRemarksKey;
	}

	public int getIntimationKey() {
		return intimationKey;
	}

	public void setIntimationKey(int intimationKey) {
		this.intimationKey = intimationKey;
	}

	public int getClaimTypeId() {
		return claimTypeId;
	}

	public void setClaimTypeId(int claimTypeId) {
		this.claimTypeId = claimTypeId;
	}

	public int getStageId() {
		return stageId;
	}

	public void setStageId(int stageId) {
		this.stageId = stageId;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public String getPccRemarks() {
		return pccRemarks;
	}

	public void setPccRemarks(String pccRemarks) {
		this.pccRemarks = pccRemarks;
	}

	public int getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(int claimKey) {
		this.claimKey = claimKey;
	}

	public int getCashlessKey() {
		return cashlessKey;
	}

	public void setCashlessKey(int cashlessKey) {
		this.cashlessKey = cashlessKey;
	}

	public int getReimbursementKey() {
		return reimbursementKey;
	}

	public void setReimbursementKey(int reimbursementKey) {
		this.reimbursementKey = reimbursementKey;
	}

	public Date getBiDate() {
		return biDate;
	}

	public void setBiDate(Date biDate) {
		this.biDate = biDate;
	}

	public int getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(int activeStatus) {
		this.activeStatus = activeStatus;
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

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	

}

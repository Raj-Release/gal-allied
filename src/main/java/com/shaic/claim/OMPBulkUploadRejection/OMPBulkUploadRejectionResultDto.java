package com.shaic.claim.OMPBulkUploadRejection;

import java.sql.Timestamp;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

public class OMPBulkUploadRejectionResultDto extends AbstractTableDTO{
	
	private Long key;

	private String rodNumber;
	
	private String dateOfDispatch;

	private String podNumber;

	private String modeOfDispatch;

	private String remarks;

	private Timestamp createdDate;
	
	private String createdBy;
	
	private Timestamp modifiedDate;
	
	private String modifiedBy;

	private int activeStatus;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getRodNumber() {
		return rodNumber;
	}

	public void setRodNumber(String rodNumber) {
		this.rodNumber = rodNumber;
	}

	public String getDateOfDispatch() {
		return dateOfDispatch;
	}

	public void setDateOfDispatch(String dateOfDispatch) {
		this.dateOfDispatch = dateOfDispatch;
	}

	public String getPodNumber() {
		return podNumber;
	}

	public void setPodNumber(String podNumber) {
		this.podNumber = podNumber;
	}

	public String getModeOfDispatch() {
		return modeOfDispatch;
	}

	public void setModeOfDispatch(String modeOfDispatch) {
		this.modeOfDispatch = modeOfDispatch;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public int getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(int activeStatus) {
		this.activeStatus = activeStatus;
	}
}

package com.shaic.claim.preauth.wizard.dto;

import java.util.Date;

public class PreauthQueryDTO {
	
	private Long key;
	
	private Long activeStatus;

	private Date activeStatusDate;

	private String createdBy;

	private Date createdDate;

	private Long preauthKey;

	private String officeCode;

	private String queryRemarks;

	private String status;

	private Date statusDate;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Date getActiveStatusDate() {
		return activeStatusDate;
	}

	public void setActiveStatusDate(Date activeStatusDate) {
		this.activeStatusDate = activeStatusDate;
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

	public Long getPreauthKey() {
		return preauthKey;
	}

	public void setPreauth(Long preauthKey) {
		this.preauthKey = preauthKey;
	}

	public String getOfficeCode() {
		return officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

	public String getQueryRemarks() {
		return queryRemarks;
	}

	public void setQueryRemarks(String queryRemarks) {
		this.queryRemarks = queryRemarks;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
	}

}

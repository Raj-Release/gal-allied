package com.shaic.claim.preauth.wizard.dto;

import java.io.File;
import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.preauth.Preauth;

public class PreauthEscalateDTO {

	private Long key;
	
	private Long activeStatus;

	private Date activeStatusDate;

	private String escalateRemarks;

	private SelectValue escalateTo;

	private File fileUpload;

	private Preauth preauth;

	private String officeCode;

	private SelectValue specialistType;

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

	public String getEscalateRemarks() {
		return escalateRemarks;
	}

	public void setEscalateRemarks(String escalateRemarks) {
		this.escalateRemarks = escalateRemarks;
	}

	public SelectValue getEscalateTo() {
		return escalateTo;
	}

	public void setEscalateTo(SelectValue escalateTo) {
		this.escalateTo = escalateTo;
	}

	public File getFileUpload() {
		return fileUpload;
	}

	public void setFileUpload(File fileUpload) {
		this.fileUpload = fileUpload;
	}

	public Preauth getPreauth() {
		return preauth;
	}

	public void setPreauth(Preauth preauth) {
		this.preauth = preauth;
	}

	public String getOfficeCode() {
		return officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

	public SelectValue getSpecialistType() {
		return specialistType;
	}

	public void setSpecialistType(SelectValue specialistType) {
		this.specialistType = specialistType;
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

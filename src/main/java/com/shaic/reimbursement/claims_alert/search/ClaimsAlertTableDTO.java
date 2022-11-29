package com.shaic.reimbursement.claims_alert.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;

import com.shaic.arch.fields.dto.SelectValue;

public class ClaimsAlertTableDTO implements Serializable {
	
	private String intimationNo;
	
	private String remarks;
	
	private String userName;
	
	private SelectValue alertCategory;
	
	private String slNo;
	
	private Long claimsAlertKey;
	
	private Boolean enable;
	
	private Boolean disable;
	
	private String uploadFile;
	
	private String deleteFlag;
	
	private String createdBy;
	
	private Date createdDate;
	
	private List<ClaimsAlertDocsDTO> docsDTOs;
	
	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public SelectValue getAlertCategory() {
		return alertCategory;
	}

	public void setAlertCategory(SelectValue alertCategory) {
		this.alertCategory = alertCategory;
	}

	public String getSlNo() {
		return slNo;
	}

	public void setSlNo(String slNo) {
		this.slNo = slNo;
	}

	public Long getClaimsAlertKey() {
		return claimsAlertKey;
	}

	public void setClaimsAlertKey(Long claimsAlertKey) {
		this.claimsAlertKey = claimsAlertKey;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public Boolean getDisable() {
		return disable;
	}

	public void setDisable(Boolean disable) {
		this.disable = disable;
	}

	public String getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(String uploadFile) {
		this.uploadFile = uploadFile;
	}

	public List<ClaimsAlertDocsDTO> getDocsDTOs() {
		return docsDTOs;
	}

	public void setDocsDTOs(List<ClaimsAlertDocsDTO> docsDTOs) {
		this.docsDTOs = docsDTOs;
	}

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
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
	
	
	
}

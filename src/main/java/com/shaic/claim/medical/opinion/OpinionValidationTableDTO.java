package com.shaic.claim.medical.opinion;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.domain.Status;

public class OpinionValidationTableDTO extends AbstractTableDTO  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long validationkey;
	private String intimationNumber;
	private Long cpuCode;
	private String claimStage;	
	private Status stage;
	private Status status;	
	private Date assignedDate;
	private String assignedRoleBy;
	private String updatedBy;
	private Date updatedDateTime;
	private String updatedRemarks;
	private Long opinionStatus;
	private String opinionStatusValue;
	private String modifiedBy;
	private Date modifiedDateTime;
	private String approveRejectRemarks;
	private Date createdDate;
	private String createdBy;
	private String assignedDocName;
	
	
	public Long getValidationkey() {
		return validationkey;
	}
	public void setValidationkey(Long validationkey) {
		this.validationkey = validationkey;
	}
	public String getIntimationNumber() {
		return intimationNumber;
	}
	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}
	public Long getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(Long cpuCode) {
		this.cpuCode = cpuCode;
	}
	public String getClaimStage() {
		return claimStage;
	}
	public void setClaimStage(String claimStage) {
		this.claimStage = claimStage;
	}
	public Status getStage() {
		return stage;
	}
	public void setStage(Status stage) {
		this.stage = stage;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public Date getAssignedDate() {
		return assignedDate;
	}
	public void setAssignedDate(Date assignedDate) {
		this.assignedDate = assignedDate;
	}
	
	public String getAssignedRoleBy() {
		return assignedRoleBy;
	}
	public void setAssignedRoleBy(String assignedRoleBy) {
		this.assignedRoleBy = assignedRoleBy;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Date getUpdatedDateTime() {
		return updatedDateTime;
	}
	public void setUpdatedDateTime(Date updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}
	public String getUpdatedRemarks() {
		return updatedRemarks;
	}
	public void setUpdatedRemarks(String updatedRemarks) {
		this.updatedRemarks = updatedRemarks;
	}
	public Long getOpinionStatus() {
		return opinionStatus;
	}
	public void setOpinionStatus(Long opinionStatus) {
		this.opinionStatus = opinionStatus;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Date getModifiedDateTime() {
		return modifiedDateTime;
	}
	public void setModifiedDateTime(Date modifiedDateTime) {
		this.modifiedDateTime = modifiedDateTime;
	}
	public String getOpinionStatusValue() {
		return opinionStatusValue;
	}
	public void setOpinionStatusValue(String opinionStatusValue) {
		this.opinionStatusValue = opinionStatusValue;
	}
	public String getApproveRejectRemarks() {
		return approveRejectRemarks;
	}
	public void setApproveRejectRemarks(String approveRejectRemarks) {
		this.approveRejectRemarks = approveRejectRemarks;
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
	public String getAssignedDocName() {
		return assignedDocName;
	}
	public void setAssignedDocName(String assignedDocName) {
		this.assignedDocName = assignedDocName;
	}
	
}

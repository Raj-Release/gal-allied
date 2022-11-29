package com.shaic.claim.preauth.wizard.view;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.shaic.domain.Intimation;

public class InvestigationDTO {

	private BigDecimal activeStatus;

	private Timestamp activeStatusDate;

	private BigDecimal allocationToId;

	private Timestamp assignedDate;

	private String createdBy;

	private Timestamp createdDate;

	private BigDecimal documentReceivedFlag;

	private Intimation intimation;

	private BigDecimal fkPolicyKey;

	private BigDecimal fkPreAuthKey;

	private Timestamp hospitalVisitedDate;

	private String investigatorCode;

	private String investigatorName;

	private BigDecimal key;

	private String modifiedBy;

	private Timestamp modifiedDate;

	private String officeCode;

	private String reasonForReferring;

	private Timestamp receivedDate;

	private String remarks;

	private BigDecimal stageId;

	private Timestamp statusDate;

	private BigDecimal statusId;

	private BigDecimal subStatusId;

	private String triggerPoints;

	private BigDecimal version;

	public BigDecimal getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(BigDecimal activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Timestamp getActiveStatusDate() {
		return activeStatusDate;
	}

	public void setActiveStatusDate(Timestamp activeStatusDate) {
		this.activeStatusDate = activeStatusDate;
	}

	public BigDecimal getAllocationToId() {
		return allocationToId;
	}

	public void setAllocationToId(BigDecimal allocationToId) {
		this.allocationToId = allocationToId;
	}

	public Timestamp getAssignedDate() {
		return assignedDate;
	}

	public void setAssignedDate(Timestamp assignedDate) {
		this.assignedDate = assignedDate;
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

	public BigDecimal getDocumentReceivedFlag() {
		return documentReceivedFlag;
	}

	public void setDocumentReceivedFlag(BigDecimal documentReceivedFlag) {
		this.documentReceivedFlag = documentReceivedFlag;
	}

	public Intimation getIntimation() {
		return intimation;
	}

	public void setIntimation(Intimation intimation) {
		this.intimation = intimation;
	}

	public BigDecimal getFkPolicyKey() {
		return fkPolicyKey;
	}

	public void setFkPolicyKey(BigDecimal fkPolicyKey) {
		this.fkPolicyKey = fkPolicyKey;
	}

	public BigDecimal getFkPreAuthKey() {
		return fkPreAuthKey;
	}

	public void setFkPreAuthKey(BigDecimal fkPreAuthKey) {
		this.fkPreAuthKey = fkPreAuthKey;
	}

	public Timestamp getHospitalVisitedDate() {
		return hospitalVisitedDate;
	}

	public void setHospitalVisitedDate(Timestamp hospitalVisitedDate) {
		this.hospitalVisitedDate = hospitalVisitedDate;
	}

	public String getInvestigatorCode() {
		return investigatorCode;
	}

	public void setInvestigatorCode(String investigatorCode) {
		this.investigatorCode = investigatorCode;
	}

	public String getInvestigatorName() {
		return investigatorName;
	}

	public void setInvestigatorName(String investigatorName) {
		this.investigatorName = investigatorName;
	}

	public BigDecimal getKey() {
		return key;
	}

	public void setKey(BigDecimal key) {
		this.key = key;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getOfficeCode() {
		return officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

	public String getReasonForReferring() {
		return reasonForReferring;
	}

	public void setReasonForReferring(String reasonForReferring) {
		this.reasonForReferring = reasonForReferring;
	}

	public Timestamp getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Timestamp receivedDate) {
		this.receivedDate = receivedDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public BigDecimal getStageId() {
		return stageId;
	}

	public void setStageId(BigDecimal stageId) {
		this.stageId = stageId;
	}

	public Timestamp getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(Timestamp statusDate) {
		this.statusDate = statusDate;
	}

	public BigDecimal getStatusId() {
		return statusId;
	}

	public void setStatusId(BigDecimal statusId) {
		this.statusId = statusId;
	}

	public BigDecimal getSubStatusId() {
		return subStatusId;
	}

	public void setSubStatusId(BigDecimal subStatusId) {
		this.subStatusId = subStatusId;
	}

	public String getTriggerPoints() {
		return triggerPoints;
	}

	public void setTriggerPoints(String triggerPoints) {
		this.triggerPoints = triggerPoints;
	}

	public BigDecimal getVersion() {
		return version;
	}

	public void setVersion(BigDecimal version) {
		this.version = version;
	}

}

package com.shaic.claim.reimbursement.rawanalysis;

import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractRowEnablerDTO;

public class RawInitiatedRequestDTO extends AbstractRowEnablerDTO{
	
	private String category;
	private String subCategory;
	private String remarksForEscalation;
	private String initiatedBy;
	private Date intiatedDate;
	private String stage;
	private Long statusId;
	private Long stageId;
	private SelectValue resolutionfromRaw;
	private String remarksfromRaw;
	private String rawRemarksUpdatedBy;
	private Date rawRemarksUpadedDate;
	private String statusValue;
	private String stageValue;
	private Long rawinvestigationKey;
	private String resolutionRawValue;
	
	private Object workFlowObject;
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getSubCategory() {
		return subCategory;
	}
	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}
	public String getRemarksForEscalation() {
		return remarksForEscalation;
	}
	public void setRemarksForEscalation(String remarksForEscalation) {
		this.remarksForEscalation = remarksForEscalation;
	}
	public String getInitiatedBy() {
		return initiatedBy;
	}
	public void setInitiatedBy(String initiatedBy) {
		this.initiatedBy = initiatedBy;
	}
	public Date getIntiatedDate() {
		return intiatedDate;
	}
	public void setIntiatedDate(Date intiatedDate) {
		this.intiatedDate = intiatedDate;
	}
	public String getStage() {
		return stage;
	}
	public void setStage(String stage) {
		this.stage = stage;
	}
	public Long getStatusId() {
		return statusId;
	}
	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}
	public Long getStageId() {
		return stageId;
	}
	public void setStageId(Long stageId) {
		this.stageId = stageId;
	}
	public SelectValue getResolutionfromRaw() {
		return resolutionfromRaw;
	}
	public void setResolutionfromRaw(SelectValue resolutionfromRaw) {
		this.resolutionfromRaw = resolutionfromRaw;
	}
	public String getRemarksfromRaw() {
		return remarksfromRaw;
	}
	public void setRemarksfromRaw(String remarksfromRaw) {
		this.remarksfromRaw = remarksfromRaw;
	}
	public String getRawRemarksUpdatedBy() {
		return rawRemarksUpdatedBy;
	}
	public void setRawRemarksUpdatedBy(String rawRemarksUpdatedBy) {
		this.rawRemarksUpdatedBy = rawRemarksUpdatedBy;
	}
	public Date getRawRemarksUpadedDate() {
		return rawRemarksUpadedDate;
	}
	public void setRawRemarksUpadedDate(Date rawRemarksUpadedDate) {
		this.rawRemarksUpadedDate = rawRemarksUpadedDate;
	}
	public String getStatusValue() {
		return statusValue;
	}
	public void setStatusValue(String statusValue) {
		this.statusValue = statusValue;
	}
	public String getStageValue() {
		return stageValue;
	}
	public void setStageValue(String stageValue) {
		this.stageValue = stageValue;
	}
	public Long getRawinvestigationKey() {
		return rawinvestigationKey;
	}
	public void setRawinvestigationKey(Long rawinvestigationKey) {
		this.rawinvestigationKey = rawinvestigationKey;
	}
	public String getResolutionRawValue() {
		return resolutionRawValue;
	}
	public void setResolutionRawValue(String resolutionRawValue) {
		this.resolutionRawValue = resolutionRawValue;
	}
	public Object getWorkFlowObject() {
		return workFlowObject;
	}
	public void setWorkFlowObject(Object workFlowObject) {
		this.workFlowObject = workFlowObject;
	}	
}

package com.shaic.claim.preauth.wizard.dto;

import java.io.File;
import java.sql.Timestamp;
import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;

public class FieldVisitRequestDTO {
	
	private Long key;

	private SelectValue allocationTo;

	private Boolean documentReceivedFlag;

	private String executiveComments;

	private File fileUpload;

	private Long intimationKey;

	private Long policyKey;

	private Long preauthKey;

	private String fvrId;

	private Date fvrReceivedDate;

	private String fvrTriggerPoints;

	private String officeCode;

	private String representativeCode;

	private String representativeName;

	private String status;

	private Timestamp statusDate;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public SelectValue getAllocationTo() {
		return allocationTo;
	}

	public void setAllocationTo(SelectValue allocationTo) {
		this.allocationTo = allocationTo;
	}

	public Boolean getDocumentReceivedFlag() {
		return documentReceivedFlag;
	}

	public void setDocumentReceivedFlag(Boolean documentReceivedFlag) {
		this.documentReceivedFlag = documentReceivedFlag;
	}

	public String getExecutiveComments() {
		return executiveComments;
	}

	public void setExecutiveComments(String executiveComments) {
		this.executiveComments = executiveComments;
	}

	public File getFileUpload() {
		return fileUpload;
	}

	public void setFileUpload(File fileUpload) {
		this.fileUpload = fileUpload;
	}

	public Long getIntimationKey() {
		return intimationKey;
	}

	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
	}

	public Long getPolicyKey() {
		return policyKey;
	}

	public void setPolicyKey(Long policyKey) {
		this.policyKey = policyKey;
	}

	public Long getPreauthKey() {
		return preauthKey;
	}

	public void setPreauthKey(Long preauthKey) {
		this.preauthKey = preauthKey;
	}

	public String getFvrId() {
		return fvrId;
	}

	public void setFvrId(String fvrId) {
		this.fvrId = fvrId;
	}

	public Date getFvrReceivedDate() {
		return fvrReceivedDate;
	}

	public void setFvrReceivedDate(Date fvrReceivedDate) {
		this.fvrReceivedDate = fvrReceivedDate;
	}

	public String getFvrTriggerPoints() {
		return fvrTriggerPoints;
	}

	public void setFvrTriggerPoints(String fvrTriggerPoints) {
		this.fvrTriggerPoints = fvrTriggerPoints;
	}

	public String getOfficeCode() {
		return officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

	public String getRepresentativeCode() {
		return representativeCode;
	}

	public void setRepresentativeCode(String representativeCode) {
		this.representativeCode = representativeCode;
	}

	public String getRepresentativeName() {
		return representativeName;
	}

	public void setRepresentativeName(String representativeName) {
		this.representativeName = representativeName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(Timestamp statusDate) {
		this.statusDate = statusDate;
	}

}

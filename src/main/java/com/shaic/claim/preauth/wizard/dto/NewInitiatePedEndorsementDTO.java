package com.shaic.claim.preauth.wizard.dto;

import javax.validation.constraints.NotNull;

import com.shaic.arch.fields.dto.SelectValue;

public class NewInitiatePedEndorsementDTO {
	
	private Long oldPedEndorsementKey;
	
	private SelectValue pedCode;
	
	private String description;
	
	private Long key;
	
	private Long ICDChapterId;
	
	private Long ICDBlockId;
	
	private Long ICDCodeId;
	
	private Long pedCodeId;
	
	@NotNull(message = "Please select ICD Chapter")
	private SelectValue ICDChapter;
	
	@NotNull(message = "Please select ICD Block")
	private SelectValue ICDBlock;
	
	@NotNull(message = "Please select ICD Code")
	private SelectValue ICDCode;
	
	private String officeCode;
	
	private SelectValue source;
	
	private String othersSpecify;
	
	private Long activeStatus;
	
	private String deletedFlag;
	
//	@NotNull(message = "Please Enter Doctor Remarks")
//	@Size (min =1 , message = "Please Enter Doctor Remarks")
	private String doctorRemarks;
	
	
	public Long getOldPedEndorsementKey() {
		return oldPedEndorsementKey;
	}
	
	public void setOldPedEndorsementKey(Long oldPedEndorsementKey) {
		this.oldPedEndorsementKey = oldPedEndorsementKey;
	}
	
	public Long getKey() {
		return key;
	}
	
	public void setKey(Long key) {
		this.key = key;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public SelectValue getICDChapter() {
		return ICDChapter;
	}
	
	public void setICDChapter(SelectValue iCDChapter) {
		ICDChapter = iCDChapter;
	}
	public SelectValue getICDBlock() {
		return ICDBlock;
	}
	
	public void setICDBlock(SelectValue iCDBlock) {
		ICDBlock = iCDBlock;
	}
	
	public SelectValue getICDCode() {
		return ICDCode;
	}
	
	public void setICDCode(SelectValue iCDCode) {
		ICDCode = iCDCode;
	}
	
	public SelectValue getSource() {
		return source;
	}
	public void setSource(SelectValue source) {
		this.source = source;
	}
	
	public String getOthersSpecify() {
		return othersSpecify;
	}
	
	public void setOthersSpecify(String othersSpecify) {
		this.othersSpecify = othersSpecify;
	}
	
	public String getDoctorRemarks() {
		return doctorRemarks;
	}
	
	public void setDoctorRemarks(String doctorRemarks) {
		this.doctorRemarks = doctorRemarks;
	}
	

	public String getOfficeCode() {
		return officeCode;
	}
	
	
	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

	public SelectValue getPedCode() {
		return pedCode;
	}

	public void setPedCode(SelectValue pedCode) {
		this.pedCode = pedCode;
	}

	public Long getICDChapterId() {
		return ICDChapterId;
	}

	public void setICDChapterId(Long iCDChapterId) {
		ICDChapterId = iCDChapterId;
	}

	public Long getICDBlockId() {
		return ICDBlockId;
	}

	public void setICDBlockId(Long iCDBlockId) {
		ICDBlockId = iCDBlockId;
	}

	public Long getICDCodeId() {
		return ICDCodeId;
	}

	public void setICDCodeId(Long iCDCodeId) {
		ICDCodeId = iCDCodeId;
	}

	public Long getPedCodeId() {
		return pedCodeId;
	}

	public void setPedCodeId(Long pedCodeId) {
		this.pedCodeId = pedCodeId;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
	}

}

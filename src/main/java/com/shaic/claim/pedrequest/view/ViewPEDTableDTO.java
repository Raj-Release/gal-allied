package com.shaic.claim.pedrequest.view;

import javax.validation.constraints.NotNull;

import com.shaic.arch.fields.dto.SelectValue;

public class ViewPEDTableDTO {

	private Long key;

	@NotNull(message="Please Select Ped Description")
	private SelectValue pedCode;

	private String description;

	@NotNull(message = "Please Select ICD Chapter")
	private SelectValue icdChapter;

	@NotNull(message = "Please Select ICD Block")
	private SelectValue icdBlock;

	@NotNull(message = "Please Select ICD Code")
	private SelectValue icdCode;

	@NotNull(message="Please Select Source Value")
	private SelectValue source;
	
	private String othersSpecify;
	
	private Long activeStatus;
	
	private Boolean enableOrDisable;
	
	private String deletedFlag;

//	@NotNull(message="Please Enter Doctor remarks")
//	@Size (min =1, message="Please Enter Doctor remarks")
	private String doctorRemarks;
	
	private Boolean permanentExclusion;
	
	private String permanentExclusionFlag;

	public SelectValue getPedCode() {
		return pedCode;
	}

	public void setPedCode(SelectValue pedCode) {
		this.pedCode = pedCode;
	}

	public SelectValue getIcdChapter() {
		return icdChapter;
	}

	public void setIcdChapter(SelectValue icdChapter) {
		this.icdChapter = icdChapter;
	}

	public SelectValue getIcdBlock() {
		return icdBlock;
	}

	public void setIcdBlock(SelectValue icdBlock) {
		this.icdBlock = icdBlock;
	}

	public SelectValue getIcdCode() {
		return icdCode;
	}

	public void setIcdCode(SelectValue icdCode) {
		this.icdCode = icdCode;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getEnableOrDisable() {
		return enableOrDisable;
	}

	public void setEnableOrDisable(Boolean enableOrDisable) {
		this.enableOrDisable = enableOrDisable;
	}

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

	public String getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
	}

	public Boolean getPermanentExclusion() {
		return permanentExclusion;
	}

	public void setPermanentExclusion(Boolean permanentExclusion) {
		this.permanentExclusion = permanentExclusion;
	}

	public String getPermanentExclusionFlag() {
		return permanentExclusionFlag;
	}

	public void setPermanentExclusionFlag(String permanentExclusionFlag) {
		this.permanentExclusionFlag = permanentExclusionFlag;
	}
	
}

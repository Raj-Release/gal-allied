package com.shaic.claim.preauth.wizard.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.shaic.arch.table.AbstractRowEnablerDTO;

public class UpdateOtherClaimDetailDTO extends AbstractRowEnablerDTO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9080269699544479876L;

	@Size(min = 1 , message = "Please Enter Intimation Number")
	private String intimationNo;
	
	private Long intimationKey;
	
	private Long claimKey;
	
	private Long cashlessKey;
	
	private String claimType;
	
	@Size(min = 1 , message = "Please Enter InsurerName")
	@NotNull(message = "Please Enter InsurerName")
	private String insurerName;
	
	@Size(min = 1 , message = "Please Enter Primary Diagnosis/procedure")
	@NotNull(message = "Please Enter Primary Diagnosis/procedure")
	private String primaryProcedure;
	
	@Size(min = 1 ,message = "Please Enter ICD Chapter")
	@NotNull(message = "Please Enter ICD Chapter")
	private String icdChaper;
	
	@Size(min = 1 ,message = "Please Enter ICD Block")
	@NotNull(message = "Please Enter ICD Block")
	private String icdBlock;
	
	@Size(min = 1 ,message = "Please Enter ICD Code")
	@NotNull(message = "Please Enter ICD Code")
	private String icdCode;
	
	private Long claimAmount;
	
	private Long deductibles;
	
	private Long admissibleAmount;
	
	private String remarks;
	
	private Boolean enableOrDisable = true;
	
	private Boolean editFlag = false;
	
	private String editFlagValue;
	
	private Long statusKey;
	
	private Long stageKey;

	public Boolean getEnableOrDisable() {
		return enableOrDisable;
	}

	public void setEnableOrDisable(Boolean enableOrDisable) {
		this.enableOrDisable = enableOrDisable;
	}

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public String getInsurerName() {
		return insurerName;
	}

	public void setInsurerName(String insurerName) {
		this.insurerName = insurerName;
	}

	public String getPrimaryProcedure() {
		return primaryProcedure;
	}

	public void setPrimaryProcedure(String primaryProcedure) {
		this.primaryProcedure = primaryProcedure;
	}

	public String getIcdChaper() {
		return icdChaper;
	}

	public void setIcdChaper(String icdChaper) {
		this.icdChaper = icdChaper;
	}

	public String getIcdCode() {
		return icdCode;
	}

	public void setIcdCode(String icdCode) {
		this.icdCode = icdCode;
	}

	public Long getClaimAmount() {
		return claimAmount;
	}

	public void setClaimAmount(Long claimAmount) {
		this.claimAmount = claimAmount;
	}
	
	public Long getDeductibles() {
		return deductibles;
	}

	public void setDeductibles(Long deductibles) {
		this.deductibles = deductibles;
	}

	public Long getAdmissibleAmount() {
		return admissibleAmount;
	}

	public Long getCashlessKey() {
		return cashlessKey;
	}

	public void setCashlessKey(Long cashlessKey) {
		this.cashlessKey = cashlessKey;
	}

	public void setAdmissibleAmount(Long admissibleAmount) {
		this.admissibleAmount = admissibleAmount;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getIcdBlock() {
		return icdBlock;
	}

	public void setIcdBlock(String icdBlock) {
		this.icdBlock = icdBlock;
	}

	public Boolean getEditFlag() {
		return editFlag;
	}

	public void setEditFlag(Boolean editFlag) {
		this.editFlag = editFlag;
		this.editFlagValue = this.editFlag ? "Y":"N";
	}

	public String getEditFlagValue() {
		return editFlagValue;
	}

	public void setEditFlagValue(String editFlagValue) {
		this.editFlagValue = editFlagValue;
		this.editFlag = this.editFlagValue != null ? this.editFlagValue.equalsIgnoreCase("Y") ? true: false:false;
		this.enableOrDisable = this.editFlagValue != null ? this.editFlagValue.equalsIgnoreCase("Y") ? true: false:false;
	}

	public Long getIntimationKey() {
		return intimationKey;
	}

	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
	}

	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	public Long getStatusKey() {
		return statusKey;
	}

	public void setStatusKey(Long statusKey) {
		this.statusKey = statusKey;
	}

	public Long getStageKey() {
		return stageKey;
	}

	public void setStageKey(Long stageKey) {
		this.stageKey = stageKey;
	}

	

}

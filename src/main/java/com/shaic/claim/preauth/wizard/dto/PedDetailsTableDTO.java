package com.shaic.claim.preauth.wizard.dto;

import java.util.List;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.preauth.ExclusionDetails;

public class PedDetailsTableDTO {
	private Long key;
	
	private String diagnosisName;
	
	private String policyAgeing;
	
	private String pedName;
	
	private String pedCode;
	
	private SelectValue pedExclusionImpactOnDiagnosis;
	
	private String impactOnDiagnosisValue;
	
	private Long preauthKey;
	
	private SelectValue exclusionDetails;
	
	private String exclusionDetailsValue;
	
	private List<ExclusionDetails> exclusionAllDetails;
	
	private Boolean enableOrDisable;
	
	private String processFlag;
	
	private String actions;
	
	private SelectValue copay;
	
	private String coPayValue;
	
	private String remarks;
	
	private String recTypeFlag = "A";
	
	private Boolean isShowingCopay = false;
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getPedName() {
		return pedName;
	}

	public void setPedName(String pedName) {
		this.pedName = pedName;
	}


	public SelectValue getPedExclusionImpactOnDiagnosis() {
		return pedExclusionImpactOnDiagnosis;
	}

	public void setPedExclusionImpactOnDiagnosis(
			SelectValue pedExclusionImpactOnDiagnosis) {
		this.pedExclusionImpactOnDiagnosis = pedExclusionImpactOnDiagnosis;
	}

	public Long getPreauthKey() {
		return preauthKey;
	}

	public void setPreauthKey(Long preauthKey) {
		this.preauthKey = preauthKey;
	}

	public Boolean getEnableOrDisable() {
		return enableOrDisable;
	}

	public void setEnableOrDisable(Boolean enableOrDisable) {
		this.enableOrDisable = enableOrDisable;
	}

	public String getProcessFlag() {
		return processFlag;
	}

	public void setProcessFlag(String processFlag) {
		this.processFlag = processFlag;
	}

	public String getActions() {
		return actions;
	}

	public void setActions(String actions) {
		this.actions = actions;
	}

	public String getDiagnosisName() {
		return diagnosisName;
	}

	public void setDiagnosisName(String diagnosisName) {
		this.diagnosisName = diagnosisName;
	}

	public String getPedCode() {
		return pedCode;
	}

	public void setPedCode(String pedCode) {
		this.pedCode = pedCode;
	}

	public String getPolicyAgeing() {
		return policyAgeing;
	}

	public void setPolicyAgeing(String policyAgeing) {
		this.policyAgeing = policyAgeing;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public SelectValue getExclusionDetails() {
		return exclusionDetails;
	}

	public void setExclusionDetails(SelectValue exclusionDetails) {
		this.exclusionDetails = exclusionDetails;
	}

	public List<ExclusionDetails> getExclusionAllDetails() {
		return exclusionAllDetails;
	}

	public void setExclusionAllDetails(List<ExclusionDetails> exclusionAllDetails) {
		this.exclusionAllDetails = exclusionAllDetails;
	}

	public SelectValue getCopay() {
		return copay;
	}

	public void setCopay(SelectValue copay) {
		this.copay = copay;
	}

	public String getRecTypeFlag() {
		return recTypeFlag;
	}

	public void setRecTypeFlag(String recTypeFlag) {
		this.recTypeFlag = recTypeFlag;
	}

	public Boolean getIsShowingCopay() {
		return isShowingCopay;
	}

	public void setIsShowingCopay(Boolean isShowingCopay) {
		this.isShowingCopay = isShowingCopay;
	}

	public String getImpactOnDiagnosisValue() {
		return impactOnDiagnosisValue;
	}

	public void setImpactOnDiagnosisValue(String impactOnDiagnosisValue) {
		this.impactOnDiagnosisValue = impactOnDiagnosisValue;
	}

	public String getExclusionDetailsValue() {
		return exclusionDetailsValue;
	}

	public void setExclusionDetailsValue(String exclusionDetailsValue) {
		this.exclusionDetailsValue = exclusionDetailsValue;
	}

	public String getCoPayValue() {
		return coPayValue;
	}

	public void setCoPayValue(String coPayValue) {
		this.coPayValue = coPayValue;
	}

}

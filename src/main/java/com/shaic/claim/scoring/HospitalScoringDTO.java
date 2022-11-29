package com.shaic.claim.scoring;

import com.shaic.domain.Status;
import com.shaic.domain.preauth.Stage;

public class HospitalScoringDTO {
	
	private String scoringName;
	private String scoringValue;
	private Boolean scoringBooleanValue;
	private Boolean optionVisible;
	private String textFieldStyleName;
	private Long subCategoryKey;
	private String componentId;
	private Boolean optionEnabled;
	
	//R1292
	private Integer actualCategoryId;
	private Integer actualSubCategoryId;
	private Long key;
	
	public String getScoringName() {
		return scoringName;
	}
	public void setScoringName(String scoringName) {
		this.scoringName = scoringName;
	}
	public String getScoringValue() {
		return scoringValue;
	}
	public void setScoringValue(String scoringValue) {
		this.scoringValue = scoringValue;
	}
	public Boolean isOptionVisible() {
		return optionVisible;
	}
	public void setOptionVisible(Boolean optionVisible) {
		this.optionVisible = optionVisible;
	}
	public String getTextFieldStyleName() {
		return textFieldStyleName;
	}
	public void setTextFieldStyleName(String textFieldStyleName) {
		this.textFieldStyleName = textFieldStyleName;
	}
	public Long getSubCategoryKey() {
		return subCategoryKey;
	}
	public void setSubCategoryKey(Long subCategoryKey) {
		this.subCategoryKey = subCategoryKey;
	}
	public Boolean getScoringBooleanValue() {
		return scoringBooleanValue;
	}
	public void setScoringBooleanValue(Boolean scoringBooleanValue) {
		this.scoringBooleanValue = scoringBooleanValue;
	}
	public String getComponentId() {
		return componentId;
	}
	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}
	public Boolean getOptionEnabled() {
		return optionEnabled;
	}
	public void setOptionEnabled(Boolean optionEnabled) {
		this.optionEnabled = optionEnabled;
	}
	public Integer getActualCategoryId() {
		return actualCategoryId;
	}
	public void setActualCategoryId(Integer actualCategoryId) {
		this.actualCategoryId = actualCategoryId;
	}
	public Integer getActualSubCategoryId() {
		return actualSubCategoryId;
	}
	public void setActualSubCategoryId(Integer actualSubCategoryId) {
		this.actualSubCategoryId = actualSubCategoryId;
	}
	public Long getKey() {
		return key;
	}
	public void setKey(Long key) {
		this.key = key;
	}

}

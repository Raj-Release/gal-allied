package com.shaic.claim.userproduct.document.search;

import com.vaadin.v7.ui.OptionGroup;

public class UserManagementLabelDTO {

private int sno;
	
	private Long key;
	
	private String value;
	
	private Boolean labelEnable;
	
	private Boolean includeEnable = false;
	
	private Boolean excludeEnable =  false;
	
	private Boolean includeValue= false;
	
	private Boolean excludeValue =  false;
	
    private OptionGroup includeRadio;
	
	private OptionGroup excludeRadio;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public int getSno() {
		return sno;
	}

	public void setSno(int sno) {
		this.sno = sno;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Boolean getLabelEnable() {
		return labelEnable;
	}

	public void setLabelEnable(Boolean labelEnable) {
		this.labelEnable = labelEnable;
	}

	public Boolean getIncludeEnable() {
		return includeEnable;
	}

	public void setIncludeEnable(Boolean includeEnable) {
		this.includeEnable = includeEnable;
	}

	public Boolean getExcludeEnable() {
		return excludeEnable;
	}

	public void setExcludeEnable(Boolean excludeEnable) {
		this.excludeEnable = excludeEnable;
	}

	public Boolean getIncludeValue() {
		return includeValue;
	}

	public void setIncludeValue(Boolean includeValue) {
		this.includeValue = includeValue;
	}

	public Boolean getExcludeValue() {
		return excludeValue;
	}

	public void setExcludeValue(Boolean excludeValue) {
		this.excludeValue = excludeValue;
	}

	public OptionGroup getIncludeRadio() {
		return includeRadio;
	}

	public void setIncludeRadio(OptionGroup includeRadio) {
		this.includeRadio = includeRadio;
	}

	public OptionGroup getExcludeRadio() {
		return excludeRadio;
	}

	public void setExcludeRadio(OptionGroup excludeRadio) {
		this.excludeRadio = excludeRadio;
	}

	
}

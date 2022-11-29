package com.shaic.claim.userproduct.document.search;

import java.util.List;

import org.vaadin.addons.comboboxmultiselect.ComboBoxMultiselect;

import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.CheckBox;

public class UserMgmtApplicableCpuDTO {
private int sno;
	
	private String cpuCodewithName;
	
	private String cpuCode;
	
	private Boolean accessability;
	
	private Boolean checkBoxValueAccesibility = false;
	
	private Boolean isEnabledAccesibility = false;
	
	private Long activeStatus;
	
	private Boolean gmc;
	
	private Boolean gmcCheckBox = false;
	
	private Boolean gmcIsEnabled = false;
	
	private Boolean retail;
	
	private Boolean retailCheckBox = false;
	
	private Boolean retailIsEnabled = false;
	
	private String lobFlag;
	
	private String activeStatusGmcRtl;
	
	private CheckBox gmcBox;
	
	private CheckBox retailBox;
	
	private ComboBoxMultiselect areaCodeBox;
	
	private Boolean areaCodeEnableBox = false;
	
	private BeanItemContainer<SelectValue> areaCodeList;
	
	private List<String> areaCodeSelectedList;

	private List<String> selectedAreCodList;

	public int getSno() {
		return sno;
	}

	public void setSno(int sno) {
		this.sno = sno;
	}

	public String getCpuCodewithName() {
		return cpuCodewithName;
	}

	public void setCpuCodewithName(String cpuCodewithName) {
		this.cpuCodewithName = cpuCodewithName;
	}

	public String getCpuCode() {
		return cpuCode;
	}

	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
	}

	public Boolean getAccessability() {
		return accessability;
	}

	public void setAccessability(Boolean accessability) {
		this.accessability = accessability;
	}

	public Boolean getCheckBoxValueAccesibility() {
		return checkBoxValueAccesibility;
	}

	public void setCheckBoxValueAccesibility(Boolean checkBoxValueAccesibility) {
		this.checkBoxValueAccesibility = checkBoxValueAccesibility;
	}

	public Boolean getIsEnabledAccesibility() {
		return isEnabledAccesibility;
	}

	public void setIsEnabledAccesibility(Boolean isEnabledAccesibility) {
		this.isEnabledAccesibility = isEnabledAccesibility;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Boolean getGmc() {
		return gmc;
	}

	public void setGmc(Boolean gmc) {
		this.gmc = gmc;
	}

	public Boolean getGmcCheckBox() {
		return gmcCheckBox;
	}

	public void setGmcCheckBox(Boolean gmcCheckBox) {
		this.gmcCheckBox = gmcCheckBox;
	}

	public Boolean getGmcIsEnabled() {
		return gmcIsEnabled;
	}

	public void setGmcIsEnabled(Boolean gmcIsEnabled) {
		this.gmcIsEnabled = gmcIsEnabled;
	}

	public Boolean getRetail() {
		return retail;
	}

	public void setRetail(Boolean retail) {
		this.retail = retail;
	}

	public Boolean getRetailCheckBox() {
		return retailCheckBox;
	}

	public void setRetailCheckBox(Boolean retailCheckBox) {
		this.retailCheckBox = retailCheckBox;
	}

	public Boolean getRetailIsEnabled() {
		return retailIsEnabled;
	}

	public void setRetailIsEnabled(Boolean retailIsEnabled) {
		this.retailIsEnabled = retailIsEnabled;
	}

	public String getLobFlag() {
		return lobFlag;
	}

	public void setLobFlag(String lobFlag) {
		this.lobFlag = lobFlag;
	}

	public String getActiveStatusGmcRtl() {
		return activeStatusGmcRtl;
	}

	public void setActiveStatusGmcRtl(String activeStatusGmcRtl) {
		this.activeStatusGmcRtl = activeStatusGmcRtl;
	}

	public CheckBox getGmcBox() {
		return gmcBox;
	}

	public void setGmcBox(CheckBox gmcBox) {
		this.gmcBox = gmcBox;
	}

	public CheckBox getRetailBox() {
		return retailBox;
	}

	public void setRetailBox(CheckBox retailBox) {
		this.retailBox = retailBox;
	}

	public ComboBoxMultiselect getAreaCodeBox() {
		return areaCodeBox;
	}

	public void setAreaCodeBox(ComboBoxMultiselect areaCodeBox) {
		this.areaCodeBox = areaCodeBox;
	}

	public BeanItemContainer<SelectValue> getAreaCodeList() {
		return areaCodeList;
	}

	public void setAreaCodeList(BeanItemContainer<SelectValue> areaCodeList) {
		this.areaCodeList = areaCodeList;
	}

	public Boolean getAreaCodeEnableBox() {
		return areaCodeEnableBox;
	}

	public void setAreaCodeEnableBox(Boolean areaCodeEnableBox) {
		this.areaCodeEnableBox = areaCodeEnableBox;
	}

	public List<String> getAreaCodeSelectedList() {
		return areaCodeSelectedList;
	}

	public void setAreaCodeSelectedList(List<String> areaCodeSelectedList) {
		this.areaCodeSelectedList = areaCodeSelectedList;
	}

	public List<String> getSelectedAreCodList() {
		return selectedAreCodList;
	}

	public void setSelectedAreCodList(List<String> selectedAreCodList) {
		this.selectedAreCodList = selectedAreCodList;
	}
	
}

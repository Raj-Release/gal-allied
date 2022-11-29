package com.shaic.claim.pincodemapping;

import java.io.Serializable;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class SearchPinCodeTableDTO extends AbstractTableDTO  implements Serializable {
	
	private String pinCode;
	private String state;
	private String district;
	private String city;
	private String category;
	private String tier;
	private SelectValue zone;
	private String strZone;
	private String strClass;
	private SelectValue cityClass;
	private Boolean chkSelect;
	
	private BeanItemContainer<SelectValue> zoneList;
	private BeanItemContainer<SelectValue> cityClassList;
	private String checkBoxStatus;
	private Integer serialNo;
	
	private String zoneValue;
	private String cityClassValue;
	
	public String getPinCode() {
		return pinCode;
	}
	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getTier() {
		return tier;
	}
	public void setTier(String tier) {
		this.tier = tier;
	}
	
	public Boolean getChkSelect() {
		return chkSelect;
	}
	public void setChkSelect(Boolean chkSelect) {
		this.chkSelect = chkSelect;
	}
	public BeanItemContainer<SelectValue> getZoneList() {
		return zoneList;
	}
	public void setZoneList(BeanItemContainer<SelectValue> zoneList) {
		this.zoneList = zoneList;
	}
	public BeanItemContainer<SelectValue> getCityClassList() {
		return cityClassList;
	}
	public void setCityClassList(BeanItemContainer<SelectValue> cityClassList) {
		this.cityClassList = cityClassList;
	}
	public String getCheckBoxStatus() {
		return checkBoxStatus;
	}
	public void setCheckBoxStatus(String checkBoxStatus) {
		this.checkBoxStatus = checkBoxStatus;
	}
	public Integer getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(Integer serialNo) {
		this.serialNo = serialNo;
	}
	
	public String getZoneValue() {
		return zoneValue;
	}
	public void setZoneValue(String zoneValue) {
		this.zoneValue = zoneValue;
	}
	public String getCityClassValue() {
		return cityClassValue;
	}
	public void setCityClassValue(String cityClassValue) {
		this.cityClassValue = cityClassValue;
	}
	
	public SelectValue getZone() {
		return zone;
	}
	public void setZone(SelectValue zone) {
		this.zone = zone;
	}
	public String getStrZone() {
		return strZone;
	}
	public void setStrZone(String strZone) {
		this.strZone = strZone;
	}
	public String getStrClass() {
		return strClass;
	}
	public void setStrClass(String strClass) {
		this.strClass = strClass;
	}
	public SelectValue getCityClass() {
		return cityClass;
	}
	public void setCityClass(SelectValue cityClass) {
		this.cityClass = cityClass;
	}
	
	
	
}

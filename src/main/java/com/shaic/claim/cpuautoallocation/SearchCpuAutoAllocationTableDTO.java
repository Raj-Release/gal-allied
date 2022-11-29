package com.shaic.claim.cpuautoallocation;

import java.io.Serializable;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class SearchCpuAutoAllocationTableDTO extends AbstractTableDTO  implements Serializable {
	
	private Long cpuCode;
	private String cpuCodestr;
	private String cpuName;
	private String cpu;
	private Double minAmt;
	private Double maxAmt;
	private SelectValue withinLimit;
	private SelectValue limitCases;
	private SelectValue aboveLimit;
	private SelectValue corpOffice;
	
	private BeanItemContainer<SelectValue> withinLimitList;
	private BeanItemContainer<SelectValue> limitCasesList;
	private BeanItemContainer<SelectValue> aboveLimitList;
	private BeanItemContainer<SelectValue> corpOfficeList;
	
	private Boolean chkSelect;
	
	private String checkBoxStatus;
	
	private Long withinLimitValue;
	private String limitCasesValue;
	private Long aboveLimitValue;
	private Long corpOfficeValue;
	
	private Integer serialNo;
	
	public Long getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(Long cpuCode) {
		this.cpuCode = cpuCode;
	}
	public String getCpuName() {
		return cpuName;
	}
	public void setCpuName(String cpuName) {
		this.cpuName = cpuName;
	}
	public Double getMinAmt() {
		return minAmt;
	}
	public void setMinAmt(Double minAmt) {
		this.minAmt = minAmt;
	}
	public Double getMaxAmt() {
		return maxAmt;
	}
	public void setMaxAmt(Double maxAmt) {
		this.maxAmt = maxAmt;
	}
	public SelectValue getWithinLimit() {
		return withinLimit;
	}
	public void setWithinLimit(SelectValue withinLimit) {
		this.withinLimit = withinLimit;
	}
	public SelectValue getLimitCases() {
		return limitCases;
	}
	public void setLimitCases(SelectValue limitCases) {
		this.limitCases = limitCases;
	}
	public SelectValue getAboveLimit() {
		return aboveLimit;
	}
	public void setAboveLimit(SelectValue aboveLimit) {
		this.aboveLimit = aboveLimit;
	}
	public SelectValue getCorpOffice() {
		return corpOffice;
	}
	public void setCorpOffice(SelectValue corpOffice) {
		this.corpOffice = corpOffice;
	}
	public BeanItemContainer<SelectValue> getWithinLimitList() {
		return withinLimitList;
	}
	public void setWithinLimitList(BeanItemContainer<SelectValue> withinLimitList) {
		this.withinLimitList = withinLimitList;
	}
	public BeanItemContainer<SelectValue> getLimitCasesList() {
		return limitCasesList;
	}
	public void setLimitCasesList(BeanItemContainer<SelectValue> limitCasesList) {
		this.limitCasesList = limitCasesList;
	}
	public BeanItemContainer<SelectValue> getAboveLimitList() {
		return aboveLimitList;
	}
	public void setAboveLimitList(BeanItemContainer<SelectValue> aboveLimitList) {
		this.aboveLimitList = aboveLimitList;
	}
	public BeanItemContainer<SelectValue> getCorpOfficeList() {
		return corpOfficeList;
	}
	public void setCorpOfficeList(BeanItemContainer<SelectValue> corpOfficeList) {
		this.corpOfficeList = corpOfficeList;
	}
	public String getCpu() {
		return cpu;
	}
	public void setCpu(String cpu) {
		this.cpu = cpu;
	}
	public Boolean getChkSelect() {
		return chkSelect;
	}
	public void setChkSelect(Boolean chkSelect) {
		this.chkSelect = chkSelect;
	}
	public String getCheckBoxStatus() {
		return checkBoxStatus;
	}
	public void setCheckBoxStatus(String checkBoxStatus) {
		this.checkBoxStatus = checkBoxStatus;
	}
	public Long getWithinLimitValue() {
		return withinLimitValue;
	}
	public void setWithinLimitValue(Long withinLimitValue) {
		this.withinLimitValue = withinLimitValue;
	}
	public Long getAboveLimitValue() {
		return aboveLimitValue;
	}
	public void setAboveLimitValue(Long aboveLimitValue) {
		this.aboveLimitValue = aboveLimitValue;
	}
	public Long getCorpOfficeValue() {
		return corpOfficeValue;
	}
	public void setCorpOfficeValue(Long corpOfficeValue) {
		this.corpOfficeValue = corpOfficeValue;
	}
	public String getCpuCodestr() {
		return cpuCodestr;
	}
	public void setCpuCodestr(String cpuCodestr) {
		this.cpuCodestr = cpuCodestr;
	}
	public Integer getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(Integer serialNo) {
		this.serialNo = serialNo;
	}
	public String getLimitCasesValue() {
		return limitCasesValue;
	}
	public void setLimitCasesValue(String limitCasesValue) {
		this.limitCasesValue = limitCasesValue;
	}
	
}

package com.shaic.claim.aadhar.search;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.newcode.wizard.dto.NewIntimationDto;

public class SearchUpdateAadharTableDTO extends AbstractTableDTO implements Serializable{
	
	private Long key;
	private String intimationNo;
	private String policyNo;
	private Long insuredKey;
	private String insuredName;
	private Date policyStartDate;
	private Date policyEndDate;
	private NewIntimationDto newIntimationDto;
	
	private String aadharCardNo;
	private String aadharRemarks;
	private String aadharRefNo;
	
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	public Long getInsuredKey() {
		return insuredKey;
	}
	public void setInsuredKey(Long insuredKey) {
		this.insuredKey = insuredKey;
	}
	public String getInsuredName() {
		return insuredName;
	}
	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}
	public Date getPolicyStartDate() {
		return policyStartDate;
	}
	public void setPolicyStartDate(Date policyStartDate) {
		this.policyStartDate = policyStartDate;
	}
	public Date getPolicyEndDate() {
		return policyEndDate;
	}
	public void setPolicyEndDate(Date policyEndDate) {
		this.policyEndDate = policyEndDate;
	}
	public Long getKey() {
		return key;
	}
	public void setKey(Long key) {
		this.key = key;
	}
	public NewIntimationDto getNewIntimationDto() {
		return newIntimationDto;
	}
	public void setNewIntimationDto(NewIntimationDto newIntimationDto) {
		this.newIntimationDto = newIntimationDto;
	}
	public String getAadharCardNo() {
		return aadharCardNo;
	}
	public void setAadharCardNo(String aadharCardNo) {
		this.aadharCardNo = aadharCardNo;
	}
	public String getAadharRemarks() {
		return aadharRemarks;
	}
	public void setAadharRemarks(String aadharRemarks) {
		this.aadharRemarks = aadharRemarks;
	}
	public String getAadharRefNo() {
		return aadharRefNo;
	}
	public void setAadharRefNo(String aadharRefNo) {
		this.aadharRefNo = aadharRefNo;
	}

}

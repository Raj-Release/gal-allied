package com.shaic.claim.clearcashless.dto;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.newcode.wizard.dto.NewIntimationDto;

public class SearchClearCashlessDTO extends  AbstractTableDTO implements Serializable {
	
	private String intimationNo;
	
	private String policyNo;
	
	private String insuredPatientName;
	
	private String cpuCode;
	
	private String hospitalName;
	
	private	Date dateOfAdmission;
	
	private String productName;
	
	private Long prauthAmount;
	
	private Long bsiAmount;
	
	private Long preauthKey;
	
	private NewIntimationDto newIntimationDTO;
	
	private Long claimKey;
	
	private Boolean isEnhancement = false;
	
	private Boolean isReconsideration = false;
	
	private Boolean isQueryReplyReceived = false;
	
	private String clearCashlessRemarks;
	
	private Boolean isFirstLevelQueryReplyReceived = false;
	
	private String screen;

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

	public String getInsuredPatientName() {
		return insuredPatientName;
	}

	public void setInsuredPatientName(String insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
	}

	public String getCpuCode() {
		return cpuCode;
	}

	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public Date getDateOfAdmission() {
		return dateOfAdmission;
	}

	public void setDateOfAdmission(Date dateOfAdmission) {
		this.dateOfAdmission = dateOfAdmission;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Long getPrauthAmount() {
		return prauthAmount;
	}

	public void setPrauthAmount(Long prauthAmount) {
		this.prauthAmount = prauthAmount;
	}

	public Long getBsiAmount() {
		return bsiAmount;
	}

	public void setBsiAmount(Long bsiAmount) {
		this.bsiAmount = bsiAmount;
	}

	public NewIntimationDto getNewIntimationDTO() {
		return newIntimationDTO;
	}

	public void setNewIntimationDTO(NewIntimationDto newIntimationDTO) {
		this.newIntimationDTO = newIntimationDTO;
	}

	public Long getPreauthKey() {
		return preauthKey;
	}

	public void setPreauthKey(Long preauthKey) {
		this.preauthKey = preauthKey;
	}

	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}

	public Boolean getIsEnhancement() {
		return isEnhancement;
	}

	public void setIsEnhancement(Boolean isEnhancement) {
		this.isEnhancement = isEnhancement;
	}

	public Boolean getIsReconsideration() {
		return isReconsideration;
	}

	public void setIsReconsideration(Boolean isReconsideration) {
		this.isReconsideration = isReconsideration;
	}

	public Boolean getIsQueryReplyReceived() {
		return isQueryReplyReceived;
	}

	public void setIsQueryReplyReceived(Boolean isQueryReplyReceived) {
		this.isQueryReplyReceived = isQueryReplyReceived;
	}

	public String getClearCashlessRemarks() {
		return clearCashlessRemarks;
	}

	public void setClearCashlessRemarks(String clearCashlessRemarks) {
		this.clearCashlessRemarks = clearCashlessRemarks;
	}

	public Boolean getIsFirstLevelQueryReplyReceived() {
		return isFirstLevelQueryReplyReceived;
	}

	public void setIsFirstLevelQueryReplyReceived(
			Boolean isFirstLevelQueryReplyReceived) {
		this.isFirstLevelQueryReplyReceived = isFirstLevelQueryReplyReceived;
	}

	public String getScreen() {
		return screen;
	}

	public void setScreen(String screen) {
		this.screen = screen;
	}

}

package com.shaic.newcode.wizard.dto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.processdraftquery.ClaimQueryDto;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ConvertClaimDTO {
	
	private Long key;
	
	private Double claimedAmount;
	
	private Double provisionAmount;
	
	private String claimStatus;
	
	private String claimType;
	
	private String intimationNumber;
	
	private String denialRemarks;
	
	private SelectValue conversionReason;
	
	private Long conversionReasonId;
	
	private ClaimDto claimDTO ;
	
	private Object dbOutArray ;
	  
	private Date conversionDate;
	/**
	 * below attributes included as part of PA Convert Claim
	 * 
	 */
	
	private String injuryLossDetails;
	
	private String remarks;
	
	private Boolean accDeathflag;
	
	private Date accDeathDate;
	
	private String currentCPUCode;
	
	private String afterConvCPUCode;
	
	private PreauthDTO preAuthDto = new PreauthDTO();
	
	private String crmFlagged;
	
	private String crcFlaggedReason;
    
    private String crcFlaggedRemark;
    
    private String IsPreferredHospital;
    
    private Double InsuredAge;
    
    private ClaimQueryDto QueryDto;
    
    private NewIntimationDto newIntimationDTO;
    
    @NotNull(message="Please Select reason for conversion")
    private BeanItemContainer<SelectValue> conversionReasonList;
    

	public NewIntimationDto getNewIntimationDTO() {
		return newIntimationDTO;
	}

	public void setNewIntimationDTO(NewIntimationDto newIntimationDTO) {
		this.newIntimationDTO = newIntimationDTO;
	}

	public PreauthDTO getPreAuthDto() {
		return preAuthDto;
	}

	public void setPreAuthDto(PreauthDTO preAuthDto) {
		this.preAuthDto = preAuthDto;
	}

	public String getCrmFlagged() {
		return crmFlagged;
	}

	public void setCrmFlagged(String crmFlagged) {
		this.crmFlagged = crmFlagged;
	}

	public String getCrcFlaggedReason() {
		return crcFlaggedReason;
	}

	public void setCrcFlaggedReason(String crcFlaggedReason) {
		this.crcFlaggedReason = crcFlaggedReason;
	}

	public String getCrcFlaggedRemark() {
		return crcFlaggedRemark;
	}

	public void setCrcFlaggedRemark(String crcFlaggedRemark) {
		this.crcFlaggedRemark = crcFlaggedRemark;
	}

	public String getIsPreferredHospital() {
		return IsPreferredHospital;
	}

	public void setIsPreferredHospital(String isPreferredHospital) {
		IsPreferredHospital = isPreferredHospital;
	}

	public Double getInsuredAge() {
		return InsuredAge;
	}

	public void setInsuredAge(Double insuredAge) {
		InsuredAge = insuredAge;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Double getClaimedAmount() {
		return claimedAmount;
	}

	public void setClaimedAmount(Double claimedAmount) {
		this.claimedAmount = claimedAmount;
	}

	public Double getProvisionAmount() {
		return provisionAmount;
	}

	public void setProvisionAmount(Double provisionAmount) {
		this.provisionAmount = provisionAmount;
	}

	public String getClaimStatus() {
		return claimStatus;
	}

	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}

	public String getDenialRemarks() {
		return denialRemarks;
	}

	public void setDenialRemarks(String denialRemarks) {
		this.denialRemarks = denialRemarks;
	}

	public SelectValue getConversionReason() {
		return conversionReason;
	}

	public void setConversionReason(SelectValue conversionReason) {
		this.conversionReason = conversionReason;
	}

	public Long getConversionReasonId() {
		return conversionReasonId;
	}

	public void setConversionReasonId(Long conversionReasonId) {
		this.conversionReasonId = conversionReasonId;
	}

	public String getIntimationNumber() {
		return intimationNumber;
	}

	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	public ClaimDto getClaimDTO() {
		return claimDTO;
	}

	public void setClaimDTO(ClaimDto claimDTO) {
		this.claimDTO = claimDTO;
	}

	public Object getDbOutArray() {
		return dbOutArray;
	}

	public void setDbOutArray(Object dbOutArray) {
		this.dbOutArray = dbOutArray;
	}
	public Date getConversionDate() {
		return conversionDate;
	}

	public void setConversionDate(Date conversionDate) {
		this.conversionDate = conversionDate;
	}
	public String getInjuryLossDetails() {
		return injuryLossDetails;
	}

	public void setInjuryLossDetails(String injuryLossDetails) {
		this.injuryLossDetails = injuryLossDetails;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Boolean getAccDeathflag() {
		return accDeathflag;
	}

	public void setAccDeathflag(Boolean accDeathflag) {
		this.accDeathflag = accDeathflag;
	}

	public Date getAccDeathDate() {
		return accDeathDate;
	}

	public void setAccDeathDate(Date accDeathDate) {
		this.accDeathDate = accDeathDate;
	}

	public String getCurrentCPUCode() {
		return currentCPUCode;
	}

	public void setCurrentCPUCode(String currentCPUCode) {
		this.currentCPUCode = currentCPUCode;
	}

	public String getAfterConvCPUCode() {
		return afterConvCPUCode;
	}

	public void setAfterConvCPUCode(String afterConvCPUCode) {
		this.afterConvCPUCode = afterConvCPUCode;
	}

	public BeanItemContainer<SelectValue> getConversionReasonList() {
		return conversionReasonList;
	}

	public void setConversionReasonList(
			BeanItemContainer<SelectValue> conversionReasonList) {
		this.conversionReasonList = conversionReasonList;
	}

}

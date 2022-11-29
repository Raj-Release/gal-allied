package com.shaic.claim.reimbursement.processdraftquery;

import java.io.Serializable;
import java.util.List;

import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.ReimbursementQueryDto;
import com.shaic.claim.viewEarlierRodDetails.ViewQueryDTO;
import com.shaic.newcode.wizard.dto.NewIntimationDto;

public class ClaimQueryDto implements Serializable{
	
	
	private ReimbursementQueryDto reimbursementQueryDto;
	
	private List<ViewQueryDTO> queryDetails;
	
	private String diagnosis;
	
	private String crmFlagged;
	
	private String crcFlaggedReason;
    
    private String crcFlaggedRemark;
    
    private String IsPreferredHospital;
    
    private Double InsuredAge;
    
    private NewIntimationDto newIntimationDTO;
    
    public NewIntimationDto getNewIntimationDTO() {
		return newIntimationDTO;
	}

	public void setNewIntimationDTO(NewIntimationDto newIntimationDTO) {
		this.newIntimationDTO = newIntimationDTO;
	}

	private PreauthDTO preAuthDto = new PreauthDTO();
    
    
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

	public PreauthDTO getPreAuthDto() {
		return preAuthDto;
	}

	public void setPreAuthDto(PreauthDTO preAuthDto) {
		this.preAuthDto = preAuthDto;
	}

	public ReimbursementQueryDto getReimbursementQueryDto() {
		return reimbursementQueryDto;
	}

	public void setReimbursementQueryDto(ReimbursementQueryDto reimbursementQueryDto) {
		this.reimbursementQueryDto = reimbursementQueryDto;
	}

	public List<ViewQueryDTO> getQueryDetails() {
		return queryDetails;
	}

	public void setQueryDetails(List<ViewQueryDTO> queryDetails) {
		this.queryDetails = queryDetails;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}
	
	
}

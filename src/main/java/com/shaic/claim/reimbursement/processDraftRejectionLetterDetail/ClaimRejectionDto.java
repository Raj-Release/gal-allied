package com.shaic.claim.reimbursement.processDraftRejectionLetterDetail;

import java.io.Serializable;
import java.util.List;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ReimbursementRejectionDetailsDto;
import com.shaic.claim.ReimbursementRejectionDto;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.newcode.wizard.dto.NewIntimationDto;

public class ClaimRejectionDto implements Serializable{
	
	private String rejectionRole;	

	private ReimbursementRejectionDto reimbursementRejectionDto;
	
	private List<ReimbursementRejectionDetailsDto> rejectionDetailsList;
	
	private List<SelectValue> rejectCategList;
	
	private String diagnosis;	

	private SelectValue reconsiderCase;

	private SelectValue reconsiderReason;
	
	private String crmFlagged;
	private String crcFlaggedReason;
    private String crcFlaggedRemark;
    private String IsPreferredHospital;
    private Double InsuredAge;
    private PreauthDTO preAuthDto = new PreauthDTO();
    private NewIntimationDto newIntimationDTO;
    
    
	
	public NewIntimationDto getNewIntimationDTO() {
		return newIntimationDTO;
	}

	public void setNewIntimationDTO(NewIntimationDto newIntimationDTO) {
		this.newIntimationDTO = newIntimationDTO;
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

	public PreauthDTO getPreAuthDto() {
		return preAuthDto;
	}

	public void setPreAuthDto(PreauthDTO preAuthDto) {
		this.preAuthDto = preAuthDto;
	}

	public ReimbursementRejectionDto getReimbursementRejectionDto() {
		return reimbursementRejectionDto;
	}

	public void setReimbursementRejectionDto(
			ReimbursementRejectionDto reimbursementRejectionDto) {
		this.reimbursementRejectionDto = reimbursementRejectionDto;
	}

	public List<ReimbursementRejectionDetailsDto> getRejectionDetailsList() {
		return rejectionDetailsList;
	}

	public void setRejectionDetailsList(
			List<ReimbursementRejectionDetailsDto> rejectionDetailsList) {
		this.rejectionDetailsList = rejectionDetailsList;
	}

	public String getRejectionRole() {
		return rejectionRole;
	}

	public void setRejectionRole(String rejectionRole) {
		this.rejectionRole = rejectionRole;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public List<SelectValue> getRejectCategList() {
		return rejectCategList;
	}

	public void setRejectCategList(List<SelectValue> rejectCategList) {
		this.rejectCategList = rejectCategList;
	}

	public SelectValue getReconsiderCase() {
		return reconsiderCase;
	}

	public void setReconsiderCase(SelectValue reconsiderCase) {
		this.reconsiderCase = reconsiderCase;
	}

	public SelectValue getReconsiderReason() {
		return reconsiderReason;
	}

	public void setReconsiderReason(SelectValue reconsiderReason) {
		this.reconsiderReason = reconsiderReason;
	}	

}

/**
 * 
 */
package com.shaic.reimbursement.queryrejection.draftrejection.search;

import java.io.Serializable;
import java.util.List;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.ReimbursementRejectionDetailsDto;
import com.shaic.claim.ReimbursementRejectionDto;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.newcode.wizard.dto.NewIntimationDto;

/**
 * @author ntv.narenj
 *
 */
public class SearchDraftRejectionLetterTableDTO extends AbstractTableDTO  implements Serializable{

	private Long claimKey;
	private Long intimationkey;
	private String insuredPatientName;
	private String cpuCode;
	private Long hospitalNameID;
	private String hospitalType;
	private Long cpuId;
	private String intimationNo;
	private String claimNo;
	private String policyNo;
	private String hospitalName;
	private String hospitalAddress;
	private String hospitalCity;
	private String reasonForAdmission;
	private String claimStatus;
//	private HumanTask humanTaskDTO;
	private Long rodKey;
	
	private String strCpuCode;
	
	private String diagnosis;
	
	private String redraftRejectionRemarks;
	private String rejectionRemarks;
	private String rejectionLetterRemarks;
	private String disapproveRejectionRemarks;
	private ReimbursementRejectionDto reimbursementRejectionDto;

	private String crmFlagged;
	
	private String crcFlaggedReason;
    
    private String crcFlaggedRemark;
    
    private String IsPreferredHospital;
    
    private Double InsuredAge;
    
    private NewIntimationDto newIntimationDTO;
    
    private PreauthDTO preAuthDto = new PreauthDTO();
	
	public PreauthDTO getPreAuthDto() {
		return preAuthDto;
	}
	public void setPreAuthDto(PreauthDTO preAuthDto) {
		this.preAuthDto = preAuthDto;
	}
	public NewIntimationDto getNewIntimationDTO() {
		return newIntimationDTO;
	}
	public void setNewIntimationDTO(NewIntimationDto newIntimationDTO) {
		this.newIntimationDTO = newIntimationDTO;
	}
	private List<SelectValue> rejectCategList;
	
	private List<ReimbursementRejectionDetailsDto> rejectionList;
	
	private SelectValue reconsiderCase;
	
	private SelectValue reconsiderReason;
	
	public String getClaimStatus() {
		return claimStatus;
	}
	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public String getClaimNo() {
		return claimNo;
	}
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}	
	
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public String getHospitalAddress() {
		return hospitalAddress;
	}
	public void setHospitalAddress(String hospitalAddress) {
		this.hospitalAddress = hospitalAddress;
	}
	public String getHospitalCity() {
		return hospitalCity;
	}
	public void setHospitalCity(String hospitalCity) {
		this.hospitalCity = hospitalCity;
	}
	public String getReasonForAdmission() {
		return reasonForAdmission;
	}
	public void setReasonForAdmission(String reasonForAdmission) {
		this.reasonForAdmission = reasonForAdmission;
	}	
	public String getDisapproveRejectionRemarks() {
		return disapproveRejectionRemarks;
	}
	public void setDisapproveRejectionRemarks(String disapproveRejectionRemarks) {
		this.disapproveRejectionRemarks = disapproveRejectionRemarks;
	}
	public ReimbursementRejectionDto getReimbursementRejectionDto() {
		return reimbursementRejectionDto;
	}
	public void setReimbursementRejectionDto(
			ReimbursementRejectionDto reimbursementRejectionDto) {
		this.reimbursementRejectionDto = reimbursementRejectionDto;
	}
	public List<ReimbursementRejectionDetailsDto> getRejectionList() {
		return rejectionList;
	}
	public void setRejectionList(List<ReimbursementRejectionDetailsDto> rejectionList) {
		this.rejectionList = rejectionList;
	}
	public String getRedraftRejectionRemarks() {
		return redraftRejectionRemarks;
	}
	public void setRedraftRejectionRemarks(String redraftRejectionRemarks) {
		this.redraftRejectionRemarks = redraftRejectionRemarks;
	}
	public String getRejectionRemarks() {
		return rejectionRemarks;
	}
	public void setRejectionRemarks(String rejectionRemarks) {
		this.rejectionRemarks = rejectionRemarks;
	}
	public String getRejectionLetterRemarks() {
		return rejectionLetterRemarks;
	}
	public void setRejectionLetterRemarks(String rejectionLetterRemarks) {
		this.rejectionLetterRemarks = rejectionLetterRemarks;
	}
	public Long getClaimKey() {
		return claimKey;
	}
	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}
	public Long getIntimationkey() {
		return intimationkey;
	}
	public void setIntimationkey(Long intimationkey) {
		this.intimationkey = intimationkey;
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
	public void setCpuCode(Long cpuCode) {
		this.cpuCode = ""+cpuCode;
	}
	public Long getHospitalNameID() {
		return hospitalNameID;
	}
	public void setHospitalNameID(Long hospitalNameID) {
		this.hospitalNameID = hospitalNameID;
	}
	public String getHospitalType() {
		return hospitalType;
	}
	public void setHospitalType(String hospitalType) {
		this.hospitalType = hospitalType;
	}
	public Long getCpuId() {
		return cpuId;
	}
	public void setCpuId(Long cpuId) {
		this.cpuId = cpuId;
	}
	/*public HumanTask getHumanTaskDTO() {
		return humanTaskDTO;
	}
	public void setHumanTaskDTO(HumanTask humanTaskDTO) {
		this.humanTaskDTO = humanTaskDTO;
	}
	*/
	public Long getRodKey() {
		return rodKey;
	}
	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}
	public String getStrCpuCode() {
		return strCpuCode;
	}
	public void setStrCpuCode(String strCpuCode) {
		this.strCpuCode = strCpuCode;
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
	
	/*public String getconcatRejectionRemarks() {
		String concatenatedString = "";
		if(reimbursementRejectionDto != null) {
			concatenatedString = concatenatedString +  ((reimbursementRejectionDto.getRejectionRemarks() != null ? procedureDTO.getProcedureStatus().getValue() + " -" : "") + (procedureDTO.getExclusionDetails() != null ? procedureDTO.getExclusionDetails().getValue() + " ;" : ""));
			this.rejectionRemarks = rejectionRemarks.getExclusionDetails() != null ? rejectionRemarks.getExclusionDetails().getValue() : null; 
		} 
		rejectionRemarks = concatenatedString;
		return rejectionRemarks;
	}*/
}

/**
 * 
 */
package com.shaic.reimbursement.queryrejection.processdraftquery.search;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.ReimbursementQueryDto;

/**
 * @author ntv.narenj
 *
 */
public class SearchProcessDraftQueryTableDTO extends AbstractTableDTO  implements Serializable{

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
	private Long queryKey;
	private Long rodKey;
		
	private String strCpuCode;
	
	private String queryType;
	
	private ReimbursementQueryDto reimbursementQueryDto;
	
	private Boolean isPolicyValidate;
	
	private String crmFlagged;

	private String crcFlaggedReason;
    
    private String crcFlaggedRemark;
    
    private String IsPreferredHospital;
    
    private Double InsuredAge;
    
    private PreauthDTO preAuthDto = new PreauthDTO();

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
	public ReimbursementQueryDto getReimbursementQueryDto() {
		return reimbursementQueryDto;
	}
	public void setReimbursementQueryDto(ReimbursementQueryDto reimbursementQueryDto) {
		this.reimbursementQueryDto = reimbursementQueryDto;
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
	
	public Long getQueryKey() {
		return queryKey;
	}
	public void setQueryKey(Long queryKey) {
		this.queryKey = queryKey;
	}
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
	public String getQueryType() {
		return queryType;
	}
	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}
	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
	}
	public Boolean getIsPolicyValidate() {
		return isPolicyValidate;
	}
	public void setIsPolicyValidate(Boolean isPolicyValidate) {
		this.isPolicyValidate = isPolicyValidate;
	}
	public String getCrmFlagged() {
		return crmFlagged;
	}
	public void setCrmFlagged(String crmFlagged) {
		this.crmFlagged = crmFlagged;
	}

	
}

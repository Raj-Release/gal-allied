/**
 * 
 */
package com.shaic.reimbursement.manageclaim.closeclaim.searchRodLevel;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.ClaimDto;
import com.shaic.newcode.wizard.dto.NewIntimationDto;

/**
 * @author ntv.narenj
 *
 */
public class SearchCloseClaimTableDTORODLevel extends AbstractTableDTO  implements Serializable{

	private String healthCardNo;
	private Long claimKey;
	private Long intimationkey;
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
	private String healthCardId;
	private String insuredPatientName;
	private Date dateOfAdmission;
	private String claimStatus;
	private Boolean isCloseClaimInProcess = false;
	
	private Long reimbursementKey;
	
	private NewIntimationDto intimationDto;
	
	private ClaimDto claimDto;
	
	public String getHealthCardNo() {
		return healthCardNo;
	}
	public void setHealthCardNo(String healthCardNo) {
		this.healthCardNo = healthCardNo;
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
	public String getHealthCardId() {
		return healthCardId;
	}
	public void setHealthCardId(String healthCardId) {
		this.healthCardId = healthCardId;
	}
	public String getInsuredPatientName() {
		return insuredPatientName;
	}
	public void setInsuredPatientName(String insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
	}
	public Date getDateOfAdmission() {
		return dateOfAdmission;
	}
	public void setDateOfAdmission(Date dateOfAdmission) {
		this.dateOfAdmission = dateOfAdmission;
	}
	public String getClaimStatus() {
		return claimStatus;
	}
	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}
	public NewIntimationDto getIntimationDto() {
		return intimationDto;
	}
	public void setIntimationDto(NewIntimationDto intimationDto) {
		this.intimationDto = intimationDto;
	}
	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
	}
	public Boolean getIsCloseClaimInProcess() {
		return isCloseClaimInProcess;
	}
	public void setIsCloseClaimInProcess(Boolean isCloseClaimInProcess) {
		this.isCloseClaimInProcess = isCloseClaimInProcess;
	}
	public Long getReimbursementKey() {
		return reimbursementKey;
	}
	public void setReimbursementKey(Long reimbursementKey) {
		this.reimbursementKey = reimbursementKey;
	}
	public ClaimDto getClaimDto() {
		return claimDto;
	}
	public void setClaimDto(ClaimDto claimDto) {
		this.claimDto = claimDto;
	}
	
	
}

/**
 * 
 */
package com.shaic.reimbursement.manageclaim.closeclaim.searchBasedClaimlevel;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.ClaimDto;
import com.shaic.newcode.wizard.dto.NewIntimationDto;

/**
 * 
 *
 */
public class SearchCloseClaimTableDTO extends AbstractTableDTO  implements Serializable{

	private String intimationNo;
	private String policyNo;
	private String hospitalName;
	private String hospitalCity;
	private String insuredPatientName;
	private Date dateOfAdmission;
	private Long claimKey;
	private String claimType;
	private Long intimationkey;
	private String cpuCode;
	private Long hospitalNameID;
	private String hospitalType;
	private Long cpuId;
	private String claimStatus;
	private int noofRods;
	
	private NewIntimationDto intimationDto;
	
	private ClaimDto claimDto;
	
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
	public String getHospitalCity() {
		return hospitalCity;
	}
	public void setHospitalCity(String hospitalCity) {
		this.hospitalCity = hospitalCity;
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
	public ClaimDto getClaimDto() {
		return claimDto;
	}
	public void setClaimDto(ClaimDto claimDto) {
		this.claimDto = claimDto;
	}
	public int getNoofRods() {
		return noofRods;
	}
	public void setNoofRods(int noofRods) {
		this.noofRods = noofRods;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	
}

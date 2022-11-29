/**
 * 
 */
package com.shaic.reimbursement.specialistprocessing.submitspecialist.search;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;

/**
 * @author ntv.narenj
 *
 */
public class SearchSubmitSpecialistAdviseTableDTO extends AbstractTableDTO  implements Serializable{

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
	private String specialityType;
	private Long rodKey;
	
	private RRCDTO rrcDTO;
	
	public String getSpecialityType() {
		return specialityType;
	}
	public void setSpecialityType(String specialityType) {
		this.specialityType = specialityType;
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
	public Long getRodKey() {
		return rodKey;
	}
	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}

	public RRCDTO getRrcDTO() {
		return rrcDTO;
	}
	public void setRrcDTO(RRCDTO rrcDTO) {
		this.rrcDTO = rrcDTO;
	}
	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
	}
	
	
}

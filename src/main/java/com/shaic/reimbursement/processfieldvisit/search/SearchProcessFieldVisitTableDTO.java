/**
 * 
 */
package com.shaic.reimbursement.processfieldvisit.search;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

/**
 * @author ntv.narenj
 *
 */
public class SearchProcessFieldVisitTableDTO extends AbstractTableDTO  implements Serializable{

	private Long key;
	private String intimationNo;
	private String claimNo;
	private String policyNo;
	private String insuredPatiendName;
	private String cpuCode;
	private String hospitalName;
	private String hospitalAddress;
	private String hospitalCity;
	private String reasonForAdmission;
	private String hospitalType;
	private Long cpuId;
	private Long hospitalNameID;
	
	
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
	
	public String getInsuredPatiendName() {
		return insuredPatiendName;
	}
	public void setInsuredPatiendName(String insuredPatiendName) {
		this.insuredPatiendName = insuredPatiendName;
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
	public Long getKey() {
		return key;
	}
	public void setKey(Long key) {
		this.key = key;
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
	
}

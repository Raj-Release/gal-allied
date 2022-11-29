/**
 * 
 */
package com.shaic.reimbursement.financialapprover.processclaimrequestBenefits.search;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

/**
 * @author ntv.narenj
 *
 */
public class SearchProcessClaimRequestBenefitsFinancTableDTO extends AbstractTableDTO  implements Serializable{

	private Long intimationKey;
	private Long policyKey;
	private String insuredPatientName;
	private Long claimKey;
	private Double requestedAmt;
	private Double balanceSI;
	private Long insuredKey;
	private Long cpuId;
	private Long hospitalNameID;
	private Double sumInsured;
	private String intimationNo;
	private String intimationSource;
	private String cpuName;
	private String productName;
	private String insuredPatiendName;
	private String hospitalName;
	private String hospitalType;
	private String claimType;
	private String treatementType;
	private String speciality;
	private String reasonForAdmission;
	private String originatorID;
	private Long rodKey;
	
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}	
	public String getInsuredPatiendName() {
		return insuredPatiendName;
	}
	public void setInsuredPatiendName(String insuredPatiendName) {
		this.insuredPatiendName = insuredPatiendName;
	}
	
	public String getIntimationSource() {
		return intimationSource;
	}
	public void setIntimationSource(String intimationSource) {
		this.intimationSource = intimationSource;
	}
	public String getCpuName() {
		return cpuName;
	}
	public void setCpuName(String cpuName) {
		this.cpuName = cpuName;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getHospitalType() {
		return hospitalType;
	}
	public void setHospitalType(String hospitalType) {
		this.hospitalType = hospitalType;
	}
	
	
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public String getTreatementType() {
		return treatementType;
	}
	public void setTreatementType(String treatementType) {
		this.treatementType = treatementType;
	}
	public String getSpeciality() {
		return speciality;
	}
	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}
	public String getReasonForAdmission() {
		return reasonForAdmission;
	}
	public void setReasonForAdmission(String reasonForAdmission) {
		this.reasonForAdmission = reasonForAdmission;
	}
	public String getOriginatorID() {
		return originatorID;
	}
	public void setOriginatorID(String originatorID) {
		this.originatorID = originatorID;
	}
	public Long getIntimationKey() {
		return intimationKey;
	}
	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
	}
	public Long getPolicyKey() {
		return policyKey;
	}
	public void setPolicyKey(Long policyKey) {
		this.policyKey = policyKey;
	}
	public String getInsuredPatientName() {
		return insuredPatientName;
	}
	public void setInsuredPatientName(String insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
	}
	public Long getClaimKey() {
		return claimKey;
	}
	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}
	public Double getRequestedAmt() {
		return requestedAmt;
	}
	public void setRequestedAmt(Double requestedAmt) {
		this.requestedAmt = requestedAmt;
	}
	public Double getBalanceSI() {
		return balanceSI;
	}
	public void setBalanceSI(Double balanceSI) {
		this.balanceSI = balanceSI;
	}
	public Long getInsuredKey() {
		return insuredKey;
	}
	public void setInsuredKey(Long insuredKey) {
		this.insuredKey = insuredKey;
	}
	public Long getCpuId() {
		return cpuId;
	}
	public void setCpuId(Long cpuId) {
		this.cpuId = cpuId;
	}
	public Long getHospitalNameID() {
		return hospitalNameID;
	}
	public void setHospitalNameID(Long hospitalNameID) {
		this.hospitalNameID = hospitalNameID;
	}
	public Double getSumInsured() {
		return sumInsured;
	}
	public void setSumInsured(Double sumInsured) {
		this.sumInsured = sumInsured;
	}
	public Long getRodKey() {
		return rodKey;
	}
	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}
	
	
	
	
}

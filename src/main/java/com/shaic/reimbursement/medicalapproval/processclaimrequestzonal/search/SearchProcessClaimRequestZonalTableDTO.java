/**
 * 
 */
package com.shaic.reimbursement.medicalapproval.processclaimrequestzonal.search;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

/**
 * @author ntv.narenj
 *
 */
public class SearchProcessClaimRequestZonalTableDTO extends AbstractTableDTO  implements Serializable{

	private Long intimationKey;
	private Long policyKey;
	private String policyNumber;
	private String intimationNo;
	private String intimationSource;
	private String cpuName;
	private String productName;
	private String insuredPatientName;
	private String hospitalName;
	private String hospitalType;
	private String claimType;
	private String documentReceivedFrom;
	private Long claimKey;
	private Double claimedAmt;
	private Double balanceSI;
	private String type;
	private Long insuredKey;
	private Long cpuId;
	private Long hospitalNameID;
	private Double sumInsured;
	private Long rodKey;
	private Boolean isFirstRODMedicalDone = true;
	private String rodAgeing;
	private String crmFlagged;
	
	
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}	
	
	
	public String getInsuredPatientName() {
		return insuredPatientName;
	}
	public void setInsuredPatientName(String insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
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
	
	public Double getClaimedAmt() {
		return claimedAmt;
	}
	public void setClaimedAmt(Double claimedAmt) {
		this.claimedAmt = claimedAmt;
	}
	public Double getBalanceSI() {
		return balanceSI;
	}
	public void setBalanceSI(Double balanceSI) {
		this.balanceSI = balanceSI;
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
	public Long getIntimationKey() {
		return intimationKey;
	}
	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
	}
	public Long getPolicyKey() {
		return policyKey;
	}
	public Boolean getIsFirstRODMedicalDone() {
		return isFirstRODMedicalDone;
	}
	public void setIsFirstRODMedicalDone(Boolean isFirstRODMedicalDone) {
		this.isFirstRODMedicalDone = isFirstRODMedicalDone;
	}
	public void setPolicyKey(Long policyKey) {
		this.policyKey = policyKey;
	}
	public Double getSumInsured() {
		return sumInsured;
	}
	public void setSumInsured(Double sumInsured) {
		this.sumInsured = sumInsured;
	}
	public Long getClaimKey() {
		return claimKey;
	}
	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}
	public Long getRodKey() {
		return rodKey;
	}
	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPolicyNumber() {
		return policyNumber;
	}
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
	public String getRodAgeing() {
		return rodAgeing;
	}
	public void setRodAgeing(String rodAgeing) {
		this.rodAgeing = rodAgeing;
	}
	public String getDocumentReceivedFrom() {
		return documentReceivedFrom;
	}
	public void setDocumentReceivedFrom(String documentReceivedFrom) {
		this.documentReceivedFrom = documentReceivedFrom;
	}
	public String getCrmFlagged() {
		return crmFlagged;
	}
	public void setCrmFlagged(String crmFlagged) {
		this.crmFlagged = crmFlagged;
	}
	
	
	
	
}

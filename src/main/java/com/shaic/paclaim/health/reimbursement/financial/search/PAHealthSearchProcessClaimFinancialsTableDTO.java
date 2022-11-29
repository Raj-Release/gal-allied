/**
 * 
 */
package com.shaic.paclaim.health.reimbursement.financial.search;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;


/**
 * @author ntv.narenj
 *
 */
public class PAHealthSearchProcessClaimFinancialsTableDTO extends AbstractTableDTO  implements Serializable{



	private String insuredPatientName;
	private String paPatientName;
	private Long productKey;
	private Long lOBId;
	private String lOB;
	private Long cpuCode;
	private String reasonForAdmission;
	private Long cpuId;
	private Long hospitalNameID;
	private String intimationNo;
	private String hospitalName;
	private String hospitalAddress;
	private String cpuName;
	private String type;
	private Long rodKey;	
	private Long claimKey;
	private String documentReceivedFrom;
	private String policyNo;
	private String originatorID;
	private String originatorName;
	private String productName;
	private String claimType;
	private String strCpuCode;
	private Double claimedAmountAsPerBill;
	
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public String getReasonForAdmission() {
		return reasonForAdmission;
	}
	public void setReasonForAdmission(String reasonForAdmission) {
		this.reasonForAdmission = reasonForAdmission;
	}
	public String getlOB() {
		return lOB;
	}
	public void setlOB(String lOB) {
		this.lOB = lOB;
	}
	public String getHospitalAddress() {
		return hospitalAddress;
	}
	public void setHospitalAddress(String hospitalAddress) {
		this.hospitalAddress = hospitalAddress;
	}
	public String getCpuName() {
		return cpuName;
	}
	public void setCpuName(String cpuName) {
		this.cpuName = cpuName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getInsuredPatientName() {
		return insuredPatientName;
	}
	public void setInsuredPatientName(String insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
	}
	public Long getlOBId() {
		return lOBId;
	}
	public void setlOBId(Long lOBId) {
		this.lOBId = lOBId;
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
	public Long getRodKey() {
		return rodKey;
	}
	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}
	
	public Long getClaimKey() {
		return claimKey;
	}
	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	
	public String getOriginatorID() {
		return originatorID;
	}
	public void setOriginatorID(String originatorID) {
		this.originatorID = originatorID;
	}
	public String getOriginatorName() {
		return originatorName;
	}
	public void setOriginatorName(String originatorName) {
		this.originatorName = originatorName;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public Long getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(Long cpuCode) {
		this.cpuCode = cpuCode;
	}
	public String getStrCpuCode() {
		return strCpuCode;
	}
	public void setStrCpuCode(String strCpuCode) {
		this.strCpuCode = strCpuCode;
	}
	public String getDocumentReceivedFrom() {
		return documentReceivedFrom;
	}
	public void setDocumentReceivedFrom(String documentReceivedFrom) {
		this.documentReceivedFrom = documentReceivedFrom;
	}
	public Double getClaimedAmountAsPerBill() {
		return claimedAmountAsPerBill;
	}
	public void setClaimedAmountAsPerBill(Double claimedAmountAsPerBill) {
		this.claimedAmountAsPerBill = claimedAmountAsPerBill;
	}
	public String getPaPatientName() {
		return paPatientName;
	}
	public void setPaPatientName(String paPatientName) {
		this.paPatientName = paPatientName;
	}
	public Long getProductKey() {
		return productKey;
	}
	public void setProductKey(Long productKey) {
		this.productKey = productKey;
	}
	
	
}

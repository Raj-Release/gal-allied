package com.shaic.claim.misc.updatesublimit;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class SearchUpdateSublimitTableDTO extends AbstractTableDTO implements Serializable{
	
	private String intimationNo;
	private Double claimedAmtAsPerBill;
	private Long cpuId;
	private Long cpuCode;
	private String strCpuCode;
	private String cpuName;
	private String claimType;
	private String documentRecvdFrm;
	private Long productKey;
	private String productName;
	private String insuredPatientName;
	private Long lobId;
	private String lob;
	private Long hospitalNameId;
	private String hospitalName;
	private String hospitalAddress;
	private String reasonForAdmission;
	private String type;
	private Long reimbursementKey;
	private Long claimKey;
	private String policyNo;
	private String hospitalizationFlag;
	private String sublimitUpdateRemarks;

	
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public Double getClaimedAmtAsPerBill() {
		return claimedAmtAsPerBill;
	}
	public void setClaimedAmtAsPerBill(Double claimedAmtAsPerBill) {
		this.claimedAmtAsPerBill = claimedAmtAsPerBill;
	}
	public Long getCpuId() {
		return cpuId;
	}
	public void setCpuId(Long cpuId) {
		this.cpuId = cpuId;
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
	public String getCpuName() {
		return cpuName;
	}
	public void setCpuName(String cpuName) {
		this.cpuName = cpuName;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public String getDocumentRecvdFrm() {
		return documentRecvdFrm;
	}
	public void setDocumentRecvdFrm(String documentRecvdFrm) {
		this.documentRecvdFrm = documentRecvdFrm;
	}
	public Long getProductKey() {
		return productKey;
	}
	public void setProductKey(Long productKey) {
		this.productKey = productKey;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getInsuredPatientName() {
		return insuredPatientName;
	}
	public void setInsuredPatientName(String insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
	}
	public Long getLobId() {
		return lobId;
	}
	public void setLobId(Long lobId) {
		this.lobId = lobId;
	}
	public String getLob() {
		return lob;
	}
	public void setLob(String lob) {
		this.lob = lob;
	}
	public Long getHospitalNameId() {
		return hospitalNameId;
	}
	public void setHospitalNameId(Long hospitalNameId) {
		this.hospitalNameId = hospitalNameId;
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
	public String getReasonForAdmission() {
		return reasonForAdmission;
	}
	public void setReasonForAdmission(String reasonForAdmission) {
		this.reasonForAdmission = reasonForAdmission;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Long getReimbursementKey() {
		return reimbursementKey;
	}
	public void setReimbursementKey(Long reimbursementKey) {
		this.reimbursementKey = reimbursementKey;
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
	public String getHospitalizationFlag() {
		return hospitalizationFlag;
	}
	public void setHospitalizationFlag(String hospitalizationFlag) {
		this.hospitalizationFlag = hospitalizationFlag;
	}
	public String getSublimitUpdateRemarks() {
		return sublimitUpdateRemarks;
	}
	public void setSublimitUpdateRemarks(String sublimitUpdateRemarks) {
		this.sublimitUpdateRemarks = sublimitUpdateRemarks;
	}
	
	

}

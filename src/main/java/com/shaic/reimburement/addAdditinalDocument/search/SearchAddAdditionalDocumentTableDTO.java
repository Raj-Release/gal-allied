package com.shaic.reimburement.addAdditinalDocument.search;

import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;




public class SearchAddAdditionalDocumentTableDTO extends AbstractTableDTO{

	private String intimationNo;
	
	private Long claimKey;
	
	private String claimNo;
	
	private String policyNo;
	
	private String insuredPatientName;
	
	private String paPatientName;
	private Long productKey;
	
	private String cpuCode;
	
	private String claimType;
	
	private String hospitalName;
	
	private String hospitalCity;
	
	private Date dateOfAdmission;
	
	private String reasonForAdmission;
	
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

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
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

	public Date getDateOfAdmission() {
		return dateOfAdmission;
	}

	public void setDateOfAdmission(Date dateOfAdmission) {
		this.dateOfAdmission = dateOfAdmission;
	}


	public String getReasonForAdmission() {
		return reasonForAdmission;
	}

	public void setReasonForAdmission(String reasonForAdmission) {
		this.reasonForAdmission = reasonForAdmission;
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

	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}

	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
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

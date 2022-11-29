package com.shaic.claim.search;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class SearchClaimTableDTO extends AbstractTableDTO implements Serializable {

	private static final long serialVersionUID = -8364518479442031487L;

	private Integer sno;

	private String intimationNo;

	private String claimNo;

	private String policyNo;

	private String insuredPatientName;

	private String cpuCode;

	private String hospitalName;

	private String hospitalCity;
	
	private String reasonforAdmission;

	private String claimStatus;
	
	
	private String viewClaimStatus;
	
	
	private Long hospitalTypeId;
	
	private Long claimTypeId;
	
	

	public Integer getSno() {
		return sno;
	}

	public void setSno(Integer sno) {
		this.sno = sno;
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

	public String getInsuredPatientName() {
		return insuredPatientName;
	}

	public void setInsuredPatientName(String insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
	}

	public String getCpuCode() {
		return cpuCode;
	}

	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
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

	public String getReasonforAdmission() {
		return reasonforAdmission;
	}

	public void setReasonforAdmission(String reasonforAdmission) {
		this.reasonforAdmission = reasonforAdmission;
	}

	public String getClaimStatus() {
		return claimStatus;
	}

	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}

	public Long getHospitalTypeId() {
		return hospitalTypeId;
	}

	public void setHospitalTypeId(Long hospitalTypeId) {
		this.hospitalTypeId = hospitalTypeId;
	}

	public String getViewClaimStatus() {
		return viewClaimStatus;
	}

	public void setViewClaimStatus(String viewClaimStatus) {
		this.viewClaimStatus = viewClaimStatus;
	}

	public Long getClaimTypeId() {
		return claimTypeId;
	}

	public void setClaimTypeId(Long claimTypeId) {
		this.claimTypeId = claimTypeId;
	}

	
	
	
}

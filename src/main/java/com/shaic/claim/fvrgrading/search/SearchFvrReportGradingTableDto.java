package com.shaic.claim.fvrgrading.search;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;

public class SearchFvrReportGradingTableDto extends AbstractTableDTO implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer srlNo;

	private String lob;

	private String claimType;

	private String cpuCode;

	private String claimNumber;

	private String insuredPatientName;

	private String hospitalName;

	private Long hospitalId;
	
	private Long acknowledgementKey;
	
	
	/**
	 * Following attributes added as part of PA Claim Conversion
	 * 
	 */
	private String intimationNo;
	
	private String policyNo;
	
	private Long rodKey;
	
	private String admissionReason;
	
	private String product;
	
	private String hospCity;
	
	private Long claimKey;
	
	private Long fvrKey;
	
	public Integer getSrlNo() {
		return srlNo;
	}

	public void setSrlNo(Integer srlNo) {
		this.srlNo = srlNo;
	}

	public String getLob() {
		return lob;
	}

	public void setLob(String lob) {
		this.lob = lob;
	}

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	public String getCpuCode() {
		return cpuCode;
	}

	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
	}

	public String getClaimNumber() {
		return claimNumber;
	}

	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}

	public String getInsuredPatientName() {
		return insuredPatientName;
	}

	public void setInsuredPatientName(String insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public Long getAcknowledgementKey() {
		return acknowledgementKey;
	}

	public void setAcknowledgementKey(Long acknowledgementKey) {
		this.acknowledgementKey = acknowledgementKey;
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

	public Long getRodKey() {
		return rodKey;
	}

	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}

	public String getAdmissionReason() {
		return admissionReason;
	}

	public void setAdmissionReason(String admissionReason) {
		this.admissionReason = admissionReason;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getHospCity() {
		return hospCity;
	}

	public void setHospCity(String hospCity) {
		this.hospCity = hospCity;
	}

	public Long getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Long hospitalId) {
		this.hospitalId = hospitalId;
	}

	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}

	public Long getFvrKey() {
		return fvrKey;
	}

	public void setFvrKey(Long fvrKey) {
		this.fvrKey = fvrKey;
	}
	
	
	
}

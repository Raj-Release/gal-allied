package com.shaic.claim.registration.convertclaimcashless;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;



public class SearchConverClaimCashlessTableDTO extends AbstractTableDTO implements Serializable {

	private static final long serialVersionUID = -6229444093245467560L;
	

	private String policyNo;
	
	private String intimationNumber;
	
	private String claimType;
	
	private String cpuCode;
	
	private String insuredPatientName;
	
	private String hospitalName;
	
	private String hospitalType;
	
	private Date intimationDate;
	
	private String strIntimationDate;
	
	private String claimStatus;
	
	private Long hospitalTypeKey;
	
	private Boolean  isackavailable = false;
	
	private Long hospitalNameIds;
	
	
	
	
	

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}


	public String getIntimationNumber() {
		return intimationNumber;
	}


	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
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


	public Date getIntimationDate() {
		return intimationDate;
	}


	public void setIntimationDate(Date intimationDate) {
		this.intimationDate = intimationDate;
	}


	public String getClaimStatus() {
		return claimStatus;
	}


	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}

	public String getHospitalType() {
		return hospitalType;
	}

	public void setHospitalType(String hospitalType) {
		this.hospitalType = hospitalType;
	}

	public Long getHospitalTypeKey() {
		return hospitalTypeKey;
	}

	public void setHospitalTypeKey(Long hospitalTypeKey) {
		this.hospitalTypeKey = hospitalTypeKey;
	}

	public Boolean getIsackavailable() {
		return isackavailable;
	}

	public void setIsackavailable(Boolean isackavailable) {
		this.isackavailable = isackavailable;
	}

	public Long getHospitalNameIds() {
		return hospitalNameIds;
	}

	public void setHospitalNameIds(Long hospitalNameIds) {
		this.hospitalNameIds = hospitalNameIds;
	}

	public String getStrIntimationDate() {
		return strIntimationDate;
	}

	public void setStrIntimationDate(String strIntimationDate) {
		this.strIntimationDate = strIntimationDate;
	}

	
	
}

package com.shaic.claim.medical.opinion;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class SearchRecordMarkEscTableDTO extends AbstractTableDTO implements Serializable{

	private static final long serialVersionUID = -6229444093245467560L;
	
	private Integer sno;

	private String intimationNo;

	private String policyNo;

	private String insuredPatientName;

	private String cpuCode;

	private String hospitalName;

	private String hospitalCity;
	
	private String lob;

	private String productName;

	private String reasonForAdmission;
		
	private String crmFlagged;
	
	private Long lobId;
	
	private Long hospitalKey;

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

	public String getLob() {
		return lob;
	}

	public void setLob(String lob) {
		this.lob = lob;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getReasonForAdmission() {
		return reasonForAdmission;
	}

	public void setReasonForAdmission(String reasonForAdmission) {
		this.reasonForAdmission = reasonForAdmission;
	}

	public String getCrmFlagged() {
		return crmFlagged;
	}

	public void setCrmFlagged(String crmFlagged) {
		this.crmFlagged = crmFlagged;
	}

	public Long getLobId() {
		return lobId;
	}

	public void setLobId(Long lobId) {
		this.lobId = lobId;
	}

	public Long getHospitalKey() {
		return hospitalKey;
	}

	public void setHospitalKey(Long hospitalKey) {
		this.hospitalKey = hospitalKey;
	}
	
    }

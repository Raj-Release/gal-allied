package com.shaic.claim.intimatino.view;

import java.io.Serializable;

public class ViewIntimationDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long Key;
	private String intimationNo;	
	private String policyNumber;
	private String dateOfIntimation;
	private String policyIssuingOffice;
	private String productName;
	private String insuredName;
	private String patientName;
	private String hospitalName;
	private String cityOfHospital;
	private String hospitalType;
	private String dateOfAdmission;
	private String fieldVisitDoctorName;
	private String reasonForAdmission;
	private String cpuCode;
	private String smCode;
	private String smName;
	private String agentBrokerCode;
	private String agentBrokerName;
	private String hospitalCode;
	private String idCardNo;

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getDateOfIntimation() {
		return dateOfIntimation;
	}

	public void setDateOfIntimation(String dateOfIntimation) {
		this.dateOfIntimation = dateOfIntimation;
	}

	public String getPolicyIssuingOffice() {
		return policyIssuingOffice;
	}

	public void setPolicyIssuingOffice(String policyIssuingOffice) {
		this.policyIssuingOffice = policyIssuingOffice;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getCityOfHospital() {
		return cityOfHospital;
	}

	public void setCityOfHospital(String cityOfHospital) {
		this.cityOfHospital = cityOfHospital;
	}

	public String getHospitalType() {
		return hospitalType;
	}

	public void setHospitalType(String hospitalType) {
		this.hospitalType = hospitalType;
	}

	public String getDateOfAdmission() {
		return dateOfAdmission;
	}

	public void setDateOfAdmission(String dateOfAdmission) {
		this.dateOfAdmission = dateOfAdmission;
	}

	public String getFieldVisitDoctorName() {
		return fieldVisitDoctorName;
	}

	public void setFieldVisitDoctorName(String fieldVisitDoctorName) {
		this.fieldVisitDoctorName = fieldVisitDoctorName;
	}

	public String getReasonForAdmission() {
		return reasonForAdmission;
	}

	public void setReasonForAdmission(String reasonForAdmission) {
		this.reasonForAdmission = reasonForAdmission;
	}

	public String getCpuCode() {
		return cpuCode;
	}

	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
	}

	public String getSmCode() {
		return smCode;
	}

	public void setSmCode(String smCode) {
		this.smCode = smCode;
	}

	public String getSmName() {
		return smName;
	}

	public void setSmName(String smName) {
		this.smName = smName;
	}

	public String getAgentBrokerCode() {
		return agentBrokerCode;
	}

	public void setAgentBrokerCode(String agentBrokerCode) {
		this.agentBrokerCode = agentBrokerCode;
	}

	public String getAgentBrokerName() {
		return agentBrokerName;
	}

	public void setAgentBrokerName(String agentBrokerName) {
		this.agentBrokerName = agentBrokerName;
	}

	public String getHospitalCode() {
		return hospitalCode;
	}

	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public Long getKey() {
		return Key;
	}

	public void setKey(Long key) {
		Key = key;
	}

}

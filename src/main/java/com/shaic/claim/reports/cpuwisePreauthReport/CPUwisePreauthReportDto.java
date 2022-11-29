package com.shaic.claim.reports.cpuwisePreauthReport;

import java.text.SimpleDateFormat;

import com.shaic.claim.preauth.wizard.dto.PreauthDTO;

public class CPUwisePreauthReportDto {
	
	private int sno;
	private String intimationNo;
	private String policyNumber;
	private String productName;
	private String preauthStatus;
	private String preauthReqAmount;
	private String preauthAmount;
	private String preauthDate;
	private String diagnosis;
	private String patientName;
	private String doctor;
	private String remarks;
	private String patientAge;
	private String hospitalName;
	private String anhHospital;
	private String treatmentType;
	
	public CPUwisePreauthReportDto(){
		
	}
	public CPUwisePreauthReportDto(PreauthDTO preauthDto){
		this.intimationNo = preauthDto.getNewIntimationDTO().getIntimationId();
		this.policyNumber = preauthDto.getNewIntimationDTO().getPolicy().getPolicyNumber();
		this.productName = preauthDto.getNewIntimationDTO().getPolicy().getProduct().getValue();
		this.preauthStatus = preauthDto.getStatusValue();
		this.preauthReqAmount = preauthDto.getAmountConsidered() != null ? preauthDto.getAmountConsidered() : "";
		this.preauthAmount = preauthDto.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null ? preauthDto.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt().toString() : "";
		this.preauthDate = preauthDto.getCreateDate() != null ? new SimpleDateFormat("dd/MM/yyyy").format(preauthDto.getCreateDate()): "";
		this.patientName = preauthDto.getNewIntimationDTO().getInsuredPatient().getInsuredName();
		this.diagnosis = preauthDto.getPreauthDataExtractionDetails().getDiagnosis();
		this.doctor = preauthDto.getModifiedBy() != null ? preauthDto.getModifiedBy() : "";
		this.remarks = preauthDto.getPreauthMedicalDecisionDetails().getApprovalRemarks();
	}
	
	public int getSno() {
		return sno;
	}
	public void setSno(int sno) {
		this.sno = sno;
	}
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
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getPreauthStatus() {
		return preauthStatus;
	}
	public void setPreauthStatus(String preauthStatus) {
		this.preauthStatus = preauthStatus;
	}
	public String getPreauthReqAmount() {
		return preauthReqAmount;
	}
	public void setPreauthReqAmount(String preauthReqAmount) {
		this.preauthReqAmount = preauthReqAmount;
	}
	public String getPreauthAmount() {
		return preauthAmount;
	}
	public void setPreauthAmount(String preauthAmount) {
		this.preauthAmount = preauthAmount;
	}
	public String getPreauthDate() {
		return preauthDate;
	}
	public void setPreauthDate(String preauthDate) {
		this.preauthDate = preauthDate;
	}
	public String getDiagnosis() {
		return diagnosis;
	}
	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getDoctor() {
		return doctor;
	}
	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getPatientAge() {
		return patientAge;
	}
	public void setPatientAge(String patientAge) {
		this.patientAge = patientAge;
	}
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public String getAnhHospital() {
		return anhHospital;
	}
	public void setAnhHospital(String anhHospital) {
		this.anhHospital = anhHospital;
	}
	public String getTreatmentType() {
		return treatmentType;
	}
	public void setTreatmentType(String treatmentType) {
		this.treatmentType = treatmentType;
	}	
	
	}

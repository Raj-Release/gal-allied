package com.shaic.claim.reports.productivityreport;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class ProductivityReportTableDTO  extends AbstractTableDTO  implements Serializable{
	
	private String intimationNo;
	private String intimationDate;
	private String claimNumber;
	private String policyNumber;
	private String cashlessReferenceNo;
	private String claimType;
	private String dateOfInception;
	private String breakInPolicy;
	private Integer noOfPreviousClaims;
	private String portableIfAny;
	private String endorsementsIfAny;
	private Double currentSumInsured;
	private String changeInSi;
	private String productCode;
	private String productType;
	private String insuredName;
	private String patientName;
	private Long patientAge;
	private String doa;
	private String dod;
	private String diagnosis;
	private Double roomRent;
	private Double icu;
	private Long cpuCode;
	private String cpuName;
	private String registrationQueueTime;
	private String matchedQueueDateTime;
	private String responseDateAndTime;
	private String documentType;
	private String responseDocType;
	private String processingDoctorId;
	private String processingDoctorName;
	private String processingDoctorsRemarks;
	private String userId;
	private String userName;
	private String remarks;
	private Double requestAmount;
	private String requestStatus;
	private String requestProcessedDate;
	private Double approvedAmount;
	private String hospitalName;
	private String city;
	private String anh;
	private String declaredPedIfAny;
	private Double copay;
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	
	public String getClaimNumber() {
		return claimNumber;
	}
	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}
	public String getPolicyNumber() {
		return policyNumber;
	}
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
	public String getCashlessReferenceNo() {
		return cashlessReferenceNo;
	}
	public void setCashlessReferenceNo(String cashlessReferenceNo) {
		this.cashlessReferenceNo = cashlessReferenceNo;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public String getBreakInPolicy() {
		return breakInPolicy;
	}
	public void setBreakInPolicy(String breakInPolicy) {
		this.breakInPolicy = breakInPolicy;
	}
	public Integer getNoOfPreviousClaims() {
		return noOfPreviousClaims;
	}
	public void setNoOfPreviousClaims(Integer noOfPreviousClaims) {
		this.noOfPreviousClaims = noOfPreviousClaims;
	}
	public String getPortableIfAny() {
		return portableIfAny;
	}
	public void setPortableIfAny(String portableIfAny) {
		this.portableIfAny = portableIfAny;
	}
	public String getEndorsementsIfAny() {
		return endorsementsIfAny;
	}
	public void setEndorsementsIfAny(String endorsementsIfAny) {
		this.endorsementsIfAny = endorsementsIfAny;
	}
	public Double getCurrentSumInsured() {
		return currentSumInsured;
	}
	public void setCurrentSumInsured(Double currentSumInsured) {
		this.currentSumInsured = currentSumInsured;
	}
	public String getChangeInSi() {
		return changeInSi;
	}
	public void setChangeInSi(String changeInSi) {
		this.changeInSi = changeInSi;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
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
	public Long getPatientAge() {
		return patientAge;
	}
	public void setPatientAge(Long patientAge) {
		this.patientAge = patientAge;
	}
	public String getDiagnosis() {
		return diagnosis;
	}
	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}
	public Double getRoomRent() {
		return roomRent;
	}
	public void setRoomRent(Double roomRent) {
		this.roomRent = roomRent;
	}
	public Double getIcu() {
		return icu;
	}
	public void setIcu(Double icu) {
		this.icu = icu;
	}
	public Long getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(Long cpuCode) {
		this.cpuCode = cpuCode;
	}
	public String getCpuName() {
		return cpuName;
	}
	public void setCpuName(String cpuName) {
		this.cpuName = cpuName;
	}
	public String getDocumentType() {
		return documentType;
	}
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	public String getResponseDocType() {
		return responseDocType;
	}
	public void setResponseDocType(String responseDocType) {
		this.responseDocType = responseDocType;
	}
	public String getProcessingDoctorId() {
		return processingDoctorId;
	}
	public void setProcessingDoctorId(String processingDoctorId) {
		this.processingDoctorId = processingDoctorId;
	}
	public String getProcessingDoctorName() {
		return processingDoctorName;
	}
	public void setProcessingDoctorName(String processingDoctorName) {
		this.processingDoctorName = processingDoctorName;
	}
	public String getProcessingDoctorsRemarks() {
		return processingDoctorsRemarks;
	}
	public void setProcessingDoctorsRemarks(String processingDoctorsRemarks) {
		this.processingDoctorsRemarks = processingDoctorsRemarks;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Double getRequestAmount() {
		return requestAmount;
	}
	public void setRequestAmount(Double requestAmount) {
		this.requestAmount = requestAmount;
	}
	public String getRequestStatus() {
		return requestStatus;
	}
	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}
	public Double getApprovedAmount() {
		return approvedAmount;
	}
	public void setApprovedAmount(Double approvedAmount) {
		this.approvedAmount = approvedAmount;
	}
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAnh() {
		return anh;
	}
	public void setAnh(String anh) {
		this.anh = anh;
	}
	public String getDeclaredPedIfAny() {
		return declaredPedIfAny;
	}
	public void setDeclaredPedIfAny(String declaredPedIfAny) {
		this.declaredPedIfAny = declaredPedIfAny;
	}
	public Double getCopay() {
		return copay;
	}
	public void setCopay(Double copay) {
		this.copay = copay;
	}
	public String getIntimationDate() {
		return intimationDate;
	}
	public void setIntimationDate(String intimationDate) {
		this.intimationDate = intimationDate;
	}
	public String getDateOfInception() {
		return dateOfInception;
	}
	public void setDateOfInception(String dateOfInception) {
		this.dateOfInception = dateOfInception;
	}
	public String getDoa() {
		return doa;
	}
	public void setDoa(String doa) {
		this.doa = doa;
	}
	public String getDod() {
		return dod;
	}
	public void setDod(String dod) {
		this.dod = dod;
	}
	public String getRegistrationQueueTime() {
		return registrationQueueTime;
	}
	public void setRegistrationQueueTime(String registrationQueueTime) {
		this.registrationQueueTime = registrationQueueTime;
	}
	public String getMatchedQueueDateTime() {
		return matchedQueueDateTime;
	}
	public void setMatchedQueueDateTime(String matchedQueueDateTime) {
		this.matchedQueueDateTime = matchedQueueDateTime;
	}
	public String getResponseDateAndTime() {
		return responseDateAndTime;
	}
	public void setResponseDateAndTime(String responseDateAndTime) {
		this.responseDateAndTime = responseDateAndTime;
	}
	public String getRequestProcessedDate() {
		return requestProcessedDate;
	}
	public void setRequestProcessedDate(String requestProcessedDate) {
		this.requestProcessedDate = requestProcessedDate;
	}
	

}

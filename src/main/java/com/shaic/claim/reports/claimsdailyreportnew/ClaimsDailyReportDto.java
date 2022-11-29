package com.shaic.claim.reports.claimsdailyreportnew;

import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;

import com.shaic.claim.ClaimDto;
import com.shaic.domain.Claim;

public class ClaimsDailyReportDto {
	
	private int sno;
	private String intimationNo;
	private String intimationDate;
	private String policyNumber;
	private String productName; 
	private String insuredProposerName;
	private String mainMemberName;
	private String CPUcode;
	private String patientName;
	private Integer patientAge;
	private String admissionReason;
	private String callerContactNo;
	private String hospitalCode;	
	private String hospitalType;	
	private String hospitalName;
	private String hospitalCity;
	private String hospitalState;
	private String hospitalDate;
	private String ANHStatus;	
	private String fieldDoctorNameAllocated;
	private String contactNumOfDoctor;
	private String dataOfAllocationWithTime;
	private String registrationStatus;
	private String cashlessOrReimbursement;
	private String plannedAdmission;
	private String suminsured;
	private String provisionAmt;
	private String initialProvisionAmt;
	public ClaimsDailyReportDto(){
		
	}
	
	public ClaimsDailyReportDto(Claim claimObj){
		
		this.policyNumber = claimObj.getIntimation().getPolicy().getPolicyNumber();
		this.mainMemberName = claimObj.getIntimation().getPolicy().getProposerFirstName();
		this.insuredProposerName = claimObj.getIntimation().getPolicy().getProposerFirstName();
		this.productName = claimObj.getIntimation().getPolicy().getProduct().getValue() != null ? claimObj.getIntimation().getPolicy().getProduct().getValue() : "";
		this.intimationDate = claimObj.getIntimation().getCreatedDate() != null ? (new SimpleDateFormat("dd-MM-yyyy")).format(claimObj.getIntimation().getCreatedDate()) : "";
		this.hospitalDate = claimObj.getIntimation().getAdmissionDate() != null ? (new SimpleDateFormat("dd-MM-yyyy")).format(claimObj.getIntimation().getAdmissionDate()) : "";
		this.intimationNo = claimObj.getIntimation().getIntimationId();
		this.mainMemberName= claimObj.getIntimation().getPolicy().getProposerFirstName();
		this.patientName = claimObj.getIntimation().getInsured().getInsuredName();
		this.patientAge = claimObj.getIntimation().getInsured().getInsuredAge() != null ? claimObj.getIntimation().getInsured().getInsuredAge().intValue() : 0;
//		this.hospitalType = (claimDto.getNewIntimationDto().getHospitalDto().getRegistedHospitals()
//				.getHospitalType() != null ? (claimDto.getNewIntimationDto().getHospitalDto().getRegistedHospitals()
//						.getHospitalType().getValue().equalsIgnoreCase("Network") ? "NETWORK" : "NON-NETWORK"):"");
//		this.hospitalName = claimDto.getNewIntimationDto().getHospitalDto().getRegistedHospitals().getName();	
//		this.hospitalCode = claimDto.getNewIntimationDto().getHospitalDto().getRegistedHospitals().getHospitalCode() != null ? claimDto.getNewIntimationDto().getHospitalDto().getRegistedHospitals().getHospitalCode() : ""; 
//		this.hospitalCity = claimDto.getNewIntimationDto().getHospitalDto().getRegistedHospitals().getCity();
//		this.hospitalState = claimDto.getNewIntimationDto().getHospitalDto().getRegistedHospitals().getState();
//		this.ANHStatus = claimDto.getNewIntimationDto().getHospitalDto().getRegistedHospitals().getNetworkHospitalType() != null ? "ANH" : "Non ANH";
		this.CPUcode = claimObj.getIntimation().getCpuCode() != null ? (claimObj.getIntimation().getCpuCode().getCpuCode() != null ? claimObj.getIntimation().getCpuCode().getCpuCode().toString() : ""): "";
		this.admissionReason = claimObj.getIntimation().getAdmissionReason() != null ? claimObj.getIntimation().getAdmissionReason() : "";
		this.callerContactNo = claimObj.getIntimation().getCallerLandlineNumber() != null ? claimObj.getIntimation().getCallerLandlineNumber() : "";
		this.cashlessOrReimbursement= claimObj.getClaimType() != null ?  (claimObj.getClaimType().getValue()  != null ? claimObj.getClaimType().getValue() : "") : "";
		this.registrationStatus = claimObj.getIntimation().getRegistrationStatus() != null ? claimObj.getIntimation().getRegistrationStatus() : "Not Registered";
		this.plannedAdmission = claimObj.getIntimation() != null && claimObj.getIntimation().getAdmissionType() != null && StringUtils.containsIgnoreCase(claimObj.getIntimation().getAdmissionType().getValue(), "plan") ? "Yes" : "No";
		this.initialProvisionAmt = claimObj.getProvisionAmount() != null ? String.valueOf(claimObj.getProvisionAmount().intValue()) : ""; 
		this.provisionAmt = claimObj.getCurrentProvisionAmount() != null ? String.valueOf(claimObj.getCurrentProvisionAmount().intValue()) : "";
		
	}
	
	
	public ClaimsDailyReportDto(ClaimDto claimDto){
		
		this.policyNumber = claimDto.getNewIntimationDto().getPolicy().getPolicyNumber();
		this.mainMemberName = claimDto.getNewIntimationDto().getPolicy().getProposerFirstName();
		this.insuredProposerName = claimDto.getNewIntimationDto().getPolicy().getProposerFirstName();
		this.productName = claimDto.getNewIntimationDto().getPolicy().getProduct().getValue() != null ? claimDto.getNewIntimationDto().getPolicy().getProduct().getValue() : "";
		this.intimationDate = claimDto.getNewIntimationDto().getCreatedDate() != null ? (new SimpleDateFormat("dd-MM-yyyy")).format(claimDto.getNewIntimationDto().getCreatedDate()) : "";
		this.hospitalDate = claimDto.getNewIntimationDto().getAdmissionDate() != null ? (new SimpleDateFormat("dd-MM-yyyy")).format(claimDto.getNewIntimationDto().getAdmissionDate()) : "";
		this.intimationNo = claimDto.getNewIntimationDto().getIntimationId();
		this.mainMemberName= claimDto.getNewIntimationDto().getPolicy().getProposerFirstName();
		this.patientName = claimDto.getNewIntimationDto().getInsuredPatient().getInsuredName();
		this.patientAge = claimDto.getNewIntimationDto().getInsuredPatient().getInsuredAge() != null ? claimDto.getNewIntimationDto().getInsuredPatient().getInsuredAge().intValue() : 0;
		this.hospitalType = (claimDto.getNewIntimationDto().getHospitalDto().getRegistedHospitals()
				.getHospitalType() != null ? (claimDto.getNewIntimationDto().getHospitalDto().getRegistedHospitals()
						.getHospitalType().getValue().equalsIgnoreCase("Network") ? "NETWORK" : "NON-NETWORK"):"");
		this.hospitalName = claimDto.getNewIntimationDto().getHospitalDto().getRegistedHospitals().getName();	
		this.hospitalCode = claimDto.getNewIntimationDto().getHospitalDto().getRegistedHospitals().getHospitalCode() != null ? claimDto.getNewIntimationDto().getHospitalDto().getRegistedHospitals().getHospitalCode() : ""; 
		this.hospitalCity = claimDto.getNewIntimationDto().getHospitalDto().getRegistedHospitals().getCity();
		this.hospitalState = claimDto.getNewIntimationDto().getHospitalDto().getRegistedHospitals().getState();
		this.ANHStatus = claimDto.getNewIntimationDto().getHospitalDto().getRegistedHospitals().getNetworkHospitalType() != null ? "ANH" : "Non ANH";
		this.CPUcode = claimDto.getNewIntimationDto().getCpuCode() != null ? claimDto.getNewIntimationDto().getCpuCode() : "";
		this.admissionReason = claimDto.getNewIntimationDto().getReasonForAdmission() != null ? claimDto.getNewIntimationDto().getReasonForAdmission() : "";
		this.callerContactNo = claimDto.getNewIntimationDto().getCallerContactNum() != null ? claimDto.getNewIntimationDto().getCallerContactNum() : "";
		this.cashlessOrReimbursement= claimDto.getClaimType() != null ?  claimDto.getClaimType().getValue() : "";
		this.registrationStatus = claimDto.getNewIntimationDto().getRegistrationStatus() != null ? claimDto.getNewIntimationDto().getRegistrationStatus() : "Not Registered";
		this.plannedAdmission = claimDto.getNewIntimationDto() != null && claimDto.getNewIntimationDto().getAdmissionType() != null && StringUtils.containsIgnoreCase(claimDto.getNewIntimationDto().getAdmissionType().getValue(), "plan") ? "Yes" : "No";
		this.initialProvisionAmt = claimDto.getProvisionAmount() != null ? String.valueOf(claimDto.getProvisionAmount().intValue()) : ""; 
		this.provisionAmt = claimDto.getCurrentProvisionAmount() != null ? String.valueOf(claimDto.getCurrentProvisionAmount().intValue()) : "";
		
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

	public String getIntimationDate() {
		return intimationDate;
	}

	public void setIntimationDate(String intimationDate) {
		this.intimationDate = intimationDate;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getInsuredProposerName() {
		return insuredProposerName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public void setInsuredProposerName(String insuredProposerName) {
		this.insuredProposerName = insuredProposerName;
	}

	public String getMainMemberName() {
		return mainMemberName;
	}

	public void setMainMemberName(String mainMemberName) {
		this.mainMemberName = mainMemberName;
	}

	public String getCPUcode() {
		return CPUcode;
	}

	public void setCPUcode(String cPUcode) {
		CPUcode = cPUcode;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public Integer getPatientAge() {
		return patientAge;
	}

	public void setPatientAge(Integer patientAge) {
		this.patientAge = patientAge;
	}

	public String getAdmissionReason() {
		return admissionReason;
	}

	public void setAdmissionReason(String admissionReason) {
		this.admissionReason = admissionReason;
	}

	public String getCallerContactNo() {
		return callerContactNo;
	}

	public void setCallerContactNo(String callerContactNo) {
		this.callerContactNo = callerContactNo;
	}

	public String getHospitalCode() {
		return hospitalCode;
	}

	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	public String getHospitalType() {
		return hospitalType;
	}

	public void setHospitalType(String hospitalType) {
		this.hospitalType = hospitalType;
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

	public String getHospitalState() {
		return hospitalState;
	}

	public void setHospitalState(String hospitalState) {
		this.hospitalState = hospitalState;
	}

	public String getANHStatus() {
		return ANHStatus;
	}

	public void setANHStatus(String aNHStatus) {
		ANHStatus = aNHStatus;
	}

	public String getFieldDoctorNameAllocated() {
		return fieldDoctorNameAllocated;
	}

	public void setFieldDoctorNameAllocated(String fieldDoctorNameAllocated) {
		this.fieldDoctorNameAllocated = fieldDoctorNameAllocated;
	}

	public String getContactNumOfDoctor() {
		return contactNumOfDoctor;
	}

	public void setContactNumOfDoctor(String contactNumOfDoctor) {
		this.contactNumOfDoctor = contactNumOfDoctor;
	}

	public String getDataOfAllocationWithTime() {
		return dataOfAllocationWithTime;
	}

	public void setDataOfAllocationWithTime(String dataOfAllocationWithTime) {
		this.dataOfAllocationWithTime = dataOfAllocationWithTime;
	}

	public String getRegistrationStatus() {
		return registrationStatus;
	}

	public void setRegistrationStatus(String registrationStatus) {
		this.registrationStatus = registrationStatus;
	}

	public String getCashlessOrReimbursement() {
		return cashlessOrReimbursement;
	}

	public void setCashlessOrReimbursement(String cashlessOrReimbursement) {
		this.cashlessOrReimbursement = cashlessOrReimbursement;
	}

	public String getPlannedAdmission() {
		return plannedAdmission;
	}

	public void setPlannedAdmission(String plannedAdmission) {
		this.plannedAdmission = plannedAdmission;
	}

	public String getSuminsured() {
		return suminsured;
	}

	public void setSuminsured(String suminsured) {
		this.suminsured = suminsured;
	}

	public String getProvisionAmt() {
		return provisionAmt;
	}

	public void setProvisionAmt(String provisionAmt) {
		this.provisionAmt = provisionAmt;
	}

	public String getInitialProvisionAmt() {
		return initialProvisionAmt;
	}

	public void setInitialProvisionAmt(String initialProvisionAmt) {
		this.initialProvisionAmt = initialProvisionAmt;
	}

	public String getHospitalDate() {
		return hospitalDate;
	}

	public void setHospitalDate(String hospitalDate) {
		this.hospitalDate = hospitalDate;
	}	

}

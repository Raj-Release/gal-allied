package com.shaic.claim.reports.dailyreport;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

public class DailyReportTableDTO  extends AbstractTableDTO  implements Serializable{
	
	private Long hospitalNameId;
	private Long key;
	private String intimationNo;
	private Date intimationDate;
	private String intimationDateValue;
	private String policyNumber;
	private String productType;
	private String insuredName;
	private String mainMemberName;
	private String callerContactNo;
	private String hospitalCode;
	private String patientName;
	private Double patientAge;
	private String admissionReason;
	private String hospitalType;
	private Date hospitalDate;
	private String hospitalDateValue;
	private String hospitalName;
	private String cpuCode;
	private String hospitalState;
	private String hospitalCity;
	private String fieldDoctorNameAllocated;
	private String contactNoOftheDoctor;
	private Date dateAndTimeOfAllocation;
	private String dateAndTimeOfAllocationValue;
	private String registrationStatus;
	private String cr;	
	private String totalAuthAmount;
	private Date authDate;
	private String authDateValue;
	private String cashlessStatus;
	private String cashlessReason;
	
	
	
	public Long getKey() {
		return key;
	}
	public void setKey(Long key) {
		this.key = key;
	}
	
	public Long getHospitalNameId() {
		return hospitalNameId;
	}
	public void setHospitalNameId(Long hospitalNameId) {
		this.hospitalNameId = hospitalNameId;
	}
	
	public String getAuthDateValue() {
		return authDateValue;
	}
	public void setAuthDateValue(String authDateValue) {
		this.authDateValue = authDateValue;
	}
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public Date getIntimationDate() {
		return intimationDate;
	}
	public void setIntimationDate(Date intimationDate) {
		if(intimationDate !=  null){
			String dateformat =new SimpleDateFormat("dd/MM/yyyy").format(intimationDate);
			setIntimationDateValue(dateformat);
		    this.intimationDate = intimationDate;
		}
	}
	public String getIntimationDateValue() {
		return intimationDateValue;
	}
	public void setIntimationDateValue(String intimationDateValue) {
		this.intimationDateValue = intimationDateValue;
	}
	public String getPolicyNumber() {
		return policyNumber;
	}
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
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
	public String getMainMemberName() {
		return mainMemberName;
	}
	public void setMainMemberName(String mainMemberName) {
		this.mainMemberName = mainMemberName;
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
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public Double getPatientAge() {
		return patientAge;
	}
	public void setPatientAge(Double patientAge) {
		this.patientAge = patientAge;
	}
	public String getAdmissionReason() {
		return admissionReason;
	}
	public void setAdmissionReason(String admissionReason) {
		this.admissionReason = admissionReason;
	}
	public String getHospitalType() {
		return hospitalType;
	}
	public void setHospitalType(String hospitalType) {
		this.hospitalType = hospitalType;
	}
	public Date getHospitalDate() {
		return hospitalDate;
	}
	public void setHospitalDate(Date hospitalDate) {	
		
		if(hospitalDate !=  null){
			String dateformat =new SimpleDateFormat("dd/MM/yyyy").format(hospitalDate);
			setHospitalDateValue(dateformat);
		    this.hospitalDate = hospitalDate;
		}
	}
	public String getHospitalDateValue() {
		return hospitalDateValue;
	}
	public void setHospitalDateValue(String hospitalDateValue) {
		this.hospitalDateValue = hospitalDateValue;
	}
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public String getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
	}
	public String getHospitalState() {
		return hospitalState;
	}
	public void setHospitalState(String hospitalState) {
		this.hospitalState = hospitalState;
	}
	public String getHospitalCity() {
		return hospitalCity;
	}
	public void setHospitalCity(String hospitalCity) {
		this.hospitalCity = hospitalCity;
	}
	public String getFieldDoctorNameAllocated() {
		return fieldDoctorNameAllocated;
	}
	public void setFieldDoctorNameAllocated(String fieldDoctorNameAllocated) {
		this.fieldDoctorNameAllocated = fieldDoctorNameAllocated;
	}
	public String getContactNoOftheDoctor() {
		return contactNoOftheDoctor;
	}
	public void setContactNoOftheDoctor(String contactNoOftheDoctor) {
		this.contactNoOftheDoctor = contactNoOftheDoctor;
	}
	public Date getDateAndTimeOfAllocation() {
		return dateAndTimeOfAllocation;
	}
	public void setDateAndTimeOfAllocation(Date dateAndTimeOfAllocation) {		
		if(dateAndTimeOfAllocation !=  null){
			String dateformat =new SimpleDateFormat("dd/MM/yyyy").format(dateAndTimeOfAllocation);
			setDateAndTimeOfAllocationValue(dateformat);
		    this.dateAndTimeOfAllocation = dateAndTimeOfAllocation;
		}
	}
	public String getDateAndTimeOfAllocationValue() {
		return dateAndTimeOfAllocationValue;
	}
	public void setDateAndTimeOfAllocationValue(String dateAndTimeOfAllocationValue) {
		this.dateAndTimeOfAllocationValue = dateAndTimeOfAllocationValue;
	}
	public String getRegistrationStatus() {
		return registrationStatus;
	}
	public void setRegistrationStatus(String registrationStatus) {
		this.registrationStatus = registrationStatus;
	}
	public String getCr() {
		return cr;
	}
	public void setCr(String cr) {
		this.cr = cr;
	}
	public String getTotalAuthAmount() {
		return totalAuthAmount;
	}
	public void setTotalAuthAmount(String totalAuthAmount) {
		this.totalAuthAmount = totalAuthAmount;
	}
	public Date getAuthDate() {
		return authDate;
	}
	public void setAuthDate(Date authDate) {
		if(authDate !=  null){
			String dateformat =new SimpleDateFormat("dd/MM/yyyy").format(authDate);
			setAuthDateValue(dateformat);
		    this.authDate = authDate;
		}
		
	}
	public String getCashlessStatus() {
		return cashlessStatus;
	}
	public void setCashlessStatus(String cashlessStatus) {
		this.cashlessStatus = cashlessStatus;
	}
	public String getCashlessReason() {
		return cashlessReason;
	}
	public void setCashlessReason(String cashlessReason) {
		this.cashlessReason = cashlessReason;
	}
	
	


}

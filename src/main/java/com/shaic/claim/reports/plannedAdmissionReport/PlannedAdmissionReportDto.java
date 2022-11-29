package com.shaic.claim.reports.plannedAdmissionReport;

import java.text.SimpleDateFormat;

import com.shaic.newcode.wizard.dto.NewIntimationDto;

public class PlannedAdmissionReportDto {
	
	private Integer sno;
	private String intimationNo;
	private String insuredName;
	private String admittedOn;
	private String createdOn;
	private String hospitalName;
	private String hospitalCity;
	private String doctorName;
	private String emailId;
	
	public PlannedAdmissionReportDto(NewIntimationDto intimationDto){
		
		this.createdOn = new SimpleDateFormat("dd/MM/yyyy").format(intimationDto.getCreatedDate());
		this.admittedOn = intimationDto.getAdmissionDate() != null ? new SimpleDateFormat("dd/MM/yyyy").format(intimationDto.getAdmissionDate()) : "";
		this.intimationNo = intimationDto.getIntimationId();
		this.insuredName = intimationDto.getInsuredPatient().getInsuredName();
		this.hospitalName = intimationDto.getHospitalDto().getRegistedHospitals().getName();	
		this.hospitalCity = intimationDto.getHospitalDto().getRegistedHospitals().getCity();
		this.doctorName = intimationDto.getDoctorName() != null ?intimationDto.getDoctorName() : "";
		
	}
	
	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
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

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public String getAdmittedOn() {
		return admittedOn;
	}

	public void setAdmittedOn(String admittedOn) {
		this.admittedOn = admittedOn;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public Integer getSno() {
		return sno;
	}

	public void setSno(Integer sno) {
		this.sno = sno;
	}	

	
}

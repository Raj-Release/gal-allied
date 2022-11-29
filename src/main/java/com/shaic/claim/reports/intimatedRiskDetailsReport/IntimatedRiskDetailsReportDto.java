package com.shaic.claim.reports.intimatedRiskDetailsReport;

import java.text.SimpleDateFormat;

import com.shaic.domain.Intimation;
import com.shaic.newcode.wizard.dto.NewIntimationDto;

public class IntimatedRiskDetailsReportDto {
	
	private String intimationNo;
	private String policyNo;
	private String claimantName;
	private String intimatorName;
	private String hospitalName;
	private String mode;
	private String madeBy;
	private String status;
	private String date;
	private String contactNo;
	
	public IntimatedRiskDetailsReportDto(NewIntimationDto intimationDto){
		
		this.date = new SimpleDateFormat("dd/MM/yyyy").format(intimationDto.getCreatedDate());
		this.policyNo = intimationDto.getPolicy().getPolicyNumber();
		this.intimationNo = intimationDto.getIntimationId();
		this.claimantName = intimationDto.getInsuredPatient().getInsuredName();
		this.intimatorName = intimationDto.getIntimaterName();
		this.mode = intimationDto.getModeOfIntimation() != null && intimationDto.getModeOfIntimation().getValue() != null ? intimationDto.getModeOfIntimation().getValue() : "";
		this.madeBy = intimationDto.getCreatedBy() != null ? intimationDto.getCreatedBy() : "";
		this.status = intimationDto.getRegistrationStatus() != null ? intimationDto.getRegistrationStatus().toUpperCase() : "NOT REGISTERED"; 
		this.hospitalName = intimationDto.getHospitalDto().getRegistedHospitals().getName();	
		this.contactNo = intimationDto.getCallerContactNum() != null ? intimationDto.getCallerContactNum() : "";
		
	}
	
	public IntimatedRiskDetailsReportDto(Intimation intimationObj, String hospName){
		
		this.date = new SimpleDateFormat("dd/MM/yyyy").format(intimationObj.getCreatedDate());
		this.policyNo = intimationObj.getPolicy().getPolicyNumber();
		this.intimationNo = intimationObj.getIntimationId();
		this.claimantName = intimationObj.getInsured().getInsuredName();
		this.intimatorName = intimationObj.getIntimaterName();
		this.mode = intimationObj.getIntimationMode() != null && intimationObj.getIntimationMode().getValue() != null ? intimationObj.getIntimationMode().getValue() : "";
		this.madeBy = intimationObj.getCreatedBy() != null ? intimationObj.getCreatedBy() : "";
		this.status = intimationObj.getRegistrationStatus() != null ? intimationObj.getRegistrationStatus().toUpperCase() : "NOT REGISTERED"; 
		this.hospitalName = hospName;	
		this.contactNo = intimationObj.getCallerLandlineNumber() != null ? intimationObj.getCallerLandlineNumber() : "";
		
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

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getClaimantName() {
		return claimantName;
	}

	public void setClaimantName(String claimantName) {
		this.claimantName = claimantName;
	}

	public String getIntimatorName() {
		return intimatorName;
	}

	public void setIntimatorName(String intimatorName) {
		this.intimatorName = intimatorName;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getMadeBy() {
		return madeBy;
	}

	public void setMadeBy(String madeBy) {
		this.madeBy = madeBy;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}
	
}

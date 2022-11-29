package com.shaic.claim.reports.callcenterDashBoard;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import com.shaic.domain.Intimation;
import com.shaic.newcode.wizard.dto.NewIntimationDto;

public class CallcenterDashBoardReportDto {
	
	private Map<String, Object> searchFilters;
	private String intimationNo;
	private String claimNo;
	private String intimationDate;
	private String policyNumber;
	private String patientName;
	private String intimatorName;
	private String intimatedBy;
	private String intimationMode;
	private String status;
	private String contactNo;
	private String hospitalName;
	
	public CallcenterDashBoardReportDto(){
		this.searchFilters = new HashMap<String, Object>();
	}
		
	public CallcenterDashBoardReportDto(NewIntimationDto newIntimationDto){
		
		this.policyNumber = newIntimationDto.getPolicy().getPolicyNumber();
		this.intimationNo = newIntimationDto.getIntimationId();
		this.intimationDate = new SimpleDateFormat("dd/MM/yyyy").format(newIntimationDto.getCreatedDate());
		this.intimatorName = newIntimationDto.getIntimaterName() != null ? newIntimationDto.getIntimaterName() : "";
		this.intimatedBy = newIntimationDto.getIntimatedBy() != null ? (newIntimationDto.getIntimatedBy().getValue() != null ? newIntimationDto.getIntimatedBy().getValue() : "" ) : "";
		this.intimationMode = newIntimationDto.getModeOfIntimation() != null ? newIntimationDto.getModeOfIntimation().getValue() : "";
		this.patientName = newIntimationDto.getInsuredPatientName();
		this.hospitalName = newIntimationDto.getHospitalDto().getRegistedHospitals().getName();	
		this.contactNo = newIntimationDto.getPolicy() != null ? ( newIntimationDto.getPolicy().getPolTelephoneNumber() != null ? newIntimationDto.getPolicy().getPolTelephoneNumber() : "" ) : "";
		this.status = newIntimationDto.getStatus() != null ? (newIntimationDto.getStatus().getProcessValue() != null ? newIntimationDto.getStatus().getProcessValue() : "" ) : "";
	}
public CallcenterDashBoardReportDto(Intimation newIntimation){
		
		this.policyNumber = newIntimation.getPolicy().getPolicyNumber();
		this.intimationNo = newIntimation.getIntimationId();
		this.intimationDate = new SimpleDateFormat("dd/MM/yyyy").format(newIntimation.getCreatedDate());
		this.intimatorName = newIntimation.getIntimaterName() != null ? newIntimation.getIntimaterName() : "";
		this.intimatedBy = newIntimation.getIntimatedBy() != null ? (newIntimation.getIntimatedBy().getValue() != null ? newIntimation.getIntimatedBy().getValue() : "" ) : "";
		this.intimationMode = newIntimation.getIntimationMode() != null ? newIntimation.getIntimationMode().getValue() : "";
		this.patientName = newIntimation.getInsured().getInsuredName();
//		this.hospitalName = newIntimation.getHospitalDto().getRegistedHospitals().getName();	
		this.contactNo = newIntimation.getPolicy() != null ? ( newIntimation.getPolicy().getRegisteredMobileNumber() != null ? newIntimation.getPolicy().getRegisteredMobileNumber() : "" ) : "";
		this.status = newIntimation.getStatus() != null ? (newIntimation.getStatus().getProcessValue() != null ? newIntimation.getStatus().getProcessValue() : "" ) : "";
	}
	
	public Map<String, Object> getSearchFilters() {
		return searchFilters;
	}

	public void setSearchFilters(Map<String, Object> filters) {
		this.searchFilters = filters;
	}

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public String getClaimNo() {
		return claimNo;
	}

	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
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

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getIntimatorName() {
		return intimatorName;
	}

	public void setIntimatorName(String intimatorName) {
		this.intimatorName = intimatorName;
	}

	public String getIntimatedBy() {
		return intimatedBy;
	}

	public void setIntimatedBy(String intimatedBy) {
		this.intimatedBy = intimatedBy;
	}

	public String getIntimationMode() {
		return intimationMode;
	}

	public void setIntimationMode(String intimationMode) {
		this.intimationMode = intimationMode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
    
	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}	

}

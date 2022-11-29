package com.shaic.claim.bedphoto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.newcode.wizard.dto.NewIntimationDto;

public class SearchBedPhotoTableDTO extends AbstractTableDTO implements Serializable{
	
	private String intimationNo;
	private String policyNo;
	private Long insuredKey;
	private String insuredPatientName;
	private String hospitalName;
	private Date dateOfAdmission;
	private Date dateOfDischarge;
	private Long hospitalNameKey;
	private String dateOfAdmsissionStr;
	private String dateOfDischargeStr;
	
	private NewIntimationDto newIntimationDto;
	
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
	public Long getInsuredKey() {
		return insuredKey;
	}
	public void setInsuredKey(Long insuredKey) {
		this.insuredKey = insuredKey;
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
	public Date getDateOfAdmission() {
		return dateOfAdmission;
	}
	public void setDateOfAdmission(Date dateOfAdmission) {
		this.dateOfAdmission = dateOfAdmission;
	}
	public Date getDateOfDischarge() {
		return dateOfDischarge;
	}
	public void setDateOfDischarge(Date dateOfDischarge) {
		this.dateOfDischarge = dateOfDischarge;
	}
	public NewIntimationDto getNewIntimationDto() {
		return newIntimationDto;
	}
	public void setNewIntimationDto(NewIntimationDto newIntimationDto) {
		this.newIntimationDto = newIntimationDto;
	}
	public Long getHospitalNameKey() {
		return hospitalNameKey;
	}
	public void setHospitalNameKey(Long hospitalNameKey) {
		this.hospitalNameKey = hospitalNameKey;
	}
	public String getDateOfAdmsissionStr() {
		return dateOfAdmsissionStr;
	}
	public void setDateOfAdmsissionStr(String dateOfAdmsissionStr) {
		this.dateOfAdmsissionStr = dateOfAdmsissionStr;
	}
	public String getDateOfDischargeStr() {
		return dateOfDischargeStr;
	}
	public void setDateOfDischargeStr(String dateOfDischargeStr) {
		this.dateOfDischargeStr = dateOfDischargeStr;
	}
	
	

}

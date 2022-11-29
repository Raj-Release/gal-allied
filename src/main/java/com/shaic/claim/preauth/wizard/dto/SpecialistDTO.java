package com.shaic.claim.preauth.wizard.dto;

import java.io.File;
import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;

public class SpecialistDTO {

private Long key;

private SelectValue specialistConsulted;

private Long newInitiatePedEndorsementKey;

private String specialistRemarks;

private SelectValue specialistType;

private String referringReason;	

private String queryRemarks;

private Long sequenceNumber;

private String reviewRemarks;

private String initiateType;

private String officeCode;

private File uploadFile;

private String status;

private Date statusDate;

public Long getKey() {
	return key;
}
public void setKey(Long key) {
	this.key = key;
}
public SelectValue getSpecialistConsulted() {
	return specialistConsulted;
}
public void setSpecialistConsulted(SelectValue specialistConsulted) {
	this.specialistConsulted = specialistConsulted;
}
public Long getNewInitiatePedEndorsementKey() {
	return newInitiatePedEndorsementKey;
}
public void setNewInitiatePedEndorsementKey(Long newInitiatePedEndorsementKey) {
	this.newInitiatePedEndorsementKey = newInitiatePedEndorsementKey;
}
public String getSpecialistRemarks() {
	return specialistRemarks;
}
public void setSpecialistRemarks(String specialistRemarks) {
	this.specialistRemarks = specialistRemarks;
}
public SelectValue getSpecialistType() {
	return specialistType;
}
public void setSpecialistType(SelectValue specialistType) {
	this.specialistType = specialistType;
}
public String getReferringReason() {
	return referringReason;
}
public void setReferringReason(String referringReason) {
	this.referringReason = referringReason;
}
public String getQueryRemarks() {
	return queryRemarks;
}
public void setQueryRemarks(String queryRemarks) {
	this.queryRemarks = queryRemarks;
}
public String getReviewRemarks() {
	return reviewRemarks;
}
public void setReviewRemarks(String reviewRemarks) {
	this.reviewRemarks = reviewRemarks;
}
public String getInitiateType() {
	return initiateType;
}
public void setInitiateType(String initiateType) {
	this.initiateType = initiateType;
}
public File getUploadFile() {
	return uploadFile;
}
public void setUploadFile(File uploadFile) {
	this.uploadFile = uploadFile;
}
public String getActivestatus() {
	return status;
}
public void setActivestatus(String status) {
	this.status = status;
}
public Date getActiveStatusDate() {
	return statusDate;
}
public void setActiveStatusDate(Date statusDate) {
	this.statusDate = statusDate;
}
public String getOfficeCode() {
	return officeCode;
}
public void setOfficeCode(String officeCode) {
	this.officeCode = officeCode;
}
public Long getSequenceNumber() {
	return sequenceNumber;
}
public void setSequenceNumber(Long sequenceNumber) {
	this.sequenceNumber = sequenceNumber;
}

}

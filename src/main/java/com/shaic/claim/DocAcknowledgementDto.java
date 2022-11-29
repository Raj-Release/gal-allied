package com.shaic.claim;

import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.Stage;

public class DocAcknowledgementDto {

	private Long key;
	
	private ClaimDto claimDto;
	
	private String acknowledgeNumber;
	
	private Long rodKey;
	
	private SelectValue documentReceivedFrom;
	
	private SelectValue reconsiderationReason;
	
	private Date documentReceivedDate;
	
	private SelectValue modeOfReceipt;
	
	private String reconsiderationRequest;
	
	private Long insuredContactNumber;
	
	private String insuredEmailId;
	
	private String hospitalisationFlag;
	
	private String preHospitalisationFlag;
	
	private String postHospitalisationFlag;
	
	private String partialHospitalisationFlag;
	
	private String lumpsumAmountFlag;
	
	private String hospitalCashFlag;
	
	private String patientCareFlag;
	
	private String additionalRemarks;
	
	private Long activeStatus;
	
	private String officeCode;
	
	private Stage stage;
	
	private Status status;
	
	private String createdBy;
	
	private Date createdDate;
	
	private String modifiedBy;
	
	private Date modifiedDate;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public ClaimDto getClaimDto() {
		return claimDto;
	}

	public void setClaimDto(ClaimDto claimDto) {
		this.claimDto = claimDto;
	}

	public String getAcknowledgeNumber() {
		return acknowledgeNumber;
	}

	public void setAcknowledgeNumber(String acknowledgeNumber) {
		this.acknowledgeNumber = acknowledgeNumber;
	}

	public Long getRodKey() {
		return rodKey;
	}

	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}

	public SelectValue getDocumentReceivedFrom() {
		return documentReceivedFrom;
	}

	public void setDocumentReceivedFrom(SelectValue documentReceivedFrom) {
		this.documentReceivedFrom = documentReceivedFrom;
	}

	public Date getDocumentReceivedDate() {
		return documentReceivedDate;
	}

	public void setDocumentReceivedDate(Date documentReceivedDate) {
		this.documentReceivedDate = documentReceivedDate;
	}

	public SelectValue getModeOfReceipt() {
		return modeOfReceipt;
	}

	public void setModeOfReceipt(SelectValue modeOfReceipt) {
		this.modeOfReceipt = modeOfReceipt;
	}

	public String getReconsiderationRequest() {
		return reconsiderationRequest;
	}

	public void setReconsiderationRequest(String reconsiderationRequest) {
		this.reconsiderationRequest = reconsiderationRequest;
	}

	public Long getInsuredContactNumber() {
		return insuredContactNumber;
	}

	public void setInsuredContactNumber(Long insuredContactNumber) {
		this.insuredContactNumber = insuredContactNumber;
	}

	public String getInsuredEmailId() {
		return insuredEmailId;
	}

	public void setInsuredEmailId(String insuredEmailId) {
		this.insuredEmailId = insuredEmailId;
	}

	public String getHospitalisationFlag() {
		return hospitalisationFlag;
	}

	public void setHospitalisationFlag(String hospitalisationFlag) {
		this.hospitalisationFlag = hospitalisationFlag;
	}

	public String getPreHospitalisationFlag() {
		return preHospitalisationFlag;
	}

	public void setPreHospitalisationFlag(String preHospitalisationFlag) {
		this.preHospitalisationFlag = preHospitalisationFlag;
	}

	public String getPostHospitalisationFlag() {
		return postHospitalisationFlag;
	}

	public void setPostHospitalisationFlag(String postHospitalisationFlag) {
		this.postHospitalisationFlag = postHospitalisationFlag;
	}

	public String getPartialHospitalisationFlag() {
		return partialHospitalisationFlag;
	}

	public void setPartialHospitalisationFlag(String partialHospitalisationFlag) {
		this.partialHospitalisationFlag = partialHospitalisationFlag;
	}

	public String getLumpsumAmountFlag() {
		return lumpsumAmountFlag;
	}

	public void setLumpsumAmountFlag(String lumpsumAmountFlag) {
		this.lumpsumAmountFlag = lumpsumAmountFlag;
	}

	public String getHospitalCashFlag() {
		return hospitalCashFlag;
	}

	public void setHospitalCashFlag(String hospitalCashFlag) {
		this.hospitalCashFlag = hospitalCashFlag;
	}

	public String getPatientCareFlag() {
		return patientCareFlag;
	}

	public void setPatientCareFlag(String patientCareFlag) {
		this.patientCareFlag = patientCareFlag;
	}

	public String getAdditionalRemarks() {
		return additionalRemarks;
	}

	public void setAdditionalRemarks(String additionalRemarks) {
		this.additionalRemarks = additionalRemarks;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getOfficeCode() {
		return officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public SelectValue getReconsiderationReason() {
		return reconsiderationReason;
	}

	public void setReconsiderationReason(SelectValue reconsiderationReason) {
		this.reconsiderationReason = reconsiderationReason;
	}    
}

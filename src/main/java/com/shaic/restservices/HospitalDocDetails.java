package com.shaic.restservices;

import java.util.List;

public class HospitalDocDetails {

	private String refId;
	private String intimationNumber;
	private String providerCode;
	private String hospitalName;
	private String claimedAmount;
	private String uploadDate;
	private String fileTypeId;
	private List<HospitalDocUploadDetails> documentDetails;
	
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
	public String getIntimationNumber() {
		return intimationNumber;
	}
	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}
	public String getProviderCode() {
		return providerCode;
	}
	public void setProviderCode(String providerCode) {
		this.providerCode = providerCode;
	}
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public String getClaimedAmount() {
		return claimedAmount;
	}
	public void setClaimedAmount(String claimedAmount) {
		this.claimedAmount = claimedAmount;
	}
	public String getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(String uploadDate) {
		this.uploadDate = uploadDate;
	}
	public String getFileTypeId() {
		return fileTypeId;
	}
	public void setFileTypeId(String fileTypeId) {
		this.fileTypeId = fileTypeId;
	}
	public List<HospitalDocUploadDetails> getDocumentDetails() {
		return documentDetails;
	}
	public void setDocumentDetails(List<HospitalDocUploadDetails> documentDetails) {
		this.documentDetails = documentDetails;
	}
}

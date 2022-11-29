package com.shaic.claim.OMPProcessOmpAcknowledgementDocuments.Upload;

public class OMPViewUploadDocumentDetailsTableDTO {

	private String serialNumber;
	private String fileType;
	private String documentType;
	private String receivedStatus;
	private String noOfDocuments;
	private String remarks;
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getDocumentType() {
		return documentType;
	}
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	public String getReceivedStatus() {
		return receivedStatus;
	}
	public void setReceivedStatus(String receivedStatus) {
		this.receivedStatus = receivedStatus;
	}
	public String getNoOfDocuments() {
		return noOfDocuments;
	}
	public void setNoOfDocuments(String noOfDocuments) {
		this.noOfDocuments = noOfDocuments;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	
}

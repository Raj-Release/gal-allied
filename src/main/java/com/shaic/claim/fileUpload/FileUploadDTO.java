package com.shaic.claim.fileUpload;


public class FileUploadDTO {
	
	private Long key;
	
	private String slNo = "1";
	
	private String requestType;
	
	private String requestorMarks;
	
	private String fileUpload;
	
	private String remarks;
	
	private String fileToken;
	
	private String fileName;
	
	private String tmpFileName;
	
	private String tmpFileToken;
	
	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getRequestorMarks() {
		return requestorMarks;
	}

	public void setRequestorMarks(String requestorMarks) {
		this.requestorMarks = requestorMarks;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getFileUpload() {
		return fileUpload;
	}

	public void setFileUpload(String fileUpload) {
		this.fileUpload = fileUpload;
	}

	public String getFileToken() {
		return fileToken;
	}

	public void setFileToken(String fileToken) {
		this.fileToken = fileToken;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getSlNo() {
		return slNo;
	}

	public void setSlNo(String slNo) {
		this.slNo = slNo;
	}

	public String getTmpFileName() {
		return tmpFileName;
	}

	public void setTmpFileName(String tmpFileName) {
		this.tmpFileName = tmpFileName;
	}

	public String getTmpFileToken() {
		return tmpFileToken;
	}

	public void setTmpFileToken(String tmpFileToken) {
		this.tmpFileToken = tmpFileToken;
	}

	

}

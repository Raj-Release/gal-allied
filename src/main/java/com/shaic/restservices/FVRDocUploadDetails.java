package com.shaic.restservices;

import org.codehaus.jackson.annotate.JsonIgnore;

public class FVRDocUploadDetails {
	private String docId;
	private String fileName;
	private String fileTypeId;
	private String fileTypeName;
	private String fileContent;
	
	@JsonIgnore
	private Long fileDMSToken;
	
	@JsonIgnore
	private boolean isFileUploadInDMS;
	
	@JsonIgnore
	private String dynamicFileName;
	
	public String getDocId() {
		return docId;
	}
	public void setDocId(String docId) {
		this.docId = docId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileTypeId() {
		return fileTypeId;
	}
	public void setFileTypeId(String fileTypeId) {
		this.fileTypeId = fileTypeId;
	}
	public String getFileTypeName() {
		return fileTypeName;
	}
	public void setFileTypeName(String fileTypeName) {
		this.fileTypeName = fileTypeName;
	}
	public String getFileContent() {
		return fileContent;
	}
	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}
	public Long getFileDMSToken() {
		return fileDMSToken;
	}
	public void setFileDMSToken(Long fileDMSToken) {
		this.fileDMSToken = fileDMSToken;
	}
	public boolean isFileUploadInDMS() {
		return isFileUploadInDMS;
	}
	public void setFileUploadInDMS(boolean isFileUploadInDMS) {
		this.isFileUploadInDMS = isFileUploadInDMS;
	}
	public String getDynamicFileName() {
		return dynamicFileName;
	}
	public void setDynamicFileName(String dynamicFileName) {
		this.dynamicFileName = dynamicFileName;
	}
	
	
}

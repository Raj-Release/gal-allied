package com.shaic.restservices;

import org.codehaus.jackson.annotate.JsonIgnore;

public class GalaxyDocuments {

	private String documentType;
	private String documentName;
	private String documentContent;
	
	@JsonIgnore
	private Long fileDMSToken;
	
	@JsonIgnore
	private boolean isFileUploadInDMS;
	
	@JsonIgnore
	private String dynamicFileName;
	
	public String getDocumentType() {
		return documentType;
	}
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	public String getDocumentName() {
		return documentName;
	}
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	public String getDocumentContent() {
		return documentContent;
	}
	public void setDocumentContent(String documentContent) {
		this.documentContent = documentContent;
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

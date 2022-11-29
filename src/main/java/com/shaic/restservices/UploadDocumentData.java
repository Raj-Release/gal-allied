package com.shaic.restservices;


public class UploadDocumentData {

	private String documentFileName;
	private String documentContent;
	private String documentKey;
	private String intimationNumber;
	private String docUniqueKey;
	
	public String getDocumentFileName() {
		return documentFileName;
	}
	public void setDocumentFileName(String documentFileName) {
		this.documentFileName = documentFileName;
	}
	public String getDocumentContent() {
		return documentContent;
	}
	public void setDocumentContent(String documentContent) {
		this.documentContent = documentContent;
	}
	public String getDocumentKey() {
		return documentKey;
	}
	public void setDocumentKey(String documentKey) {
		this.documentKey = documentKey;
	}
	public String getIntimationNumber() {
		return intimationNumber;
	}
	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}
	public String getDocUniqueKey() {
		return docUniqueKey;
	}
	public void setDocUniqueKey(String docUniqueKey) {
		this.docUniqueKey = docUniqueKey;
	}
}

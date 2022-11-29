package com.shaic.restservices;

public class RSDocumentDetails {
	
	private String documentType;
	private String documentName;
	private Long documentId;
	private String documentUrl;
	private String typeOfDoc;
	private String chequeNo;
	
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
	public Long getDocumentId() {
		return documentId;
	}
	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}
	public String getDocumentUrl() {
		return documentUrl;
	}
	public void setDocumentUrl(String documentUrl) {
		this.documentUrl = documentUrl;
	}
	public String getTypeOfDoc() {
		return typeOfDoc;
	}
	public void setTypeOfDoc(String typeOfDoc) {
		this.typeOfDoc = typeOfDoc;
	}
	public String getChequeNo() {
		return chequeNo;
	}
	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}
	@Override
	public String toString() {
		return "RSDocumentDetails [documentType=" + documentType
				+ ", documentName=" + documentName + ", documentId="
				+ documentId + ", documentUrl=" + documentUrl + ", typeOfDoc="
				+ typeOfDoc + ", chequeNo=" + chequeNo + "]";
	}
	
}

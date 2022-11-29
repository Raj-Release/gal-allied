package com.shaic.restservices;

public class DocumentDetails {

	private String documentName;
	private String fileType;
	private String documentToken;
	private String billNo;
	private String billDate;
	private String noOfItems;
	private String billValue;
	
	public String getDocumentName() {
		return documentName;
	}
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getDocumentToken() {
		return documentToken;
	}
	public void setDocumentToken(String documentToken) {
		this.documentToken = documentToken;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	public String getNoOfItems() {
		return noOfItems;
	}
	public void setNoOfItems(String noOfItems) {
		this.noOfItems = noOfItems;
	}
	public String getBillValue() {
		return billValue;
	}
	public void setBillValue(String billValue) {
		this.billValue = billValue;
	}
	
}

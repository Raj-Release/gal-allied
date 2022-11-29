package com.shaic.claim.lumen.components;

import com.shaic.arch.table.AbstractTableDTO;

@SuppressWarnings("serial")
public class LetterDetailsDTO extends AbstractTableDTO{

	//private Boolean isPreviewFlag;
	private String letterName;
	private String toPerson;
	private LetterAddressDetails addressDetails;
	private String subject;
	private String letterContent;
	private String address;
	
//	private Button deleteButton;
//	private Button previewButton;
	
	private Long documentToken;
	
/*	public Boolean getIsPreviewFlag() {
		return isPreviewFlag;
	}
	public void setIsPreviewFlag(Boolean isPreviewFlag) {
		this.isPreviewFlag = isPreviewFlag;
	}*/
	public String getLetterName() {
		return letterName;
	}
	public void setLetterName(String letterName) {
		this.letterName = letterName;
	}
//	public Button getDeleteButton() {
//		return deleteButton;
//	}
//	public void setDeleteButton(Button deleteButton) {
//		this.deleteButton = deleteButton;
//	}
	public String getToPerson() {
		return toPerson;
	}
	public void setToPerson(String toPerson) {
		this.toPerson = toPerson;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getLetterContent() {
		return letterContent;
	}
	public void setLetterContent(String letterContent) {
		this.letterContent = letterContent;
	}
//	public Button getPreviewButton() {
//		return previewButton;
//	}
//	public void setPreviewButton(Button previewButton) {
//		this.previewButton = previewButton;
//	}
	public LetterAddressDetails getAddressDetails() {
		return addressDetails;
	}
	public void setAddressDetails(LetterAddressDetails addressDetails) {
		this.addressDetails = addressDetails;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Long getDocumentToken() {
		return documentToken;
	}
	public void setDocumentToken(Long documentToken) {
		this.documentToken = documentToken;
	}
	
}

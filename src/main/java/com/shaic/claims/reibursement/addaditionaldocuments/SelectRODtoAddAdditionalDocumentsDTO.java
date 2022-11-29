package com.shaic.claims.reibursement.addaditionaldocuments;

import java.util.Date;

public class SelectRODtoAddAdditionalDocumentsDTO {
	
	private Long key;
	
	private String sNo;
	
	private String rodNo;
	
	private String billClassification;
	
	private String claimedAmt;
	
	private String approvedAmt;
	
	private String rodStatus;
	
	private Long statusKey;
	
	private String coverCode;
	
	private Date docUplodedDate;
	
	private Long rodKey;
	
	private Boolean isDocumentVerified;
	
	private String screenName;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getsNo() {
		return sNo;
	}

	public void setsNo(String sNo) {
		this.sNo = sNo;
	}

	public String getRodNo() {
		return rodNo;
	}

	public void setRodNo(String rodNo) {
		this.rodNo = rodNo;
	}

	public String getBillClassification() {
		return billClassification;
	}

	public void setBillClassification(String billClassification) {
		this.billClassification = billClassification;
	}

	public String getClaimedAmt() {
		return claimedAmt;
	}

	public void setClaimedAmt(String claimedAmt) {
		this.claimedAmt = claimedAmt;
	}

	public String getApprovedAmt() {
		return approvedAmt;
	}

	public void setApprovedAmt(String approvedAmt) {
		this.approvedAmt = approvedAmt;
	}

	public String getRodStatus() {
		return rodStatus;
	}

	public void setRodStatus(String rodStatus) {
		this.rodStatus = rodStatus;
	}

	public Long getStatusKey() {
		return statusKey;
	}

	public void setStatusKey(Long statusKey) {
		this.statusKey = statusKey;
	}

	public String getCoverCode() {
		return coverCode;
	}

	public void setCoverCode(String coverCode) {
		this.coverCode = coverCode;
	}	

	public Long getRodKey() {
		return rodKey;
	}

	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}

	public Date getDocUplodedDate() {
		return docUplodedDate;
	}

	public void setDocUplodedDate(Date docUplodedDate) {
		this.docUplodedDate = docUplodedDate;
	}

	public Boolean getIsDocumentVerified() {
		return isDocumentVerified;
	}

	public void setIsDocumentVerified(Boolean isDocumentVerified) {
		this.isDocumentVerified = isDocumentVerified;
	}
	
	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

}

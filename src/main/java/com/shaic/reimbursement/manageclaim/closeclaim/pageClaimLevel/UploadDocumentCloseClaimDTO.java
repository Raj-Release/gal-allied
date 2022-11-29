package com.shaic.reimbursement.manageclaim.closeclaim.pageClaimLevel;

import java.io.Serializable;
import java.util.Date;

import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;
import com.shaic.domain.DocumentDetails;

public class UploadDocumentCloseClaimDTO extends AbstractSearchDTO implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String fileName;
	
	private String strDateAndTime;
	
	private Date dateAndTime;
	
	private String referenceNo;
	
	private Long documentDetailsKey;
	
	private DocumentDetails documentDetails;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getStrDateAndTime() {
		return strDateAndTime;
	}

	public void setStrDateAndTime(String strDateAndTime) {
		this.strDateAndTime = strDateAndTime;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public Date getDateAndTime() {
		return dateAndTime;
	}

	public void setDateAndTime(Date dateAndTime) {
		this.dateAndTime = dateAndTime;
	}

	public Long getDocumentDetailsKey() {
		return documentDetailsKey;
	}

	public void setDocumentDetailsKey(Long documentDetailsKey) {
		this.documentDetailsKey = documentDetailsKey;
	}

	public DocumentDetails getDocumentDetails() {
		return documentDetails;
	}

	public void setDocumentDetails(DocumentDetails documentDetails) {
		this.documentDetails = documentDetails;
	}
	
	

}

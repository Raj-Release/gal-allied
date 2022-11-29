package com.shaic.claim.fileUpload;

import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;

/**
 * @author Star
 *
 */
public class MultipleUploadDocumentDTO extends AbstractTableDTO {
	
	private Long docKey;
	
	private Boolean isEdit = false;
	
	public Long getDocKey() {
		return docKey;
	}

	public void setDocKey(Long docKey) {
		this.docKey = docKey;
	}

	private Integer sNo;
	
	private String transactionName;
	
	private Long transactionKey;
	
	private String fileName;
	
	private String fileToken;
	
	private Date uploadedDate;
	
	private SelectValue fileType;
	
	private String deletedFlag;
	
	private Date uploadLetterDate;
	
	private Integer noOfPages;
	
	private String fileUrl;
	
	private String pccFileType;
	
	private String intimationNumber;
	
	private String fileUploadRemarks;

	public SelectValue getFileType() {
		return fileType;
	}

	public void setFileType(SelectValue fileType) {
		this.fileType = fileType;
	}

	public String getTransactionName() {
		return transactionName;
	}

	public void setTransactionName(String transactionName) {
		this.transactionName = transactionName;
	}

	public Long getTransactionKey() {
		return transactionKey;
	}

	public void setTransactionKey(Long transactionKey) {
		this.transactionKey = transactionKey;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileToken() {
		return fileToken;
	}

	public void setFileToken(String fileToken) {
		this.fileToken = fileToken;
	}

	public Integer getsNo() {
		return sNo;
	}

	public void setsNo(Integer sNo) {
		this.sNo = sNo;
	}

	public Date getUploadedDate() {
		return uploadedDate;
	}

	public void setUploadedDate(Date uploadedDate) {
		this.uploadedDate = uploadedDate;
	}

	public String getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
	}

	public Date getUploadLetterDate() {
		return uploadLetterDate;
	}

	public void setUploadLetterDate(Date uploadLetterDate) {
		this.uploadLetterDate = uploadLetterDate;
	}

	public Integer getNoOfPages() {
		return noOfPages;
	}

	public void setNoOfPages(Integer noOfPages) {
		this.noOfPages = noOfPages;
	}

	public Boolean getIsEdit() {
		return isEdit;
	}

	public void setIsEdit(Boolean isEdit) {
		this.isEdit = isEdit;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getPccFileType() {
		return pccFileType;
	}

	public void setPccFileType(String pccFileType) {
		this.pccFileType = pccFileType;
	}

	public String getIntimationNumber() {
		return intimationNumber;
	}

	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}

	public String getFileUploadRemarks() {
		return fileUploadRemarks;
	}

	public void setFileUploadRemarks(String fileUploadRemarks) {
		this.fileUploadRemarks = fileUploadRemarks;
	}
	
	
	
}

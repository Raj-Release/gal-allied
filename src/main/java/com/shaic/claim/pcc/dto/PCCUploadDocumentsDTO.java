package com.shaic.claim.pcc.dto;

import com.shaic.arch.table.AbstractTableDTO;

public class PCCUploadDocumentsDTO extends AbstractTableDTO {

	private static final long serialVersionUID = 1L;
	
	private Integer sno;
	
	private String intimationNumber;
	
	private String fileName;
	
	private String fileType;
	
	private String token;
	
	private String deletedFlag;
	
	private String fileUploadRemarks;
	
	private String transacName;
	
	private Long transactionKey;
	
	private Boolean isEdit = false;

	public Integer getSno() {
		return sno;
	}

	public void setSno(Integer sno) {
		this.sno = sno;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
	}

	public String getTransacName() {
		return transacName;
	}

	public void setTransacName(String transacName) {
		this.transacName = transacName;
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

	public Boolean getIsEdit() {
		return isEdit;
	}

	public void setIsEdit(Boolean isEdit) {
		this.isEdit = isEdit;
	}

	public Long getTransactionKey() {
		return transactionKey;
	}

	public void setTransactionKey(Long transactionKey) {
		this.transactionKey = transactionKey;
	}

	
	
}

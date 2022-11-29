package com.shaic.reimbursement.uploadrodreports;

import com.shaic.arch.table.AbstractTableDTO;

public class UploadedDocumentsDTO extends AbstractTableDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer sno;
	
	private String RODNo;
	
	private String fileType;
	
	private String fileName;
	
	private String token;
	
	private String deletedFlag;
	
	private String transacName;

	public String getRODNo() {
		return RODNo;
	}

	public void setRODNo(String rODNo) {
		RODNo = rODNo;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Integer getSno() {
		return sno;
	}

	public void setSno(Integer sno) {
		this.sno = sno;
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

}

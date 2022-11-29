package com.shaic.claim.pancard.page;

import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

public class UploadedPanCardDocumentsDTO extends AbstractTableDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer sno;
	
	private String RODNo;
	
	private String fileType;
	
	private String fileName;
	
	private String token;

	private Date uploadDate;
	
	private String uploadBy;
	
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

	public String getUploadBy() {
		return uploadBy;
	}

	public void setUploadBy(String uploadBy) {
		this.uploadBy = uploadBy;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}
	
	

}

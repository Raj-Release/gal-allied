package com.shaic.claim.lumen.upload;

import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

@SuppressWarnings("serial")
public class DocumentTableDTO extends AbstractTableDTO{

	private String fileType;
	private String fileName;
	private Date uploadedDate;
	private String uploadedBy;
	//required while deleting the document
	private String intimationNum;
	private String deletedFlag;
	//To identify doc in the list uniquely, using the sfFileName property
	private String sfFileName;

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
	public Date getUploadedDate() {
		return uploadedDate;
	}
	public void setUploadedDate(Date uploadedDate) {
		this.uploadedDate = uploadedDate;
	}
	public String getUploadedBy() {
		return uploadedBy;
	}
	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}
	public String getIntimationNum() {
		return intimationNum;
	}
	public void setIntimationNum(String intimationNum) {
		this.intimationNum = intimationNum;
	}
	public String getDeletedFlag() {
		return deletedFlag;
	}
	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
	}
	public String getSfFileName() {
		return sfFileName;
	}
	public void setSfFileName(String sfFileName) {
		this.sfFileName = sfFileName;
	}
}

package com.shaic.claim.lumen.querytomis;

import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.domain.LumenQuery;
import com.shaic.domain.LumenRequest;

@SuppressWarnings("serial")
public class DocumentAckTableDTO extends AbstractTableDTO{

	private String fileType;
	private String fileName;
	private Date uploadedDate;
	private String uploadedBy;
	//required while deleting the document
	private String intimationNum;
	private String deletedFlag;
	//To identify doc in the list uniquely, using the sfFileName property
	private String sfFileName;
	
	private Long documentToken;
	
	private LumenRequest lumenRequest;
	private LumenQuery lumenQuery;
	
	private String queryRemarks;
	private String replyRemarks;
	
	private Long queryDetailsKey;

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
	public Long getDocumentToken() {
		return documentToken;
	}
	public void setDocumentToken(Long documentToken) {
		this.documentToken = documentToken;
	}
	public LumenRequest getLumenRequest() {
		return lumenRequest;
	}
	public void setLumenRequest(LumenRequest lumenRequest) {
		this.lumenRequest = lumenRequest;
	}
	public LumenQuery getLumenQuery() {
		return lumenQuery;
	}
	public void setLumenQuery(LumenQuery lumenQuery) {
		this.lumenQuery = lumenQuery;
	}
	public String getQueryRemarks() {
		return queryRemarks;
	}
	public void setQueryRemarks(String queryRemarks) {
		this.queryRemarks = queryRemarks;
	}
	public String getReplyRemarks() {
		return replyRemarks;
	}
	public void setReplyRemarks(String replyRemarks) {
		this.replyRemarks = replyRemarks;
	}
	public Long getQueryDetailsKey() {
		return queryDetailsKey;
	}
	public void setQueryDetailsKey(Long queryDetailsKey) {
		this.queryDetailsKey = queryDetailsKey;
	}
}

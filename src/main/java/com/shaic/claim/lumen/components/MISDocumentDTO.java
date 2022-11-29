package com.shaic.claim.lumen.components;

import java.io.Serializable;
import java.util.Date;

import com.shaic.domain.LumenQuery;
import com.shaic.domain.LumenQueryDetails;
import com.shaic.domain.LumenQueryDocument;
import com.shaic.domain.LumenRequest;

@SuppressWarnings("serial")
public class MISDocumentDTO implements Serializable{
	
	private String uploadedFileType;
	private String uploadedFileName;
	private Date uploadedDate;
	private String uploadedBy;
	private LumenRequest lumenRequest;
	private LumenQuery lumenQuery;
	private LumenQueryDetails lumenQueryDetails;
	
	private String queryRemarks;
	private String replyRemarks;
	
	public String getUploadedFileType() {
		return uploadedFileType;
	}
	public void setUploadedFileType(String uploadedFileType) {
		this.uploadedFileType = uploadedFileType;
	}
	public String getUploadedFileName() {
		return uploadedFileName;
	}
	public void setUploadedFileName(String uploadedFileName) {
		this.uploadedFileName = uploadedFileName;
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
	public LumenQueryDetails getLumenQueryDetails() {
		return lumenQueryDetails;
	}
	public void setLumenQueryDetails(LumenQueryDetails lumenQueryDetails) {
		this.lumenQueryDetails = lumenQueryDetails;
	}
	
}

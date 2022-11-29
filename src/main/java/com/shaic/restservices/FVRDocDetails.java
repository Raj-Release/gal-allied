package com.shaic.restservices;

import java.util.List;

public class FVRDocDetails {
	
	private String refId;
	private String intimationNumber;
	private String fvrNo;
	private String fvrReplyDate;
	private String uploadDate;
	private String fileTypeId;
	private List<FVRDocUploadDetails> documentDetails;
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
	public String getIntimationNumber() {
		return intimationNumber;
	}
	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}
	public String getFvrNo() {
		return fvrNo;
	}
	public void setFvrNo(String fvrNo) {
		this.fvrNo = fvrNo;
	}
	public String getFvrReplyDate() {
		return fvrReplyDate;
	}
	public void setFvrReplyDate(String fvrReplyDate) {
		this.fvrReplyDate = fvrReplyDate;
	}
	public String getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(String uploadDate) {
		this.uploadDate = uploadDate;
	}
	public String getFileTypeId() {
		return fileTypeId;
	}
	public void setFileTypeId(String fileTypeId) {
		this.fileTypeId = fileTypeId;
	}
	public List<FVRDocUploadDetails> getDocumentDetails() {
		return documentDetails;
	}
	public void setDocumentDetails(List<FVRDocUploadDetails> documentDetails) {
		this.documentDetails = documentDetails;
	}

	
}

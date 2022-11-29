package com.shaic.claim.coordinator.view;

import java.io.Serializable;
import java.util.List;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.fileUpload.MultipleUploadDocumentDTO;

public class ViewCoOrdinatorDTO extends AbstractTableDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long key;

	private String requestedDate;
	
	private String repliedDate;
	
	private String requestType;
	
	private String requestorRole;
	
	private String requestroNameId;
	
	private String requestorRemarks;
	
	private String viewFile;
	
	private String coOrdinatorRepliedId;
	
	private String coOrdinatorRemarks;
	
	private String fileName;
	
	private List<MultipleUploadDocumentDTO> uploadedDocumentList;

	public String getRequestedDate() {
		return requestedDate;
	}

	public void setRequestedDate(String requestedDate) {
		this.requestedDate = requestedDate;
	}

	public String getRepliedDate() {
		return repliedDate;
	}

	public void setRepliedDate(String repliedDate) {
		this.repliedDate = repliedDate;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getRequestorRole() {
		return requestorRole;
	}

	public void setRequestorRole(String requestorRole) {
		this.requestorRole = requestorRole;
	}

	public String getRequestroNameId() {
		return requestroNameId;
	}

	public void setRequestroNameId(String requestroNameId) {
		this.requestroNameId = requestroNameId;
	}

	public String getRequestorRemarks() {
		return requestorRemarks;
	}

	public void setRequestorRemarks(String requestorRemarks) {
		this.requestorRemarks = requestorRemarks;
	}

	public String getViewFile() {
		return viewFile;
	}

	public void setViewFile(String viewFile) {
		this.viewFile = viewFile;
	}

	public String getCoOrdinatorRepliedId() {
		return coOrdinatorRepliedId;
	}

	public void setCoOrdinatorRepliedId(String coOrdinatorRepliedId) {
		this.coOrdinatorRepliedId = coOrdinatorRepliedId;
	}

	public String getCoOrdinatorRemarks() {
		return coOrdinatorRemarks;
	}

	public void setCoOrdinatorRemarks(String coOrdinatorRemarks) {
		this.coOrdinatorRemarks = coOrdinatorRemarks;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public List<MultipleUploadDocumentDTO> getUploadedDocumentList() {
		return uploadedDocumentList;
	}

	public void setUploadedDocumentList(
			List<MultipleUploadDocumentDTO> uploadedDocumentList) {
		this.uploadedDocumentList = uploadedDocumentList;
	}	
	
}

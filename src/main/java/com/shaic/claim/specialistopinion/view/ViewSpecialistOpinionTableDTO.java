package com.shaic.claim.specialistopinion.view;

import java.io.Serializable;
import java.util.List;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.fileUpload.MultipleUploadDocumentDTO;

public class ViewSpecialistOpinionTableDTO extends AbstractTableDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String requestedDate;
	
	private String repliedDate;
	
	private String specialistType;
	
	private String specialistDrNameId;
	
	private String specialistDrDesignation;
	
	private String requestorRole;
	
	private String  requestorNameId;
	
	private String requestorRemarks;
	
	private String fileUpload;
	
	private String specialistRemarks;
	
	private String fileToken;
	
	private String viewFile;
	
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

	public String getSpecialistType() {
		return specialistType;
	}

	public void setSpecialistType(String specialistType) {
		this.specialistType = specialistType;
	}

	public String getSpecialistDrNameId() {
		return specialistDrNameId;
	}

	public void setSpecialistDrNameId(String specialistDrNameId) {
		this.specialistDrNameId = specialistDrNameId;
	}

	public String getSpecialistDrDesignation() {
		return specialistDrDesignation;
	}

	public void setSpecialistDrDesignation(String specialistDrDesignation) {
		this.specialistDrDesignation = specialistDrDesignation;
	}

	public String getRequestorRole() {
		return requestorRole;
	}

	public void setRequestorRole(String requestorRole) {
		this.requestorRole = requestorRole;
	}

	public String getRequestorNameId() {
		return requestorNameId;
	}

	public void setRequestorNameId(String requestorNameId) {
		this.requestorNameId = requestorNameId;
	}

	public String getRequestorRemarks() {
		return requestorRemarks;
	}

	public void setRequestorRemarks(String requestorRemarks) {
		this.requestorRemarks = requestorRemarks;
	}

	public String getFileUpload() {
		return fileUpload;
	}

	public void setFileUpload(String fileUpload) {
		this.fileUpload = fileUpload;
	}

	public String getSpecialistRemarks() {
		return specialistRemarks;
	}

	public void setSpecialistRemarks(String specialistRemarks) {
		this.specialistRemarks = specialistRemarks;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getFileToken() {
		return fileToken;
	}

	public void setFileToken(String fileToken) {
		this.fileToken = fileToken;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getViewFile() {
		return viewFile;
	}

	public void setViewFile(String viewFile) {
		this.viewFile = viewFile;
	}

	public List<MultipleUploadDocumentDTO> getUploadedDocumentList() {
		return uploadedDocumentList;
	}

	public void setUploadedDocumentList(
			List<MultipleUploadDocumentDTO> uploadedDocumentList) {
		this.uploadedDocumentList = uploadedDocumentList;
	}	

}

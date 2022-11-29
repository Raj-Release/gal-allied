package com.shaic.claim.submitSpecialist;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class SubmitSpecialistDTO extends AbstractSearchDTO{
	
	private String requestedDate;
	
	private Long key;
	
	private String viewFile;                      //need tom implements
	
	private String requestorRemarks;
	
	private String specialistRemarks;
	
	private String fileUpload;
	
	private String fileName;
	
	private String fileToken;
	
	private String tmpFileName;
	
	private String tmpFileToken;
	
	private Long stageKey;
	
	private BeanItemContainer<SelectValue> specialistType = new BeanItemContainer<SelectValue>(SelectValue.class);

	public String getRequestedDate() {
		return requestedDate;
	}

	public void setRequestedDate(String requestedDate) {
		this.requestedDate = requestedDate;
	}

	public String getViewFile() {
		return viewFile;
	}

	public void setViewFile(String viewFile) {
		this.viewFile = viewFile;
	}

	public String getRequestorRemarks() {
		return requestorRemarks;
	}

	public void setRequestorRemarks(String requestorRemarks) {
		this.requestorRemarks = requestorRemarks;
	}

	public String getSpecialistRemarks() {
		return specialistRemarks;
	}

	public void setSpecialistRemarks(String specialistRemarks) {
		this.specialistRemarks = specialistRemarks;
	}

	public String getFileUpload() {
		return fileUpload;
	}

	public void setFileUpload(String fileUpload) {
		this.fileUpload = fileUpload;
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

	public String getFileToken() {
		return fileToken;
	}

	public void setFileToken(String fileToken) {
		this.fileToken = fileToken;
	}

	public String getTmpFileName() {
		return tmpFileName;
	}

	public void setTmpFileName(String tmpFileName) {
		this.tmpFileName = tmpFileName;
	}

	public String getTmpFileToken() {
		return tmpFileToken;
	}

	public void setTmpFileToken(String tmpFileToken) {
		this.tmpFileToken = tmpFileToken;
	}

	public BeanItemContainer<SelectValue> getSpecialistType() {
		return specialistType;
	}

	public void setSpecialistType(BeanItemContainer<SelectValue> specialistType) {
		this.specialistType = specialistType;
	}

	public Long getStageKey() {
		return stageKey;
	}

	public void setStageKey(Long stageKey) {
		this.stageKey = stageKey;
	}
	
}

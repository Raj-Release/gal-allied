package com.shaic.reimbursement.manageclaim.closeclaim.pageClaimLevel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.enhacement.table.PreviousPreAuthTableDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;

public class CloseClaimPageDTO {
	
	 private NewIntimationDto newIntimationDto;
	    
	 private ClaimDto claimDto;
	 
	 private String diagnosis;
	 
	 private String fileName;
	 
	 private String fileToken;
	 
	 private String fileType;
	 
	 private String documentSource;
	 
	 private String userName;
	 
	 private String intimationNumber;
	 
	 private String reimbursmentNumber;
	 
	 private String cashlessNumber;
	 
	 private String claimNumber;
	 
	 private String referenceNo;
	 
	 private SelectValue reasonId;
	 
	 private String closeRemarks;
	 
	 private Date closeDate;

	 
	 private Long claimKey;
	 
	 private Double closedProvisionAmt;
	 
	 private List<PreviousPreAuthTableDTO> previousPreauthDetailsList;
	 
	 private List<ViewDocumentDetailsDTO> rodDocumentDetailsList;
	 
	 private List<UploadDocumentCloseClaimDTO> uploadDocumentDetails;
	 
	 private List<CloseClaimTableDTO> closeClaimList;
	 
	 private BeanItemContainer<SelectValue> fileTypeContainer;
	 
	 
	 private BeanItemContainer<SelectValue> referenceNoContainer;
	 
	 private BeanItemContainer<SelectValue> reasonForCloseClaimContainer;
	 
	 private Date closedDate;
	 
	 public CloseClaimPageDTO(){
		 previousPreauthDetailsList = new ArrayList<PreviousPreAuthTableDTO>();
		 rodDocumentDetailsList = new ArrayList<ViewDocumentDetailsDTO>();
		 closeClaimList = new ArrayList<CloseClaimTableDTO>();
		 uploadDocumentDetails = new ArrayList<UploadDocumentCloseClaimDTO>();
	 }

	public NewIntimationDto getNewIntimationDto() {
		return newIntimationDto;
	}

	public void setNewIntimationDto(NewIntimationDto newIntimationDto) {
		this.newIntimationDto = newIntimationDto;
	}

	public ClaimDto getClaimDto() {
		return claimDto;
	}

	public void setClaimDto(ClaimDto claimDto) {
		this.claimDto = claimDto;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public List<PreviousPreAuthTableDTO> getPreviousPreauthDetailsList() {
		return previousPreauthDetailsList;
	}

	public void setPreviousPreauthDetailsList(
			List<PreviousPreAuthTableDTO> previousPreauthDetailsList) {
		this.previousPreauthDetailsList = previousPreauthDetailsList;
	}

	public List<ViewDocumentDetailsDTO> getRodDocumentDetailsList() {
		return rodDocumentDetailsList;
	}

	public void setRodDocumentDetailsList(
			List<ViewDocumentDetailsDTO> rodDocumentDetailsList) {
		this.rodDocumentDetailsList = rodDocumentDetailsList;
	}

	public List<CloseClaimTableDTO> getCloseClaimList() {
		return closeClaimList;
	}

	public void setCloseClaimList(List<CloseClaimTableDTO> closeClaimList) {
		this.closeClaimList = closeClaimList;
	}

	public BeanItemContainer<SelectValue> getFileTypeContainer() {
		return fileTypeContainer;
	}

	public void setFileTypeContainer(
			BeanItemContainer<SelectValue> fileTypeContainer) {
		this.fileTypeContainer = fileTypeContainer;
	}

	public BeanItemContainer<SelectValue> getReferenceNoContainer() {
		return referenceNoContainer;
	}

	public void setReferenceNoContainer(
			BeanItemContainer<SelectValue> referenceNoContainer) {
		this.referenceNoContainer = referenceNoContainer;
	}

	public BeanItemContainer<SelectValue> getReasonForCloseClaimContainer() {
		return reasonForCloseClaimContainer;
	}

	public void setReasonForCloseClaimContainer(
			BeanItemContainer<SelectValue> reasonForCloseClaimContainer) {
		this.reasonForCloseClaimContainer = reasonForCloseClaimContainer;
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

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getDocumentSource() {
		return documentSource;
	}

	public void setDocumentSource(String documentSource) {
		this.documentSource = documentSource;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIntimationNumber() {
		return intimationNumber;
	}

	public List<UploadDocumentCloseClaimDTO> getUploadDocumentDetails() {
		return uploadDocumentDetails;
	}

	public void setUploadDocumentDetails(
			List<UploadDocumentCloseClaimDTO> uploadDocumentDetails) {
		this.uploadDocumentDetails = uploadDocumentDetails;
	}

	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}

	public String getReimbursmentNumber() {
		return reimbursmentNumber;
	}

	public void setReimbursmentNumber(String reimbursmentNumber) {
		this.reimbursmentNumber = reimbursmentNumber;
	}

	public String getCashlessNumber() {
		return cashlessNumber;
	}

	public void setCashlessNumber(String cashlessNumber) {
		this.cashlessNumber = cashlessNumber;
	}

	public String getClaimNumber() {
		return claimNumber;
	}

	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public SelectValue getReasonId() {
		return reasonId;
	}

	public void setReasonId(SelectValue reasonId) {
		this.reasonId = reasonId;
	}

	public String getCloseRemarks() {
		return closeRemarks;
	}

	public void setCloseRemarks(String closeRemarks) {
		this.closeRemarks = closeRemarks;
	}

	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}

	public Double getClosedProvisionAmt() {
		return closedProvisionAmt;
	}

	public void setClosedProvisionAmt(Double closedProvisionAmt) {
		this.closedProvisionAmt = closedProvisionAmt;
	}

	public Date getClosedDate() {
		return closedDate;
	}

	public void setClosedDate(Date closedDate) {
		this.closedDate = closedDate;
	}

}

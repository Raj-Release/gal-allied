package com.shaic.claim.pcc.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.dto.UploadedDocumentsDTO;
import com.shaic.newcode.wizard.dto.NewIntimationDto;

public class PccDetailsTableDTO {

	private Long pccKey;
	
	private String doctorName;

	private String doctorId;

	private SelectValue pccCategory;

	private SelectValue pccSubCategory1;

	private SelectValue pccSubCategory2;

	private String escalatePccRemarks;

	private NewIntimationDto intimationDto;

	private ClaimDto claimDto;

	private List<PCCQueryDetailsTableDTO> queryDetails;

	private List<PCCReplyDetailsTableDTO> replyDetails;
	
	private List<ZonalMedicalDetailsTableDTO> zonalDetails;
		
	private SelectValue responceRole;
	
	private SelectValue responceUser;
	
	private SelectValue approveRole;
	
	private Boolean isResponceRecord = false;
	
	private Long reponceQueryKey;

	private SelectValue directresponceUser;

	private Boolean isdirectResponceRecord = false;

	private Long directreponceQueryKey;
	
	private DocumentDetailsDTO documentDetails;
	
	private List<PCCUploadDocumentsDTO> uploadedFileTableDto; 	
	
	private List<PCCUploadDocumentsDTO> uploadedNDeletedFileListDto;

	public Long getPccKey() {
		return pccKey;
	}

	public void setPccKey(Long pccKey) {
		this.pccKey = pccKey;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	public SelectValue getPccCategory() {
		return pccCategory;
	}

	public void setPccCategory(SelectValue pccCategory) {
		this.pccCategory = pccCategory;
	}

	public SelectValue getPccSubCategory1() {
		return pccSubCategory1;
	}

	public void setPccSubCategory1(SelectValue pccSubCategory1) {
		this.pccSubCategory1 = pccSubCategory1;
	}

	public SelectValue getPccSubCategory2() {
		return pccSubCategory2;
	}

	public void setPccSubCategory2(SelectValue pccSubCategory2) {
		this.pccSubCategory2 = pccSubCategory2;
	}

	public String getEscalatePccRemarks() {
		return escalatePccRemarks;
	}

	public void setEscalatePccRemarks(String escalatePccRemarks) {
		this.escalatePccRemarks = escalatePccRemarks;
	}

	public NewIntimationDto getIntimationDto() {
		return intimationDto;
	}

	public void setIntimationDto(NewIntimationDto intimationDto) {
		this.intimationDto = intimationDto;
	}

	public ClaimDto getClaimDto() {
		return claimDto;
	}

	public void setClaimDto(ClaimDto claimDto) {
		this.claimDto = claimDto;
	}

	public List<PCCQueryDetailsTableDTO> getQueryDetails() {
		return queryDetails;
	}

	public void setQueryDetails(List<PCCQueryDetailsTableDTO> queryDetails) {
		this.queryDetails = queryDetails;
	}

	public List<PCCReplyDetailsTableDTO> getReplyDetails() {
		return replyDetails;
	}

	public void setReplyDetails(List<PCCReplyDetailsTableDTO> replyDetails) {
		this.replyDetails = replyDetails;
	}

	public SelectValue getResponceRole() {
		return responceRole;
	}

	public void setResponceRole(SelectValue responceRole) {
		this.responceRole = responceRole;
	}

	public SelectValue getResponceUser() {
		return responceUser;
	}

	public void setResponceUser(SelectValue responceUser) {
		this.responceUser = responceUser;
	}

	public SelectValue getApproveRole() {
		return approveRole;
	}

	public void setApproveRole(SelectValue approveRole) {
		this.approveRole = approveRole;
	}

	public Boolean getIsResponceRecord() {
		return isResponceRecord;
	}

	public void setIsResponceRecord(Boolean isResponceRecord) {
		this.isResponceRecord = isResponceRecord;
	}

	public Long getReponceQueryKey() {
		return reponceQueryKey;
	}

	public void setReponceQueryKey(Long reponceQueryKey) {
		this.reponceQueryKey = reponceQueryKey;
	}

	public List<ZonalMedicalDetailsTableDTO> getZonalDetails() {
		return zonalDetails;
	}

	public void setZonalDetails(List<ZonalMedicalDetailsTableDTO> zonalDetails) {
		this.zonalDetails = zonalDetails;
	}

	public SelectValue getDirectresponceUser() {
		return directresponceUser;
	}

	public void setDirectresponceUser(SelectValue directresponceUser) {
		this.directresponceUser = directresponceUser;
	}

	public Boolean getIsdirectResponceRecord() {
		return isdirectResponceRecord;
	}

	public void setIsdirectResponceRecord(Boolean isdirectResponceRecord) {
		this.isdirectResponceRecord = isdirectResponceRecord;
	}

	public Long getDirectreponceQueryKey() {
		return directreponceQueryKey;
	}

	public void setDirectreponceQueryKey(Long directreponceQueryKey) {
		this.directreponceQueryKey = directreponceQueryKey;
	}

	public DocumentDetailsDTO getDocumentDetails() {
		return documentDetails;
	}

	public void setDocumentDetails(DocumentDetailsDTO documentDetails) {
		this.documentDetails = documentDetails;
	}

	public List<PCCUploadDocumentsDTO> getUploadedFileTableDto() {
		return uploadedFileTableDto;
	}

	public void setUploadedFileTableDto(
			List<PCCUploadDocumentsDTO> uploadedFileTableDto) {
		this.uploadedFileTableDto = uploadedFileTableDto;
	}

	public List<PCCUploadDocumentsDTO> getUploadedNDeletedFileListDto() {
		return uploadedNDeletedFileListDto;
	}

	public void setUploadedNDeletedFileListDto(
			List<PCCUploadDocumentsDTO> uploadedNDeletedFileListDto) {
		this.uploadedNDeletedFileListDto = uploadedNDeletedFileListDto;
	}

	
	
	/*public PccDetailsTableDTO()
	{
		documentDetails = new DocumentDetailsDTO();
		uploadDocumentsDTO = new UploadDocumentDTO();
		uploadedDocumentsDTO = new UploadedDocumentsDTO();
	}*/
	

}

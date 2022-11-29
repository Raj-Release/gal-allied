package com.shaic.claim.adviseonped;

import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;

public class AdviseOnPEDDTO {

	private com.shaic.claim.preauth.wizard.dto.OldPedEndorsementDTO oldPedEndorsementDTO;



	private SelectValue pedSuggestion;

	private String nameofPED;

	private String remarks;

	private Date repudiationLetterDate;
	
	private String queryRemarks;
	
	private SelectValue specialistType;
	
	private String uploadFile;
	
	private String reasonforReferring;
	
	private String rejectionRemarks;
	
	private String approveRemarks;
	
	private String processorRemarks;
	
	private String viewFile;
	
	private String specialistRemarks;
	
	

	public String getViewFile() {
		return viewFile;
	}

	public void setViewFile(String viewFile) {
		this.viewFile = viewFile;
	}

	public String getSpecialistRemarks() {
		return specialistRemarks;
	}

	public void setSpecialistRemarks(String specialistRemarks) {
		this.specialistRemarks = specialistRemarks;
	}

	public com.shaic.claim.preauth.wizard.dto.OldPedEndorsementDTO getOldPedEndorsementDTO() {
		return oldPedEndorsementDTO;
	}

	public void setOldPedEndorsementDTO(
			com.shaic.claim.preauth.wizard.dto.OldPedEndorsementDTO oldPedEndorsementDTO) {
		this.oldPedEndorsementDTO = oldPedEndorsementDTO;
	}

	public SelectValue getPedSuggestion() {
		return pedSuggestion;
	}

	public void setPedSuggestion(SelectValue pedSuggestion) {
		this.pedSuggestion = pedSuggestion;
	}

	public String getNameofPED() {
		return nameofPED;
	}

	public void setNameofPED(String nameofPED) {
		this.nameofPED = nameofPED;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getRepudiationLetterDate() {
		return repudiationLetterDate;
	}

	public void setRepudiationLetterDate(Date repudiationLetterDate) {
		this.repudiationLetterDate = repudiationLetterDate;
	}

	public String getQueryRemarks() {
		return queryRemarks;
	}

	public void setQueryRemarks(String queryRemarks) {
		this.queryRemarks = queryRemarks;
	}

	public SelectValue getSpecialistType() {
		return specialistType;
	}

	public void setSpecialistType(SelectValue specialistType) {
		this.specialistType = specialistType;
	}

	public String getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(String uploadFile) {
		this.uploadFile = uploadFile;
	}

	public String getReasonforReferring() {
		return reasonforReferring;
	}

	public void setReasonforReferring(String reasonforReferring) {
		this.reasonforReferring = reasonforReferring;
	}


	public String getRejectionRemarks() {
		return rejectionRemarks;
	}

	public void setRejectionRemarks(String rejectionRemarks) {
		this.rejectionRemarks = rejectionRemarks;
	}

	public String getApproveRemarks() {
		return approveRemarks;
	}

	public void setApproveRemarks(String approveRemarks) {
		this.approveRemarks = approveRemarks;
	}

	public String getProcessorRemarks() {
		return processorRemarks;
	}

	public void setProcessorRemarks(String processorRemarks) {
		this.processorRemarks = processorRemarks;
	}
	

}

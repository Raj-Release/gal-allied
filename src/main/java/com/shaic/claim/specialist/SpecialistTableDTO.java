package com.shaic.claim.specialist;

import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

public class SpecialistTableDTO extends AbstractTableDTO {

	private com.shaic.claim.preauth.wizard.dto.OldPedEndorsementDTO oldPedEndorsementDTO;



	private Integer sno;

	private String  requestorRemarks;

	private Date requestedDate;
	
	private String viewFile;
	
	private String specialistRemarks;
	
	private String uploadFile;
	

	

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

	public Integer getSno() {
		return sno;
	}

	public void setSno(Integer sno) {
		this.sno = sno;
	}

	public String getRequestorRemarks() {
		return requestorRemarks;
	}

	public void setRequestorRemarks(String requestorRemarks) {
		this.requestorRemarks = requestorRemarks;
	}

	public Date getRequestedDate() {
		return requestedDate;
	}

	public void setRequestedDate(Date requestedDate) {
		this.requestedDate = requestedDate;
	}

	public String getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(String uploadFile) {
		this.uploadFile = uploadFile;
	}

	

}

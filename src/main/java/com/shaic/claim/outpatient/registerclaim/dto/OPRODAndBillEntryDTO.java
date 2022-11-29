package com.shaic.claim.outpatient.registerclaim.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;

public class OPRODAndBillEntryDTO implements Serializable {

	private static final long serialVersionUID = 6341283362142431095L;
	
	private UploadDocumentDTO uploadDocumentDTO;
	
	private List<UploadDocumentDTO> uploadDocumentDTOList;
	
	List<OPBillDetailsDTO> billDetailsDTOList ;
	
	public OPRODAndBillEntryDTO() {
		uploadDocumentDTO = new UploadDocumentDTO();
		uploadDocumentDTOList = new ArrayList<UploadDocumentDTO>();
		this.billDetailsDTOList = new ArrayList<OPBillDetailsDTO>();
	}

	public UploadDocumentDTO getUploadDocumentDTO() {
		return uploadDocumentDTO;
	}

	public void setUploadDocumentDTO(UploadDocumentDTO uploadDocumentDTO) {
		this.uploadDocumentDTO = uploadDocumentDTO;
	}

	public List<UploadDocumentDTO> getUploadDocumentDTOList() {
		return uploadDocumentDTOList;
	}

	public void setUploadDocumentDTOList(
			List<UploadDocumentDTO> uploadDocumentDTOList) {
		this.uploadDocumentDTOList = uploadDocumentDTOList;
	}

	public List<OPBillDetailsDTO> getBillDetailsDTOList() {
		return billDetailsDTOList;
	}

	public void setBillDetailsDTOList(List<OPBillDetailsDTO> billDetailsDTOList) {
		this.billDetailsDTOList = billDetailsDTOList;
	}

}

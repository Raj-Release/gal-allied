package com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.domain.ReferenceTable;

public class TreatmentQualityVerificationDTO extends AbstractTableDTO implements Serializable{
	private static final long serialVersionUID = -5445925456818406055L;

	private Long key;
	
	private String slNo;
	
	private Long descriptionId;
	
	private String description;
	
	private String remarks;
	
	private String verifiedFlag;
	
	@NotNull(message = "Please Select Verified in Treatment Verification Table")
	private SelectValue verified;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getVerifiedFlag() {
		return verifiedFlag;
	}

	public void setVerifiedFlag(String verifiedFlag) {
		this.verifiedFlag = verifiedFlag;
		this.verified = new SelectValue();
		this.verified.setId((this.verifiedFlag != null && this.verifiedFlag.equalsIgnoreCase("Y")) ? ReferenceTable.COMMONMASTER_YES : ReferenceTable.COMMONMASTER_NO);
	}

	public SelectValue getVerified() {
		return verified;
	}

	public void setVerified(SelectValue verified) {
		this.verified = verified;
		this.verifiedFlag = this.verified != null && this.verified.getValue().toString().toLowerCase().contains("yes") ? "Y" : "N";
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getDescriptionId() {
		return descriptionId;
	}

	public void setDescriptionId(Long descriptionId) {
		this.descriptionId = descriptionId;
	}

	public String getSlNo() {
		return slNo;
	}

	public void setSlNo(Integer slNo) {
		this.slNo = ""+slNo;
	}

	
}

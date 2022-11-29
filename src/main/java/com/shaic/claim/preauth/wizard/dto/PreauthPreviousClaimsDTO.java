package com.shaic.claim.preauth.wizard.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.ReferenceTable;

public class PreauthPreviousClaimsDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -134264589764950007L;
	
	private SelectValue relapseOfIllness;
	
	private String relapseFlag;
	
	@NotNull(message = "Please Enter Illness Remarks.")
	private String relapseRemarks;
	
	private SelectValue attachToPreviousClaim;

	public SelectValue getRelapseOfIllness() {
		return relapseOfIllness;
	}

	public void setRelapseOfIllness(SelectValue relapseOfIllness) {
		this.relapseOfIllness = relapseOfIllness;
		if(relapseOfIllness != null) {
			if(relapseOfIllness.getValue() != null) {
				this.relapseFlag = relapseOfIllness.getValue().toLowerCase().contains("yes") ? "Y" : "N";
			} else if(relapseOfIllness.getId() != null) {
				this.relapseFlag = relapseOfIllness.getId() == ReferenceTable.COMMONMASTER_YES ? "Y" : "N";
				
			}
		}
		
	}

	
	public SelectValue getAttachToPreviousClaim() {
		return attachToPreviousClaim;
	}

	public void setAttachToPreviousClaim(SelectValue attachToPreviousClaim) {
		this.attachToPreviousClaim = attachToPreviousClaim;
	}

	public String getRelapseRemarks() {
		return relapseRemarks;
	}

	public void setRelapseRemarks(String relapseRemarks) {
		this.relapseRemarks = relapseRemarks;
	}

	public String getRelapseFlag() {
		return relapseFlag;
	}

	public void setRelapseFlag(String relapseFlag) {
		this.relapseFlag = relapseFlag;
		this.relapseOfIllness = new SelectValue();
		this.relapseOfIllness.setId((this.relapseFlag != null && this.relapseFlag.equalsIgnoreCase("Y")) ? ReferenceTable.COMMONMASTER_YES : ReferenceTable.COMMONMASTER_NO);
	}

}

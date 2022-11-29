package com.shaic.claim.cashlessprocess.processicac.search;

import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractRowEnablerDTO;

public class ProcessingICACTeamResponseDTO extends AbstractRowEnablerDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2142201719774943832L;

	private Long key;

	private String responseGivenBY;
	
	private String icacResponseRemarks;

	private Date repliedDate;
	
	private Boolean hasChanges = false;

	private String icacFinalResponseRemarks;
	
	private int serialNo;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Boolean getHasChanges() {
		return hasChanges;
	}

	public void setHasChanges(Boolean hasChanges) {
		this.hasChanges = hasChanges;
	}

	public int getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(int serialNo) {
		this.serialNo = serialNo;
	}

	public String getIcacResponseRemarks() {
		return icacResponseRemarks;
	}

	public void setIcacResponseRemarks(String icacResponseRemarks) {
		this.icacResponseRemarks = icacResponseRemarks;
	}

	public Date getRepliedDate() {
		return repliedDate;
	}

	public void setRepliedDate(Date repliedDate) {
		this.repliedDate = repliedDate;
	}

	public String getResponseGivenBY() {
		return responseGivenBY;
	}

	public void setResponseGivenBY(String responseGivenBY) {
		this.responseGivenBY = responseGivenBY;
	}

	public String getIcacFinalResponseRemarks() {
		return icacFinalResponseRemarks;
	}

	public void setIcacFinalResponseRemarks(String icacFinalResponseRemarks) {
		this.icacFinalResponseRemarks = icacFinalResponseRemarks;
	}
	
}

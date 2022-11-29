package com.shaic.domain.gmcautomailer;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class GmcAutomailerTableDTO extends AbstractTableDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String policyNo_branchCode;	
	private String emailId;	
	private Boolean disable = Boolean.FALSE;
	
	public String getPolicyNo_branchCode() {
		return policyNo_branchCode;
	}
	public void setPolicyNo_branchCode(String policyNo_branchCode) {
		this.policyNo_branchCode = policyNo_branchCode;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public Boolean getDisable() {
		return disable;
	}
	public void setDisable(Boolean disable) {
		this.disable = disable;
	}
	
}

package com.shaic.claim.cpuskipzmr;

import java.io.Serializable;

public class SkipZMRListenerTableDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long key ;
	
	private String userName;
	
	private String cpuCode;
	
	private String cpuName;
	
	private Boolean skipZMRForCashless;
	
	private Boolean skipZMRForReimbursement;

	public String getCpuCode() {
		return cpuCode;
	}

	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
	}

	public String getCpuName() {
		return cpuName;
	}

	public void setCpuName(String cpuName) {
		this.cpuName = cpuName;
	}

	public Boolean getSkipZMRForCashless() {
		return skipZMRForCashless;
	}

	public void setSkipZMRForCashless(Boolean skipZMRForCashless) {
		this.skipZMRForCashless = skipZMRForCashless;
	}

	public Boolean getSkipZMRForReimbursement() {
		return skipZMRForReimbursement;
	}

	public void setSkipZMRForReimbursement(Boolean skipZMRForReimbursement) {
		this.skipZMRForReimbursement = skipZMRForReimbursement;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	} 

}

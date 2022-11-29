package com.shaic.reimbursement.investigationin_direct_assigment.search;

import java.io.Serializable;

public class InvestigationDirectAssignmentTableDTO implements Serializable {
	private String policyNo;
	private Boolean enable;
	private Boolean disable;
	
	private String userName;
	
	

	public Boolean getDisable() {
		return disable;
	}

	public void setDisable(Boolean disable) {
		this.disable = disable;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	

}

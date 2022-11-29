package com.shaic.domain.gmcautomailer;

import java.io.Serializable;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;
import com.shaic.cmn.login.ImsUser;

public class GmcAutomailerFormDTO extends AbstractSearchDTO implements Serializable{
	private static final long serialVersionUID = 1L;
//	private SelectValue type;
	private String policyNo;
	private String branchCode;
	private String newPolicyNo;
	private String emailId;
	private String check;
//	private SelectValue intimationSource;
//	private SelectValue networkHospType;
	
	private ImsUser imsUser;

	
	public String getCheck() {
		return check;
	}

	public void setCheck(String check) {
		this.check = check;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public ImsUser getImsUser() {
		return imsUser;
	}

	public void setImsUser(ImsUser imsUser) {
		this.imsUser = imsUser;
	}

	public String getNewPolicyNo() {
		return newPolicyNo;
	}

	public void setNewPolicyNo(String newPolicyNo) {
		this.newPolicyNo = newPolicyNo;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
}

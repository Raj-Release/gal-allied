package com.shaic.claim.linkedPolicyDetails;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class ViewLinkedPolicyTableDTO extends AbstractTableDTO implements Serializable{
	
	private String nameOftheCorporate;
	private String policyNumber;
	private String mainMemberName;
	private String mainMemberId;
	
	public String getNameOftheCorporate() {
		return nameOftheCorporate;
	}
	public void setNameOftheCorporate(String nameOftheCorporate) {
		this.nameOftheCorporate = nameOftheCorporate;
	}
	public String getPolicyNumber() {
		return policyNumber;
	}
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
	public String getMainMemberName() {
		return mainMemberName;
	}
	public void setMainMemberName(String mainMemberName) {
		this.mainMemberName = mainMemberName;
	}
	public String getMainMemberId() {
		return mainMemberId;
	}
	public void setMainMemberId(String mainMemberId) {
		this.mainMemberId = mainMemberId;
	}

}

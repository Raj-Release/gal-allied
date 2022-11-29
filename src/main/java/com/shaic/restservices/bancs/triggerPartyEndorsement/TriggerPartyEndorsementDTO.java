package com.shaic.restservices.bancs.triggerPartyEndorsement;

import java.util.ArrayList;

public class TriggerPartyEndorsementDTO {
	 private String serviceTransactionId;
	 private String businessChannel;
	 private String userCode;
	 private String roleCode;
	 private String stakeCode;
	 private String partyCode;
	 private String typeOfEndorsement;
	 ArrayList<TriggerPartyEndorsementDetails>endorsementDetailsObject;
	 
	 
	public String getServiceTransactionId() {
		return serviceTransactionId;
	}
	public void setServiceTransactionId(String serviceTransactionId) {
		this.serviceTransactionId = serviceTransactionId;
	}
	public String getBusinessChannel() {
		return businessChannel;
	}
	public void setBusinessChannel(String businessChannel) {
		this.businessChannel = businessChannel;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getRoleCode() {
		return roleCode;
	}
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
	public String getStakeCode() {
		return stakeCode;
	}
	public void setStakeCode(String stakeCode) {
		this.stakeCode = stakeCode;
	}
	public String getPartyCode() {
		return partyCode;
	}
	public void setPartyCode(String partyCode) {
		this.partyCode = partyCode;
	}
	public String getTypeOfEndorsement() {
		return typeOfEndorsement;
	}
	public void setTypeOfEndorsement(String typeOfEndorsement) {
		this.typeOfEndorsement = typeOfEndorsement;
	}
	public ArrayList<TriggerPartyEndorsementDetails> getEndorsementDetailsObject() {
		return endorsementDetailsObject;
	}
	public void setEndorsementDetailsObject(
			ArrayList<TriggerPartyEndorsementDetails> endorsementDetailsObject) {
		this.endorsementDetailsObject = endorsementDetailsObject;
	}

	 
	 
}


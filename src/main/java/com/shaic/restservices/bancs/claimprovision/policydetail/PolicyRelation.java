package com.shaic.restservices.bancs.claimprovision.policydetail;

public class PolicyRelation {
	
	private String stakeCode;
	private String partyCode;
	private String partyName;
	private Integer partySharePercentage;
	
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
	public String getPartyName() {
		return partyName;
	}
	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}
	public Integer getPartySharePercentage() {
		return partySharePercentage;
	}
	public void setPartySharePercentage(Integer partySharePercentage) {
		this.partySharePercentage = partySharePercentage;
	}
	
}

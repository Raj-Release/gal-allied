package com.shaic.restservices.bancs.claimprovision;

import java.util.List;

import com.shaic.claim.policy.search.ui.premia.PremPolicy;

public class PolicySummaryResponse {
	
	private Integer errorCode;
	private String errorDescription;
	//List<PoilcySummary> policySummary;
	List<PremPolicy> policySummary;

	
	public Integer getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorDescription() {
		return errorDescription;
	}
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public List<PremPolicy> getPolicySummary() {
		return policySummary;
	}
	public void setPolicySummary(List<PremPolicy> policySummary) {
		this.policySummary = policySummary;
	}
	
	
}

package com.shaic.restservices.bancs.checkendomnt64vbstatus;

import java.util.List;

import com.shaic.claim.policy.search.ui.PremUnique64VBStatusDetails;

public class GetUnique64VBResponseStatus {
	private int errorCode;
	
	private String errorDescription;
	
	private List<PremUnique64VBStatusDetails> queries;


	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public List<PremUnique64VBStatusDetails> getQueries() {
		return queries;
	}

	public void setQueries(List<PremUnique64VBStatusDetails> queries) {
		this.queries = queries;
	}

}

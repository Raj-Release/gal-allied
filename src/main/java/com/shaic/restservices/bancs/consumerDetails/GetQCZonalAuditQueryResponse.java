package com.shaic.restservices.bancs.consumerDetails;

import java.util.List;

public class GetQCZonalAuditQueryResponse {

	private int errorCode;
	private String errorDescription;
	private List<Queries>queries;
	
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
	public List<Queries> getQueries() {
		return queries;
	}
	public void setQueries(List<Queries> queries) {
		this.queries = queries;
	}
	
	
}

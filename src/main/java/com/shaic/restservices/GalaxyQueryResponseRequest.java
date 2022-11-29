package com.shaic.restservices;

import java.util.List;

public class GalaxyQueryResponseRequest {
	
	private String queryResponse;
	private List<GalaxyDocuments> documents;
	private String username;
	private String password;
	private String claimNumber;
	private String queryId;
	
	public String getQueryResponse() {
		return queryResponse;
	}
	public void setQueryResponse(String queryResponse) {
		this.queryResponse = queryResponse;
	}
	public List<GalaxyDocuments> getDocuments() {
		return documents;
	}
	public void setDocuments(List<GalaxyDocuments> documents) {
		this.documents = documents;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getClaimNumber() {
		return claimNumber;
	}
	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}
	public String getQueryId() {
		return queryId;
	}
	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}
	
}

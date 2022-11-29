package com.shaic.restservices;

import java.util.List;

public class GalaxyCreateRodRequest {

	private String claimNumber;
	private List<String> classifications;
	private List<GalaxyDocuments> documents;
	private String username;
	private String password;
	
	public String getClaimNumber() {
		return claimNumber;
	}
	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}
	public List<String> getClassifications() {
		return classifications;
	}
	public void setClassifications(List<String> classifications) {
		this.classifications = classifications;
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
	
}

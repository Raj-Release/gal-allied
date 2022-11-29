package com.shaic.restservices.crm;

public class Error {
	
	private String error;

	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}

	@Override
	public String toString() {
		return "Error [error=" + error + "]";
	}
	
}

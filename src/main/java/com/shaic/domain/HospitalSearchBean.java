package com.shaic.domain;

import java.io.Serializable;

public class HospitalSearchBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	private String phoneNumber;


	public HospitalSearchBean() {
	}


	public String getPhoneNumber() {
		return phoneNumber;
	}


	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	
}
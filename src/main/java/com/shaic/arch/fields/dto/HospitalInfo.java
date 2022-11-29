package com.shaic.arch.fields.dto;

import java.io.Serializable;

public class HospitalInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6149844628869871420L;
	
	private Long key;
	
	private String hospitalCode;
	
	private String hospitalName;
	
	private String emailId;
	
	public HospitalInfo()
	{
		
	}
	
	public HospitalInfo(Long key, String value)
	{
		this.key = key;
		this.hospitalCode = value;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getHospitalCode() {
		return hospitalCode;
	}

	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}


}

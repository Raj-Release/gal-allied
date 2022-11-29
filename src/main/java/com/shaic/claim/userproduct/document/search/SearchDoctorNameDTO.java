package com.shaic.claim.userproduct.document.search;

import java.io.Serializable;

import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;


public class SearchDoctorNameDTO extends AbstractSearchDTO implements Serializable{

	/**
	 * 
	 */
	

	private String doctorName;
	
	private String empId;
	
	private String loginId;
	
	

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	
	
}

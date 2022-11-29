package com.shaic.domain;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;


public class InsuredSearchBean implements Serializable {
	private static final long serialVersionUID = 1L;

	

	private String insuredName;

	private SelectValue gender;

	private Integer age;

	private Date dateofbirth;

	private String employeeId;
	
	private String healthCardNumber;
	
	private String gmcMainMemberName;
	

	public InsuredSearchBean() {
	}


	public String getInsuredName() {
		return insuredName;
	}


	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}



	public SelectValue getGender() {
		return gender;
	}


	public void setGender(SelectValue gender) {
		this.gender = gender;
	}


	public Integer getAge() {
		return age;
	}


	public void setAge(Integer age) {
		this.age = age;
	}


	public Date getDateofbirth() {
		return dateofbirth;
	}


	public void setDateofbirth(Date dateofbirth) {
		this.dateofbirth = dateofbirth;
	}


	public String getEmployeeId() {
		return employeeId;
	}


	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}


	public String getHealthCardNumber() {
		return healthCardNumber;
	}


	public void setHealthCardNumber(String healthCardNumber) {
		this.healthCardNumber = healthCardNumber;
	}


	public String getGmcMainMemberName() {
		return gmcMainMemberName;
	}


	public void setGmcMainMemberName(String gmcMainMemberName) {
		this.gmcMainMemberName = gmcMainMemberName;
	}



}
package com.shaic.claim.registration.previousinsurance.view;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

public class PreviousInsuranceInsuredDetailsTableDTO extends AbstractTableDTO implements Serializable{
	
	private String insuredName;
	
	private String pedCode;
	
	private String sex;
	
	private Date DOB;
	
	private Double age;
	
	private String relation;
	
	private Double sumInsured;
	
	private String pedDescription;
	
	private String dateOfBirth;

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public String getPedCode() {
		return pedCode;
	}

	public void setPedCode(String pedCode) {
		this.pedCode = pedCode;
	}

	public String getPedDescription() {
		return pedDescription;
	}

	public void setPedDescription(String pedDescription) {
		this.pedDescription = pedDescription;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Date getDOB() {
		return DOB;
	}

	public void setDOB(Date dOB) {
		DOB = dOB;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public Double getSumInsured() {
		return sumInsured;
	}

	public void setSumInsured(Double sumInsured) {
		this.sumInsured = sumInsured;
	}

	public Double getAge() {
		return age;
	}

	public void setAge(Double age) {
		this.age = age;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

}

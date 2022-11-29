package com.shaic.newcode.wizard.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.shaic.arch.fields.dto.SelectValue;

public class NewBabyIntimationDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -935834660947211832L;

	private Long key;

	private Long intimationKey;

	@NotNull(message = "Please Choose Baby Relationship.")
	private SelectValue babyRelationship;
	
	@NotNull(message = "Please Enter Baby Name")
	@Pattern(regexp="^[a-zA-Z./' ]*$", message="Please Enter a valid Baby Name")
	private String babyName;

	private String officeCode;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getIntimationKey() {
		return intimationKey;
	}

	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
	}

	public SelectValue getBabyRelationship() {
		return babyRelationship;
	}

	public void setBabyRelationship(SelectValue babyRelationship) {
		this.babyRelationship = babyRelationship;
	}

	public String getBabyName() {
		return babyName;
	}

	public void setBabyName(String babyName) {
		this.babyName = babyName;
	}

	public String getOfficeCode() {
		return officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

	
	
}

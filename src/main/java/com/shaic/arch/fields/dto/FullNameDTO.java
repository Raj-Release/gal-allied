package com.shaic.arch.fields.dto;

import java.io.Serializable;

import javax.validation.constraints.Pattern;

public class FullNameDTO implements Serializable {

	private static final long serialVersionUID = 5082614677682343456L;
	
	@Pattern(regexp="^[a-zA-Z0-9./' ]*$", message="Please Enter a valid First Name")
	private String firstName;
	@Pattern(regexp="^[a-zA-Z0-9./' ]*$", message="Please Enter a valid Last Name")
	private String lastName;
	@Pattern(regexp="^[a-zA-Z0-9./' ]*$", message="Please Enter a valid Middle Name")
	private String middleName;
	private String fullName;
	
	public FullNameDTO()
	{
		firstName = "";
		lastName = "";
		middleName = "";
		fullName = "";
	}
	
	public FullNameDTO(String fName, String mName, String lName)
	{
		this.firstName = fName;
		this.lastName = lName;
		this.middleName = mName;
	}
	
	public void setFullName(String fullName)
	{
		this.fullName = fullName;
	}
	public String getFullName()
	{
		this.fullName = this.firstName + " " + this.middleName + "  " + this.lastName;
		return this.fullName;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	
	public void clearValues()
	{
		this.firstName = "";
		this.lastName = "";
		this.middleName = "";
	}
	
}

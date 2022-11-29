package com.shaic.newcode.wizard.dto;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.shaic.arch.fields.dto.FullNameDTO;
import com.shaic.arch.fields.dto.SelectValue;

public class HospitalizationDetailsDTO implements Serializable{

	private static final long serialVersionUID = -8031665175373095615L;
	
	@NotNull
	@Size(max = 64)
	@Pattern(regexp="^[a-zA-Z0-9./']*$", message="Please Enter a valid First Name")
	private String firstName;
	
	@NotNull
	@Size(max = 64)
	@Pattern(regexp="^[a-zA-Z0-9./']*$", message="Please Enter a valid Last Name")
	private String lastName;
	
	@NotNull
	@Size(max = 64)
	@Pattern(regexp="^[a-zA-Z0-9./']*$", message="Please Enter a valid Lastst Name")
	private  String middleName;
	
	private Date birthdate;
	
	@NotNull(message = "Please choose Admission Date.")
	private Date admissionDate;
	
	@NotNull(message = "Please Select Management Type")
	private SelectValue managementType;
	
	private FullNameDTO doctorName;
	
	@NotNull(message = "Please Select Room Category.")
	private SelectValue roomCategory;
	
	private String comments;
	
	private String suspicousComments;
	
	@NotNull(message = "Please Select Admission Type")
	private SelectValue admissionType;
	
	@NotNull(message = "Please Enter Inpatient Number")
	@Size(min = 1, message = "Please Enter Inpatient Number.")
	@Pattern (regexp="(^[0-9]*)$", message="Please Enter a valid Inpatient Number")
	private String inpatientNumber;
	
	@NotNull(message = "Please Enter Reason for late Intimation")
	@Size(min = 1, message = "Please Enter Reason for late Intimation.")
	private String reasonForIntimation;
	
	public HospitalizationDetailsDTO()
	{
		this.doctorName = new FullNameDTO();
	}
	public Date getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
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
	public Date getAdmissionDate() {
		return admissionDate;
	}
	public void setAdmissionDate(Date admissionDate) {
		this.admissionDate = admissionDate;
	}
	public SelectValue getManagementType() {
		return managementType;
	}
	public void setManagementType(SelectValue managementType) {
		this.managementType = managementType;
	}
	public FullNameDTO getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(FullNameDTO doctorName) {
		this.doctorName = doctorName;
	}
	public SelectValue getRoomCategory() {
		return roomCategory;
	}
	public void setRoomCategory(SelectValue roomCategory) {
		this.roomCategory = roomCategory;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getSuspicousComments() {
		return suspicousComments;
	}
	public void setSuspicousComments(String suspicousComments) {
		this.suspicousComments = suspicousComments;
	}
	public SelectValue getAdmissionType() {
		return admissionType;
	}
	public void setAdmissionType(SelectValue admissionType) {
		this.admissionType = admissionType;
	}
	public String getInpatientNumber() {
		return inpatientNumber;
	}
	public void setInpatientNumber(String inpatientNumber) {
		this.inpatientNumber = inpatientNumber;
	}
	public String getReasonForIntimation() {
		return reasonForIntimation;
	}
	public void setReasonForIntimation(String reasonForIntimation) {
		this.reasonForIntimation = reasonForIntimation;
	}

}

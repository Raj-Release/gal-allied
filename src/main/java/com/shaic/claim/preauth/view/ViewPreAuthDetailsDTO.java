package com.shaic.claim.preauth.view;

import java.io.Serializable;

public class ViewPreAuthDetailsDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String dateOfAdmission;
	private String reasonForAdmission;	
	private String preAuthRequestedAmt;
	private String roomCategory;
	private String treatmentType;
	private String noOfDays;
	private String natureOfTreatement;
	private String firstConsultationDate;
	private String replaceOfIlleness;
	private String remarks;

	public String getDateOfAdmission() {
		return dateOfAdmission;
	}

	public void setDateOfAdmission(String dateOfAdmission) {
		this.dateOfAdmission = dateOfAdmission;
	}

	public String getReasonForAdmission() {
		return reasonForAdmission;
	}

	public void setReasonForAdmission(String reasonForAdmission) {
		this.reasonForAdmission = reasonForAdmission;
	}

	public String getPreAuthRequestedAmt() {
		return preAuthRequestedAmt;
	}

	public void setPreAuthRequestedAmt(String preAuthRequestedAmt) {
		this.preAuthRequestedAmt = preAuthRequestedAmt;
	}

	public String getRoomCategory() {
		return roomCategory;
	}

	public void setRoomCategory(String roomCategory) {
		this.roomCategory = roomCategory;
	}

	public String getTreatmentType() {
		return treatmentType;
	}

	public void setTreatmentType(String treatmentType) {
		this.treatmentType = treatmentType;
	}

	public String getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(String noOfDays) {
		this.noOfDays = noOfDays;
	}

	public String getNatureOfTreatement() {
		return natureOfTreatement;
	}

	public void setNatureOfTreatement(String natureOfTreatement) {
		this.natureOfTreatement = natureOfTreatement;
	}

	public String getFirstConsultationDate() {
		return firstConsultationDate;
	}

	public void setFirstConsultationDate(String firstConsultationDate) {
		this.firstConsultationDate = firstConsultationDate;
	}

	public String getReplaceOfIlleness() {
		return replaceOfIlleness;
	}

	public void setReplaceOfIlleness(String replaceOfIlleness) {
		this.replaceOfIlleness = replaceOfIlleness;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}

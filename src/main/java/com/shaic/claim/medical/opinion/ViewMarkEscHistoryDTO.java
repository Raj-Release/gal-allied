package com.shaic.claim.medical.opinion;

import java.util.Date;

public class ViewMarkEscHistoryDTO {
	
	private Integer sNo;
	
	private String intimationNumber;

	private String dateAndTime;
	
	private String userName;
	
	private String escalatedRole;
	
	private String escalatedBy;
	
	private String escalatedDate;
	
	private String escalationReason;
	
	private String actionTaken;
	
	private String doctorRemarks;
	
	private String lackOfmrkPersonnel;

	public Integer getsNo() {
		return sNo;
	}

	public void setsNo(Integer sNo) {
		this.sNo = sNo;
	}

	public String getIntimationNumber() {
		return intimationNumber;
	}

	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}

	public String getDateAndTime() {
		return dateAndTime;
	}

	public void setDateAndTime(String dateAndTime) {
		this.dateAndTime = dateAndTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEscalatedRole() {
		return escalatedRole;
	}

	public void setEscalatedRole(String escalatedRole) {
		this.escalatedRole = escalatedRole;
	}

	public String getEscalatedBy() {
		return escalatedBy;
	}

	public void setEscalatedBy(String escalatedBy) {
		this.escalatedBy = escalatedBy;
	}

	public String getEscalatedDate() {
		return escalatedDate;
	}

	public void setEscalatedDate(String escalatedDate) {
		this.escalatedDate = escalatedDate;
	}

	public String getEscalationReason() {
		return escalationReason;
	}

	public void setEscalationReason(String escalationReason) {
		this.escalationReason = escalationReason;
	}

	public String getActionTaken() {
		return actionTaken;
	}

	public void setActionTaken(String actionTaken) {
		this.actionTaken = actionTaken;
	}

	public String getDoctorRemarks() {
		return doctorRemarks;
	}

	public void setDoctorRemarks(String doctorRemarks) {
		this.doctorRemarks = doctorRemarks;
	}

	public String getLackOfmrkPersonnel() {
		return lackOfmrkPersonnel;
	}

	public void setLackOfmrkPersonnel(String lackOfmrkPersonnel) {
		this.lackOfmrkPersonnel = lackOfmrkPersonnel;
	}
	

}

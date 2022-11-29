package com.shaic.claim.intimation.create;

import java.util.List;

public class ClaimGrievanceDTO {
	
	private String dateTime;
	private String zohoTicNo;
	private String userId;
	private String escalationSource;
	private String grievDoctorSol;
	private String drOpinion;
	private String grievDoctorSolMailTrail;
	
	private List<ClaimGrievanceDTO> grievanceDetails;
	
	private String escTicketCreationDateTime;
	private String escTicketNo;
	private String escSource;
	private String escContent;
	private String escTicketClosedDateTime;
	
	private String intimationNumber;
	private String grievanceFlag;
	private String escalationFlag;
	
	public String getDateTime() {
		return dateTime;
	}
	public String getZohoTicNo() {
		return zohoTicNo;
	}
	public String getUserId() {
		return userId;
	}
	public String getEscalationSource() {
		return escalationSource;
	}
	public String getGrievDoctorSol() {
		return grievDoctorSol;
	}
	public String getDrOpinion() {
		return drOpinion;
	}
	public String getGrievDoctorSolMailTrail() {
		return grievDoctorSolMailTrail;
	}
	
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public void setZohoTicNo(String zohoTicNo) {
		this.zohoTicNo = zohoTicNo;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setEscalationSource(String escalationSource) {
		this.escalationSource = escalationSource;
	}
	public void setGrievDoctorSol(String grievDoctorSol) {
		this.grievDoctorSol = grievDoctorSol;
	}
	public void setDrOpinion(String drOpinion) {
		this.drOpinion = drOpinion;
	}
	public void setGrievDoctorSolMailTrail(String grievDoctorSolMailTrail) {
		this.grievDoctorSolMailTrail = grievDoctorSolMailTrail;
	}
	
	
	public String toGrievanceString() {
		return "ClaimGrievanceDTO [dateTime=" + dateTime + ", zohoTicNo="
				+ zohoTicNo + ", userId=" + userId + ", escalationSource="
				+ escalationSource + ", grievDoctorSol=" + grievDoctorSol
				+ ", drOpinion=" + drOpinion + ", grievDoctorSolMailTrail="
				+ grievDoctorSolMailTrail + "]";
	}
	public String getIntimationNumber() {
		return intimationNumber;
	}
	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}
	public String getGrievanceFlag() {
		return grievanceFlag;
	}
	public void setGrievanceFlag(String grievanceFlag) {
		this.grievanceFlag = grievanceFlag;
	}
	public List<ClaimGrievanceDTO> getGrievanceDetails() {
		return grievanceDetails;
	}
	public void setGrievanceDetails(List<ClaimGrievanceDTO> grievanceDetails) {
		this.grievanceDetails = grievanceDetails;
	}
	public String getEscalationFlag() {
		return escalationFlag;
	}
	public void setEscalationFlag(String escalationFlag) {
		this.escalationFlag = escalationFlag;
	}
	public String getEscTicketCreationDateTime() {
		return escTicketCreationDateTime;
	}
	public void setEscTicketCreationDateTime(String escTicketCreationDateTime) {
		this.escTicketCreationDateTime = escTicketCreationDateTime;
	}
	public String getEscTicketNo() {
		return escTicketNo;
	}
	public void setEscTicketNo(String escTicketNo) {
		this.escTicketNo = escTicketNo;
	}
	public String getEscSource() {
		return escSource;
	}
	public void setEscSource(String escSource) {
		this.escSource = escSource;
	}
	public String getEscContent() {
		return escContent;
	}
	public void setEscContent(String escContent) {
		this.escContent = escContent;
	}
	public String getEscTicketClosedDateTime() {
		return escTicketClosedDateTime;
	}
	public void setEscTicketClosedDateTime(String escTicketClosedDateTime) {
		this.escTicketClosedDateTime = escTicketClosedDateTime;
	}
	
	public String toEscalationString() {
		return "ClaimGrievanceDTO [escTicketCreationDateTime=" + escTicketCreationDateTime + ", escTicketNo="
				+ escTicketNo + ", escSource=" + escSource + ", escContent="
				+ escContent + ", escTicketClosedDateTime=" + escTicketClosedDateTime
				+ ", grievDoctorSolMailTrail="
				+ grievDoctorSolMailTrail + "]";
	}
	
	
}
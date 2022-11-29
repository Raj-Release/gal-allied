package com.shaic.claim.intimation;

import java.io.Serializable;
import java.util.Date;

public class CashLessTableDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String cashLessType;

	private Date responseDate;

	private String replyStatus;

	private String authApprovedAmt;

	private String reason;
	
	private String doctorNote;
	
	private String cashlessStatus;
	
	private String totalApprovedAmt;

	public String getCashLessType() {
		return cashLessType;
	}

	public void setCashLessType(String cashLessType) {
		this.cashLessType = cashLessType;
	}
	
	public Date getResponseDate() {
		return responseDate;
	}

	public void setResponseDate(Date responseDate) {
		this.responseDate = responseDate;
	}

	public String getReplyStatus() {
		return replyStatus;
	}

	public void setReplyStatus(String replyStatus) {
		this.replyStatus = replyStatus;
	}

	public String getAuthApprovedAmt() {
		return authApprovedAmt;
	}

	public void setAuthApprovedAmt(String authApprovedAmt) {
		this.authApprovedAmt = authApprovedAmt;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getDoctorNote() {
		return doctorNote;
	}

	public void setDoctorNote(String doctorNote) {
		this.doctorNote = doctorNote;
	}

	public String getCashlessStatus() {
		return cashlessStatus;
	}

	public void setCashlessStatus(String cashlessStatus) {
		this.cashlessStatus = cashlessStatus;
	}

	public String getTotalApprovedAmt() {
		return totalApprovedAmt;
	}

	public void setTotalApprovedAmt(String totalApprovedAmt) {
		this.totalApprovedAmt = totalApprovedAmt;
	}

}

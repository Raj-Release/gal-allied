package com.shaic.claim.OMPSearchAndCreateintimation.wizard.pages;

import java.io.Serializable;

public class OMPAckDetailsDTO implements Serializable{
	private static final long serialVersionUID = -6397463345867570777L;
	
	private int sno;
	private String ackNo;
	private String ackCreatedDate;
	private String ackClassification;
	private String ackCategory;
	private String ackDocReceivedFrom;
	private String ackDocReceivedDate;
	private String ackCreatedName;
	private String ackStatus;
	
	public int getSno() {
		return sno;
	}
	public void setSno(int sno) {
		this.sno = sno;
	}
	public String getAckNo() {
		return ackNo;
	}
	public void setAckNo(String ackNo) {
		this.ackNo = ackNo;
	}
	public String getAckCreatedDate() {
		return ackCreatedDate;
	}
	public void setAckCreatedDate(String ackCreatedDate) {
		this.ackCreatedDate = ackCreatedDate;
	}
	public String getAckClassification() {
		return ackClassification;
	}
	public void setAckClassification(String ackClassification) {
		this.ackClassification = ackClassification;
	}
	public String getAckCategory() {
		return ackCategory;
	}
	public void setAckCategory(String ackCategory) {
		this.ackCategory = ackCategory;
	}
	public String getAckDocReceivedFrom() {
		return ackDocReceivedFrom;
	}
	public void setAckDocReceivedFrom(String ackDocReceivedFrom) {
		this.ackDocReceivedFrom = ackDocReceivedFrom;
	}
	public String getAckDocReceivedDate() {
		return ackDocReceivedDate;
	}
	public void setAckDocReceivedDate(String ackDocReceivedDate) {
		this.ackDocReceivedDate = ackDocReceivedDate;
	}
	public String getAckCreatedName() {
		return ackCreatedName;
	}
	public void setAckCreatedName(String ackCreatedName) {
		this.ackCreatedName = ackCreatedName;
	}
	public String getAckStatus() {
		return ackStatus;
	}
	public void setAckStatus(String ackStatus) {
		this.ackStatus = ackStatus;
	}
}

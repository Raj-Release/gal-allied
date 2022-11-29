package com.shaic.restservices.bancs;

public class CatastropheDataRequest {

	private String catReferenceNo;
	private String catDescription;
	private String startDate;
	private String endDate;
	
	public String getCatReferenceNo() {
		return catReferenceNo;
	}
	public void setCatReferenceNo(String catReferenceNo) {
		this.catReferenceNo = catReferenceNo;
	}
	public String getCatDescription() {
		return catDescription;
	}
	public void setCatDescription(String catDescription) {
		this.catDescription = catDescription;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	
}

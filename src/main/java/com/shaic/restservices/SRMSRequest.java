package com.shaic.restservices;


public class SRMSRequest {

	private String intimationNo;

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	@Override
	public String toString() {
		return "SRMSRequest [intimationNo=" + intimationNo + "]";
	}
}

package com.shaic.restservices.bancs.consumerDetails;

import java.util.List;

public class MultiSetDetail {
	
	private String sequenceNo;
	private List<Property> property;
	
	public String getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(String sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	public List<Property> getProperty() {
		return property;
	}
	public void setProperty(List<Property> property) {
		this.property = property;
	}

}

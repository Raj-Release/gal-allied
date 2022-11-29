package com.shaic.claim.coinsurance.view;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class CoInsuranceTableDTO  extends AbstractTableDTO implements Serializable{
	
	private static final long serialVersionUID = -2011694563880359025L;
	
	private Integer sequenceNumber;
	
	private String shareType;
	
	private String insuranceName;
	
	private Double sharePercentage;
	

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public Double getSharePercentage() {
		return sharePercentage;
	}

	public void setSharePercentage(Double sharePercentage) {
		this.sharePercentage = sharePercentage;
	}

	public String getShareType() {
		return shareType;
	}

	public void setShareType(String shareType) {
		this.shareType = shareType;
	}

	public String getInsuranceName() {
		return insuranceName;
	}

	public void setInsuranceName(String insuranceName) {
		this.insuranceName = insuranceName;
	}


	
}

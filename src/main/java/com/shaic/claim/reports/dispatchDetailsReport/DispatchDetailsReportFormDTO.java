package com.shaic.claim.reports.dispatchDetailsReport;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class DispatchDetailsReportFormDTO extends AbstractSearchDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1833643368171989036L;

	private String intimationNo;
	
	private SelectValue updateType;
	
	private String rodNumber;
	
	private String awsNumber;
	
	private String batchNumber;
	
	private Date fromDate;
	
	private Date toDate;

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public SelectValue getUpdateType() {
		return updateType;
	}

	public void setUpdateType(SelectValue updateType) {
		this.updateType = updateType;
	}

	public String getRodNumber() {
		return rodNumber;
	}

	public void setRodNumber(String rodNumber) {
		this.rodNumber = rodNumber;
	}

	public String getAwsNumber() {
		return awsNumber;
	}

	public void setAwsNumber(String awsNumber) {
		this.awsNumber = awsNumber;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	
}

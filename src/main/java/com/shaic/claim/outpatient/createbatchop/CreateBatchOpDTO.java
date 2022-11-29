package com.shaic.claim.outpatient.createbatchop;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;
import com.shaic.domain.ReferenceTable;

public class CreateBatchOpDTO extends AbstractSearchDTO implements Serializable{
	private Date fromDate = new Date();
	private Date toDate = new Date();
	private SelectValue paymentStatus = new SelectValue(ReferenceTable.PAYMENT_STATUS_FRESH, "Fresh");
	private String intimationNo;
	private String claimNo;
	private String batchNo;
	private SelectValue paymentMode = null;
	private SelectValue pioCode;
	
	private String policyNumber;
	
	private String healthCardNo;
	
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
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
	
	public SelectValue getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(SelectValue paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	
	public String getClaimNo() {
		return claimNo;
	}
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}
	public String getPolicyNumber() {
		return policyNumber;
	}
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
	public String getHealthCardNo() {
		return healthCardNo;
	}
	public void setHealthCardNo(String healthCardNo) {
		this.healthCardNo = healthCardNo;
	}
	public SelectValue getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(SelectValue paymentMode) {
		this.paymentMode = paymentMode;
	}
	public SelectValue getPioCode() {
		return pioCode;
	}
	public void setPioCode(SelectValue pioCode) {
		this.pioCode = pioCode;
	}

	
}

package com.shaic.claim.reimbursement.paymentprocesscpu;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class PaymentProcessCpuFormDTO extends AbstractSearchDTO implements Serializable{
	
	private String intimationNo;
	private String intimationSeqNo;
	private Date fromDate;
	private String fromDateValue;
	private Date toDate;
	private String toDateValue;
	private SelectValue year;
	private String yearValue;
	private SelectValue cpu;
	private SelectValue cpuLotNo;
	private String claimNumber;
	private SelectValue status;	
	private SelectValue branch;

	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public String getIntimationSeqNo() {
		return intimationSeqNo;
	}
	public void setIntimationSeqNo(String intimationSeqNo) {
		this.intimationSeqNo = intimationSeqNo;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public String getFromDateValue() {
		return fromDateValue;
	}
	public void setFromDateValue(String fromDateValue) {
		this.fromDateValue = fromDateValue;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public String getToDateValue() {
		return toDateValue;
	}
	public void setToDateValue(String toDateValue) {
		this.toDateValue = toDateValue;
	}
	public SelectValue getYear() {
		return year;
	}
	public void setYear(SelectValue year) {
		this.year = year;
	}
	public String getYearValue() {
		return yearValue;
	}
	public void setYearValue(String yearValue) {
		this.yearValue = yearValue;
	}
	
	public SelectValue getCpu() {
		return cpu;
	}
	public void setCpu(SelectValue cpu) {
		this.cpu = cpu;
	}
	public SelectValue getCpuLotNo() {
		return cpuLotNo;
	}
	public void setCpuLotNo(SelectValue cpuLotNo) {
		this.cpuLotNo = cpuLotNo;
	}
	public String getClaimNumber() {
		return claimNumber;
	}
	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}
	public SelectValue getStatus() {
		return status;
	}
	public void setStatus(SelectValue status) {
		this.status = status;
	}
	public SelectValue getBranch() {
		return branch;
	}
	public void setBranch(SelectValue branch) {
		this.branch = branch;
	}
	
}

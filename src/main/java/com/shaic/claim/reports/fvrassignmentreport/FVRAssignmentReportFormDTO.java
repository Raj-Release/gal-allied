package com.shaic.claim.reports.fvrassignmentreport;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class FVRAssignmentReportFormDTO extends AbstractSearchDTO implements Serializable {
	private Date fromDate;
	private Date toDate;
	private SelectValue cpuCode;
	private SelectValue reportType;
	private SelectValue claimType;
	private SelectValue fvrCpuCode;
	
	public SelectValue getFvrCpuCode() {
		return fvrCpuCode;
	}
	public void setFvrCpuCode(SelectValue fvrCpuCode) {
		this.fvrCpuCode = fvrCpuCode;
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
	public SelectValue getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(SelectValue cpuCode) {
		this.cpuCode = cpuCode;
	}
	public SelectValue getReportType() {
		return reportType;
	}
	public void setReportType(SelectValue reportType) {
		this.reportType = reportType;
	}
	public SelectValue getClaimType() {
		return claimType;
	}
	public void setClaimType(SelectValue claimType) {
		this.claimType = claimType;
	}
	
	
}

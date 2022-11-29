package com.shaic.claim.reports.ExecutiveStatusReport;

import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;

public class EmpSearchDto {
	
	private Date fromDate;
	private Date toDate;
	private SelectValue empName;
	private SelectValue empType;
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
	public SelectValue getEmpName() {
		return empName;
	}
	public void setEmpName(SelectValue empName) {
		this.empName = empName;
	}
	public SelectValue getEmpType() {
		return empType;
	}
	public void setEmpType(SelectValue empType) {
		this.empType = empType;
	}
	
	
	
	
}

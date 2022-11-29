package com.shaic.claim.reports.hospitalwisereport;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class HospitalWiseReportFormDTO extends AbstractSearchDTO implements Serializable{
	private Date fromDate;
	private Date toDate;
	private SelectValue dateType;
	private String hospitalCode;
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
	public SelectValue getDateType() {
		return dateType;
	}
	public void setDateType(SelectValue dateType) {
		this.dateType = dateType;
	}
	public String getHospitalCode() {
		return hospitalCode;
	}
	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}
	

}

package com.shaic.claim.medical.opinion;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class OpinionValidationFormDTO extends AbstractSearchDTO implements Serializable {

	private static final long serialVersionUID = 1L;	
	
	private String intimationNo;
	
	private Object cpuCodeMulti;
	
	private SelectValue source;
	
	private Object doctorNameMulti;
	
	private Date activityFromDate;
	
	private Date activityToDate;
	
	private String employeeName;
	
	private Date fromDate;
	
	private Date ToDate;
	
	private Long status;
		
	public String getIntimationNo() {
		return intimationNo;
	}
	
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	
	public SelectValue getSource() {
		return source;
	}
	
	public void setSource(SelectValue source) {
		this.source = source;
	}
		
	public Date getActivityFromDate() {
		return activityFromDate;
	}

	public void setActivityFromDate(Date activityFromDate) {
		this.activityFromDate = activityFromDate;
	}

	public Date getActivityToDate() {
		return activityToDate;
	}

	public void setActivityToDate(Date activityToDate) {
		this.activityToDate = activityToDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Object getCpuCodeMulti() {
		return cpuCodeMulti;
	}

	public void setCpuCodeMulti(Object cpuCodeMulti) {
		this.cpuCodeMulti = cpuCodeMulti;
	}

	public Object getDoctorNameMulti() {
		return doctorNameMulti;
	}

	public void setDoctorNameMulti(Object doctorNameMulti) {
		this.doctorNameMulti = doctorNameMulti;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return ToDate;
	}

	public void setToDate(Date toDate) {
		ToDate = toDate;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}
	
	
}

package com.shaic.claim.reports.opinionvalidationreport;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

/**
 * @author GokulPrasath.A
 *
 */
public class OpinionValidationReportFormDTO extends AbstractSearchDTO implements Serializable {
	
//	private String role;
	
	private Object role;
	
	private String employeeName;
	
	private Date fromDate;
	
	private Date toDate;
	
	private SelectValue opinionStatus;
	
	private SelectValue validatedStatus;

	/*public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}*/

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

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public SelectValue getOpinionStatus() {
		return opinionStatus;
	}

	public void setOpinionStatus(SelectValue opinionStatus) {
		this.opinionStatus = opinionStatus;
	}

	public SelectValue getValidatedStatus() {
		return validatedStatus;
	}

	public void setValidatedStatus(SelectValue validatedStatus) {
		this.validatedStatus = validatedStatus;
	}

	public Object getRole() {
		return role;
	}

	public void setRole(Object role) {
		this.role = role;
	}
}

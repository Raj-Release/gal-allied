package com.shaic.claim.reports.opinionvalidationreport;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

/**
 * @author GokulPrasath.A
 *
 */
public class OpinionValidationReportTableDTO  extends AbstractTableDTO  implements Serializable {
	
	private String intimationNo;
	
	private String updatedBy;
	
	private Timestamp updatedDateTime;
	
	private String consultedRole;
	
	private String consultedName;
	
	private String consultedRemarks;
	
	private String validatedBy;
	
	private Timestamp validatedDateTime;
	
	private String validatedStatus;
	
	private String validatedRemarks;

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdatedDateTime() {
		return updatedDateTime;
	}

	public void setUpdatedDateTime(Timestamp updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}

	public String getConsultedRole() {
		return consultedRole;
	}

	public void setConsultedRole(String consultedRole) {
		this.consultedRole = consultedRole;
	}

	public String getConsultedName() {
		return consultedName;
	}

	public void setConsultedName(String consultedName) {
		this.consultedName = consultedName;
	}

	public String getConsultedRemarks() {
		return consultedRemarks;
	}

	public void setConsultedRemarks(String consultedRemarks) {
		this.consultedRemarks = consultedRemarks;
	}

	public String getValidatedBy() {
		return validatedBy;
	}

	public void setValidatedBy(String validatedBy) {
		this.validatedBy = validatedBy;
	}

	public Timestamp getValidatedDateTime() {
		return validatedDateTime;
	}

	public void setValidatedDateTime(Timestamp validatedDateTime) {
		this.validatedDateTime = validatedDateTime;
	}

	public String getValidatedStatus() {
		return validatedStatus;
	}

	public void setValidatedStatus(String validatedStatus) {
		this.validatedStatus = validatedStatus;
	}

	public String getValidatedRemarks() {
		return validatedRemarks;
	}

	public void setValidatedRemarks(String validatedRemarks) {
		this.validatedRemarks = validatedRemarks;
	}
	
}

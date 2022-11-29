package com.shaic.claim.preauth.procedurelist.view;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class ProcedureListTableDTO extends AbstractTableDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String procedureName;
	private String procedureCode;
	private String packageAvail;
	private String packageRate;
	private String approvedRate;
	private String reference;
	private String newProceudreName;
	private String specialityValue;
	
	public String getProcedureName() {
		return procedureName;
	}

	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}

	public String getProcedureCode() {
		return procedureCode;
	}

	public void setProcedureCode(String procedureCode) {
		this.procedureCode = procedureCode;
	}

	public String getPackageAvail() {
		return packageAvail;
	}

	public void setPackageAvail(String packageAvail) {
		this.packageAvail = packageAvail;
	}

	public String getPackageRate() {
		return packageRate;
	}

	public void setPackageRate(String packageRate) {
		this.packageRate = packageRate;
	}

	public String getApprovedRate() {
		return approvedRate;
	}

	public void setApprovedRate(String approvedRate) {
		this.approvedRate = approvedRate;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getNewProceudreName() {
		return newProceudreName;
	}

	public void setNewProceudreName(String newProceudreName) {
		this.newProceudreName = newProceudreName;
	}

	public String getSpecialityValue() {
		return specialityValue;
	}

	public void setSpecialityValue(String specialityValue) {
		this.specialityValue = specialityValue;
	}
}

package com.shaic.claim.procedureexclusioncheck.view;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class ViewProcedureExclusionCheckDTO extends AbstractTableDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String procedure;	
	private String procedureCode;
	private String procedurePackageRate;
	private String dayCareProcedure;
	private String considerForDayCare;
	private String subLimitApplicable;
	private String subLimitName;
	private String subLimitDesc;
	private String subLimitAmt;
	private String considerForPayment;
	private String remarks;
	private String policyAgeing;
	private String subLimits;
	private String approvedAmt;
	private String exclusion;
	private String procedureAgreedPackageRate;
	private String reasonPkgRateChange;
	private String newProcedureName;	
	private String speciality;	
	
	public String getExclusion() {
		return exclusion;
	}

	public void setExclusion(String exclusion) {
		this.exclusion = exclusion;
	}

	public String getProcedure() {
		return procedure;
	}

	public void setProcedure(String procedure) {
		this.procedure = procedure;
	}

	public String getPolicyAgeing() {
		return policyAgeing;
	}

	public void setPolicyAgeing(String policyAgeing) {
		this.policyAgeing = policyAgeing;
	}

	public String getSubLimits() {
		return subLimits;
	}

	public void setSubLimits(String subLimits) {
		this.subLimits = subLimits;
	}

	public String getApprovedAmt() {
		return approvedAmt;
	}

	public void setApprovedAmt(String approvedAmt) {
		this.approvedAmt = approvedAmt;
	}

	public String getProcedureCode() {
		return procedureCode;
	}

	public void setProcedureCode(String procedureCode) {
		this.procedureCode = procedureCode;
	}

	public String getProcedurePackageRate() {
		return procedurePackageRate;
	}

	public void setProcedurePackageRate(String procedurePackageRate) {
		this.procedurePackageRate = procedurePackageRate;
	}

	public String getDayCareProcedure() {
		return dayCareProcedure;
	}

	public void setDayCareProcedure(String dayCareProcedure) {
		this.dayCareProcedure = dayCareProcedure;
	}

	public String getConsiderForDayCare() {
		return considerForDayCare;
	}

	public void setConsiderForDayCare(String considerForDayCare) {
		this.considerForDayCare = considerForDayCare;
	}

	public String getSubLimitApplicable() {
		return subLimitApplicable;
	}

	public void setSubLimitApplicable(String subLimitApplicable) {
		this.subLimitApplicable = subLimitApplicable;
	}

	public String getSubLimitName() {
		return subLimitName;
	}

	public void setSubLimitName(String subLimitName) {
		this.subLimitName = subLimitName;
	}

	public String getSubLimitDesc() {
		return subLimitDesc;
	}

	public void setSubLimitDesc(String subLimitDesc) {
		this.subLimitDesc = subLimitDesc;
	}

	public String getSubLimitAmt() {
		return subLimitAmt;
	}

	public void setSubLimitAmt(String subLimitAmt) {
		this.subLimitAmt = subLimitAmt;
	}

	public String getConsiderForPayment() {
		return considerForPayment;
	}

	public void setConsiderForPayment(String considerForPayment) {
		this.considerForPayment = considerForPayment;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getProcedureAgreedPackageRate() {
		return procedureAgreedPackageRate;
	}

	public void setProcedureAgreedPackageRate(String procedureAgreedPackageRate) {
		this.procedureAgreedPackageRate = procedureAgreedPackageRate;
	}

	public String getReasonPkgRateChange() {
		return reasonPkgRateChange;
	}

	public void setReasonPkgRateChange(String reasonPkgRateChange) {
		this.reasonPkgRateChange = reasonPkgRateChange;
	}
	
	public String getNewProcedureName() {
		return newProcedureName;
	}

	public void setNewProcedureName(String newProcedureName) {
		this.newProcedureName = newProcedureName;
	}

	public String getSpeciality() {
		return speciality;
	}

	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}
	
}

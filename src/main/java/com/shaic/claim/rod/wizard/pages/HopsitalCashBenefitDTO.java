package com.shaic.claim.rod.wizard.pages;

import java.io.Serializable;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;

public class HopsitalCashBenefitDTO extends AbstractTableDTO implements Serializable  {
	
	private static final long serialVersionUID = -3493908214693590425L;
	
	private String sNo;
	private SelectValue particulars;
	private String hospitalCashDays;
	private String hospitalCashPerDayAmt;
	private String hospitalCashTotalClaimedAmt;
	private String particularsValue;
	private String noOfDaysAllowed;
	private String disallowanceRemarks;
	
	public String getsNo() {
		return sNo;
	}
	public void setsNo(String sNo) {
		this.sNo = sNo;
	}
	public SelectValue getParticulars() {
		return particulars;
	}
	public void setParticulars(SelectValue particulars) {
		this.particulars = particulars;
	}
	
	public String getHospitalCashDays() {
		return hospitalCashDays;
	}
	public void setHospitalCashDays(String hospitalCashDays) {
		this.hospitalCashDays = hospitalCashDays;
	}
	public String getHospitalCashPerDayAmt() {
		return hospitalCashPerDayAmt;
	}
	public void setHospitalCashPerDayAmt(String hospitalCashPerDayAmt) {
		this.hospitalCashPerDayAmt = hospitalCashPerDayAmt;
	}
	public String getHospitalCashTotalClaimedAmt() {
		return hospitalCashTotalClaimedAmt;
	}
	public void setHospitalCashTotalClaimedAmt(String hospitalCashTotalClaimedAmt) {
		this.hospitalCashTotalClaimedAmt = hospitalCashTotalClaimedAmt;
	}
	public String getParticularsValue() {
		return particularsValue;
	}
	public void setParticularsValue(String particularsValue) {
		this.particularsValue = particularsValue;
	}
	public String getNoOfDaysAllowed() {
		return noOfDaysAllowed;
	}
	public void setNoOfDaysAllowed(String noOfDaysAllowed) {
		this.noOfDaysAllowed = noOfDaysAllowed;
	}
	public String getDisallowanceRemarks() {
		return disallowanceRemarks;
	}
	public void setDisallowanceRemarks(String disallowanceRemarks) {
		this.disallowanceRemarks = disallowanceRemarks;
	}

}

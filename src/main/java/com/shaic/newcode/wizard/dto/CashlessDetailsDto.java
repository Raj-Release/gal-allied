package com.shaic.newcode.wizard.dto;

public class CashlessDetailsDto {
	
	private String Ailment;
	
	private String statusOfCashless;
	
	private String totalAuthAmt;

	public String getAilment() {
		return Ailment;
	}

	public void setAilment(String ailment) {
		Ailment = ailment;
	}

	public String getStatusOfCashless() {
		return statusOfCashless;
	}

	public void setStatusOfCashless(String statusOfCashless) {
		this.statusOfCashless = statusOfCashless;
	}

	public String getTotalAuthAmt() {
		return totalAuthAmt;
	}

	public void setTotalAuthAmt(String totalAuthAmt) {
		this.totalAuthAmt = totalAuthAmt;
	}
	
}

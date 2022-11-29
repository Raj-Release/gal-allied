package com.shaic.claim.registration.balancesuminsured.view;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class OPBalanceSumInsuredTableDTO extends AbstractTableDTO implements
		Serializable {

	private String particulars;

	private double amount;

	OPBalanceSumInsuredTableDTO(String particulars, double amount) {
		this.particulars = particulars;
		this.amount = amount;
	}

	public String getParticulars() {
		return particulars;
	}

	public void setParticulars(String particulars) {
		this.particulars = particulars;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

}

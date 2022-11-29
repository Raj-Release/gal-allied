package com.shaic.claim.process64VB.wizard.pages;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class SearchProcess64DetailTableDTO extends AbstractTableDTO  implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4916652043237507041L;

	private String requestedBy;
	
	private String type;
	
	private String RemarksComplaince;
	
	private String payment;
	
	private String remarks;
	

	public String getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRemarksComplaince() {
		return RemarksComplaince;
	}

	public void setRemarksComplaince(String remarksComplaince) {
		RemarksComplaince = remarksComplaince;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}

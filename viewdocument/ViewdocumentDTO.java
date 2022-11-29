package com.shaic.claim.viewdocument;

import java.io.Serializable;

import org.vaadin.addon.cdimvp.ViewComponent;

public class ViewdocumentDTO extends ViewComponent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long particulars;

	private String mandatorydocuments;

	private String requireddocumenttype;

	private String recievedstatus;

	private Long noofdocs;

	private String remarks;

	public Long getParticulars() {
		return particulars;
	}

	public void setParticulars(Long long1) {
		this.particulars = long1;
	}

	public String getMandatorydocuments() {
		return mandatorydocuments;
	}

	public void setMandatorydocuments(String mandatorydocuments) {
		this.mandatorydocuments = mandatorydocuments;
	}

	public Long getNoofdocs() {
		return noofdocs;
	}

	public void setNoofdocs(Long long1) {
		this.noofdocs = long1;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getRecievedstatus() {
		return recievedstatus;
	}

	public void setRecievedstatus(String receivedStatusId) {

	}

	public String getRequireddocumenttype() {
		return requireddocumenttype;
	}

	public void setRequireddocumenttype(String requireddocumenttype) {
		this.requireddocumenttype = requireddocumenttype;
	}

}

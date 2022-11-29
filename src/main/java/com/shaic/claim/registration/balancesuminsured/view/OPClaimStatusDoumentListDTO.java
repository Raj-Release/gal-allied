package com.shaic.claim.registration.balancesuminsured.view;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class OPClaimStatusDoumentListDTO extends AbstractTableDTO implements
		Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String value;

	private String mandatoryDocFlag;

	private String requiredDocType;

	private String strReceivedStatus;

	private Long noOfDocuments;

	private String remarks;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getMandatoryDocFlag() {
		return mandatoryDocFlag;
	}

	public void setMandatoryDocFlag(String mandatoryDocFlag) {
		this.mandatoryDocFlag = mandatoryDocFlag;
	}

	public String getRequiredDocType() {
		return requiredDocType;
	}

	public void setRequiredDocType(String requiredDocType) {
		this.requiredDocType = requiredDocType;
	}

	public String getStrReceivedStatus() {
		return strReceivedStatus;
	}

	public void setStrReceivedStatus(String strReceivedStatus) {
		this.strReceivedStatus = strReceivedStatus;
	}

	public Long getNoOfDocuments() {
		return noOfDocuments;
	}

	public void setNoOfDocuments(Long noOfDocuments) {
		this.noOfDocuments = noOfDocuments;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}

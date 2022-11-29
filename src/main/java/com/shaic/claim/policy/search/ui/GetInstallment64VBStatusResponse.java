package com.shaic.claim.policy.search.ui;

import org.codehaus.jackson.annotate.JsonProperty;

public class GetInstallment64VBStatusResponse {
	

	@JsonProperty("CHQ_DT")
	private String chqDate;
	
	@JsonProperty("ERR_DESC")
	private String errorDesc;
	
	@JsonProperty("INSTALLMENT_DT")
	private String installmentDate;
	
	@JsonProperty("INSTALLMENT_NO")
	private String installmentNo;
	
	@JsonProperty("INSTALLMENT_STATUS")
	private String installmentStatus;
	
	@JsonProperty("INST_AMT")
	private String instAmt;
	
	@JsonProperty("INST_BANK")
	private String instBank;
	
	@JsonProperty("INST_NO")
	private String instNo;
	
	@JsonProperty("INST_STATUS")
	private String instStatus;
	
	@JsonProperty("INST_TYPE")
	private String instType;

	public String getChqDate() {
		return chqDate;
	}

	public void setChqDate(String chqDate) {
		this.chqDate = chqDate;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

	public String getInstallmentDate() {
		return installmentDate;
	}

	public void setInstallmentDate(String installmentDate) {
		this.installmentDate = installmentDate;
	}

	public String getInstallmentNo() {
		return installmentNo;
	}

	public void setInstallmentNo(String installmentNo) {
		this.installmentNo = installmentNo;
	}

	public String getInstallmentStatus() {
		return installmentStatus;
	}

	public void setInstallmentStatus(String installmentStatus) {
		this.installmentStatus = installmentStatus;
	}

	public String getInstAmt() {
		return instAmt;
	}

	public void setInstAmt(String instAmt) {
		this.instAmt = instAmt;
	}

	public String getInstBank() {
		return instBank;
	}

	public void setInstBank(String instBank) {
		this.instBank = instBank;
	}

	public String getInstNo() {
		return instNo;
	}

	public void setInstNo(String instNo) {
		this.instNo = instNo;
	}

	public String getInstStatus() {
		return instStatus;
	}

	public void setInstStatus(String instStatus) {
		this.instStatus = instStatus;
	}

	public String getInstType() {
		return instType;
	}

	public void setInstType(String instType) {
		this.instType = instType;
	}
	
	

}

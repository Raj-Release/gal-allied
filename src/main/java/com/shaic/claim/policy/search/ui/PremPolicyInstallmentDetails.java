package com.shaic.claim.policy.search.ui;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PremPolicyInstallmentDetails {

	@JsonProperty("ERR_DESC")
	private String errorDesc;
	
	@JsonProperty("INSTL_AMT")
	private String installmentAmount;
	
	@JsonProperty("INSTL_COLLN_DT")
	private String instCollectionDate;
	
	@JsonProperty("INSTL_DUE_DT")
	private String installmentDueDate;
	
	@JsonProperty("INSTL_NO")
	private String instNo;
	
	@JsonProperty("INSTL_SCROLL_DT")
	private String instScrollDate;
	
	@JsonProperty("INSTL_STATUS")
	private String instalmentStatus;
	
	@JsonProperty("TAX_AMT")
	private String taxAmount;
	
	@JsonProperty("TOT_INSTL_AMT")
	private String totalInstAmount;

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

	public String getInstallmentAmount() {
		return installmentAmount;
	}

	public void setInstallmentAmount(String installmentAmount) {
		this.installmentAmount = installmentAmount;
	}

	public String getInstCollectionDate() {
		return instCollectionDate;
	}

	public void setInstCollectionDate(String instCollectionDate) {
		this.instCollectionDate = instCollectionDate;
	}

	public String getInstallmentDueDate() {
		return installmentDueDate;
	}

	public void setInstallmentDueDate(String installmentDueDate) {
		this.installmentDueDate = installmentDueDate;
	}

	public String getInstNo() {
		return instNo;
	}

	public void setInstNo(String instNo) {
		this.instNo = instNo;
	}

	public String getInstScrollDate() {
		return instScrollDate;
	}

	public void setInstScrollDate(String instScrollDate) {
		this.instScrollDate = instScrollDate;
	}

	public String getInstalmentStatus() {
		return instalmentStatus;
	}

	public void setInstalmentStatus(String instalmentStatus) {
		this.instalmentStatus = instalmentStatus;
	}

	public String getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(String taxAmount) {
		this.taxAmount = taxAmount;
	}

	public String getTotalInstAmount() {
		return totalInstAmount;
	}

	public void setTotalInstAmount(String totalInstAmount) {
		this.totalInstAmount = totalInstAmount;
	}
	
}

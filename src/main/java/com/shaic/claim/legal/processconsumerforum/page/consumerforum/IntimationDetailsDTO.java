package com.shaic.claim.legal.processconsumerforum.page.consumerforum;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.Claim;

public class IntimationDetailsDTO implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7713313358808228874L;

	private String intimationNo;
	
	@NotNull(message="Please Enter Financial Year")
	@Size(min = 1 , message = "Please Enter Financial Year")
	private String financialYear;
	
	@NotNull(message="Please Select Repudiation")
	private SelectValue repudiation;
	
	@NotNull(message="Please Enter Provision Amount")
	private Double provisionAmt =0d;
	
	@NotNull(message="Please Select Policy number")
	@Size(min = 1 , message = "Please Select Policy number")
	private String policyNo;
	
	@NotNull(message="Please Enter Product name")
	@Size(min = 1 , message = "Please Enter Product name")
	private String productNo;
	
	@NotNull(message="Please Select Insured name")
	@Size(min = 1 , message = "Please Select Insured name")
	private String insuredName; 
	
	private Claim claimKey;

	//@NotNull(message="Please Enter Diagnosis")
	//@Size(min = 1 , message = "Please Enter Diagnosis")
	private String diagnosis;
	
	private SelectValue receivedFrom;
	
	private Date issueRejectionDate;
	
	private String remarks;
	
	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public String getFinancialYear() {
		return financialYear;
	}

	public void setFinancialYear(String financialYear) {
		this.financialYear = financialYear;
	}

	public SelectValue getRepudiation() {
		return repudiation;
	}

	public void setRepudiation(SelectValue repudiation) {
		this.repudiation = repudiation;
	}

	public Double getProvisionAmt() {
		return provisionAmt;
	}

	public void setProvisionAmt(Double provisionAmt) {
		this.provisionAmt = provisionAmt;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getProductNo() {
		return productNo;
	}

	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public Claim getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Claim claimKey) {
		this.claimKey = claimKey;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public SelectValue getReceivedFrom() {
		return receivedFrom;
	}

	public void setReceivedFrom(SelectValue receivedFrom) {
		this.receivedFrom = receivedFrom;
	}

	public Date getIssueRejectionDate() {
		return issueRejectionDate;
	}

	public void setIssueRejectionDate(Date issueRejectionDate) {
		this.issueRejectionDate = issueRejectionDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


}

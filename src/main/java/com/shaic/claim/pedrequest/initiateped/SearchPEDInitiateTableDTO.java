package com.shaic.claim.pedrequest.initiateped;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.domain.Insured;



public class SearchPEDInitiateTableDTO extends AbstractTableDTO implements Serializable {

	private static final long serialVersionUID = -6229444093245467560L;
	
	private Integer sno;

	private String policyNo;
	
	private Long policyKey;
	
	private String proposerCode;
	
	private String proposerName;
	
	private Integer noOfInsured;
	
	private String productName;
	
	private String policyIssuingOfficeName;
	
	private String periodOfInsurance;
	
	private Double premium;
	
	List< Insured> insuredList = new ArrayList<Insured>();

	public Integer getSno() {
		return sno;
	}

	public void setSno(Integer sno) {
		this.sno = sno;
	}


	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getProposerCode() {
		return proposerCode;
	}

	public void setProposerCode(String proposerCode) {
		this.proposerCode = proposerCode;
	}

	public String getProposerName() {
		return proposerName;
	}

	public void setProposerName(String proposerName) {
		this.proposerName = proposerName;
	}

	public Integer getNoOfInsured() {
		return noOfInsured;
	}

	public void setNoOfInsured(Integer noOfInsured) {
		this.noOfInsured = noOfInsured;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getPolicyIssuingOfficeName() {
		return policyIssuingOfficeName;
	}

	public void setPolicyIssuingOfficeName(String policyIssuingOfficeName) {
		this.policyIssuingOfficeName = policyIssuingOfficeName;
	}

	public String getPeriodOfInsurance() {
		return periodOfInsurance;
	}

	public void setPeriodOfInsurance(String periodOfInsurance) {
		this.periodOfInsurance = periodOfInsurance;
	}

	public Double getPremium() {
		return premium;
	}

	public void setPremium(Double premium) {
		this.premium = premium;
	}

	public Long getPolicyKey() {
		return policyKey;
	}

	public void setPolicyKey(Long policyKey) {
		this.policyKey = policyKey;
	}

	public List<Insured> getInsuredList() {
		return insuredList;
	}

	public void setInsuredList(List<Insured> insuredList) {
		this.insuredList = insuredList;
	}

	
	
	
}

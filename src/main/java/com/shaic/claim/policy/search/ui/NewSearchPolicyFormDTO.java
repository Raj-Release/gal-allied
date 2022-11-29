package com.shaic.claim.policy.search.ui;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class NewSearchPolicyFormDTO  extends AbstractSearchDTO implements Serializable  {
	
	
	private static final long serialVersionUID = 11L;
	
	//Variables for Search by policy --- starts.
	private String policyNo;
	private String policyReceiptNo;
	private String healthCardNumber;
	private SelectValue productName;
	private SelectValue productType;
	private SelectValue polhDivnCode;
	//private String registerdMobileNumber;
	private Long registerdMobileNumber;
	private SelectValue searchBy;
	private Boolean fromPremia;

	
	//Variables for Search by policy --- ends.
	
	private String polAssrName;
	private String insuredName;
	
	//private String insuredDateOfBirth;
	private Date insuredDateOfBirth;

	public String getPolNo() {
		return policyNo;
	}

	public void setPolNo(String polNo) {
		this.policyNo = polNo;
	}

	public String getPolReceiptNo() {
		return policyReceiptNo;
	}

	public void setPolReceiptNo(String polReceiptNo) {
		policyReceiptNo = polReceiptNo;
	}

	public String getHealthCardNumber() {
		return healthCardNumber;
	}

	public void setHealthCardNumber(String healthCardNumber) {
		this.healthCardNumber = healthCardNumber;
	}

	public SelectValue getPolProduct() {
		return productName;
	}

	public void setPolProduct(SelectValue polProduct) {
		this.productName = polProduct;
	}

	public SelectValue getPolType() {
		return productType;
	}

	public void setPolType(SelectValue polType) {
		this.productType = polType;
	}

	public SelectValue getPolhDivnCode() {
		return polhDivnCode;
	}

	public void setPolhDivnCode(SelectValue polhDivnCode) {
		this.polhDivnCode = polhDivnCode;
	}


	public String getPolAssrName() {
		return polAssrName;
	}

	public void setPolAssrName(String polAssrName) {
		this.polAssrName = polAssrName;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	/*public String getInsuredDateOfBirth() {
		return insuredDateOfBirth;
	}

	public void setInsuredDateOfBirth(String insuredDateOfBirth) {
		this.insuredDateOfBirth = insuredDateOfBirth;
	}
*/
	public SelectValue getSearchBy() {
		return searchBy;
	}

	public void setSearchBy(SelectValue searchBy) {
		this.searchBy = searchBy;
	}

	public Boolean getFromPremia() {
		return fromPremia;
	}

	public void setFromPremia(Boolean fromPremia) {
		this.fromPremia = fromPremia;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getPolicyReceiptNo() {
		return policyReceiptNo;
	}

	public void setPolicyReceiptNo(String policyReceiptNo) {
		this.policyReceiptNo = policyReceiptNo;
	}

	public SelectValue getProductName() {
		return productName;
	}

	public void setProductName(SelectValue productName) {
		this.productName = productName;
	}

	public SelectValue getProductType() {
		return productType;
	}

	public void setProductType(SelectValue productType) {
		this.productType = productType;
	}

	public Date getInsuredDateOfBirth() {
		return insuredDateOfBirth;
	}

	public void setInsuredDateOfBirth(Date insuredDateOfBirth) {
		this.insuredDateOfBirth = insuredDateOfBirth;
	}

	public Long getRegisterdMobileNumber() {
		return registerdMobileNumber;
	}

	public void setRegisterdMobileNumber(Long registerdMobileNumber) {
		this.registerdMobileNumber = registerdMobileNumber;
	}


	

}

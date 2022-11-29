package com.shaic.gpaclaim.unnamedriskdetails;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class SearchUnnamedRiskDetailsTableDTO extends AbstractTableDTO  implements Serializable{

	private String intimationNo;
	private String policyNo;
	private String productName;
	private String insuredName;
	private String section;
	private String category;
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	
	public String getInsuredName() {
		return insuredName;
	}
	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	
	
}

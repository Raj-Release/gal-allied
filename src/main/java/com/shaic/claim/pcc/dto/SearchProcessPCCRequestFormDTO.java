package com.shaic.claim.pcc.dto;

import java.io.Serializable;



import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class SearchProcessPCCRequestFormDTO extends AbstractSearchDTO implements
		Serializable {
	
	private String intimationNo;
	
	private SelectValue cpuCode;
	
	private SelectValue source;
	
	private SelectValue pccCatagory;
	
	private SelectValue pccQueryType;
	
	private SelectValue productName;

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public SelectValue getCpuCode() {
		return cpuCode;
	}

	public void setCpuCode(SelectValue cpuCode) {
		this.cpuCode = cpuCode;
	}

	public SelectValue getSource() {
		return source;
	}

	public void setSource(SelectValue source) {
		this.source = source;
	}

	public SelectValue getPccCatagory() {
		return pccCatagory;
	}

	public void setPccCatagory(SelectValue pccCatagory) {
		this.pccCatagory = pccCatagory;
	}

	public SelectValue getPccQueryType() {
		return pccQueryType;
	}

	public void setPccQueryType(SelectValue pccQueryType) {
		this.pccQueryType = pccQueryType;
	}

	public SelectValue getProductName() {
		return productName;
	}

	public void setProductName(SelectValue productName) {
		this.productName = productName;
	}
	
	
	
}

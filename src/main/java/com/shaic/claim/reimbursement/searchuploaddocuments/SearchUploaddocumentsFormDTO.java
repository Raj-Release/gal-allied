package com.shaic.claim.reimbursement.searchuploaddocuments;

import java.io.Serializable;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class SearchUploaddocumentsFormDTO extends AbstractSearchDTO implements Serializable{
	private String intimationNo;
	private String policyNo;
	private SelectValue type;
	private String ackNo;
	
	
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
	
	public SelectValue getType() {
		return type;
	}
	public void setType(SelectValue type) {
		this.type = type;
	}
	public String getAckNo() {
		return ackNo;
	}
	public void setAckNo(String ackNo) {
		this.ackNo = ackNo;
	}
	
	

}

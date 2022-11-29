package com.shaic.claim.OMPreceiptofdocumentsbillentry.search;

import java.io.Serializable;

import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class OMPReceiptofDocumentsAndBillEntryFormDto extends AbstractSearchDTO implements Serializable{
	
	
	private String intimationNo;
	private String policyno;
	
	public String getIntimationNo(){
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo){
		this.intimationNo = intimationNo;
	}
	public String getPolicyno(){
		return policyno;
	}
	public void setPolicyno(String policyno){
		this.policyno = policyno;
	}
}

package com.shaic.claim.OMPProcessOmpClaimProcessor.search;

import java.io.Serializable;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class OMPProcessOmpClaimProcessorFormDto extends AbstractSearchDTO implements Serializable{
	
	private String intimationNo;
//	private SelectValue type;
	private String policyno;
	private SelectValue classification;
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public String getPolicyno() {
		return policyno;
	}
	public void setPolicyno(String policyno) {
		this.policyno = policyno;
	}
	public SelectValue getClassification() {
		return classification;
	}
	public void setClassification(SelectValue classification) {
		this.classification = classification;
	}
	
	

}

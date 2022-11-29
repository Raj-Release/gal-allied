package com.shaic.claim.lumen.create;

import java.io.Serializable;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

@SuppressWarnings("serial")
public class LumenSearchFormDTO extends AbstractSearchDTO implements Serializable{

	private SelectValue type;
	private String intimationNumber;
	//private String claimNumber;
	private String policyNumber;
	private Object cpuCodeMulti;
	
	
	public SelectValue getType() {
		return type;
	}
	public void setType(SelectValue type) {
		this.type = type;
	}
	public String getIntimationNumber() {
		return intimationNumber;
	}
	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}
/*	public String getClaimNumber() {
		return claimNumber;
	}
	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}*/
	public String getPolicyNumber() {
		return policyNumber;
	}
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
	public Object getCpuCodeMulti() {
		return cpuCodeMulti;
	}
	public void setCpuCodeMulti(Object cpuCodeMulti) {
		this.cpuCodeMulti = cpuCodeMulti;
	}
}

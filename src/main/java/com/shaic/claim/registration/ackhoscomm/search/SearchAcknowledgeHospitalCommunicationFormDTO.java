package com.shaic.claim.registration.ackhoscomm.search;

import java.io.Serializable;

import com.shaic.arch.fields.dto.SelectValue;

public class SearchAcknowledgeHospitalCommunicationFormDTO extends AbstractSearchDTO implements Serializable{

	private static final long serialVersionUID = -2534535903489108481L;

	private String intimationNo;
	
	private SelectValue cpuCode;
	
	private String policyNo;

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

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	
	
	
}

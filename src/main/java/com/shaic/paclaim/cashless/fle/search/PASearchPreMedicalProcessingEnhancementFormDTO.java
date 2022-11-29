package com.shaic.paclaim.cashless.fle.search;

import java.io.Serializable;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class PASearchPreMedicalProcessingEnhancementFormDTO extends AbstractSearchDTO implements Serializable{


	private static final long serialVersionUID = 8080827772333467609L;

	private String intimationNo;
	
	private String policyNo;
	
	private SelectValue type;
	
	private SelectValue intimationSource;

	private SelectValue networkHospitalType;
	
	private SelectValue cpuCode;
	
	private SelectValue sortOrder;

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

	public SelectValue getIntimationSource() {
		return intimationSource;
	}

	public void setIntimationSource(SelectValue intimationSource) {
		this.intimationSource = intimationSource;
	}

	public SelectValue getNetworkHospitalType() {
		return networkHospitalType;
	}

	public void setNetworkHospitalType(SelectValue networkHospitalType) {
		this.networkHospitalType = networkHospitalType;
	}

	public SelectValue getCpuCode() {
		return cpuCode;
	}

	public void setCpuCode(SelectValue cpuCode) {
		this.cpuCode = cpuCode;
	}

	public SelectValue getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(SelectValue sortOrder) {
		this.sortOrder = sortOrder;
	}

	
	
}

package com.shaic.claim.reassignfieldvisit.search;

import java.io.Serializable;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class SearchReAssignFieldVisitFormDTO extends AbstractSearchDTO  implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String intimationNo;
	
	private String policyNo;
	
	private String hospitalName;
	
	private SelectValue intimationSource;
	
	private SelectValue cpuCode;
	
	private SelectValue hospitalType;
	
	private SelectValue networkHospType;
	
	private SelectValue fvrCpuCode;
	
	private SelectValue productCode;
	
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
	public SelectValue getIntimationSource() {
		return intimationSource;
	}
	public void setIntimationSource(SelectValue intimationSource) {
		this.intimationSource = intimationSource;
	}
	public SelectValue getNetworkHospType() {
		return networkHospType;
	}
	public void setNetworkHospType(SelectValue networkHospType) {
		this.networkHospType = networkHospType;
	}
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public SelectValue getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(SelectValue cpuCode) {
		this.cpuCode = cpuCode;
	}
	public SelectValue getHospitalType() {
		return hospitalType;
	}
	public void setHospitalType(SelectValue hospitalType) {
		this.hospitalType = hospitalType;
	}
	public SelectValue getFvrCpuCode() {
		return fvrCpuCode;
	}
	public void setFvrCpuCode(SelectValue fvrCpuCode) {
		this.fvrCpuCode = fvrCpuCode;
	}
	public SelectValue getProductCode() {
		return productCode;
	}
	public void setProductCode(SelectValue productCode) {
		this.productCode = productCode;
	}

}

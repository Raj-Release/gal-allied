package com.shaic.claim.premedical.search;

import java.io.Serializable;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class ProcessPreMedicalFormDTO extends AbstractSearchDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//private Long intimationNo;
	private String intimationNo;
	
	private SelectValue type;
	private String policyNo;
	private SelectValue intimationSource;
	private SelectValue networkHospType;
	private SelectValue cpuCode;
	private SelectValue sortOrder;
	
	private Boolean isCorpUser = false;
	private Boolean isCPUUser = false;
	
	private Boolean isAboveLimit = false;
	
	private SelectValue priorityNew;
	
	private Boolean priorityAll;
	
	private Boolean crm;
	
	private Boolean vip;
	
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	
/*	public Long getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(Long intimationNo) {
		this.intimationNo = intimationNo;
	}*/
	public SelectValue getType() {
		return type;
	}
	public void setType(SelectValue type) {
		this.type = type;
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
	public Boolean getIsCorpUser() {
		return isCorpUser;
	}
	public void setIsCorpUser(Boolean isCorpUser) {
		this.isCorpUser = isCorpUser;
	}
	public Boolean getIsCPUUser() {
		return isCPUUser;
	}
	public void setIsCPUUser(Boolean isCPUUser) {
		this.isCPUUser = isCPUUser;
	}
	public Boolean getIsAboveLimit() {
		return isAboveLimit;
	}
	public void setIsAboveLimit(Boolean isAboveLimit) {
		this.isAboveLimit = isAboveLimit;
	}
	public SelectValue getPriorityNew() {
		return priorityNew;
	}
	public void setPriorityNew(SelectValue priorityNew) {
		this.priorityNew = priorityNew;
	}
	public Boolean getPriorityAll() {
		return priorityAll;
	}
	public void setPriorityAll(Boolean priorityAll) {
		this.priorityAll = priorityAll;
	}
	public Boolean getCrm() {
		return crm;
	}
	public void setCrm(Boolean crm) {
		this.crm = crm;
	}
	public Boolean getVip() {
		return vip;
	}
	public void setVip(Boolean vip) {
		this.vip = vip;
	}
	
	

}

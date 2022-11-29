package com.shaic.reimbursement.processi_investigationi_initiated.search;

import java.io.Serializable;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

/**
 * @author ntv.narenj
 *
 */
public class SearchProcessInvestigationInitiatedFormDTO extends AbstractSearchDTO implements Serializable{
	
	private String intimationNo;
	private String policyNo; 

	private SelectValue cpuCode;
	private SelectValue claimType;
	private SelectValue hospitalType;
	private SelectValue networkHospType;
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

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public SelectValue getCpuCode() {
		return cpuCode;
	}

	public void setCpuCode(SelectValue cpuCode) {
		this.cpuCode = cpuCode;
	}

	public SelectValue getClaimType() {
		return claimType;
	}

	public void setClaimType(SelectValue claimType) {
		this.claimType = claimType;
	}

	public SelectValue getHospitalType() {
		return hospitalType;
	}

	public void setHospitalType(SelectValue hospitalType) {
		this.hospitalType = hospitalType;
	}

	public SelectValue getNetworkHospType() {
		return networkHospType;
	}

	public void setNetworkHospType(SelectValue networkHospType) {
		this.networkHospType = networkHospType;
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

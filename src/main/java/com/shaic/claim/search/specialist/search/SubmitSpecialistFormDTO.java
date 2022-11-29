package com.shaic.claim.search.specialist.search;

import java.io.Serializable;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class SubmitSpecialistFormDTO extends AbstractSearchDTO implements Serializable {

	private static final long serialVersionUID = 4013434192867752587L;

	private String intimationNo;

	private String policyNo;
	
	private SelectValue refferedBy;
	
	private SelectValue specialistType;
	
	private SelectValue priorityNew;
	
	private Boolean priorityAll;
	
	private Boolean crm;
	
	private Boolean vip;
	
	private SelectValue cpuCode;

	public SelectValue getRefferedBy() {
		return refferedBy;
	}

	public void setRefferedBy(SelectValue refferedBy) {
		this.refferedBy = refferedBy;
	}

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

	public SelectValue getSpecialistType() {
		return specialistType;
	}

	public void setSpecialistType(SelectValue specialistType) {
		this.specialistType = specialistType;
	}

	public SelectValue getPriorityNew() {
		return priorityNew;
	}

	public void setPriorityNew(SelectValue priorityNew) {
		this.priorityNew = priorityNew;
	}

	public SelectValue getCpuCode() {
		return cpuCode;
	}

	public void setCpuCode(SelectValue cpuCode) {
		this.cpuCode = cpuCode;
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

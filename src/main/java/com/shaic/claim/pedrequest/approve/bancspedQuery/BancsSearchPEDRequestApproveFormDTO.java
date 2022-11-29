package com.shaic.claim.pedrequest.approve.bancspedQuery;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class BancsSearchPEDRequestApproveFormDTO extends AbstractSearchDTO implements Serializable{


	private static final long serialVersionUID = -7344397735388837177L;

	private String intimationNo;
	
	private String policyNo;
	
	private SelectValue pedSuggestion;
	
	private SelectValue pedStatus;
	
	private Date fromDate;
	
	private Date toDate;
	
	private SelectValue cpuCode;
	
	private SelectValue priorityNew;
	
	private Boolean priorityAll;
	
	private Boolean crm;
	
	private Boolean vip;

	public SelectValue getPriorityNew() {
		return priorityNew;
	}

	public void setPriorityNew(SelectValue priorityNew) {
		this.priorityNew = priorityNew;
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

	public SelectValue getPedSuggestion() {
		return pedSuggestion;
	}

	public void setPedSuggestion(SelectValue pedSuggestion) {
		this.pedSuggestion = pedSuggestion;
	}

	public SelectValue getPedStatus() {
		return pedStatus;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setPedStatus(SelectValue pedStatus) {
		this.pedStatus = pedStatus;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
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

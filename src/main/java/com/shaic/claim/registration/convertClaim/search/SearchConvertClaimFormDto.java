package com.shaic.claim.registration.convertClaim.search;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class SearchConvertClaimFormDto extends AbstractSearchDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String policyNumber;
	
	private SelectValue cpuCode;
	
	private SelectValue source;
	
	private SelectValue accDeath;
	
	private SelectValue type;
	
	private String intimationNumber;	
	
	private Date registeredDate;
	
	private Long lobKey;
	
	private SelectValue priorityNew;

	public SelectValue getPriorityNew() {
		return priorityNew;
	}

	public void setPriorityNew(SelectValue priorityNew) {
		this.priorityNew = priorityNew;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public SelectValue getCpuCode() {
		return cpuCode;
	}

	public void setCpuCode(SelectValue cpuCode) {
		this.cpuCode = cpuCode;
	}

	public String getIntimationNumber() {
		return intimationNumber;
	}

	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}

	public Date getRegisteredDate() {
		return registeredDate;
	}

	public void setRegisteredDate(Date registeredDate) {
		this.registeredDate = registeredDate;
	}

	public Long getLobKey() {
		return lobKey;
	}

	public void setLobKey(Long lobKey) {
		this.lobKey = lobKey;
	}

	public SelectValue getSource() {
		return source;
	}

	public void setSource(SelectValue source) {
		this.source = source;
	}

	public SelectValue getAccDeath() {
		return accDeath;
	}

	public void setAccDeath(SelectValue accDeath) {
		this.accDeath = accDeath;
	}

	public SelectValue getType() {
		return type;
	}

	public void setType(SelectValue type) {
		this.type = type;
	}
	
	
}

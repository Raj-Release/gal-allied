package com.shaic.claim.processrejection.search;

import java.io.Serializable;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;
import com.shaic.cmn.login.ImsUser;

public class SearchProcessRejectionFormDTO extends AbstractSearchDTO implements Serializable{


	private static final long serialVersionUID = 8080827772333467609L;

	private String intimationNo;
	
	private String policyNo;
	
	private ImsUser imsUser;
	
	private SelectValue accidentDeath;
	
	private SelectValue priorityNew;

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

	public ImsUser getImsUser() {
		return imsUser;
	}

	public void setImsUser(ImsUser imsUser) {
		this.imsUser = imsUser;
	}

	public SelectValue getAccidentDeath() {
		return accidentDeath;
	}

	public void setAccidentDeath(SelectValue accidentDeath) {
		this.accidentDeath = accidentDeath;
	}

	public SelectValue getPriorityNew() {
		return priorityNew;
	}

	public void setPriorityNew(SelectValue priorityNew) {
		this.priorityNew = priorityNew;
	}
	
	
}

package com.shaic.reimbursement.manageclaim.HoldMonitorScreen;

import java.io.Serializable;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class SearchHoldMonitorScreenFormDTO extends AbstractSearchDTO implements Serializable {
	
	private String intimationNumber;
	private SelectValue user;
	private SelectValue type;
	private SelectValue cpuCode;
	private String screenName;
	
	
	public String getIntimationNumber() {
		return intimationNumber;
	}
	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}
	public SelectValue getUser() {
		return user;
	}
	public void setUser(SelectValue user) {
		this.user = user;
	}
	public SelectValue getType() {
		return type;
	}
	public void setType(SelectValue type) {
		this.type = type;
	}
	public SelectValue getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(SelectValue cpuCode) {
		this.cpuCode = cpuCode;
	}
	public String getScreenName() {
		return screenName;
	}
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	
	

}

package com.shaic.claim.reports.provisionhistory;

import java.io.Serializable;

import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class ProvisionHistoryFormDTO extends AbstractSearchDTO implements Serializable {
	private String intimationNo;
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
}

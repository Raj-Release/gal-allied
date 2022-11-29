package com.shaic.reimburement.gatewayAddAdditinalDocument.search;

import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class PhysicalDocumentReceivedMakerFormDTO extends AbstractSearchDTO{



	private String intimationNo;
	
	private String policyNo;
	
	private String accidentOrDeath;
	
	private String screenName;
	
	

	public String getAccidentOrDeath() {
		return accidentOrDeath;
	}

	public void setAccidentOrDeath(String accidentOrDeath) {
		this.accidentOrDeath = accidentOrDeath;
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

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	
	

}

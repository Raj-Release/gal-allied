package com.shaic.claim.policy.search.ui;

import java.util.Date;

public class Unique64VbstatusDto {

	private Date admissionDate;
	
	private Date policyFromDate;
	
	private Date policyToDate;

	public Date getAdmissionDate() {
		return admissionDate;
	}

	public void setAdmissionDate(Date admissionDate) {
		this.admissionDate = admissionDate;
	}

	public Date getPolicyFromDate() {
		return policyFromDate;
	}

	public void setPolicyFromDate(Date policyFromDate) {
		this.policyFromDate = policyFromDate;
	}

	public Date getPolicyToDate() {
		return policyToDate;
	}

	public void setPolicyToDate(Date policyToDate) {
		this.policyToDate = policyToDate;
	}
	
}

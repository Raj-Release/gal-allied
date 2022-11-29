package com.shaic.claim.reimbursement.billing.dto;

import java.io.Serializable;
import java.util.Date;

public class PatientCareDTO implements Serializable {

	private static final long serialVersionUID = -3493908214693590425L;

	private Long key;
	
	private Long reconsiderReimbursementBenefitsKey;
	
	private Long reimbursementBenefitsDetailsKey;
	
	//@NotNull(message = "Please Enter Patient Care Engaged From.")
	private Date engagedFrom;
	
	//@NotNull(message = "Please Enter Patient Care Engaged To.")
	private Date engagedTo;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Date getEngagedFrom() {
		return engagedFrom;
	}

	public void setEngagedFrom(Date engagedFrom) {
		this.engagedFrom = engagedFrom;
	}

	public Date getEngagedTo() {
		return engagedTo;
	}

	public void setEngagedTo(Date engagedTo) {
		this.engagedTo = engagedTo;
	}

	public Long getReconsiderReimbursementBenefitsKey() {
		return reconsiderReimbursementBenefitsKey;
	}

	public void setReconsiderReimbursementBenefitsKey(
			Long reconsiderReimbursementBenefitsKey) {
		this.reconsiderReimbursementBenefitsKey = reconsiderReimbursementBenefitsKey;
	}

	public Long getReimbursementBenefitsDetailsKey() {
		return reimbursementBenefitsDetailsKey;
	}

	public void setReimbursementBenefitsDetailsKey(
			Long reimbursementBenefitsDetailsKey) {
		this.reimbursementBenefitsDetailsKey = reimbursementBenefitsDetailsKey;
	}
}

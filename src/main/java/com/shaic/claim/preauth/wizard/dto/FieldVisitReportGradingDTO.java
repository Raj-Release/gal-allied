package com.shaic.claim.preauth.wizard.dto;

import java.util.Date;


public class FieldVisitReportGradingDTO {
	
	private Long key;

	private Boolean noneOfTheAbove;

	private String officeCode;

	private String status;
	
	private Date statusDate;

	private Boolean tarfigVerified;

	private Boolean terminatedFvPatientDischa;

	private Boolean totQuantumReduAch;

	private Boolean tracedPed;

	private Boolean triggerPointAttached;

	private Long fvrKey;
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Boolean getNoneOfTheAbove() {
		return noneOfTheAbove;
	}

	public void setNoneOfTheAbove(Boolean noneOfTheAbove) {
		this.noneOfTheAbove = noneOfTheAbove;
	}

	public String getOfficeCode() {
		return officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
	}

	public Boolean getTarfigVerified() {
		return tarfigVerified;
	}

	public void setTarfigVerified(Boolean tarfigVerified) {
		this.tarfigVerified = tarfigVerified;
	}

	public Boolean getTerminatedFvPatientDischa() {
		return terminatedFvPatientDischa;
	}

	public void setTerminatedFvPatientDischa(Boolean terminatedFvPatientDischa) {
		this.terminatedFvPatientDischa = terminatedFvPatientDischa;
	}

	public Boolean getTotQuantumReduAch() {
		return totQuantumReduAch;
	}

	public void setTotQuantumReduAch(Boolean totQuantumReduAch) {
		this.totQuantumReduAch = totQuantumReduAch;
	}

	public Boolean getTracedPed() {
		return tracedPed;
	}

	public void setTracedPed(Boolean tracedPed) {
		this.tracedPed = tracedPed;
	}

	public Boolean getTriggerPointAttached() {
		return triggerPointAttached;
	}

	public void setTriggerPointAttached(Boolean triggerPointAttached) {
		this.triggerPointAttached = triggerPointAttached;
	}

	public Long getFvrKey() {
		return fvrKey;
	}

	public void setFvr(Long fvrKey) {
		this.fvrKey = fvrKey;
	}
	
}

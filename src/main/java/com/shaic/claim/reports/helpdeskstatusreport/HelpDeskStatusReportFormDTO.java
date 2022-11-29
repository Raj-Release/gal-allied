package com.shaic.claim.reports.helpdeskstatusreport;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class HelpDeskStatusReportFormDTO extends AbstractSearchDTO implements Serializable{
	private Date fromDate;
	private Date toDate;
	private Date billRecFrom;
	private Date billRecTo;
	private SelectValue hospitalType;
	private SelectValue cpuCode;
	private SelectValue claimType;
    private Boolean health;	
	private String healthFlag;
	private Boolean pa;	
	private String paFlag;
	private Boolean tmpPolicy;	
	private String tmpPolicyFlag;
	private Boolean claimDays;	
	private String claimDaysFlag;
	private Boolean seniorCitizenClaim;	
	private String seniorCitizenClaimFlag;
	
		
	public SelectValue getClaimType() {
		return claimType;
	}
	public void setClaimType(SelectValue claimType) {
		this.claimType = claimType;
	}
	public SelectValue getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(SelectValue cpuCode) {
		this.cpuCode = cpuCode;
	}
	public Boolean getHealth() {
		return health;
	}
	public void setHealth(Boolean health) {
		this.health = health;
	}
	public String getHealthFlag() {
		return healthFlag;
	}
	public void setHealthFlag(String healthFlag) {
		this.healthFlag = healthFlag;
	}
	public Boolean getPa() {
		return pa;
	}
	public void setPa(Boolean pa) {
		this.pa = pa;
	}
	public String getPaFlag() {
		return paFlag;
	}
	public void setPaFlag(String paFlag) {
		this.paFlag = paFlag;
	}
	public Boolean getTmpPolicy() {
		return tmpPolicy;
	}
	public void setTmpPolicy(Boolean tmpPolicy) {
		this.tmpPolicy = tmpPolicy;
	}
	public String getTmpPolicyFlag() {
		return tmpPolicyFlag;
	}
	public void setTmpPolicyFlag(String tmpPolicyFlag) {
		this.tmpPolicyFlag = tmpPolicyFlag;
	}
	public Boolean getClaimDays() {
		return claimDays;
	}
	public void setClaimDays(Boolean claimDays) {
		this.claimDays = claimDays;
	}
	public String getClaimDaysFlag() {
		return claimDaysFlag;
	}
	public void setClaimDaysFlag(String claimDaysFlag) {
		this.claimDaysFlag = claimDaysFlag;
	}
	public Boolean getSeniorCitizenClaim() {
		return seniorCitizenClaim;
	}
	public void setSeniorCitizenClaim(Boolean seniorCitizenClaim) {
		this.seniorCitizenClaim = seniorCitizenClaim;
	}
	public String getSeniorCitizenClaimFlag() {
		return seniorCitizenClaimFlag;
	}
	public void setSeniorCitizenClaimFlag(String seniorCitizenClaimFlag) {
		this.seniorCitizenClaimFlag = seniorCitizenClaimFlag;
	}
	public SelectValue getHospitalType() {
		return hospitalType;
	}
	public void setHospitalType(SelectValue hospitalType) {
		this.hospitalType = hospitalType;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public Date getBillRecFrom() {
		return billRecFrom;
	}
	public void setBillRecFrom(Date billRecFrom) {
		this.billRecFrom = billRecFrom;
	}
	public Date getBillRecTo() {
		return billRecTo;
	}
	public void setBillRecTo(Date billRecTo) {
		this.billRecTo = billRecTo;
	}

}

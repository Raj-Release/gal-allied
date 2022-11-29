package com.shaic.claim.reports.tatreport;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class TATReportFormDTO extends AbstractSearchDTO implements Serializable {
	
	private Boolean pendingClaims;
	private String pendingClaimsFlg;
	
	private Boolean completedClaims;
	private SelectValue claimsQueueType;
	private SelectValue dateType;
	private SelectValue cpuCode;
	private List<Long> cpuCodeList;
	private SelectValue officeCode;
	private Date fromDate;
	private Date toDate;
	private SelectValue tatDate;
	private SelectValue claimType;
	
	public Boolean getPendingClaims() {
		return pendingClaims;
	}
	public void setPendingClaims(Boolean pendingClaims) {
		this.pendingClaims = pendingClaims;
		this.pendingClaimsFlg = this.pendingClaims != null && pendingClaims ? "Y" : "N" ;
		
	}
	public String getPendingClaimsFlg() {
		return pendingClaimsFlg;
	}
	public void setPendingClaimsFlg(String pendingClaimsFlg) {
		this.pendingClaimsFlg = pendingClaimsFlg;
		if(this.pendingClaimsFlg != null && this.pendingClaimsFlg.equalsIgnoreCase("Y")) {
			this.pendingClaims = true;
		}
	}
	public Boolean getCompletedClaims() {
		return completedClaims;
	}
	public void setCompletedClaims(Boolean completedClaims) {
		this.completedClaims = completedClaims;
	}
	public SelectValue getClaimsQueueType() {
		return claimsQueueType;
	}
	public void setClaimsQueueType(SelectValue claimsQueueType) {
		this.claimsQueueType = claimsQueueType;
	}
	public SelectValue getDateType() {
		return dateType;
	}
	public void setDateType(SelectValue dateType) {
		this.dateType = dateType;
	}
	public SelectValue getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(SelectValue cpuCode) {
		this.cpuCode = cpuCode;
	}
	public SelectValue getOfficeCode() {
		return officeCode;
	}
	public void setOfficeCode(SelectValue officeCode) {
		this.officeCode = officeCode;
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
	public SelectValue getTatDate() {
		return tatDate;
	}
	public void setTatDate(SelectValue tatDate) {
		this.tatDate = tatDate;
	}
	public List<Long> getCpuCodeList() {
		return cpuCodeList;
	}
	public void setCpuCodeList(List<Long> cpuCodeList) {
		this.cpuCodeList = cpuCodeList;
	}
	public SelectValue getClaimType() {
		return claimType;
	}
	public void setClaimType(SelectValue claimType) {
		this.claimType = claimType;
	}
	
	

}

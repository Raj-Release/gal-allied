/**
 * 
 */
package com.shaic.reimbursement.investigation.assigninvestigation.search;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

/**
 * @author ntv.narenj
 *
 */
public class SearchAssignInvestigationFormDTO extends AbstractSearchDTO implements Serializable{
	
	
	private String intimationNo;
	private SelectValue cpuCode;
	private String policyNo;

	private SelectValue claimType;	
	
	private SelectValue priorityNew;
	
	private Boolean priorityAll;
	
	private Boolean crm;
	
	private Boolean vip;
	
	private SelectValue invstigationState;
	
	private Date fromDate;
	
	private Date endDate;
	
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	
	public SelectValue getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(SelectValue cpuCode) {
		this.cpuCode = cpuCode;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	public SelectValue getClaimType() {
		return claimType;
	}
	public void setClaimType(SelectValue claimType) {
		this.claimType = claimType;
	}
	public SelectValue getPriorityNew() {
		return priorityNew;
	}
	public void setPriorityNew(SelectValue priorityNew) {
		this.priorityNew = priorityNew;
	}
	
	public SelectValue getInvstigationState() {
		return invstigationState;
	}
	public void setInvstigationState(SelectValue invstigationState) {
		this.invstigationState = invstigationState;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Boolean getPriorityAll() {
		return priorityAll;
	}
	public void setPriorityAll(Boolean priorityAll) {
		this.priorityAll = priorityAll;
	}
	public Boolean getCrm() {
		return crm;
	}
	public void setCrm(Boolean crm) {
		this.crm = crm;
	}
	public Boolean getVip() {
		return vip;
	}
	public void setVip(Boolean vip) {
		this.vip = vip;
	}
	
}

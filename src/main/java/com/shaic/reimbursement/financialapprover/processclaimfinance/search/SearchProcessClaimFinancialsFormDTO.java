/**
 * 
 */
package com.shaic.reimbursement.financialapprover.processclaimfinance.search;

import java.io.Serializable;
import java.util.List;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

/**
 * @author ntv.narenj
 *
 */
public class SearchProcessClaimFinancialsFormDTO extends AbstractSearchDTO implements Serializable{
	
	
	private String intimationNo;
	private String policyNo;
	
	private SelectValue cpuCode;
	private SelectValue claimType;
	private SpecialSelectValue productName;
	private SelectValue networkHospType;
	
	private Double claimedAmountFrom;
	
	private Double claimedAmountTo;
	
	private SelectValue accidentOrDeath;	
	
	private SelectValue priorityNew;
	
	private Boolean priorityAll;
	
	private Boolean crm;
	
	private Boolean vip;
	
	private SelectValue pccFlag;
	
	private List<String> selectedPriority;
	
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
	public SelectValue getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(SelectValue cpuCode) {
		this.cpuCode = cpuCode;
	}
	public SelectValue getClaimType() {
		return claimType;
	}
	public void setClaimType(SelectValue claimType) {
		this.claimType = claimType;
	}
	public SpecialSelectValue getProductName() {
		return productName;
	}
	public void setProductName(SpecialSelectValue productName) {
		this.productName = productName;
	}
	public Double getClaimedAmountFrom() {
		return claimedAmountFrom;
	}
	public void setClaimedAmountFrom(Double claimedAmountFrom) {
		this.claimedAmountFrom = claimedAmountFrom;
	}
	public Double getClaimedAmountTo() {
		return claimedAmountTo;
	}
	public void setClaimedAmountTo(Double claimedAmountTo) {
		this.claimedAmountTo = claimedAmountTo;
	}
	public SelectValue getAccidentOrDeath() {
		return accidentOrDeath;
	}
	public void setAccidentOrDeath(SelectValue accidentOrDeath) {
		this.accidentOrDeath = accidentOrDeath;
	}
	public SelectValue getPriorityNew() {
		return priorityNew;
	}
	public void setPriorityNew(SelectValue priorityNew) {
		this.priorityNew = priorityNew;
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
	public SelectValue getPccFlag() {
		return pccFlag;
	}
	public void setPccFlag(SelectValue pccFlag) {
		this.pccFlag = pccFlag;
	}
	public SelectValue getNetworkHospType() {
		return networkHospType;
	}
	public void setNetworkHospType(SelectValue networkHospType) {
		this.networkHospType = networkHospType;
	}
	public List<String> getSelectedPriority() {
		return selectedPriority;
	}
	public void setSelectedPriority(List<String> selectedPriority) {
		this.selectedPriority = selectedPriority;
	}
}

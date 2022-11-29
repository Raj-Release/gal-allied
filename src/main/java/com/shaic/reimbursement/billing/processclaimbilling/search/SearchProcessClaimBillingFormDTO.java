/**
 * 
 */
package com.shaic.reimbursement.billing.processclaimbilling.search;

import java.io.Serializable;
import java.util.List;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

/**
 * @author ntv.narenj
 *
 */
public class SearchProcessClaimBillingFormDTO extends AbstractSearchDTO implements Serializable{
	
	
	private String intimationNo;
	private SelectValue cpuCode;
	private String claimNo;
	private String policyNo;
	private SpecialSelectValue productNameCode;
	
	private SelectValue coverBenefits;
	
	private SelectValue claimType;
	
	private SelectValue priorityNew;
	
	private Boolean priorityAll;
	
	private Boolean crm;
	
	private Boolean vip;
	
	private List<String> selectedPriority;
	
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
	public String getClaimNo() {
		return claimNo;
	}
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	
	public SpecialSelectValue getProductNameCode() {
		return productNameCode;
	}
	public void setProductNameCode(SpecialSelectValue productNameCode) {
		this.productNameCode = productNameCode;
	}
	
	public SelectValue getCoverBenefits() {
		return coverBenefits;
	}
	public void setCoverBenefits(SelectValue coverBenefits) {
		this.coverBenefits = coverBenefits;
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
	public List<String> getSelectedPriority() {
		return selectedPriority;
	}
	public void setSelectedPriority(List<String> selectedPriority) {
		this.selectedPriority = selectedPriority;
	}

}

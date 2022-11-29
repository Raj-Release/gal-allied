/**
 * 
 */
package com.shaic.paclaim.health.reimbursement.financial.search;

import java.io.Serializable;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

/**
 * @author ntv.narenj
 *
 */
public class PAHealthSearchProcessClaimFinancialsFormDTO extends AbstractSearchDTO implements Serializable{
	
	
	private String intimationNo;
	private String policyNo;
	
	private SelectValue cpuCode;
	private SelectValue claimType;
	private SpecialSelectValue productName;
	
	private Double claimedAmountFrom;
	
	private Double claimedAmountTo;
	
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

}

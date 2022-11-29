/**
 * 
 */
package com.shaic.paclaim.health.reimbursement.billing.search;

import java.io.Serializable;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

/**
 * @author ntv.narenj
 *
 */
public class PAHealthSearchProcessClaimBillingFormDTO extends AbstractSearchDTO implements Serializable{
	
	private static final long serialVersionUID = 738250486346633232L;
	private String intimationNo;
	private SelectValue cpuCode;
	private String claimNo;
	private String policyNo;
	private SpecialSelectValue productNameCode;
	
	private SelectValue coverBenefits;
	
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
	

}

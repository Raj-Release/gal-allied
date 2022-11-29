/**
 * 
 */
package com.shaic.reimbursement.manageclaim.reopenclaim.searchRodLevel;

import java.io.Serializable;

import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

/**
 * @author ntv.narenj
 *
 */
public class SearchReOpenClaimFormDTORODLevel extends AbstractSearchDTO implements Serializable{
	
	
	private String intimationNo;
	private String claimNo;
	private String policyNo;
	public String getIntimationNo() {
		return intimationNo;
		
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
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
	

}

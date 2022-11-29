/**
 * 
 */
package com.shaic.reimbursement.investigation.ackinvestivationcompleted.search;

import java.io.Serializable;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

/**
 * @author ntv.narenj
 *
 */
public class SearchAckInvestigationCompletedFormDTO extends AbstractSearchDTO implements Serializable{
	
	
	private String intimationNo;
	private SelectValue cpuCode;
	private String claimNo;
	private SelectValue claimType;
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
	public SelectValue getClaimType() {
		return claimType;
	}
	public void setClaimType(SelectValue claimType) {
		this.claimType = claimType;
	}
	

}

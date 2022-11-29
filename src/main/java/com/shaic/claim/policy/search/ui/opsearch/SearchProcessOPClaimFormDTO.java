/**
 * 
 */
package com.shaic.claim.policy.search.ui.opsearch;

import java.io.Serializable;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

/**
 * @author SARAVANA
 *
 */
public class SearchProcessOPClaimFormDTO  extends AbstractSearchDTO implements Serializable {
	private static final long serialVersionUID = -6136871219289073777L;

	private String policyNumber;
	
	private String intimationNo;
	
	private SelectValue claimType;
	
	private String healthCardNo;
	
	private String claimNo;
	
	private SelectValue pioCode;

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public SelectValue getClaimType() {
		return claimType;
	}

	public void setClaimType(SelectValue claimType) {
		this.claimType = claimType;
	}

	public String getHealthCardNo() {
		return healthCardNo;
	}

	public void setHealthCardNo(String healthCardNo) {
		this.healthCardNo = healthCardNo;
	}

	public String getClaimNo() {
		return claimNo;
	}

	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

	public SelectValue getPioCode() {
		return pioCode;
	}

	public void setPioCode(SelectValue pioCode) {
		this.pioCode = pioCode;
	}
	
	
	
}

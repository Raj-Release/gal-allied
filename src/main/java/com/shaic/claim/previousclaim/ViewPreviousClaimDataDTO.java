package com.shaic.claim.previousclaim;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

@SuppressWarnings("serial")
public class ViewPreviousClaimDataDTO extends AbstractTableDTO implements Serializable {
	
	private String policyNo;
	
	private String policyYear;
	
	private String claimNo;
	
	private String claimStatus;
	
	private String claimedAmount;
	
	private String approvedAmount;
	
	public String getPolicyno() {
	return policyNo;
	}

	public void setPolicyno(String policyno) {
		this.policyNo = policyno;
	}

	public String getPolicyyear() {
		return policyYear;
	}

	public void setPolicyyear(String policyyear) {
		this.policyYear = policyyear;
	}

	public String getClaimno() {
		return claimNo;
	}

	public void setClaimno(String claimno) {
		this.claimNo = claimno;
	}

	
	public String getApprovedamount() {
		return approvedAmount;
	}

	public void setApprovedamount(String approvedamount) {
		this.approvedAmount = approvedamount;
	}

	public String getClaimstatus() {
		return claimStatus;
	}

	public void setClaimstatus(String claimstatus) {
		this.claimStatus = claimstatus;
	}

	public String getClaimedAmount() {
		return claimedAmount;
	}

	public void setClaimedAmount(String claimedAmount) {
		this.claimedAmount = claimedAmount;
	}

}

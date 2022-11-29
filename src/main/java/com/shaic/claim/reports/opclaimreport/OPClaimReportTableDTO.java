package com.shaic.claim.reports.opclaimreport;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class OPClaimReportTableDTO  extends AbstractTableDTO  implements Serializable{
	
	private Double noOfOpClaims;
	private Double claimsOpAmount;
	
	
	
	public Double getNoOfOpClaims() {
		return noOfOpClaims;
	}
	public void setNoOfOpClaims(Double noOfOpClaims) {
		this.noOfOpClaims = noOfOpClaims;
	}
	public Double getClaimsOpAmount() {
		return claimsOpAmount;
	}
	public void setClaimsOpAmount(Double claimsOpAmount) {
		this.claimsOpAmount = claimsOpAmount;
	}
	
	


}

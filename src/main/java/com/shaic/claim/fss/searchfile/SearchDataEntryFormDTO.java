
/**
 * 
 */
package com.shaic.claim.fss.searchfile;

import java.io.Serializable;

import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

/**
 * 
 *
 */
public class SearchDataEntryFormDTO  extends AbstractSearchDTO implements Serializable {
	
	private String claimNo;
	
	private String chequeNo;
	
	private String patientName;

	public String getClaimNo() {
		return claimNo;
	}

	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

	public String getChequeNo() {
		return chequeNo;
	}

	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	

}


package com.shaic.claim.viewEarlierRodDetails;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class CorporateBufferUtilisationTableDTO extends AbstractTableDTO  implements Serializable {
	
	private String typeOfClaim;
	private String referenceOrRodNo;
	private String status;
	private Double corpBufferUtilisation;
	private Double corpBufferRemainingforClaim;
	
	public String getTypeOfClaim() {
		return typeOfClaim;
	}
	public void setTypeOfClaim(String typeOfClaim) {
		this.typeOfClaim = typeOfClaim;
	}
	public String getReferenceOrRodNo() {
		return referenceOrRodNo;
	}
	public void setReferenceOrRodNo(String referenceOrRodNo) {
		this.referenceOrRodNo = referenceOrRodNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Double getCorpBufferUtilisation() {
		return corpBufferUtilisation;
	}
	public void setCorpBufferUtilisation(Double corpBufferUtilisation) {
		this.corpBufferUtilisation = corpBufferUtilisation;
	}
	public Double getCorpBufferRemainingforClaim() {
		return corpBufferRemainingforClaim;
	}
	public void setCorpBufferRemainingforClaim(Double corpBufferRemainingforClaim) {
		this.corpBufferRemainingforClaim = corpBufferRemainingforClaim;
	}
	
	
	
}

package com.shaic.reimburement.gatewayAddAdditinalDocument.search;

import java.util.Date;

import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class SelectedRODForPhysicaldocumentDTO extends AbstractSearchDTO{
	
	private String rodNO;
	
	private String billClassification;
	
	private Date docUplodedDate;
	
	private Boolean originalDocumentVerified;

	public String getRodNO() {
		return rodNO;
	}

	public void setRodNO(String rodNO) {
		this.rodNO = rodNO;
	}

	public String getBillClassification() {
		return billClassification;
	}

	public void setBillClassification(String billClassification) {
		this.billClassification = billClassification;
	}

	public Date getDocUplodedDate() {
		return docUplodedDate;
	}

	public void setDocUplodedDate(Date docUplodedDate) {
		this.docUplodedDate = docUplodedDate;
	}

	public Boolean getOriginalDocumentVerified() {
		return originalDocumentVerified;
	}

	public void setOriginalDocumentVerified(Boolean originalDocumentVerified) {
		this.originalDocumentVerified = originalDocumentVerified;
	}
		
}

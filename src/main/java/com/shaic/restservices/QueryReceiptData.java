package com.shaic.restservices;

import java.util.List;

public class QueryReceiptData {

	private String rodNumber;
	private List<DocumentDetails> documentDetails;
	
	public String getRodNumber() {
		return rodNumber;
	}
	public void setRodNumber(String rodNumber) {
		this.rodNumber = rodNumber;
	}
	public List<DocumentDetails> getDocumentDetails() {
		return documentDetails;
	}
	public void setDocumentDetails(List<DocumentDetails> documentDetails) {
		this.documentDetails = documentDetails;
	}
}

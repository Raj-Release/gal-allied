package com.shaic.reimbursement.paymentprocess.initiateRecovery;

import java.io.Serializable;

import com.shaic.arch.fields.dto.SelectValue;

public class PaymentInitiateRecoverySearchFormDTO implements Serializable{

	private String intimationNo;
	private SelectValue documentReceivedFrom;
	
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public SelectValue getDocumentReceivedFrom() {
		return documentReceivedFrom;
	}
	public void setDocumentReceivedFrom(SelectValue documentReceivedFrom) {
		this.documentReceivedFrom = documentReceivedFrom;
	}
	
}
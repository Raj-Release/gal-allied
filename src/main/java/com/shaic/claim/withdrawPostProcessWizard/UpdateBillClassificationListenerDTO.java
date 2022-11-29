package com.shaic.claim.withdrawPostProcessWizard;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

public class UpdateBillClassificationListenerDTO extends AbstractTableDTO implements Serializable {
	
	private String rodNumber;
	private Date documentReceivedDate;
	private String modeOfReceipt;
	private String billClassification;
	private Double approvedAmount;
	private String status;
	private Long documentSummaryKey;
	
	public String getRodNumber() {
		return rodNumber;
	}
	public void setRodNumber(String rodNumber) {
		this.rodNumber = rodNumber;
	}
	public Date getDocumentReceivedDate() {
		return documentReceivedDate;
	}
	public void setDocumentReceivedDate(Date documentReceivedDate) {
		this.documentReceivedDate = documentReceivedDate;
	}
	public String getModeOfReceipt() {
		return modeOfReceipt;
	}
	public void setModeOfReceipt(String modeOfReceipt) {
		this.modeOfReceipt = modeOfReceipt;
	}
	public String getBillClassification() {
		return billClassification;
	}
	public void setBillClassification(String billClassification) {
		this.billClassification = billClassification;
	}
	public Double getApprovedAmount() {
		return approvedAmount;
	}
	public void setApprovedAmount(Double approvedAmount) {
		this.approvedAmount = approvedAmount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getDocumentSummaryKey() {
		return documentSummaryKey;
	}
	public void setDocumentSummaryKey(Long documentSummaryKey) {
		this.documentSummaryKey = documentSummaryKey;
	}

}

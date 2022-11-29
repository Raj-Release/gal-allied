package com.shaic.claim.OMPProcessOmpAcknowledgementDocuments.Upload;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.domain.Insured;
import com.shaic.domain.OMPIntimation;

public class OMPEarlierAcknowledgementDetailsPageTableDTO extends AbstractTableDTO  implements Serializable{
	
	/**
	 * S.no, Acknowledgement number, ROD number,Document received from, Document received date, Mode of Receipt,
	 *  Classification, Sub classification, Category, Final approved amount $, Final approved amount INR,
	 *  status and View Documents link. 
	 */
	private static final long serialVersionUID = 1L;
	
	private String acknowledgementNumber;
	private String rODNumber;
	private String documentReceivedFrom;
	private String documentReceivedDate;
	private String modeOfReceipt;
	private String classification;
	private String subClassification;
	private String category;
	private String finalApprovedAmount;
	private String finalApprovedAmountINR;
	private String status;
	private String viewDocumentsLink;
	private String intimationNumber;
	public String getAcknowledgementNumber() {
		return acknowledgementNumber;
	}
	public void setAcknowledgementNumber(String acknowledgementNumber) {
		this.acknowledgementNumber = acknowledgementNumber;
	}
	public String getrODNumber() {
		return rODNumber;
	}
	public void setrODNumber(String rODNumber) {
		this.rODNumber = rODNumber;
	}
	public String getDocumentReceivedFrom() {
		return documentReceivedFrom;
	}
	public void setDocumentReceivedFrom(String documentReceivedFrom) {
		this.documentReceivedFrom = documentReceivedFrom;
	}
	public String getDocumentReceivedDate() {
		return documentReceivedDate;
	}
	public void setDocumentReceivedDate(String documentReceivedDate) {
		this.documentReceivedDate = documentReceivedDate;
	}
	public String getModeOfReceipt() {
		return modeOfReceipt;
	}
	public void setModeOfReceipt(String modeOfReceipt) {
		this.modeOfReceipt = modeOfReceipt;
	}
	public String getSubClassification() {
		return subClassification;
	}
	public void setSubClassification(String subClassification) {
		this.subClassification = subClassification;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getFinalApprovedAmount() {
		return finalApprovedAmount;
	}
	public void setFinalApprovedAmount(String finalApprovedAmount) {
		this.finalApprovedAmount = finalApprovedAmount;
	}
	public String getFinalApprovedAmountINR() {
		return finalApprovedAmountINR;
	}
	public void setFinalApprovedAmountINR(String finalApprovedAmountINR) {
		this.finalApprovedAmountINR = finalApprovedAmountINR;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getViewDocumentsLink() {
		return viewDocumentsLink;
	}
	public void setViewDocumentsLink(String viewDocumentsLink) {
		this.viewDocumentsLink = viewDocumentsLink;
	}
	public String getClassification() {
		return classification;
	}
	public void setClassification(String classification) {
		this.classification = classification;
	}
	public String getIntimationNumber() {
		return intimationNumber;
	}
	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}
	
}
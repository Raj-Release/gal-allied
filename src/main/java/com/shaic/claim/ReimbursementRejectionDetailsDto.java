package com.shaic.claim;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;
import com.vaadin.v7.ui.OptionGroup;

public class ReimbursementRejectionDetailsDto extends AbstractTableDTO {
	
	private OptionGroup select;
	private Long rejectionKey;
	private int rejectionNo;
	private String acknowledgementNo;
	private String rodNo;
	private String documentReceivedFrom;
	private String billClassification;
	private String hospitalName;
	private String hospitalCity;
	private String hospitalAddress;
	private String diagnosis;
	private String rejectedByRole;
	private Date rejectionDate;
	private String rejectedDate;
	private String rejectionStatus;
	private String benefitsAndCovers; 
	
	public int getRejectionNo() {
		return rejectionNo;
	}
	public void setRejectionNo(int rejectionNo) {
		this.rejectionNo = rejectionNo;
	}
	public String getAcknowledgementNo() {
		return acknowledgementNo;
	}
	public void setAcknowledgementNo(String acknowledgementNo) {
		this.acknowledgementNo = acknowledgementNo;
	}
	public String getRodNo() {
		return rodNo;
	}
	public void setRodNo(String rodNo) {
		this.rodNo = rodNo;
	}
	public String getDocumentReceivedFrom() {
		return documentReceivedFrom;
	}
	public void setDocumentReceivedFrom(String documentReceivedFrom) {
		this.documentReceivedFrom = documentReceivedFrom;
	}
	public String getBillClassification() {
		return billClassification;
	}
	public void setBillClassification(String billClassification) {
		this.billClassification = billClassification;
	}
	public String getRejectedByRole() {
		return rejectedByRole;
	}
	public void setRejectedByRole(String rejectedByRole) {
		this.rejectedByRole = rejectedByRole;
	}
	public Date getRejectionDate() {
		return rejectionDate;
	}
	public void setRejectionDate(Date rejectionDate) {
		this.rejectionDate = rejectionDate;
		
		if(this.rejectionDate != null){
			rejectedDate = new SimpleDateFormat("dd/MM/yyyy").format(this.rejectionDate);
		}
	}
	public String getRejectionStatus() {
		return rejectionStatus;
	}
	public void setRejectionStatus(String rejectionStatus) {
		this.rejectionStatus = rejectionStatus;
	}
	public Long getRejectionKey() {
		return rejectionKey;
	}
	public void setRejectionKey(Long rejectionKey) {
		this.rejectionKey = rejectionKey;
	}
	public String getRejectedDate() {
		return rejectedDate;
	}
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public String getHospitalCity() {
		return hospitalCity;
	}
	public void setHospitalCity(String hospitalCity) {
		this.hospitalCity = hospitalCity;
	}
	public String getHospitalAddress() {
		return hospitalAddress;
	}
	public void setHospitalAddress(String hospitalAddress) {
		this.hospitalAddress = hospitalAddress;
	}
	public String getDiagnosis() {
		return diagnosis;
	}
	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}
	public void setRejectedDate(String rejectedDate) {
		this.rejectedDate = rejectedDate;
	}
	public OptionGroup getSelect() {
		return select;
	}
	public void setSelect(OptionGroup select) {
		this.select = select;
	}
	public String getBenefitsAndCovers() {
		return benefitsAndCovers;
	}
	public void setBenefitsAndCovers(String benefitsAndCovers) {
		this.benefitsAndCovers = benefitsAndCovers;
	}
	
}

package com.shaic.claim.outpatient.registerclaim.dto;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.Insured;

public class OPDocumentDetailsDTO implements Serializable {

	private static final long serialVersionUID = 3284124767863500959L;

	private SelectValue claimType;
	private Insured insuredPatientName;
	private Date OPCheckupDate;
	private Date billReceivedDate;
	private Double amountClaimed;	
	private Double provisionAmt;
	private SelectValue reasonForOPVisit;	
	private String remarksForOpVisit;
	
	
	private String docSubmittedName;
	private Date docSubmittedDate;
	private Long docSubmittedContactNo;
	private SelectValue modeOfReceipt;
	private String docEmailId;
	private SelectValue treatmentType;
	private String reasonforConsultation;
	private String remarksForEmergencyAccident;
	private String emergencyFlag;
	private String accidentFlag;
	
	private Long modeOfReceiptKey;
	private String modeOfReceiptValue;
	private Long treatmentTypeId;
	private String treatmentTypeValue;
	
	private SelectValue payeeName;
	
	private Double balanceSI;
	private Double totalBillAmount;
	private Double amountPayable;
	private Double deductions;
	private Double amountEligible;
	private Date documentReceivedDate;
	
	private String physicalDocsReceivedFlag;
	private Date physicalDocsReceivedDate;
	
	private SelectValue consultationType;

	public SelectValue getClaimType() {
		return claimType;
	}
	public void setClaimType(SelectValue claimType) {
		this.claimType = claimType;
	}
	public Insured getInsuredPatientName() {
		return insuredPatientName;
	}
	public void setInsuredPatientName(Insured insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
	}
	public Date getOPCheckupDate() {
		return OPCheckupDate;
	}
	public void setOPCheckupDate(Date oPCheckupDate) {
		OPCheckupDate = oPCheckupDate;
	}
	public Date getBillReceivedDate() {
		return billReceivedDate;
	}
	public void setBillReceivedDate(Date billReceivedDate) {
		this.billReceivedDate = billReceivedDate;
	}
	public Double getAmountClaimed() {
		return amountClaimed;
	}
	public void setAmountClaimed(Double amountClaimed) {
		this.amountClaimed = amountClaimed;
	}
	public Double getProvisionAmt() {
		return provisionAmt;
	}
	public void setProvisionAmt(Double provisionAmt) {
		this.provisionAmt = provisionAmt;
	}
	public SelectValue getReasonForOPVisit() {
		return reasonForOPVisit;
	}
	public void setReasonForOPVisit(SelectValue reasonForOPVisit) {
		this.reasonForOPVisit = reasonForOPVisit;
	}
	public String getRemarksForOpVisit() {
		return remarksForOpVisit;
	}
	public void setRemarksForOpVisit(String remarksForOpVisit) {
		this.remarksForOpVisit = remarksForOpVisit;
	}
	
	
	
	public String getDocSubmittedName() {
		return docSubmittedName;
	}
	public void setDocSubmittedName(String docSubmittedName) {
		this.docSubmittedName = docSubmittedName;
	}
	public Date getDocSubmittedDate() {
		return docSubmittedDate;
	}
	public void setDocSubmittedDate(Date docSubmittedDate) {
		this.docSubmittedDate = docSubmittedDate;
	}
	public Long getDocSubmittedContactNo() {
		return docSubmittedContactNo;
	}
	public void setDocSubmittedContactNo(Long docSubmittedContactNo) {
		this.docSubmittedContactNo = docSubmittedContactNo;
	}
	public SelectValue getModeOfReceipt() {
		return modeOfReceipt;
	}
	public void setModeOfReceipt(SelectValue modeOfReceipt) {
		this.modeOfReceipt = modeOfReceipt;
	}
	public String getDocEmailId() {
		return docEmailId;
	}
	public void setDocEmailId(String docEmailId) {
		this.docEmailId = docEmailId;
	}
	public SelectValue getTreatmentType() {
		return treatmentType;
	}
	public void setTreatmentType(SelectValue treatmentType) {
		this.treatmentType = treatmentType;
	}
	public String getReasonforConsultation() {
		return reasonforConsultation;
	}
	public void setReasonforConsultation(String reasonforConsultation) {
		this.reasonforConsultation = reasonforConsultation;
	}
	public String getRemarksForEmergencyAccident() {
		return remarksForEmergencyAccident;
	}
	public void setRemarksForEmergencyAccident(
			String remarksForEmergencyAccident) {
		this.remarksForEmergencyAccident = remarksForEmergencyAccident;
	}
	public String getEmergencyFlag() {
		return emergencyFlag;
	}
	public void setEmergencyFlag(String emergencyFlag) {
		this.emergencyFlag = emergencyFlag;
	}
	public String getAccidentFlag() {
		return accidentFlag;
	}
	public void setAccidentFlag(String accidentFlag) {
		this.accidentFlag = accidentFlag;
	}
	public Long getModeOfReceiptKey() {
		return modeOfReceiptKey;
	}
	public void setModeOfReceiptKey(Long modeOfReceiptKey) {
		this.modeOfReceiptKey = modeOfReceiptKey;
	}
	public String getModeOfReceiptValue() {
		return modeOfReceiptValue;
	}
	public void setModeOfReceiptValue(String modeOfReceiptValue) {
		this.modeOfReceiptValue = modeOfReceiptValue;
	}
	public Long getTreatmentTypeId() {
		return treatmentTypeId;
	}
	public void setTreatmentTypeId(Long treatmentTypeId) {
		this.treatmentTypeId = treatmentTypeId;
	}
	public String getTreatmentTypeValue() {
		return treatmentTypeValue;
	}
	public void setTreatmentTypeValue(String treatmentTypeValue) {
		this.treatmentTypeValue = treatmentTypeValue;
	}
	public SelectValue getPayeeName() {
		return payeeName;
	}
	public void setPayeeName(SelectValue payeeName) {
		this.payeeName = payeeName;
	}
	public Double getBalanceSI() {
		return balanceSI;
	}
	public void setBalanceSI(Double balanceSI) {
		this.balanceSI = balanceSI;
	}
	public Date getDocumentReceivedDate() {
		return documentReceivedDate;
	}
	public void setDocumentReceivedDate(Date documentReceivedDate) {
		this.documentReceivedDate = documentReceivedDate;
	}
	public Double getTotalBillAmount() {
		return totalBillAmount;
	}
	public void setTotalBillAmount(Double totalBillAmount) {
		this.totalBillAmount = totalBillAmount;
	}
	public Double getAmountPayable() {
		return amountPayable;
	}
	public void setAmountPayable(Double amountPayable) {
		this.amountPayable = amountPayable;
	}
	public Double getDeductions() {
		return deductions;
	}
	public void setDeductions(Double deductions) {
		this.deductions = deductions;
	}
	public Double getAmountEligible() {
		return amountEligible;
	}
	public void setAmountEligible(Double amountEligible) {
		this.amountEligible = amountEligible;
	}
	public String getPhysicalDocsReceivedFlag() {
		return physicalDocsReceivedFlag;
	}
	public void setPhysicalDocsReceivedFlag(String physicalDocsReceivedFlag) {
		this.physicalDocsReceivedFlag = physicalDocsReceivedFlag;
	}
	public Date getPhysicalDocsReceivedDate() {
		return physicalDocsReceivedDate;
	}
	public void setPhysicalDocsReceivedDate(Date physicalDocsReceivedDate) {
		this.physicalDocsReceivedDate = physicalDocsReceivedDate;
	}
	public SelectValue getConsultationType() {
		return consultationType;
	}
	public void setConsultationType(SelectValue consultationType) {
		this.consultationType = consultationType;
	}
	
}

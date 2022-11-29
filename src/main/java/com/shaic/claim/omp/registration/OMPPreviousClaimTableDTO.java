package com.shaic.claim.omp.registration;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.domain.Intimation;
import com.shaic.domain.OMPIntimation;

public class OMPPreviousClaimTableDTO  extends AbstractTableDTO  implements Serializable{
	
	private Long srno;
	private String policyno;
	private String insuredname;
	private String intimationno;
	private Date admissiondate;
	private String hospitalname;
	private String claimtype;
	private String eventcode;
	private Double provAmt;
	private Double settledAmt;
	private String claimStatus;
	private String customerID;
	
	private String claimNumber;
	
	private String insuredPatientName;
	
	private String diagnosis;
	
	private String claimAmount;

	private Double settledAmount; 
	
	private Double settledAmountForPreviousClaim;
	
	private String diagnosisForPreviousClaim;
	
	private String patientName;
	
	private Long claimTypeKey;
	
	private Long claimedAmount;
	
	private String policyYear;
	
	private Double amountPayable;
	
	private Intimation intimation;
	
	private OMPIntimation ompIntimation;
	
	private Long copayPercentage;
	
	private String pedName;
	
	private String icdCodes;
	
	private String recordFlag;
	
	private String benefits;
	
	private String approvedAmount;
	
	
	public Long getSrno() {
		return srno;
	}
	public void setSrno(Long srno) {
		this.srno = srno;
	}
	public String getPolicyno() {
		return policyno;
	}
	public void setPolicyno(String policyno) {
		this.policyno = policyno;
	}
	public String getInsuredname() {
		return insuredname;
	}
	public void setInsuredname(String insuredname) {
		this.insuredname = insuredname;
	}
	public String getIntimationno() {
		return intimationno;
	}
	public void setIntimationno(String intimationno) {
		this.intimationno = intimationno;
	}
	public Date getAdmissiondate() {
		return admissiondate;
	}
	public void setAdmissiondate(Date admissiondate) {
		this.admissiondate = admissiondate;
	}
	public String getHospitalname() {
		return hospitalname;
	}
	public void setHospitalname(String hospitalname) {
		this.hospitalname = hospitalname;
	}
	public String getClaimtype() {
		return claimtype;
	}
	public void setClaimtype(String claimtype) {
		this.claimtype = claimtype;
	}
	public String getEventcode() {
		return eventcode;
	}
	public void setEventcode(String eventcode) {
		this.eventcode = eventcode;
	}
	public Double getProvAmt() {
		return provAmt;
	}
	public void setProvAmt(Double provAmt) {
		this.provAmt = provAmt;
	}
	public Double getSettledAmt() {
		return settledAmt;
	}
	public void setSettledAmt(Double settledAmt) {
		this.settledAmt = settledAmt;
	}
	public String getClaimStatus() {
		return claimStatus;
	}
	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}
	public String getCustomerID() {
		return customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	
	}
	public String getInsuredPatientName() {
		return insuredPatientName;
	}
	public void setInsuredPatientName(String insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
	}
	public String getDiagnosis() {
		return diagnosis;
	}
	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}
	public String getClaimAmount() {
		return claimAmount;
	}
	public void setClaimAmount(String claimAmount) {
		this.claimAmount = claimAmount;
	}
	
	public Double getSettledAmount() {
		return settledAmount;
	}
	public void setSettledAmount(Double settledAmount) {
		this.settledAmount = settledAmount;
	}
	public Double getSettledAmountForPreviousClaim() {
		return settledAmountForPreviousClaim;
	}
	public void setSettledAmountForPreviousClaim(
			Double settledAmountForPreviousClaim) {
		this.settledAmountForPreviousClaim = settledAmountForPreviousClaim;
	}
	public String getDiagnosisForPreviousClaim() {
		return diagnosisForPreviousClaim;
	}
	public void setDiagnosisForPreviousClaim(String diagnosisForPreviousClaim) {
		this.diagnosisForPreviousClaim = diagnosisForPreviousClaim;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public Long getClaimTypeKey() {
		return claimTypeKey;
	}
	public void setClaimTypeKey(Long claimTypeKey) {
		this.claimTypeKey = claimTypeKey;
	}
	public Long getClaimedAmount() {
		return claimedAmount;
	}
	public void setClaimedAmount(Long claimedAmount) {
		this.claimedAmount = claimedAmount;
	}
	public String getPolicyYear() {
		return policyYear;
	}
	public void setPolicyYear(String policyYear) {
		this.policyYear = policyYear;
	}
	public Double getAmountPayable() {
		return amountPayable;
	}
	public void setAmountPayable(Double amountPayable) {
		this.amountPayable = amountPayable;
	}
	public Intimation getIntimation() {
		return intimation;
	}
	public void setIntimation(Intimation intimation) {
		this.intimation = intimation;
	}
	public OMPIntimation getOmpIntimation() {
		return ompIntimation;
	}
	public void setOmpIntimation(OMPIntimation ompIntimation) {
		this.ompIntimation = ompIntimation;
	}
	public Long getCopayPercentage() {
		return copayPercentage;
	}
	public void setCopayPercentage(Long copayPercentage) {
		this.copayPercentage = copayPercentage;
	}
	public String getPedName() {
		return pedName;
	}
	public void setPedName(String pedName) {
		this.pedName = pedName;
	}
	public String getIcdCodes() {
		return icdCodes;
	}
	public void setIcdCodes(String icdCodes) {
		this.icdCodes = icdCodes;
	}
	public String getRecordFlag() {
		return recordFlag;
	}
	public void setRecordFlag(String recordFlag) {
		this.recordFlag = recordFlag;
	}
	public String getBenefits() {
		return benefits;
	}
	public void setBenefits(String benefits) {
		this.benefits = benefits;
	}
	public String getApprovedAmount() {
		return approvedAmount;
	}
	public void setApprovedAmount(String approvedAmount) {
		this.approvedAmount = approvedAmount;
	}
	public String getClaimNumber() {
		return claimNumber;
	}
	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}
	
	

}

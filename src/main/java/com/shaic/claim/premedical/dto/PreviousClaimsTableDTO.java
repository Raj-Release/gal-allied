package com.shaic.claim.premedical.dto;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.domain.Intimation;
import com.shaic.domain.OMPIntimation;

public class PreviousClaimsTableDTO extends AbstractTableDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7187633646778679066L;
	
	private Long intimationKey;
	
	private String policyNumber;
	
	private String intimationNumber;
	
	private String hospitalName;
	
	private String claimNumber;
	
	private String claimType;
	
	private String customerID;
	
	private String insuredName;
	
	private String insuredPatientName;
	
	private String diagnosis;
	
	private String admissionDate;
	
	private String claimStatus;
	
	private String claimAmount;

	private Double provisionAmount;
	
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
	
	private Double copayPercentage;
	
	private String pedName;
	
	private String icdCodes;
	
	private String recordFlag;
	
	private String benefits;
	
	private String ailmentLoss;
	
	private String parentName;
	
	private Date parentDOB;
	
	private Date patientDOB;
	
	private String category;
	
	private String lossDetails;
	
	private String subLimitApplicable;
	
	private String subLimitName;
	
	private String subLimitAmount;
	
	private String siRestrication;
	
	private String forSIRest;
	

	private String isSubLimitApplicable;
	
	private String dmsUrlForBancs;
	
	private String tokenString;
	
	public String getBenefits() {
		return benefits;
	}

	public void setBenefits(String benefits) {
		this.benefits = benefits;
	}

	private String approvedAmount;
	
		
	public Intimation getIntimation() {
		return intimation;
	}

	public void setIntimation(Intimation intimation) {
		this.intimation = intimation;
	}

	public String getClaimNumber() {
		return claimNumber;
	}

	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
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


	public String getClaimStatus() {
		return claimStatus;
	}

	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}

	public String getClaimAmount() {
		return claimAmount;
	}

	public void setClaimAmount(String claimAmount) {
		this.claimAmount = claimAmount;
	}

	public String getAdmissionDate() {
		return admissionDate;
	}

	public void setAdmissionDate(String admissionDate) {
		this.admissionDate = admissionDate;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getIntimationNumber() {
		return intimationNumber;
	}

	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	public Double getProvisionAmount() {
		return provisionAmount;
	}

	public void setProvisionAmount(Double provisionAmount) {
		this.provisionAmount = provisionAmount;
	}

	public Double getSettledAmount() {
		return settledAmount;
	}

	public void setSettledAmount(Double settledAmount) {
		this.settledAmount = settledAmount;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public Long getIntimationKey() {
		return intimationKey;
	}

	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
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

	public Double getCopayPercentage() {
		return copayPercentage;
	}

	public void setCopayPercentage(Double copayPercentage) {
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

	public String getApprovedAmount() {
		return approvedAmount;
	}

	public void setApprovedAmount(String approvedAmount) {
		this.approvedAmount = approvedAmount;
	}

	public String getRecordFlag() {
		return recordFlag;
	}

	public void setRecordFlag(String recordFlag) {
		this.recordFlag = recordFlag;
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

	public OMPIntimation getOmpIntimation() {
		return ompIntimation;
	}

	public void setOmpIntimation(OMPIntimation ompIntimation) {
		this.ompIntimation = ompIntimation;
	}

	public String getAilmentLoss() {
		return ailmentLoss;
	}

	public void setAilmentLoss(String ailmentLoss) {
		this.ailmentLoss = ailmentLoss;
	}
	

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Date getParentDOB() {
		return parentDOB;
	}

	public void setParentDOB(Date parentDOB) {
		this.parentDOB = parentDOB;
	}

	public Date getPatientDOB() {
		return patientDOB;
	}

	public void setPatientDOB(Date patientDOB) {
		this.patientDOB = patientDOB;
	}

	public String getLossDetails() {
		return lossDetails;
	}

	public void setLossDetails(String lossDetails) {
		this.lossDetails = lossDetails;
	}

	public String getSubLimitApplicable() {
		return subLimitApplicable;
	}

	public void setSubLimitApplicable(String subLimitApplicable) {
		this.subLimitApplicable = subLimitApplicable;
	}

	public String getSubLimitName() {
		return subLimitName;
	}

	public void setSubLimitName(String subLimitName) {
		this.subLimitName = subLimitName;
	}

	public String getSubLimitAmount() {
		return subLimitAmount;
	}

	public void setSubLimitAmount(String subLimitAmount) {
		this.subLimitAmount = subLimitAmount;
	}

	public String getSiRestrication() {
		return siRestrication;
	}

	public void setSiRestrication(String siRestrication) {
		this.siRestrication = siRestrication;
	}

	public String getForSIRest() {
		return forSIRest;
	}

	public void setForSIRest(String forSIRest) {
		this.forSIRest = forSIRest;
	}
	
	public String getIsSubLimitApplicable() {
		return isSubLimitApplicable;
	}

	public void setIsSubLimitApplicable(String isSubLimitApplicable) {
		this.isSubLimitApplicable = isSubLimitApplicable;
	}

	public String getDmsUrlForBancs() {
		return dmsUrlForBancs;
	}

	public void setDmsUrlForBancs(String dmsUrlForBancs) {
		this.dmsUrlForBancs = dmsUrlForBancs;
	}

	public String getTokenString() {
		return tokenString;
	}

	public void setTokenString(String tokenString) {
		this.tokenString = tokenString;
	}
}

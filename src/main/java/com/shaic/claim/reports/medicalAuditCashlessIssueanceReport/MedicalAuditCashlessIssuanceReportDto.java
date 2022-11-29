package com.shaic.claim.reports.medicalAuditCashlessIssueanceReport;

import com.shaic.claim.ClaimDto;
import com.shaic.domain.Claim;
import com.shaic.domain.Policy;

public class MedicalAuditCashlessIssuanceReportDto {

	private String claimNumber;
	private String policyNumber;
	private String preAuthAmount;
	private String intimationNo;
	private String product;
	private String diagnosis;
	private String generalRemarks;
	private String reBillingOrReQuery;
	private String queryRaisedOrMedRejReq;
	private String finalRemarks;	
	private String doctorNote;
	private Policy policyObj;
	private String hospitalCode;
	
	public MedicalAuditCashlessIssuanceReportDto(ClaimDto claimDto){
		
		this.claimNumber = claimDto.getClaimId();
		this.policyNumber = claimDto.getNewIntimationDto().getPolicy().getPolicyNumber();
		this.intimationNo = claimDto.getNewIntimationDto().getIntimationId();
		this.product = claimDto.getNewIntimationDto().getPolicy().getProduct().getValue();
		this.policyObj = claimDto.getNewIntimationDto().getPolicy();
		this.hospitalCode = claimDto.getNewIntimationDto().getHospitalDto().getRegistedHospitals().getHospitalCode();
	}
	
	public MedicalAuditCashlessIssuanceReportDto(Claim claimObj,String hospCode){
		
		this.claimNumber = claimObj.getClaimId();
		this.policyNumber = claimObj.getIntimation().getPolicy().getPolicyNumber();
		this.intimationNo = claimObj.getIntimation().getIntimationId();
		this.product = claimObj.getIntimation().getPolicy().getProduct().getValue();
		this.policyObj = claimObj.getIntimation().getPolicy();
		this.hospitalCode = hospCode;
	}
	
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getDiagnosis() {
		return diagnosis;
	}
	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}
	public String getGeneralRemarks() {
		return generalRemarks;
	}
	public void setGeneralRemarks(String generalRemarks) {
		this.generalRemarks = generalRemarks;
	}
	public String getReBillingOrReQuery() {
		return reBillingOrReQuery;
	}
	public void setReBillingOrReQuery(String reBillingOrReQuery) {
		this.reBillingOrReQuery = reBillingOrReQuery;
	}
	public String getQueryRaisedOrMedRejReq() {
		return queryRaisedOrMedRejReq;
	}
	public void setQueryRaisedOrMedRejReq(String queryRaisedOrMedRejReq) {
		this.queryRaisedOrMedRejReq = queryRaisedOrMedRejReq;
	}
	public String getFinalRemarks() {
		return finalRemarks;
	}
	public void setFinalRemarks(String finalRemarks) {
		this.finalRemarks = finalRemarks;
	}
	public String getDoctorNote() {
		return doctorNote;
	}
	public void setDoctorNote(String doctorNote) {
		this.doctorNote = doctorNote;
	}
	public String getClaimNumber() {
		return claimNumber;
	}
	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}
	public String getPolicyNumber() {
		return policyNumber;
	}
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
	public Policy getPolicyObj() {
		return policyObj;
	}
	public void setPolicyObj(Policy policyObj) {
		this.policyObj = policyObj;
	}

	public String getPreAuthAmount() {
		return preAuthAmount;
	}

	public void setPreAuthAmount(String preAuthAmount) {
		this.preAuthAmount = preAuthAmount;
	}

	public String getHospitalCode() {
		return hospitalCode;
	}

	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}
	
	
	
}

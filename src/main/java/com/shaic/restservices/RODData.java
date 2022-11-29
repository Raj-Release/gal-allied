package com.shaic.restservices;

import java.util.List;

public class RODData {

	private String docReceivedFrom;
	private String docReceivedDate;
	private String claimNumber;
	private String intimationNumber;
	private String modeOfReceipt;
	private String reconsiderationRequest;
	private String hospitalizationClaimedAmt;
	private String preHospitalizationClaimedAmt;
	private String postHospitalizationClaimedAmt;
	private String emailId;
	private String ackContactNumber;
	
	private String hospitalizationFlag;
	private String preHospitalizationFlag;
	private String postHospitalizationFlag;
	private String partialHospitalizationFlag;
	private String repeatHospitalizationFlag;
	private String lumpsumAmountFlag;
	private String addOnBenefitsHospitalCashFlag;
	private String addOnBenefitsPatientCareFlag;
	
	private String additionalRemarks;
	private String createdBy;
	
	private String reconsiderationRequestReason;
	private String paymentCancellationFlag;
	private String reconsideredDate;
	
	private String dateOfAdmission;
	private String dateOfDischarge;
	private String paymentMode;
	private String payeeName;
	private String payeeEmailId;
	private String reasonForChange;
	private String panNumber;
	
	private String payableAt;
	private String legalHeirName;
	private String accountNo;
	private String ifscCode;
	private String bankName;
	private String city;
	private String branch;
	
	private String queryKey;
	private String queryReplyRemarks;
	private String rodNumber;

	private List<DocumentDetails> documentDetails;
	
	public String getDocReceivedFrom() {
		return docReceivedFrom;
	}
	public void setDocReceivedFrom(String docReceivedFrom) {
		this.docReceivedFrom = docReceivedFrom;
	}
	public List<DocumentDetails> getDocumentDetails() {
		return documentDetails;
	}
	public void setDocumentDetails(List<DocumentDetails> documentDetails) {
		this.documentDetails = documentDetails;
	}
	public String getClaimNumber() {
		return claimNumber;
	}
	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}
	public String getDocReceivedDate() {
		return docReceivedDate;
	}
	public void setDocReceivedDate(String docReceivedDate) {
		this.docReceivedDate = docReceivedDate;
	}
	public String getModeOfReceipt() {
		return modeOfReceipt;
	}
	public void setModeOfReceipt(String modeOfReceipt) {
		this.modeOfReceipt = modeOfReceipt;
	}
	public String getReconsiderationRequest() {
		return reconsiderationRequest;
	}
	public void setReconsiderationRequest(String reconsiderationRequest) {
		this.reconsiderationRequest = reconsiderationRequest;
	}
	public String getHospitalizationClaimedAmt() {
		return hospitalizationClaimedAmt;
	}
	public void setHospitalizationClaimedAmt(String hospitalizationClaimedAmt) {
		this.hospitalizationClaimedAmt = hospitalizationClaimedAmt;
	}
	public String getPreHospitalizationClaimedAmt() {
		return preHospitalizationClaimedAmt;
	}
	public void setPreHospitalizationClaimedAmt(String preHospitalizationClaimedAmt) {
		this.preHospitalizationClaimedAmt = preHospitalizationClaimedAmt;
	}
	public String getPostHospitalizationClaimedAmt() {
		return postHospitalizationClaimedAmt;
	}
	public void setPostHospitalizationClaimedAmt(String postHospitalizationClaimedAmt) {
		this.postHospitalizationClaimedAmt = postHospitalizationClaimedAmt;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getAckContactNumber() {
		return ackContactNumber;
	}
	public void setAckContactNumber(String ackContactNumber) {
		this.ackContactNumber = ackContactNumber;
	}
	public String getHospitalizationFlag() {
		return hospitalizationFlag;
	}
	public void setHospitalizationFlag(String hospitalizationFlag) {
		this.hospitalizationFlag = hospitalizationFlag;
	}
	public String getPreHospitalizationFlag() {
		return preHospitalizationFlag;
	}
	public void setPreHospitalizationFlag(String preHospitalizationFlag) {
		this.preHospitalizationFlag = preHospitalizationFlag;
	}
	public String getPostHospitalizationFlag() {
		return postHospitalizationFlag;
	}
	public void setPostHospitalizationFlag(String postHospitalizationFlag) {
		this.postHospitalizationFlag = postHospitalizationFlag;
	}
	public String getPartialHospitalizationFlag() {
		return partialHospitalizationFlag;
	}
	public void setPartialHospitalizationFlag(String partialHospitalizationFlag) {
		this.partialHospitalizationFlag = partialHospitalizationFlag;
	}
	public String getRepeatHospitalizationFlag() {
		return repeatHospitalizationFlag;
	}
	public void setRepeatHospitalizationFlag(String repeatHospitalizationFlag) {
		this.repeatHospitalizationFlag = repeatHospitalizationFlag;
	}
	public String getLumpsumAmountFlag() {
		return lumpsumAmountFlag;
	}
	public void setLumpsumAmountFlag(String lumpsumAmountFlag) {
		this.lumpsumAmountFlag = lumpsumAmountFlag;
	}
	public String getAddOnBenefitsHospitalCashFlag() {
		return addOnBenefitsHospitalCashFlag;
	}
	public void setAddOnBenefitsHospitalCashFlag(String addOnBenefitsHospitalCashFlag) {
		this.addOnBenefitsHospitalCashFlag = addOnBenefitsHospitalCashFlag;
	}
	public String getAddOnBenefitsPatientCareFlag() {
		return addOnBenefitsPatientCareFlag;
	}
	public void setAddOnBenefitsPatientCareFlag(String addOnBenefitsPatientCareFlag) {
		this.addOnBenefitsPatientCareFlag = addOnBenefitsPatientCareFlag;
	}
	public String getAdditionalRemarks() {
		return additionalRemarks;
	}
	public void setAdditionalRemarks(String additionalRemarks) {
		this.additionalRemarks = additionalRemarks;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getReconsiderationRequestReason() {
		return reconsiderationRequestReason;
	}
	public void setReconsiderationRequestReason(String reconsiderationRequestReason) {
		this.reconsiderationRequestReason = reconsiderationRequestReason;
	}
	public String getPaymentCancellationFlag() {
		return paymentCancellationFlag;
	}
	public void setPaymentCancellationFlag(String paymentCancellationFlag) {
		this.paymentCancellationFlag = paymentCancellationFlag;
	}
	public String getReconsideredDate() {
		return reconsideredDate;
	}
	public void setReconsideredDate(String reconsideredDate) {
		this.reconsideredDate = reconsideredDate;
	}
	public String getDateOfAdmission() {
		return dateOfAdmission;
	}
	public void setDateOfAdmission(String dateOfAdmission) {
		this.dateOfAdmission = dateOfAdmission;
	}
	public String getDateOfDischarge() {
		return dateOfDischarge;
	}
	public void setDateOfDischarge(String dateOfDischarge) {
		this.dateOfDischarge = dateOfDischarge;
	}
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	public String getPayeeName() {
		return payeeName;
	}
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}
	public String getPayeeEmailId() {
		return payeeEmailId;
	}
	public void setPayeeEmailId(String payeeEmailId) {
		this.payeeEmailId = payeeEmailId;
	}
	public String getReasonForChange() {
		return reasonForChange;
	}
	public void setReasonForChange(String reasonForChange) {
		this.reasonForChange = reasonForChange;
	}
	public String getPanNumber() {
		return panNumber;
	}
	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}
	public String getPayableAt() {
		return payableAt;
	}
	public void setPayableAt(String payableAt) {
		this.payableAt = payableAt;
	}
	public String getLegalHeirName() {
		return legalHeirName;
	}
	public void setLegalHeirName(String legalHeirName) {
		this.legalHeirName = legalHeirName;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getIfscCode() {
		return ifscCode;
	}
	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public String getIntimationNumber() {
		return intimationNumber;
	}
	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}
	public String getQueryKey() {
		return queryKey;
	}
	public void setQueryKey(String queryKey) {
		this.queryKey = queryKey;
	}
	public String getQueryReplyRemarks() {
		return queryReplyRemarks;
	}
	public void setQueryReplyRemarks(String queryReplyRemarks) {
		this.queryReplyRemarks = queryReplyRemarks;
	}
	public String getRodNumber() {
		return rodNumber;
	}
	public void setRodNumber(String rodNumber) {
		this.rodNumber = rodNumber;
	}
}

package com.shaic.claim.reports.claimstatusreportnew;

import java.text.SimpleDateFormat;

import com.shaic.claim.ClaimDto;
import com.shaic.claim.ReimbursementDto;
import com.shaic.claim.ReimbursementRejectionDto;
import com.shaic.claim.reimbursement.dto.ReimbursementQueryDto;
import com.shaic.domain.Claim;

public class ClaimsStatusReportDto {
	
	private int sno;
	private String claimNo;
	private String intimationNo;	
	private String policyNumber;
	private String productName;	
	private String cpuCode;
	private String cpuName;
	private String diagnosis;
	private String hospitalName;
	private String preauthAmt;
	private String preauthApprovalDate;
	private String status;
	private String patientName;
	private String paidDate;
	private String paidAmout;
	private String claimedAmount;
	private String deductedAmount;	
	private String cashlessOrReimbursement;
	private String fvrUploaded;
	private String medicalApprovalPerson;	
	private String billingPerson;
	private String financialApprovalPerson;
	private String icdCode;
	private String claimCoverage;
	private String suminsured;
	private String patientAge;
	private String finacialYear;
	private String admissionDate;
	private String intimationDate;
	private String scheme;
	private String rejectDate;
	private String closeStage;
	private String rejectionRemarks;
	private String withDrawRemarks;
	private String billReceivedDate;
	private String billingApprovedAmount;
	private String initialProvisionAmount;
	private String userName;
	private String userId;
	private String noofTimeBillRec;
	private String maApprovedDate;
	private String queryRaisedDate;
	private String queryRaisedRemarks;
	private String queryReplyDate;
	private String queryReplyRemarks;
	private String closeDate;
	private String currentProvisionAmount;
	private String closedRemarks;
	private String billAmount;	
	private String tat;
	private Long reimbursementKey;
	private String hospCity;
	private String docRecvdFrom;
	private String statusValue;
	private String smCode;
	private String smName;
	private String agentCode;
	private String agentName;
	private String offiCode;
	private String offiName;
	
	public ClaimsStatusReportDto() {
	}
	
	public ClaimsStatusReportDto(Claim claimObj,String hospName){
		
		this.policyNumber = claimObj.getIntimation().getPolicy().getPolicyNumber();
		this.intimationNo = claimObj.getIntimation().getIntimationId();
		this.claimNo = claimObj.getClaimId();
		this.productName = claimObj.getIntimation().getPolicy().getProduct() != null && claimObj.getIntimation().getPolicy().getProduct().getValue() != null ? claimObj.getIntimation().getPolicy().getProduct().getValue() : "";
		this.intimationDate = claimObj.getIntimation().getCreatedDate() != null ? new SimpleDateFormat("dd-MM-yyyy").format(claimObj.getIntimation().getCreatedDate()).toUpperCase() : "";
		this.admissionDate = claimObj.getIntimation().getAdmissionDate() != null ? new SimpleDateFormat("dd-MM-yyyy").format(claimObj.getIntimation().getAdmissionDate()).toUpperCase() : "";
		this.patientName = claimObj.getIntimation().getInsured().getInsuredName() != null ? claimObj.getIntimation().getInsured().getInsuredName() : "";
		this.patientAge = claimObj.getIntimation().getInsured().getInsuredAge() != null ? String.valueOf(claimObj.getIntimation().getInsured().getInsuredAge().intValue()) : "";
		this.hospitalName = hospName;	
		this.cpuCode = claimObj.getIntimation().getCpuCode() != null ? String.valueOf(claimObj.getIntimation().getCpuCode().getCpuCode()) : ""; 
		this.cashlessOrReimbursement= claimObj.getClaimType() != null && claimObj.getClaimType().getValue() != null ? claimObj.getClaimType().getValue() : "" ;
		this.currentProvisionAmount = claimObj.getCurrentProvisionAmount() != null ? String.valueOf(claimObj.getCurrentProvisionAmount().intValue()) : "";
		this.initialProvisionAmount = claimObj.getProvisionAmount() != null ? String.valueOf(claimObj.getProvisionAmount().intValue()) : "";
		this.closeStage = claimObj.getStage().getStageName() != null ? claimObj.getStage().getStageName() : "";
		
	}
	
	public ClaimsStatusReportDto(ClaimDto claimDto){
		
		this.policyNumber = claimDto.getNewIntimationDto().getPolicy().getPolicyNumber();
		this.intimationNo = claimDto.getNewIntimationDto().getIntimationId();
		this.claimNo = claimDto.getClaimId();
		this.productName = claimDto.getNewIntimationDto().getPolicy().getProduct() != null && claimDto.getNewIntimationDto().getPolicy().getProduct().getValue() != null ? claimDto.getNewIntimationDto().getPolicy().getProduct().getValue() : "";
		this.intimationDate = claimDto.getNewIntimationDto().getCreatedDate() != null ? new SimpleDateFormat("dd-MM-yyyy").format(claimDto.getNewIntimationDto().getCreatedDate()).toUpperCase() : "";
		this.admissionDate = claimDto.getNewIntimationDto().getAdmissionDate() != null ? new SimpleDateFormat("dd-MM-yyyy").format(claimDto.getNewIntimationDto().getAdmissionDate()).toUpperCase() : "";
		this.patientName = claimDto.getNewIntimationDto().getInsuredPatient().getInsuredName() != null ? claimDto.getNewIntimationDto().getInsuredPatient().getInsuredName() : "";
		this.patientAge = claimDto.getNewIntimationDto().getInsuredPatient().getInsuredAge() != null ? String.valueOf(claimDto.getNewIntimationDto().getInsuredPatient().getInsuredAge().intValue()) : "";
		this.hospitalName = claimDto.getNewIntimationDto().getHospitalDto().getRegistedHospitals().getName();	
		this.cpuCode = claimDto.getNewIntimationDto().getCpuCode() != null ? claimDto.getNewIntimationDto().getCpuCode() : ""; 
		this.cashlessOrReimbursement= claimDto.getClaimType() != null && claimDto.getClaimType().getValue() != null ? claimDto.getClaimType().getValue() : "" ;
		this.currentProvisionAmount = claimDto.getCurrentProvisionAmount() != null ? String.valueOf(claimDto.getCurrentProvisionAmount().intValue()) : "";
		this.initialProvisionAmount = claimDto.getProvisionAmount() != null ? String.valueOf(claimDto.getProvisionAmount().intValue()) : "";
		this.closeStage = claimDto.getStageName() != null ? claimDto.getStageName() : "";
		
	}

	public ClaimsStatusReportDto(ReimbursementDto reimbDto){
			this(reimbDto.getClaimDto());
			this.reimbursementKey = reimbDto.getKey();
			this.billReceivedDate = reimbDto.getDocAcknowledgementDto().getDocumentReceivedDate() != null ? new SimpleDateFormat("dd-MM-yyyy").format(reimbDto.getDocAcknowledgementDto().getDocumentReceivedDate()).toUpperCase() : "";
			this.billingApprovedAmount = reimbDto.getBillingApprovedAmount() != null ? String.valueOf(reimbDto.getBillingApprovedAmount().intValue()) : "";
			
//		this.policyNumber = claimDto.getNewIntimationDto().getPolicy().getPolicyNumber();
//		this.intimationNo = claimDto.getNewIntimationDto().getIntimationId();
//		this.claimNo = claimDto.getClaimId();
//		this.productName = claimDto.getNewIntimationDto().getPolicy().getProduct() != null && claimDto.getNewIntimationDto().getPolicy().getProduct().getValue() != null ? claimDto.getNewIntimationDto().getPolicy().getProduct().getValue() : "";
//		this.intimationDate = claimDto.getNewIntimationDto().getCreatedDate() != null ? new SimpleDateFormat("dd-MMM-yy").format(claimDto.getNewIntimationDto().getCreatedDate()).toUpperCase() : "";
//		this.admissionDate = claimDto.getNewIntimationDto().getAdmissionDate() != null ? new SimpleDateFormat("dd-MMM-yy").format(claimDto.getNewIntimationDto().getAdmissionDate()).toUpperCase() : "";
//		this.patientName = claimDto.getNewIntimationDto().getInsuredPatient().getInsuredName() != null ? claimDto.getNewIntimationDto().getInsuredPatient().getInsuredName() : "";
//		this.patientAge = claimDto.getNewIntimationDto().getInsuredPatient().getInsuredAge() != null ? String.valueOf(claimDto.getNewIntimationDto().getInsuredPatient().getInsuredAge().intValue()) : "";
//		this.hospitalName = claimDto.getNewIntimationDto().getHospitalDto().getRegistedHospitals().getName();	
//		this.cpuCode = claimDto.getNewIntimationDto().getCpuCode() != null ? claimDto.getNewIntimationDto().getCpuCode() : ""; 
//		this.cashlessOrReimbursement= claimDto.getClaimType() != null && claimDto.getClaimType().getValue() != null ? claimDto.getClaimType().getValue() : "" ;
//		this.currentProvisionAmount = claimDto.getCurrentProvisionAmount() != null ? String.valueOf(claimDto.getCurrentProvisionAmount().intValue()) : "";
//		this.initialProvisionAmount = claimDto.getProvisionAmount() != null ? String.valueOf(claimDto.getProvisionAmount().intValue()) : "";
//		this.closeStage = claimDto.getStageName() != null ? claimDto.getStageName() : "";
		
	}
	public ClaimsStatusReportDto(ReimbursementQueryDto reimbQueryDto){
		this(reimbQueryDto.getReimbursementDto());
		this.queryRaisedDate = reimbQueryDto.getCreatedDate() != null ? new SimpleDateFormat("dd-MM-yyyy").format(reimbQueryDto.getCreatedDate()).toUpperCase() : "";
		this.queryRaisedRemarks = reimbQueryDto.getQueryRemarks() != null ? reimbQueryDto.getQueryRemarks() : "";		
	}
	public ClaimsStatusReportDto(ReimbursementRejectionDto reimbRejectDto){
		this(reimbRejectDto.getReimbursementDto());
		this.rejectDate = reimbRejectDto.getModifiedDate() != null ? new SimpleDateFormat("dd-MM-yyy").format(reimbRejectDto.getModifiedDate()).toUpperCase() : "";
		this.rejectionRemarks = reimbRejectDto.getRejectionRemarks() != null ? reimbRejectDto.getRejectionRemarks() : "";
	}
	
	public int getSno() {
		return sno;
	}

	public void setSno(int sno) {
		this.sno = sno;
	}

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public String getIntimationDate() {
		return intimationDate;
	}

	public void setIntimationDate(String intimationDate) {
		this.intimationDate = intimationDate;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getClaimNo() {
		return claimNo;
	}

	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getCpuCode() {
		return cpuCode;
	}

	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getPaidDate() {
		return paidDate;
	}

	public void setPaidDate(String paidDate) {
		this.paidDate = paidDate;
	}	

	public String getPaidAmout() {
		return paidAmout;
	}

	public void setPaidAmout(String paidAmout) {
		this.paidAmout = paidAmout;
	}

	public String getClaimedAmount() {
		return claimedAmount;
	}

	public void setClaimedAmount(String claimedAmount) {
		this.claimedAmount = claimedAmount;
	}

	public String getDeductedAmount() {
		return deductedAmount;
	}

	public void setDeductedAmount(String deductedAmount) {
		this.deductedAmount = deductedAmount;
	}

	public String getCashlessOrReimbursement() {
		return cashlessOrReimbursement;
	}

	public void setCashlessOrReimbursement(String cashlessOrReimbursement) {
		this.cashlessOrReimbursement = cashlessOrReimbursement;
	}
    
	public String getFvrUploaded() {
		return fvrUploaded;
	}

	public void setFvrUploaded(String fvrUploaded) {
		this.fvrUploaded = fvrUploaded;
	}

	public String getIcdCode() {
		return icdCode;
	}

	public void setIcdCode(String icdCode) {
		this.icdCode = icdCode;
	}

	public String getMedicalApprovalPerson() {
		return medicalApprovalPerson;
	}

	public void setMedicalApprovalPerson(String medicalApprovalPerson) {
		this.medicalApprovalPerson = medicalApprovalPerson;
	}

	public String getBillingPerson() {
		return billingPerson;
	}

	public void setBillingPerson(String billingPerson) {
		this.billingPerson = billingPerson;
	}

	public String getFinancialApprovalPerson() {
		return financialApprovalPerson;
	}

	public void setFinancialApprovalPerson(String financialApprovalPerson) {
		this.financialApprovalPerson = financialApprovalPerson;
	}

	public String getClaimCoverage() {
		return claimCoverage;
	}

	public void setClaimCoverage(String claimCoverage) {
		this.claimCoverage = claimCoverage;
	}

	public String getSuminsured() {
		return suminsured;
	}

	public void setSuminsured(String suminsured) {
		this.suminsured = suminsured;
	}

	public String getPatientAge() {
		return patientAge;
	}

	public void setPatientAge(String patientAge) {
		this.patientAge = patientAge;
	}

	public String getFinacialYear() {
		return finacialYear;
	}

	public void setFinacialYear(String finacialYear) {
		this.finacialYear = finacialYear;
	}

	public String getAdmissionDate() {
		return admissionDate;
	}

	public void setAdmissionDate(String admissionDate) {
		this.admissionDate = admissionDate;
	}

	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public String getPreauthAmt() {
		return preauthAmt;
	}

	public void setPreauthAmt(String preauthAmt) {
		this.preauthAmt = preauthAmt;
	}

	public String getPreauthApprovalDate() {
		return preauthApprovalDate;
	}

	public void setPreauthApprovalDate(String preauthApprovalDate) {
		this.preauthApprovalDate = preauthApprovalDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRejectDate() {
		return rejectDate;
	}

	public void setRejectDate(String rejectDate) {
		this.rejectDate = rejectDate;
	}

	public String getCloseStage() {
		return closeStage;
	}

	public void setCloseStage(String closeStage) {
		this.closeStage = closeStage;
	}

	public String getRejectionRemarks() {
		return rejectionRemarks;
	}

	public void setRejectionRemarks(String rejectionRemarks) {
		this.rejectionRemarks = rejectionRemarks;
	}

	public String getBillReceivedDate() {
		return billReceivedDate;
	}

	public void setBillReceivedDate(String billReceivedDate) {
		this.billReceivedDate = billReceivedDate;
	}

	public String getInitialProvisionAmount() {
		return initialProvisionAmount;
	}

	public void setInitialProvisionAmount(String initialProvisionAmount) {
		this.initialProvisionAmount = initialProvisionAmount;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNoofTimeBillRec() {
		return noofTimeBillRec;
	}

	public void setNoofTimeBillRec(String noofTimeBillRec) {
		this.noofTimeBillRec = noofTimeBillRec;
	}

	public String getMaApprovedDate() {
		return maApprovedDate;
	}

	public void setMaApprovedDate(String maApprovedDate) {
		this.maApprovedDate = maApprovedDate;
	}

	public String getQueryRaisedDate() {
		return queryRaisedDate;
	}

	public void setQueryRaisedDate(String queryRaisedDate) {
		this.queryRaisedDate = queryRaisedDate;
	}

	public String getQueryRaisedRemarks() {
		return queryRaisedRemarks;
	}

	public void setQueryRaisedRemarks(String queryRaisedRemarks) {
		this.queryRaisedRemarks = queryRaisedRemarks;
	}

	public String getQueryReplyDate() {
		return queryReplyDate;
	}

	public void setQueryReplyDate(String queryReplyDate) {
		this.queryReplyDate = queryReplyDate;
	}

	public String getQueryReplyRemarks() {
		return queryReplyRemarks;
	}

	public void setQueryReplyRemarks(String queryReplyRemarks) {
		this.queryReplyRemarks = queryReplyRemarks;
	}

	public String getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(String closeDate) {
		this.closeDate = closeDate;
	}

	public String getCurrentProvisionAmount() {
		return currentProvisionAmount;
	}

	public void setCurrentProvisionAmount(String currentProvisionAmount) {
		this.currentProvisionAmount = currentProvisionAmount;
	}

	public String getClosedRemarks() {
		return closedRemarks;
	}

	public void setClosedRemarks(String closedRemarks) {
		this.closedRemarks = closedRemarks;
	}

	public String getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(String billAmount) {
		this.billAmount = billAmount;
	}

	public String getBillingApprovedAmount() {
		return billingApprovedAmount;
	}

	public void setBillingApprovedAmount(String billingApprovedAmount) {
		this.billingApprovedAmount = billingApprovedAmount;
	}

	public String getTat() {
		return tat;
	}

	public void setTat(String tat) {
		this.tat = tat;
	}

	public Long getReimbursementKey() {
		return reimbursementKey;
	}

	public void setReimbursementKey(Long reimbursementKey) {
		this.reimbursementKey = reimbursementKey;
	}

	public String getHospCity() {
		return hospCity;
	}

	public String getDocRecvdFrom() {
		return docRecvdFrom;
	}

	public String getStatusValue() {
		return statusValue;
	}

	public String getSmCode() {
		return smCode;
	}

	public String getSmName() {
		return smName;
	}

	public String getAgentCode() {
		return agentCode;
	}

	public String getAgentName() {
		return agentName;
	}

	public String getOffiCode() {
		return offiCode;
	}

	public String getOffiName() {
		return offiName;
	}

	public void setHospCity(String hospCity) {
		this.hospCity = hospCity;
	}

	public void setDocRecvdFrom(String docRecvdFrom) {
		this.docRecvdFrom = docRecvdFrom;
	}

	public void setStatusValue(String statusValue) {
		this.statusValue = statusValue;
	}

	public void setSmCode(String smCode) {
		this.smCode = smCode;
	}

	public void setSmName(String smName) {
		this.smName = smName;
	}

	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public void setOffiCode(String offiCode) {
		this.offiCode = offiCode;
	}

	public void setOffiName(String offiName) {
		this.offiName = offiName;
	}

	public String getCpuName() {
		return cpuName;
	}

	public void setCpuName(String cpuName) {
		this.cpuName = cpuName;
	}

	public String getWithDrawRemarks() {
		return withDrawRemarks;
	}

	public void setWithDrawRemarks(String withDrawRemarks) {
		this.withDrawRemarks = withDrawRemarks;
	}	
	
}
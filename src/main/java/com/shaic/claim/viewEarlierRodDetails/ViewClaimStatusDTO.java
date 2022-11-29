package com.shaic.claim.viewEarlierRodDetails;

import java.util.Date;
import java.util.List;

import com.shaic.claim.enhacement.table.PreviousPreAuthTableDTO;
import com.shaic.claim.intimation.CashLessTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthMedicalDecisionDTO;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class ViewClaimStatusDTO extends AbstractSearchDTO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long intimationKey;
	
	private Integer sno;
	
	private Long claimKey;

	private String intimationId;
	
	private String dateOfIntimation;
	
	private String cpuId;
	
	private String intimationMode;
	
	private Long rodKey;
	
	private String intimatedBy;
	
	private String insuredPatientName;
	
	private String healthCardNo;
	
	private String patientNotCoveredName;
	
	private String relationshipWithInsuredId;
	
	private Date admissionDate;
	
	private String admissionDateStr;
	
	private String admissionType;
	
	private String reasonForAdmission;
	
	private String inpatientNumber;
	
	private String lateIntimationReason;
	
	private String policyNumber;
	
	private String policyIssuing;
	
	private String productName;
	
	private String patientName;
	
	private String state;
	
	private String city;
	
	private String area;
	
	private String hospitalName;
	
	private String hospitalAddress;
	
	private String billingType;
	
	private String billingDate;
	
	private String billAssessmentAmt;
	
	private String status;
	
	//Payement details;
	
	private String claimProcessType;
	
	private String typeOfPayment;
	
	private String bankName;
	
	private String utrNumber;
	
	private String accountName;
	
	private String chequeNumber;
	
	private String branchName;
	
	private String neftDate;
	
	private String chequeDate;
	
	private CashLessTableDTO cashlessTableDetails;
	
	private List<ViewDocumentDetailsDTO> receiptOfDocumentValues;
	
	private ClaimStatusRegistrationDTO claimStatusRegistrionDetails;
	
	private List<PreviousPreAuthTableDTO> previousPreAuthTableDTO;
	
	private PreauthDTO preauthDTO;
	
	private String rodNumber;
	
	private String benefitCover;
	
	private String docReceivedFrom;
	
	private String rodType;
	
	private String typeOfClaim;
	
	private String billClassification;
	
	private String hospitalCategory;
	
	private boolean isJioPolicy;
	
	private String employeeCode;
	
	private String hospitalFlag;
	
	private String billDtslToken;
	
	private String suspiciousReason;

	private PreauthMedicalDecisionDTO preauthMedicalDecisionDetails;
	
	public String getRodType() {
		return rodType;
	}

	public void setRodType(String rodType) {
		this.rodType = rodType;
	}

	public String getRodNumber() {
		return rodNumber;
	}

	public void setRodNumber(String rodNumber) {
		this.rodNumber = rodNumber;
	}

	public String getBenefitCover() {
		return benefitCover;
	}

	public void setBenefitCover(String benefitCover) {
		this.benefitCover = benefitCover;
	}

	public String getDocReceivedFrom() {
		return docReceivedFrom;
	}

	public void setDocReceivedFrom(String docReceivedFrom) {
		this.docReceivedFrom = docReceivedFrom;
	}

	public ViewClaimStatusDTO(){
		
		cashlessTableDetails = new CashLessTableDTO();
		claimStatusRegistrionDetails = new ClaimStatusRegistrationDTO();
	}
	
	public String getIntimationId() {
		return intimationId;
	}

	public void setIntimationId(String intimationId) {
		this.intimationId = intimationId;
	}

	public String getDateOfIntimation() {
		return dateOfIntimation;
	}

	public void setDateOfIntimation(String dateOfIntimation) {
		this.dateOfIntimation = dateOfIntimation;
	}

	

	public String getCpuId() {
		return cpuId;
	}

	public void setCpuId(String cpuId) {
		this.cpuId = cpuId;
	}

	public String getIntimationMode() {
		return intimationMode;
	}

	public void setIntimationMode(String intimationMode) {
		this.intimationMode = intimationMode;
	}

	public String getIntimatedBy() {
		return intimatedBy;
	}

	public void setIntimatedBy(String intimatedBy) {
		this.intimatedBy = intimatedBy;
	}

	public String getInsuredPatientName() {
		return insuredPatientName;
	}

	public void setInsuredPatientName(String insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
	}

	public String getHealthCardNo() {
		return healthCardNo;
	}

	public void setHealthCardNo(String healthCardNo) {
		this.healthCardNo = healthCardNo;
	}

	public String getPatientNotCoveredName() {
		return patientNotCoveredName;
	}

	public void setPatientNotCoveredName(String patientNotCoveredName) {
		this.patientNotCoveredName = patientNotCoveredName;
	}

	public String getRelationshipWithInsuredId() {
		return relationshipWithInsuredId;
	}

	public void setRelationshipWithInsuredId(String relationshipWithInsuredId) {
		this.relationshipWithInsuredId = relationshipWithInsuredId;
	}

	public Date getAdmissionDate() {
		return admissionDate;
	}

	public void setAdmissionDate(Date admissionDate) {
		this.admissionDate = admissionDate;
	}

	public String getAdmissionType() {
		return admissionType;
	}

	public void setAdmissionType(String admissionType) {
		this.admissionType = admissionType;
	}

	public String getReasonForAdmission() {
		return reasonForAdmission;
	}

	public void setReasonForAdmission(String reasonForAdmission) {
		this.reasonForAdmission = reasonForAdmission;
	}

	public String getInpatientNumber() {
		return inpatientNumber;
	}

	public void setInpatientNumber(String inpatientNumber) {
		this.inpatientNumber = inpatientNumber;
	}

	public String getLateIntimationReason() {
		return lateIntimationReason;
	}

	public void setLateIntimationReason(String lateIntimationReason) {
		this.lateIntimationReason = lateIntimationReason;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getPolicyIssuing() {
		return policyIssuing;
	}

	public void setPolicyIssuing(String policyIssuing) {
		this.policyIssuing = policyIssuing;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getHospitalAddress() {
		return hospitalAddress;
	}

	public void setHospitalAddress(String hospitalAddress) {
		this.hospitalAddress = hospitalAddress;
	}

	public String getHospitalTypeValue() {
		return hospitalTypeValue;
	}

	public void setHospitalTypeValue(String hospitalTypeValue) {
		this.hospitalTypeValue = hospitalTypeValue;
	}

	public String getHospitalInternalCode() {
		return hospitalInternalCode;
	}

	public void setHospitalInternalCode(String hospitalInternalCode) {
		this.hospitalInternalCode = hospitalInternalCode;
	}

	public String getHospitalIrdaCode() {
		return hospitalIrdaCode;
	}

	public void setHospitalIrdaCode(String hospitalIrdaCode) {
		this.hospitalIrdaCode = hospitalIrdaCode;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getSmCode() {
		return smCode;
	}

	public void setSmCode(String smCode) {
		this.smCode = smCode;
	}

	public String getSmName() {
		return smName;
	}

	public void setSmName(String smName) {
		this.smName = smName;
	}

	public String getAgentBrokerCode() {
		return agentBrokerCode;
	}

	public void setAgentBrokerCode(String agentBrokerCode) {
		this.agentBrokerCode = agentBrokerCode;
	}

	public String getAgentBrokerName() {
		return agentBrokerName;
	}

	public void setAgentBrokerName(String agentBrokerName) {
		this.agentBrokerName = agentBrokerName;
	}

	private String hospitalTypeValue;
	
	private String hospitalInternalCode;
	
	private String hospitalIrdaCode;
	
	private String comments;
	
	private String smCode;
	
	private String smName;
	
	private String agentBrokerCode;
	
	private String agentBrokerName;
		
	private String intimatorName;
	
	private String callerMobileNumber;
	
	private String callerAddress;
	
	private String callerEmail;
	
	private String roomCategory;
	
	private String hospitalPhoneNo;
	
	private String hospitalFaxNo;
	
	private String hospitalDoctorName;
	
	public CashLessTableDTO getCashlessTableDetails() {
		return cashlessTableDetails;
	}

	public void setCashlessTableDetails(CashLessTableDTO cashlessTableDetails) {
		this.cashlessTableDetails = cashlessTableDetails;
	}

	public ClaimStatusRegistrationDTO getClaimStatusRegistrionDetails() {
		return claimStatusRegistrionDetails;
	}

	public void setClaimStatusRegistrionDetails(
			ClaimStatusRegistrationDTO claimStatusRegistrionDetails) {
		this.claimStatusRegistrionDetails = claimStatusRegistrionDetails;
	}

	public List<PreviousPreAuthTableDTO> getPreviousPreAuthTableDTO() {
		return previousPreAuthTableDTO;
	}

	public String getBillingType() {
		return billingType;
	}

	public void setBillingType(String billingType) {
		this.billingType = billingType;
	}

	public String getTypeOfPayment() {
		return typeOfPayment;
	}

	public void setTypeOfPayment(String typeOfPayment) {
		this.typeOfPayment = typeOfPayment;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getUtrNumber() {
		return utrNumber;
	}

	public void setUtrNumber(String utrNumber) {
		this.utrNumber = utrNumber;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getChequeNumber() {
		return chequeNumber;
	}

	public void setChequeNumber(String chequeNumber) {
		this.chequeNumber = chequeNumber;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getNeftDate() {
		return neftDate;
	}

	public void setNeftDate(String neftDate) {
		this.neftDate = neftDate;
	}

	public String getChequeDate() {
		return chequeDate;
	}

	public void setChequeDate(String chequeDate) {
		this.chequeDate = chequeDate;
	}

	public String getBillAssessmentAmt() {
		return billAssessmentAmt;
	}

	public void setBillAssessmentAmt(String billAssessmentAmt) {
		this.billAssessmentAmt = billAssessmentAmt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setPreviousPreAuthTableDTO(
			List<PreviousPreAuthTableDTO> previousPreAuthTableDTO) {
		this.previousPreAuthTableDTO = previousPreAuthTableDTO;
	}

	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}

	public List<ViewDocumentDetailsDTO> getReceiptOfDocumentValues() {
		return receiptOfDocumentValues;
	}

	public void setReceiptOfDocumentValues(
			List<ViewDocumentDetailsDTO> receiptOfDocumentValues) {
		this.receiptOfDocumentValues = receiptOfDocumentValues;
	}

	public Integer getSno() {
		return sno;
	}

	public void setSno(Integer sno) {
		this.sno = sno;
	}


	public Long getIntimationKey() {
		return intimationKey;
	}

	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
	}

	public Long getRodKey() {
		return rodKey;
	}

	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}

	public String getAdmissionDateStr() {
		return admissionDateStr;
	}

	public void setAdmissionDateStr(String admissionDateStr) {
		this.admissionDateStr = admissionDateStr;
	}

	public String getBillingDate() {
		return billingDate;
	}

	public void setBillingDate(String billingDate) {
		this.billingDate = billingDate;
	}
	public String getClaimProcessType() {
		return claimProcessType;
	}

	public void setClaimProcessType(String claimProcessType) {
		this.claimProcessType = claimProcessType;
	}

	public String getTypeOfClaim() {
		return typeOfClaim;
	}

	public void setTypeOfClaim(String typeOfClaim) {
		this.typeOfClaim = typeOfClaim;
	}

	public String getBillClassification() {
		return billClassification;
	}

	public void setBillClassification(String billClassification) {
		this.billClassification = billClassification;
	}

	public String getHospitalCategory() {
		return hospitalCategory;
	}

	public void setHospitalCategory(String hospitalCategory) {
		this.hospitalCategory = hospitalCategory;
	}

	public boolean isJioPolicy() {
		return isJioPolicy;
	}

	public void setJioPolicy(boolean isJioPolicy) {
		this.isJioPolicy = isJioPolicy;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getIntimatorName() {
		return intimatorName;
	}

	public void setIntimatorName(String intimatorName) {
		this.intimatorName = intimatorName;
	}

	public String getCallerMobileNumber() {
		return callerMobileNumber;
	}

	public void setCallerMobileNumber(String callerMobileNumber) {
		this.callerMobileNumber = callerMobileNumber;
	}

	public String getCallerAddress() {
		return callerAddress;
	}

	public void setCallerAddress(String callerAddress) {
		this.callerAddress = callerAddress;
	}

	public String getCallerEmail() {
		return callerEmail;
	}

	public void setCallerEmail(String callerEmail) {
		this.callerEmail = callerEmail;
	}

	public String getRoomCategory() {
		return roomCategory;
	}

	public void setRoomCategory(String roomCategory) {
		this.roomCategory = roomCategory;
	}

	public String getHospitalPhoneNo() {
		return hospitalPhoneNo;
	}

	public void setHospitalPhoneNo(String hospitalPhoneNo) {
		this.hospitalPhoneNo = hospitalPhoneNo;
	}

	public String getHospitalFaxNo() {
		return hospitalFaxNo;
	}

	public void setHospitalFaxNo(String hospitalFaxNo) {
		this.hospitalFaxNo = hospitalFaxNo;
	}

	public String getHospitalDoctorName() {
		return hospitalDoctorName;
	}

	public void setHospitalDoctorName(String hospitalDoctorName) {
		this.hospitalDoctorName = hospitalDoctorName;
	}

	public String getHospitalFlag() {
		return hospitalFlag;
	}

	public void setHospitalFlag(String hospitalFlag) {
		this.hospitalFlag = hospitalFlag;
	}

	public PreauthDTO getPreauthDTO() {
		return preauthDTO;
	}

	public void setPreauthDTO(PreauthDTO preauthDTO) {
		this.preauthDTO = preauthDTO;
	}

	public String getBillDtslToken() {
		return billDtslToken;
	}

	public void setBillDtslToken(String billDtslToken) {
		this.billDtslToken = billDtslToken;
	}

	public PreauthMedicalDecisionDTO getPreauthMedicalDecisionDetails() {
		return preauthMedicalDecisionDetails;
	}

	public void setPreauthMedicalDecisionDetails(
			PreauthMedicalDecisionDTO preauthMedicalDecisionDetails) {
		this.preauthMedicalDecisionDetails = preauthMedicalDecisionDetails;
	}
	
	public String getSuspiciousReason() {
		return suspiciousReason;
	}

	public void setSuspiciousReason(String suspiciousReason) {
		this.suspiciousReason = suspiciousReason;
	}
}

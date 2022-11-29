/**
 * 
 */
package com.shaic.claim.rod.wizard.dto;

import java.util.Date;
import java.util.List;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.reimbursement.billing.dto.PatientCareDTO;
import com.shaic.newcode.wizard.dto.LegalHeirDTO;

/**
 * @author ntv.vijayar
 * 
 * This DTO is mapped with Earlier request
 * to be reconsidered table.
 */
public class ReconsiderRODRequestTableDTO extends AbstractTableDTO{
	
	private Integer slNo;
	
	private String acknowledgementNo;
	
	private String rodNo;
	
	private String documentReceivedFrom;
	
	private Date documentReceivedDate;
	
	private String modeOfReceipt;
	
	private String billClassification;
	
	private String hospitalizationFlag;
	
	private String preHospitalizationFlag;
	
	private String postHospitalizationFlag;
	
	private String partialHospitalizationFlag;
	
	private String lumpSumAmountFlag;

	private String addOnBenefitsHospitalCashFlag;

	private String addOnBenefitsPatientCareFlag;
	
	private String hospitalizationRepeatFlag;
	
	private String benefitFlag;
	private String otherBenefitFlag;
	
	private String emergencyMedicalEvaluationFlag;
	
	private String compassionateTravelFlag;

	private String repatriationOfMortalRemainsFlag;
	
	private String preferredNetworkHospitalFlag;
	
	private String sharedAccomodationFlag;	


	//private Double claimedAmt;
	
	private Double approvedAmt;
	
	private String rodStatus;
	
	private Long rodKey;
	
	private Long key;
	
	private Boolean select;
	
	private Double hospitalizationClaimedAmt;
	
	private Double preHospClaimedAmt;
	
	private Double postHospClaimedAmt;
	
	private Double claimedAmt;
	
	private Double benefitClaimedAmnt;
	private Double otherBenefitClaimedAmnt;
	
	
	private String intimationNo;

	
	private Double billingApprovedAmount;
	
	private Double financialApprovedAmount;
	
	private Long statusId;
	
	private Long docAcknowledgementKey;
	
	private Long hospitalCashNoOfDaysBills;
	
	private Double hospitalCashPerDayAmtBills;
	
	private Double hospitalCashTotalClaimedAmount;
	
	private Long patientCareNoOfDaysBills;
	
	private Double patientCarePerDayAmtBills;
	
	private Double patientCareTotalClaimedAmount;
	
	private Long hospitalCashReimbursementBenefitsKey;
	
	private Long patientCareReimbursementBenefitsKey;
	
	private List<PatientCareDTO> patientCareDTOList;
	
	private Long stageId;
	
	private String emailId;
	
	private String accountNo;
	
	private String payableAt;
	
	private String payeeName;
	
	private String reasonForChange;
	
	private String leagalHeirName;
	
	private String panNo;
	
	private String ifscCode;
	
	private String bankName;
	
	private String branchName;
	
	private String city;
	
	private Boolean isRejectReconsidered = false;
	
 	private String benifitOrCover;	
 	
 	private Double benefitsApprovedAmt = 0d;
 	
 	private String processClmType;
 	
 	private String reconsiderationFlag;
 	
 	private SelectValue patientStatus;
 	
 	private List<LegalHeirDTO> legalHeirDTOList;

 	private String legalHeirName;
 	
 	private String legalHeirAddr;
 	
 	public String getProcessClmType() {
		return processClmType;
	}

	public void setProcessClmType(String processClmType) {
		this.processClmType = processClmType;
	}

	public Double getBenefitsApprovedAmt() {
		return benefitsApprovedAmt;
	}

	public void setBenefitsApprovedAmt(Double benefitsApprovedAmt) {
		this.benefitsApprovedAmt = benefitsApprovedAmt;
	}

	public Double getAddOnApprovedAmt() {
		return addOnApprovedAmt;
	}

	public void setAddOnApprovedAmt(Double addOnApprovedAmt) {
		this.addOnApprovedAmt = addOnApprovedAmt;
	}

	public Double getOptionalApprovedAmt() {
		return optionalApprovedAmt;
	}

	public void setOptionalApprovedAmt(Double optionalApprovedAmt) {
		this.optionalApprovedAmt = optionalApprovedAmt;
	}

	private Double addOnApprovedAmt = 0d;
 	
 	private Double optionalApprovedAmt = 0d;
 	
	private Boolean isSettledReconsideration = false;
	
	private String classification;
	
	private String subclassification;

	private Date negotiationReqstDate;
	
	private Date negotiationCompletDate;
	
	private String nameOfNegotiatior;
	
	private Double agreedAmount;
	
	private String negotiationRemarks;
	
	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getPayableAt() {
		return payableAt;
	}

	public void setPayableAt(String payableAt) {
		this.payableAt = payableAt;
	}

	public String getPayeeName() {
		return payeeName;
	}

	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}

	public String getReasonForChange() {
		return reasonForChange;
	}

	public void setReasonForChange(String reasonForChange) {
		this.reasonForChange = reasonForChange;
	}

	public String getLeagalHeirName() {
		return leagalHeirName;
	}

	public void setLeagalHeirName(String leagalHeirName) {
		this.leagalHeirName = leagalHeirName;
	}

	public String getPanNo() {
		return panNo;
	}

	public void setPanNo(String panNo) {
		this.panNo = panNo;
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

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getSlNo() {
		return slNo;
	}

	public void setSlNo(Integer slNo) {
		this.slNo = slNo;
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

	public Double getApprovedAmt() {
		return approvedAmt;
	}

	public void setApprovedAmt(Double approvedAmt) {
		this.approvedAmt = approvedAmt;
	}

	public String getRodStatus() {
		return rodStatus;
	}

	public void setRodStatus(String rodStatus) {
		this.rodStatus = rodStatus;
	}

	/**
	 * @return the hospitalizationFlag
	 */
	public String getHospitalizationFlag() {
		return hospitalizationFlag;
	}

	/**
	 * @param hospitalizationFlag the hospitalizationFlag to set
	 */
	public void setHospitalizationFlag(String hospitalizationFlag) {
		this.hospitalizationFlag = hospitalizationFlag;
	}

	/**
	 * @return the preHospitalizationFlag
	 */
	public String getPreHospitalizationFlag() {
		return preHospitalizationFlag;
	}

	/**
	 * @param preHospitalizationFlag the preHospitalizationFlag to set
	 */
	public void setPreHospitalizationFlag(String preHospitalizationFlag) {
		this.preHospitalizationFlag = preHospitalizationFlag;
	}

	/**
	 * @return the postHospitalizationFlag
	 */
	public String getPostHospitalizationFlag() {
		return postHospitalizationFlag;
	}

	/**
	 * @param postHospitalizationFlag the postHospitalizationFlag to set
	 */
	public void setPostHospitalizationFlag(String postHospitalizationFlag) {
		this.postHospitalizationFlag = postHospitalizationFlag;
	}

	/**
	 * @return the partialHospitalizationFlag
	 */
	public String getPartialHospitalizationFlag() {
		return partialHospitalizationFlag;
	}

	/**
	 * @param partialHospitalizationFlag the partialHospitalizationFlag to set
	 */
	public void setPartialHospitalizationFlag(String partialHospitalizationFlag) {
		this.partialHospitalizationFlag = partialHospitalizationFlag;
	}

	/**
	 * @return the lumpSumAmountFlag
	 */
	public String getLumpSumAmountFlag() {
		return lumpSumAmountFlag;
	}

	/**
	 * @param lumpSumAmountFlag the lumpSumAmountFlag to set
	 */
	public void setLumpSumAmountFlag(String lumpSumAmountFlag) {
		this.lumpSumAmountFlag = lumpSumAmountFlag;
	}

	/**
	 * @return the addOnBenefitsHospitalCashFlag
	 */
	public String getAddOnBenefitsHospitalCashFlag() {
		return addOnBenefitsHospitalCashFlag;
	}

	/**
	 * @param addOnBenefitsHospitalCashFlag the addOnBenefitsHospitalCashFlag to set
	 */
	public void setAddOnBenefitsHospitalCashFlag(
			String addOnBenefitsHospitalCashFlag) {
		this.addOnBenefitsHospitalCashFlag = addOnBenefitsHospitalCashFlag;
	}

	/**
	 * @return the addOnBenefitsPatientCareFlag
	 */
	public String getAddOnBenefitsPatientCareFlag() {
		return addOnBenefitsPatientCareFlag;
	}

	/**
	 * @param addOnBenefitsPatientCareFlag the addOnBenefitsPatientCareFlag to set
	 */
	public void setAddOnBenefitsPatientCareFlag(String addOnBenefitsPatientCareFlag) {
		this.addOnBenefitsPatientCareFlag = addOnBenefitsPatientCareFlag;
	}

	
	/**
	 * @return the key
	 */
	public Long getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(Long key) {
		this.key = key;
	}

	/**
	 * @return the rodKey
	 */
	public Long getRodKey() {
		return rodKey;
	}

	/**
	 * @param rodKey the rodKey to set
	 */
	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}

	/**
	 * @return the select
	 */
	public Boolean getSelect() {
		return select;
	}

	/**
	 * @param select the select to set
	 */
	public void setSelect(Boolean select) {
		this.select = select;
	}

	public Double getHospitalizationClaimedAmt() {
		return hospitalizationClaimedAmt;
	}

	public void setHospitalizationClaimedAmt(Double hospitalizationClaimedAmt) {
		this.hospitalizationClaimedAmt = hospitalizationClaimedAmt;
	}

	public Double getPreHospClaimedAmt() {
		return preHospClaimedAmt;
	}

	public void setPreHospClaimedAmt(Double preHospClaimedAmt) {
		this.preHospClaimedAmt = preHospClaimedAmt;
	}

	public Double getPostHospClaimedAmt() {
		return postHospClaimedAmt;
	}

	public void setPostHospClaimedAmt(Double postHospClaimedAmt) {
		this.postHospClaimedAmt = postHospClaimedAmt;
	}

	public Double getClaimedAmt() {
		return claimedAmt;
	}

	public void setClaimedAmt(Double claimedAmt) {
		this.claimedAmt = claimedAmt;
	}

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public Double getBillingApprovedAmount() {
		return billingApprovedAmount;
	}

	public void setBillingApprovedAmount(Double billingApprovedAmount) {
		this.billingApprovedAmount = billingApprovedAmount;
	}

	public Double getFinancialApprovedAmount() {
		return financialApprovedAmount;
	}

	public void setFinancialApprovedAmount(Double financialApprovedAmount) {
		this.financialApprovedAmount = financialApprovedAmount;
	}

	public Long getStatusId() {
		return statusId;
	}

	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}

	public String getHospitalizationRepeatFlag() {
		return hospitalizationRepeatFlag;
	}

	public void setHospitalizationRepeatFlag(String hospitalizationRepeatFlag) {
		this.hospitalizationRepeatFlag = hospitalizationRepeatFlag;
	}

	public Long getDocAcknowledgementKey() {
		return docAcknowledgementKey;
	}

	public void setDocAcknowledgementKey(Long docAcknowledgementKey) {
		this.docAcknowledgementKey = docAcknowledgementKey;
	}

	public Long getHospitalCashNoOfDaysBills() {
		return hospitalCashNoOfDaysBills;
	}

	public void setHospitalCashNoOfDaysBills(Long hospitalCashNoOfDaysBills) {
		this.hospitalCashNoOfDaysBills = hospitalCashNoOfDaysBills;
	}

	public Double getHospitalCashPerDayAmtBills() {
		return hospitalCashPerDayAmtBills;
	}

	public void setHospitalCashPerDayAmtBills(Double hospitalCashPerDayAmtBills) {
		this.hospitalCashPerDayAmtBills = hospitalCashPerDayAmtBills;
	}

	public Double getHospitalCashTotalClaimedAmount() {
		return hospitalCashTotalClaimedAmount;
	}

	public void setHospitalCashTotalClaimedAmount(
			Double hospitalCashTotalClaimedAmount) {
		this.hospitalCashTotalClaimedAmount = hospitalCashTotalClaimedAmount;
	}

	public Long getPatientCareNoOfDaysBills() {
		return patientCareNoOfDaysBills;
	}

	public void setPatientCareNoOfDaysBills(Long patientCareNoOfDaysBills) {
		this.patientCareNoOfDaysBills = patientCareNoOfDaysBills;
	}

	public Double getPatientCarePerDayAmtBills() {
		return patientCarePerDayAmtBills;
	}

	public void setPatientCarePerDayAmtBills(Double patientCarePerDayAmtBills) {
		this.patientCarePerDayAmtBills = patientCarePerDayAmtBills;
	}

	public Double getPatientCareTotalClaimedAmount() {
		return patientCareTotalClaimedAmount;
	}

	public void setPatientCareTotalClaimedAmount(
			Double patientCareTotalClaimedAmount) {
		this.patientCareTotalClaimedAmount = patientCareTotalClaimedAmount;
	}

/*	public Long getReimbursementBenefitsKey() {
		return reimbursementBenefitsKey;
	}

	public void setReimbursementBenefitsKey(Long reimbursementBenefitsKey) {
		this.reimbursementBenefitsKey = reimbursementBenefitsKey;
	}*/

	public Long getHospitalCashReimbursementBenefitsKey() {
		return hospitalCashReimbursementBenefitsKey;
	}

	public void setHospitalCashReimbursementBenefitsKey(
			Long hospitalCashReimbursementBenefitsKey) {
		this.hospitalCashReimbursementBenefitsKey = hospitalCashReimbursementBenefitsKey;
	}

	public Long getPatientCareReimbursementBenefitsKey() {
		return patientCareReimbursementBenefitsKey;
	}

	public void setPatientCareReimbursementBenefitsKey(
			Long patientCareReimbursementBenefitsKey) {
		this.patientCareReimbursementBenefitsKey = patientCareReimbursementBenefitsKey;
	}

	public List<PatientCareDTO> getPatientCareDTOList() {
		return patientCareDTOList;
	}

	public void setPatientCareDTOList(List<PatientCareDTO> patientCareDTOList) {
		this.patientCareDTOList = patientCareDTOList;
	}

	public Long getStageId() {
		return stageId;
	}

	public void setStageId(Long stageId) {
		this.stageId = stageId;
	}

	public Boolean getIsRejectReconsidered() {
		return isRejectReconsidered;
	}

	public void setIsRejectReconsidered(Boolean isRejectReconsidered) {
		this.isRejectReconsidered = isRejectReconsidered;
	}

	public String getBenifitOrCover() {
		return benifitOrCover;
	}

	public void setBenifitOrCover(String benifitOrCover) {
		this.benifitOrCover = benifitOrCover;
	}

	public Double getBenefitClaimedAmnt() {
		return benefitClaimedAmnt;
	}

	public void setBenefitClaimedAmnt(Double benefitClaimedAmnt) {
		this.benefitClaimedAmnt = benefitClaimedAmnt;
	}

	public String getBenefitFlag() {
		return benefitFlag;
	}

	public void setBenefitFlag(String benefitFlag) {
		this.benefitFlag = benefitFlag;
	}
	public Boolean getIsSettledReconsideration() {
		return isSettledReconsideration;
	}

	public void setIsSettledReconsideration(Boolean isSettledReconsideration) {
		this.isSettledReconsideration = isSettledReconsideration;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getSubclassification() {
		return subclassification;
	}

	public void setSubclassification(String subclassification) {
		this.subclassification = subclassification;
	}

	public Date getNegotiationReqstDate() {
		return negotiationReqstDate;
	}

	public void setNegotiationReqstDate(Date negotiationReqstDate) {
		this.negotiationReqstDate = negotiationReqstDate;
	}

	public Date getNegotiationCompletDate() {
		return negotiationCompletDate;
	}

	public void setNegotiationCompletDate(Date negotiationCompletDate) {
		this.negotiationCompletDate = negotiationCompletDate;
	}

	public String getNameOfNegotiatior() {
		return nameOfNegotiatior;
	}

	public void setNameOfNegotiatior(String nameOfNegotiatior) {
		this.nameOfNegotiatior = nameOfNegotiatior;
	}

	public Double getAgreedAmount() {
		return agreedAmount;
	}

	public void setAgreedAmount(Double agreedAmount) {
		this.agreedAmount = agreedAmount;
	}

	public String getNegotiationRemarks() {
		return negotiationRemarks;
	}

	public void setNegotiationRemarks(String negotiationRemarks) {
		this.negotiationRemarks = negotiationRemarks;
	}
	public String getEmergencyMedicalEvaluationFlag() {
		return emergencyMedicalEvaluationFlag;
	}

	public void setEmergencyMedicalEvaluationFlag(
			String emergencyMedicalEvaluationFlag) {
		this.emergencyMedicalEvaluationFlag = emergencyMedicalEvaluationFlag;
	}

	public String getCompassionateTravelFlag() {
		return compassionateTravelFlag;
	}

	public void setCompassionateTravelFlag(String compassionateTravelFlag) {
		this.compassionateTravelFlag = compassionateTravelFlag;
	}

	public String getRepatriationOfMortalRemainsFlag() {
		return repatriationOfMortalRemainsFlag;
	}

	public void setRepatriationOfMortalRemainsFlag(
			String repatriationOfMortalRemainsFlag) {
		this.repatriationOfMortalRemainsFlag = repatriationOfMortalRemainsFlag;
	}

	public String getPreferredNetworkHospitalFlag() {
		return preferredNetworkHospitalFlag;
	}

	public void setPreferredNetworkHospitalFlag(String preferredNetworkHospitalFlag) {
		this.preferredNetworkHospitalFlag = preferredNetworkHospitalFlag;
	}

	public String getSharedAccomodationFlag() {
		return sharedAccomodationFlag;
	}

	public void setSharedAccomodationFlag(String sharedAccomodationFlag) {
		this.sharedAccomodationFlag = sharedAccomodationFlag;
	}

	public Double getOtherBenefitClaimedAmnt() {
		return otherBenefitClaimedAmnt;
	}

	public void setOtherBenefitClaimedAmnt(Double otherBenefitClaimedAmnt) {
		this.otherBenefitClaimedAmnt = otherBenefitClaimedAmnt;
	}

	public String getOtherBenefitFlag() {
		return otherBenefitFlag;
	}

	public void setOtherBenefitFlag(String otherBenefitFlag) {
		this.otherBenefitFlag = otherBenefitFlag;
	}

	public String getReconsiderationFlag() {
		return reconsiderationFlag;
	}

	public void setReconsiderationFlag(String reconsiderationFlag) {
		this.reconsiderationFlag = reconsiderationFlag;
	}

	public SelectValue getPatientStatus() {
		return patientStatus;
	}

	public void setPatientStatus(SelectValue patientStatus) {
		this.patientStatus = patientStatus;
	}

	public List<LegalHeirDTO> getLegalHeirDTOList() {
		return legalHeirDTOList;
	}

	public void setLegalHeirDTOList(List<LegalHeirDTO> legalHeirDTOList) {
		this.legalHeirDTOList = legalHeirDTOList;
	}
	
	public String getLegalHeirName() {
		return this.legalHeirName;
	}
	
	public void setLegalHeirName(String legalHeirName) {
		this.legalHeirName = legalHeirName;
	}
	
	public String getLegalHeirAddr() {
		return this.legalHeirAddr;
	}
	
	public void setLegalHeirAddr(String legalHeirAddr) {
		this.legalHeirAddr = legalHeirAddr;
	}
	
}
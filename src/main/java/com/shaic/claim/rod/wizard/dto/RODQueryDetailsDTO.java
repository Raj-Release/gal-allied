/**
 * 
 */
package com.shaic.claim.rod.wizard.dto;

import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.DMSDocumentDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewQueryDTO;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.Reimbursement;

/**
 * @author ntv.vijayar
 * 
 * This DTO is mapped with View Query Details
 * Table. 
 *
 */

public class RODQueryDetailsDTO extends AbstractTableDTO {

	
	private Integer queryNo;
	
	private String acknowledgementNo;
	
	private String rodNo = "";
	
	private String documentReceivedFrom;
	
	private Date documentReceivedDate;
	
	private String diagnosis = "";
	
	private String queryRaisedRole;
	
	private Date queryRaisedDate;
	
	private String queryStatus;
	
	private String billClassification = "";
	private Double claimedAmount;
	
	private Long reimbursementKey;
	private Long reimbursementQueryKey;
	
	private  String replyStatus;
	
	private String docReceivedFrom;
	
	private Long acknowledgementKey;
	
	private Integer sno;
	
	private String acknowledgementContactNumber;
	
	//Added for enabling or disabling bill classification values in document ack screen and rod screen.
	
	private Double hospitalizationClaimedAmt;
	private Double preHospitalizationClaimedAmt;
	private Double  postHospitalizationClaimedAmt;
	private Double otherBenefitClaimedAmnt;
	
	private String hospitalizationFlag;
	
	private String preHospitalizationFlag;
	
	private String postHospitalizationFlag;
	
	private String addOnBenefitsLumpsumFlag;
	
	private String addOnBenefitsPatientCareFlag;
	
	private String addOnBeneftisHospitalCashFlag;
	
	private String hospitalizationRepeatFlag;
	
	private String partialHospitalizationFlag;
	
	private String otherBenefitsFlag;
	
	private String emergencyMedicalEvaluationFlag;
	
	private String compassionateTravelFlag;

	private String repatriationOfMortalRemainsFlag;
	
	private String preferredNetworkHospitalFlag;
	
	private String sharedAccomodationFlag;		
	
	private DocAcknowledgement docAcknowledgment;

	private Reimbursement reimbursement;
	
	private String queryReplyStatus;
	
	private Boolean onPageLoad = false;
	private String accNo;
	private String bankName;
	private String branchName;
	private String panNo;
	private String emailId;
	private String payableAt;
	private String reasonForchange;
	private String legalHairName;
	private String ifscCode;
	private String city;
	
	private String benifitOrCover;	
	private String queryType;
	private Long documentTypeId;
	private String benefitFlag;
	private Double benefitClaimedAmount;
	private String queryRaisedDateString;
	private ViewQueryDTO queryDetails;
	private DMSDocumentDTO dmsDocumentDto;
	
	private String relaInstalFlag;
	
	private String hospitalCashFlag;	
	
	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public String getPanNo() {
		return panNo;
	}

	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}

	

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPayableAt() {
		return payableAt;
	}

	public void setPayableAt(String payableAt) {
		this.payableAt = payableAt;
	}

	public String getReasonForchange() {
		return reasonForchange;
	}

	public void setReasonForchange(String reasonForchange) {
		this.reasonForchange = reasonForchange;
	}

	public String getLegalHairName() {
		return legalHairName;
	}

	public void setLegalHairName(String legalHairName) {
		this.legalHairName = legalHairName;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getAccNo() {
		return accNo;
	}

	public void setAccNo(String accNo) {
		this.accNo = accNo;
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

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public String getQueryRaisedRole() {
		return queryRaisedRole;
	}

	public void setQueryRaisedRole(String queryRaisedRole) {
		this.queryRaisedRole = queryRaisedRole;
	}

	public Date getQueryRaisedDate() {
		return queryRaisedDate;
	}

	public void setQueryRaisedDate(Date queryRaisedDate) {
		this.queryRaisedDate = queryRaisedDate;
	}

	public String getQueryStatus() {
		return queryStatus;
	}

	public void setQueryStatus(String queryStatus) {
		this.queryStatus = queryStatus;
	}

	public Integer getQueryNo() {
		return queryNo;
	}

	public void setQueryNo(Integer queryNo) {
		this.queryNo = queryNo;
	}

	public String getBillClassification() {
		return billClassification;
	}

	public void setBillClassification(String billClassification) {
		this.billClassification = billClassification;
	}

	public Double getClaimedAmount() {
		return claimedAmount;
	}

	public void setClaimedAmount(Double claimedAmount) {
		this.claimedAmount = claimedAmount;
	}

	public Long getReimbursementQueryKey() {
		return reimbursementQueryKey;
	}

	public void setReimbursementQueryKey(Long reimbursementQueryKey) {
		this.reimbursementQueryKey = reimbursementQueryKey;
	}

	public Long getReimbursementKey() {
		return reimbursementKey;
	}

	public void setReimbursementKey(Long reimbursementKey) {
		this.reimbursementKey = reimbursementKey;
	}

	public String getReplyStatus() {
		return replyStatus;
	}

	public void setReplyStatus(String replyStatus) {
		this.replyStatus = replyStatus;
	}

	public String getDocReceivedFrom() {
		return docReceivedFrom;
	}

	public void setDocReceivedFrom(String docReceivedFrom) {
		this.docReceivedFrom = docReceivedFrom;
	}

	public Long getAcknowledgementKey() {
		return acknowledgementKey;
	}

	public void setAcknowledgementKey(Long acknowledgementKey) {
		this.acknowledgementKey = acknowledgementKey;
	}

	public Double getHospitalizationClaimedAmt() {
		return hospitalizationClaimedAmt;
	}

	public void setHospitalizationClaimedAmt(Double hospitalizationClaimedAmt) {
		this.hospitalizationClaimedAmt = hospitalizationClaimedAmt;
	}

	public Double getPreHospitalizationClaimedAmt() {
		return preHospitalizationClaimedAmt;
	}

	public void setPreHospitalizationClaimedAmt(Double preHospitalizationClaimedAmt) {
		this.preHospitalizationClaimedAmt = preHospitalizationClaimedAmt;
	}

	public Double getPostHospitalizationClaimedAmt() {
		return postHospitalizationClaimedAmt;
	}

	public void setPostHospitalizationClaimedAmt(
			Double postHospitalizationClaimedAmt) {
		this.postHospitalizationClaimedAmt = postHospitalizationClaimedAmt;
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

	public String getAddOnBenefitsLumpsumFlag() {
		return addOnBenefitsLumpsumFlag;
	}

	public void setAddOnBenefitsLumpsumFlag(String addOnBenefitsLumpsumFlag) {
		this.addOnBenefitsLumpsumFlag = addOnBenefitsLumpsumFlag;
	}

	public String getAddOnBenefitsPatientCareFlag() {
		return addOnBenefitsPatientCareFlag;
	}

	public void setAddOnBenefitsPatientCareFlag(String addOnBenefitsPatientCareFlag) {
		this.addOnBenefitsPatientCareFlag = addOnBenefitsPatientCareFlag;
	}

	public String getAddOnBeneftisHospitalCashFlag() {
		return addOnBeneftisHospitalCashFlag;
	}

	public void setAddOnBeneftisHospitalCashFlag(
			String addOnBeneftisHospitalCashFlag) {
		this.addOnBeneftisHospitalCashFlag = addOnBeneftisHospitalCashFlag;
	}

	public String getHospitalizationRepeatFlag() {
		return hospitalizationRepeatFlag;
	}

	public void setHospitalizationRepeatFlag(String hospitalizationRepeatFlag) {
		this.hospitalizationRepeatFlag = hospitalizationRepeatFlag;
	}

	public String getPartialHospitalizationFlag() {
		return partialHospitalizationFlag;
	}

	public void setPartialHospitalizationFlag(String partialHospitalizationFlag) {
		this.partialHospitalizationFlag = partialHospitalizationFlag;
	}

	public String getAcknowledgementContactNumber() {
		return acknowledgementContactNumber;
	}

	public void setAcknowledgementContactNumber(String acknowledgementContactNumber) {
		this.acknowledgementContactNumber = acknowledgementContactNumber;
	}

	public Boolean getOnPageLoad() {
		return onPageLoad;
	}

	public void setOnPageLoad(Boolean onPageLoad) {
		this.onPageLoad = onPageLoad;
	}

	public DocAcknowledgement getDocAcknowledgment() {
		return docAcknowledgment;
	}

	public void setDocAcknowledgment(DocAcknowledgement docAcknowledgment) {
		this.docAcknowledgment = docAcknowledgment;
	}

	public Reimbursement getReimbursement() {
		return reimbursement;
	}

	public void setReimbursement(Reimbursement reimbursement) {
		this.reimbursement = reimbursement;
	}

	public String getQueryReplyStatus() {
		return queryReplyStatus;
	}

	public void setQueryReplyStatus(String queryReplyStatus) {
		this.queryReplyStatus = queryReplyStatus;
	}

	public String getBenifitOrCover() {
		return benifitOrCover;
	}

	public void setBenifitOrCover(String benifitOrCover) {
		this.benifitOrCover = benifitOrCover;
	}

	public Long getDocumentTypeId() {
		return documentTypeId;
	}

	public void setDocumentTypeId(Long documentTypeId) {
		this.documentTypeId = documentTypeId;
	}

	public String getBenefitFlag() {
		return benefitFlag;
	}

	public void setBenefitFlag(String benefitFlag) {
		this.benefitFlag = benefitFlag;
	}
	public Integer getSno() {
		return sno;
	}

	public void setSno(Integer sno) {
		this.sno = sno;
	}

	public Double getBenefitClaimedAmount() {
		return benefitClaimedAmount;
	}

	public void setBenefitClaimedAmount(Double benefitClaimedAmount) {
		this.benefitClaimedAmount = benefitClaimedAmount;
	}


	public String getOtherBenefitsFlag() {
		return otherBenefitsFlag;
	}

	public void setOtherBenefitsFlag(String otherBenefitsFlag) {
		this.otherBenefitsFlag = otherBenefitsFlag;
	}

	public Double getOtherBenefitClaimedAmnt() {
		return otherBenefitClaimedAmnt;
	}

	public void setOtherBenefitClaimedAmnt(Double otherBenefitClaimedAmnt) {
		this.otherBenefitClaimedAmnt = otherBenefitClaimedAmnt;
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

	public String getQueryRaisedDateString() {
		return queryRaisedDateString;
	}

	public void setQueryRaisedDateString(String queryRaisedDateString) {
		this.queryRaisedDateString = queryRaisedDateString;
	}

	public ViewQueryDTO getQueryDetails() {
		return queryDetails;
	}

	public void setQueryDetails(ViewQueryDTO queryDetails) {
		this.queryDetails = queryDetails;
	}

	public DMSDocumentDTO getDmsDocumentDto() {
		return dmsDocumentDto;
	}

	public void setDmsDocumentDto(DMSDocumentDTO dmsDocumentDto) {
		this.dmsDocumentDto = dmsDocumentDto;
	}

	public String getRelaInstalFlag() {
		return relaInstalFlag;
	}

	public void setRelaInstalFlag(String relaInstalFlag) {
		this.relaInstalFlag = relaInstalFlag;
	}

	public String getHospitalCashFlag() {
		return hospitalCashFlag;
	}

	public void setHospitalCashFlag(String hospitalCashFlag) {
		this.hospitalCashFlag = hospitalCashFlag;
	}		

	
}

package com.shaic.claim.viewEarlierRodDetails;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.Intimation;
import com.shaic.domain.OMPDocAcknowledgement;
import com.shaic.domain.Reimbursement;

public class ViewDocumentDetailsDTO extends AbstractSearchDTO implements Serializable {
	
	/**
	 * "sno","rodNumber","receivedFromValue","documentReceivedDate","modeOfReceipt",
		"eventCode","classification","subClassification","approvedAmount","status"
	 */
	private static final long serialVersionUID = 1L;

	private Long key;
	
	private Integer sno;
	
	private Long latestKey;
	
	private String acknowledgeNumber;
	
	private Long personContactNumber;
	
	private String rodNumber;
	
	private SelectValue receivedFrom;
	
	private String receivedFromValue;
	
	private Date documentReceivedDate;
	
	private String strDocumentReceivedDate;
	
	private SelectValue modeOfReceipt;
	
	private String billClassification;
	
	private Double approvedAmount;
	
	private String status;
	
	private Long statusKey;
	
	private String medicalResponseTime;
	
	private Date medicalCompleteDate;
	
	private Claim claim;
	
	private ClaimDto claimDto;
	
	private SelectValue modeOfPayment;
	
	private String personemailID;
	
	private String rodKey;
	
	private String modeOfReceiptValue;
	
	private Boolean isReadOnly = false;
	
	@NotNull(message = "Please Select Closing Reason")
	private SelectValue closingReason;
	
	
	@NotNull(message = "Please Enter Closing Remarks")
	@Size(min = 1 , message = "Please Enter Closing Remarks")
	private String closingRemarks;

	private String additionalRemarks;
	
	private Intimation intimation;
	
	private Boolean closeClaimStatus;
	
	private Long reimbursementKey;
	
	private List<ViewDocumentDetailsDTO> documentDetailsList;
	
	private Integer taskNumber;
	
	private String intimationNumber;
	
	private DocAcknowledgement docAcknowledgement;
	
	private Date lastDocumentReceivedDate;
	
	private String strLastDocumentReceivedDate;
	
	private String classification;
	
	private String subclassification;
	
	private OMPDocAcknowledgement ompDocAcknowledgement;
	
	private String eventCode;
	
	private String corpBufferUtilised;

	private String benefits;
	
	private String typeOfClaim;
	
	private String rodType;
	
	private double claimedAmount;
	
	private Double totalBillAmount = 0d;
	
	private Long rodVersion;
	
	private String reconsiderationFlag;
	
	private Long rodStatusKey;
	
	private String portalStatusVal;
	
	private String websiteStatusVal;
	
	private String acknowledgementNoToken;
	
	private String crmFlagged;
	
	private String reconsiderationRejectionFlagRemarks;
	
	private String reconsiderationRequestFlag;
	
	private Boolean grievanceRepresentation;
	
	private String grievanceRepresentationFlag;
	
	public String getRodType() {
		return rodType;
	}

	public void setRodType(String rodType) {
		this.rodType = rodType;
	}

	public String getTypeOfClaim() {
		return typeOfClaim;
	}

	public void setTypeOfClaim(String typeOfClaim) {
		this.typeOfClaim = typeOfClaim;
	}

	public Intimation getIntimation() {
		return intimation;
	}

	public void setIntimation(Intimation intimation) {
		this.intimation = intimation;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}


	public String getRodNumber() {
		return rodNumber;
	}

	public void setRodNumber(String rodNumber) {
		this.rodNumber = rodNumber;
	}

	public SelectValue getReceivedFrom() {
		return receivedFrom;
	}

	public void setReceivedFrom(SelectValue string) {
		this.receivedFrom = string;
	}


	public SelectValue getModeOfReceipt() {
		return modeOfReceipt;
	}

	public void setModeOfReceipt(SelectValue modeOfReceipt) {
		this.modeOfReceipt = modeOfReceipt;
	}

	public String getBillClassification() {
		return billClassification;
	}

	public void setBillClassification(String billClassification) {
		this.billClassification = billClassification;
	}

	public Double getApprovedAmount() {
		return approvedAmount;
	}

	public void setApprovedAmount(Double approvedAmount) {
		this.approvedAmount = approvedAmount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ClaimDto getClaimDto() {
		return claimDto;
	}

	public void setClaimDto(ClaimDto claimDto) {
		this.claimDto = claimDto;
	}

	public String getRodKey() {
		return rodKey;
	}

	public void setRodKey(String rodKey) {
		this.rodKey = rodKey;
	}

	public Claim getClaim() {
		return claim;
	}

	public void setClaim(Claim claim) {
		this.claim = claim;
	}

	

	public Long getLatestKey() {
		return latestKey;
	}

	public void setLatestKey(Long latestKey) {
		this.latestKey = latestKey;
	}

	public Integer getSno() {
		return sno;
	}

	public void setSno(Integer sno) {
		this.sno = sno;
	}

	public String getMedicalResponseTime() {
		return medicalResponseTime;
	}

	public void setMedicalResponseTime(String medicalResponseTime) {
		this.medicalResponseTime = medicalResponseTime;
	}

	public SelectValue getClosingReason() {
		return closingReason;
	}

	public void setClosingReason(SelectValue closingReason) {
		this.closingReason = closingReason;
	}

	public String getClosingRemarks() {
		return closingRemarks;
	}

	public void setClosingRemarks(String closingRemarks) {
		this.closingRemarks = closingRemarks;
	}

	public void setAcknowledgeNumber(Long personContactNumber) {
		// TODO Auto-generated method stub
		
	}
	public SelectValue getModeOfPayment() {
		return modeOfPayment;
	}

	public void setModeOfPayment(SelectValue modeOfPayment) {
		this.modeOfPayment = modeOfPayment;
	}

	public void setModeOfReceipt(String value) {
		// TODO Auto-generated method stub
		
	}

	public String getPersonemailID() {
		return personemailID;
	}

	public void setPersonemailID(String personemailID) {
		this.personemailID = personemailID;
	}

	public Long getPersonContactNumber() {
		return personContactNumber;
	}

	public void setPersonContactNumber(Long personContactNumber) {
		this.personContactNumber = personContactNumber;
	}

	public String getAdditionalRemarks() {
		return additionalRemarks;
	}

	public void setAdditionalRemarks(String additionalRemarks) {
		this.additionalRemarks = additionalRemarks;
	}

	public Date getDocumentReceivedDate() {
		return documentReceivedDate;
	}

	public void setDocumentReceivedDate(Date date) {
		this.documentReceivedDate = date;
	}

	public String getReceivedDate() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setReceivedDate(String date) {
		// TODO Auto-generated method stub
		
	}

	public String getAcknowledgeNumber() {
		return acknowledgeNumber;
	}

	public void setAcknowledgeNumber(String acknowledgeNumber) {
		this.acknowledgeNumber = acknowledgeNumber;
	}

	public String getReceivedFromValue() {
		return receivedFromValue;
	}

	public void setReceivedFromValue(String receivedFromValue) {
		this.receivedFromValue = receivedFromValue;
	}

	public Boolean getCloseClaimStatus() {
		return closeClaimStatus;
	}

	public void setCloseClaimStatus(Boolean closeClaimStatus) {
		this.closeClaimStatus = closeClaimStatus;
	}

	public Long getReimbursementKey() {
		return reimbursementKey;
	}

	public void setReimbursementKey(Long reimbursementKey) {
		this.reimbursementKey = reimbursementKey;
	}

	public List<ViewDocumentDetailsDTO> getDocumentDetailsList() {
		return documentDetailsList;
	}

	public void setDocumentDetailsList(
			List<ViewDocumentDetailsDTO> documentDetailsList) {
		this.documentDetailsList = documentDetailsList;
	}

	public Integer getTaskNumber() {
		return taskNumber;
	}

	public void setTaskNumber(Integer taskNumber) {
		this.taskNumber = taskNumber;
	}

	public String getIntimationNumber() {
		return intimationNumber;
	}

	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}

	public String getModeOfReceiptValue() {
		return modeOfReceiptValue;
	}

	public void setModeOfReceiptValue(String modeOfReceiptValue) {
		this.modeOfReceiptValue = modeOfReceiptValue;
	}

	public String getStrDocumentReceivedDate() {
		return strDocumentReceivedDate;
	}

	public void setStrDocumentReceivedDate(String strDocumentReceivedDate) {
		this.strDocumentReceivedDate = strDocumentReceivedDate;
	}

	public Boolean getIsReadOnly() {
		return isReadOnly;
	}

	public void setIsReadOnly(Boolean isReadOnly) {
		this.isReadOnly = isReadOnly;
	}

	public Long getStatusKey() {
		return statusKey;
	}

	public void setStatusKey(Long statusKey) {
		this.statusKey = statusKey;
	}

	public Date getMedicalCompleteDate() {
		return medicalCompleteDate;
	}

	public void setMedicalCompleteDate(Date medicalCompleteDate) {
		this.medicalCompleteDate = medicalCompleteDate;
	}

	public DocAcknowledgement getDocAcknowledgement() {
		return docAcknowledgement;
	}

	public void setDocAcknowledgement(DocAcknowledgement docAcknowledgement) {
		this.docAcknowledgement = docAcknowledgement;
	}

	public Date getLastDocumentReceivedDate() {
		return lastDocumentReceivedDate;
	}

	public void setLastDocumentReceivedDate(Date lastDocumentReceivedDate) {
		this.lastDocumentReceivedDate = lastDocumentReceivedDate;
	}

	public String getStrLastDocumentReceivedDate() {
		return strLastDocumentReceivedDate;
	}

	public void setStrLastDocumentReceivedDate(String strLastDocumentReceivedDate) {
		this.strLastDocumentReceivedDate = strLastDocumentReceivedDate;
	}

	public String getSubclassification() {
		return subclassification;
	}

	public void setSubclassification(String subclassification) {
		this.subclassification = subclassification;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public OMPDocAcknowledgement getOmpDocAcknowledgement() {
		return ompDocAcknowledgement;
	}

	public void setOmpDocAcknowledgement(OMPDocAcknowledgement ompDocAcknowledgement) {
		this.ompDocAcknowledgement = ompDocAcknowledgement;
	}

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}
	public String getCorpBufferUtilised() {
		return corpBufferUtilised;
	}

	public void setCorpBufferUtilised(String corpBufferUtilised) {
		this.corpBufferUtilised = corpBufferUtilised;
	}

	public String getBenefits() {
		return benefits;
	}

	public void setBenefits(String benefits) {
		this.benefits = benefits;
	}
	
	public double getClaimedAmount() {
		return claimedAmount;
	}

	public void setClaimedAmount(double claimedAmount) {
		this.claimedAmount = claimedAmount;
	}

	public Double getTotalBillAmount() {
		return totalBillAmount;
	}

	public void setTotalBillAmount(Double totalBillAmount) {
		this.totalBillAmount = totalBillAmount;
	}

	public Long getRodVersion() {
		return rodVersion;
	}

	public void setRodVersion(Long rodVersion) {
		this.rodVersion = rodVersion;
	}

	public String getReconsiderationFlag() {
		return reconsiderationFlag;
	}

	public void setReconsiderationFlag(String reconsiderationFlag) {
		this.reconsiderationFlag = reconsiderationFlag;
	}

	public Long getRodStatusKey() {
		return rodStatusKey;
	}

	public void setRodStatusKey(Long rodStatusKey) {
		this.rodStatusKey = rodStatusKey;
	}

	public String getPortalStatusVal() {
		return portalStatusVal;
	}

	public void setPortalStatusVal(String portalStatusVal) {
		this.portalStatusVal = portalStatusVal;
	}

	public String getWebsiteStatusVal() {
		return websiteStatusVal;
	}

	public void setWebsiteStatusVal(String websiteStatusVal) {
		this.websiteStatusVal = websiteStatusVal;
	}

	public String getAcknowledgementNoToken() {
		return acknowledgementNoToken;
	}

	public void setAcknowledgementNoToken(String acknowledgementNoToken) {
		this.acknowledgementNoToken = acknowledgementNoToken;
	}

	public String getCrmFlagged() {
		return crmFlagged;
	}

	public void setCrmFlagged(String crmFlagged) {
		this.crmFlagged = crmFlagged;
	}

	public String getReconsiderationRejectionFlagRemarks() {
		return reconsiderationRejectionFlagRemarks;
	}

	public void setReconsiderationRejectionFlagRemarks(
			String reconsiderationRejectionFlagRemarks) {
		this.reconsiderationRejectionFlagRemarks = reconsiderationRejectionFlagRemarks;
	}

	public String getReconsiderationRequestFlag() {
		return reconsiderationRequestFlag;
	}

	public void setReconsiderationRequestFlag(String reconsiderationRequestFlag) {
		this.reconsiderationRequestFlag = reconsiderationRequestFlag;
	}

	public Boolean getGrievanceRepresentation() {
		return grievanceRepresentation;
	}

	public void setGrievanceRepresentation(Boolean grievanceRepresentation) {
		this.grievanceRepresentation = grievanceRepresentation;
	}

	public String getGrievanceRepresentationFlag() {
		return grievanceRepresentationFlag;
	}

	public void setGrievanceRepresentationFlag(String grievanceRepresentationFlag) {
		this.grievanceRepresentationFlag = grievanceRepresentationFlag;
		this.grievanceRepresentation = grievanceRepresentationFlag !=null && grievanceRepresentationFlag.equals("Y") ? true : false;
	}
	
}

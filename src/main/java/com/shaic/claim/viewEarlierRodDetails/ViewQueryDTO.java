package com.shaic.claim.viewEarlierRodDetails;

import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.domain.Claim;
import com.vaadin.v7.ui.OptionGroup;

public class ViewQueryDTO extends AbstractTableDTO{
	
	private Long key;
	
	private Integer sno;
	
	private Claim claim;
	
	private OptionGroup select;
	
	private String hospitalName;
	
	private String hospitalCity;
	
	private String diagnosis;
	
	private String queryRemarks;
	
	private String queryRaised;
	
	private String designation;
	
	private Date queryRaisedDate;
	
	private String queryDate;
	
	private String queryRaisedDateStr;
	
	private String queryStatus;
	
	private String queryRaiseRole;
	
	// View Query Details
	
	private String intimationNo;
	
	private String claimNo;
	
	private String policyNo;
	
	private String acknowledgementNo;
	
	private String rodNumber;
	
	private String receivedFrom;
	
	private String receivedDate;
	
	private String billClassification;
	
	private String productName;
	
	private String claimType;
	
	private String insuredPatientName;
	
	private String hospitalType;
	
	private String admissionDate;
	
	private String queryDraftedDate;
	
	private String queryLetterRemarks;
	
	private String approvedRejectedDate;
	
	private String rejectedRemarks;
	
	private String redraftRemarks;
	
	private String queryType;
	
	private String benefitCover;
	
	private Double claimedAmt;
	
	private Long statusId;
	
	private String opnQryTyp;
	
	private String portalStatusVal;
	
	private String websiteStatusVal;
	
	private Long reimbursementKey;
	
	private String queryToken;
	

	public Double getClaimedAmt() {
		return claimedAmt;
	}

	public void setClaimedAmt(Double claimedAmt) {
		this.claimedAmt = claimedAmt;
	}

	public String getBenefitCover() {
		return benefitCover;
	}

	public void setBenefitCover(String benefitCover) {
		this.benefitCover = benefitCover;
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getHospitalCity() {
		return hospitalCity;
	}

	public void setHospitalCity(String hospitalCity) {
		this.hospitalCity = hospitalCity;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public String getQueryRemarks() {
		return queryRemarks;
	}

	public void setQueryRemarks(String queryRemarks) {
		this.queryRemarks = queryRemarks;
	}

	public String getQueryRaised() {
		return queryRaised;
	}

	public void setQueryRaised(String queryRaised) {
		this.queryRaised = queryRaised;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getQueryStatus() {
		return queryStatus;
	}

	public void setQueryStatus(String queryStatus) {
		this.queryStatus = queryStatus;
	}

	public Date getQueryRaisedDate() {
		return queryRaisedDate;
	}

	public void setQueryRaisedDate(Date queryRaisedDate) {
		this.queryRaisedDate = queryRaisedDate;
	}

	public String getQueryDate() {
		return queryDate;
	}

	public void setQueryDate(String queryDate) {
		this.queryDate = queryDate;
	}

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public String getClaimNo() {
		return claimNo;
	}

	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getAcknowledgementNo() {
		return acknowledgementNo;
	}

	public void setAcknowledgementNo(String acknowledgementNo) {
		this.acknowledgementNo = acknowledgementNo;
	}

	public String getRodNumber() {
		return rodNumber;
	}

	public void setRodNumber(String rodNumber) {
		this.rodNumber = rodNumber;
	}

	public String getReceivedFrom() {
		return receivedFrom;
	}

	public void setReceivedFrom(String receivedFrom) {
		this.receivedFrom = receivedFrom;
	}

	public String getBillClassification() {
		return billClassification;
	}

	public void setBillClassification(String billClassification) {
		this.billClassification = billClassification;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	public String getInsuredPatientName() {
		return insuredPatientName;
	}

	public void setInsuredPatientName(String insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
	}

	public String getHospitalType() {
		return hospitalType;
	}

	public void setHospitalType(String hospitalType) {
		this.hospitalType = hospitalType;
	}

	public String getAdmissionDate() {
		return admissionDate;
	}

	public void setAdmissionDate(String admissionDate) {
		this.admissionDate = admissionDate;
	}

	
	
	public String getQueryLetterRemarks() {
		return queryLetterRemarks;
	}

	public void setQueryLetterRemarks(String queryLetterRemarks) {
		this.queryLetterRemarks = queryLetterRemarks;
	}
	

	public String getApprovedRejectedDate() {
		return approvedRejectedDate;
	}

	public void setApprovedRejectedDate(String approvedRejectedDate) {
		this.approvedRejectedDate = approvedRejectedDate;
	}

	public String getRejectedRemarks() {
		return rejectedRemarks;
	}

	public void setRejectedRemarks(String rejectedRemarks) {
		this.rejectedRemarks = rejectedRemarks;
	}

	public String getRedraftRemarks() {
		return redraftRemarks;
	}

	public void setRedraftRemarks(String redraftRemarks) {
		this.redraftRemarks = redraftRemarks;
	}

	public Claim getClaim() {
		return claim;
	}

	public void setClaim(Claim claim) {
		this.claim = claim;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Integer getSno() {
		return sno;
	}

	public void setSno(Integer sno) {
		this.sno = sno;
	}

	public String getQueryRaiseRole() {
		return queryRaiseRole;
	}

	public void setQueryRaiseRole(String queryRaiseRole) {
		this.queryRaiseRole = queryRaiseRole;
	}

	public String getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(String receivedDate) {
		this.receivedDate = receivedDate;
	}

	public String getQueryRaisedDateStr() {
		return queryRaisedDateStr;
	}

	public void setQueryRaisedDateStr(String queryRaisedDateStr) {
		this.queryRaisedDateStr = queryRaisedDateStr;
	}

	public String getQueryDraftedDate() {
		return queryDraftedDate;
	}

	public void setQueryDraftedDate(String queryDraftedDate) {
		this.queryDraftedDate = queryDraftedDate;
	}

	public OptionGroup getSelect() {
		return select;
	}

	public void setSelect(OptionGroup select) {
		this.select = select;
	}

	public Long getStatusId() {
		return statusId;
	}

	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}

	public String getOpnQryTyp() {
		return opnQryTyp;
	}

	public void setOpnQryTyp(String opnQryTyp) {
		this.opnQryTyp = opnQryTyp;
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

	public Long getReimbursementKey() {
		return reimbursementKey;
	}

	public void setReimbursementKey(Long reimbursementKey) {
		this.reimbursementKey = reimbursementKey;
	}

	public String getQueryToken() {
		return queryToken;
	}

	public void setQueryToken(String queryToken) {
		this.queryToken = queryToken;
	}

	
}

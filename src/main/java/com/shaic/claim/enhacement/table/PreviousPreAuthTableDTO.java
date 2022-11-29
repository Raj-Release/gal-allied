package com.shaic.claim.enhacement.table;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.domain.Intimation;

public class PreviousPreAuthTableDTO extends AbstractTableDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer sno;
	
	private Long key;
	
	private Long claimKey;

	private String referenceNo;

	private String referenceType;

	private String treatementType;

	private String requestedAmt;

	private String approvedAmt;
	
	private String approvedAmtAftPremiumDeduction;
	
	private Intimation intimation;	
	
	private String diffAmount;
	
	private String processFlag;
	
	private String createdDateStr;
	
	private Date createdDate;
	
	private Date modifiedDate;
	
	private Long statusKey;
	
	//For PA close claim
	private String docReceivedFrom;
	
	private String treatmentRemarks;
	
	private Date docReceivedDate;
	
	private String strDocReceivedDate;
	
	private String status;
	
	private String remarks;
	
	private String strmodifiedDate;
	
	private String medicalRemarks;
	
	private String preauthCreatedDate;
	
	private String preauthModifiedDate;
	
	private String portalStatusVal;
	
	private String websiteStatusVal;
	
	private String negotiatedWith;
	
	private String cashlessDocToken;

	public String getStrDocReceivedDate() {
		return strDocReceivedDate;
	}

	public void setStrDocReceivedDate(String strDocReceivedDate) {
		this.strDocReceivedDate = strDocReceivedDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getDocReceivedDate() {
		return docReceivedDate;
	}

	public void setDocReceivedDate(Date docuReceivedDate) {
		this.docReceivedDate = docuReceivedDate;
	}

	public String getDocReceivedFrom() {
		return docReceivedFrom;
	}

	public void setDocReceivedFrom(String docReceivedFrom) {
		this.docReceivedFrom = docReceivedFrom;
	}

	public String getTreatmentRemarks() {
		return treatmentRemarks;
	}

	public void setTreatmentRemarks(String treatmentRemarks) {
		this.treatmentRemarks = treatmentRemarks;
	}

	public Integer getSno() {
		return sno;
	}

	public void setSno(Integer sno) {
		this.sno = sno;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public String getReferenceType() {
		return referenceType;
	}

	public void setReferenceType(String referenceType) {
		this.referenceType = referenceType;
	}

	public String getTreatementType() {
		return treatementType;
	}

	public void setTreatementType(String treatementType) {
		this.treatementType = treatementType;
	}

	public String getRequestedAmt() {
		return requestedAmt;
	}

	public void setRequestedAmt(String requestedAmt) {
		this.requestedAmt = requestedAmt;
	}

	public String getApprovedAmt() {
		return approvedAmt;
	}

	public void setApprovedAmt(String approvedAmt) {
		this.approvedAmt = approvedAmt;
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

	public String getDiffAmount() {
		return diffAmount;
	}

	public void setDiffAmount(String diffAmount) {
		this.diffAmount = diffAmount;
	}

	public String getProcessFlag() {
		return processFlag;
	}

	public void setProcessFlag(String processFlag) {
		this.processFlag = processFlag;
	}

	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}

	public String getCreatedDateStr() {
		return createdDateStr;
	}

	public void setCreatedDateStr(String createdDateStr) {
		this.createdDateStr = createdDateStr;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Long getStatusKey() {
		return statusKey;
	}

	public void setStatusKey(Long statusKey) {
		this.statusKey = statusKey;
	}

	public String getApprovedAmtAftPremiumDeduction() {
		return approvedAmtAftPremiumDeduction;
	}

	public void setApprovedAmtAftPremiumDeduction(
			String approvedAmtAftPremiumDeduction) {
		this.approvedAmtAftPremiumDeduction = approvedAmtAftPremiumDeduction;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getStrmodifiedDate() {
		return strmodifiedDate;
	}

	public void setStrmodifiedDate(String strmodifiedDate) {
		this.strmodifiedDate = strmodifiedDate;
	}

	public String getMedicalRemarks() {
		return medicalRemarks;
	}

	public void setMedicalRemarks(String medicalRemarks) {
		this.medicalRemarks = medicalRemarks;
	}

	public String getPreauthCreatedDate() {
		return preauthCreatedDate;
	}

	public void setPreauthCreatedDate(String preauthCreatedDate) {
		this.preauthCreatedDate = preauthCreatedDate;
	}

	public String getPreauthModifiedDate() {
		return preauthModifiedDate;
	}

	public void setPreauthModifiedDate(String preauthModifiedDate) {
		this.preauthModifiedDate = preauthModifiedDate;
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

	public String getNegotiatedWith() {
		return negotiatedWith;
	}

	public void setNegotiatedWith(String negotiatedWith) {
		this.negotiatedWith = negotiatedWith;
	}

	public String getCashlessDocToken() {
		return cashlessDocToken;
	}

	public void setCashlessDocToken(String cashlessDocToken) {
		this.cashlessDocToken = cashlessDocToken;
	}
	
	

}

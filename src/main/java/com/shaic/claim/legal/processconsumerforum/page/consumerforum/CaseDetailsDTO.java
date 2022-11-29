package com.shaic.claim.legal.processconsumerforum.page.consumerforum;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.shaic.arch.fields.dto.SelectValue;

public class CaseDetailsDTO implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7972365945052880380L;

	@NotNull(message="Please Select Legal Lock")
	private Boolean legalLock ;
	
	private Boolean legalCompleted ; 
	
	@NotNull(message="Please Select DCDRF")
	@Size(min = 1 , message = "Please Select DCDRF")
	private String dcdrf;
	
	@NotNull(message="Please Select Zone")
	private SelectValue zone;
	
	@NotNull(message="Please Select State")
	private SelectValue state;
	
	@NotNull(message="Please Enter CC No")
	@Size(min = 1 , message = "Please Enter CC No")
	private String ccNo;
	
	@NotNull(message="Please Enter Advocate Name")
	@Size(min = 1 , message = "Please Enter Advocate Name")
	private String adVocateName;
	
	private Double complainceClaimedAmt;
	
	private Date complaintDate;
	
	@NotNull(message="Please Select Advocate Notice Date")
	private Date advocateNoticeDate;
	
	@NotNull(message="Please Select Limitation Date")
	private Date limitationTime;
	
	@NotNull(message="Please Select Status")
	@Size(min = 1 , message = "Please Select Status")
	private String status;
	
	private Boolean isStandRejection;

	@NotNull(message="Please Select Reply Sent Date")
	private Date replySentDate;
	
	@NotNull(message="Please Enter Rejection Reason")
	@Size(min = 1 , message = "Please Enter Rejection Reason")
	private String rejectionReason;
	
	private SelectValue movedTo;
	
	@NotNull(message="Please Select Notice Recieved Date")
	private Date noticeRecievedDate;
	
	@NotNull(message="Please Select Complaince Date")
	private Date complainceDate;
	
	@NotNull(message="Please Select Pending Value")
	private SelectValue pendingValue;
	
	private Boolean isToSettle;
	
	@NotNull(message="Please Select Settle Date")
	private Date settleDate;
	
	@NotNull(message="Please Select Settle Amount")
	private Double settleAmt;
	
	@NotNull(message="Please Select Settle Reason")
	@Size(min = 1 , message = "Please Select Settle Reason")
	private String settleReason;

	
	public Boolean getLegalLock() {
		return legalLock;
	}

	public void setLegalLock(Boolean legalLock) {
		this.legalLock = legalLock;
	}

	public Boolean getLegalCompleted() {
		return legalCompleted;
	}

	public void setLegalCompleted(Boolean legalCompleted) {
		this.legalCompleted = legalCompleted;
	}

	public String getDcdrf() {
		return dcdrf;
	}

	public void setDcdrf(String dcdrf) {
		this.dcdrf = dcdrf;
	}

	public SelectValue getZone() {
		return zone;
	}

	public void setZone(SelectValue zone) {
		this.zone = zone;
	}

	public SelectValue getState() {
		return state;
	}

	public void setState(SelectValue state) {
		this.state = state;
	}

	public String getCcNo() {
		return ccNo;
	}

	public void setCcNo(String ccNo) {
		this.ccNo = ccNo;
	}

	public String getAdVocateName() {
		return adVocateName;
	}

	public void setAdVocateName(String adVocateName) {
		this.adVocateName = adVocateName;
	}

	public Double getComplainceClaimedAmt() {
		return complainceClaimedAmt;
	}

	public void setComplainceClaimedAmt(Double complainceClaimedAmt) {
		this.complainceClaimedAmt = complainceClaimedAmt;
	}

	public Date getComplaintDate() {
		return complaintDate;
	}

	public void setComplaintDate(Date complaintDate) {
		this.complaintDate = complaintDate;
	}

	public Date getAdvocateNoticeDate() {
		return advocateNoticeDate;
	}

	public void setAdvocateNoticeDate(Date advocateNoticeDate) {
		this.advocateNoticeDate = advocateNoticeDate;
	}

	public Date getLimitationTime() {
		return limitationTime;
	}

	public void setLimitationTime(Date limitationTime) {
		this.limitationTime = limitationTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getIsStandRejection() {
		return isStandRejection;
	}

	public void setIsStandRejection(Boolean isStandRejection) {
		this.isStandRejection = isStandRejection;
	}

	public Date getReplySentDate() {
		return replySentDate;
	}

	public void setReplySentDate(Date replySentDate) {
		this.replySentDate = replySentDate;
	}

	public String getRejectionReason() {
		return rejectionReason;
	}

	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}

	public SelectValue getMovedTo() {
		return movedTo;
	}

	public void setMovedTo(SelectValue movedTo) {
		this.movedTo = movedTo;
	}

	public Date getNoticeRecievedDate() {
		return noticeRecievedDate;
	}

	public void setNoticeRecievedDate(Date noticeRecievedDate) {
		this.noticeRecievedDate = noticeRecievedDate;
	}

	public Date getComplainceDate() {
		return complainceDate;
	}

	public void setComplainceDate(Date complainceDate) {
		this.complainceDate = complainceDate;
	}

	public SelectValue getPendingValue() {
		return pendingValue;
	}

	public void setPendingValue(SelectValue pendingValue) {
		this.pendingValue = pendingValue;
	}

	public Boolean getIsToSettle() {
		return isToSettle;
	}

	public void setIsToSettle(Boolean isToSettle) {
		this.isToSettle = isToSettle;
	}

	public Date getSettleDate() {
		return settleDate;
	}

	public void setSettleDate(Date settleDate) {
		this.settleDate = settleDate;
	}

	public Double getSettleAmt() {
		return settleAmt;
	}

	public void setSettleAmt(Double settleAmt) {
		this.settleAmt = settleAmt;
	}

	public String getSettleReason() {
		return settleReason;
	}

	public void setSettleReason(String settleReason) {
		this.settleReason = settleReason;
	}
	


}

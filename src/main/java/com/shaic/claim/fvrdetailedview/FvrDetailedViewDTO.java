package com.shaic.claim.fvrdetailedview;

import java.io.Serializable;
import java.util.Date;

import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;
import com.vaadin.v7.ui.TextField;

/**
 * @author GokulPrasath.A
 *
 */
public class FvrDetailedViewDTO  extends AbstractSearchDTO  implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String hospitalName;

	private String fvrInitiatedBy;

	private String allocationTo;

	private String fvrPriority;

	private Date fvrInitiatedDateTime;

	private String fvrCancelledBy;

	private String fvrStatus;

	private Date fvrCancelledDateTime;

	private String fvrClosedRemarks;

	private String fvrExecutiveClosedReason;

	private String fvrAssignedBy;

	private Date fvrAssignedDateTime;

	private String repCode;

	private String fvrRemarks;

	private String repName;

	private String repContactNo;

	private String fvrReplyReceivedFrom;

	private Date hospitalVisitedDate;

	private Date fvrReplyReceivedDateTime;

	private String fvrGradedBy;

	private String fvrGradedRemarks;

	private Date fvrGradedDateTime;

	private String modifiedBy;

	private Date modifiedDate;
	
	private String fvrRemarksNotRequiredBy;
	
	private Date fvrNotRequiredUpdatedDateAndTime;
	
	private String fvrNotRequiredRemarks;
	
	private String claimStage;
	
	private Long intimationKey;
	
	private String fvrClaimAlertRemarks;

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getFvrInitiatedBy() {
		return fvrInitiatedBy;
	}

	public void setFvrInitiatedBy(String fvrInitiatedBy) {
		this.fvrInitiatedBy = fvrInitiatedBy;
	}

	public String getAllocationTo() {
		return allocationTo;
	}

	public void setAllocationTo(String allocationTo) {
		this.allocationTo = allocationTo;
	}

	public String getFvrPriority() {
		return fvrPriority;
	}

	public void setFvrPriority(String fvrPriority) {
		this.fvrPriority = fvrPriority;
	}

	public Date getFvrInitiatedDateTime() {
		return fvrInitiatedDateTime;
	}

	public void setFvrInitiatedDateTime(Date fvrInitiatedDateTime) {
		this.fvrInitiatedDateTime = fvrInitiatedDateTime;
	}

	public String getFvrCancelledBy() {
		return fvrCancelledBy;
	}

	public void setFvrCancelledBy(String fvrCancelledBy) {
		this.fvrCancelledBy = fvrCancelledBy;
	}

	public String getFvrStatus() {
		return fvrStatus;
	}

	public void setFvrStatus(String fvrStatus) {
		this.fvrStatus = fvrStatus;
	}

	public Date getFvrCancelledDateTime() {
		return fvrCancelledDateTime;
	}

	public void setFvrCancelledDateTime(Date fvrCancelledDateTime) {
		this.fvrCancelledDateTime = fvrCancelledDateTime;
	}

	public String getFvrClosedRemarks() {
		return fvrClosedRemarks;
	}

	public void setFvrClosedRemarks(String fvrClosedRemarks) {
		this.fvrClosedRemarks = fvrClosedRemarks;
	}

	public String getFvrExecutiveClosedReason() {
		return fvrExecutiveClosedReason;
	}

	public void setFvrExecutiveClosedReason(String fvrExecutiveClosedReason) {
		this.fvrExecutiveClosedReason = fvrExecutiveClosedReason;
	}

	public String getFvrAssignedBy() {
		return fvrAssignedBy;
	}

	public void setFvrAssignedBy(String fvrAssignedBy) {
		this.fvrAssignedBy = fvrAssignedBy;
	}

	public Date getFvrAssignedDateTime() {
		return fvrAssignedDateTime;
	}

	public void setFvrAssignedDateTime(Date fvrAssignedDateTime) {
		this.fvrAssignedDateTime = fvrAssignedDateTime;
	}

	public String getRepCode() {
		return repCode;
	}

	public void setRepCode(String repCode) {
		this.repCode = repCode;
	}

	public String getFvrRemarks() {
		return fvrRemarks;
	}

	public void setFvrRemarks(String fvrRemarks) {
		this.fvrRemarks = fvrRemarks;
	}

	public String getRepName() {
		return repName;
	}

	public void setRepName(String repName) {
		this.repName = repName;
	}

	public String getRepContactNo() {
		return repContactNo;
	}

	public void setRepContactNo(String repContactNo) {
		this.repContactNo = repContactNo;
	}

	public String getFvrReplyReceivedFrom() {
		return fvrReplyReceivedFrom;
	}

	public void setFvrReplyReceivedFrom(String fvrReplyReceivedFrom) {
		this.fvrReplyReceivedFrom = fvrReplyReceivedFrom;
	}

	public Date getHospitalVisitedDate() {
		return hospitalVisitedDate;
	}

	public void setHospitalVisitedDate(Date hospitalVisitedDate) {
		this.hospitalVisitedDate = hospitalVisitedDate;
	}

	public Date getFvrReplyReceivedDateTime() {
		return fvrReplyReceivedDateTime;
	}

	public void setFvrReplyReceivedDateTime(Date fvrReplyReceivedDateTime) {
		this.fvrReplyReceivedDateTime = fvrReplyReceivedDateTime;
	}

	public String getFvrGradedBy() {
		return fvrGradedBy;
	}

	public void setFvrGradedBy(String fvrGradedBy) {
		this.fvrGradedBy = fvrGradedBy;
	}

	public String getFvrGradedRemarks() {
		return fvrGradedRemarks;
	}

	public void setFvrGradedRemarks(String fvrGradedRemarks) {
		this.fvrGradedRemarks = fvrGradedRemarks;
	}

	public Date getFvrGradedDateTime() {
		return fvrGradedDateTime;
	}

	public void setFvrGradedDateTime(Date fvrGradedDateTime) {
		this.fvrGradedDateTime = fvrGradedDateTime;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getFvrRemarksNotRequiredBy() {
		return fvrRemarksNotRequiredBy;
	}

	public void setFvrRemarksNotRequiredBy(String fvrRemarksNotRequiredBy) {
		this.fvrRemarksNotRequiredBy = fvrRemarksNotRequiredBy;
	}

	public Date getFvrNotRequiredUpdatedDateAndTime() {
		return fvrNotRequiredUpdatedDateAndTime;
	}

	public void setFvrNotRequiredUpdatedDateAndTime(
			Date fvrNotRequiredUpdatedDateAndTime) {
		this.fvrNotRequiredUpdatedDateAndTime = fvrNotRequiredUpdatedDateAndTime;
	}

	public String getFvrNotRequiredRemarks() {
		return fvrNotRequiredRemarks;
	}

	public void setFvrNotRequiredRemarks(String fvrNotRequiredRemarks) {
		this.fvrNotRequiredRemarks = fvrNotRequiredRemarks;
	}

	public String getClaimStage() {
		return claimStage;
	}

	public void setClaimStage(String claimStage) {
		this.claimStage = claimStage;
	}

	public Long getIntimationKey() {
		return intimationKey;
	}

	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
	}

	public String getFvrClaimAlertRemarks() {
		return fvrClaimAlertRemarks;
	}

	public void setFvrClaimAlertRemarks(String fvrClaimAlertRemarks) {
		this.fvrClaimAlertRemarks = fvrClaimAlertRemarks;
	}

}

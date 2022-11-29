package com.shaic.reimbursement.processi_investigationi_initiated;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.fvrdetails.view.ViewFVRDTO;


public class ProcessInvestigationInitiatedDto {
	
	private String investigationNo;
	
	private String requestingRole;
	
	private String requestorIdOrName;
	
	private String allocationTo;
	
	private String reasonForReferring;
	
	private String triggerPointsToFocus;
	
	private String investigationApprovedRemarks;
	
	private String investigationNotRequiredRemarks;	
	
	private String userName;
	
	private String passWord;
	

	private List<SelectValue> invAllocationToList;
	
	private SelectValue invAllocationTo;
	
	private Long reasonForIniInvestId;
	
	private String reasonForIniInvestValue;
	
	private List<SelectValue> reasonForInitiatingInvestIdList;
	
	private List<SelectValue> reasonForInitiatingInvestSelectValueList;
	
	private SelectValue reasonForInitiatingInvestSelectValue;
	
	
	private Object dbOutArray;
	
	@NotNull(message = "Please Select Initiate Field Visit Request")
	private Boolean initiateFieldVisitRequestFlag;
	
	@NotNull(message = "Please Choose Allocation to.")
	private SelectValue fvrAllocationTo;
	
	@NotNull(message = "Please Choose Assign to")
	private SelectValue assignTo;
	
	@NotNull(message= "Please Choose Priority")
	private SelectValue priority;
	
//	@NotNull(message = "Please Enter FVR Trigger Points.")
//	@Size(min=1,message = "Please Enter FVR Trigger Points.")
	private String fvrTriggerPoints;
	
	private List<ViewFVRDTO> fvrTriggerPtsList;

	public String getRequestingRole() {
		return requestingRole;
	}

	public void setRequestingRole(String requestingRole) {
		this.requestingRole = requestingRole;
	}

	public String getRequestorIdOrName() {
		return requestorIdOrName;
	}

	public void setRequestorIdOrName(String requestorIdOrName) {
		this.requestorIdOrName = requestorIdOrName;
	}
	

	public String getAllocationTo() {
		return allocationTo;
	}

	public void setAllocationTo(String allocationTo) {
		this.allocationTo = allocationTo;
	}

	public String getReasonForReferring() {
		return reasonForReferring;
	}

	public void setReasonForReferring(String reasonForReferring) {
		this.reasonForReferring = reasonForReferring;
	}

	public String getTriggerPointsToFocus() {
		return triggerPointsToFocus;
	}

	public void setTriggerPointsToFocus(String triggerPointsToFocus) {
		this.triggerPointsToFocus = triggerPointsToFocus;
	}

	public String getInvestigationNo() {
		return investigationNo;
	}

	public void setInvestigationNo(String investigationNo) {
		this.investigationNo = investigationNo;
	}

	public String getInvestigationApprovedRemarks() {
		return investigationApprovedRemarks;
	}

	public void setInvestigationApprovedRemarks(String investigationApprovedRemarks) {
		this.investigationApprovedRemarks = investigationApprovedRemarks;
	}

	public String getInvestigationNotRequiredRemarks() {
		return investigationNotRequiredRemarks;
	}

	public void setInvestigationNotRequiredRemarks(
			String investigationNotRequiredRemarks) {
		this.investigationNotRequiredRemarks = investigationNotRequiredRemarks;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	/*public HumanTask getHumanTaks() {
		return humanTaks;
	}

	public void setHumanTaks(HumanTask humanTaks) {
		this.humanTaks = humanTaks;
	} */


	public Boolean getInitiateFieldVisitRequestFlag() {
		return initiateFieldVisitRequestFlag;
	}

	public void setInitiateFieldVisitRequestFlag(
			Boolean initiateFieldVisitRequestFlag) {
		this.initiateFieldVisitRequestFlag = initiateFieldVisitRequestFlag;
	}

	public SelectValue getFvrAllocationTo() {
		return fvrAllocationTo;
	}

	public void setFvrAllocationTo(SelectValue fvrAllocationTo) {
		this.fvrAllocationTo = fvrAllocationTo;
	}

	public SelectValue getAssignTo() {
		return assignTo;
	}

	public void setAssignTo(SelectValue assignTo) {
		this.assignTo = assignTo;
	}

	public SelectValue getPriority() {
		return priority;
	}

	public void setPriority(SelectValue priority) {
		this.priority = priority;
	}

	public String getFvrTriggerPoints() {
		return fvrTriggerPoints;
	}

	public void setFvrTriggerPoints(String fvrTriggerPoints) {
		this.fvrTriggerPoints = fvrTriggerPoints;
	}

	public List<SelectValue> getInvAllocationToList() {
		return invAllocationToList;
	}

	public void setInvAllocationToList(List<SelectValue> invAllocationToList) {
		this.invAllocationToList = invAllocationToList;
	}

	public SelectValue getInvAllocationTo() {
		return invAllocationTo;
	}

	public void setInvAllocationTo(SelectValue invAllocationTo) {
		this.invAllocationTo = invAllocationTo;
	}

	public Object getDbOutArray() {
		return dbOutArray;
	}

	public void setDbOutArray(Object dbOutArray) {
		this.dbOutArray = dbOutArray;
	}

	public List<ViewFVRDTO> getFvrTriggerPtsList() {
		return fvrTriggerPtsList;
	}

	public void setFvrTriggerPtsList(List<ViewFVRDTO> fvrTriggerPtsList) {
		this.fvrTriggerPtsList = fvrTriggerPtsList;
	}

	public List<SelectValue> getReasonForInitiatingInvestSelectValueList() {
		return reasonForInitiatingInvestSelectValueList;
	}

	public void setReasonForInitiatingInvestSelectValueList(
			List<SelectValue> reasonForInitiatingInvestSelectValueList) {
		this.reasonForInitiatingInvestSelectValueList = reasonForInitiatingInvestSelectValueList;
	}

	public SelectValue getReasonForInitiatingInvestSelectValue() {
		return reasonForInitiatingInvestSelectValue;
	}

	public void setReasonForInitiatingInvestSelectValue(
			SelectValue reasonForInitiatingInvestSelectValue) {
		this.reasonForInitiatingInvestSelectValue = reasonForInitiatingInvestSelectValue;
	}

	public Long getReasonForIniInvestId() {
		return reasonForIniInvestId;
	}

	public void setReasonForIniInvestId(Long reasonForIniInvestId) {
		this.reasonForIniInvestId = reasonForIniInvestId;
	}

	public String getReasonForIniInvestValue() {
		return reasonForIniInvestValue;
	}

	public void setReasonForIniInvestValue(String reasonForIniInvestValue) {
		this.reasonForIniInvestValue = reasonForIniInvestValue;
	}

	public List<SelectValue> getReasonForInitiatingInvestIdList() {
		return reasonForInitiatingInvestIdList;
	}

	public void setReasonForInitiatingInvestIdList(
			List<SelectValue> reasonForInitiatingInvestIdList) {
		this.reasonForInitiatingInvestIdList = reasonForInitiatingInvestIdList;
	}
	

}

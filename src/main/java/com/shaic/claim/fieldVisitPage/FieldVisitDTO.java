package com.shaic.claim.fieldVisitPage;

import javax.validation.constraints.NotNull;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;

public class FieldVisitDTO extends AbstractTableDTO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long key;
	
	private String approvedFVR;
	
	private String allocateDoctor;
	
	private String assignTo;
	
	private String priority;
	
	private SelectValue prioritySelect;
	
	private SelectValue allocateTo;
	
	
	@NotNull(message="Please Enter Represent Name")
	private String name;
	
	private Long telNo;
	
	private Long mobileNo;
	
	@NotNull(message="Please Enter Executive Comments")
	private String comments;
	
	@NotNull(message="Please Enter Reason for not Assigning FVR")
	private String reasons;
	
	private String representativeCode;
	
	private String fvrCount;
	
	private String reasonForAdmission;
	
	private Long stageId;
	
	private Boolean isFinancialFvr = false;
	
	//Added for feild visit request if raised by system.
	
	private Boolean isSystemFVRTask ;
	
	private String representativeName;
	
	private String fvrTriggerPoints;
	
	private String excecutiveComments;
	
	public String getApprovedFVR() {
		return approvedFVR;
	}

	public void setApprovedFVR(String approvedFVR) {
		this.approvedFVR = approvedFVR;
	}

	public String getAllocateDoctor() {
		return allocateDoctor;
	}

	public void setAllocateDoctor(String allocateDoctor) {
		this.allocateDoctor = allocateDoctor;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getReasons() {
		return reasons;
	}

	public void setReasons(String reasons) {
		this.reasons = reasons;
	}

	public Long getTelNo() {
		return telNo;
	}

	public void setTelNo(Long telNo) {
		this.telNo = telNo;
	}

	public Long getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(Long mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getRepresentativeCode() {
		return representativeCode;
	}

	public void setRepresentativeCode(String representativeCode) {
		this.representativeCode = representativeCode;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getFvrCount() {
		return fvrCount;
	}

	public void setFvrCount(String fvrCount) {
		this.fvrCount = fvrCount;
	}

	public String getReasonForAdmission() {
		return reasonForAdmission;
	}

	public void setReasonForAdmission(String reasonForAdmission) {
		this.reasonForAdmission = reasonForAdmission;
	}

	public Long getStageId() {
		return stageId;
	}

	public void setStageId(Long stageId) {
		this.stageId = stageId;
	}

	public Boolean getIsFinancialFvr() {
		return isFinancialFvr;
	}

	public void setIsFinancialFvr(Boolean isFinancialFvr) {
		this.isFinancialFvr = isFinancialFvr;
	}

	public Boolean getIsSystemFVRTask() {
		return isSystemFVRTask;
	}

	public void setIsSystemFVRTask(Boolean isSystemFVRTask) {
		this.isSystemFVRTask = isSystemFVRTask;
	}

	public String getAssignTo() {
		return assignTo;
	}

	public void setAssignTo(String assignTo) {
		this.assignTo = assignTo;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getRepresentativeName() {
		return representativeName;
	}

	public void setRepresentativeName(String representativeName) {
		this.representativeName = representativeName;
	}

	public String getFvrTriggerPoints() {
		return fvrTriggerPoints;
	}

	public void setFvrTriggerPoints(String fvrTriggerPoints) {
		this.fvrTriggerPoints = fvrTriggerPoints;
	}

	public String getExcecutiveComments() {
		return excecutiveComments;
	}

	public void setExcecutiveComments(String excecutiveComments) {
		this.excecutiveComments = excecutiveComments;
	}

	public SelectValue getPrioritySelect() {
		return prioritySelect;
	}

	public void setPrioritySelect(SelectValue prioritySelect) {
		this.prioritySelect = prioritySelect;
	}

	public SelectValue getAllocateTo() {
		return allocateTo;
	}

	public void setAllocateTo(SelectValue allocateTo) {
		this.allocateTo = allocateTo;
	}
	
	

}

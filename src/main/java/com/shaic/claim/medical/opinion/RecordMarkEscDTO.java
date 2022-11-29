package com.shaic.claim.medical.opinion;

import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class RecordMarkEscDTO {
		
	private String refNumber;
	
	private String intimationNumber;
	
	private String escalatedRole;
	
	private Date escalatedDate;
	
	private String escalatedBy;
	
	private Long escalationReason;
	
	private Long actionTaken;
	
	private String doctorRemarks;
	
	private String escalationRemarks;
	
	private Long statusId;
	
	private Long stageId;
	
	private String createdBy;
	
	private Date createdDate;
	
	private String modifiedBy;
	
	private Date modifiedDate;
	
	BeanItemContainer<SelectValue> escalatedRoleContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
	
	BeanItemContainer<SelectValue> escalationReasonContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
	
	private PreauthDTO preauthDto;
	
	private ClaimDto claimDTO;
	
	private String username;
	
	private String password;
	
	private Boolean mrkPersonnel;
	private String mrkPersonnelFlag;

	public String getRefNumber() {
		return refNumber;
	}

	public void setRefNumber(String refNumber) {
		this.refNumber = refNumber;
	}

	public String getIntimationNumber() {
		return intimationNumber;
	}

	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}

	public String getEscalatedRole() {
		return escalatedRole;
	}

	public void setEscalatedRole(String escalatedRole) {
		this.escalatedRole = escalatedRole;
	}

	public Date getEscalatedDate() {
		return escalatedDate;
	}

	public void setEscalatedDate(Date escalatedDate) {
		this.escalatedDate = escalatedDate;
	}

	public String getEscalatedBy() {
		return escalatedBy;
	}

	public void setEscalatedBy(String escalatedBy) {
		this.escalatedBy = escalatedBy;
	}

	public Long getEscalationReason() {
		return escalationReason;
	}

	public void setEscalationReason(Long escalationReason) {
		this.escalationReason = escalationReason;
	}

	public Long getActionTaken() {
		return actionTaken;
	}

	public void setActionTaken(Long actionTaken) {
		this.actionTaken = actionTaken;
	}

	public String getDoctorRemarks() {
		return doctorRemarks;
	}

	public void setDoctorRemarks(String doctorRemarks) {
		this.doctorRemarks = doctorRemarks;
	}

	public String getEscalationRemarks() {
		return escalationRemarks;
	}

	public void setEscalationRemarks(String escalationRemarks) {
		this.escalationRemarks = escalationRemarks;
	}

	public Long getStatusId() {
		return statusId;
	}

	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}

	public Long getStageId() {
		return stageId;
	}

	public void setStageId(Long stageId) {
		this.stageId = stageId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
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

	public BeanItemContainer<SelectValue> getEscalatedRoleContainer() {
		return escalatedRoleContainer;
	}

	public void setEscalatedRoleContainer(
			BeanItemContainer<SelectValue> escalatedRoleContainer) {
		this.escalatedRoleContainer = escalatedRoleContainer;
	}

	public BeanItemContainer<SelectValue> getEscalationReasonContainer() {
		return escalationReasonContainer;
	}

	public void setEscalationReasonContainer(
			BeanItemContainer<SelectValue> escalationReasonContainer) {
		this.escalationReasonContainer = escalationReasonContainer;
	}

	public PreauthDTO getPreauthDto() {
		return preauthDto;
	}

	public void setPreauthDto(PreauthDTO preauthDto) {
		this.preauthDto = preauthDto;
	}

	public ClaimDto getClaimDTO() {
		return claimDTO;
	}

	public void setClaimDTO(ClaimDto claimDTO) {
		this.claimDTO = claimDTO;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getMrkPersonnel() {
		return mrkPersonnel;
	}

	public void setMrkPersonnel(Boolean mrkPersonnel) {
		this.mrkPersonnel = mrkPersonnel;
		//this.mrkPersonnelFlag = this.mrkPersonnel != null && mrkPersonnel ? "Yes" : "No" ;
	}

	public String getMrkPersonnelFlag() {
		return mrkPersonnelFlag;
	}

	public void setMrkPersonnelFlag(String mrkPersonnelFlag) {
		this.mrkPersonnelFlag = mrkPersonnelFlag;
		if(this.mrkPersonnelFlag != null && this.mrkPersonnelFlag.equals("Yes")) {
			this.mrkPersonnel = true;
		}
	}
	
	

}

package com.shaic.claim.preauth.wizard.view;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.Stage;
import com.shaic.reimbursement.draftinvesigation.DraftTriggerPointsToFocusDetailsTableDto;

public class InitiateInvestigationDTO {

	@NotNull(message = "Please Enter Allocation To.")
	private SelectValue allocationTo;

	@NotNull(message = "Please Enter Reason For Refering.")
	private String reasonForRefering;

	@NotNull(message = "Please Enter Trigger Points To Focus.")
	private String triggerPointsToFocus;

	private Long intimationkey;

	private Long policyKey;

	private Long claimKey;

	private InvestigationDetailsTableDTO investigationDetailsTableDTO;
	
	private Status status;
	
	private Stage stage;
	
	private Long stageKey;
	
	private Long statusKey;
	
	private Long transactionKey;
	
	private String transactionFlag;

	public SelectValue getAllocationTo() {
		return allocationTo;
	}

	public void setAllocationTo(SelectValue allocationTo) {
		this.allocationTo = allocationTo;
	}

	public String getReasonForRefering() {
		return reasonForRefering;
	}

	public void setReasonForRefering(String reasonForRefering) {
		this.reasonForRefering = reasonForRefering;
	}

	public String getTriggerPointsToFocus() {
		return triggerPointsToFocus;
	}

	public void setTriggerPointsToFocus(String triggerPointsToFocus) {
		this.triggerPointsToFocus = triggerPointsToFocus;
	}

	public InvestigationDetailsTableDTO getInvestigationDetailsTableDTO() {
		return investigationDetailsTableDTO;
	}

	public void setInvestigationDetailsTableDTO(
			InvestigationDetailsTableDTO investigationDetailsTableDTO) {
		this.investigationDetailsTableDTO = investigationDetailsTableDTO;
	}
	
	private List<DraftTriggerPointsToFocusDetailsTableDto> triggerPointsList;
	
	private List<DraftTriggerPointsToFocusDetailsTableDto> deletedTriggerPointsList;

	public Long getIntimationkey() {
		return intimationkey;
	}

	public void setIntimationkey(Long intimationkey) {
		this.intimationkey = intimationkey;
	}

	
	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}

	public Long getPolicyKey() {
		return policyKey;
	}

	public void setPolicyKey(Long policyKey) {
		this.policyKey = policyKey;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public Long getTransactionKey() {
		return transactionKey;
	}

	public void setTransactionKey(Long transactionKey) {
		this.transactionKey = transactionKey;
	}

	public String getTransactionFlag() {
		return transactionFlag;
	}

	public void setTransactionFlag(String transactionFlag) {
		this.transactionFlag = transactionFlag;
	}

	public Long getStageKey() {
		return stageKey;
	}

	public void setStageKey(Long stageKey) {
		this.stageKey = stageKey;
	}

	public Long getStatusKey() {
		return statusKey;
	}

	public void setStatusKey(Long statusKey) {
		this.statusKey = statusKey;
	}

	public List<DraftTriggerPointsToFocusDetailsTableDto> getTriggerPointsList() {
		return triggerPointsList;
	}

	public void setTriggerPointsList(
			List<DraftTriggerPointsToFocusDetailsTableDto> triggerPointsList) {
		this.triggerPointsList = triggerPointsList;
	}

	public List<DraftTriggerPointsToFocusDetailsTableDto> getDeletedTriggerPointsList() {
		return deletedTriggerPointsList;
	}

	public void setDeletedTriggerPointsList(
			List<DraftTriggerPointsToFocusDetailsTableDto> deletedTriggerPointsList) {
		this.deletedTriggerPointsList = deletedTriggerPointsList;
	}
}

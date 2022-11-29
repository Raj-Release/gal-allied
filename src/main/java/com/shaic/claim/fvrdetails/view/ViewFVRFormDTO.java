package com.shaic.claim.fvrdetails.view;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;

public class ViewFVRFormDTO extends AbstractTableDTO {
	
	private static final long serialVersionUID = 1L;
	
	@NotNull(message = "Please Choose Intimated By.")
	private SelectValue allocateTo;
	
	@NotNull(message = "Please Choose Assign To.")
	private SelectValue assignTo;
	
	@NotNull(message = "Please Choose Fvr Priority.")
	private SelectValue fvrPriority;
	

//	@NotNull(message = "Please Enter Caller Address.")
//	@Size(min = 1, message = "Please Enter Caller Address.")
	private String triggerPoints;
	
	private List<ViewFVRDTO> trgrPtsList; 
	
	private Long claimKey;
	
	private Long key;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}
	
	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}

	public SelectValue getAllocateTo() {
		return allocateTo;
	}

	public void setAllocateTo(SelectValue allocateTo) {
		this.allocateTo = allocateTo;
	}

	public String getTriggerPoints() {
		return triggerPoints;
	}

	public void setTriggerPoints(String triggerPoints) {
		this.triggerPoints = triggerPoints;
	}

	public SelectValue getAssignTo() {
		return assignTo;
	}

	public void setAssignTo(SelectValue assignTo) {
		this.assignTo = assignTo;
	}

	public SelectValue getFvrPriority() {
		return fvrPriority;
	}

	public void setFvrPriority(SelectValue fvrPriority) {
		this.fvrPriority = fvrPriority;
	}

	public List<ViewFVRDTO> getTrgrPtsList() {
		return trgrPtsList;
	}

	public void setTrgrPtsList(List<ViewFVRDTO> trgrPtsList) {
		this.trgrPtsList = trgrPtsList;
	}	

}

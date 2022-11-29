package com.shaic.claim.intimation.unlockintimationaudit;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class SearchUnlockIntimationAuditDBTableDTO  extends  AbstractTableDTO implements Serializable {
	
	private Long workflowKey;
	
	private String intimationNo;
	
	private String stage;
	
	private String lockedBy;
	
	private Long lobKey;
	
	//private HumanTaskDetail humanTaskDetail;

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public String getLockedBy() {
		return lockedBy;
	}

	public void setLockedBy(String lockedBy) {
		this.lockedBy = lockedBy;
	}
	
	public Long getWorkflowKey() {
		return workflowKey;
	}

	public void setWorkflowKey(Long workflowKey) {
		this.workflowKey = workflowKey;
	}

	public Long getLobKey() {
		return lobKey;
	}

	public void setLobKey(Long lobKey) {
		this.lobKey = lobKey;
	}
	
	

}

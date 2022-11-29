package com.shaic.claim.intimation.unlock;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class SearchUnlockIntimationTableDTO  extends  AbstractTableDTO implements Serializable {
	
	
	private String intimationNo;
	
	private String stage;
	
	private String lockedBy;
	
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

	

}

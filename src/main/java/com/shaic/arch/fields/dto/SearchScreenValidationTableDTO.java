/**
 * 
 */
package com.shaic.arch.fields.dto;

import com.shaic.arch.table.AbstractTableDTO;

/**
 * @author ntv.vijayar
 *
 */
public class SearchScreenValidationTableDTO extends AbstractTableDTO {
	
	private String rodNo;
	
	private String lastCompletedStage;
	
	private String status;
	
	private String lastRemarks;
	
	private String lockedUserId;
	
	private String cashlessNo;
	
	private Long cashlessKey;
	
	

	public Long getCashlessKey() {
		return cashlessKey;
	}

	public void setCashlessKey(Long cashlessKey) {
		this.cashlessKey = cashlessKey;
	}

	public String getCashlessNo() {
		return cashlessNo;
	}

	public void setCashlessNo(String cashlessNo) {
		this.cashlessNo = cashlessNo;
	}

	public String getRodNo() {
		return rodNo;
	}

	public void setRodNo(String rodNo) {
		this.rodNo = rodNo;
	}

	public String getLastCompletedStage() {
		return lastCompletedStage;
	}

	public void setLastCompletedStage(String lastCompletedStage) {
		this.lastCompletedStage = lastCompletedStage;
	}

	public String getLastRemarks() {
		return lastRemarks;
	}

	public void setLastRemarks(String lastRemarks) {
		this.lastRemarks = lastRemarks;
	}

	public String getLockedUserId() {
		return lockedUserId;
	}

	public void setLockedUserId(String lockedUserId) {
		this.lockedUserId = lockedUserId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}

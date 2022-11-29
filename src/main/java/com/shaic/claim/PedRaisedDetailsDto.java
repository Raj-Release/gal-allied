package com.shaic.claim;

import com.shaic.arch.table.AbstractTableDTO;

public class PedRaisedDetailsDto extends AbstractTableDTO{

	private String suggType;
	private String description;
	private String statusValue;
	private Long statusId;
	public String getSuggType() {
		return suggType;
	}
	public void setSuggType(String suggType) {
		this.suggType = suggType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStatusValue() {
		return statusValue;
	}
	public void setStatusValue(String statusValue) {
		this.statusValue = statusValue;
	}
	public Long getStatusId() {
		return statusId;
	}
	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}	
	
}

package com.shaic.claim.viewEarlierRodDetails;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class ViewProductBenefitsTableDTO extends AbstractTableDTO  implements Serializable{

	private String conditionCode;
	private String description;
	private String longDescription;
	public String getConditionCode() {
		return conditionCode;
	}
	public void setConditionCode(String conditionCode) {
		this.conditionCode = conditionCode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLongDescription() {
		return longDescription;
	}
	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}
	
	
}

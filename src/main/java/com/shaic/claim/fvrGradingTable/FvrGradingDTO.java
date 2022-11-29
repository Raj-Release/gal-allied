package com.shaic.claim.fvrGradingTable;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class FvrGradingDTO extends AbstractTableDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String category;
	
	private Boolean categoryValue = false;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Boolean getCategoryValue() {
		return categoryValue;
	}

	public void setCategoryValue(Boolean categoryValue) {
		this.categoryValue = categoryValue;
	}

}

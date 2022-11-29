package com.shaic.claim.rod.citySearchCriteria;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class SearchPayableAtTableDTO extends AbstractTableDTO  implements Serializable {

	private String payable;

	public String getPayable() {
		return payable;
	}

	public void setPayable(String payable) {
		this.payable = payable;
	}
	
	
}

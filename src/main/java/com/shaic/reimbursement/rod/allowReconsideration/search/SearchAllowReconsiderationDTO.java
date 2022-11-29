package com.shaic.reimbursement.rod.allowReconsideration.search;

import java.io.Serializable;

import com.shaic.arch.fields.dto.AbstractSearchDTO;
import com.shaic.arch.table.AbstractTableDTO;

public class SearchAllowReconsiderationDTO extends AbstractSearchDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String intimationNo;
	private String rodNo;
	
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public String getRodNo() {
		return rodNo;
	}
	public void setRodNo(String rodNo) {
		this.rodNo = rodNo;
	}
	
	

}

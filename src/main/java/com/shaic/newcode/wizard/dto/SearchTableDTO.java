package com.shaic.newcode.wizard.dto;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class SearchTableDTO extends AbstractTableDTO implements Serializable {

	private static final long serialVersionUID = -8529271807547913319L;
	
	private Long key;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}
	

}

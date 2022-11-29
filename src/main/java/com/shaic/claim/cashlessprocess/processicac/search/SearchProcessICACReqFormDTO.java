package com.shaic.claim.cashlessprocess.processicac.search;

import java.io.Serializable;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class SearchProcessICACReqFormDTO extends AbstractSearchDTO
		implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String intimationNo;

	private SelectValue clmType;

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public SelectValue getClmType() {
		return clmType;
	}

	public void setClmType(SelectValue clmType) {
		this.clmType = clmType;
	}

	
}

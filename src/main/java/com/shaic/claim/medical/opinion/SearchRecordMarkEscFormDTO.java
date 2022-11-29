package com.shaic.claim.medical.opinion;

import java.io.Serializable;

import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class SearchRecordMarkEscFormDTO  extends AbstractSearchDTO implements Serializable{

	private static final long serialVersionUID = -2649459376620334696L;

	private String intimationNo;	
	

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

}
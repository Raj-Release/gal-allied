package com.shaic.claim.cashlessprocess.flagreconsiderationrequest.search;

import java.io.Serializable;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class SearchFlagReconsiderationReqFormDTO extends AbstractSearchDTO
		implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String intimationNo;

	private SelectValue type;

	private String rodNo;

	private SelectValue intimationSource;

	private SelectValue networkHospType;

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public SelectValue getType() {
		return type;
	}

	public void setType(SelectValue type) {
		this.type = type;
	}
	
	public String getRodNo() {
		return rodNo;
	}

	public void setRodNo(String rodNo) {
		this.rodNo = rodNo;
	}

	public SelectValue getIntimationSource() {
		return intimationSource;
	}

	public void setIntimationSource(SelectValue intimationSource) {
		this.intimationSource = intimationSource;
	}

	public SelectValue getNetworkHospType() {
		return networkHospType;
	}

	public void setNetworkHospType(SelectValue networkHospType) {
		this.networkHospType = networkHospType;
	}

}

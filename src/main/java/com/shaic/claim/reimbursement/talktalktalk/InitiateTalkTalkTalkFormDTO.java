package com.shaic.claim.reimbursement.talktalktalk;

import java.io.Serializable;
import java.util.Map;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;


public class InitiateTalkTalkTalkFormDTO extends AbstractSearchDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4865095906533699381L;
	
	private String intimationNo;
	
	private SelectValue intimationyear;
	
	private Map<String, Object> filters;
	
	private ClaimDto claimDto;
	
	public String getIntimationNo() {
		return intimationNo;
	}
	
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public Map<String, Object> getFilters() {
		return filters;
	}
	
	public void setFilters(Map<String, Object> filters) {
		this.filters = filters;
	}

	public ClaimDto getClaimDto() {
		return claimDto;
	}

	public void setClaimDto(ClaimDto claimDto) {
		this.claimDto = claimDto;
	}

	public SelectValue getIntimationyear() {
		return intimationyear;
	}

	public void setIntimationyear(SelectValue intimationyear) {
		this.intimationyear = intimationyear;
	}

}

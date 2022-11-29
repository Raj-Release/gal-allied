package com.shaic.claim.registration;

import java.util.Map;

import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class GenerateCoveringLetterSearchDto extends AbstractSearchDTO {
	
	Map<String,Object> queryFilter;

	public Map<String, Object> getQueryFilter() {
		return queryFilter;
	}

	public void setQueryFilter(Map<String, Object> queryFilter) {
		this.queryFilter = queryFilter;
	}
	

}

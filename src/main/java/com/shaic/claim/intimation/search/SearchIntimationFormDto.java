package com.shaic.claim.intimation.search;

import java.io.Serializable;
import java.util.Map;

import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;


public class SearchIntimationFormDto extends AbstractSearchDTO implements Serializable{

	private Map<String, Object> filters;

	public Map<String, Object> getFilters() {
		return filters;
	}

	public void setFilters(Map<String, Object> filters) {
		this.filters = filters;
	}
	
	
	
}

package com.shaic.claim.cpuautoallocation;

import java.io.Serializable;

import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;


public class SearchCpuAutoAllocationDTO extends AbstractSearchDTO implements Serializable{

	/**
	 * 
	 */
	
	private String cpuName;
	
	public String getCpuName() {
		return cpuName;
	}

	public void setCpuName(String cpuName) {
		this.cpuName = cpuName;
	}
	
}

package com.shaic.claim.registration.ackhoscomm.search;

import java.io.Serializable;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.arch.table.Pageable;

public class AbstractSearchDTO  extends AbstractTableDTO implements Serializable {

	private static final long serialVersionUID = -539356869023440935L;

	private Pageable pageable;
	
	private String username;
	
	private String password;
	
	private SelectValue priority;
	
	private SelectValue type;
	
	private SelectValue source;
	
	private SelectValue claimType;
	
	//Added for no records found enhancement
	private String searchName;
	/*
	 * The below feild was planned to name as
	 * intimationNo. But since this DTO is
	 * reused, may be we might face a name clash
	 * problem. Hence searchId was coined.
	 * */
	private String searchId; 

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Pageable getPageable() {
		return pageable;
	}

	public void setPageable(Pageable pageable) {
		this.pageable = pageable;
	}

	public SelectValue getPriority() {
		return priority;
	}

	public void setPriority(SelectValue priority) {
		this.priority = priority;
	}

	public SelectValue getType() {
		return type;
	}

	public void setType(SelectValue type) {
		this.type = type;
	}

	public SelectValue getSource() {
		return source;
	}

	public void setSource(SelectValue source) {
		this.source = source;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public String getSearchId() {
		return searchId;
	}

	public void setSearchId(String searchId) {
		this.searchId = searchId;
	}

	public SelectValue getClaimType() {
		return claimType;
	}

	public void setClaimType(SelectValue claimType) {
		this.claimType = claimType;
	}
	
	

}

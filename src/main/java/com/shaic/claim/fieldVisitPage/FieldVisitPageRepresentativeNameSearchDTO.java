package com.shaic.claim.fieldVisitPage;

import com.shaic.arch.fields.dto.SelectValue;

public class FieldVisitPageRepresentativeNameSearchDTO {
	
	private SelectValue state;
	
	private SelectValue city;
	
	private SelectValue branch;
	
	private SelectValue allocationTo;

	public SelectValue getState() {
		return state;
	}

	public void setState(SelectValue state) {
		this.state = state;
	}

	public SelectValue getCity() {
		return city;
	}

	public void setCity(SelectValue city) {
		this.city = city;
	}

	public SelectValue getBranch() {
		return branch;
	}

	public void setBranch(SelectValue branch) {
		this.branch = branch;
	}

	public SelectValue getAllocationTo() {
		return allocationTo;
	}

	public void setAllocationTo(SelectValue allocationTo) {
		this.allocationTo = allocationTo;
	}
	
	

}

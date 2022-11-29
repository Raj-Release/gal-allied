package com.shaic.newcode.wizard.dto;

import java.io.Serializable;

import javax.validation.constraints.Pattern;

public class NewBornBabyDetailsDTO implements Serializable
{
	private static final long serialVersionUID = 3754120619981120595L;
	
	@Pattern(regexp="^[a-zA-Z0-9./']*$", message="Please Enter a valid First Name")
	private String name;
	private Long relationShipId;
	public String getName() {
		return name;
	}
	public Long getRelationShipId() {
		return relationShipId;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setRelationShipId(Long relationShipId) {
		this.relationShipId = relationShipId;
	}
}

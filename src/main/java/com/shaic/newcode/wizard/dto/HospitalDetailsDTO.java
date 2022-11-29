package com.shaic.newcode.wizard.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.intimation.create.dto.HospitalDto;
import com.shaic.domain.CityTownVillage;
import com.shaic.domain.State;


public class HospitalDetailsDTO {
	
private static final long serialVersionUID = -4393948868604854977L;
	
	private Long key;
	
	@Pattern(regexp="^[a-zA-Z0-9./' ]*$", message="Please Enter a valid Hospital Name")
	private String name; 
	
	@NotNull(message = "Please choose Hospital Type")
	private SelectValue hospitalType;
	
	private State state;
	
	private CityTownVillage city;
	
	private HospitalDto hospital;
	
	public Long getKey() {
		return key;
	}
	public String getName() {
		return name;
	}
	public SelectValue getHospitalType() {
		return hospitalType;
	}
	public void setKey(Long key) {
		this.key = key;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setHospitalType(SelectValue hospitalType) {
		this.hospitalType = hospitalType;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public HospitalDto getHospital() {
		return hospital;
	}
	public void setHospital(HospitalDto hospital) {
		this.hospital = hospital;
	}
	public CityTownVillage getCity() {
		return city;
	}
	public void setCity(CityTownVillage city) {
		this.city = city;
	}
	
}

package com.shaic.domain;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;

public class InsuredDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long key;
	
	private Policy policy;

	private String insuredName;
	
	private Long insuredId;
	
	private String insuredEmployeeId;
	
	private SelectValue insuredGender;
	
	private Integer insuredAge;
	
	private Date insuredDateOfBirth;
	
	private SelectValue relationshipwithInsuredId;
	
	private String healthCardNumber;

	private String registerdMobileNumber;
	
	private Double insuredSumInsured;
	
	private Double insuredRechargedSI;
	
	private Double insuredRestoredSI;
	
	private Double cummulativeBonus;
	
	private String lobFlag;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Policy getPolicy() {
		return policy;
	}

	public void setPolicy(Policy policy) {
		this.policy = policy;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public Long getInsuredId() {
		return insuredId;
	}

	public void setInsuredId(Long insuredId) {
		this.insuredId = insuredId;
	}

	public String getInsuredEmployeeId() {
		return insuredEmployeeId;
	}

	public void setInsuredEmployeeId(String insuredEmployeeId) {
		this.insuredEmployeeId = insuredEmployeeId;
	}

	public SelectValue getInsuredGender() {
		return insuredGender;
	}

	public void setInsuredGender(SelectValue insuredGender) {
		this.insuredGender = insuredGender;
	}

	public Integer getInsuredAge() {
		return insuredAge;
	}

	public void setInsuredAge(Integer insuredAge) {
		this.insuredAge = insuredAge;
	}

	public Date getInsuredDateOfBirth() {
		return insuredDateOfBirth;
	}

	public void setInsuredDateOfBirth(Date insuredDateOfBirth) {
		this.insuredDateOfBirth = insuredDateOfBirth;
	}

	public SelectValue getRelationshipwithInsuredId() {
		return relationshipwithInsuredId;
	}

	public void setRelationshipwithInsuredId(SelectValue relationshipwithInsuredId) {
		this.relationshipwithInsuredId = relationshipwithInsuredId;
	}

	public String getHealthCardNumber() {
		return healthCardNumber;
	}

	public void setHealthCardNumber(String healthCardNumber) {
		this.healthCardNumber = healthCardNumber;
	}

	public String getRegisterdMobileNumber() {
		return registerdMobileNumber;
	}

	public void setRegisterdMobileNumber(String registerdMobileNumber) {
		this.registerdMobileNumber = registerdMobileNumber;
	}

	public Double getInsuredSumInsured() {
		return insuredSumInsured;
	}

	public void setInsuredSumInsured(Double insuredSumInsured) {
		this.insuredSumInsured = insuredSumInsured;
	}

	public Double getInsuredRechargedSI() {
		return insuredRechargedSI;
	}

	public void setInsuredRechargedSI(Double insuredRechargedSI) {
		this.insuredRechargedSI = insuredRechargedSI;
	}

	public Double getInsuredRestoredSI() {
		return insuredRestoredSI;
	}

	public void setInsuredRestoredSI(Double insuredRestoredSI) {
		this.insuredRestoredSI = insuredRestoredSI;
	}

	public Double getCummulativeBonus() {
		return cummulativeBonus;
	}

	public void setCummulativeBonus(Double cummulativeBonus) {
		this.cummulativeBonus = cummulativeBonus;
	}

	public String getLobFlag() {
		return lobFlag;
	}

	public void setLobFlag(String lobFlag) {
		this.lobFlag = lobFlag;
	}
	
	

}

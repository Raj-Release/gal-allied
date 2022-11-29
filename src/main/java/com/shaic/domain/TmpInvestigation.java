package com.shaic.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the IMS_TMP_INVESTIGATION database table.
 * 
 */
@Entity
@Table(name="IMS_TMP_INVESTIGATION")
@NamedQueries({
	@NamedQuery(name="TmpInvestigation.findAll", query="SELECT o FROM TmpInvestigation o where o.activeStatus is not null and o.activeStatus = 1"),
	@NamedQuery(name="TmpInvestigation.findByInvestigaitonKey", query="SELECT o FROM TmpInvestigation o where o.key =:investigationkey and o.activeStatus is not null and o.activeStatus = 1"),
	@NamedQuery(name="TmpInvestigation.findByInvestigaitonCode", query="SELECT o FROM TmpInvestigation o where o.investigatorCode =:investigatorCode and o.activeStatus is not null and o.activeStatus = 1"),
	@NamedQuery(name="TmpInvestigation.findByStateCity",query="SELECT  o FROM TmpInvestigation o where (o.state.key =:stateId and o.cityTownVillage.key =:cityId and o.allocationTo.key =:categoryId and o.activeStatus is not null and o.activeStatus = 1)"),
	@NamedQuery(name="TmpInvestigation.findTataTrust", query="SELECT o FROM TmpInvestigation o where o.tatTrustStatus is not null and o.tatTrustStatus = 1"),
	@NamedQuery(name="TmpInvestigation.findByInvestigaitonCodeInactive", query="SELECT o FROM TmpInvestigation o where o.investigatorCode =:investigatorCode and o.activeStatus is not null"),
	@NamedQuery(name="TmpInvestigation.findByAllocationTo", query="SELECT o FROM TmpInvestigation o where o.allocationTo.key =:allocationTo and o.activeStatus is not null and o.activeStatus = 1"),
	@NamedQuery(name="TmpInvestigation.findByAllocation", query="SELECT o FROM TmpInvestigation o where o.allocationTo.key =:allocationTo and o.activeStatus is not null"),
	@NamedQuery(name="TmpInvestigation.findByInvestigaitonCodeWithoutStatus", query="SELECT o FROM TmpInvestigation o where o.investigatorCode =:investigatorCode"),
})
public class TmpInvestigation implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="IMS_CLS_SEQ_INVESTIGATION_KEY_GENERATOR", sequenceName = "SEQ_INVESTIGATION_KEY" , allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_SEQ_INVESTIGATION_KEY_GENERATOR" )	
	@Column(name = "INVESTIGATION_KEY")
	private Long key;
	
	@Column(name="INVESTIGATOR_CODE")
	private String investigatorCode;

	@Column(name="INVESTIGATOR_NAME")
	private String investigatorName;

	@Column(name="MOBILE_NUMBER")
	private Long mobileNumber;

	@Column(name="PHONE_NUMBER")
	private Long phoneNumber;
	
	@JoinColumn(name = "STATE_ID")
	@OneToOne
	private State state;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;

	@JoinColumn(name = "CITY_ID")
	@OneToOne
	private CityTownVillage cityTownVillage;

	@JoinColumn(name = "ALLOCATION_TO_ID")
	@OneToOne
	private MastersValue allocationTo;

	@Column(name = "BRANCH_CODE")
	private String branchCode;	
	
	@Column(name = "MAX_COUNT")
	private int maxCount;
	
	@Column(name="TATA_PROD_FLAG")
	private Long tatTrustStatus;
	
	@Column(name="EMAIL_ID")
	private String emailID;
	
	@Column(name="GENDER")
	private String gender;
	
	@Column(name="USER_ID")
	private String starEmployeeID;
	
	@Column(name="ADD_MOBILE_NUMBER")
	private Long additionalMobileNumber;

	
	public String getEmailID() {
		return emailID;
	}

	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public TmpInvestigation() {
	}

	public String getInvestigatorCode() {
		return this.investigatorCode;
	}

	public void setInvestigatorCode(String investigatorCode) {
		this.investigatorCode = investigatorCode;
	}

	public String getInvestigatorName() {
		return this.investigatorName;
	}

	public void setInvestigatorName(String investigatorName) {
		this.investigatorName = investigatorName;
	}

	public Long getMobileNumber() {
		return this.mobileNumber;
	}

	public void setMobileNumber(Long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Long getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(Long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public CityTownVillage getCityTownVillage() {
		return cityTownVillage;
	}

	public void setCityTownVillage(CityTownVillage cityTownVillage) {
		this.cityTownVillage = cityTownVillage;
	}

	public MastersValue getAllocationTo() {
		return allocationTo;
	}

	public void setAllocationTo(MastersValue allocationTo) {
		this.allocationTo = allocationTo;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	public Long getTatTrustStatus() {
		return tatTrustStatus;
	}

	public void setTatTrustStatus(Long tatTrustStatus) {
		this.tatTrustStatus = tatTrustStatus;
	}

	public String getStarEmployeeID() {
		return starEmployeeID;
	}

	public void setStarEmployeeID(String starEmployeeID) {
		this.starEmployeeID = starEmployeeID;
	}

	public Long getAdditionalMobileNumber() {
		return additionalMobileNumber;
	}

	public void setAdditionalMobileNumber(Long additionalMobileNumber) {
		this.additionalMobileNumber = additionalMobileNumber;
	}	
	
}
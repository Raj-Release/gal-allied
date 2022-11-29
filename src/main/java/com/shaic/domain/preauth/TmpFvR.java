package com.shaic.domain.preauth;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.shaic.domain.CityTownVillage;
import com.shaic.domain.MastersValue;
import com.shaic.domain.State;

@Entity
@Table(name = "MAS_FVR")
@NamedQueries({
		@NamedQuery(name = "TmpFvR.findAll", query = "SELECT o FROM TmpFvR o where o.activeStatus is not null and o.activeStatus = 1"),
		@NamedQuery(name = "TmpFvR.findByCode", query = "SELECT  o FROM TmpFvR o where o.representiveCode=:code and o.activeStatus is not null and o.activeStatus = 1"),
		@NamedQuery(name = "TmpFvR.findByRepresentativeName", query = "SELECT  o FROM TmpFvR o where Lower(o.representiveName) like :representativeName and o.activeStatus is not null and o.activeStatus = 1"),
		@NamedQuery(name = "TmpFvR.findByState", query = "SELECT  o FROM TmpFvR o where o.state.key =:stateId and o.activeStatus is not null and o.activeStatus = 1"),
		@NamedQuery(name = "TmpFvR.findByStateCityBrach", query = "SELECT  o FROM TmpFvR o where (o.state.key =:stateId and o.cityTownVillage.key =:cityId) and o.activeStatus is not null and o.activeStatus = 1") })
public class TmpFvR implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "FVR_CODE", nullable = false)
	private String representiveCode;

	@Column(name = "DOC_REP_NAME")
	private String representiveName;

	@Column(name = "REP_CATEGORY")
	private String category;

	@Column(name = "MAX_NO_ALLOC")
	private Long totalAllocation;

//	@Column(name = "ALLOCATED")
	@Transient
	private Long allocated;

//	@Column(name = "CAN_BE_ALLOCATED")
	@Transient
	private String canBeAllocated;

	@Column(name = "MOBILE_NO")
	private String mobileNumber;

	@Column(name = "TELE_NO")
	private String phoneNumber;

	@JoinColumn(name = "STATE_ID_PRESENT")
	@OneToOne
	private State state;

	@JoinColumn(name = "CITY_ID_PRESENT")
	@OneToOne
	private CityTownVillage cityTownVillage;

	@JoinColumn(name = "DOC_REP_TYPE_ID")
	@OneToOne
	private MastersValue allocationTo;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;

//	@Column(name = "BRANCH_CODE")
	@Transient
	private String branchCode;

	public Long getTotalAllocation() {
		return totalAllocation;
	}

	public void setTotalAllocation(Long totalAllocation) {
		this.totalAllocation = totalAllocation;
	}

	public Long getAllocated() {
		return allocated;
	}

	public void setAllocated(Long allocated) {
		this.allocated = allocated;
	}

	public String getCanBeAllocated() {
		return canBeAllocated;
	}

	public void setCanBeAllocated(String canBeAllocated) {
		this.canBeAllocated = canBeAllocated;
	}

	
	public String getRepresentiveCode() {
		return representiveCode;
	}

	public void setRepresentiveCode(String representiveCode) {
		this.representiveCode = representiveCode;
	}

	public String getRepresentiveName() {
		return representiveName;
	}

	public void setRepresentiveName(String representiveName) {
		this.representiveName = representiveName;
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

}
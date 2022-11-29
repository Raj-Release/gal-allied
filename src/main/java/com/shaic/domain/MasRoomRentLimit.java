package com.shaic.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "MAS_GMC_ROOMRENTLIMIT")
@NamedQueries({
		@NamedQuery(name = "MasRoomRentLimit.findAll", query = "SELECT i FROM MasRoomRentLimit i"),
		@NamedQuery(name = "MasRoomRentLimit.findByPolicyKey", query = "SELECT i FROM MasRoomRentLimit i where i.policyKey = :policyKey"),
 		@NamedQuery(name = "MasRoomRentLimit.findBasedOnSIFromTo", query = "SELECT i FROM MasRoomRentLimit i where i.policyKey = :policyKey and i.sumInsuredFrom = :sumInsuredFrom and i.sumInsuredTo = :sumInsuredTo and i.roomType = :roomType"),
		@NamedQuery(name = "MasRoomRentLimit.findBasedOnSITo", query = "SELECT i FROM MasRoomRentLimit i where i.policyKey = :policyKey and  (:sumInsuredTo) BETWEEN i.sumInsuredFrom AND i.sumInsuredTo and i.roomType = :roomType"),
		@NamedQuery(name = "MasRoomRentLimit.findBasedOnSI", query = "SELECT i FROM MasRoomRentLimit i where i.policyKey = :policyKey and  (:sumInsuredTo) BETWEEN i.sumInsuredFrom AND i.sumInsuredTo order by i.key")
})
public class MasRoomRentLimit implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="MAS_GMC_ROOMRENTLIMIT_KEY_GENERATOR", sequenceName = "SEQ_GMC_HOS_KEY"  ,allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MAS_GMC_ROOMRENTLIMIT_KEY_GENERATOR" ) 
	@Column(name = "ROOMREMT_KEY")
	private Long key;

	@Column(name = "POLICY_KEY")
	private Long policyKey;

	@Column(name = "LIMIT_AMOUNT")
	private Double limitAmount;
	
	@Column(name = "LIMIT_PER")
	private Double limitPercentage;
	
	@Column(name = "NR_LIMIT")
	private Double nrLimit;
	
	@Column(name = "STD_AC")
	private Double stdAc;

	@Column(name = "SI_FROM")
	private Double sumInsuredFrom;
	
	@Column(name = "SI_TO")
	private Double sumInsuredTo;
	
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name="ACTIVE_STATUS")
	private String activeStatus;
	
	@Column(name="ROOM_TYPE")
	private String roomType;
	
	
	@Column(name="PROPORTIONATE_FLAG")
	private String proportionateFlag;
	
	@Column(name="CHARGES")
	private String Charges;
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getPolicyKey() {
		return policyKey;
	}

	public void setPolicyKey(Long policyKey) {
		this.policyKey = policyKey;
	}

	public Double getLimitAmount() {
		return limitAmount;
	}

	public void setLimitAmount(Double limitAmount) {
		this.limitAmount = limitAmount;
	}

	public Double getLimitPercentage() {
		return limitPercentage;
	}

	public void setLimitPercentage(Double limitPercentage) {
		this.limitPercentage = limitPercentage;
	}

	public Double getNrLimit() {
		return nrLimit;
	}

	public void setNrLimit(Double nrLimit) {
		this.nrLimit = nrLimit;
	}

	public Double getStdAc() {
		return stdAc;
	}

	public void setStdAc(Double stdAc) {
		this.stdAc = stdAc;
	}

	public Double getSumInsuredFrom() {
		return sumInsuredFrom;
	}

	public void setSumInsuredFrom(Double sumInsuredFrom) {
		this.sumInsuredFrom = sumInsuredFrom;
	}

	public Double getSumInsuredTo() {
		return sumInsuredTo;
	}

	public void setSumInsuredTo(Double sumInsuredTo) {
		this.sumInsuredTo = sumInsuredTo;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}

	public String getProportionateFlag() {
		return proportionateFlag;
	}

	public void setProportionateFlag(String proportionateFlag) {
		this.proportionateFlag = proportionateFlag;
	}

	public String getCharges() {
		return Charges;
	}

	public void setCharges(String charges) {
		Charges = charges;
	}

}

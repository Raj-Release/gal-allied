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
@Table(name = "MAS_GMC_PREPOSTHOSPLIMIT")
@NamedQueries({
		@NamedQuery(name = "MasPrePostHospLimit.findAll", query = "SELECT i FROM MasPrePostHospLimit i"),
		@NamedQuery(name = "MasPrePostHospLimit.findByPolicyKey", query = "SELECT i FROM MasPrePostHospLimit i where i.policyKey = :policyKey"),
		@NamedQuery(name = "MasPrePostHospLimit.findBasedOnSIFromTo", query = "SELECT i FROM MasPrePostHospLimit i where i.policyKey = :policyKey and i.sumInsuredFrom = :sumInsuredFrom and i.sumInsuredTo = :sumInsuredTo and i.hospitalType = :hospitalType")
})
public class MasPrePostHospLimit implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="MAS_GMC_PREPOSTHOSPLIMIT_KEY_GENERATOR", sequenceName = "SEQ_GMC_HOS_KEY"  ,allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MAS_GMC_PREPOSTHOSPLIMIT_KEY_GENERATOR" ) 
	@Column(name = "HOS_KEY")
	private Long key;

	@Column(name = "POLICY_KEY")
	private Long policyKey;
	
	@Column(name = "HOS_TYPE")
	private String hospitalType;

	@Column(name = "LIMIT_AMOUNT")
	private Double limitAmount;
	
	@Column(name = "LIMIT_PER")
	private String limitPercentage;
	
	@Column(name = "NO_DAYS")
	private String noOfDays;

	@Column(name = "SI_FROM")
	private Double sumInsuredFrom;
	
	@Column(name = "SI_TO")
	private Double sumInsuredTo;
	
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name="ACTIVE_STATUS")
	private String activeStatus;

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

	public String getHospitalType() {
		return hospitalType;
	}

	public void setHospitalType(String hospitalType) {
		this.hospitalType = hospitalType;
	}

	

	public String getLimitPercentage() {
		return limitPercentage;
	}

	public void setLimitPercentage(String limitPercentage) {
		this.limitPercentage = limitPercentage;
	}

	public String getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(String noOfDays) {
		this.noOfDays = noOfDays;
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

	public Double getLimitAmount() {
		return limitAmount;
	}

	public void setLimitAmount(Double limitAmount) {
		this.limitAmount = limitAmount;
	}

}

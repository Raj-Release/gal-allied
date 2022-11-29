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
@Table(name = "MAS_GMC_COPAYLIMIT")
@NamedQueries({
		@NamedQuery(name = "MasCopayLimit.findAll", query = "SELECT i FROM MasCopayLimit i"),
		@NamedQuery(name = "MasCopayLimit.findByPolicyKey", query = "SELECT i FROM MasCopayLimit i where i.policyKey = :policyKey"),
		@NamedQuery(name = "MasCopayLimit.findBasedOnSIFromTo", query = "SELECT i FROM MasCopayLimit i where i.policyKey = :policyKey and i.sumInsuredFrom = :sumInsuredFrom and i.sumInsuredTo = :sumInsuredTo and i.claimType = :claimType")
})
public class MasCopayLimit implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="MAS_GMC_COPAYLIMIT_KEY_GENERATOR", sequenceName = "SEQ_GMC_COPAY_KEY"  ,allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MAS_GMC_COPAYLIMIT_KEY_GENERATOR" ) 
	@Column(name = "COPAY_KEY")
	private Long key;

	@Column(name = "POLICY_KEY")
	private Long policyKey;
	
	@Column(name = "AGE_TYPE")
	private String ageType;
	
	@Column(name = "CLAIM_TYPE")
	private String claimType;
	
	/*@Column(name = "COPAY_PER")
	private Long copayPercentage;*/
	@Column(name = "COPAY_PER")
	private Double copayPercentage;
	
	@Column(name = "END_NO_IDX")
	private Long endNoIndex;
	
	@Column(name = "REL_TYPE")
	private String relType;

	@Column(name = "LIMIT_PER_FAMILY")
	private Double limitPerFamily;
	
	@Column(name = "LIMIT_PER_POLICY")
	private Double limitPerPolicy;
	
	@Column(name = "LIMIT_PER_RISK")
	private Double limitPerRisk;

	@Column(name = "SI_FROM")
	private Double sumInsuredFrom;
	
	@Column(name = "SI_TO")
	private Double sumInsuredTo;
	
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name="ACTIVE_STATUS")
	private String activeStatus;
	
	@Column(name="AGE_FROM")
	private Integer ageFrom;
	
	@Column(name="AGE_TO")
	private Integer ageTo;

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

	public Double getLimitPerFamily() {
		return limitPerFamily;
	}

	public void setLimitPerFamily(Double limitPerFamily) {
		this.limitPerFamily = limitPerFamily;
	}

	public Double getLimitPerPolicy() {
		return limitPerPolicy;
	}

	public void setLimitPerPolicy(Double limitPerPolicy) {
		this.limitPerPolicy = limitPerPolicy;
	}

	public Double getLimitPerRisk() {
		return limitPerRisk;
	}

	public void setLimitPerRisk(Double limitPerRisk) {
		this.limitPerRisk = limitPerRisk;
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

	public String getAgeType() {
		return ageType;
	}

	public void setAgeType(String ageType) {
		this.ageType = ageType;
	}

	/*public Long getClaimType() {
		return claimType;
	}

	public void setClaimType(Long claimType) {
		this.claimType = claimType;
	}*/

	public Long getEndNoIndex() {
		return endNoIndex;
	}

	public void setEndNoIndex(Long endNoIndex) {
		this.endNoIndex = endNoIndex;
	}

	public String getRelType() {
		return relType;
	}

	public void setRelType(String relType) {
		this.relType = relType;
	}

	public Double getCopayPercentage() {
		return copayPercentage;
	}

	public void setCopayPercentage(Double copayPercentage) {
		this.copayPercentage = copayPercentage;
	}

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	public Integer getAgeFrom() {
		return ageFrom;
	}

	public void setAgeFrom(Integer ageFrom) {
		this.ageFrom = ageFrom;
	}

	public Integer getAgeTo() {
		return ageTo;
	}

	public void setAgeTo(Integer ageTo) {
		this.ageTo = ageTo;
	}
	
	

}

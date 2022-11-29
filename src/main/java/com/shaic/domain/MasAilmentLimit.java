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
@Table(name = "MAS_GMC_AILMENTLIMIT")
@NamedQueries({
		@NamedQuery(name = "MasAilmentLimit.findAll", query = "SELECT i FROM MasAilmentLimit i"),
		@NamedQuery(name = "MasAilmentLimit.findByKey", query = "SELECT i FROM MasAilmentLimit i where i.key = :limitKey"),
		@NamedQuery(name = "MasAilmentLimit.findByPolicyKey", query = "SELECT i FROM MasAilmentLimit i where i.policyKey = :policyKey"),
		@NamedQuery(name = "MasAilmentLimit.findBasedOnSIFromTo", query = "SELECT i FROM MasAilmentLimit i where i.policyKey = :policyKey and i.sumInsuredFrom = :sumInsuredFrom and i.sumInsuredTo = :sumInsuredTo and i.ailment = :ailment")
})
public class MasAilmentLimit implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="MAS_GMC_AILMENTLIMIT_KEY_GENERATOR", sequenceName = "SEQ_GMC_AILMENT_KEY"  ,allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MAS_GMC_AILMENTLIMIT_KEY_GENERATOR" ) 
	@Column(name = "AILMENT_KEY")
	private Long key;

	@Column(name = "POLICY_KEY")
	private Long policyKey;
	
	@Column(name = "AILMENT")
	private String ailment;
	
	@Column(name = "SUB_LIMIT_NAME")
	private String submlimitName;
	
	@Column(name = "LIMIT_PER_CLAIM")
	private Double limitPerClaim;
	
	@Column(name = "LIMIT_PER_EYE")
	private Double limitPerEye;
	
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

	public String getAilment() {
		return ailment;
	}

	public void setAilment(String ailment) {
		this.ailment = ailment;
	}

	public String getSubmlimitName() {
		return submlimitName;
	}

	public void setSubmlimitName(String submlimitName) {
		this.submlimitName = submlimitName;
	}

	public Double getLimitPerClaim() {
		return limitPerClaim;
	}

	public void setLimitPerClaim(Double limitPerClaim) {
		this.limitPerClaim = limitPerClaim;
	}

	public Double getLimitPerEye() {
		return limitPerEye;
	}

	public void setLimitPerEye(Double limitPerEye) {
		this.limitPerEye = limitPerEye;
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

}

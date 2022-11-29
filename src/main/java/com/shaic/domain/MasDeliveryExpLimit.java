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
@Table(name = "MAS_GMC_DELIVERYEXPLIMIT")
@NamedQueries({
		@NamedQuery(name = "MasDeliveryExpLimit.findAll", query = "SELECT i FROM MasDeliveryExpLimit i"),
		@NamedQuery(name = "MasDeliveryExpLimit.findByPolicyKey", query = "SELECT i FROM MasDeliveryExpLimit i where i.policyKey = :policyKey"),
		@NamedQuery(name = "MasDeliveryExpLimit.findBasedOnSIFromTo", query = "SELECT i FROM MasDeliveryExpLimit i where i.policyKey = :policyKey and i.sumInsuredFrom = :sumInsuredFrom and i.sumInsuredTo = :sumInsuredTo and i.deliveryType = :delType")
})
public class MasDeliveryExpLimit implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="MAS_GMC_DELIVERYEXPLIMIT_KEY_GENERATOR", sequenceName = "SEQ_GMC_DEL_KEY"  ,allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MAS_GMC_DELIVERYEXPLIMIT_KEY_GENERATOR" ) 
	@Column(name = "DEL_KEY")
	private Long key;

	@Column(name = "POLICY_KEY")
	private Long policyKey;
	
	@Column(name="DEL_TYPE")
	private String deliveryType;
	
	@Column(name = "END_NO_IDX")
	private Long endNoIndex;

	@Column(name = "LIMIT_AMOUNT")
	private Long limitAmount;

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

	public Long getEndNoIndex() {
		return endNoIndex;
	}

	public void setEndNoIndex(Long endNoIndex) {
		this.endNoIndex = endNoIndex;
	}

	public Long getLimitAmount() {
		return limitAmount;
	}

	public void setLimitAmount(Long limitAmount) {
		this.limitAmount = limitAmount;
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

	public String getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}

}

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
import javax.persistence.Transient;

@Entity
@Table(name = "IMS_CLS_POLICY_COVER_DETAILS")
@NamedQueries({
		@NamedQuery(name = "PolicyCoverDetails.findAll", query = "SELECT i FROM PolicyCoverDetails i"),
		@NamedQuery(name = "PolicyCoverDetails.findByPolicy", query = "SELECT i FROM PolicyCoverDetails i where i.policyKey = :policyKey")
})
public class PolicyCoverDetails implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="IMS_CLS_POLICY_COVER_DETAILS_KEY_GENERATOR", sequenceName = "SEQ_CVR_DTLS_KEY"  ,allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_POLICY_COVER_DETAILS_KEY_GENERATOR" ) 
	@Column(name = "CVR_DTLS_KEY")
	private Long key;
	
	@Column(name = "COVER_CODE")
	private String coverCode;
	
	@Column(name = "POLICY_KEY")
	private Long policyKey;
	
	@Column(name = "RISK_ID")
	private Long riskId;
	
	@Column(name = "COVER_DESCRIPTION")
	private String coverCodeDescription;
	
	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "SUM_INSURED")
	private Double sumInsured;
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getCoverCode() {
		return coverCode;
	}

	public void setCoverCode(String coverCode) {
		this.coverCode = coverCode;
	}

	public String getCoverCodeDescription() {
		return coverCodeDescription;
	}

	public void setCoverCodeDescription(String coverCodeDescription) {
		this.coverCodeDescription = coverCodeDescription;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Long getPolicyKey() {
		return policyKey;
	}

	public void setPolicyKey(Long policyKey) {
		this.policyKey = policyKey;
	}

	public Double getSumInsured() {
		return sumInsured;
	}

	public void setSumInsured(Double sumInsured) {
		this.sumInsured = sumInsured;
	}

	public Long getRiskId() {
		return riskId;
	}

	public void setRiskId(Long riskId) {
		this.riskId = riskId;
	}
	
	

}

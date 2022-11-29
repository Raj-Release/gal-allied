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
@Table(name = "IMS_CLS_POLICY_COVER")
@NamedQueries({
		@NamedQuery(name = "PolicyRiskCover.findAll", query = "SELECT i FROM PolicyRiskCover i"),
		@NamedQuery(name = "PolicyRiskCover.findByPolicy", query = "SELECT i FROM PolicyRiskCover i where i.policyKey = :policyKey")
})
public class PolicyRiskCover implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="IMS_CLS_POLICY_COVER_KEY_GENERATOR", sequenceName = "SEQ_POLICY_COVER_KEY"  ,allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_POLICY_COVER_KEY_GENERATOR" ) 
	@Column(name = "POLICY_COVER_KEY")
	private Long key;
	
	@Column(name = "COVER_CODE")
	private String coverCode;
	
	@Column(name = "POLICY_KEY")
	private Long policyKey;
	
	@Column(name = "COVER_DESCRIPTION")
	private String coverCodeDescription;
	
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(nullable = true, columnDefinition = "NUMBER", name="ACTIVE_STATUS", length = 1)
	private Boolean activeStatus;

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

	public Boolean getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Boolean activeStatus) {
		this.activeStatus = activeStatus;
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
	
	

}

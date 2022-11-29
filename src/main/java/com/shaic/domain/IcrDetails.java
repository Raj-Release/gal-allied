package com.shaic.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "IMS_CLS_GMC_CLAIM_RATIO_COLOR")
@NamedQueries({
	@NamedQuery(name = "IcrDetails.findAll", query = "SELECT i FROM IcrDetails i"),
	@NamedQuery(name = "IcrDetails.findByPolicyNumber", query = "SELECT i FROM IcrDetails i where i.policyNumber = :policyNumber")
})

public class IcrDetails implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Id
	@Column(name="RATIO_COLOR_KEY")
	private Long key;
	
	@Column(name = "POLICY_KEY")
	private Long policyKey;
	             
	       
	@Column(name = "POLICY_NUMBER")
	private String policyNumber;
	
	@Column(name = "GROSS_PREMIUM") 
	private Double grossPremium;
	
	@Column(name = "NO_OF_DAYS")
	private Integer noOfDays;
	
	@Column(name = "EARNED_PREMIUM") 
	private Double endorPremium;
	
	@Column(name = "CLAIM_PAID") 
	private Double paidAmount;
	
	@Column(name = "CLAIM_OUTSTANDING")
	private Double outstandingAmount;
	
	@Column(name = "CLAIM_RATIO") 
	private Double claimRatio;
	
	@Column(name = "CLAIM_COLOUR") 
	private String color;
	
	@Column(name = "ACTIVE_STATUS")       
	private Integer activeStatus;
	
	@Column(name = "INSURED_NUMBER")
	private Long insuredNumber;

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

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public Double getGrossPremium() {
		return grossPremium;
	}

	public void setGrossPremium(Double grossPremium) {
		this.grossPremium = grossPremium;
	}

	public Integer getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(Integer noOfDays) {
		this.noOfDays = noOfDays;
	}

	public Double getEndorPremium() {
		return endorPremium;
	}

	public void setEndorPremium(Double endorPremium) {
		this.endorPremium = endorPremium;
	}

	public Double getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(Double paidAmount) {
		this.paidAmount = paidAmount;
	}

	public Double getOutstandingAmount() {
		return outstandingAmount;
	}

	public void setOutstandingAmount(Double outstandingAmount) {
		this.outstandingAmount = outstandingAmount;
	}

	public Double getClaimRatio() {
		return claimRatio;
	}

	public void setClaimRatio(Double claimRatio) {
		this.claimRatio = claimRatio;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Integer getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Long getInsuredNumber() {
		return insuredNumber;
	}

	public void setInsuredNumber(Long insuredNumber) {
		this.insuredNumber = insuredNumber;
	}	
	
}

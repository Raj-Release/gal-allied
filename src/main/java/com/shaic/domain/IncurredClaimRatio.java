package com.shaic.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.shaic.arch.fields.dto.AbstractEntity;

@Entity
@Table(name="IMS_CLS_GMC_CLAIM_RATIO_COLOR")
@NamedQueries({
	@NamedQuery(name="IncurredClaimRatio.findAll", query="SELECT r FROM IncurredClaimRatio r"),
	@NamedQuery(name="IncurredClaimRatio.findByPolicyNumber", query="SELECT r FROM IncurredClaimRatio r where r.policyNumber= :policyNumber"),
})
public class IncurredClaimRatio extends AbstractEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="RATIO_COLOR_KEY")
	private Long key;
	
	@Column(name="POLICY_KEY")
	private Long policyKey;
	
	@Column(name="POLICY_NUMBER")
	private String policyNumber;
	
	@Column(name="INSURED_NUMBER")
	private String insuredNumber;
	
	@Column(name="GROSS_PREMIUM")
	private Long grossPremium;
	
	@Column(name="NO_OF_DAYS")
	private Long noOfDays;
	
	@Column(name="EARNED_PREMIUM")
	private Long earnedPremium;
	
	@Column(name="CLAIM_PAID")
	private Long claimPaid;
	
	@Column(name="CLAIM_OUTSTANDING")
	private Long claimOutstanding;
	
	@Column(name="CLAIM_RATIO")
	private Long claimRatio;
	
	@Column(name="CLAIM_COLOUR")
	private String claimColour;

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

	public Long getGrossPremium() {
		return grossPremium;
	}

	public void setGrossPremium(Long grossPremium) {
		this.grossPremium = grossPremium;
	}

	public Long getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(Long noOfDays) {
		this.noOfDays = noOfDays;
	}

	public Long getEarnedPremium() {
		return earnedPremium;
	}

	public void setEarnedPremium(Long earnedPremium) {
		this.earnedPremium = earnedPremium;
	}

	public Long getClaimPaid() {
		return claimPaid;
	}

	public void setClaimPaid(Long claimPaid) {
		this.claimPaid = claimPaid;
	}

	public Long getClaimOutstanding() {
		return claimOutstanding;
	}

	public void setClaimOutstanding(Long claimOutstanding) {
		this.claimOutstanding = claimOutstanding;
	}

	public Long getClaimRatio() {
		return claimRatio;
	}

	public void setClaimRatio(Long claimRatio) {
		this.claimRatio = claimRatio;
	}

	public String getClaimColour() {
		return claimColour;
	}

	public void setClaimColour(String claimColour) {
		this.claimColour = claimColour;
	}

	public String getInsuredNumber() {
		return insuredNumber;
	}

	public void setInsuredNumber(String insuredNumber) {
		this.insuredNumber = insuredNumber;
	}

}
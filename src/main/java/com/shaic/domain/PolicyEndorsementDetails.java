package com.shaic.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="IMS_CLS_POLICY_ENDORSEMENT")
@NamedQueries( {
	@NamedQuery(name="PolicyEndorsementDetails.findAll", query="SELECT i FROM PolicyEndorsementDetails i"),
	@NamedQuery(name = "PolicyEndorsementDetails.findByPolicyNo",query = "select o from PolicyEndorsementDetails o where o.policy is not null and o.policy.policyNumber like :policyNumber")
})
public class PolicyEndorsementDetails implements Serializable {

	@Id
	@SequenceGenerator(name="IMS_CLS_POLICY_ENDORSEMENT_KEY_GENERATOR", sequenceName = "SEQ_CLS_POLICY_ENDORSEMENT_KEY"  ,allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_POLICY_ENDORSEMENT_KEY_GENERATOR" )
	
	@Column(name= "ENDORSEMENT_KEY") 
	private Long endorsementKey;
	
	@JoinColumn(name= "POLICY_KEY")
	@ManyToOne
	private Policy policy;
	
	@Column(name= "ENDORSEMENT_NUMBER")  
	private String endorsementNumber;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name= "ENDORSEMENT_DATE")  
	private Date endoresementDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name= "EFFECTIVE_FROM_DATE")  
	private Date effectiveFromDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name= "EFFECTIVE_TO_DATE")  
	private Date effectiveToDate;
	
	@Column(name= "ENDORSEMENT_TYPE") 
	private String endorsementType;
	
	@Column(name= "ENDORSEMENT_TEXT") 
	private String endorsementText;
	
	@Column(name= "SUM_INSURED")  
	private Double sumInsured;
	
	@Column(name= "REVISED_SUM_INSURED")  
	private Double revisedSumInsured;
	
	@Column(name= "PREMIUM")
	private Double premium;
	
	@Column(name= "DELETED_FLAG")
	private String deletedFlag;
	
	public Long getEndorsementKey() {
		return endorsementKey;
	}

	public void setEndorsementKey(Long endorsementKey) {
		this.endorsementKey = endorsementKey;
	}

	public Policy getPolicy() {
		return policy;
	}

	public void setPolicy(Policy policy) {
		this.policy = policy;
	}

	public String getEndorsementNumber() {
		return endorsementNumber;
	}

	public void setEndorsementNumber(String endorsementNumber) {
		this.endorsementNumber = endorsementNumber;
	}

	public Date getEndoresementDate() {
		return endoresementDate;
	}

	public void setEndoresementDate(Date endoresementDate) {
		this.endoresementDate = endoresementDate;
	}

	public Date getEffectiveFromDate() {
		return effectiveFromDate;
	}

	public void setEffectiveFromDate(Date effectiveFromDate) {
		this.effectiveFromDate = effectiveFromDate;
	}

	public Date getEffectiveToDate() {
		return effectiveToDate;
	}

	public void setEffectiveToDate(Date effectiveToDate) {
		this.effectiveToDate = effectiveToDate;
	}

	public String getEndorsementType() {
		return endorsementType;
	}

	public void setEndorsementType(String endorsementType) {
		this.endorsementType = endorsementType;
	}

	public String getEndorsementText() {
		return endorsementText;
	}

	public void setEndorsementText(String endorsementText) {
		this.endorsementText = endorsementText;
	}

	public Double getSumInsured() {
		return sumInsured;
	}

	public void setSumInsured(Double sumInsured) {
		this.sumInsured = sumInsured;
	}

	public Double getRevisedSumInsured() {
		return revisedSumInsured;
	}

	public void setRevisedSumInsured(Double revisedSumInsured) {
		this.revisedSumInsured = revisedSumInsured;
	}

	public Double getPremium() {
		return premium;
	}

	public void setPremium(Double premium) {
		this.premium = premium;
	}

	public String getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
	}
	
}

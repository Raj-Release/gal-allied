package com.shaic.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.shaic.arch.fields.dto.AbstractEntity;

@Entity
@Table(name="IMS_CLS_GMC_CONT_BENEFIT")
@NamedQueries( {
	@NamedQuery(name="GmcContinuityBenefit.findByPolicyKeyAndInsuredKey", query="SELECT p FROM GmcContinuityBenefit p where p.insured.key = :insuredKey and p.policy.key = :policyKey and (p.activeFlag is not null and p.activeFlag = 'Y')")
})
public class GmcContinuityBenefit extends AbstractEntity implements Serializable {

	@Id
	@SequenceGenerator(name="IMS_CLS_GMC_CONT_BENEFIT_KEY_GENERATOR", sequenceName = "SEQ_INS_GMC_CON_BEN_ID"  ,allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_GMC_CONT_BENEFIT_KEY_GENERATOR" ) 
	@Column(name="INS_GMC_CON_BEN_ID")	
	private Long key;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="POLICY_KEY")    
	private Policy policy;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="INSURED_KEY")
	private Insured insured;
	
	@Column(name="POLICY_YEAR")  
	private Integer policyYr;
	
	@Column(name="INSURED_NAME")  
	private String insuredName;
	
	@Column(name="POLICY_NUMBER")  
	private String policyNo;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "POLICY_FROM_DATE")
	private Date policyFromDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "POLICY_TO_DATE")
	private Date policyToDate;
	
	@Column(name="WAIVER_30DAYS")  
	private Integer waiver30Days;

	@Column(name="EXCLUSION_1STYEAR")  
	private Integer exclusion1Yr;

	@Column(name="EXCLUSION_2NDYEAR")  
	private Integer exclusion2Yr;
	
	@Column(name="PED_WAIVER")  
	private Integer pedWaiver;
	
	@Column(name="CREATED_BY")  
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE")  
	private Date createdDate;

	@Column(name="MODIFIED_BY")
	private String modifiedBy;
		
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name="ACTIVE_FLAG")  
	private String activeFlag;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "POLICY_INCEPTION_DATE")
	private Date inceptionDate;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Policy getPolicy() {
		return policy;
	}

	public void setPolicy(Policy policy) {
		this.policy = policy;
	}

	public Insured getInsured() {
		return insured;
	}

	public void setInsured(Insured insured) {
		this.insured = insured;
	}

	public Integer getPolicyYr() {
		return policyYr;
	}

	public void setPolicyYr(Integer policyYr) {
		this.policyYr = policyYr;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public Date getPolicyFromDate() {
		return policyFromDate;
	}

	public void setPolicyFromDate(Date policyFromDate) {
		this.policyFromDate = policyFromDate;
	}

	public Date getPolicyToDate() {
		return policyToDate;
	}

	public void setPolicyToDate(Date policyToDate) {
		this.policyToDate = policyToDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	public Integer getWaiver30Days() {
		return waiver30Days;
	}

	public void setWaiver30Days(Integer waiver30Days) {
		this.waiver30Days = waiver30Days;
	}

	public Integer getExclusion1Yr() {
		return exclusion1Yr;
	}

	public void setExclusion1Yr(Integer exclusion1Yr) {
		this.exclusion1Yr = exclusion1Yr;
	}

	public Integer getExclusion2Yr() {
		return exclusion2Yr;
	}

	public void setExclusion2Yr(Integer exclusion2Yr) {
		this.exclusion2Yr = exclusion2Yr;
	}

	public Integer getPedWaiver() {
		return pedWaiver;
	}

	public void setPedWaiver(Integer pedWaiver) {
		this.pedWaiver = pedWaiver;
	}

	public Date getInceptionDate() {
		return inceptionDate;
	}

	public void setInceptionDate(Date inceptionDate) {
		this.inceptionDate = inceptionDate;
	}

	
}

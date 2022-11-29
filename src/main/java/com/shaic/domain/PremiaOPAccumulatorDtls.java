package com.shaic.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="GALAXY_OP_DETAIL")
@NamedQueries({
	@NamedQuery(name ="PremiaOPAccumulatorDtls.findAll", query = "SELECT p FROM PremiaOPAccumulatorDtls p"),
	@NamedQuery(name = "PremiaOPAccumulatorDtls.findByPolicyNo",query = "select p from PremiaOPAccumulatorDtls p where p.policyNumber = :policyNumber"),
})
public class PremiaOPAccumulatorDtls {
	
	@Id
	@Column(name="POLICY_NUMBER")
	private String policyNumber;
	
	@Column(name="PREMIA_LOCK_FLAG")
	private String premiaLockFlag;
	
	@Column(name="POLICY_SYS_ID")
	private String policySysId;
	
	@Column(name="POLICY_TYPE")
	private String policyType;
	
	@Column(name="POLICY_PLAN")
	private String policyPlan;
	
    @Column(name="HEALTH_CARD_NO")
    private String healthCardNo;
    
    @Column(name="INSURED_NUMBER")
    private Long insuredNumber;
    
    @Column(name="INSURED_NAME")
    private String insuredName;
    
    @Column(name="AGE")
    private Long age;
    
    @Column(name="SUM_INSURED")
    private Long sumInsured;
    
    @Column(name="REFERENCE_FLAG")
    private String referenceFlag;
    
    @Column(name="LIMIT_AMT")
    private Double limitAmt;
    
    @Column(name="PROVISION_AMT")
    private Double provisionAmt;
    
    @Column(name="PAID_AMT")
    private Double paidAmt;
    
    @Column(name="AVILABLE_AMT")
    private Double availableAmt;
    
    @Column(name="COVER_CODE")
    private String coverCode;
    
    @Column(name="COVER_DESC")
    private String coverDesc;
    
    @Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE")
	private Date createdDate;
    
    @Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;

	public String getPremiaLockFlag() {
		return premiaLockFlag;
	}

	public void setPremiaLockFlag(String premiaLockFlag) {
		this.premiaLockFlag = premiaLockFlag;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getPolicySysId() {
		return policySysId;
	}

	public void setPolicySysId(String policySysId) {
		this.policySysId = policySysId;
	}

	public String getPolicyType() {
		return policyType;
	}

	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}

	public String getPolicyPlan() {
		return policyPlan;
	}

	public void setPolicyPlan(String policyPlan) {
		this.policyPlan = policyPlan;
	}

	public String getHealthCardNo() {
		return healthCardNo;
	}

	public void setHealthCardNo(String healthCardNo) {
		this.healthCardNo = healthCardNo;
	}

	public Long getInsuredNumber() {
		return insuredNumber;
	}

	public void setInsuredNumber(Long insuredNumber) {
		this.insuredNumber = insuredNumber;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public Long getAge() {
		return age;
	}

	public void setAge(Long age) {
		this.age = age;
	}

	public Long getSumInsured() {
		return sumInsured;
	}

	public void setSumInsured(Long sumInsured) {
		this.sumInsured = sumInsured;
	}

	public String getReferenceFlag() {
		return referenceFlag;
	}

	public void setReferenceFlag(String referenceFlag) {
		this.referenceFlag = referenceFlag;
	}

	public Double getLimitAmt() {
		return limitAmt;
	}

	public void setLimitAmt(Double limitAmt) {
		this.limitAmt = limitAmt;
	}

	public Double getProvisionAmt() {
		return provisionAmt;
	}

	public void setProvisionAmt(Double provisionAmt) {
		this.provisionAmt = provisionAmt;
	}

	public Double getPaidAmt() {
		return paidAmt;
	}

	public void setPaidAmt(Double paidAmt) {
		this.paidAmt = paidAmt;
	}

	public Double getAvailableAmt() {
		return availableAmt;
	}

	public void setAvailableAmt(Double availableAmt) {
		this.availableAmt = availableAmt;
	}

	public String getCoverCode() {
		return coverCode;
	}

	public void setCoverCode(String coverCode) {
		this.coverCode = coverCode;
	}

	public String getCoverDesc() {
		return coverDesc;
	}

	public void setCoverDesc(String coverDesc) {
		this.coverDesc = coverDesc;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

}

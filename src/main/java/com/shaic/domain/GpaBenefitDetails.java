package com.shaic.domain;

import java.io.Serializable;
import java.sql.Timestamp;
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

/**
 * The persistent class for the IMS_CLS_POLICY database table.
 * 
 */
/**
 * @author yosuva.a
 *
 */
@Entity
@Table(name = "IMS_CLS_GPA_BENEFIT_DETAILS")
@NamedQueries({
		@NamedQuery(name = "GpaBenefitDetails.findAll", query = "SELECT o FROM GpaBenefitDetails o"),
		@NamedQuery(name = "GpaBenefitDetails.findByPolicy", query = "SELECT o FROM GpaBenefitDetails o where o.policyKey = :policyKey")
	 })

public class GpaBenefitDetails implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="IMS_GPA_BENEFIT_KEY_GENERATOR", sequenceName = "seq_POLICY_BENEFIT_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_GPA_BENEFIT_KEY_GENERATOR" )
	@Column(name = "POLICY_BENEFIT_KEY")
	private Long key;
	
	@Column(name = "POLICY_KEY")
	private Long policyKey;
	
	@Column(name = "AMOUNTPERUNIT")
	private Double amountPerUnit;
	
	@Column(name = "BENEFITBASEDON")
	private String benefitBasedOn;
	
	@Column(name = "BENEFITCODE")
	private String benefitCode;
	
	@Column(name = "BENEFITDESCRIPTION")
	private String benefitDescription;
	
	@Column(name = "BENEFITLONGDESCRIPTION")
	private String benefitLongDescription;
	
	@Column(name = "CLAIMMAXAMOUNT")
	private Double claimMaxAmount;
	
	@Column(name = "CLAIMPERCENTAGE")
	private Double claimPercentage;
	
	@Column(name = "FLATAMOUNTPERCLAIM")
	private Double flatAmountPerClaim;
	
	@Column(name = "FLATAMOUNTPERPOLICYPERIOD")
	private Double flatAmountPerPolicyPeriod;
	
	@Column(name = "NOOFUNITS")
	private Double numberOfUnits;
	
	@Column(name = "REMARKS")
	private String remarks;
	
	@Column(name = "SIMAXAMOUNT")
	private Double siMaxAmount;
	
	@Column(name = "SIPERCENTAGE")
	private Double siPercentage;
	
	@Column(name = "SRNO")
	private Integer srNumber;
	
	@Column(name = "SUBBENEFITDESCRIPTION")
	private String subBenefitDescription;
	
	@Column(name = "WITHINSI")
	private String withInSi;
	
	@Column(name = "CREATE_DATE")
	private Date createdDate;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name = "MODIFIED_DATE")
	private Timestamp modifiedDate;

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

	public Double getAmountPerUnit() {
		return amountPerUnit;
	}

	public void setAmountPerUnit(Double amountPerUnit) {
		this.amountPerUnit = amountPerUnit;
	}

	public String getBenefitBasedOn() {
		return benefitBasedOn;
	}

	public void setBenefitBasedOn(String benefitBasedOn) {
		this.benefitBasedOn = benefitBasedOn;
	}

	public String getBenefitCode() {
		return benefitCode;
	}

	public void setBenefitCode(String benefitCode) {
		this.benefitCode = benefitCode;
	}

	public String getBenefitDescription() {
		return benefitDescription;
	}

	public void setBenefitDescription(String benefitDescription) {
		this.benefitDescription = benefitDescription;
	}

	public String getBenefitLongDescription() {
		return benefitLongDescription;
	}

	public void setBenefitLongDescription(String benefitLongDescription) {
		this.benefitLongDescription = benefitLongDescription;
	}

	public Double getClaimMaxAmount() {
		return claimMaxAmount;
	}

	public void setClaimMaxAmount(Double claimMaxAmount) {
		this.claimMaxAmount = claimMaxAmount;
	}

	public Double getClaimPercentage() {
		return claimPercentage;
	}

	public void setClaimPercentage(Double claimPercentage) {
		this.claimPercentage = claimPercentage;
	}

	public Double getFlatAmountPerClaim() {
		return flatAmountPerClaim;
	}

	public void setFlatAmountPerClaim(Double flatAmountPerClaim) {
		this.flatAmountPerClaim = flatAmountPerClaim;
	}

	public Double getFlatAmountPerPolicyPeriod() {
		return flatAmountPerPolicyPeriod;
	}

	public void setFlatAmountPerPolicyPeriod(Double flatAmountPerPolicyPeriod) {
		this.flatAmountPerPolicyPeriod = flatAmountPerPolicyPeriod;
	}

	public Double getNumberOfUnits() {
		return numberOfUnits;
	}

	public void setNumberOfUnits(Double numberOfUnits) {
		this.numberOfUnits = numberOfUnits;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Double getSiMaxAmount() {
		return siMaxAmount;
	}

	public void setSiMaxAmount(Double siMaxAmount) {
		this.siMaxAmount = siMaxAmount;
	}

	public Double getSiPercentage() {
		return siPercentage;
	}

	public void setSiPercentage(Double siPercentage) {
		this.siPercentage = siPercentage;
	}

	public Integer getSrNumber() {
		return srNumber;
	}

	public void setSrNumber(Integer srNumber) {
		this.srNumber = srNumber;
	}

	public String getSubBenefitDescription() {
		return subBenefitDescription;
	}

	public void setSubBenefitDescription(String subBenefitDescription) {
		this.subBenefitDescription = subBenefitDescription;
	}

	public String getWithInSi() {
		return withInSi;
	}

	public void setWithInSi(String withInSi) {
		this.withInSi = withInSi;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	}
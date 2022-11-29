package com.shaic.domain.outpatient;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.shaic.arch.fields.dto.AbstractEntity;

@Entity
@Table(name="IMS_CLS_BENEFITS_ACCUMULATOR")
@NamedQueries({
	@NamedQuery(name="OPBenefitAccumulator.findAll", query="SELECT r FROM OPBenefitAccumulator r"),
	@NamedQuery(name ="OPBenefitAccumulator.findByKey",query="SELECT r FROM OPBenefitAccumulator r WHERE r.key = :key")
})
public class OPBenefitAccumulator extends AbstractEntity{
	
	@Id
	@SequenceGenerator(name="IMS_CLS_OP_BENEFITS_ACCUMULATOR_KEY_GENERATOR", sequenceName = "SEQ_BENEFITS_ACCUMULATOR_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_OP_BENEFITS_ACCUMULATOR_KEY_GENERATOR" ) 
	@Column(name="BENEFITS_ACCUMULATOR_KEY")
	private Long key;
	
	@Column(name="POLICY_NUMBER")
	private String policyNumber;
	
	@Column(name="INSURED_NUMBER")
	private Long insuredNumber;
	
	@Column(name="INSURED_NAME")
	private String insuredName;
	
	@Column(name="SUM_INSURED")
	private Double sumInsured;
	
	@Column(name="CLAIM_YEAR")
	private Long claimYear;
	
	@Column(name="REFERENCE_FLAG")
	private String referenceflag;
	
	@Column(name="LIMIT_AMT")
	private Double limitAmt;
	
	@Column(name="CARRY_FORWARD_AMT")
	private Double carryForwardAmt;
	
	@Column(name="APPROVED_AMT")
	private Double approvedAmt;
	
	@Column(name="AVILABLE_AMT")
	private String availableAmt;
	
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
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

	public Double getSumInsured() {
		return sumInsured;
	}

	public void setSumInsured(Double sumInsured) {
		this.sumInsured = sumInsured;
	}

	public Long getClaimYear() {
		return claimYear;
	}

	public void setClaimYear(Long claimYear) {
		this.claimYear = claimYear;
	}

	public String getReferenceflag() {
		return referenceflag;
	}

	public void setReferenceflag(String referenceflag) {
		this.referenceflag = referenceflag;
	}

	public Double getLimitAmt() {
		return limitAmt;
	}

	public void setLimitAmt(Double limitAmt) {
		this.limitAmt = limitAmt;
	}

	public Double getCarryForwardAmt() {
		return carryForwardAmt;
	}

	public void setCarryForwardAmt(Double carryForwardAmt) {
		this.carryForwardAmt = carryForwardAmt;
	}

	public Double getApprovedAmt() {
		return approvedAmt;
	}

	public void setApprovedAmt(Double approvedAmt) {
		this.approvedAmt = approvedAmt;
	}

	public String getAvailableAmt() {
		return availableAmt;
	}

	public void setAvailableAmt(String availableAmt) {
		this.availableAmt = availableAmt;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

}

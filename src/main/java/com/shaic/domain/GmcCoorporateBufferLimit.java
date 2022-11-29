package com.shaic.domain;

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
@Table(name = "MAS_GMC_CORP_BUFFER_LIMIT")
@NamedQueries({
		@NamedQuery(name = "GmcCoorporateBufferLimit.findBasedOnSIFromTo", query = "SELECT i FROM GmcCoorporateBufferLimit i where i.policyKey = :policyKey and i.sumInsuredFrom = :sumInsuredFrom and i.sumInsuredTo = :sumInsuredTo"),
		@NamedQuery(name = "GmcCoorporateBufferLimit.findBasedOnSI", query = "SELECT i FROM GmcCoorporateBufferLimit i WHERE i.policyKey = :policyKey AND (:suminsured) BETWEEN i.sumInsuredFrom AND i.sumInsuredTo")

})

public class GmcCoorporateBufferLimit {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="MAS_GMC_CORP_BUFFER_LIMIT_KEY_GENERATOR", sequenceName = "SEQ_BUFFER_LMT_KEY"  ,allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MAS_GMC_CORP_BUFFER_LIMIT_KEY_GENERATOR" ) 

	@Column(name = "BUFFER_LMT_KEY")
	private Long key;

	@Column(name = "POLICY_KEY")
	private Long policyKey;

	@Column(name="BUFFER_TYPE")
	private String bufferType;

	@Column(name="FAMILY_SI_YN")
	private String familySiType;

	@Column(name = "LIMIT_AMOUNT")
	private Double limitAmount;

	@Column(name = "LIMIT_APPL")
	private String limitApplicable;

	@Column(name="NO_SI_YN")
	private String noSiType;

	@Column(name = "SI_FROM")
	private Double sumInsuredFrom;

	@Column(name = "SI_TO")
	private Double sumInsuredTo;
	
	@Column(name = "TOT_NO_SI")
	private Double totalNoSi;

	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;

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

	public String getBufferType() {
		return bufferType;
	}

	public void setBufferType(String bufferType) {
		this.bufferType = bufferType;
	}

	public String getFamilySiType() {
		return familySiType;
	}

	public void setFamilySiType(String familySiType) {
		this.familySiType = familySiType;
	}

	public Double getLimitAmount() {
		return limitAmount;
	}

	public void setLimitAmount(Double limitAmount) {
		this.limitAmount = limitAmount;
	}

	public String getLimitApplicable() {
		return limitApplicable;
	}

	public void setLimitApplicable(String limitApplicable) {
		this.limitApplicable = limitApplicable;
	}

	public String getNoSiType() {
		return noSiType;
	}

	public void setNoSiType(String noSiType) {
		this.noSiType = noSiType;
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

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
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

	public Double getTotalNoSi() {
		return totalNoSi;
	}

	public void setTotalNoSi(Double totalNoSi) {
		this.totalNoSi = totalNoSi;
	}

}

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

import com.shaic.arch.fields.dto.AbstractEntity;

@Entity
@Table(name = "IMS_CLS_CORPORATE_BUFFER")
@NamedQueries({
		@NamedQuery(name = "CoorporateBuffer.findbyinsuredNumber", query = "SELECT i FROM CoorporateBuffer i where i.insuredNumber = :insuredNumber order by i.key desc"),
		@NamedQuery(name = "CoorporateBuffer.findbyinsuredNumberBuffertype", query = "SELECT i FROM CoorporateBuffer i where i.insuredNumber = :insuredNumber and i.bufferType = :bufferType order by i.key desc"),
		@NamedQuery(name = "CoorporateBuffer.findbyinsuredMainMember", query = "SELECT i FROM CoorporateBuffer i where i.mainMember = :mainMember and i.bufferType = :bufferType order by i.key desc"),
		@NamedQuery(name = "CoorporateBuffer.findbyinsuredMainMemberWithoutBuffer", query = "SELECT i FROM CoorporateBuffer i where i.mainMember = :mainMember order by i.key desc")
})
public class CoorporateBuffer extends AbstractEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7887530375537600951L;

	@Id
	@SequenceGenerator(name="MAS_CORP_BUFFER_KEY_GENERATOR", sequenceName = "SEQ_CORP_BUFFER_KEY"  ,allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MAS_CORP_BUFFER_KEY_GENERATOR" ) 

	@Column(name = "BUFFER_KEY")
	private Long key;

	@Column(name="BUFFER_TYPE")
	private String bufferType;
	
	@Column(name = "POLICY_NUMBER")
	private String policyNumber;

	@Column(name="MAIN_MEMBER")
	private Long mainMember;
	
	@Column(name="INSURED_NUMBER")
	private Long insuredNumber;

	@Column(name = "ALLOCATED_AMOUNT")
	private Double allocatedAmount;

	@Column(name = "LIMIT_APPLICABLE")
	private String limitApplicable;

	@Column(name="INDIVIDUAL_SI")
	private Long individualSi;

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
	
	@Column(name = "BI_DATE")
	private Date biDate;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getBufferType() {
		return bufferType;
	}

	public void setBufferType(String bufferType) {
		this.bufferType = bufferType;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public Long getMainMember() {
		return mainMember;
	}

	public void setMainMember(Long mainMember) {
		this.mainMember = mainMember;
	}

	public Long getInsuredNumber() {
		return insuredNumber;
	}

	public void setInsuredNumber(Long insuredNumber) {
		this.insuredNumber = insuredNumber;
	}

	public Double getAllocatedAmount() {
		return allocatedAmount;
	}

	public void setAllocatedAmount(Double allocatedAmount) {
		this.allocatedAmount = allocatedAmount;
	}

	public String getLimitApplicable() {
		return limitApplicable;
	}

	public void setLimitApplicable(String limitApplicable) {
		this.limitApplicable = limitApplicable;
	}

	public Long getIndividualSi() {
		return individualSi;
	}

	public void setIndividualSi(Long individualSi) {
		this.individualSi = individualSi;
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

	public Date getBiDate() {
		return biDate;
	}

	public void setBiDate(Date biDate) {
		this.biDate = biDate;
	}
	
}

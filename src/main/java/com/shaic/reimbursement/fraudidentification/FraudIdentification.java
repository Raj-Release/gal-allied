package com.shaic.reimbursement.fraudidentification;


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
@Table(name="MAS_FRAUD_IDENTIFICATION")
@NamedQueries({
@NamedQuery(name="FraudIdentification.findByParameterType",query = "SELECT o FROM FraudIdentification o where o.valueType = :valueType"),
@NamedQuery(name="FraudIdentification.findByParameterValue",query = "SELECT o FROM FraudIdentification o where o.value = :value and o.valueType = :valueType"),
})

public class FraudIdentification implements Serializable{
	@Id
	@SequenceGenerator(name="FRAUD_IDENTIFICATION_KEY_GENERATOR", sequenceName = "SEQ_FRAUD_IDENTITY_ID", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FRAUD_IDENTIFICATION_KEY_GENERATOR" )
	@Column(name="FRAUD_IDENTITY_KEY")
	private Long key;
	
	@Column(name="VALUE_TYPE")
	private String valueType;
	
	@Column(name="VALUE")
	private String value;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="MODIFIED_BY")
	private String modifyBy;
	
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name="ACTIVE_STATUS")
	private String activeStatus;
	
	@Column(name="To_Email")
	private String toEmail;
	
	@Column(name="CC_Email")
	private String ccEmail;
	
	@Column(name="USER_Remark")
	private String userRemark;

	@Column(name="effective_from_date")
	private Date effectiveFromDate;
	
	@Column(name="effective_to_date")
	private Date effectiveToDate;
	
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
	
	public String getToEmail() {
		return toEmail;
	}

	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}

	public String getCcEmail() {
		return ccEmail;
	}

	public void setCcEmail(String ccEmail) {
		this.ccEmail = ccEmail;
	}

	public String getUserRemark() {
		return userRemark;
	}

	public void setUserRemark(String userRemark) {
		this.userRemark = userRemark;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getValueType() {
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}


	public String getValue() {
		return value;
	}

	public void setValue(String valueStatus) {
		this.value = valueStatus;
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

	public String getModifyBy() {
		return modifyBy;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
}

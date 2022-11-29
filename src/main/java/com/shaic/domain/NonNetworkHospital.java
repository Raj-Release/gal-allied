package com.shaic.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the MAS_NON_NETWORK_HOSPITAL_T database table.
 * 
 */
@Entity
@Table(name="MAS_NON_NETWORK_HOSPITAL")
@NamedQuery(name="NonNetworkHospital.findAll", query="SELECT m FROM NonNetworkHospital m")
public class NonNetworkHospital implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="\"KEY\"")
	private BigDecimal key;
	
	@Column(name="ACTIVE_STATUS")
	private BigDecimal activeStatus;

	@Column(name="ACTIVE_STATUS_DATE")
	private Timestamp activeStatusDate;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;

	@Column(name="\"VALUE\"")
	private String value;

	@Column(name="\"VERSION\"")
	private BigDecimal version;

	public NonNetworkHospital() {
	}

	public BigDecimal getActiveStatus() {
		return this.activeStatus;
	}

	public void setActiveStatus(BigDecimal activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Timestamp getActiveStatusDate() {
		return this.activeStatusDate;
	}

	public void setActiveStatusDate(Timestamp activeStatusDate) {
		this.activeStatusDate = activeStatusDate;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public BigDecimal getKey() {
		return this.key;
	}

	public void setKey(BigDecimal key) {
		this.key = key;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Timestamp getModifiedDate() {
		return this.modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public BigDecimal getVersion() {
		return this.version;
	}

	public void setVersion(BigDecimal version) {
		this.version = version;
	}

}
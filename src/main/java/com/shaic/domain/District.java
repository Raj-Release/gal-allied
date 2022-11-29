//package com.shaic.domain;
//
//import java.io.Serializable;
//
//import javax.persistence.*;
//
//import java.sql.Timestamp;
//import java.math.BigDecimal;
//
//
///**
// * The persistent class for the MAS_DISTRICT_T database table.
// * 
// */
//@Entity
//@Table(name="MAS_DISTRICT")
//@NamedQuery(name="District.findAll", query="SELECT m FROM District m")
//public class District implements Serializable {
//	private static final long serialVersionUID = 1L;
//
//	@Id
//	@Column(name="\"KEY\"")
//	private BigDecimal key;
//
//	@Column(name="ACTIVE_STATUS")
//	private BigDecimal activeStatus;
//
//	@Column(name="ACTIVE_STATUS_DATE")
//	private Timestamp activeStatusDate;
//
//	@Column(name="CREATED_BY")
//	private String createdBy;
//
//	@Column(name="CREATED_DATE")
//	private Timestamp createdDate;
//
//	@Column(name="FK_COUNTRY_KEY")
//	private BigDecimal fkCountryKey;
//
//	@Column(name="FK_STATE_KEY")
//	private BigDecimal fkStateKey;
//
//	@Column(name="MODIFIED_BY")
//	private String modifiedBy;
//
//	@Column(name="MODIFIED_DATE")
//	private BigDecimal modifiedDate;
//
//	@Column(name="\"VALUE\"")
//	private String value;
//
//	@Column(name="\"VERSION\"")
//	private BigDecimal version;
//
//	public District() {
//	}
//
//	public BigDecimal getActiveStatus() {
//		return this.activeStatus;
//	}
//
//	public void setActiveStatus(BigDecimal activeStatus) {
//		this.activeStatus = activeStatus;
//	}
//
//	public Timestamp getActiveStatusDate() {
//		return this.activeStatusDate;
//	}
//
//	public void setActiveStatusDate(Timestamp activeStatusDate) {
//		this.activeStatusDate = activeStatusDate;
//	}
//
//	public String getCreatedBy() {
//		return this.createdBy;
//	}
//
//	public void setCreatedBy(String createdBy) {
//		this.createdBy = createdBy;
//	}
//
//	public Timestamp getCreatedDate() {
//		return this.createdDate;
//	}
//
//	public void setCreatedDate(Timestamp createdDate) {
//		this.createdDate = createdDate;
//	}
//
//	public BigDecimal getFkCountryKey() {
//		return this.fkCountryKey;
//	}
//
//	public void setFkCountryKey(BigDecimal fkCountryKey) {
//		this.fkCountryKey = fkCountryKey;
//	}
//
//	public BigDecimal getFkStateKey() {
//		return this.fkStateKey;
//	}
//
//	public void setFkStateKey(BigDecimal fkStateKey) {
//		this.fkStateKey = fkStateKey;
//	}
//
//	public BigDecimal getKey() {
//		return this.key;
//	}
//
//	public void setKey(BigDecimal key) {
//		this.key = key;
//	}
//
//	public String getModifiedBy() {
//		return this.modifiedBy;
//	}
//
//	public void setModifiedBy(String modifiedBy) {
//		this.modifiedBy = modifiedBy;
//	}
//
//	public BigDecimal getModifiedDate() {
//		return this.modifiedDate;
//	}
//
//	public void setModifiedDate(BigDecimal modifiedDate) {
//		this.modifiedDate = modifiedDate;
//	}
//
//	public String getValue() {
//		return this.value;
//	}
//
//	public void setValue(String value) {
//		this.value = value;
//	}
//
//	public BigDecimal getVersion() {
//		return this.version;
//	}
//
//	public void setVersion(BigDecimal version) {
//		this.version = version;
//	}
//
//}
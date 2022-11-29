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
// * The persistent class for the MAS_PINCODE_T database table.
// * 
// */
//@Entity
//@Table(name="MAS_PINCODE")
//@NamedQueries({
//	@NamedQuery(name="Pincode.findAll", query="SELECT m FROM Pincode m"),
//	@NamedQuery(name="Pincode.findByCode", query="SELECT m FROM Pincode m where m.value = :pinCode"),
//	@NamedQuery(name="Pincode.findByCodeAndStateAndCity", query="SELECT m FROM Pincode m where m.value = :pinCode")
//})
//public class Pincode implements Serializable {
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
//	@Column(name="CPU_ID")
//	private BigDecimal cpuId;
//
//	@Column(name="CREATED_BY")
//	private String createdBy;
//
//	@Column(name="CREATED_DATE")
//	private Timestamp createdDate;
//
//	@Column(name="FK_CITY_TOWN_VILLAGE_KEY")
//	private BigDecimal fkCityTownVillageKey;
//
//	@Column(name="FK_COUNTRY_KEY")
//	private BigDecimal fkCountryKey;
//
//	@Column(name="FK_DISTRICT_KEY")
//	private BigDecimal fkDistrictKey;
//
//	@Column(name="FK_LOCALITY_KEY")
//	private BigDecimal fkLocalityKey;
//
//	@Column(name="FK_STATE_KEY")
//	private BigDecimal fkStateKey;
//
//	private String latitude;
//
//	private String longitude;
//
//	@Column(name="MODIFIED_BY")
//	private String modifiedBy;
//
//	@Column(name="MODIFIED_DATE")
//	private BigDecimal modifiedDate;
//
//	@Column(name="URBAN_RURAL")
//	private String urbanRural;
//
//	@Column(name="\"VALUE\"")
//	private String value;
//
//	@Column(name="\"VERSION\"")
//	private BigDecimal version;
//
//	public Pincode() {
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
//	public BigDecimal getCpuCode() {
//		return this.cpuId;
//	}
//
//	public void setCpuCode(BigDecimal cpuCode) {
//		this.cpuId = cpuCode;
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
//	public BigDecimal getFkCityTownVillageKey() {
//		return this.fkCityTownVillageKey;
//	}
//
//	public void setFkCityTownVillageKey(BigDecimal fkCityTownVillageKey) {
//		this.fkCityTownVillageKey = fkCityTownVillageKey;
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
//	public BigDecimal getFkDistrictKey() {
//		return this.fkDistrictKey;
//	}
//
//	public void setFkDistrictKey(BigDecimal fkDistrictKey) {
//		this.fkDistrictKey = fkDistrictKey;
//	}
//
//	public BigDecimal getFkLocalityKey() {
//		return this.fkLocalityKey;
//	}
//
//	public void setFkLocalityKey(BigDecimal fkLocalityKey) {
//		this.fkLocalityKey = fkLocalityKey;
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
//	public String getLatitude() {
//		return this.latitude;
//	}
//
//	public void setLatitude(String latitude) {
//		this.latitude = latitude;
//	}
//
//	public String getLongitude() {
//		return this.longitude;
//	}
//
//	public void setLongitude(String longitude) {
//		this.longitude = longitude;
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
//	public String getUrbanRural() {
//		return this.urbanRural;
//	}
//
//	public void setUrbanRural(String urbanRural) {
//		this.urbanRural = urbanRural;
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
//package com.shaic.domain;
//
//import java.io.Serializable;
//
//import javax.persistence.*;
//
//import java.sql.Timestamp;
//
//
///**
// * The persistent class for the MAS_LOCALITY_T database table.
// * 
// */
//@Entity
//@Table(name="MAS_LOCALITY")
//@NamedQueries({
//	@NamedQuery(name="Locality.findAll", query="SELECT m FROM Locality m"),
//	@NamedQuery(name="Locality.searchCityByNameAndCity", query="select o from Locality o where o.fkCityTownVillageKey = :cityKey and Lower(o.value) like :localityQuery ORDER BY o.value"),
//	@NamedQuery(name="Locality.searchAreaByCity",query="select o from Locality o where o.fkCityTownVillageKey = :cityKey"),
//	@NamedQuery(name="Locality.searchByKey",query="select o from Locality o where o.key = :primaryKey")
//})
//public class Locality implements Serializable {
//	private static final long serialVersionUID = 1L;
//
//	@Id
//	@Column(name="\"KEY\"")
//	private Long key;
//
//	@Column(name="ACTIVE_STATUS")
//	private Long activeStatus;
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
//	@Column(name="FK_CITY_TOWN_VILLAGE_KEY")
//	private Long fkCityTownVillageKey;
//
//	@Column(name="FK_COUNTRY_KEY")
//	private Long fkCountryKey;
//
//	@Column(name="FK_DISTRICT_KEY")
//	private Long fkDistrictKey;
//
//	@Column(name="FK_STATE_KEY")
//	private Long fkStateKey;
//
//	@Column(name="MODIFIED_BY")
//	private String modifiedBy;
//
//	@Column(name="MODIFIED_DATE")
//	private Long modifiedDate;
//
//	@Column(name="\"VALUE\"")
//	private String value;
//
//	@Column(name="\"VERSION\"")
//	private Long version;
//
//	public Locality() {
//	}
//
//	public Long getActiveStatus() {
//		return this.activeStatus;
//	}
//
//	public void setActiveStatus(Long activeStatus) {
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
//	public Long getFkCityTownVillageKey() {
//		return this.fkCityTownVillageKey;
//	}
//
//	public void setFkCityTownVillageKey(Long fkCityTownVillageKey) {
//		this.fkCityTownVillageKey = fkCityTownVillageKey;
//	}
//
//	public Long getFkCountryKey() {
//		return this.fkCountryKey;
//	}
//
//	public void setFkCountryKey(Long fkCountryKey) {
//		this.fkCountryKey = fkCountryKey;
//	}
//
//	public Long getFkDistrictKey() {
//		return this.fkDistrictKey;
//	}
//
//	public void setFkDistrictKey(Long fkDistrictKey) {
//		this.fkDistrictKey = fkDistrictKey;
//	}
//
//	public Long getFkStateKey() {
//		return this.fkStateKey;
//	}
//
//	public void setFkStateKey(Long fkStateKey) {
//		this.fkStateKey = fkStateKey;
//	}
//
//	public Long getKey() {
//		return this.key;
//	}
//
//	public void setKey(Long key) {
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
//	public Long getModifiedDate() {
//		return this.modifiedDate;
//	}
//
//	public void setModifiedDate(Long modifiedDate) {
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
//	public Long getVersion() {
//		return this.version;
//	}
//
//	public void setVersion(Long version) {
//		this.version = version;
//	}
//
//}
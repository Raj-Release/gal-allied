package com.shaic.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the MAS_CITY_TOWN_VILLAGE_T database table.
 * 
 */
@Entity
@Table(name="MAS_CITY_TOWN_VILLAGE")
@NamedQueries( {
@NamedQuery(name="CityTownVillage.findAll", query="SELECT m FROM CityTownVillage m  ORDER BY m.value"),
@NamedQuery(name = "CityTownVillage.findByStateKey",
query = "select o from CityTownVillage o where o.fkStateKey = :parentKey ORDER BY o.value"),
@NamedQuery(name = "CityTownVillage.findByKey", query = "SELECT m FROM CityTownVillage m where m.key = :cityID"),
@NamedQuery(name = "CityTownVillage.findByCity", query = "SELECT m FROM CityTownVillage m where Lower(m.value) like :cityName"),
@NamedQuery(name = "CityTownVillage.searchCityByNameAndState", query = "select o from CityTownVillage o where o.fkStateKey = :sateKey and Lower(o.value) like :cityQuery ORDER BY o.value")
})
public class CityTownVillage implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="\"CITY_KEY\"")
	private Long key;
	
//	@Column(name="ACTIVE_STATUS")
//	private BigDecimal activeStatus;

//	@Column(name="ACTIVE_STATUS_DATE")
//	private Timestamp activeStatusDate;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

//	@Column(name="COUNTRY_KEY")
//	private BigDecimal fkCountryKey;

//	@Column(name="FK_DISTRICT_KEY")
//	private BigDecimal fkDistrictKey;
	
	@Column(name = "STATE_CODE")
	private String stateCode;

	@Column(name="STATE_KEY")
	private Long fkStateKey;

	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Column(name="MODIFIED_DATE")
	private BigDecimal modifiedDate;

	@Column(name="\"CITY_NAME\"")
	private String value;

//	@Column(name="\"VERSION\"")
//	private BigDecimal version;

	public CityTownVillage() {
	}

//	public BigDecimal getActiveStatus() {
//		return this.activeStatus;
//	}
//
//	public void setActiveStatus(BigDecimal activeStatus) {
//		this.activeStatus = activeStatus;
//	}

//	public Timestamp getActiveStatusDate() {
//		return this.activeStatusDate;
//	}
//
//	public void setActiveStatusDate(Timestamp activeStatusDate) {
//		this.activeStatusDate = activeStatusDate;
//	}

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

//	public BigDecimal getFkCountryKey() {
//		return this.fkCountryKey;
//	}
//
//	public void setFkCountryKey(BigDecimal fkCountryKey) {
//		this.fkCountryKey = fkCountryKey;
//	}

//	public BigDecimal getFkDistrictKey() {
//		return this.fkDistrictKey;
//	}
//
//	public void setFkDistrictKey(BigDecimal fkDistrictKey) {
//		this.fkDistrictKey = fkDistrictKey;
//	}

	public Long getFkStateKey() {
		return this.fkStateKey;
	}

	public void setFkStateKey(Long fkStateKey) {
		this.fkStateKey = fkStateKey;
	}

	public Long getKey() {
		return this.key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public BigDecimal getModifiedDate() {
		return this.modifiedDate;
	}

	public void setModifiedDate(BigDecimal modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

//	public BigDecimal getVersion() {
//		return this.version;
//	}
//
//	public void setVersion(BigDecimal version) {
//		this.version = version;
//	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}
	
	@Override
    public boolean equals(Object obj) {
        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        } else if (getKey() == null) {
            return obj == this;
        } else {
            return getKey().equals(((CityTownVillage) obj).getKey());
        }
    }

    @Override
    public int hashCode() {
        if (key != null) {
            return key.hashCode();
        } else {
            return super.hashCode();
        }
    }

}
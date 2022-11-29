/**
 * 
 */
package com.shaic.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author ntv.vijayar
 *
 */
@Entity
@Table(name="MAS_PINCODE")
@NamedQueries({
	@NamedQuery(name="MASPincode.findByPinCode", query="SELECT m FROM MASPincode m  where m.pinCodeKey = :primaryKey"),
})
public class MASPincode implements Serializable {
	
	@Id
	@Column(name="PINCODE_KEY")
	private Long pinCodeKey;
	
	
	@Column(name = "VALUE")
	private String value;
	
	
	@Column(name = "CITY_KEY")
	private Long cityKey;
	
	@Column(name = "CITY_NAME")
	private String cityName;
	
	@Column(name = "STATE_KEY")
	private Long stateKey;
	
	@Column(name = "COUNTRY_KEY")
	private Long countryKey;
	
	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_DATE")
	private Timestamp modifiedDate;

	public Long getPinCodeKey() {
		return pinCodeKey;
	}

	public void setPinCodeKey(Long pinCodeKey) {
		this.pinCodeKey = pinCodeKey;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Long getCityKey() {
		return cityKey;
	}

	public void setCityKey(Long cityKey) {
		this.cityKey = cityKey;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Long getStateKey() {
		return stateKey;
	}

	public void setStateKey(Long stateKey) {
		this.stateKey = stateKey;
	}

	public Long getCountryKey() {
		return countryKey;
	}

	public void setCountryKey(Long countryKey) {
		this.countryKey = countryKey;
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

	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	

}

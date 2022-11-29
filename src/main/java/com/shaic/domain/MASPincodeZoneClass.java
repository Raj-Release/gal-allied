/**
 * 
 */
package com.shaic.domain;

import java.io.Serializable;
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
@Table(name="MAS_PINCODE_ZONE_CLASS")
@NamedQueries({
	@NamedQuery(name="MASPincodeZoneClass.findByPinCode", query="SELECT m FROM MASPincodeZoneClass m  where m.pinCode = :pincode"),
	@NamedQuery(name="MASPincodeZoneClass.findDistinctZone", query="SELECT distinct m.zone FROM MASPincodeZoneClass m "),
	@NamedQuery(name="MASPincodeZoneClass.findDistinctCityClass", query="SELECT distinct m.cityClass FROM MASPincodeZoneClass m ")
})
public class MASPincodeZoneClass implements Serializable {
	
	@Id
	@Column(name="PINCODE")
	private Long pinCode;
	
	@Column(name = "ZONE")
	private String zone;
	
	@Column(name = "CITY_CLASS")
	private String cityClass;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;

	public Long getPinCode() {
		return pinCode;
	}

	public void setPinCode(Long pinCode) {
		this.pinCode = pinCode;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getCityClass() {
		return cityClass;
	}

	public void setCityClass(String cityClass) {
		this.cityClass = cityClass;
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
	

}

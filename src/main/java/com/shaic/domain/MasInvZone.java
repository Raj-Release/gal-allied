package com.shaic.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
/**
 * The persistent class for the MAS_COUNTRY_T database table.
 * 
 */
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
@Entity
@Table(name="MAS_INV_ZONE")
@NamedQueries({
	@NamedQuery(name="MasInvZone.findAll", query="SELECT m FROM MasInvZone m where m.activeStatus is not null and m.activeStatus = '1'"),
	@NamedQuery(name="MasInvZone.findByZoneName",query = "SELECT o FROM MasInvZone o where o.zoneName = :zoneName"),
	@NamedQuery(name="MasInvZone.findByKey",query = "SELECT o FROM MasInvZone o where o.key = :key"),
})
public class MasInvZone implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7217543402237269703L;

	@Id
	@Column(name="\"ZONE_KEY\"")
	private Long key;

	@Column(name="ZONE_NAME")
	private String zoneName;

	@Column(name="ACTIVE_STATUS")
	private String activeStatus;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATE")
	private Date CreatedDate;
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getZoneName() {
		return zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return CreatedDate;
	}

	public void setCreatedDate(Date createdDate) {
		CreatedDate = createdDate;
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
	
}

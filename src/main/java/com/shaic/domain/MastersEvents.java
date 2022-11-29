package com.shaic.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Entity
@Table(name="MAS_OMP_EVENTS")
@NamedQueries( {
@NamedQuery(name="MastersEvents.findAll", query="SELECT m FROM MastersEvents m"),
@NamedQuery(name="MastersEvents.findByKey", query="SELECT m FROM MastersEvents m WHERE m.key = :primaryKey"),
@NamedQuery(name="MastersEvents.findByproduct", query="SELECT m FROM MastersEvents m WHERE m.productKey = :productKey"),
@NamedQuery(name="MastersEvents.findByEventDesc", query="SELECT m FROM MastersEvents m WHERE m.eventCode = :eventCode and m.activeStatus='Y'"),
})
public class MastersEvents  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 300714628843860430L;

	@Id
	@Column(name="OMP_EVENTS_KEY")
	private Long key;
	
	@Column(name="OMP_EVENT_CODE")
	private String eventCode;
	
	@Column(name="OMP_EVENT_TYPE")
	private String eventType;
	
	@Column(name="OMP_EVENT_DESCRIPTION")
	private String eventDescription;
	
	@Column(name="OMP_CREATED_BY")
	private String createdBy;

	@Column(name="OMP_CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name="OMP_ACTIVE_STATUS")
	private String activeStatus;

	@Column(name="OMP_MODIFIED_BY")
	private String modifiedBy;

	@Column(name="OMP_MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column(name="PRODUCT_ID")
	private Long productKey;
	
	@Column(name="PROCESS_TYPE")
	private String processType;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
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

	public String getProcessType() {
		return processType;
	}

	public void setProcessType(String processType) {
		this.processType = processType;
	}
	
}

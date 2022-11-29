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
 * The persistent class for the MAS_STATE_T database table.
 * 
 */
@Entity
@Table(name="MAS_STATE")
@NamedQueries({
	@NamedQuery(name="State.findAll", query="SELECT m FROM State m  where m.activeStatus is not null and m.activeStatus = 1 ORDER BY m.value"),
	@NamedQuery(name="State.findByName", query="SELECT m FROM State m where  Lower(m.value) like :stateName ORDER BY m.value"),
	@NamedQuery(name="State.findBySpecificList", query="SELECT m FROM State m  where m.key in(:stateKeyList)"),
})


public class State implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="\"STATE_KEY\"")
	private Long key;
	
	@Column(name="ACTIVE_STATUS")
	private BigDecimal activeStatus;

//	@Column(name="ACTIVE_STATUS_DATE")
//	private Timestamp activeStatusDate;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

//	@Column(name="COUNTRY_KEY")
//	private BigDecimal fkCountryKey;

	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Column(name="MODIFIED_DATE")
	private BigDecimal modifiedDate;

	@Column(name="\"VALUE\"")
	private String value;

//	@Column(name="\"VERSION\"")
//	private BigDecimal version;

	public State() {
	}

	public BigDecimal getActiveStatus() {
		return this.activeStatus;
	}

	public void setActiveStatus(BigDecimal activeStatus) {
		this.activeStatus = activeStatus;
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

//	public BigDecimal getFkCountryKey() {
//		return this.fkCountryKey;
//	}
//
//	public void setFkCountryKey(BigDecimal fkCountryKey) {
//		this.fkCountryKey = fkCountryKey;
//	}

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

}
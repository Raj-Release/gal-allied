package com.shaic.domain.preauth;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.shaic.arch.fields.dto.AbstractEntity;

/**
 * The persistent class for the MAS_PRE_AUTH_STATUS_T database table.
 * 
 */
@Entity
@Table(name="MAS_PRE_AUTH_STATUS")
@NamedQuery(name="PreauthStatus.findAll", query="SELECT m FROM PreauthStatus m")
public class PreauthStatus extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="IMS_CLS_PRE_AUTH_STATUS_KEY_GENERATOR", sequenceName = "SEQ_PRE_AUTH_STATUS_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_PRE_AUTH_STATUS_KEY_GENERATOR" ) 
	@Column(name="KEY")
	private Long key;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;

	@Column(name="ACTIVE_STATUS_DATE")
	private Timestamp activeStatusDate;

	private Long code;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;

	@Column(name="PRE_AUTH_STATUS")
	private String preAuthStatus;

	@Column(name="PRE_AUTH_TYPE")
	private String preAuthType;

	@Column(name="VERSION")
	private Long version;

	public PreauthStatus() {
	}

	public Long getActiveStatus() {
		return this.activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Timestamp getActiveStatusDate() {
		return this.activeStatusDate;
	}

	public void setActiveStatusDate(Timestamp activeStatusDate) {
		this.activeStatusDate = activeStatusDate;
	}

	public Long getCode() {
		return this.code;
	}

	public void setCode(Long code) {
		this.code = code;
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

	public Long getKey() {
		return this.key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

//	public String getModifiedBy() {
//		return this.modifiedBy;
//	}

//	public void setModifiedBy(String modifiedBy) {
//		this.modifiedBy = modifiedBy;
//	}

//	public Timestamp getModifiedDate() {
//		return this.modifiedDate;
//	}

//	public void setModifiedDate(Timestamp modifiedDate) {
//		this.modifiedDate = modifiedDate;
//	}

	public String getPreAuthStatus() {
		return this.preAuthStatus;
	}

	public void setPreAuthStatus(String preAuthStatus) {
		this.preAuthStatus = preAuthStatus;
	}

	public String getPreAuthType() {
		return this.preAuthType;
	}

	public void setPreAuthType(String preAuthType) {
		this.preAuthType = preAuthType;
	}

	public Long getVersion() {
		return this.version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

}
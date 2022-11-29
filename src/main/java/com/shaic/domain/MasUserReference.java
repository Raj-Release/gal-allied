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
@Table(name="MAS_USER_REFERENCE")
@NamedQueries( {
	@NamedQuery(name="MasUserReference.findAll", query="SELECT m FROM MasUserReference m where m.code Like :code and m.masterCode in (:masterCode) and m.activeStatus = 1"),
	@NamedQuery(name="MasUserReference.findByKey", query = "SELECT m FROM MasUserReference m where m.key =:key")
})
public class MasUserReference implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="MASTER_KEY")
	private Long key;
	
	@Column(name="MASTER_VALUE")
	private String masterValue;
	
	@Column(name="MASTER_CODE")
	private String masterCode;

	@Column(name="ACTIVE_STATUS")
	private Integer activeStatus;
	
	@Column(name="MASTER_TYPE_CODE")
	private String code;
	
	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getMasterValue() {
		return masterValue;
	}

	public void setMasterValue(String masterValue) {
		this.masterValue = masterValue;
	}

	public String getMasterCode() {
		return masterCode;
	}

	public void setMasterCode(String masterCode) {
		this.masterCode = masterCode;
	}

	public Integer getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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
	
}

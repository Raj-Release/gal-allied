package com.shaic.domain.preauth;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
@Entity
@Table(name="MAS_CRITICAL_ILLNESS")
@NamedQueries({
	@NamedQuery(name="CriticalIllnessMaster.findAll", query="SELECT m FROM CriticalIllnessMaster m where m.activeStatus is not null and m.activeStatus = 1"),
	@NamedQuery(name="CriticalIllnessMaster.findByKey",query="SELECT o FROM CriticalIllnessMaster o where o.key=:primaryKey")

})
public class CriticalIllnessMaster implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="CRITICAL_ILLNESS_KEY")
	private Long key;

	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;

//	@Column(name="ACTIVE_STATUS_DATE")
//	private Timestamp activeStatusDate;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;

	@Column(name="VALUE")
	private String value;
	
	@Column(name="ILLNESS_FLAG")
	private String illnessFlag;

//	@Column(name="VERSION")
//	private Long version;


	public Long getActiveStatus() {
		return this.activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
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

	public Timestamp getModifiedDate() {
		return this.modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getIllnessFlag() {
		return illnessFlag;
	}

	public void setIllnessFlag(String illnessFlag) {
		this.illnessFlag = illnessFlag;
	}

}

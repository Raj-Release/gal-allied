package com.shaic.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.shaic.arch.fields.dto.AbstractEntity;

@Entity
@Table(name="MAS_ADMISSION_REASON")
@NamedQueries({
	@NamedQuery(name="ReasonForAdmission.findAll", query="SELECT m FROM ReasonForAdmission m where m.activeStatus is not null and m.activeStatus=1"),
	@NamedQuery(name="ReasonForAdmission.findByValue", query="SELECT r FROM ReasonForAdmission r where r.value in(:reasons)")
})	
public class ReasonForAdmission extends AbstractEntity{


	/**
	 * 
	 */
	private static final long serialVersionUID = -7094328525681954346L;
	
	@Id
	@SequenceGenerator(name="IMS_CLS_REASON_FOR_ADMISSION_KEY_GENERATOR", sequenceName = "SEQ_ADMISSION_REASON_KEY"  ,allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_REASON_FOR_ADMISSION_KEY_GENERATOR" ) 
	@Column(name="ADMISSION_REASON_KEY")
	private Long key;
	
	@Column(name="VALUE")
	private String value;
	
//	@Column(name="VERSION")
//	private Integer version;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;
	
//	@Column(name="ACTIVE_STATUS_DATE")
//	private Date activeStatusDate;
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	
	@Override
	public Long getKey() {
		return this.key;
	}

	@Override
	public void setKey(Long key) {
		this.key = key;
	}

	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
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

	/*public Boolean getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Boolean activeStatus) {
		this.activeStatus = activeStatus;
	}*/

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

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}
}

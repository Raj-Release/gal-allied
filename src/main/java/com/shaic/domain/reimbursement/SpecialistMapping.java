package com.shaic.domain.reimbursement;

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

import com.shaic.arch.fields.dto.AbstractEntity;



@Entity
@Table(name="MAS_USER_SPECIALIST_MAPPING")
@NamedQueries({
@NamedQuery(name="SpecialistMapping.findAll", query="SELECT i FROM SpecialistMapping i"),
@NamedQuery(name="SpecialistMapping.findByUserId", query="SELECT m FROM SpecialistMapping m WHERE Lower(m.userID) LIKE :loginId and m.activeStatus is not null and m.activeStatus = 1"),
})
public class SpecialistMapping extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="EMP_SPL_KEY")
	private Long key;
	
	@Column(name="USER_ID")
	private String userID;
	
	@Column(name="SPECIALIST_TYPE")
	private String specialistType;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;
	
	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE")
	private Date createdDate;


	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	
	public Long getKey() {
		return key;
	}


	public void setKey(Long key) {
		this.key = key;
	}


	public String getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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



	public Long getActiveStatus() {
		return activeStatus;
	}


	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}


	public String getUserID() {
		return userID;
	}


	public void setUserID(String userID) {
		this.userID = userID;
	}


	public String getSpecialistType() {
		return specialistType;
	}


	public void setSpecialistType(String specialistType) {
		this.specialistType = specialistType;
	}


	public Date getCreatedDate() {
		return createdDate;
	}


	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

}

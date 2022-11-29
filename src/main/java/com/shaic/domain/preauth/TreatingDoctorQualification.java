package com.shaic.domain.preauth;

import java.io.Serializable;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="MAS_DOCTOR_QUALIFICATION")

@NamedQueries({
	@NamedQuery(name="TreatingDoctorQualification.findAll",query="SELECT i from TreatingDoctorQualification i where i.activeStatus=1"),
	@NamedQuery(name="TreatingDoctorQualification.findByQualificationKey", query="SELECT p FROM TreatingDoctorQualification p where (:key is null or p.key=:key) and p.activeStatus is not null and p.activeStatus = 1 order by p.qualification asc"),
	@NamedQuery(name="TreatingDoctorQualification.findByQualification", query="SELECT m FROM TreatingDoctorQualification m where m.qualification = :qualification and m.activeStatus=1")
})

public class TreatingDoctorQualification implements Serializable{
	
	@Id
	@SequenceGenerator(name = "IMS_CLS_MAS_SEQ_QUALIFY_KEY", sequenceName = "SEQ_QUALIFY_KEY", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IMS_CLS_MAS_SEQ_QUALIFY_KEY")
	@Column(name="QUALIFY_KEY")
	private Long key;
	

	@Column(name="DOCTOR_QUALIFICATION")
	private String qualification;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}
	
	

}

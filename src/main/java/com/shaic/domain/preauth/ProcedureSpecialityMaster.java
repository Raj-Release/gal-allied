package com.shaic.domain.preauth;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name="MAS_PROCEDURE_SPECIALITY")
@NamedQueries({
	@NamedQuery(name="ProcedureSpecialityMaster.findAll", query="SELECT p.key,p.procedureName FROM ProcedureSpecialityMaster p where p.activeStatus is not null and p.activeStatus = 1 order by p.procedureName asc"),
	@NamedQuery(name="ProcedureSpecialityMaster.findBySpecialistKey", query="SELECT p FROM ProcedureSpecialityMaster p where (:specialityKey is null or p.specialityKey=:specialityKey) and p.activeStatus is not null and p.activeStatus = 1 order by p.procedureName asc"),
	@NamedQuery(name="ProcedureSpecialityMaster.findBySpecialistKeywithtransaction", query="SELECT p FROM ProcedureSpecialityMaster p where (:specialityKey is null or p.specialityKey=:specialityKey) and p.activeStatus is not null and p.activeStatus = 1 and p.transactionFlag=:transactionFlag order by p.procedureName asc"),
	@NamedQuery(name="ProcedureSpecialityMaster.findByProcedureName", query="SELECT p FROM ProcedureSpecialityMaster p where Lower(p.procedureName) like :procedureName ORDER BY p.procedureName")
})
public class ProcedureSpecialityMaster implements Serializable {
	
	@Id
	@SequenceGenerator(name = "IMS_CLS_SEQ_MAS_PROC_SPECIALITY_KEY_GENERATOR", sequenceName = "SEQ_PROC_SPECIALITY_KEY", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IMS_CLS_SEQ_MAS_PROC_SPECIALITY_KEY_GENERATOR")
	@Column(name="PROC_SPECIALITY_KEY")
	private Long key;
	
	@Column(name="PROCEDUE_NAME")
	private String procedureName;
	
	@Column(name="SPECIALITY_KEY")
	private Long specialityKey;
	
	@Column(name="SPECIALITY_NAME")
	private String specialityName;
	
	@Column(name="TREATMENT_TYPE")
	private String treatmentType;
	
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
	
	@Column(name="TRANSACTION_FLAG")
	private String transactionFlag;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getProcedureName() {
		return procedureName;
	}

	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}

	public Long getSpecialityKey() {
		return specialityKey;
	}

	public void setSpecialityKey(Long specialityKey) {
		this.specialityKey = specialityKey;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public String getTreatmentType() {
		return treatmentType;
	}

	public void setTreatmentType(String treatmentType) {
		this.treatmentType = treatmentType;
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

	public String getTransactionFlag() {
		return transactionFlag;
	}

	public void setTransactionFlag(String transactionFlag) {
		this.transactionFlag = transactionFlag;
	}

}

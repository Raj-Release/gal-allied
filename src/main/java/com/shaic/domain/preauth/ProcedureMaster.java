package com.shaic.domain.preauth;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the HMS_MAS_PROCEDURE_T database table.
 * 
 */
@Entity
@Table(name="MAS_PROCEDURE")
@NamedQueries({
@NamedQuery(name="ProcedureMaster.findAll", query="SELECT h.key,h.procedureName FROM ProcedureMaster h where h.activeStatus is not null and h.activeStatus = 1 order by h.procedureName asc"),
@NamedQuery(name="ProcedureMaster.findByProcedureCode", query="SELECT p FROM ProcedureMaster p where Lower(p.procedureCode) like :procedureCode"),
@NamedQuery(name="ProcedureMaster.findByKey", query="SELECT h FROM ProcedureMaster h where h.key = :primarykey"),

})
public class ProcedureMaster implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PROCEDURE_KEY")
	private Long key;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;

//	@Column(name="ACTIVE_STATUS_DATE")
//	private Timestamp activeStatusDate;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE")
	private Date createdDate;

	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;

	@Column(name="PROCEDURE_CODE")
	private String procedureCode;

	@Column(name="PROCEDURE_NAME")
	private String procedureName;
	
	@Column(nullable = true, columnDefinition = "VARCHAR", name="DAY_CARE_FLAG", length=1)
	private String dayCareFlag;

//	@Column(name="VERSION")
//	private Long version;
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="SPECIALITY_TYPE_KEY")
	private SpecialityType speciality;

	public ProcedureMaster() {
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
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

	public String getProcedureCode() {
		return procedureCode;
	}

	public void setProcedureCode(String procedureCode) {
		this.procedureCode = procedureCode;
	}

	public String getProcedureName() {
		return procedureName;
	}

	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}

	public String getDayCareFlag() {
		return dayCareFlag;
	}

	public void setDayCareFlag(String dayCareFlag) {
		this.dayCareFlag = dayCareFlag;
	}
	public SpecialityType getSpeciality() {
		return speciality;
	}

	public void setSpeciality(SpecialityType speciality) {
		this.speciality = speciality;
	}

}
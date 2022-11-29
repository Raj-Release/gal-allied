package com.shaic.domain.preauth;

import java.sql.Timestamp;

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

/**
 * The persistent class for the MAS_DIAGNOSIS_T database table.
 * 
 */
@Entity
@Table(name="MAS_DIAGNOSIS")
@NamedQueries({
	@NamedQuery(name="Diagnosis.findAll", query="SELECT m.key,m.value FROM Diagnosis m where m.activeStatus is not null and m.activeStatus = 1 order by m.value"),
	@NamedQuery(name="Diagnosis.findByName", query="SELECT m FROM Diagnosis m where Lower(m.value) like :diagnosisString ORDER BY m.value"),
	@NamedQuery(name="Diagnosis.findDiagnosisName", query="SELECT o.value FROM Diagnosis o where o.key = :diagnosisKey"),
	@NamedQuery(name="Diagnosis.findDiagnosisByKey", query="SELECT o FROM Diagnosis o where o.key = :diagnosisKey")
})

public class Diagnosis extends AbstractEntity /*implements Serializable*/ {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "IMS_CLS_SEQ_MAS_DIAGNOSIS_KEY_GENERATOR", sequenceName = "SEQ_MAS_DIAGNOSIS_KEY", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IMS_CLS_SEQ_MAS_DIAGNOSIS_KEY_GENERATOR")
	@Column(name="DIAGNOSIS_KEY")
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

//	@Column(name="VERSION")
//	private Long version;

	public Diagnosis() {
	}

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

	
}
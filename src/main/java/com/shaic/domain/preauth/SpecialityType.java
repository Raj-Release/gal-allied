package com.shaic.domain.preauth;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the MAS_ICD_CHAPTER_T database table.
 * 
 */
@Entity
@Table(name="MAS_SPECIALITY_TYPE")
@NamedQueries({
	@NamedQuery(name="SpecialityType.findAll", query="SELECT m FROM SpecialityType m  where m.activeStatus is not null and m.activeStatus = 1 order by m.value asc"),
	@NamedQuery(name="SpecialityType.findByMedical", query="SELECT m FROM SpecialityType m where m.code = 'M' and  m.activeStatus is not null and m.activeStatus = 1 order by m.value asc"),
	@NamedQuery(name="SpecialityType.findBySurgical", query="SELECT m FROM SpecialityType m where m.code = 'S' and m.activeStatus is not null and m.activeStatus = 1  order by m.value asc"),
	@NamedQuery(name="SpecialityType.findByKey", query="SELECT m FROM SpecialityType m where m.key = :key order by m.value asc"),
	@NamedQuery(name="SpecialityType.findBytreatmentTypeId", query="SELECT m FROM SpecialityType m where m.treatmentTypeId = :treatmentTypeId and m.activeStatus is not null and m.activeStatus = 1  order by m.value asc"),
	@NamedQuery(name="SpecialityType.findBygetNextLOV", query="SELECT m FROM SpecialityType m where m.getNextLOV = 'Y' and m.activeStatus is not null and m.activeStatus = 1 ")
	
})
public class SpecialityType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4613869004828855968L;

	@Id
	@Column(name="SPECIALITY_TYPE_KEY")
	private Long key;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;

	@Column(name="TREATMENT_TYPE_KEY")
	private Long treatmentTypeId;

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
	
	@Column(name="CODE")
	private String code;
	
	@Column(name="GET_NEXT_LOV")
	private String getNextLOV;

	public SpecialityType() {
		
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

	public Long getTreatmentTypeId() {
		return treatmentTypeId;
	}

	public void setTreatmentTypeId(Long treatmentTypeId) {
		this.treatmentTypeId = treatmentTypeId;
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
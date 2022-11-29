package com.shaic.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name= "MAS_PHC_DIAGNOSIS")
@NamedQueries({
	@NamedQuery(name="DiagnosisHospitalDetails.findAll",query="SELECT i from DiagnosisHospitalDetails i"),
	@NamedQuery(name="DiagnosisHospitalDetails.findDiagnosisByKey", query="SELECT o FROM DiagnosisHospitalDetails o where o.diagnosisHospitalKey = :findDiagnosisByKey")
	})

public class DiagnosisHospitalDetails {
	
	@Id
	@Column(name="PHC_DISGNOSIS_KEY")
	private Long diagnosisHospitalKey;
	
	@Column(name="DISGNOSIS_NAME")
	private String diagnosisName;
	
	@Column(name="PROCESS_FLOW")
	private String processFlow;
	
	@Column(name="ACTIVE_STATUS")
	private String activeStatus;
	
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
	
	
	public Long getDiagnosisHospitalKey() {
		return diagnosisHospitalKey;
	}

	public void setDiagnosisHospitalKey(Long diagnosisHospitalKey) {
		this.diagnosisHospitalKey = diagnosisHospitalKey;
	}

	public String getDiagnosisName() {
		return diagnosisName;
	}

	public void setDiagnosisName(String diagnosisName) {
		this.diagnosisName = diagnosisName;
	}

	public String getProcessFlow() {
		return processFlow;
	}

	public void setProcessFlow(String processFlow) {
		this.processFlow = processFlow;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
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
}

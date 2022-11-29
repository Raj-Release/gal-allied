package com.shaic.domain;

import java.sql.Timestamp;
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
@Table(name="MAS_SEC_USER_DIAGNOSIS_MAPPING")
@NamedQueries({
@NamedQuery(name="MasSecUserDiagnosisMapping.findByUserId", query="SELECT m FROM MasSecUserDiagnosisMapping m where  Lower(m.UserId) = Lower(:userId) order by m.diagnosisValue asc "),
@NamedQuery(name="MasSecUserDiagnosisMapping.findByUserIdAndKey", query="SELECT c FROM MasSecUserDiagnosisMapping c where  c.diagnosisKey LIKE :key and Lower(c.UserId) = Lower(:userId)")
})	
public class MasSecUserDiagnosisMapping {
	
	@Id
	@SequenceGenerator(name="GN_USER_DIAGNOSIS_KEY_GENERATOR", sequenceName = "SEQ_GN_USER_DIAGNOSIS_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GN_USER_DIAGNOSIS_KEY_GENERATOR" )
	@Column(name="GN_USER_DIAGNOSIS_KEY")
	private Long key;
	
	@Column(name="GN_USER_ID")
	private String UserId;
	
	@Column(name="GET_NEXT_DIAGNOSIS_KEY")
	private Long diagnosisKey;
	
	@Column(name="GET_NEXT_DIAGNOSIS")
	private String diagnosisValue;
	
	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getUserId() {
		return UserId;
	}

	public void setUserId(String userId) {
		UserId = userId;
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

	public Long getDiagnosisKey() {
		return diagnosisKey;
	}

	public void setDiagnosisKey(Long diagnosisKey) {
		this.diagnosisKey = diagnosisKey;
	}

	public String getDiagnosisValue() {
		return diagnosisValue;
	}

	public void setDiagnosisValue(String diagnosisValue) {
		this.diagnosisValue = diagnosisValue;
	}
	
}

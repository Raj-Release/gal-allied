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
@Table(name="MAS_PED_EXCLUSION")
@NamedQueries({ 
		@NamedQuery(name="ExclusionDetails.findAll", query="SELECT m FROM ExclusionDetails m where m.activeStatus is not null and m.activeStatus = 1"),
		@NamedQuery(name="ExclusionDetails.findByImpactId", query="SELECT m FROM ExclusionDetails m where m.impactId = :impactKey and m.activeStatus is not null and m.activeStatus = 1 order by m.exclusion asc")
})
public class ExclusionDetails implements Serializable {
	private static final long serialVersionUID = 2844026706546800712L;

	@Id
	@Column(name="PED_EXCLUSION_KEY")
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
	private Long modifiedDate;

	@Column(name="EXCLUSION")
	private String exclusion;
	
	@Column(name="IMPACT_DIAGNOSIS_CODE")
	private Long impactId;

//	@Column(name="VERSION")
//	private Long version;
	
	//new column
	@Column(name="PAYMENT_FLAG")
	private String paymentFlag;

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

	public Long getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Long modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getExclusion() {
		return exclusion;
	}

	public void setExclusion(String exclusion) {
		this.exclusion = exclusion;
	}

	public Long getImpactId() {
		return impactId;
	}

	public void setImpactId(Long impactId) {
		this.impactId = impactId;
	}


	public String getPaymentFlag() {
		return paymentFlag;
	}

	public void setPaymentFlag(String paymentFlag) {
		this.paymentFlag = paymentFlag;
	}
}

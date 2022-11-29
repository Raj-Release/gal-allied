package com.shaic.domain;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "IMS_CLS_CLAIM_REMAINDER_SKIP")
@NamedQueries({
		@NamedQuery(name = "MasClaimRemainderSkip.findAll", query = "SELECT i FROM MasClaimRemainderSkip i"),
		@NamedQuery(name = "MasClaimRemainderSkip.findByIssuingOffice", query = "SELECT i FROM MasClaimRemainderSkip i where i.PioCode = :issuingOffKey"),
		
})
public class MasClaimRemainderSkip {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CLM_REM_SKIP_KEY")
	private Long key;

	@Column(name = "PIO_CODE")
	private Long PioCode;
	
	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column(name="ACTIVE_STATUS")
	private String activeStatus;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getPioCode() {
		return PioCode;
	}

	public void setPioCode(Long pioCode) {
		PioCode = pioCode;
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

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}

	
}

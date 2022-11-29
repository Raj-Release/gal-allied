package com.shaic.domain;

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
@Table(name="IMS_CLS_BONUS_WAIVER")
@NamedQueries({
	@NamedQuery(name ="BonusWaiver.findAll", query = "SELECT b FROM BonusWaiver b"),
	@NamedQuery(name = "BonusWaiver.findByIntimationNo",query = "select b from BonusWaiver b where b.intimationNumber = :intimationNumber"),
})
public class BonusWaiver {
	
	@Id
	@Column(name="BONUS_WAIVER_KEY")
	private Long bonusWaiverKey;
	
	@Column(name="INTIMATION_NUMER")
	private String intimationNumber;
	
	@Column(name="REMARKS")
	private String remarks;
	
	@Column(name="ACTIVE_FLAG")
	private String activeFlag;
	
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

	public Long getBonusWaiverKey() {
		return bonusWaiverKey;
	}

	public void setBonusWaiverKey(Long bonusWaiverKey) {
		this.bonusWaiverKey = bonusWaiverKey;
	}

	public String getIntimationNumber() {
		return intimationNumber;
	}

	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
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

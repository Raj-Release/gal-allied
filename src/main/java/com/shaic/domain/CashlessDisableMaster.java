package com.shaic.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="MAS_CASHLESS_PROCESS_DISABLE")
@NamedQueries( {
@NamedQuery(name= "CashlessDisableMaster.findAll", query = "SELECT c FROM CashlessDisableMaster c"),
@NamedQuery(name = "CashlessDisableMaster.findByPolicyNumber", query = "select c from CashlessDisableMaster c where c.policyNumber = :policyNumber and c.activeStatus is not null and c.activeStatus = 1")
})
public class CashlessDisableMaster implements Serializable{

	@Id
	@Column(name="CLS_PROS_BLOCK_KEY")
	private Long key;

	@Column(name="ACTIVE_STATUS")
	private Integer activeStatus;
	
	@Column(name="POLICY_NUMBER")
	private String policyNumber;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;
 
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Integer getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
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
	
}

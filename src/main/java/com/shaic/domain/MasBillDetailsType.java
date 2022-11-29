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
@Table(name="MAS_BILL_DETAILS_TYPE")
@NamedQueries( {
@NamedQuery(name="MasBillDetailsType.findAll", query="SELECT m FROM MasBillDetailsType m where m.activeStatus is not null and m.activeStatus = 1"),
@NamedQuery(name = "MasBillDetailsType.findByKey",query = "select o from MasBillDetailsType o where o.key = :parentKey"),
@NamedQuery(name = "MasBillDetailsType.findByBillClassification",query = "select o from MasBillDetailsType o where o.billClassification = :billClassificationKey")
})


public class MasBillDetailsType {
	
	@Id
	@Column(name = "BILL_DETAILS_KEY")
	private Long key;
	
	@Column(name = "BILL_CLASSIFICATION_ID")
	private  Long billClassification;
	
	@Column(name = "VALUE")
	private String value;
	
	@Column(name = "SEQUENCE_NUMBER")
	private Long sequenceNumber;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getBillClassification() {
		return billClassification;
	}

	public void setBillClassification(Long billClassification) {
		this.billClassification = billClassification;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Long getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
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
	
	


}

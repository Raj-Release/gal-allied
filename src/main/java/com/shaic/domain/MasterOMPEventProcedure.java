package com.shaic.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="MAS_OMP_EVENTS_PROCEDURE")
@NamedQueries( {
@NamedQuery(name="MasterOMPEventProcedure.findByKey", query="SELECT m FROM MasterOMPEventProcedure m WHERE m.key = :primaryKey and m.activeStatus='Y'"),
@NamedQuery(name="MasterOMPEventProcedure.findByEventKey", query="SELECT m FROM MasterOMPEventProcedure m WHERE m.eventKey = :eventKey and m.activeStatus='Y'")
})
public class MasterOMPEventProcedure {
	


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="OMP_PROC_KEY")
	private Long key;
	
	@Column(name="OMP_EVENT_KEY")
	private Long eventKey;
	
	@Column(name="EVENT_CODE")
	private String eventCode;
	
	@Column(name="EVENT_DESCRIPTION")
	private String typeOfClaim;
	
	@Column(name="RISK_COVERED")
	private String riskCovered;

	@Column(name="EXPENSES_PAYABLE")
	private String expensesPayable;
	
	@Column(name="PROC_TO_BE_FOLLOW")
	private String procedureToFollow;
	
	@Column(name="DOCUMENTS_REQUIRED")
	private String documentsRequired;	
	
	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name="ACTIVE_STATUS")
	private String activeStatus;

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

	public Long getEventKey() {
		return eventKey;
	}

	public void setEventKey(Long eventKey) {
		this.eventKey = eventKey;
	}

	public String getTypeOfClaim() {
		return typeOfClaim;
	}

	public void setTypeOfClaim(String typeOfClaim) {
		this.typeOfClaim = typeOfClaim;
	}

	public String getRiskCovered() {
		return riskCovered;
	}

	public void setRiskCovered(String riskCovered) {
		this.riskCovered = riskCovered;
	}

	public String getExpensesPayable() {
		return expensesPayable;
	}

	public void setExpensesPayable(String expensesPayable) {
		this.expensesPayable = expensesPayable;
	}

	public String getProcedureToFollow() {
		return procedureToFollow;
	}

	public void setProcedureToFollow(String procedureToFollow) {
		this.procedureToFollow = procedureToFollow;
	}

	public String getDocumentsRequired() {
		return documentsRequired;
	}

	public void setDocumentsRequired(String documentsRequired) {
		this.documentsRequired = documentsRequired;
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

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
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
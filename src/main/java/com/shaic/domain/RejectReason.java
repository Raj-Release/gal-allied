package com.shaic.domain;

import java.io.Serializable;
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

import com.shaic.arch.fields.dto.AbstractEntity;

/**
 * @author karthikeyan.r
 * The persistent class for the IMS_CLS_REJECT_REASON database table.
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name="IMS_CLS_REJECT_REASON")
@NamedQueries({
	@NamedQuery(name="RejectReason.findAll", query="SELECT m FROM RejectReason m")

})
public class RejectReason extends AbstractEntity implements Serializable  {

	@Id
	@SequenceGenerator(name="IMS_CLS_REJECT_REASON_GENERATOR", sequenceName = "SEQ_REJECT_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_REJECT_REASON_GENERATOR" ) 
	@Column(name="REJECT_KEY")
	private Long key;
	
	@Column(name = "TRANSACTION_KEY")
	private Long transactionKey;
	
	@Column(name = "TRANSACTION_TYPE")
	private String transactionType;
	
	@Column(name = "SUBMITTED_DOCUMENTS")
	private String submittedDocFlag;
	
	@Column(name = "FIELD_VISIT_REPORT")
	private String fieldVisitReportFlag;
	
	@Column(name = "INVESTIGATION_REPORT")
	private String InvestigationFlag;
	
	@Column(name = "OTHERS_REPORTS")
	private String othersFlag;
	
	@Column(name = "OTHERS_REPORT_REMARKS")
	private String othersRemarks;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getTransactionKey() {
		return transactionKey;
	}

	public void setTransactionKey(Long transactionKey) {
		this.transactionKey = transactionKey;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getSubmittedDocFlag() {
		return submittedDocFlag;
	}

	public void setSubmittedDocFlag(String submittedDocFlag) {
		this.submittedDocFlag = submittedDocFlag;
	}

	public String getFieldVisitReportFlag() {
		return fieldVisitReportFlag;
	}

	public void setFieldVisitReportFlag(String fieldVisitReportFlag) {
		this.fieldVisitReportFlag = fieldVisitReportFlag;
	}

	public String getInvestigationFlag() {
		return InvestigationFlag;
	}

	public void setInvestigationFlag(String investigationFlag) {
		InvestigationFlag = investigationFlag;
	}

	public String getOthersFlag() {
		return othersFlag;
	}

	public void setOthersFlag(String othersFlag) {
		this.othersFlag = othersFlag;
	}

	public String getOthersRemarks() {
		return othersRemarks;
	}

	public void setOthersRemarks(String othersRemarks) {
		this.othersRemarks = othersRemarks;
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

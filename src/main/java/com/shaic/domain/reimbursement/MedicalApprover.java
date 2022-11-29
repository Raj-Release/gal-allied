package com.shaic.domain.reimbursement;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.Reimbursement;


/**
 * The persistent class for the IMS_CLS_PRE_AUTH_QUERY_T database table.
 * 
 */
@Entity
@Table(name="IMS_CLS_REFER_TO_APPROVER_DTLS")
@NamedQueries({
@NamedQuery(name="MedicalApprover.findAll", query="SELECT i FROM MedicalApprover i"),
@NamedQuery(name="MedicalApprover.findKey", query="SELECT o FROM MedicalApprover o where o.key = :primaryKey"),
@NamedQuery(name="MedicalApprover.findByReimbursementKey", query="SELECT o FROM MedicalApprover o where o.reimbursement.key = :reimbursementKey order by o.key desc"),
@NamedQuery(name="MedicalApprover.findByReimbursementKeyandrecordType", query="SELECT o FROM MedicalApprover o where o.reimbursement.key = :reimbursementKey and o.recordType=:recordType order by o.key desc")
})
public class MedicalApprover extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="IMS_CLS_REFER_TO_APPROVER_DTLS_KEY_GENERATOR", sequenceName = "SEQ_REFER_TO_APPROVER_DTLS_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_REFER_TO_APPROVER_DTLS_KEY_GENERATOR" ) 
	@Column(name="REFERED_APPROVER_KEY")
	private Long key;
	
	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@OneToOne
	@JoinColumn(name="REIMBURSEMENT_KEY", nullable=false)
	private Reimbursement reimbursement;

//	@Column(name="MIGRATED_APPLICATION_ID")
//	private Long migratedApplicationId;

//	@Column(name="MIGRATED_CODE")
//	private String migratedCode;

	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;

	@Column(name="REFERING_REMARKS")
	private String referringRemarks;
	
	@Column(name="REASON_FOR_REFERING")
	private String reasonForReferring;
	
	@Column(name="APPROVER_REPLY")
	private String approverReply;
	
	@Column(name="RECORD_TYPE")
	private String recordType;
	
	
	
	public MedicalApprover() {
	}


	public Long getKey() {
		return key;
	}


	public void setKey(Long key) {
		this.key = key;
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


	public Reimbursement getReimbursement() {
		return reimbursement;
	}


	public void setReimbursement(Reimbursement reimbursement) {
		this.reimbursement = reimbursement;
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


	public String getReferringRemarks() {
		return referringRemarks;
	}


	public void setReferringRemarks(String referringRemarks) {
		this.referringRemarks = referringRemarks;
	}


	public String getReasonForReferring() {
		return reasonForReferring;
	}


	public void setReasonForReferring(String reasonForReferring) {
		this.reasonForReferring = reasonForReferring;
	}


	public String getApproverReply() {
		return approverReply;
	}


	public void setApproverReply(String approverReply) {
		this.approverReply = approverReply;
	}


	public String getRecordType() {
		return recordType;
	}


	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	

	
}
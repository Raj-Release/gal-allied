package com.shaic.domain;

import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.shaic.domain.preauth.Stage;

/**
 * The persistent class for the IMS_CLS_REIMBURSEMENT_REJECT database table.
 * 
 */

/**
 * 
 * @author Lakshminarayana
 *
 */
@Entity
@Table(name="IMS_CLS_REIMBURSEMENT_REJECT")
@NamedQueries({
@NamedQuery(name="ReimbursementRejection.findAll", query="SELECT r FROM ReimbursementRejection r"),
@NamedQuery(name ="ReimbursementRejection.findByKey",query="SELECT r FROM ReimbursementRejection r WHERE r.key = :primaryKey"),
@NamedQuery(name="ReimbursementRejection.findByIntimationKey", query="SELECT r FROM ReimbursementRejection r WHERE r.reimbursement is not null and r.reimbursement.claim is not null and r.reimbursement.claim.intimation is not null and r.reimbursement.claim.intimation.key = :intimationKey"),
@NamedQuery(name="ReimbursementRejection.findByIntimationNumber", query="SELECT r FROM ReimbursementRejection r WHERE r.reimbursement is not null and r.reimbursement.claim is not null and r.reimbursement.claim.intimation is not null and r.reimbursement.claim.intimation.intimationId = :intimationNumber"),
@NamedQuery(name="ReimbursementRejection.findByPolicyNumber", query="SELECT r FROM ReimbursementRejection r WHERE r.reimbursement is not null and r.reimbursement.claim is not null and r.reimbursement.claim.intimation is not null and r.reimbursement.claim.intimation.policy is not null and r.reimbursement.claim.intimation.policy.policyNumber = :policyNumber"),
@NamedQuery(name="ReimbursementRejection.findByClaimKey", query="SELECT r FROM ReimbursementRejection r WHERE r.key = :claimKey"),
@NamedQuery(name="ReimbursementRejection.findByReimbursementKey", query="SELECT r FROM ReimbursementRejection r WHERE r.reimbursement.key = :reimbursementKey order by r.key desc"),
@NamedQuery(name="ReimbursementRejection.findByStatus", query="SELECT r FROM ReimbursementRejection r WHERE r.reimbursement.key = :reimbursementKey and r.status is not null and r.status.key in(97) order by r.key desc"),
@NamedQuery(name="ReimbursementRejection.findByRejectStatus", query="SELECT r FROM ReimbursementRejection r WHERE r.reimbursement is not null and r.reimbursement.claim is not null and r.reimbursement.claim.intimation is not null and r.reimbursement.claim.intimation.key = :intimationKey and r.status is not null and r.status.key  = :statusKey")

})
public class ReimbursementRejection {

	@Id
	@SequenceGenerator(name="IMS_CLS_SEQ_REIMBURSEMENT_REJECT_KEY_GENERATOR", sequenceName = "SEQ_REIMBURSEMENT_REJECT_KEY" , allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_SEQ_REIMBURSEMENT_REJECT_KEY_GENERATOR" )
	@Column(name="REIMBURSEMENT_REJECT_KEY")
	private Long key;
	
	@OneToOne
	@JoinColumn(name="REIMBURSEMENT_KEY",updatable= false)
	private Reimbursement reimbursement;
	
	@Column(name="REJECTION_REMARKS")
	private String rejectionRemarks;
	
	@OneToOne
	@JoinColumn(name="REJECTION_CATEGORY_ID", nullable=false)
	private MastersValue rejectionCategory;
		
	@Column(name="REJECTION_LETTER_REMARKS")
	private String rejectionLetterRemarks;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DRAFTED_DATE")
	private Date rejectionDraftDate;
	
	@Column(name="DISAPPROVED_REMARKS")
	private String disapprovedRemarks;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DISAPPROVED_DATE")
	private Date disapprovedDate;  
	
	@Column(name="REDRAFT_REMARKS")
	private String redraftRemarks;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REDRAFT_DATE")
	private Date redraftDate;
	   
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="APPROVED_REJECTION_DATE")
	private Date approvedRejectionDate;
	
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
	
	@Column(name="DOCUMENT_TOKEN")
	private String fileToken;
	
	@OneToOne
	@JoinColumn(name="STAGE_ID", nullable=false)
	private Stage stage;    
			
	@OneToOne
	@JoinColumn(name="STATUS_ID",nullable=false)
	private Status status;  
	
	@Column(name="ALLOW_RECONSIDERATION")
	private String allowReconsideration;
	
	@Column(name="RECON_UNCHECK_REMARKS")
	private String reconUncheckRemarks;
	
	@Column(name="REJECTION_REMARKS2")
	private String rejectionRemarks2;
	
	@Column(name="REJECTION_LETTER_REMARKS2")
	private String rejectionLetterRemarks2;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Reimbursement getReimbursement() {
		return reimbursement;
	}

	public void setReimbursement(Reimbursement reimbursement) {
		this.reimbursement = reimbursement;
	}

	public String getRejectionRemarks() {
		return rejectionRemarks;
	}

	public void setRejectionRemarks(String rejectionRemarks) {
		this.rejectionRemarks = rejectionRemarks;
	}

	public String getRejectionLetterRemarks() {
		return rejectionLetterRemarks;
	}

	public void setRejectionLetterRemarks(String rejectionLetterRemarks) {
		this.rejectionLetterRemarks = rejectionLetterRemarks;
	}

	public Date getRejectionDraftDate() {
		return rejectionDraftDate;
	}

	public void setRejectionDraftDate(Date rejectionDraftDate) {
		this.rejectionDraftDate = rejectionDraftDate;
	}

	public String getDisapprovedRemarks() {
		return disapprovedRemarks;
	}

	public void setDisapprovedRemarks(String disapprovedRemarks) {
		this.disapprovedRemarks = disapprovedRemarks;
	}

	public Date getDisapprovedDate() {
		return disapprovedDate;
	}

	public void setDisapprovedDate(Date disapprovedDate) {
		this.disapprovedDate = disapprovedDate;
	}

	public String getRedraftRemarks() {
		return redraftRemarks;
	}

	public void setRedraftRemarks(String redraftRemarks) {
		this.redraftRemarks = redraftRemarks;
	}

	public Date getRedraftDate() {
		return redraftDate;
	}

	public void setRedraftDate(Date redraftDate) {
		this.redraftDate = redraftDate;
	}

	public Date getApprovedRejectionDate() {
		return approvedRejectionDate;
	}

	public void setApprovedRejectionDate(Date approvedRejectionDate) {
		this.approvedRejectionDate = approvedRejectionDate;
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

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}


	public String getFileToken() {
		return fileToken;
	}

	public void setFileToken(String fileToken) {
		this.fileToken = fileToken;
	}

	public MastersValue getRejectionCategory() {
		return rejectionCategory;
	}

	public void setRejectionCategory(MastersValue rejectionCategory) {
		this.rejectionCategory = rejectionCategory;
	}

	public String getAllowReconsideration() {
		return allowReconsideration;
	}

	public void setAllowReconsideration(String allowReconsideration) {
		this.allowReconsideration = allowReconsideration;
	}

	public String getReconUncheckRemarks() {
		return reconUncheckRemarks;
	}

	public void setReconUncheckRemarks(String reconUncheckRemarks) {
		this.reconUncheckRemarks = reconUncheckRemarks;
	}
	
	public String getRejectionRemarks2() {
		return rejectionRemarks2;
	}

	public void setRejectionRemarks2(String rejectionRemarks2) {
		this.rejectionRemarks2 = rejectionRemarks2;
	}

	public String getRejectionLetterRemarks2() {
		return rejectionLetterRemarks2;
	}

	public void setRejectionLetterRemarks2(String rejectionLetterRemarks2) {
		this.rejectionLetterRemarks2 = rejectionLetterRemarks2;
	}
	
	
}

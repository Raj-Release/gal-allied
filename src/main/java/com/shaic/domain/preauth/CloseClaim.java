package com.shaic.domain.preauth;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.shaic.domain.Claim;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Policy;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.Status;


@Entity
@Table(name = "IMS_CLS_Manage_ROD")

@NamedQueries({
	@NamedQuery(name = "CloseClaim.findAll", query = "SELECT i FROM CloseClaim i"),
	@NamedQuery(name = "CloseClaim.findByKey", query = "SELECT i FROM CloseClaim i where i.key = :primaryKey"),
	/*IMSSUPPOR-30440
	@NamedQuery(name = "CloseClaim.findClaimsClosedByClaimKey", query = "SELECT i FROM CloseClaim i where i.claim.key = :claimKey and i.closeType like 'C' order by i.key desc"),*/
	@NamedQuery(name = "CloseClaim.findClaimsClosedByClaimKey", query = "SELECT i FROM CloseClaim i where i.claim.key = :claimKey and i.closeType = 'C' order by i.key desc"),
	@NamedQuery(name = "CloseClaim.getByReimbursmentKey", query = "SELECT i FROM CloseClaim i where i.reimbursement.key = :reimbursmentKey order by i.key desc"),
	@NamedQuery(name = "CloseClaim.findByReimbursmentKey", query = "SELECT i FROM CloseClaim i where i.reimbursement.key = :reimbursmentKey and i.status.key between :fromCreateRod and :toFinancial or (i.reimbursement.key = :reimbursmentKey and i.status.key = 214)"),
	@NamedQuery(name = "CloseClaim.getByCloseClaimKey", query = "SELECT i FROM CloseClaim i where i.claim.key = :claimKey and i.closeType = 'C' and i.status.key = 164 order by i.key desc"),
	@NamedQuery(name = "CloseClaim.getByReimbursmentKeyType", query = "SELECT i FROM CloseClaim i where i.reimbursement.key = :reimbursmentKey and i.closeType = 'R' order by i.key desc")
	
})

public class CloseClaim {
	
	@Id
	@SequenceGenerator(name="IMS_CLS_SEQ_MANAGE_ROD_KEY_GENERATOR", sequenceName = "SEQ_MANAGE_ROD_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_SEQ_MANAGE_ROD_KEY_GENERATOR" ) 
	@Column(name="MANAGE_ROD_KEY", updatable=false)
	private Long key;
	
	@OneToOne
	@JoinColumn(name="REIMBURSEMENT_KEY")
	private Reimbursement reimbursement;
	
	@OneToOne
	@JoinColumn(name = "CLOSING_REASON_ID", nullable = true)
	private MastersValue closingReasonId;
	
	@OneToOne
	@JoinColumn(name = "REOPEN_REASON_ID", nullable = true)
	private MastersValue reOpenReasonId;
	
	@Column(name = "CLOSING_REMARKS")
	private String closingRemarks;
	
	@Column(name = "REOPEN_REMARKS")
	private String reOpenRemarks;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;
	
	@OneToOne
	@JoinColumn(name = "STATUS_ID", nullable = false)
	private Status status;

	@OneToOne
	@JoinColumn(name = "STAGE_ID", nullable = false)
	private Stage stage;
	
	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DATE")
	private Timestamp createdDate;

	@Column(name = "CLOSED_DATE")
	private Timestamp closedDate;
	
	@Column(name = "REOPEN_DATE")
	private Timestamp reOpenDate;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CLAIM_KEY", nullable = false)
	private Claim claim;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "POLICY_KEY", nullable = false)
	private Policy policy;
	
	@Column(nullable = true, columnDefinition = "VARCHAR(1)", name = "CLOSE_TYPE", length = 1)
	private String  closeType;
	
	@Column(name="CLOSED_PROV_AMT")
	private Double closedProvisionAmt;
	
	@Column(name="REOPEN_PROV_AMT")
	private Double reopenProvisonAmt;

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

	public MastersValue getClosingReasonId() {
		return closingReasonId;
	}

	public void setClosingReasonId(MastersValue closingReasonId) {
		this.closingReasonId = closingReasonId;
	}

	public String getClosingRemarks() {
		return closingRemarks;
	}

	public void setClosingRemarks(String closingRemarks) {
		this.closingRemarks = closingRemarks;
	}

	public String getReOpenRemarks() {
		return reOpenRemarks;
	}

	public void setReOpenRemarks(String reOpenRemarks) {
		this.reOpenRemarks = reOpenRemarks;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
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

	public MastersValue getReOpenReasonId() {
		return reOpenReasonId;
	}

	public void setReOpenReasonId(MastersValue reOpenReasonId) {
		this.reOpenReasonId = reOpenReasonId;
	}

	public Timestamp getClosedDate() {
		return closedDate;
	}

	public void setClosedDate(Timestamp closedDate) {
		this.closedDate = closedDate;
	}

	public Timestamp getReOpenDate() {
		return reOpenDate;
	}

	public void setReOpenDate(Timestamp reOpenDate) {
		this.reOpenDate = reOpenDate;
	}

	public Claim getClaim() {
		return claim;
	}

	public void setClaim(Claim claim) {
		this.claim = claim;
	}

	public Policy getPolicy() {
		return policy;
	}

	public void setPolicy(Policy policy) {
		this.policy = policy;
	}

	public String getCloseType() {
		return closeType;
	}

	public void setCloseType(String closeType) {
		this.closeType = closeType;
	}

	public Double getClosedProvisionAmt() {
		return closedProvisionAmt;
	}

	public void setClosedProvisionAmt(Double closedProvisionAmt) {
		this.closedProvisionAmt = closedProvisionAmt;
	}

	public Double getReopenProvisonAmt() {
		return reopenProvisonAmt;
	}

	public void setReopenProvisonAmt(Double reopenProvisonAmt) {
		this.reopenProvisonAmt = reopenProvisonAmt;
	}
	


}

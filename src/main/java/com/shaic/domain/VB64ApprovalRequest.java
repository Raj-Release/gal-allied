package com.shaic.domain;

import java.sql.Timestamp;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.Stage;
@Entity
@Table(name="IMS_CLS_VB64_APPROVAL_REQUEST")
@NamedQueries({
@NamedQuery(name="VB64ApprovalRequest.findAll", query="SELECT r FROM VB64ApprovalRequest r"),
@NamedQuery(name ="VB64ApprovalRequest.findByKey",query="SELECT r FROM VB64ApprovalRequest r WHERE r.key = :key"),
@NamedQuery(name ="VB64ApprovalRequest.findByCashlessKey",query="SELECT r FROM VB64ApprovalRequest r WHERE r.cashlessKey.key = :cashlessKey order by r.key desc")
})
public class VB64ApprovalRequest extends AbstractEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8798945375548348348L;


	@Id
	@SequenceGenerator(name="IMS_CLS_VB64_APPROVAL_REQUEST_KEY_GENERATOR", sequenceName = "SEQ_APP_REQ_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_VB64_APPROVAL_REQUEST_KEY_GENERATOR" )
	@Column(name="APP_REQ_KEY")
	private	Long	key;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="INTIMATION_KEY", nullable=false)
	private	Intimation	intimationKey;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CLAIM_KEY", nullable=false)
	private	Claim	claimKey;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CASHLESS_KEY", nullable=false)
	private	Preauth	cashlessKey;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="REIMBURSEMENT_KEY", nullable=false)
	private	Reimbursement	reimbursementKey;
	
	@Column(name="TRANSACTION_FLAG")
	private	String	transactionFlag;
	
	@Column(name="REQUESTED_BY")
	private	String	requestedBy;
	
	@Column(name="PROCESS_TYPE")
	private	String	processType;
	
	@Column(name="REQUESTOR_REMARKS")
	private	String	requestorRemarks;
	
	@Column(name="PAYMENT_STATUS")
	private	String	paymentStatus;
	
	@Column(name="STATUS_FLAG")
	private	String	statusFlag;
	
	@Column(name="APPROVER_REMARKS")
	private	String	approverRemarks;
	
	@Column(name="CREATED_BY")
	private String createdBy;  
			
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column(name="ACTIVE_STATUS")
	private	String	activeStatus;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="STAGE_ID", nullable=false)
	private Stage stage;    
			
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="STATUS_ID",nullable=false)
	private Status status;    
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Intimation getIntimationKey() {
		return intimationKey;
	}

	public void setIntimationKey(Intimation intimationKey) {
		this.intimationKey = intimationKey;
	}

	public Claim getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Claim claimKey) {
		this.claimKey = claimKey;
	}

	public Preauth getCashlessKey() {
		return cashlessKey;
	}

	public void setCashlessKey(Preauth cashlessKey) {
		this.cashlessKey = cashlessKey;
	}

	public Reimbursement getReimbursementKey() {
		return reimbursementKey;
	}

	public void setReimbursementKey(Reimbursement reimbursementKey) {
		this.reimbursementKey = reimbursementKey;
	}

	public String getTransactionFlag() {
		return transactionFlag;
	}

	public void setTransactionFlag(String transactionFlag) {
		this.transactionFlag = transactionFlag;
	}

	public String getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
	}

	public String getProcessType() {
		return processType;
	}

	public void setProcessType(String processType) {
		this.processType = processType;
	}

	public String getRequestorRemarks() {
		return requestorRemarks;
	}

	public void setRequestorRemarks(String requestorRemarks) {
		this.requestorRemarks = requestorRemarks;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getStatusFlag() {
		return statusFlag;
	}

	public void setStatusFlag(String statusFlag) {
		this.statusFlag = statusFlag;
	}

	public String getApproverRemarks() {
		return approverRemarks;
	}

	public void setApproverRemarks(String approverRemarks) {
		this.approverRemarks = approverRemarks;
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
	

}

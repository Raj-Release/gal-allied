package com.shaic.domain;

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
import com.shaic.domain.preauth.Stage;


@Entity
@Table(name = "IMS_CLS_PHY_DOC_VERIFICATION")
@NamedQueries({
	@NamedQuery(name = "PhysicalDocumentVerification.findAll", query = "SELECT i FROM PhysicalDocumentVerification i"),
	@NamedQuery(name ="PhysicalDocumentVerification.findByRodKey",query="SELECT r FROM PhysicalDocumentVerification r WHERE r.reimbursement.key = :primaryKey"),
	@NamedQuery(name ="PhysicalDocumentVerification.findByClaimKey",query="SELECT r FROM PhysicalDocumentVerification r WHERE r.claim.key = :claimKey")	
})
public class PhysicalDocumentVerification  extends AbstractEntity {
	

	private static final long serialVersionUID = 4052781126397799714L;

	@Id
	@SequenceGenerator(name="IMS_PHY_DOC_VER_KEY_GENERATOR", sequenceName = "SEQ_PHY_DOC_VER_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_PHY_DOC_VER_KEY_GENERATOR" ) 
	@Column(name="PHY_DOC_VER_ID")
	private Long key;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CLAIM_KEY", nullable = false)
	private Claim claim;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="INTIMATION_KEY", nullable=true)
	private Intimation intimation;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ROD_KEY", nullable=true)
	private Reimbursement reimbursement;
	
	@Column(name = "SMS_SENT_FLAG")
	private String smsSentFlag;
	
	@Column(name = "EMAIL_SENT_FLAG")
	private String emailSentFlag;
	
	@Column(name = "COMMUNICATION_ENABLE_FLAG")
	private String communicationEnableFlag;
	
	@Column(name = "SMS_SENT_TO")
	private String smsSentTo;
	
	@Column(name = "EMAIL_SENT_TO")
	private String emailSentTo;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="SMS_SENT_DATE")
	private Date smsSentDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="EMAIL_SENT_DATE")
	private Date emailSentDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name="ACTIVE_STATUS")
	private String activeStatus;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="STATUS_ID",nullable=true)
	private Status status;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="STAGE_ID",nullable=true)
	private Stage stage;
	
	@Column(name="PHYSICAL_REMARKS")
	private String physicalRemarks;
	

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Claim getClaim() {
		return claim;
	}

	public void setClaim(Claim claim) {
		this.claim = claim;
	}

	public Intimation getIntimation() {
		return intimation;
	}

	public void setIntimation(Intimation intimation) {
		this.intimation = intimation;
	}

	public Reimbursement getReimbursement() {
		return reimbursement;
	}

	public void setReimbursement(Reimbursement reimbursement) {
		this.reimbursement = reimbursement;
	}

	public String getSmsSentFlag() {
		return smsSentFlag;
	}

	public void setSmsSentFlag(String smsSentFlag) {
		this.smsSentFlag = smsSentFlag;
	}

	public String getEmailSentFlag() {
		return emailSentFlag;
	}

	public void setEmailSentFlag(String emailSentFlag) {
		this.emailSentFlag = emailSentFlag;
	}

	public String getCommunicationEnableFlag() {
		return communicationEnableFlag;
	}

	public void setCommunicationEnableFlag(String communicationEnableFlag) {
		this.communicationEnableFlag = communicationEnableFlag;
	}

	public String getSmsSentTo() {
		return smsSentTo;
	}

	public void setSmsSentTo(String smsSentTo) {
		this.smsSentTo = smsSentTo;
	}

	public String getEmailSentTo() {
		return emailSentTo;
	}

	public void setEmailSentTo(String emailSentTo) {
		this.emailSentTo = emailSentTo;
	}

	public Date getSmsSentDate() {
		return smsSentDate;
	}

	public void setSmsSentDate(Date smsSentDate) {
		this.smsSentDate = smsSentDate;
	}

	public Date getEmailSentDate() {
		return emailSentDate;
	}

	public void setEmailSentDate(Date emailSentDate) {
		this.emailSentDate = emailSentDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
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

	public String getPhysicalRemarks() {
		return physicalRemarks;
	}

	public void setPhysicalRemarks(String physicalRemarks) {
		this.physicalRemarks = physicalRemarks;
	}
	
}

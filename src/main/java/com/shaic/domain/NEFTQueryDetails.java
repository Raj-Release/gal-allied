package com.shaic.domain;

import java.io.Serializable;
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
import javax.persistence.Transient;

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.preauth.Stage;


/**
 * Entity implementation class for Entity: NEFT Query Details
 *
 */
@Entity
@Table(name="IMS_CLS_NEFT_QUERY_DTLS")
@NamedQueries({
	@NamedQuery(name ="NEFTQueryDetails.findAll",query="SELECT n FROM NEFTQueryDetails n"),
	@NamedQuery(name ="NEFTQueryDetails.findByKey",query="SELECT n FROM NEFTQueryDetails n WHERE n.reimbursement is not null and n.reimbursement.key = :rodKey"),
	@NamedQuery(name ="NEFTQueryDetails.findByRODDKey",query="SELECT n FROM NEFTQueryDetails n WHERE n.reimbursement is not null and n.reimbursement.key = :rodKey")
})

public class NEFTQueryDetails extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="IMS_CLS_NEFT_QUERY_DTLS_GENERATOR", sequenceName = "SEQ_NEFT_QUERY_DTLS", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_NEFT_QUERY_DTLS_GENERATOR" )
	@Column(name = "NEFT_QUERY_DTLS_KEY")
	private Long key;
	
	/*@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="INTIMATION_KEY", nullable=true)
	private Intimation intimation;
	*/
	@Column(name = "INTIMATION_KEY")
	private Long intimationKey;
	
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CLAIM_KEY", nullable=false)
	private Claim claim;
	
	/*@Column(name = "CLAIM_KEY")
	private Long claimKey;*/
	
	/*@Column(name = "ROD_KEY")
	private Long rodKey;*/
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ROD_KEY", nullable=true)
	private Reimbursement reimbursement;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name= "QUERY_DATE")
	private Date queryDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REMAINDER_DATE1")
	private Date reminderDate1;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REMAINDER_DATE2")
	private Date reminderDate2;	
	
	@Column(name = "EMAIL_SENT_FLAG")
	private String emailSentFlag;
	
	@Column(name = "EMAIL_SENT_TO")
	private String emailSentTo;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EMAIL_SENT_DATE")
	private Date emailSentDate;	
	
	@Column(name = "SMS_SENT_FLAG")
	private String smsSentFlag;
	
	@Column(name = "SMS_SENT_TO")
	private String smsSentTo;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SMS_SENT_DATE")
	private Date smsSentDate;
	
	@Column(nullable = true, columnDefinition = "NUMBER", name="ACTIVE_STATUS", length = 1)
	private String activeStatus;
	
	@OneToOne
	@JoinColumn(name="STAGE_ID",nullable=false)
	private Stage stage;
	
	@OneToOne
	@JoinColumn(name="STATUS_ID",nullable=false)
	private Status status;
	
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
	
	@Column(name = "REMINDER1_FLAG")
	private String reminder1Flag;
	
	@Column(name = "REMINDER2_FLAG")
	private String reminder2Flag;	


	public Long getKey() {
		return key;
	}


	public void setKey(Long key) {
		this.key = key;
	}


	public Long getIntimationKey() {
		return intimationKey;
	}


	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
	}


	/*public Long getClaimKey() {
		return claimKey;
	}


	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}
*/

	public Date getQueryDate() {
		return queryDate;
	}


	public void setQueryDate(Date queryDate) {
		this.queryDate = queryDate;
	}


	public Date getReminderDate1() {
		return reminderDate1;
	}


	public void setReminderDate1(Date reminderDate1) {
		this.reminderDate1 = reminderDate1;
	}


	public Date getReminderDate2() {
		return reminderDate2;
	}


	public void setReminderDate2(Date reminderDate2) {
		this.reminderDate2 = reminderDate2;
	}


	public String getEmailSentFlag() {
		return emailSentFlag;
	}


	public void setEmailSentFlag(String emailSentFlag) {
		this.emailSentFlag = emailSentFlag;
	}


	public String getEmailSentTo() {
		return emailSentTo;
	}


	public void setEmailSentTo(String emailSentTo) {
		this.emailSentTo = emailSentTo;
	}


	public Date getEmailSentDate() {
		return emailSentDate;
	}


	public void setEmailSentDate(Date emailSentDate) {
		this.emailSentDate = emailSentDate;
	}


	public String getSmsSentFlag() {
		return smsSentFlag;
	}


	public void setSmsSentFlag(String smsSentFlag) {
		this.smsSentFlag = smsSentFlag;
	}


	public String getSmsSentTo() {
		return smsSentTo;
	}


	public void setSmsSentTo(String smsSentTo) {
		this.smsSentTo = smsSentTo;
	}


	public Date getSmsSentDate() {
		return smsSentDate;
	}


	public void setSmsSentDate(Date smsSentDate) {
		this.smsSentDate = smsSentDate;
	}


	public String getActiveStatus() {
		return activeStatus;
	}


	public void setActiveStatus(String activeStatus) {
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
	

	public Claim getClaim() {
		return claim;
	}


	public void setClaim(Claim claim) {
		this.claim = claim;
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


	public Reimbursement getReimbursement() {
		return reimbursement;
	}


	public void setReimbursement(Reimbursement reimbursement) {
		this.reimbursement = reimbursement;
	}


	public String getReminder1Flag() {
		return reminder1Flag;
	}


	public void setReminder1Flag(String reminder1Flag) {
		this.reminder1Flag = reminder1Flag;
	}


	public String getReminder2Flag() {
		return reminder2Flag;
	}


	public void setReminder2Flag(String reminder2Flag) {
		this.reminder2Flag = reminder2Flag;
	}
	
	
	
	
	
	

}
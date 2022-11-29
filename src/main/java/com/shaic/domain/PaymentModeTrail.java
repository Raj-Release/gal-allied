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

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.preauth.Stage;


@Entity
@Table(name="IMS_CLS_PAYMODE_TRAIL")
@NamedQueries({
	@NamedQuery(name ="PaymentModeTrail.findByClaimKey",query="SELECT r FROM PaymentModeTrail r WHERE r.claimKey = :claimKey"),
	@NamedQuery(name ="PaymentModeTrail.findByreimbursementKey",query="SELECT r FROM PaymentModeTrail r WHERE r.reimbursementKey = :reimbursementKey order by r.key desc")
})

public class PaymentModeTrail extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="IMS_CLS_PAYMODE_TRAIL_KEY_GENERATOR", sequenceName = "SEQ_PAYMODE_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_PAYMODE_TRAIL_KEY_GENERATOR" )
	
	@Column(name = "PAYMODE_KEY")
	private Long key;
	
	@Column(name = "INTIMATION_KEY")
	private Long intimationKey;
	
	@Column(name = "CLAIM_TYPE_ID")
	private Long claimTypeId;
	
	@OneToOne
	@JoinColumn(name="STAGE_ID",nullable=false)
	private Stage stageId;
		
	@OneToOne
	@JoinColumn(name="STATUS_ID",nullable=false)
	private Status statusId;
	
	@Column(name = "PAYMODE_REMARKS")
	private String payModeRemarks;
	
	@Column(name = "ACTIVE_STATUS")
	private Long activeStatus;
	
	@Column(name = "OFFICE_CODE")
	private String officeCode;
	
	@Column(name = "CREATED_BY")
	private String createdBy;	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")	
	private Date createdDate;
	
	@Column(name = "CLAIM_KEY")
	private Long claimKey;
	
	@Column(name = "CASHLESS_KEY")
	private Long cashlessKey;
	
	@Column(name = "REIMBURSEMENT_KEY")
	private Long reimbursementKey;
	
	@Column(name = "ALERT_FLAG")
	private String alertFlag;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "HISTORY_DATE")
	private Date historyDate;
	
	@Column(name = "DOC_ACKNOWLEDGEMENT_KEY")
	private Long docAckKey;
	
	@Column(name = "PAYMENT_MODE")
	private String paymentMode;

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

	public Long getClaimTypeId() {
		return claimTypeId;
	}

	public void setClaimTypeId(Long claimTypeId) {
		this.claimTypeId = claimTypeId;
	}
		
	public String getPayModeRemarks() {
		return payModeRemarks;
	}

	public void setPayModeRemarks(String payModeRemarks) {
		this.payModeRemarks = payModeRemarks;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getOfficeCode() {
		return officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
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

	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}

	public Long getCashlessKey() {
		return cashlessKey;
	}

	public void setCashlessKey(Long cashlessKey) {
		this.cashlessKey = cashlessKey;
	}

	public Long getReimbursementKey() {
		return reimbursementKey;
	}

	public void setReimbursementKey(Long reimbursementKey) {
		this.reimbursementKey = reimbursementKey;
	}

	public String getAlertFlag() {
		return alertFlag;
	}

	public void setAlertFlag(String alertFlag) {
		this.alertFlag = alertFlag;
	}

	public Date getHistoryDate() {
		return historyDate;
	}

	public void setHistoryDate(Date historyDate) {
		this.historyDate = historyDate;
	}

	public Long getDocAckKey() {
		return docAckKey;
	}

	public void setDocAckKey(Long docAckKey) {
		this.docAckKey = docAckKey;
	}
	
	public Stage getStageId() {
		return stageId;
	}

	public void setStageId(Stage stageId) {
		this.stageId = stageId;
	}

	public Status getStatusId() {
		return statusId;
	}

	public void setStatusId(Status statusId) {
		this.statusId = statusId;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	
	
	
}

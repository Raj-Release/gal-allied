package com.shaic.domain;

import java.io.Serializable;
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
@Table(name = "IMS_CLS_DOCTOR_STG_INFOR")
@NamedQueries({
	@NamedQuery(name = "InternalNotesHistory.findAll", query = "SELECT i FROM InternalNotesHistory i"),
	@NamedQuery(name = "InternalNotesHistory.findByClaimKey", query = "SELECT i FROM InternalNotesHistory i where i.claimKey=:claimKey and i.activeStatus = 1 order by i.key asc"),
})
public class InternalNotesHistory implements Serializable {
	
	@Id
	@Column(name = "STAGE_DOC_INFOR_KEY")
	private Long key;
	
	@Column(name = "INTIMATION_KEY")
	private Long intiamtionKey;
	
	@Column(name = "CLAIM_TYPE_ID")
	private Long claimTypeKey;
	
	@Column(name = "STAGE_ID")
	private Long stageKey;
	
	@Column(name = "STATUS_ID")
	private Long statusKey;     
	
	@Column(name = "STATUS_REMARKS")
	private String statusRemarks;
	
	@Column(name = "ACTIVE_STATUS")
	private Integer activeStatus;
	
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
	private Long rodKey;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "HISTORY_DATE")
	private Date historyDate;
	
	@Column(name = "REF_NO")
	private String referenceNo;
	
	@Column(name = "TRANSACTION_TYPE")
	private String transacType;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getIntiamtionKey() {
		return intiamtionKey;
	}

	public void setIntiamtionKey(Long intiamtionKey) {
		this.intiamtionKey = intiamtionKey;
	}

	public Long getClaimTypeKey() {
		return claimTypeKey;
	}

	public void setClaimTypeKey(Long claimTypeKey) {
		this.claimTypeKey = claimTypeKey;
	}

	public Long getStageKey() {
		return stageKey;
	}

	public void setStageKey(Long stageKey) {
		this.stageKey = stageKey;
	}

	public Long getStatusKey() {
		return statusKey;
	}

	public void setStatusKey(Long statusKey) {
		this.statusKey = statusKey;
	}

	public String getStatusRemarks() {
		return statusRemarks;
	}

	public void setStatusRemarks(String statusRemarks) {
		this.statusRemarks = statusRemarks;
	}

	public Integer getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Integer activeStatus) {
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

	public Long getRodKey() {
		return rodKey;
	}

	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}

	public Date getHistoryDate() {
		return historyDate;
	}

	public void setHistoryDate(Date historyDate) {
		this.historyDate = historyDate;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public String getTransacType() {
		return transacType;
	}

	public void setTransacType(String transacType) {
		this.transacType = transacType;
	}  
	
}

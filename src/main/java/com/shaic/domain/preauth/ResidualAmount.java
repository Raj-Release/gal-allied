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

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Status;

@Entity
@Table(name = "IMS_CLS_RESIDUAL_AMOUNT")
@NamedQueries({
	@NamedQuery(name = "ResidualAmount.findAll", query = "SELECT i FROM ResidualAmount i"),
	@NamedQuery(name = "ResidualAmount.findByPreauthKey", query = "SELECT o FROM ResidualAmount o where o.transactionKey = :preauthKey"),
	@NamedQuery(name = "ResidualAmount.findByKey", query = "SELECT o FROM ResidualAmount o where o.key = :primarykey"),
	@NamedQuery(name = "ResidualAmount.findByTransactionKey", query = "SELECT o FROM ResidualAmount o where o.transactionKey = :transactionKey"),
})
public class ResidualAmount extends AbstractEntity {

	private static final long serialVersionUID = 6324167521456235591L;

	@Id
	@SequenceGenerator(name="IMS_CLS_RESIDUAL_AMOUNT_KEY_GENERATOR", sequenceName = "SEQ_RESIDUAL_AMOUNT_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_RESIDUAL_AMOUNT_KEY_GENERATOR" ) 
	@Column(name="RESIDUAL_AMOUNT_KEY", updatable=false)
	private Long key;
	
	@Column(name = "TRANSACTION_KEY", nullable = false)
	private Long transactionKey;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;

//	@Column(name="ACTIVE_STATUS_DATE")
//	private Timestamp activeStatusDate;
	
	@OneToOne
	@JoinColumn(name="STAGE_ID",nullable=false)
	private Stage stage;
	
	@OneToOne
	@JoinColumn(name="STATUS_ID",nullable=false)
	private Status status;
	
	@Column(name="OFFICE_CODE")
	private String officeCode;

	
	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	
//	@Column(name="STATUS_DATE")
//	private Timestamp statusDate;
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column(name="APPROVED_AMOUNT")
	private Double approvedAmount;
	
	@Column(name="NET_APPROVED_AMOUNT")
	private Double netApprovedAmount;

//	@Column(name="DIFF_AMOUNT")
//	private Double diffAmount;
	
//	@Column(name="DOWNSIZED_AMOUNT")
//	private Double downSizedAmount;
	
	@Column(name="REMARKS")
	private String remarks;
	
	@Column(name = "AMOUNT_CONSIDERED")
	private Double amountConsideredAmount;
	
	@Column(name = "MINIMUM_AMOUNT")
	private Double minimumAmount;
	
	@Column(name = "COPAY_PERCENTAGE")
	private Double copayPercentage;
	
	@Column(name = "COPAY_AMOUNT")
	private Double copayAmount;
	
	@Column(name = "NET_AMOUNT")
	private Double netAmount;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COPAY_TYPE_ID", nullable = true)
	private MastersValue coPayTypeId;

	public Long getKey() {
		return key;
	}


	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
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

	public Double getApprovedAmount() {
		return approvedAmount;
	}

	public void setApprovedAmount(Double approvedAmount) {
		this.approvedAmount = approvedAmount;
	}

//	public Double getDiffAmount() {
//		return diffAmount;
//	}
//
//	public void setDiffAmount(Double diffAmount) {
//		this.diffAmount = diffAmount;
//	}
	
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Double getAmountConsideredAmount() {
		return amountConsideredAmount;
	}

	public void setAmountConsideredAmount(Double amountConsideredAmount) {
		this.amountConsideredAmount = amountConsideredAmount;
	}

	public Double getMinimumAmount() {
		return minimumAmount;
	}

	public void setMinimumAmount(Double minimumAmount) {
		this.minimumAmount = minimumAmount;
	}

	public Double getCopayPercentage() {
		return copayPercentage;
	}

	public void setCopayPercentage(Double copayPercentage) {
		this.copayPercentage = copayPercentage;
	}

	public Double getCopayAmount() {
		return copayAmount;
	}

	public void setCopayAmount(Double copayAmount) {
		this.copayAmount = copayAmount;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}


	public Long getTransactionKey() {
		return transactionKey;
	}


	public void setTransactionKey(Long transactionKey) {
		this.transactionKey = transactionKey;
	}


	public Double getNetApprovedAmount() {
		return netApprovedAmount;
	}


	public void setNetApprovedAmount(Double netApprovedAmount) {
		this.netApprovedAmount = netApprovedAmount;
	}


	public MastersValue getCoPayTypeId() {
		return coPayTypeId;
	}


	public void setCoPayTypeId(MastersValue coPayTypeId) {
		this.coPayTypeId = coPayTypeId;
	}
	
}

package com.shaic.reimbursement.paymentprocess.stoppaymenttracking.bean;

import java.io.Serializable;
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

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.MastersValue;

@Entity
@Table(name="IMS_CLS_STOP_PAYMENT_REQ")
@NamedQueries({
@NamedQuery(name ="StopPaymentRequest.findByKey",query="SELECT c FROM StopPaymentRequest c WHERE c.key = :Key"),
@NamedQuery(name ="StopPaymentRequest.findByintimationKey",query="SELECT c FROM StopPaymentRequest c WHERE c.intimationKey = :intimationKey"),
@NamedQuery(name ="StopPaymentRequest.findByRodNumber",query="SELECT c FROM StopPaymentRequest c WHERE c.rodNo = :rodNo")

})
public class StopPaymentRequest extends AbstractEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@SequenceGenerator(name="IMS_CLS_STOP_PAYMENT_REQ_GENERATOR", sequenceName = "SEQ_STOP_PAYMENT_REQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_STOP_PAYMENT_REQ_GENERATOR" )
	@Column(name = "STOP_PAYMENT_KEY")
	private Long key;
	
	@Column(name = "INTIMATION_KEY")
	private Long intimationKey;
	
	@Column(name = "INTIMATION_NUMBER")
	private String intimationNo;
	
	@Column(name = "ROD_NUMBER")
	private String rodNo;
	
	@Column(name = "POLICY_NUMBER")
	private String policyNo;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "REASON_STOP_PAYMENT", nullable=true)
	private MastersValue reasonStopPaymnt;
	
	@Column(name = "OTHERS_REMARKS")
	private String otherRemarks;
	
	@Column(name = "STOP_PAYMENT_REMARKS")
	private String stopPaymentReamrks;
	
	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "ACTION")
	private String action;
	
	@Column(name = "VALIDATION_REMARKS")
	private String validationRemarks;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "MODIFIED_BY")
	private String ModifiedBy;
	
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name = "UTR_NUMBER")
	private String utrNumber;
	
	@Column(name = "RE_ISSUEING_PAYMENT_MODE")
	private String paymentMode;
	
	@Column(name = "PAYMENT_CREDIT_DATE")
	private Date paymentCreditDate;
	
	@Column(name = "PAID_DATE")
	private Date paidDate;

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

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public String getRodNo() {
		return rodNo;
	}

	public void setRodNo(String rodNo) {
		this.rodNo = rodNo;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public MastersValue getReasonStopPaymnt() {
		return reasonStopPaymnt;
	}

	public void setReasonStopPaymnt(MastersValue reasonStopPaymnt) {
		this.reasonStopPaymnt = reasonStopPaymnt;
	}

	public String getOtherRemarks() {
		return otherRemarks;
	}

	public void setOtherRemarks(String otherRemarks) {
		this.otherRemarks = otherRemarks;
	}

	public String getStopPaymentReamrks() {
		return stopPaymentReamrks;
	}

	public void setStopPaymentReamrks(String stopPaymentReamrks) {
		this.stopPaymentReamrks = stopPaymentReamrks;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getValidationRemarks() {
		return validationRemarks;
	}

	public void setValidationRemarks(String validationRemarks) {
		this.validationRemarks = validationRemarks;
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
		return ModifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		ModifiedBy = modifiedBy;
	}

    

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getUtrNumber() {
		return utrNumber;
	}

	public void setUtrNumber(String utrNumber) {
		this.utrNumber = utrNumber;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public Date getPaymentCreditDate() {
		return paymentCreditDate;
	}

	public void setPaymentCreditDate(Date paymentCreditDate) {
		this.paymentCreditDate = paymentCreditDate;
	}

	public Date getPaidDate() {
		return paidDate;
	}

	public void setPaidDate(Date paidDate) {
		this.paidDate = paidDate;
	}
	
	
	


}

/**
 * 
 */
package com.shaic.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.shaic.arch.fields.dto.AbstractEntity;

/**
 * @author ntv.vijayar
 *
 */
@Entity
@Table(name="IMS_CLS_CLAIM_PAYMENT_CANCEL")
@NamedQueries({
	@NamedQuery(name ="ClaimPaymentCancel.findByKey",query="SELECT r FROM ClaimPaymentCancel r WHERE r.key = :primaryKey"),
	@NamedQuery(name ="ClaimPaymentCancel.findByRodNo",query="SELECT r FROM ClaimPaymentCancel r WHERE r.rodNumber = :rodNumber and r.readFlag = 'N' and r.cancelType = :cancelType"),
	@NamedQuery(name ="ClaimPaymentCancel.findAll",query="SELECT r FROM ClaimPaymentCancel r WHERE r.readFlag = 'N' and r.cancelType = '1'"),
	@NamedQuery(name ="ClaimPaymentCancel.findByCancelType",query="SELECT r FROM ClaimPaymentCancel r WHERE r.readFlag = 'N' and r.cancelType = :cancelType"),
	@NamedQuery(name ="ClaimPaymentCancel.findByRodNoPaymentType",query="SELECT r FROM ClaimPaymentCancel r WHERE r.rodNumber = :rodNumber and Lower(r.paymentType) = :paymentType and r.readFlag = 'N'")
	
})
public class ClaimPaymentCancel extends AbstractEntity implements Serializable {
	
	@Id
	@SequenceGenerator(name="IMS_CLAIM_PAYMENT_CANCEL_KEY_GENERATOR", sequenceName = "SEQ_CLAIM_PAYMENT_CANCEL_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLAIM_PAYMENT_CANCEL_KEY_GENERATOR" )
	@Column(name = "PAYMENT_CANCEL_KEY")
	private Long key;
	
	@Column(name = "CLAIM_NUMBER")
	private String claimNumber;
	
	@Column(name = "INTIMATION_NUMBER")
	private String intimationNumber;
	
	@Column(name = "POLICY_NUMBER")
	private String policyNumber;
	
	@Column(name = "ROD_NUMBER")
	private String rodNumber;
	
	@Column(name = "CLAIM_TYPE")
	private String claimType;

	@Column(name = "PAYMENT_KEY")
	private Long paymentKey;
	
	@Column(name = "LOT_NUMBER")
	private String lotNumber;
	
	@Column(name = "BATCH_NUMBER")
	private String batchNumber;
	
	@Column(name = "PAYMENT_TYPE")
	private String paymentType;
	
	@Column(name = "CHEQUE_NUMBER")
	private String chequeNumber;
	
	@Column(name = "CANCEL_TYPE")
	private String cancelType;
	
	@Column(name = "REISSUE_REJECTION")
	private String reissueRejection;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CANCEL_DATE")
	private Date cancelDate;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "READ_FLAG")
	private String readFlag;

	@Column(name = "CRCN_REMARKS")
	private String cancelRemarks;
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getClaimNumber() {
		return claimNumber;
	}

	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}

	public String getIntimationNumber() {
		return intimationNumber;
	}

	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getRodNumber() {
		return rodNumber;
	}

	public void setRodNumber(String rodNumber) {
		this.rodNumber = rodNumber;
	}

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	public String getLotNumber() {
		return lotNumber;
	}

	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getChequeNumber() {
		return chequeNumber;
	}

	public void setChequeNumber(String chequeNumber) {
		this.chequeNumber = chequeNumber;
	}

	public String getCancelType() {
		return cancelType;
	}

	public void setCancelType(String cancelType) {
		this.cancelType = cancelType;
	}

	public String getReissueRejection() {
		return reissueRejection;
	}

	public void setReissueRejection(String reissueRejection) {
		this.reissueRejection = reissueRejection;
	}

	public Date getCancelDate() {
		return cancelDate;
	}

	public void setCancelDate(Date cancelDate) {
		this.cancelDate = cancelDate;
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

	public String getReadFlag() {
		return readFlag;
	}

	public void setReadFlag(String readFlag) {
		this.readFlag = readFlag;
	}
	
	public String getCancelRemarks() {
		return cancelRemarks;
	}

	public void setCancelRemarks(String cancelRemarks) {
		this.cancelRemarks = cancelRemarks;
	}
	
	public Long getPaymentKey() {
		return paymentKey;
	}

	public void setPaymentKey(Long paymentKey) {
		this.paymentKey = paymentKey;
	}

	@Override
    public boolean equals(Object obj) {
        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        } else if (getKey() == null) {
            return obj == this;
        } else {
            return getKey().equals(((ClaimPaymentCancel) obj).getKey());
        }
    }

    @Override
    public int hashCode() {
        if (key != null) {
            return key.hashCode();
        } else {
            return super.hashCode();
        }
    }
	
	

}

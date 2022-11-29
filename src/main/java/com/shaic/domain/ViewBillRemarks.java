/**
 * 
 */
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

/**
 * @author ntv.vijayar
 *
 */
@Entity
@Table(name="IMS_CLS_VIEW_BILL_REMARKS")
@NamedQueries({
@NamedQuery(name= "ViewBillRemarks.findAll", query="SELECT v FROM ViewBillRemarks v"),
@NamedQuery(name= "ViewBillRemarks.findByRodBillTypeBillClassification", query="SELECT v FROM ViewBillRemarks v where v.reimbursementKey.key = :rodKey and v.billClassification.key = :billClassificationKey and v.billType.key = :billTypeKey")
})

public class ViewBillRemarks extends AbstractEntity {
	
	@Id
	@SequenceGenerator(name="IMS_VIEW_BILL_REMARKS_KEY_GENERATOR", sequenceName = "SEQ_BILL_VW_RKS_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_VIEW_BILL_REMARKS_KEY_GENERATOR")
	@Column(name = "BILL_KEY")
	private Long billKey;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="REIMBURSEMENT_KEY", nullable=false)
	private Reimbursement reimbursementKey;

	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="BILL_TYPE_NUMBER", nullable=false)
	private MasBillDetailsType billType;
	
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="BILL_CLASSIFICATION_ID", nullable=false)
	private MasBillClassification billClassification;
	
	
	@Column(name = "BILLING_REMARKS")
	private String billingRemarks;
	
	@Column(name = "FA_REMARKS")
	private String faRemarks;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="BILLING_DATE")
	private Date billingDate;  
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FA_DATE")
	private Date faDate;
	
	@Column(name = "DELETED_FLAG")
	private String deletedFlag;

	public Long getBillKey() {
		return billKey;
	}

	public void setBillKey(Long billKey) {
		this.billKey = billKey;
	}

	public Reimbursement getReimbursementKey() {
		return reimbursementKey;
	}

	public void setReimbursementKey(Reimbursement reimbursementKey) {
		this.reimbursementKey = reimbursementKey;
	}

	public MasBillDetailsType getBillType() {
		return billType;
	}

	public void setBillType(MasBillDetailsType billType) {
		this.billType = billType;
	}

	public MasBillClassification getBillClassification() {
		return billClassification;
	}

	public void setBillClassification(MasBillClassification billClassification) {
		this.billClassification = billClassification;
	}

	public String getBillingRemarks() {
		return billingRemarks;
	}

	public void setBillingRemarks(String billingRemarks) {
		this.billingRemarks = billingRemarks;
	}

	public String getFaRemarks() {
		return faRemarks;
	}

	public void setFaRemarks(String faRemarks) {
		this.faRemarks = faRemarks;
	}

	public Date getBillingDate() {
		return billingDate;
	}

	public void setBillingDate(Date billingDate) {
		this.billingDate = billingDate;
	}

	public Date getFaDate() {
		return faDate;
	}

	public void setFaDate(Date faDate) {
		this.faDate = faDate;
	}

	public String getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
	}

	@Override
	public Long getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setKey(Long key) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	

}

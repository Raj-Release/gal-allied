package com.shaic.domain;

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

@Entity
@Table(name="IMS_CLS_PA_OPT_COVER")
@NamedQueries({
	@NamedQuery(name = "PAOptionalCover.findByKey", query = "SELECT o FROM PAOptionalCover o where o.key = :key"),
	@NamedQuery(name = "PAOptionalCover.findByRodKey", query = "SELECT o FROM PAOptionalCover o where o.rodKey = :rodKey and (o.deletedFlag is null or o.deletedFlag = 'N')"),
	@NamedQuery(name = "PAOptionalCover.findByAckKey", query = "SELECT o FROM PAOptionalCover o where o.acknowledgementKey = :ackDocKey and (o.deletedFlag is null or o.deletedFlag = 'N')"),
	@NamedQuery(name = "PAOptionalCover.findByRodKeyAndCoverId", query = "SELECT o FROM PAOptionalCover o where o.rodKey = :rodKey and o.coverId = :coverId and (o.deletedFlag is null or o.deletedFlag = 'N')"),
	@NamedQuery(name = "PAOptionalCover.findByClaimKey", query = "SELECT o FROM PAOptionalCover o where o.claimKey = :claimKey"),
	@NamedQuery(name = "PAOptionalCover.findByClaimKeyAndDeletedFlag", query = "SELECT o FROM PAOptionalCover o where o.claimKey = :claimKey and (o.deletedFlag is null or o.deletedFlag = 'N')"),
})

public class PAOptionalCover extends AbstractEntity{

	@Id
	@SequenceGenerator(name="IMS_CLS_PA_OPT_COVER_KEY_GENERATOR", sequenceName = "SEQ_PA_OPT_COVER_KEY" , allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_PA_OPT_COVER_KEY_GENERATOR" )
	@Column(name="PA_COVER_KEY")
	private Long key;
	
	@Column(name="ACKNOWLEDGEMENT_KEY")
	private Long acknowledgementKey;
	
	@Column(name="ROD_KEY")
	private Long rodKey;
	
	@Column(name="INSURED_KEY")
	private Long insuredKey;
	
	@Column(name="COVER_ID")
	private Long coverId;
	
	@Column(name="CLAIMED_AMOUNT")
	private Double claimedAmount;
	
	@Column(name="REMARKS")
	private String remarks;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name = "DELETED_FLAG")
	private String deletedFlag;

	@Column(name = "APPROVED_AMOUNT")
	private Double approvedAmt;
	
	@Column(name = "LMT_ELIGIBLE_AMT")
	private Double lmtEligibleAmt;
	
	@Column(name = "PAYABLE_AMOUNT_PER_DAY")
	private Double payableAmtPerDay;
	
	@Column(name = "PAYABLE_DAYS")
	private Long payableDays;
	
	@Column(name = "ALLOWED_DAYS")
	private Long allowedDays;
	
	@Column(name = "TOTAL_CLAIMED_AMOUNT")
	private Double totalClaimAmt;
	
	// CR2019100 -  Deduction for Medical Extention ROD
	@Column(name = "DEDUCTION_AMOUNT")
	private Double deductionAmt;
	
	@Column(name = "CLAIMED_AMOUNT_PER_DAY")
	private Double claimedAmtPerDay;
	
	@Column(name = "CLAIMED_DAYS")
	private Double claimedDays;
	
	@Column(name = "BILL_DATE")
	private Date billDate;

	@Column(name = "BILL_NO")
	private String billNo;
	
	@Column(name = "CLAIM_KEY")
	private Long claimKey;
	
	@Column(name = "PROVISION_AMOUNT")
	private Double provisionAmount;
	
	@Column(name = "COV_ELIGIBLE_FLAG")
	private String covEligibleFlag;
	
	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getAcknowledgementKey() {
		return acknowledgementKey;
	}

	public void setAcknowledgementKey(Long acknowledgementKey) {
		this.acknowledgementKey = acknowledgementKey;
	}

	public Long getRodKey() {
		return rodKey;
	}

	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}

	public Long getInsuredKey() {
		return insuredKey;
	}

	public void setInsuredKey(Long insuredKey) {
		this.insuredKey = insuredKey;
	}

	public Long getCoverId() {
		return coverId;
	}

	public void setCoverId(Long coverId) {
		this.coverId = coverId;
	}

	public Double getClaimedAmount() {
		return claimedAmount;
	}

	public void setClaimedAmount(Double claimedAmount) {
		this.claimedAmount = claimedAmount;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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

	public String getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
	}





	public Double getClaimedAmtPerDay() {
		return claimedAmtPerDay;
	}

	public void setClaimedAmtPerDay(Double claimedAmtPerDay) {
		this.claimedAmtPerDay = claimedAmtPerDay;
	}

	public Double getClaimedDays() {
		return claimedDays;
	}

	public void setClaimedDays(Double claimedDays) {
		this.claimedDays = claimedDays;
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public Double getTotalClaimAmt() {
		return totalClaimAmt;
	}

	public void setTotalClaimAmt(Double totalClaimAmt) {
		this.totalClaimAmt = totalClaimAmt;
	}

	
	// CR2019100 -  Deduction for Medical Extention ROD
	public Double getDeductionAmt() {
		return deductionAmt;
	}

	public void setDeductionAmt(Double deductionAmt) {
		this.deductionAmt = deductionAmt;
	}                                                    // CR2019100 -  Deduction for Medical Extention ROD

	public Double getApprovedAmt() {
		return approvedAmt;
	}

	public void setApprovedAmt(Double approvedAmt) {
		this.approvedAmt = approvedAmt;
	}

	public Double getLmtEligibleAmt() {
		return lmtEligibleAmt;
	}

	public void setLmtEligibleAmt(Double lmtEligibleAmt) {
		this.lmtEligibleAmt = lmtEligibleAmt;
	}

	public Double getPayableAmtPerDay() {
		return payableAmtPerDay;
	}

	public void setPayableAmtPerDay(Double payableAmtPerDay) {
		this.payableAmtPerDay = payableAmtPerDay;
	}

	public Long getPayableDays() {
		return payableDays;
	}

	public void setPayableDays(Long payableDays) {
		this.payableDays = payableDays;
	}

	public Long getAllowedDays() {
		return allowedDays;
	}

	public void setAllowedDays(Long allowedDays) {
		this.allowedDays = allowedDays;
	}

	public Double getProvisionAmount() {
		return provisionAmount;
	}

	public void setProvisionAmount(Double provisionAmount) {
		this.provisionAmount = provisionAmount;
	}

	public String getCovEligibleFlag() {
		return covEligibleFlag;
	}

	public void setCovEligibleFlag(String covEligibleFlag) {
		this.covEligibleFlag = covEligibleFlag;
	}
	
	
	

}

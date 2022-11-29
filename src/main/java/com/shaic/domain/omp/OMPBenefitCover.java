package com.shaic.domain.omp;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.Insured;
import com.shaic.domain.MastersValue;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.OMPReimbursement;


/**
 * 
 * The persistent class for the IMS_CLS_OMP_BENEFIT_COVER table.
 * 
 */

@Entity
@Table(name="IMS_CLS_OMP_BENEFIT_COVER")
@NamedQueries({
	@NamedQuery(name ="OMPBenefitCover.findByKey",query="SELECT r FROM OMPBenefitCover r WHERE r.key = :primaryKey and r.deletedFlag is NOT NULL and r.deletedFlag = 'N'"),
	@NamedQuery(name = "OMPBenefitCover.findByRodKey", query = "SELECT r FROM OMPBenefitCover r where r.rodKey is not null and r.rodKey.key = :rodKey and r.deletedFlag = 'N'"),
})

public class OMPBenefitCover extends AbstractEntity implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="IMS_CLS_OMP_BENEFIT_COVER_KEY_GENERATOR", sequenceName = "SEQ_OMP_BENEFITS_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_OMP_BENEFIT_COVER_KEY_GENERATOR" )
	@Column(name = "OMP_BENEFITS_KEY")
	private Long key;
		
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ROD_KEY", nullable=false)
	private OMPReimbursement rodKey;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="INSURED_KEY", nullable=false)
	private Insured insuredKey;
	
	@Column(name = "WEEKS_DURATION")
	private String weeksDuration;
	
	@Column(name = "BILL_NO")
	private String billNo;
	
	@Temporal(TemporalType.DATE)
	@Column(name="BILL_DATE")
	private Date billDate;
	
	@Column(name = "BILL_AMOUNT")
	private Double billAmount;
	
	@Column(name = "DEDUCTION_AMOUNT")
	private Double deductionAmount;
	
	@Column(name = "APPROVED_AMOUNT")
	private Double approvedAmount;
	
	@Column(name = "DEDUCTION_REASON")
	private String deductionReason;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.DATE)
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@Temporal(TemporalType.DATE)
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name = "BENEFITS_ID")
	private Long benefitsId;
	
	@Column(name = "COVER_ID")
	private Long covedId;
	
	@Column(name = "DELETED_FLAG")
	private String deletedFlag;
	
	@Column(name = "ELIGIBLE_AMOUNT")
	private Double eligibleAmt;
	
	@Column(name = "NET_AMOUNT")
	private Double netAmt;
	
	@Column(name = "AMOUNT_DOLLER")
	private Double amountDoller;
	
	@Column(name = "DEDUCTIBLE_NON_PAYBLES")
	private Double deductibleNonPayBles;
	
	@Column(name = "TOTAL_AMT_PAYABLE_DOLLAR")
	private Double totalAmtPayBleDollar;
	
	@Column(name = "TOTAL_AMT_INR")
	private Double totalAmtInr;
	
	@Column(name = "APPROVED_AMT_DOLLAR")
	private Double approvedAmountDollor;
	
	@Column(name = "AGREED_AMT_DOLLAR")
	private Double agreedAmtDollar;
	
	@Column(name = "DIFF_AMT_DOLLLAR")
	private Double diffAmtDollar;
		
	@Column(name = "EXPENSES_DOLLAR")
	private Double expenesesDollar;
	
	@Column(name = "NEGO_FEES_CLAIMED_DOLLAR")
	private Double negoFeesClaimedDollar;
	
	@Column(name = "NEGO_FEES_CLAIMED_INR")
	private Double negoFeesClaimedInr;
	
	@Column(name = "NEGO_FEE_CAPPING")
	private Double negoFeeCapping;
	
	@Column(name = "HANDLING_CHARGES_DOLLAR")
	private Double handlingChargesDollar;
	
	@Column(name = "TOTAL_EXPENCE_DOLLAR")
	private Double totalExpenceDollar;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CATEGORY")
	private MastersValue category;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CLAIM_KEY", nullable=false)
	private OMPClaim claimKey;
	
	@Column(name = "COPAY_AMOUNT")
	private Double copayAmount;
	
	@Column(name = "COPAY_PERCENTAGE")
	private Long copayPercentage;
	
	@Column(name = "APPR_AMT_AFTER_COPAY")
	private Double apprAmtAftrCopya;
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public OMPReimbursement getRodKey() {
		return rodKey;
	}

	public void setRodKey(OMPReimbursement rodKey) {
		this.rodKey = rodKey;
	}

	public Insured getInsuredKey() {
		return insuredKey;
	}

	public void setInsuredKey(Insured insuredKey) {
		this.insuredKey = insuredKey;
	}

	public String getWeeksDuration() {
		return weeksDuration;
	}

	public void setWeeksDuration(String weeksDuration) {
		this.weeksDuration = weeksDuration;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public Double getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(Double billAmount) {
		this.billAmount = billAmount;
	}

	public Double getDeductionAmount() {
		return deductionAmount;
	}

	public void setDeductionAmount(Double deductionAmount) {
		this.deductionAmount = deductionAmount;
	}

	public Double getApprovedAmount() {
		return approvedAmount;
	}

	public void setApprovedAmount(Double approvedAmount) {
		this.approvedAmount = approvedAmount;
	}

	public String getDeductionReason() {
		return deductionReason;
	}

	public void setDeductionReason(String deductionReason) {
		this.deductionReason = deductionReason;
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

	public Long getBenefitsId() {
		return benefitsId;
	}

	public void setBenefitsId(Long benefitsId) {
		this.benefitsId = benefitsId;
	}

	public Long getCovedId() {
		return covedId;
	}

	public void setCovedId(Long covedId) {
		this.covedId = covedId;
	}

	public String getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
	}

	public Double getEligibleAmt() {
		return eligibleAmt;
	}

	public void setEligibleAmt(Double eligibleAmt) {
		this.eligibleAmt = eligibleAmt;
	}

	public Double getNetAmt() {
		return netAmt;
	}

	public void setNetAmt(Double netAmt) {
		this.netAmt = netAmt;
	}

	public Double getAmountDoller() {
		return amountDoller;
	}

	public void setAmountDoller(Double amountDoller) {
		this.amountDoller = amountDoller;
	}

	public Double getDeductibleNonPayBles() {
		return deductibleNonPayBles;
	}

	public void setDeductibleNonPayBles(Double deductibleNonPayBles) {
		this.deductibleNonPayBles = deductibleNonPayBles;
	}

	public Double getTotalAmtPayBleDollar() {
		return totalAmtPayBleDollar;
	}

	public void setTotalAmtPayBleDollar(Double totalAmtPayBleDollar) {
		this.totalAmtPayBleDollar = totalAmtPayBleDollar;
	}

	public Double getTotalAmtInr() {
		return totalAmtInr;
	}

	public void setTotalAmtInr(Double totalAmtInr) {
		this.totalAmtInr = totalAmtInr;
	}

	public Double getApprovedAmountDollor() {
		return approvedAmountDollor;
	}

	public void setApprovedAmountDollor(Double approvedAmountDollor) {
		this.approvedAmountDollor = approvedAmountDollor;
	}

	public Double getAgreedAmtDollar() {
		return agreedAmtDollar;
	}

	public void setAgreedAmtDollar(Double agreedAmtDollar) {
		this.agreedAmtDollar = agreedAmtDollar;
	}

	public Double getDiffAmtDollar() {
		return diffAmtDollar;
	}

	public void setDiffAmtDollar(Double diffAmtDollar) {
		this.diffAmtDollar = diffAmtDollar;
	}

	public Double getExpenesesDollar() {
		return expenesesDollar;
	}

	public void setExpenesesDollar(Double expenesesDollar) {
		this.expenesesDollar = expenesesDollar;
	}

	public Double getNegoFeesClaimedDollar() {
		return negoFeesClaimedDollar;
	}

	public void setNegoFeesClaimedDollar(Double negoFeesClaimedDollar) {
		this.negoFeesClaimedDollar = negoFeesClaimedDollar;
	}

	public Double getNegoFeesClaimedInr() {
		return negoFeesClaimedInr;
	}

	public void setNegoFeesClaimedInr(Double negoFeesClaimedInr) {
		this.negoFeesClaimedInr = negoFeesClaimedInr;
	}

	public Double getNegoFeeCapping() {
		return negoFeeCapping;
	}

	public void setNegoFeeCapping(Double negoFeeCapping) {
		this.negoFeeCapping = negoFeeCapping;
	}

	public Double getHandlingChargesDollar() {
		return handlingChargesDollar;
	}

	public void setHandlingChargesDollar(Double handlingChargesDollar) {
		this.handlingChargesDollar = handlingChargesDollar;
	}

	public Double getTotalExpenceDollar() {
		return totalExpenceDollar;
	}

	public void setTotalExpenceDollar(Double totalExpenceDollar) {
		this.totalExpenceDollar = totalExpenceDollar;
	}

	public MastersValue getCategory() {
		return category;
	}

	public void setCategory(MastersValue category) {
		this.category = category;
	}

	public OMPClaim getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(OMPClaim claimKey) {
		this.claimKey = claimKey;
	}

	public Double getCopayAmount() {
		return copayAmount;
	}

	public void setCopayAmount(Double copayAmount) {
		this.copayAmount = copayAmount;
	}

	public Long getCopayPercentage() {
		return copayPercentage;
	}

	public void setCopayPercentage(Long copayPercentage) {
		this.copayPercentage = copayPercentage;
	}

	public Double getApprAmtAftrCopya() {
		return apprAmtAftrCopya;
	}

	public void setApprAmtAftrCopya(Double apprAmtAftrCopya) {
		this.apprAmtAftrCopya = apprAmtAftrCopya;
	}
	


}

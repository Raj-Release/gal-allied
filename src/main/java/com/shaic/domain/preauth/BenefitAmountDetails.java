package com.shaic.domain.preauth;

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
import com.shaic.domain.Claim;
import com.shaic.domain.Insured;
import com.shaic.domain.Status;

@Entity
@Table(name = "IMS_CLS_BENEFIT_AMOUNT_DETAILS")
@NamedQueries({
		@NamedQuery(name = "BenefitAmountDetails.findAll", query = "SELECT b FROM BenefitAmountDetails b where (b.deletedFlag is null or b.deletedFlag = 'N')"),
		@NamedQuery(name = "BenefitAmountDetails.findByKey", query = "SELECT b FROM BenefitAmountDetails b where b.key = :benefitKey and (b.deletedFlag is null or b.deletedFlag = 'N')"),
		@NamedQuery(name = "BenefitAmountDetails.findByPreauthKey", query = "SELECT o FROM BenefitAmountDetails o where o.transactionKey = :preauthKey and (o.deletedFlag is null or o.deletedFlag = 'N')"),
		@NamedQuery(name = "BenefitAmountDetails.findByClaimKey", query = "SELECT o FROM BenefitAmountDetails o where o.claim.key = :claimkey and (o.deletedFlag is null or o.deletedFlag = 'N')"),
		@NamedQuery(name = "BenefitAmountDetails.findByClaimKeyInDesc", query = "SELECT o FROM BenefitAmountDetails o where o.claim.key = :claimkey and (o.deletedFlag is null or o.deletedFlag = 'N') order by o.key desc"),
		@NamedQuery(name = "BenefitAmountDetails.findByPolicyNum", query = "Select p FROM BenefitAmountDetails p where p.claim.intimation.policy.policyNumber = :policyNum and (p.deletedFlag is null or p.deletedFlag = 'N')"),
		@NamedQuery(name = "BenefitAmountDetails.findByStatus", query = "Select o FROM BenefitAmountDetails o where o.status.key in(:status) and (o.deletedFlag is null or o.deletedFlag = 'N')"),
		@NamedQuery(name = "BenefitAmountDetails.findByTransactionKey", query = "SELECT o FROM BenefitAmountDetails o where o.transactionKey = :transactionKey and (o.deletedFlag is null or o.deletedFlag = 'N')"),
})
public class BenefitAmountDetails extends AbstractEntity {
		
	@Id
	@SequenceGenerator(name="IMS_BENEFIT_AMT_KEY_GENERATOR", sequenceName = "SEQ_BENEFIT_AMOUNT_DETAILS_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_BENEFIT_AMT_KEY_GENERATOR" ) 
	@Column(name="BENEFIT_AMOUNT_DETAILS_KEY", updatable=false)
	private Long key;
		
	@Column(name = "TRANSACTION_KEY", nullable = false)
	private Long transactionKey;
		
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CLAIM_KEY", nullable = false)
	private Claim claim;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "INSURED_KEY", nullable = false)
	private Insured insured;
	
	@Column(name="SEQUENCE_NUMBER")
	private String seqNo;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COMPONENT_ID", nullable = false)     
	private ClaimLimit benefit;
	
	@Column(name="BENEFIT_APPLICABLE")
	private String applicable;
	
	@Column(name="DELETED_FLAG")
	private String deletedFlag;
	
	@Column(name="CLAIMED_AMOUNT_BILLS")  
	private Double claimedAmt;
	
	@Column(name="NON_PAYABLE_AMOUNT")  
	private Double nonPayableAmt;
	
	@Column(name="NET_AMOUNT")  
	private Double netAmt;
	
	@Column(name="TOTAL_AMOUNT_POLICY")  
	private Double eligibleAmt;
	
	@Column(name="DEDUCTIBLE_AMOUNT")  
	private Double deductableAmt;
	
	@Column(name="PAYABLE_AMOUNT")  
	private Double payableAmt;
	
	@Column(name="ALREADY_PAID_TO_HOSP")  
	private Double alreadyPaidToHosp;
	
	@Column(name="NON_PAYABLE_REASON") 
	private String remarks;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")  
	private Date createdDate;
		
	@Column(name="CREATED_BY")  
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE")  
	private Date modifiedDate;
	
	@Column(name="MODIFIED_BY")  
	private String modifiedBy;
	
	@Column(name="ACTIVE_STATUS")     
	private long activeStatus;
	
	@Column(name="OFFICE_CODE")  
	private String officeCode;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATUS_ID", nullable = false)    
	private Status status;
	
	@Column(name="AMT_PAY_TO_INSURED")  
	private Double amountPayableToInsured;
	
	@Column(name="AMT_ALREADY_PAID")  
	private Double amtAlreadyPaid;
	
	@Column(name="BALANCE_PAYABLE")  
	private Double balancePayable;
	
	@Column(name="NO_OF_DAYS")  
	private Integer numberOfDays;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STAGE_ID", nullable = false)
	private Stage stage;

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

	public Insured getInsured() {
		return insured;
	}

	public void setInsured(Insured insured) {
		this.insured = insured;
	}

	public String getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}
	
	public ClaimLimit getBenefit() {
		return benefit;
	}

	public void setBenefit(ClaimLimit benefit) {
		this.benefit = benefit;
	}

	public String getApplicable() {
		return applicable;
	}

	public void setApplicable(String applicable) {
		this.applicable = applicable;
	}

	public Double getClaimedAmt() {
		return claimedAmt;
	}

	public void setClaimedAmt(Double claimedAmt) {
		this.claimedAmt = claimedAmt;
	}

	public Double getNonPayableAmt() {
		return nonPayableAmt;
	}

	public void setNonPayableAmt(Double nonPayableAmt) {
		this.nonPayableAmt = nonPayableAmt;
	}

	public Double getNetAmt() {
		return netAmt;
	}

	public void setNetAmt(Double netAmt) {
		this.netAmt = netAmt;
	}

	public Double getEligibleAmt() {
		return eligibleAmt;
	}

	public void setEligibleAmt(Double eligibleAmt) {
		this.eligibleAmt = eligibleAmt;
	}

	public Double getDeductableAmt() {
		return deductableAmt;
	}

	public void setDeductableAmt(Double deductableAmt) {
		this.deductableAmt = deductableAmt;
	}

	public Double getPayableAmt() {
		return payableAmt;
	}

	public void setPayableAmt(Double payableAmt) {
		this.payableAmt = payableAmt;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(long activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getOfficeCode() {
		return officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
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

	public Double getAmountPayableToInsured() {
		return amountPayableToInsured;
	}

	public void setAmountPayableToInsured(Double amountPayableToInsured) {
		this.amountPayableToInsured = amountPayableToInsured;
	}

	public Double getAmtAlreadyPaid() {
		return amtAlreadyPaid;
	}

	public void setAmtAlreadyPaid(Double amtAlreadyPaid) {
		this.amtAlreadyPaid = amtAlreadyPaid;
	}

	public Double getBalancePayable() {
		return balancePayable;
	}

	public void setBalancePayable(Double balancePayable) {
		this.balancePayable = balancePayable;
	}

	public Integer getNumberOfDays() {
		return numberOfDays;
	}

	public void setNumberOfDays(Integer numberOfDays) {
		this.numberOfDays = numberOfDays;
	}

	public Long getTransactionKey() {
		return transactionKey;
	}

	public void setTransactionKey(Long transactionKey) {
		this.transactionKey = transactionKey;
	}

	public Double getAlreadyPaidToHosp() {
		return alreadyPaidToHosp;
	}

	public void setAlreadyPaidToHosp(Double alreadyPaidToHosp) {
		this.alreadyPaidToHosp = alreadyPaidToHosp;
	}

	public String getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
	}	
}

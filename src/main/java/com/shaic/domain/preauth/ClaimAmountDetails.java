package com.shaic.domain.preauth;

import java.sql.Timestamp;

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

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.Status;

@Entity
@Table(name="IMS_CLS_CLAIM_AMOUNT_DETAILS")
@NamedQueries({
	@NamedQuery(name="ClaimAmountDetails.findAll", query="SELECT i FROM ClaimAmountDetails i where i.activeStatus = 1"),
	@NamedQuery(name = "ClaimAmountDetails.findByKey", query = "SELECT o FROM ClaimAmountDetails o where o.key = :claimAmountDetailsKey  and o.activeStatus = 1"),
	@NamedQuery(name="ClaimAmountDetails.findByPreauthKey", query="SELECT i FROM ClaimAmountDetails i where i.preauth.key = :preauthKey and i.activeStatus = 1")
})
public class ClaimAmountDetails extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6193438755097812581L;
	
	@Id
	@SequenceGenerator(name="IMS_CLS_CLAIM_AMOUNT_KEY_GENERATOR", sequenceName = "SEQ_CLAIM_AMOUNT_DETAILS_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_CLAIM_AMOUNT_KEY_GENERATOR" )
	@Column(name="CLAIM_AMOUNT_DETAILS_KEY")
	private Long key;
	
	@OneToOne
	@JoinColumn(name="CASHLESS_KEY")
	private Preauth preauth;
	
	@Column(name="INSURED_KEY")
	private Long insuredKey;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;
	
	@OneToOne
	@JoinColumn(name="STATUS_ID",nullable=false)
	private Status status;
	
	@OneToOne
	@JoinColumn(name="STAGE_ID",nullable=false)
	private Stage stage;
	
	@Column(name="OFFICE_CODE")
	private String officeCode;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column(name="COMPONENT_ID")
	private Long benefitId;
	
	@Column(name="NUMBER_OF_DAYS_BILLS")
	private Float totalBillingDays;
	
	@Column(name="PER_DAY_AMOUNT_BILLS")
	private Float billDayPayment;
	
	@Column(name="CLAIMED_AMOUNT_BILLS")
	private Float claimedBillAmount;
	
	@Column(name="DEDUCTIBLE_AMOUNT")
	private Float deductibleAmount;
	
	@Column(name="NET_AMOUNT")
	private Float netAmount;
	
	@Column(name="NUMBER_OF_DAYS_POLICY")
	private Float totalDaysForPolicy;
	
	@Column(name="PER_DAY_AMOUNT_POLICY")
	private Float policyDayPayment;
	
	@Column(name="TOTAL_AMOUNT_POLICY")
	private Float policyMaxAmount;
	
	@Column(name="PAYABLE_AMOUNT")
	private Float paybleAmount;
	
	@Column(name="NON_PAYABLE_AMOUNT")
	private Float nonPayableAmount;
	
	@Column(name="OVERRIDE_PACKAGE_DEDUCTION")
	private String overridePackageDeduction;
	
	@Column(name="RESTRICT_TO")
	private String restrictTo;
	
	@Column(name="PER_DAY_PAYABLE")
	private Float perDayPayable;
	
	@Column(name = "NON_PAYABLE_REASON")
	private String nonPayableReason;
	
	@Column(name = "BENEFIT_CHARGES_FLAG")
	private String benefitChargesFlag;
	
	@Column(name="SEQUENCE_NUMBER")
	private Long sequenceNumber;
	

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Preauth getPreauth() {
		return preauth;
	}

	public void setPreauth(Preauth preauth) {
		this.preauth = preauth;
	}

	public Long getInsuredKey() {
		return insuredKey;
	}

	public void setInsuredKey(Long insuredKey) {
		this.insuredKey = insuredKey;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
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

	public Long getBenefitId() {
		return benefitId;
	}

	public void setBenefitId(Long benefitId) {
		this.benefitId = benefitId;
	}

	public Float getTotalBillingDays() {
		return totalBillingDays;
	}

	public void setTotalBillingDays(Float totalBillingDays) {
		this.totalBillingDays = totalBillingDays;
	}

	public Float getBillDayPayment() {
		return billDayPayment;
	}

	public void setBillDayPayment(Float billDayPayment) {
		this.billDayPayment = billDayPayment;
	}

	public Float getClaimedBillAmount() {
		if(benefitId != null && benefitId.equals(21L) && claimedBillAmount != null){
			return claimedBillAmount > 0F ? claimedBillAmount*= -1 : claimedBillAmount;
		}else{
			return claimedBillAmount;	
		}
		
	}

	public void setClaimedBillAmount(Float claimedBillAmount) {
		if(benefitId != null && benefitId.equals(21L) && claimedBillAmount != null){
			this.claimedBillAmount = claimedBillAmount < 0F ? claimedBillAmount*= -1 : claimedBillAmount;
		}else{
			this.claimedBillAmount = claimedBillAmount;
		}
	}

	public Float getDeductibleAmount() {
		return deductibleAmount;
	}

	public void setDeductibleAmount(Float deductibleAmount) {
		this.deductibleAmount = deductibleAmount;
	}

	public Float getNetAmount() {
		if(benefitId != null && benefitId.equals(21L) && netAmount != null){
			return netAmount > 0F ? netAmount*= -1 : netAmount;
		}else{
			return netAmount;	
		}
	}

	public void setNetAmount(Float netAmount) {
		if(benefitId != null && benefitId.equals(21L) && netAmount != null){
			this.netAmount = netAmount < 0F ? netAmount*= -1 : netAmount;
		}else{
			this.netAmount = netAmount;
		}
	}

	public Float getTotalDaysForPolicy() {
		return totalDaysForPolicy;
	}

	public void setTotalDaysForPolicy(Float totalDaysForPolicy) {
		this.totalDaysForPolicy = totalDaysForPolicy;
	}

	public Float getPolicyDayPayment() {
		return policyDayPayment;
	}

	public void setPolicyDayPayment(Float policyDayPayment) {
		this.policyDayPayment = policyDayPayment;
	}

	public Float getPolicyMaxAmount() {
		return policyMaxAmount;
	}

	public void setPolicyMaxAmount(Float policyMaxAmount) {
		this.policyMaxAmount = policyMaxAmount;
	}

	public Float getPaybleAmount() {
		if(benefitId != null && benefitId.equals(21L) && paybleAmount != null){
			return paybleAmount > 0F ? paybleAmount*= -1 : paybleAmount;
		}else{
			return paybleAmount;	
		}
	}

	public void setPaybleAmount(Float paybleAmount) {
		if(benefitId != null && benefitId.equals(21L) && paybleAmount != null){
			this.paybleAmount = paybleAmount < 0F ? paybleAmount*= -1 : paybleAmount;
		}else{
			this.paybleAmount = paybleAmount;
		}
	}

	public Float getNonPayableAmount() {
		return nonPayableAmount;
	}

	public void setNonPayableAmount(Float nonPayableAmount) {
		this.nonPayableAmount = nonPayableAmount;
	}

	public String getOverridePackageDeduction() {
		return overridePackageDeduction;
	}

	public void setOverridePackageDeduction(String overridePackageDeduction) {
		this.overridePackageDeduction = overridePackageDeduction;
	}

	public String getRestrictTo() {
		return restrictTo;
	}

	public void setRestrictTo(String restrictTo) {
		this.restrictTo = restrictTo;
	}

	public Float getPerDayPayable() {
		return perDayPayable;
	}

	public void setPerDayPayable(Float perDayPayable) {
		this.perDayPayable = perDayPayable;
	}

	public String getNonPayableReason() {
		return nonPayableReason;
	}

	public void setNonPayableReason(String nonPayableReason) {
		this.nonPayableReason = nonPayableReason;
	}
	
	public String getBenefitChargesFlag() {
		return benefitChargesFlag;
	}

	public void setBenefitChargesFlag(String benefitChargesFlag) {
		this.benefitChargesFlag = benefitChargesFlag;
	}

	public Long getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
		
}

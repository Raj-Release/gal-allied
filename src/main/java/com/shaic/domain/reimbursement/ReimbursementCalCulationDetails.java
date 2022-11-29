package com.shaic.domain.reimbursement;

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
import javax.persistence.Transient;

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.Reimbursement;

@Entity
@Table(name="IMS_CLS_REIMBURSEMENT_CAL_DTLS")
@NamedQueries({
	@NamedQuery(name="ReimbursementCalCulationDetails.findAll", query="SELECT r FROM ReimbursementCalCulationDetails r"),
	@NamedQuery(name ="ReimbursementCalCulationDetails.findByKey",query="SELECT r FROM ReimbursementCalCulationDetails r WHERE r.key = :primaryKey"),
	@NamedQuery(name="ReimbursementCalCulationDetails.findByRodKey", query="SELECT r FROM ReimbursementCalCulationDetails r WHERE r.reimbursement is not null and r.reimbursement.key = :reimbursementKey order by r.key asc"),
	@NamedQuery(name="ReimbursementCalCulationDetails.findByRodAndBillClassificationKey", query="SELECT r FROM ReimbursementCalCulationDetails r WHERE r.reimbursement is not null and r.reimbursement.key = :reimbursementKey and r.billClassificationId = :billClassificationKey order by r.key asc"),
})
public class ReimbursementCalCulationDetails extends AbstractEntity {
	private static final long serialVersionUID = -6677441377745030280L;

	@Id
	@SequenceGenerator(name="IMS_CLS_REIMBURSEMENT_CAL_DTLS_KEY_GENERATOR", sequenceName = "SEQ_REIMBURSEMENT_CAL_DTLS_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_REIMBURSEMENT_CAL_DTLS_KEY_GENERATOR" )
	@Column(name="REIMBURSEMENT_CAL_DTLS_KEY")
	private Long key;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="REIMBURSEMENT_KEY", nullable=true)
	private Reimbursement reimbursement;
	
	@Column(name="BILL_CLASSIFICATION_ID")
	private Long billClassificationId;
	
	@Column(name="ELIGIBLE_AMOUNT")
	private Integer eligibleAmount;
	
	@Column(name="COPAY_AMOUNT")
	private Integer copayAmount;
	
	
	@Column(name="NET_PAYABLE_AMOUNT")
	private Integer netPayableAmount;
	
	@Column(name="MAXIMUM_PAYABLE_AMOUNT")
	private Integer maximumPayableAmount;
	
	@Column(name="NET_ELIGIBLE_PAYABLE_AMOUNT")
	private Integer netEligiblePayableAmount;
	
	@Column(name="CLAIM_RESTRICTION_AMOUNT")
	private Integer claimRestrictionAmount;
	
	@Column(name="CASHLESS_APPROVED_AMOUNT")
	private Integer cashlessApprovedAmount;
	
	@Column(name="PAYABLE_TO_HOSPITAL")
	private Integer payableToHospital;
	
	@Column(name="PAYABLE_TO_INSURED")
	private Integer payableToInsured;
	
	@Column(name="TDS_AMOUNT")
	private Integer tdsAmount;
	
	@Column(name="AFTER_TDS_PAYABLE_TO_HOSPITAL")
	private Integer payableToHospAftTDS;
	
	@Column(name="DEDUCTED_BALANCE_PREMIUM")
	private Integer deductedBalancePremium;
	
	@Column(name="AFTER_PREMIUM_PAYABLE_INSURED")
	private Integer payableInsuredAfterPremium;
	
	@Column(name="AVAILABLE_SI")
	private Integer balanceSIAftHosp;
	
	@Column(name="RESTRICTED_SI")
	private Integer restrictedSIAftHosp;
	
	@Column(name="HOS_DISCOUNT_AMOUNT")
	private Integer hospitalDiscount;
	
	@Column(name="HOS_DISCOUNT_AMT_AFTER")
	private Integer hospitalDiscountAmtAft;
	
	@Column(name="HOSP_TOT_CLM_AMT")
	private Integer tpaClaimedAmt;
	
//	@Column(name="TPA_NON_MED_AMT")
	@Transient
	private Integer tpaNonMedicalAmt;
	
	
//	@Column(name="TPA_SETL_AMT")
	@Transient
	private Integer tpaSettledAmt;
	
	
	@Column(name="TPA_BAL_AMT")
	@Transient
	private Integer tpaBalanceAmt;
	
	
	@Column(name="TPA_PAY_TO_INSURED")
	@Transient
	private Integer tpaPayableToInsured;
	
	@Column(name="PAYABLE_AMOUNT")
	@Transient
	private Integer tpaPayableAmt;
	
	@Column(name="PAID_AMT")
	private Integer amountAlreadyPaidAmt;
	
	@Column(name="BAL_PAY_AMT")
	private Integer balanceToBePaidAmt;
	
	//Added for consiliation
	@Column(name="ALREADY_PAID_AMT")
	private Integer alreadyPaid;


	@Override
	public Long getKey() {
		return key;
	}

	@Override
	public void setKey(Long key) {
		this.key = key;
	}

	public Reimbursement getReimbursement() {
		return reimbursement;
	}

	public void setReimbursement(Reimbursement reimbursement) {
		this.reimbursement = reimbursement;
	}

	public Long getBillClassificationId() {
		return billClassificationId;
	}

	public void setBillClassificationId(Long billClassificationId) {
		this.billClassificationId = billClassificationId;
	}

	public Integer getEligibleAmount() {
		return eligibleAmount;
	}

	public void setEligibleAmount(Integer eligibleAmount) {
		this.eligibleAmount = eligibleAmount;
	}

	public Integer getCopayAmount() {
		return copayAmount;
	}

	public void setCopayAmount(Integer copayAmount) {
		this.copayAmount = copayAmount;
	}

	public Integer getNetPayableAmount() {
		return netPayableAmount;
	}

	public void setNetPayableAmount(Integer netPayableAmount) {
		this.netPayableAmount = netPayableAmount;
	}

	public Integer getMaximumPayableAmount() {
		return maximumPayableAmount;
	}

	public void setMaximumPayableAmount(Integer maximumPayableAmount) {
		this.maximumPayableAmount = maximumPayableAmount;
	}

	public Integer getNetEligiblePayableAmount() {
		return netEligiblePayableAmount;
	}

	public void setNetEligiblePayableAmount(Integer netEligiblePayableAmount) {
		this.netEligiblePayableAmount = netEligiblePayableAmount;
	}

	public Integer getClaimRestrictionAmount() {
		return claimRestrictionAmount;
	}

	public void setClaimRestrictionAmount(Integer claimRestrictionAmount) {
		this.claimRestrictionAmount = claimRestrictionAmount;
	}

	public Integer getCashlessApprovedAmount() {
		return cashlessApprovedAmount;
	}

	public void setCashlessApprovedAmount(Integer cashlessApprovedAmount) {
		this.cashlessApprovedAmount = cashlessApprovedAmount;
	}

	public Integer getPayableToHospital() {
		return payableToHospital;
	}

	public void setPayableToHospital(Integer payableToHospital) {
		this.payableToHospital = payableToHospital;
	}

	public Integer getPayableToInsured() {
		return payableToInsured;
	}

	public void setPayableToInsured(Integer payableToInsured) {
		this.payableToInsured = payableToInsured;
	}

	public Integer getTdsAmount() {
		return tdsAmount;
	}

	public void setTdsAmount(Integer tdsAmount) {
		this.tdsAmount = tdsAmount;
	}

	public Integer getPayableToHospAftTDS() {
		return payableToHospAftTDS;
	}

	public void setPayableToHospAftTDS(Integer payableToHospAftTDS) {
		this.payableToHospAftTDS = payableToHospAftTDS;
	}

	public Integer getDeductedBalancePremium() {
		return deductedBalancePremium;
	}

	public Integer getBalanceSIAftHosp() {
		return balanceSIAftHosp;
	}

	public void setBalanceSIAftHosp(Integer balanceSIAftHosp) {
		this.balanceSIAftHosp = balanceSIAftHosp;
	}

	public Integer getRestrictedSIAftHosp() {
		return restrictedSIAftHosp;
	}

	public void setRestrictedSIAftHosp(Integer restrictedSIAftHosp) {
		this.restrictedSIAftHosp = restrictedSIAftHosp;
	}

	public void setDeductedBalancePremium(Integer deductedBalancePremium) {
		this.deductedBalancePremium = deductedBalancePremium;
	}

	public Integer getPayableInsuredAfterPremium() {
		return payableInsuredAfterPremium;
	}

	public void setPayableInsuredAfterPremium(Integer payableInsuredAfterPremium) {
		this.payableInsuredAfterPremium = payableInsuredAfterPremium;
	}

	public Integer getHospitalDiscount() {
		return hospitalDiscount;
	}

	public void setHospitalDiscount(Integer hospitalDiscount) {
		this.hospitalDiscount = hospitalDiscount;
	}

	public Integer getHospitalDiscountAmtAft() {
		return hospitalDiscountAmtAft;
	}

	public void setHospitalDiscountAmtAft(Integer hospitalDiscountAmtAft) {
		this.hospitalDiscountAmtAft = hospitalDiscountAmtAft;
	}

	public Integer getTpaClaimedAmt() {
		return tpaClaimedAmt;
	}

	public void setTpaClaimedAmt(Integer tpaClaimedAmt) {
		this.tpaClaimedAmt = tpaClaimedAmt;
	}

	public Integer getTpaNonMedicalAmt() {
		return tpaNonMedicalAmt;
	}

	public void setTpaNonMedicalAmt(Integer tpaNonMedicalAmt) {
		this.tpaNonMedicalAmt = tpaNonMedicalAmt;
	}

	public Integer getTpaSettledAmt() {
		return tpaSettledAmt;
	}

	public void setTpaSettledAmt(Integer tpaSettledAmt) {
		this.tpaSettledAmt = tpaSettledAmt;
	}

	public Integer getTpaBalanceAmt() {
		return tpaBalanceAmt;
	}

	public void setTpaBalanceAmt(Integer tpaBalanceAmt) {
		this.tpaBalanceAmt = tpaBalanceAmt;
	}

	public Integer getTpaPayableToInsured() {
		return tpaPayableToInsured;
	}

	public void setTpaPayableToInsured(Integer tpaPayableToInsured) {
		this.tpaPayableToInsured = tpaPayableToInsured;
	}

	public Integer getTpaPayableAmt() {
		return tpaPayableAmt;
	}

	public void setTpaPayableAmt(Integer tpaPayableAmt) {
		this.tpaPayableAmt = tpaPayableAmt;
	}

	public Integer getAmountAlreadyPaidAmt() {
		return amountAlreadyPaidAmt;
	}

	public void setAmountAlreadyPaidAmt(Integer amountAlreadyPaidAmt) {
		this.amountAlreadyPaidAmt = amountAlreadyPaidAmt;
	}

	public Integer getBalanceToBePaidAmt() {
		return balanceToBePaidAmt;
	}

	public void setBalanceToBePaidAmt(Integer balanceToBePaidAmt) {
		this.balanceToBePaidAmt = balanceToBePaidAmt;
	}

	public Integer getAlreadyPaid() {
		return alreadyPaid;
	}

	public void setAlreadyPaid(Integer alreadyPaid) {
		this.alreadyPaid = alreadyPaid;
	}
	
	
	
}

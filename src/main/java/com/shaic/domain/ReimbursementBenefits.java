/**
 * 
 */
package com.shaic.domain;

import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.shaic.arch.fields.dto.AbstractEntity;

/**
 * @author ntv.vijayar
 * The persistent class for the IMS_CLS_DOC_ACKNOWLEDGEMENT database table.
 * 
 */
@Entity
@Table(name = "IMS_CLS_REIMBURSEMENT_BENEFITS")
@NamedQueries({
	@NamedQuery(name = "ReimbursementBenefits.findAll", query = "SELECT i FROM ReimbursementBenefits i"),
	@NamedQuery(name = "ReimbursementBenefits.findByKey", query = "SELECT o FROM ReimbursementBenefits o where o.key = :key and o.deletedFlag is NOT NULL and o.deletedFlag = 'N'"),
	@NamedQuery(name = "ReimbursementBenefits.findByRodKey", query = "SELECT o FROM ReimbursementBenefits o where o.reimbursementKey.key = :rodKey and o.deletedFlag is NOT NULL and o.deletedFlag = 'N'" ),
	@NamedQuery(name = "ReimbursementBenefits.findByRodKeyAndBenefitsFlag", query = "SELECT o FROM ReimbursementBenefits o where o.reimbursementKey.key = :rodKey and o.benefitsFlag = :benefitsFlag and o.deletedFlag is NOT NULL and o.deletedFlag = 'N'"),
	@NamedQuery(name = "ReimbursementBenefits.findByRodKeyAndPHC", query = "SELECT o FROM ReimbursementBenefits o where o.reimbursementKey.key = :rodKey and o.productBenefitID = :phcId and o.deletedFlag is NOT NULL and o.deletedFlag = 'N'" ),
	
})

public class ReimbursementBenefits extends AbstractEntity {
	
	@Id
	@SequenceGenerator(name="IMS_REIMBURSEMENT_BENEFITS_KEY_GENERATOR", sequenceName = "SEQ_REIMBURSEMENT_BENEFITS_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_REIMBURSEMENT_BENEFITS_KEY_GENERATOR" ) 
	@Column(name="REIMBURSEMENT_BENEFITS_KEY")
	private Long key;
	
	@OneToOne
	@JoinColumn(name="REIMBURSEMENT_KEY", nullable=false)
	private Reimbursement reimbursementKey;
	
	@Column(name = "TREATMENT_FOR_PHYSIOTHERAPY")
	private String treatmentForPhysiotherapy;
	
	@Column(name = "GH_TREATMENT_FLAG")
	private String treatmentForGovtHosp;
	
	@Column(name = "NUMBER_OF_DAYS_BILLS")
	private Long numberOfDaysBills;
	
	@Column(name = "PER_DAY_AMOUNT_BILLS")
	private Double perDayAmountBills;
	
	@Column(name = "TOTAL_CLAIM_AMOUNT_BILLS")
	private Double totalClaimAmountBills;
	
	@Column(name = "NUMBER_OF_DAYS_ELIGIBILE")
	private Long numberOfDaysEligible;
	
	@Column(name = "NUMBER_OF_DAYS_PAYABLE")
	private Long numberOfDaysPayable;
	
	@Column(name = "PER_DAY_AMOUNT")
	private Double perDayAmount;
	
	@Column(name = "TOTAL_AMOUNT")
	private Double totalAmount;
	
	@Column(name = "COPAY_PERCENTAGE")
	private Double copayPercentage;
	
	@Column(name = "COPAY_AMOUNT")
	private Double copayAmount;
	
	@Column(name = "NET_AMOUNT")
	private Double netAmount;
	
	@Column(name = "LIMIT_FOR_POLICY")
	private Double limitForPolicy;
	
	@Column(name = "PAYABLE_AMOUNT")
	private Double payableAmount;
	
	@Column(name = "BENEFITS_FLAG")
	private String benefitsFlag;
	
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
	
	
	@Column(name = "DELETED_FLAG")
	private String deletedFlag;
	
	@Column(name="PHC_BENEFIT_ID")
	private Long productBenefitID;
	
	@Column(name = "DISALLOWANCE_REMARKS")
	private String disallowanceRemarks;
	
	@Column(name="TOTAL_NO_DAYS")
	private Long totalNoOfDays;
	

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	

	public String getTreatmentForPhysiotherapy() {
		return treatmentForPhysiotherapy;
	}

	public void setTreatmentForPhysiotherapy(String treatmentForPhysiotherapy) {
		this.treatmentForPhysiotherapy = treatmentForPhysiotherapy;
	}

	public Long getNumberOfDaysBills() {
		return numberOfDaysBills;
	}

	public void setNumberOfDaysBills(Long numberOfDaysBills) {
		this.numberOfDaysBills = numberOfDaysBills;
	}

	public Double getPerDayAmount() {
		return perDayAmount;
	}

	public void setPerDayAmount(Double perDayAmount) {
		this.perDayAmount = perDayAmount;
	}

	public Double getPerDayAmountBills() {
		return perDayAmountBills;
	}

	public void setPerDayAmountBills(Double perDayAmountBills) {
		this.perDayAmountBills = perDayAmountBills;
	}

	public Double getTotalClaimAmountBills() {
		return totalClaimAmountBills;
	}

	public void setTotalClaimAmountBills(Double totalClaimAmountBills) {
		this.totalClaimAmountBills = totalClaimAmountBills;
	}

	public Long getNumberOfDaysEligible() {
		return numberOfDaysEligible;
	}

	public void setNumberOfDaysEligible(Long numberOfDaysEligible) {
		this.numberOfDaysEligible = numberOfDaysEligible;
	}

	public Long getNumberOfDaysPayable() {
		return numberOfDaysPayable;
	}

	public void setNumberOfDaysPayable(Long numberOfDaysPayable) {
		this.numberOfDaysPayable = numberOfDaysPayable;
	}

	/*public Double getPreDayAmount() {
		return preDayAmount;
	}

	public void setPreDayAmount(Double preDayAmount) {
		this.preDayAmount = preDayAmount;
	}*/

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
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

	public Double getLimitForPolicy() {
		return limitForPolicy;
	}

	public void setLimitForPolicy(Double limitForPolicy) {
		this.limitForPolicy = limitForPolicy;
	}

	public Double getPayableAmount() {
		return payableAmount;
	}

	public void setPayableAmount(Double payableAmount) {
		this.payableAmount = payableAmount;
	}

	public String getBenefitsFlag() {
		return benefitsFlag;
	}

	public void setBenefitsFlag(String benefitsFlag) {
		this.benefitsFlag = benefitsFlag;
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

	public Reimbursement getReimbursementKey() {
		return reimbursementKey;
	}

	public void setReimbursementKey(Reimbursement reimbursementKey) {
		this.reimbursementKey = reimbursementKey;
	}

	public String getTreatmentForGovtHosp() {
		return treatmentForGovtHosp;
	}

	public void setTreatmentForGovtHosp(String treatmentForGovtHosp) {
		this.treatmentForGovtHosp = treatmentForGovtHosp;
	}

	public String getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
	}

	public Long getProductBenefitID() {
		return productBenefitID;
	}

	public void setProductBenefitID(Long productBenefitID) {
		this.productBenefitID = productBenefitID;
	}

	public String getDisallowanceRemarks() {
		return disallowanceRemarks;
	}

	public void setDisallowanceRemarks(String disallowanceRemarks) {
		this.disallowanceRemarks = disallowanceRemarks;
	}

	public Long getTotalNoOfDays() {
		return totalNoOfDays;
	}

	public void setTotalNoOfDays(Long totalNoOfDays) {
		this.totalNoOfDays = totalNoOfDays;
	}

	/*public String getBenefitFlag() {
		return benefitFlag;
	}

	public void setBenefitFlag(String benefitFlag) {
		this.benefitFlag = benefitFlag;
	}*/
	
	
	

}

/**
 * 
 */


/**
 * @author ntv.vijayar
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

@Entity
@Table(name="IMS_CLS_PA_BENEFIT_COVER")
@NamedQueries({
	@NamedQuery(name = "PABenefitsCovers.findByKey", query = "SELECT o FROM PABenefitsCovers o where o.key = :key"),
	@NamedQuery(name = "PABenefitsCovers.findByRodKey", query = "SELECT o FROM PABenefitsCovers o where o.rodKey = :rodKey and (o.deletedFlag is null or o.deletedFlag = 'N')")
})
public class PABenefitsCovers extends AbstractEntity{
	
	@Id
	@SequenceGenerator(name="IMS_CLS_PA_BENEFIT_COVER_KEY_GENERATOR", sequenceName = "SEQ_PA_BENEFITS_KEY" , allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_PA_BENEFIT_COVER_KEY_GENERATOR" )
	@Column(name="PA_BENEFITS_KEY")
	private Long key;

	@Column(name="ROD_KEY")
	private Long rodKey;
	
	@Column(name="INSURED_KEY")
	private Long insuredKey;
	
	@Column(name="WEEKS_DURATION")
	private String weeksDuration;
	
	@Column(name="BILL_NO")
	private String billNo;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "BILL_DATE")
	private Date billDate;
	
	@Column(name = "BILL_AMOUNT")
	private Double billAmount;
	
	@Column(name = "DEDUCTION_AMOUNT")
	private Double deductionAmount;
	
	@Column(name = "APPROVED_AMOUNT")
	private Double approvedAmount;
	
	@Column(name = "DEDUCTION_REASON")
	private String deductionReason;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="BENEFITS_ID", nullable=false)
	private MastersValue benefitsId;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="COVER_ID", nullable=false)
	private MasPAClaimBenefitsCover coverId;
	
	@Column(name = "DELETED_FLAG")
	private String deletedFlag;
	
	
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
	
	@Column(name = "ELIGIBLE_AMOUNT")
	private Double eligibleAmount;

	@Column(name = "NET_AMOUNT")
	private Double netAmount;
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
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

	public MastersValue getBenefitsId() {
		return benefitsId;
	}

	public void setBenefitsId(MastersValue benefitsId) {
		this.benefitsId = benefitsId;
	}



	public String getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
	}

	public Double getEligibleAmount() {
		return eligibleAmount;
	}

	public void setEligibleAmount(Double eligibleAmount) {
		this.eligibleAmount = eligibleAmount;
	}

	public MasPAClaimBenefitsCover getCoverId() {
		return coverId;
	}

	public void setCoverId(MasPAClaimBenefitsCover coverId) {
		this.coverId = coverId;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	
	
	
	
	
}

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
@Table(name="IMS_CLS_PA_ADD_COVER")
@NamedQueries({
	@NamedQuery(name = "PAAdditionalCovers.findByKey", query = "SELECT o FROM PAAdditionalCovers o where o.key = :key"),
	@NamedQuery(name = "PAAdditionalCovers.findByAckKey", query = "SELECT o FROM PAAdditionalCovers o where o.acknowledgementKey = :ackDocKey and (o.deletedFlag is null or o.deletedFlag = 'N')"),
	@NamedQuery(name = "PAAdditionalCovers.findByRodKey", query = "SELECT o FROM PAAdditionalCovers o where o.rodKey = :rodKey and (o.deletedFlag is null or o.deletedFlag = 'N')"),
	@NamedQuery(name = "PAAdditionalCovers.findByRodKeyAndCoverId", query = "SELECT o FROM PAAdditionalCovers o where o.rodKey = :rodKey and o.coverId = :coverId and (o.deletedFlag is null or o.deletedFlag = 'N')"),
	@NamedQuery(name = "PAAdditionalCovers.findByClaimKey", query = "SELECT o FROM PAAdditionalCovers o where o.claimKey = :claimKey"),
	@NamedQuery(name = "PAAdditionalCovers.findByClaimKeyAndDeletedFlag", query = "SELECT o FROM PAAdditionalCovers o where o.claimKey = :claimKey and (o.deletedFlag is null or o.deletedFlag = 'N')"),
})
public class PAAdditionalCovers extends AbstractEntity{
	
	@Id
	@SequenceGenerator(name="IMS_CLS_PA_ADD_COVER_KEY_GENERATOR", sequenceName = "SEQ_PA_ADD_COVER_KEY" , allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_PA_ADD_COVER_KEY_GENERATOR" )
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

	@Column(name = "CLAIMED_CHILDREN_NO")
	private Integer claimedChildrenNo;
	
	@Column(name = "ALLOWABLE_CHILDREN_NO")
	private Integer allowableChildrenNo;
	
	@Column(name = "BILL_NO")
	private String billNo;
	
	@Column(name = "BILL_DATE")
	private Date billDate;

	@Column(name = "BILL_AMOUNT")
	private Double billAmt;
	
	@Column(name = "DEDUCTION_AMOUNT")
	private Double deductionAmt;
	
	@Column(name = "NET_AMOUNT")
	private Double netAmt;
	
	@Column(name = "APPROVED_AMOUNT")
	private Double approvedAmt;
	
	@Column(name = "DEDUCTION_REASON")
	private String deductionReason;
	
	@Column(name = "CLAIM_KEY")
	private Long claimKey;
	@Column(name = "PROVISION_AMOUNT")
	private Double provisionAmount;
	
	@Column(name = "COV_ELIGIBLE_FLAG")
	private String covEligibleFlag;
	
	@Column(name = "UN_NAMED_KEY")
	private Long unNamedKey;
	
	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}

	public Integer getClaimedChildrenNo() {
		return claimedChildrenNo;
	}

	public void setClaimedChildrenNo(Integer claimedChildrenNo) {
		this.claimedChildrenNo = claimedChildrenNo;
	}

	public Integer getAllowableChildrenNo() {
		return allowableChildrenNo;
	}

	public void setAllowableChildrenNo(Integer allowableChildrenNo) {
		this.allowableChildrenNo = allowableChildrenNo;
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

	public Double getBillAmt() {
		return billAmt;
	}

	public void setBillAmt(Double billAmt) {
		this.billAmt = billAmt;
	}

	public Double getDeductionAmt() {
		return deductionAmt;
	}

	public void setDeductionAmt(Double deductionAmt) {
		this.deductionAmt = deductionAmt;
	}

	public Double getNetAmt() {
		return netAmt;
	}

	public void setNetAmt(Double netAmt) {
		this.netAmt = netAmt;
	}

	public Double getApprovedAmt() {
		return approvedAmt;
	}

	public void setApprovedAmt(Double approvedAmt) {
		this.approvedAmt = approvedAmt;
	}

	public String getDeductionReason() {
		return deductionReason;
	}

	public void setDeductionReason(String deductionReason) {
		this.deductionReason = deductionReason;
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

	public Long getUnNamedKey() {
		return unNamedKey;
	}

	public void setUnNamedKey(Long unNamedKey) {
		this.unNamedKey = unNamedKey;
	}

	
}

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

import com.shaic.arch.fields.dto.AbstractEntity;


@Entity
@Table(name="IMS_CLS_LEGAL_ADV_NOTICE")
@NamedQueries({
@NamedQuery(name="LegalAdvocate.findAll", query="SELECT l FROM LegalAdvocate l"),
@NamedQuery(name ="LegalAdvocate.findByKey",query="SELECT l FROM LegalAdvocate l WHERE l.key = :primaryKey and l.activeStatus=1"),
@NamedQuery(name ="LegalAdvocate.findByIntimationNumber",query="SELECT l FROM LegalAdvocate l WHERE l.intimationNumber = :intimationNumber and l.activeStatus=1"),
@NamedQuery(name ="LegalAdvocate.findByIntimationNumberAndType",query="SELECT l FROM LegalAdvocate l WHERE l.intimationNumber = :intimationNumber and l.activeStatus=1 and l.legalType = :legalType")})
public class LegalAdvocate extends AbstractEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3799807063257179012L;

	@Id
	@SequenceGenerator(name="IMS_CLS_LEGAL_ADV_NOTICE_KEY_GENERATOR", sequenceName = "SEQ_ADV_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_LEGAL_ADV_NOTICE_KEY_GENERATOR" ) 
	@Column(name="ADV_KEY", updatable=false)
	private Long key;
	
	@Column(name="LEGAL_KEY", updatable=false)
	private Long legalKey;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CLAIM_KEY", nullable = true)
	private Claim claimKey;

	@Column(name="INTIMATION_NUMBER")
	private String intimationNumber;
	
	@Column(name="POLICY_NUMBER")
	private String policyNumber;
	
	@Column(name="PRODUCT_NAME")
	private String productName;
	
	@Column(name="INSURED_NAME")
	private String insuredName;
	
	@Column(name="FINANCIAL_YEAR")
	private String financialYear;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REPUDIATION_ID", nullable = true)
	private MastersValue repudiationId;
	
	@Column(name="PROVISION_AMOUNT")
	private	Double	provisionAmount;
	
	@Column(name="LEGAL_TYPE")
	private String legalType;
	
/*	@Column(name="DCDRF_REMARKS")
	private	String	dcdrfRemarks;
*/	
	/*@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ZONE_ID", nullable = true)
	private	TmpCPUCode	zoneId;*/
	
	/*@Column(name="CC_NO")
	private	String	ccNo;*/
	

	@Column(name="ADVOCATE_NAME")
	private	String	advocateName;
	
/*	@Column(name="ADVOCATE_FEE")
	private	Double	advocateFee;*/
	
	/*@Column(name="PART_PAYMENT_FLAG")
	private	Boolean	partPaymentFlag;
	
	@Column(name="FULL_PAYMENT_FLAG")
	private	Boolean	fullPaymentFlag;
	
	@Column(name="PAID_AMOUNT")
	private	Double	paidAmount;
	
	@Column(name="DD_NAME")
	private	String	ddName;*/
	
	@Column(name="ADV_NOTICE_DATE")
	private	Date	advnoticeDate;
	
	@Column(name="NOTICE_REC_DATE")
	private	Date	noticerecDate;
	
	@Column(name="LIMIT_DATE")
	private	Date	limitDate;
	
	@Column(name="ADV_STATUS")
	private	String	advstatus;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MOVED_ID", nullable = true)
	private	MastersValue	movedId;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PENDING_LEVEL_ID", nullable = true)
	private	MastersValue	pendingLevelId;
	
	@Column(name="REJECT_FLAG")
	private	Boolean	rejectFlag;

	@Column(name="REPLIED_DATE")
	private	Date	repliedDate;
	
	@Column(name="REJECT_REASON")
	private	String	rejectReason;
	
	@Column(name="SETTLE_FLAG")
	private	Boolean	settleFlag;
	
	@Column(name="SETTLE_DATE")
	private	Date	settleDate;
	
	@Column(name="SETTLE_AMT")
	private	Double	settleAmt;
	
	@Column(name="SETTLE_REASON")
	private	String	settleReason;
	
	@Column(name="LEGAL_LOCK_FLAG")
	private Boolean legalLockFlag;
	
	@Column(name="LEGAL_COMPLETED_FLAG")
	private Boolean legalCompletedFlag;
	
	@Column(name="COMPLAINCE_DATE")
	private Date complainceDate;
	
	@Column(name="CREATED_BY")
	private	String	createdBy;
	
	@Column(name="CREATED_DATE")
	private	Date	createdDate;
	
	@Column(name="MODIFIED_BY")
	private	String	modifiedBy;
	
	@Column(name="MODIFIED_DATE")
	private	Date	modifiedDate;
	
	@Column(name="ACTIVE_STATUS")
	private	Long	activeStatus;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RECEIVED_FROM", nullable = true)
	private MastersValue receievedFrom;
	
	@Column(name="DIAGNOSIS")
	private String diagnosis;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getLegalKey() {
		return legalKey;
	}

	public void setLegalKey(Long legalKey) {
		this.legalKey = legalKey;
	}

	public Claim getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Claim claimKey) {
		this.claimKey = claimKey;
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

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public String getFinancialYear() {
		return financialYear;
	}

	public void setFinancialYear(String financialYear) {
		this.financialYear = financialYear;
	}

	public MastersValue getRepudiationId() {
		return repudiationId;
	}

	public void setRepudiationId(MastersValue repudiationId) {
		this.repudiationId = repudiationId;
	}

	public Double getProvisionAmount() {
		return provisionAmount;
	}

	public void setProvisionAmount(Double provisionAmount) {
		this.provisionAmount = provisionAmount;
	}

	public String getLegalType() {
		return legalType;
	}

	public void setLegalType(String legalType) {
		this.legalType = legalType;
	}

	/*public String getDcdrfRemarks() {
		return dcdrfRemarks;
	}

	public void setDcdrfRemarks(String dcdrfRemarks) {
		this.dcdrfRemarks = dcdrfRemarks;
	}

	public TmpCPUCode getZoneId() {
		return zoneId;
	}

	public void setZoneId(TmpCPUCode zoneId) {
		this.zoneId = zoneId;
	}

	public String getCcNo() {
		return ccNo;
	}

	public void setCcNo(String ccNo) {
		this.ccNo = ccNo;
	}

	public String getAdvocateName() {
		return advocateName;
	}

	public void setAdvocateName(String advocateName) {
		this.advocateName = advocateName;
	}

	public Double getAdvocateFee() {
		return advocateFee;
	}

	public void setAdvocateFee(Double advocateFee) {
		this.advocateFee = advocateFee;
	}

	public Boolean getPartPaymentFlag() {
		return partPaymentFlag;
	}

	public void setPartPaymentFlag(Boolean partPaymentFlag) {
		this.partPaymentFlag = partPaymentFlag;
	}

	public Boolean getFullPaymentFlag() {
		return fullPaymentFlag;
	}

	public void setFullPaymentFlag(Boolean fullPaymentFlag) {
		this.fullPaymentFlag = fullPaymentFlag;
	}

	public Double getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(Double paidAmount) {
		this.paidAmount = paidAmount;
	}

	public String getDdName() {
		return ddName;
	}

	public void setDdName(String ddName) {
		this.ddName = ddName;
	}*/

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

	public Date getAdvnoticeDate() {
		return advnoticeDate;
	}

	public void setAdvnoticeDate(Date advnoticeDate) {
		this.advnoticeDate = advnoticeDate;
	}

	public Date getNoticerecDate() {
		return noticerecDate;
	}

	public void setNoticerecDate(Date noticerecDate) {
		this.noticerecDate = noticerecDate;
	}

	public Date getLimitDate() {
		return limitDate;
	}

	public void setLimitDate(Date limitDate) {
		this.limitDate = limitDate;
	}

	public String getAdvstatus() {
		return advstatus;
	}

	public void setAdvstatus(String advstatus) {
		this.advstatus = advstatus;
	}

	public MastersValue getMovedId() {
		return movedId;
	}

	public void setMovedId(MastersValue movedId) {
		this.movedId = movedId;
	}

	public MastersValue getPendingLevelId() {
		return pendingLevelId;
	}

	public void setPendingLevelId(MastersValue pendingLevelId) {
		this.pendingLevelId = pendingLevelId;
	}

	public Boolean getRejectFlag() {
		return rejectFlag;
	}

	public void setRejectFlag(Boolean rejectFlag) {
		this.rejectFlag = rejectFlag;
	}

	public Date getRepliedDate() {
		return repliedDate;
	}

	public void setRepliedDate(Date repliedDate) {
		this.repliedDate = repliedDate;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public Boolean getSettleFlag() {
		return settleFlag;
	}

	public void setSettleFlag(Boolean settleFlag) {
		this.settleFlag = settleFlag;
	}

	public Date getSettleDate() {
		return settleDate;
	}

	public void setSettleDate(Date settleDate) {
		this.settleDate = settleDate;
	}

	public Double getSettleAmt() {
		return settleAmt;
	}

	public void setSettleAmt(Double settleAmt) {
		this.settleAmt = settleAmt;
	}

	public String getSettleReason() {
		return settleReason;
	}

	public void setSettleReason(String settleReason) {
		this.settleReason = settleReason;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Boolean getLegalLockFlag() {
		return legalLockFlag;
	}

	public void setLegalLockFlag(Boolean legalLockFlag) {
		this.legalLockFlag = legalLockFlag;
	}

	public Boolean getLegalCompletedFlag() {
		return legalCompletedFlag;
	}

	public void setLegalCompletedFlag(Boolean legalCompletedFlag) {
		this.legalCompletedFlag = legalCompletedFlag;
	}

	public Date getComplainceDate() {
		return complainceDate;
	}

	public void setComplainceDate(Date complainceDate) {
		this.complainceDate = complainceDate;
	}


	public String getAdvocateName() {
		return advocateName;
	}

	public void setAdvocateName(String advocateName) {
		this.advocateName = advocateName;
	}

	public MastersValue getReceievedFrom() {
		return receievedFrom;
	}

	public void setReceievedFrom(MastersValue receievedFrom) {
		this.receievedFrom = receievedFrom;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	
}

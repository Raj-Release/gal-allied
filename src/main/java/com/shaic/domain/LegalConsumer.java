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
@Table(name="IMS_CLS_CLAIM_LEGAL")
@NamedQueries({
@NamedQuery(name="LegalConsumer.findAll", query="SELECT l FROM LegalConsumer l"),
@NamedQuery(name ="LegalConsumer.findByKey",query="SELECT l FROM LegalConsumer l WHERE l.key = :primaryKey and l.activeStatus=1"),
@NamedQuery(name ="LegalConsumer.findByIntimationNumber",query="SELECT l FROM LegalConsumer l WHERE l.intimationNumber = :intimationNumber and l.activeStatus=1"),
@NamedQuery(name ="LegalConsumer.findByIntimationNumberAndType",query="SELECT l FROM LegalConsumer l WHERE  l.intimationNumber = :intimationNumber and l.activeStatus=1 and  l.legalType = :legalType")})
public class LegalConsumer extends AbstractEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 712546437971907030L;

	@Id
	@SequenceGenerator(name="IMS_CLS_CLAIM_LEGAL_KEY_GENERATOR", sequenceName = "SEQ_LEGAL_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_CLAIM_LEGAL_KEY_GENERATOR" ) 
	@Column(name="LEGAL_KEY", updatable=false)
	private Long key;
	
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
	private Double provisionAmount;
	
	@Column(name="LEGAL_TYPE")
	private String legalType;
	
	@Column(name="DCDRF_REMARKS")
	private String dcdrfRemarks;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ZONE_ID", nullable = true)
	private TmpCPUCode zoneId;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATE_ID", nullable = true)
	private State stateId;
	
	@Column(name="CC_NO")
	private String ccNo;
	
	@Column(name="ADVOCATE_NAME")
	private String advocateName;
	
	@Column(name="COMP_CLM_AMT")
	private Double compClmAmt;
	
	@Column(name="COMPLAINT_DATE")
	private Date complaintDate;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORDER_ID", nullable = true)
	private MastersValue orderId;
	
	@Column(name="RECEIPT_DATE")
	private Date receiptDate;
	
	@Column(name="ORDER_DATE")
	private Date orderDate;
	
	@Column(name="CASE_COMPLAINT_DATE")
	private Date complainceDate;
	
	@Column(name="ORDER_STATUS_ID")
	private Long orderStatusId;
	
	@Column(name="WON_CASE_REASON")
	private String wonCaseReason;
	
	@Column(name="REC_UPD_DATE")
	private Date recUpdDate;
	
	@Column(name="REC_UPD_REMARKS")
	private String recUpdRemarks;
	
	@Column(name="SETTLEMENT_DATE")
	private Date settlementDate;
	
	@Column(name="AWARD_FLAG")
	private Boolean awardFlag;
	
	@Column(name="AWARD_AGAINST_AMT")
	private Double awardAgainstAmt;
	
	@Column(name="AWARD_REASON_ID")
	private String awardReasonId;
	
	@Column(name="COMPENSATION")
	private String compensation;
	
	@Column(name="LITIGATION_COST")
	private Double litigationCost;
	
	@Column(name="INTEREST_HISTORY")
	private String interestHistory;
	
	@Column(name="STATE_COMM_FLAG")
	private Boolean stateCommFlag;
	
	@Column(name="NATIONAL_COMM_FLAG")
	private Boolean nationalCommFlag;
	
	@Column(name="SUPREME_COURT_FLAG")
	private Boolean supremeCourtFlag;
	
	@Column(name="MANDATORY_DEP_FLAG")
	private Boolean mandatoryDepFlag;
	
	@Column(name="MAN_DEP_AMOUNT")
	private Double manDepAmount;
	
	@Column(name="MAN_DEP_DATE")
	private Date manDepDate;
	
	@Column(name="MAN_PAYEE_NAME")
	private String manPayeeName;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEP_AMT_STS_IS", nullable = true)
	private MastersValue depAmtStsIs;
	
	@Column(name="GRND_APPEAL_FLAG")
	private Boolean grndAppealFlag;
	
	@Column(name="STAY_STATUS_FLAG")
	private Boolean stayStatusFlag;
	
	@Column(name="CONDITIONAL_FLAG")
	private Boolean conditionalFlag;
	
	@Column(name="CON_DEP_AMT")
	private Double conDepAmt;
	
	@Column(name="CON_DEP_DATE")
	private Date conDepDate;
	
	@Column(name="CON_PAYEE_NAME")
	private String conPayeeName;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CON_AMT_STS_ID", nullable = true)
	private MastersValue conAmtStsId;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CASE_STATUS_ID", nullable = true)
	private MastersValue caseStatusId;
	
	@Column(name="CASE_UPD_REMARKS")
	private String caseUpdRemarks;
	
	@Column(name="FRSH_PREV_HEARING_DT")
	private Date frshPrevHearingDt;
	
	@Column(name="NEXT_HEARING_DATE")
	private Date nextHearingDate;
	
	@Column(name="WON_CASE")
	private Boolean wonCase;
	
	@Column(name="LEGAL_LOCK_FLAG")
	private Boolean legalLockFlag;
	
	@Column(name="LIMIT_DATE")
	private Date limitofPeriod;
	
	@Column(name="OOC_SETTLE_FLAG")
	private Boolean oocSettleFlag;
	
	@Column(name="OOC_REASON")
	private String oocReason;
	
	@Column(name="SETTLED_DATE")
	private Date settledDate;
	
	@Column(name="SETTLED_AMOUNT")
	private Double settledAmount;
	
	@Column(name="OFFERED_AMOUNT")
	private Double offeredAmount;
	
	@Column(name="SAVED_AMOUNT")
	private Double savedAmount;
	
	@Column(name="CONSENT_LETTER_FLAG")
	private Boolean consentLetterFlag;
	
	@Column(name="APPEAL_FLAG")
	private	Boolean	appealFlag;
	
	@Column(name="STATISFY_FLAG")
	private	Boolean	statisfyFlag;
	
	@Column(name="UPD_CASE_FLAG")
	private	Boolean	updcaseFlag;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name="ACTIVE_STATUS")
	private	Long	activeStatus;
	
	@Column(name="AWD_ADVOCATE_NAME")
	private String awdAdvocateName; 

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RECEIVED_FROM", nullable = true)
	private MastersValue receievedFrom;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MOVED_TO", nullable = true)
	private MastersValue movedTO;
	
	@Column(name="DIAGNOSIS")
	private String diagnosis;
	
	@Column(name="VAKALATH_FIELD_DATE")
	private Date vakalathFieldDate;
	
	@Column(name="APPEAL_PREFFERED_BY")
	private	Boolean	appealPrefferedBy;
	
	@Column(name="FA_NUMBER")
	private	String	faNumber;
	
	@Column(name="DATE_HEARING")
	private Date dateHearing;
	
	@Column(name="REPLY_FIELD")
	private	Boolean	replyField;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATUS_CASE_ID", nullable = true)
	private MastersValue statusCaseId;
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
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

	public String getDcdrfRemarks() {
		return dcdrfRemarks;
	}

	public void setDcdrfRemarks(String dcdrfRemarks) {
		this.dcdrfRemarks = dcdrfRemarks;
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

	public Double getCompClmAmt() {
		return compClmAmt;
	}

	public void setCompClmAmt(Double compClmAmt) {
		this.compClmAmt = compClmAmt;
	}

	public Date getComplaintDate() {
		return complaintDate;
	}

	public void setComplaintDate(Date complaintDate) {
		this.complaintDate = complaintDate;
	}


	public Date getReceiptDate() {
		return receiptDate;
	}

	public void setReceiptDate(Date receiptDate) {
		this.receiptDate = receiptDate;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public Date getComplainceDate() {
		return complainceDate;
	}

	public void setComplainceDate(Date complainceDate) {
		this.complainceDate = complainceDate;
	}

	public Long getOrderStatusId() {
		return orderStatusId;
	}

	public void setOrderStatusId(Long orderStatusId) {
		this.orderStatusId = orderStatusId;
	}

	public String getWonCaseReason() {
		return wonCaseReason;
	}

	public void setWonCaseReason(String wonCaseReason) {
		this.wonCaseReason = wonCaseReason;
	}

	public Date getRecUpdDate() {
		return recUpdDate;
	}

	public void setRecUpdDate(Date recUpdDate) {
		this.recUpdDate = recUpdDate;
	}

	public String getRecUpdRemarks() {
		return recUpdRemarks;
	}

	public void setRecUpdRemarks(String recUpdRemarks) {
		this.recUpdRemarks = recUpdRemarks;
	}

	public Date getSettlementDate() {
		return settlementDate;
	}

	public void setSettlementDate(Date settlementDate) {
		this.settlementDate = settlementDate;
	}

	public Boolean getAwardFlag() {
		return awardFlag;
	}

	public void setAwardFlag(Boolean awardFlag) {
		this.awardFlag = awardFlag;
	}

	public Double getAwardAgainstAmt() {
		return awardAgainstAmt;
	}

	public void setAwardAgainstAmt(Double awardAgainstAmt) {
		this.awardAgainstAmt = awardAgainstAmt;
	}

	public String getAwardReasonId() {
		return awardReasonId;
	}

	public void setAwardReasonId(String awardReasonId) {
		this.awardReasonId = awardReasonId;
	}

	public String getCompensation() {
		return compensation;
	}

	public void setCompensation(String compensation) {
		this.compensation = compensation;
	}

	public Double getLitigationCost() {
		return litigationCost;
	}

	public void setLitigationCost(Double litigationCost) {
		this.litigationCost = litigationCost;
	}

	public String getInterestHistory() {
		return interestHistory;
	}

	public void setInterestHistory(String interestHistory) {
		this.interestHistory = interestHistory;
	}

	public Boolean getStateCommFlag() {
		return stateCommFlag;
	}

	public void setStateCommFlag(Boolean stateCommFlag) {
		this.stateCommFlag = stateCommFlag;
	}

	public Boolean getNationalCommFlag() {
		return nationalCommFlag;
	}

	public void setNationalCommFlag(Boolean nationalCommFlag) {
		this.nationalCommFlag = nationalCommFlag;
	}

	public Boolean getSupremeCourtFlag() {
		return supremeCourtFlag;
	}

	public void setSupremeCourtFlag(Boolean supremeCourtFlag) {
		this.supremeCourtFlag = supremeCourtFlag;
	}

	public Boolean getMandatoryDepFlag() {
		return mandatoryDepFlag;
	}

	public void setMandatoryDepFlag(Boolean mandatoryDepFlag) {
		this.mandatoryDepFlag = mandatoryDepFlag;
	}

	public Double getManDepAmount() {
		return manDepAmount;
	}

	public void setManDepAmount(Double manDepAmount) {
		this.manDepAmount = manDepAmount;
	}

	public Date getManDepDate() {
		return manDepDate;
	}

	public void setManDepDate(Date manDepDate) {
		this.manDepDate = manDepDate;
	}

	public String getManPayeeName() {
		return manPayeeName;
	}

	public void setManPayeeName(String manPayeeName) {
		this.manPayeeName = manPayeeName;
	}

	public MastersValue getDepAmtStsIs() {
		return depAmtStsIs;
	}

	public void setDepAmtStsIs(MastersValue depAmtStsIs) {
		this.depAmtStsIs = depAmtStsIs;
	}

	public Boolean getGrndAppealFlag() {
		return grndAppealFlag;
	}

	public void setGrndAppealFlag(Boolean grndAppealFlag) {
		this.grndAppealFlag = grndAppealFlag;
	}

	public Boolean getStayStatusFlag() {
		return stayStatusFlag;
	}

	public void setStayStatusFlag(Boolean stayStatusFlag) {
		this.stayStatusFlag = stayStatusFlag;
	}

	public Boolean getConditionalFlag() {
		return conditionalFlag;
	}

	public void setConditionalFlag(Boolean conditionalFlag) {
		this.conditionalFlag = conditionalFlag;
	}

	public Double getConDepAmt() {
		return conDepAmt;
	}

	public void setConDepAmt(Double conDepAmt) {
		this.conDepAmt = conDepAmt;
	}

	public Date getConDepDate() {
		return conDepDate;
	}

	public void setConDepDate(Date conDepDate) {
		this.conDepDate = conDepDate;
	}

	public String getConPayeeName() {
		return conPayeeName;
	}

	public void setConPayeeName(String conPayeeName) {
		this.conPayeeName = conPayeeName;
	}

	public MastersValue getConAmtStsId() {
		return conAmtStsId;
	}

	public void setConAmtStsId(MastersValue conAmtStsId) {
		this.conAmtStsId = conAmtStsId;
	}

	public MastersValue getCaseStatusId() {
		return caseStatusId;
	}

	public void setCaseStatusId(MastersValue caseStatusId) {
		this.caseStatusId = caseStatusId;
	}

	public String getCaseUpdRemarks() {
		return caseUpdRemarks;
	}

	public void setCaseUpdRemarks(String caseUpdRemarks) {
		this.caseUpdRemarks = caseUpdRemarks;
	}

	public Date getFrshPrevHearingDt() {
		return frshPrevHearingDt;
	}

	public void setFrshPrevHearingDt(Date frshPrevHearingDt) {
		this.frshPrevHearingDt = frshPrevHearingDt;
	}

	public Date getNextHearingDate() {
		return nextHearingDate;
	}

	public void setNextHearingDate(Date nextHearingDate) {
		this.nextHearingDate = nextHearingDate;
	}

	public Boolean getOocSettleFlag() {
		return oocSettleFlag;
	}

	public void setOocSettleFlag(Boolean oocSettleFlag) {
		this.oocSettleFlag = oocSettleFlag;
	}

	public MastersValue getRepudiationId() {
		return repudiationId;
	}

	public void setRepudiationId(MastersValue repudiationId) {
		this.repudiationId = repudiationId;
	}

	public TmpCPUCode getZoneId() {
		return zoneId;
	}

	public void setZoneId(TmpCPUCode zoneId) {
		this.zoneId = zoneId;
	}

	public State getStateId() {
		return stateId;
	}

	public void setStateId(State stateId) {
		this.stateId = stateId;
	}

	public MastersValue getOrderId() {
		return orderId;
	}

	public void setOrderId(MastersValue orderId) {
		this.orderId = orderId;
	}

	public String getOocReason() {
		return oocReason;
	}

	public void setOocReason(String oocReason) {
		this.oocReason = oocReason;
	}

	public Date getSettledDate() {
		return settledDate;
	}

	public void setSettledDate(Date settledDate) {
		this.settledDate = settledDate;
	}

	public Double getSettledAmount() {
		return settledAmount;
	}

	public void setSettledAmount(Double settledAmount) {
		this.settledAmount = settledAmount;
	}

	public Double getOfferedAmount() {
		return offeredAmount;
	}

	public void setOfferedAmount(Double offeredAmount) {
		this.offeredAmount = offeredAmount;
	}

	public Double getSavedAmount() {
		return savedAmount;
	}

	public void setSavedAmount(Double savedAmount) {
		this.savedAmount = savedAmount;
	}

	public Boolean getConsentLetterFlag() {
		return consentLetterFlag;
	}

	public void setConsentLetterFlag(Boolean consentLetterFlag) {
		this.consentLetterFlag = consentLetterFlag;
	}

	public Boolean getAppealFlag() {
		return appealFlag;
	}

	public void setAppealFlag(Boolean appealFlag) {
		this.appealFlag = appealFlag;
	}

	public Boolean getStatisfyFlag() {
		return statisfyFlag;
	}

	public void setStatisfyFlag(Boolean statisfyFlag) {
		this.statisfyFlag = statisfyFlag;
	}

	public Boolean getUpdcaseFlag() {
		return updcaseFlag;
	}

	public void setUpdcaseFlag(Boolean updcaseFlag) {
		this.updcaseFlag = updcaseFlag;
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


	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Boolean getWonCase() {
		return wonCase;
	}

	public void setWonCase(Boolean wonCase) {
		this.wonCase = wonCase;
	}

	public Boolean getLegalLockFlag() {
		return legalLockFlag;
	}

	public void setLegalLockFlag(Boolean legalLockFlag) {
		this.legalLockFlag = legalLockFlag;
	}

	public Date getLimitofPeriod() {
		return limitofPeriod;
	}

	public void setLimitofPeriod(Date limitofPeriod) {
		this.limitofPeriod = limitofPeriod;
	}

	public MastersValue getReceievedFrom() {
		return receievedFrom;
	}

	public void setReceievedFrom(MastersValue receievedFrom) {
		this.receievedFrom = receievedFrom;
	}

	public MastersValue getMovedTO() {
		return movedTO;
	}

	public void setMovedTO(MastersValue movedTO) {
		this.movedTO = movedTO;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public Date getVakalathFieldDate() {
		return vakalathFieldDate;
	}

	public void setVakalathFieldDate(Date vakalathFieldDate) {
		this.vakalathFieldDate = vakalathFieldDate;
	}

	public Boolean getAppealPrefferedBy() {
		return appealPrefferedBy;
	}

	public void setAppealPrefferedBy(Boolean appealPrefferedBy) {
		this.appealPrefferedBy = appealPrefferedBy;
	}

	public String getFaNumber() {
		return faNumber;
	}

	public void setFaNumber(String faNumber) {
		this.faNumber = faNumber;
	}

	public Date getDateHearing() {
		return dateHearing;
	}

	public void setDateHearing(Date dateHearing) {
		this.dateHearing = dateHearing;
	}

	public Boolean getReplyField() {
		return replyField;
	}

	public void setReplyField(Boolean replyField) {
		this.replyField = replyField;
	}

	public MastersValue getStatusCaseId() {
		return statusCaseId;
	}

	public void setStatusCaseId(MastersValue statusCaseId) {
		this.statusCaseId = statusCaseId;
	}

	public String getAwdAdvocateName() {
		return awdAdvocateName;
	}

	public void setAwdAdvocateName(String awdAdvocateName) {
		this.awdAdvocateName = awdAdvocateName;
	}

	
	
}

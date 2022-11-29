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
@Table(name="IMS_CLS_LEGAL_OMBUDSMAN")
@NamedQueries({
@NamedQuery(name="LegalOmbudsman.findAll", query="SELECT l FROM LegalOmbudsman l"),
@NamedQuery(name ="LegalOmbudsman.findByKey",query="SELECT l FROM LegalOmbudsman l WHERE l.key = :primaryKey and l.activeStatus=1"),
@NamedQuery(name ="LegalOmbudsman.findByIntimationNumber",query="SELECT l FROM LegalOmbudsman l WHERE l.intimationNumber = :intimationNumber and l.activeStatus=1"),
@NamedQuery(name ="LegalOmbudsman.findByIntimationNumberAndType",query="SELECT l FROM LegalOmbudsman l WHERE l.intimationNumber = :intimationNumber and l.activeStatus=1 and l.legalType = :legalType")})
public class LegalOmbudsman extends AbstractEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3799807063257179012L;

	@Id
	@SequenceGenerator(name="IMS_CLS_LEGAL_OMBUDSMAN_KEY_GENERATOR", sequenceName = "SEQ_OMBUD_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_LEGAL_OMBUDSMAN_KEY_GENERATOR" ) 
	@Column(name="OMBUD_KEY", updatable=false)
	private Long key;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OMBUD_CPU", nullable = true)
	private MasOmbudsman ombudCpu;
	
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
	
	@Column(name="REMARKS")
	private	String	remarks;
	
	@Column(name="COMPLAINT_NUMBER")
	private	String	complaintNumber;
	
	@Column(name="COMPLAINT_RECEIPT_DATE")
	private	Date complaintReceiptDate;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ADD_DAYS_ID", nullable = true)
	private	MastersValue addDaysId;
	
	@Column(name="OMBUD_STIPULATED_DATE")
	private	Date ombudStipulatedDate;
	
	@Column(name="REFER_TO_MEDICAL")
	private	String	referToMedical;
	
	@Column(name="RECEIPT_MEDICAL_OPN_DATE")
	private	Date receiptMedicalOpnDate;
	
	@Column(name="OMBUD_LOCK_FLAG")
	private Boolean ombudLockFlag;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OMBUD_DECISION_ID", nullable = true)
	private	MastersValue ombudDecisionId;
	
	@Column(name="CONTEST_HRG_DATE")
	private	Date contestHrgdate;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "HEARING_STATUS_ID", nullable = true)
	private	MastersValue hearingStatusId;

	@Column(name="SELF_CONTAINED_FLAG")
	private Boolean selfContainedFlag;
	
	@Column(name="SELF_CONT_PREPARATION_DT")
	private	Date selfContPreparationDt;
	
	@Column(name="SELF_CONT_SUBMISSION_DT")
	private	Date selfContSubmissionDt;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "AWARD_STATUS_ID", nullable = true)
	private	MastersValue awardStatusId;
	
	@Column(name="CASE_WON")
	private	String caseWon;
	
	@Column(name="CLOSURE_DATE")
	private	Date closureDate;
	
	@Column(name="CASE_WON_REMARKS")
	private	String caseWonRemarks;
	
	@Column(name="AWARD_RECEIPT_DATE")
	private	Date awardReceiptDate;
	
	@Column(name="LAST_DT_STATISFY_AWD")
	private	Date lastDtStatisfactionAwd;
	
	@Column(name="EXGRATIA_AWARD")
	private Boolean exgratiaAward;
	
	@Column(name="EXGRATIA_AWARD_DT")
	private	Date exgratiaAwardDt;
	
	@Column(name="EXGRATIA_AWARD_AMT")
	private	Double exgratiaAwardAmt;
	
	@Column(name="REASON_EXGRATIA")
	private	String exgratiaReason;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATUS_CASE_ID", nullable = true)
	private	MastersValue statusCaseId;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PENDING_STATUS", nullable = true)
	private	MastersValue pendingStatus;
	
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
	
	@Column(name="WON_INTEREST")
	private	String	wonInterest;
	
	@Column(name="CASE_LOST")
	private	String	caseLost;
	
	@Column(name="LOST_CLOSURE_DATE")
	private	Date	lostClosureDate;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LOST_RSN_AWD_ID", nullable = true)
	private	MastersValue	lostRsnAwdId;
	
	@Column(name="LOST_CASE_REASON")
	private	String	lostCaseReason;
	
	@Column(name="LOST_INTEREST")
	private	String	lostInterest;
	
	@Column(name="LOST_AWD_REC_DATE")
	private	Date lostAwdRecDate;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LOST_ADD_DAYS_ID", nullable = true)
	private	MastersValue lostAddDaysId;
	
	@Column(name="LOST_LAST_DT_STATISFY_AWD")
	private Date lostLastDateStafisfyAwd;
	
	@Column(name="ADJOURNMENT_NXT_HRG_DT")
	private	Date adjournmenNxtHearingDate;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COMPROMISE_SETTLE_RSN_ID", nullable = true)
	private	MastersValue compromiseSettleRsnId;
	
	@Column(name="COMPROMISE_AMOUNT")
	private	Double compromiseAmount;
	
	@Column(name="COMPROMISE_SETTLE_REMARK")
	private	String compromiseSettleRemark;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RECEIVED_FROM", nullable = true)
	private MastersValue receievedFrom;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MOVED_TO", nullable = true)
	private MastersValue movedTO;
	
	@Column(name="DIAGNOSIS")
	private String diagnosis;
	
	@Column(name="GRIEVANCE_REMARKS")
	private String grievanceRemarks;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GRIEVANCE_OUTCOME", nullable = true)
	private MastersValue grievanceOutcome;
	
	@Column(name="AMT_REFUND_BY_INSURED")
	private Double amtRefundByInsured;
	
	@Column(name="RECON_CANCEL_POLICY_DATE")
	private	Date reconCancelPolicyDate;
	
	@Column(name="RECON_CANCEL_POLICY")
	private String reconCancelPolicy;
	
	@Column(name="PRORATA_PREMIUM_REF_DATE")
	private	Date proratePremiumRejDate;
	
	@Column(name="REFUND_PRORATA_PREMIUM")
	private Double refundProRataPermium;
	
	@Column(name="CANCELLATION_POLICY")
	private String cancellationPolicy;
	
	@Column(name="RECON_ACCEPTANCE_AMOUNT")
	private Double reconAcceptanceAmount;
	
	@Column(name="RECON_ACCEPT_DATE")
	private	Date reconAcceptDate;
	
	@Column(name="RECONSIDERATION")
	private String reconsideration;
	
	@Column(name="GRIEVANCE_INITIATED_DATE")
	private	Date grievanceInitiatedDate;
	
	@Column(name="RECONSIDERATION_RECEIPT")
	private	Date reconsiderationReceipt;
	
	@Column(name="CLAIM_RECONSIDERATION")
	private String claimReconsideration;
	
	@Column(name="AWARD_OMB_CONTESTED")
	private String awardOmbContested;
	
	@Column(name="PP_DATE_HEARING")
	private Date ppDateHearing;
	
	@Column(name="RECON_REJECT_DATE")
	private Date reconRejectDate;
	
	@Column(name="GRIEVANCE_REQ_INITIATED")
	private String grievanceReqIntiated;
	
	@Column(name="ISSUE_REJECTION_DATE")
	private Date issueRejectionDate;
	
	@Column(name="AWARD_RESERVED_DTLS")
	private String awardReservedDetail;
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public MasOmbudsman getOmbudCpu() {
		return ombudCpu;
	}

	public void setOmbudCpu(MasOmbudsman ombudCpu) {
		this.ombudCpu = ombudCpu;
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getComplaintNumber() {
		return complaintNumber;
	}

	public void setComplaintNumber(String complaintNumber) {
		this.complaintNumber = complaintNumber;
	}

	public Date getComplaintReceiptDate() {
		return complaintReceiptDate;
	}

	public void setComplaintReceiptDate(Date complaintReceiptDate) {
		this.complaintReceiptDate = complaintReceiptDate;
	}

	public MastersValue getAddDaysId() {
		return addDaysId;
	}

	public void setAddDaysId(MastersValue addDaysId) {
		this.addDaysId = addDaysId;
	}

	public Date getOmbudStipulatedDate() {
		return ombudStipulatedDate;
	}

	public void setOmbudStipulatedDate(Date ombudStipulatedDate) {
		this.ombudStipulatedDate = ombudStipulatedDate;
	}

	public String getReferToMedical() {
		return referToMedical;
	}

	public void setReferToMedical(String referToMedical) {
		this.referToMedical = referToMedical;
	}

	public Date getReceiptMedicalOpnDate() {
		return receiptMedicalOpnDate;
	}

	public void setReceiptMedicalOpnDate(Date receiptMedicalOpnDate) {
		this.receiptMedicalOpnDate = receiptMedicalOpnDate;
	}

	public Boolean getOmbudLockFlag() {
		return ombudLockFlag;
	}

	public void setOmbudLockFlag(Boolean ombudLockFlag) {
		this.ombudLockFlag = ombudLockFlag;
	}

	public MastersValue getOmbudDecisionId() {
		return ombudDecisionId;
	}

	public void setOmbudDecisionId(MastersValue ombudDecisionId) {
		this.ombudDecisionId = ombudDecisionId;
	}

	public Date getContestHrgdate() {
		return contestHrgdate;
	}

	public void setContestHrgdate(Date contestHrgdate) {
		this.contestHrgdate = contestHrgdate;
	}

	public MastersValue getHearingStatusId() {
		return hearingStatusId;
	}

	public void setHearingStatusId(MastersValue hearingStatusId) {
		this.hearingStatusId = hearingStatusId;
	}

	public Boolean getSelfContainedFlag() {
		return selfContainedFlag;
	}

	public void setSelfContainedFlag(Boolean selfContainedFlag) {
		this.selfContainedFlag = selfContainedFlag;
	}

	public Date getSelfContPreparationDt() {
		return selfContPreparationDt;
	}

	public void setSelfContPreparationDt(Date selfContPreparationDt) {
		this.selfContPreparationDt = selfContPreparationDt;
	}

	public Date getSelfContSubmissionDt() {
		return selfContSubmissionDt;
	}

	public void setSelfContSubmissionDt(Date selfContSubmissionDt) {
		this.selfContSubmissionDt = selfContSubmissionDt;
	}

	public MastersValue getAwardStatusId() {
		return awardStatusId;
	}

	public void setAwardStatusId(MastersValue awardStatusId) {
		this.awardStatusId = awardStatusId;
	}

	public String getCaseWon() {
		return caseWon;
	}

	public void setCaseWon(String caseWon) {
		this.caseWon = caseWon;
	}

	public Date getClosureDate() {
		return closureDate;
	}

	public void setClosureDate(Date closureDate) {
		this.closureDate = closureDate;
	}

	public String getCaseWonRemarks() {
		return caseWonRemarks;
	}

	public void setCaseWonRemarks(String caseWonRemarks) {
		this.caseWonRemarks = caseWonRemarks;
	}

	public Date getAwardReceiptDate() {
		return awardReceiptDate;
	}

	public void setAwardReceiptDate(Date awardReceiptDate) {
		this.awardReceiptDate = awardReceiptDate;
	}

	public Date getLastDtStatisfactionAwd() {
		return lastDtStatisfactionAwd;
	}

	public void setLastDtStatisfactionAwd(Date lastDtStatisfactionAwd) {
		this.lastDtStatisfactionAwd = lastDtStatisfactionAwd;
	}

	public Boolean getExgratiaAward() {
		return exgratiaAward;
	}

	public void setExgratiaAward(Boolean exgratiaAward) {
		this.exgratiaAward = exgratiaAward;
	}

	public Date getExgratiaAwardDt() {
		return exgratiaAwardDt;
	}

	public void setExgratiaAwardDt(Date exgratiaAwardDt) {
		this.exgratiaAwardDt = exgratiaAwardDt;
	}

	public Double getExgratiaAwardAmt() {
		return exgratiaAwardAmt;
	}

	public void setExgratiaAwardAmt(Double exgratiaAwardAmt) {
		this.exgratiaAwardAmt = exgratiaAwardAmt;
	}

	public String getExgratiaReason() {
		return exgratiaReason;
	}

	public void setExgratiaReason(String exgratiaReason) {
		this.exgratiaReason = exgratiaReason;
	}

	public MastersValue getStatusCaseId() {
		return statusCaseId;
	}

	public void setStatusCaseId(MastersValue statusCaseId) {
		this.statusCaseId = statusCaseId;
	}

	public MastersValue getPendingStatus() {
		return pendingStatus;
	}

	public void setPendingStatus(MastersValue pendingStatus) {
		this.pendingStatus = pendingStatus;
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

	public String getWonInterest() {
		return wonInterest;
	}

	public void setWonInterest(String wonInterest) {
		this.wonInterest = wonInterest;
	}

	public String getCaseLost() {
		return caseLost;
	}

	public void setCaseLost(String caseLost) {
		this.caseLost = caseLost;
	}

	public Date getLostClosureDate() {
		return lostClosureDate;
	}

	public void setLostClosureDate(Date lostClosureDate) {
		this.lostClosureDate = lostClosureDate;
	}

	public MastersValue getLostRsnAwdId() {
		return lostRsnAwdId;
	}

	public void setLostRsnAwdId(MastersValue lostRsnAwdId) {
		this.lostRsnAwdId = lostRsnAwdId;
	}

	public String getLostCaseReason() {
		return lostCaseReason;
	}

	public void setLostCaseReason(String lostCaseReason) {
		this.lostCaseReason = lostCaseReason;
	}

	public String getLostInterest() {
		return lostInterest;
	}

	public void setLostInterest(String lostInterest) {
		this.lostInterest = lostInterest;
	}

	public Date getLostAwdRecDate() {
		return lostAwdRecDate;
	}

	public void setLostAwdRecDate(Date lostAwdRecDate) {
		this.lostAwdRecDate = lostAwdRecDate;
	}

	public MastersValue getLostAddDaysId() {
		return lostAddDaysId;
	}

	public void setLostAddDaysId(MastersValue lostAddDaysId) {
		this.lostAddDaysId = lostAddDaysId;
	}

	public Date getLostLastDateStafisfyAwd() {
		return lostLastDateStafisfyAwd;
	}

	public void setLostLastDateStafisfyAwd(Date lostLastDateStafisfyAwd) {
		this.lostLastDateStafisfyAwd = lostLastDateStafisfyAwd;
	}

	public Date getAdjournmenNxtHearingDate() {
		return adjournmenNxtHearingDate;
	}

	public void setAdjournmenNxtHearingDate(Date adjournmenNxtHearingDate) {
		this.adjournmenNxtHearingDate = adjournmenNxtHearingDate;
	}

	public MastersValue getCompromiseSettleRsnId() {
		return compromiseSettleRsnId;
	}

	public void setCompromiseSettleRsnId(MastersValue compromiseSettleRsnId) {
		this.compromiseSettleRsnId = compromiseSettleRsnId;
	}

	public Double getCompromiseAmount() {
		return compromiseAmount;
	}

	public void setCompromiseAmount(Double compromiseAmount) {
		this.compromiseAmount = compromiseAmount;
	}

	public String getCompromiseSettleRemark() {
		return compromiseSettleRemark;
	}

	public void setCompromiseSettleRemark(String compromiseSettleRemark) {
		this.compromiseSettleRemark = compromiseSettleRemark;
	}

	public MastersValue getMovedTO() {
		return movedTO;
	}

	public void setMovedTO(MastersValue movedTO) {
		this.movedTO = movedTO;
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

	public String getGrievanceRemarks() {
		return grievanceRemarks;
	}

	public void setGrievanceRemarks(String grievanceRemarks) {
		this.grievanceRemarks = grievanceRemarks;
	}

	public MastersValue getGrievanceOutcome() {
		return grievanceOutcome;
	}

	public void setGrievanceOutcome(MastersValue grievanceOutcome) {
		this.grievanceOutcome = grievanceOutcome;
	}

	public Double getAmtRefundByInsured() {
		return amtRefundByInsured;
	}

	public void setAmtRefundByInsured(Double amtRefundByInsured) {
		this.amtRefundByInsured = amtRefundByInsured;
	}

	public Date getReconCancelPolicyDate() {
		return reconCancelPolicyDate;
	}

	public void setReconCancelPolicyDate(Date reconCancelPolicyDate) {
		this.reconCancelPolicyDate = reconCancelPolicyDate;
	}

	public String getReconCancelPolicy() {
		return reconCancelPolicy;
	}

	public void setReconCancelPolicy(String reconCancelPolicy) {
		this.reconCancelPolicy = reconCancelPolicy;
	}

	public Date getProratePremiumRejDate() {
		return proratePremiumRejDate;
	}

	public void setProratePremiumRejDate(Date proratePremiumRejDate) {
		this.proratePremiumRejDate = proratePremiumRejDate;
	}

	public Double getRefundProRataPermium() {
		return refundProRataPermium;
	}

	public void setRefundProRataPermium(Double refundProRataPermium) {
		this.refundProRataPermium = refundProRataPermium;
	}

	public String getCancellationPolicy() {
		return cancellationPolicy;
	}

	public void setCancellationPolicy(String cancellationPolicy) {
		this.cancellationPolicy = cancellationPolicy;
	}

	public Double getReconAcceptanceAmount() {
		return reconAcceptanceAmount;
	}

	public void setReconAcceptanceAmount(Double reconAcceptanceAmount) {
		this.reconAcceptanceAmount = reconAcceptanceAmount;
	}

	public Date getReconAcceptDate() {
		return reconAcceptDate;
	}

	public void setReconAcceptDate(Date reconAcceptDate) {
		this.reconAcceptDate = reconAcceptDate;
	}

	public String getReconsideration() {
		return reconsideration;
	}

	public void setReconsideration(String reconsideration) {
		this.reconsideration = reconsideration;
	}

	public Date getGrievanceInitiatedDate() {
		return grievanceInitiatedDate;
	}

	public void setGrievanceInitiatedDate(Date grievanceInitiatedDate) {
		this.grievanceInitiatedDate = grievanceInitiatedDate;
	}

	public Date getReconsiderationReceipt() {
		return reconsiderationReceipt;
	}

	public void setReconsiderationReceipt(Date reconsiderationReceipt) {
		this.reconsiderationReceipt = reconsiderationReceipt;
	}

	public String getClaimReconsideration() {
		return claimReconsideration;
	}

	public void setClaimReconsideration(String claimReconsideration) {
		this.claimReconsideration = claimReconsideration;
	}

	public String getAwardOmbContested() {
		return awardOmbContested;
	}

	public void setAwardOmbContested(String awardOmbContested) {
		this.awardOmbContested = awardOmbContested;
	}

	public Date getPpDateHearing() {
		return ppDateHearing;
	}

	public void setPpDateHearing(Date ppDateHearing) {
		this.ppDateHearing = ppDateHearing;
	}

	public String getGrievanceReqIntiated() {
		return grievanceReqIntiated;
	}

	public void setGrievanceReqIntiated(String grievanceReqIntiated) {
		this.grievanceReqIntiated = grievanceReqIntiated;
	}

	public Date getReconRejectDate() {
		return reconRejectDate;
	}

	public void setReconRejectDate(Date reconRejectDate) {
		this.reconRejectDate = reconRejectDate;
	}

	public Date getIssueRejectionDate() {
		return issueRejectionDate;
	}

	public void setIssueRejectionDate(Date issueRejectionDate) {
		this.issueRejectionDate = issueRejectionDate;
	}

	public String getAwardReservedDetail() {
		return awardReservedDetail;
	}

	public void setAwardReservedDetail(String awardReservedDetail) {
		this.awardReservedDetail = awardReservedDetail;
	}

	
}

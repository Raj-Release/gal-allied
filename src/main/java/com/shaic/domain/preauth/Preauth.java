
package com.shaic.domain.preauth;

import java.sql.Timestamp;
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
import javax.persistence.Transient;

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.Claim;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.Intimation;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Policy;
import com.shaic.domain.Status;

/**
 * The persistent class for the IMS_CLS_PRE_AUTH_T database table.
 * 
 */
@Entity
@Table(name = "IMS_CLS_CASHLESS")
@NamedQueries({
		@NamedQuery(name = "Preauth.findAll", query = "SELECT i FROM Preauth i"),
		@NamedQuery(name = "Preauth.findByKey", query = "SELECT o FROM Preauth o where o.key = :preauthKey order by o.preauthId"),
		@NamedQuery(name = "Preauth.findByClaimKey", query = "SELECT o FROM Preauth o where o.claim.key = :claimkey and o.status is not null and (o.status.key <> 167 and o.status.key <> 166) order by o.key"),
		@NamedQuery(name = "Preauth.findByClaimKeyInDesc", query = "SELECT o FROM Preauth o where o.claim.key = :claimkey and o.status is not null and (o.status.key <> 167 and o.status.key <> 166) order by o.key desc"),
		@NamedQuery(name = "Preauth.findByIntimationKey", query = "SELECT o FROM Preauth o where o.intimation.key = :intimationKey and o.status is not null and (o.status.key <> 167 and o.status.key <> 166) order by o.preauthId"),
		@NamedQuery(name = "Preauth.findPreAuthIdInDescendingOrder", query = "SELECT o FROM Preauth o where o.intimation.key = :intimationKey and o.status is not null and (o.status.key <> 167 and o.status.key <> 166) order by o.preauthId desc"),
		@NamedQuery(name = "Preauth.getFinalEnhAmtByClaim", query = "SELECT o.totalApprovalAmount FROM Preauth o where o.claim.key = :claimKey and o.enhancementType = 'F'"),
		@NamedQuery(name = "Preauth.findByClaimKeyInDescendingOrder", query = "SELECT o FROM Preauth o where o.claim.key = :claimkey and o.status is not null and (o.status.key <> 167 and o.status.key <> 166) order by o.preauthId desc"),
		@NamedQuery(name = "Preauth.getApprovedPreauthInDescendingOrder", query = "SELECT o FROM Preauth o where(:fromDate is null or  o.createdDate >= :fromDate) and (:endDate is null or o.createdDate <= :endDate ) order by o.preauthId desc"),
		@NamedQuery(name = "Preauth.findByPolicyNum", query = "Select p FROM Preauth p where p.claim.intimation.policy.policyNumber = :policyNum"),
		@NamedQuery(name = "Preauth.findMaxPreauthIdByClaimKey", query = "Select max(p.preauthId) FROM Preauth p where p.claim.key <> :claimKey"),
		@NamedQuery(name = "Preauth.findLatestPreauthByClaim", query = "SELECT o FROM Preauth o where o.claim.key = :claimkey and o.status is not null and (o.status.key <> 167 and o.status.key <> 166) order by o.key desc"),
		@NamedQuery(name = "Preauth.findLatestPreauthByClaimAndRejectedKey", query = "SELECT o FROM Preauth o where o.claim.key = :claimkey and o.status is not null and o.status.key not in (:preauthKey) and (o.status.key <> 167 and o.status.key <> 166) order by o.key desc"),
		@NamedQuery(name = "Preauth.findByLatestIntimationKey", query = "SELECT o From Preauth o where o.intimation.key = :intimationKey order by o.intimation.key desc"),
		@NamedQuery(name = "Preauth.findCashlessEstimation", query = "SELECT count(o.key) From Preauth o where o.createdDate >= :fromDate and o.createdDate <= :endDate and (:cpuKey is null or o.intimation.cpuCode.key = :cpuKey)"),
		@NamedQuery(name = "Preauth.findPreauthSummaryByStageNStaus", query = "SELECT count(o.key) From Preauth o where o.stage.key = :preauthStageKey and o.status.key = :approvedStatusKey and o.createdDate >= :fromDate and o.createdDate <= :endDate and (o.intimation.cpuCode.key is null or o.intimation.cpuCode.key = :cpuKey)" ),
		@NamedQuery(name = "Preauth.findEnhancementApprovedSummary",    query = "SELECT count(o.key) From Preauth o where o.stage.key = :enhanceStageKey and o.status.key = :approvedStatusKey and (o.enhancementType = 'I' or o.enhancementType = 'F') and o.createdDate >= :fromDate and o.createdDate <= :endDate and (o.intimation.cpuCode.key is null or o.intimation.cpuCode.key = :cpuKey)"),
		@NamedQuery(name = "Preauth.findByStatus", query = "Select o FROM Preauth o where o.status.key in(:status)"),
		@NamedQuery(name = "Preauth.findByToDate", query = "Select o FROM Preauth o where o.createdDate <= :createdDate ORDER BY  o.createdDate DESC"),
		@NamedQuery(name = "Preauth.findByQueryRaisedStatus", query = "Select o FROM Preauth o where o.status.key IN (:statusList) and o.claim.key = :claimKey"),
		@NamedQuery(name = "Preauth.findByClaimKeyWithClearCashless", query = "SELECT o FROM Preauth o where o.claim.key = :claimkey order by o.key "),
		@NamedQuery(name = "Preauth.findByClaimKeyInDescorder", query = "SELECT o FROM Preauth o where o.claim.key = :claimkey and o.status is not null and (o.status.key <> 167 and o.status.key <> 166) order by o.key desc"),
		@NamedQuery(name = "Preauth.findByClaimKeyInAsc", query = "SELECT o FROM Preauth o where o.claim.key = :claimkey and o.status is not null and (o.status.key <> 167 and o.status.key <> 166) order by o.key asc"),
		@NamedQuery(name = "Preauth.findByIntimationNumber", query = "SELECT o From Preauth o where o.intimation.intimationId = :intimationNo"),
		@NamedQuery(name = "Preauth.findByCashlessIntimationKey", query = "SELECT o From Preauth o where o.intimation.key = :iKey ORDER BY o.key ASC"),
		@NamedQuery(name = "Preauth.findPreauthApprovedDateByClaimKey", query = "SELECT o FROM Preauth o where o.claim.key = :claimkey and o.status.key in (22,30) order by o.key desc"),
		@NamedQuery(name = "Preauth.findByStatusAndIntimation", query = "SELECT o FROM Preauth o where o.intimation.key = :intimationKey and o.status.key in (181,33,20,66,21,22,30,63,38) order by o.key asc"),		
		@NamedQuery(name = "Preauth.findByStatusAndIntimationDesc", query = "SELECT o FROM Preauth o where o.intimation.key = :intimationKey and o.status.key not in (167,166,31,23,26) order by o.key desc"),
		@NamedQuery(name = "Preauth.findByIntimationByAmtDesc", query = "SELECT o FROM Preauth o where o.intimation.key = :intimationKey and o.status.key not in (167,166) order by o.claimedAmt desc")

})
public class Preauth extends AbstractEntity {
	
	@Id
	@SequenceGenerator(name="IMS_CASHLESS_KEY_GENERATOR", sequenceName = "SEQ_CASHLESS_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CASHLESS_KEY_GENERATOR" ) 
	@Column(name="CASHLESS_KEY", updatable=false)
	private Long key;
	
	/**	
	 * 
	 */
	private static final long serialVersionUID = -3873349889369766466L;

	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;

//	@Column(name = "ACTIVE_STATUS_DATE")
//	private Timestamp activeStatusDate;

//	@Column(name = "APPROVED_AMOUNT")
//	private Double approvedAmount;
	
	@Column(name = "DIFF_AMOUNT")
	private Double diffAmount;
	
	@Column(name = "COPAY_PERCENTAGE")
	private Double coPay;

	@Temporal(TemporalType.DATE)
	@Column(name = "CONSULTATION_DATE")
	private Date consultationDate;
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "COORDINATOR_FLAG", length = 1)
	private String coordinatorFlag;
	
	@Column(nullable = false, columnDefinition = "NUMBER", name="CORPORATE_BUFFER_FLAG", length=1)
	private Long corporateBufferFlag;


	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
//	@Column(name = "SEQUENCE_NUMBER")
//	private Long sequenceNo;
	
	@Column(nullable = false, columnDefinition = "NUMBER", name="CRITICAL_ILLNESS_FLAG",length=1)
	private Long criticalIllnessFlag;


	/*@Column(nullable = false, columnDefinition = "NUMBER", name = "CRITICAL_ILLNESS_FLAG", length = 1)
	private Boolean criticalIllnessFlag;
	@Column(nullable = false, columnDefinition = "NUMBER", name="CRITICAL_ILLNESS_FLAG",length=1)
	private Long criticalIllnessFlag;*/


	/*@Column(name = "CRITICAL_ILLNESS_SPECIFY")
	private String criticalIllnessSpecify;*/
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CRITICAL_ILLNESS_ID",nullable=true)
	private CriticalIllnessMaster criticalIllness;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_OF_ADMISSION")
	private Date dataOfAdmission;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_OF_DEATH")
	private Date dateOfDeath;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_OF_DISCHARGE")
	private Date dateOfDischarge;

	@Column(name = "DEATH_REASON")
	private String deathReason;

	@Column(name = "DOCTOR_NOTE")
	private String doctorNote;

	@Column(nullable = false, name = "ENHANCEMENT_TYPE", columnDefinition = "VARCHAR(1)", length = 1)
	private String enhancementType;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CLAIM_KEY", nullable = false)
	private Claim claim;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "INTIMATION_KEY", nullable = false)
	private Intimation intimation;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "POLICY_KEY", nullable = false)
	private Policy policy;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FVR_NOT_REQUIRED_REMARKS", nullable = true)
	private MastersValue fvrNotRequiredRemarks;
	
//	@OneToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "CLAIM_KEY", nullable = false)
//	private DocAcknowledgement docAcknowledgement;

	@Column(nullable = false, name = "INITIATE_FVR", length = 1)
	private Integer initiateFvr;

	@Column(name = "MEDICAL_CATEGORY_ID")
	private Long medicalCategoryId;

	@Column(name = "MEDICAL_REMARKS")
	private String medicalRemarks;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_DATE")
	private Timestamp modifiedDate;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "NATURE_OF_TREATMENT_ID", nullable = true)
	private MastersValue natureOfTreatment;

	@Column(name = "NUMBER_OF_DAYS")
	private Long numberOfDays;

	@Column(name = "OFFICE_CODE")
	private String officeCode;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PATIENT_STATUS_ID", nullable = false)
	private MastersValue patientStatus;

	@Column(name = "ITERATION_NUMBER")
	private String preauthId;

	@Column(name = "PROCESS_TYPE")
	private String processType;

	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "RELAPSE_FLAG", length = 1)
	private String relapseFlag;

	@Column(name = "RELAPSE_REMARKS")
	private String relapseRemarks;

	@Column(name = "REMARKS")
	private String remarks;

	// @Column(name="REQUESTED_AMOUNT")
	// private Long requestedAmount;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ROOM_CATEGORY_ID", nullable = false)
	private MastersValue roomCategory;

	@Column(name = "SPECIALIST_CONSULTED_ID")
	private Long specialistConsulted;

	@Column(nullable = false, name = "SPECIALIST_OPINION_TAKEN", length = 1)
	private Integer specialistOpinionTaken;

	@Column(name = "SPECIALIST_REMARKS")
	private String specialistRemarks;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SPECIALIST_TYPE_ID", nullable = true)
	private MastersValue specialistType;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATUS_ID", nullable = false)
	private Status status;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STAGE_ID", nullable = false)
	private Stage stage;

//	@Column(name = "STATUS_DATE")
//	private Timestamp statusDate;

	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "AUTOMATIC_RESTORATION", length = 1)
	private String autoRestoration;

	/*@OneToOne
	@JoinColumn(name = "ILLNESS", nullable = false)
	private MastersValue illness;*/
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ILLNESS_ID", nullable=true)
	private MastersValue illness;

	
	@Column(name="HOME_CPU_ID")
	private Long homeCpuId;

	@Column(name = "PROCESSING_CPU_ID")
	private Long processingCpuId;

	@Column(nullable = false, name = "SEND_TO_CPU", length = 1)
	private Integer sendToCpu;

	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "REPORT_REVIEWED", length = 1)
	private String reportReviewed;

	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "CHANGE_PRE_AUTH", length = 1)
	private String changePreauth;

	@Column(name = "INVESTIGATOR_NAME")
	private String investigatorName;

	@Column(name = "CPU_REMARKS")
	private String cpuRemarks;

	@Column(name = "REVIEW_REMARKS")
	private String reviewRemarks;

	@Column(name = "DOA_CHANGE_REASON")
	private String doaChangeReason;

	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "TERMINATOR_COVER", length = 1)
	private String terminatorCover;

	@Column(name = "TOTAL_APPROVAL_AMOUNT")
	private Double totalApprovalAmount;

	@Column(name = "TREATMENT_REMARKS")
	private String treatmentRemarks;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TREATMENT_TYPE_ID", nullable = false)
	private MastersValue treatmentType;

//	@Column(name = "VERSION")
//	private Long version;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WITHDRAW_REASON_ID", nullable = true)
	private MastersValue withdrawReason;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DENIAL_REASON_ID", nullable = true)
	private MastersValue denialReason;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DOWNSIZE_REASON_ID", nullable = true)
	private MastersValue downSizeReason;

//	@OneToOne
//	@JoinColumn(name = "CATEGORY_REASON_ID", nullable = true)
//	private MastersValue categoryReason;
	
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REJECTION_CATEGORY_ID", nullable = true)
	private MastersValue rejectionCategorId;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REJ_SUB_CATEGORY_ID", nullable = true)
	private MasRejectSubCategory rejSubCategory;
		
	@Column(name="CONDITION_NO")
	private String conditionNo;
		
	@Column(name = "INSURED_KEY")
	private Long insuredKey;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="HOSPITALIZATION_DUE_ID", nullable=true)
	private MastersValue hopsitaliztionDueto;
	
	@Column(name="INJURY_CAUSE_ID")
	private Long injuryCauseId;    
	
	@Temporal(TemporalType.DATE)
	@Column(name="INJURY_DATE")
	private Date injuryDate;          
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "MEDICO_LEGAL_CARE", length = 1)
	private String  medicoLeagalCare;   
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "REPORTED_TO_POLICE", length = 1)
	private String reportedToPolice;   
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "ATTACHED_POLICE_REPORT", length = 1)
	private String attachedPoliceReport;   
	
	@Column(name="FIR_NUMBER")
	private String firNumber; 
	
	@Temporal(TemporalType.DATE)
	@Column(name="FIRST_DISEASE_DETECTED_DATE")
	private Date firstDiseaseDetectedDate;     
	
	@Temporal(TemporalType.DATE)
	@Column(name="DATE_OF_DELIVERY")
	private Date dateOfDelivery;      

	@Column(name="DELIVERY_TYPE_ID", nullable = true)
	private Long typeOfDelivery;
	
	@Column(name="SECTION_CATEGORY_ID", nullable = true)
	private Long sectionCategory;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SFX_REGISTEREDQ_DATE")
	private Date sfxRegisteredQDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SFX_MATCHEDQ_DATE")
	private Date sfxMatchedQDate;

	@Column(name = "AMT_CONS_COPAY_PRCNT", nullable = true)
	private Long amtConsCopayPercentage;
	
	@Column(name = "BAL_SI_COPAY_PRCNT", nullable = true)
	private Long balanceSICopayPercentage;
	
	@Column(name = "AMT_CONS_COPAY_AMT")
	private Double amtConsAftCopayAmount;
	
	@Column(name = "BAL_SI_COPAY_AMT")
	private Double balanceSIAftCopayAmt;
	
	@Column(name = "CLEAR_REMARKS")
	private String clearingRemarks;
	
	@Column(name = "ZONAL_REMARKS")
	private String referToFLPRemarks;
	
	@Column(name = "PREMIUM_AMT")
	private Double premiumAmt;
	
	@Column(name = "APR_AMT_AFT_PREM")
	private Double approvedAmtAftPremium;
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "PED_DISABILITY_FLAG", length = 1)
	private String pedDisabilityFlag;
	
	@Column(name = "PED_DISABILITY_DTLS")
	private String pedDisabilityDetails;
	
	@Column(name = "WORKPLACE")
	private String workPlace;
	
	@Column(name = "UN_NAMED_KEY")
	private Long unNamedKey;
	
	@Column(name = "STENT")
	private Long stent;
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CLAIM_PROCESS_SUGG_ID", nullable = true)
	private MastersValue cpuAllocationSuggestionId;
	
	@Column(name = "REMARKS_REFERRING_BK_CPU")
	private String cpuAllocationReferringRemarks;
	
	@Column(name = "CPU_AMOUNT_SUGGESTED")
	private Double cpuSuggestedAmount;
	
	@Column(name = "CORPORATE_BUFFER_LIMIT")
	private Integer corporateUtilizedAmt;
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "OTHER_BENEFIT_FLAG", length = 1)
	private String otherBenefitFlag;	
	
	@Column(name = "OTHER_BENEFIT_AMT")
	private Double otherBenefitApprovedAmt;	

	@Column(nullable = false, name = "PREAUTH_WITHOUT_DOC", length = 1)
	private Integer preauthWithoutDoc;
	
	@Column(name = "DEFINED_LIMIT_AMT")
	private Integer consideredDefineLimitAmnt;
	
	@Column(name = "WITHDRAW_REMARKS")
	private String withdrawRemarks;
	
	@Column(name = "INSURED_WITHDRAW_REMARKS")
	private String insuredWithdrawRemarks;	
	
	@Column(name = "REJECT_REMARKS")
	private String rejectRemarks;
	
	@Column(name = "INSURED_REMARKS")
	private String insuredRemarks;
	
	@Column(name = "DOWNSIZE_INSURED_REMARKS")
	private String downsizeInsuredRemarks;
	
	@Column(name = "FVR_ALERT_FLAG")
	private String fvrAlertFlag;

	@Column(name = "FVR_OTHER_REMARKS")
	private String fvrOtherRmrks;

	@Column(name = "AMOUNT_CLAIMED")
	private Integer claimedAmt;
	
	@Column(name = "DISCNT_HOSP_BILL")
	private Integer discntHospBill;
	
	@Column(name = "NET_AMOUNT")
	private Integer netAmount;
	
	@Column(name = "HOSPITAL_SCORE_FLAG")
	private String scoringFlag;
	
	@Column(name = "AUTO_CANCEL_REMARKS")
	private String autoCancelRemarks;
	
	@Column(name = "STP_REMARKS")
	private String stpRemarks;
	
	@Column(name = "NOT_ADHERING_ANH")
	private String notAdheringToANHReport;
	
	@Column(name = "CVC_AUDIT_FLAG")
	private String cvcAuditFlag;
	
	@Column(name= "CVC_LOCK_FLAG")
	private String cvcLockFlag;

	@Column(name = "WITHDRAW_INTERNAL_REMARKS")
	private String withdrawInternalRemarks;
	
	@Column(name = "STP_PROCESS_LEVEL")
	private Long stpProcessLevel;

	@Column(name = "LETTER_VERIFIED")
	private String letterVerified;
	
	@Column(name = "NATURE_OF_LOSS")
	private Long natureOfLoss;
	
	@Column(name = "CAUSE_OF_LOSS")
	private Long causeOfLoss;
	
	@Column(name = "CATASTROPHIC_LOSS")
	private Long catastrophicLoss;
	
	@Column(name = "AA_USER_ID")
	private String aaUserId;

	@Column(name = "AA_SUBMIT_DATE")
	private Timestamp aaSubmitDate;
	
	@Column(name = "FLP_AA_USER_ID")
	private String flpUserID;

	@Column(name = "FLP_AA_SUB_DATE")
	private Timestamp FLPSubmitDate;
	
	@Column(name = "BATCH_CHEQUE_FLAG")
	private String batchChequeFlag;
	
	@Column(name = "BATCH_CHEQUE_RUN_DATE")
	private Timestamp batchChequeRunDate;
	
	@Column(name = "IMPLANT_FLAG")
	private String implantFlag;

	@Column(name = "MB_AGRD_DISCNT_NOT_APLY")
	private String mbAgrdDisNtAplyChkBx;
	
	@Column(name = "HOSP_COLLECT_OVR_AMT_SOC")
	private String hospCollectOvrAmtChkBx;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BEHAVIOUR_OF_HOSP", nullable = true)
	private MastersValue behaviourOfHospCmb;
	
	@Column(name="NHP_UPD_DOC_KEY")
	private Long nhpUpdDocKey;

	@Column(name="ICAC_FLAG")
	private String icacFlag;
	
	@Column(name = "REASON_FOR_ICD_EXCLSN")
	private String reasonForIcdExclusion;
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "VENTILATOR_SUPPORT", length = 1)
	private String ventilatorSupport;
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "DC_VENTILATOR_SUPPORT", length = 1)
	private String dcVentilatorSupport;
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "OLD_VENTILATOR_SUPPORT ", length = 1)
	private String oldVentilatorSupport;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="COVID_TREATMENT_ID", nullable=true)
	private MastersValue covidTreatmentId;
	
	@Column (name = "TYPE_OF_ADMISSION")
	private Long typeOfAdmission;
	
	@Column (name = "OLD_TYPE_OF_ADMISSION")
	private Long oldTypeOfAdmission;
	
	@Column(name="DC_TOA_FLAG")
	private String dcTOAFlag;
	
	@Column(name = "WINTAGE_BUFFER_AMT")
	private Integer wintageBufferUtilAmt;
	
	@Column(name = "NACB_BUFFER_AMT")
	private Integer nacBufferUtilAmt;

	@Column(name="REJECTION_REMARKS2")
	private String rejectionRemarks2;

	@Column(name = "PROPORTIONATE_FLAG")
	private String proportionateFlag;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="DECISION_CHANGE_REASON", nullable=true)
	private MastersValue decisionChangeReason;
	
	@Column(name = "DECISION_CHANGE_REMARKS")
	private String decisionChangeRemarks;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="OLD_ROOM_CATEGORY_ID",nullable=true)
	private MastersValue oldRoomCategory;
	
	@Column(name="CRCN_RC_FLAG")
	private String rcFlag;
	
	@Column(name="DC_HCODING_FLAG")
	private String dcFlag;
	
	/*@Column(nullable = true, columnDefinition = "VARCHAR(1)", name = "COCKTAIL_DRUG", length = 1)
	private String cocktailDrug;*/
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COCKTAIL_DRUG", nullable = true)
	private MastersValue cocktailDrug;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COVID19_VARIANT", nullable = true)
	private MastersValue covid19Variant;
	
	/*@Column(nullable = true, columnDefinition = "VARCHAR(1)", name = "OLD_COCKTAIL_DRUG", length = 1)
	private String oldCocktailDrug;*/
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OLD_COCKTAIL_DRUG", nullable = true)
	private MastersValue oldCocktailDrug;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OLD_COVID19_VARIANT", nullable = true)
	private MastersValue oldCovid19Variant;
	
	@Column(name="SOURCE")
	private String source;
	
	@Column(name="CHANNEL")
	private String channel;
	
	
	public String getReasonForIcdExclusion() {
		return reasonForIcdExclusion;
	}

	public void setReasonForIcdExclusion(String reasonForIcdExclusion) {
		this.reasonForIcdExclusion = reasonForIcdExclusion;
	}
	
	public Long getNatureOfLoss() {
		return natureOfLoss;
	}

	public void setNatureOfLoss(Long natureOfLoss) {
		this.natureOfLoss = natureOfLoss;
	}

	public Long getCauseOfLoss() {
		return causeOfLoss;
	}

	public void setCauseOfLoss(Long causeOfLoss) {
		this.causeOfLoss = causeOfLoss;
	}

	public Long getCatastrophicLoss() {
		return catastrophicLoss;
	}

	public void setCatastrophicLoss(Long catastrophicLoss) {
		this.catastrophicLoss = catastrophicLoss;
	}

	@Column(name = "GN_CATEGORY")
	private String category;

	public Long getStpProcessLevel() {
		return stpProcessLevel;
	}

	public void setStpProcessLevel(Long stpProcessLevel) {
		this.stpProcessLevel = stpProcessLevel;
	}

	public Long getKey() {
		return this.key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}
	
	public Double getCoPay() {
		return coPay;
	}

	public void setCoPay(Double coPay) {
		this.coPay = coPay;
	}

	public Date getConsultationDate() {
		return consultationDate;
	}

	public void setConsultationDate(Date consultationDate) {
		this.consultationDate = consultationDate;
	}

	public String getCoordinatorFlag() {
		return coordinatorFlag;
	}

	public MastersValue getIllness() {
		return illness;
	}

	public void setIllness(MastersValue illness) {
		this.illness = illness;
	}

	public void setCoordinatorFlag(String coordinatorFlag) {
		this.coordinatorFlag = coordinatorFlag;
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

	/*public String getCriticalIllnessSpecify() {
		return criticalIllnessSpecify;
	}

	public void setCriticalIllnessSpecify(String criticalIllnessSpecify) {
		this.criticalIllnessSpecify = criticalIllnessSpecify;
	}*/

	public Date getDataOfAdmission() {
		return dataOfAdmission;
	}

	public void setDataOfAdmission(Date dataOfAdmission) {
		this.dataOfAdmission = dataOfAdmission;
	}

	public Date getDateOfDeath() {
		return dateOfDeath;
	}

	public void setDateOfDeath(Date dateOfDeath) {
		this.dateOfDeath = dateOfDeath;
	}

	public Date getDateOfDischarge() {
		return dateOfDischarge;
	}

	public void setDateOfDischarge(Date dateOfDischarge) {
		this.dateOfDischarge = dateOfDischarge;
	}

	public String getDeathReason() {
		return deathReason;
	}

	public void setDeathReason(String deathReason) {
		this.deathReason = deathReason;
	}

	public String getDoctorNote() {
		return doctorNote;
	}

	public void setDoctorNote(String doctorNote) {
		this.doctorNote = doctorNote;
	}

	public String getEnhancementType() {
		return enhancementType;
	}

	public void setEnhancementType(String enhancementType) {
		this.enhancementType = enhancementType;
	}

	public Claim getClaim() {
		return claim;
	}

	public void setClaim(Claim claim) {
		this.claim = claim;
	}

	public Intimation getIntimation() {
		return intimation;
	}

	public void setIntimation(Intimation intimation) {
		this.intimation = intimation;
	}

	public Policy getPolicy() {
		return policy;
	}

	public void setPolicy(Policy policy) {
		this.policy = policy;
	}

	public MastersValue getFvrNotRequiredRemarks() {
		return fvrNotRequiredRemarks;
	}

	public void setFvrNotRequiredRemarks(MastersValue fvrNotRequiredRemarks) {
		this.fvrNotRequiredRemarks = fvrNotRequiredRemarks;
	}

	public Integer getInitiateFvr() {
		return initiateFvr;
	}

	public void setInitiateFvr(Integer initiateFvr) {
		this.initiateFvr = initiateFvr;
	}

	public Long getMedicalCategoryId() {
		return medicalCategoryId;
	}

	public void setMedicalCategoryId(Long medicalCategoryId) {
		this.medicalCategoryId = medicalCategoryId;
	}

	public String getMedicalRemarks() {
		return medicalRemarks;
	}

	public void setMedicalRemarks(String medicalRemarks) {
		this.medicalRemarks = medicalRemarks;
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

	public CriticalIllnessMaster getCriticalIllness() {
		return criticalIllness;
	}

	public void setCriticalIllness(CriticalIllnessMaster criticalIllness) {
		this.criticalIllness = criticalIllness;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public MastersValue getNatureOfTreatment() {
		return natureOfTreatment;
	}

	public void setNatureOfTreatment(MastersValue natureOfTreatment) {
		this.natureOfTreatment = natureOfTreatment;
	}

	public Long getNumberOfDays() {
		return numberOfDays;
	}

	public void setNumberOfDays(Long numberOfDays) {
		this.numberOfDays = numberOfDays;
	}

	public String getOfficeCode() {
		return officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

	public MastersValue getPatientStatus() {
		return patientStatus;
	}

	public void setPatientStatus(MastersValue patientStatus) {
		this.patientStatus = patientStatus;
	}

	public String getPreauthId() {
		return preauthId;
	}

	public void setPreauthId(String preauthId) {
		this.preauthId = preauthId;
	}

	public String getProcessType() {
		return processType;
	}

	public void setProcessType(String processType) {
		this.processType = processType;
	}

	public String getRelapseFlag() {
		return relapseFlag;
	}

	public void setRelapseFlag(String relapseFlag) {
		this.relapseFlag = relapseFlag;
	}

	public String getRelapseRemarks() {
		return relapseRemarks;
	}

	public void setRelapseRemarks(String relapseRemarks) {
		this.relapseRemarks = relapseRemarks;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public MastersValue getRoomCategory() {
		return roomCategory;
	}

	public void setRoomCategory(MastersValue roomCategory) {
		this.roomCategory = roomCategory;
	}

	public Long getSpecialistConsulted() {
		return specialistConsulted;
	}

	public void setSpecialistConsulted(Long specialistConsulted) {
		this.specialistConsulted = specialistConsulted;
	}

	public Integer getSpecialistOpinionTaken() {
		return specialistOpinionTaken;
	}

	public void setSpecialistOpinionTaken(Integer specialistOpinionTaken) {
		this.specialistOpinionTaken = specialistOpinionTaken;
	}

	public String getSpecialistRemarks() {
		return specialistRemarks;
	}

	public void setSpecialistRemarks(String specialistRemarks) {
		this.specialistRemarks = specialistRemarks;
	}

	public MastersValue getSpecialistType() {
		return specialistType;
	}

	public void setSpecialistType(MastersValue specialistType) {
		this.specialistType = specialistType;
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
	
	public String getAutoRestoration() {
		return autoRestoration;
	}

	public void setAutoRestoration(String autoRestoration) {
		this.autoRestoration = autoRestoration;
	}

	/*public MastersValue getIllness() {
		return illness;
	}

	public void setIllness(MastersValue illness) {
		this.illness = illness;
	}*/

	public Long getHomeCpuId() {
		return homeCpuId;
	}

	public void setHomeCpuId(Long homeCpuId) {
		this.homeCpuId = homeCpuId;
	}

	public Long getProcessingCpuId() {
		return processingCpuId;
	}

	public void setProcessingCpuId(Long processingCpuId) {
		this.processingCpuId = processingCpuId;
	}

	public Integer getSendToCpu() {
		return sendToCpu;
	}

	public void setSendToCpu(Integer sendToCpu) {
		this.sendToCpu = sendToCpu;
	}

	public String getReportReviewed() {
		return reportReviewed;
	}

	public void setReportReviewed(String reportReviewed) {
		this.reportReviewed = reportReviewed;
	}

	public String getChangePreauth() {
		return changePreauth;
	}

	public void setChangePreauth(String changePreauth) {
		this.changePreauth = changePreauth;
	}

	public String getInvestigatorName() {
		return investigatorName;
	}

	public void setInvestigatorName(String investigatorName) {
		this.investigatorName = investigatorName;
	}

	public String getCpuRemarks() {
		return cpuRemarks;
	}

	public void setCpuRemarks(String cpuRemarks) {
		this.cpuRemarks = cpuRemarks;
	}

	public String getReviewRemarks() {
		return reviewRemarks;
	}

	public void setReviewRemarks(String reviewRemarks) {
		this.reviewRemarks = reviewRemarks;
	}

	public String getDoaChangeReason() {
		return doaChangeReason;
	}

	public void setDoaChangeReason(String doaChangeReason) {
		this.doaChangeReason = doaChangeReason;
	}

	public String getTerminatorCover() {
		return terminatorCover;
	}

	public void setTerminatorCover(String terminatorCover) {
		this.terminatorCover = terminatorCover;
	}

	public Double getTotalApprovalAmount() {
		return totalApprovalAmount;
	}

	public void setTotalApprovalAmount(Double totalApprovalAmount) {
		this.totalApprovalAmount = totalApprovalAmount;
	}

	public String getTreatmentRemarks() {
		return treatmentRemarks;
	}

	public void setTreatmentRemarks(String treatmentRemarks) {
		this.treatmentRemarks = treatmentRemarks;
	}

	public MastersValue getTreatmentType() {
		return treatmentType;
	}

	public void setTreatmentType(MastersValue treatmentType) {
		this.treatmentType = treatmentType;
	}

	public MastersValue getWithdrawReason() {
		return withdrawReason;
	}

	public void setWithdrawReason(MastersValue withdrawReason) {
		this.withdrawReason = withdrawReason;
	}

	public MastersValue getIllnessId() {
		return illness;
	}

	public void setIllnessId(MastersValue illnessId) {
		this.illness = illnessId;
	}
	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}

		if (this.key == null || obj == null || !(this.getClass().equals(obj.getClass()))) {
			return false;
		}

		AbstractEntity that = (AbstractEntity) obj;

		return this.key.equals(that.getKey());
	}

	/* 
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return key == null ? 0 : key.hashCode();
	}
	
	public boolean isNew() {
		return key == null ? true: false;
	}

	public Long getCorporateBufferFlag() {
		return corporateBufferFlag;
	}

	public void setCorporateBufferFlag(Long corporateBufferFlag) {
		this.corporateBufferFlag = corporateBufferFlag;
	}

	public Long getCriticalIllnessFlag() {
		return criticalIllnessFlag;
	}

	public void setCriticalIllnessFlag(Long criticalIllnessFlag) {
		this.criticalIllnessFlag = criticalIllnessFlag;
	}

	public Double getDiffAmount() {
		return diffAmount;
	}

	public void setDiffAmount(Double diffAmount) {
		this.diffAmount = diffAmount;
	}

	public MastersValue getDownSizeReason() {
		return downSizeReason;
	}

	public void setDownSizeReason(MastersValue downSizeReason) {
		this.downSizeReason = downSizeReason;
	}

	public Long getInsuredKey() {
		return insuredKey;
	}

	public void setInsuredKey(Long insuredKey) {
		this.insuredKey = insuredKey;
	}

	public MastersValue getRejectionCategorId() {
		return rejectionCategorId;
	}

	public void setRejectionCategorId(MastersValue rejectionCategorId) {
		this.rejectionCategorId = rejectionCategorId;
	}

	public MastersValue getHopsitaliztionDueto() {
		return hopsitaliztionDueto;
	}

	public void setHopsitaliztionDueto(MastersValue hopsitaliztionDueto) {
		this.hopsitaliztionDueto = hopsitaliztionDueto;
	}

	public Long getInjuryCauseId() {
		return injuryCauseId;
	}

	public void setInjuryCauseId(Long injuryCauseId) {
		this.injuryCauseId = injuryCauseId;
	}

	public Date getInjuryDate() {
		return injuryDate;
	}

	public void setInjuryDate(Date injuryDate) {
		this.injuryDate = injuryDate;
	}

	public String getMedicoLeagalCare() {
		return medicoLeagalCare;
	}

	public void setMedicoLeagalCare(String medicoLeagalCare) {
		this.medicoLeagalCare = medicoLeagalCare;
	}

	public String getReportedToPolice() {
		return reportedToPolice;
	}

	public void setReportedToPolice(String reportedToPolice) {
		this.reportedToPolice = reportedToPolice;
	}

	public String getAttachedPoliceReport() {
		return attachedPoliceReport;
	}

	public void setAttachedPoliceReport(String attachedPoliceReport) {
		this.attachedPoliceReport = attachedPoliceReport;
	}

	public String getFirNumber() {
		return firNumber;
	}

	public void setFirNumber(String firNumber) {
		this.firNumber = firNumber;
	}

	public Date getFirstDiseaseDetectedDate() {
		return firstDiseaseDetectedDate;
	}

	public void setFirstDiseaseDetectedDate(Date firstDiseaseDetectedDate) {
		this.firstDiseaseDetectedDate = firstDiseaseDetectedDate;
	}

	public Date getDateOfDelivery() {
		return dateOfDelivery;
	}

	public void setDateOfDelivery(Date dateOfDelivery) {
		this.dateOfDelivery = dateOfDelivery;
	}

	public Long getTypeOfDelivery() {
		return typeOfDelivery;
	}

	public void setTypeOfDelivery(Long typeOfDelivery) {
		this.typeOfDelivery = typeOfDelivery;
	}

	public Long getSectionCategory() {
		return sectionCategory;
	}

	public void setSectionCategory(Long sectionCategory) {
		this.sectionCategory = sectionCategory;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Date getSfxRegisteredQDate() {
		return sfxRegisteredQDate;
	}

	public void setSfxRegisteredQDate(Date sfxRegisteredQDate) {
		this.sfxRegisteredQDate = sfxRegisteredQDate;
	}

	public Date getSfxMatchedQDate() {
		return sfxMatchedQDate;
	}

	public void setSfxMatchedQDate(Date sfxMatchedQDate) {
		this.sfxMatchedQDate = sfxMatchedQDate;
	}

	public Long getAmtConsCopayPercentage() {
		return amtConsCopayPercentage;
	}

	public void setAmtConsCopayPercentage(Long amtConsCopayPercentage) {
		this.amtConsCopayPercentage = amtConsCopayPercentage;
	}

	public Long getBalanceSICopayPercentage() {
		return balanceSICopayPercentage;
	}

	public void setBalanceSICopayPercentage(Long balanceSICopayPercentage) {
		this.balanceSICopayPercentage = balanceSICopayPercentage;
	}

	public Double getAmtConsAftCopayAmount() {
		return amtConsAftCopayAmount;
	}

	public void setAmtConsAftCopayAmount(Double amtConsAftCopayAmount) {
		this.amtConsAftCopayAmount = amtConsAftCopayAmount;
	}

	public Double getBalanceSIAftCopayAmt() {
		return balanceSIAftCopayAmt;
	}

	public void setBalanceSIAftCopayAmt(Double balanceSIAftCopayAmt) {
		this.balanceSIAftCopayAmt = balanceSIAftCopayAmt;
	}

	public String getClearingRemarks() {
		return clearingRemarks;
	}

	public void setClearingRemarks(String clearingRemarks) {
		this.clearingRemarks = clearingRemarks;
	}

	public String getReferToFLPRemarks() {
		return referToFLPRemarks;
	}

	public void setReferToFLPRemarks(String referToFLPRemarks) {
		this.referToFLPRemarks = referToFLPRemarks;
	}

	public MastersValue getDenialReason() {
		return denialReason;
	}

	public void setDenialReason(MastersValue denialReason) {
		this.denialReason = denialReason;
	}
	public Double getPremiumAmt() {
		return premiumAmt;
	}

	public void setPremiumAmt(Double premiumAmt) {
		this.premiumAmt = premiumAmt;
	}

	public Double getApprovedAmtAftPremium() {
		return approvedAmtAftPremium;
	}

	public void setApprovedAmtAftPremium(Double approvedAmtAftPremium) {
		this.approvedAmtAftPremium = approvedAmtAftPremium;
	}

	public String getPedDisabilityFlag() {
		return pedDisabilityFlag;
	}

	public void setPedDisabilityFlag(String pedDisabilityFlag) {
		this.pedDisabilityFlag = pedDisabilityFlag;
	}

	public String getPedDisabilityDetails() {
		return pedDisabilityDetails;
	}

	public void setPedDisabilityDetails(String pedDisabilityDetails) {
		this.pedDisabilityDetails = pedDisabilityDetails;
	}

	public String getWorkPlace() {
		return workPlace;
	}

	public void setWorkPlace(String workPlace) {
		this.workPlace = workPlace;
	}

	public Long getUnNamedKey() {
		return unNamedKey;
	}

	public void setUnNamedKey(Long unNamedKey) {
		this.unNamedKey = unNamedKey;
	}
	

	public Long getStent() {
		return stent;
	}

	public void setStent(Long stent) {
		this.stent = stent;
	}

	public Integer getCorporateUtilizedAmt() {
		return corporateUtilizedAmt;
	}

	public void setCorporateUtilizedAmt(Integer corporateUtilizedAmt) {
		this.corporateUtilizedAmt = corporateUtilizedAmt;
	}
	public Integer getPreauthWithoutDoc() {
		return preauthWithoutDoc;
	}

	public void setPreauthWithoutDoc(Integer preauthWithoutDoc) {
		this.preauthWithoutDoc = preauthWithoutDoc;
	}

	public MastersValue getCpuAllocationSuggestionId() {
		return cpuAllocationSuggestionId;
	}

	public void setCpuAllocationSuggestionId(MastersValue cpuAllocationSuggestionId) {
		this.cpuAllocationSuggestionId = cpuAllocationSuggestionId;
	}

	public String getCpuAllocationReferringRemarks() {
		return cpuAllocationReferringRemarks;
	}

	public void setCpuAllocationReferringRemarks(
			String cpuAllocationReferringRemarks) {
		this.cpuAllocationReferringRemarks = cpuAllocationReferringRemarks;
	}

	public Double getCpuSuggestedAmount() {
		return cpuSuggestedAmount;
	}

	public void setCpuSuggestedAmount(Double cpuSuggestedAmount) {
		this.cpuSuggestedAmount = cpuSuggestedAmount;
	}

	public String getOtherBenefitFlag() {
		return otherBenefitFlag;
	}

	public void setOtherBenefitFlag(String otherBenefitFlag) {
		this.otherBenefitFlag = otherBenefitFlag;
	}

	public Double getOtherBenefitApprovedAmt() {
		return otherBenefitApprovedAmt;
	}

	public void setOtherBenefitApprovedAmt(Double otherBenefitApprovedAmt) {
		this.otherBenefitApprovedAmt = otherBenefitApprovedAmt;
	}

	public Integer getConsideredDefineLimitAmnt() {
		return consideredDefineLimitAmnt;
	}

	public void setConsideredDefineLimitAmnt(Integer consideredDefineLimitAmnt) {
		this.consideredDefineLimitAmnt = consideredDefineLimitAmnt;
	}

	public String getWithdrawRemarks() {
		return withdrawRemarks;
	}

	public void setWithdrawRemarks(String withdrawRemarks) {
		this.withdrawRemarks = withdrawRemarks;
	}

	public String getInsuredWithdrawRemarks() {
		return insuredWithdrawRemarks;
	}

	public void setInsuredWithdrawRemarks(String insuredWithdrawRemarks) {
		this.insuredWithdrawRemarks = insuredWithdrawRemarks;
	}

	public String getRejectRemarks() {
		return rejectRemarks;
	}

	public void setRejectRemarks(String rejectRemarks) {
		this.rejectRemarks = rejectRemarks;
	}

	public String getInsuredRemarks() {
		return insuredRemarks;
	}

	public void setInsuredRemarks(String insuredRemarks) {
		this.insuredRemarks = insuredRemarks;
	}

	public String getFvrAlertFlag() {
		return fvrAlertFlag;
	}

	public void setFvrAlertFlag(String fvrAlertFlag) {
		this.fvrAlertFlag = fvrAlertFlag;
	}		

	public String getFvrOtherRmrks() {
		return fvrOtherRmrks;
	}

	public void setFvrOtherRmrks(String fvrOtherRmrks) {
		this.fvrOtherRmrks = fvrOtherRmrks;
	}	
	
	public Integer getClaimedAmt() {
		return claimedAmt;
	}

	public void setClaimedAmt(Integer claimedAmt) {
		this.claimedAmt = claimedAmt;
	}

	public Integer getDiscntHospBill() {
		return discntHospBill;
	}

	public void setDiscntHospBill(Integer discntHospBill) {
		this.discntHospBill = discntHospBill;
	}

	public Integer getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Integer netAmount) {
		this.netAmount = netAmount;
	}

	public String getScoringFlag() {
		return scoringFlag;
	}

	public void setScoringFlag(String scoringFlag) {
		this.scoringFlag = scoringFlag;
	}
	
	public String getAutoCancelRemarks() {
		return autoCancelRemarks;
	}

	public void setAutoCancelRemarks(String autoCancelRemarks) {
		this.autoCancelRemarks = autoCancelRemarks;
	}

	public String getStpRemarks() {
		return stpRemarks;
	}

	public void setStpRemarks(String stpRemarks) {
		this.stpRemarks = stpRemarks;
	}

	public String getNotAdheringToANHReport() {
		return notAdheringToANHReport;
	}

	public void setNotAdheringToANHReport(String notAdheringToANHReport) {
		this.notAdheringToANHReport = notAdheringToANHReport;
	}

	public String getCvcAuditFlag() {
		return cvcAuditFlag;
	}

	public void setCvcAuditFlag(String cvcAuditFlag) {
		this.cvcAuditFlag = cvcAuditFlag;
	}

	public String getCvcLockFlag() {
		return cvcLockFlag;
	}

	public void setCvcLockFlag(String cvcLockFlag) {
		this.cvcLockFlag = cvcLockFlag;
	}

	public String getDownsizeInsuredRemarks() {
		return downsizeInsuredRemarks;
	}

	public void setDownsizeInsuredRemarks(String downsizeInsuredRemarks) {
		this.downsizeInsuredRemarks = downsizeInsuredRemarks;
	}	

	public String getWithdrawInternalRemarks() {
		return withdrawInternalRemarks;
	}

	public void setWithdrawInternalRemarks(String withdrawInternalRemarks) {
		this.withdrawInternalRemarks = withdrawInternalRemarks;
	}

	public MasRejectSubCategory getRejSubCategory() {
		return rejSubCategory;
	}

	public void setRejSubCategory(MasRejectSubCategory rejSubCategory) {
		this.rejSubCategory = rejSubCategory;
	}

	public String getConditionNo() {
		return conditionNo;
	}

	public void setConditionNo(String conditionNo) {
		this.conditionNo = conditionNo;
	}

	public String getLetterVerified() {
		return letterVerified;
	}

	public void setLetterVerified(String letterVerified) {
		this.letterVerified = letterVerified;
	}	
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getAaUserId() {
		return aaUserId;
	}

	public void setAaUserId(String aaUserId) {
		this.aaUserId = aaUserId;
	}

	public Timestamp getAaSubmitDate() {
		return aaSubmitDate;
	}

	public void setAaSubmitDate(Timestamp aaSubmitDate) {
		this.aaSubmitDate = aaSubmitDate;
	}

	public String getFlpUserID() {
		return flpUserID;
	}

	public void setFlpUserID(String flpUserID) {
		this.flpUserID = flpUserID;
	}

	public Timestamp getFLPSubmitDate() {
		return FLPSubmitDate;
	}

	public void setFLPSubmitDate(Timestamp fLPSubmitDate) {
		FLPSubmitDate = fLPSubmitDate;
	}

	public String getBatchChequeFlag() {
		return batchChequeFlag;
	}

	public void setBatchChequeFlag(String batchChequeFlag) {
		this.batchChequeFlag = batchChequeFlag;
	}

	public Timestamp getBatchChequeRunDate() {
		return batchChequeRunDate;
	}

	public void setBatchChequeRunDate(Timestamp batchChequeRunDate) {
		this.batchChequeRunDate = batchChequeRunDate;
	}

	public String getImplantFlag() {
		return implantFlag;
	}

	public void setImplantFlag(String implantFlag) {
		this.implantFlag = implantFlag;
	}
	
	public String getMbAgrdDisNtAplyChkBx() {
		return mbAgrdDisNtAplyChkBx;
	}

	public void setMbAgrdDisNtAplyChkBx(String mbAgrdDisNtAplyChkBx) {
		this.mbAgrdDisNtAplyChkBx = mbAgrdDisNtAplyChkBx;
	}

	public String getHospCollectOvrAmtChkBx() {
		return hospCollectOvrAmtChkBx;
	}

	public void setHospCollectOvrAmtChkBx(String hospCollectOvrAmtChkBx) {
		this.hospCollectOvrAmtChkBx = hospCollectOvrAmtChkBx;
	}

	public MastersValue getBehaviourOfHospCmb() {
		return behaviourOfHospCmb;
	}

	public void setBehaviourOfHospCmb(MastersValue behaviourOfHospCmb) {
		this.behaviourOfHospCmb = behaviourOfHospCmb;
	}

	public Long getNhpUpdDocKey() {
		return nhpUpdDocKey;
	}

	public void setNhpUpdDocKey(Long nhpUpdDocKey) {
		this.nhpUpdDocKey = nhpUpdDocKey;
	}

	public String getIcacFlag() {
		return icacFlag;
	}

	public void setIcacFlag(String icacFlag) {
		this.icacFlag = icacFlag;
	}
	
	public MastersValue getCovidTreatmentId() {
		return covidTreatmentId;
	}

	public void setCovidTreatmentId(MastersValue covidTreatmentId) {
		this.covidTreatmentId = covidTreatmentId;
	}

	public Long getTypeOfAdmission() {
		return typeOfAdmission;
	}

	public void setTypeOfAdmission(Long typeOfAdmission) {
		this.typeOfAdmission = typeOfAdmission;
	}

	public Long getOldTypeOfAdmission() {
		return oldTypeOfAdmission;
	}

	public void setOldTypeOfAdmission(Long oldTypeOfAdmission) {
		this.oldTypeOfAdmission = oldTypeOfAdmission;
	}

	public String getDcTOAFlag() {
		return dcTOAFlag;
	}

	public void setDcTOAFlag(String dcTOAFlag) {
		this.dcTOAFlag = dcTOAFlag;
	}

	public String getVentilatorSupport() {
		return ventilatorSupport;
	}

	public void setVentilatorSupport(String ventilatorSupport) {
		this.ventilatorSupport = ventilatorSupport;
	}

	public String getDcVentilatorSupport() {
		return dcVentilatorSupport;
	}

	public void setDcVentilatorSupport(String dcVentilatorSupport) {
		this.dcVentilatorSupport = dcVentilatorSupport;
	}

	public String getOldVentilatorSupport() {
		return oldVentilatorSupport;
	}

	public void setOldVentilatorSupport(String oldVentilatorSupport) {
		this.oldVentilatorSupport = oldVentilatorSupport;
	}

	public Integer getWintageBufferUtilAmt() {
		return wintageBufferUtilAmt;
	}

	public void setWintageBufferUtilAmt(Integer wintageBufferUtilAmt) {
		this.wintageBufferUtilAmt = wintageBufferUtilAmt;
	}

	public Integer getNacBufferUtilAmt() {
		return nacBufferUtilAmt;
	}

	public void setNacBufferUtilAmt(Integer nacBufferUtilAmt) {
		this.nacBufferUtilAmt = nacBufferUtilAmt;
	}

	public String getRejectionRemarks2() {
		return rejectionRemarks2;
	}

	public void setRejectionRemarks2(String rejectionRemarks2) {
		this.rejectionRemarks2 = rejectionRemarks2;
	}
	
	public String getProportionateFlag() {
		return proportionateFlag;
	}

	public void setProportionateFlag(String proportionateFlag) {
		this.proportionateFlag = proportionateFlag;
	}

	public MastersValue getDecisionChangeReason() {
		return decisionChangeReason;
	}

	public void setDecisionChangeReason(MastersValue decisionChangeReason) {
		this.decisionChangeReason = decisionChangeReason;
	}

	public String getDecisionChangeRemarks() {
		return decisionChangeRemarks;
	}

	public void setDecisionChangeRemarks(String decisionChangeRemarks) {
		this.decisionChangeRemarks = decisionChangeRemarks;
	}

	public MastersValue getOldRoomCategory() {
		return oldRoomCategory;
	}

	public void setOldRoomCategory(MastersValue oldRoomCategory) {
		this.oldRoomCategory = oldRoomCategory;
	}

	public String getRcFlag() {
		return rcFlag;
	}

	public void setRcFlag(String rcFlag) {
		this.rcFlag = rcFlag;
	}

	public String getDcFlag() {
		return dcFlag;
	}

	public void setDcFlag(String dcFlag) {
		this.dcFlag = dcFlag;
	}

	public MastersValue getCocktailDrug() {
		return cocktailDrug;
	}

	public void setCocktailDrug(MastersValue cocktailDrug) {
		this.cocktailDrug = cocktailDrug;
	}

	public MastersValue getCovid19Variant() {
		return covid19Variant;
	}

	public void setCovid19Variant(MastersValue covid19Variant) {
		this.covid19Variant = covid19Variant;
	}

	public MastersValue getOldCocktailDrug() {
		return oldCocktailDrug;
	}

	public void setOldCocktailDrug(MastersValue oldCocktailDrug) {
		this.oldCocktailDrug = oldCocktailDrug;
	}

	public MastersValue getOldCovid19Variant() {
		return oldCovid19Variant;
	}

	public void setOldCovid19Variant(MastersValue oldCovid19Variant) {
		this.oldCovid19Variant = oldCovid19Variant;
	}
	
	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
}
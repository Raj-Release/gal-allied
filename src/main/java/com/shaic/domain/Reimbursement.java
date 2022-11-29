	package com.shaic.domain;

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
import com.shaic.domain.preauth.CriticalIllnessMaster;
import com.shaic.domain.preauth.Stage;
/**
 * 
 * @author Lakshminarayana
 * 
 * The persistent class for the IMS_CLS_REIMBURSEMENT database table.
 * 
 */

@Entity
@Table(name="IMS_CLS_REIMBURSEMENT")
@NamedQueries({
@NamedQuery(name="Reimbursement.findAll", query="SELECT r FROM Reimbursement r"),
@NamedQuery(name ="Reimbursement.findByKey",query="SELECT r FROM Reimbursement r WHERE r.key = :primaryKey"),
@NamedQuery(name="Reimbursement.findByIntimationKey", query="SELECT r FROM Reimbursement r WHERE r.claim is not null and r.claim.intimation is not null and r.claim.intimation.key = :intimationKey"),
@NamedQuery(name="Reimbursement.findByIntimationNumber", query="SELECT r FROM Reimbursement r WHERE r.claim is not null and r.claim.intimation is not null and r.claim.intimation.intimationId = :intimationNumber"),
@NamedQuery(name="Reimbursement.findByPolicyNumber", query="SELECT r FROM Reimbursement r WHERE r.claim is not null and r.claim.intimation is not null and r.claim.intimation.policy is not null and r.claim.intimation.policy.policyNumber = :policyNumber"),
@NamedQuery(name="Reimbursement.findByClaimKey", query="SELECT r FROM Reimbursement r WHERE r.claim is not null and r.claim.key = :claimKey order by r.key asc"),
@NamedQuery(name="Reimbursement.findByClaimKeyForReconsideration", query="SELECT f FROM Reimbursement f where f.key in (SELECT MAX(r.key) FROM Reimbursement r WHERE r.claim is not null and r.claim.key = :claimKey GROUP BY r.rodNumber) order by f.key asc"),
@NamedQuery(name="Reimbursement.findByClaimKeyAndStageId", query="SELECT r FROM Reimbursement r WHERE r.claim is not null and r.claim.key = :claimKey and r.stage.key in (:stageList)"),
@NamedQuery(name = "Reimbursement.CountAckByClaimKey", query = "SELECT count(o) FROM Reimbursement o where o.claim.key = :claimkey"),
@NamedQuery(name = "Reimbursement.getAckandRodKey", query = "SELECT max(o.key) , max(o.docAcknowLedgement.key) from Reimbursement o where o.claim.key = :claimkey"),
@NamedQuery(name = "Reimbursement.getRodkeyByClaim", query = "SELECT o from Reimbursement o where o.claim.key = :claimkey"),
@NamedQuery(name = "Reimbursement.getRodAscendingOrder",query = "SELECT o from Reimbursement o where o.claim.key = :claimkey order by o.key asc"),
@NamedQuery(name = "Reimbursement.findByAcknowledgement",query = "SELECT o from Reimbursement o where o.docAcknowLedgement.key = :docAcknowledgmentKey"),
@NamedQuery(name="Reimbursement.findLatestRODByClaimKey", query="SELECT r FROM Reimbursement r WHERE  r.claim is not null and r.claim.key = :claimKey order by r.key desc"),
@NamedQuery(name="Reimbursement.findFirstRODByClaimKey", query="SELECT r FROM Reimbursement r WHERE  r.claim is not null and r.claim.key = :claimKey order by r.key asc"),
@NamedQuery(name="Reimbursement.findByStatus", query = "SELECT r.claim.key, r.key , r.status.key , r.stage.key , r.claim.intimation.cpuCode.cpuCode, r.claim.claimType.value FROM Reimbursement r  WHERE  r.stage.key between :fromStage and :toStage and  r.claim.intimation.cpuCode.cpuCode =:cpuCode group by r.claim.key, r.key , r.status.key , r.stage.key , r.claim.intimation.cpuCode.cpuCode , r.claim.claimType.value order by  r.claim.intimation.cpuCode.cpuCode asc"),
@NamedQuery(name="Reimbursement.findByStatusId", query = "SELECT r.claim.key, r.key , r.status.key , r.stage.key , r.claim.intimation.cpuCode.cpuCode, r.claim.claimType.value FROM Reimbursement r  WHERE  r.stage.key between :fromStage and :toStage and r.createdDate >= :fromDate and  r.createdDate <= :endDate and r.claim.intimation.cpuCode.cpuCode =:cpuCode group by r.claim.key, r.key , r.status.key , r.stage.key , r.claim.intimation.cpuCode.cpuCode , r.claim.claimType.value order by  r.claim.intimation.cpuCode.cpuCode asc"),
@NamedQuery(name="Reimbursement.findRodByNumber", query="SELECT r FROM Reimbursement r WHERE  r.rodNumber = :rodNumber"),
@NamedQuery(name="Reimbursement.findLatestRODByRodNumberWise", query="SELECT r FROM Reimbursement r WHERE  r.claim is not null and r.claim.key = :claimKey order by r.rodNumber desc"),
@NamedQuery(name="Reimbursement.findByStatusForQuery", query="SELECT r FROM Reimbursement r WHERE  r.claim.key = :claimKey and r.status.key IN (:statusList)"),
@NamedQuery(name="Reimbursement.findSettledRod", query="SELECT r FROM Reimbursement r WHERE r.claim is not null and r.claim.key = :claimKey and r.status.key = :statusId order by r.key asc"),
@NamedQuery(name="Reimbursement.findLatestNonCanceledRODByClaimKey", query="SELECT r FROM Reimbursement r WHERE  r.claim is not null and r.claim.key = :claimKey and r.docAcknowLedgement.status.key <> :statusId  order by r.key desc"),
@NamedQuery(name="Reimbursement.findByClaimKeyForAddAdditionalDocs", query="SELECT r FROM Reimbursement r WHERE r.claim is not null and r.claim.key = :claimKey and r.status.key <> 103 order by r.key asc"),
@NamedQuery(name="Reimbursement.findLatestNonCanceledRODACKByClaimKey", query="SELECT r FROM Reimbursement r WHERE  r.claim is not null and r.claim.key = :claimKey and (r.docAcknowLedgement.status.key <> :ackcancelstatusId  or r.status.key <> :rodcancelstatusId) order by r.key desc"),
@NamedQuery(name="Reimbursement.getRodNumber", query="SELECT distinct(r.rodNumber) FROM Reimbursement r WHERE  r.claim.key = :claimKey"),
@NamedQuery(name="Reimbursement.findRodByNumberWise", query="SELECT r FROM Reimbursement r WHERE  r.rodNumber = :rodNumber order by r.key desc"),
@NamedQuery(name="Reimbursement.findByIntimationRodNumber", query="SELECT r FROM Reimbursement r WHERE  r.claim.intimation.key = :iKey AND r.rodNumber = :rodNumber"),
@NamedQuery(name="Reimbursement.findLatestRODByClaimKeyStatus", query="SELECT r FROM Reimbursement r WHERE  r.claim is not null and r.claim.key = :claimKey and r.status.key not in (148,149,150,160,179,188) order by r.key desc"),
@NamedQuery(name="Reimbursement.findReimbursementParentKey", query="SELECT r FROM Reimbursement r WHERE  r.parentKey = :parentKey order by r.key desc"),
@NamedQuery(name="Reimbursement.findByClaimKeyForReceivedPhyDocs", query="SELECT r FROM Reimbursement r WHERE r.claim is not null and r.claim.key = :claimKey order by r.key asc"),
@NamedQuery(name="Reimbursement.findCorrectionRODACKByClaimKey", query="SELECT r FROM Reimbursement r WHERE  r.claim is not null and r.claim.key = :claimKey and r.dcFlag ='Y' and (r.docAcknowLedgement.hospitalisationFlag='Y' or r.docAcknowLedgement.partialHospitalisationFlag = 'Y') order by r.key desc"),
@NamedQuery(name="Reimbursement.getRodKeysByPolicyKey", query="SELECT r.key FROM Reimbursement r WHERE  r.status.key in (161) and r.claim is not null and r.claim.intimation is not null and r.claim.intimation.policy is not null and r.claim.intimation.policy.key = :policykey"),
@NamedQuery(name="Reimbursement.getRodNotSetledByPolyKeyAndRodKey", query="SELECT r.key FROM Reimbursement r WHERE  r.status.key not in (161) and r.claim is not null and r.claim.intimation is not null and r.claim.intimation.policy is not null and r.claim.intimation.policy.key = :policykey and r.key <> :primaryKey"),
})
public class Reimbursement extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3083370657042450986L;


	@Id
	@SequenceGenerator(name="IMS_REIMBURSEMENT_KEY_GENERATOR", sequenceName = "SEQ_CASHLESS_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_REIMBURSEMENT_KEY_GENERATOR" )
	@Column(name="REIMBURSEMENT_KEY")
	private Long key;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="DOC_ACKNOWLEDGEMENT_KEY", nullable=false)
	private DocAcknowledgement docAcknowLedgement;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CLAIM_KEY", nullable=false)
	private Claim claim;
	
	@Column(name="ROD_NUMBER")
	private String rodNumber;
	
	@Column(name="BANK_ID")    
	private Long bankId;
	
	/*@OneToOne
	@JoinColumn(name="BANK_ID", nullable=false)
	private BankMaster bankId;   */
	
	
	@Column(name="PAYMENT_MODE_ID")
	private Long paymentModeId;
	
	
	@Column(name="PAYEE_NAME")
	private String payeeName;    
	
	@Column(name="NAME_AS_PER_BANK_ACC")
	private String nameAsPerBankAccount;
	
	@Column(name="PAYEE_EMAIL_ID")
	private String payeeEmailId;
		
	@Column(name="PAN_NUMBER")
	private String panNumber;  
	
	@Column(name="REASON_FOR_CHANGE")
	private String reasonForChange; 
	
	@Column(name="LEGAL_HEIR_FIRST_NAME")
	private String legalHeirFirstName; 
	
	@Column(name="LEGAL_HEIR_MIDDLE_NAME")
	private String legalHeirMiddleName; 
	
	@Column(name="LEGAL_HEIR_LAST_NAME")
	private String legalHeirLastName; 
	
	@Column(name="PAYABLE_AT")
	private String payableAt;  
	
	@Column(name="ACCOUNT_NUMBER")
	private String accountNumber;
	
	@Column(name="ACCOUNT_TYPE")
	private String accountType;
	
	@Temporal(TemporalType.DATE)
	@Column(name="DATE_OF_ADMISSION")
	private Date dateOfAdmission;
	
	@Column(name="MEDICAL_COMPLETED_DATE")
	private Timestamp medicalCompletedDate;
	
	@Column(name="DOA_CHANGE_REASON")
	private String doaChangeReason;
			
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ROOM_CATEGORY_ID",nullable=true)
	private MastersValue roomCategory;    
	
	@Column(name="NUMBER_OF_DAYS")
	private Integer numberOfDays;     
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "NATURE_OF_TREATMENT_ID", nullable = true)
	private MastersValue natureOfTreatment;
	
	@Temporal(TemporalType.DATE)
	@Column(name="CONSULTATION_DATE")
	private Date consultationDate;          
	
	@Column(name="CRITICAL_ILLNESS_FLAG")
	private Integer criticalIllnessFlag;     
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CRITICAL_ILLNESS_ID",nullable=true)
	private CriticalIllnessMaster criticalIllness;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATE_OF_DISCHARGE")
	private Date dateOfDischarge;  
		
	@Column(name="CORPORATE_BUFFER_FLAG")
	private Integer corporateBufferFlag;     
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ILLNESS_ID", nullable=true)
	private MastersValue illness;
	
	@Column(name="AUTOMATIC_RESTORATION")
	private String automaticRestoration;
		
	@Column(name="SYSTEM_OF_MEDICINE")
	private String systemOfMedicine;  
	
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
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TREATMENT_TYPE_ID", nullable = true)
	private MastersValue treatmentType; 
		
	@Column(name="TREATMENT_REMARKS")
	private String treatmentRemarks; 
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PATIENT_STATUS_ID", nullable = true)
	private MastersValue patientStatus;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATE_OF_DEATH")
	private Date dateOfDeath;  
	
	@Column(name="DEATH_REASON")
	private String deathReason; 
			
	@Column(name="REJECTION_CATEGORY_ID")
	private Long rejectionCategoryId;    
	
	@Column(name="REJ_SUB_CATEGORY_ID")
	private Long rejSubCategoryId;
	
	@Column(name="REJECTION_REMARKS")
	private String rejectionRemarks;
	
	@Column(name="REJECTION_REMARKS2")
	private String rejectionRemarks2;
	
	@Column(name="APPROVAL_REMARKS")
	private String approvalRemarks; 
			
	@Column(name="BILLING_REMARKS")
	private String billingRemarks; 
			
	@Column(name="PRE_HOSPITALIZATION_DAYS")
	private Integer preHospitalizationDays;     
			
	@Column(name="POST_HOSPITALIZATION_DAYS")
	private Integer postHospitalizationDays;     
		
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "DOMICILLARY_FLAG", length = 1)
	private String domicillary;   
			
	@Column(name="BILLING_APPROVED_AMOUNT")
	private Double billingApprovedAmount;  
			
	@Column(name="FINANCIAL_APPROVED_AMOUNT")
	private Double financialApprovedAmount;  
			
	@Column(name="APPROVED_AMOUNT")
	private Double approvedAmount;  
	
	@Column(name="PROCESS_CLM_TYPE")
	private String processClaimType;  
	
	/*@Column(name = "FA_HOSP_CLMED_AMT")
	private Integer faHospClaimedAmount;
	
	@Column(name = "FA_PRE_HOSP_CLAIM_AMT")
	private Integer faPreHospClaimedAmount;
	
	@Column(name = "FA_POST_HOSP_CLAIM_AMT")
	private Integer faPostHospClaimedAmount;
	
	@Column(name = "FA_OTHER_BENEFITS_CLAIM_AMT")
	private Integer faOtherBenefitsAmount;*/
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "FA_DOC_VERIFIED", length = 1)
	private String faDocumentVerifiedFlag;
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "INVESTIGATION_REVIEWED", length = 1)
	private String investigationReportReview;
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "SKIP_ZMR_FLAG", length = 1)
	private String skipZmrFlag;
	
	@Column(name="CAUSE_OF_LOSS")
	private Long causeOfLoss;
	
	@Column(name="NATURE_OF_LOSS")
	private Long natureOfLoss;
		
	@Column(name="CATASTROPHIC_LOSS")
	private Long catastrophicLoss;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="OLD_ROOM_CATEGORY_ID",nullable=true)
	private MastersValue oldRoomCategory;
	
	@Column(name="CRCN_RC_FLAG")
	private String rcFlag;
	
	@Column(name="DC_HCODING_FLAG")
	private String dcFlag;

	@Column(name = "IMPLANT_FLAG")
	private String implantFlag;
	
	@Column(name = "REASON_FOR_ICD_EXCLSN")
	private String reasonForIcdExclusion;
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "VENTILATOR_SUPPORT", length = 1)
	private String ventilatorSupport;
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "DC_VENTILATOR_SUPPORT", length = 1)
	private String dcVentilatorSupport;
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "OLD_VENTILATOR_SUPPORT ", length = 1)
	private String oldVentilatorSupport;
	
	@Column(name = "WINTAGE_BUFFER_AMT")
	private Integer wintageBufferUtilAmt;
	
	@Column(name = "NACB_BUFFER_AMT")
	private Integer nacBufferUtilAmt;
	
	@Column(name = "BILLING_FA_AA_SUBMIT_TYPE")
	private String billingFaAutoAllocSuBType;
	
	@Column(name = "BILLING_FA_AA_SUB_DATE")
	private Timestamp billingFaAutoAllocSuBDate;

	@Column(name = "BILLING_FA_AA_USER_ID")
	private String billingFaAutoAllocSuBUser;
	
	@Column(name = "AUTO_CANCEL_REMARKS")
	private String autoCancelRemarks;
	
	@Column(name = "AA_GMC_ICD_FLAG")
	private String billingAutoAllocIcdFlag;
	
	@Column(name = "AA_MA_CANCEL_REMARKS")
	private String autoCancelMARemarks;
	
	@Column(name = "MA_AA_SUBMIT_TYPE")
	private String claimRequestAutoAllocSuBType;
	
	@Column(name = "AA_MA_SUBMIT_DATE")
	private Timestamp claimRequestAutoAllocSuBDate;

	@Column(name = "AA_MA_SUBMIT_USER")
	private String claimRequestAutoAllocSuBUser;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="DECISION_CHANGE_REASON", nullable=true)
	private MastersValue decisionChangeReason;
	
	@Column(name = "DECISION_CHANGE_REMARKS")
	private String decisionChangeRemarks;
	
	@Column(name = "REMARKS_BILL_ENTRY")
	private String remarksBillEntry;
	
	@Column(name = "GRIEVANCE_REPRESENTATION")
	private String grievanceRepresentation;
	
	
	public String getReasonForIcdExclusion() {
		return reasonForIcdExclusion;
	}

	public void setReasonForIcdExclusion(String reasonForIcdExclusion) {
		this.reasonForIcdExclusion = reasonForIcdExclusion;
	}
	
	public Long getCauseOfLoss() {
		return causeOfLoss;
	}

	public void setCauseOfLoss(Long causeOfLoss) {
		this.causeOfLoss = causeOfLoss;
	}

	public Long getNatureOfLoss() {
		return natureOfLoss;
	}

	public void setNatureOfLoss(Long natureOfLoss) {
		this.natureOfLoss = natureOfLoss;
	}

	public Long getCatastrophicLoss() {
		return catastrophicLoss;
	}

	public void setCatastrophicLoss(Long catastrophicLoss) {
		this.catastrophicLoss = catastrophicLoss;
	}

	/***
	 * Transaction Number and transaction date is used for view claim status
	 */
	@Transient
	private String transactionNumber;
	
	

	@Transient
	private String claimTypeForPayment;
	
	@Transient
	private String claimPaymentMode;
	
	@Transient
	private String financialSetteledDate;

	@Transient
	private String transactionDate;
	
	@Column(name="INVESTIGATOR_CODE")
	private String investigatorCode;
	
	@Column(name="INVESTIGATOR_REMARKS")
	private String investigatorRemarks; 
			
//	@Column(name="NET_PAYABLE_AMOUNT")
//	private Double netPayableAmount;
//	
//	@Column(name="CLAIM_RESTRICTION_AMOUNT")
//	private Double claimRestrictionAmount;  
//			
//	@Column(name="CASHLESS_APPROVED_AMOUNT")
//	private Double cashlessApprovedAmount;  
			
//	@Column(name="PAYABLE_TO_HOSPITAL")
//	private Double payableToHospital;  
//			
//	@Column(name="PAYABLE_TO_INSURED")
//	private Double payableToInsured;  
//			
//	@Column(name="TDS_AMOUNT")
//	private Double tdsAmount;  
//			
//	@Column(name="AFTER_TDS_PAYABLE_TO_HOSPITAL")
//	private Double afterTdsPayableToHospital;  
//			
//	@Column(name="DEDUCTED_BALANCE_PREMIUM")
//	private Double deductedBalancePremium;  
//			
//	@Column(name="AFTER_PREMIUM_PAYABLE_INSURED")
//	private Double afterPremiumPayableInsured;  
			
	@Column(name="FINANCIAL_APPROVAL_REMARKS")
	private String financialApprovalRemarks; 
			
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;     
			
	@Column(name="OFFICE_CODE")
	private String officeCode;  
	
	@Column(name="MEDICAL_REMARKS")
	private String medicalRemarks;  
	
	@Column(name="DOCTOR_NOTE")
	private String doctorNote;  
			
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="STAGE_ID", nullable=false)
	private Stage stage;    
			
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="STATUS_ID",nullable=false)
	private Status status;    
			
	@Column(name="CREATED_BY")
	private String createdBy;  
			
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(nullable = true, columnDefinition = "VARCHAR(1)", name = "RELAPSE_FLAG", length = 1)
	private String relapseFlag;

	@Column(name = "RELAPSE_REMARKS")
	private String relapseRemarks;
	
	@Column(nullable = true, columnDefinition = "VARCHAR(1)", name = "COORDINATOR_FLAG", length = 1)
	private String coordinatorFlag;
	
	@Column(name="Clm_Amt_Other_Insurer_Hosp")
	private Double otherInsurerHospAmt;  
	
	@Column(name="Clm_Amt_Other_Insurer_Post")
	private Double otherInsurerPostHospAmt;  
	
	@Column(name="Clm_Amt_Other_Insurer_Pre")
	private Double otherInsurerPreHospAmt;  
	
	@Column(nullable = true, columnDefinition = "VARCHAR(1)", name = "Verified_Policy_Schedule_Flag", length = 1)
	private String verifiedPolicyScheduleFlag;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="BILLING_COMPLETED_DATE")
	private Date billingCompletedDate;  
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FINANCIAL_COMPLETED_DATE")
	private Date financialCompletedDate;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;

	@Column(name = "CURRENT_PROVISION_AMOUNT")
	private Double currentProvisionAmt;
	
	@Column(name="Prorate_Percentage")
	private Double prorataPercentage;  
	
	@Column(nullable = true, columnDefinition = "VARCHAR(1)", name = "Prorate_Deduction_Flag", length = 1)
	private String prorataDeductionFlag;
	
	@Column(nullable = true, columnDefinition = "VARCHAR(1)", name = "Package_Available_Flag", length = 1)
	private String packageAvailableFlag;
	
	@Column(name="CANCELLATION_REASON_ID", nullable = true)
	private Long cancellationReasonId;
	
	@Column(name="CANCELLATION_REMARKS", nullable = true)
	private String cancellationRemarks;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="INSURED_KEY", nullable=true)
	private Insured insuredKey;
	
	@Column(name = "HOSPITAL_ID")
	private Long hospitalId;
	
	@Column(name="SECTION_CATEGORY_ID", nullable = true)
	private Long sectionCategory;
	
	@Column(name="DELIVERY_TYPE_ID", nullable = true)
	private Long typeOfDelivery;
	
	@Temporal(TemporalType.DATE)
	@Column(name="TREATMENT_START_DATE")
	private Date treatmentStartDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name="TREATMENT_END_DATE")
	private Date treatmentEndDate;
	
	@Column(name="TREATMENT_DAYS")
	private Integer domicillaryNumberOfDays;     
	
	@Column(name = "AMT_CONS_COPAY_PRCNT", nullable = true)
	private Double amtConsCopayPercentage;
	
	@Column(name = "BAL_SI_COPAY_PRCNT", nullable = true)
	private Long balanceSICopayPercentage;
	
	@Column(name = "AMT_CONS_COPAY_AMT")
	private Double amtConsAftCopayAmount;
	
	@Column(name = "BAL_SI_COPAY_AMT")
	private Double balanceSIAftCopayAmt;
	
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="BILL_ENTRY_DATE")
	private Date billEntryDate;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ZONAL_DATE")
	private Date zonalDate;
	
	@Column(name = "OTHER_BENEFITS_APPROVED_AMT")
	private Double otherBenefitApprovedAmt;
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "OTH_INSR_SETL_FLAG", length = 1)
//	@Transient
	private String  otherInsurerApplicableFlag;   
	
	
	@Column(name = "BILL_ENTRY_REMARKS")
//	@Transient
	private String billEntryRemarks;
	
	@Column(name="BILL_ENTRY_AMT")
//	@Transient
	private Double billEntryAmt;  
//	@Transient
	@Column(name = "COPAY_REMARKS")
	private String copayRemarks;
	

	@Column(name = "PREMIUM_AMT")
	private Double premiumAmt;
	
	@Column(name = "CORPORATE_BUFFER_LIMIT")
	private Integer corporateUtilizedAmt;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ACC_CAUSE_ID", nullable=false)
	private MastersValue accidentCauseId;
	
	@Column(name = "PED_DISABILITY_FLAG")
	private String pedFlag;
	
	@Column(name = "PED_DISABILITY_DTLS")
	private String pedDisablitiyDetails;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="BENEFITS_ID", nullable=false)
	private MastersValue benefitsId;
	
	
	@Column(name = "ADD_APPROVED_AMOUNT")
	private Double addOnCoversApprovedAmount;
	
	@Column(name = "OPT_APPROVED_AMOUNT")
	private Double optionalApprovedAmount;
	
	@Column(name = "CLM_APPROVAL_AMOUNT")
	private Double claimApprovalAmount;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CLM_APPROVAL_DATE")
	private Date claimApprovalDate;
	
	@Column(name = "BEN_APR_AMT")
	private Double benApprovedAmt;

	@Column(name = "NOMINEE_NAME")
	private String nomineeName;
	
	@Column(name = "NOMINEE_ADDRESS")
	private String nomineeAddr;
	

	@Column(name = "CLM_APPROVAL_REMARKS")
	private String claimApprovalRemarks;
	
	@Column(name = "WORKPLACE")
	private String workPlace;
	
	@Column(name = "UN_NAMED_KEY")
	private Long unNamedKey;
	
	@Column(name="PAYMODE_CHANGE_REASON")
	private String payModeChangeReason; 
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FVR_NOT_REQUIRED_REMARKS", nullable = true)
	private MastersValue fvrNotRequiredRemarks;
	
	@Column(name="UPDATE_PAYMENT_DTLS_FLAG")
	private String updatePaymentDtlsFlag; 
	
	@Column(name = "PAYMENT_CPU_CODE")
	private Long paymentCpuCode;
	
	@Column(name = "FA_INTERNAL_REMARKS")
	private String faInternalRemarks;
	
	@Column(name = "BILLING_INTERNAL_REMARKS")
	private String billingInternalRemarks;
	
	@Column(name = "REIMBURSEMENT_PARENT_KEY")
	private Long parentKey;
	
	@Column(name = "REIMBURSEMENT_VERSION")
	private Long version;
	
	@Column(name = "RECONSIDERATION_REQUEST")
	private String reconsiderationRequest;
	
	@Column(name = "FVR_ALERT_FLAG")
	private String fvrAlertFlag;

	@Column(name = "FVR_OTHER_REMARKS")
	private String fvrNotRequiredOthersRemarks;
	
	@Column(name = "HOSPITAL_SCORE_FLAG")
	private String scoringFlag;

	@Column(name="HOS_CLAIMED_AMT")
	private Double amountClaimedFromHospital;  
	
	@Column(name = "CVC_AUDIT_FLAG")
	private String cvcAuditFlag;
	
	@Column(name = "CVC_LOCK_FLAG")
	private String cvcLockFlag;
	
	@Column(name = "NON_ALLOPATH_DIFF_AMT")
	private Double nonAllopathicDiffAmt;
	
	@Column(name = "NON_ALLOPATH_APR_AMT")
	private Double nonAllopathicApprAmt;
	
	@Column(name = "PHC_BENEFIT_AMT")
	private Double prodBenefitAmount;

	@Column(name = "ACCOUNT_PREFERENCE")
	private String accountPreference;

	//	@OneToOne(fetch = FetchType.LAZY)
	@Column(name="PHC_DIAGNOSIS_ID")
	private Long prodDiagnosisID;
	
	@Column(name = "PHC_BENEFIT_DUE_ID")
	private Long prodBenefitDueToID;
	
	@Column(name = "PHC_BENEFIT_FLAG")
	private String prodBenefitFlag;
	
	@Column(name = "HOSPITAL_DISCOUNT_FLAG")
	private String hospitalDiscountFlag;

	
	@Column(name = "PHC_DAYCARE_FLAG")
	private String phcDayCareFlag;
	
	@Column(name="PHC_DAYCARE_ID")
	private Long phcDayCareID;
	

	@Column(name="REJECT_RECONSIDERATION_FLAG")
	private String reconsiderationFlagReq;
	

	@Column(name="REJECT_RECONSIDERATION_REMARKS")
	private String reconsiderationFlagRemark;
	
	@Column(name="NOMINEE_FLG")
	private String nomineeFlag;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="COVID_TREATMENT_ID", nullable=true)
	private MastersValue covidTreatmentId;
	
	@Column (name = "TYPE_OF_ADMISSION")
	private Long typeOfAdmission;
	
	@Column (name = "OLD_TYPE_OF_ADMISSION")
	private Long oldTypeOfAdmission;
	
	@Column(name="DC_TOA_FLAG")
	private String dcTOAFlag;
	
	@Column(name="ICAC_FLAG")
	private String icacFlag;
	
	@Column(name="EMERGENCY_EXCLUSION_PROVIDER")
	private String emergencyFlag;
	
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

	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public DocAcknowledgement getDocAcknowLedgement() {
		return docAcknowLedgement;
	}

	public void setDocAcknowLedgement(DocAcknowledgement docAcknowLedgement) {
		this.docAcknowLedgement = docAcknowLedgement;
	}

	public Claim getClaim() {
		return claim;
	}

	public void setClaim(Claim claim) {
		this.claim = claim;
	}

	public String getRodNumber() {
		return rodNumber;
	}

	public void setRodNumber(String rodNumber) {
		this.rodNumber = rodNumber;
	}

	public Long getBankId() {
		return bankId;
	}

	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}

	public String getPayeeEmailId() {
		return payeeEmailId;
	}

	public void setPayeeEmailId(String payeeEmailId) {
		this.payeeEmailId = payeeEmailId;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	public String getReasonForChange() {
		return reasonForChange;
	}

	public void setReasonForChange(String reasonForChange) {
		this.reasonForChange = reasonForChange;
	}

	public String getLegalHeirFirstName() {
		return legalHeirFirstName;
	}

	public void setLegalHeirFirstName(String legalHeirFirstName) {
		this.legalHeirFirstName = legalHeirFirstName;
	}

	public String getLegalHeirMiddleName() {
		return legalHeirMiddleName;
	}

	public void setLegalHeirMiddleName(String legalHeirMiddleName) {
		this.legalHeirMiddleName = legalHeirMiddleName;
	}

	public Double getOtherInsurerHospAmt() {
		return otherInsurerHospAmt;
	}

	public Double getOtherInsurerPostHospAmt() {
		return otherInsurerPostHospAmt;
	}

	public Double getOtherInsurerPreHospAmt() {
		return otherInsurerPreHospAmt;
	}

	public String getVerifiedPolicyScheduleFlag() {
		return verifiedPolicyScheduleFlag;
	}

	public Double getProrataPercentage() {
		return prorataPercentage;
	}

	public String getProrataDeductionFlag() {
		return prorataDeductionFlag;
	}

	public String getPackageAvailableFlag() {
		return packageAvailableFlag;
	}

	public void setOtherInsurerHospAmt(Double otherInsurerHospAmt) {
		this.otherInsurerHospAmt = otherInsurerHospAmt;
	}

	public void setOtherInsurerPostHospAmt(Double otherInsurerPostHospAmt) {
		this.otherInsurerPostHospAmt = otherInsurerPostHospAmt;
	}

	public void setOtherInsurerPreHospAmt(Double otherInsurerPreHospAmt) {
		this.otherInsurerPreHospAmt = otherInsurerPreHospAmt;
	}

	public void setVerifiedPolicyScheduleFlag(String verifiedPolicyScheduleFlag) {
		this.verifiedPolicyScheduleFlag = verifiedPolicyScheduleFlag;
	}

	public void setProrataPercentage(Double prorataPercentage) {
		this.prorataPercentage = prorataPercentage;
	}

	public void setProrataDeductionFlag(String prorataDeductionFlag) {
		this.prorataDeductionFlag = prorataDeductionFlag;
	}

	public void setPackageAvailableFlag(String packageAvailableFlag) {
		this.packageAvailableFlag = packageAvailableFlag;
	}

	public String getMedicalRemarks() {
		return medicalRemarks;
	}

	public void setMedicalRemarks(String medicalRemarks) {
		this.medicalRemarks = medicalRemarks;
	}

	public String getDoctorNote() {
		return doctorNote;
	}

	public void setDoctorNote(String doctorNote) {
		this.doctorNote = doctorNote;
	}

	public String getLegalHeirLastName() {
		return legalHeirLastName;
	}

	public void setLegalHeirLastName(String legalHeirLastName) {
		this.legalHeirLastName = legalHeirLastName;
	}

	public String getPayableAt() {
		return payableAt;
	}

	public void setPayableAt(String payableAt) {
		this.payableAt = payableAt;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Date getDateOfAdmission() {
		return dateOfAdmission;
	}

	public void setDateOfAdmission(Date dateOfAdmission) {
		this.dateOfAdmission = dateOfAdmission;
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

	public String getCoordinatorFlag() {
		return coordinatorFlag;
	}

	public void setCoordinatorFlag(String coordinatorFlag) {
		this.coordinatorFlag = coordinatorFlag;
	}

	public String getDoaChangeReason() {
		return doaChangeReason;
	}

	public void setDoaChangeReason(String doaChangeReason) {
		this.doaChangeReason = doaChangeReason;
	}


	public Integer getNumberOfDays() {
		return numberOfDays;
	}

	public void setNumberOfDays(Integer numberOfDays) {
		this.numberOfDays = numberOfDays;
	}


	public Date getConsultationDate() {
		return consultationDate;
	}

	public void setConsultationDate(Date consultationDate) {
		this.consultationDate = consultationDate;
	}

	public Integer getCriticalIllnessFlag() {
		return criticalIllnessFlag;
	}

	public void setCriticalIllnessFlag(Integer criticalIllnessFlag) {
		this.criticalIllnessFlag = criticalIllnessFlag;
	}


	public Date getDateOfDischarge() {
		return dateOfDischarge;
	}

	public void setDateOfDischarge(Date dateOfDischarge) {
		this.dateOfDischarge = dateOfDischarge;
	}

	public Integer getCorporateBufferFlag() {
		return corporateBufferFlag;
	}

	public void setCorporateBufferFlag(Integer corporateBufferFlag) {
		this.corporateBufferFlag = corporateBufferFlag;
	}

	public String getAutomaticRestoration() {
		return automaticRestoration;
	}

	public void setAutomaticRestoration(String automaticRestoration) {
		this.automaticRestoration = automaticRestoration;
	}

	public String getSystemOfMedicine() {
		return systemOfMedicine;
	}

	public void setSystemOfMedicine(String systemOfMedicine) {
		this.systemOfMedicine = systemOfMedicine;
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

	/*public String getAttachedPolicyReport() {
		return attachedPoliceReport;
	}

	public void setAttachedPolicyReport(String attachedPolicyReport) {
		this.attachedPoliceReport = attachedPolicyReport;
	}*/

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


	public String getTreatmentRemarks() {
		return treatmentRemarks;
	}

	public void setTreatmentRemarks(String treatmentRemarks) {
		this.treatmentRemarks = treatmentRemarks;
	}

	public Date getDateOfDeath() {
		return dateOfDeath;
	}

	public void setDateOfDeath(Date dateOfDeath) {
		this.dateOfDeath = dateOfDeath;
	}

	public String getDeathReason() {
		return deathReason;
	}

	public void setDeathReason(String deathReason) {
		this.deathReason = deathReason;
	}

	public Long getRejectionCategoryId() {
		return rejectionCategoryId;
	}

	public void setRejectionCategoryId(Long rejectionCategoryId) {
		this.rejectionCategoryId = rejectionCategoryId;
	}

	public String getRejectionRemarks() {
		return rejectionRemarks;
	}

	public void setRejectionRemarks(String rejectionRemarks) {
		this.rejectionRemarks = rejectionRemarks;
	}

	public String getApprovalRemarks() {
		return approvalRemarks;
	}

	public void setApprovalRemarks(String approvalRemarks) {
		this.approvalRemarks = approvalRemarks;
	}

	public String getBillingRemarks() {
		return billingRemarks;
	}

	public void setBillingRemarks(String billingRemarks) {
		this.billingRemarks = billingRemarks;
	}

	public Integer getPreHospitalizationDays() {
		return preHospitalizationDays;
	}

	public void setPreHospitalizationDays(Integer preHospitalizationDays) {
		this.preHospitalizationDays = preHospitalizationDays;
	}

	public Integer getPostHospitalizationDays() {
		return postHospitalizationDays;
	}

	public void setPostHospitalizationDays(Integer postHospitalizationDays) {
		this.postHospitalizationDays = postHospitalizationDays;
	}

	public String getDomicillary() {
		return domicillary;
	}

	public void setDomicillary(String domicillary) {
		this.domicillary = domicillary;
	}

	public Double getBillingApprovedAmount() {
		return billingApprovedAmount;
	}

	public void setBillingApprovedAmount(Double billingApprovedAmount) {
		this.billingApprovedAmount = billingApprovedAmount;
	}

	public Double getFinancialApprovedAmount() {
		return financialApprovedAmount;
	}

	public void setFinancialApprovedAmount(Double financialApprovedAmount) {
		this.financialApprovedAmount = financialApprovedAmount;
	}

	public Double getApprovedAmount() {
		return approvedAmount;
	}

	public void setApprovedAmount(Double approvedAmount) {
		this.approvedAmount = approvedAmount;
	}


	public String getFinancialApprovalRemarks() {
		return financialApprovalRemarks;
	}

	public void setFinancialApprovalRemarks(String financialApprovalRemarks) {
		this.financialApprovalRemarks = financialApprovalRemarks;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getOfficeCode() {
		return officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
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

	/**
	 * @return the payeeName
	 */
	public String getPayeeName() {
		return payeeName;
	}

	/**
	 * @param payeeName the payeeName to set
	 */
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}

	/**
	 * @return the paymentModeId
	 */
	public Long getPaymentModeId() {
		return paymentModeId;
	}

	/**
	 * @param paymentModeId the paymentModeId to set
	 */
	public void setPaymentModeId(Long paymentModeId) {
		this.paymentModeId = paymentModeId;
	}

	/**
	 * @return the attachedPoliceReport
	 */
	public String getAttachedPoliceReport() {
		return attachedPoliceReport;
	}

	public Date getBillingCompletedDate() {
		return billingCompletedDate;
	}

	public void setBillingCompletedDate(Date billingCompletedDate) {
		this.billingCompletedDate = billingCompletedDate;
	}

	public Date getFinancialCompletedDate() {
		return financialCompletedDate;
	}

	public void setFinancialCompletedDate(Date financialCompletedDate) {
		this.financialCompletedDate = financialCompletedDate;
	}

	/**
	 * @param attachedPoliceReport the attachedPoliceReport to set
	 */
	public void setAttachedPoliceReport(String attachedPoliceReport) {
		this.attachedPoliceReport = attachedPoliceReport;
	}

	public MastersValue getRoomCategory() {
		return roomCategory;
	}

	public void setRoomCategory(MastersValue roomCategory) {
		this.roomCategory = roomCategory;
	}

	public MastersValue getTreatmentType() {
		return treatmentType;
	}

	public void setTreatmentType(MastersValue treatmentType) {
		this.treatmentType = treatmentType;
	}


	public CriticalIllnessMaster getCriticalIllness() {
		return criticalIllness;
	}

	public void setCriticalIllness(CriticalIllnessMaster criticalIllness) {
		this.criticalIllness = criticalIllness;
	}

	public MastersValue getNatureOfTreatment() {
		return natureOfTreatment;
	}

	public void setNatureOfTreatment(MastersValue natureOfTreatment) {
		this.natureOfTreatment = natureOfTreatment;
	}

	public MastersValue getIllness() {
		return illness;
	}

	public void setIllness(MastersValue illness) {
		this.illness = illness;
	}

	public MastersValue getPatientStatus() {
		return patientStatus;
	}

	public void setPatientStatus(MastersValue patientStatus) {
		this.patientStatus = patientStatus;
	}

	public MastersValue getHopsitaliztionDueto() {
		return hopsitaliztionDueto;
	}

	public void setHopsitaliztionDueto(MastersValue hopsitaliztionDueto) {
		this.hopsitaliztionDueto = hopsitaliztionDueto;
	}

	public Date getDateOfDelivery() {
		return dateOfDelivery;
	}

	public void setDateOfDelivery(Date dateOfDelivery) {
		this.dateOfDelivery = dateOfDelivery;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Double getCurrentProvisionAmt() {
		return currentProvisionAmt;
	}

	public void setCurrentProvisionAmt(Double currentProvisionAmt) {
		this.currentProvisionAmt = currentProvisionAmt;
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

	public String getInvestigationReportReview() {
		return investigationReportReview;
	}

	public void setInvestigationReportReview(String investigationReportReview) {
		this.investigationReportReview = investigationReportReview;
	}

	public String getInvestigatorRemarks() {
		return investigatorRemarks;
	}

	public void setInvestigatorRemarks(String investigatorRemarks) {
		this.investigatorRemarks = investigatorRemarks;
	}

	public String getInvestigatorCode() {
		return investigatorCode;
	}

	public void setInvestigatorCode(String investigatorCode) {
		this.investigatorCode = investigatorCode;
	}

	public Timestamp getMedicalCompletedDate() {
		return medicalCompletedDate;
	}

	public void setMedicalCompletedDate(Timestamp medicalCompletedDate) {
		this.medicalCompletedDate = medicalCompletedDate;
	}

	public Long getCancellationReasonId() {
		return cancellationReasonId;
	}

	public void setCancellationReasonId(Long cancellationReasonId) {
		this.cancellationReasonId = cancellationReasonId;
	}

	public String getCancellationRemarks() {
		return cancellationRemarks;
	}

	public void setCancellationRemarks(String cancellationRemarks) {
		this.cancellationRemarks = cancellationRemarks;
	}

	public Insured getInsuredKey() {
		return insuredKey;
	}

	public void setInsuredKey(Insured insuredKey) {
		this.insuredKey = insuredKey;
	}

	public Long getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Long hospitalId) {
		this.hospitalId = hospitalId;
	}

	public Long getSectionCategory() {
		return sectionCategory;
	}

	public void setSectionCategory(Long sectionCategory) {
		this.sectionCategory = sectionCategory;
	}

	public Long getTypeOfDelivery() {
		return typeOfDelivery;
	}

	public void setTypeOfDelivery(Long typeOfDelivery) {
		this.typeOfDelivery = typeOfDelivery;
	}

	public Date getTreatmentStartDate() {
		return treatmentStartDate;
	}

	public void setTreatmentStartDate(Date treatmentStartDate) {
		this.treatmentStartDate = treatmentStartDate;
	}

	public Date getTreatmentEndDate() {
		return treatmentEndDate;
	}

	public void setTreatmentEndDate(Date treatmentEndDate) {
		this.treatmentEndDate = treatmentEndDate;
	}

	public Integer getDomicillaryNumberOfDays() {
		return domicillaryNumberOfDays;
	}

	public void setDomicillaryNumberOfDays(Integer domicillaryNumberOfDays) {
		this.domicillaryNumberOfDays = domicillaryNumberOfDays;
	}

	public Double getAmtConsCopayPercentage() {
		return amtConsCopayPercentage;
	}

	public void setAmtConsCopayPercentage(Double amtConsCopayPercentage) {
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

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}
	
	public String getClaimTypeForPayment() {
		return claimTypeForPayment;
	}

	public void setClaimTypeForPayment(String claimTypeForPayment) {
		this.claimTypeForPayment = claimTypeForPayment;
	}
	
	public String getClaimPaymentMode() {
		return claimPaymentMode;
	}

	public void setClaimPaymentMode(String claimPaymentMode) {
		this.claimPaymentMode = claimPaymentMode;
	}
	
	public String getFinancialSetteledDate() {
		return financialSetteledDate;
	}

	public void setFinancialSetteledDate(String financialSetteledDate) {
		this.financialSetteledDate = financialSetteledDate;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	public Date getBillEntryDate() {
		return billEntryDate;
	}

	public void setBillEntryDate(Date billEntryDate) {
		this.billEntryDate = billEntryDate;
	}

	public Date getZonalDate() {
		return zonalDate;
	}

	public void setZonalDate(Date zonalDate) {
		this.zonalDate = zonalDate;
	}

	public String getOtherInsurerApplicableFlag() {
		return otherInsurerApplicableFlag;
	}

	public void setOtherInsurerApplicableFlag(String otherInsurerApplicableFlag) {
		this.otherInsurerApplicableFlag = otherInsurerApplicableFlag;
	}

	public String getBillEntryRemarks() {
		return billEntryRemarks;
	}

	public void setBillEntryRemarks(String billEntryRemarks) {
		this.billEntryRemarks = billEntryRemarks;
	}
	public String getCopayRemarks() {
		return copayRemarks;
	}

	public void setCopayRemarks(String copayRemarks) {
		this.copayRemarks = copayRemarks;
	}


	public void setBillEntryAmt(Double billEntryAmt) {
		this.billEntryAmt = billEntryAmt;

	}

	public Double getPremiumAmt() {
		return premiumAmt;
	}

	public void setPremiumAmt(Double premiumAmt) {
		this.premiumAmt = premiumAmt;
}


	public String getSkipZmrFlag() {
		return skipZmrFlag;
	}

	public void setSkipZmrFlag(String skipZmrFlag) {
		this.skipZmrFlag = skipZmrFlag;
	}

	

	public String getPedFlag() {
		return pedFlag;
	}
	public Double getBillEntryAmt() {
		return billEntryAmt;
	}

	public void setPedFlag(String pedFlag) {
		this.pedFlag = pedFlag;
	}

	public MastersValue getBenefitsId() {
		return benefitsId;
	}

	public void setBenefitsId(MastersValue benefitsId) {
		this.benefitsId = benefitsId;
	}

	public MastersValue getAccidentCauseId() {
		return accidentCauseId;
	}

	public void setAccidentCauseId(MastersValue accidentCauseId) {
		this.accidentCauseId = accidentCauseId;
	}

	public String getPedDisablitiyDetails() {
		return pedDisablitiyDetails;
	}

	public void setPedDisablitiyDetails(String pedDisablitiyDetails) {
		this.pedDisablitiyDetails = pedDisablitiyDetails;
	}

	public String getProcessClaimType() {
		return processClaimType;
	}

	public void setProcessClaimType(String processClaimType) {
		this.processClaimType = processClaimType;
	}

	public Double getAddOnCoversApprovedAmount() {
		return addOnCoversApprovedAmount;
	}

	public void setAddOnCoversApprovedAmount(Double addOnCoversApprovedAmount) {
		this.addOnCoversApprovedAmount = addOnCoversApprovedAmount;
	}

	public Double getOptionalApprovedAmount() {
		return optionalApprovedAmount;
	}

	public void setOptionalApprovedAmount(Double optionalApprovedAmount) {
		this.optionalApprovedAmount = optionalApprovedAmount;
	}

	public Double getClaimApprovalAmount() {
		return claimApprovalAmount;
	}

	public void setClaimApprovalAmount(Double claimApprovalAmount) {
		this.claimApprovalAmount = claimApprovalAmount;
	}

	public Date getClaimApprovalDate() {
		return claimApprovalDate;
	}

	public void setClaimApprovalDate(Date claimApprovalDate) {
		this.claimApprovalDate = claimApprovalDate;
	}

	public Double getBenApprovedAmt() {
		return benApprovedAmt;
	}

	public void setBenApprovedAmt(Double benApprovedAmt) {
		this.benApprovedAmt = benApprovedAmt;
	}
	
	public String getNomineeName() {
		return nomineeName;
	}

	public void setNomineeName(String nomineeName) {
		this.nomineeName = nomineeName;
	}
	
	public String getNomineeAddr() {
		return nomineeAddr;
	}
	
	public void setNomineeAddr(String nomineeAddr) {
		this.nomineeAddr = nomineeAddr;
	}

	public String getClaimApprovalRemarks() {
		return claimApprovalRemarks;
	}

	public void setClaimApprovalRemarks(String claimApprovalRemarks) {
		this.claimApprovalRemarks = claimApprovalRemarks;
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
	
	public Double getOtherBenefitApprovedAmt() {
		return otherBenefitApprovedAmt;
	}

	public void setOtherBenefitApprovedAmt(Double otherBenefitApprovedAmt) {
		this.otherBenefitApprovedAmt = otherBenefitApprovedAmt;
	}

	public Integer getCorporateUtilizedAmt() {
		return corporateUtilizedAmt;
	}

	public void setCorporateUtilizedAmt(Integer corporateUtilizedAmt) {
		this.corporateUtilizedAmt = corporateUtilizedAmt;
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

	public String getPayModeChangeReason() {
		return payModeChangeReason;
	}

	public void setPayModeChangeReason(String payModeChangeReason) {
		this.payModeChangeReason = payModeChangeReason;
	}

	public String getFaDocumentVerifiedFlag() {
		return faDocumentVerifiedFlag;
	}

	public void setFaDocumentVerifiedFlag(String faDocumentVerifiedFlag) {
		this.faDocumentVerifiedFlag = faDocumentVerifiedFlag;
	}

	public MastersValue getFvrNotRequiredRemarks() {
		return fvrNotRequiredRemarks;
	}

	public void setFvrNotRequiredRemarks(MastersValue fvrNotRequiredRemarks) {
		this.fvrNotRequiredRemarks = fvrNotRequiredRemarks;
	}

	/*public Integer getFaHospClaimedAmount() {
		return faHospClaimedAmount;
	}

	public void setFaHospClaimedAmount(Integer faHospClaimedAmount) {
		this.faHospClaimedAmount = faHospClaimedAmount;
	}

	public Integer getFaPreHospClaimedAmount() {
		return faPreHospClaimedAmount;
	}

	public void setFaPreHospClaimedAmount(Integer faPreHospClaimedAmount) {
		this.faPreHospClaimedAmount = faPreHospClaimedAmount;
	}

	public Integer getFaPostHospClaimedAmount() {
		return faPostHospClaimedAmount;
	}

	public void setFaPostHospClaimedAmount(Integer faPostHospClaimedAmount) {
		this.faPostHospClaimedAmount = faPostHospClaimedAmount;
	}

	public Integer getFaOtherBenefitsAmount() {
		return faOtherBenefitsAmount;
	}

	public void setFaOtherBenefitsAmount(Integer faOtherBenefitsAmount) {
		this.faOtherBenefitsAmount = faOtherBenefitsAmount;
	}*/
	
	

	public String getUpdatePaymentDtlsFlag() {
		return updatePaymentDtlsFlag;
	}

	public void setUpdatePaymentDtlsFlag(String updatePaymentDtlsFlag) {
		this.updatePaymentDtlsFlag = updatePaymentDtlsFlag;
	}

	public Long getPaymentCpuCode() {
		return paymentCpuCode;
	}

	public void setPaymentCpuCode(Long paymentCpuCode) {
		this.paymentCpuCode = paymentCpuCode;
	}
	
	public String getBillingInternalRemarks() {
		return billingInternalRemarks;
	}
	public void setBillingInternalRemarks(String billingInternalRemarks) {
		this.billingInternalRemarks = billingInternalRemarks;
	}
	
	public String getFaInternalRemarks() {
		return faInternalRemarks;
	}
	public void setFaInternalRemarks(String faInternalRemarks) {
		this.faInternalRemarks = faInternalRemarks;
	}

	public Long getParentKey() {
		return parentKey;
	}

	public void setParentKey(Long parentKey) {
		this.parentKey = parentKey;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String getReconsiderationRequest() {
		return reconsiderationRequest;
	}

	public void setReconsiderationRequest(String reconsiderationRequest) {
		this.reconsiderationRequest = reconsiderationRequest;
	}

	public String getFvrAlertFlag() {
		return fvrAlertFlag;
	}

	public void setFvrAlertFlag(String fvrAlertFlag) {
		this.fvrAlertFlag = fvrAlertFlag;
	}	
	
	public String getFvrNotRequiredOthersRemarks() {
		return fvrNotRequiredOthersRemarks;
	}

	public void setFvrNotRequiredOthersRemarks(String fvrNotRequiredOthersRemarks) {
		this.fvrNotRequiredOthersRemarks = fvrNotRequiredOthersRemarks;
	}

	public Double getAmountClaimedFromHospital() {
		return amountClaimedFromHospital;
	}

	public void setAmountClaimedFromHospital(Double amountClaimedFromHospital) {
		this.amountClaimedFromHospital = amountClaimedFromHospital;
	}
	
	public String getScoringFlag() {
		return scoringFlag;
	}

	public void setScoringFlag(String scoringFlag) {
		this.scoringFlag = scoringFlag;
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

	public Double getNonAllopathicDiffAmt() {
		return nonAllopathicDiffAmt;
	}

	public void setNonAllopathicDiffAmt(Double nonAllopathicDiffAmt) {
		this.nonAllopathicDiffAmt = nonAllopathicDiffAmt;
	}

	public Double getNonAllopathicApprAmt() {
		return nonAllopathicApprAmt;
	}

	public void setNonAllopathicApprAmt(Double nonAllopathicApprAmt) {
		this.nonAllopathicApprAmt = nonAllopathicApprAmt;
	}

	public Long getRejSubCategoryId() {
		return rejSubCategoryId;
	}

	public void setRejSubCategoryId(Long rejSubCategoryId) {
		this.rejSubCategoryId = rejSubCategoryId;
	}

	public String getAccountPreference() {
		return accountPreference;
	}

	public void setAccountPreference(String accountPreference) {
		this.accountPreference = accountPreference;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getNameAsPerBankAccount() {
		return nameAsPerBankAccount;
	}

	public void setNameAsPerBankAccount(String nameAsPerBankAccount) {
		this.nameAsPerBankAccount = nameAsPerBankAccount;
	}

	
	public Double getProdBenefitAmount() {
		return prodBenefitAmount;
	}

	public void setProdBenefitAmount(Double prodBenefitAmount) {
		this.prodBenefitAmount = prodBenefitAmount;
	}

	public Long getProdBenefitDueToID() {
		return prodBenefitDueToID;
	}

	public void setProdBenefitDueToID(Long prodBenefitDueToID) {
		this.prodBenefitDueToID = prodBenefitDueToID;
	}

	public String getProdBenefitFlag() {
		return prodBenefitFlag;
	}

	public void setProdBenefitFlag(String prodBenefitFlag) {
		this.prodBenefitFlag = prodBenefitFlag;
	}

	public Long getProdDiagnosisID() {
		return prodDiagnosisID;
	}

	public void setProdDiagnosisID(Long prodDiagnosisID) {
		this.prodDiagnosisID = prodDiagnosisID;
	}

	public String getPhcDayCareFlag() {
		return phcDayCareFlag;
	}

	public void setPhcDayCareFlag(String phcDayCareFlag) {
		this.phcDayCareFlag = phcDayCareFlag;
	}

	public Long getPhcDayCareID() {
		return phcDayCareID;
	}

	public void setPhcDayCareID(Long phcDayCareID) {
		this.phcDayCareID = phcDayCareID;
	}
	
	public String getHospitalDiscountFlag() {
		return hospitalDiscountFlag;
	}

	public void setHospitalDiscountFlag(String hospitalDiscountFlag) {
		this.hospitalDiscountFlag = hospitalDiscountFlag;
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

	public String getReconsiderationFlagReq() {
		return reconsiderationFlagReq;
	}

	public void setReconsiderationFlagReq(String reconsiderationFlagReq) {
		this.reconsiderationFlagReq = reconsiderationFlagReq;
	}

	public String getReconsiderationFlagRemark() {
		return reconsiderationFlagRemark;
	}

	public void setReconsiderationFlagRemark(String reconsiderationFlagRemark) {
		this.reconsiderationFlagRemark = reconsiderationFlagRemark;
	}
	
	public String getDcFlag() {
		return dcFlag;
	}

	public void setDcFlag(String dcFlag) {
		this.dcFlag = dcFlag;
	}
	
	public String getImplantFlag() {
		return implantFlag;
	}

	public void setImplantFlag(String implantFlag) {
		this.implantFlag = implantFlag;
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

	public String getNomineeFlag() {
		return nomineeFlag;
	}

	public void setNomineeFlag(String nomineeFlag) {
		this.nomineeFlag = nomineeFlag;
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

	public String getEmergencyFlag() {
		return emergencyFlag;
	}

	public void setEmergencyFlag(String emergencyFlag) {
		this.emergencyFlag = emergencyFlag;
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
	
	public String getBillingFaAutoAllocSuBType() {
		return billingFaAutoAllocSuBType;
	}

	public void setBillingFaAutoAllocSuBType(String billingFaAutoAllocSuBType) {
		this.billingFaAutoAllocSuBType = billingFaAutoAllocSuBType;
	}

	public Timestamp getBillingFaAutoAllocSuBDate() {
		return billingFaAutoAllocSuBDate;
	}

	public void setBillingFaAutoAllocSuBDate(Timestamp billingFaAutoAllocSuBDate) {
		this.billingFaAutoAllocSuBDate = billingFaAutoAllocSuBDate;
	}

	public String getBillingFaAutoAllocSuBUser() {
		return billingFaAutoAllocSuBUser;
	}

	public void setBillingFaAutoAllocSuBUser(String billingFaAutoAllocSuBUser) {
		this.billingFaAutoAllocSuBUser = billingFaAutoAllocSuBUser;
	}
	
	public String getAutoCancelRemarks() {
		return autoCancelRemarks;
	}

	public void setAutoCancelRemarks(String autoCancelRemarks) {
		this.autoCancelRemarks = autoCancelRemarks;
	}

	public String getBillingAutoAllocIcdFlag() {
		return billingAutoAllocIcdFlag;
	}

	public void setBillingAutoAllocIcdFlag(String billingAutoAllocIcdFlag) {
		this.billingAutoAllocIcdFlag = billingAutoAllocIcdFlag;
	}

	public String getAutoCancelMARemarks() {
		return autoCancelMARemarks;
	}

	public void setAutoCancelMARemarks(String autoCancelMARemarks) {
		this.autoCancelMARemarks = autoCancelMARemarks;
	}

	public String getClaimRequestAutoAllocSuBType() {
		return claimRequestAutoAllocSuBType;
	}

	public void setClaimRequestAutoAllocSuBType(String claimRequestAutoAllocSuBType) {
		this.claimRequestAutoAllocSuBType = claimRequestAutoAllocSuBType;
	}

	public Timestamp getClaimRequestAutoAllocSuBDate() {
		return claimRequestAutoAllocSuBDate;
	}

	public void setClaimRequestAutoAllocSuBDate(
			Timestamp claimRequestAutoAllocSuBDate) {
		this.claimRequestAutoAllocSuBDate = claimRequestAutoAllocSuBDate;
	}

	public String getClaimRequestAutoAllocSuBUser() {
		return claimRequestAutoAllocSuBUser;
	}

	public void setClaimRequestAutoAllocSuBUser(String claimRequestAutoAllocSuBUser) {
		this.claimRequestAutoAllocSuBUser = claimRequestAutoAllocSuBUser;
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

	public String getRemarksBillEntry() {
		return remarksBillEntry;
	}

	public void setRemarksBillEntry(String remarksBillEntry) {
		this.remarksBillEntry = remarksBillEntry;
	}

	public String getGrievanceRepresentation() {
		return grievanceRepresentation;
	}

	public void setGrievanceRepresentation(String grievanceRepresentation) {
		this.grievanceRepresentation = grievanceRepresentation;
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
	
}
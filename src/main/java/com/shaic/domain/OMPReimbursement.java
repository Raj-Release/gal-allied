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
 * The persistent class for the IMS_CLS_OMP_REIMBURSEMENT table.
 * 
 */

@Entity
@Table(name="IMS_CLS_OMP_REIMBURSEMENT")
@NamedQueries({
@NamedQuery(name="OMPReimbursement.findAll", query="SELECT r FROM OMPReimbursement r where  (r.activeStatus is null or R.activeStatus = 1)"),
@NamedQuery(name ="OMPReimbursement.findByKey",query="SELECT r FROM OMPReimbursement r WHERE r.key = :primaryKey and (r.activeStatus is null or R.activeStatus = 1)"),
@NamedQuery(name="OMPReimbursement.findOMPRodByNumber", query="SELECT r FROM OMPReimbursement r WHERE  r.rodNumber = :rodNumber and (r.activeStatus is null or R.activeStatus = 1)"),
@NamedQuery(name="OMPReimbursement.CountAckByClaimKey", query = "SELECT count(o) FROM OMPReimbursement o where o.claim.key = :claimkey"),
@NamedQuery(name="OMPReimbursement.findLatestRODByClaimKey", query="SELECT r FROM OMPReimbursement r WHERE  r.claim is not null and r.claim.key = :claimKey and (r.activeStatus is null or R.activeStatus = 1) order by r.key desc"),
@NamedQuery(name="OMPReimbursement.findByOMPClaimKey", query="SELECT r FROM OMPReimbursement r WHERE r.claim is not null and r.claim.key = :claimKey and (r.activeStatus is null or R.activeStatus = 1) order by r.key asc"),
@NamedQuery(name="OMPReimbursement.findByOMPClaimKeyClass", query="SELECT r.claim.key FROM OMPReimbursement r WHERE r.claim is not null and r.classificationId.value=:value and (r.activeStatus is null or R.activeStatus = 1) order by r.key asc"),
@NamedQuery(name="OMPReimbursement.findByOMPClaimKeyDocRec", query="SELECT r FROM OMPReimbursement r WHERE r.claim is not null and r.claim.key = :claimKey and r.classiDocumentRecivedFmId.value=:value and (r.activeStatus is null or R.activeStatus = 1)order by r.key asc"),
@NamedQuery(name="OMPReimbursement.findByOMPClmKey", query = "SELECT o FROM OMPReimbursement o where o.claim.key = :claimkey order by o.key asc")
})
public class OMPReimbursement extends AbstractEntity {

	private static final long serialVersionUID = -7989533461490851555L;

	@Id
	@SequenceGenerator(name="IMS_OMP_REIMBURSEMENT_KEY_GENERATOR", sequenceName = "SEQ_OMP_REIMBURSEMENT_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_OMP_REIMBURSEMENT_KEY_GENERATOR" )
	@Column(name="REIMBURSEMENT_KEY")
	private Long key;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="DOC_ACKNOWLEDGEMENT_KEY", nullable=false)
	private OMPDocAcknowledgement docAcknowLedgement;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CLAIM_KEY", nullable=false)
	private OMPClaim claim;
	
	@Column(name="ROD_NUMBER")
	private String rodNumber;
	
	@Column(name="BANK_ID")    
	private Long bankId;
	
	@Column(name="PAYMENT_MODE_ID")
	private Long paymentModeId;
	
	
	@Column(name="PAYEE_NAME")
	private String payeeName;    
	
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
			
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REJECTION_CATEGORY_ID", nullable = true)
	private MastersValue rejectionCategoryId;    
	
	@Column(name="REJECTION_REMARKS")
	private String rejectionRemarks; 
			
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
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "INVESTIGATION_REVIEWED", length = 1)
	private String investigationReportReview;
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "SKIP_ZMR_FLAG", length = 1)
	private String skipZmrFlag;
	
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;

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
	private Long amtConsCopayPercentage;
	
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
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "OTH_INSR_SETL_FLAG", length = 1)
	private String  otherInsurerApplicableFlag;   
	
	@Column(name = "BILL_ENTRY_REMARKS")
	private String billEntryRemarks;
	
	@Column(name="BILL_ENTRY_AMT")
	private Double billEntryAmt;  
	
	@Column(name = "COPAY_REMARKS")
	private String copayRemarks;
	
	@Column(name = "PREMIUM_AMT")
	private Double premiumAmt;
	
	@Column(name="HOSPITALISATION_FLAG")
	private String hospitalisationFlag;
	
	@Column(name="NON_HOSPITALISATION_FLAG")
	private String nonHospitalisationFlag;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="EVENT_CODE_ID", nullable=true)
	private MastersEvents eventCodeId;
	
	@Column(name="LOSS_DATE_TIME")
	private Date lossDateTime;
	
	@Column(name="HOSPITAL_NAME")
	private String hospitalName;
	
	@Column(name="CITY_NAME")
	private String cityName;

	@Column(name="COUNTRY_ID")
	private Long countryId;
	
	@Column(name="AILMENT_LOSS")
	private String ailmentLoss;
	
	@Column(name="DELAY_HR")
	private String delayHR;
	
	@Column(name="PLACE_VISIT")
	private String placeVisit;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DOCUMENT_TYPE_ID", nullable = true)
	private MastersValue documentTypeId; 
	
	
	@Column(name="REASON_RECONSIDERATION")
	private String reasonReconsideration;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CLASSIFICATION_ID", nullable=true)
	private MastersValue classificationId;
		
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="SUB_CLASSIFICATION_ID", nullable=true)
	private MastersValue subClassificationId;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CURRENCY_TYPE_ID")
	private Currency currencyTypeId;
	
	@Column(name="INR_CONVERSION_RATE")
	private Double  inrConversionRate;
	
	@Column(name="INR_TOTAL_AMOUNT")
	private Double  inrTotalAmount;	
	
	
	@Column(name="CLASSI_NEGOTIATOR_NAME")
	private String negotiatorName;

	@Column(name="CLASSI_SUB_DOCTOR_NAME")
	private String classiSubDoctorName;
	
	@Column(name="CLASSI_INVESTIGATOR_NAME")
	private String classiInvestigatorName;
	
	@Column(name="CLASSI_ADVOCATE_NAME")
	private String classiAdvocateName;
	
	@Column(name="CLASSI_AUDITOR_NAME")
	private String classiAuditorName;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CLASSI_DOCUMENT_RECIEVED_FM_ID", nullable = true)
	private MastersValue classiDocumentRecivedFmId; 
		
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CLASSI_MODE_RECEIPT_ID", nullable = true)
	private MastersValue classiModeReceiptId; 
	
		
	@Column(name="EMAIL_ID")
	private String emailId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CLASSI_DOCUMENT_RECIEVED_DATE")
	private Date documentRecivedDate;
	
	@Column(name="TOTAL_AMOUNT")
	private Long totalAmount;
	
	@Column(name="DEDUCTIBLE_USD")
	private Long deductbleUsd;
	
	@Column(name="COPAY")
	private Long coPay;
	
	@Column(name="APPROVED_AMT_POST_COPAY_USD")
	private Long approvedAmtAfterCoPayUsd;
	
	@Column(name="AGREED_AMT_POST_NGN_USD")
	private Long agreedAmtAfterCopayUsd;
	
	@Column(name="BALANCE_SI")
	private Long balanceSI;
	
	@Column(name="TOTAL_PAYABLE_AMT_USD")
	private Long totalPayableAmtUsd;
	
	@Column(name="ALREADY_PAID_AMT_USD")
	private Long alreadyPaidAmtUsd;
	
	@Column(name="PAYABLE_AMT_INR")
	private Long payableAmtInr;
	
	@Column(name="PAYABLE_AMT_USD")
	private Long payableAmtUsd;
	
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="PAYMENT_TO")
	private MastersValue paymentTo;
	
	@Column(name="DEDUCTIBLE_REC_FROM_INSURED")
	private String deductibleRecFrmInsured;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="RECOVERED_DATE")
	private Date recoveredDate;
	
	@Column(name="RECOVERED_AMOUNT_INR")
	private Long recoveredAmountInr;
	
	@Column(name="RECOVERED_AMOUNT_USD")
	private Long recoveredAmountUsd;
	
	@Column(name="REMARKS")
	private String remarks;
	
	@Column(name="NEGOTIATEAGREEDAMT")
	private Long negogiationAgreedAmt;
	
	@Column(name="REMARKS_PROCESSOR")
	private String remarksProcessor;
	
	@Column(name="REMARKS_APPROVER")
	private String remarksApprover;
	
	@Column(name="REASON_NEGOTIATION")
	private String reasonNegotiation;
	
	@Column(name="REASON_SUGGEST_REJECTIOR")
	private String reasonSuggestRejection;
	
	@Column(name="REASON_SUGESST_APPROVA")
	private String reasonSuggestApprover;
	
	@Column(name="CLAIM_TYPE_FLAG")
	private String claimTypeFlag;
	
	
	@Column(name="BEN_APR_AMT")
	private Double benApprAmt;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CATEGORY_ID", nullable = true)
	private MastersValue categoryId;
	
	@Column(name="VALUE_FOR_APPROVER")
	private String viewForApprover;
	
	@Column(name="SEND_FOR_APPROVER")
	private String sendForApproverFlg;
	
	@Column(name="REJECT")
	private String rejectFlg;
	
	@Column(name="NOT_IN_CLAIM_COUNT")
	private String notInClaimCountFlg;
	
	@Column(name="NEGOTIATION_DONE")
	private String negotiationDone;
	
	@Column(name="LEGAL_OPINION_TAKEN")
	private String legalPinaionFlg;
	
	@Column(name="AMOUNT_AFTER_NEGO")
	private Double amountAfterNego;
	
	@Column(name="NEGOTIATION_KEY")
	private Long negotiationList;
	
	@Column(name="FINAL_APPROVED_AMT_USD")
	private Double finalApprovedAmtUsd;
	
	@Column(name="FINAL_APPROVED_AMT_INR")
	private Double finalApprovedAmtInr;
	
	@Column(name="APPROVED")
	private String approvedFlg;
	
	@Column(name="FA_APPROVED_FLAG")
	private String faSubmitFlg;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ROD_CLAIM_TYPE_ID", nullable = true)
	private MastersValue rodClaimType;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="PAYEE_CURRENCY_ID")
	private Currency payeeCurrencyTypeId;
	
	@Column(name="SENT_TO_ACCOUNTS")
	private String sendToAccounts;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LAST_DOC_REC_DT")
	private Date lastDocRecDate;
	
	@Column(name="RECONSIDER_FLAG")
	private String reconsiderFlag;
	
	@Column(name="DISCHARGE_FLAG")
	private String dischargeFlag;
	
	@Column(name="GEN_DISCHARGE_FLAG")
	private String generateDischargeVoucherFlag;
	
	@Column(name="DISCHARGE_REMARKS")
	private String dischargeVoucherRemarks;
	
	@Column(name="GEN_COVERING_FLAG")
	private String generateCoveringLetterFlag;
	
	@Column(name="NOMINEE_NAME")
	private String nomineeName;
	
	@Column(name="NOMINEE_ADDRESS")
	private String nomineeAddress;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DV_RECEVIED_DATE")
	private Date dvReceivedDate;
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public OMPDocAcknowledgement getDocAcknowLedgement() {
		return docAcknowLedgement;
	}

	public void setDocAcknowLedgement(OMPDocAcknowledgement docAcknowLedgement) {
		this.docAcknowLedgement = docAcknowLedgement;
	}

	public OMPClaim getClaim() {
		return claim;
	}

	public void setClaim(OMPClaim claim) {
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

	public MastersValue getRejectionCategoryId() {
		return rejectionCategoryId;
	}

	public void setRejectionCategoryId(MastersValue rejectionCategoryId) {
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

	public String getPayeeName() {
		return payeeName;
	}

	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}

	public Long getPaymentModeId() {
		return paymentModeId;
	}

	public void setPaymentModeId(Long paymentModeId) {
		this.paymentModeId = paymentModeId;
	}

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

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
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
	
	public Double getBillEntryAmt() {
		return billEntryAmt;
	}

	public void setBillEntryAmt(Double billEntryAmt) {
		this.billEntryAmt = billEntryAmt;
	}

	public String getHospitalisationFlag() {
		return hospitalisationFlag;
	}

	public void setHospitalisationFlag(String hospitalisationFlag) {
		this.hospitalisationFlag = hospitalisationFlag;
	}

	public String getNonHospitalisationFlag() {
		return nonHospitalisationFlag;
	}

	public void setNonHospitalisationFlag(String nonHospitalisationFlag) {
		this.nonHospitalisationFlag = nonHospitalisationFlag;
	}

	public MastersEvents getEventCodeId() {
		return eventCodeId;
	}

	public void setEventCodeId(MastersEvents eventCodeId) {
		this.eventCodeId = eventCodeId;
	}

	public Date getLossDateTime() {
		return lossDateTime;
	}

	public void setLossDateTime(Date lossDateTime) {
		this.lossDateTime = lossDateTime;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public String getAilmentLoss() {
		return ailmentLoss;
	}

	public void setAilmentLoss(String ailmentLoss) {
		this.ailmentLoss = ailmentLoss;
	}

	public String getDelayHR() {
		return delayHR;
	}

	public void setDelayHR(String delayHR) {
		this.delayHR = delayHR;
	}

	public String getPlaceVisit() {
		return placeVisit;
	}

	public void setPlaceVisit(String placeVisit) {
		this.placeVisit = placeVisit;
	}



	public String getReasonReconsideration() {
		return reasonReconsideration;
	}

	public void setReasonReconsideration(String reasonReconsideration) {
		this.reasonReconsideration = reasonReconsideration;
	}

	public MastersValue getClassificationId() {
		return classificationId;
	}

	public void setClassificationId(MastersValue classificationId) {
		this.classificationId = classificationId;
	}

	public MastersValue getSubClassificationId() {
		return subClassificationId;
	}

	public void setSubClassificationId(MastersValue subClassificationId) {
		this.subClassificationId = subClassificationId;
	}

	public Currency getCurrencyTypeId() {
		return currencyTypeId;
	}

	public void setCurrencyTypeId(Currency currencyTypeId) {
		this.currencyTypeId = currencyTypeId;
	}

	public Double getInrConversionRate() {
		return inrConversionRate;
	}

	public void setInrConversionRate(Double inrConversionRate) {
		this.inrConversionRate = inrConversionRate;
	}

	public Double getInrTotalAmount() {
		return inrTotalAmount;
	}

	public void setInrTotalAmount(Double inrTotalAmount) {
		this.inrTotalAmount = inrTotalAmount;
	}
	
	@Override
    public boolean equals(Object obj) {
        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        } else if (getKey() == null) {
            return obj == this;
        } else {
            return getKey().equals(((OMPReimbursement) obj).getKey());
        }
    }

    @Override
    public int hashCode() {
        if (key != null) {
            return key.hashCode();
        } else {
            return super.hashCode();
        }
    }

	public String getNegotiatorName() {
		return negotiatorName;
	}

	public void setNegotiatorName(String negotiatorName) {
		this.negotiatorName = negotiatorName;
	}

	
	public String getClassiSubDoctorName() {
		return classiSubDoctorName;
	}

	public void setClassiSubDoctorName(String classiSubDoctorName) {
		this.classiSubDoctorName = classiSubDoctorName;
	}

	public String getClassiInvestigatorName() {
		return classiInvestigatorName;
	}

	public void setClassiInvestigatorName(String classiInvestigatorName) {
		this.classiInvestigatorName = classiInvestigatorName;
	}

	public String getClassiAdvocateName() {
		return classiAdvocateName;
	}

	public void setClassiAdvocateName(String classiAdvocateName) {
		this.classiAdvocateName = classiAdvocateName;
	}

	public String getClassiAuditorName() {
		return classiAuditorName;
	}

	public void setClassiAuditorName(String classiAuditorName) {
		this.classiAuditorName = classiAuditorName;
	}

	public MastersValue getClassiDocumentRecivedFmId() {
		return classiDocumentRecivedFmId;
	}

	public void setClassiDocumentRecivedFmId(MastersValue classiDocumentRecivedFmId) {
		this.classiDocumentRecivedFmId = classiDocumentRecivedFmId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Date getDocumentRecivedDate() {
		return documentRecivedDate;
	}

	public void setDocumentRecivedDate(Date documentRecivedDate) {
		this.documentRecivedDate = documentRecivedDate;
	}

	public MastersValue getDocumentTypeId() {
		return documentTypeId;
	}

	public void setDocumentTypeId(MastersValue documentTypeId) {
		this.documentTypeId = documentTypeId;
	}

	public MastersValue getClassiModeReceiptId() {
		return classiModeReceiptId;
	}

	public void setClassiModeReceiptId(MastersValue classiModeReceiptId) {
		this.classiModeReceiptId = classiModeReceiptId;
	}

	public Long getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Long totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Long getDeductbleUsd() {
		return deductbleUsd;
	}

	public void setDeductbleUsd(Long deductbleUsd) {
		this.deductbleUsd = deductbleUsd;
	}

	public Long getCoPay() {
		return coPay;
	}

	public void setCoPay(Long coPay) {
		this.coPay = coPay;
	}

	public Long getApprovedAmtAfterCoPayUsd() {
		return approvedAmtAfterCoPayUsd;
	}

	public void setApprovedAmtAfterCoPayUsd(Long approvedAmtAfterCoPayUsd) {
		this.approvedAmtAfterCoPayUsd = approvedAmtAfterCoPayUsd;
	}

	public Long getAgreedAmtAfterCopayUsd() {
		return agreedAmtAfterCopayUsd;
	}

	public void setAgreedAmtAfterCopayUsd(Long agreedAmtAfterCopayUsd) {
		this.agreedAmtAfterCopayUsd = agreedAmtAfterCopayUsd;
	}

	public Long getBalanceSI() {
		return balanceSI;
	}

	public void setBalanceSI(Long balanceSI) {
		this.balanceSI = balanceSI;
	}

	public Long getTotalPayableAmtUsd() {
		return totalPayableAmtUsd;
	}

	public void setTotalPayableAmtUsd(Long totalPayableAmtUsd) {
		this.totalPayableAmtUsd = totalPayableAmtUsd;
	}

	public Long getAlreadyPaidAmtUsd() {
		return alreadyPaidAmtUsd;
	}

	public void setAlreadyPaidAmtUsd(Long alreadyPaidAmtUsd) {
		this.alreadyPaidAmtUsd = alreadyPaidAmtUsd;
	}

	public Long getPayableAmtInr() {
		return payableAmtInr;
	}

	public void setPayableAmtInr(Long payableAmtInr) {
		this.payableAmtInr = payableAmtInr;
	}

	public Long getPayableAmtUsd() {
		return payableAmtUsd;
	}

	public void setPayableAmtUsd(Long payableAmtUsd) {
		this.payableAmtUsd = payableAmtUsd;
	}

	public MastersValue getPaymentTo() {
		return paymentTo;
	}

	public void setPaymentTo(MastersValue paymentTo) {
		this.paymentTo = paymentTo;
	}

	public String getDeductibleRecFrmInsured() {
		return deductibleRecFrmInsured;
	}

	public void setDeductibleRecFrmInsured(String deductibleRecFrmInsured) {
		this.deductibleRecFrmInsured = deductibleRecFrmInsured;
	}

	public Date getRecoveredDate() {
		return recoveredDate;
	}

	public void setRecoveredDate(Date recoveredDate) {
		this.recoveredDate = recoveredDate;
	}

	public Long getRecoveredAmountInr() {
		return recoveredAmountInr;
	}

	public void setRecoveredAmountInr(Long recoveredAmountInr) {
		this.recoveredAmountInr = recoveredAmountInr;
	}

	public Long getRecoveredAmountUsd() {
		return recoveredAmountUsd;
	}

	public void setRecoveredAmountUsd(Long recoveredAmountUsd) {
		this.recoveredAmountUsd = recoveredAmountUsd;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Long getNegogiationAgreedAmt() {
		return negogiationAgreedAmt;
	}

	public void setNegogiationAgreedAmt(Long negogiationAgreedAmt) {
		this.negogiationAgreedAmt = negogiationAgreedAmt;
	}

	public String getRemarksProcessor() {
		return remarksProcessor;
	}

	public void setRemarksProcessor(String remarksProcessor) {
		this.remarksProcessor = remarksProcessor;
	}

	public String getRemarksApprover() {
		return remarksApprover;
	}

	public void setRemarksApprover(String remarksApprover) {
		this.remarksApprover = remarksApprover;
	}

	public String getReasonNegotiation() {
		return reasonNegotiation;
	}

	public void setReasonNegotiation(String reasonNegotiation) {
		this.reasonNegotiation = reasonNegotiation;
	}

	public String getReasonSuggestRejection() {
		return reasonSuggestRejection;
	}

	public void setReasonSuggestRejection(String reasonSuggestRejection) {
		this.reasonSuggestRejection = reasonSuggestRejection;
	}

	public String getReasonSuggestApprover() {
		return reasonSuggestApprover;
	}

	public void setReasonSuggestApprover(String reasonSuggestApprover) {
		this.reasonSuggestApprover = reasonSuggestApprover;
	}

	public String getClaimTypeFlag() {
		return claimTypeFlag;
	}

	public void setClaimTypeFlag(String claimTypeFlag) {
		this.claimTypeFlag = claimTypeFlag;
	}

	public Double getBenApprAmt() {
		return benApprAmt;
	}

	public void setBenApprAmt(Double benApprAmt) {
		this.benApprAmt = benApprAmt;
	}

	public MastersValue getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(MastersValue categoryId) {
		this.categoryId = categoryId;
	}

	public String getViewForApprover() {
		return viewForApprover;
	}

	public void setViewForApprover(String viewForApprover) {
		this.viewForApprover = viewForApprover;
	}

	public String getSendForApproverFlg() {
		return sendForApproverFlg;
	}

	public void setSendForApproverFlg(String sendForApproverFlg) {
		this.sendForApproverFlg = sendForApproverFlg;
	}

	public String getRejectFlg() {
		return rejectFlg;
	}

	public void setRejectFlg(String rejectFlg) {
		this.rejectFlg = rejectFlg;
	}

	public String getNotInClaimCountFlg() {
		return notInClaimCountFlg;
	}

	public void setNotInClaimCountFlg(String notInClaimCountFlg) {
		this.notInClaimCountFlg = notInClaimCountFlg;
	}

	public String getNegotiationDone() {
		return negotiationDone;
	}

	public void setNegotiationDone(String negotiationDone) {
		this.negotiationDone = negotiationDone;
	}

	public String getLegalPinaionFlg() {
		return legalPinaionFlg;
	}

	public void setLegalPinaionFlg(String legalPinaionFlg) {
		this.legalPinaionFlg = legalPinaionFlg;
	}

	public Double getAmountAfterNego() {
		return amountAfterNego;
	}

	public void setAmountAfterNego(Double amountAfterNego) {
		this.amountAfterNego = amountAfterNego;
	}

	public Long getNegotiationList() {
		return negotiationList;
	}

	public void setNegotiationList(Long negotiationList) {
		this.negotiationList = negotiationList;
	}

	public Double getFinalApprovedAmtInr() {
		return finalApprovedAmtInr;
	}

	public void setFinalApprovedAmtInr(Double finalApprovedAmtInr) {
		this.finalApprovedAmtInr = finalApprovedAmtInr;
	}

	public Double getFinalApprovedAmtUsd() {
		return finalApprovedAmtUsd;
	}

	public void setFinalApprovedAmtUsd(Double finalApprovedAmtUsd) {
		this.finalApprovedAmtUsd = finalApprovedAmtUsd;
	}

	public String getApprovedFlg() {
		return approvedFlg;
	}

	public void setApprovedFlg(String approvedFlg) {
		this.approvedFlg = approvedFlg;
	}

	public String getFaSubmitFlg() {
		return faSubmitFlg;
	}

	public void setFaSubmitFlg(String faSubmitFlg) {
		this.faSubmitFlg = faSubmitFlg;
	}

	public MastersValue getRodClaimType() {
		return rodClaimType;
	}

	public void setRodClaimType(MastersValue rodClaimType) {
		this.rodClaimType = rodClaimType;
	}

	public Currency getPayeeCurrencyTypeId() {
		return payeeCurrencyTypeId;
	}

	public void setPayeeCurrencyTypeId(Currency payeeCurrencyTypeId) {
		this.payeeCurrencyTypeId = payeeCurrencyTypeId;
	}

	public String getSendToAccounts() {
		return sendToAccounts;
	}

	public void setSendToAccounts(String sendToAccounts) {
		this.sendToAccounts = sendToAccounts;
	}

	public Date getLastDocRecDate() {
		return lastDocRecDate;
	}

	public void setLastDocRecDate(Date lastDocRecDate) {
		this.lastDocRecDate = lastDocRecDate;
	}
	
	public String getReconsiderFlag() {
		return reconsiderFlag;
	}

	public void setReconsiderFlag(String reconsiderFlag) {
		this.reconsiderFlag = reconsiderFlag;
	}
	
	public String getDischargeFlag() {
		return dischargeFlag;
	}

	public void setDischargeFlag(String dischargeFlag) {
		this.dischargeFlag = dischargeFlag;
	}

	public String getGenerateDischargeVoucherFlag() {
		return generateDischargeVoucherFlag;
	}

	public void setGenerateDischargeVoucherFlag(String generateDischargeVoucherFlag) {
		this.generateDischargeVoucherFlag = generateDischargeVoucherFlag;
	}

	public String getDischargeVoucherRemarks() {
		return dischargeVoucherRemarks;
	}

	public void setDischargeVoucherRemarks(String dischargeVoucherRemarks) {
		this.dischargeVoucherRemarks = dischargeVoucherRemarks;
	}

	public String getGenerateCoveringLetterFlag() {
		return generateCoveringLetterFlag;
	}

	public void setGenerateCoveringLetterFlag(String generateCoveringLetterFlag) {
		this.generateCoveringLetterFlag = generateCoveringLetterFlag;
	}

	public String getNomineeName() {
		return nomineeName;
	}

	public void setNomineeName(String nomineeName) {
		this.nomineeName = nomineeName;
	}

	public String getNomineeAddress() {
		return nomineeAddress;
	}

	public void setNomineeAddress(String nomineeAddress) {
		this.nomineeAddress = nomineeAddress;
	}

	public Date getDvReceivedDate() {
		return dvReceivedDate;
	}

	public void setDvReceivedDate(Date dvReceivedDate) {
		this.dvReceivedDate = dvReceivedDate;
	}

}


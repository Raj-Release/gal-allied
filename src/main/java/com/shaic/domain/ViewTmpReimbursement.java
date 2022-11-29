package com.shaic.domain;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.preauth.CriticalIllnessMaster;
import com.shaic.domain.preauth.Stage;


@Entity
@Table(name="VW_REIMBURSEMENT_TMP")
@NamedQueries({
@NamedQuery(name="ViewTmpReimbursement.findAll", query="SELECT r FROM ViewTmpReimbursement r"),
@NamedQuery(name ="ViewTmpReimbursement.findByKey",query="SELECT r FROM ViewTmpReimbursement r WHERE r.key = :primaryKey"),
@NamedQuery(name="ViewTmpReimbursement.findByIntimationKey", query="SELECT r FROM ViewTmpReimbursement r WHERE r.claim is not null and r.claim.intimation is not null and r.claim.intimation.key = :intimationKey"),
@NamedQuery(name="ViewTmpReimbursement.findByIntimationNumber", query="SELECT r FROM ViewTmpReimbursement r WHERE r.claim is not null and r.claim.intimation is not null and r.claim.intimation.intimationId = :intimationNumber"),
@NamedQuery(name="ViewTmpReimbursement.findByPolicyNumber", query="SELECT r FROM ViewTmpReimbursement r WHERE r.claim is not null and r.claim.intimation is not null and r.claim.intimation.policy is not null and r.claim.intimation.policyNumber = :policyNumber"),
@NamedQuery(name="ViewTmpReimbursement.findByClaimKey", query="SELECT r FROM ViewTmpReimbursement r WHERE r.claim is not null and r.claim.key = :claimKey order by r.key asc"),
@NamedQuery(name="ViewTmpReimbursement.findByClaimKeyAndStageId", query="SELECT r FROM ViewTmpReimbursement r WHERE r.claim is not null and r.claim.key = :claimKey and r.stage.key in (:stageList)"),
@NamedQuery(name = "ViewTmpReimbursement.CountAckByClaimKey", query = "SELECT count(o) FROM ViewTmpReimbursement o where o.claim.key = :claimkey"),
@NamedQuery(name = "ViewTmpReimbursement.getAckandRodKey", query = "SELECT max(o.key) , max(o.docAcknowLedgement.key) from ViewTmpReimbursement o where o.claim.key = :claimkey"),
@NamedQuery(name = "ViewTmpReimbursement.getRodkeyByClaim", query = "SELECT o from ViewTmpReimbursement o where o.claim.key = :claimkey"),
@NamedQuery(name = "ViewTmpReimbursement.getRodAscendingOrder",query = "SELECT o from ViewTmpReimbursement o where o.claim.key = :claimkey order by o.key asc"),
@NamedQuery(name = "ViewTmpReimbursement.findByAcknowledgement",query = "SELECT o from ViewTmpReimbursement o where o.docAcknowLedgement.key = :docAcknowledgmentKey"),
@NamedQuery(name="ViewTmpReimbursement.findLatestRODByClaimKey", query="SELECT r FROM ViewTmpReimbursement r WHERE  r.claim is not null and r.claim.key = :claimKey order by r.key desc"),
@NamedQuery(name="ViewTmpReimbursement.findFirstRODByClaimKey", query="SELECT r FROM ViewTmpReimbursement r WHERE  r.claim is not null and r.claim.key = :claimKey order by r.key asc"),
@NamedQuery(name="ViewTmpReimbursement.findByStatus", query = "SELECT r.claim.key, r.key , r.status.key , r.stage.key , r.claim.intimation.cpuCode.cpuCode, r.claim.claimType.value FROM ViewTmpReimbursement r  WHERE  r.stage.key between :fromStage and :toStage and  r.claim.intimation.cpuCode.cpuCode =:cpuCode group by r.claim.key, r.key , r.status.key , r.stage.key , r.claim.intimation.cpuCode.cpuCode , r.claim.claimType.value order by  r.claim.intimation.cpuCode.cpuCode asc"),
@NamedQuery(name="ViewTmpReimbursement.findByStatusId", query = "SELECT r.claim.key, r.key , r.status.key , r.stage.key , r.claim.intimation.cpuCode.cpuCode, r.claim.claimType.value FROM ViewTmpReimbursement r  WHERE  r.stage.key between :fromStage and :toStage and r.createdDate >= :fromDate and  r.createdDate <= :endDate and r.claim.intimation.cpuCode.cpuCode =:cpuCode group by r.claim.key, r.key , r.status.key , r.stage.key , r.claim.intimation.cpuCode.cpuCode , r.claim.claimType.value order by  r.claim.intimation.cpuCode.cpuCode asc"),

@NamedQuery(name="ViewTmpReimbursement.findRodByNumber", query="SELECT r FROM ViewTmpReimbursement r WHERE  r.rodNumber = :rodNumber"),
@NamedQuery(name="ViewTmpReimbursement.findByStatusForQuery", query="SELECT r FROM ViewTmpReimbursement r WHERE  r.claim.key = :claimKey and r.status.key IN (:statusList)")

})
public class ViewTmpReimbursement extends AbstractEntity {

	private static final long serialVersionUID = -4448955171293228443L;

	@Id
	
	@Column(name="REIMBURSEMENT_KEY")
	private Long key;
	
	@OneToOne
	@JoinColumn(name="DOC_ACKNOWLEDGEMENT_KEY", nullable=true)
	private DocAcknowledgement docAcknowLedgement;
	
	@OneToOne
	@JoinColumn(name="CLAIM_KEY", nullable=false)
	private ViewTmpClaim claim;
	
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
			
	@OneToOne
	@JoinColumn(name="ROOM_CATEGORY_ID",nullable=true)
	private MastersValue roomCategory;    
	
	@Column(name="NUMBER_OF_DAYS")
	private Integer numberOfDays;     
	
	@OneToOne
	@JoinColumn(name = "NATURE_OF_TREATMENT_ID", nullable = true)
	private MastersValue natureOfTreatment;
	
	@Temporal(TemporalType.DATE)
	@Column(name="CONSULTATION_DATE")
	private Date consultationDate;          
	
	@Column(name="CRITICAL_ILLNESS_FLAG")
	private Integer criticalIllnessFlag;     
	
	@OneToOne
	@JoinColumn(name="CRITICAL_ILLNESS_ID",nullable=true)
	private CriticalIllnessMaster criticalIllness;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATE_OF_DISCHARGE")
	private Date dateOfDischarge;  
		
	@Column(name="CORPORATE_BUFFER_FLAG")
	private Integer corporateBufferFlag;     
	
	@OneToOne
	@JoinColumn(name="ILLNESS_ID", nullable=true)
	private MastersValue illness;
	
	@Column(name="AUTOMATIC_RESTORATION")
	private String automaticRestoration;
		
	@Column(name="SYSTEM_OF_MEDICINE")
	private String systemOfMedicine;  
	
	@OneToOne
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
	
	@OneToOne
	@JoinColumn(name = "TREATMENT_TYPE_ID", nullable = true)
	private MastersValue treatmentType; 
		
	@Column(name="TREATMENT_REMARKS")
	private String treatmentRemarks; 
	
	@OneToOne
	@JoinColumn(name = "PATIENT_STATUS_ID", nullable = true)
	private MastersValue patientStatus;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATE_OF_DEATH")
	private Date dateOfDeath;  
	
	@Column(name="DEATH_REASON")
	private String deathReason; 
			
	@Column(name="REJECTION_CATEGORY_ID")
	private Long rejectionCategoryId;    
	
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
			
	@OneToOne
	@JoinColumn(name="STAGE_ID", nullable=false)
	private Stage stage;    
			
	@OneToOne
	@JoinColumn(name="STATUS_ID",nullable=false)
	private Status status;    
			
	@Column(name="CREATED_BY")
	private String createdBy;  
			
	@Temporal(TemporalType.DATE)
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
	
	/***\
	 * @author yosuva.a
	 * 
	 * new column added
	 * 
	 */
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
	
	@OneToOne
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
	
	@Column(name = "RECORD_FLAG")
	private String recordFlag;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="BENEFITS_ID", nullable=false)
	private MastersValue benefitsId;
	
	@Column(name = "RECONSIDERATION_REQUEST")
	private String reconsiderationRequest;
	
	@Column(name = "REIMBURSEMENT_VERSION")
	private Long version;
	
//	@Column(name = "AMT_CONS_COPAY_PRCNT", nullable = true)
//	private Long amtConsCopayPercentage;
//	
//	@Column(name = "BAL_SI_COPAY_PRCNT", nullable = true)
//	private Long balanceSICopayPercentage;
//	
//	@Column(name = "AMT_CONS_COPAY_AMT")
//	private Double amtConsAftCopayAmount;
//	
//	@Column(name = "BAL_SI_COPAY_AMT")
//	private Double balanceSIAftCopayAmt;
//	
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name="BILL_ENTRY_DATE")
//	private Date billEntryDate;
//	
//	
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name="ZONAL_DATE")
//	private Date zonalDate;
	
	public MastersValue getBenefitsId() {
		return benefitsId;
	}

	public void setBenefitsId(MastersValue benefitsId) {
		this.benefitsId = benefitsId;
	}

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

	

	public ViewTmpClaim getClaim() {
		return claim;
	}

	public void setClaim(ViewTmpClaim claim) {
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

	public String getRecordFlag() {
		return recordFlag;
	}

	public void setRecordFlag(String recordFlag) {
		this.recordFlag = recordFlag;
	}

	public String getReconsiderationRequest() {
		return reconsiderationRequest;
	}

	public void setReconsiderationRequest(String reconsiderationRequest) {
		this.reconsiderationRequest = reconsiderationRequest;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

//	public Long getAmtConsCopayPercentage() {
//		return amtConsCopayPercentage;
//	}
//
//	public void setAmtConsCopayPercentage(Long amtConsCopayPercentage) {
//		this.amtConsCopayPercentage = amtConsCopayPercentage;
//	}
//
//	public Long getBalanceSICopayPercentage() {
//		return balanceSICopayPercentage;
//	}
//
//	public void setBalanceSICopayPercentage(Long balanceSICopayPercentage) {
//		this.balanceSICopayPercentage = balanceSICopayPercentage;
//	}
//
//	public Double getAmtConsAftCopayAmount() {
//		return amtConsAftCopayAmount;
//	}
//
//	public void setAmtConsAftCopayAmount(Double amtConsAftCopayAmount) {
//		this.amtConsAftCopayAmount = amtConsAftCopayAmount;
//	}
//
//	public Double getBalanceSIAftCopayAmt() {
//		return balanceSIAftCopayAmt;
//	}
//
//	public void setBalanceSIAftCopayAmt(Double balanceSIAftCopayAmt) {
//		this.balanceSIAftCopayAmt = balanceSIAftCopayAmt;
//	}
//
//	public Date getBillEntryDate() {
//		return billEntryDate;
//	}
//
//	public void setBillEntryDate(Date billEntryDate) {
//		this.billEntryDate = billEntryDate;
//	}
//
//	public Date getZonalDate() {
//		return zonalDate;
//	}
//
//	public void setZonalDate(Date zonalDate) {
//		this.zonalDate = zonalDate;
//	}

	
	
}


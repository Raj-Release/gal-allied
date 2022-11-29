package com.shaic.domain.preauth;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Policy;
import com.shaic.domain.Status;
import com.shaic.domain.ViewTmpClaim;
import com.shaic.domain.ViewTmpIntimation;

@Entity
@Table(name = "VW_CASHLESS_TMP")
@NamedQueries({
		@NamedQuery(name = "ViewTmpPreauth.findAll", query = "SELECT i FROM ViewTmpPreauth i"),
		@NamedQuery(name = "ViewTmpPreauth.findByKey", query = "SELECT o FROM ViewTmpPreauth o where o.key = :preauthKey order by o.preauthId"),
		@NamedQuery(name = "ViewTmpPreauth.findByClaimKey", query = "SELECT o FROM ViewTmpPreauth o where o.claim.key = :claimkey and o.status is not null and (o.status.key <> 167 and o.status.key <> 166) order by o.preauthId "),
		@NamedQuery(name = "ViewTmpPreauth.findByIntimationKey", query = "SELECT o FROM ViewTmpPreauth o where o.intimation.key = :intimationKey order by o.preauthId"),
		@NamedQuery(name = "ViewTmpPreauth.findPreAuthIdInDescendingOrder", query = "SELECT o FROM ViewTmpPreauth o where o.intimation.key = :intimationKey and o.status is not null and (o.status.key <> 167 and o.status.key <> 166) order by o.preauthId desc"),
		@NamedQuery(name = "ViewTmpPreauth.getFinalEnhAmtByClaim", query = "SELECT o.totalApprovalAmount FROM ViewTmpPreauth o where o.claim.key = :claimKey and o.status is not null and (o.status.key <> 167 and o.status.key <> 166) and o.enhancementType = 'F'"),
		@NamedQuery(name = "ViewTmpPreauth.findByClaimKeyInDescendingOrder", query = "SELECT o FROM ViewTmpPreauth o where o.claim.key = :claimkey and o.status is not null and (o.status.key <> 167 and o.status.key <> 166) order by o.preauthId desc"),
		@NamedQuery(name = "ViewTmpPreauth.getApprovedPreauthInDescendingOrder", query = "SELECT o FROM ViewTmpPreauth o where(:fromDate is null or  o.createdDate >= :fromDate) and (:endDate is null or o.createdDate <= :endDate ) order by o.preauthId desc"),
		@NamedQuery(name = "ViewTmpPreauth.findByPolicyNum", query = "Select p FROM ViewTmpPreauth p where p.claim.intimation.policyNumber = :policyNum"),
		@NamedQuery(name = "ViewTmpPreauth.findMaxPreauthIdByClaimKey", query = "Select max(p.preauthId) FROM ViewTmpPreauth p where p.claim.key <> :claimKey and p.status is not null and (p.status.key <> 167 and p.status.key <> 166)"),
		@NamedQuery(name = "ViewTmpPreauth.findLatestPreauthByClaim", query = "SELECT o FROM ViewTmpPreauth o where o.claim.key = :claimkey order by o.preauthId desc"),
		@NamedQuery(name = "ViewTmpPreauth.findByLatestIntimationKey", query = "SELECT o From ViewTmpPreauth o where o.intimation.key = :intimationKey and o.status is not null and (o.status.key <> 167 and o.status.key <> 166) order by o.intimation.key desc"),
		@NamedQuery(name = "ViewTmpPreauth.findCashlessEstimation", query = "SELECT count(o.key) From ViewTmpPreauth o where o.createdDate >= :fromDate and o.createdDate <= :endDate and (:cpuKey is null or o.intimation.cpuCode.key = :cpuKey)"),
		@NamedQuery(name = "ViewTmpPreauth.findPreauthSummaryByStageNStaus", query = "SELECT count(o.key) From ViewTmpPreauth o where o.stage.key = :preauthStageKey and o.status.key = :approvedStatusKey and o.createdDate >= :fromDate and o.createdDate <= :endDate and (o.intimation.cpuCode.key is null or o.intimation.cpuCode.key = :cpuKey)" ),
		@NamedQuery(name = "ViewTmpPreauth.findEnhancementApprovedSummary",    query = "SELECT count(o.key) From ViewTmpPreauth o where o.stage.key = :enhanceStageKey and o.status.key = :approvedStatusKey and (o.enhancementType = 'I' or o.enhancementType = 'F') and o.createdDate >= :fromDate and o.createdDate <= :endDate and (o.intimation.cpuCode.key is null or o.intimation.cpuCode.key = :cpuKey)"),
		@NamedQuery(name = "ViewTmpPreauth.findByStatus", query = "Select o FROM ViewTmpPreauth o where o.status.key in(:status)"),
		@NamedQuery(name = "ViewTmpPreauth.findByToDate", query = "Select o FROM ViewTmpPreauth o where o.createdDate <= :createdDate ORDER BY  o.createdDate DESC"),
		@NamedQuery(name = "ViewTmpPreauth.findByQueryRaisedStatus", query = "Select o FROM ViewTmpPreauth o where o.status.key IN (:statusList) and o.claim.key = :claimKey")
		
		
})
public class ViewTmpPreauth extends AbstractEntity {
	
	@Id
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
	@OneToOne
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

	@OneToOne
	@JoinColumn(name = "CLAIM_KEY", nullable = false)
	private ViewTmpClaim claim;

	@OneToOne
	@JoinColumn(name = "INTIMATION_KEY", nullable = false)
	private ViewTmpIntimation intimation;

	@OneToOne
	@JoinColumn(name = "POLICY_KEY", nullable = false)
	private Policy policy;

	@OneToOne
	@JoinColumn(name = "FVR_NOT_REQUIRED_REMARKS", nullable = true)
	private MastersValue fvrNotRequiredRemarks;

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

	@OneToOne
	@JoinColumn(name = "NATURE_OF_TREATMENT_ID", nullable = true)
	private MastersValue natureOfTreatment;

	@Column(name = "NUMBER_OF_DAYS")
	private Long numberOfDays;

	@Column(name = "OFFICE_CODE")
	private String officeCode;

	@OneToOne
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

	@OneToOne
	@JoinColumn(name = "ROOM_CATEGORY_ID", nullable = false)
	private MastersValue roomCategory;

	@Column(name = "SPECIALIST_CONSULTED_ID")
	private Long specialistConsulted;

	@Column(nullable = false, name = "SPECIALIST_OPINION_TAKEN", length = 1)
	private Integer specialistOpinionTaken;

	@Column(name = "SPECIALIST_REMARKS")
	private String specialistRemarks;

	@OneToOne
	@JoinColumn(name = "SPECIALIST_TYPE_ID", nullable = true)
	private MastersValue specialistType;

	@OneToOne
	@JoinColumn(name = "STATUS_ID", nullable = false)
	private Status status;

	@OneToOne
	@JoinColumn(name = "STAGE_ID", nullable = false)
	private Stage stage;

//	@Column(name = "STATUS_DATE")
//	private Timestamp statusDate;

	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "AUTOMATIC_RESTORATION", length = 1)
	private String autoRestoration;

	/*@OneToOne
	@JoinColumn(name = "ILLNESS", nullable = false)
	private MastersValue illness;*/
	
	@OneToOne
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

	@OneToOne
	@JoinColumn(name = "TREATMENT_TYPE_ID", nullable = false)
	private MastersValue treatmentType;

//	@Column(name = "VERSION")
//	private Long version;

	@OneToOne
	@JoinColumn(name = "WITHDRAW_REASON_ID", nullable = true)
	private MastersValue withdrawReason;
	
	@OneToOne
	@JoinColumn(name = "DOWNSIZE_REASON_ID", nullable = true)
	private MastersValue downSizeReason;

//	@OneToOne
//	@JoinColumn(name = "CATEGORY_REASON_ID", nullable = true)
//	private MastersValue categoryReason;
	
	
	@OneToOne
	@JoinColumn(name = "REJECTION_CATEGORY_ID", nullable = true)
	private MastersValue rejectionCategorId;
	
	
	
	@Column(name = "INSURED_KEY")
	private Long insuredKey;
	
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
	
	@Column(name = "RECORD_FLAG")
	private String recordFlag;
	
	
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

	public ViewTmpClaim getClaim() {
		return claim;
	}

	public void setClaim(ViewTmpClaim claim) {
		this.claim = claim;
	}

	public ViewTmpIntimation getIntimation() {
		return intimation;
	}

	public void setIntimation(ViewTmpIntimation intimation) {
		this.intimation = intimation;
	}

	public String getRecordFlag() {
		return recordFlag;
	}

	public void setRecordFlag(String recordFlag) {
		this.recordFlag = recordFlag;
	}
	

}
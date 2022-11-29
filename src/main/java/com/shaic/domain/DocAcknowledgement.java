/**
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
import com.shaic.domain.preauth.Stage;

/**
 * @author ntv.vijayar
 * The persistent class for the IMS_CLS_DOC_ACKNOWLEDGEMENT database table.
 * 
 */
@Entity
@Table(name = "IMS_CLS_DOC_ACKNOWLEDGEMENT")
@NamedQueries({
	@NamedQuery(name = "DocAcknowledgement.findAll", query = "SELECT i FROM DocAcknowledgement i"),
	@NamedQuery(name = "DocAcknowledgement.findByKey", query = "SELECT o FROM DocAcknowledgement o where o.key = :ackDocKey"),
	@NamedQuery(name = "DocAcknowledgement.findByClaimKey", query = "SELECT o FROM DocAcknowledgement o where o.claim.key = :claimkey order by o.rodKey asc"),
	@NamedQuery(name = "DocAcknowledgement.findAckByClaim", query = "SELECT o FROM DocAcknowledgement o where o.claim.key = :claimkey order by o.key asc"),
	@NamedQuery(name = "DocAcknowledgement.CountAckByClaimKey", query = "SELECT count(o) FROM DocAcknowledgement o where o.claim.key = :claimkey"),
	@NamedQuery(name = "DocAcknowledgement.CountAckExceptCancel", query = "SELECT count(o) FROM DocAcknowledgement o where o.claim.key = :claimkey and o.status.key <> :statusId"),
	@NamedQuery(name = "DocAcknowledgement.findMaxRODKey", query = "SELECT Max(o.rodKey) FROM DocAcknowledgement o where o.claim.key = :claimkey"),
	@NamedQuery(name = "DocAcknowledgement.findMaxDocAckKey", query = "SELECT Max(o.key) FROM DocAcknowledgement o where o.rodKey = :rodKey"),
	@NamedQuery(name = "DocAcknowledgement.findMaxAckNo", query = "SELECT o FROM DocAcknowledgement o where o.acknowledgeNumber like :ackNoMax order by o.acknowledgeNumber desc"),
	@NamedQuery(name = "DocAcknowledgement.findAckNo", query = "SELECT o FROM DocAcknowledgement o where o.acknowledgeNumber = :ackNo"),
	@NamedQuery(name = "DocAcknowledgement.findByRODKey", query = "SELECT o FROM DocAcknowledgement o where o.rodKey = :rodKey order by o.key desc"),
	@NamedQuery(name = "DocAcknowledgement.findByReimbursementKey", query = "SELECT o FROM DocAcknowledgement o where o.rodKey = :rodKey"),
	@NamedQuery(name = "DocAcknowledgement.findByReimbursement", query = "SELECT o FROM DocAcknowledgement o where o.rodKey = :rodKey order by o.key asc"),
	@NamedQuery(name = "DocAcknowledgement.getByClaimKey", query = "SELECT o FROM DocAcknowledgement o where o.claim.key = :claimKey order by o.key desc"),
	@NamedQuery(name = "DocAcknowledgement.findByLatestAcknowledge", query = "SELECT o FROM DocAcknowledgement o where o.rodKey = :rodKey  order by o.key desc"),
	@NamedQuery(name = "DocAcknowledgement.findByLatestClaimKey", query = "SELECT o FROM DocAcknowledgement o where o.claim.key = :claimkey order by o.rodKey desc"),
	@NamedQuery(name = "DocAcknowledgement.findNonCancelledAck", query = "SELECT o FROM DocAcknowledgement o where o.claim.key = :claimKey and o.status.key <> :statusId"),
	@NamedQuery(name = "DocAcknowledgement.findAckNumber", query = "SELECT o FROM DocAcknowledgement o where o.acknowledgeNumber like :ackNo"),
	@NamedQuery(name = "DocAcknowledgement.findLatestAckByRod", query = "SELECT o FROM DocAcknowledgement o where o.rodKey = :rodKey order by o.key desc"),
	@NamedQuery(name = "DocAcknowledgement.getByClaimKeyWithReconsiderflag", query = "SELECT o FROM DocAcknowledgement o where o.claim.key = :claimkey and o.reconsiderationRequest = 'Y' order by o.key desc"),
	@NamedQuery(name = "DocAcknowledgement.getByClaimKeyWithRodNull", query = "SELECT o FROM DocAcknowledgement o where o.claim.key = :claimkey and o.modeOfReceiptId.value = :rmode and o.modeOfReceiptId.code = :mcode order by o.key desc"),
	@NamedQuery(name = "DocAcknowledgement.getAckByRodKey", query = "SELECT o FROM DocAcknowledgement o where o.rodKey = :rodKey and o.modeOfReceiptId.value = :rmode and o.modeOfReceiptId.code = :mcode order by o.key desc"),
	@NamedQuery(name = "DocAcknowledgement.findhspAckByClaim", query = "SELECT o FROM DocAcknowledgement o where o.claim.key = :claimkey and (o.hospitalisationFlag='Y' or o.partialHospitalisationFlag = 'Y') order by o.rodKey desc"),
	@NamedQuery(name = "DocAcknowledgement.findByFlag", query = "SELECT o FROM DocAcknowledgement o where o.ackLetterGenFlag = :ackLetterFlag")
	
})
public class DocAcknowledgement extends AbstractEntity {
	

	private static final long serialVersionUID = 4052781126397799714L;

	@Id
	@SequenceGenerator(name="IMS_ACKNOWLEDGE_DOCUMENT_KEY_GENERATOR", sequenceName = "SEQ_DOC_ACKNOWLEDGEMENT_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_ACKNOWLEDGE_DOCUMENT_KEY_GENERATOR" ) 
	@Column(name="DOC_ACKNOWLEDGEMENT_KEY")
	private Long key;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CLAIM_KEY", nullable = false)
	private Claim claim;
	
	@Column(name = "ACKNOWLEDGE_NUMBER")
	private String acknowledgeNumber;
	
	@Column(name = "ROD_KEY")
	private Long rodKey;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="DOCUMENT_RECEIVED_FROM_ID", nullable=true)
	private MastersValue documentReceivedFromId;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DOCUMENT_RECEIVED_DATE")
	private Date documentReceivedDate;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MODE_OF_RECEIPT_ID" , nullable= true)
	private MastersValue modeOfReceiptId;
	
	@Column(name = "RECONSIDERATION_REQUEST")
	private String reconsiderationRequest;
	
	@Column(name = "INSURED_CONTACT_NUMBER")
	private String insuredContactNumber;
	
	@Column(name = "INSURED_EMAIL_ID")
	private String insuredEmailId;
	
	
	@Column(name = "HOSPITALISATION_FLAG")
	private String hospitalisationFlag;
	
	@Column(name = "PRE_HOSPITALISATION_FLAG")
	private String preHospitalisationFlag;
	
	@Column(name = "POST_HOSPITALISATION_FLAG")
	private String postHospitalisationFlag;
	
	@Column(name = "PARTIAL_HOSPITALISATION_FLAG")
	private String partialHospitalisationFlag;
	
	@Column(name = "HOSPITALIZATION_REPEAT_FLAG")
	private String hospitalizationRepeatFlag;
	
	@Column(name = "LUMPSUM_AMOUNT_FLAG")
	private String lumpsumAmountFlag;
	
	@Column(name = "HOSPITAL_CASH_FLAG")
	private String hospitalCashFlag;
	
	@Column(name = "PATIENT_CARE_FLAG")
	private String patientCareFlag;
	
	@Column(name = "ADDITIONAL_REMARKS")
	private String additionalRemarks;
	
	@Column(name = "ACTIVE_STATUS")
	private Long activeStatus;
	
	@Column(name = "OFFICE_CODE")
	private String officeCode;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STAGE_ID", nullable= false)
	private Stage stage;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATUS_ID", nullable= false)
	private Status status;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name = "HOSPITALIZATION_CLAIMED_AMOUNT")
	private Double hospitalizationClaimedAmount;
	
	@Column(name = "PRE_HOSPITALIZATION_CLAIM_AMT")
	private Double preHospitalizationClaimedAmount;
	
	@Column(name = "POST_HOSPITALIZATION_CLAIM_AMT")
	private Double postHospitalizationClaimedAmount;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="RECONSIDERATION_REASON_ID", nullable=true)
	private MastersValue reconsiderationReasonId;
	
	
	@Column(name = "CANCEL_REMARKS")
	private String cancelRemarks;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CANCEL_REASON_ID", nullable=true)
	private MastersValue cancelReason;
	
	@Column(name = "PAYMENT_CANCELLATION_FLAG")
	private String paymentCancellationFlag; 
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RECONSIDERED_DATE")
	private Date reconsideredDate;
	
	@Column(name = "BENEFITS_FLAG  ")
	private String benifitFlag; 	
	
	@Column(name = "CLAIMED_AMOUNT")
	private Double  benifitClaimedAmount;
	
	@Column(name = "DOC_TYPE_ID")
	private Long  documentTypeId;	
	
	@Column(name = "PROCESS_CLM_TYPE")
	private String  processClaimType;	
	
	@Column(name = "WORKPLACE")
	private String workPlace;


	@Column(name = "OTHER_BENEFIT_FLAG")
	private String otherBenefitsFlag;
	
	@Column(name = "EMERGENCY_MEDICAL_EVACUATION")
	private String emergencyMedicalEvaluation;
	
	@Column(name = "COMPASSIONATE_TRAVEL")
	private String compassionateTravel;
	
	@Column(name = "REPATRIATION_OF_MORTAL_REMAINS")
	private String repatriationOfMortalRemain;
	
	@Column(name = "PREFERRED_NETWORK_HOSPITAL")
	private String preferredNetworkHospita;
	
	@Column(name = "SHARED_ACCOMMODATION")
	private String sharedAccomodation;

	@Column(name = "OTHER_BENEFITS_CLAIM_AMT")
	private Double otherBenefitsClaimedAmount;
	
	@Column(name = "MAKER_REMARKS")
	private String makerRemarks;
	
	@Column(name = "MAKER_VERIFIED")
	private String makerVerified;
	
	@Column(name = "CHECKER_VERIFIED")
	private String checkerVerified;
	
	@Column(name = "SOURCE_OF_DOC")
	private String sourceOfDocument;
	
	@Column(name = "ACK_HOSP_CLMED_AMT")
	private Double hospClaimedAmountDocRec;
	
	@Column(name = "ACK_PRE_HOSP_CLAIM_AMT")
	private Double preHospClaimedAmountDocRec;
	
	@Column(name = "ACK_POST_HOSP_CLAIM_AMT")
	private Double postHospClaimedAmountDocRec;
	
	@Column(name = "ACK_OTHER_BENEFITS_CLAIM_AMT")
	private Double otherBenefitsAmountDocRec;
	
	@Column(name = "ACK_DOC_VERIFIED")
	private String documentVerifiedFlag;
	
	@Column(name = "CROD_DOC_VERIFIED")
	private String rodDocumentVerifiedFlag;
	
	@Column(name = "PHC_BENEFIT_FLAG")
	private String prodHospBenefitFlag;

	@Column(name = "PHC_BENEFIT_CLAIMED_AMT")
	private Double prodHospBenefitClaimedAmount;
	
	@Column(name = "NHP_ACK_LETTER_GEN_FLAG")
	private String ackLetterGenFlag;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "NHP_ACK_LETTER_GEN_DATE")
	private Date ackLetterGenDate;
	
	@Column(name = "STAR_WOMEN_CARE")
	private String starWomenCare;

	/**
	 * @return the key
	 */
	public Long getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(Long key) {
		this.key = key;
	}

	/**
	 * @return the claim
	 */
	public Claim getClaim() {
		return claim;
	}

	/**
	 * @param claim the claim to set
	 */
	public void setClaim(Claim claim) {
		this.claim = claim;
	}


	/**
	 * @return the acknowledgeNumber
	 */
	public String getAcknowledgeNumber() {
		return acknowledgeNumber;
	}

	/**
	 * @param acknowledgeNumber the acknowledgeNumber to set
	 */
	public void setAcknowledgeNumber(String acknowledgeNumber) {
		this.acknowledgeNumber = acknowledgeNumber;
	}


	/**
	 * @return the documentReceivedFromId
	 */
	public MastersValue getDocumentReceivedFromId() {
		return documentReceivedFromId;
	}

	/**
	 * @param documentReceivedFromId the documentReceivedFromId to set
	 */
	public void setDocumentReceivedFromId(MastersValue documentReceivedFromId) {
		this.documentReceivedFromId = documentReceivedFromId;
	}

	/**
	 * @return the documentReceivedDate
	 */
	public Date getDocumentReceivedDate() {
		return documentReceivedDate;
	}

	/**
	 * @param documentReceivedDate the documentReceivedDate to set
	 */
	public void setDocumentReceivedDate(Date documentReceivedDate) {
		this.documentReceivedDate = documentReceivedDate;
	}

	

	/**
	 * @return the reconsiderationRequest
	 */
	public String getReconsiderationRequest() {
		return reconsiderationRequest;
	}

	/**
	 * @param reconsiderationRequest the reconsiderationRequest to set
	 */
	public void setReconsiderationRequest(String reconsiderationRequest) {
		this.reconsiderationRequest = reconsiderationRequest;
	}



	/**
	 * @return the insuredEmailId
	 */
	public String getInsuredEmailId() {
		return insuredEmailId;
	}

	/**
	 * @param insuredEmailId the insuredEmailId to set
	 */
	public void setInsuredEmailId(String insuredEmailId) {
		this.insuredEmailId = insuredEmailId;
	}

	/**
	 * @return the hospitalisationFlag
	 */
	public String getHospitalisationFlag() {
		return hospitalisationFlag;
	}

	/**
	 * @param hospitalisationFlag the hospitalisationFlag to set
	 */
	public void setHospitalisationFlag(String hospitalisationFlag) {
		this.hospitalisationFlag = hospitalisationFlag;
	}

	/**
	 * @return the preHospitalisationFlag
	 */
	public String getPreHospitalisationFlag() {
		return preHospitalisationFlag;
	}

	/**
	 * @param preHospitalisationFlag the preHospitalisationFlag to set
	 */
	public void setPreHospitalisationFlag(String preHospitalisationFlag) {
		this.preHospitalisationFlag = preHospitalisationFlag;
	}

	/**
	 * @return the postHospitalisationFlag
	 */
	public String getPostHospitalisationFlag() {
		return postHospitalisationFlag;
	}

	/**
	 * @param postHospitalisationFlag the postHospitalisationFlag to set
	 */
	public void setPostHospitalisationFlag(String postHospitalisationFlag) {
		this.postHospitalisationFlag = postHospitalisationFlag;
	}

	/**
	 * @return the partialHospitalisationFlag
	 */
	public String getPartialHospitalisationFlag() {
		return partialHospitalisationFlag;
	}

	/**
	 * @param partialHospitalisationFlag the partialHospitalisationFlag to set
	 */
	public void setPartialHospitalisationFlag(String partialHospitalisationFlag) {
		this.partialHospitalisationFlag = partialHospitalisationFlag;
	}

	/**
	 * @return the lumpsumAmountFlag
	 */
	public String getLumpsumAmountFlag() {
		return lumpsumAmountFlag;
	}

	/**
	 * @param lumpsumAmountFlag the lumpsumAmountFlag to set
	 */
	public void setLumpsumAmountFlag(String lumpsumAmountFlag) {
		this.lumpsumAmountFlag = lumpsumAmountFlag;
	}

	/**
	 * @return the hospitalCashFlag
	 */
	public String getHospitalCashFlag() {
		return hospitalCashFlag;
	}

	/**
	 * @param hospitalCashFlag the hospitalCashFlag to set
	 */
	public void setHospitalCashFlag(String hospitalCashFlag) {
		this.hospitalCashFlag = hospitalCashFlag;
	}

	/**
	 * @return the patientCareFlag
	 */
	public String getPatientCareFlag() {
		return patientCareFlag;
	}

	/**
	 * @param patientCareFlag the patientCareFlag to set
	 */
	public void setPatientCareFlag(String patientCareFlag) {
		this.patientCareFlag = patientCareFlag;
	}

	/**
	 * @return the additionalRemarks
	 */
	public String getAdditionalRemarks() {
		return additionalRemarks;
	}

	/**
	 * @param additionalRemarks the additionalRemarks to set
	 */
	public void setAdditionalRemarks(String additionalRemarks) {
		this.additionalRemarks = additionalRemarks;
	}

	/**
	 * @return the activeStatus
	 */
	public Long getActiveStatus() {
		return activeStatus;
	}

	/**
	 * @param activeStatus the activeStatus to set
	 */
	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	/**
	 * @return the officeCode
	 */
	public String getOfficeCode() {
		return officeCode;
	}

	/**
	 * @param officeCode the officeCode to set
	 */
	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

	/**
	 * @return the stage
	 */
	public Stage getStage() {
		return stage;
	}

	/**
	 * @param stage the stage to set
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the modifiedBy
	 */
	public String getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * @param modifiedBy the modifiedBy to set
	 */
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 * @return the modifiedDate
	 */
	public Date getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * @param modifiedDate the modifiedDate to set
	 */
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/**
	 * @return the rodKey
	 */
	public Long getRodKey() {
		return rodKey;
	}

	/**
	 * @param rodKey the rodKey to set
	 */
	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}

	/**
	 * @return the modeOfReceiptId
	 */
	public MastersValue getModeOfReceiptId() {
		return modeOfReceiptId;
	}

	/**
	 * @param modeOfReceiptId the modeOfReceiptId to set
	 */
	public void setModeOfReceiptId(MastersValue modeOfReceiptId) {
		this.modeOfReceiptId = modeOfReceiptId;
	}

	public Double getHospitalizationClaimedAmount() {
		return hospitalizationClaimedAmount;
	}

	public void setHospitalizationClaimedAmount(Double hospitalizationClaimedAmount) {
		this.hospitalizationClaimedAmount = hospitalizationClaimedAmount;
	}

	public Double getPreHospitalizationClaimedAmount() {
		return preHospitalizationClaimedAmount;
	}

	public void setPreHospitalizationClaimedAmount(
			Double preHospitalizationClaimedAmount) {
		this.preHospitalizationClaimedAmount = preHospitalizationClaimedAmount;
	}

	public Double getPostHospitalizationClaimedAmount() {
		return postHospitalizationClaimedAmount;
	}

	public void setPostHospitalizationClaimedAmount(
			Double postHospitalizationClaimedAmount) {
		this.postHospitalizationClaimedAmount = postHospitalizationClaimedAmount;
	}

	public String getInsuredContactNumber() {
		return insuredContactNumber;
	}

	public void setInsuredContactNumber(String insuredContactNumber) {
		this.insuredContactNumber = insuredContactNumber;
	}

	public String getHospitalizationRepeatFlag() {
		return hospitalizationRepeatFlag;
	}

	public void setHospitalizationRepeatFlag(String hospitalizationRepeatFlag) {
		this.hospitalizationRepeatFlag = hospitalizationRepeatFlag;
	}

	public MastersValue getReconsiderationReasonId() {
		return reconsiderationReasonId;
	}

	public void setReconsiderationReasonId(MastersValue reconsiderationReasonId) {
		this.reconsiderationReasonId = reconsiderationReasonId;
	}

	public String getCancelRemarks() {
		return cancelRemarks;
	}

	public void setCancelRemarks(String cancelRemarks) {
		this.cancelRemarks = cancelRemarks;
	}

	public MastersValue getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(MastersValue cancelReason) {
		this.cancelReason = cancelReason;
	}

	public String getPaymentCancellationFlag() {
		return paymentCancellationFlag;
	}

	public void setPaymentCancellationFlag(String paymentCancellationFlag) {
		this.paymentCancellationFlag = paymentCancellationFlag;
	}

	public Date getReconsideredDate() {
		return reconsideredDate;
	}

	public void setReconsideredDate(Date reconsideredDate) {
		this.reconsideredDate = reconsideredDate;
	}

	
	public String getBenifitFlag() {
		return benifitFlag;
	}

	public void setBenifitFlag(String benifitFlag) {
		this.benifitFlag = benifitFlag;
	}

	public Double getBenifitClaimedAmount() {
		return benifitClaimedAmount;
	}

	public void setBenifitClaimedAmount(Double benifitClaimedAmount) {
		this.benifitClaimedAmount = benifitClaimedAmount;
	}

	public Long getDocumentTypeId() {
		return documentTypeId;
	}

	public void setDocumentTypeId(Long documentTypeId) {
		this.documentTypeId = documentTypeId;
	}

	public String getProcessClaimType() {
		return processClaimType;
	}

	public void setProcessClaimType(String processClaimType) {
		this.processClaimType = processClaimType;
	}

	public String getWorkPlace() {
		return workPlace;
	}

	public void setWorkPlace(String workPlace) {
		this.workPlace = workPlace;
	}

	
	


	
	
	

/*	public MastersValue getReconsiderationReasonId() {
		return reconsiderationReasonId;
	}

	public void setReconsiderationReasonId(MastersValue reconsiderationReasonId) {
		this.reconsiderationReasonId = reconsiderationReasonId;
	}*/

		
	public String getOtherBenefitsFlag() {
		return otherBenefitsFlag;
	}

	public void setOtherBenefitsFlag(String otherBenefitsFlag) {
		this.otherBenefitsFlag = otherBenefitsFlag;
	}

	public String getEmergencyMedicalEvaluation() {
		return emergencyMedicalEvaluation;
	}

	public void setEmergencyMedicalEvaluation(String emergencyMedicalEvaluation) {
		this.emergencyMedicalEvaluation = emergencyMedicalEvaluation;
	}

	public String getCompassionateTravel() {
		return compassionateTravel;
	}

	public void setCompassionateTravel(String compassionateTravel) {
		this.compassionateTravel = compassionateTravel;
	}

	public String getRepatriationOfMortalRemain() {
		return repatriationOfMortalRemain;
	}

	public void setRepatriationOfMortalRemain(String repatriationOfMortalRemain) {
		this.repatriationOfMortalRemain = repatriationOfMortalRemain;
	}

	public String getPreferredNetworkHospita() {
		return preferredNetworkHospita;
	}

	public void setPreferredNetworkHospita(String preferredNetworkHospita) {
		this.preferredNetworkHospita = preferredNetworkHospita;
	}

	public String getSharedAccomodation() {
		return sharedAccomodation;
	}

	public void setSharedAccomodation(String sharedAccomodation) {
		this.sharedAccomodation = sharedAccomodation;
	}

	public Double getOtherBenefitsClaimedAmount() {
		return otherBenefitsClaimedAmount;
	}

	public void setOtherBenefitsClaimedAmount(Double otherBenefitsClaimedAmount) {
		this.otherBenefitsClaimedAmount = otherBenefitsClaimedAmount;
	}

	public String getMakerRemarks() {
		return makerRemarks;
	}

	public void setMakerRemarks(String makerRemarks) {
		this.makerRemarks = makerRemarks;
	}

	public String getMakerVerified() {
		return makerVerified;
	}

	public void setMakerVerified(String makerVerified) {
		this.makerVerified = makerVerified;
	}

	public String getCheckerVerified() {
		return checkerVerified;
	}

	public void setCheckerVerified(String checkerVerified) {
		this.checkerVerified = checkerVerified;
	}
	
	@Override
    public boolean equals(Object obj) {
        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        } else if (getKey() == null) {
            return obj == this;
        } else {
            return getKey().equals(((DocAcknowledgement) obj).getKey());
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

	public String getSourceOfDocument() {
		return sourceOfDocument;
	}

	public void setSourceOfDocument(String sourceOfDocument) {
		this.sourceOfDocument = sourceOfDocument;
	}

	
	public String getDocumentVerifiedFlag() {
		return documentVerifiedFlag;
	}

	public void setDocumentVerifiedFlag(String documentVerifiedFlag) {
		this.documentVerifiedFlag = documentVerifiedFlag;
	}

	public Double getHospClaimedAmountDocRec() {
		return hospClaimedAmountDocRec;
	}

	public void setHospClaimedAmountDocRec(Double hospClaimedAmountDocRec) {
		this.hospClaimedAmountDocRec = hospClaimedAmountDocRec;
	}

	public Double getPreHospClaimedAmountDocRec() {
		return preHospClaimedAmountDocRec;
	}

	public void setPreHospClaimedAmountDocRec(Double preHospClaimedAmountDocRec) {
		this.preHospClaimedAmountDocRec = preHospClaimedAmountDocRec;
	}

	public Double getPostHospClaimedAmountDocRec() {
		return postHospClaimedAmountDocRec;
	}

	public void setPostHospClaimedAmountDocRec(Double postHospClaimedAmountDocRec) {
		this.postHospClaimedAmountDocRec = postHospClaimedAmountDocRec;
	}

	public Double getOtherBenefitsAmountDocRec() {
		return otherBenefitsAmountDocRec;
	}

	public void setOtherBenefitsAmountDocRec(Double otherBenefitsAmountDocRec) {
		this.otherBenefitsAmountDocRec = otherBenefitsAmountDocRec;
	}

	public String getRodDocumentVerifiedFlag() {
		return rodDocumentVerifiedFlag;
	}

	public void setRodDocumentVerifiedFlag(String rodDocumentVerifiedFlag) {
		this.rodDocumentVerifiedFlag = rodDocumentVerifiedFlag;
	}

	public String getProdHospBenefitFlag() {
		return prodHospBenefitFlag;
	}

	public void setProdHospBenefitFlag(String prodHospBenefitFlag) {
		this.prodHospBenefitFlag = prodHospBenefitFlag;
	}

	public Double getProdHospBenefitClaimedAmount() {
		return prodHospBenefitClaimedAmount;
	}

	public void setProdHospBenefitClaimedAmount(Double prodHospBenefitClaimedAmount) {
		this.prodHospBenefitClaimedAmount = prodHospBenefitClaimedAmount;
	}

	public String getAckLetterGenFlag() {
		return ackLetterGenFlag;
	}

	public void setAckLetterGenFlag(String ackLetterGenFlag) {
		this.ackLetterGenFlag = ackLetterGenFlag;
	}

	public Date getAckLetterGenDate() {
		return ackLetterGenDate;
	}

	public void setAckLetterGenDate(Date ackLetterGenDate) {
		this.ackLetterGenDate = ackLetterGenDate;
	}

	public String getStarWomenCare() {
		return starWomenCare;
	}

	public void setStarWomenCare(String starWomenCare) {
		this.starWomenCare = starWomenCare;
	}

    
	
/*	public MastersValue getReconsiderationReasonId() {
		return reconsiderationReasonId;
	}

	public void setReconsiderationReasonId(MastersValue reconsiderationReasonId) {
		this.reconsiderationReasonId = reconsiderationReasonId;
	}*/
	

}

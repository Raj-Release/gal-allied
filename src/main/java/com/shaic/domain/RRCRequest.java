/**
 * 
 */
package com.shaic.domain;

/**
 * @author ntv.vijayar
 *
 */
import java.io.Serializable;
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
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.Stage;


/**
 * The persistent class for the IMS_CLS_RRC_REQUEST database table.
 * 
 */
@Entity
@Table(name="IMS_CLS_RRC_REQUEST")
@NamedQueries({
	@NamedQuery(name="RRCRequest.findAll", query="SELECT m FROM RRCRequest m"),
	@NamedQuery(name="RRCRequest.findByKey", query="SELECT m FROM RRCRequest m where m.rrcRequestKey = :rrcRequestKey"),
	@NamedQuery(name = "RRCRequest.CountAckByClaimKey", query = "SELECT count(o) FROM RRCRequest o where o.claim.key = :claimkey"),
	@NamedQuery(name = "RRCRequest.CountAckByRRCRequestNo", query = "SELECT m FROM RRCRequest m where m.rrcRequestNumber = :rrcRequestNumber order by m.rrcRequestKey desc"),
	@NamedQuery(name = "RRCRequest.findByReimburesmentKey", query = "SELECT o FROM RRCRequest o where o.reimbursement.key = :rodKey"),
	@NamedQuery(name = "RRCRequest.findByClaimKey", query = "SELECT o FROM RRCRequest o where o.claim.key = :claimKey"),
	@NamedQuery(name = "RRCRequest.findLatestRRCByClaimKey", query = "SELECT o FROM RRCRequest o where o.claim.key = :claimKey order by o.rrcRequestKey desc"),
	@NamedQuery(name = "RRCRequest.findByStageKey", query = "SELECT o FROM RRCRequest o where o.stage.key = :stageKey"),
	@NamedQuery(name = "RRCRequest.findByStatusKey", query = "SELECT o FROM RRCRequest o where o.status.key = :statusKey"),
	@NamedQuery(name = "RRCRequest.findByUniqueRecord", query="SELECT o FROM RRCRequest o where o.claim.key IN (:claimKey) and o.rrcRequestNumber IN (:rrcRequestNumber) and o.rrcRequestKey in(select MAX(m.rrcRequestKey) from RRCRequest m group by m.rrcRequestNumber) order by o.rrcRequestKey asc"),
	@NamedQuery(name = "RRCRequest.findByClaimKeyForInitiate", query = "SELECT o FROM RRCRequest o where o.claim.key = :claimKey and o.processedBy is null and o.processedDate is null and o.reviewedBy is null and o.reviewedDate is null"),
	@NamedQuery(name="RRCRequest.findByinKey", query="SELECT m FROM RRCRequest m where m.rrcRequestKey in (:rrcRequestKey)"),
	@NamedQuery(name = "RRCRequest.findByintimationNo", query = "SELECT o FROM RRCRequest o where o.claim.intimation.intimationId = :intimationId")
	
	//@NamedQuery(name="TmpEmployee.findByCPUId", query="SELECT m FROM TmpEmployee m WHERE m.empCPUId = :cpuId"),
	//@NamedQuery(name="TmpEmployee.findByEmpName", query="SELECT m FROM TmpEmployee m WHERE m.empFirstName = :empName")
})
public class RRCRequest extends AbstractEntity implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1086997314154372927L;

	@Id
	@SequenceGenerator(name="IMS_CLS_RRC_REQUEST_GENERATOR", sequenceName = "SEQ_RRC_REQUEST_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_RRC_REQUEST_GENERATOR" ) 
	@Column(name="RRC_REQUEST_KEY")
	private Long rrcRequestKey;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="REIMBURSEMENT_KEY", nullable=true)
	private Reimbursement reimbursement;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CASHLESS_KEY", nullable=true)
	private Preauth preauth;
	
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CLAIM_KEY", nullable=true)
	private Claim claim;
	
	@Column(name = "RRC_REQUEST_NUMBER")
	private String rrcRequestNumber;
	
	@Column(name = "PRE_AUTH_AMOUNT")
	private Long preAuthAmount;
	
	@Column(name = "FINAL_BILL_AMOUNT")
	private Long finalBillAmount;
	
	@Column(name = "SETTLEMENT_AMOUNT")
	private Long settlementAmount;
	
	@Column(name = "ANH_AMOUNT")
	private Long anhAmount;
	
	@Column(name = "DIAGNOSIS")
	private String diagnosis;
	
	@Column(name = "MANAGEMENT")
	private String management;
	
	
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="SIGNIFICANT_CLININCAL_ID", nullable=true)
	private MastersValue significantClinicalId;
	
	@Column(name = "REQUEST_REMARKS")
	private String requestRemarks;
	
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="REQUESTED_TYPE_ID", nullable=true)
	private MastersValue requestedTypeId;
	
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ELIGIBILITY_TYPE_ID" , nullable = true)
	private MastersValue eligiblityTypeId;
	
	@Column(name = "REQUESTOR_SAVED_AMOUNT")
	private Long requestorSavedAmount;
	
	@Column(name = "SAVED_AMOUNT")
	private Long savedAmount;
	
	@Column(name = "ELIGIBILITY_REMARKS")
	private String eligibiltyRemarks;
	
	@Column(name = "REQUEST_HOLD_REMARKS")
	private String requestHoldRemarks;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RRC_INITIATED_DATE")
	private Date rrcInitiatedDate;
	
	@Column(name = "PROCESSED_BY")
	private String processedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PROCESSED_DATE")
	private Date processedDate;

	@Column(name = "REVIEWED_BY")
	private String reviewedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REVIEWED_DATE")
	private Date reviewedDate;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REQUESTED_STAGE_ID", nullable= true)
	private Stage requestedStageId;
	
	@Column(name = "ACTIVE_STATUS")
	private Long activeStatus;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STAGE_ID", nullable= true)
	private Stage stage;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATUS_ID", nullable= true)
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
	
	@Column(name = "REVIEW_REMARKS")
	private String reviewRemarks;
	
	@Column(name = "RRC_TYPE")
	private String rrcType;
	
	@Column(name = "REVIEWER_SAVED_AMOUNT")
	private Long reviewerSavedAmount;
	
	//@Column(name = "REVIEWER_ELIGIBILITY_TYPE_ID")
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REVIEWER_ELIGIBILITY_TYPE_ID", nullable= true)
	private MastersValue reviewerEligibilityTypeId;
	
	//@Column(name = "MODIFIER_ELIGIBILITY_TYPE_ID")
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MODIFIER_ELIGIBILITY_TYPE_ID", nullable= true)
	private MastersValue modifierEligibilityTypeId;
	
	
	@Column(name =  "MODIFIER_SAVED_AMOUNT")
	private Long modifierSavedAmount;
	
	@Column(name = "MODIFY_REMARKS")
	private String modifyRemarks;
	
	@Column(name = "REQUESTOR_ID")
	private String requestorID;
	
	@Column(name = "ANH_FLAG")
	private String anh;
	

	public Long getRrcRequestKey() {
		return rrcRequestKey;
	}

	public void setRrcRequestKey(Long rrcRequestKey) {
		this.rrcRequestKey = rrcRequestKey;
	}

	public Reimbursement getReimbursement() {
		return reimbursement;
	}

	public void setReimbursement(Reimbursement reimbursement) {
		this.reimbursement = reimbursement;
	}

	/*public Claim getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Claim claimKey) {
		this.claimKey = claimKey;
	}*/

	public String getRrcRequestNumber() {
		return rrcRequestNumber;
	}

	public void setRrcRequestNumber(String rrcRequestNumber) {
		this.rrcRequestNumber = rrcRequestNumber;
	}

	public Long getPreAuthAmount() {
		return preAuthAmount;
	}

	public void setPreAuthAmount(Long preAuthAmount) {
		this.preAuthAmount = preAuthAmount;
	}

	public Long getFinalBillAmount() {
		return finalBillAmount;
	}

	public void setFinalBillAmount(Long finalBillAmount) {
		this.finalBillAmount = finalBillAmount;
	}

	public Long getSettlementAmount() {
		return settlementAmount;
	}

	public void setSettlementAmount(Long settlementAmount) {
		this.settlementAmount = settlementAmount;
	}

	public Long getAnhAmount() {
		return anhAmount;
	}

	public void setAnhAmount(Long anhAmount) {
		this.anhAmount = anhAmount;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public String getManagement() {
		return management;
	}

	public void setManagement(String management) {
		this.management = management;
	}

	public MastersValue getSignificantClinicalId() {
		return significantClinicalId;
	}

	public void setSignificantClinicalId(MastersValue significantClinicalId) {
		this.significantClinicalId = significantClinicalId;
	}

	public String getRequestRemarks() {
		return requestRemarks;
	}

	public void setRequestRemarks(String requestRemarks) {
		this.requestRemarks = requestRemarks;
	}

	public MastersValue getRequestedTypeId() {
		return requestedTypeId;
	}

	public void setRequestedTypeId(MastersValue requestedTypeId) {
		this.requestedTypeId = requestedTypeId;
	}

	public MastersValue getEligiblityTypeId() {
		return eligiblityTypeId;
	}

	public void setEligiblityTypeId(MastersValue eligiblityTypeId) {
		this.eligiblityTypeId = eligiblityTypeId;
	}

	public Long getRequestorSavedAmount() {
		return requestorSavedAmount;
	}

	public void setRequestorSavedAmount(Long requestorSavedAmount) {
		this.requestorSavedAmount = requestorSavedAmount;
	}

	public Long getSavedAmount() {
		return savedAmount;
	}

	public void setSavedAmount(Long savedAmount) {
		this.savedAmount = savedAmount;
	}

	public String getEligibiltyRemarks() {
		return eligibiltyRemarks;
	}

	public void setEligibiltyRemarks(String eligibiltyRemarks) {
		this.eligibiltyRemarks = eligibiltyRemarks;
	}

	public String getRequestHoldRemarks() {
		return requestHoldRemarks;
	}

	public void setRequestHoldRemarks(String requestHoldRemarks) {
		this.requestHoldRemarks = requestHoldRemarks;
	}

	public Date getRrcInitiatedDate() {
		return rrcInitiatedDate;
	}

	public void setRrcInitiatedDate(Date rrcInitiatedDate) {
		this.rrcInitiatedDate = rrcInitiatedDate;
	}

	public String getProcessedBy() {
		return processedBy;
	}

	public void setProcessedBy(String processedBy) {
		this.processedBy = processedBy;
	}

	public Date getProcessedDate() {
		return processedDate;
	}

	public void setProcessedDate(Date processedDate) {
		this.processedDate = processedDate;
	}

	public String getReviewedBy() {
		return reviewedBy;
	}

	public void setReviewedBy(String reviewedBy) {
		this.reviewedBy = reviewedBy;
	}

	public Date getReviewedDate() {
		return reviewedDate;
	}

	public void setReviewedDate(Date reviewedDate) {
		this.reviewedDate = reviewedDate;
	}


	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Claim getClaim() {
		return claim;
	}

	public void setClaim(Claim claim) {
		this.claim = claim;
	}

	@Override
	public Long getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setKey(Long key) {
		// TODO Auto-generated method stub
		
	}



	public String getReviewRemarks() {
		return reviewRemarks;
	}

	public void setReviewRemarks(String reviewRemarks) {
		this.reviewRemarks = reviewRemarks;
	}

	public String getRrcType() {
		return rrcType;
	}

	public void setRrcType(String rrcType) {
		this.rrcType = rrcType;
	}

	public Long getReviewerSavedAmount() {
		return reviewerSavedAmount;
	}

	public void setReviewerSavedAmount(Long reviewerSavedAmount) {
		this.reviewerSavedAmount = reviewerSavedAmount;
	}

	public MastersValue getReviewerEligibilityTypeId() {
		return reviewerEligibilityTypeId;
	}

	public void setReviewerEligibilityTypeId(MastersValue reviewerEligibilityTypeId) {
		this.reviewerEligibilityTypeId = reviewerEligibilityTypeId;
	}

	public MastersValue getModifierEligibilityTypeId() {
		return modifierEligibilityTypeId;
	}

	public void setModifierEligibilityTypeId(MastersValue modifierEligibilityTypeId) {
		this.modifierEligibilityTypeId = modifierEligibilityTypeId;
	}

	public Long getModifierSavedAmount() {
		return modifierSavedAmount;
	}

	public void setModifierSavedAmount(Long modifierSavedAmount) {
		this.modifierSavedAmount = modifierSavedAmount;
	}

	public String getModifyRemarks() {
		return modifyRemarks;
	}

	public void setModifyRemarks(String modifyRemarks) {
		this.modifyRemarks = modifyRemarks;
	}

	public Stage getRequestedStageId() {
		return requestedStageId;
	}

	public void setRequestedStageId(Stage requestedStageId) {
		this.requestedStageId = requestedStageId;
	}

	public Preauth getPreauth() {
		return preauth;
	}

	public void setPreauth(Preauth preauth) {
		this.preauth = preauth;
	}

	public String getRequestorID() {
		return requestorID;
	}

	public void setRequestorID(String requestorID) {
		this.requestorID = requestorID;
	}

	public String getAnh() {
		return anh;
	}

	public void setAnh(String anh) {
		this.anh = anh;
	}

}

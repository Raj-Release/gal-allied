package com.shaic.domain.outpatient;

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

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.Claim;
import com.shaic.domain.MastersValue;
import com.shaic.domain.OPClaim;
import com.shaic.domain.OPIntimation;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.Stage;

@Entity
@Table(name = "IMS_CLS_OP_HEALTH_CHECKUP")
@NamedQueries({
	@NamedQuery(name="OPHealthCheckup.findAll", query="SELECT r FROM OPHealthCheckup r"),
	@NamedQuery(name ="OPHealthCheckup.findByKey",query="SELECT r FROM OPHealthCheckup r WHERE r.key = :primaryKey"),
	@NamedQuery(name ="OPHealthCheckup.findByClaim",query="SELECT r FROM OPHealthCheckup r WHERE r.claim is not null and r.claim.key = :claimKey"),
	@NamedQuery(name ="OPHealthCheckup.findByIntimationKey",query="SELECT r FROM OPHealthCheckup r WHERE r.intimation is not null and r.intimation.key = :intimationKey"),
})
public class OPHealthCheckup extends AbstractEntity {
	private static final long serialVersionUID = 6939081087180612810L;

	@Id
	@SequenceGenerator(name="IMS_CLS_OP_HEALTH_CHECKUP_KEY_GENERATOR", sequenceName = "SEQ_OP_HC_Key", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_OP_HEALTH_CHECKUP_KEY_GENERATOR" ) 
	@Column(name="OP_HEALTH_CHECKUP_KEY", updatable=false)
	private Long key;
	
	@OneToOne
	@JoinColumn(name = "CLAIM_KEY", nullable = false)
	private OPClaim claim;
	
	@OneToOne
	@JoinColumn(name = "DOC_RECEIVED_FROM_ID", nullable = true)
	private MastersValue documentReceivedFromId;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DOC_RECEIVED_DATE")
	private Date documentReceivedDate;
	
	@OneToOne
	@JoinColumn(name="MODE_OF_RECEIPT_ID",nullable=true)
	private MastersValue modeOfReceipt;
	
	@Column(name = "PERSON_CONTACT_NUMBER")
	private Long personContactNumber;
	
	@Column(name = "PERSON_EMAIL_ID")
	private String personEmailId;
	
	@Column(name = "ADDITIONAL_REMARKS")
	private String additionalRemarks;
	
	@Column(name = "AMOUNT_ELIGIBLE")
	private Double amountEligible;
	
	@Column(name = "AMOUNT_PAYABLE")
	private Double amountPayable;
	
	@Column(name = "APPROVAL_REMARKS")
	private String approvalRemarks;
	
	@Column(name = "AVAILABLE_SI")
	private Double availableSI;
	
	@Column(name = "REJECTION_REMARKS")
	private String rejectionRemarks;
	
	@Column(name = "PAYMENT_MODE_ID")
	private Long paymentNoteId;
	
	@Column(name = "PAYEE_EMAIL_ID")
	private String payeeEmailId;
	
	@Column(name = "PAN_NUMBER")
	private String panNumber;
	
	@Column(name = "PAYABLE_AT")
	private String payableAt;
	
	@Column(name = "PAYEE_NAME")
	private String payeeName;
	
	@Column(name = "REASON_FOR_CHNAGE")
	private String reasonForChange;
	
	@Column(name = "ACCOUNT_NUMBER")
	private String accountNumber;
	
	@Column(name = "BANK_ID")
	private Long bankId;

	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;
	
	@Column(name = "OFFICE_CODE")
	private String officeCode;
	
	@OneToOne
	@JoinColumn(name = "STATUS_ID", nullable = false)
	private Status status;

	@OneToOne
	@JoinColumn(name = "STAGE_ID", nullable = false)
	private Stage stage;
	
	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	//OP_HEALTH_CHECKUP_DATE, BILL_REC_DATE, OP_HEALTH_REASON, OP_HEALTH_REMARKS
	
	@Column(name = "OP_HEALTH_CHECKUP_DATE")
	private Date opHealthCheckupDate;
	
	@Column(name = "BILL_REC_DATE")
	private Date billReceivedDate;
	
	@OneToOne
	@JoinColumn(name="OP_HEALTH_REASON", nullable=true)
	private MastersValue opReason;
	
	@Column(name = "OP_HEALTH_REMARKS")
	private String opHealthRemarks;
	
	// Second Set New Columns ...
	@Column(name = "DOC_SUBMITTED_NAME")
	private String docSubmittedName;
	
	@Column(name = "LEGAL_HEIR_NAME")
	private String legalHeirName;
	
	@Column(name = "IFSC_CODE")
	private String ifscCode;
	
	
	@Column(name = "REASON_FOR_CHANGE_PAYEE_NAME")
	private String changePayeeNameReason;
	
	@Column(name= "CHEQUE_NO")
	private String chequeNo;
	
	@Column(name="CHEQUE_DATE")
	private Date chequeDate;
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "SETTLED_LETTER_FLAG", length = 1)
	private String settlementLetterFlag;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="INTIMATION_KEY", nullable=true)
	private OPIntimation intimation;
	
	@Column(name = "PER_VISIT")
	private Double perVisitAmount;
	
	@Column(name = "PER_POLICY_PERIOD")
	private Double perPolicyPeriod;
	
	@Column(name="BATCH_NUMBER")
	private String batchNumber;
	
	@Column(name="UPLOAD_DATE")
	private Timestamp uploaddate;
	
	@Override
	public Long getKey() {
		return this.key;
	}

	@Override
	public void setKey(Long key) {
		this.key = key;
	}

	public OPClaim getClaim() {
		return claim;
	}

	public void setClaim(OPClaim claim) {
		this.claim = claim;
	}

	public Date getDocumentReceivedDate() {
		return documentReceivedDate;
	}

	public void setDocumentReceivedDate(Date documentReceivedDate) {
		this.documentReceivedDate = documentReceivedDate;
	}

	public MastersValue getModeOfReceipt() {
		return modeOfReceipt;
	}

	public void setModeOfReceipt(MastersValue modeOfReceipt) {
		this.modeOfReceipt = modeOfReceipt;
	}

	public Long getPersonContactNumber() {
		return personContactNumber;
	}

	public void setPersonContactNumber(Long personContactNumber) {
		this.personContactNumber = personContactNumber;
	}

	public String getPersonEmailId() {
		return personEmailId;
	}

	public void setPersonEmailId(String personEmailId) {
		this.personEmailId = personEmailId;
	}

	public String getAdditionalRemarks() {
		return additionalRemarks;
	}

	public void setAdditionalRemarks(String additionalRemarks) {
		this.additionalRemarks = additionalRemarks;
	}

	public Double getAmountEligible() {
		return amountEligible;
	}

	public void setAmountEligible(Double amountEligible) {
		this.amountEligible = amountEligible;
	}

	public Double getAmountPayable() {
		return amountPayable;
	}

	public void setAmountPayable(Double amountPayable) {
		this.amountPayable = amountPayable;
	}

	public String getApprovalRemarks() {
		return approvalRemarks;
	}

	public void setApprovalRemarks(String approvalRemarks) {
		this.approvalRemarks = approvalRemarks;
	}

	public Double getAvailableSI() {
		return availableSI;
	}

	public void setAvailableSI(Double availableSI) {
		this.availableSI = availableSI;
	}

	public String getRejectionRemarks() {
		return rejectionRemarks;
	}

	public void setRejectionRemarks(String rejectionRemarks) {
		this.rejectionRemarks = rejectionRemarks;
	}

	public Long getPaymentNoteId() {
		return paymentNoteId;
	}

	public void setPaymentNoteId(Long paymentNoteId) {
		this.paymentNoteId = paymentNoteId;
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

	public String getPayableAt() {
		return payableAt;
	}

	public void setPayableAt(String payableAt) {
		this.payableAt = payableAt;
	}

	public String getPayeeName() {
		return payeeName;
	}

	public MastersValue getDocumentReceivedFromId() {
		return documentReceivedFromId;
	}

	public void setDocumentReceivedFromId(MastersValue documentReceivedFromId) {
		this.documentReceivedFromId = documentReceivedFromId;
	}

	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}

	public String getReasonForChange() {
		return reasonForChange;
	}

	public void setReasonForChange(String reasonForChange) {
		this.reasonForChange = reasonForChange;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Long getBankId() {
		return bankId;
	}

	public void setBankId(Long bankId) {
		this.bankId = bankId;
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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
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

	public Date getOpHealthCheckupDate() {
		return opHealthCheckupDate;
	}

	public void setOpHealthCheckupDate(Date opHealthCheckupDate) {
		this.opHealthCheckupDate = opHealthCheckupDate;
	}

	public Date getBillReceivedDate() {
		return billReceivedDate;
	}

	public void setBillReceivedDate(Date billReceivedDate) {
		this.billReceivedDate = billReceivedDate;
	}

	public MastersValue getOpReason() {
		return opReason;
	}

	public void setOpReason(MastersValue opReason) {
		this.opReason = opReason;
	}

	public String getOpHealthRemarks() {
		return opHealthRemarks;
	}

	public void setOpHealthRemarks(String opHealthRemarks) {
		this.opHealthRemarks = opHealthRemarks;
	}

	public String getDocSubmittedName() {
		return docSubmittedName;
	}

	public void setDocSubmittedName(String docSubmittedName) {
		this.docSubmittedName = docSubmittedName;
	}

	public String getLegalHeirName() {
		return legalHeirName;
	}

	public void setLegalHeirName(String legalHeirName) {
		this.legalHeirName = legalHeirName;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	public String getChangePayeeNameReason() {
		return changePayeeNameReason;
	}

	public void setChangePayeeNameReason(String changePayeeNameReason) {
		this.changePayeeNameReason = changePayeeNameReason;
	}

	public String getChequeNo() {
		return chequeNo;
	}

	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}

	public Date getChequeDate() {
		return chequeDate;
	}

	public void setChequeDate(Date chequeDate) {
		this.chequeDate = chequeDate;
	}

	public String getSettlementLetterFlag() {
		return settlementLetterFlag;
	}

	public void setSettlementLetterFlag(String settlementLetterFlag) {
		this.settlementLetterFlag = settlementLetterFlag;
	}

	public OPIntimation getIntimation() {
		return intimation;
	}

	public void setIntimation(OPIntimation intimation) {
		this.intimation = intimation;
	}

	public Double getPerVisitAmount() {
		return perVisitAmount;
	}

	public void setPerVisitAmount(Double perVisitAmount) {
		this.perVisitAmount = perVisitAmount;
	}

	public Double getPerPolicyPeriod() {
		return perPolicyPeriod;
	}

	public void setPerPolicyPeriod(Double perPolicyPeriod) {
		this.perPolicyPeriod = perPolicyPeriod;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public Timestamp getUploaddate() {
		return uploaddate;
	}

	public void setUploaddate(Timestamp uploaddate) {
		this.uploaddate = uploaddate;
	}
	
}

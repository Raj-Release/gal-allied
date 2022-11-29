package com.shaic.claim.legal.processconsumerforum.page.ombudsman;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.legal.processconsumerforum.page.consumerforum.IntimationDetailsDTO;

public class OmbudsmanDetailsDTO {
	
	public OmbudsmanDetailsDTO() {
		intimationDetailsDTO = new IntimationDetailsDTO();
		decisionDetailsDto = new DecisionDetailsDto();
	}
	
	@NotNull(message="Please Select Ombudsman Office")
	private SelectValue ombudsmanOffice;
	
	@NotNull(message="Please Enter Complaint no")
	@Size(min = 1 , message = "Please Enter Complaint no")
	private String complaintNo;
	
	private Date complaintRecieptDate;
	
	@NotNull(message="Please Select Add Days")
	private SelectValue addDays;
	
	@NotNull(message="Please Enter Stipulated Date")
	private Date stipulateDate;
	
	@NotNull(message="Please Select Ombudsman Claim Lock")
	private Boolean isOmbudsmanClaimLock;
	
	private String referToMedical;
	
	private Date receiptOfMedicalOpinion;
	
	@NotNull(message="Please Select Ombudsman Contact")
	@Size(min = 1 , message = "Please Select Ombudsman Contact")
	private String ombudsmanContact;
	
	private String ombudsmanContactAddress;
	
	@NotNull(message="Please Select Decision")
	private SelectValue decision;
	
	@NotNull(message="Please Select Status of the Case")
	private SelectValue statusOfCase;
	
	@NotNull(message="Please Select Pending Status")
	private SelectValue pendingStatus;
	
	private Date dateofHearing;
	
	private SelectValue hearingStatus;
	
	private Boolean selfContainedNote;
	
	private Date notePreparationDate;
	
	private Date noteSubmissionDate;
	
	private SelectValue awardStatus;
	
	private String caseLost;
	
	private String interest;
	
	private Date closureDate;
	
	private Date awardReceiptDate;
	
	private SelectValue reasonOfAward;
	
	private SelectValue adddays;
	
	private String reasonForCaseLost;
	
	private Date lastDateSatisfaction;
	
	private Boolean exgratiaAward;
	
	private Date exgratiaAwardDate;
	
	private String exgratiaAwardAmount;
	
	private String reasonForExgratia;
	
	private IntimationDetailsDTO intimationDetailsDTO;
	
	private String username;
	
	private DecisionDetailsDto decisionDetailsDto;
	
	private SelectValue movedTo;
	
	private Boolean claimReconsideration;
	
	private Boolean cancellationOfPolicy;
	
	private Boolean reconsiderationCancellationOfPolicy;
	
	private Boolean grievanceRequestInitiated;

	@NotNull(message="Please Enter Reconsideration Receipt Date")
	private Date reconsiderationReceiptDate;
	
	private Boolean reconsideration;
	
	@NotNull(message="Please Enter Reconsideration Accept Date")
	private Date reconsiderationAcceptDate;
	
	@NotNull(message="Please Enter Acceptance Amount")
	private Double acceptanceAmount;
	
	@NotNull(message="Please Enter Reconsideration Reject Date")
	private Date reconsiderationRejectDate;
	
	@NotNull(message="Please Enter Refund of Prorata Permium")
	private Double refundofProrataPremium;
	
	@NotNull(message="Please Enter Prorate Premium Refund Date")
	private Date prorataPremiumRefundDate;
	
	@NotNull(message="Please Enter Reconsideration Cancel Policy Accept Date")
	private Date reconsiderationCancelPolicyAcceptDate;
	
	@NotNull(message="Please Enter Amount Refund by the Insured")
	private Double amountrefundbytheInsured;
	
	@NotNull(message="Please Enter Grievance Initiated Date")
	private Date grievanceInitiatedDate;
	
	@NotNull(message="Please Enter Grievance Outcome")
	private SelectValue grievanceOutcome;
	
	@NotNull(message="Please Enter Grievance Remark")
	@Size(min = 1 , message = "Please Enter Grievance Remark")
	private String grievanceRemarks;
	
	private Boolean anyAwardofOmbudsmanContested;
	
	public SelectValue getOmbudsmanOffice() {
		return ombudsmanOffice;
	}

	public void setOmbudsmanOffice(SelectValue ombudsmanOffice) {
		this.ombudsmanOffice = ombudsmanOffice;
	}

	public String getComplaintNo() {
		return complaintNo;
	}

	public void setComplaintNo(String complaintNo) {
		this.complaintNo = complaintNo;
	}

	public Date getComplaintRecieptDate() {
		return complaintRecieptDate;
	}

	public void setComplaintRecieptDate(Date complaintRecieptDate) {
		this.complaintRecieptDate = complaintRecieptDate;
	}

	public SelectValue getAddDays() {
		return addDays;
	}

	public void setAddDays(SelectValue addDays) {
		this.addDays = addDays;
	}

	public Date getStipulateDate() {
		return stipulateDate;
	}

	public void setStipulateDate(Date stipulateDate) {
		this.stipulateDate = stipulateDate;
	}

	public Boolean getIsOmbudsmanClaimLock() {
		return isOmbudsmanClaimLock;
	}

	public void setIsOmbudsmanClaimLock(Boolean isOmbudsmanClaimLock) {
		this.isOmbudsmanClaimLock = isOmbudsmanClaimLock;
	}

	public String getReferToMedical() {
		return referToMedical;
	}

	public void setReferToMedical(String referToMedical) {
		this.referToMedical = referToMedical;
	}

	public Date getReceiptOfMedicalOpinion() {
		return receiptOfMedicalOpinion;
	}

	public void setReceiptOfMedicalOpinion(Date receiptOfMedicalOpinion) {
		this.receiptOfMedicalOpinion = receiptOfMedicalOpinion;
	}

	public String getOmbudsmanContactAddress() {
		return ombudsmanContactAddress;
	}

	public void setOmbudsmanContactAddress(String ombudsmanContactAddress) {
		this.ombudsmanContactAddress = ombudsmanContactAddress;
	}

	public String getOmbudsmanContact() {
		return ombudsmanContact;
	}

	public void setOmbudsmanContact(String ombudsmanContact) {
		this.ombudsmanContact = ombudsmanContact;
	}
	
	public SelectValue getDecision() {
		return decision;
	}

	public void setDecision(SelectValue decision) {
		this.decision = decision;
	}

	public SelectValue getStatusOfCase() {
		return statusOfCase;
	}

	public void setStatusOfCase(SelectValue statusOfCase) {
		this.statusOfCase = statusOfCase;
	}

	public SelectValue getPendingStatus() {
		return pendingStatus;
	}

	public void setPendingStatus(SelectValue pendingStatus) {
		this.pendingStatus = pendingStatus;
	}

	public IntimationDetailsDTO getIntimationDetailsDTO() {
		return intimationDetailsDTO;
	}

	public void setIntimationDetailsDTO(IntimationDetailsDTO intimationDetailsDTO) {
		this.intimationDetailsDTO = intimationDetailsDTO;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public DecisionDetailsDto getDecisionDetailsDto() {
		return decisionDetailsDto;
	}

	public void setDecisionDetailsDto(DecisionDetailsDto decisionDetailsDto) {
		decisionDetailsDto = decisionDetailsDto;
	}

	public SelectValue getMovedTo() {
		return movedTo;
	}

	public void setMovedTo(SelectValue movedTo) {
		this.movedTo = movedTo;
	}

	public Boolean getClaimReconsideration() {
		return claimReconsideration;
	}

	public void setClaimReconsideration(Boolean claimReconsideration) {
		this.claimReconsideration = claimReconsideration;
	}

	public Boolean getCancellationOfPolicy() {
		return cancellationOfPolicy;
	}

	public void setCancellationOfPolicy(Boolean cancellationOfPolicy) {
		this.cancellationOfPolicy = cancellationOfPolicy;
	}

	public Boolean getReconsiderationCancellationOfPolicy() {
		return reconsiderationCancellationOfPolicy;
	}

	public void setReconsiderationCancellationOfPolicy(
			Boolean reconsiderationCancellationOfPolicy) {
		this.reconsiderationCancellationOfPolicy = reconsiderationCancellationOfPolicy;
	}

	public Boolean getGrievanceRequestInitiated() {
		return grievanceRequestInitiated;
	}

	public void setGrievanceRequestInitiated(Boolean grievanceRequestInitiated) {
		this.grievanceRequestInitiated = grievanceRequestInitiated;
	}

	public Date getReconsiderationReceiptDate() {
		return reconsiderationReceiptDate;
	}

	public void setReconsiderationReceiptDate(Date reconsiderationReceiptDate) {
		this.reconsiderationReceiptDate = reconsiderationReceiptDate;
	}

	public Boolean getReconsideration() {
		return reconsideration;
	}

	public void setReconsideration(Boolean reconsideration) {
		this.reconsideration = reconsideration;
	}

	public Double getRefundofProrataPremium() {
		return refundofProrataPremium;
	}

	public void setRefundofProrataPremium(Double refundofProrataPremium) {
		this.refundofProrataPremium = refundofProrataPremium;
	}

	public Date getProrataPremiumRefundDate() {
		return prorataPremiumRefundDate;
	}

	public void setProrataPremiumRefundDate(Date prorataPremiumRefundDate) {
		this.prorataPremiumRefundDate = prorataPremiumRefundDate;
	}

	public Date getReconsiderationCancelPolicyAcceptDate() {
		return reconsiderationCancelPolicyAcceptDate;
	}

	public void setReconsiderationCancelPolicyAcceptDate(
			Date reconsiderationCancelPolicyAcceptDate) {
		this.reconsiderationCancelPolicyAcceptDate = reconsiderationCancelPolicyAcceptDate;
	}

	public Double getAmountrefundbytheInsured() {
		return amountrefundbytheInsured;
	}

	public void setAmountrefundbytheInsured(Double amountrefundbytheInsured) {
		this.amountrefundbytheInsured = amountrefundbytheInsured;
	}

	public Date getGrievanceInitiatedDate() {
		return grievanceInitiatedDate;
	}

	public void setGrievanceInitiatedDate(Date grievanceInitiatedDate) {
		this.grievanceInitiatedDate = grievanceInitiatedDate;
	}

	public SelectValue getGrievanceOutcome() {
		return grievanceOutcome;
	}

	public void setGrievanceOutcome(SelectValue grievanceOutcome) {
		this.grievanceOutcome = grievanceOutcome;
	}

	public String getGrievanceRemarks() {
		return grievanceRemarks;
	}

	public void setGrievanceRemarks(String grievanceRemarks) {
		this.grievanceRemarks = grievanceRemarks;
	}

	public Date getReconsiderationRejectDate() {
		return reconsiderationRejectDate;
	}

	public void setReconsiderationRejectDate(Date reconsiderationRejectDate) {
		this.reconsiderationRejectDate = reconsiderationRejectDate;
	}

	public Double getAcceptanceAmount() {
		return acceptanceAmount;
	}

	public void setAcceptanceAmount(Double acceptanceAmount) {
		this.acceptanceAmount = acceptanceAmount;
	}

	public Date getReconsiderationAcceptDate() {
		return reconsiderationAcceptDate;
	}

	public void setReconsiderationAcceptDate(Date reconsiderationAcceptDate) {
		this.reconsiderationAcceptDate = reconsiderationAcceptDate;
	}

	public Boolean getAnyAwardofOmbudsmanContested() {
		return anyAwardofOmbudsmanContested;
	}

	public void setAnyAwardofOmbudsmanContested(
			Boolean anyAwardofOmbudsmanContested) {
		this.anyAwardofOmbudsmanContested = anyAwardofOmbudsmanContested;
	}

}

package com.shaic.claim.legal.processconsumerforum.page.ombudsman;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.shaic.arch.fields.dto.SelectValue;

public class DecisionDetailsDto {

	@NotNull(message="Please Select Decision")
	private SelectValue decision;
	
	@NotNull(message="Please Select Hearing Status")
	private SelectValue hearingStatus;
	
	@NotNull(message="Please Select Award Status")
	private SelectValue awardStatus;
	
	@NotNull(message="Please Select Compromise Settlement")
	private SelectValue reasonForCompromiseSettlement;
	
	private Date dateOfHearing;
	
	private Date nxtDateOfHearing;
	
	private Date postponmentDateofHearing;
	
	private String awardReservedDetails;
	
	private Boolean anyAwardofOmbudsmanContested;
	
	@NotNull(message="Please Enter Self Contained Note Preparation Date")
	private Date selfConNotePreDate;
	
	private Date selfConNoteSubDate;
	
	@NotNull(message="Please Enter Compromise Amount")
	private Double compromiseAmount;
	
	private String remarksCompromiseSettlement;
	
	private Boolean selfContainedNote;
	
	@NotNull(message="Please Select Status of the Case")
	private SelectValue statusOfCase;
	
	@NotNull(message="Please Select Pending Status")
	private SelectValue pendingStatus;
	
	@NotNull(message="Please Enter Case Won")
	@Size(min = 1 , message = "Please Enter Case Won")
	private String caseWon;
	
	@NotNull(message="Please Enter Closure Date")
	private Date closurDate;
	
	private String caseWonRemarks;
	
	private String interest;
	
	@NotNull(message="Please Enter Award Reciept Date")
	private Date awardRecepitDate;
	
	@NotNull(message="Please Enter Last Date for Satification Award")
	private Date lastDateForSatisfactionAward;
	
	@NotNull(message="Please Enter Case Lost")
	@Size(min = 1 , message = "Please Enter Case Lost")
	private String caseLost;
	
	@NotNull(message="Please Enter Award Receipt Date")
	private Date lostAwardRecepitDate;
	
	@NotNull(message="Please Enter Closure Date")
	private Date lostAwardClosureDate;
	
	@NotNull(message="Please Select Reason for Awards")
	private SelectValue reasonForAwards;
	
	@NotNull(message="Please Enter Last Dt. for Satisfaction Award")
	private Date lostLastDateForSatisfactionAward;
	
	@NotNull(message="Please Enter Reason For Case Lost")
	@Size(min = 1 , message = "Please Enter Reason For Case Lost")
	private String reasonForCaseLost;
	
	private Boolean exgratiaAward;
	
	@NotNull(message="Please Enter Exgratia Award Date")
	private Date exgratiaAwardDate;
	
	private String reasonForExgratia;
	
	@NotNull(message="Please Enter Exgratia Award Amount")
	private Double exgratiaAwardAmount;

	private	Date adjournmenNxtHearingDate;
	
	private	SelectValue compromiseSettleRsnId;
	
	@NotNull(message="Please Select Add Days")
	private SelectValue wonAddDays;
	
	@NotNull(message="Please Select Add Days")
	private SelectValue lostAddDays;
	
	public SelectValue getDecision() {
		return decision;
	}

	public void setDecision(SelectValue decision) {
		this.decision = decision;
	}

	public SelectValue getHearingStatus() {
		return hearingStatus;
	}

	public void setHearingStatus(SelectValue hearingStatus) {
		this.hearingStatus = hearingStatus;
	}

	public SelectValue getAwardStatus() {
		return awardStatus;
	}

	public void setAwardStatus(SelectValue awardStatus) {
		this.awardStatus = awardStatus;
	}

	public SelectValue getReasonForCompromiseSettlement() {
		return reasonForCompromiseSettlement;
	}

	public void setReasonForCompromiseSettlement(
			SelectValue reasonForCompromiseSettlement) {
		this.reasonForCompromiseSettlement = reasonForCompromiseSettlement;
	}

	public Date getDateOfHearing() {
		return dateOfHearing;
	}

	public void setDateOfHearing(Date dateOfHearing) {
		this.dateOfHearing = dateOfHearing;
	}

	public Date getNxtDateOfHearing() {
		return nxtDateOfHearing;
	}

	public void setNxtDateOfHearing(Date nxtDateOfHearing) {
		this.nxtDateOfHearing = nxtDateOfHearing;
	}

	public Date getSelfConNotePreDate() {
		return selfConNotePreDate;
	}

	public void setSelfConNotePreDate(Date selfConNotePreDate) {
		this.selfConNotePreDate = selfConNotePreDate;
	}

	public Date getSelfConNoteSubDate() {
		return selfConNoteSubDate;
	}

	public void setSelfConNoteSubDate(Date selfConNoteSubDate) {
		this.selfConNoteSubDate = selfConNoteSubDate;
	}

	public Double getCompromiseAmount() {
		return compromiseAmount;
	}

	public void setCompromiseAmount(Double compromiseAmount) {
		this.compromiseAmount = compromiseAmount;
	}

	public String getRemarksCompromiseSettlement() {
		return remarksCompromiseSettlement;
	}

	public void setRemarksCompromiseSettlement(String remarksCompromiseSettlement) {
		this.remarksCompromiseSettlement = remarksCompromiseSettlement;
	}

	public Boolean getSelfContainedNote() {
		return selfContainedNote;
	}

	public void setSelfContainedNote(Boolean selfContainedNote) {
		this.selfContainedNote = selfContainedNote;
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

	public String getCaseWon() {
		return caseWon;
	}

	public void setCaseWon(String caseWon) {
		this.caseWon = caseWon;
	}

	public Date getClosurDate() {
		return closurDate;
	}

	public void setClosurDate(Date closurDate) {
		this.closurDate = closurDate;
	}

	public String getCaseWonRemarks() {
		return caseWonRemarks;
	}

	public void setCaseWonRemarks(String caseWonRemarks) {
		this.caseWonRemarks = caseWonRemarks;
	}

	public String getInterest() {
		return interest;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}

	public Date getAwardRecepitDate() {
		return awardRecepitDate;
	}

	public void setAwardRecepitDate(Date awardRecepitDate) {
		this.awardRecepitDate = awardRecepitDate;
	}

	public Date getLastDateForSatisfactionAward() {
		return lastDateForSatisfactionAward;
	}

	public void setLastDateForSatisfactionAward(Date lastDateForSatisfactionAward) {
		this.lastDateForSatisfactionAward = lastDateForSatisfactionAward;
	}

	public String getCaseLost() {
		return caseLost;
	}

	public void setCaseLost(String caseLost) {
		this.caseLost = caseLost;
	}

	public SelectValue getReasonForAwards() {
		return reasonForAwards;
	}

	public void setReasonForAwards(SelectValue reasonForAwards) {
		this.reasonForAwards = reasonForAwards;
	}

	public String getReasonForCaseLost() {
		return reasonForCaseLost;
	}

	public void setReasonForCaseLost(String reasonForCaseLost) {
		this.reasonForCaseLost = reasonForCaseLost;
	}

	public Boolean getExgratiaAward() {
		return exgratiaAward;
	}

	public void setExgratiaAward(Boolean exgratiaAward) {
		this.exgratiaAward = exgratiaAward;
	}

	public Date getExgratiaAwardDate() {
		return exgratiaAwardDate;
	}

	public void setExgratiaAwardDate(Date exgratiaAwardDate) {
		this.exgratiaAwardDate = exgratiaAwardDate;
	}

	public String getReasonForExgratia() {
		return reasonForExgratia;
	}

	public void setReasonForExgratia(String reasonForExgratia) {
		this.reasonForExgratia = reasonForExgratia;
	}

	public Double getExgratiaAwardAmount() {
		return exgratiaAwardAmount;
	}

	public void setExgratiaAwardAmount(Double exgratiaAwardAmount) {
		this.exgratiaAwardAmount = exgratiaAwardAmount;
	}

	public Date getLostAwardRecepitDate() {
		return lostAwardRecepitDate;
	}

	public void setLostAwardRecepitDate(Date lostAwardRecepitDate) {
		this.lostAwardRecepitDate = lostAwardRecepitDate;
	}

	public Date getLostAwardClosureDate() {
		return lostAwardClosureDate;
	}

	public void setLostAwardClosureDate(Date lostAwardClosureDate) {
		this.lostAwardClosureDate = lostAwardClosureDate;
	}

	public Date getLostLastDateForSatisfactionAward() {
		return lostLastDateForSatisfactionAward;
	}

	public void setLostLastDateForSatisfactionAward(
			Date lostLastDateForSatisfactionAward) {
		this.lostLastDateForSatisfactionAward = lostLastDateForSatisfactionAward;
	}

	public SelectValue getCompromiseSettleRsnId() {
		return compromiseSettleRsnId;
	}

	public void setCompromiseSettleRsnId(SelectValue compromiseSettleRsnId) {
		this.compromiseSettleRsnId = compromiseSettleRsnId;
	}

	public Date getAdjournmenNxtHearingDate() {
		return adjournmenNxtHearingDate;
	}

	public void setAdjournmenNxtHearingDate(Date adjournmenNxtHearingDate) {
		this.adjournmenNxtHearingDate = adjournmenNxtHearingDate;
	}

	public SelectValue getLostAddDays() {
		return lostAddDays;
	}

	public void setLostAddDays(SelectValue lostAddDays) {
		this.lostAddDays = lostAddDays;
	}

	public SelectValue getWonAddDays() {
		return wonAddDays;
	}

	public void setWonAddDays(SelectValue wonAddDays) {
		this.wonAddDays = wonAddDays;
	}

	public Date getPostponmentDateofHearing() {
		return postponmentDateofHearing;
	}

	public void setPostponmentDateofHearing(Date postponmentDateofHearing) {
		this.postponmentDateofHearing = postponmentDateofHearing;
	}

	public String getAwardReservedDetails() {
		return awardReservedDetails;
	}

	public void setAwardReservedDetails(String awardReservedDetails) {
		this.awardReservedDetails = awardReservedDetails;
	}

	public Boolean getAnyAwardofOmbudsmanContested() {
		return anyAwardofOmbudsmanContested;
	}

	public void setAnyAwardofOmbudsmanContested(Boolean anyAwardofOmbudsmanContested) {
		this.anyAwardofOmbudsmanContested = anyAwardofOmbudsmanContested;
	}
	
	
	
	
	
}

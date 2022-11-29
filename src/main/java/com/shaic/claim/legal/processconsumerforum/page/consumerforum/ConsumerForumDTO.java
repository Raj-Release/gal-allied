package com.shaic.claim.legal.processconsumerforum.page.consumerforum;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.shaic.arch.fields.dto.SelectValue;

public class ConsumerForumDTO implements Serializable {

	
	public ConsumerForumDTO() {
		intimationDetailsDTO = new IntimationDetailsDTO();
		caseDetailsDTO = new CaseDetailsDTO();
		orderDetailsDTO = new OrderDetailsDTO();
		outOfCourtSettlmentDTO = new OutOfCourtSettlementDTO();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -1921176310533140299L;

	private IntimationDetailsDTO intimationDetailsDTO;
	
	private CaseDetailsDTO caseDetailsDTO;
	
	private OrderDetailsDTO orderDetailsDTO;
	
	private OutOfCourtSettlementDTO outOfCourtSettlmentDTO;
	
	
	private Boolean wonCase;
	
	@NotNull(message="Please Enter Reason")
	@Size(min = 1 , message = "Please Enter Reason")
	private String reason;
	
	private Date recordLastUpdated;
	
	@NotNull(message="Please Enter Settlement Date")
	private Date settlementDate;
	
	//@NotNull(message="Please Enter Record Last Updated Remarks")
	//@Size(min = 1 , message = "Please Enter Record Last Updated Remarks")
	private String wonRecordLastUpdatedRemarks;
	
	private Boolean awardAgainstus;
	
	@NotNull(message="Please Select Satisfy/Appeal")
	private Boolean appeal;
	
	@NotNull(message="Please Enter Amount")
	private Double amount;
	
	//@NotNull(message="Please Enter Litigation Cost")
	private Double litigationCost;
	
	//@NotNull(message="Please Enter Compensation")
	//@Size(min = 1 , message = "Please Enter Compensation")
	private String compensation;
	
	//@NotNull(message="Please Select Award Reason")
	private String awardReason;
	
	//@NotNull(message="Please Enter Interest History")
	//@Size(min = 1 , message = "Please Enter Interest History")
	private String interestHistory;
	
	private Boolean updateOftheCase;

	@NotNull(message="Please Enter Last Updated Remark")
	@Size(min = 1 , message = "Please Enter Last Updated Remark")
	private String recordLastUpdatedRemarks;
	
	@NotNull(message="Please Enter Fresh/Previous Date Hearing")
	private Date freshPreviousDateHearing;
	
	@NotNull(message="Please Enter Next Date Hearing")
	private Date  dateofNextHearing;
	
	@NotNull(message="Please Select Case Update")
	private SelectValue caseUpdate;
	
	private Boolean stateCommission;
	
	private Boolean nationalCommission;
	
	private Boolean supremeCourt;
	
	private Boolean mandatoryDeposit;
	
	private Double mandatoryAmt;
	
	private Date mandatoryDate;
	
	private String mandatoryPayeeName;
	
	private SelectValue mandatoryDepAmtSts;
	
	private Boolean groundforAppealField;
	
	private Boolean statusofStay;
	
	private Boolean conditionalDeposit;
	
	private Double conditionalAmt;
	
	private Date conditionalDate;
	
	private String conditionalPayeeName;
	
	private SelectValue conditionalDepAmtSts;
	
	private String userName;
	
	private Date  vakalathFieldDate;
	
	@NotNull(message="Please select Appeal Preffered By")
	private Boolean appealPrefferedBy;
	
	@NotNull(message="Please enter Advocate Name")
	private String advocateName;
	
	private String faNumber;
	
	private SelectValue state;
	
	private Date dateOfHearing;
	
	private Boolean replyField;
	
	private SelectValue statusOfCase;
	
	public IntimationDetailsDTO getIntimationDetailsDTO() {
		return intimationDetailsDTO;
	}

	public void setIntimationDetailsDTO(IntimationDetailsDTO intimationDetailsDTO) {
		this.intimationDetailsDTO = intimationDetailsDTO;
	}

	public CaseDetailsDTO getCaseDetailsDTO() {
		return caseDetailsDTO;
	}

	public void setCaseDetailsDTO(CaseDetailsDTO caseDetailsDTO) {
		this.caseDetailsDTO = caseDetailsDTO;
	}

	public OrderDetailsDTO getOrderDetailsDTO() {
		return orderDetailsDTO;
	}

	public void setOrderDetailsDTO(OrderDetailsDTO orderDetailsDTO) {
		this.orderDetailsDTO = orderDetailsDTO;
	}

	public OutOfCourtSettlementDTO getOutOfCourtSettlmentDTO() {
		return outOfCourtSettlmentDTO;
	}

	public void setOutOfCourtSettlmentDTO(
			OutOfCourtSettlementDTO outOfCourtSettlmentDTO) {
		this.outOfCourtSettlmentDTO = outOfCourtSettlmentDTO;
	}

	public Boolean getAwardAgainstus() {
		return awardAgainstus;
	}

	public void setAwardAgainstus(Boolean awardAgainstus) {
		this.awardAgainstus = awardAgainstus;
	}

	public Boolean getAppeal() {
		return appeal;
	}

	public void setAppeal(Boolean appeal) {
		this.appeal = appeal;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getLitigationCost() {
		return litigationCost;
	}

	public void setLitigationCost(Double litigationCost) {
		this.litigationCost = litigationCost;
	}

	public String getCompensation() {
		return compensation;
	}

	public void setCompensation(String compensation) {
		this.compensation = compensation;
	}

	public String getAwardReason() {
		return awardReason;
	}

	public void setAwardReason(String awardReason) {
		this.awardReason = awardReason;
	}

	public String getInterestHistory() {
		return interestHistory;
	}

	public void setInterestHistory(String interestHistory) {
		this.interestHistory = interestHistory;
	}

	public Boolean getUpdateOftheCase() {
		return updateOftheCase;
	}

	public void setUpdateOftheCase(Boolean updateOftheCase) {
		this.updateOftheCase = updateOftheCase;
	}

	public String getRecordLastUpdatedRemarks() {
		return recordLastUpdatedRemarks;
	}

	public void setRecordLastUpdatedRemarks(String recordLastUpdatedRemarks) {
		this.recordLastUpdatedRemarks = recordLastUpdatedRemarks;
	}

	public Date getFreshPreviousDateHearing() {
		return freshPreviousDateHearing;
	}

	public void setFreshPreviousDateHearing(Date freshPreviousDateHearing) {
		this.freshPreviousDateHearing = freshPreviousDateHearing;
	}

	public Date getDateofNextHearing() {
		return dateofNextHearing;
	}

	public void setDateofNextHearing(Date dateofNextHearing) {
		this.dateofNextHearing = dateofNextHearing;
	}

	public SelectValue getCaseUpdate() {
		return caseUpdate;
	}

	public void setCaseUpdate(SelectValue caseUpdate) {
		this.caseUpdate = caseUpdate;
	}

	public Boolean getStateCommission() {
		return stateCommission;
	}

	public void setStateCommission(Boolean stateCommission) {
		this.stateCommission = stateCommission;
	}

	public Boolean getNationalCommission() {
		return nationalCommission;
	}

	public void setNationalCommission(Boolean nationalCommission) {
		this.nationalCommission = nationalCommission;
	}

	public Boolean getSupremeCourt() {
		return supremeCourt;
	}

	public void setSupremeCourt(Boolean supremeCourt) {
		this.supremeCourt = supremeCourt;
	}

	public Boolean getMandatoryDeposit() {
		return mandatoryDeposit;
	}

	public void setMandatoryDeposit(Boolean mandatoryDeposit) {
		this.mandatoryDeposit = mandatoryDeposit;
	}

	public Double getMandatoryAmt() {
		return mandatoryAmt;
	}

	public void setMandatoryAmt(Double mandatoryAmt) {
		this.mandatoryAmt = mandatoryAmt;
	}

	public Date getMandatoryDate() {
		return mandatoryDate;
	}

	public void setMandatoryDate(Date mandatoryDate) {
		this.mandatoryDate = mandatoryDate;
	}

	public String getMandatoryPayeeName() {
		return mandatoryPayeeName;
	}

	public void setMandatoryPayeeName(String mandatoryPayeeName) {
		this.mandatoryPayeeName = mandatoryPayeeName;
	}

	public SelectValue getMandatoryDepAmtSts() {
		return mandatoryDepAmtSts;
	}

	public void setMandatoryDepAmtSts(SelectValue mandatoryDepAmtSts) {
		this.mandatoryDepAmtSts = mandatoryDepAmtSts;
	}

	public Boolean getGroundforAppealField() {
		return groundforAppealField;
	}

	public void setGroundforAppealField(Boolean groundforAppealField) {
		this.groundforAppealField = groundforAppealField;
	}

	public Boolean getStatusofStay() {
		return statusofStay;
	}

	public void setStatusofStay(Boolean statusofStay) {
		this.statusofStay = statusofStay;
	}

	public Boolean getConditionalDeposit() {
		return conditionalDeposit;
	}

	public void setConditionalDeposit(Boolean conditionalDeposit) {
		this.conditionalDeposit = conditionalDeposit;
	}

	public Double getConditionalAmt() {
		return conditionalAmt;
	}

	public void setConditionalAmt(Double conditionalAmt) {
		this.conditionalAmt = conditionalAmt;
	}

	public Date getConditionalDate() {
		return conditionalDate;
	}

	public void setConditionalDate(Date conditionalDate) {
		this.conditionalDate = conditionalDate;
	}

	public String getConditionalPayeeName() {
		return conditionalPayeeName;
	}

	public void setConditionalPayeeName(String conditionalPayeeName) {
		this.conditionalPayeeName = conditionalPayeeName;
	}

	public SelectValue getConditionalDepAmtSts() {
		return conditionalDepAmtSts;
	}

	public void setConditionalDepAmtSts(SelectValue conditionalDepAmtSts) {
		this.conditionalDepAmtSts = conditionalDepAmtSts;
	}

	public Boolean getWonCase() {
		return wonCase;
	}

	public void setWonCase(Boolean wonCase) {
		this.wonCase = wonCase;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Date getRecordLastUpdated() {
		return recordLastUpdated;
	}

	public void setRecordLastUpdated(Date recordLastUpdated) {
		this.recordLastUpdated = recordLastUpdated;
	}

	public Date getSettlementDate() {
		return settlementDate;
	}

	public void setSettlementDate(Date settlementDate) {
		this.settlementDate = settlementDate;
	}

	public String getWonRecordLastUpdatedRemarks() {
		return wonRecordLastUpdatedRemarks;
	}

	public void setWonRecordLastUpdatedRemarks(String wonRecordLastUpdatedRemarks) {
		this.wonRecordLastUpdatedRemarks = wonRecordLastUpdatedRemarks;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getVakalathFieldDate() {
		return vakalathFieldDate;
	}

	public void setVakalathFieldDate(Date vakalathFieldDate) {
		this.vakalathFieldDate = vakalathFieldDate;
	}

	public Boolean getAppealPrefferedBy() {
		return appealPrefferedBy;
	}

	public void setAppealPrefferedBy(Boolean appealPrefferedBy) {
		this.appealPrefferedBy = appealPrefferedBy;
	}

	public String getAdvocateName() {
		return advocateName;
	}

	public void setAdvocateName(String advocateName) {
		this.advocateName = advocateName;
	}

	public String getFaNumber() {
		return faNumber;
	}

	public void setFaNumber(String faNumber) {
		this.faNumber = faNumber;
	}

	public SelectValue getState() {
		return state;
	}

	public void setState(SelectValue state) {
		this.state = state;
	}

	public Date getDateOfHearing() {
		return dateOfHearing;
	}

	public void setDateOfHearing(Date dateOfHearing) {
		this.dateOfHearing = dateOfHearing;
	}

	public Boolean getReplyField() {
		return replyField;
	}

	public void setReplyField(Boolean replyField) {
		this.replyField = replyField;
	}

	public SelectValue getStatusOfCase() {
		return statusOfCase;
	}

	public void setStatusOfCase(SelectValue statusOfCase) {
		this.statusOfCase = statusOfCase;
	}

}

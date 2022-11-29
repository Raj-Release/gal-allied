package com.shaic.claim.preauth.wizard.dto;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.claim.fvrdetails.view.ViewFVRDTO;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.premedical.dto.OtherBenefitsTableDto;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto.MedicalVerificationDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto.TreatmentQualityVerificationDTO;
import com.shaic.domain.AssignedInvestigatiorDetails;
import com.shaic.domain.TmpInvestigation;
import com.shaic.reimbursement.draftinvesigation.DraftTriggerPointsToFocusDetailsTableDto;
import com.shaic.reimbursement.assigninvesigation.AssignInvestigatorDto;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.TextField;

public class PreauthMedicalDecisionDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1843716694221976167L;
	
	public PreauthMedicalDecisionDTO() {
		investigatorName = new TmpInvestigation();
		medicalDecisionTableDTO = new ArrayList<DiagnosisProcedureTableDTO>();
		medicalVerificationTableDTO = new ArrayList<MedicalVerificationDTO>();
		treatmentVerificationDTO = new ArrayList<TreatmentQualityVerificationDTO>();
		fvrTriggerPtsList = new ArrayList<ViewFVRDTO>();
		fvrGradingDTO = new ArrayList<FvrGradingDetailsDTO>();
	}
	
	private Boolean investigationReportReviewed;
	
	private String investigationReportReviewedFlag;
	
	private Boolean otherInsurerApplicable;
	
	private String otherInsurerApplicableFlag;
	
//	@NotNull(message = "Please Enter Investigation Review Remarks.")
//	@Size(min=1,message = "Please Enter Investigation Review Remarks.")
	private String investigationReviewRemarks;
	
	private List<DiagnosisProcedureTableDTO> medicalDecisionTableDTO;
	
	private List<MedicalVerificationDTO> medicalVerificationTableDTO;
	
	private List<TreatmentQualityVerificationDTO> treatmentVerificationDTO;
	
//	@NotNull(message = "Please Select Investigator Name")
	private TmpInvestigation investigatorName;
	
	private String investigatorNameValue;
	
	private String investigatorCode;
	
	private Integer initiateFieldVisitRequest;
	
	private Boolean initiateFieldVisitRequestFlag;
	
//	@NotNull(message = "Please Choose FVR Not Required Remarks.")
	private SelectValue fvrNotRequiredRemarks;
	
	private String fvrNotRequiredRemarksValue;
	
	private Integer specialistOpinionTaken;
	
//	@NotNull(message = "Please Enter Downsize Remarks.")  only applicable for GMC not mandatory for Retail CR2019157
	private String downsizeRemarks;
		
	private String downsizeInsuredRemarks;
	
	private Boolean specialistOpinionTakenFlag;
	
	@NotNull(message = "Please Enter Specialist Type.")
	private SelectValue specialistType;
	
	private String specialistTypeValue;
	
	@NotNull(message = "Please Choose Specialist Consulted.")
	private SelectValue specialistConsulted;
	
	private String specialistConsultedValue;
	
	@NotNull(message = "Please Enter Remarks By Specialist.")
	private String remarksBySpecialist;
	
	//@Size(min=15,message = "Please Enter atleast 15 characters")
	private String icdExclusionReason;
	
	public String getIcdExclusionReason() {
		return icdExclusionReason;
	}

	public void setIcdExclusionReason(String icdExclusionReason) {
		this.icdExclusionReason = icdExclusionReason;
	}

	@NotNull(message = "Please Choose Allocation to.")
	private SelectValue allocationTo;
	
	@NotNull(message = "Please Choose Assign to")
	private SelectValue assignTo;
	
	
	@NotNull(message= "Please Choose Priority")
	private SelectValue priority;
	
	private String allocationToValue;
	
//	@NotNull(message = "Please Enter FVR Trigger Points.")
//	@Size(min=1,message = "Please Enter FVR Trigger Points.")
	private String fvrTriggerPoints;
	
	private List<ViewFVRDTO> fvrTriggerPtsList;
	
//	@NotNull(message = "Please Enter Medical Remarks.")
//	@Size(min=1,message = "Please Enter Medical Remarks")
	private String medicalRemarks;
	
	private String doctorNote;
	
	@NotNull(message = "Please Enter Escalate Remarks.")
	@Size(min=1,message = "Please Enter Escalate Remarks")
	private String escalateRemarks;
	
	@NotNull(message = "Please Enter Initial Approved Amount.")
	private Double initialApprovedAmt;
	
	@NotNull(message = "Please Enter Initial Approved Amount.")
	private Double approvedAmount;
	
	@NotNull(message = "Please Enter Selected Copay.")
	private Double selectedCopay;
	
	private Double initialTotalApprovedAmt;
	
	private Double hospitalDiscountReverseAllocatedAmt = 0d;
	
	private Double reverseAllocatedMininumAmt;
	
	private Double uniquePremiumAmt = 0d;
	
	private Double amountToHospAftPremium = 0d;
	
	@NotNull(message = "Please Select Specialist Type.")
	private SelectValue specialistValue;
	
	private String amountInwords;
	
	private String otherInsurerHospAmountClaimed;
	
	private String otherInsurerPreHospAmountClaimed;
	
	private String otherInsurerPostHospAmountClaimed;
	
	private List<OtherBenefitsTableDto> otherBenefitTableDtoList;
	
	private Double financialApprovedAmt;
	
	private Double billingApprovedAmt;
	
	private Boolean isFvrIntiatedMA;
	
	//@NotNull(message = "Please Enter Approval Remarks.")
//	@Size(min=1,message = "Please Enter Approval Remarks")
	private String approvalRemarks;
	
	private String referToFLPremarks;
	
	@NotNull(message = "Please Select Query Type.")
	private SelectValue queryType;
	
	@NotNull(message = "Please Enter Query Remarks.")
	@Size(min=1 , message ="Please Enter Query Remarks.")
	private String queryRemarks;
	
	@NotNull(message = "Please Select Rejection Category.")
	private SelectValue rejectionCategory;
	
	@NotNull(message = "Please Enter Rejection Remarks.")
	@Size(min=1,message = "Please Enter Rejection Remarks.")
	private String rejectionRemarks;
	
	private String rejectionRemarks2;
	
	//@NotNull(message = "Please Select Decision Change Reason to proceed further.")
	private SelectValue decisionChangeReason;
	
	private String decisionChangeRemarks;
	
	private String remarksForInsured;
	
	private String withDrawRemarksForInsured;
	
	@NotNull(message = "Please Enter Remarks.")
	@Size(min=1,message = "Please Enter Remarks.")
	private String medicalApproverRemarks;
	
	@NotNull(message = "Please Enter Billing Remarks.")
	@Size(min=1,message = "Please Enter Billing Remarks.")
	private String billingRemarks;
	
	@NotNull(message = "Please Enter Reason for reffering to claim approval.")
	@Size(min=1,message = "Please Enter Reason for reffering to claim approval.")
	private String reasonForRefferingClaimApproval;
	
	
	@NotNull(message = "Please Enter Financial Remarks.")
	@Size(min=1,message = "Please Enter Financial Remarks.")
	private String financialRemarks;
	
	@AssertTrue(message = "Please Select Verified the amount claimed and documents based on the bills received Option.")
	private boolean originalBillsReceived;
	
	@NotNull(message = "Please Enter Claim Approver Remarks.")
	@Size(min=1,message = "Please Enter Claim Approver Remarks.")
	private String claimApproverRemarks;
	
	@NotNull(message = "Please Enter Financial Approver Remarks.")
	@Size(min=1,message = "Please Enter Financial Approver Remarks.")
	private String financialApproverRemarks;

	@NotNull(message = "Please Enter Reason for Referring to Billing.")
	@Size(min=1,message = "Please Enter Reason for Referring to Billing.")
	private String reasonForReferringToBilling;
	
	private String zonalRemarks;
	
	private String corporateRemarks;
	
	private Boolean paymentMode;
	
	private Long paymentModeFlag;
	
	private SelectValue payeeName;
	
//	@NotNull(message = "Please Enter Reason for Changing Payee Name")
//	@Size(min = 1 , message = "Please Enter Reason for Changing Payee Name")
	private String reasonForChange;
	
	private String panNo;
	
	private String legalFirstName;
	
	private String legalMiddleName;
	
	private String legalLastName;
	
	private String payableAt;
	
	private Long docAcknowledgementKey;
	
	private String accountNo;
	
	private String ifscCode;
	
	private String branch;
	
	private String bankName;
	
	private String city;
	
	private String emailId;
	
	private Long bankId;
	
	private Boolean isBeneifitSheetAvailable = false;
	
	private Boolean stopLossFlag;
	
	private Integer stopLossAvailableAmt = 0;
	
	private Integer definedLimitAmnt = 0;
	
	@NotNull(message = "Please Enter Reason for Referring to Bill Entry.")
	@Size(min=1,message = "Please Enter Reason for Referring to Bill Entry.")
	private String referToBillEntryBillingRemarks;
	
	private Boolean isWithoutSuppDoc;
	
	private Integer withoutSuppDoc;

	//@NotNull(message = "Please Select Suggestion Category.")
	private SelectValue cpuSuggestionCategory;
	
	private String cpuRemarks;
	
	private Double cpuAmountSuggested = 0d;
	
	private Boolean isFvrIntiated;
	
	private List<AssignedInvestigatiorDetails> invsReviewRemarksTableList;
	
	private Double policyInstPremiumAmt = 0d;
	
	public List<FvrGradingDetailsDTO> getFvrGradingDTO() {
		return fvrGradingDTO;
	}

	public void setFvrGradingDTO(List<FvrGradingDetailsDTO> fvrGradingDTO) {
		this.fvrGradingDTO = fvrGradingDTO;
	}

	@NotNull(message = "Please Select Reason for Denial.")
	private SelectValue reasonForDenial;
	
	@NotNull(message = "Please Enter Denial Remarks.")
	@Size(min=1)
	private String denialRemarks;
	
	@NotNull(message = "Please Select Escalate To.")
	private SelectValue escalateTo;
	
	private String uploadFileName;
	
	private File uploadFile;
	
	private Double downsizedAmt;
	
	@NotNull(message = "Please Enter Escalation Remarks.")
	private String escalationRemarks;
	
	@NotNull(message = "Please Select Type of Coordinator Request.")
	private SelectValue typeOfCoordinatorRequest;
	
	@NotNull(message = "Please Enter Reason for Refering.")
	@Size(min=1,message = "Please Enter Reason for Refering.")
	private String reasonForRefering;
	
	@NotNull(message = "Please Enter Trigger Points to Focus.")
	@Size(min=1,message = "Please Enter Trigger Points to Focus.")
	private String triggerPointsToFocus;
	
	private List<DraftTriggerPointsToFocusDetailsTableDto> triggerPointsList;
	
	@NotNull(message = "Please Enter Escalate Reply.")
	@Size(min=1,message = "Please Enter Escalate Reply.")
	private String escalateReply;
	
	@NotNull(message = "Please Choose Category")
	private SelectValue category;
	
	private Double innerLimitAmount = 0d;
	
	private Integer corporateBufferUtilizedAmt = 0;
	
	private Double enhBenefitApprovedAmount;
	
	
	private Double otherBenefitPreauthApprAmt = 0d;
	
	private String definedLimit;
	
	private Boolean isAllowInitiateFVR;
	
	private String fvrNotRequiredOthersRemarks;
	
	private Boolean isInvsInitiated = Boolean.FALSE;
	
	private Boolean isFvrReplyReceived = Boolean.FALSE;
	
	private List<ViewFVRDTO> fvrAdditionalTriggerPtsList;
	
	private Object userRoleMulti;
	private Object doctorName;
	private String remarksFromDeptHead;
	private Boolean isMandatory = Boolean.FALSE;
	private String docRoleCode;
	private BeanItemContainer<SpecialSelectValue> doctorContainer;
	private String isDecisionMismatched;
	private String stpClaimedAmt;
	private String stpDeductibleAmt;
	private String stpApprovedAmt;
	private String stpStatus;
	private Boolean negotiationDecisionTaken;	
	private String negotiatePoints;
	private String updateNegotiatePoints;
	private Boolean isValidNegotiation = Boolean.FALSE;
	private Boolean isNegotiationCancelled = Boolean.FALSE;
	private String amtToNegotiated;
	
//	CR20181286
	private String claimedAmt;
	private Double negotiationAmount;
	private Double savedAmt;
	private Boolean negotiationMade = false;
	private Boolean isNegSelect = false;
	
	@NotNull(message = "Please Enter the amount claimed from Hospital.")
	@Size(min=1,message = "Please Enter the amount claimed from Hospital.")
	private String amountClaimedFrmHosp;
	
	private Boolean isNegotiationDone = Boolean.TRUE;

	private SelectValue rejSubCategory;
	
	/*@NotNull(message = "Please Enter Condition Number.")
	@Size(min=1,message = "Please Enter Condition Number.")*/
	private String policyConditionNoReject;
	
	private String policyRejectConditClause;
	
	private String negotiationWith;
	
	private Double totalNegotiationSaved;
	
	private Double higestCLTrans;
	
	private Boolean verifiedBonus;
	
	private Double policyInstalmentPremiumAmt = 0d;
	
	private Date policyInstalmentDueDate;
	
	@NotNull(message = "Please Choose anyone from Installment Query related.")
	private Boolean relInstalmentOptFlag;
	
	public String getOtherInsurerHospAmountClaimed() {
		return otherInsurerHospAmountClaimed;
	}

	public String getOtherInsurerPreHospAmountClaimed() {
		return otherInsurerPreHospAmountClaimed;
	}

	public String getOtherInsurerPostHospAmountClaimed() {
		return otherInsurerPostHospAmountClaimed;
	}

	public void setOtherInsurerHospAmountClaimed(
			String otherInsurerHospAmountClaimed) {
		this.otherInsurerHospAmountClaimed = otherInsurerHospAmountClaimed;
	}

	public void setOtherInsurerPreHospAmountClaimed(
			String otherInsurerPreHospAmountClaimed) {
		this.otherInsurerPreHospAmountClaimed = otherInsurerPreHospAmountClaimed;
	}

	public void setOtherInsurerPostHospAmountClaimed(
			String otherInsurerPostHospAmountClaimed) {
		this.otherInsurerPostHospAmountClaimed = otherInsurerPostHospAmountClaimed;
	}

	private Boolean sentToCPUFlag;
	
	private Integer sentToCPU;
	
	@NotNull(message = "Please Select Reason For Withdrawal.")
	private SelectValue withdrawReason;
	
	@NotNull(message = "Please Enter Withdrawal Remarks.")
	@Size(min=1,message = "Please Enter Withdrawal Remarks")
	private String withdrawRemarks;
	
	@NotNull(message = "Please Select Reason For Downsize.")
	private SelectValue downSizeReason;
	
	@NotNull(message = "Please Select Reason for Cancellation")
	private SelectValue cancellationReason;
	
	@NotNull(message = "Please Enter Cancellation Remarks")
	@Size(min = 1, message = "Please Enter Cancellation Remarks")
	private String cancelRemarks;
	
	@AssertTrue(message = "Please Select Hospital Discount has been checked.")
	private boolean hospitalDiscount;
	
	private Boolean RMA1 = false;
	private Boolean	RMA2 = false;
	private Boolean	RMA3 = false;
	private Boolean	RMA4 = false;
	private Boolean	RMA5 = false;
	private Boolean	RMA6 = false;
	
	public Double getFinancialApprovedAmt() {
		return financialApprovedAmt;
	}

	public void setFinancialApprovedAmt(Double financialApprovedAmt) {
		this.financialApprovedAmt = financialApprovedAmt;
	}

	public Double getBillingApprovedAmt() {
		return billingApprovedAmt;
	}

	public void setBillingApprovedAmt(Double billingApprovedAmt) {
		this.billingApprovedAmt = billingApprovedAmt;
	}

	@NotNull(message = "Please Enter Remarks For CPU.")
	@Size(min=1,message= "Please Enter Remarks For CPU.")
	private String remarksForCPU;
	
	private String fvrSequence;
	
	private String representativeCode;
	
	@NotNull(message = "Please Enter Medical Approver's Reply.")
	@Size(min=1,message = "Please Enter Medical Approver's Reply")
	private String approverReply;
	
	@NotNull(message = "Please Enter Remarks.")
	@Size(min=1,message = "Please Enter Remarks.")
	private String refer64VBRemarks;
	
	private List<FvrGradingDetailsDTO> fvrGradingDTO;
	
	private String holdRemarks;
	
	private String negotiationDeductionAmt;
	
	private String amtAfterNegotiationAmt;
	
	private String preauthDownsizeAmt;
	
	//added for CR R1180
	private String withdrawInternalRemarks;
	
	private String accountPreference;
	
	private String accountType;
	
	private String payeeRelationship;
	

	private SelectValue behaviourHospValue;

	private Boolean behaviourHosp1chkbox = false;
	
	private Boolean behaviourHospMbagreedchkbox = false;
	
	private String hospCollectOvrAmtChkBx;
	
	private String mbAgrdDisNtAplyChkBx;
	
	public SelectValue getFvrNotRequiredRemarks() {
		return fvrNotRequiredRemarks;
	}

	public void setFvrNotRequiredRemarks(SelectValue fvrNotRequiredRemarks) {
		this.fvrNotRequiredRemarks = fvrNotRequiredRemarks;
	}

	public Integer getSpecialistOpinionTaken() {
		return specialistOpinionTaken;
	}

	public String getFinancialApproverRemarks() {
		return financialApproverRemarks;
	}

	public void setFinancialApproverRemarks(String financialApproverRemarks) {
		this.financialApproverRemarks = financialApproverRemarks;
	}

	public String getReasonForReferringToBilling() {
		return reasonForReferringToBilling;
	}

	public void setReasonForReferringToBilling(String reasonForReferringToBilling) {
		this.reasonForReferringToBilling = reasonForReferringToBilling;
	}

	public Boolean getInvestigationReportReviewed() {
		return investigationReportReviewed;
	}

	public String getFinancialRemarks() {
		return financialRemarks;
	}

	public void setFinancialRemarks(String financialRemarks) {
		this.financialRemarks = financialRemarks;
	}
	
	public boolean getOriginalBillsReceived() {
		return originalBillsReceived;
	}

	public void setOriginalBillsReceived(boolean originalBillsReceived) {
		this.originalBillsReceived = originalBillsReceived;
	}

	public String getMedicalApproverRemarks() {
		return medicalApproverRemarks;
	}

	public String getBillingRemarks() {
		return billingRemarks;
	}

	public void setBillingRemarks(String billingRemarks) {
		this.billingRemarks = billingRemarks;
	}

	public void setMedicalApproverRemarks(String medicalApproverRemarks) {
		this.medicalApproverRemarks = medicalApproverRemarks;
	}

	public void setInvestigationReportReviewed(Boolean investigationReportReviewed) {
		
		
		this.investigationReportReviewed = investigationReportReviewed;
		this.investigationReportReviewedFlag = (this.investigationReportReviewed != null && investigationReportReviewed)  ? "Y" : "N";
//		/this.investigationReportReviewed = investigationReportReviewed;
	}

	public String getZonalRemarks() {
		return zonalRemarks;
	}

	public void setZonalRemarks(String zonalRemarks) {
		this.zonalRemarks = zonalRemarks;
	}

	public String getCorporateRemarks() {
		return corporateRemarks;
	}

	public void setCorporateRemarks(String corporateRemarks) {
		this.corporateRemarks = corporateRemarks;
	}

	public String getInvestigationReviewRemarks() {
		return investigationReviewRemarks;
	}

	public void setInvestigationReviewRemarks(String investigationReviewRemarks) {
		this.investigationReviewRemarks = investigationReviewRemarks;
	}

	public TmpInvestigation getInvestigatorName() {
		return investigatorName;
	}

	public void setInvestigatorName(TmpInvestigation investigatorName) {
		this.investigatorName = investigatorName;
	}

	public void setSpecialistOpinionTaken(Integer specialistOpinionTaken) {
		this.specialistOpinionTaken = specialistOpinionTaken;
	}

	public SelectValue getSpecialistType() {
		return specialistType;
	}

	public SelectValue getCategory() {
		return category;
	}

	public void setCategory(SelectValue category) {
		this.category = category;
	}

	public void setReasonForRefering(String reasonForRefering) {
		this.reasonForRefering = reasonForRefering;
	}

	public void setSpecialistType(SelectValue specialistType) {
		this.specialistType = specialistType;
	}

	public SelectValue getSpecialistConsulted() {
		return specialistConsulted;
	}

	public void setSpecialistConsulted(SelectValue specialistConsulted) {
		this.specialistConsulted = specialistConsulted;
	}

	public String getRemarksBySpecialist() {
		return remarksBySpecialist;
	}

	public void setRemarksBySpecialist(String remarksBySpecialist) {
		this.remarksBySpecialist = remarksBySpecialist;
	}

	public SelectValue getAllocationTo() {
		return allocationTo;
	}

	public void setAllocationTo(SelectValue allocationTo) {
		this.allocationTo = allocationTo;
	}

	public String getFvrTriggerPoints() {
		return fvrTriggerPoints;
	}

	public void setFvrTriggerPoints(String fvrTriggerPoints) {
		this.fvrTriggerPoints = fvrTriggerPoints;
	}

	public Integer getInitiateFieldVisitRequest() {
		return initiateFieldVisitRequest;
	}

	public SelectValue getDownSizeReason() {
		return downSizeReason;
	}

	public void setDownSizeReason(SelectValue downSizeReason) {
		this.downSizeReason = downSizeReason;
	}

	public void setInitiateFieldVisitRequest(Integer initiateFieldVisitRequest) {
		this.initiateFieldVisitRequest = initiateFieldVisitRequest;
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

	public String getRemarksForCPU() {
		return remarksForCPU;
	}

	public void setRemarksForCPU(String remarksForCPU) {
		this.remarksForCPU = remarksForCPU;
	}

	public void setDoctorNote(String doctorNote) {
		this.doctorNote = doctorNote;
	}
	
	public Double getInitialApprovedAmt() {
		return initialApprovedAmt;
	}
	
	public void setInitialApprovedAmt(Double initialApprovedAmt) {
		this.initialApprovedAmt = initialApprovedAmt;
	}
	
	public Double getSelectedCopay() {
		return selectedCopay;
	}
	
	public void setSelectedCopay(Double selectedCopay) {
		this.selectedCopay = selectedCopay;
	}
	
	public Double getInitialTotalApprovedAmt() {
		return initialTotalApprovedAmt;
	}
	
	public void setInitialTotalApprovedAmt(Double initialTotalApprovedAmt) {
		this.initialTotalApprovedAmt = initialTotalApprovedAmt;
	}
	
	public String getApprovalRemarks() {
		return approvalRemarks;
	}
	
	public void setApprovalRemarks(String approvalRemarks) {
		this.approvalRemarks = approvalRemarks;
	}
	
	public String getQueryRemarks() {
		return queryRemarks;
	}
	
	public void setQueryRemarks(String queryRemarks) {
		this.queryRemarks = queryRemarks;
	}
	
	public SelectValue getRejectionCategory() {
		return rejectionCategory;
	}
	
	public void setRejectionCategory(SelectValue rejectionCategory) {
		this.rejectionCategory = rejectionCategory;
	}
	
	public String getRejectionRemarks() {
		return rejectionRemarks;
	}
	
	public void setRejectionRemarks(String rejectionRemarks) {
		this.rejectionRemarks = rejectionRemarks;
	}
	
	public SelectValue getReasonForDenial() {
		return reasonForDenial;
	}
	
	public void setReasonForDenial(SelectValue reasonForDenial) {
		this.reasonForDenial = reasonForDenial;
	}
	
	public String getDenialRemarks() {
		return denialRemarks;
	}
	
	public void setDenialRemarks(String denialRemarks) {
		this.denialRemarks = denialRemarks;
	}
	
	public SelectValue getEscalateTo() {
		return escalateTo;
	}
	
	public void setEscalateTo(SelectValue escalateTo) {
		this.escalateTo = escalateTo;
	}
	
	public String getUploadFileName() {
		return uploadFileName;
	}
	
	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}
	
	public File getUploadFile() {
		return uploadFile;
	}
	
	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}
	
	public String getEscalationRemarks() {
		return escalationRemarks;
	}
	
	public void setEscalationRemarks(String escalationRemarks) {
		this.escalationRemarks = escalationRemarks;
	}
	
	public SelectValue getTypeOfCoordinatorRequest() {
		return typeOfCoordinatorRequest;
	}
	
	public void setTypeOfCoordinatorRequest(SelectValue typeOfCoordinatorRequest) {
		this.typeOfCoordinatorRequest = typeOfCoordinatorRequest;
	}
	
	public String getReasonForRefering() {
		return reasonForRefering;
	}
	
	public void setReasonForReferring(String reasonForRefering) {
		this.reasonForRefering = reasonForRefering;
	}

	public Double getApprovedAmount() {
		return approvedAmount;
	}

	public void setApprovedAmount(Double approvedAmount) {
		this.approvedAmount = approvedAmount;
	}

	public Boolean getInitiateFieldVisitRequestFlag() {
		
		return initiateFieldVisitRequestFlag;
	}

	public void setInitiateFieldVisitRequestFlag(
			Boolean initiateFieldVisitRequestFlag) {
		
		this.initiateFieldVisitRequest = initiateFieldVisitRequestFlag  ? 1 : 0;
		this.initiateFieldVisitRequestFlag = initiateFieldVisitRequestFlag;
	}

	public Boolean getSpecialistOpinionTakenFlag() {
	
		return specialistOpinionTakenFlag;
	}

	public void setSpecialistOpinionTakenFlag(Boolean specialistOpinionTakenFlag) {
		this.specialistOpinionTaken = specialistOpinionTakenFlag  ? 1 : 0;
		this.specialistOpinionTakenFlag = specialistOpinionTakenFlag;
	}

	public Boolean getSentToCPUFlag() {
		return sentToCPUFlag;
	}

	public void setSentToCPUFlag(Boolean sentToCPUFlag) {
		this.sentToCPU = sentToCPUFlag  ? 1 : 0;
		this.sentToCPUFlag = sentToCPUFlag;
	}

	public Integer getSentToCPU() {
		return sentToCPU;
	}

	public void setSentToCPU(Integer sentToCPU) {
		this.sentToCPU = sentToCPU;
	}

	public SelectValue getWithdrawReason() {
		return withdrawReason;
	}

	public void setWithdrawReason(SelectValue withdrawReason) {
		this.withdrawReason = withdrawReason;
	}

	public String getInvestigationReportReviewedFlag() {
		return investigationReportReviewedFlag;
	}

	public void setInvestigationReportReviewedFlag(
			String investigationReportReviewedFlag) {
		if(investigationReportReviewedFlag != null) {
			this.investigationReportReviewed = investigationReportReviewedFlag.toLowerCase().equalsIgnoreCase("y") ? true : false;
		}
	}

	public String getFvrSequence() {
		return fvrSequence;
	}

	public void setFvrSequence(String fvrSequence) {
		this.fvrSequence = fvrSequence;
	}

	public String getRepresentativeCode() {
		return representativeCode;
	}

	public void setRepresentativeCode(String representativeCode) {
		this.representativeCode = representativeCode;
	}

	public Double getDownsizedAmt() {
		return downsizedAmt;
	}

	public void setDownsizedAmt(Double downsizedAmt) {
		this.downsizedAmt = downsizedAmt;
	}

	public List<DiagnosisProcedureTableDTO> getMedicalDecisionTableDTO() {
		return medicalDecisionTableDTO;
	}

	public void setMedicalDecisionTableDTO(
			List<DiagnosisProcedureTableDTO> medicalDecisionTableDTO) {
		this.medicalDecisionTableDTO = medicalDecisionTableDTO;
	}

	public String getDownsizeRemarks() {
		return downsizeRemarks;
	}

	public void setDownsizeRemarks(String downsizeRemarks) {
		this.downsizeRemarks = downsizeRemarks;
	}

	public String getEscalateRemarks() {
		return escalateRemarks;
	}

	public void setEscalateRemarks(String escalateRemarks) {
		this.escalateRemarks = escalateRemarks;
	}

	public String getTriggerPointsToFocus() {
		return triggerPointsToFocus;
	}

	public void setTriggerPointsToFocus(String triggerPointsToFocus) {
		this.triggerPointsToFocus = triggerPointsToFocus;
	}

	public String getApproverReply() {
		return approverReply;
	}

	public void setApproverReply(String approverReply) {
		this.approverReply = approverReply;
	}

	public String getEscalateReply() {
		return escalateReply;
	}

	public void setEscalateReply(String escalateReply) {
		this.escalateReply = escalateReply;
	}

	public List<MedicalVerificationDTO> getMedicalVerificationTableDTO() {
		return medicalVerificationTableDTO;
	}

	public void setMedicalVerificationTableDTO(
			List<MedicalVerificationDTO> medicalVerificationTableDTO) {
		this.medicalVerificationTableDTO = medicalVerificationTableDTO;
	}

	public List<TreatmentQualityVerificationDTO> getTreatmentVerificationDTO() {
		return treatmentVerificationDTO;
	}

	public void setTreatmentVerificationDTO(
			List<TreatmentQualityVerificationDTO> treatmentVerificationDTO) {
		this.treatmentVerificationDTO = treatmentVerificationDTO;
	}
	
	public String getInvestigatorNameValue() {
		return investigatorNameValue;
	}

	public void setInvestigatorNameValue(String investigatorNameValue) {
		this.investigatorNameValue = investigatorNameValue;
	}

	public String getFvrNotRequiredRemarksValue() {
		return fvrNotRequiredRemarksValue;
	}

	public void setFvrNotRequiredRemarksValue(String fvrNotRequiredRemarksValue) {
		this.fvrNotRequiredRemarksValue = fvrNotRequiredRemarksValue;
	}

	public String getAllocationToValue() {
		return allocationToValue;
	}

	public void setAllocationToValue(String allocationToValue) {
		this.allocationToValue = allocationToValue;
	}

	public String getSpecialistTypeValue() {
		return specialistTypeValue;
	}

	public void setSpecialistTypeValue(String specialistTypeValue) {
		this.specialistTypeValue = specialistTypeValue;
	}

	public String getSpecialistConsultedValue() {
		return specialistConsultedValue;
	}

	public void setSpecialistConsultedValue(String specialistConsultedValue) {
		this.specialistConsultedValue = specialistConsultedValue;
	}

	public String getAmountInwords() {
		return amountInwords;
	}

	public void setAmountInwords(String amountInwords) {
		this.amountInwords = amountInwords;
	}

	public SelectValue getSpecialistValue() {
		return specialistValue;
	}

	public void setSpecialistValue(SelectValue specialistValue) {
		this.specialistValue = specialistValue;
	}

	public String getInvestigatorCode() {
		return investigatorCode;
	}

	public void setInvestigatorCode(String investigatorCode) {
		this.investigatorCode = investigatorCode;
	}

	public SelectValue getCancellationReason() {
		return cancellationReason;
	}

	public void setCancellationReason(SelectValue cancellationReason) {
		this.cancellationReason = cancellationReason;
	}

	public String getCancelRemarks() {
		return cancelRemarks;
	}

	public void setCancelRemarks(String cancelRemarks) {
		this.cancelRemarks = cancelRemarks;
	}

	public SelectValue getAssignTo() {
		return assignTo;
	}

	public void setAssignTo(SelectValue assignTo) {
		this.assignTo = assignTo;
	}

	public SelectValue getPriority() {
		return priority;
	}

	public void setPriority(SelectValue priority) {
		this.priority = priority;
	}

	public Boolean getRMA1() {
		return RMA1;
	}

	public void setRMA1(Boolean rMA1) {
		RMA1 = rMA1;
	}

	public Boolean getRMA2() {
		return RMA2;
	}

	public void setRMA2(Boolean rMA2) {
		RMA2 = rMA2;
	}

	public Boolean getRMA3() {
		return RMA3;
	}

	public void setRMA3(Boolean rMA3) {
		RMA3 = rMA3;
	}

	public Boolean getRMA4() {
		return RMA4;
	}

	public void setRMA4(Boolean rMA4) {
		RMA4 = rMA4;
	}

	public Boolean getRMA5() {
		return RMA5;
	}

	public void setRMA5(Boolean rMA5) {
		RMA5 = rMA5;
	}

	public Boolean getRMA6() {
		return RMA6;
	}

	public void setRMA6(Boolean rMA6) {
		RMA6 = rMA6;
	}

	public String getWithdrawRemarks() {
		return withdrawRemarks;
	}

	public Boolean getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(Boolean paymentMode) {
		this.paymentMode = paymentMode;
	}

	public Long getPaymentModeFlag() {
		return paymentModeFlag;
	}

	public void setPaymentModeFlag(Long paymentModeFlag) {
		this.paymentModeFlag = paymentModeFlag;
	}

	public SelectValue getPayeeName() {
		return payeeName;
	}

	public void setPayeeName(SelectValue payeeName) {
		this.payeeName = payeeName;
	}

	public String getReasonForChange() {
		return reasonForChange;
	}

	public void setReasonForChange(String reasonForChange) {
		this.reasonForChange = reasonForChange;
	}

	public String getPanNo() {
		return panNo;
	}

	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}

	public String getLegalFirstName() {
		return legalFirstName;
	}

	public void setLegalFirstName(String legalFirstName) {
		this.legalFirstName = legalFirstName;
	}

	public String getLegalMiddleName() {
		return legalMiddleName;
	}

	public void setLegalMiddleName(String legalMiddleName) {
		this.legalMiddleName = legalMiddleName;
	}

	public String getLegalLastName() {
		return legalLastName;
	}

	public void setLegalLastName(String legalLastName) {
		this.legalLastName = legalLastName;
	}

	public String getPayableAt() {
		return payableAt;
	}

	public void setPayableAt(String payableAt) {
		this.payableAt = payableAt;
	}

	public Long getDocAcknowledgementKey() {
		return docAcknowledgementKey;
	}

	public void setDocAcknowledgementKey(Long docAcknowledgementKey) {
		this.docAcknowledgementKey = docAcknowledgementKey;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Boolean getOtherInsurerApplicable() {
		return otherInsurerApplicable;
	}

	public void setOtherInsurerApplicable(Boolean otherInsurerApplicable) {
		this.otherInsurerApplicable = otherInsurerApplicable;
		this.otherInsurerApplicableFlag = (this.otherInsurerApplicable != null && otherInsurerApplicable)  ? "Y" : "N";
	}

	public String getOtherInsurerApplicableFlag() {
		return otherInsurerApplicableFlag;
	}

	public void setOtherInsurerApplicableFlag(String otherInsurerApplicableFlag) {
		this.otherInsurerApplicableFlag = otherInsurerApplicableFlag;
		if(otherInsurerApplicableFlag != null) {
			this.otherInsurerApplicable = otherInsurerApplicableFlag.toLowerCase().equalsIgnoreCase("y") ? true : false;
		}
	}

	public Long getBankId() {
		return bankId;
	}

	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}

	public void setWithdrawRemarks(String withdrawRemarks) {
		this.withdrawRemarks = withdrawRemarks;
	}

	

	public String getReferToBillEntryBillingRemarks() {
		return referToBillEntryBillingRemarks;
	}

	public void setReferToBillEntryBillingRemarks(
			String referToBillEntryBillingRemarks) {
		this.referToBillEntryBillingRemarks = referToBillEntryBillingRemarks;
	}

	public String getReferToFLPremarks() {
		return referToFLPremarks;
	}

	public void setReferToFLPremarks(String referToFLPremarks) {
		this.referToFLPremarks = referToFLPremarks;
	}

	public Double getUniquePremiumAmt() {
		return uniquePremiumAmt;
	}

	public void setUniquePremiumAmt(Double uniquePremiumAmt) {
		this.uniquePremiumAmt = uniquePremiumAmt;
	}

	public Double getAmountToHospAftPremium() {
		return amountToHospAftPremium;
	}

	public void setAmountToHospAftPremium(Double amountToHospAftPremium) {
		this.amountToHospAftPremium = amountToHospAftPremium;
	}

	public String getReasonForRefferingClaimApproval() {
		return reasonForRefferingClaimApproval;
	}

	public void setReasonForRefferingClaimApproval(
			String reasonForRefferingClaimApproval) {
		this.reasonForRefferingClaimApproval = reasonForRefferingClaimApproval;
	}

	public String getClaimApproverRemarks() {
		return claimApproverRemarks;
	}

	public void setClaimApproverRemarks(String claimApproverRemarks) {
		this.claimApproverRemarks = claimApproverRemarks;
	}
	public Boolean getIsBeneifitSheetAvailable() {
		return isBeneifitSheetAvailable;
	}

	public void setIsBeneifitSheetAvailable(Boolean isBeneifitSheetAvailable) {
		this.isBeneifitSheetAvailable = isBeneifitSheetAvailable;
	}

	public String getRefer64VBRemarks() {
		return refer64VBRemarks;
	}

	public void setRefer64VBRemarks(String refer64vbRemarks) {
		refer64VBRemarks = refer64vbRemarks;
	}
	
	public SelectValue getCpuSuggestionCategory() {
		return cpuSuggestionCategory;
	}

	public void setCpuSuggestionCategory(SelectValue cpuSuggestionCategory) {
		this.cpuSuggestionCategory = cpuSuggestionCategory;
	}

	public String getCpuRemarks() {
		return cpuRemarks;
	}

	public void setCpuRemarks(String cpuRemarks) {
		this.cpuRemarks = cpuRemarks;
	}

	public Double getCpuAmountSuggested() {
		return cpuAmountSuggested;
	}

	public void setCpuAmountSuggested(Double cpuAmountSuggested) {
		this.cpuAmountSuggested = cpuAmountSuggested;
	}

	public Double getInnerLimitAmount() {
		return innerLimitAmount;
	}

	public void setInnerLimitAmount(Double innerLimitAmount) {
		this.innerLimitAmount = innerLimitAmount;
	}

	public Boolean getStopLossFlag() {
		return stopLossFlag;
	}

	public List<ViewFVRDTO> getFvrTriggerPtsList() {
		return fvrTriggerPtsList;
	}

	public void setFvrTriggerPtsList(List<ViewFVRDTO> fvrTriggerPtsList) {
		this.fvrTriggerPtsList = fvrTriggerPtsList;
	}

	public void setStopLossFlag(Boolean stopLossFlag) {
		this.stopLossFlag = stopLossFlag;
	}

	public Integer getStopLossAvailableAmt() {
		return stopLossAvailableAmt;
	}

	public void setStopLossAvailableAmt(Integer stopLossAvailableAmt) {
		this.stopLossAvailableAmt = stopLossAvailableAmt;
	}

	public Integer getCorporateBufferUtilizedAmt() {
		return corporateBufferUtilizedAmt;
	}

	public void setCorporateBufferUtilizedAmt(Integer corporateBufferUtilizedAmt) {
		this.corporateBufferUtilizedAmt = corporateBufferUtilizedAmt;
	}
	public List<OtherBenefitsTableDto> getOtherBenefitTableDtoList() {
		return otherBenefitTableDtoList;
	}

	public Boolean getIsWithoutSuppDoc() {
		return isWithoutSuppDoc;
	}

	public void setIsWithoutSuppDoc(Boolean isWithoutSuppDoc) {
		this.withoutSuppDoc =isWithoutSuppDoc  != null ?  isWithoutSuppDoc  ? 1 : 0 : 0;
		this.isWithoutSuppDoc = isWithoutSuppDoc;
	}

	public Integer getWithoutSuppDoc() {
		return withoutSuppDoc;
	}

	public void setWithoutSuppDoc(Integer withoutSuppDoc) {
		this.withoutSuppDoc = withoutSuppDoc;
	}

	public void setOtherBenefitTableDtoList(
			List<OtherBenefitsTableDto> otherBenefitTableDtoList) {
		this.otherBenefitTableDtoList = otherBenefitTableDtoList;
	}

	public String getDefinedLimit() {
		return definedLimit;
	}

	public void setDefinedLimit(String definedLimit) {
		this.definedLimit = definedLimit;
	}

	public Double getEnhBenefitApprovedAmount() {
		return enhBenefitApprovedAmount;
	}

	public void setEnhBenefitApprovedAmount(Double enhBenefitApprovedAmount) {
		this.enhBenefitApprovedAmount = enhBenefitApprovedAmount;
	}

	public Integer getDefinedLimitAmnt() {
		return definedLimitAmnt;
	}

	public void setDefinedLimitAmnt(Integer definedLimitAmnt) {
		this.definedLimitAmnt = definedLimitAmnt;
	}
	public Boolean getIsFvrIntiated() {
		return isFvrIntiated;
	}

	public void setIsFvrIntiated(Boolean isFvrIntiated) {
		this.isFvrIntiated = isFvrIntiated;
	}

	public Double getOtherBenefitPreauthApprAmt() {
		return otherBenefitPreauthApprAmt;
	}

	public void setOtherBenefitPreauthApprAmt(Double otherBenefitPreauthApprAmt) {
		this.otherBenefitPreauthApprAmt = otherBenefitPreauthApprAmt;
	}
	public Double getReverseAllocatedMininumAmt() {
		return reverseAllocatedMininumAmt;
	}

	public void setReverseAllocatedMininumAmt(Double reverseAllocatedMininumAmt) {
		this.reverseAllocatedMininumAmt = reverseAllocatedMininumAmt;
	}

	public Double getHospitalDiscountReverseAllocatedAmt() {
		return hospitalDiscountReverseAllocatedAmt;
	}

	public void setHospitalDiscountReverseAllocatedAmt(
			Double hospitalDiscountReverseAllocatedAmt) {
		this.hospitalDiscountReverseAllocatedAmt = hospitalDiscountReverseAllocatedAmt;
	}

	public Boolean getIsFvrIntiatedMA() {
		return isFvrIntiatedMA;
	}

	public void setIsFvrIntiatedMA(Boolean isFvrIntiatedMA) {
		this.isFvrIntiatedMA = isFvrIntiatedMA;
	}

	public String getRemarksForInsured() {
		return remarksForInsured;
	}

	public void setRemarksForInsured(String remarksForInsured) {
		this.remarksForInsured = remarksForInsured;
	}

	public String getWithDrawRemarksForInsured() {
		return withDrawRemarksForInsured;
	}

	public void setWithDrawRemarksForInsured(String withDrawRemarksForInsured) {
		this.withDrawRemarksForInsured = withDrawRemarksForInsured;
	}	
	public Boolean getIsAllowInitiateFVR() {
		return isAllowInitiateFVR;
	}

	public void setIsAllowInitiateFVR(Boolean isAllowInitiateFVR) {
		this.isAllowInitiateFVR = isAllowInitiateFVR;
	}

	public String getFvrNotRequiredOthersRemarks() {
		return fvrNotRequiredOthersRemarks;
	}

	public void setFvrNotRequiredOthersRemarks(
			String fvrNotRequiredOthersRemarks) {
		this.fvrNotRequiredOthersRemarks = fvrNotRequiredOthersRemarks;
	}

	public Boolean getIsInvsInitiated() {
		return isInvsInitiated;
	}

	public void setIsInvsInitiated(Boolean isInvsInitiated) {
		this.isInvsInitiated = isInvsInitiated;
	}

	public Boolean getIsFvrReplyReceived() {
		return isFvrReplyReceived;
	}

	public void setIsFvrReplyReceived(Boolean isFvrReplyReceived) {
		this.isFvrReplyReceived = isFvrReplyReceived;
	}

	public List<ViewFVRDTO> getFvrAdditionalTriggerPtsList() {
		return fvrAdditionalTriggerPtsList;
	}

	public void setFvrAdditionalTriggerPtsList(
			List<ViewFVRDTO> fvrAdditionalTriggerPtsList) {
		this.fvrAdditionalTriggerPtsList = fvrAdditionalTriggerPtsList;
	}
	
	public String getRemarksFromDeptHead() {
		return remarksFromDeptHead;
	}

	public void setRemarksFromDeptHead(String remarksFromDeptHead) {
		this.remarksFromDeptHead = remarksFromDeptHead;
	}

	public Object getUserRoleMulti() {
		return userRoleMulti;
	}

	public void setUserRoleMulti(Object userRoleMulti) {
		this.userRoleMulti = userRoleMulti;
	}

	public Object getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(Object doctorName) {
		this.doctorName = doctorName;
	}

	public Boolean getIsMandatory() {
		return isMandatory;
	}

	public void setIsMandatory(Boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	public String getDocRoleCode() {
		return docRoleCode;
	}

	public void setDocRoleCode(String docRoleCode) {
		this.docRoleCode = docRoleCode;
	}

	public BeanItemContainer<SpecialSelectValue> getDoctorContainer() {
		return doctorContainer;
	}

	public void setDoctorContainer(
			BeanItemContainer<SpecialSelectValue> doctorContainer) {
		this.doctorContainer = doctorContainer;
	}

	public String getHoldRemarks() {
		return holdRemarks;
	}

	public void setHoldRemarks(String holdRemarks) {
		this.holdRemarks = holdRemarks;
	}
	
	public String getAmountClaimedFrmHosp() {
		return amountClaimedFrmHosp;
	}

	public void setAmountClaimedFrmHosp(String amountClaimedFrmHosp) {
		this.amountClaimedFrmHosp = amountClaimedFrmHosp;
	}

	private Boolean isPortedPolicy;

	public Boolean getIsPortedPolicy() {
		return isPortedPolicy;
	}

	public void setIsPortedPolicy(Boolean isPortedPolicy) {
		this.isPortedPolicy = isPortedPolicy;
	}
	
	private String userClickAction;

	public String getUserClickAction() {
		return userClickAction;
	}

	public void setUserClickAction(String userClickAction) {
		this.userClickAction = userClickAction;
	}
	
	private Boolean isSDMarkedForOpinion;
	
	public Boolean getIsSDMarkedForOpinion() {
		return isSDMarkedForOpinion;
	}

	public void setIsSDMarkedForOpinion(Boolean isSDMarkedForOpinion) {
		this.isSDMarkedForOpinion = isSDMarkedForOpinion;
	}

	private Long finalClaimAmout;

	public Long getFinalClaimAmout() {
		return finalClaimAmout;
	}

	public void setFinalClaimAmout(Long finalClaimAmout) {
		this.finalClaimAmout = finalClaimAmout;
	}

	public List<DraftTriggerPointsToFocusDetailsTableDto> getTriggerPointsList() {
		return triggerPointsList;
	}

	public void setTriggerPointsList(
			List<DraftTriggerPointsToFocusDetailsTableDto> triggerPointsList) {
		this.triggerPointsList = triggerPointsList;
	}

	public List<AssignedInvestigatiorDetails> getInvsReviewRemarksTableList() {
		return invsReviewRemarksTableList;
	}

	public void setInvsReviewRemarksTableList(
			List<AssignedInvestigatiorDetails> invsReviewRemarksTableList) {
		this.invsReviewRemarksTableList = invsReviewRemarksTableList;
	}

	public String getNegotiationDeductionAmt() {
		return negotiationDeductionAmt;
	}

	public void setNegotiationDeductionAmt(String negotiationDeductionAmt) {
		this.negotiationDeductionAmt = negotiationDeductionAmt;
	}

	public String getAmtAfterNegotiationAmt() {
		return amtAfterNegotiationAmt;
	}

	public void setAmtAfterNegotiationAmt(String amtAfterNegotiationAmt) {
		this.amtAfterNegotiationAmt = amtAfterNegotiationAmt;
	}

	public String getPreauthDownsizeAmt() {
		return preauthDownsizeAmt;
	}

	public void setPreauthDownsizeAmt(String preauthDownsizeAmt) {
		this.preauthDownsizeAmt = preauthDownsizeAmt;
	}

	public String getIsDecisionMismatched() {
		return isDecisionMismatched;
	}

	public void setIsDecisionMismatched(String isDecisionMismatched) {
		this.isDecisionMismatched = isDecisionMismatched;
	}

	public String getStpClaimedAmt() {
		return stpClaimedAmt;
	}

	public void setStpClaimedAmt(String stpClaimedAmt) {
		this.stpClaimedAmt = stpClaimedAmt;
	}

	public String getStpDeductibleAmt() {
		return stpDeductibleAmt;
	}

	public void setStpDeductibleAmt(String stpDeductibleAmt) {
		this.stpDeductibleAmt = stpDeductibleAmt;
	}

	public String getStpApprovedAmt() {
		return stpApprovedAmt;
	}

	public void setStpApprovedAmt(String stpApprovedAmt) {
		this.stpApprovedAmt = stpApprovedAmt;
	}

	public String getStpStatus() {
		return stpStatus;
	}

	public void setStpStatus(String stpStatus) {
		this.stpStatus = stpStatus;
	}
	
	//R1256
	private boolean chkSubmittedDoc = false;
	private boolean chkFieldVisitReport = false;
	private boolean chkInvestigationReport = false;
	private boolean chkOthers = false;
	private String txtaOthersRemarks;

	public boolean getChkSubmittedDoc() {
		return chkSubmittedDoc;
	}

	public void setChkSubmittedDoc(boolean chkSubmittedDoc) {
		this.chkSubmittedDoc = chkSubmittedDoc;
	}

	public boolean getChkFieldVisitReport() {
		return chkFieldVisitReport;
	}

	public void setChkFieldVisitReport(boolean chkFieldVisitReport) {
		this.chkFieldVisitReport = chkFieldVisitReport;
	}

	public boolean getChkInvestigationReport() {
		return chkInvestigationReport;
	}

	public void setChkInvestigationReport(boolean chkInvestigationReport) {
		this.chkInvestigationReport = chkInvestigationReport;
	}

	public boolean getChkOthers() {
		return chkOthers;
	}

	public void setChkOthers(boolean chkOthers) {
		this.chkOthers = chkOthers;
	}

	public String getTxtaOthersRemarks() {
		return txtaOthersRemarks;
	}

	public void setTxtaOthersRemarks(String txtaOthersRemarks) {
		this.txtaOthersRemarks = txtaOthersRemarks;
	}

	public String getClaimedAmt() {
		return claimedAmt;
	}

	public void setClaimedAmt(String claimedAmt) {
		this.claimedAmt = claimedAmt;
	}

	public Double getNegotiationAmount() {
		return negotiationAmount;
	}

	public void setNegotiationAmount(Double negotiationAmount) {
		this.negotiationAmount = negotiationAmount;
	}

	public Double getSavedAmt() {
		return savedAmt;
	}

	public void setSavedAmt(Double savedAmt) {
		this.savedAmt = savedAmt;
	}

	public Boolean getNegotiationMade() {
		return negotiationMade;
	}

	public void setNegotiationMade(Boolean negotiationMade) {
		this.negotiationMade = negotiationMade;
	}
	
	public Boolean getNegotiationDecisionTaken() {
		return negotiationDecisionTaken;
	}

	public void setNegotiationDecisionTaken(Boolean negotiationDecisionTaken) {
		this.negotiationDecisionTaken = negotiationDecisionTaken;
	}

	public String getNegotiatePoints() {
		return negotiatePoints;
	}

	public void setNegotiatePoints(String negotiatePoints) {
		this.negotiatePoints = negotiatePoints;
	}

	public String getUpdateNegotiatePoints() {
		return updateNegotiatePoints;
	}

	public void setUpdateNegotiatePoints(String updateNegotiatePoints) {
		this.updateNegotiatePoints = updateNegotiatePoints;
	}

	public Boolean getIsValidNegotiation() {
		return isValidNegotiation;
	}

	public void setIsValidNegotiation(Boolean isValidNegotiation) {
		this.isValidNegotiation = isValidNegotiation;
	}

	public Boolean getIsNegotiationCancelled() {
		return isNegotiationCancelled;
	}

	public void setIsNegotiationCancelled(Boolean isNegotiationCancelled) {
		this.isNegotiationCancelled = isNegotiationCancelled;
	}

	public String getAmtToNegotiated() {
		return amtToNegotiated;
	}

	public void setAmtToNegotiated(String amtToNegotiated) {
		this.amtToNegotiated = amtToNegotiated;
	}
	
	//R1295
	public SelectValue getQueryType() {
		return queryType;
	}

	public void setQueryType(SelectValue queryType) {
		this.queryType = queryType;
	}

	public Boolean getIsNegotiationDone() {
		return isNegotiationDone;
	}

	public void setIsNegotiationDone(Boolean isNegotiationDone) {
		this.isNegotiationDone = isNegotiationDone;
	}
	public Boolean getIsNegSelect() {
		return isNegSelect;
	}

	public void setIsNegSelect(Boolean isNegSelect) {
		this.isNegSelect = isNegSelect;
	}

	public String getDownsizeInsuredRemarks() {
		return downsizeInsuredRemarks;
	}

	public void setDownsizeInsuredRemarks(String downsizeInsuredRemarks) {
		this.downsizeInsuredRemarks = downsizeInsuredRemarks;
	}
	
	
	public String getWithdrawInternalRemarks() {
		return withdrawInternalRemarks;
	}

	public void setWithdrawInternalRemarks(String withdrawInternalRemarks) {
		this.withdrawInternalRemarks = withdrawInternalRemarks;
	}

	public SelectValue getRejSubCategory() {
		return rejSubCategory;
	}

	public void setRejSubCategory(SelectValue rejSubCategory) {
		this.rejSubCategory = rejSubCategory;
	}

	public String getPolicyConditionNoReject() {
		return policyConditionNoReject;
	}

	public void setPolicyConditionNoReject(String policyConditionNoReject) {
		this.policyConditionNoReject = policyConditionNoReject;
	}
	

	public String getPolicyRejectConditClause() {
		return policyRejectConditClause;
	}

	public void setPolicyRejectConditClause(String policyRejectConditClause) {
		this.policyRejectConditClause = policyRejectConditClause;
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

	public String getPayeeRelationship() {
		return payeeRelationship;
	}

	public void setPayeeRelationship(String payeeRelationship) {
		this.payeeRelationship = payeeRelationship;
	}

	public boolean isHospitalDiscount() {
		return hospitalDiscount;
	}

	public void setHospitalDiscount(boolean hospitalDiscount) {
		this.hospitalDiscount = hospitalDiscount;
	}
	
	public String getNegotiationWith() {
		return negotiationWith;
	}

	public void setNegotiationWith(String negotiationWith) {
		this.negotiationWith = negotiationWith;
	}

	public Double getTotalNegotiationSaved() {
		return totalNegotiationSaved;
	}

	public void setTotalNegotiationSaved(Double totalNegotiationSaved) {
		this.totalNegotiationSaved = totalNegotiationSaved;
	}

	public Double getHigestCLTrans() {
		return higestCLTrans;
	}

	public void setHigestCLTrans(Double higestCLTrans) {
		this.higestCLTrans = higestCLTrans;
	}

	public Boolean getVerifiedBonus() {
		return verifiedBonus;
	}

	public void setVerifiedBonus(Boolean verifiedBonus) {
		this.verifiedBonus = verifiedBonus;
	}

	public Double getPolicyInstalmentPremiumAmt() {
		return policyInstalmentPremiumAmt;
	}

	public void setPolicyInstalmentPremiumAmt(Double policyInstalmentPremiumAmt) {
		this.policyInstalmentPremiumAmt = policyInstalmentPremiumAmt;
	}

	public Date getPolicyInstalmentDueDate() {
		return policyInstalmentDueDate;
	}

	public void setPolicyInstalmentDueDate(Date policyInstalmentDueDate) {
		this.policyInstalmentDueDate = policyInstalmentDueDate;
	}

	public Double getPolicyInstPremiumAmt() {
		return policyInstPremiumAmt;
	}

	public void setPolicyInstPremiumAmt(Double policyInstPremiumAmt) {
		this.policyInstPremiumAmt = policyInstPremiumAmt;
	}
	
	public SelectValue getBehaviourHospValue() {
		return behaviourHospValue;
	}

	public void setBehaviourHospValue(SelectValue behaviourHospValue) {
		this.behaviourHospValue = behaviourHospValue;
	}

	public Boolean getBehaviourHosp1chkbox() {
		return behaviourHosp1chkbox;
	}

	public void setBehaviourHosp1chkbox(Boolean behaviourHosp1chkbox) {
		this.behaviourHosp1chkbox = behaviourHosp1chkbox;
	}

	public Boolean getBehaviourHospMbagreedchkbox() {
		return behaviourHospMbagreedchkbox;
	}

	public void setBehaviourHospMbagreedchkbox(Boolean behaviourHospMbagreedchkbox) {
		this.behaviourHospMbagreedchkbox = behaviourHospMbagreedchkbox;
	}

	public String getHospCollectOvrAmtChkBx() {
		return hospCollectOvrAmtChkBx;
	}

	public void setHospCollectOvrAmtChkBx(String hospCollectOvrAmtChkBx) {
		this.hospCollectOvrAmtChkBx = hospCollectOvrAmtChkBx;
	}

	public String getMbAgrdDisNtAplyChkBx() {
		return mbAgrdDisNtAplyChkBx;
	}

	public void setMbAgrdDisNtAplyChkBx(String mbAgrdDisNtAplyChkBx) {
		this.mbAgrdDisNtAplyChkBx = mbAgrdDisNtAplyChkBx;
	}

	public Boolean getRelInstalmentOptFlag() {
		return relInstalmentOptFlag;
	}

	public void setRelInstalmentOptFlag(Boolean relInstalmentOptFlag) {
		this.relInstalmentOptFlag = relInstalmentOptFlag;
	}

	public String getRejectionRemarks2() {
		return rejectionRemarks2;
	}

	public void setRejectionRemarks2(String rejectionRemarks2) {
		this.rejectionRemarks2 = rejectionRemarks2;
	}

	public SelectValue getDecisionChangeReason() {
		return decisionChangeReason;
	}

	public void setDecisionChangeReason(SelectValue decisionChangeReason) {
		this.decisionChangeReason = decisionChangeReason;
	}

	public String getDecisionChangeRemarks() {
		return decisionChangeRemarks;
	}

	public void setDecisionChangeRemarks(String decisionChangeRemarks) {
		this.decisionChangeRemarks = decisionChangeRemarks;
	}
	
	
	
}
	

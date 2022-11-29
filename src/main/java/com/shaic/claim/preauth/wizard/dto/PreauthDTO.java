package com.shaic.claim.preauth.wizard.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.DMSDocumentDetailsDTO;
import com.shaic.claim.SumInsuredBonusAlertDTO;
import com.shaic.claim.cashlessprocess.downsize.wizard.DownSizePreauthDataExtractionDTO;
import com.shaic.claim.coinsurance.view.CoInsuranceTableDTO;
import com.shaic.claim.common.RoomRentMatchingDTO;
import com.shaic.claim.enhacement.table.PreviousPreAuthTableDTO;
import com.shaic.claim.enhancements.preauth.wizard.PreauthEnhancementButtons;
import com.shaic.claim.intimation.create.dto.PolicyDto;
import com.shaic.claim.leagalbilling.LegalBillingDTO;
import com.shaic.claim.preauth.ProcessPreAuthButtonLayout;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.dto.ResidualAmountDTO;
import com.shaic.claim.preauth.dto.SpecificProductDeductibleTableDTO;
import com.shaic.claim.preauth.wizard.view.InitiateInvestigationDTO;
import com.shaic.claim.premedical.dto.PreauthMedicalProcessingDTO;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.shaic.claim.reimbursement.billing.dto.ConsolidatedAmountDetailsDTO;
import com.shaic.claim.reimbursement.billing.dto.HopitalizationCalulationDetailsDTO;
import com.shaic.claim.reimbursement.billing.dto.OtherInsHospSettlementDetailsDTO;
import com.shaic.claim.reimbursement.billing.dto.OtherInsPostHospSettlementDetailsDTO;
import com.shaic.claim.reimbursement.billing.dto.OtherInsPreHospSettlementDetailsDTO;
import com.shaic.claim.reimbursement.billing.dto.PostHopitalizationDetailsDTO;
import com.shaic.claim.reimbursement.billing.dto.PreHopitalizationDetailsDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.reimbursement.financialapproval.pages.billinghospitalization.VerificationAccountDeatilsTableDTO;
import com.shaic.claim.reimbursement.financialapproval.pages.communicationPage.NonPayableReasonDto;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.ClaimRequestButtonsForWizard;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.ClaimRequestMedicalDecisionButtons;
import com.shaic.claim.reimbursement.talktalktalk.InitiateTalkTalkTalkDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.viewEarlierRodDetails.EsclateToRawTableDTO;
import com.shaic.claim.viewEarlierRodDetails.dto.PreHospitalizationDTO;
import com.shaic.cmn.login.ImsUser;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.InsuredPedDetails;
import com.shaic.domain.SublimitFunObject;
import com.shaic.domain.preauth.PreExistingDisease;
import com.shaic.newcode.wizard.dto.LegalHeirDTO;
//import com.shaic.ims.bpm.claim.HumanTask;
import com.shaic.newcode.wizard.dto.NewIntimationDto;

public class PreauthDTO {

	private Long key;
	private Object searchFormDTO;
	private Long preauthKey;
	private Long previousPreauthKey = 0l;
	private Long policyKey;
	private Long intimationKey;
	private Long unNamedKey;
	private String claimNumber;
	private String intimationNumber;
	private Long hospitalKey;
	private String hospitalCode;
	private Long claimKey;
	private Long stageKey;
	private Long statusKey;
	
	private Long oldStatusKey;
	private String statusValue;
	private String processType;
	private Long investigationKey;
	private Long fvrKey;
    private Long queryKey;
    private Long specialistKey;
    private String createdBy;
    private Date createDate;
    private Integer fvrCount = 0;
    private Boolean isPreviousROD = false;
    private Boolean shouldDetectPremium = true;
    private Boolean isWithDrawn = false;
//    private Reimbursement previousROD;
    private Boolean isPostHospitalization = false;
   
    private Boolean isCashlessType = false;
    private Boolean isReferToMedicalApprover = false;
    private Boolean isZonalReviewQuery = false;
    private String hospitalisationValue = "0";
    private String postHospitalisationValue = "0";
    private Boolean isEscalateReplyEnabled = false;
    private Double postHospPercentage = 0d;
    private Double postHospclaimRestrictionAmount = 0d;
    private Boolean isSIRestrictionAvail = false;
    private Integer siRestrictionAmount = 0;
    private Integer balanceSIAftHosp = 0;
    private Integer previousPostHospAmount = 0;
    private Integer previousPreHospAmount = 0;
    private Double nonAllopathicOriginalAmt = 0d;
    private Double nonAllopathicUtilizedAmt = 0d;
    private Boolean isPostHospApplicable = true;
    private Boolean isPreHospApplicable = true;
    private Boolean isLumpsumApplicable = false;
    private Boolean isHospitalCashApplicable = false;
    private Boolean isPatientCareApplicable = false;
    private Boolean shouldShowPostHospAlert = false;
    private Boolean shouldDisableSection = false;
    private Date zonalDate;

    private Boolean isDefaultCopay = false;
    private Boolean isDefaultCopaySaved = false;
    private String defaultCopayStr ;

    private Boolean isReferTOFLP = false;

    private Double patientCareAmt = 0d;
    private Double hospitalCashAmt = 0d;
    private Double revisedProvisionAmount = 0d;

    private Double uniqueDeductedAmount = 0d;
    
    private Double uniquePremiumAmount = 0d;

    private Double totalConsolidatedAmt = 0d;
    private String modifiedBy;
    private Boolean isPEDInitiated = false;
    private Boolean isAutoRestorationDone = false;
    private Boolean isPEDInitiatedForBtn = false;
    private Boolean isCopaySelected = false;
    
    private Boolean isFirstStepRejection = false;
    
    private String skipZmrFlag;
    
    private Long claimCount = 1l;

    private Double siRestrictionAmt = 0d;
    
    private Double sublimitTotalAvailableAmt = 0d;
    
    private Boolean is64VBChequeStatusAlert = false;    

    private Object dbOutArray ;
    
    private String finalScore;
    
    private String finalScoreDeficiency;
    
    private Boolean isPreauthAutoAllocationQ = false;

    private String isPaymentQuery ;
    
    private Boolean isReferToClaimApproval =false;
    
    private String coversBenefitsValue;    
    private Boolean isEditBillClassification = false;
    
    private Boolean isNEFTDetailsAvailable = false;
    
    private Boolean isNEFTDetailsAvailableinDMS = false;
    
    private Integer noOfdaysLimit = 0;
    private Integer previousRODNoOfDays = 0;
    private String clmPrcsInstruction;
    
    private String isSuspicious;
    
    private Boolean isViewCashlessDocClicked = false;
    private Boolean isAboveLimitCorpAdvise = false;
    private Boolean isAutoAllocationCorpUser = false;
	private Boolean isAutoAllocationCPUUser = false;
	private Boolean isAboveLimitCorpProcess = false;
    
    
    private Double admissableAmntOfCurrentClaim;
    
    private Double amntAfterDefinedLimit;
    
    private Boolean isChangeInsumInsuredAlert = false;
    
    private Long rodParentKey;
    private Long version;
    private String crcFlaggedReason;
    
    private String crcFlaggedRemark;
    
    private Long coPayTypeId;
    
    private Long preauthHoldStatusKey;
    
    private Boolean isInvsRaised = Boolean.FALSE;
    
    private String stageName;
    
	private List<EsclateToRawTableDTO> deletedEsclateRawList;
	
	private List<EsclateToRawTableDTO> EsclateToRawList;	
	private Long esclateStageKey;
    private Boolean isDateOfAdmissionAlertOpened = Boolean.FALSE;    
    private SelectValue riskName;
    
    private String zohoGrievanceFlag;
    
    private Boolean isPremiumInstAmtEql = false;
    
    private boolean letterContentValidated;
    
    private Boolean isModernSublimitSelected = false;
    
    private Boolean isSublimitIdAvail = false;
    
    private Boolean isModernSublimitSIExhaust = false;
    
    private Boolean isHcTopupPolicyAvail = false;
    
    private Date topUpInsuredDOB;
	
	private Double topUpInsuredage;
    
    public Boolean getIsCopaySelected() {
		return isCopaySelected;
	}

	public void setIsCopaySelected(Boolean isCopaySelected) {
		this.isCopaySelected = isCopaySelected;
	}

	private Double roomRentEligiblity = 0d;
    
    private Boolean isNonAllopathicApplicable = false;
    
    private Boolean isRejectionROD = false;
    private Boolean isQueryReceived = false;
    private Boolean alertMessageOpened = false;
    private Boolean alertMessageForCopay = false;
    private Boolean dialysisOpened = false;
    private Boolean isDialysis = false;
    private Boolean isReverseAllocation = false;
    private Boolean isReverseAllocationHappened = false;
    private Boolean isReconsiderationRequest = false;
    private Boolean isPopupMessageOpened = false;
    private Boolean isHospitalizationRejected = false;
    private Boolean rejectionAlertMessageOpened = false;
    private Boolean isFinalEnhancement = false;
    private Boolean isBack = false;
    private Boolean isPreMedicalForCoordinator = false;
    private Boolean isPaymentAvailable = false;
    private Boolean isPaymentAvailableShown = false;
    private Double hospitalDiscount = 0d;
    private Double deductions = 0d;
    
    private Double preHospitalDeduction = 0d;
    private Double postHospitalDeduction = 0d;
    private Double rodTotalClaimedAmount = 0d;
    
    private String hospitalDiscountRemarks = "";
    private String hospitalDiscountRemarksForAssessmentSheet;
    private String deductionRemarks = "";
    private String deductionRemarksForAssessmentSheet;
    
    private Integer taskNumber;
    
    private Double amountConsAftCopayAmt = 0d;
    private Double balanceSIAftCopayAmt = 0d;
    
    private Double amountConsCopayPercentage = 0d;
    private Double balanceSICopayPercentage = 0d;
    
    private Map<String, String> popupMap = new HashMap<String, String>();
    
    private Map<String, String> suspiciousPopupMap = new HashMap<String, String>();
    
    Map<Integer, Object> hospitalizationDetailsVal = new HashMap<Integer, Object>();
    
    private Boolean isInvestigation = false;
    
    private boolean directToAssignInv;
    
    private Long claimRestrictionAmount = 0l;
    
    private Integer sittingsAmount = 1000;
    
    private Integer hospAmountInBiling = 0;
    
    private Integer preHospAmtInBilling = 0;
    
    private Integer postHospAmtInBilling = 0;
    
    private Integer hospAmountAlreadyPaid = 0;
    
    private Integer preHospAmtAlreadyPaid = 0;
    
    private Integer postHospAmtAlreadyPaid = 0;
    
    private UploadDocumentDTO uploadDocDTO;
    
    private String productBasedProRata;
    
    private String productBasedPackage;
    
    private Double SublimitAndSIAmt = 0d;
    
    
    private Boolean isDishonoured = false;

    private Double cpuProvisionAmt = 0d;
    
    private Boolean isPending = false;
    
    private Boolean isReleased = false;
    
    private Float entitlmentNoOfDays = 0f;
    private String comparisonResult = "";
    private Boolean isComparisonDone = false;
    
    private Double FAApprovedAmount = 0d;
    
    private Long insuredKey;
    
    private Long hospitalId;
    

    private Boolean maternityFlag=false;
    
    private Long documentReceivedFromId = 0l;
    
    private String makerVerified = null;
    
    private String checkerVerified = null;
    
    private Boolean isRechargePopUpOpened = false;
    
    private String networkHospitalType;

    private Double claimPaymentAmount = 0d;	
    
    private Boolean isByPass = false;
    
    private Boolean admissionDatePopup = true;
    
    private Boolean  isFirstPageSubmit= false;
    
    private Boolean isPreviousPreauthWithdraw = false;
    
    private Boolean decisionChangeReasonApplicable = false;
    
    /*
     * Added for claim dms enhancement requirement -
     * The generated letter will be upload to DMS system.
     * */ 
	private String docFilePath;
	
	private String docType;
	
	private String docSource;
	
	private String copayRemarks;
	

    /*
     * Added for claim dms enhancement requirement -
     * The generated letter will be upload to DMS system.
     * */ 
	private String billAssessmentDocFilePath;
	
	private String billAssessmentDocType;
	
	private String billAssessmentDocSource;
	
	/**
	 * This is used only incase  of FA,
	 * where two documents are generated in same panel.
	 * */
	private WeakHashMap filePathAndTypeMap;
	
	private List<ReconsiderRODRequestTableDTO> reconsiderRodRequestList;
	
	private List<InitiateTalkTalkTalkDTO> initiateTalkTalkTalkDTOList;
	
	private SelectValue reconsiderationRequest;
	
	private SelectValue reasonForReconsideration;
	
	private Boolean isReplyToFA = false;
	
	private Double nonPayableTotalAmt;
	
	private Double proportionateDeductionTotalAmt;
	
	private Double reasonableDeductionTotalAmt;
	
	private List<InsuredPedDetails> insuredPedDetails;
	
	private Boolean isAmbulanceApplicable = false;
	
	private Double ambulanceLimitAmount = 0d;

    //Added for current provision amt enhancement.

	
	/**
	 * Below vars. added for the purpose of Bill Summary Report Pdf
	 * 
	 */
	
	
	private Double totalClaimedAmt;
	private Double toatlNonPayableAmt;
	private Double totalApprovedAmt;
	private Double amountTotal;
	private Double nonpayableProdTotal;
	private Double nonpayableTotal;
	private Double propDecutTotal;
	private Double reasonableDeducTotal;
	private Double disallowanceTotal;
	private Double netPayableAmtTotal;	
	
	private Double exceeds25percentAmt = 0d;
	private Double exceeds50percentAmt = 0d;
	private Double exceeds75percentAmt = 0d;
	
	
	private Double finalTotalApprovedAmount;
	
	
	private Double diffAmount;
	
	private Double hospDiscountAmount = 0d;	
	private Double payableToHospAmt = 0d;	
	private Double payableToInsAmt = 0d;	
	
	private Boolean isPedPending = false;
	
	private Date sfxRegisteredQDate;
	
	private Date sfxMatchedQDate;
	
	private String documentSource;
	
	private String totalDeductibleAmount = "0";
	
	private List<DMSDocumentDetailsDTO> dmsDocumentDTOList ;
	
	private List<DocumentDetailsDTO> documentDetailsDTOList = new ArrayList<DocumentDetailsDTO>();
	
	private List<DocAcknowledgement> documentAckList = new ArrayList<DocAcknowledgement>();
	
	private InitiateInvestigationDTO initInvDto;

	
	private Boolean hospitalizaionFlag = false;
    private Boolean partialHospitalizaionFlag = false;
    private Boolean preHospitalizaionFlag = false;
    private Boolean postHospitalizaionFlag = false;
    private Boolean isHospitalizationRepeat = false;
    private Boolean lumpSumAmountFlag = false;
    private Boolean addOnBenefitsHospitalCash = false;
    private Boolean addOnBenefitsPatientCare = false;
    private Boolean otherBenefitsFlag = false;
	private Boolean emergencyMedicalEvaluation = false;	
	private Boolean compassionateTravel = false;	
	private Boolean repatriationOfMortalRemains = false;	
	private Boolean preferredNetworkHospital = false;	
	private Boolean sharedAccomodation = false;    

    private Boolean hospitalCash = false;

    
    private Boolean isPedWatchList = false;
    private Integer addOnBenefitsAmount = 0;
    private Boolean isConsiderForPaymentNo = false;
    private Boolean isRejectReconsidered = null;
    private Boolean isHospitalDiscountApplicable = false;
    
    
    private SelectValue catastrophicLoss;
	private SelectValue natureOfLoss;
	private SelectValue causeOfLoss;
    private Long modeOfReceipt;

    /**
     * Added for view bill summary scrc requirement. 
     */

    private List<BillEntryDetailsDTO> hospitalizationTabSummaryList;
    
    private List<PreHospitalizationDTO> preHospitalizationTabSummaryList;
    
    private List<PreHospitalizationDTO> postHospitalizationTabSummaryList;
    
    private List<PreHospitalizationDTO> prePostHospitalizationList = null;


    //Specifically set in view bill summary;
    private Double sumInsuredFromView;
    
    //Added for assesement.
    private Double totalItemValueForAssesment;
    
    //Added for scrc assesment
    
    private Double policyETotalDisallowanceAmt = 0d;;
    
    //private Double poli
    
    private Double bariatricAvailableAmount = 0d;
    
   private Double paTotalClaimedAmnt = 0d;
   private Double paTotalDisAllowance = 0d;
   private Double paTotalApprovedAmnt = 0d;
   private Double paAlreadyPaidAmnt = 0d;

   private Boolean isPaymentSettled = false; 

    private Integer otherInsurerClaimedAmount = 0;
    private Integer otherInsurerDeductionAmt = 0;
    private Integer otherInsurerAdmissibleAmt = 0;   
    
    private Long benefitId;
    
    private String paCoversSelected;
    private Long investigationStage;
    private Long investigationStatus;
    
    private Boolean isScheduleClicked = false;

    private Boolean isInsuredChannedNameClicked = false;
    
    private Boolean isBusinessProfileClicked = false;
    
    public String userLimitAmount;
    
    private String alreadySettlementAmt;

    private String gstNumber;
    private String gstState;
    private String arnNumber;
    public String payModeChangeReason;
    
    private List<PreviousAccountDetailsDTO> previousAccntDetailsList;
    public Boolean isAlertSublimtChanges = false;
    
    private Map<Long, SublimitFunObject> sublimitFunMap;
    private Boolean isZUAQueryAvailable;
    public Boolean isFvrPending = Boolean.FALSE;
    public Boolean isInvsPending = Boolean.FALSE;
    public Boolean isQueryPending = Boolean.FALSE;
    private Boolean isWaitingDaysLessThan30 = false;
    private Boolean isWaitingDaysLessThan180 = false;
    
    private Boolean isFvrInitiate = false;
    
    private Boolean isFvrClicked = false;
    private Boolean isParallelInvFvrQuery = false;
    private Boolean isFvrInitiatedInZMR = false;
    private Boolean isInvsInitiatedInZMR = false;
    

    private Boolean isFvrNotRequiredAndSelected = false;
    private String updatePaymentDtlsFlag;
    
    private Boolean alertMessageHoldOpened = false;
    
    private List<PreExistingDisease> approvedPedDetails;
    
    private String FVRPendingRsn;
    private String screenName;
    
    private Long parallelStatusKey;
    private String crmFlagged;
    private Boolean isPaayasPolicy = Boolean.FALSE;
    private String fvrAlertFlag;
    private Boolean isFVRAlertOpened = Boolean.FALSE;
    
    private String fvrNotRequiredRemarks;
    
    private Boolean isFvrButtonDisabled = false;
    private Boolean isInvsPendingInFA = Boolean.FALSE;
    private List<SelectValue> duplicateInsuredList;
    private List<SelectValue> superSurplusAlertList;
    
    private Boolean scoringClicked = false;

    private Boolean verificationClicked = false;
 
    private Boolean scoringVisibility = false;
    
    private String hospitalScoreFlag;
    private String topUpPolicyAlertFlag;
    private String topUpPolicyAlertMessage;
    
    private boolean isInsuredDeleted = false;
    
    private boolean multiplePEDAvailableNotDeleted;
    private PedDetailsTableDTO pedDetailsDTO;
    private Boolean isTataPolicy = Boolean.FALSE;

    private Boolean negotiationMade = false;

    private boolean isAllowedToNxt = true;

    
    private Boolean isNegotiationPending = false;
    private Boolean isNegotiationApplicable = false;
    
    private Boolean negotiationagreed;
    
    private String negotiationagreedFlagValue;
    
    private String regotiateRemarks;
    
    private String agreedWith;
    
    private String pointstoNegotiate;
    
    private Boolean isNegotiationEnhnApproved = false;
    
    private String amtToNegotiate;
    private String sublimitUpdateRemarks;
    
    //CR2019017 - Start
    private Boolean isSDEnabled = false;
    private ProcessPreAuthButtonLayout processPreauthButtonObj;
    private PreauthEnhancementButtons processEnhancementButtonObj;
    private ClaimRequestMedicalDecisionButtons claimReqButtonObj;
    private Boolean isValidationReq = false;
    //CR2019017 - End
    private Long docAckKey;
    private Boolean isBillClasificationEdit;
    private String vbCheckStatusFlag;
    
    //top up policy variables for procedure
    private String topUpInsuredName;
    
    private Long topUpInsuredNo;

    private Long vipCustomer;
    
    private LegalHeirDTO legalHeirDto;
    
    private List<LegalHeirDTO> legalHeirDTOList;
    
    
    private Boolean isOthrBenefitApplicable = false;
    
  //IMSSUPPOR-29851
  	private Double maxCopayAmtValueforAssmtSheet;
  	
  	private String hospitalDiscountFlag;

  	private Boolean auditFlag = false;
  	
  	private String siAlertFlag;
  	
  	private String siAlertDesc;
  	
  	private boolean isSIRequired;
  	
	private String flpAaUserID;
	
	private Date flpAaUserSubmittedDate;
	
    private Double policyInstalmentPremiumAmt = 0d;
	
	private Date policyInstalmentDueDate;
	
	private Long nhpUpdKey;
	
	private String policyInsuredAgeingFlag;
	
	private Date docRecievedDate;
	
	
	//Added for GMC
	private Boolean iccuProportionalDeductionFlg = false;
	
	private Boolean otProportionalDeductionFlg = false;
	
	private Boolean profFeesProportionalDeductionFlg = false;
	
	private Boolean medicineProportionalDeductionFlg = false;
	
	private Boolean investigationProportionalDeductionFlg = false;
	
	private Boolean otherPackProportionalDeductionFlg = false;
	
	private Boolean othersProportionalDeductionFlg = false;
	
	private Boolean ambulanceProportionalDeductionFlg = false;
	
    private Boolean aNHProportionalDeductionFlg = false;
	
	private Boolean compositeProportionalDeductionFlg = false;
	
    private Boolean procedureProportionalDeductionFlg = false;
	
	private Boolean misWithHosProportionalDeductionFlg = false;   
	
	private Boolean misWithoutHosProportionalDeductionFlg = false; 
	
	//Added for GMC prop Checked
		private Boolean iccuProportionalDeductionChecked = false;
		
		private Boolean otProportionalDeductionChecked = false;
		
		private Boolean profFeesProportionalDeductionChecked = false;
		
		private Boolean medicineProportionalDeductionChecked = false;
		
		private Boolean investigationProportionalDeductionChecked = false;
		
		private Boolean otherPackProportionalDeductionChecked = false;
		
		private Boolean othersProportionalDeductionChecked = false;
		
		private Boolean ambulanceProportionalDeductionChecked = false;
		
	    private Boolean aNHProportionalDeductionChecked = false;
		
		private Boolean compositeProportionalDeductionChecked = false;
		
	    private Boolean procedureProportionalDeductionChecked = false;
		
		private Boolean misWithHosProportionalDeductionChecked = false;  
		
		private Boolean misWithoutHosProportionalDeductionChecked = false;
		
		private Boolean isCashlessPropDedSelected = false; 

	private Boolean approveButtonExists = false;

	private Boolean rejectionButtonExists = false;
	
	private String claimPriorityLabel;
	
	private Boolean isPhysicalVerificationPending = false;
	 
	private Boolean isPhysicalVerificationcompleted = false;
	
	private Long productVersionNumber;
	
	private String productUinNumber;
	
	public Boolean getIsPaymentSettled() {
		return isPaymentSettled;
	}

    private Map<String, String> nonPreferredPopupMap = new HashMap<String, String>();


	public void setIsPaymentSettled(Boolean isPaymentSettled) {
		this.isPaymentSettled = isPaymentSettled;
	}
   

	public Double getPolicyETotalDisallowanceAmt() {
			return policyETotalDisallowanceAmt;
		}



	public void setPolicyETotalDisallowanceAmt(Double policyETotalDisallowanceAmt) {
		this.policyETotalDisallowanceAmt = policyETotalDisallowanceAmt;
	}
    
	private List<List<PreviousAccountDetailsDTO>> previousAccountDetailsList;


	public List<List<PreviousAccountDetailsDTO>> getPreviousAccountDetailsList() {
		return previousAccountDetailsList;
	}


public Double getTotalItemValueForAssesment() {
		return totalItemValueForAssesment;
	}



	public void setTotalItemValueForAssesment(Double totalItemValueForAssesment) {
		this.totalItemValueForAssesment = totalItemValueForAssesment;
	}

	public void setPreviousAccountDetailsList(
			List<List<PreviousAccountDetailsDTO>> previousAccountDetailsList) {
		this.previousAccountDetailsList = previousAccountDetailsList;
	}


public String getDocType() {
		return docType;
	}



	public void setDocType(String docType) {
		this.docType = docType;
	}



public String getDocFilePath() {
		return docFilePath;
	}



	public void setDocFilePath(String docFilePath) {
		this.docFilePath = docFilePath;
	}



	//  private Double reimbursementCurrentProvisionAmt ; 
    public Long getPreauthKey() {
		return preauthKey;
	}



	public Double getPatientCareAmt() {
		return patientCareAmt;
	}



	public void setPatientCareAmt(Double patientCareAmt) {
		this.patientCareAmt = patientCareAmt;
	}



	public Double getHospitalCashAmt() {
		return hospitalCashAmt;
	}



	public void setHospitalCashAmt(Double hospitalCashAmt) {
		this.hospitalCashAmt = hospitalCashAmt;
	}



	public void setPreauthKey(Long preauthKey) {
		this.preauthKey = preauthKey;
	}



	private String preHospitalisationValue = "0";
    private Integer postHospAmt  = 0;
    private RRCDTO rrcDTO;
    private InitiateTalkTalkTalkDTO initiateTalkTalkTalkDTO;

    public Boolean getPartialHospitalizaionFlag() {
		return partialHospitalizaionFlag;
	}



	public void setPartialHospitalizaionFlag(Boolean partialHospitalizaionFlag) {
		this.partialHospitalizaionFlag = partialHospitalizaionFlag;
	}



	private Integer hospitalizationAmount = 0;
    private Double billingApprovedAmount = 0d;
    private Boolean isHospitalizationRODApproved = true;
    private Double currentProvisionAmt = 0d;
    private Integer investigationSize = 0;
	private String previousRemarks;
	private String previousReasonForReferring;
	private List<RoomRentMatchingDTO> roomRentMappingDTOList;
	private List<RoomRentMatchingDTO> icuRoomRentMappingDTOList;
	private List<RoomRentMatchingDTO> iccuRoomRentMappingDTOList;
	private Boolean isOneMapping = true;
	private Boolean isICUoneMapping = true;
	private Boolean isICCUoneMapping = true;
	private Boolean isReferToBilling = false;
	private Boolean isReferToBillEntry = false;
	private Date medicalCompletedDate;
	private Date financialCompletedDate;
	
	private Double settledAmount = 0d;
	private Double deductibleAmount = 0d;
	private Boolean isNonAllopathic = false;
	private Integer nonAllopathicAvailAmt = 0;
	
	private Double prorataPercentage = 0d;
	private String packageAvailableFlag;
	private String prorataDeductionFlag;
	
	private Long medicalApproverKey = 0l;
	private Boolean isDirectToBilling = false;
	private Boolean isDirectToFinancial = false;
	
	private String remarks;
	
	private Double totalHospitalizationDeductionsForAssesment;
	
	private String proceedWithoutReport;
	
	private Boolean isDefaultDeductable = Boolean.FALSE;
	
	private String autoAllocCancelRemarks;
	
	private String agentColorCode;
	
	private String branchColorCode;
	
	public Boolean getIsSIRestrictionAvail() {
		return isSIRestrictionAvail;
	}



	public Double getNonAllopathicOriginalAmt() {
		return nonAllopathicOriginalAmt;
	}



	public void setNonAllopathicOriginalAmt(Double nonAllopathicOriginalAmt) {
		this.nonAllopathicOriginalAmt = nonAllopathicOriginalAmt;
	}



	public Double getNonAllopathicUtilizedAmt() {
		return nonAllopathicUtilizedAmt;
	}



	public void setNonAllopathicUtilizedAmt(Double nonAllopathicUtilizedAmt) {
		this.nonAllopathicUtilizedAmt = nonAllopathicUtilizedAmt;
	}



	public void setIsSIRestrictionAvail(Boolean isSIRestrictionAvail) {
		this.isSIRestrictionAvail = isSIRestrictionAvail;
	}



	public Integer getSiRestrictionAmount() {
		return siRestrictionAmount;
	}



	public void setSiRestrictionAmount(Integer siRestrictionAmount) {
		this.siRestrictionAmount = siRestrictionAmount;
	}



	public Integer getBalanceSIAftHosp() {
		return balanceSIAftHosp;
	}



	public void setBalanceSIAftHosp(Integer balanceSIAftHosp) {
		this.balanceSIAftHosp = balanceSIAftHosp;
	}



	private Double amountConsidedAfterCoPay = 0d;
	private Double balanceSumInsuredAfterCoPay = 0d;
	public Double getPostHospPercentage() {
		return postHospPercentage;
	}



	public void setPostHospPercentage(Double postHospPercentage) {
		this.postHospPercentage = postHospPercentage;
	}



	public Double getPostHospclaimRestrictionAmount() {
		return postHospclaimRestrictionAmount;
	}



	public void setPostHospclaimRestrictionAmount(
			Double postHospclaimRestrictionAmount) {
		this.postHospclaimRestrictionAmount = postHospclaimRestrictionAmount;
	}



	public Boolean getIsEscalateReplyEnabled() {
		return isEscalateReplyEnabled;
	}



	public void setIsEscalateReplyEnabled(Boolean isEscalateReplyEnabled) {
		this.isEscalateReplyEnabled = isEscalateReplyEnabled;
	}



	private Double coPayValue = 0d;
	
	private List<DiagnosisProcedureTableDTO> diagnosisProcedureDtoList;
	
	public Integer getFvrCount() {
		return fvrCount;
	}



	public void setFvrCount(Integer fvrCount) {
		this.fvrCount = fvrCount;
	}



	private Boolean approvalCategory = false;
	private String specialityName;
	
    private String fileName;
	private String tokenName;	
	
	public Integer getPostHospAmt() {
		return postHospAmt;
	}



	public void setPostHospAmt(Integer postHospAmt) {
		this.postHospAmt = postHospAmt;
	}



	private Boolean isReBilling = false;
	private Boolean isReMedical = false;
	private Boolean isRemedicalRebilling = false;

	private Date billingDate;
	
	
	
	//pdf generation -- bill summary.
	
	private List<BillEntryDetailsDTO> billEntryDetailsDTO;
	
	private List<PreHospitalizationDTO> prehospitalizationDTO;
	
	private List<PreHospitalizationDTO> postHospitalizationDTO;
	
	
	//Added for scrc enhancement. -- starts
	
	private List<BillEntryDetailsDTO> roomRentNursingChargeList;

	private List<BillEntryDetailsDTO> policyLimitDList;
	
	private List<BillEntryDetailsDTO> policyLimitEList;
	
	private List<BillEntryDetailsDTO> policyLimitDandEList;
	
	private List<BillEntryDetailsDTO> ambulanceChargeList;
	
	private Boolean packageFlag;
	
	//Added for scrc enhancement. -- ends

	public Long getInvestigationKey() {
		return investigationKey;
	}



	public void setInvestigationKey(Long investigationKey) {
		this.investigationKey = investigationKey;
	}



	public Long getFvrKey() {
		return fvrKey;
	}



	public void setFvrKey(Long fvrKey) {
		this.fvrKey = fvrKey;
	}



	public Long getQueryKey() {
		return queryKey;
	}



	public void setQueryKey(Long queryKey) {
		this.queryKey = queryKey;
	}


	
	private Boolean isCancelPolicy = false;
	
	private Integer previousPreauthPayableAmount;
	
	private List<Double> productCopay = new ArrayList<Double>();
	
	private List<String> treatmentRemarksList = new ArrayList<String>();
	
	private Double downsizeTotalAppAmt;
	
	private Boolean isRepremedical = false;
	
	private Double balanceSI;
	
	public Boolean getIsReferToBilling() {
		return isReferToBilling;
	}



	public void setIsReferToBilling(Boolean isReferToBilling) {
		this.isReferToBilling = isReferToBilling;
	}



	public List<RoomRentMatchingDTO> getIcuRoomRentMappingDTOList() {
		return icuRoomRentMappingDTOList;
	}



	public void setIcuRoomRentMappingDTOList(
			List<RoomRentMatchingDTO> icuRoomRentMappingDTOList) {
		this.icuRoomRentMappingDTOList = icuRoomRentMappingDTOList;
	}



	public Boolean getIsICUoneMapping() {
		return isICUoneMapping;
	}



	public void setIsICUoneMapping(Boolean isICUoneMapping) {
		this.isICUoneMapping = isICUoneMapping;
	}



	private NewIntimationDto newIntimationDTO;
	
	private Double preauthClaimedAmountAsPerBill;
	
	private List<PreviousPreAuthTableDTO> previousPreauthTableDTO;
	
	private List<PreviousPreAuthTableDTO> previousPreauthTableDTOReportList;
	
	private Date preAuthRequestedDate;
	
	private String prevPreAuthApprovDetails;
	
	private List<ReconsiderRODRequestTableDTO> reconsiderationList;
	
	private List<Long> deletedClaimedAmountIds = new ArrayList<Long>();
	
	//private HumanTask humanTask;
	
	//private com.shaic.ims.bpm.claim.modelv2.HumanTask rodHumanTask;
	
	private ClaimDto claimDTO;
	
	private Date dateOfAdmission;
	
	private String dateOfAdmissionStr;
	
	private String reasonForAdmission;
	
	private String amountRequested;
	
	private String escalateRemarks;
	
	private int otherDeductionAmount;
	
	private int totalApprovedAmount;
	
	private String totalApprovedAmountInWords;


	
	public Boolean getIsOneMapping() {
		return isOneMapping;
	}

	public void setIsOneMapping(Boolean isOneMapping) {
		this.isOneMapping = isOneMapping;
	}



	private String amountConsidered;
	
	private String ambulanceAmountConsidered;
	
	private String otherInsurerAmount = "0";
	
	private String reverseAmountConsidered;
	
	private String initialAmountConsidered;
	
	private PolicyDto policyDto;
	
	private String referenceType;	
	
	private List<NonPayableReasonDto> nonPayableReasonListDto;
	
	private Boolean isGeoSame = Boolean.TRUE;
	
	
	private Integer superSurplusExceededSI;
	
	private Double oldNonAllopathicApprovedAmt;
	
	private String polCondtNo;
	
	private String preauthRejCondition;
	
	private SumInsuredBonusAlertDTO bonusAlertDTO;
	
	private Boolean isFAorBillingScreen = false;
	
	public Integer getNonAllopathicAvailAmt() {
		return nonAllopathicAvailAmt;
	}



	public void setNonAllopathicAvailAmt(Integer nonAllopathicAvailAmt) {
		this.nonAllopathicAvailAmt = nonAllopathicAvailAmt;
	}



	public Boolean getIsNonAllopathic() {
		return isNonAllopathic;
	}



	public void setIsNonAllopathic(Boolean isNonAllopathic) {
		this.isNonAllopathic = isNonAllopathic;
	}



	private String rodNumber;
	
	public Double getSettledAmount() {
		return settledAmount;
	}



	public Double getDeductibleAmount() {
		return deductibleAmount;
	}



	public void setSettledAmount(Double settledAmount) {
		this.settledAmount = settledAmount;
	}



	public void setDeductibleAmount(Double deductibleAmount) {
		this.deductibleAmount = deductibleAmount;
	}



	public String getPreviousRemarks() {
		return previousRemarks;
	}



	public String getPreviousReasonForReferring() {
		return previousReasonForReferring;
	}



	public void setPreviousRemarks(String previousRemarks) {
		this.previousRemarks = previousRemarks;
	}



	public void setPreviousReasonForReferring(String previousReasonForReferring) {
		this.previousReasonForReferring = previousReasonForReferring;
	}



	public Double getCurrentProvisionAmt() {
		return currentProvisionAmt;
	}



	public void setCurrentProvisionAmt(Double currentProvisionAmt) {
		this.currentProvisionAmt = currentProvisionAmt;
	}



	public Boolean getIsHospitalizationRODApproved() {
		return isHospitalizationRODApproved;
	}



	public void setIsHospitalizationRODApproved(Boolean isHospitalizationRODApproved) {
		this.isHospitalizationRODApproved = isHospitalizationRODApproved;
	}



	public Double getBillingApprovedAmount() {
		return billingApprovedAmount;
	}



	public void setBillingApprovedAmount(Double billingApprovedAmount) {
		this.billingApprovedAmount = billingApprovedAmount;
	}



	public Integer getHospitalizationAmount() {
		return hospitalizationAmount;
	}



	public void setHospitalizationAmount(Integer hospitalizationAmount) {
		this.hospitalizationAmount = hospitalizationAmount;
	}



	private Long bankId;
	
	public List<PreHospitalizationDTO> getPostHospitalizationDTO() {
		return postHospitalizationDTO;
	}



	public void setPostHospitalizationDTO(
			List<PreHospitalizationDTO> postHospitalizationDTO) {
		this.postHospitalizationDTO = postHospitalizationDTO;
	}



	public Boolean getIsReferToMedicalApprover() {
		return isReferToMedicalApprover;
	}



	public void setIsReferToMedicalApprover(Boolean isReferToMedicalApprover) {
		this.isReferToMedicalApprover = isReferToMedicalApprover;
	}



	public Boolean getIsPostHospitalization() {
		return isPostHospitalization;
	}



	public void setIsPostHospitalization(Boolean isPostHospitalization) {
		this.isPostHospitalization = isPostHospitalization;
	}

	private Long paymentModeId;
	
	public Boolean getIsPreviousROD() {
		return isPreviousROD;
	}



	public void setIsPreviousROD(Boolean isPreviousROD) {
		this.isPreviousROD = isPreviousROD;
	}



//	public Reimbursement getPreviousROD() {
//		return previousROD;
//	}
//
//
//
//	public void setPreviousROD(Reimbursement previousROD) {
//		this.previousROD = previousROD;
//	}



	private String payeeName;   
	
	private String payeeRelationship;
	
	private String payeeEmailId;
	
	private String panNumber; 
	
	private String payableAt;  
	
	private String accountNumber;
	
	private String nameAsPerBankAccount;
	
	private String accountType;
	
	private PreauthDataExtaractionDTO preauthDataExtractionDetails;
	
	private PreauthPreviousClaimsDTO preauthPreviousClaimsDetails;
	
	private PreauthMedicalDecisionDTO preauthMedicalDecisionDetails;
	
	private List<UpdateOtherClaimDetailDTO> updateOtherClaimDetailDTO;
	
	private List<VerificationAccountDeatilsTableDTO> VerificationAccountDeatilsTableDTO;
	
	
	
	public Boolean getIsCashlessType() {
		return isCashlessType;
	}



	public void setIsCashlessType(Boolean isCashlessType) {
		this.isCashlessType = isCashlessType;
	}



	private PreauthMedicalProcessingDTO preauthMedicalProcessingDetails;
	
	private ResidualAmountDTO residualAmountDTO;
	
	private HopitalizationCalulationDetailsDTO hospitalizationCalculationDTO;
	
	private PreHopitalizationDetailsDTO preHospitalizationCalculationDTO;
	
	private PostHopitalizationDetailsDTO postHospitalizationCalculationDTO;
	
	private OtherInsHospSettlementDetailsDTO otherInsHospSettlementCalcDTO;
	
	private OtherInsPreHospSettlementDetailsDTO otherInsPreHospSettlementCalcDTO;
	
	private OtherInsPostHospSettlementDetailsDTO otherInsPostHospSettlementCalcDTO;
	
	private ConsolidatedAmountDetailsDTO consolidatedAmtDTO;
	
	private List<SpecificProductDeductibleTableDTO> specificProductDeductibleDTO;
	
//	private TextField approveAmtTxt; 
	
	public String getInitialAmountConsidered() {
		return initialAmountConsidered;
	}



	public void setInitialAmountConsidered(String initialAmountConsidered) {
		this.initialAmountConsidered = initialAmountConsidered;
	}



	private List<PreviousClaimsTableDTO> previousClaimsList;
	
	private List<UploadDocumentDTO> uploadDocumentDTO;
	
//	private MedicalDecisionDTO medicalDecisionDetails;
	
	private CoordinatorDTO coordinatorDetails;
	
	private Map<String, Double> hospitalizationDetails;
	
	private SelectValue withdrawReason;
	
	private String doctorNote;
	
	private String medicalRemarks;
	
	//Set for user name and password issue.
	private String strUserName;
	
	
	private ImsUser userRole;
	
	private String amountConsideredForClaimAlert;
	//added for CR R1180
	private String withdrawInternalRemarks;
	
	//added for new product
	private SelectValue diagnosisHospitalCash;
	
	private String HospitalCashClaimedAmnt;
	
	//CR2019179
	private ClaimRequestButtonsForWizard claimRequestButtonsWizard;
	
	//CR2019202
	private String fraudAlertFlag;
	private String fraudAlertMsg;
	
	//CRCR2019214
	private Boolean approveBtnFlag = false;
	private String approveBtnMsg;

	private Boolean isCoInsurance = false;
	
	private List<CoInsuranceTableDTO> coInsuranceList;
	
	//added for installement payment status check at policy level
	private String policyInstalmentFlag;
	private String policyInstalmentMsg;
	
	//added for installement payment details status check at preauth level
	private String policyInstalmentDetailsFlag;
	private String policyInstalmentDetailsMsg;
	
	public Boolean getHospitalizaionFlag() {
		return hospitalizaionFlag;
	}



	public void setHospitalizaionFlag(Boolean hospitalizaionFlag) {
		this.hospitalizaionFlag = hospitalizaionFlag;
	}

	public Boolean getPreHospitalizaionFlag() {
		return preHospitalizaionFlag;
	}



	public void setPreHospitalizaionFlag(Boolean preHospitalizaionFlag) {
		this.preHospitalizaionFlag = preHospitalizaionFlag;
	}



	public Boolean getPostHospitalizaionFlag() {
		return postHospitalizaionFlag;
	}



	public void setPostHospitalizaionFlag(Boolean postHospitalizaionFlag) {
		this.postHospitalizaionFlag = postHospitalizaionFlag;
	}



	private String strPassword;
	
	private String investigatorName;
	
	private String investigationReviewRemarks;
	
	private Boolean reportReviewed = false;
	
	//Added for copay calculation
	private Double copay;
	
	private Double highestCopay = 0d;
	
	//Introducing the below variable , down size pre auth screen.
	private DownSizePreauthDataExtractionDTO downSizePreauthDataExtrationDetails;
	
	private String finalDiagnosis;
	
	private String icdCodeDesc;
	
	private Date billingApprovedDate;
	
	private List<DiagnosisDetailsTableDTO> deletedDiagnosis;
	private List<ProcedureDTO> deletedProcedure;
	
	private Double exceedSublimitAmt = 0d;
	
	private Double exceedPackageAmt = 0d;
	
	private Double exceedSumInsAmt = 0d;
	
	private Boolean isPolicyValidate;

	private String billingInternalRemarks;
	
	private String faInternalRemarks;
	
	private String scrcCopayPercentSelected;

	private Integer copay30PercentAmt;
	
	private Integer copay50PercentAmt;
	
	private boolean isClsProsAllowed;
	
	private String stpRemarks;
	
	private Boolean stpApplicable;
	
	private String hospIcdMappingAlertFlag;

	private String multiclaimAvailFlag;
	
	private String invPendingFlag;

	private String lastQueryRaiseDate;
	
	private String portalStatusVal;
	private String websiteStatusVal;
	
	private String topUPPolicyNumber;
	
	private String topUpProposerName;
	
	private String consolidatedFinalAppAmnt;

	private List<String> popupSIRestrication = new ArrayList<String>();

	
	private List<String> popupPrevClmInvst = new ArrayList<String>();
	
	private String accountPreference;
	
	private ViewSearchCriteriaTableDTO dto;

	//CR2019217
	private String icrAgentValue;
	
	private String smAgentValue;
	
	private String icacProcessFlag;

    private String icacProcessRemark;
    
    private Long icacStatusId;

    private LegalBillingDTO legalBillingDTO;
    
    private Boolean isHospitalExpenseCoverSelected = false;
    

    private Boolean isBalSIForSublimitCardicSelected = false;
    
    
    private ReceiptOfDocumentsDTO receiptOfDocumentsDTO;
    
    private String grievanceRepresentation;
    private Double networkHospitalDiscount = 0d;
    
    private String networkHospitalDiscountRemarksForAssessmentSheet;
    
    private String networkHospitalDiscountRemarks = "";
    
    private Integer networkHospitalDiscountNegative ;
	
	public Boolean getIsHospitalizationRepeat() {
		return isHospitalizationRepeat;
	}



	public void setIsHospitalizationRepeat(Boolean isHospitalizationRepeat) {
		this.isHospitalizationRepeat = isHospitalizationRepeat;
	}



	public PreauthDTO()
	{
		
		preauthDataExtractionDetails = new PreauthDataExtaractionDTO();
		coordinatorDetails = new CoordinatorDTO();
		preauthMedicalDecisionDetails = new PreauthMedicalDecisionDTO();
		preauthPreviousClaimsDetails = new PreauthPreviousClaimsDTO();
		preauthMedicalProcessingDetails = new PreauthMedicalProcessingDTO();
		previousClaimsList = new ArrayList<PreviousClaimsTableDTO>();
		hospitalizationDetails = new HashMap<String, Double>();
		residualAmountDTO = new ResidualAmountDTO();
		previousPreauthTableDTO = new ArrayList<PreviousPreAuthTableDTO>();
		insuredPedDetails = new ArrayList<InsuredPedDetails>();
		updateOtherClaimDetailDTO = new ArrayList<UpdateOtherClaimDetailDTO>();
		
		//Introducing the below method , down size pre auth screen.
		downSizePreauthDataExtrationDetails = new DownSizePreauthDataExtractionDTO();
		deletedDiagnosis = new ArrayList<DiagnosisDetailsTableDTO>();
		deletedProcedure = new ArrayList<ProcedureDTO>();
		reconsiderationList = new ArrayList<ReconsiderRODRequestTableDTO>();
		uploadDocumentDTO = new ArrayList<UploadDocumentDTO>();
		hospitalizationCalculationDTO = new HopitalizationCalulationDetailsDTO();
		preHospitalizationCalculationDTO = new PreHopitalizationDetailsDTO();
		postHospitalizationCalculationDTO = new PostHopitalizationDetailsDTO();
		otherInsHospSettlementCalcDTO = new OtherInsHospSettlementDetailsDTO();
		otherInsPreHospSettlementCalcDTO = new OtherInsPreHospSettlementDetailsDTO();
		otherInsPostHospSettlementCalcDTO = new OtherInsPostHospSettlementDetailsDTO();
		consolidatedAmtDTO = new ConsolidatedAmountDetailsDTO();
		roomRentMappingDTOList = new ArrayList<RoomRentMatchingDTO>();
		icuRoomRentMappingDTOList = new ArrayList<RoomRentMatchingDTO>();
		iccuRoomRentMappingDTOList = new ArrayList<RoomRentMatchingDTO>();
		diagnosisProcedureDtoList = new ArrayList<DiagnosisProcedureTableDTO>();
		rrcDTO = new RRCDTO();
		uploadDocDTO = new UploadDocumentDTO();
		dto = new ViewSearchCriteriaTableDTO();
		
//		medicalDecisionDetails = new MedicalDecisionDTO();
	}



	public String getReferenceType() {
		return referenceType;
	}


	public void setReferenceType(String referenceType) {
		this.referenceType = referenceType;
	}


	public Long getPolicyKey() {
		return policyKey;
	}

	public void setPolicyKey(Long policyKey) {
		this.policyKey = policyKey;
	}

	public Long getIntimationKey() {
		return intimationKey;
	}

	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
	}

	public String getEscalateRemarks() {
		return escalateRemarks;
	}



	public void setEscalateRemarks(String escalateRemarks) {
		this.escalateRemarks = escalateRemarks;
	}



	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}

	public CoordinatorDTO getCoordinatorDetails() {
		return coordinatorDetails;
	}

	public Long getBankId() {
		return bankId;
	}



	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}



	public Long getPaymentModeId() {
		return paymentModeId;
	}



	public void setPaymentModeId(Long paymentModeId) {
		this.paymentModeId = paymentModeId;
	}



	public String getPayeeName() {
		return payeeName;
	}



	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
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



	public String getAccountNumber() {
		return accountNumber;
	}



	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}



	public String getRodNumber() {
		return rodNumber;
	}



	public void setRodNumber(String rodNumber) {
		this.rodNumber = rodNumber;
	}



	public void setCoordinatorDetails(CoordinatorDTO coordinatorDetails) {
		this.coordinatorDetails = coordinatorDetails;
	}

	public Long getKey() {
		return key;
	}


	public List<ProcedureDTO> getDeletedProcedure() {
		return deletedProcedure;
	}



	public void setDeletedProcedure(List<ProcedureDTO> deletedProcedure) {
		this.deletedProcedure = deletedProcedure;
	}



	public List<RoomRentMatchingDTO> getRoomRentMappingDTOList() {
		return roomRentMappingDTOList;
	}



	public void setRoomRentMappingDTOList(
			List<RoomRentMatchingDTO> roomRentMappingDTOList) {
		this.roomRentMappingDTOList = roomRentMappingDTOList;
	}



	public PreHopitalizationDetailsDTO getPreHospitalizationCalculationDTO() {
		return preHospitalizationCalculationDTO;
	}



	public void setPreHospitalizationCalculationDTO(
			PreHopitalizationDetailsDTO preHospitalizationCalculationDTO) {
		this.preHospitalizationCalculationDTO = preHospitalizationCalculationDTO;
	}



	public PostHopitalizationDetailsDTO getPostHospitalizationCalculationDTO() {
		return postHospitalizationCalculationDTO;
	}



	public void setPostHospitalizationCalculationDTO(
			PostHopitalizationDetailsDTO postHospitalizationCalculationDTO) {
		this.postHospitalizationCalculationDTO = postHospitalizationCalculationDTO;
	}



	public List<UploadDocumentDTO> getUploadDocumentDTO() {
		return uploadDocumentDTO;
	}



	public void setUploadDocumentDTO(List<UploadDocumentDTO> uploadDocumentDTO) {
		this.uploadDocumentDTO = uploadDocumentDTO;
	}



	public void setKey(Long key) {
		this.key = key;
	}


	public PreauthDataExtaractionDTO getPreauthDataExtractionDetails() {
		return preauthDataExtractionDetails;
	}

	public void setPreauthDataExtractionDetails(
			PreauthDataExtaractionDTO preauthDataExtractionDetails) {
		this.preauthDataExtractionDetails = preauthDataExtractionDetails;
	}

	public PreauthMedicalDecisionDTO getPreauthMedicalDecisionDetails() {
		return preauthMedicalDecisionDetails;
	}

	public void setPreauthMedicalDecisionDetails(
			PreauthMedicalDecisionDTO preauthMedicalDecisionDetails) {
		this.preauthMedicalDecisionDetails = preauthMedicalDecisionDetails;
	}

	public PreauthPreviousClaimsDTO getPreauthPreviousClaimsDetails() {
		return preauthPreviousClaimsDetails;
	}

	public void setPreauthPreviousClaimsDetails(
			PreauthPreviousClaimsDTO preauthPreviousClaimsDetails) {
		this.preauthPreviousClaimsDetails = preauthPreviousClaimsDetails;
	}



	public PreauthMedicalProcessingDTO getPreMedicalPreauthMedicalDecisionDetails() {
		return preauthMedicalProcessingDetails;
	}



	public void setPreMedicalPreauthMedicalDecisionDetails(
			PreauthMedicalProcessingDTO preMedicalPreauthMedicalDecisionDetails) {
		this.preauthMedicalProcessingDetails = preMedicalPreauthMedicalDecisionDetails;
	}




	public PreauthMedicalProcessingDTO getPreauthMedicalProcessingDetails() {
		return preauthMedicalProcessingDetails;
	}



	public void setPreauthMedicalProcessingDetails(
			PreauthMedicalProcessingDTO preauthMedicalProcessingDetails) {
		this.preauthMedicalProcessingDetails = preauthMedicalProcessingDetails;
	}

	public Map<String, Double> getHospitalizationDetails() {
		return hospitalizationDetails;
	}
	



	public void setHospitalizationDetails(Map<String, Double> hospitalizationDetails) {
		this.hospitalizationDetails = hospitalizationDetails;
	}


	public List<PreviousClaimsTableDTO> getPreviousClaimsList() {
		return previousClaimsList;
	}

	public void setPreviousClaimsList(
			List<PreviousClaimsTableDTO> previousClaimsList) {
		this.previousClaimsList = previousClaimsList;
	}
	
	
	public DownSizePreauthDataExtractionDTO getDownSizePreauthDataExtractionDetails() {
		return downSizePreauthDataExtrationDetails;
	}

	

	public List<ReconsiderRODRequestTableDTO> getReconsiderationList() {
		return reconsiderationList;
	}



	public void setReconsiderationList(
			List<ReconsiderRODRequestTableDTO> reconsiderationList) {
		this.reconsiderationList = reconsiderationList;
	}



	public Long getStatusKey() {
		return statusKey;
	}



	public void setStatusKey(Long statusKey) {
		this.statusKey = statusKey;
	}



	public String getStatusValue() {
		return statusValue;
	}



	public void setStatusValue(String statusValue) {
		this.statusValue = statusValue;
	}



	public Long getStageKey() {
		return stageKey;
	}



	public HopitalizationCalulationDetailsDTO getHospitalizationCalculationDTO() {
		return hospitalizationCalculationDTO;
	}



	public void setHospitalizationCalculationDTO(
			HopitalizationCalulationDetailsDTO hospitalizationCalculationDTO) {
		this.hospitalizationCalculationDTO = hospitalizationCalculationDTO;
	}



	public void setStageKey(Long stageKey) {
		this.stageKey = stageKey;
	}




	public Long getHospitalKey() {
		return hospitalKey;
	}



	public void setHospitalKey(Long hospitalKey) {
		this.hospitalKey = hospitalKey;
	}



	public String getClaimNumber() {
		return claimNumber;
	}



	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}



	public PolicyDto getPolicyDto() {
		return policyDto;
	}



	public void setPolicyDto(PolicyDto policyDto) {
		this.policyDto = policyDto;
	}



	public String getAmountRequested() {
		return amountRequested;
	}



	public void setAmountRequested(String amountRequested) {
		this.amountRequested = amountRequested;
	}



	public String getAmountConsidered() {
		return amountConsidered;
	}



	public void setAmountConsidered(String amountConsidered) {
		this.amountConsidered = amountConsidered;
	}



	public Date getDateOfAdmission() {
		return dateOfAdmission;
	}



	public void setDateOfAdmission(Date dateOfAdmission) {
		this.dateOfAdmission = dateOfAdmission;
	}



	public String getReasonForAdmission() {
		return reasonForAdmission;
	}



	public void setReasonForAdmission(String reasonForAdmission) {
		this.reasonForAdmission = reasonForAdmission;
	}



	public NewIntimationDto getNewIntimationDTO() {
		return newIntimationDTO;
	}



	public void setNewIntimationDTO(NewIntimationDto newIntimationDTO) {
		this.newIntimationDTO = newIntimationDTO;
	}



	public ClaimDto getClaimDTO() {
		return claimDTO;
	}



	public void setClaimDTO(ClaimDto claimDTO) {
		this.claimDTO = claimDTO;
	}



	/*public HumanTask getHumanTask() {
		return humanTask;
	}



	public void setHumanTask(HumanTask humanTask) {
		this.humanTask = humanTask;
	}*/



	public String getStrUserName() {
		return strUserName;
	}



	public void setStrUserName(String strUserName) {
		this.strUserName = strUserName;
	}



	public String getStrPassword() {
		return strPassword;
	}



	public void setStrPassword(String strPassword) {
		this.strPassword = strPassword;
	}



	public SelectValue getWithdrawReason() {
		return withdrawReason;
	}



	public void setWithdrawReason(SelectValue withdrawReason) {
		this.withdrawReason = withdrawReason;
	}



	public String getDoctorNote() {
		return doctorNote;
	}



	public void setDoctorNote(String doctorNote) {
		this.doctorNote = doctorNote;
	}



	public String getMedicalRemarks() {
		return medicalRemarks;
	}



	public void setMedicalRemarks(String medicalRemarks) {
		this.medicalRemarks = medicalRemarks;
	}



	public ResidualAmountDTO getResidualAmountDTO() {
		return residualAmountDTO;
	}



	public void setResidualAmountDTO(ResidualAmountDTO residualAmountDTO) {
		this.residualAmountDTO = residualAmountDTO;
	}



	public DownSizePreauthDataExtractionDTO getDownSizePreauthDataExtrationDetails() {
		return downSizePreauthDataExtrationDetails;
	}



	public void setDownSizePreauthDataExtrationDetails(
			DownSizePreauthDataExtractionDTO downSizePreauthDataExtrationDetails) {
		this.downSizePreauthDataExtrationDetails = downSizePreauthDataExtrationDetails;
	}



	public Double getBalanceSI() {
		return balanceSI;
	}



	public void setBalanceSI(Double balanceSI) {
		this.balanceSI = balanceSI;
	}



	public Double getCopay() {
		return copay;
	}



	public void setCopay(Double copay) {
		this.copay = copay;
	}



	public Boolean getIsRepremedical() {
		return isRepremedical;
	}



	public void setIsRepremedical(Boolean isRepremedical) {
		this.isRepremedical = isRepremedical;
	}



	public String getProcessType() {
		return processType;
	}



	public void setProcessType(String processType) {
		this.processType = processType;
	}



	public Double getDownsizeTotalAppAmt() {
		return downsizeTotalAppAmt;
	}



	public void setDownsizeTotalAppAmt(Double downsizeTotalAppAmt) {
		this.downsizeTotalAppAmt = downsizeTotalAppAmt;
	}



	public List<String> getTreatmentRemarksList() {
		return treatmentRemarksList;
	}



	public void setTreatmentRemarksList(List<String> treatmentRemarksList) {
		this.treatmentRemarksList = treatmentRemarksList;
	}



	public List<Double> getProductCopay() {
		return productCopay;
	}



	public void setProductCopay(List<Double> productCopay) {
		this.productCopay = productCopay;
	}



	public List<Long> getDeletedClaimedAmountIds() {
		return deletedClaimedAmountIds;
	}



	public void setDeletedClaimedAmountIds(List<Long> deletedClaimedAmountIds) {
		this.deletedClaimedAmountIds = deletedClaimedAmountIds;
	}



	public List<PreviousPreAuthTableDTO> getPreviousPreauthTableDTO() {
		return previousPreauthTableDTO;
	}

	public void setPreviousPreauthTableDTO(
			List<PreviousPreAuthTableDTO> previousPreauthTableDTO) {
		this.previousPreauthTableDTO = previousPreauthTableDTO;
	}

	public Long getPreviousPreauthKey() {
		return previousPreauthKey;
	}



	public List<SpecificProductDeductibleTableDTO> getSpecificProductDeductibleDTO() {
		return specificProductDeductibleDTO;
	}



	public void setSpecificProductDeductibleDTO(
			List<SpecificProductDeductibleTableDTO> specificProductDeductibleDTO) {
		this.specificProductDeductibleDTO = specificProductDeductibleDTO;
	}



	public void setPreviousPreauthKey(Long previousPreauthKey) {
		this.previousPreauthKey = previousPreauthKey;
	}



	public Integer getPreviousPreauthPayableAmount() {
		return previousPreauthPayableAmount;
	}



	public void setPreviousPreauthPayableAmount(Integer previousPreauthPayableAmount) {
		this.previousPreauthPayableAmount = previousPreauthPayableAmount;
	}


	public Boolean getIsCancelPolicy() {
		return isCancelPolicy;
	}

	public void setIsCancelPolicy(Boolean isCancelPolicy) {
		this.isCancelPolicy = isCancelPolicy;
	}



	public List<DiagnosisDetailsTableDTO> getDeletedDiagnosis() {
		return deletedDiagnosis;
	}



	public void setDeletedDiagnosis(List<DiagnosisDetailsTableDTO> deletedDiagnosis) {
		this.deletedDiagnosis = deletedDiagnosis;
	}



	public String getInvestigatorName() {
		return investigatorName;
	}



	public String getHospitalisationValue() {
		return hospitalisationValue;
	}



	public void setHospitalisationValue(String hospitalisationValue) {
		this.hospitalisationValue = hospitalisationValue;
	}



	public String getPostHospitalisationValue() {
		return postHospitalisationValue;
	}



	public void setPostHospitalisationValue(String postHospitalisationValue) {
		this.postHospitalisationValue = postHospitalisationValue;
	}



	public String getPreHospitalisationValue() {
		return preHospitalisationValue;
	}



	public void setPreHospitalisationValue(String preHospitalisationValue) {
		this.preHospitalisationValue = preHospitalisationValue;
	}



	public void setInvestigatorName(String investigatorName) {
		this.investigatorName = investigatorName;
	}



	public String getInvestigationReviewRemarks() {
		return investigationReviewRemarks;
	}



	public void setInvestigationReviewRemarks(String investigationReviewRemarks) {
		this.investigationReviewRemarks = investigationReviewRemarks;
	}



	public Boolean getReportReviewed() {
		return reportReviewed;
	}



	public void setReportReviewed(Boolean reportReviewed) {
		this.reportReviewed = reportReviewed;
	}



	/*public com.shaic.ims.bpm.claim.modelv2.HumanTask getRodHumanTask() {
		return rodHumanTask;
	}*/



/*	public void setRodHumanTask(
			com.shaic.ims.bpm.claim.modelv2.HumanTask rodHumanTask) {
		this.rodHumanTask = rodHumanTask;
	}*/



	public Long getSpecialistKey() {
		return specialistKey;
	}



	public void setSpecialistKey(Long specialistKey) {
		this.specialistKey = specialistKey;
	}



	public String getCreatedBy() {
		return createdBy;
	}



	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}



	public Boolean getIsZonalReviewQuery() {
		return isZonalReviewQuery;
	}



	public void setIsZonalReviewQuery(Boolean isZonalReviewQuery) {
		this.isZonalReviewQuery = isZonalReviewQuery;
	}


	public ImsUser getUserRole() {
		return userRole;
	}

	public void setUserRole(ImsUser userRole) {
		this.userRole = userRole;
	}

	public Integer getInvestigationSize() {
		return investigationSize;
	}



	public void setInvestigationSize(Integer investigationSize) {
		this.investigationSize = investigationSize;
	}



	public Long getOldStatusKey() {
		return oldStatusKey;
	}



	public void setOldStatusKey(Long oldStatusKey) {
		this.oldStatusKey = oldStatusKey;
	}



	public String getDateOfAdmissionStr() {
		return dateOfAdmissionStr;
	}



	public void setDateOfAdmissionStr(String dateOfAdmissionStr) {
		this.dateOfAdmissionStr = dateOfAdmissionStr;
	}



	public Boolean getApprovalCategory() {
		return approvalCategory;
	}



	public void setApprovalCategory(Boolean approvalCategory) {
		this.approvalCategory = approvalCategory;
	}



	public String getSpecialityName() {
		return specialityName;
	}



	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}



	public String getFileName() {
		return fileName;
	}



	public void setFileName(String fileName) {
		this.fileName = fileName;
	}



	public String getTokenName() {
		return tokenName;
	}



	public void setTokenName(String tokenName) {
		this.tokenName = tokenName;
	}



	public Date getBillingDate() {
		return billingDate;
	}



	public void setBillingDate(Date billingDate) {
		this.billingDate = billingDate;
	}



	public Boolean getIsReBilling() {
		return isReBilling;
	}



	public void setIsReBilling(Boolean isReBilling) {
		this.isReBilling = isReBilling;
	}



	public Boolean getIsReMedical() {
		return isReMedical;
	}



	public void setIsReMedical(Boolean isReMedical) {
		this.isReMedical = isReMedical;
	}



	public List<BillEntryDetailsDTO> getBillEntryDetailsDTO() {
		return billEntryDetailsDTO;
	}



	public void setBillEntryDetailsDTO(List<BillEntryDetailsDTO> billEntryDetailsDTO) {
		
		if(billEntryDetailsDTO != null && ! billEntryDetailsDTO.isEmpty()){
			this.billEntryDetailsDTO = null;
		}
		this.billEntryDetailsDTO = billEntryDetailsDTO;
	}



	public List<PreHospitalizationDTO> getPrehospitalizationDTO() {
		return prehospitalizationDTO;
	}



	public void setPrehospitalizationDTO(
			List<PreHospitalizationDTO> prehospitalizationDTO) {
		this.prehospitalizationDTO = prehospitalizationDTO;
	}

	public Date getMedicalCompletedDate() {
		return medicalCompletedDate;
	}



	public void setMedicalCompletedDate(Date medicalCompletedDate) {
		this.medicalCompletedDate = medicalCompletedDate;
	}



	public Date getFinancialCompletedDate() {
		return financialCompletedDate;
	}



	public void setFinancialCompletedDate(Date financialCompletedDate) {
		this.financialCompletedDate = financialCompletedDate;
	}



	public Boolean getIsRemedicalRebilling() {
		return isRemedicalRebilling;
	}



	public void setIsRemedicalRebilling(Boolean isRemedicalRebilling) {
		this.isRemedicalRebilling = isRemedicalRebilling;
	}



	public Double getProrataPercentage() {
		return prorataPercentage;
	}



	public void setProrataPercentage(Double prorataPercentage) {
		this.prorataPercentage = prorataPercentage;
	}



	public String getPackageAvailableFlag() {
		return packageAvailableFlag;
	}



	public void setPackageAvailableFlag(String packageAvailableFlag) {
		this.packageAvailableFlag = packageAvailableFlag;
		
		if(null != this.packageAvailableFlag && ("y").equalsIgnoreCase(this.packageAvailableFlag) ){
			this.packageFlag = true;
		}
		else{
			this.packageFlag = false;
		}
	}



	public String getProrataDeductionFlag() {
		return prorataDeductionFlag;
	}



	public void setProrataDeductionFlag(String prorataDeductionFlag) {
		this.prorataDeductionFlag = prorataDeductionFlag;
	}



	public List<DiagnosisProcedureTableDTO> getDiagnosisProcedureDtoList() {
		return diagnosisProcedureDtoList;
	}
	
	public RRCDTO getRrcDTO() {
		return rrcDTO;
	}

	public void setDiagnosisProcedureDtoList(
			List<DiagnosisProcedureTableDTO> diagnosisProcedureDtoList) {
		this.diagnosisProcedureDtoList = diagnosisProcedureDtoList;
	}

	public Double getAmountConsidedAfterCoPay() {
		return amountConsidedAfterCoPay;
	}



	public void setAmountConsidedAfterCoPay(Double amountConsidedAfterCoPay) {
		this.amountConsidedAfterCoPay = amountConsidedAfterCoPay;
		
	}

	public Double getBalanceSumInsuredAfterCoPay() {
		return balanceSumInsuredAfterCoPay;
	}



	public void setBalanceSumInsuredAfterCoPay(Double balanceSumInsuredAfterCoPay) {
		this.balanceSumInsuredAfterCoPay = balanceSumInsuredAfterCoPay;
	}



	public Double getCoPayValue() {
		return coPayValue;
	}

	public void setCoPayValue(Double coPayValue) {
		this.coPayValue = coPayValue;
	}



	public Date getCreateDate() {
		return createDate;
	}



	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}



	public Integer getPreviousPostHospAmount() {
		return previousPostHospAmount;
	}
		

	public String getPrevPreAuthApprovDetails() {
		return prevPreAuthApprovDetails;
	}

	public void setPreviousPostHospAmount(Integer previousPostHospAmount) {
		this.previousPostHospAmount = previousPostHospAmount;
	}

	public void setPrevPreAuthApprovDetails(String prevPreAuthApprovDetails) {
		this.prevPreAuthApprovDetails = prevPreAuthApprovDetails;

	}



	public Boolean getIsRejectionROD() {
		return isRejectionROD;
	}



	public void setIsRejectionROD(Boolean isRejectionROD) {
		this.isRejectionROD = isRejectionROD;
	}



	public Boolean getIsQueryReceived() {
		return isQueryReceived;
	}



	public void setIsQueryReceived(Boolean isQueryReceived) {
		this.isQueryReceived = isQueryReceived;
	}



	public Boolean getAlertMessageOpened() {
		return alertMessageOpened;
	}



	public void setAlertMessageOpened(Boolean alertMessageOpened) {
		this.alertMessageOpened = alertMessageOpened;
	}

	public void setRrcDTO(RRCDTO rrcDTO) {
		this.rrcDTO = rrcDTO;
	}



	public UploadDocumentDTO getUploadDocDTO() {
		return uploadDocDTO;
	}



	public void setUploadDocDTO(UploadDocumentDTO uploadDocDTO) {
		this.uploadDocDTO = uploadDocDTO;
	}



	public Double getSublimitAndSIAmt() {
		return SublimitAndSIAmt;
	}



	public void setSublimitAndSIAmt(Double sublimitAndSIAmt) {
		SublimitAndSIAmt = sublimitAndSIAmt;
	}

	public String getProductBasedProRata() {
		return productBasedProRata;
	}
	public String getRemarks() {
		return remarks;

	}

	public void setProductBasedProRata(String productBasedProRata) {
		this.productBasedProRata = productBasedProRata;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;

	}

	public String getProductBasedPackage() {
		return productBasedPackage;
	}

	public Long getMedicalApproverKey() {
		return medicalApproverKey;

	}

	public void setProductBasedPackage(String productBasedPackage) {
		this.productBasedPackage = productBasedPackage;
	}
	public void setMedicalApproverKey(Long medicalApproverKey) {
		this.medicalApproverKey = medicalApproverKey;

	}

	public Boolean getIsWithDrawn() {
		return isWithDrawn;
	}

	public void setIsWithDrawn(Boolean isWithDrawn) {
		this.isWithDrawn = isWithDrawn;
	}

	public Boolean getIsDishonoured() {
		return isDishonoured;
	}

	public void setIsDishonoured(Boolean isDishonoured) {
		this.isDishonoured = isDishonoured;
	}

	public Boolean getIsPending() {
		return isPending;
	}

	public void setIsPending(Boolean isPending) {
		this.isPending = isPending;
	}

	public Double getCpuProvisionAmt() {
		return cpuProvisionAmt;
	}

	public void setCpuProvisionAmt(Double cpuProvisionAmt) {
		this.cpuProvisionAmt = cpuProvisionAmt;
	}

	public Float getEntitlmentNoOfDays() {
		return entitlmentNoOfDays;
	}
	
	public void setEntitlmentNoOfDays(Float entitlmentNoOfDays) {
		this.entitlmentNoOfDays = entitlmentNoOfDays;
	}

	public String getHospitalCode() {
		return hospitalCode;
	}

	public String getDocSource() {
		return docSource;

	}

	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	public void setDocSource(String docSource) {
		this.docSource = docSource;
	}

	public String getModifiedBy() {
		
		return this.modifiedBy;
	
	}
		
	public WeakHashMap getFilePathAndTypeMap() {
		return filePathAndTypeMap;

	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	
	public void setFilePathAndTypeMap(WeakHashMap filePathAndTypeMap) {
		this.filePathAndTypeMap = filePathAndTypeMap;

	}

	public Long getClaimRestrictionAmount() {
		return claimRestrictionAmount;
	}

	public void setClaimRestrictionAmount(Long claimRestrictionAmount) {
		this.claimRestrictionAmount = claimRestrictionAmount;
	}

	public Integer getSittingsAmount() {
		return sittingsAmount;
	}

	public List<ReconsiderRODRequestTableDTO> getReconsiderRodRequestList() {
		return reconsiderRodRequestList;
	}


	public void setSittingsAmount(Integer sittingsAmount) {
		this.sittingsAmount = sittingsAmount;
	}

	public void setReconsiderRodRequestList(
			List<ReconsiderRODRequestTableDTO> reconsiderRodRequestList) {
		this.reconsiderRodRequestList = reconsiderRodRequestList;

	}

	public Boolean getDialysisOpened() {
		return dialysisOpened;
    }

	public SelectValue getReconsiderationRequest() {
		return reconsiderationRequest;

	}

	public void setDialysisOpened(Boolean dialysisOpened) {
		this.dialysisOpened = dialysisOpened;
	}

	public void setReconsiderationRequest(SelectValue reconsiderationRequest) {
		this.reconsiderationRequest = reconsiderationRequest;
	}

	public Boolean getIsDialysis() {
		return isDialysis;
	}

	public SelectValue getReasonForReconsideration() {
		return reasonForReconsideration;

	}

	public void setIsDialysis(Boolean isDialysis) {
		this.isDialysis = isDialysis;
     }

	public void setReasonForReconsideration(SelectValue reasonForReconsideration) {
		this.reasonForReconsideration = reasonForReconsideration;

	}

	public Double getTotalClaimedAmt() {
		return totalClaimedAmt;
	}

	public void setTotalClaimedAmt(Double totalClaimedAmt) {
		this.totalClaimedAmt = totalClaimedAmt;
	}

	public Double getToatlNonPayableAmt() {
		return toatlNonPayableAmt;
	}

	public void setToatlNonPayableAmt(Double toatlNonPayableAmt) {
		this.toatlNonPayableAmt = toatlNonPayableAmt;
	}

	public Double getTotalApprovedAmt() {
		return totalApprovedAmt;
	}

	public void setTotalApprovedAmt(Double totalApprovedAmt) {
		this.totalApprovedAmt = totalApprovedAmt;
	}
	public Boolean getIsReverseAllocation() {
		return isReverseAllocation;
	}
	public Boolean getIsDefaultCopaySaved() {
		return isDefaultCopaySaved;
	}

	public void setIsDefaultCopaySaved(Boolean isDefaultCopaySaved) {
		this.isDefaultCopaySaved = isDefaultCopaySaved;
	}

	public void setIsReverseAllocation(Boolean isReverseAllocation) {
		this.isReverseAllocation = isReverseAllocation;
	}
	public String getReverseAmountConsidered() {
		return reverseAmountConsidered;
	}

	public void setReverseAmountConsidered(String reverseAmountConsidered) {
		this.reverseAmountConsidered = reverseAmountConsidered;
	}

	public Boolean getIsInvestigation() {
		return isInvestigation;
	}

	public void setIsInvestigation(Boolean isInvestigation) {
		this.isInvestigation = isInvestigation;
	}

	public Boolean getIsPedPending() {
		return isPedPending;
	}

	public void setIsPedPending(Boolean isPedPending) {
		this.isPedPending = isPedPending;
	}

	public Boolean getIsReconsiderationRequest() {
		return isReconsiderationRequest;
	}

	public void setIsReconsiderationRequest(Boolean isReconsiderationRequest) {
		this.isReconsiderationRequest = isReconsiderationRequest;
	}

	public String getComparisonResult() {
		return comparisonResult;
	}

	public void setComparisonResult(String comparisonResult) {
		this.comparisonResult = comparisonResult;
	}

	public Boolean getIsComparisonDone() {
		return isComparisonDone;
	}

	public void setIsComparisonDone(Boolean isComparisonDone) {
		this.isComparisonDone = isComparisonDone;
	}

	public Boolean getIsPopupMessageOpened() {
		return isPopupMessageOpened;
	}

	public void setIsPopupMessageOpened(Boolean isPopupMessageOpened) {
		this.isPopupMessageOpened = isPopupMessageOpened;
	}

	public Map<String, String> getPopupMap() {
		return popupMap;
	}

	public void setPopupMap(Map<String, String> popupMap) {
		this.popupMap = popupMap;
	}

	public Boolean getIsHospitalizationRejected() {
		return isHospitalizationRejected;
	}

	public void setIsHospitalizationRejected(Boolean isHospitalizationRejected) {
		this.isHospitalizationRejected = isHospitalizationRejected;
	}

	public Boolean getIsPostHospApplicable() {
		return isPostHospApplicable;
	}	

	public Double getDiffAmount() {
		return diffAmount;
	}

	public void setIsPostHospApplicable(Boolean isPostHospApplicable) {
		this.isPostHospApplicable = isPostHospApplicable;
	}

	public Boolean getIsPreHospApplicable() {
		return isPreHospApplicable;
	}

	public void setIsPreHospApplicable(Boolean isPreHospApplicable) {
		this.isPreHospApplicable = isPreHospApplicable;
	}

	public void setDiffAmount(Double diffAmount) {
		this.diffAmount = diffAmount;
	}

	public Double getAmountTotal() {
		return amountTotal;
	}

	public void setAmountTotal(Double amountTotal) {
		this.amountTotal = amountTotal;
	}

	public Double getNonpayableProdTotal() {
		return nonpayableProdTotal;
	}

	public void setNonpayableProdTotal(Double nonpayableProdTotal) {
		this.nonpayableProdTotal = nonpayableProdTotal;
	}

	public Double getNonpayableTotal() {
		return nonpayableTotal;
	}

	public void setNonpayableTotal(Double nonpayableTotal) {
		this.nonpayableTotal = nonpayableTotal;
	}

	public Double getPropDecutTotal() {
		return propDecutTotal;
	}

	public void setPropDecutTotal(Double propDecutTotal) {
		this.propDecutTotal = propDecutTotal;
	}

	public Double getReasonableDeducTotal() {
		return reasonableDeducTotal;
	}

	public void setReasonableDeducTotal(Double reasonableDeducTotal) {
		this.reasonableDeducTotal = reasonableDeducTotal;
	}

	public Double getDisallowanceTotal() {
		return disallowanceTotal;
	}

	public void setDisallowanceTotal(Double disallowanceTotal) {
		this.disallowanceTotal = disallowanceTotal;
	}

	public Double getNetPayableAmtTotal() {
		return netPayableAmtTotal;
	}

	public void setNetPayableAmtTotal(Double netPayableAmtTotal) {
		this.netPayableAmtTotal = netPayableAmtTotal;
	}

	public String getFinalDiagnosis() {
		return finalDiagnosis;
	}

	public void setFinalDiagnosis(String finalDiagnosis) {
		this.finalDiagnosis = finalDiagnosis;
	}

	public String getIcdCodeDesc() {
		return icdCodeDesc;
	}

	public void setIcdCodeDesc(String icdCodeDesc) {
		this.icdCodeDesc = icdCodeDesc;
	}

	public Date getBillingApprovedDate() {
		return billingApprovedDate;
	}

	public void setBillingApprovedDate(Date billingApprovedDate) {
		this.billingApprovedDate = billingApprovedDate;
	}

	public String getOtherInsurerAmount() {
		return otherInsurerAmount;
	}

	public void setOtherInsurerAmount(String otherInsurerAmount) {
		this.otherInsurerAmount = otherInsurerAmount;
	}

	public Integer getPreviousPreHospAmount() {
		return previousPreHospAmount;
	}

	public void setPreviousPreHospAmount(Integer previousPreHospAmount) {
		this.previousPreHospAmount = previousPreHospAmount;
	}

	public Double getFinalTotalApprovedAmount() {
		return finalTotalApprovedAmount;
	}

	public void setFinalTotalApprovedAmount(Double finalTotalApprovedAmount) {
		this.finalTotalApprovedAmount = finalTotalApprovedAmount;
	}

	public Boolean getRejectionAlertMessageOpened() {
		return rejectionAlertMessageOpened;
	}

	public void setRejectionAlertMessageOpened(Boolean rejectionAlertMessageOpened) {
		this.rejectionAlertMessageOpened = rejectionAlertMessageOpened;
	}

	public Boolean getMaternityFlag() {
		return maternityFlag;
	}

	public void setMaternityFlag(Boolean maternityFlag) {
		this.maternityFlag = maternityFlag;
	}
	
	public Long getDocumentReceivedFromId() {
		return documentReceivedFromId;
	}

	public void setDocumentReceivedFromId(Long documentReceivedFromId) {
		this.documentReceivedFromId = documentReceivedFromId;
	}

	public Map<String, String> getSuspiciousPopupMap() {
		return suspiciousPopupMap;
	}

	public void setSuspiciousPopupMap(Map<String, String> suspiciousPopupMap) {
		this.suspiciousPopupMap = suspiciousPopupMap;
	}

	public Boolean getIsRechargePopUpOpened() {
		return isRechargePopUpOpened;
	}

	public void setIsRechargePopUpOpened(Boolean isRechargePopUpOpened) {
		this.isRechargePopUpOpened = isRechargePopUpOpened;
	}

	public Date getSfxRegisteredQDate() {
		return sfxRegisteredQDate;
	}
		
	public Boolean getIsFinalEnhancement() {
		return isFinalEnhancement;
	}

	public void setSfxRegisteredQDate(Date sfxRegisteredQDate) {
		this.sfxRegisteredQDate = sfxRegisteredQDate;
	}
	
	public void setIsFinalEnhancement(Boolean isFinalEnhancement) {
		this.isFinalEnhancement = isFinalEnhancement;
	}

	public Date getSfxMatchedQDate() {
		return sfxMatchedQDate;
	}

	public void setSfxMatchedQDate(Date sfxMatchedQDate) {
		this.sfxMatchedQDate = sfxMatchedQDate;
	}

	public String getNetworkHospitalType() {
		return networkHospitalType;
	}

	public void setNetworkHospitalType(String networkHospitalType) {
		this.networkHospitalType = networkHospitalType;
	}

	public Boolean getIsBack() {
		return isBack;
	}
	public Double getRoomRentEligiblity() {
		return roomRentEligiblity;
	}

	public void setIsBack(Boolean isBack) {
		this.isBack = isBack;
	}
	
	public void setRoomRentEligiblity(Double roomRentEligiblity) {
		this.roomRentEligiblity = roomRentEligiblity;
	}

	public Boolean getIsPreMedicalForCoordinator() {
		return isPreMedicalForCoordinator;
	}

	public void setIsPreMedicalForCoordinator(Boolean isPreMedicalForCoordinator) {
		this.isPreMedicalForCoordinator = isPreMedicalForCoordinator;
	}

	public String getDocumentSource() {
		return documentSource;
	}

	public void setDocumentSource(String documentSource) {
		this.documentSource = documentSource;
	}

	public Boolean getIsNonAllopathicApplicable() {
		return isNonAllopathicApplicable;
	}

	public void setIsNonAllopathicApplicable(Boolean isNonAllopathicApplicable) {
		this.isNonAllopathicApplicable = isNonAllopathicApplicable;
	}

	public Double getClaimPaymentAmount() {
		return claimPaymentAmount;
	}

	public void setClaimPaymentAmount(Double claimPaymentAmount) {
		this.claimPaymentAmount = claimPaymentAmount;
	}

	public Boolean getIsDirectToBilling() {
		return isDirectToBilling;
	}
	public Boolean getIsByPass() {
		return isByPass;
	}

	public void setIsDirectToBilling(Boolean isDirectToBilling) {
		this.isDirectToBilling = isDirectToBilling;
	}

	public Boolean getIsDirectToFinancial() {
		return isDirectToFinancial;
	}

	public void setIsDirectToFinancial(Boolean isDirectToFinancial) {
		this.isDirectToFinancial = isDirectToFinancial;
	}
	public void setIsByPass(Boolean isByPass) {
		this.isByPass = isByPass;
	}

	public Double getHospitalDiscount() {
		return hospitalDiscount;
	}

	public void setHospitalDiscount(Double hospitalDiscount) {
		this.hospitalDiscount = hospitalDiscount;
	}

	public Double getDeductions() {
		return deductions;
	}

	public void setDeductions(Double deductions) {
		this.deductions = deductions;
	}

	public Double getPreHospitalDeduction() {
		return preHospitalDeduction;
	}

	public void setPreHospitalDeduction(Double preHospitalDeduction) {
		this.preHospitalDeduction = preHospitalDeduction;
	}

	public Double getPostHospitalDeduction() {
		return postHospitalDeduction;
	}

	public void setPostHospitalDeduction(Double postHospitalDeduction) {
		this.postHospitalDeduction = postHospitalDeduction;
	}

	public String getBillAssessmentDocFilePath() {
		return billAssessmentDocFilePath;
	}

	public void setBillAssessmentDocFilePath(String billAssessmentDocFilePath) {
		this.billAssessmentDocFilePath = billAssessmentDocFilePath;
	}

	public String getBillAssessmentDocType() {
		return billAssessmentDocType;
	}

	public void setBillAssessmentDocType(String billAssessmentDocType) {
		this.billAssessmentDocType = billAssessmentDocType;
	}

	public String getBillAssessmentDocSource() {
		return billAssessmentDocSource;
	}

	public void setBillAssessmentDocSource(String billAssessmentDocSource) {
		this.billAssessmentDocSource = billAssessmentDocSource;
	}

	public String getTotalDeductibleAmount() {
		return totalDeductibleAmount;
	}

	public void setTotalDeductibleAmount(String totalDeductibleAmount) {
		this.totalDeductibleAmount = totalDeductibleAmount;
	}

	public Boolean getAdmissionDatePopup() {
		return admissionDatePopup;
	}

	public void setAdmissionDatePopup(Boolean admissionDatePopup) {
		this.admissionDatePopup = admissionDatePopup;
	}

	public Boolean getIsReplyToFA() {
		return isReplyToFA;
	}

	public void setIsReplyToFA(Boolean isReplyToFA) {
		this.isReplyToFA = isReplyToFA;
	}

	public Long getInsuredKey() {
		return insuredKey;
	}

	public void setInsuredKey(Long insuredKey) {
		this.insuredKey = insuredKey;
	}

	public Long getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Long hospitalId) {
		this.hospitalId = hospitalId;
	}

	public Double getNonPayableTotalAmt() {
		return nonPayableTotalAmt;
	}

	public void setNonPayableTotalAmt(Double nonPayableTotalAmt) {
		this.nonPayableTotalAmt = nonPayableTotalAmt;
	}

	public Double getProportionateDeductionTotalAmt() {
		return proportionateDeductionTotalAmt;
	}

	public void setProportionateDeductionTotalAmt(
			Double proportionateDeductionTotalAmt) {
		this.proportionateDeductionTotalAmt = proportionateDeductionTotalAmt;
	}

	public Double getReasonableDeductionTotalAmt() {
		return reasonableDeductionTotalAmt;
	}

	public void setReasonableDeductionTotalAmt(Double reasonableDeductionTotalAmt) {
		this.reasonableDeductionTotalAmt = reasonableDeductionTotalAmt;
	}

	public Boolean getIsPEDInitiated() {
		return isPEDInitiated;
	}

	public void setIsPEDInitiated(Boolean isPEDInitiated) {
		this.isPEDInitiated = isPEDInitiated;
	}

	
	public Boolean getVerificationClicked() {
		return verificationClicked;
	}

	public void setVerificationClicked(Boolean verificationClicked) {
		this.verificationClicked = verificationClicked;
	}

	public Integer getTaskNumber() {
		return taskNumber;
	}

	public void setTaskNumber(Integer taskNumber) {
		this.taskNumber = taskNumber;
	}
	
	public List<DMSDocumentDetailsDTO> getDmsDocumentDTOList() {
		return dmsDocumentDTOList;
	}

	public void setDmsDocumentDTOList(List<DMSDocumentDetailsDTO> dmsDocumentDTOList) {
		this.dmsDocumentDTOList = dmsDocumentDTOList;
	}

	public String getHospitalDiscountRemarks() {
		return hospitalDiscountRemarks;
	}

	public void setHospitalDiscountRemarks(String hospitalDiscountRemarks) {
		this.hospitalDiscountRemarks = hospitalDiscountRemarks;
	}

	public String getDeductionRemarks() {
		return deductionRemarks;
	}

	public void setDeductionRemarks(String deductionRemarks) {
		this.deductionRemarks = deductionRemarks;
	}

	public Boolean getIsPEDInitiatedForBtn() {
		return isPEDInitiatedForBtn;
	}

	public void setIsPEDInitiatedForBtn(Boolean isPEDInitiatedForBtn) {
		this.isPEDInitiatedForBtn = isPEDInitiatedForBtn;
	}

	public Double getAmountConsAftCopayAmt() {
		return amountConsAftCopayAmt;
	}

	public void setAmountConsAftCopayAmt(Double amountConsAftCopayAmt) {
		this.amountConsAftCopayAmt = amountConsAftCopayAmt;
		if(this.amountConsAftCopayAmt != null) {
			setAmountConsidedAfterCoPay(this.amountConsAftCopayAmt);
		}
	}

	public Double getBalanceSIAftCopayAmt() {
		return balanceSIAftCopayAmt;
	}

	public void setBalanceSIAftCopayAmt(Double balanceSIAftCopayAmt) {
		this.balanceSIAftCopayAmt = balanceSIAftCopayAmt;
	}

	public Boolean getIsFirstPageSubmit() {
		return isFirstPageSubmit;
	}

	public void setIsFirstPageSubmit(Boolean isFirstPageSubmit) {
		this.isFirstPageSubmit = isFirstPageSubmit;
	}

	public Boolean getIsPaymentAvailable() {
		return isPaymentAvailable;
	}

	public void setIsPaymentAvailable(Boolean isPaymentAvailable) {
		this.isPaymentAvailable = isPaymentAvailable;
	}

	public Boolean getIsPaymentAvailableShown() {
		return isPaymentAvailableShown;
	}

	public void setIsPaymentAvailableShown(Boolean isPaymentAvailableShown) {
		this.isPaymentAvailableShown = isPaymentAvailableShown;
	}

	public List<InsuredPedDetails> getInsuredPedDetails() {
		return insuredPedDetails;
	}

	public void setInsuredPedDetails(List<InsuredPedDetails> insuredPedDetails) {
		this.insuredPedDetails = insuredPedDetails;
	}

	public Boolean getIsLumpsumApplicable() {
		return isLumpsumApplicable;
	}

	public void setIsLumpsumApplicable(Boolean isLumpsumApplicable) {
		this.isLumpsumApplicable = isLumpsumApplicable;
	}

	public Boolean getLumpSumAmountFlag() {
		return lumpSumAmountFlag;
	}

	public void setLumpSumAmountFlag(Boolean lumpSumAmountFlag) {
		this.lumpSumAmountFlag = lumpSumAmountFlag;
	}

	public Boolean getAddOnBenefitsHospitalCash() {
		return addOnBenefitsHospitalCash;
	}

	public void setAddOnBenefitsHospitalCash(Boolean addOnBenefitsHospitalCash) {
		this.addOnBenefitsHospitalCash = addOnBenefitsHospitalCash;
	}

	public Boolean getIsHospitalCashApplicable() {
		return isHospitalCashApplicable;
	}

	public void setIsHospitalCashApplicable(Boolean isHospitalCashApplicable) {
		this.isHospitalCashApplicable = isHospitalCashApplicable;
	}

	public Boolean getIsPatientCareApplicable() {
		return isPatientCareApplicable;
	}

	public void setIsPatientCareApplicable(Boolean isPatientCareApplicable) {
		this.isPatientCareApplicable = isPatientCareApplicable;
	}

	public Boolean getAddOnBenefitsPatientCare() {
		return addOnBenefitsPatientCare;
	}

	public void setAddOnBenefitsPatientCare(Boolean addOnBenefitsPatientCare) {
		this.addOnBenefitsPatientCare = addOnBenefitsPatientCare;
	}

	public List<DocumentDetailsDTO> getDocumentDetailsDTOList() {
		return documentDetailsDTOList;
	}

	public void setDocumentDetailsDTOList(
			List<DocumentDetailsDTO> documentDetailsDTOList) {
		this.documentDetailsDTOList = documentDetailsDTOList;
	}

	public Boolean getIsPedWatchList() {
		return isPedWatchList;
	}

	public void setIsPedWatchList(Boolean isPedWatchList) {
		this.isPedWatchList = isPedWatchList;
	}

	public Double getFAApprovedAmount() {
		return FAApprovedAmount;
	}

	public void setFAApprovedAmount(Double fAApprovedAmount) {
		FAApprovedAmount = fAApprovedAmount;
	}

	public Double getHospDiscountAmount() {
		return hospDiscountAmount;
	}

	public void setHospDiscountAmount(Double hospDiscountAmount) {
		this.hospDiscountAmount = hospDiscountAmount;
	}

	public Double getPayableToHospAmt() {
		return payableToHospAmt;
	}

	public void setPayableToHospAmt(Double payableToHospAmt) {
		this.payableToHospAmt = payableToHospAmt;
	}

	public Double getPayableToInsAmt() {
		return payableToInsAmt;
	}

	public void setPayableToInsAmt(Double payableToInsAmt) {
		this.payableToInsAmt = payableToInsAmt;
	}

	public Object getSearchFormDTO() {
		return searchFormDTO;
	}

	public void setSearchFormDTO(Object searchFormDTO) {
		this.searchFormDTO = searchFormDTO;
	}

	public Boolean getIsConsiderForPaymentNo() {
		return isConsiderForPaymentNo;
	}

	public void setIsConsiderForPaymentNo(Boolean isConsiderForPaymentNo) {
		this.isConsiderForPaymentNo = isConsiderForPaymentNo;
	}

	public OtherInsHospSettlementDetailsDTO getOtherInsHospSettlementCalcDTO() {
		return otherInsHospSettlementCalcDTO;
	}
		
	public Boolean getIsAmbulanceApplicable() {
		return isAmbulanceApplicable;
	}

	public void setOtherInsHospSettlementCalcDTO(
			OtherInsHospSettlementDetailsDTO otherInsHospSettlementCalcDTO) {
		this.otherInsHospSettlementCalcDTO = otherInsHospSettlementCalcDTO;
	}
	
	public void setIsAmbulanceApplicable(Boolean isAmbulanceApplicable) {
		this.isAmbulanceApplicable = isAmbulanceApplicable;
	}

	public OtherInsPreHospSettlementDetailsDTO getOtherInsPreHospSettlementCalcDTO() {
		return otherInsPreHospSettlementCalcDTO;
    }
	
	public void setOtherInsPreHospSettlementCalcDTO(
			OtherInsPreHospSettlementDetailsDTO otherInsPreHospSettlementCalcDTO) {
		this.otherInsPreHospSettlementCalcDTO = otherInsPreHospSettlementCalcDTO;
	}
	
	public void setAmbulanceLimitAmount(Double ambulanceLimitAmount) {
		this.ambulanceLimitAmount = ambulanceLimitAmount;
	}

	public Double getAmbulanceLimitAmount() {
		return ambulanceLimitAmount;
	}


	public OtherInsPostHospSettlementDetailsDTO getOtherInsPostHospSettlementCalcDTO() {
		return otherInsPostHospSettlementCalcDTO;
	}



	public void setOtherInsPostHospSettlementCalcDTO(
			OtherInsPostHospSettlementDetailsDTO otherInsPostHospSettlementCalcDTO) {
		this.otherInsPostHospSettlementCalcDTO = otherInsPostHospSettlementCalcDTO;
	}



	public Integer getAddOnBenefitsAmount() {
		return addOnBenefitsAmount;
	}



	public void setAddOnBenefitsAmount(Integer addOnBenefitsAmount) {
		this.addOnBenefitsAmount = addOnBenefitsAmount;
	}



	public ConsolidatedAmountDetailsDTO getConsolidatedAmtDTO() {
		return consolidatedAmtDTO;
	}



	public void setConsolidatedAmtDTO(
			ConsolidatedAmountDetailsDTO consolidatedAmtDTO) {
		this.consolidatedAmtDTO = consolidatedAmtDTO;
	}



	public Double getRevisedProvisionAmount() {
		return revisedProvisionAmount;
	}



	public void setRevisedProvisionAmount(Double revisedProvisionAmount) {
		this.revisedProvisionAmount = revisedProvisionAmount;
	}

	
	public Double getTotalConsolidatedAmt() {
		return totalConsolidatedAmt;
	}


	public void setTotalConsolidatedAmt(Double totalConsolidatedAmt) {
		this.totalConsolidatedAmt = totalConsolidatedAmt;
	}



	public Double getUniqueDeductedAmount() {
		return uniqueDeductedAmount;
	}



	public void setUniqueDeductedAmount(Double uniqueDeductedAmount) {
		this.uniqueDeductedAmount = uniqueDeductedAmount;
	}


	public String getAmbulanceAmountConsidered() {
		return ambulanceAmountConsidered;
	}

	public Boolean getIsRejectReconsidered() {
		return isRejectReconsidered;

	}


	public void setAmbulanceAmountConsidered(String ambulanceAmountConsidered) {
		this.ambulanceAmountConsidered = ambulanceAmountConsidered;
	}

	public void setIsRejectReconsidered(Boolean isRejectReconsidered) {
		this.isRejectReconsidered = isRejectReconsidered;
	}



	public Boolean getIsReverseAllocationHappened() {
		return isReverseAllocationHappened;
	}



	public void setIsReverseAllocationHappened(Boolean isReverseAllocationHappened) {
		this.isReverseAllocationHappened = isReverseAllocationHappened;
	}



	public Integer getHospAmountInBiling() {
		return hospAmountInBiling;
	}



	public void setHospAmountInBiling(Integer hospAmountInBiling) {
		this.hospAmountInBiling = hospAmountInBiling;
	}



	public Integer getPreHospAmtInBilling() {
		return preHospAmtInBilling;
	}



	public void setPreHospAmtInBilling(Integer preHospAmtInBilling) {
		this.preHospAmtInBilling = preHospAmtInBilling;
	}



	public Integer getPostHospAmtInBilling() {
		return postHospAmtInBilling;
	}



	public void setPostHospAmtInBilling(Integer postHospAmtInBilling) {
		this.postHospAmtInBilling = postHospAmtInBilling;
	}



	public Integer getHospAmountAlreadyPaid() {
		return hospAmountAlreadyPaid;
	}



	public void setHospAmountAlreadyPaid(Integer hospAmountAlreadyPaid) {
		this.hospAmountAlreadyPaid = hospAmountAlreadyPaid;
	}



	public Integer getPreHospAmtAlreadyPaid() {
		return preHospAmtAlreadyPaid;
	}



	public void setPreHospAmtAlreadyPaid(Integer preHospAmtAlreadyPaid) {
		this.preHospAmtAlreadyPaid = preHospAmtAlreadyPaid;
	}



	public Integer getPostHospAmtAlreadyPaid() {
		return postHospAmtAlreadyPaid;
	}



	public void setPostHospAmtAlreadyPaid(Integer postHospAmtAlreadyPaid) {
		this.postHospAmtAlreadyPaid = postHospAmtAlreadyPaid;
	}



	public Double getRodTotalClaimedAmount() {
		return rodTotalClaimedAmount;
	}

	public List<BillEntryDetailsDTO> getRoomRentNursingChargeList() {
		return roomRentNursingChargeList;

	}


	public void setRodTotalClaimedAmount(Double rodTotalClaimedAmount) {
		this.rodTotalClaimedAmount = rodTotalClaimedAmount;
    }

	public void setRoomRentNursingChargeList(
			List<BillEntryDetailsDTO> roomRentNursingChargeList) {
		this.roomRentNursingChargeList = roomRentNursingChargeList;
	}



	public List<BillEntryDetailsDTO> getPolicyLimitDList() {
		return policyLimitDList;
	}



	public void setPolicyLimitDList(List<BillEntryDetailsDTO> policyLimitDList) {
		this.policyLimitDList = policyLimitDList;
	}



	public List<BillEntryDetailsDTO> getPolicyLimitEList() {
		return policyLimitEList;
	}



	public void setPolicyLimitEList(List<BillEntryDetailsDTO> policyLimitEList) {
		this.policyLimitEList = policyLimitEList;
	}



	public List<BillEntryDetailsDTO> getAmbulanceChargeList() {
		return ambulanceChargeList;
	}



	public void setAmbulanceChargeList(List<BillEntryDetailsDTO> ambulanceChargeList) {
		this.ambulanceChargeList = ambulanceChargeList;
	}



	public List<BillEntryDetailsDTO> getHospitalizationTabSummaryList() {
		return hospitalizationTabSummaryList;
	}



	public void setHospitalizationTabSummaryList(
			List<BillEntryDetailsDTO> hospitalizationTabSummaryList) {
		this.hospitalizationTabSummaryList = hospitalizationTabSummaryList;
	}



	public List<PreHospitalizationDTO> getPreHospitalizationTabSummaryList() {
		return preHospitalizationTabSummaryList;
	}



	public void setPreHospitalizationTabSummaryList(
			List<PreHospitalizationDTO> preHospitalizationTabSummaryList) {
		this.preHospitalizationTabSummaryList = preHospitalizationTabSummaryList;
	}



	public List<PreHospitalizationDTO> getPostHospitalizationTabSummaryList() {
		return postHospitalizationTabSummaryList;
	}



	public void setPostHospitalizationTabSummaryList(
			List<PreHospitalizationDTO> postHospitalizationTabSummaryList) {
		this.postHospitalizationTabSummaryList = postHospitalizationTabSummaryList;
	}



	public List<PreHospitalizationDTO> getPrePostHospitalizationList() {
		return prePostHospitalizationList;
	}



	public void setPrePostHospitalizationList(
			List<PreHospitalizationDTO> prePostHospitalizationList) {
		this.prePostHospitalizationList = prePostHospitalizationList;
	}



	


	public Double getSumInsuredFromView() {
		return sumInsuredFromView;
	}



	public void setSumInsuredFromView(Double sumInsuredFromView) {
		this.sumInsuredFromView = sumInsuredFromView;
	}



	public List<BillEntryDetailsDTO> getPolicyLimitDandEList() {
		return policyLimitDandEList;
	}



	public void setPolicyLimitDandEList(
			List<BillEntryDetailsDTO> policyLimitDandEList) {
		this.policyLimitDandEList = policyLimitDandEList;
	}



	public Boolean getPackageFlag() {
		
		if(null != this.packageAvailableFlag && ("y").equalsIgnoreCase(this.packageAvailableFlag) ){
			this.packageFlag = true;
		}
		else{
			this.packageFlag = false;
		}
		return packageFlag;
	}



	public void setPackageFlag(Boolean packageFlag) {
		this.packageFlag = packageFlag;
	}



	public String getCopayRemarks() {
		return copayRemarks;
	}



	public void setCopayRemarks(String copayRemarks) {
		this.copayRemarks = copayRemarks;
	}



	public Double getExceeds25percentAmt() {
		return exceeds25percentAmt;
	}



	public void setExceeds25percentAmt(Double exceeds25percentAmt) {
		this.exceeds25percentAmt = exceeds25percentAmt;
	}



	public Double getExceeds50percentAmt() {
		return exceeds50percentAmt;
	}



	public void setExceeds50percentAmt(Double exceeds50percentAmt) {
		this.exceeds50percentAmt = exceeds50percentAmt;
	}



	public Double getExceeds75percentAmt() {
		return exceeds75percentAmt;
	}



	public void setExceeds75percentAmt(Double exceeds75percentAmt) {
		this.exceeds75percentAmt = exceeds75percentAmt;
	}

	public Boolean getIsDefaultCopay() {
		return isDefaultCopay;
}
	public Boolean getIsReferTOFLP() {
		return isReferTOFLP;

	}

	public void setIsDefaultCopay(Boolean isDefaultCopay) {
		this.isDefaultCopay = isDefaultCopay;
	}

	public Double getTotalHospitalizationDeductionsForAssesment() {
		return totalHospitalizationDeductionsForAssesment;

	}



	public String getDefaultCopayStr() {
		return defaultCopayStr;
	}



	public void setDefaultCopayStr(String defaultCopayStr) {
		this.defaultCopayStr = defaultCopayStr;
	}
	public void setIsReferTOFLP(Boolean isReferTOFLP) {
		this.isReferTOFLP = isReferTOFLP;
	}
	public void setTotalHospitalizationDeductionsForAssesment(
			Double totalHospitalizationDeductionsForAssesment) {
		this.totalHospitalizationDeductionsForAssesment = totalHospitalizationDeductionsForAssesment;

	}
	public Boolean getShouldShowPostHospAlert() {
		return shouldShowPostHospAlert;
	}
	
	public Boolean getIsHospitalDiscountApplicable() {
		return isHospitalDiscountApplicable;
	}

	public void setShouldShowPostHospAlert(Boolean shouldShowPostHospAlert) {
		this.shouldShowPostHospAlert = shouldShowPostHospAlert;
	}

	public void setIsHospitalDiscountApplicable(Boolean isHospitalDiscountApplicable) {
		this.isHospitalDiscountApplicable = isHospitalDiscountApplicable;
	}

	public Boolean getIsAutoRestorationDone() {
		return isAutoRestorationDone;
}

	public void setIsAutoRestorationDone(Boolean isAutoRestorationDone) {
		this.isAutoRestorationDone = isAutoRestorationDone;
	}

	public Date getZonalDate() {
		return zonalDate;
	}

	public Double getUniquePremiumAmount() {
		return uniquePremiumAmount;
	}

	public void setZonalDate(Date zonalDate) {
		this.zonalDate = zonalDate;
	}
	
	public void setUniquePremiumAmount(Double uniquePremiumAmount) {
		this.uniquePremiumAmount = uniquePremiumAmount;
	}

	public String getSkipZmrFlag() {
		return skipZmrFlag;
	}


	public void setSkipZmrFlag(String skipZmrFlag) {
		this.skipZmrFlag = skipZmrFlag;
	}

	public Long getClaimCount() {
		return claimCount;
	}

	public void setClaimCount(Long claimCount) {
		this.claimCount = claimCount;
	}

	public Double getBariatricAvailableAmount() {
		return bariatricAvailableAmount;
	}

	public void setBariatricAvailableAmount(Double bariatricAvailableAmount) {
		this.bariatricAvailableAmount = bariatricAvailableAmount;
}


	public Double getExceedSublimitAmt() {
		return exceedSublimitAmt;
	}



	public void setExceedSublimitAmt(Double exceedSublimitAmt) {
		this.exceedSublimitAmt = exceedSublimitAmt;
	}



	public Double getExceedPackageAmt() {
		return exceedPackageAmt;
	}


	public Boolean getShouldDisableSection() {
		return shouldDisableSection;
	}

	public void setShouldDisableSection(Boolean shouldDisableSection) {
		this.shouldDisableSection = shouldDisableSection;
	}

	public Boolean getIsFirstStepRejection() {
		return isFirstStepRejection;
	}
	public void setExceedPackageAmt(Double exceedPackageAmt) {
		this.exceedPackageAmt = exceedPackageAmt;
	}

	public InitiateInvestigationDTO getInitInvDto() {
		return initInvDto;
	}



	public void setInitInvDto(InitiateInvestigationDTO initInvDto) {
		this.initInvDto = initInvDto;
	}



	public Double getExceedSumInsAmt() {
		return exceedSumInsAmt;
	}



	public void setExceedSumInsAmt(Double exceedSumInsAmt) {
		this.exceedSumInsAmt = exceedSumInsAmt;
	}

	public Double getSiRestrictionAmt() {
		return siRestrictionAmt;
	}

	public void setSiRestrictionAmt(Double siRestrictionAmt) {
		this.siRestrictionAmt = siRestrictionAmt;
	}



	public Boolean getIs64VBChequeStatusAlert() {
		return is64VBChequeStatusAlert;
	}



	public void setIs64VBChequeStatusAlert(Boolean is64vbChequeStatusAlert) {
		is64VBChequeStatusAlert = is64vbChequeStatusAlert;
	}



	public Double getSublimitTotalAvailableAmt() {
		return sublimitTotalAvailableAmt;
	}



	public void setSublimitTotalAvailableAmt(Double sublimitTotalAvailableAmt) {
		this.sublimitTotalAvailableAmt = sublimitTotalAvailableAmt;
	}



	public Double getPaTotalClaimedAmnt() {
		return paTotalClaimedAmnt;
	}



	public void setPaTotalClaimedAmnt(Double paTotalClaimedAmnt) {
		this.paTotalClaimedAmnt = paTotalClaimedAmnt;
	}



	public Double getPaTotalDisAllowance() {
		return paTotalDisAllowance;
	}



	public void setPaTotalDisAllowance(Double paTotalDisAllowance) {
		this.paTotalDisAllowance = paTotalDisAllowance;
	}



	public Double getPaTotalApprovedAmnt() {
		return paTotalApprovedAmnt;
	}



	public void setPaTotalApprovedAmnt(Double paTotalApprovedAmnt) {
		this.paTotalApprovedAmnt = paTotalApprovedAmnt;
	}



	public Double getPaAlreadyPaidAmnt() {
		return paAlreadyPaidAmnt;
	}



	public void setPaAlreadyPaidAmnt(Double paAlreadyPaidAmnt) {
		this.paAlreadyPaidAmnt = paAlreadyPaidAmnt;
	}



	public String getIsPaymentQuery() {
		return isPaymentQuery;
	}



	public void setIsPaymentQuery(String isPaymentQuery) {
		this.isPaymentQuery = isPaymentQuery;
	}



	public Boolean getIsReferToClaimApproval() {
		return isReferToClaimApproval;
	}



	public void setIsReferToClaimApproval(Boolean isReferToClaimApproval) {
		this.isReferToClaimApproval = isReferToClaimApproval;
	}
	public void setIsFirstStepRejection(Boolean isFirstStepRejection) {
		this.isFirstStepRejection = isFirstStepRejection;
}



	public Boolean getAlertMessageForCopay() {
		return alertMessageForCopay;
	}



	public void setAlertMessageForCopay(Boolean alertMessageForCopay) {
		this.alertMessageForCopay = alertMessageForCopay;
	}




	public Object getDbOutArray() {
		return dbOutArray;
	}
	public Boolean getShouldDetectPremium() {
		return shouldDetectPremium;

	}

	public void setDbOutArray(Object dbOutArray) {
		this.dbOutArray = dbOutArray;
	}
	public void setShouldDetectPremium(Boolean shouldDetectPremium) {
		this.shouldDetectPremium = shouldDetectPremium;

	}

	public Boolean getIsPreauthAutoAllocationQ() {
		return isPreauthAutoAllocationQ;
}
	public Boolean getIsReleased() {
		return isReleased;
	}
	public String getCoversBenefitsValue() {
		return coversBenefitsValue;

	}




	public void setIsPreauthAutoAllocationQ(Boolean isPreauthAutoAllocationQ) {
		this.isPreauthAutoAllocationQ = isPreauthAutoAllocationQ;
}
	public void setIsReleased(Boolean isReleased) {
		this.isReleased = isReleased;
	}
	public void setCoversBenefitsValue(String coversBenefitsValue) {
		this.coversBenefitsValue = coversBenefitsValue;

	}


	public Integer getOtherInsurerClaimedAmount() {
		return otherInsurerClaimedAmount;
	}

	public void setOtherInsurerClaimedAmount(Integer otherInsurerClaimedAmount) {
		this.otherInsurerClaimedAmount = otherInsurerClaimedAmount;
	}

	public Integer getOtherInsurerDeductionAmt() {
		return otherInsurerDeductionAmt;
	}

	public void setOtherInsurerDeductionAmt(Integer otherInsurerDeductionAmt) {
		this.otherInsurerDeductionAmt = otherInsurerDeductionAmt;
	}

	public Integer getOtherInsurerAdmissibleAmt() {
		return otherInsurerAdmissibleAmt;
	}

	public void setOtherInsurerAdmissibleAmt(Integer otherInsurerAdmissibleAmt) {
		this.otherInsurerAdmissibleAmt = otherInsurerAdmissibleAmt;
	}

	public List<UpdateOtherClaimDetailDTO> getUpdateOtherClaimDetailDTO() {
		return updateOtherClaimDetailDTO;
	}

	public void setUpdateOtherClaimDetailDTO(
			List<UpdateOtherClaimDetailDTO> updateOtherClaimDetailDTO) {
		this.updateOtherClaimDetailDTO = updateOtherClaimDetailDTO;
	}

	public Long getBenefitId() {
		return benefitId;
	}

	public void setBenefitId(Long benefitId) {
		this.benefitId = benefitId;
	}

	public String getPaCoversSelected() {
		return paCoversSelected;
	}

	public void setPaCoversSelected(String paCoversSelected) {
		this.paCoversSelected = paCoversSelected;
	}

	public Long getInvestigationStage() {
		return investigationStage;
	}

	public void setInvestigationStage(Long investigationStage) {
		this.investigationStage = investigationStage;
	}

	public Long getInvestigationStatus() {
		return investigationStatus;
	}

	public void setInvestigationStatus(Long investigationStatus) {
		this.investigationStatus = investigationStatus;
	}

	public List<DocAcknowledgement> getDocumentAckList() {
		return documentAckList;
	}

	public void setDocumentAckList(List<DocAcknowledgement> documentAckList) {
		this.documentAckList = documentAckList;
	}

	public Long getUnNamedKey() {
		return unNamedKey;
	}

	public void setUnNamedKey(Long unNamedKey) {
		this.unNamedKey = unNamedKey;
	}

	public Map<String, String> getNonPreferredPopupMap() {
		return nonPreferredPopupMap;
	}

	public void setNonPreferredPopupMap(Map<String, String> nonPreferredPopupMap) {
		this.nonPreferredPopupMap = nonPreferredPopupMap;
	}

	public Boolean getIsScheduleClicked() {
		return isScheduleClicked;
	}

	public void setIsScheduleClicked(Boolean isScheduleClicked) {
		this.isScheduleClicked = isScheduleClicked;
	}

	public Boolean getOtherBenefitsFlag() {
		return otherBenefitsFlag;
	}

	public void setOtherBenefitsFlag(Boolean otherBenefitsFlag) {
		this.otherBenefitsFlag = otherBenefitsFlag;
	}

	public Boolean getEmergencyMedicalEvaluation() {
		return emergencyMedicalEvaluation;
	}

	public void setEmergencyMedicalEvaluation(Boolean emergencyMedicalEvaluation) {
		this.emergencyMedicalEvaluation = emergencyMedicalEvaluation;
	}

	public Boolean getCompassionateTravel() {
		return compassionateTravel;
	}

	public void setCompassionateTravel(Boolean compassionateTravel) {
		this.compassionateTravel = compassionateTravel;
	}

	public Boolean getIsAboveLimitCorpAdvise() {
		return isAboveLimitCorpAdvise;
	}

	public void setIsAboveLimitCorpAdvise(Boolean isAboveLimitCorpAdvise) {
		this.isAboveLimitCorpAdvise = isAboveLimitCorpAdvise;
	}

	public Boolean getIsAutoAllocationCorpUser() {
		return isAutoAllocationCorpUser;
	}

	public void setIsAutoAllocationCorpUser(Boolean isAutoAllocationCorpUser) {
		this.isAutoAllocationCorpUser = isAutoAllocationCorpUser;
	}

	public Boolean getIsAutoAllocationCPUUser() {
		return isAutoAllocationCPUUser;
	}

	public void setIsAutoAllocationCPUUser(Boolean isAutoAllocationCPUUser) {
		this.isAutoAllocationCPUUser = isAutoAllocationCPUUser;
	}

	public Boolean getIsAboveLimitCorpProcess() {
		return isAboveLimitCorpProcess;
	}

	public void setIsAboveLimitCorpProcess(Boolean isAboveLimitCorpProcess) {
		this.isAboveLimitCorpProcess = isAboveLimitCorpProcess;
	}
	
	public String getIntimationNumber() {
		return intimationNumber;
	}

	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}

	public Boolean getRepatriationOfMortalRemains() {
		return repatriationOfMortalRemains;
	}

	public void setRepatriationOfMortalRemains(Boolean repatriationOfMortalRemains) {
		this.repatriationOfMortalRemains = repatriationOfMortalRemains;
	}

	public Boolean getPreferredNetworkHospital() {
		return preferredNetworkHospital;
	}

	public void setPreferredNetworkHospital(Boolean preferredNetworkHospital) {
		this.preferredNetworkHospital = preferredNetworkHospital;
	}

	public Boolean getSharedAccomodation() {
		return sharedAccomodation;
	}

	public void setSharedAccomodation(Boolean sharedAccomodation) {
		this.sharedAccomodation = sharedAccomodation;
	}

	public Boolean getIsEditBillClassification() {
		return isEditBillClassification;
	}

	public void setIsEditBillClassification(Boolean isEditBillClassification) {
		this.isEditBillClassification = isEditBillClassification;
	}

	public Integer getNoOfdaysLimit() {
		return noOfdaysLimit;
	}

	public void setNoOfdaysLimit(Integer noOfdaysLimit) {
		this.noOfdaysLimit = noOfdaysLimit;
	}

	public String getUserLimitAmount() {
		return userLimitAmount;
	}

	public void setUserLimitAmount(String userLimitAmount) {
		this.userLimitAmount = userLimitAmount;
	}

	public String getMakerVerified() {
		return makerVerified;
	}

	public void setMakerVerified(String makerVerified) {
		this.makerVerified = makerVerified;
	}

	public Long getModeOfReceipt() {
		return modeOfReceipt;
	}

	public void setModeOfReceipt(Long modeOfReceipt) {
		this.modeOfReceipt = modeOfReceipt;
	}

	public String getCheckerVerified() {
		return checkerVerified;
	}

	public void setCheckerVerified(String checkerVerified) {
		this.checkerVerified = checkerVerified;
	}
	/*public BillEntryDetailsDTO getBillEntryDetailsDTO() {
		return billEntryDetailsDTO;
	}

	public void setBillEntryDetailsDTO(BillEntryDetailsDTO billEntryDetailsDTO) {
		this.billEntryDetailsDTO = billEntryDetailsDTO;
	}*/

	public String getClmPrcsInstruction() {
		return clmPrcsInstruction;
	}

	public void setClmPrcsInstruction(String clmPrcsInstruction) {
		this.clmPrcsInstruction = clmPrcsInstruction;
	}

	public String getIsSuspicious() {
		return isSuspicious;
	}

	public void setIsSuspicious(String isSuspicious) {
		this.isSuspicious = isSuspicious;
	}
	
	@Override
    public boolean equals(Object obj) {
        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        } else if (getKey() == null) {
            return obj == this;
        } else {
            return getKey().equals(((PreauthDTO) obj).getKey());
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


	public Boolean getIsViewCashlessDocClicked() {
		return isViewCashlessDocClicked;
	}

	public void setIsViewCashlessDocClicked(Boolean isViewCashlessDocClicked) {
		this.isViewCashlessDocClicked = isViewCashlessDocClicked;
	}

	public String getAlreadySettlementAmt() {
		return alreadySettlementAmt;
	}

	public void setAlreadySettlementAmt(String alreadySettlementAmt) {
		this.alreadySettlementAmt = alreadySettlementAmt;
	}

	public Double getAdmissableAmntOfCurrentClaim() {
		return admissableAmntOfCurrentClaim;
	}

	public void setAdmissableAmntOfCurrentClaim(Double admissableAmntOfCurrentClaim) {
		this.admissableAmntOfCurrentClaim = admissableAmntOfCurrentClaim;
	}

	public Double getAmntAfterDefinedLimit() {
		return amntAfterDefinedLimit;
	}

	public void setAmntAfterDefinedLimit(Double amntAfterDefinedLimit) {
		this.amntAfterDefinedLimit = amntAfterDefinedLimit;
	}
	
	public String getGstNumber() {
		return gstNumber;
	}

	public void setGstNumber(String gstNumber) {
		this.gstNumber = gstNumber;
	}

	public String getGstState() {
		return gstState;
	}

	public void setGstState(String gstState) {
		this.gstState = gstState;
	}

	public String getArnNumber() {
		return arnNumber;
	}

	public void setArnNumber(String arnNumber) {
		this.arnNumber = arnNumber;
	}

	public Map<Integer, Object> getHospitalizationDetailsVal() {
		return hospitalizationDetailsVal;
	}

	public void setHospitalizationDetailsVal(
			Map<Integer, Object> hospitalizationDetailsVal) {
		this.hospitalizationDetailsVal = hospitalizationDetailsVal;
	}
		

	public String getPayModeChangeReason() {
		return payModeChangeReason;
	}

	public void setPayModeChangeReason(String payModeChangeReason) {
		this.payModeChangeReason = payModeChangeReason;
	}

	public Boolean getIsPolicyValidate() {
		return isPolicyValidate;
	}

	public void setIsPolicyValidate(Boolean isPolicyValidate) {
		this.isPolicyValidate = isPolicyValidate;
	}
	
	public List<PreviousAccountDetailsDTO> getPreviousAccntDetailsList() {
		return previousAccntDetailsList;
	}

	public void setPreviousAccntDetailsList(
			List<PreviousAccountDetailsDTO> previousAccntDetailsList) {
		this.previousAccntDetailsList = previousAccntDetailsList;
	}
	public Map<Long, SublimitFunObject> getSublimitFunMap() {
		return sublimitFunMap;
	}

	public void setSublimitFunMap(Map<Long, SublimitFunObject> sublimitFunMap) {
		this.sublimitFunMap = sublimitFunMap;
	}

	public Boolean getIsAlertSublimtChanges() {
		return isAlertSublimtChanges;
	}

	public void setIsAlertSublimtChanges(Boolean isAlertSublimtChanges) {
		this.isAlertSublimtChanges = isAlertSublimtChanges;
	}

	public String getUpdatePaymentDtlsFlag() {
		return updatePaymentDtlsFlag;
	}

	public void setUpdatePaymentDtlsFlag(String updatePaymentDtlsFlag) {
		this.updatePaymentDtlsFlag = updatePaymentDtlsFlag;
	}

	public Boolean getAlertMessageHoldOpened() {
		return alertMessageHoldOpened;
	}

	public void setAlertMessageHoldOpened(Boolean alertMessageHoldOpened) {
		this.alertMessageHoldOpened = alertMessageHoldOpened;
	}	
	

	public Boolean getIsFvrPending() {
		return isFvrPending;
	}

	public void setIsFvrPending(Boolean isFvrPending) {
		this.isFvrPending = isFvrPending;
	}

	public Boolean getIsInvsPending() {
		return isInvsPending;
	}

	public void setIsInvsPending(Boolean isInvsPending) {
		this.isInvsPending = isInvsPending;
	}

	public Boolean getIsQueryPending() {
		return isQueryPending;
	}

	public void setIsQueryPending(Boolean isQueryPending) {
		this.isQueryPending = isQueryPending;
	}	

	public Boolean getIsZUAQueryAvailable() {
		return isZUAQueryAvailable;
	}

	public void setIsZUAQueryAvailable(Boolean isZUAQueryAvailable) {
		this.isZUAQueryAvailable = isZUAQueryAvailable;
	}

	public Boolean getIsPreviousPreauthWithdraw() {
		return isPreviousPreauthWithdraw;
	}

	public void setIsPreviousPreauthWithdraw(Boolean isPreviousPreauthWithdraw) {
		this.isPreviousPreauthWithdraw = isPreviousPreauthWithdraw;
	}
	
	public Boolean getIsWaitingDaysLessThan30() {
		return isWaitingDaysLessThan30;
	}

	public void setIsWaitingDaysLessThan30(Boolean isWaitingDaysLessThan30) {
		this.isWaitingDaysLessThan30 = isWaitingDaysLessThan30;
	}

	public Boolean getIsWaitingDaysLessThan180() {
		return isWaitingDaysLessThan180;
	}

	public void setIsWaitingDaysLessThan180(Boolean isWaitingDaysLessThan180) {
		this.isWaitingDaysLessThan180 = isWaitingDaysLessThan180;
	}

	public Integer getPreviousRODNoOfDays() {
		return previousRODNoOfDays;
	}

	public void setPreviousRODNoOfDays(Integer previousRODNoOfDays) {
		this.previousRODNoOfDays = previousRODNoOfDays;
	}
	public Boolean getIsFvrInitiate() {
		return isFvrInitiate;
	}

	public void setIsFvrInitiate(Boolean isFvrInitiate) {
		this.isFvrInitiate = isFvrInitiate;
	}

	public Boolean getIsFvrClicked() {
		return isFvrClicked;
	}

	public void setIsFvrClicked(Boolean isFvrClicked) {
		this.isFvrClicked = isFvrClicked;
	}

	public Boolean getIsFvrNotRequiredAndSelected() {
		return isFvrNotRequiredAndSelected;
	}

	public void setIsFvrNotRequiredAndSelected(Boolean isFvrNotRequiredAndSelected) {
		this.isFvrNotRequiredAndSelected = isFvrNotRequiredAndSelected;
	}

	public Boolean getIsChangeInsumInsuredAlert() {
		return isChangeInsumInsuredAlert;
	}

	public void setIsChangeInsumInsuredAlert(Boolean isChangeInsumInsuredAlert) {
		this.isChangeInsumInsuredAlert = isChangeInsumInsuredAlert;
	}
	public List<PreExistingDisease> getApprovedPedDetails() {
		return approvedPedDetails;
	}

	public void setApprovedPedDetails(List<PreExistingDisease> approvedPedDetails) {
		this.approvedPedDetails = approvedPedDetails;
	}

	public String getCrmFlagged() {
		return crmFlagged;
	}

	public void setCrmFlagged(String crmFlagged) {
		this.crmFlagged = crmFlagged;
	}

	public String getCrcFlaggedReason() {
		return crcFlaggedReason;
	}

	public void setCrcFlaggedReason(String crcFlaggedReason) {
		this.crcFlaggedReason = crcFlaggedReason;
	}

	public String getCrcFlaggedRemark() {
		return crcFlaggedRemark;
	}

	public void setCrcFlaggedRemark(String crcFlaggedRemark) {
		this.crcFlaggedRemark = crcFlaggedRemark;
	}
	
	public Boolean getIsParallelInvFvrQuery() {
		return isParallelInvFvrQuery;
	}

	public void setIsParallelInvFvrQuery(Boolean isParallelInvFvrQuery) {
		this.isParallelInvFvrQuery = isParallelInvFvrQuery;
	}

	public Boolean getIsFvrInitiatedInZMR() {
		return isFvrInitiatedInZMR;
	}

	public void setIsFvrInitiatedInZMR(Boolean isFvrInitiatedInZMR) {
		this.isFvrInitiatedInZMR = isFvrInitiatedInZMR;
	}

	public Boolean getIsInvsInitiatedInZMR() {
		return isInvsInitiatedInZMR;
	}

	public void setIsInvsInitiatedInZMR(Boolean isInvsInitiatedInZMR) {
		this.isInvsInitiatedInZMR = isInvsInitiatedInZMR;
	}

	public String getProceedWithoutReport() {
		return proceedWithoutReport;
	}

	public void setProceedWithoutReport(String proceedWithoutReport) {
		this.proceedWithoutReport = proceedWithoutReport;
	}
	
	public String getBillingInternalRemarks() {
		return billingInternalRemarks;
	}
	public void setBillingInternalRemarks(String billingInternalRemarks) {
		this.billingInternalRemarks = billingInternalRemarks;
	}
	
	public String getFaInternalRemarks() {
		return faInternalRemarks;
	}
	public void setFaInternalRemarks(String faInternalRemarks) {
		this.faInternalRemarks = faInternalRemarks;
	}

	public Long getRodParentKey() {
		return rodParentKey;
	}

	public void setRodParentKey(Long rodParentKey) {
		this.rodParentKey = rodParentKey;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
	public String getFVRPendingRsn() {
		return FVRPendingRsn;
	}

	public void setFVRPendingRsn(String fVRPendingRsn) {
		FVRPendingRsn = fVRPendingRsn;
	}	

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public Long getParallelStatusKey() {
		return parallelStatusKey;
	}

	public void setParallelStatusKey(Long parallelStatusKey) {
		this.parallelStatusKey = parallelStatusKey;
	}
	public List<NonPayableReasonDto> getNonPayableReasonListDto() {
		return nonPayableReasonListDto;
	}

	public void setNonPayableReasonListDto(
			List<NonPayableReasonDto> nonPayableReasonListDto) {
		this.nonPayableReasonListDto = nonPayableReasonListDto;
	}
	
	public Boolean getIsReferToBillEntry() {
		return isReferToBillEntry;
	}

	public void setIsReferToBillEntry(Boolean isReferToBillEntry) {
		this.isReferToBillEntry = isReferToBillEntry;
	}

	public String getScrcCopayPercentSelected() {
		return scrcCopayPercentSelected;
	}

	public void setScrcCopayPercentSelected(String scrcCopayPercentSelected) {
		this.scrcCopayPercentSelected = scrcCopayPercentSelected;
	}

	public Integer getCopay30PercentAmt() {
		return copay30PercentAmt;
	}

	public void setCopay30PercentAmt(Integer copay30PercentAmt) {
		this.copay30PercentAmt = copay30PercentAmt;
	}

	public Integer getCopay50PercentAmt() {
		return copay50PercentAmt;
	}

	public void setCopay50PercentAmt(Integer copay50PercentAmt) {
		this.copay50PercentAmt = copay50PercentAmt;
	}
	public Boolean getIsPaayasPolicy() {
		return isPaayasPolicy;
	}

	public void setIsPaayasPolicy(Boolean isPaayasPolicy) {
		this.isPaayasPolicy = isPaayasPolicy;
	}

	public String getFvrAlertFlag() {
		return fvrAlertFlag;
	}

	public void setFvrAlertFlag(String fvrAlertFlag) {
		this.fvrAlertFlag = fvrAlertFlag;
	}

	public Boolean getIsFVRAlertOpened() {
		return isFVRAlertOpened;
	}

	public void setIsFVRAlertOpened(Boolean isFVRAlertOpened) {
		this.isFVRAlertOpened = isFVRAlertOpened;
	}

	public String getFvrNotRequiredRemarks() {
		return fvrNotRequiredRemarks;
	}

	public void setFvrNotRequiredRemarks(String fvrNotRequiredRemarks) {
		this.fvrNotRequiredRemarks = fvrNotRequiredRemarks;
	}

	public Long getCoPayTypeId() {
		return coPayTypeId;
	}

	public void setCoPayTypeId(Long coPayTypeId) {
		this.coPayTypeId = coPayTypeId;
	}
	
	public Boolean getIsDefaultDeductable() {
		return isDefaultDeductable;
	}

	public void setIsDefaultDeductable(Boolean isDefaultDeductable) {
		this.isDefaultDeductable = isDefaultDeductable;
	}

	public Boolean getIsFvrButtonDisabled() {
		return isFvrButtonDisabled;
	}

	public void setIsFvrButtonDisabled(Boolean isFvrButtonDisabled) {
		this.isFvrButtonDisabled = isFvrButtonDisabled;
	}

	public String getHospitalDiscountRemarksForAssessmentSheet() {
		return hospitalDiscountRemarksForAssessmentSheet;
	}

	public void setHospitalDiscountRemarksForAssessmentSheet(
			String hospitalDiscountRemarksForAssessmentSheet) {
		this.hospitalDiscountRemarksForAssessmentSheet = hospitalDiscountRemarksForAssessmentSheet;
	}

	public String getDeductionRemarksForAssessmentSheet() {
		return deductionRemarksForAssessmentSheet;
	}

	public void setDeductionRemarksForAssessmentSheet(
			String deductionRemarksForAssessmentSheet) {
		this.deductionRemarksForAssessmentSheet = deductionRemarksForAssessmentSheet;
	}	
	
	public Boolean getIsInvsRaised() {
		return isInvsRaised;
	}

	public void setIsInvsRaised(Boolean isInvsRaised) {
		this.isInvsRaised = isInvsRaised;
	}

	public Boolean getScoringClicked() {
		return scoringClicked;
	}

	public void setScoringClicked(Boolean scoringClicked) {
		this.scoringClicked = scoringClicked;
	}

	public Boolean getScoringVisibility() {
		return scoringVisibility;
	}

	public void setScoringVisibility(Boolean scoringVisibility) {
		this.scoringVisibility = scoringVisibility;
	}

	public String getStageName() {
		return stageName;
	}

	public void setStageName(String stageName) {
		this.stageName = stageName;
	}

	public Boolean getIsInvsPendingInFA() {
		return isInvsPendingInFA;
	}

	public void setIsInvsPendingInFA(Boolean isInvsPendingInFA) {
		this.isInvsPendingInFA = isInvsPendingInFA;
	}
	
	public Long getPreauthHoldStatusKey() {
		return preauthHoldStatusKey;
	}

	public void setPreauthHoldStatusKey(Long preauthHoldStatusKey) {
		this.preauthHoldStatusKey = preauthHoldStatusKey;
	}

	public String getSublimitUpdateRemarks() {
		return sublimitUpdateRemarks;
	}

	public void setSublimitUpdateRemarks(String sublimitUpdateRemarks) {
		this.sublimitUpdateRemarks = sublimitUpdateRemarks;
	}
	
	public List<SelectValue> getDuplicateInsuredList() {
		return duplicateInsuredList;
	}

	public void setDuplicateInsuredList(List<SelectValue> duplicateInsuredList) {
		this.duplicateInsuredList = duplicateInsuredList;
	}

	public List<SelectValue> getSuperSurplusAlertList() {
		return superSurplusAlertList;
	}

	public void setSuperSurplusAlertList(List<SelectValue> superSurplusAlertList) {
		this.superSurplusAlertList = superSurplusAlertList;
	}
	public Boolean getIsGeoSame() {
		return isGeoSame;
	}

	public void setIsGeoSame(Boolean isGeoSame) {
		this.isGeoSame = isGeoSame;
	}
	
	public boolean isInsuredDeleted() {
		return isInsuredDeleted;
	}

	public void setInsuredDeleted(boolean isInsuredDeleted) {
		this.isInsuredDeleted = isInsuredDeleted;
	}

	public String getHospitalScoreFlag() {
		return hospitalScoreFlag;
	}

	public void setHospitalScoreFlag(String hospitalScoreFlag) {
		this.hospitalScoreFlag = hospitalScoreFlag;
	}

	public List<EsclateToRawTableDTO> getDeletedEsclateRawList() {
		return deletedEsclateRawList;
	}

	public void setDeletedEsclateRawList(
			List<EsclateToRawTableDTO> deletedEsclateRawList) {
		this.deletedEsclateRawList = deletedEsclateRawList;
	}

	public List<EsclateToRawTableDTO> getEsclateToRawList() {
		return EsclateToRawList;
	}

	public void setEsclateToRawList(List<EsclateToRawTableDTO> esclateToRawList) {
		EsclateToRawList = esclateToRawList;
	}

	public Long getEsclateStageKey() {
		return esclateStageKey;
	}

	public void setEsclateStageKey(Long esclateStageKey) {
		this.esclateStageKey = esclateStageKey;
	}

	public Boolean getIsDateOfAdmissionAlertOpened() {
		return isDateOfAdmissionAlertOpened;
	}

	public void setIsDateOfAdmissionAlertOpened(Boolean isDateOfAdmissionAlertOpened) {
		this.isDateOfAdmissionAlertOpened = isDateOfAdmissionAlertOpened;
	}
	
	public boolean isDirectToAssignInv() {
		return directToAssignInv;
	}

	public void setDirectToAssignInv(boolean directToAssignInv) {
		this.directToAssignInv = directToAssignInv;
	}

	public Double getPreauthClaimedAmountAsPerBill() {
		return preauthClaimedAmountAsPerBill;
	}

	public void setPreauthClaimedAmountAsPerBill(
			Double preauthClaimedAmountAsPerBill) {
		this.preauthClaimedAmountAsPerBill = preauthClaimedAmountAsPerBill;
	}

	
	public String getAutoAllocCancelRemarks() {
		return autoAllocCancelRemarks;
	}

	public void setAutoAllocCancelRemarks(String autoAllocCancelRemarks) {
		this.autoAllocCancelRemarks = autoAllocCancelRemarks;
	}

	public boolean isClsProsAllowed() {
		return isClsProsAllowed;
	}

	public void setClsProsAllowed(boolean isClsProsAllowed) {
		this.isClsProsAllowed = isClsProsAllowed;
	}

	public Boolean getNegotiationagreed() {
		return negotiationagreed;
	}

	public void setNegotiationagreed(Boolean negotiationagreed) {
		this.negotiationagreed = negotiationagreed;
	}

	public String getNegotiationagreedFlagValue() {
		return negotiationagreedFlagValue;
	}

	public void setNegotiationagreedFlagValue(String negotiationagreedFlagValue) {
		this.negotiationagreedFlagValue = negotiationagreedFlagValue;
	}

	public String getRegotiateRemarks() {
		return regotiateRemarks;
	}

	public void setRegotiateRemarks(String regotiateRemarks) {
		this.regotiateRemarks = regotiateRemarks;
	}

	public String getAgreedWith() {
		return agreedWith;
	}

	public void setAgreedWith(String agreedWith) {
		this.agreedWith = agreedWith;
	}

	public String getStpRemarks() {
		return stpRemarks;
	}

	public void setStpRemarks(String stpRemarks) {
		this.stpRemarks = stpRemarks;
	}
	public String getTopUpPolicyAlertFlag() {
		return topUpPolicyAlertFlag;
	}

	public void setTopUpPolicyAlertFlag(String topUpPolicyAlertFlag) {
		this.topUpPolicyAlertFlag = topUpPolicyAlertFlag;
	}

	public String getTopUpPolicyAlertMessage() {
		return topUpPolicyAlertMessage;
	}

	public void setTopUpPolicyAlertMessage(String topUpPolicyAlertMessage) {
		this.topUpPolicyAlertMessage = topUpPolicyAlertMessage;
	}
	public boolean isMultiplePEDAvailableNotDeleted() {
		return multiplePEDAvailableNotDeleted;
	}

	public void setMultiplePEDAvailableNotDeleted(
			boolean multiplePEDAvailableNotDeleted) {
		this.multiplePEDAvailableNotDeleted = multiplePEDAvailableNotDeleted;
	}
		
	public PedDetailsTableDTO getPedDetailsDTO() {
		return pedDetailsDTO;
	}

	public void setPedDetailsDTO(PedDetailsTableDTO pedDetailsDTO) {
		this.pedDetailsDTO = pedDetailsDTO;
	}

	public Boolean getIsTataPolicy() {
		return isTataPolicy;
	}

	public void setIsTataPolicy(Boolean isTataPolicy) {
		this.isTataPolicy = isTataPolicy;
	}
	public Boolean getNegotiationMade() {
		return negotiationMade;
	}

	public void setNegotiationMade(Boolean negotiationMade) {
		this.negotiationMade = negotiationMade;
	}
	
	public boolean isAllowedToNxt() {
		return isAllowedToNxt;
	}

	public void setAllowedToNxt(boolean isAllowedToNxt) {
		this.isAllowedToNxt = isAllowedToNxt;
	}

	public String getAmountConsideredForClaimAlert() {
		return amountConsideredForClaimAlert;
	}

	public void setAmountConsideredForClaimAlert(
			String amountConsideredForClaimAlert) {
		this.amountConsideredForClaimAlert = amountConsideredForClaimAlert;
	}

	public Boolean getIsNegotiationPending() {
		return isNegotiationPending;
	}

	public void setIsNegotiationPending(Boolean isNegotiationPending) {
		this.isNegotiationPending = isNegotiationPending;
	}

	public Boolean getIsNegotiationApplicable() {
		return isNegotiationApplicable;
	}

	public void setIsNegotiationApplicable(Boolean isNegotiationApplicable) {
		this.isNegotiationApplicable = isNegotiationApplicable;
	}

	public String getPointstoNegotiate() {
		return pointstoNegotiate;
	}

	public void setPointstoNegotiate(String pointstoNegotiate) {
		this.pointstoNegotiate = pointstoNegotiate;
	}

	public Boolean getIsNegotiationEnhnApproved() {
		return isNegotiationEnhnApproved;
	}

	public void setIsNegotiationEnhnApproved(Boolean isNegotiationEnhnApproved) {
		this.isNegotiationEnhnApproved = isNegotiationEnhnApproved;
	}

	public String getAmtToNegotiate() {
		return amtToNegotiate;
	}

	public void setAmtToNegotiate(String amtToNegotiate) {
		this.amtToNegotiate = amtToNegotiate;
	}
	
	//R1295
	private SelectValue queryType;
	private Integer queryCount;

	public SelectValue getQueryType() {
		return queryType;
	}

	public void setQueryType(SelectValue queryType) {
		this.queryType = queryType;
	}

	public Integer getQueryCount() {
		return queryCount;
	}

	public void setQueryCount(Integer queryCount) {
		this.queryCount = queryCount;
	}
	
	public List<RoomRentMatchingDTO> getIccuRoomRentMappingDTOList() {
		return iccuRoomRentMappingDTOList;
	}

	public void setIccuRoomRentMappingDTOList(
			List<RoomRentMatchingDTO> iccuRoomRentMappingDTOList) {
		this.iccuRoomRentMappingDTOList = iccuRoomRentMappingDTOList;
	}

	public Boolean getIsICCUoneMapping() {
		return isICCUoneMapping;
	}

	public void setIsICCUoneMapping(Boolean isICCUoneMapping) {
		this.isICCUoneMapping = isICCUoneMapping;
	}

	public Double getHighestCopay() {
		return highestCopay;
	}

	public void setHighestCopay(Double highestCopay) {
		this.highestCopay = highestCopay;
	}

	public Double getAmountConsCopayPercentage() {
		return amountConsCopayPercentage;
	}

	public void setAmountConsCopayPercentage(Double amountConsCopayPercentage) {
		this.amountConsCopayPercentage = amountConsCopayPercentage;
	}

	public Double getBalanceSICopayPercentage() {
		return balanceSICopayPercentage;
	}

	public void setBalanceSICopayPercentage(Double balanceSICopayPercentage) {
		this.balanceSICopayPercentage = balanceSICopayPercentage;
	}

	public Boolean getIsSDEnabled() {
		return isSDEnabled;
	}

	public void setIsSDEnabled(Boolean isSDEnabled) {
		this.isSDEnabled = isSDEnabled;
	}

	public ProcessPreAuthButtonLayout getProcessPreauthButtonObj() {
		return processPreauthButtonObj;
	}

	public void setProcessPreauthButtonObj(
			ProcessPreAuthButtonLayout processPreauthButtonObj) {
		this.processPreauthButtonObj = processPreauthButtonObj;
	}

	public PreauthEnhancementButtons getProcessEnhancementButtonObj() {
		return processEnhancementButtonObj;
	}

	public void setProcessEnhancementButtonObj(
			PreauthEnhancementButtons processEnhancementButtonObj) {
		this.processEnhancementButtonObj = processEnhancementButtonObj;
	}

	public ClaimRequestMedicalDecisionButtons getClaimReqButtonObj() {
		return claimReqButtonObj;
	}

	public void setClaimReqButtonObj(
			ClaimRequestMedicalDecisionButtons claimReqButtonObj) {
		this.claimReqButtonObj = claimReqButtonObj;
	}

	public Boolean getIsValidationReq() {
		return isValidationReq;
	}

	public void setIsValidationReq(Boolean isValidationReq) {
		this.isValidationReq = isValidationReq;
	}
	
	public Long getDocAckKey() {
		return docAckKey;
	}

	public void setDocAckKey(Long docAckKey) {
		this.docAckKey = docAckKey;
	}

	public Boolean getIsBillClasificationEdit() {
		return isBillClasificationEdit;
	}

	public void setIsBillClasificationEdit(Boolean isBillClasificationEdit) {
		this.isBillClasificationEdit = isBillClasificationEdit;
	}

	public String getWithdrawInternalRemarks() {
		return withdrawInternalRemarks;
	}

	public void setWithdrawInternalRemarks(String withdrawInternalRemarks) {
		this.withdrawInternalRemarks = withdrawInternalRemarks;
	}

	public String getHospIcdMappingAlertFlag() {
		return hospIcdMappingAlertFlag;
	}

	public void setHospIcdMappingAlertFlag(String hospIcdMappingAlertFlag) {
		this.hospIcdMappingAlertFlag = hospIcdMappingAlertFlag;
	}

	public String getMulticlaimAvailFlag() {
		return multiclaimAvailFlag;
	}

	public void setMulticlaimAvailFlag(String multiclaimAvailFlag) {
		this.multiclaimAvailFlag = multiclaimAvailFlag;
	}

	public String getInvPendingFlag() {
		return invPendingFlag;
	}

	public void setInvPendingFlag(String invPendingFlag) {
		this.invPendingFlag = invPendingFlag;
	}	

	public String getLastQueryRaiseDate() {
		return lastQueryRaiseDate;
	}

	public void setLastQueryRaiseDate(String lastQueryRaiseDate) {
		this.lastQueryRaiseDate = lastQueryRaiseDate;
	}

	public String getPortalStatusVal() {
		return portalStatusVal;
	}

	public void setPortalStatusVal(String portalStatusVal) {
		this.portalStatusVal = portalStatusVal;
	}

	public String getWebsiteStatusVal() {
		return websiteStatusVal;
	}

	public void setWebsiteStatusVal(String websiteStatusVal) {
		this.websiteStatusVal = websiteStatusVal;
	}
	
	public List<String> getPopupSIRestrication() {
		return popupSIRestrication;
	}

	public void setPopupSIRestrication(List<String> popupSIRestrication) {
		this.popupSIRestrication = popupSIRestrication;
	}
	
	public Integer getSuperSurplusExceededSI() {
		return superSurplusExceededSI;
	}

	public void setSuperSurplusExceededSI(Integer superSurplusExceededSI) {
		this.superSurplusExceededSI = superSurplusExceededSI;
	}
	public String getVbCheckStatusFlag() {
		return vbCheckStatusFlag;
	}

	public void setVbCheckStatusFlag(String vbCheckStatusFlag) {
		this.vbCheckStatusFlag = vbCheckStatusFlag;
	}
	
	public SelectValue getRiskName() {
		return riskName;
	}

	public void setRiskName(SelectValue riskName) {
		this.riskName = riskName;
	}

	public String getTopUpInsuredName() {
		return topUpInsuredName;
	}

	public void setTopUpInsuredName(String topUpInsuredName) {
		this.topUpInsuredName = topUpInsuredName;
	}

	public Long getTopUpInsuredNo() {
		return topUpInsuredNo;
	}

	public void setTopUpInsuredNo(Long topUpInsuredNo) {
		this.topUpInsuredNo = topUpInsuredNo;
	}

	public String getTopUPPolicyNumber() {
		return topUPPolicyNumber;
	}

	public void setTopUPPolicyNumber(String topUPPolicyNumber) {
		this.topUPPolicyNumber = topUPPolicyNumber;
	}

	public String getTopUpProposerName() {
		return topUpProposerName;
	}

	public void setTopUpProposerName(String topUpProposerName) {
		this.topUpProposerName = topUpProposerName;
	}

	public Long getVipCustomer() {
		return vipCustomer;
	}

	public void setVipCustomer(Long vipCustomer) {
		this.vipCustomer = vipCustomer;
	}

	public Double getOldNonAllopathicApprovedAmt() {
		return oldNonAllopathicApprovedAmt;
	}

	public void setOldNonAllopathicApprovedAmt(Double oldNonAllopathicApprovedAmt) {
		this.oldNonAllopathicApprovedAmt = oldNonAllopathicApprovedAmt;
	}

	public List<String> getPopupPrevClmInvst() {
		return popupPrevClmInvst;
	}

	public void setPopupPrevClmInvst(List<String> popupPrevClmInvst) {
		this.popupPrevClmInvst = popupPrevClmInvst;
	}	
	public String getAgentColorCode() {
		return agentColorCode;
	}

	public void setAgentColorCode(String agentColorCode) {
		this.agentColorCode = agentColorCode;
	}

	public String getBranchColorCode() {
		return branchColorCode;
	}

	public void setBranchColorCode(String branchColorCode) {
		this.branchColorCode = branchColorCode;
	}

	public List<VerificationAccountDeatilsTableDTO> getVerificationAccountDeatilsTableDTO() {
		return VerificationAccountDeatilsTableDTO;
	}

	public void setVerificationAccountDeatilsTableDTO(
			List<VerificationAccountDeatilsTableDTO> verificationAccountDeatilsTableDTO) {
		VerificationAccountDeatilsTableDTO = verificationAccountDeatilsTableDTO;
	}
	
	public String getPreauthRejCondition() {
		return preauthRejCondition;
	}

	public void setPreauthRejCondition(String preauthRejCondition) {
		this.preauthRejCondition = preauthRejCondition;
	}

	public String getPolCondtNo() {
		return polCondtNo;
	}

	public void setPolCondtNo(String polCondtNo) {
		this.polCondtNo = polCondtNo;
	}
	
	public String getFinalScore() {
		return finalScore;
	}

	public void setFinalScore(String finalScore) {
		this.finalScore = finalScore;
	}

	public Boolean getIsOthrBenefitApplicable() {
		return isOthrBenefitApplicable;
	}

	public void setIsOthrBenefitApplicable(Boolean isOthrBenefitApplicable) {
		this.isOthrBenefitApplicable = isOthrBenefitApplicable;
	}
	
	public List<PreviousPreAuthTableDTO> getPreviousPreauthTableDTOReportList() {
		return previousPreauthTableDTOReportList;
	}

	public void setPreviousPreauthTableDTOReportList(
			List<PreviousPreAuthTableDTO> previousPreauthTableDTOReportList) {
		this.previousPreauthTableDTOReportList = previousPreauthTableDTOReportList;
	}

	public Date getPreAuthRequestedDate() {
		return preAuthRequestedDate;
	}

	public void setPreAuthRequestedDate(Date preAuthRequestedDate) {
		this.preAuthRequestedDate = preAuthRequestedDate;
	}

	public int getOtherDeductionAmount() {
		return otherDeductionAmount;
	}

	public void setOtherDeductionAmount(int otherDeductionAmount) {
		this.otherDeductionAmount = otherDeductionAmount;
	}

	public int getTotalApprovedAmount() {
		return totalApprovedAmount;
	}

	public void setTotalApprovedAmount(int totalApprovedAmount) {
		this.totalApprovedAmount = totalApprovedAmount;
	}

	public String getTotalApprovedAmountInWords() {
		return totalApprovedAmountInWords;
	}

	public void setTotalApprovedAmountInWords(String totalApprovedAmountInWords) {
		this.totalApprovedAmountInWords = totalApprovedAmountInWords;
	}

	public SelectValue getCatastrophicLoss() {
		return catastrophicLoss;
	}

	public void setCatastrophicLoss(SelectValue catastrophicLoss) {
		this.catastrophicLoss = catastrophicLoss;
	}

	public SelectValue getNatureOfLoss() {
		return natureOfLoss;
	}

	public void setNatureOfLoss(SelectValue natureOfLoss) {
		this.natureOfLoss = natureOfLoss;
	}

	public SelectValue getCauseOfLoss() {
		return causeOfLoss;
	}

	public void setCauseOfLoss(SelectValue causeOfLoss) {
		this.causeOfLoss = causeOfLoss;
	}

	public LegalHeirDTO getLegalHeirDto() {
		return legalHeirDto;
	}

	public void setLegalHeirDto(LegalHeirDTO legalHeirDto) {
		this.legalHeirDto = legalHeirDto;
	}

	public List<LegalHeirDTO> getLegalHeirDTOList() {
		return legalHeirDTOList;
	}

	public void setLegalHeirDTOList(List<LegalHeirDTO> legalHeirDTOList) {
		this.legalHeirDTOList = legalHeirDTOList;
	}

	public String getAccountPreference() {
		return accountPreference;
	}

	public void setAccountPreference(String accountPreference) {
		this.accountPreference = accountPreference;
	}

	public ViewSearchCriteriaTableDTO getDto() {
		return dto;
	}

	public void setDto(ViewSearchCriteriaTableDTO dto) {
		this.dto = dto;
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

	public String getNameAsPerBankAccount() {
		return nameAsPerBankAccount;
	}

	public void setNameAsPerBankAccount(String nameAsPerBankAccount) {
		this.nameAsPerBankAccount = nameAsPerBankAccount;
	}   	

	public SelectValue getDiagnosisHospitalCash() {
		return diagnosisHospitalCash;
	}

	public void setDiagnosisHospitalCash(SelectValue diagnosisHospitalCash) {
		this.diagnosisHospitalCash = diagnosisHospitalCash;
	}

	public String getHospitalCashClaimedAmnt() {
		return HospitalCashClaimedAmnt;
	}

	public void setHospitalCashClaimedAmnt(String hospitalCashClaimedAmnt) {
		HospitalCashClaimedAmnt = hospitalCashClaimedAmnt;
	}

	public ClaimRequestButtonsForWizard getClaimRequestButtonsWizard() {
		return claimRequestButtonsWizard;
	}

	public void setClaimRequestButtonsWizard(
			ClaimRequestButtonsForWizard claimRequestButtonsWizard) {
		this.claimRequestButtonsWizard = claimRequestButtonsWizard;
	}

	public String getFraudAlertFlag() {
		return fraudAlertFlag;
	}

	public void setFraudAlertFlag(String fraudAlertFlag) {
		this.fraudAlertFlag = fraudAlertFlag;
	}

	public String getFraudAlertMsg() {
		return fraudAlertMsg;
	}

	public void setFraudAlertMsg(String fraudAlertMsg) {
		this.fraudAlertMsg = fraudAlertMsg;
	}
	
	public Boolean getStpApplicable() {
		return stpApplicable;
	}

	public void setStpApplicable(Boolean stpApplicable) {
		this.stpApplicable = stpApplicable;
	}
	
	public Boolean getApproveBtnFlag() {
		return approveBtnFlag;
	}

	public void setApproveBtnFlag(Boolean approveBtnFlag) {
		this.approveBtnFlag = approveBtnFlag;
	}

	public String getApproveBtnMsg() {
		return approveBtnMsg;
	}

	public void setApproveBtnMsg(String approveBtnMsg) {
		this.approveBtnMsg = approveBtnMsg;
	}	
	
	public Double getMaxCopayAmtValueforAssmtSheet() {
		return maxCopayAmtValueforAssmtSheet;
	}

	public void setMaxCopayAmtValueforAssmtSheet(
			Double maxCopayAmtValueforAssmtSheet) {
		this.maxCopayAmtValueforAssmtSheet = maxCopayAmtValueforAssmtSheet;
	}


	//CR2019217
	public String getIcrAgentValue() {
		return icrAgentValue;
	}

	public void setIcrAgentValue(String icrAgentValue) {
		this.icrAgentValue = icrAgentValue;
	}
	
	public Boolean getIsCoInsurance() {
		return isCoInsurance;
	}

	public void setIsCoInsurance(Boolean isCoInsurance) {
		this.isCoInsurance = isCoInsurance;
	}

	public List<CoInsuranceTableDTO> getCoInsuranceList() {
		return coInsuranceList;
	}

	public void setCoInsuranceList(List<CoInsuranceTableDTO> coInsuranceList) {
		this.coInsuranceList = coInsuranceList;
	}

	

	public boolean isLetterContentValidated() {
		return letterContentValidated;
	}

	public void setLetterContentValidated(boolean letterContentValidated) {
		this.letterContentValidated = letterContentValidated;
	}

	public String getHospitalDiscountFlag() {
		return hospitalDiscountFlag;
	}

	public void setHospitalDiscountFlag(String hospitalDiscountFlag) {
		this.hospitalDiscountFlag = hospitalDiscountFlag;
	}
	
	public String getSmAgentValue() {
		return smAgentValue;
	}

	public void setSmAgentValue(String smAgentValue) {
		this.smAgentValue = smAgentValue;
	}	

	public Boolean getAuditFlag() {
		return auditFlag;
	}

	public void setAuditFlag(Boolean auditFlag) {
		this.auditFlag = auditFlag;
	}

	public String getSiAlertFlag() {
		return siAlertFlag;
	}

	public void setSiAlertFlag(String siAlertFlag) {
		this.siAlertFlag = siAlertFlag;
	}

	public String getSiAlertDesc() {
		return siAlertDesc;
	}

	public void setSiAlertDesc(String siAlertDesc) {
		this.siAlertDesc = siAlertDesc;
	}

	public String getConsolidatedFinalAppAmnt() {
		return consolidatedFinalAppAmnt;
	}

	public void setConsolidatedFinalAppAmnt(String consolidatedFinalAppAmnt) {
		this.consolidatedFinalAppAmnt = consolidatedFinalAppAmnt;
	}

	public SumInsuredBonusAlertDTO getBonusAlertDTO() {
		return bonusAlertDTO;
	}

	public void setBonusAlertDTO(SumInsuredBonusAlertDTO bonusAlertDTO) {
		this.bonusAlertDTO = bonusAlertDTO;
	}

	public boolean isSIRequired() {
		return isSIRequired;
	}

	public void setSIRequired(boolean isSIRequired) {
		this.isSIRequired = isSIRequired;
	}

	public String getFlpAaUserID() {
		return flpAaUserID;
	}

	public void setFlpAaUserID(String flpAaUserID) {
		this.flpAaUserID = flpAaUserID;
	}

	public Date getFlpAaUserSubmittedDate() {
		return flpAaUserSubmittedDate;
	}

	public void setFlpAaUserSubmittedDate(Date flpAaUserSubmittedDate) {
		this.flpAaUserSubmittedDate = flpAaUserSubmittedDate;
	}

	public String getPolicyInstalmentFlag() {
		return policyInstalmentFlag;
	}

	public void setPolicyInstalmentFlag(String policyInstalmentFlag) {
		this.policyInstalmentFlag = policyInstalmentFlag;
	}

	public String getPolicyInstalmentMsg() {
		return policyInstalmentMsg;
	}

	public void setPolicyInstalmentMsg(String policyInstalmentMsg) {
		this.policyInstalmentMsg = policyInstalmentMsg;
	}

	public String getPolicyInstalmentDetailsMsg() {
		return policyInstalmentDetailsMsg;
	}

	public void setPolicyInstalmentDetailsMsg(String policyInstalmentDetailsMsg) {
		this.policyInstalmentDetailsMsg = policyInstalmentDetailsMsg;
	}

	public String getPolicyInstalmentDetailsFlag() {
		return policyInstalmentDetailsFlag;
	}

	public void setPolicyInstalmentDetailsFlag(String policyInstalmentDetailsFlag) {
		this.policyInstalmentDetailsFlag = policyInstalmentDetailsFlag;
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

	public Long getNhpUpdKey() {
		return nhpUpdKey;
	}

	public void setNhpUpdKey(Long nhpUpdKey) {
		this.nhpUpdKey = nhpUpdKey;
	}

	public String getIcacProcessFlag() {
		return icacProcessFlag;
	}

	public void setIcacProcessFlag(String icacProcessFlag) {
		this.icacProcessFlag = icacProcessFlag;
	}

	public String getIcacProcessRemark() {
		return icacProcessRemark;
	}

	public void setIcacProcessRemark(String icacProcessRemark) {
		this.icacProcessRemark = icacProcessRemark;
	}

	public Long getIcacStatusId() {
		return icacStatusId;
	}

	public void setIcacStatusId(Long icacStatusId) {
		this.icacStatusId = icacStatusId;
	}
	
	public LegalBillingDTO getLegalBillingDTO() {
		return legalBillingDTO;
	}

	public void setLegalBillingDTO(LegalBillingDTO legalBillingDTO) {
		this.legalBillingDTO = legalBillingDTO;
	}

	public ReceiptOfDocumentsDTO getReceiptOfDocumentsDTO() {
		return receiptOfDocumentsDTO;
	}

	public void setReceiptOfDocumentsDTO(ReceiptOfDocumentsDTO receiptOfDocumentsDTO) {
		this.receiptOfDocumentsDTO = receiptOfDocumentsDTO;
	}

	public Boolean getHospitalCash() {
		return hospitalCash;
	}

	public void setHospitalCash(Boolean hospitalCash) {
		this.hospitalCash = hospitalCash;
	}

	public String getPolicyInsuredAgeingFlag() {
		return policyInsuredAgeingFlag;
	}

	public void setPolicyInsuredAgeingFlag(String policyInsuredAgeingFlag) {
		this.policyInsuredAgeingFlag = policyInsuredAgeingFlag;
	}

	public Date getDocRecievedDate() {
		return docRecievedDate;
	}

	public void setDocRecievedDate(Date docRecievedDate) {
		this.docRecievedDate = docRecievedDate;
	}

	public Boolean getIsPremiumInstAmtEql() {
		return isPremiumInstAmtEql;
	}

	public void setIsPremiumInstAmtEql(Boolean isPremiumInstAmtEql) {
		this.isPremiumInstAmtEql = isPremiumInstAmtEql;
	}

	public Boolean getApproveButtonExists() {
		return approveButtonExists;
	}

	public void setApproveButtonExists(Boolean approveButtonExists) {
		this.approveButtonExists = approveButtonExists;
	}

	public Boolean getRejectionButtonExists() {
		return rejectionButtonExists;
	}

	public void setRejectionButtonExists(Boolean rejectionButtonExists) {
		this.rejectionButtonExists = rejectionButtonExists;
	}

	public String getFinalScoreDeficiency() {
		return finalScoreDeficiency;
	}

	public void setFinalScoreDeficiency(String finalScoreDeficiency) {
		this.finalScoreDeficiency = finalScoreDeficiency;
	}
	
	public Boolean getIccuProportionalDeductionFlg() {
		return iccuProportionalDeductionFlg;
	}

	public void setIccuProportionalDeductionFlg(Boolean iccuProportionalDeductionFlg) {
		this.iccuProportionalDeductionFlg = iccuProportionalDeductionFlg;
	}

	public Boolean getOtProportionalDeductionFlg() {
		return otProportionalDeductionFlg;
	}

	public void setOtProportionalDeductionFlg(Boolean otProportionalDeductionFlg) {
		this.otProportionalDeductionFlg = otProportionalDeductionFlg;
	}

	public Boolean getProfFeesProportionalDeductionFlg() {
		return profFeesProportionalDeductionFlg;
	}

	public void setProfFeesProportionalDeductionFlg(
			Boolean profFeesProportionalDeductionFlg) {
		this.profFeesProportionalDeductionFlg = profFeesProportionalDeductionFlg;
	}

	public Boolean getMedicineProportionalDeductionFlg() {
		return medicineProportionalDeductionFlg;
	}

	public void setMedicineProportionalDeductionFlg(
			Boolean medicineProportionalDeductionFlg) {
		this.medicineProportionalDeductionFlg = medicineProportionalDeductionFlg;
	}

	public Boolean getInvestigationProportionalDeductionFlg() {
		return investigationProportionalDeductionFlg;
	}

	public void setInvestigationProportionalDeductionFlg(
			Boolean investigationProportionalDeductionFlg) {
		this.investigationProportionalDeductionFlg = investigationProportionalDeductionFlg;
	}

	public Boolean getOtherPackProportionalDeductionFlg() {
		return otherPackProportionalDeductionFlg;
	}

	public void setOtherPackProportionalDeductionFlg(
			Boolean otherPackProportionalDeductionFlg) {
		this.otherPackProportionalDeductionFlg = otherPackProportionalDeductionFlg;
	}

	public Boolean getOthersProportionalDeductionFlg() {
		return othersProportionalDeductionFlg;
	}

	public void setOthersProportionalDeductionFlg(
			Boolean othersProportionalDeductionFlg) {
		this.othersProportionalDeductionFlg = othersProportionalDeductionFlg;
	}

	public Boolean getAmbulanceProportionalDeductionFlg() {
		return ambulanceProportionalDeductionFlg;
	}

	public void setAmbulanceProportionalDeductionFlg(
			Boolean ambulanceProportionalDeductionFlg) {
		this.ambulanceProportionalDeductionFlg = ambulanceProportionalDeductionFlg;
	}

	public Boolean getaNHProportionalDeductionFlg() {
		return aNHProportionalDeductionFlg;
	}

	public void setaNHProportionalDeductionFlg(Boolean aNHProportionalDeductionFlg) {
		this.aNHProportionalDeductionFlg = aNHProportionalDeductionFlg;
	}

	public Boolean getCompositeProportionalDeductionFlg() {
		return compositeProportionalDeductionFlg;
	}

	public void setCompositeProportionalDeductionFlg(
			Boolean compositeProportionalDeductionFlg) {
		this.compositeProportionalDeductionFlg = compositeProportionalDeductionFlg;
	}

	public Boolean getProcedureProportionalDeductionFlg() {
		return procedureProportionalDeductionFlg;
	}

	public void setProcedureProportionalDeductionFlg(
			Boolean procedureProportionalDeductionFlg) {
		this.procedureProportionalDeductionFlg = procedureProportionalDeductionFlg;
	}

	public Boolean getMisWithHosProportionalDeductionFlg() {
		return misWithHosProportionalDeductionFlg;
	}

	public void setMisWithHosProportionalDeductionFlg(
			Boolean misWithHosProportionalDeductionFlg) {
		this.misWithHosProportionalDeductionFlg = misWithHosProportionalDeductionFlg;
	}

	public Boolean getMisWithoutHosProportionalDeductionFlg() {
		return misWithoutHosProportionalDeductionFlg;
	}

	public void setMisWithoutHosProportionalDeductionFlg(
			Boolean misWithoutHosProportionalDeductionFlg) {
		this.misWithoutHosProportionalDeductionFlg = misWithoutHosProportionalDeductionFlg;
	}

	public Boolean getIsFAorBillingScreen() {
		return isFAorBillingScreen;
	}

	public void setIsFAorBillingScreen(Boolean isFAorBillingScreen) {
		this.isFAorBillingScreen = isFAorBillingScreen;
	}

	public Boolean getIccuProportionalDeductionChecked() {
		return iccuProportionalDeductionChecked;
	}

	public void setIccuProportionalDeductionChecked(
			Boolean iccuProportionalDeductionChecked) {
		this.iccuProportionalDeductionChecked = iccuProportionalDeductionChecked;
	}

	public Boolean getOtProportionalDeductionChecked() {
		return otProportionalDeductionChecked;
	}

	public void setOtProportionalDeductionChecked(
			Boolean otProportionalDeductionChecked) {
		this.otProportionalDeductionChecked = otProportionalDeductionChecked;
	}

	public Boolean getProfFeesProportionalDeductionChecked() {
		return profFeesProportionalDeductionChecked;
	}

	public void setProfFeesProportionalDeductionChecked(
			Boolean profFeesProportionalDeductionChecked) {
		this.profFeesProportionalDeductionChecked = profFeesProportionalDeductionChecked;
	}

	public Boolean getMedicineProportionalDeductionChecked() {
		return medicineProportionalDeductionChecked;
	}

	public void setMedicineProportionalDeductionChecked(
			Boolean medicineProportionalDeductionChecked) {
		this.medicineProportionalDeductionChecked = medicineProportionalDeductionChecked;
	}

	public Boolean getInvestigationProportionalDeductionChecked() {
		return investigationProportionalDeductionChecked;
	}

	public void setInvestigationProportionalDeductionChecked(
			Boolean investigationProportionalDeductionChecked) {
		this.investigationProportionalDeductionChecked = investigationProportionalDeductionChecked;
	}

	public Boolean getOtherPackProportionalDeductionChecked() {
		return otherPackProportionalDeductionChecked;
	}

	public void setOtherPackProportionalDeductionChecked(
			Boolean otherPackProportionalDeductionChecked) {
		this.otherPackProportionalDeductionChecked = otherPackProportionalDeductionChecked;
	}

	public Boolean getOthersProportionalDeductionChecked() {
		return othersProportionalDeductionChecked;
	}

	public void setOthersProportionalDeductionChecked(
			Boolean othersProportionalDeductionChecked) {
		this.othersProportionalDeductionChecked = othersProportionalDeductionChecked;
	}

	public Boolean getAmbulanceProportionalDeductionChecked() {
		return ambulanceProportionalDeductionChecked;
	}

	public void setAmbulanceProportionalDeductionChecked(
			Boolean ambulanceProportionalDeductionChecked) {
		this.ambulanceProportionalDeductionChecked = ambulanceProportionalDeductionChecked;
	}

	public Boolean getaNHProportionalDeductionChecked() {
		return aNHProportionalDeductionChecked;
	}

	public void setaNHProportionalDeductionChecked(
			Boolean aNHProportionalDeductionChecked) {
		this.aNHProportionalDeductionChecked = aNHProportionalDeductionChecked;
	}

	public Boolean getCompositeProportionalDeductionChecked() {
		return compositeProportionalDeductionChecked;
	}

	public void setCompositeProportionalDeductionChecked(
			Boolean compositeProportionalDeductionChecked) {
		this.compositeProportionalDeductionChecked = compositeProportionalDeductionChecked;
	}

	public Boolean getProcedureProportionalDeductionChecked() {
		return procedureProportionalDeductionChecked;
	}

	public void setProcedureProportionalDeductionChecked(
			Boolean procedureProportionalDeductionChecked) {
		this.procedureProportionalDeductionChecked = procedureProportionalDeductionChecked;
	}

	public Boolean getMisWithHosProportionalDeductionChecked() {
		return misWithHosProportionalDeductionChecked;
	}

	public void setMisWithHosProportionalDeductionChecked(
			Boolean misWithHosProportionalDeductionChecked) {
		this.misWithHosProportionalDeductionChecked = misWithHosProportionalDeductionChecked;
	}

	public Boolean getMisWithoutHosProportionalDeductionChecked() {
		return misWithoutHosProportionalDeductionChecked;
	}

	public void setMisWithoutHosProportionalDeductionChecked(
			Boolean misWithoutHosProportionalDeductionChecked) {
		this.misWithoutHosProportionalDeductionChecked = misWithoutHosProportionalDeductionChecked;
	}

	public Boolean getIsCashlessPropDedSelected() {
		return isCashlessPropDedSelected;
	}

	public void setIsCashlessPropDedSelected(Boolean isCashlessPropDedSelected) {
		this.isCashlessPropDedSelected = isCashlessPropDedSelected;
	}

	public String getClaimPriorityLabel() {
		return claimPriorityLabel;
	}

	public void setClaimPriorityLabel(String claimPriorityLabel) {
		this.claimPriorityLabel = claimPriorityLabel;
	}
	
	public Boolean getIsModernSublimitSelected() {
		return isModernSublimitSelected;
	}

	public void setIsModernSublimitSelected(Boolean isModernSublimitSelected) {
		this.isModernSublimitSelected = isModernSublimitSelected;
	}

	public Boolean getIsSublimitIdAvail() {
		return isSublimitIdAvail;
	}

	public void setIsSublimitIdAvail(Boolean isSublimitIdAvail) {
		this.isSublimitIdAvail = isSublimitIdAvail;
	}

	public Boolean getIsModernSublimitSIExhaust() {
		return isModernSublimitSIExhaust;
	}

	public void setIsModernSublimitSIExhaust(Boolean isModernSublimitSIExhaust) {
		this.isModernSublimitSIExhaust = isModernSublimitSIExhaust;
	}

	public Boolean getIsHcTopupPolicyAvail() {
		return isHcTopupPolicyAvail;
	}

	public void setIsHcTopupPolicyAvail(Boolean isHcTopupPolicyAvail) {
		this.isHcTopupPolicyAvail = isHcTopupPolicyAvail;
	}

	public Date getTopUpInsuredDOB() {
		return topUpInsuredDOB;
	}

	public void setTopUpInsuredDOB(Date topUpInsuredDOB) {
		this.topUpInsuredDOB = topUpInsuredDOB;
	}

	public Double getTopUpInsuredage() {
		return topUpInsuredage;
	}

	public void setTopUpInsuredage(Double topUpInsuredage) {
		this.topUpInsuredage = topUpInsuredage;
	}

	public Boolean getDecisionChangeReasonApplicable() {
		return decisionChangeReasonApplicable;
	}

	public void setDecisionChangeReasonApplicable(
			Boolean decisionChangeReasonApplicable) {
		this.decisionChangeReasonApplicable = decisionChangeReasonApplicable;
	}

	public Boolean getIsNEFTDetailsAvailable() {
		return isNEFTDetailsAvailable;
	}

	public void setIsNEFTDetailsAvailable(Boolean isNEFTDetailsAvailable) {
		this.isNEFTDetailsAvailable = isNEFTDetailsAvailable;
	}

	public Boolean getIsNEFTDetailsAvailableinDMS() {
		return isNEFTDetailsAvailableinDMS;
	}

	public void setIsNEFTDetailsAvailableinDMS(Boolean isNEFTDetailsAvailableinDMS) {
		this.isNEFTDetailsAvailableinDMS = isNEFTDetailsAvailableinDMS;
	}

	public Boolean getIsBusinessProfileClicked() {
		return isBusinessProfileClicked;
	}

	public void setIsBusinessProfileClicked(Boolean isBusinessProfileClicked) {
		this.isBusinessProfileClicked = isBusinessProfileClicked;
	}
	
	public Boolean getIsInsuredChannedNameClicked() {
		return isInsuredChannedNameClicked;
	}

	public void setIsInsuredChannedNameClicked(Boolean isInsuredChannedNameClicked) {
		this.isInsuredChannedNameClicked = isInsuredChannedNameClicked;
	}

	public Boolean getIsPhysicalVerificationPending() {
		return isPhysicalVerificationPending;
	}

	public void setIsPhysicalVerificationPending(
			Boolean isPhysicalVerificationPending) {
		this.isPhysicalVerificationPending = isPhysicalVerificationPending;
	}

	public Boolean getIsPhysicalVerificationcompleted() {
		return isPhysicalVerificationcompleted;
	}

	public void setIsPhysicalVerificationcompleted(
			Boolean isPhysicalVerificationcompleted) {
		this.isPhysicalVerificationcompleted = isPhysicalVerificationcompleted;
	}

	public Long getProductVersionNumber() {
		return productVersionNumber;
	}

	public void setProductVersionNumber(Long productVersionNumber) {
		this.productVersionNumber = productVersionNumber;
	}

	public String getProductUinNumber() {
		return productUinNumber;
	}

	public void setProductUinNumber(String productUinNumber) {
		this.productUinNumber = productUinNumber;
	}
	public Boolean getIsHospitalExpenseCoverSelected() {
		return isHospitalExpenseCoverSelected;
	}

	public void setIsHospitalExpenseCoverSelected(
			Boolean isHospitalExpenseCoverSelected) {
		this.isHospitalExpenseCoverSelected = isHospitalExpenseCoverSelected;
	}

	public String getGrievanceRepresentation() {
		return grievanceRepresentation;
	}

	public void setGrievanceRepresentation(String grievanceRepresentation) {
		this.grievanceRepresentation = grievanceRepresentation;
	}
		
	public Double getNetworkHospitalDiscount() {
		return networkHospitalDiscount;
	}

	public void setNetworkHospitalDiscount(Double networkHospitalDiscount) {
		this.networkHospitalDiscount = networkHospitalDiscount;
	}

	public String getNetworkHospitalDiscountRemarksForAssessmentSheet() {
		return networkHospitalDiscountRemarksForAssessmentSheet;
	}

	public void setNetworkHospitalDiscountRemarksForAssessmentSheet(
			String networkHospitalDiscountRemarksForAssessmentSheet) {
		this.networkHospitalDiscountRemarksForAssessmentSheet = networkHospitalDiscountRemarksForAssessmentSheet;
	}

	public String getNetworkHospitalDiscountRemarks() {
		return networkHospitalDiscountRemarks;
	}

	public void setNetworkHospitalDiscountRemarks(
			String networkHospitalDiscountRemarks) {
		this.networkHospitalDiscountRemarks = networkHospitalDiscountRemarks;
	}
	
	public Boolean getIsBalSIForSublimitCardicSelected() {
		return isBalSIForSublimitCardicSelected;
	}

	public void setIsBalSIForSublimitCardicSelected(
			Boolean isBalSIForSublimitCardicSelected) {
		this.isBalSIForSublimitCardicSelected = isBalSIForSublimitCardicSelected;
	}
	
	public Integer getNetworkHospitalDiscountNegative() {
		return networkHospitalDiscountNegative;
	}

	public void setNetworkHospitalDiscountNegative(
			Integer networkHospitalDiscountNegative) {
		this.networkHospitalDiscountNegative = networkHospitalDiscountNegative;
	}

	public InitiateTalkTalkTalkDTO getInitiateTalkTalkTalkDTO() {
		return initiateTalkTalkTalkDTO;
	}

	public void setInitiateTalkTalkTalkDTO(
			InitiateTalkTalkTalkDTO initiateTalkTalkTalkDTO) {
		this.initiateTalkTalkTalkDTO = initiateTalkTalkTalkDTO;
	}

	public List<InitiateTalkTalkTalkDTO> getInitiateTalkTalkTalkDTOList() {
		return initiateTalkTalkTalkDTOList;
	}

	public void setInitiateTalkTalkTalkDTOList(
			List<InitiateTalkTalkTalkDTO> initiateTalkTalkTalkDTOList) {
		this.initiateTalkTalkTalkDTOList = initiateTalkTalkTalkDTOList;
	}

	public String getZohoGrievanceFlag() {
		return zohoGrievanceFlag;
	}

	public void setZohoGrievanceFlag(String zohoGrievanceFlag) {
		this.zohoGrievanceFlag = zohoGrievanceFlag;
	}
	
	
	
	
}
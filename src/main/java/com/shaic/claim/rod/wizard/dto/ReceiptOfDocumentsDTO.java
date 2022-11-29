/**
 * 
 */
package com.shaic.claim.rod.wizard.dto;

import java.util.List;
import java.util.Map;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ReimbursementRejectionDto;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.registration.updateHospitalDetails.UpdateHospitalDetailsDTO;
import com.shaic.claim.reimbursement.billing.dto.AddOnBenefitsDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.reimbursement.searchuploaddocumentsforacknotreceived.UploadDocumentsForAckNotReceivedPageTableDTO;
import com.shaic.claim.reimbursement.talktalktalk.InitiateTalkTalkTalkDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claims.reibursement.addaditionaldocuments.SelectRODtoAddAdditionalDocumentsDTO;
import com.shaic.domain.DocumentDetails;
import com.shaic.domain.reimbursement.ReimbursementCalCulationDetails;
import com.shaic.newcode.wizard.dto.LegalHeirDTO;
//import com.shaic.ims.bpm.claim.modelv2.HumanTask;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;

/**
 * @author ntv.vijayar
 * 
 * This DTO captures the data which 
 * is needed for acknowledging the
 * received document. 
 *
 */
public class ReceiptOfDocumentsDTO extends AbstractTableDTO{
	
	private NewIntimationDto newIntimationDTO;
	
	private LegalHeirDTO legalHeirDto;
	
	private Long acknowledgementNumber;
	
	private List<SelectRODtoAddAdditionalDocumentsDTO> selectRODtoAddAdditionalDocumentsDTO;
	
	private SelectRODtoAddAdditionalDocumentsDTO selectedPhysicalDocumentsDTO;
		
	private DocumentDetailsDTO documentDetails;
	
	private UploadDocumentDTO uploadDocumentsDTO;
	
	private UploadedDocumentsDTO uploadedDocumentsDTO;
	
	private List<RODQueryDetailsDTO> rodQueryDetailsList;
	
	private List<RODQueryDetailsDTO> paymentQueryDetailsList;
	
	private List<ReconsiderRODRequestTableDTO> reconsiderRodRequestList;
	
	private List<ReimbursementRejectionDto> rejectionDetails;
	
	private UpdateHospitalDetailsDTO changeHospitalDto;
	
	private String createdByForQuery;
	
	private SectionDetailsTableDTO sectionDetailsDTO;
	
	private AddAddlDocsPaymentInfoDTO addAddlDocsPaymentInfoDetails;
	
	private ClaimDto claimDTO;
	
	private Long stageKey;
	private Long statusKey;
	
	private String statusValue;
	
	private String createdBy;
	
	private String modifiedBy;
	
	private String policyNo;
	
	
	private ReconsiderRODRequestTableDTO reconsiderRODdto;
	private RODQueryDetailsDTO rodqueryDTO;
	
	private List<UploadDocumentDTO> uploadDocsList;
	
	//private List<UploadDocumentDTO> uploadedDocsList;
	
	private List<BillEntryDetailsDTO> billEntryList;
	
	private BillEntryDetailsDTO billEntryDetailsDTO;
	
	private Long claimCount = 1L;
	
	
	/**
	 * The below variable is added for validating
	 * product benefit check box in rod screens.
	 * */
	
	private Map<String, Integer> productBenefitMap;
	/**
	 * The below given variables are added for bill 
	 * classification valdiation , which will be done
	 * in CREATE ROD screen. In documentDetailsDTO we
	 * have the same constants. But those are binded to
	 * fields. To avoid ambuigity , for validation alone
	 * we use below feilds.
	 * */
	private String hospitalizationFlag;
	
	private String preHospitalizationFlag;
	
	private String postHospitalizationFlag;
	
	private String partialHospitalizationFlag;
	
	//Set for user name and password issue.
	private String strUserName;
		
	private String strPassword;
	
	//private HumanTask humanTask;
	
	private Double totalClaimedAmount;
	
	private List<AddOnBenefitsDTO> addOnBenefitsDTO;
	
	private List<Double> productCoPay;
	
	private String rodNumberForUploadTbl;
	
	private Double currentProvisionAmount;
	
	private PreauthDTO preauthDTO;
	
	//Added for setting treatment type at ROD level to enable filtering search in Zonal.
	private String treatmentType;
	
	private RRCDTO rrcDTO;
	
	private InitiateTalkTalkTalkDTO initiateTalkTalkTalkDTO;
	
	private Boolean isDishonoured = false;
	
	private Boolean isPEDInitiated = false;
	
	private Boolean isPEDInitiatedbtn = false;
	
	private Map reconsiderationMap;
	
	private boolean queryReplyStatus = false;
	
	private String docFilePath;
	
	private String docType;
	
	
	private String dateOfAdmission;
	
	private Boolean isAlreadyHospitalizationExist = false;
	
	// This is for Comparison with Previous ROD
	private Boolean isComparisonDone = false;
	
	private Boolean isBillingScreen = false;
	
	private String comparisonResult = "";
	
	private Boolean isConversionAllowed = null;
	
	private String  bpmnOutCome ;
	
	private Boolean isHospitalizationRejected = false;
	
	private Boolean isHospitalizationRODApproved = true; 
	
	private Boolean isCancelPolicy = false;
	
	private Long copay;
	
	private String diagnosis;
	
	// Added for ticket 4287.
	private String emailIdForPaymentMode;
	
	private Double rodCurrentProvisionAmt;

	private String docToken;
	
	private List<UploadDocumentDTO> alreadyUploadDocsList;
	
	private List<UploadDocumentsForAckNotReceivedPageTableDTO> searchUploadpreauthUploadTblList;
	
	private List<UploadDocumentsForAckNotReceivedPageTableDTO> searchUploadrodUploadTblList;
	
	private Boolean isRejectRODReconsidered;
	
	private Boolean shouldDisableSection = false;
	
	
	private BeanItemContainer<SelectValue> checkListTableContainerForROD;
	
	//Added for saving benefits approved amount in claim payment table.
	private Double benefitsApprovedAmt ;
	
	BeanItemContainer<SelectValue> sectionList;
	
	private Boolean isQueryReplyReceived = false;
	

	private Long rodKeyFromPayload;
	
	private Boolean isDocumentVerified;
	
	private String crcFlaggedReason;
    
    private String crcFlaggedRemark;

	
	/**
	 * Used only by acknowledgement submit. 
	 * The amount to be sent to premia.
	 * 
	 * */
	private Double paProvisionAmtToPremia;

	private List<DocumentDetails> selectedPhysicalDocuments;
	
//	private Boolean isletterGeneratedInROD = false;
	

	private String clmPrcsInstruction;
    
    private String isSuspicious;
    
    private Long queryStage;
    
    private Long previousRodForReconsider;
    
    private String consentAlert;  
    
    private Boolean isMedicalScreen = false;
    
    private Boolean isPCCSelected = false;
    
    private Boolean isNEFTDetailsAvailable = false;
    
    private Boolean isNEFTDetailsAvailableinDMS = false;

	private String sourceRiskID;
	
	public String getConsentAlert() {
		return consentAlert;
	}
	
	public void setConsentAlert(String consentAlert) {
		this.consentAlert = consentAlert;
	}
	
	public String getSourceRiskID() {
		return sourceRiskID;
	}
	
	public void setSourceRiskID(String sourceRiskID) {
		this.sourceRiskID = sourceRiskID;
	}
    

	public Boolean getIsQueryReplyReceived() {
		return isQueryReplyReceived;
	}



	public void setIsQueryReplyReceived(Boolean isQueryReplyReceived) {
		this.isQueryReplyReceived = isQueryReplyReceived;
	}



	//Added for populating previous claims list;
	private List<List<PreviousAccountDetailsDTO>> previousAccountDetailsList;
	
	
	private List<ReimbursementCalCulationDetails> existingReimbursementCalDtsList;
	
	private Boolean death;
	private String deathFlag;
	
	private Boolean permanentPartialDisability;
	private String permanentPartialDisabilityflag;
	
	private Boolean permanentTotalDisability;
	private String permanentTotalDisabilityFlag;
	
	private Boolean temporaryTotalDisability;
	private String temporyTotalDisabilityFlag;
	
	private Boolean hospitalExpensesCover;
	private String hospitalExpensesCoverFlag;
	
	private Boolean isRodVersion = false;
	
	private List<PreviousAccountDetailsDTO> previousAccntDetailsList;
	private BeanItemContainer<SelectValue> fileTypeContainer;
	
	private String screenName;
	
	private ViewSearchCriteriaTableDTO dto;
	
	public Double getBenefitsApprovedAmt() {
		return benefitsApprovedAmt;
	}



	public void setBenefitsApprovedAmt(Double benefitsApprovedAmt) {
		this.benefitsApprovedAmt = benefitsApprovedAmt;
	}



	public String getDateOfAdmission() {
		return dateOfAdmission;
	}



	public void setDateOfAdmission(String dateOfAdmission) {
		this.dateOfAdmission = dateOfAdmission;
	}



	public String getDateOfDischarge() {
		return dateOfDischarge;
	}



	public void setDateOfDischarge(String dateOfDischarge) {
		this.dateOfDischarge = dateOfDischarge;
	}



	public String getIntimationNo() {
		return intimationNo;
	}



	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}



	public String getInsuredPatientName() {
		return insuredPatientName;
	}



	public void setInsuredPatientName(String insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
	}



	private String dateOfDischarge;
	
	private String intimationNo;
	
	private String insuredPatientName;

	
	
	//Added for claim request benefits service.
	
	//private List<Double> copayList;
	
	//private Long rodKey;
	
//	private AddOnBenefitsDTO addOnBenefitsDTO;
	

	

	
//	private List<DocumentCheckListDTO> documentCheckList;
	

	


	/*public List<Double> getCopayList() {
		return copayList;
	}



	public void setCopayList(List<Double> copayList) {
		this.copayList = copayList;
	}
*/


	public String getDocFilePath() {
		return docFilePath;
	}



	public void setDocFilePath(String docFilePath) {
		this.docFilePath = docFilePath;
	}



	public BillEntryDetailsDTO getBillEntryDetailsDTO() {
		return billEntryDetailsDTO;
	}



	public void setBillEntryDetailsDTO(BillEntryDetailsDTO billEntryDetailsDTO) {
		this.billEntryDetailsDTO = billEntryDetailsDTO;
	}



	public ReceiptOfDocumentsDTO()
	{
		newIntimationDTO = new NewIntimationDto();
		documentDetails = new DocumentDetailsDTO();
		claimDTO = new ClaimDto();
		uploadDocumentsDTO = new UploadDocumentDTO();
		uploadedDocumentsDTO = new UploadedDocumentsDTO();
		reconsiderRODdto = new ReconsiderRODRequestTableDTO();
		billEntryDetailsDTO = new BillEntryDetailsDTO();
		preauthDTO = new PreauthDTO();
		addAddlDocsPaymentInfoDetails = new AddAddlDocsPaymentInfoDTO();
		//addOnBenefitsDTO = new AddOnBenefitsDTO();
		dto = new ViewSearchCriteriaTableDTO();
	}
	

	
	public NewIntimationDto getNewIntimationDTO() {
		return newIntimationDTO;
	}

	public void setNewIntimationDTO(NewIntimationDto newIntimationDTO) {
		this.newIntimationDTO = newIntimationDTO;
	}

	public Long getAcknowledgementNumber() {
		return acknowledgementNumber;
	}

	public void setAcknowledgementNumber(Long acknowledgementNumber) {
		this.acknowledgementNumber = acknowledgementNumber;
	}


	public List<RODQueryDetailsDTO> getRodQueryDetailsList() {
		return rodQueryDetailsList;
	}

	public void setRodQueryDetailsList(List<RODQueryDetailsDTO> rodQueryDetailsList) {
		this.rodQueryDetailsList = rodQueryDetailsList;
	}

	public List<ReconsiderRODRequestTableDTO> getReconsiderRodRequestList() {
		return reconsiderRodRequestList;
	}

	public void setReconsiderRodRequestList(
			List<ReconsiderRODRequestTableDTO> reconsiderRodRequestList) {
		this.reconsiderRodRequestList = reconsiderRodRequestList;
	}

	public List<ReimbursementRejectionDto> getRejectionDetails() {
		return rejectionDetails;
	}



	public void setRejectionDetails(List<ReimbursementRejectionDto> rejectionDetails) {
		this.rejectionDetails = rejectionDetails;
	}



	public DocumentDetailsDTO getDocumentDetails() {
		return documentDetails;
	}

	public void setDocumentDetails(DocumentDetailsDTO documentDetails) {
		this.documentDetails = documentDetails;
	}

	public ClaimDto getClaimDTO() {
		return claimDTO;
	}

	public void setClaimDTO(ClaimDto claimDTO) {
		this.claimDTO = claimDTO;
	}


	/**
	 * @return the stageKey
	 */
	public Long getStageKey() {
		return stageKey;
	}


	/**
	 * @param stageKey the stageKey to set
	 */
	public void setStageKey(Long stageKey) {
		this.stageKey = stageKey;
	}


	/**
	 * @return the statusKey
	 */
	public Long getStatusKey() {
		return statusKey;
	}


	/**
	 * @param statusKey the statusKey to set
	 */
	public void setStatusKey(Long statusKey) {
		this.statusKey = statusKey;
	}


	/**
	 * @return the statusValue
	 */
	public String getStatusValue() {
		return statusValue;
	}


	/**
	 * @param statusValue the statusValue to set
	 */
	public void setStatusValue(String statusValue) {
		this.statusValue = statusValue;
	}


	/**
	 * @return the uploadDocumentsDTO
	 */
	public UploadDocumentDTO getUploadDocumentsDTO() {
		return uploadDocumentsDTO;
	}


	/**
	 * @param uploadDocumentsDTO the uploadDocumentsDTO to set
	 */
	public void setUploadDocumentsDTO(UploadDocumentDTO uploadDocumentsDTO) {
		this.uploadDocumentsDTO = uploadDocumentsDTO;
	}


	/**
	 * @return the uploadedDocumentsDTO
	 */
	public UploadedDocumentsDTO getUploadedDocumentsDTO() {
		return uploadedDocumentsDTO;
	}


	/**
	 * @param uploadedDocumentsDTO the uploadedDocumentsDTO to set
	 */
	public void setUploadedDocumentsDTO(UploadedDocumentsDTO uploadedDocumentsDTO) {
		this.uploadedDocumentsDTO = uploadedDocumentsDTO;
	}



	/**
	 * @return the reconsiderRODdto
	 */
	public ReconsiderRODRequestTableDTO getReconsiderRODdto() {
		return reconsiderRODdto;
	}



	/**
	 * @param reconsiderRODdto the reconsiderRODdto to set
	 */
	public void setReconsiderRODdto(ReconsiderRODRequestTableDTO reconsiderRODdto) {
		this.reconsiderRODdto = reconsiderRODdto;
	}



	/**
	 * @return the uploadDocsList
	 */
	public List<UploadDocumentDTO> getUploadDocsList() {
		return uploadDocsList;
	}



	/**
	 * @param uploadDocsList the uploadDocsList to set
	 */
	public void setUploadDocsList(List<UploadDocumentDTO> uploadDocsList) {
		this.uploadDocsList = uploadDocsList;
	}



	/**
	 * @return the billEntryList
	 */
	public List<BillEntryDetailsDTO> getBillEntryList() {
		return billEntryList;
	}



	/**
	 * @param billEntryList the billEntryList to set
	 */
	public void setBillEntryList(List<BillEntryDetailsDTO> billEntryList) {
		this.billEntryList = billEntryList;
	}



	public String getHospitalizationFlag() {
		return hospitalizationFlag;
	}



	public void setHospitalizationFlag(String hospitalizationFlag) {
		this.hospitalizationFlag = hospitalizationFlag;
	}



	public String getPreHospitalizationFlag() {
		return preHospitalizationFlag;
	}



	public void setPreHospitalizationFlag(String preHospitalizationFlag) {
		this.preHospitalizationFlag = preHospitalizationFlag;
	}



	public String getPostHospitalizationFlag() {
		return postHospitalizationFlag;
	}



	public void setPostHospitalizationFlag(String postHospitalizationFlag) {
		this.postHospitalizationFlag = postHospitalizationFlag;
	}



	public String getPartialHospitalizationFlag() {
		return partialHospitalizationFlag;
	}



	public void setPartialHospitalizationFlag(String partialHospitalizationFlag) {
		this.partialHospitalizationFlag = partialHospitalizationFlag;
	}



	public Map<String, Integer> getProductBenefitMap() {
		return productBenefitMap;
	}



	public void setProductBenefitMap(Map<String, Integer> productBenefitMap) {
		this.productBenefitMap = productBenefitMap;
	}



	public RODQueryDetailsDTO getRodqueryDTO() {
		return rodqueryDTO;
	}



	public void setRodqueryDTO(RODQueryDetailsDTO rodqueryDTO) {
		this.rodqueryDTO = rodqueryDTO;
	}



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



	/*public HumanTask getHumanTask() {
		return humanTask;
	}



	public void setHumanTask(HumanTask humanTask) {
		this.humanTask = humanTask;
	}*/



	public Double getTotalClaimedAmount() {
		return totalClaimedAmount;
	}



	public void setTotalClaimedAmount(Double totalClaimedAmount) {
		this.totalClaimedAmount = totalClaimedAmount;
	}



	public List<AddOnBenefitsDTO> getAddOnBenefitsDTO() {
		return addOnBenefitsDTO;
	}



	public void setAddOnBenefitsDTO(List<AddOnBenefitsDTO> addOnBenefitsDTO) {
		this.addOnBenefitsDTO = addOnBenefitsDTO;
	}



	public List<Double> getProductCoPay() {
		return productCoPay;
	}



	public void setProductCoPay(List<Double> productCoPay) {
		this.productCoPay = productCoPay;
	}



	public String getRodNumberForUploadTbl() {
		return rodNumberForUploadTbl;
	}



	public void setRodNumberForUploadTbl(String rodNumberForUploadTbl) {
		this.rodNumberForUploadTbl = rodNumberForUploadTbl;
	}



	public Double getCurrentProvisionAmount() {
		return currentProvisionAmount;
	}



	public void setCurrentProvisionAmount(Double currentProvisionAmount) {
		this.currentProvisionAmount = currentProvisionAmount;
	}



	public PreauthDTO getPreauthDTO() {
		return preauthDTO;
	}



	public void setPreauthDTO(PreauthDTO preauthDTO) {
		this.preauthDTO = preauthDTO;
	}



	
	public UpdateHospitalDetailsDTO getChangeHospitalDto() {
		return changeHospitalDto;
	}



	public void setChangeHospitalDto(UpdateHospitalDetailsDTO changeHospitalDto) {
		this.changeHospitalDto = changeHospitalDto;
	}



	public List<SelectRODtoAddAdditionalDocumentsDTO> getSelectRODtoAddAdditionalDocumentsDTO() {
		return selectRODtoAddAdditionalDocumentsDTO;
	}



	public void setSelectRODtoAddAdditionalDocumentsDTO(
			List<SelectRODtoAddAdditionalDocumentsDTO> selectRODtoAddAdditionalDocumentsDTO) {
		this.selectRODtoAddAdditionalDocumentsDTO = selectRODtoAddAdditionalDocumentsDTO;
	}



	public String getTreatmentType() {
		return treatmentType;
	}



	public void setTreatmentType(String treatmentType) {
		this.treatmentType = treatmentType;
	}



	public RRCDTO getRrcDTO() {
		return rrcDTO;
	}



	public void setRrcDTO(RRCDTO rrcDTO) {
		this.rrcDTO = rrcDTO;
	}



	public Boolean getIsDishonoured() {
		return isDishonoured;
	}



	public void setIsDishonoured(Boolean isDishonoured) {
		this.isDishonoured = isDishonoured;
	}



	public Map getReconsiderationMap() {
		return reconsiderationMap;
	}



	public void setReconsiderationMap(Map reconsiderationMap) {
		this.reconsiderationMap = reconsiderationMap;
	}



	public String getDocType() {
		return docType;
	}



	public void setDocType(String docType) {
		this.docType = docType;
	}



	public Boolean getIsComparisonDone() {
		return isComparisonDone;
	}



	public void setIsComparisonDone(Boolean isComparisonDone) {
		this.isComparisonDone = isComparisonDone;
	}



	public String getComparisonResult() {
		return comparisonResult;
	}



	public void setComparisonResult(String comparisonResult) {
		this.comparisonResult = comparisonResult;
	}



	public String getCreatedBy() {
		return createdBy;
	}



	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}



	public String getModifiedBy() {
		return modifiedBy;
	}



	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}



	public Boolean getIsConversionAllowed() {
		return isConversionAllowed;
	}



	public void setIsConversionAllowed(Boolean isConversionAllowed) {
		this.isConversionAllowed = isConversionAllowed;
	}


	public Boolean getIsBillingScreen() {
		return isBillingScreen;
	}
	


	public void setIsBillingScreen(Boolean isBillingScreen) {
		this.isBillingScreen = isBillingScreen;
	}



	public String getBpmnOutCome() {
		return bpmnOutCome;
	}



	public void setBpmnOutCome(String bpmnOutCome) {
		this.bpmnOutCome = bpmnOutCome;
	}



	public Boolean getIsHospitalizationRejected() {
		return isHospitalizationRejected;
	}



	public void setIsHospitalizationRejected(Boolean isHospitalizationRejected) {
		this.isHospitalizationRejected = isHospitalizationRejected;
	}



	public Boolean getIsHospitalizationRODApproved() {
		return isHospitalizationRODApproved;
	}



	public void setIsHospitalizationRODApproved(Boolean isHospitalizationRODApproved) {
		this.isHospitalizationRODApproved = isHospitalizationRODApproved;
	}



	public Boolean getIsCancelPolicy() {
		return isCancelPolicy;
	}



	public void setIsCancelPolicy(Boolean isCancelPolicy) {
		this.isCancelPolicy = isCancelPolicy;
	}



	public Long getCopay() {
		return copay;
	}



	public void setCopay(Long copay) {
		this.copay = copay;
	}



	public String getEmailIdForPaymentMode() {
		return emailIdForPaymentMode;
	}



	public void setEmailIdForPaymentMode(String emailIdForPaymentMode) {
		this.emailIdForPaymentMode = emailIdForPaymentMode;
	}



	public Double getRodCurrentProvisionAmt() {
		return rodCurrentProvisionAmt;
	}



	public void setRodCurrentProvisionAmt(Double rodCurrentProvisionAmt) {
		this.rodCurrentProvisionAmt = rodCurrentProvisionAmt;
	}



	public Boolean getIsPEDInitiated() {
		return isPEDInitiated;
	}



	public void setIsPEDInitiated(Boolean isPEDInitiated) {
		this.isPEDInitiated = isPEDInitiated;
	}



	public Boolean getIsPEDInitiatedbtn() {
		return isPEDInitiatedbtn;
	}



	public void setIsPEDInitiatedbtn(Boolean isPEDInitiatedbtn) {
		this.isPEDInitiatedbtn = isPEDInitiatedbtn;
	}



	public String getDiagnosis() {
		return diagnosis;
	}



	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}



	public String getDocToken() {
		return docToken;
	}



	public void setDocToken(String docToken) {
		this.docToken = docToken;
	}





public void setAlreadyUploadDocsList(
		List<UploadDocumentDTO> alreadyUploadDocsList) {
	this.alreadyUploadDocsList = alreadyUploadDocsList;
}
	public List<UploadDocumentDTO> getAlreadyUploadDocsList() {
		return alreadyUploadDocsList;
	}
		
	public String getCreatedByForQuery() {
		return createdByForQuery;
	}



	
	public void setCreatedByForQuery(String createdByForQuery) {
		this.createdByForQuery = createdByForQuery;
	}



	public List<UploadDocumentsForAckNotReceivedPageTableDTO> getSearchUploadpreauthUploadTblList() {
		return searchUploadpreauthUploadTblList;
	}



	public void setSearchUploadpreauthUploadTblList(
			List<UploadDocumentsForAckNotReceivedPageTableDTO> searchUploadpreauthUploadTblList) {
		this.searchUploadpreauthUploadTblList = searchUploadpreauthUploadTblList;
	}



	public List<UploadDocumentsForAckNotReceivedPageTableDTO> getSearchUploadrodUploadTblList() {
		return searchUploadrodUploadTblList;
	}



	public void setSearchUploadrodUploadTblList(
			List<UploadDocumentsForAckNotReceivedPageTableDTO> searchUploadrodUploadTblList) {
		this.searchUploadrodUploadTblList = searchUploadrodUploadTblList;
	}



	public Boolean getIsRejectRODReconsidered() {
		return isRejectRODReconsidered;
	}



	public void setIsRejectRODReconsidered(Boolean isRejectRODReconsidered) {
		this.isRejectRODReconsidered = isRejectRODReconsidered;
	}



	public Boolean getIsAlreadyHospitalizationExist() {
		return isAlreadyHospitalizationExist;
}
	public BeanItemContainer<SelectValue> getCheckListTableContainerForROD() {
		return checkListTableContainerForROD;
	}



	public void setIsAlreadyHospitalizationExist(
			Boolean isAlreadyHospitalizationExist) {
		this.isAlreadyHospitalizationExist = isAlreadyHospitalizationExist;
}
	public void setCheckListTableContainerForROD(
			BeanItemContainer<SelectValue> checkListTableContainerForROD) {
		this.checkListTableContainerForROD = checkListTableContainerForROD;
	}



	public List<List<PreviousAccountDetailsDTO>> getPreviousAccountDetailsList() {
		return previousAccountDetailsList;
	}



	public void setPreviousAccountDetailsList(
			List<List<PreviousAccountDetailsDTO>> previousAccountDetailsList) {
		this.previousAccountDetailsList = previousAccountDetailsList;
	}



	public Long getClaimCount() {
		return claimCount;
	}



	public void setClaimCount(Long claimCount) {
		this.claimCount = claimCount;
	}



	public Boolean getDeath() {
		return death;
}
	public BeanItemContainer<SelectValue> getSectionList() {
		return sectionList;
	}



	public void setDeath(Boolean death) {
		this.death = death;
	}
	public void setSectionList(BeanItemContainer<SelectValue> sectionList) {
		this.sectionList = sectionList;
	}



	public String getDeathFlag() {
		return deathFlag;
	}
	public SectionDetailsTableDTO getSectionDetailsDTO() {
		return sectionDetailsDTO;
	}



	public void setDeathFlag(String deathFlag) {
		this.deathFlag = deathFlag;
	}
	public void setSectionDetailsDTO(SectionDetailsTableDTO sectionDetailsDTO) {
		this.sectionDetailsDTO = sectionDetailsDTO;
	}



	public Boolean getPermanentPartialDisability() {
		return permanentPartialDisability;
	}
	public Boolean getShouldDisableSection() {
		return shouldDisableSection;
	}


	public void setPermanentPartialDisability(Boolean permanentPartialDisability) {
		this.permanentPartialDisability = permanentPartialDisability;
	}



	public String getPermanentPartialDisabilityflag() {
		return permanentPartialDisabilityflag;
	}



	public void setPermanentPartialDisabilityflag(
			String permanentPartialDisabilityflag) {
		this.permanentPartialDisabilityflag = permanentPartialDisabilityflag;
	}



	public Boolean getPermanentTotalDisability() {
		return permanentTotalDisability;
	}



	public void setPermanentTotalDisability(Boolean permanentTotalDisability) {
		this.permanentTotalDisability = permanentTotalDisability;
	}



	public String getPermanentTotalDisabilityFlag() {
		return permanentTotalDisabilityFlag;
	}



	public void setPermanentTotalDisabilityFlag(String permanentTotalDisabilityFlag) {
		this.permanentTotalDisabilityFlag = permanentTotalDisabilityFlag;
	}



	public Boolean getTemporaryTotalDisability() {
		return temporaryTotalDisability;
	}



	public void setTemporaryTotalDisability(Boolean temporaryTotalDisability) {
		this.temporaryTotalDisability = temporaryTotalDisability;
	}



	public String getTemporyTotalDisabilityFlag() {
		return temporyTotalDisabilityFlag;
	}



	public void setTemporyTotalDisabilityFlag(String temporyTotalDisabilityFlag) {
		this.temporyTotalDisabilityFlag = temporyTotalDisabilityFlag;
	}



	public Boolean getHospitalExpensesCover() {
		return hospitalExpensesCover;
	}



	public void setHospitalExpensesCover(Boolean hospitalExpensesCover) {
		this.hospitalExpensesCover = hospitalExpensesCover;
	}



	public String getHospitalExpensesCoverFlag() {
		return hospitalExpensesCoverFlag;
	}



	public void setHospitalExpensesCoverFlag(String hospitalExpensesCoverFlag) {
		this.hospitalExpensesCoverFlag = hospitalExpensesCoverFlag;
	}
	public void setShouldDisableSection(Boolean shouldDisableSection) {
		this.shouldDisableSection = shouldDisableSection;
	}




	public Long getRodKeyFromPayload() {
		return rodKeyFromPayload;
	}
	public Double getPaProvisionAmtToPremia() {
		return paProvisionAmtToPremia;

	}




	public void setRodKeyFromPayload(Long rodKeyFromPayload) {
		this.rodKeyFromPayload = rodKeyFromPayload;
	}
	public void setPaProvisionAmtToPremia(Double paProvisionAmtToPremia) {
		this.paProvisionAmtToPremia = paProvisionAmtToPremia;

	}



	public List<RODQueryDetailsDTO> getPaymentQueryDetailsList() {
		return paymentQueryDetailsList;
	}



	public void setPaymentQueryDetailsList(
			List<RODQueryDetailsDTO> paymentQueryDetailsList) {
		this.paymentQueryDetailsList = paymentQueryDetailsList;
	}



	public List<DocumentDetails> getSelectedPhysicalDocuments() {
		return selectedPhysicalDocuments;
	}



	public void setSelectedPhysicalDocuments(
			List<DocumentDetails> selectedPhysicalDocuments) {
		this.selectedPhysicalDocuments = selectedPhysicalDocuments;
	}



	public SelectRODtoAddAdditionalDocumentsDTO getSelectedPhysicalDocumentsDTO() {
		return selectedPhysicalDocumentsDTO;
	}



	public void setSelectedPhysicalDocumentsDTO(
			SelectRODtoAddAdditionalDocumentsDTO selectedPhysicalDocumentsDTO) {
		this.selectedPhysicalDocumentsDTO = selectedPhysicalDocumentsDTO;
	}



	public Boolean getIsDocumentVerified() {
		return isDocumentVerified;
	}



	public void setIsDocumentVerified(Boolean isDocumentVerified) {
		this.isDocumentVerified = isDocumentVerified;
	}



	public String getIsSuspicious() {
		return isSuspicious;
	}



	public void setIsSuspicious(String isSuspicious) {
		this.isSuspicious = isSuspicious;
	}



	public String getClmPrcsInstruction() {
		return clmPrcsInstruction;
	}



	public void setClmPrcsInstruction(String clmPrcsInstruction) {
		this.clmPrcsInstruction = clmPrcsInstruction;
	}



/*	public Boolean getIsletterGeneratedInROD() {
		return isletterGeneratedInROD;
	}



	public void setIsletterGeneratedInROD(Boolean isletterGeneratedInROD) {
		this.isletterGeneratedInROD = isletterGeneratedInROD;
	}
*/


	/*public List<DocumentCheckListDTO> getDocumentCheckList() {
		return documentCheckList;
	}

	public void setDocumentCheckList(List<DocumentCheckListDTO> documentCheckList) {
		this.documentCheckList = documentCheckList;
	}*/
	
	@Override
    public boolean equals(Object obj) {
        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        } else if (getAcknowledgementNumber() == null) {
            return obj == this;
        } else {
            return getAcknowledgementNumber().equals(((ReceiptOfDocumentsDTO) obj).getAcknowledgementNumber());
        }
    }

    @Override
    public int hashCode() {
        if (acknowledgementNumber != null) {
            return acknowledgementNumber.hashCode();
        } else {
            return super.hashCode();
        }
    }

	public List<PreviousAccountDetailsDTO> getPreviousAccntDetailsList() {
		return previousAccntDetailsList;
	}
	public BeanItemContainer<SelectValue> getFileTypeContainer() {
		return fileTypeContainer;
	}

	public void setPreviousAccntDetailsList(
			List<PreviousAccountDetailsDTO> previousAccntDetailsList) {
		this.previousAccntDetailsList = previousAccntDetailsList;
	}
	public void setFileTypeContainer(BeanItemContainer<SelectValue> fileTypeContainer) {
		this.fileTypeContainer = fileTypeContainer;
	}



	public Boolean getIsRodVersion() {
		return isRodVersion;
    }
	public Long getQueryStage() {
		return queryStage;
	}

	public void setIsRodVersion(Boolean isRodVersion) {
		this.isRodVersion = isRodVersion;
	}

	public String getCrcFlaggedReason() {
		return crcFlaggedReason;
	}

	public List<ReimbursementCalCulationDetails> getExistingReimbursementCalDtsList() {
		return existingReimbursementCalDtsList;
	}



	public void setExistingReimbursementCalDtsList(
			List<ReimbursementCalCulationDetails> existingReimbursementCalDtsList) {
		this.existingReimbursementCalDtsList = existingReimbursementCalDtsList;
	}
	public void setQueryStage(Long queryStage) {
		this.queryStage = queryStage;
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



	public String getScreenName() {
		return screenName;
	}



	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}



	public AddAddlDocsPaymentInfoDTO getAddAddlDocsPaymentInfoDetails() {
		return addAddlDocsPaymentInfoDetails;
	}



	public void setAddAddlDocsPaymentInfoDetails(
			AddAddlDocsPaymentInfoDTO addAddlDocsPaymentInfoDetails) {
		this.addAddlDocsPaymentInfoDetails = addAddlDocsPaymentInfoDetails;
	}



	public Long getPreviousRodForReconsider() {
		return previousRodForReconsider;
	}



	public void setPreviousRodForReconsider(Long previousRodForReconsider) {
		this.previousRodForReconsider = previousRodForReconsider;
	}



	public LegalHeirDTO getLegalHeirDto() {
		return legalHeirDto;
	}

	public Boolean getIsMedicalScreen() {
		return isMedicalScreen;
	}



	public void setIsMedicalScreen(Boolean isMedicalScreen) {
		this.isMedicalScreen = isMedicalScreen;
	}



	public Boolean getIsPCCSelected() {
		return isPCCSelected;
	}



	public void setIsPCCSelected(Boolean isPCCSelected) {
		this.isPCCSelected = isPCCSelected;
	}


	public void setLegalHeirDto(LegalHeirDTO legalHeirDto) {
		this.legalHeirDto = legalHeirDto;
	}

	public ViewSearchCriteriaTableDTO getDto() {
		return dto;
	}

	public void setDto(ViewSearchCriteriaTableDTO dto) {
		this.dto = dto;
	}

	public String getPolicyNo() {
		return policyNo;
	}


	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public boolean isQueryReplyStatus() {
		return queryReplyStatus;
	}

	public void setQueryReplyStatus(boolean queryReplyStatus) {
		this.queryReplyStatus = queryReplyStatus;
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

	public InitiateTalkTalkTalkDTO getInitiateTalkTalkTalkDTO() {
		return initiateTalkTalkTalkDTO;
	}

	public void setInitiateTalkTalkTalkDTO(
			InitiateTalkTalkTalkDTO initiateTalkTalkTalkDTO) {
		this.initiateTalkTalkTalkDTO = initiateTalkTalkTalkDTO;
	}

	
	
	
}
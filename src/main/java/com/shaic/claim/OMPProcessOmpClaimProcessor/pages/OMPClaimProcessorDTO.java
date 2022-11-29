package com.shaic.claim.OMPProcessOmpClaimProcessor.pages;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.OMPProcessNegotiation.pages.OMPNegotiationDetailsDTO;
import com.shaic.claim.ompviewroddetails.OMPPaymentDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDataExtaractionDTO;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.domain.Policy;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;

public class OMPClaimProcessorDTO extends AbstractTableDTO{

	public OMPClaimProcessorDTO() {
		documentDetails = new DocumentDetailsDTO();
		calculationSheetDTO = new OMPClaimProcessorCalculationSheetDTO();
		receiptOfDocumentsDTO = new ReceiptOfDocumentsDTO();
	}

	private Boolean claimType;
	
	private Boolean classificationType;
	
	@NotNull(message ="Please select Event Code Value")
	private SelectValue eventCode;
	
	@NotNull(message ="Please enter Loss Date ")
	private Date lossDate;
	
	private String lossDateStr;
	
//	@NotNull(message ="Please Enter Ailment/Loss")
	private String ailmentLoss;
	
	private String delayHrs;
	
//	@NotNull(message ="Please Enter Hospital Name")
	private String hospName;
	
	private SelectValue hospital;
	
//	@NotNull(message ="Please Enter Hospital City")
	private String hospCity;
	
//	@NotNull(message ="Please Enter Hospital Country")
	private SelectValue hospCountry;
	
//	@NotNull(message ="Please Enter Place Of Visit")
	private String placeOfVisit;
	
	@NotNull(message ="Please Select Document Type")
	private SelectValue docType;
	
	private String reasonForReconsider;
	
	@NotNull(message ="Please Select Classification")
	private SelectValue classification;
	
	@NotNull(message ="Please Select SubClassification")
	private SelectValue subClassification;
	
	@NotNull(message ="Please Enter Doctor Name")
	private String doctorName;
	
	@NotNull(message ="Please Enter Investigator Name")
	private String investigatorName;
	
	@NotNull(message ="Please Enter Advocate Name")
	private String advocateName;
	
	@NotNull(message ="Please Enter Auditor Name")
	private String auditorName;
	
	@NotNull(message ="Please Select Negotiator Name")
	private SelectValue negotiatorName;
	
	@NotNull(message ="Please Select Document Received From")
	private SelectValue documentsReceivedFrom;
	
	private Date documentsReceivedDate;
	
	@NotNull(message ="Please Select Mode Of Receipt")
	private SelectValue modeOfReceipt;
	
	@NotNull(message ="Please Select Currency Type")
	private SelectValue currencyType;
	
	@NotNull(message ="Please Enter Currency Rate")
	private Double currencyRate;
	
	@NotNull(message ="Please Enter Conversion Value")
	private Double conversionValue;
	
	private Boolean isDeductibleRecovered;
	
	private String processorRemarks;
	
	@NotNull(message ="Please Enter Sent to negotiator name")
	private SelectValue sendToNegotiatorName;
	
	@NotNull(message ="Please Enter Reason for negotiation")
	private String reasonForNegotiation;
	
//	@NotNull(message ="Please Enter Reason for Rejection")
	private String reasonForRejection;
	
//	@NotNull(message ="Please Enter Reason for Approval")
	private String reasonForApproval;

	private PreauthDataExtaractionDTO preauthDataExtractioDto;
	
	private NewIntimationDto  newIntimationDto;
	
	private ClaimDto claimDto;
	
	private String intimationId;
	
	private OMPClaimProcessorCalculationSheetDTO calculationSheetDTO;
	
	private String remarks;
	
	@NotNull(message ="Please Enter Payee Name")
	@Size(min = 1 , message = "Please Enter Payee Name")
	private String payeeNameStr;
	
	@NotNull(message ="Please Select Payment To")
	private SelectValue paymentTo;
	
	private SelectValue payMode;
	
	private String panNo;
	
	private String emailId;
	
	@NotNull(message ="Please enter Payable At")
	@Size(min = 1 , message = "Please enter Payable At")
	private String payableAt;
	
	private NewIntimationDto intimationDto;
	
	private List<ReconsiderRODRequestTableDTO> reconsiderRodRequestList;
	
	private DocumentDetailsDTO documentDetails;
	
	private SelectValue claimTypeValue;
	
	private String hospitalisationFlag;
	
	private String nonHospitalisationFlag;
	
	private Boolean hospTypeBooleanval; 
	
	private Long key;
	
	private Long stageKey;
	
	private Long statusKey;
	
	private String remarksApprover;
	
	private String userId;

	private List<UploadDocumentDTO> uploadDocsList;
	
	private Long rodKey;
	
	private String outCome;
	
	private List<OMPClaimCalculationViewTableDTO> claimCalculationViewTable;
	
	private Double inrCurrency;
	
	private String rodNumber;
	
	private Date dateofModification;
	
	private String modifyby;
	
	private String processingStage;
	
	private Double  inrConversionRate;
	
	private String eventdescription;
	
	private Long slno;
	
	private String description;
	
	private Double deductibles;
	
	private String otherurrencyCode;
	
	private String currencyName;
	
	private String Country;
	
	private Date date;
	
	private String userName;
	
	private String endorsementNo;
	
	private Date passedDate;
	
	private Date effectiveFromDt;
	
	private Date effectiveToDt;
	
	private String endorsementCode;
	
	private String endorsementDescription;
	
	private String coverCode;
	
	private Double sumInsured;
	
	private Double utilizedOtherClaims;
	
	private Double availableBalance;
	
	private Double utilizedthisclaim;
	
	private Double balanceSI;
	
	private String endorsementType;
	
	private String endorsementText;
	
	private Double endorsementSumInsured;
	
	private Double revisedSumInsured;
	
	private Double endorsementPremium;
	
	private ReceiptOfDocumentsDTO receiptOfDocumentsDTO;
	
	private Boolean isMutipleRod = Boolean.FALSE;
	
	private Boolean isDocRecHospital = Boolean.FALSE;
	
	private Boolean isDocRecInsured = Boolean.FALSE;
	
	private List<OMPNegotiationDetailsDTO> negotiationDetailsDTOs;
	
	private Double agreedAmt;
	
	private Boolean buttonflag = Boolean.FALSE;
	
	private String productCode;
	
	private Double rodProvisionAmt =0d;
	
	private Date admissionDate;
	
	private Date dischargeDate;
	
	private Double provisionAmt;
	
	private Double inrtotal;
	
	private String lossDetails;
	
	private Date lossTime;
	
	private String lossOrDelay;
	
	private BeanItemContainer<SelectValue> classificationContainer;
	
	private BeanItemContainer<SelectValue> subClassificationContainer;
	
	private BeanItemContainer<SelectValue> currencyValueContainer;
	
	private BeanItemContainer<SelectValue> documentRecievedFromContainer;
	
	private BeanItemContainer<SelectValue> paymentToContainer;
	
	private BeanItemContainer<SelectValue> paymentModeContainer;
	
	private String transectionChequeNo;
	
	private Date paymentDate;
	
	private String paymentStatus;
	
	private List<OMPPaymentDetailsTableDTO> ompPaymentDetailsList;
	
	private List<OMPNewRecoverableTableDto> ompRecoverableTableList;
	
	private Boolean isLegalFlag;
	
	private String placeEvent;
	
	private SelectValue remarksForReopn;
	
	private String remarksForClose;
	
	private Date lossOfDate;
	
	private Boolean isApproverScrn = Boolean.FALSE;
	
	private Boolean isCashless = Boolean.FALSE;
	
	private SelectValue reasonForRejectionRemarks;
	
	private String dedutEventCode;
	
	private Policy policy;
	
	private String delete;
	
	private Double age;
	
	private String plan;
	
	private Boolean isOnLoad = Boolean.FALSE;
	
	private Boolean copayEnable = Boolean.TRUE;
	
	private Date dateofAdmission;
	
	private BeanItemContainer<SelectValue> rodClaimTypeContainer;
	
	private Double deductiblesOriginal;
	
	private SelectValue patientStatus;
	
	
	//OptionGroup CGOption cgOption
			//TextField txtCGApprovedAmt cgApprovedAmt
			//TextArea txtCGRemarks  cgRemarks
	private String cgOption;
	private Long cgApprovedAmt;
	private String cgRemarks;	
	private Date cgDate;
	
	private Date deathDate;
	
	//CR2019034
	
	private String documentType;
	
	private String documentFilePath;

	private String documentSource;

	private BeanItemContainer<SelectValue> modeOfReceiptContainer;
	
	private Long ackKey;
	
	private String status;
	
	private Long rodStatus;
	public String getDocumentFilePath() {
		return documentFilePath;
	}

	public void setDocumentFilePath(String documentFilePath) {
		this.documentFilePath = documentFilePath;
	}

	public String getDocumentSource() {
		return documentSource;
	}

	public void setDocumentSource(String documentSource) {
		this.documentSource = documentSource;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getPanNo() {
		return panNo;
	}

	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPayableAt() {
		return payableAt;
	}

	public void setPayableAt(String payableAt) {
		this.payableAt = payableAt;
	}

	public Boolean getClaimType() {
		return claimType;
	}

	public void setClaimType(Boolean claimType) {
		this.claimType = claimType;
	}

	public Boolean getClassificationType() {
		return classificationType;
	}

	public void setClassificationType(Boolean classificationType) {
		this.classificationType = classificationType;
	}

	public SelectValue getEventCode() {
		return eventCode;
	}

	public void setEventCode(SelectValue eventCode) {
		this.eventCode = eventCode;
	}

	public Date getLossDate() {
		return lossDate;
	}

	public void setLossDate(Date lossDate) {
		this.lossDate = lossDate;
	}

	public String getLossDateStr() {
		return lossDateStr;
	}

	public void setLossDateStr(String lossDateStr) {
		this.lossDateStr = lossDateStr;
	}

	public String getAilmentLoss() {
		return ailmentLoss;
	}

	public void setAilmentLoss(String ailmentLoss) {
		this.ailmentLoss = ailmentLoss;
	}

	public String getDelayHrs() {
		return delayHrs;
	}

	public void setDelayHrs(String delayHrs) {
		this.delayHrs = delayHrs;
	}

	public String getHospName() {
		return hospName;
	}

	public void setHospName(String hospName) {
		this.hospName = hospName;
	}

	public String getHospCity() {
		return hospCity;
	}

	public void setHospCity(String hospCity) {
		this.hospCity = hospCity;
	}

	public SelectValue getHospCountry() {
		return hospCountry;
	}

	public void setHospCountry(SelectValue hospCountry) {
		this.hospCountry = hospCountry;
	}

	public String getPlaceOfVisit() {
		return placeOfVisit;
	}

	public void setPlaceOfVisit(String placeOfVisit) {
		this.placeOfVisit = placeOfVisit;
	}

	public SelectValue getDocType() {
		return docType;
	}

	public void setDocType(SelectValue docType) {
		this.docType = docType;
	}

	public String getReasonForReconsider() {
		return reasonForReconsider;
	}

	public void setReasonForReconsider(String reasonForReconsider) {
		this.reasonForReconsider = reasonForReconsider;
	}

	public SelectValue getClassification() {
		return classification;
	}

	public void setClassification(SelectValue classification) {
		this.classification = classification;
	}

	public SelectValue getSubClassification() {
		return subClassification;
	}

	public void setSubClassification(SelectValue subClassification) {
		this.subClassification = subClassification;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getInvestigatorName() {
		return investigatorName;
	}

	public void setInvestigatorName(String investigatorName) {
		this.investigatorName = investigatorName;
	}

	public String getAdvocateName() {
		return advocateName;
	}

	public void setAdvocateName(String advocateName) {
		this.advocateName = advocateName;
	}

	public String getAuditorName() {
		return auditorName;
	}

	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
	}

	public SelectValue getNegotiatorName() {
		return negotiatorName;
	}

	public void setNegotiatorName(SelectValue negotiatorName) {
		this.negotiatorName = negotiatorName;
	}

	public SelectValue getDocumentsReceivedFrom() {
		return documentsReceivedFrom;
	}

	public void setDocumentsReceivedFrom(SelectValue documentsReceivedFrom) {
		this.documentsReceivedFrom = documentsReceivedFrom;
	}

	public Date getDocumentsReceivedDate() {
		return documentsReceivedDate;
	}

	public void setDocumentsReceivedDate(Date documentsReceivedDate) {
		this.documentsReceivedDate = documentsReceivedDate;
	}

	public SelectValue getModeOfReceipt() {
		return modeOfReceipt;
	}

	public void setModeOfReceipt(SelectValue modeOfReceipt) {
		this.modeOfReceipt = modeOfReceipt;
	}

	public SelectValue getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(SelectValue currencyType) {
		this.currencyType = currencyType;
	}

	public Double getCurrencyRate() {
		return currencyRate;
	}

	public void setCurrencyRate(Double currencyRate) {
		this.currencyRate = currencyRate;
	}

	public Double getConversionValue() {
		return conversionValue;
	}

	public void setConversionValue(Double conversionValue) {
		this.conversionValue = conversionValue;
	}

	public Boolean getIsDeductibleRecovered() {
		return isDeductibleRecovered;
	}

	public void setIsDeductibleRecovered(Boolean isDeductibleRecovered) {
		this.isDeductibleRecovered = isDeductibleRecovered;
	}

	public String getProcessorRemarks() {
		return processorRemarks;
	}

	public void setProcessorRemarks(String processorRemarks) {
		this.processorRemarks = processorRemarks;
	}

	public SelectValue getSendToNegotiatorName() {
		return sendToNegotiatorName;
	}

	public void setSendToNegotiatorName(SelectValue sendToNegotiatorName) {
		this.sendToNegotiatorName = sendToNegotiatorName;
	}

	public String getReasonForNegotiation() {
		return reasonForNegotiation;
	}

	public void setReasonForNegotiation(String reasonForNegotiation) {
		this.reasonForNegotiation = reasonForNegotiation;
	}

	public String getReasonForRejection() {
		return reasonForRejection;
	}

	public void setReasonForRejection(String reasonForRejection) {
		this.reasonForRejection = reasonForRejection;
	}

	public String getReasonForApproval() {
		return reasonForApproval;
	}

	public void setReasonForApproval(String reasonForApproval) {
		this.reasonForApproval = reasonForApproval;
	}

	public PreauthDataExtaractionDTO getPreauthDataExtractioDto() {
		return preauthDataExtractioDto;
	}

	public void setPreauthDataExtractioDto(PreauthDataExtaractionDTO preauthDataExtractioDto) {
		this.preauthDataExtractioDto = preauthDataExtractioDto;
	}

	public NewIntimationDto getNewIntimationDto() {
		return newIntimationDto;
	}

	public void setNewIntimationDto(NewIntimationDto newIntimationDto) {
		this.newIntimationDto = newIntimationDto;
	}

	public ClaimDto getClaimDto() {
		return claimDto;
	}

	public void setClaimDto(ClaimDto claimDto) {
		this.claimDto = claimDto;
	}

	public String getIntimationId() {
		return intimationId;
	}

	public void setIntimationId(String intimationId) {
		this.intimationId = intimationId;
	}

	public OMPClaimProcessorCalculationSheetDTO getCalculationSheetDTO() {
		return calculationSheetDTO;
	}

	public void setCalculationSheetDTO(OMPClaimProcessorCalculationSheetDTO calculationSheetDTO) {
		this.calculationSheetDTO = calculationSheetDTO;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public SelectValue getPayMode() {
		return payMode;
	}

	public void setPayMode(SelectValue payMode) {
		this.payMode = payMode;
	}

	public SelectValue getPaymentTo() {
		return paymentTo;
	}

	public void setPaymentTo(SelectValue paymentTo) {
		this.paymentTo = paymentTo;
	}

	public String getPayeeNameStr() {
		return payeeNameStr;
	}

	public void setPayeeNameStr(String payeeNameStr) {
		this.payeeNameStr = payeeNameStr;
	}

	public List<ReconsiderRODRequestTableDTO> getReconsiderRodRequestList() {
		return reconsiderRodRequestList;
	}

	public void setReconsiderRodRequestList(List<ReconsiderRODRequestTableDTO> reconsiderRodRequestList) {
		this.reconsiderRodRequestList = reconsiderRodRequestList;
	}

	public NewIntimationDto getIntimationDto() {
		return intimationDto;
	}

	public void setIntimationDto(NewIntimationDto intimationDto) {
		this.intimationDto = intimationDto;
	}

	public DocumentDetailsDTO getDocumentDetails() {
		return documentDetails;
	}

	public void setDocumentDetails(DocumentDetailsDTO documentDetails) {
		this.documentDetails = documentDetails;
	}

	public SelectValue getClaimTypeValue() {
		return claimTypeValue;
	}

	public void setClaimTypeValue(SelectValue claimTypeValue) {
		this.claimTypeValue = claimTypeValue;
	}

	public String getHospitalisationFlag() {
		return hospitalisationFlag;
	}

	public void setHospitalisationFlag(String hospitalisationFlag) {
		this.hospitalisationFlag = hospitalisationFlag;
	}

	public String getNonHospitalisationFlag() {
		return nonHospitalisationFlag;
	}

	public void setNonHospitalisationFlag(String nonHospitalisationFlag) {
		this.nonHospitalisationFlag = nonHospitalisationFlag;
	}

	public Boolean getHospTypeBooleanval() {
		return hospTypeBooleanval;
	}

	public void setHospTypeBooleanval(Boolean hospTypeBooleanval) {
		this.hospTypeBooleanval = hospTypeBooleanval;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getStageKey() {
		return stageKey;
	}

	public void setStageKey(Long stageKey) {
		this.stageKey = stageKey;
	}

	public Long getStatusKey() {
		return statusKey;
	}

	public void setStatusKey(Long statusKey) {
		this.statusKey = statusKey;
	}

	public String getRemarksApprover() {
		return remarksApprover;
	}

	public void setRemarksApprover(String remarksApprover) {
		this.remarksApprover = remarksApprover;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<UploadDocumentDTO> getUploadDocsList() {
		return uploadDocsList;
	}


	public void setUploadDocsList(List<UploadDocumentDTO> uploadDocsList) {
		this.uploadDocsList = uploadDocsList;
	}

	public Long getRodKey() {
		return rodKey;
	}

	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}

	public String getOutCome() {
		return outCome;
	}

	public void setOutCome(String outCome) {
		this.outCome = outCome;
	}


	public Double getInrCurrency() {
		return inrCurrency;
	}

	public void setInrCurrency(Double inrCurrency) {
		this.inrCurrency = inrCurrency;
	}

	public String getRodNumber() {
		return rodNumber;
	}

	public void setRodNumber(String rodNumber) {
		this.rodNumber = rodNumber;
	}

	public Date getDateofModification() {
		return dateofModification;
	}

	public Double getInrConversionRate() {
		return inrConversionRate;
	}

	public void setInrConversionRate(Double inrConversionRate) {
		this.inrConversionRate = inrConversionRate;
	}

	public void setDateofModification(Date dateofModification) {
		this.dateofModification = dateofModification;
	}

	public String getModifyby() {
		return modifyby;
	}

	public void setModifyby(String modifyby) {
		this.modifyby = modifyby;
	}

	public String getProcessingStage() {
		return processingStage;
	}

	public void setProcessingStage(String processingStage) {
		this.processingStage = processingStage;
	}

	public String getEventdescription() {
		return eventdescription;
	}

	public void setEventdescription(String eventdescription) {
		this.eventdescription = eventdescription;
	}

	public Long getSlno() {
		return slno;
	}

	public void setSlno(Long slno) {
		this.slno = slno;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getDeductibles() {
		return deductibles;
	}

	public void setDeductibles(Double deductibles) {
		this.deductibles = deductibles;
	}

	public String getOtherurrencyCode() {
		return otherurrencyCode;
	}

	public void setOtherurrencyCode(String otherurrencyCode) {
		this.otherurrencyCode = otherurrencyCode;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public String getCountry() {
		return Country;
	}

	public void setCountry(String country) {
		Country = country;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEndorsementNo() {
		return endorsementNo;
	}

	public void setEndorsementNo(String endorsementNo) {
		this.endorsementNo = endorsementNo;
	}

	public Date getPassedDate() {
		return passedDate;
	}

	public void setPassedDate(Date passedDate) {
		this.passedDate = passedDate;
	}

	public Date getEffectiveFromDt() {
		return effectiveFromDt;
	}

	public void setEffectiveFromDt(Date effectiveFromDt) {
		this.effectiveFromDt = effectiveFromDt;
	}

	public Date getEffectiveToDt() {
		return effectiveToDt;
	}

	public void setEffectiveToDt(Date effectiveToDt) {
		this.effectiveToDt = effectiveToDt;
	}

	public String getEndorsementCode() {
		return endorsementCode;
	}

	public void setEndorsementCode(String endorsementCode) {
		this.endorsementCode = endorsementCode;
	}

	public String getEndorsementDescription() {
		return endorsementDescription;
	}

	public void setEndorsementDescription(String endorsementDescription) {
		this.endorsementDescription = endorsementDescription;
	}

	public String getCoverCode() {
		return coverCode;
	}

	public void setCoverCode(String coverCode) {
		this.coverCode = coverCode;
	}

	public Double getSumInsured() {
		return sumInsured;
	}

	public void setSumInsured(Double sumInsured) {
		this.sumInsured = sumInsured;
	}

	public Double getUtilizedOtherClaims() {
		return utilizedOtherClaims;
	}

	public void setUtilizedOtherClaims(Double utilizedOtherClaims) {
		this.utilizedOtherClaims = utilizedOtherClaims;
	}

	public Double getAvailableBalance() {
		return availableBalance;
	}

	public void setAvailableBalance(Double availableBalance) {
		this.availableBalance = availableBalance;
	}

	public Double getUtilizedthisclaim() {
		return utilizedthisclaim;
	}

	public void setUtilizedthisclaim(Double utilizedthisclaim) {
		this.utilizedthisclaim = utilizedthisclaim;
	}

	public Double getBalanceSI() {
		return balanceSI;
	}

	public void setBalanceSI(Double balanceSI) {
		this.balanceSI = balanceSI;
	}

	public String getEndorsementType() {
		return endorsementType;
	}

	public void setEndorsementType(String endorsementType) {
		this.endorsementType = endorsementType;
	}

	public String getEndorsementText() {
		return endorsementText;
	}

	public void setEndorsementText(String endorsementText) {
		this.endorsementText = endorsementText;
	}

	public Double getEndorsementSumInsured() {
		return endorsementSumInsured;
	}

	public void setEndorsementSumInsured(Double endorsementSumInsured) {
		this.endorsementSumInsured = endorsementSumInsured;
	}

	public Double getRevisedSumInsured() {
		return revisedSumInsured;
	}

	public void setRevisedSumInsured(Double revisedSumInsured) {
		this.revisedSumInsured = revisedSumInsured;
	}

	public Double getEndorsementPremium() {
		return endorsementPremium;
	}

	public void setEndorsementPremium(Double endorsementPremium) {
		this.endorsementPremium = endorsementPremium;
	}

	public List<OMPClaimCalculationViewTableDTO> getClaimCalculationViewTable() {
		return claimCalculationViewTable;
	}

	public void setClaimCalculationViewTable(
			List<OMPClaimCalculationViewTableDTO> claimCalculationViewTable) {
		this.claimCalculationViewTable = claimCalculationViewTable;
	}

	public ReceiptOfDocumentsDTO getReceiptOfDocumentsDTO() {
		return receiptOfDocumentsDTO;
	}

	public void setReceiptOfDocumentsDTO(ReceiptOfDocumentsDTO receiptOfDocumentsDTO) {
		this.receiptOfDocumentsDTO = receiptOfDocumentsDTO;
	}

	public Boolean getIsMutipleRod() {
		return isMutipleRod;
	}

	public void setIsMutipleRod(Boolean isMutipleRod) {
		this.isMutipleRod = isMutipleRod;
	}

	public List<OMPNegotiationDetailsDTO> getNegotiationDetailsDTOs() {
		return negotiationDetailsDTOs;
	}

	public void setNegotiationDetailsDTOs(List<OMPNegotiationDetailsDTO> negotiationDetailsDTOs) {
		this.negotiationDetailsDTOs = negotiationDetailsDTOs;
	}

	public Double getAgreedAmt() {
		return agreedAmt;
	}

	public void setAgreedAmt(Double agreedAmt) {
		this.agreedAmt = agreedAmt;
	}

	public Boolean getIsDocRecInsured() {
		return isDocRecInsured;
	}

	public void setIsDocRecInsured(Boolean isDocRecInsured) {
		this.isDocRecInsured = isDocRecInsured;
	}

	public Boolean getIsDocRecHospital() {
		return isDocRecHospital;
	}

	public void setIsDocRecHospital(Boolean isDocRecHospital) {
		this.isDocRecHospital = isDocRecHospital;
	}

	public Boolean getButtonflag() {
		return buttonflag;
	}

	public void setButtonflag(Boolean buttonflag) {
		this.buttonflag = buttonflag;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public Double getRodProvisionAmt() {
		return rodProvisionAmt;
	}

	public void setRodProvisionAmt(Double rodProvisionAmt) {
		this.rodProvisionAmt = rodProvisionAmt;
	}

	public Date getAdmissionDate() {
		return admissionDate;
	}

	public void setAdmissionDate(Date admissionDate) {
		this.admissionDate = admissionDate;
	}

	public Date getDischargeDate() {
		return dischargeDate;
	}

	public void setDischargeDate(Date dischargeDate) {
		this.dischargeDate = dischargeDate;
	}

	public Double getProvisionAmt() {
		return provisionAmt;
	}

	public void setProvisionAmt(Double provisionAmt) {
		this.provisionAmt = provisionAmt;
	}

	public Double getInrtotal() {
		return inrtotal;
	}

	public void setInrtotal(Double inrtotal) {
		this.inrtotal = inrtotal;
	}

	public String getLossDetails() {
		return lossDetails;
	}

	public void setLossDetails(String lossDetails) {
		this.lossDetails = lossDetails;
	}

	public Date getLossTime() {
		return lossTime;
	}

	public void setLossTime(Date lossTime) {
		this.lossTime = lossTime;
	}

	public String getLossOrDelay() {
		return lossOrDelay;
	}

	public void setLossOrDelay(String lossOrDelay) {
		this.lossOrDelay = lossOrDelay;
	}

	public BeanItemContainer<SelectValue> getClassificationContainer() {
		return classificationContainer;
	}

	public void setClassificationContainer(BeanItemContainer<SelectValue> classificationContainer) {
		this.classificationContainer = classificationContainer;
	}

	public BeanItemContainer<SelectValue> getSubClassificationContainer() {
		return subClassificationContainer;
	}

	public void setSubClassificationContainer(
			BeanItemContainer<SelectValue> subClassificationContainer) {
		this.subClassificationContainer = subClassificationContainer;
	}

	public BeanItemContainer<SelectValue> getCurrencyValueContainer() {
		return currencyValueContainer;
	}

	public void setCurrencyValueContainer(
			BeanItemContainer<SelectValue> currencyValueContainer) {
		this.currencyValueContainer = currencyValueContainer;
	}

	public BeanItemContainer<SelectValue> getDocumentRecievedFromContainer() {
		return documentRecievedFromContainer;
	}

	public void setDocumentRecievedFromContainer(
			BeanItemContainer<SelectValue> documentRecievedFromContainer) {
		this.documentRecievedFromContainer = documentRecievedFromContainer;
	}

	public String getTransectionChequeNo() {
		return transectionChequeNo;
	}

	public void setTransectionChequeNo(String transectionChequeNo) {
		this.transectionChequeNo = transectionChequeNo;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public BeanItemContainer<SelectValue> getPaymentToContainer() {
		return paymentToContainer;
	}

	public void setPaymentToContainer(
			BeanItemContainer<SelectValue> paymentToContainer) {
		this.paymentToContainer = paymentToContainer;
	}

	public BeanItemContainer<SelectValue> getPaymentModeContainer() {
		return paymentModeContainer;
	}

	public void setPaymentModeContainer(
			BeanItemContainer<SelectValue> paymentModeContainer) {
		this.paymentModeContainer = paymentModeContainer;
	}

	public List<OMPPaymentDetailsTableDTO> getOmpPaymentDetailsList() {
		return ompPaymentDetailsList;
	}

	public void setOmpPaymentDetailsList(List<OMPPaymentDetailsTableDTO> ompPaymentDetailsList) {
		this.ompPaymentDetailsList = ompPaymentDetailsList;
	}

	public List<OMPNewRecoverableTableDto> getOmpRecoverableTableList() {
		return ompRecoverableTableList;
	}

	public void setOmpRecoverableTableList(List<OMPNewRecoverableTableDto> ompRecoverableTableList) {
		this.ompRecoverableTableList = ompRecoverableTableList;
	}

	public Boolean getIsLegalFlag() {
		return isLegalFlag;
	}

	public void setIsLegalFlag(Boolean isLegalFlag) {
		this.isLegalFlag = isLegalFlag;
	}

	public String getPlaceEvent() {
		return placeEvent;
	}

	public void setPlaceEvent(String placeEvent) {
		this.placeEvent = placeEvent;
	}

	public SelectValue getHospital() {
		return hospital;
	}

	public void setHospital(SelectValue hospital) {
		this.hospital = hospital;
	}

	public Date getLossOfDate() {
		return lossOfDate;
	}

	public void setLossOfDate(Date lossOfDate) {
		this.lossOfDate = lossOfDate;
	}

	public Boolean getIsApproverScrn() {
		return isApproverScrn;
	}

	public void setIsApproverScrn(Boolean isApproverScrn) {
		this.isApproverScrn = isApproverScrn;
	}

	public SelectValue getRemarksForReopn() {
		return remarksForReopn;
	}

	public void setRemarksForReopn(SelectValue remarksForReopn) {
		this.remarksForReopn = remarksForReopn;
	}

	public String getRemarksForClose() {
		return remarksForClose;
	}

	public void setRemarksForClose(String remarksForClose) {
		this.remarksForClose = remarksForClose;
	}

	public Boolean getIsCashless() {
		return isCashless;
	}

	public void setIsCashless(Boolean isCashless) {
		this.isCashless = isCashless;
	}
	public SelectValue getReasonForRejectionRemarks() {
		return reasonForRejectionRemarks;
	}

	public void setReasonForRejectionRemarks(SelectValue reasonForRejectionRemarks) {
		this.reasonForRejectionRemarks = reasonForRejectionRemarks;
	}

	public String getDedutEventCode() {
		return dedutEventCode;
	}

	public void setDedutEventCode(String dedutEventCode) {
		this.dedutEventCode = dedutEventCode;
	}

	public Policy getPolicy() {
		return policy;
	}

	public void setPolicy(Policy policy) {
		this.policy = policy;
	}

	public String getDelete() {
		return delete;
	}

	public void setDelete(String delete) {
		this.delete = delete;
	}

	public Boolean getIsOnLoad() {
		return isOnLoad;
	}

	public void setIsOnLoad(Boolean isOnLoad) {
		this.isOnLoad = isOnLoad;
	}


	public Date getDateofAdmission() {
		return dateofAdmission;
	}

	public void setDateofAdmission(Date dateofAdmission) {
		this.dateofAdmission = dateofAdmission;
	}

	public Double getAge() {
		return age;
	}

	public void setAge(Double age) {
		this.age = age;
	}

	public String getPlan() {
		return plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}

	public Boolean getCopayEnable() {
		return copayEnable;
	}

	public void setCopayEnable(Boolean copayEnable) {
		this.copayEnable = copayEnable;
	}

	public BeanItemContainer<SelectValue> getRodClaimTypeContainer() {
		return rodClaimTypeContainer;
	}

	public void setRodClaimTypeContainer(BeanItemContainer<SelectValue> rodClaimTypeContainer) {
		this.rodClaimTypeContainer = rodClaimTypeContainer;
	}

	public Double getDeductiblesOriginal() {
		return deductiblesOriginal;
	}

	public void setDeductiblesOriginal(Double deductiblesOriginal) {
		this.deductiblesOriginal = deductiblesOriginal;
	}

	public SelectValue getPatientStatus() {
		return patientStatus;
	}

	public void setPatientStatus(SelectValue patientStatus) {
		this.patientStatus = patientStatus;
	}

	public String getCgOption() {
		return cgOption;
	}

	public void setCgOption(String cgOption) {
		this.cgOption = cgOption;
	}

	public Long getCgApprovedAmt() {
		return cgApprovedAmt;
	}

	public void setCgApprovedAmt(Long cgApprovedAmt) {
		this.cgApprovedAmt = cgApprovedAmt;
	}

	public String getCgRemarks() {
		return cgRemarks;
	}

	public void setCgRemarks(String cgRemarks) {
		this.cgRemarks = cgRemarks;
	}

	public Date getCgDate() {
		return cgDate;
	}

	public void setCgDate(Date cgDate) {
		this.cgDate = cgDate;
	}

	public Date getDeathDate() {
		return deathDate;
	}

	public void setDeathDate(Date deathDate) {
		this.deathDate = deathDate;
	}

	public BeanItemContainer<SelectValue> getModeOfReceiptContainer() {
		return modeOfReceiptContainer;
	}

	public void setModeOfReceiptContainer(BeanItemContainer<SelectValue> modeOfReceiptContainer) {
		this.modeOfReceiptContainer = modeOfReceiptContainer;
	}

	public Long getAckKey() {
		return ackKey;
	}

	public void setAckKey(Long ackKey) {
		this.ackKey = ackKey;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getRodStatus() {
		return rodStatus;
	}

	public void setRodStatus(Long rodStatus) {
		this.rodStatus = rodStatus;
	}
}

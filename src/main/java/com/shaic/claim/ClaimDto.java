package com.shaic.claim;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.domain.MasOmbudsman;
import com.shaic.newcode.wizard.dto.NewIntimationDto;

public class ClaimDto {
	
	private Long key;
	
	private String claimId;

	private Long claimLink;
	
	private SelectValue claimType;

	private Double claimedAmount;

	private Double claimedHomeAmount;
	
	private Double cashlessAppAmt;

	private Long claimedamountCurrencyId;

	private Long claimedhomeamountCurrencyId;

	private Long conversionFlag;

	private Long conversionLetter;

	private SelectValue conversionReason;

	private String createdBy;

	private String stageSequenceNumber;
	
	private Date createdDate;

	private Long intimationKey;
	
	private NewIntimationDto newIntimationDto;

	private Long isVipCustomer;

	private String modifiedBy;

	private Date modifiedDate;

	private String officeCode;

	private Long proamountCurrencyId;

	private Long prohomeamountCurrencyId;

	private Double provisionAmount;

	private Double provisionHomeAmount;

	private Long rejectionLetterflag;
	
	private String accDeathBenefitflag;

	private String stageName;
	
	private String statusName;
	
	private Long statusId;
	
	private Long stageId;
	
	private Long substatusId;	

	private Date statusDate;

	private Long version;
	
	private SelectValue currencyId;

	private Boolean suggestRejection;
	
	private SelectValue rejectionCategoryId;
	
	private String registrationRemarks;
	
	private String suggestedRejectionRemarks;
	
	private String rejectionRemarks;
	
	private String waiverRemarks;
	
	private String medicalRemarks;
	
	private String doctorNote;
	
	private String claimStatus;

	private Double currentProvisionAmount;
	
	private Long latestPreauthKey;
	
	private Date admissionDate;
	
	private Date dischargeDate;
	
	private Integer reminderCount;
	
	private Date firstReminderDate;
	
	private Date secondReminderDate;
	
	private Date thirdReminderDate;
	
	private Date preauthApprovedDate;
	
	private String legalFlag;
	
	private String sectionCode;
	
	private String coverCode;
	
	private String subCoverCode;
	
	private boolean incidenceFlag;
	
	private String incidenceFlagValue;
	
	private String docFilePath;
	
	private String docType;
	
	private String docToken;
	
	private Long lobId;
	
	private String processClaimType;
	
	private String hospReqFlag;
	
	private Date incidenceDate;
	
	private Date accidentDate;
	
	private Date deathDate;
	
	private Date disablementDate;
	
	private String injuryRemarks;
	
	/**
	 * For PA Covering Letter doc. check list will be selected by user
	 */

	private List<MasOmbudsman> ombudsManAddressList = new ArrayList<MasOmbudsman>();
	private String claimSectionCode;
	
	private String claimCoverCode;
	
	private String claimSubCoverCode;
	
	private String productCode;
	
	private String productName;
	
	private Date lossDateTime;

	private Double dollarInitProvisionAmount;
	
	private String hospitalisationFlag;
	
	private String nonHospitalisationFlag;
	
	private Double inrConversionRate;
	
	private Double inrTotalAmount;

	private String hospitalName;
	
	private String cityName;

	private Long countryId;

	private Long eventCodeId;
	
	private String eventCode;
	
	private String eventDescription;
	
	private String eventIdDescription;

	private String ailmentLoss;
	
	private String confirmRejectionRemarks;	

	private String hospitalType;
	
	private SelectValue hospitalCountry;
	
	private String claimTypevalue;
	
	private Long lodId;
	
	private Boolean claimTypeBoolean;
	
	private Boolean hospitalTypeBoolean;
	
	private SelectValue eventCodeValue;
	
	private Integer gmcCorpBufferLmt;
	
	private Long isgmcCorpBuffer;
	
	private List<DocumentCheckListDTO> documentCheckListDTO;
	
	private String paCoveringLetterRemarks;
	
	private Long investigationKey;
	
	private String nomineeName;
	
	private String nomineeAddr;
	
	private String gpaParentName;
	
	private Date gpaParentDOB;
	
	private Double gpaParentAge;
	
	private String gpaRiskName;
	
	private Date gpaRiskDOB;
	
	private Double gpaRiskAge;
	
	private String gpaCategory;
	
	private String gpaSection;
	
	private String placeOfvisit;
	
	private String placeOfEvent;
	
	private Date lossTime;
	
	private String lossDetails;
	
	private String PlaceLossDelay;
	
	private Long hospitalId;
	
	private String ghiAllowFlag;
	
	private String ghiAllowUser;
	
	private Double preauthClaimedAmountAsPerBill;
	
	//R1276
	private String intimationRemarks;
	
	private Long latestDiagnosisKey;

	private Boolean legalHeirDocAvailable;
	
	private String diagnosis;
	
	private String coadingFlag;
	
	private String coadingUser;
	
	private Date coadingDate;
	
	private String coadingRemark;

	private String legalClaim;
	
	private String rodNumber;
	
	private String priorityEvent;

	private Long priorityWeightage;
	
	private Date documentReceivedDate;
	
	private Long insuredKey;
	
	private String claimPriorityLabel;
	
	public Long getLatestDiagnosisKey() {
		return latestDiagnosisKey;
	}

	public void setLatestDiagnosisKey(Long latestDiagnosisKey) {
		this.latestDiagnosisKey = latestDiagnosisKey;
	}

	public String getPlaceOfvisit() {
		return placeOfvisit;
	}

	public void setPlaceOfvisit(String placeOfvisit) {
		this.placeOfvisit = placeOfvisit;
	}

	public String getPlaceOfEvent() {
		return placeOfEvent;
	}

	public void setPlaceOfEvent(String placeOfEvent) {
		this.placeOfEvent = placeOfEvent;
	}

	public Date getLossTime() {
		return lossTime;
	}

	public void setLossTime(Date lossTime) {
		this.lossTime = lossTime;
	}

	public String getLossDetails() {
		return lossDetails;
	}

	public void setLossDetails(String lossDetails) {
		this.lossDetails = lossDetails;
	}

	//For getting Bill entry dischargeDate Carousel view
	private Long rodKeyForDischargeDate;
	
	public Long getRodKeyForDischargeDate() {
		return rodKeyForDischargeDate;
	}

	public void setRodKeyForDischargeDate(Long rodKeyForDischargeDate) {
		this.rodKeyForDischargeDate = rodKeyForDischargeDate;
	}

	public String getClaimStatus() {
		return claimStatus;
	}

	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}

	public ClaimDto() {
		newIntimationDto = new NewIntimationDto();
		suggestRejection=false;
		documentCheckListDTO = new ArrayList<DocumentCheckListDTO>();
	}	
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getClaimId() {
		return claimId;
	}

	public void setClaimId(String claimId) {
		this.claimId = claimId;
	}

	public Long getClaimLink() {
		return claimLink;
	}

	public void setClaimLink(Long claimLink) {
		this.claimLink = claimLink;
	}

	public SelectValue getClaimType() {
		return claimType;
	}

	public void setClaimType(SelectValue claimType) {
		this.claimType = claimType;
	}

	public Double getClaimedAmount() {
		return claimedAmount;
	}

	public void setClaimedAmount(Double claimedAmount) {
		this.claimedAmount = claimedAmount;
	}

	public Double getClaimedHomeAmount() {
		return claimedHomeAmount;
	}

	public void setClaimedHomeAmount(Double claimedHomeAmount) {
		this.claimedHomeAmount = claimedHomeAmount;
	}

	public Long getClaimedamountCurrencyId() {
		return claimedamountCurrencyId;
	}

	public void setClaimedamountCurrencyId(Long claimedamountCurrencyId) {
		this.claimedamountCurrencyId = claimedamountCurrencyId;
	}

	public Long getClaimedhomeamountCurrencyId() {
		return claimedhomeamountCurrencyId;
	}

	public void setClaimedhomeamountCurrencyId(Long claimedhomeamountCurrencyId) {
		this.claimedhomeamountCurrencyId = claimedhomeamountCurrencyId;
	}

	public Long getConversionFlag() {
		return conversionFlag;
	}

	public void setConversionFlag(Long conversionFlag) {
		this.conversionFlag = conversionFlag;
	}

	public Long getConversionLetter() {
		return conversionLetter;
	}

	public void setConversionLetter(Long conversionLetter) {
		this.conversionLetter = conversionLetter;
	}

	public SelectValue getConversionReason() {
		return conversionReason;
	}

	public void setConversionReason(SelectValue conversionReason) {
		this.conversionReason = conversionReason;
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

	public Long getIntimationKey() {
		return intimationKey;
	}

	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
	}

	public Long getIsVipCustomer() {
		return isVipCustomer;
	}

	public void setIsVipCustomer(Long isVipCustomer) {
		this.isVipCustomer = isVipCustomer;
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

	public String getOfficeCode() {
		return officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

	public Long getProamountCurrencyId() {
		return proamountCurrencyId;
	}

	public void setProamountCurrencyId(Long proamountCurrencyId) {
		this.proamountCurrencyId = proamountCurrencyId;
	}

	public Long getProhomeamountCurrencyId() {
		return prohomeamountCurrencyId;
	}

	public void setProhomeamountCurrencyId(Long prohomeamountCurrencyId) {
		this.prohomeamountCurrencyId = prohomeamountCurrencyId;
	}

	public Double getProvisionAmount() {
		return provisionAmount;
	}

	public void setProvisionAmount(Double provisionAmount) {
		this.provisionAmount = provisionAmount;
	}

	public Double getProvisionHomeAmount() {
		return provisionHomeAmount;
	}

	public void setProvisionHomeAmount(Double provisionHomeAmount) {
		this.provisionHomeAmount = provisionHomeAmount;
	}

	public Long getRejectionLetterflag() {
		return rejectionLetterflag;
	}

	public void setRejectionLetterflag(Long rejectionLetterflag) {
		this.rejectionLetterflag = rejectionLetterflag;
	}
	
	public Long getSubstatusId() {
		return substatusId;
	}

	public void setSubstatusId(Long substatusId) {
		this.substatusId = substatusId;
	}

	public Date getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public SelectValue getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(SelectValue currencyId) {
		this.currencyId = currencyId;
	}

	public SelectValue getRejectionCategoryId() {
		return rejectionCategoryId;
	}

	public void setRejectionCategoryId(SelectValue rejectionCategoryId) {
		this.rejectionCategoryId = rejectionCategoryId;
	}

	public String getRegistrationRemarks() {
		return registrationRemarks;
	}

	public void setRegistrationRemarks(String registrationRemarks) {
		this.registrationRemarks = registrationRemarks;
	}

	public String getSuggestedRejectionRemarks() {
		return suggestedRejectionRemarks;
	}

	public void setSuggestedRejectionRemarks(String suggestedRejectionRemarks) {
		this.suggestedRejectionRemarks = suggestedRejectionRemarks;
	}

	public String getRejectionRemarks() {
		return rejectionRemarks;
	}

	public void setRejectionRemarks(String rejectionRemarks) {
		this.rejectionRemarks = rejectionRemarks;
	}

	public String getWaiverRemarks() {
		return waiverRemarks;
	}

	public void setWaiverRemarks(String waiverRemarks) {
		this.waiverRemarks = waiverRemarks;
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

	public void setDoctorNote(String doctorNote) {
		this.doctorNote = doctorNote;
	}

	public NewIntimationDto getNewIntimationDto() {
		return newIntimationDto;
	}

	public void setNewIntimationDto(NewIntimationDto newIntimationDto) {
		this.newIntimationDto = newIntimationDto;
	}

	public String getStageName() {
		return stageName;
	}

	public void setStageName(String stageName) {
		this.stageName = stageName;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public Long getStatusId() {
		return statusId;
	}

	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}

	public Long getStageId() {
		return stageId;
	}

	public void setStageId(Long stageId) {
		this.stageId = stageId;
	}

	public Boolean getSuggestRejection() {
		return suggestRejection;
	}

	public void setSuggestRejection(Boolean suggestRejection) {
		this.suggestRejection = suggestRejection;
	}

	public String getStageSequenceNumber() {
		return stageSequenceNumber;
	}

	public void setStageSequenceNumber(String sequenceNumber) {
		this.stageSequenceNumber = sequenceNumber;
	}	
	
	public String getClaimTypeValue()
	{
		String strClaimType = "";
		if(null != this.claimType)
		{
			strClaimType = this.claimType.getValue();
		}
		return strClaimType;
		//return (null != this.claimType.getValue() ? this.claimType.getValue() : "");
	}

	public Double getCurrentProvisionAmount() {
		return currentProvisionAmount;
	}

	public void setCurrentProvisionAmount(Double currentProvisionAmount) {
		this.currentProvisionAmount = currentProvisionAmount;
	}

	public Long getLatestPreauthKey() {
		return latestPreauthKey;
	}

	public void setLatestPreauthKey(Long latestPreauthKey) {
		this.latestPreauthKey = latestPreauthKey;
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

	public Integer getReminderCount() {
		return reminderCount;
	}

	public void setReminderCount(Integer reminderCount) {
		this.reminderCount = reminderCount;
	}

	public Date getFirstReminderDate() {
		return firstReminderDate;
	}

	public void setFirstReminderDate(Date firstReminderDate) {
		this.firstReminderDate = firstReminderDate;
	}

	public Date getSecondReminderDate() {
		return secondReminderDate;
	}

	public void setSecondReminderDate(Date secondReminderDate) {
		this.secondReminderDate = secondReminderDate;
	}

	public Date getThirdReminderDate() {
		return thirdReminderDate;
	}

	public void setThirdReminderDate(Date thirdReminderDate) {
		this.thirdReminderDate = thirdReminderDate;
	}

	public Date getPreauthApprovedDate() {
		return preauthApprovedDate;
	}

	public void setPreauthApprovedDate(Date preauthApprovedDate) {
		this.preauthApprovedDate = preauthApprovedDate;
	}

	public Double getCashlessAppAmt() {
		return cashlessAppAmt;
	}

	public void setCashlessAppAmt(Double cashlessAppAmt) {
		this.cashlessAppAmt = cashlessAppAmt;
	}

	public String getLegalFlag() {
		return legalFlag;
	}

	public void setLegalFlag(String legalFlag) {
		this.legalFlag = legalFlag;
	}
	public String getDocFilePath() {
		return docFilePath;
	}

	public void setDocFilePath(String docFilePath) {
		this.docFilePath = docFilePath;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(String sectionCode) {
		this.sectionCode = sectionCode;
	}

	public String getCoverCode() {
		return coverCode;
	}

	public void setCoverCode(String coverCode) {
		this.coverCode = coverCode;
	}

	public String getSubCoverCode() {
		return subCoverCode;
	}

	public void setSubCoverCode(String subCoverCode) {
		this.subCoverCode = subCoverCode;
	}

	public boolean getIncidenceFlag() {
		return incidenceFlag;
	}

	public void setIncidenceFlag(boolean incidenceFlag) {
		this.incidenceFlag = incidenceFlag;
	}

	public String getIncidenceFlagValue() {
		return incidenceFlagValue;
	}

	public void setIncidenceFlagValue(String incidenceFlagValue) {
		this.incidenceFlagValue = incidenceFlagValue;
		
		if(this.incidenceFlagValue != null && !this.incidenceFlagValue.isEmpty() && ("A").equalsIgnoreCase(this.incidenceFlagValue)){

			 this.incidenceFlag = true;
		}
		else if(this.incidenceFlagValue != null && !this.incidenceFlagValue.isEmpty() && ("D").equalsIgnoreCase(this.incidenceFlagValue)){
			this.incidenceFlag = false;
		}		
	}

	public Long getLobId() {
		return lobId;
	}

	public void setLobId(Long lobId) {
		this.lobId = lobId;
	}

	public String getProcessClaimType() {
		return processClaimType;
	}

	public void setProcessClaimType(String processClaimType) {
		this.processClaimType = processClaimType;
	}

	public String getHospReqFlag() {
		return hospReqFlag;
	}

	public void setHospReqFlag(String hospReqFlag) {
		this.hospReqFlag = hospReqFlag;
	}
	
	public Date getIncidenceDate() {
		return incidenceDate;
	}

	public void setIncidenceDate(Date incidenceDate) {
		this.incidenceDate = incidenceDate;
	}

	public String getInjuryRemarks() {
		return injuryRemarks;
	}

	public void setInjuryRemarks(String injuryRemarks) {
		this.injuryRemarks = injuryRemarks;
	}
	
	public List<MasOmbudsman> getOmbudsManAddressList() {
		return ombudsManAddressList;
	}

	public void setOmbudsManAddressList(List<MasOmbudsman> ombudsManAddressList) {
		this.ombudsManAddressList = ombudsManAddressList;
	}


	public String getClaimSectionCode() {
		return claimSectionCode;
	}

	public void setClaimSectionCode(String claimSectionCode) {
		this.claimSectionCode = claimSectionCode;
	}

	public String getClaimCoverCode() {
		return claimCoverCode;
	}

	public void setClaimCoverCode(String claimCoverCode) {
		this.claimCoverCode = claimCoverCode;
	}

	public String getClaimSubCoverCode() {
		return claimSubCoverCode;
	}

	public void setClaimSubCoverCode(String claimSubCoverCode) {
		this.claimSubCoverCode = claimSubCoverCode;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Date getLossDateTime() {
		return lossDateTime;
	}

	public void setLossDateTime(Date lossDateTime) {
		this.lossDateTime = lossDateTime;
	}

	public Double getDollarInitProvisionAmount() {
		return dollarInitProvisionAmount;
	}

	public void setDollarInitProvisionAmount(Double dollarInitProvisionAmount) {
		this.dollarInitProvisionAmount = dollarInitProvisionAmount;
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

	public Double getInrConversionRate() {
		return inrConversionRate;
	}

	public void setInrConversionRate(Double inrConversionRate) {
		this.inrConversionRate = inrConversionRate;
	}

	public Double getInrTotalAmount() {
		return inrTotalAmount;
	}

	public void setInrTotalAmount(Double inrTotalAmount) {
		this.inrTotalAmount = inrTotalAmount;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public Long getEventCodeId() {
		return eventCodeId;
	}

	public void setEventCodeId(Long eventCodeId) {
		this.eventCodeId = eventCodeId;
	}

	public String getAilmentLoss() {
		return ailmentLoss;
	}

	public void setAilmentLoss(String ailmentLoss) {
		this.ailmentLoss = ailmentLoss;
	}

	public String getConfirmRejectionRemarks() {
		return confirmRejectionRemarks;
	}

	public void setConfirmRejectionRemarks(String confirmRejectionRemarks) {
		this.confirmRejectionRemarks = confirmRejectionRemarks;
	}	

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	public String getEventIdDescription() {

		eventIdDescription = eventCode != null ? (eventCode +(" - " + (eventDescription != null ? eventDescription : ""))) : "";
		return eventIdDescription;
	}

	public String getHospitalType() {
		return hospitalType;
	}

	public void setHospitalType(String hospitalType) {
		this.hospitalType = hospitalType;
	}

	public SelectValue getHospitalCountry() {
		return hospitalCountry;
	}

	public void setHospitalCountry(SelectValue hospitalCountry) {
		this.hospitalCountry = hospitalCountry;
	}

	public String getClaimTypevalue() {
		return claimTypevalue;
	}

	public void setClaimTypevalue(String claimTypevalue) {
		this.claimTypevalue = claimTypevalue;
	}

	public Long getLodId() {
		return lodId;
	}

	public void setLodId(Long lodId) {
		this.lodId = lodId;
	}

	public Boolean getClaimTypeBoolean() {
		return claimTypeBoolean;
	}

	public void setClaimTypeBoolean(Boolean claimTypeBoolean) {
		this.claimTypeBoolean = claimTypeBoolean;
	}

	public Boolean getHospitalTypeBoolean() {
		return hospitalTypeBoolean;
	}

	public void setHospitalTypeBoolean(Boolean hospitalTypeBoolean) {
		this.hospitalTypeBoolean = hospitalTypeBoolean;
	}

	public SelectValue getEventCodeValue() {
		return eventCodeValue;
	}

	public void setEventCodeValue(SelectValue eventCodeValue) {
		this.eventCodeValue = eventCodeValue;
	}
	public Long getIsgmcCorpBuffer() {
		return isgmcCorpBuffer;
	}

	public void setIsgmcCorpBuffer(Long isgmcCorpBuffer) {
		this.isgmcCorpBuffer = isgmcCorpBuffer;
	}

	public Integer getGmcCorpBufferLmt() {
		return gmcCorpBufferLmt;
	}

	public void setGmcCorpBufferLmt(Integer gmcCorpBufferLmt) {
		this.gmcCorpBufferLmt = gmcCorpBufferLmt;
	}

	public boolean isIncidenceFlag() {
		return incidenceFlag;
	}

	
	
	public List<DocumentCheckListDTO> getDocumentCheckListDTO() {
		return documentCheckListDTO;
	}

	public void setDocumentCheckListDTO(
			List<DocumentCheckListDTO> documentCheckListDTO) {
		this.documentCheckListDTO = documentCheckListDTO;
	}

	public String getPaCoveringLetterRemarks() {
		return paCoveringLetterRemarks;
	}

	public void setPaCoveringLetterRemarks(String paCoveringLetterRemarks) {
		this.paCoveringLetterRemarks = paCoveringLetterRemarks;
	}


	public Long getInvestigationKey() {
		return investigationKey;
	}

	public void setInvestigationKey(Long investigationKey) {
		this.investigationKey = investigationKey;
	}

	public String getNomineeName() {
		return nomineeName;
	}

	public void setNomineeName(String nomineeName) {
		this.nomineeName = nomineeName;
	}

	public String getNomineeAddr() {
		return nomineeAddr;
	}

	public void setNomineeAddr(String nomineeAddr) {
		this.nomineeAddr = nomineeAddr;
	}

	public Date getAccidentDate() {
		return accidentDate;
	}

	public void setAccidentDate(Date accidentDate) {
		this.accidentDate = accidentDate;
	}

	public Date getDeathDate() {
		return deathDate;
	}

	public void setDeathDate(Date deathDate) {
		this.deathDate = deathDate;
	}

	public Date getDisablementDate() {
		return disablementDate;
	}

	public void setDisablementDate(Date disablementDate) {
		this.disablementDate = disablementDate;
	}

	public String getDocToken() {
		return docToken;
	}

	public void setDocToken(String docToken) {
		this.docToken = docToken;
	}

	public String getAccDeathBenefitflag() {
		return accDeathBenefitflag;
	}

	public void setAccDeathBenefitflag(String accDeathBenefitflag) {
		this.accDeathBenefitflag = accDeathBenefitflag;
	}
	
	public String getGpaParentName() {
		return gpaParentName;
	}

	public void setGpaParentName(String gpaParentName) {
		this.gpaParentName = gpaParentName;
	}

	public Date getGpaParentDOB() {
		return gpaParentDOB;
	}

	public void setGpaParentDOB(Date gpaParentDOB) {
		this.gpaParentDOB = gpaParentDOB;
	}

	public Double getGpaParentAge() {
		return gpaParentAge;
	}

	public void setGpaParentAge(Double gpaParentAge) {
		this.gpaParentAge = gpaParentAge;
	}

	public String getGpaRiskName() {
		return gpaRiskName;
	}

	public void setGpaRiskName(String gpaRiskName) {
		this.gpaRiskName = gpaRiskName;
	}

	public Date getGpaRiskDOB() {
		return gpaRiskDOB;
	}

	public void setGpaRiskDOB(Date gpaRiskDOB) {
		this.gpaRiskDOB = gpaRiskDOB;
	}

	public Double getGpaRiskAge() {
		return gpaRiskAge;
	}

	public void setGpaRiskAge(Double gpaRiskAge) {
		this.gpaRiskAge = gpaRiskAge;
	}

	public String getGpaCategory() {
		return gpaCategory;
	}

	public void setGpaCategory(String gpaCategory) {
		this.gpaCategory = gpaCategory;
	}

	public String getGpaSection() {
		return gpaSection;
	}

	public void setGpaSection(String gpaSection) {
		this.gpaSection = gpaSection;
	}

	public String getPlaceLossDelay() {
		return PlaceLossDelay;
	}

	public void setPlaceLossDelay(String placeLossDelay) {
		PlaceLossDelay = placeLossDelay;
	}

	public Long getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Long hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getGhiAllowFlag() {
		return ghiAllowFlag;
	}

	public void setGhiAllowFlag(String ghiAllowFlag) {
		this.ghiAllowFlag = ghiAllowFlag;
	}

	public String getGhiAllowUser() {
		return ghiAllowUser;
	}

	public void setGhiAllowUser(String ghiAllowUser) {
		this.ghiAllowUser = ghiAllowUser;
	}

	public Double getPreauthClaimedAmountAsPerBill() {
		return preauthClaimedAmountAsPerBill;
	}

	public void setPreauthClaimedAmountAsPerBill(
			Double preauthClaimedAmountAsPerBill) {
		this.preauthClaimedAmountAsPerBill = preauthClaimedAmountAsPerBill;
	}
	
	//R1276
	public String getIntimationRemarks() {
		return intimationRemarks;
	}

	public void setIntimationRemarks(String intimationRemarks) {
		this.intimationRemarks = intimationRemarks;
	}
	
	public Boolean getLegalHeirDocAvailable() {
		return legalHeirDocAvailable;
	}

	public void setLegalHeirDocAvailable(Boolean legalHeirDocAvailable) {
		this.legalHeirDocAvailable = legalHeirDocAvailable;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public String getCoadingFlag() {
		return coadingFlag;
	}

	public void setCoadingFlag(String coadingFlag) {
		this.coadingFlag = coadingFlag;
	}

	public String getCoadingUser() {
		return coadingUser;
	}

	public void setCoadingUser(String coadingUser) {
		this.coadingUser = coadingUser;
	}

	public Date getCoadingDate() {
		return coadingDate;
	}

	public void setCoadingDate(Date coadingDate) {
		this.coadingDate = coadingDate;
	}

	public String getCoadingRemark() {
		return coadingRemark;
	}

	public void setCoadingRemark(String coadingRemark) {
		this.coadingRemark = coadingRemark;
	}	
	
	public String getRodNumber() {	
		return rodNumber;	
	}
	
	public void setRodNumber(String rodNumber) {	
		this.rodNumber = rodNumber;	
	}
	
	public String getLegalClaim() {
		return legalClaim;
	}

	public void setLegalClaim(String legalClaim) {
		this.legalClaim = legalClaim;
	}

	public String getPriorityEvent() {
		return priorityEvent;
	}

	public void setPriorityEvent(String priorityEvent) {
		this.priorityEvent = priorityEvent;
	}

	public Long getPriorityWeightage() {
		return priorityWeightage;
	}

	public void setPriorityWeightage(Long priorityWeightage) {
		this.priorityWeightage = priorityWeightage;
	}

	public Date getDocumentReceivedDate() {
		return documentReceivedDate;
	}

	public void setDocumentReceivedDate(Date documentReceivedDate) {
		this.documentReceivedDate = documentReceivedDate;
	}

	public Long getInsuredKey() {
		return insuredKey;
	}

	public void setInsuredKey(Long insuredKey) {
		this.insuredKey = insuredKey;
	}

	public String getClaimPriorityLabel() {
		return claimPriorityLabel;
	}

	public void setClaimPriorityLabel(String claimPriorityLabel) {
		this.claimPriorityLabel = claimPriorityLabel;
	}	
	
}

package com.shaic.newcode.wizard.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;

public class ProcessRejectionDTO extends SearchTableDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Double claimedAmount;
	
	private String currencyName;
	
	private Long intimationKey;
	
	private Boolean isPremedical=false;
	
	@NotNull(message="Select Rejection Category")
	private SelectValue rejectionCategory;
	
	@NotNull(message="Please Enter Rejection Remarks")
	private String confirmRemarks;
	
	@NotNull(message="Please Enter Medical Remarks")
	private String medicalRemarks;
	
	private String doctorNote;
	
	private String suggestion;
	
	@NotNull(message="Please Enter Remarks")
	private String waiveRemarks;
	
	private String waiveProvisionAmt;

	private Double provisionAmt;
	
	private String claimNumber;
	
	private boolean claimType;
	
	private boolean hospitalisation;
	
	private String intimationNumber;
	
	private String registerRemarks;
	
	private SelectValue rejection;
	
	private String rejectionRemarks;
	
	private String rejectionStatus;
	
	private String premedicalCategory;
	
	private Long premedicalCategoryId;
	
	private Long statusKey;
	
	private SelectValue eventCode;
	
	private String eventCodeValue;
	
	private String ailmentLossDetails;
	
	private String eventLossDate;	
	
	private String inrConversionRate;
	
	private String inrValue;	
	
	private String hospitalName;
	
	private String hospitalCity;
	
	private String hospitalCounty;
	
	private boolean accDeathFlag;
	
	private String accDeathValue;
	
	private Date accDeathDate;
	
	private String injuryLossDetails;
	
	private Date dateOfDeath;
	
	private Date dateOfAccident;
	
	private Date dateOfDisablement;
	
	private PreauthDTO preauthDTO;	
	
	private String crmFlagged;
	
	private NewIntimationDto newIntimationDTO;
	
	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public String getClaimNumber() {
		return claimNumber;
	}

	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}

	public String getRegisterRemarks() {
		return registerRemarks;
	}

	public void setRegisterRemarks(String registerRemarks) {
		this.registerRemarks = registerRemarks;
	}

	public SelectValue getRejection() {
		return rejection;
	}

	public void setRejection(SelectValue rejection) {
		this.rejection = rejection;
	}

	public String getRejectionRemarks() {
		return rejectionRemarks;
	}

	public void setRejectionRemarks(String rejectionRemarks) {
		this.rejectionRemarks = rejectionRemarks;
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

	public Double getClaimedAmount() {
		return claimedAmount;
	}

	public void setClaimedAmount(Double claimedAmount) {
		this.claimedAmount = claimedAmount;
	}

	public void setDoctorNote(String doctorNote) {
		this.doctorNote = doctorNote;
	}

	public String getWaiveRemarks() {
		return waiveRemarks;
	}

	public void setWaiveRemarks(String waiveRemarks) {
		this.waiveRemarks = waiveRemarks;
	}

	public String getWaiveProvisionAmt() {
		return waiveProvisionAmt;
	}

	public void setWaiveProvisionAmt(String waiveProvisionAmt) {
		this.waiveProvisionAmt = waiveProvisionAmt;
	}

	public Double getProvisionAmt() {
		return provisionAmt;
	}

	public void setProvisionAmt(Double provisionAmt) {
		this.provisionAmt = provisionAmt;
	}

	public String getIntimationNumber() {
		return intimationNumber;
	}

	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}

	public String getSuggestion() {
		return suggestion;
	}

	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}

	public SelectValue getRejectionCategory() {
		return rejectionCategory;
	}

	public void setRejectionCategory(SelectValue rejectionCategory) {
		this.rejectionCategory = rejectionCategory;
	}

	public String getConfirmRemarks() {
		return confirmRemarks;
	}

	public void setConfirmRemarks(String confirmRemarks) {
		this.confirmRemarks = confirmRemarks;
	}

	public Long getIntimationKey() {
		return intimationKey;
	}

	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
	}

	public String getRejectionStatus() {
		return rejectionStatus;
	}

	public void setRejectionStatus(String rejectionStatus) {
		rejectionStatus="Suggested Rejection";
		this.rejectionStatus = rejectionStatus;
	}

	public String getPremedicalCategory() {
		return premedicalCategory;
	}

	public void setPremedicalCategory(String premedicalCategory) {
		this.premedicalCategory = premedicalCategory;
	}

	public Long getPremedicalCategoryId() {
		return premedicalCategoryId;
	}

	public void setPremedicalCategoryId(Long premedicalCategoryId) {
		this.premedicalCategoryId = premedicalCategoryId;
	}

	public Boolean getIsPremedical() {
		return isPremedical;
	}

	public void setIsPremedical(Boolean isPremedical) {
		this.isPremedical = isPremedical;
	}

	public Long getStatusKey() {
		return statusKey;
	}

	public void setStatusKey(Long statusKey) {
		this.statusKey = statusKey;
	}

	public String getEventCodeValue() {
		return eventCodeValue;
	}

	public void setEventCodeValue(String eventCodeValue) {
		this.eventCodeValue = eventCodeValue;
	}

	public String getAilmentLossDetails() {
		return ailmentLossDetails;
	}

	public void setAilmentLossDetails(String ailmentLossDetails) {
		this.ailmentLossDetails = ailmentLossDetails;
	}

	public String getEventLossDate() {
		return eventLossDate;
	}

	public void setEventLossDate(String eventLossDate) {
		this.eventLossDate = eventLossDate;
	}

	public boolean isClaimType() {
		return claimType;
	}

	public void setClaimType(boolean claimType) {
		this.claimType = claimType;
	}

	public boolean isHospitalisation() {
		return hospitalisation;
	}

	public void setHospitalisation(boolean hospitalisation) {
		this.hospitalisation = hospitalisation;
	}

	public String getInrConversionRate() {
		return inrConversionRate;
	}

	public void setInrConversionRate(String inrConversionRate) {
		this.inrConversionRate = inrConversionRate;
	}

	public String getInrValue() {
		return inrValue;
	}

	public void setInrValue(String inrValue) {
		this.inrValue = inrValue;
	}

	public SelectValue getEventCode() {
		return eventCode;
	}

	public void setEventCode(SelectValue eventCode) {
		this.eventCode = eventCode;
		
		if(this.eventCode != null){
			this.eventCodeValue = this.eventCode.getValue();
		}
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getHospitalCity() {
		return hospitalCity;
	}

	public void setHospitalCity(String hospitalCity) {
		this.hospitalCity = hospitalCity;
	}

	public String getHospitalCounty() {
		return hospitalCounty;
	}

	public void setHospitalCounty(String hospitalCounty) {
		this.hospitalCounty = hospitalCounty;
	}        
	public boolean isAccDeathFlag() {
		return accDeathFlag;
	}

	public void setAccDeathFlag(boolean accDeathFlag) {
		this.accDeathFlag = accDeathFlag;
	}

	public Date getAccDeathDate() {
		return accDeathDate;
	}

	public void setAccDeathDate(Date accDeathDate) {
		this.accDeathDate = accDeathDate;
	}

	public String getInjuryLossDetails() {
		return injuryLossDetails;
	}

	public void setInjuryLossDetails(String injuryLossDetails) {
		this.injuryLossDetails = injuryLossDetails;
	}

	public String getAccDeathValue() {
		return accDeathValue;
	}

	public void setAccDeathValue(String accDeathValue) {
		this.accDeathValue = accDeathValue;
		if(this.accDeathValue != null && !this.accDeathValue.isEmpty())
		accDeathFlag = (SHAConstants.ACCIDENT_FLAG).equalsIgnoreCase(this.accDeathValue) ? true : false;
	}

	public Date getDateOfDeath() {
		return dateOfDeath;
	}

	public void setDateOfDeath(Date dateOfDeath) {
		this.dateOfDeath = dateOfDeath;
	}

	public Date getDateOfAccident() {
		return dateOfAccident;
	}

	public void setDateOfAccident(Date dateOfAccident) {
		this.dateOfAccident = dateOfAccident;
	}

	public Date getDateOfDisablement() {
		return dateOfDisablement;
	}

	public void setDateOfDisablement(Date dateOfDisablement) {
		this.dateOfDisablement = dateOfDisablement;
	}

	public PreauthDTO getPreauthDTO() {
		return preauthDTO;
	}

	public void setPreauthDTO(PreauthDTO preauthDTO) {
		this.preauthDTO = preauthDTO;
	}

	public NewIntimationDto getNewIntimationDTO() {
		return newIntimationDTO;
	}

	public void setNewIntimationDTO(NewIntimationDto newIntimationDTO) {
		this.newIntimationDTO = newIntimationDTO;
	}

	public String getCrmFlagged() {
		return crmFlagged;
	}

	public void setCrmFlagged(String crmFlagged) {
		this.crmFlagged = crmFlagged;
	}	
}

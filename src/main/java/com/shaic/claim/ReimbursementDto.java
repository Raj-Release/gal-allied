package com.shaic.claim;

import java.util.Date;
import java.util.List;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.newcode.wizard.dto.LegalHeirDTO;
/**
 * 
 * @author Lakshminarayana
 *
 */

public class ReimbursementDto {

		private Long key;
		
		private DocAcknowledgementDto docAcknowledgementDto;
		
		private ClaimDto claimDto;
		
		private LegalHeirDTO legalHeirDto;
		
		private String rodNumber;
		
		private Long bankId;
		
		private Long paymentModeId;
		
		private String payeeName;    
		
		private String payeeEmailId;
			
		private String panNumber;  
	
		private String reasonForChange; 
		
		private String legalHeirFirstName; 
		
		private String legalHeirMiddleName; 
		
		private String legalHeirLastName; 
		
		private String payableAt;  
		
		private String accountNumber;  
		
		private Date dateOfAdmission;
		
		private String doaChangeReason;
				
		private SelectValue roomCategory;    
		
		private Integer numberOfDays;     
		
		private Long natureOfTreatmentId;    
		
		private Date consultationDate;          
		
		private Integer criticalIllnessFlag;     
		
		private Long criticalIllnessId;    
		
		private Date dateOfDischarge;  
			
		private Integer corporateBufferFlag;     
		
		private Long illnessId;    
		
		private Integer automaticRestoration;
			
		private String systemOfMedicine;  
		
		private Long hopsitaliztionDuetoId;    
		
		private Long injuryCauseId;    
		
		private Date injuryDate;          
		
		private String  medicoLeagalCare;   
		
		private String reportedToPolice;   
		
		private String attachedPoliceReport;   
		
		private String firNumber; 
		
		private Date firstDiseaseDetectedDate;     
		
		private Date DateOfDelivery;          
		
		private Long treatmentTypeId;    
			
		private String treatmentRemarks; 
		
		private Long patientStatusId;    
		
		private Date dateOfDeath;  
		
		private String deathReason; 
		
		private String causeOfAccident;
				
		private Long rejectionCategoryId;    
		
		private Long rejectionSubCategoryId;
		
		private String rejectionRemarks; 
				
		private String approvalRemarks; 
				
		private String billingRemarks; 
				
		private Integer preHospitalizationDays;     
				
		private Integer postHospitalizationDays;     
			
		private String domicillary;   
				
		private Double billingApprovedAmount;  
				
		private Double financialApprovedAmount;  
				
		private Double claimApprovalAmount;
		
		private Double approvedAmount;  
				
		private Double netPayableAmount;
		
		private Double claimRestrictionAmount;  
				
		private Double cashlessApprovedAmount;  
				
		private Double payableToHospital;  
				
		private Double payableToInsured;  
				
		private Double tdsAmount;  
				
		private Double afterTdsPayableToHospital;  
				
		private Double deductedBalancePremium;  
				
		private Double afterPremiumPayableInsured;  
				
		private String financialApprovalRemarks; 
				
		private Long activeStatus;     
				
		private String officeCode;  
				
		private SelectValue stageSelectValue;    
				
		private SelectValue statusSelectValue;    
				
		private String createdBy;  
				
		private Date createdDate;
		
		private String diagnosis;
		
		private Double addOnCoversApprovedAmount;
		
		private Double optionalApprovedAmount;
		
		private Double benApprovedAmt;

		private SelectValue benefitSelected;
		
		private String nomineeName;
		
		private String nomineeAddr;
		
		private String legalHeirAddress;
		
		private Double nonAllopathicDiffAmount;
		
		private Double nonAllopathicApprvalAmt;
		
		private List<LegalHeirDTO> legalHeirDTOList;
		
		private String nomineeDeceasedFlag;
		
		private Boolean isNomineeDeceased;

		private String ventilatorSuppport;
		
		public Long getKey() {
			return key;
		}

		public void setKey(Long key) {
			this.key = key;
		}

		public DocAcknowledgementDto getDocAcknowledgementDto() {
			return docAcknowledgementDto;
		}

		public void setDocAcknowledgementDto(DocAcknowledgementDto docAcknowledgementDto) {
			this.docAcknowledgementDto = docAcknowledgementDto;
		}

		public ClaimDto getClaimDto() {
			return claimDto;
		}

		public void setClaimDto(ClaimDto claimDto) {
			this.claimDto = claimDto;
		}

		public String getRodNumber() {
			return rodNumber;
		}

		public void setRodNumber(String rodNumber) {
			this.rodNumber = rodNumber;
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

		public String getReasonForChange() {
			return reasonForChange;
		}

		public void setReasonForChange(String reasonForChange) {
			this.reasonForChange = reasonForChange;
		}

		public String getLegalHeirFirstName() {
			return legalHeirFirstName;
		}

		public void setLegalHeirFirstName(String legalHeirFirstName) {
			this.legalHeirFirstName = legalHeirFirstName;
		}

		public String getLegalHeirMiddleName() {
			return legalHeirMiddleName;
		}

		public void setLegalHeirMiddleName(String legalHeirMiddleName) {
			this.legalHeirMiddleName = legalHeirMiddleName;
		}

		public String getLegalHeirLastName() {
			return legalHeirLastName;
		}

		public void setLegalHeirLastName(String legalHeirLastName) {
			this.legalHeirLastName = legalHeirLastName;
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

		public Date getDateOfAdmission() {
			return dateOfAdmission;
		}

		public void setDateOfAdmission(Date dateOfAdmission) {
			this.dateOfAdmission = dateOfAdmission;
		}

		public String getDoaChangeReason() {
			return doaChangeReason;
		}

		public void setDoaChangeReason(String doaChangeReason) {
			this.doaChangeReason = doaChangeReason;
		}

		public SelectValue getRoomCategory() {
			return roomCategory;
		}

		public void setRoomCategoryId(SelectValue roomCategory) {
			this.roomCategory = roomCategory;
		}

		public Integer getNumberOfDays() {
			return numberOfDays;
		}

		public void setNumberOfDays(Integer numberOfDays) {
			this.numberOfDays = numberOfDays;
		}

		public Long getNatureOfTreatmentId() {
			return natureOfTreatmentId;
		}

		public void setNatureOfTreatmentId(Long natureOfTreatmentId) {
			this.natureOfTreatmentId = natureOfTreatmentId;
		}

		public Date getConsultationDate() {
			return consultationDate;
		}

		public void setConsultationDate(Date consultationDate) {
			this.consultationDate = consultationDate;
		}

		public Integer getCriticalIllnessFlag() {
			return criticalIllnessFlag;
		}

		public void setCriticalIllnessFlag(Integer criticalIllnessFlag) {
			this.criticalIllnessFlag = criticalIllnessFlag;
		}

		public Long getCriticalIllnessId() {
			return criticalIllnessId;
		}

		public void setCriticalIllnessId(Long criticalIllnessId) {
			this.criticalIllnessId = criticalIllnessId;
		}

		public Date getDateOfDischarge() {
			return dateOfDischarge;
		}

		public void setDateOfDischarge(Date dateOfDischarge) {
			this.dateOfDischarge = dateOfDischarge;
		}

		public Integer getCorporateBufferFlag() {
			return corporateBufferFlag;
		}

		public void setCorporateBufferFlag(Integer corporateBufferFlag) {
			this.corporateBufferFlag = corporateBufferFlag;
		}

		public Long getIllnessId() {
			return illnessId;
		}

		public void setIllnessId(Long illnessId) {
			this.illnessId = illnessId;
		}

		public Integer getAutomaticRestoration() {
			return automaticRestoration;
		}

		public void setAutomaticRestoration(Integer automaticRestoration) {
			this.automaticRestoration = automaticRestoration;
		}

		public String getSystemOfMedicine() {
			return systemOfMedicine;
		}

		public void setSystemOfMedicine(String systemOfMedicine) {
			this.systemOfMedicine = systemOfMedicine;
		}

		public Long getHopsitaliztionDuetoId() {
			return hopsitaliztionDuetoId;
		}

		public void setHopsitaliztionDuetoId(Long hopsitaliztionDuetoId) {
			this.hopsitaliztionDuetoId = hopsitaliztionDuetoId;
		}

		public Long getInjuryCauseId() {
			return injuryCauseId;
		}

		public void setInjuryCauseId(Long injuryCauseId) {
			this.injuryCauseId = injuryCauseId;
		}

		public Date getInjuryDate() {
			return injuryDate;
		}

		public void setInjuryDate(Date injuryDate) {
			this.injuryDate = injuryDate;
		}

		public String getMedicoLeagalCare() {
			return medicoLeagalCare;
		}

		public void setMedicoLeagalCare(String medicoLeagalCare) {
			this.medicoLeagalCare = medicoLeagalCare;
		}

		public String getReportedToPolice() {
			return reportedToPolice;
		}

		public void setReportedToPolice(String reportedToPolice) {
			this.reportedToPolice = reportedToPolice;
		}

		public String getAttachedPoliceReport() {
			return attachedPoliceReport;
		}

		public void setAttachedPoliceReport(String attachedPoliceReport) {
			this.attachedPoliceReport = attachedPoliceReport;
		}

		public String getFirNumber() {
			return firNumber;
		}

		public void setFirNumber(String firNumber) {
			this.firNumber = firNumber;
		}

		public Date getFirstDiseaseDetectedDate() {
			return firstDiseaseDetectedDate;
		}

		public void setFirstDiseaseDetectedDate(Date firstDiseaseDetectedDate) {
			this.firstDiseaseDetectedDate = firstDiseaseDetectedDate;
		}

		public Date getDateOfDelivery() {
			return DateOfDelivery;
		}

		public void setDateOfDelivery(Date dateOfDelivery) {
			DateOfDelivery = dateOfDelivery;
		}

		public Long getTreatmentTypeId() {
			return treatmentTypeId;
		}

		public void setTreatmentTypeId(Long treatmentTypeId) {
			this.treatmentTypeId = treatmentTypeId;
		}

		public String getTreatmentRemarks() {
			return treatmentRemarks;
		}

		public void setTreatmentRemarks(String treatmentRemarks) {
			this.treatmentRemarks = treatmentRemarks;
		}

		public Long getPatientStatusId() {
			return patientStatusId;
		}

		public void setPatientStatusId(Long patientStatusId) {
			this.patientStatusId = patientStatusId;
		}

		public Date getDateOfDeath() {
			return dateOfDeath;
		}

		public void setDateOfDeath(Date dateOfDeath) {
			this.dateOfDeath = dateOfDeath;
		}

		public String getDeathReason() {
			return deathReason;
		}

		public void setDeathReason(String deathReason) {
			this.deathReason = deathReason;
		}

		public Long getRejectionCategoryId() {
			return rejectionCategoryId;
		}

		public void setRejectionCategoryId(Long rejectionCategoryId) {
			this.rejectionCategoryId = rejectionCategoryId;
		}

		public String getRejectionRemarks() {
			return rejectionRemarks;
		}

		public void setRejectionRemarks(String rejectionRemarks) {
			this.rejectionRemarks = rejectionRemarks;
		}

		public String getApprovalRemarks() {
			return approvalRemarks;
		}

		public void setApprovalRemarks(String approvalRemarks) {
			this.approvalRemarks = approvalRemarks;
		}

		public String getBillingRemarks() {
			return billingRemarks;
		}

		public void setBillingRemarks(String billingRemarks) {
			this.billingRemarks = billingRemarks;
		}

		public Integer getPreHospitalizationDays() {
			return preHospitalizationDays;
		}

		public void setPreHospitalizationDays(Integer preHospitalizationDays) {
			this.preHospitalizationDays = preHospitalizationDays;
		}

		public Integer getPostHospitalizationDays() {
			return postHospitalizationDays;
		}

		public void setPostHospitalizationDays(Integer postHospitalizationDays) {
			this.postHospitalizationDays = postHospitalizationDays;
		}

		public String getDomicillary() {
			return domicillary;
		}

		public void setDomicillary(String domicillary) {
			this.domicillary = domicillary;
		}

		public Double getBillingApprovedAmount() {
			return billingApprovedAmount;
		}

		public void setBillingApprovedAmount(Double billingApprovedAmount) {
			this.billingApprovedAmount = billingApprovedAmount;
		}

		public Double getFinancialApprovedAmount() {
			return financialApprovedAmount;
		}

		public void setFinancialApprovedAmount(Double financialApprovedAmount) {
			this.financialApprovedAmount = financialApprovedAmount;
		}

		public Double getApprovedAmount() {
			return approvedAmount;
		}

		public void setApprovedAmount(Double approvedAmount) {
			this.approvedAmount = approvedAmount;
		}

		public Double getNetPayableAmount() {
			return netPayableAmount;
		}

		public void setNetPayableAmount(Double netPayableAmount) {
			this.netPayableAmount = netPayableAmount;
		}

		public Double getClaimRestrictionAmount() {
			return claimRestrictionAmount;
		}

		public void setClaimRestrictionAmount(Double claimRestrictionAmount) {
			this.claimRestrictionAmount = claimRestrictionAmount;
		}

		public Double getCashlessApprovedAmount() {
			return cashlessApprovedAmount;
		}

		public void setCashlessApprovedAmount(Double cashlessApprovedAmount) {
			this.cashlessApprovedAmount = cashlessApprovedAmount;
		}

		public Double getPayableToHospital() {
			return payableToHospital;
		}

		public void setPayableToHospital(Double payableToHospital) {
			this.payableToHospital = payableToHospital;
		}

		public Double getPayableToInsured() {
			return payableToInsured;
		}

		public void setPayableToInsured(Double payableToInsured) {
			this.payableToInsured = payableToInsured;
		}

		public Double getTdsAmount() {
			return tdsAmount;
		}

		public void setTdsAmount(Double tdsAmount) {
			this.tdsAmount = tdsAmount;
		}

		public Double getAfterTdsPayableToHospital() {
			return afterTdsPayableToHospital;
		}

		public void setAfterTdsPayableToHospital(Double afterTdsPayableToHospital) {
			this.afterTdsPayableToHospital = afterTdsPayableToHospital;
		}

		public Double getDeductedBalancePremium() {
			return deductedBalancePremium;
		}

		public void setDeductedBalancePremium(Double deductedBalancePremium) {
			this.deductedBalancePremium = deductedBalancePremium;
		}

		public Double getAfterPremiumPayableInsured() {
			return afterPremiumPayableInsured;
		}

		public void setAfterPremiumPayableInsured(Double afterPremiumPayableInsured) {
			this.afterPremiumPayableInsured = afterPremiumPayableInsured;
		}

		public String getFinancialApprovalRemarks() {
			return financialApprovalRemarks;
		}

		public void setFinancialApprovalRemarks(String financialApprovalRemarks) {
			this.financialApprovalRemarks = financialApprovalRemarks;
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
		
		
		public SelectValue getStageSelectValue() {
			return stageSelectValue;
		}

		public void setStageSelectValue(SelectValue stageSelectValue) {
			this.stageSelectValue = stageSelectValue;
		}

		public SelectValue getStatusSelectValue() {
			return statusSelectValue;
		}

		public void setStatusSelectValue(SelectValue statusSelectValue) {
			this.statusSelectValue = statusSelectValue;
		}

		public void setRoomCategory(SelectValue roomCategory) {
			this.roomCategory = roomCategory;
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

		public String getDiagnosis() {
			return diagnosis;
		}

		public void setDiagnosis(String diagnosis) {
			this.diagnosis = diagnosis;
		}

		public Double getAddOnCoversApprovedAmount() {
			return addOnCoversApprovedAmount;
		}

		public void setAddOnCoversApprovedAmount(Double addOnCoversApprovedAmount) {
			this.addOnCoversApprovedAmount = addOnCoversApprovedAmount;
		}

		public Double getOptionalApprovedAmount() {
			return optionalApprovedAmount;
		}

		public void setOptionalApprovedAmount(Double optionalApprovedAmount) {
			this.optionalApprovedAmount = optionalApprovedAmount;
		}

		public Double getBenApprovedAmt() {
			return benApprovedAmt;
		}

		public void setBenApprovedAmt(Double benApprovedAmt) {
			this.benApprovedAmt = benApprovedAmt;
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

		public Double getClaimApprovalAmount() {
			return claimApprovalAmount;
		}

		public void setClaimApprovalAmount(Double claimApprovalAmount) {
			this.claimApprovalAmount = claimApprovalAmount;
		}

		public String getLegalHeirAddress() {
			return legalHeirAddress;
		}

		public void setLegalHeirAddress(String legalHeirAddress) {
			this.legalHeirAddress = legalHeirAddress;
		}

		public Double getNonAllopathicDiffAmount() {
			return nonAllopathicDiffAmount;
		}

		public void setNonAllopathicDiffAmount(Double nonAllopathicDiffAmount) {
			this.nonAllopathicDiffAmount = nonAllopathicDiffAmount;
		}

		public Double getNonAllopathicApprvalAmt() {
			return nonAllopathicApprvalAmt;
		}

		public void setNonAllopathicApprvalAmt(Double nonAllopathicApprvalAmt) {
			this.nonAllopathicApprvalAmt = nonAllopathicApprvalAmt;
		}

		public Long getRejectionSubCategoryId() {
			return rejectionSubCategoryId;
		}

		public void setRejectionSubCategoryId(Long rejectionSubCategoryId) {
			this.rejectionSubCategoryId = rejectionSubCategoryId;
		}

		public List<LegalHeirDTO> getLegalHeirDTOList() {
			return legalHeirDTOList;
		}

		public void setLegalHeirDTOList(List<LegalHeirDTO> legalHeirDTOList) {
			this.legalHeirDTOList = legalHeirDTOList;
		}

		public LegalHeirDTO getLegalHeirDto() {
			return legalHeirDto;
		}

		public void setLegalHeirDto(LegalHeirDTO legalHeirDto) {
			this.legalHeirDto = legalHeirDto;
		}

		public SelectValue getBenefitSelected() {
			return benefitSelected;
		}

		public void setBenefitSelected(SelectValue benefitSelected) {
			this.benefitSelected = benefitSelected;
		}						

		public String getCauseOfAccident() {
			return causeOfAccident;
		}

		public void setCauseOfAccident(String causeOfAccident) {
			this.causeOfAccident = causeOfAccident;
		}
		
		public String getNomineeDeceasedFlag() {
			return nomineeDeceasedFlag;
		}

		public void setNomineeDeceasedFlag(String nomineeDeceasedFlag) {
			this.nomineeDeceasedFlag = nomineeDeceasedFlag;
			if(this.nomineeDeceasedFlag != null) {
				this.isNomineeDeceased = this.nomineeDeceasedFlag.equalsIgnoreCase(SHAConstants.YES_FLAG) ? true : false;
			}
		}

		public Boolean getIsNomineeDeceased() {
			return isNomineeDeceased;
		}

		public void setIsNomineeDeceased(Boolean isNomineeDeceased) {
			this.isNomineeDeceased = isNomineeDeceased;
			this.setNomineeDeceasedFlag((this.isNomineeDeceased != null && this.isNomineeDeceased) ? "Y" : "N" );
		}

		public String getVentilatorSuppport() {
			return ventilatorSuppport;
		}

		public void setVentilatorSuppport(String ventilatorSuppport) {
			this.ventilatorSuppport = ventilatorSuppport;
		}
}
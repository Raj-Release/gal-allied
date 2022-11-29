package com.shaic.claim.preauth.wizard.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.premedical.dto.NoOfDaysCell;
import com.shaic.claim.premedical.dto.OtherBenefitsTableDto;
import com.shaic.claim.reimbursement.billing.dto.AddOnBenefitsDTO;
import com.shaic.claim.reimbursement.billing.dto.PatientCareDTO;
import com.shaic.claim.reimbursement.dto.OtherClaimDetailsDTO;
import com.shaic.claim.reimbursement.dto.OtherClaimDiagnosisDTO;
import com.shaic.claim.reimbursement.dto.ZonalReviewUpdateHospitalDetailsDTO;
import com.shaic.claim.rod.wizard.dto.SectionDetailsTableDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.ReferenceTable;
import com.shaic.paclaim.billing.processclaimbilling.page.billreview.AddOnCoverOnLoadDTO;
import com.shaic.paclaim.billing.processclaimbilling.page.billreview.OptionalCoversDTO;
import com.shaic.paclaim.billing.processclaimbilling.page.billreview.PABillingConsolidatedDTO;
import com.shaic.paclaim.billing.processclaimbilling.page.billreview.TableBenefitsDTO;
import com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search.AddOnCoversTableDTO;
import com.shaic.paclaim.rod.wizard.dto.PABenefitsDTO;

public class PreauthDataExtaractionDTO {

	private String referenceNo;
	
	private String reasonForAdmission;
	
	//@NotNull(message = "Please Select Admission Date.")
	@NotNull(message = "Please Select Date of Admission")
	private Date admissionDate;
	

	
	private String admissionDateStr;
	
	@NotNull(message = "Please Enter Discharge Date")
	private Date dischargeDate;
	
	private Date dischargeDateForPa;
	
	
	private String dischargeDateStr;
	
	private String noOfDays;
	
	
	private String relapseIllness;
	
	private String statusValue;
	
	private SelectValue natureOfTreatment;
	
	private SelectValue hospitalisationDueTo;
	
	@NotNull(message = "Please Select Pre-Auth Type.")
	private SelectValue preAuthType;
	
	private String systemOfMedicine;
	
	@NotNull(message = "Please Enter atleast one Diagnosis.")
	private String diagnosis;
	
	private Date firstConsultantDate;
	
	private Boolean subLimit;
	
	private Boolean corpBuffer;

	private Long corporateBufferFlag;
	
	private Boolean criticalIllness;
	
	private String caReasonForRefferingToBilling;
	
	private String caApproverRemarks;
	
	private String faReasonForRefferingToBilling;
	
	private String faApproverRemarks;
	
	private String maReasonForRefferingToBilling;
	
	private String maApproverRemarks;
	
	private Long criticalIllnessFlag;
	
	private SelectValue specifyIllness;
	
	private String terminateCoverFlag;
	
	private String autoRestoration;
	
	private int restorationCount;
	
	private SelectValue illness;
	
	private Double alreadyPaidAmt;
	
	private Double netPayableAmt;

	private SelectValue covid19Variant;
	
	private SelectValue cocktailDrug;
	
	private String cocktailDrugFlag;
	
	//@NotNull(message = "Please Choose Date of Death.")
	private Date deathDate;
	
	@NotNull(message = "Please Enter Reason for Death.")
	private String reasonForDeath;
	
	@NotNull(message = "Please Select Terminate Cover.")
	private SelectValue terminateCover;
	
	@NotNull(message = "Please Select Room Category.")
	private SelectValue roomCategory;
	
//	@NotNull(message = "Please Select Treatment Type.")
	private SelectValue treatmentType;
	
	private SelectValue treatmentTypeForPA;
	
	@NotNull(message = "Please Enter Preauth Requested Amount.")
	private Double preauthReqAmt;
	
//	@NotNull(message = "Please Enter Treatment Remarks.")
//	@Size(min = 1 , message = "Please Enter Treatment Remarks")
	private String treatmentRemarks;
	
	//New field for treatmentRemarks which is disabled.
//	@NotNull(message = "Please Enter Treatment Remarks.")
//	@Size(min = 1 , message = "Please Enter Treatment Remarks")
	private String enhancementTreatmentRemarks;
	
	private SelectValue sublimitSpecify;
	
	@NotNull(message = "Please Enter preauthApprAmt.")
	private String preauthApprAmt;
	
	@NotNull(message = "Please Enter enhancementReqAmt.")
	private String enhancementReqAmt;
	
	@NotNull(message = "Please Enter patientStatus.")
	private SelectValue patientStatus;
	
	private String billingRemarks;
	
	private String medicalRemarks;

	private Boolean enhancementType;
	
	private SelectValue sumInsured;
	
	private SelectValue sublimitSumInsured;
	
	private SelectValue section;
	
	private Date treatmentStartDate;
	
	private Date treatmentEndDate;
	
	private String hospitalizationNoOfDays;
	
	private List<ProcedureDTO> procedureList;
	
	private List<ProcedureDTO> newProcedureList;
	
	private List<SpecialityDTO> specialityList;
	
	private List<DiagnosisDetailsTableDTO> diagnosisTableList;
	
	private List<OtherClaimDiagnosisDTO> otherClaimDetailsList;
	
	private UploadDocumentDTO uploadDocumentDTO;
	
	private List<UploadDocumentDTO> uploadDocumentDTOList;
	
	private List<AddOnBenefitsDTO> addOnBenefitsDTOList;
	
	private  List<TableBenefitsDTO> benefitDTOList;
	
	private  List<PABillingConsolidatedDTO> billingConsolidatedDTOList;
	
	private List<PatientCareDTO> treatmentDateList;
	
	private SectionDetailsTableDTO sectionDetailsDTO;
	
	private List<SectionDetailsTableDTO> sectionDetailsListDto;
	
	private Boolean changeInDiagnosis;
	
	private Boolean verifiedPolicySchedule;
	
	private String verifiedPolicyScheduleFlag;
	
	private Boolean accOrDeath;
	
	private Date dateOfDeathAcc;
	
	private String remarksBillEntry;
	
	private String dateOfDeathAccStr;
	
	private List<SelectValue> coverListContainer;
	
	private List<SelectValue> optionalCoverListContainer;
	
	private List<SelectValue> addOnCoverListContainer;
	
	private Double consolidatedPayableAmt = 0d;
	
	private Boolean isRTAButtonEnable = false;
	
	private SelectValue ptcaCabg;
	
	private Boolean ventilatorSupport;
	
	private String ventilatorSupportFlag;
	
	private List<ImplantDetailsDTO> implantDetailsDTOs;
	
	private Boolean implantApplicable = false;
	
	public OtherClaimDetailsDTO getOtherClaimDetails() {
		return otherClaimDetails;
	}

	public void setOtherClaimDetails(OtherClaimDetailsDTO otherClaimDetails) {
		this.otherClaimDetails = otherClaimDetails;
	}

	private List<NoOfDaysCell> claimedDetailsList;
	
	private List<NoOfDaysCell> claimedDetailsListForBenefitSheet;
	
	@NotNull(message = "Please Choose Interim or Final Enhancement.")
	private Boolean interimOrFinalEnhancement;
	
	private String interimOrFinalEnhancementFlag;
	
	private String changeOfDOA;
	
	private String changeInReasonDOA;
	
	private String changeInReasonDOD;
	
	private String approvedAmount;
	
	private Double totalApprAmt;
	
	private Double approvedAmountAftDeduction = 0d;
	
	private Double preauthTotalApprAmt;
	
	private String amountInWords;
	
	private Date injuryDate;
	
	private SelectValue causeOfInjury;
	
	private String medicalLegalCaseFlag;
	
	private Boolean medicalLegalCase;
	
	private Boolean workOrNonWorkPlace = false;
	
	private String workOrNonWorkPlaceFlag;
	
	private Boolean reportedToPolice;
	
	private String reportedToPoliceFlag;
	
	private Boolean policeReportAttached;
	
	private String policeReportAttachedFlag;
	
	private String firNumber;
	
	private Date diseaseFirstDetectedDate;
	
	@NotNull(message = "Please Select Delivery Date.")
	private Date deliveryDate;
	
	private SelectValue typeOfDelivery;
	
	private Boolean coveredPreviousClaim;
	
	private String coveredPreviousClaimFlag;

	private String preHospitalisationPeriod ;
	
	private String postHospitalisationPeriod ;
	
	private Boolean domicillaryHospitalisation;
	
	private String domicillaryHospitalisationFlag;
	
	private OtherClaimDetailsDTO otherClaimDetails;
	
	private ZonalReviewUpdateHospitalDetailsDTO updateHospitalDetails;
	
	private DocAcknowledgement docAckknowledgement;
	
	private Boolean hospitalCashAddonBenefits;
	
	private Boolean patientCareAddOnBenefits;
	
	private String hospitalCashAddonBenefitsFlag;
	
	private SelectValue reconsiderationRequest;
	
	private SelectValue reasonForReconsideration;
	
	private Integer corpBufferAllocatedClaim = 0;
	
	private Double corpBufferSI = 0d;
	private Double corpBufferUtilisedAmt = 0d;
	private Double corpBufferLimit = 0d;
	private Double corpBufferAvalBal = 0d;
	
	private List<PABillingConsolidatedDTO> paBillingConsolidatedDTOList;
	
	private Double payableToInsured;
	
	private String organisationName;
	
	private Double paSumInsured;
	
	private String parentName;
	
	private Date dateOfBirth;
	
	private String dateOfBirthValue;
	
	private String riskName;
	
	private Double age;	
	
	private Date gpaRiskDOB;
	
	private Double gpaRiskAge;
	
	private SelectValue gpaCategory;
	
	private String gpaSection;
	
	private String billingInternalRemarks;
	
	private String faInternalRemarks;
	
	private String reconsiderationFlag;

	private String amtClaimed;
	
	private String disCntHospBill;
	
	private String netAmt;
	
	private Integer investigatorsCount;
	
	private Integer invsReplyReceivedCount;
	
	private Boolean isMultipleInvsAssigned = Boolean.FALSE;
	private Boolean isNegotiationInitiated = Boolean.FALSE;
	
	private SelectValue catastrophicLoss;
	private SelectValue natureOfLoss;
	private SelectValue causeOfLoss;

	private SelectValue category;
	
	private SelectValue homeCareTreatment;
	
	private Boolean preAuthOrPreaAuthFinalBill;
	
	private String nomineeDeceasedFlag;
	
	private Boolean isNomineeDeceased;
	private String bufferType;
	
	private Integer corpBufferUtilizedAmt = 0;
	
	private String bufferTypeValue;
	
	private Integer nonPayableAmt = 0;
	
	private Integer deductibleAmt = 0;
	
    private Integer empBufferAvlBal = 0;
	
	private Integer winBufferAvlBal = 0;
	
	private Integer NacBufferAvlBal = 0;
	
	public List<UploadDocumentDTO> getUploadDocumentDTOList() {
		return uploadDocumentDTOList;
	}

	public void setUploadDocumentDTOList(
			List<UploadDocumentDTO> uploadDocumentDTOList) {
		this.uploadDocumentDTOList = uploadDocumentDTOList;
	}

	private String patientCareAddOnBenefitsFlag;
	
	public UploadDocumentDTO getUploadDocumentDTO() {
		return uploadDocumentDTO;
	}

	public void setUploadDocumentDTO(UploadDocumentDTO uploadDocumentDTO) {
		this.uploadDocumentDTO = uploadDocumentDTO;
	}

	private String hospitalCashNoofDays;
	
	private String hospitalCashPerDayAmt;
	
	private String hospitalCashTotalClaimedAmt;
	
	private String patientCareNoofDays;
	
	private String patientCarePerDayAmt;
	
	private String patientCareTotalClaimedAmt;
	
	private Boolean treatmentPhysiotherapy;
	
	private String treatmentPhysiotherapyFlag;
	
	private List<PatientCareDTO> patientCareDTO;
	
	private Boolean paymentMode;
	
	private Long paymentModeFlag;
	
	private SelectValue payeeName;
	
	private String payeeRelationship;
	
//	@NotNull(message = "Please Enter Reason for Changing Payee Name")
//	@Size(min = 1 , message = "Please Enter Reason for Changing Payee Name")
	private String reasonForChange;
	
	private String panNo;
	
	private String legalFirstName;
	
	private String legalMiddleName;
	
	private String legalLastName;
	
	private String payableAt;
	
	private Long docAcknowledgementKey;
	
//	@NotNull(message = "Please Enter Account No")
//	@Size(min = 1 , message = "Please Enter Account No")
	private String accountNo;
	
	private String accountPref;
	
	private String accType;
	
	private String nameAsPerBank;	
	
//	@NotNull(message = "Please Enter Ifsc code")
//	@Size(min = 1 , message = "Please Enter Ifsc code")
	private String ifscCode;
	
	private String branch;
	
	private String bankName;
	
	private String city;
	
	private String emailId;
	
	private Long bankId;
	
	private String billingMedicalReason;
    
    private String billingMedicalRemarks;
    
    private String claimApprovalMedicalReason;
    
    private String claimApprovalMedicalRemarks;
    
    private String financialApprovalMedicalReason;
    
    private String financialApprovalMedicalRemarks;
    
    private SelectValue paBenefits;
    
    /*
     * This variable is added to set the benefits id
     * if its already existing. This would occur in
     * case of referring to medical approver from billing or FA.
     * **/
    private Long onloadBenefitId;
    
    private String investigatorName;
    
    private SelectValue investigationNameVal;
	
	private String investigationReviewRemarks;
	private Boolean investigationReportReviewed;
	
	private String investigationReportReviewedFlag;
	
	private List<PABenefitsDTO> paBenefitsList;
	
	private List<PABenefitsDTO> deletedPaBenefitsList;
	
	private List<AddOnCoversTableDTO> addOnCoversTableList;
	
	private List<com.shaic.paclaim.billing.processclaimbilling.page.billreview.AddOnCoversTableDTO> addOnCoversTableListBilling;
	
	private List<com.shaic.paclaim.billing.processclaimbilling.page.billreview.AddOnCoversTableDTO> addOnAllCoversTableListBilling;
	
	private List<AddOnCoverOnLoadDTO> addOnCoverNameList;
	
	private List<AddOnCoversTableDTO> optionalCoversTableList;
	
	private Boolean otherBenfitOpt;
	
	private int otherBeneitApplicableFlag;
	
	private String otherBenfitFlag;
	
	List<OtherBenefitsTableDto> otherBenefitsList;
	
	private Double totalOtherBenefitsApprovedAmt;
 	
	
	private List<OptionalCoversDTO> optionalCoversTableListBilling;
	
	private Date accidentDeathDate;
	
	private Boolean accidentOrDeath;
	
	private String accidentOrDeathFlag;
	
	@NotNull(message = "Please Select Cause Of Accident")
	private SelectValue causeOfAccident;
	
	private Date dftDate;
	
	private Boolean preExistingDisabilities;
	
	private String preExistingDisablitiesFlag;
	
	private String disabilitesRemarks;
	
	private String benefitsValue;
	
	private List<AddOnCoversTableDTO> addOnCoversTableDeletedList;
	
	private List<AddOnCoversTableDTO> optionalCoversTableDeletedList;
	
	private List<SelectValue> benefitsValueContainer;
	
	private String availableSI;
	
	private Date dateOfDeath;
	
	private Date dateOfAccident;
	
	private Date dateOfDisablement;
	
	private String payModeChangeReason;
	
	private Boolean isFvrOrInvsInitiated = Boolean.FALSE;
	
	private Boolean isFvrInitiated = Boolean.FALSE;
	
	private Boolean isInvsInitiated = Boolean.FALSE;
	//private Date dischargeDateForPA;
	
	private Integer hospitalizationClaimedAmount;
	private Integer preHospitalizationClaimedAmount;
	private Integer postHospitalizationClaimedAmount;
	private Integer otherBenefitclaimedAmount;
	
	private Integer hospClaimedAmountDocRec;	
	private Integer preHospClaimedAmountDocRec;	
	private Integer postHospClaimedAmountDocRec;	
	private Integer otherBenefitsAmountDocRec;
	
	private String documentVerificationFlag;	
	private Boolean documentVerification;
	//R1257
	private Boolean notAdheringToANHReport;
	private String notAdheringToANHReportFlag;
	private List<TreatingDoctorDTO> treatingDoctorDTOs;
	
	//
	private String escalatePccRemarksvalue;

	private Boolean isStarGrpCorApproveBtn = Boolean.FALSE;
	
	private SelectValue hospitalCashDueTo;

	private Boolean patientDayCare;
	
	private SelectValue patientDayCareDueTo;

	private SelectValue admissionType;

	private Boolean isHomeIsolationExist;	
	
	private Boolean emergency=false;
	
	private String emergenyFlag;
	
	private Integer nacBufferUtilizedAmount = 0;
	
	private Boolean isDiscreationaryAplicable = false;	
	
	private Boolean isWintageApplicable =false;	
	
	private Boolean isNacbApplicable = false;
	
   private Boolean isDisBufUtlzed = false;	
	
	private Boolean isWinBufUtlzed =false;	
	
	private Boolean isNacbufUtilzed = false;
	
	public String getAvailableSI() {
		return availableSI;
	}

	public void setAvailableSI(String availableSI) {
		this.availableSI = availableSI;
	}

	public List<SelectValue> getBenefitsValueContainer() {
		return benefitsValueContainer;
	}

	public void setBenefitsValueContainer(
			List<SelectValue> benefitsValueContainer) {
		this.benefitsValueContainer = benefitsValueContainer;
	}

	public PreauthDataExtaractionDTO()
	{
	  procedureList = new ArrayList<ProcedureDTO>();
	  newProcedureList = new ArrayList<ProcedureDTO>();
	  specialityList = new ArrayList<SpecialityDTO>();
	  claimedDetailsList = new ArrayList<NoOfDaysCell>();
	  claimedDetailsListForBenefitSheet = new ArrayList<NoOfDaysCell>();
	  diagnosisTableList = new ArrayList<DiagnosisDetailsTableDTO>();
	  updateHospitalDetails = new ZonalReviewUpdateHospitalDetailsDTO();
	  otherClaimDetails = new OtherClaimDetailsDTO();
	  otherClaimDetailsList = new ArrayList<OtherClaimDiagnosisDTO>();
	  patientCareDTO = new ArrayList<PatientCareDTO>();
	  uploadDocumentDTO = new UploadDocumentDTO();	
	  uploadDocumentDTOList = new ArrayList<UploadDocumentDTO>();
	  addOnBenefitsDTOList = new ArrayList<AddOnBenefitsDTO>();
	  treatmentDateList = new ArrayList<PatientCareDTO>();
	  sectionDetailsDTO = new SectionDetailsTableDTO();
	  otherBenefitsList = new ArrayList<OtherBenefitsTableDto>();
	  totalOtherBenefitsApprovedAmt= 0d;
	  treatingDoctorDTOs = new ArrayList<TreatingDoctorDTO>();
	}
	
	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public String getReasonForAdmission() {
		return reasonForAdmission;
	}

	public void setReasonForAdmission(String reasonForAdmission) {
		this.reasonForAdmission = reasonForAdmission;
	}

	public Date getAdmissionDate() {
		return admissionDate;
	}

	public void setAdmissionDate(Date admissionDate) {
		this.admissionDate = admissionDate;
	}

	public String getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(String noOfDays) {
		this.noOfDays = noOfDays;
	}

	public SelectValue getNatureOfTreatment() {
		return natureOfTreatment;
	}


	
	public SelectValue getIllness() {
		return illness;
	}

	public void setIllness(SelectValue illness) {
		this.illness = illness;
	}

	public void setNatureOfTreatment(SelectValue natureOfTreatment) {
		this.natureOfTreatment = natureOfTreatment;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public Date getFirstConsultantDate() {
		return firstConsultantDate;
	}

	public void setFirstConsultantDate(Date firstConsultantDate) {
		this.firstConsultantDate = firstConsultantDate;
	}

	public Boolean getSubLimit() {
		return subLimit;
	}

	public void setSubLimit(Boolean subLimit) {
		this.subLimit = subLimit;
	}

	public Boolean getEnhancementType() {
		return enhancementType;
	}

	public void setEnhancementType(Boolean enhancementType) {
		this.enhancementType = enhancementType;
	}

	public Boolean getCorpBuffer() {
		return corpBuffer;
	}


	public void setCorpBuffer(Boolean corpBuffer) {
		this.corpBuffer = corpBuffer;
		this.corporateBufferFlag = this.corpBuffer != null && this.corpBuffer ? 1l : 0l;
	}

	public Boolean getCriticalIllness() {
		return criticalIllness;
	}

	/*public OptionGroup getPaymentMode() {
		return paymentMode;
	}*/

	/*public void setPaymentMode(OptionGroup paymentMode) {
		this.paymentMode = paymentMode;
		
		this.paymentModeFlag = null != this.paymentMode && paymentMode ? ReferenceTable.PAYMENT_MODE_CHEQUE_DD : ReferenceTable.PAYMENT_MODE_BANK_TRANSFER; 
	}*/

	public Long getPaymentModeFlag() {
		return paymentModeFlag;
	}

	public void setPaymentModeFlag(Long paymentModeFlag) {
		this.paymentModeFlag = paymentModeFlag;
	}

	public SelectValue getPayeeName() {
		return payeeName;
	}

	public String getEmailId() {
		return emailId;
	}

	public List<PatientCareDTO> getTreatmentDateList() {
		return treatmentDateList;
	}

	public void setTreatmentDateList(List<PatientCareDTO> treatmentDateList) {
		this.treatmentDateList = treatmentDateList;
	}

	public List<AddOnBenefitsDTO> getAddOnBenefitsDTOList() {
		return addOnBenefitsDTOList;
	}

	public void setAddOnBenefitsDTOList(List<AddOnBenefitsDTO> addOnBenefitsDTOList) {
		this.addOnBenefitsDTOList = addOnBenefitsDTOList;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
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

	public void setCriticalIllness(Boolean criticalIllness) {
		this.criticalIllness = criticalIllness;
		
		this.criticalIllnessFlag = this.criticalIllness != null && criticalIllness ? 1l : 0l ;
	}

	public SelectValue getSpecifyIllness() {
		return specifyIllness;
	}


	public List<NoOfDaysCell> getClaimedDetailsList() {
		return claimedDetailsList;
	}

	public void setClaimedDetailsList(List<NoOfDaysCell> claimedDetailsList) {
		this.claimedDetailsList = claimedDetailsList;
	}


	public void setSpecifyIllness(SelectValue specifyIllness) {
		this.specifyIllness = specifyIllness;
	}

	public List<PatientCareDTO> getPatientCareDTO() {
		return patientCareDTO;
	}

	public void setPatientCareDTO(List<PatientCareDTO> patientCareDTO) {
		this.patientCareDTO = patientCareDTO;
	}

	public Boolean getTreatmentPhysiotherapy() {
		return treatmentPhysiotherapy;
		
	}

	public void setTreatmentPhysiotherapy(Boolean treatmentPhysiotherapy) {
		this.treatmentPhysiotherapy = treatmentPhysiotherapy;
		this.setTreatmentPhysiotherapyFlag((this.treatmentPhysiotherapy != null && this.treatmentPhysiotherapy) ? "Y" : "N" );
	}

	public String getTreatmentPhysiotherapyFlag() {
		return treatmentPhysiotherapyFlag;
	}

	public void setTreatmentPhysiotherapyFlag(String treatmentPhysiotherapyFlag) {
		this.treatmentPhysiotherapyFlag = treatmentPhysiotherapyFlag;
		if(this.treatmentPhysiotherapyFlag != null) {
			this.treatmentPhysiotherapy = this.treatmentPhysiotherapyFlag.toLowerCase().equalsIgnoreCase("y") ? true : false;
		}
	}

	public String getHospitalCashNoofDays() {
		return hospitalCashNoofDays;
	}

	public void setHospitalCashNoofDays(String hospitalCashNoofDays) {
		this.hospitalCashNoofDays = hospitalCashNoofDays;
	}

	public String getHospitalCashPerDayAmt() {
		return hospitalCashPerDayAmt;
	}

	public void setHospitalCashPerDayAmt(String hospitalCashPerDayAmt) {
		this.hospitalCashPerDayAmt = hospitalCashPerDayAmt;
	}

	public String getHospitalCashTotalClaimedAmt() {
		return hospitalCashTotalClaimedAmt;
	}

	public void setHospitalCashTotalClaimedAmt(String hospitalCashTotalClaimedAmt) {
		this.hospitalCashTotalClaimedAmt = hospitalCashTotalClaimedAmt;
	}

	public String getPatientCareNoofDays() {
		return patientCareNoofDays;
	}

	public void setPatientCareNoofDays(String patientCareNoofDays) {
		this.patientCareNoofDays = patientCareNoofDays;
	}

	public String getPatientCarePerDayAmt() {
		return patientCarePerDayAmt;
	}

	public void setPatientCarePerDayAmt(String patientCarePerDayAmt) {
		this.patientCarePerDayAmt = patientCarePerDayAmt;
	}

	public String getPatientCareTotalClaimedAmt() {
		return patientCareTotalClaimedAmt;
	}

	public void setPatientCareTotalClaimedAmt(String patientCareTotalClaimedAmt) {
		this.patientCareTotalClaimedAmt = patientCareTotalClaimedAmt;
	}

	public Boolean getHospitalCashAddonBenefits() {
		return hospitalCashAddonBenefits;
	}

	public void setHospitalCashAddonBenefits(Boolean hospitalCashAddonBenefits) {
		this.hospitalCashAddonBenefits = hospitalCashAddonBenefits;
		this.setHospitalCashAddonBenefitsFlag((this.hospitalCashAddonBenefits != null && this.hospitalCashAddonBenefits) ? "Y" : "N" );
		
	}

	public Boolean getPatientCareAddOnBenefits() {
		return patientCareAddOnBenefits;
		
	}

	public void setPatientCareAddOnBenefits(Boolean patientCareAddOnBenefits) {
		this.patientCareAddOnBenefits = patientCareAddOnBenefits;
		this.setPatientCareAddOnBenefitsFlag((this.patientCareAddOnBenefits != null && this.patientCareAddOnBenefits) ? "Y" : "N" );
	}

	public String getHospitalCashAddonBenefitsFlag() {
		return hospitalCashAddonBenefitsFlag;
	}

	public void setHospitalCashAddonBenefitsFlag(
			String hospitalCashAddonBenefitsFlag) {
		this.hospitalCashAddonBenefitsFlag = hospitalCashAddonBenefitsFlag;
		if(this.hospitalCashAddonBenefitsFlag != null) {
			this.hospitalCashAddonBenefits = this.hospitalCashAddonBenefitsFlag.toLowerCase().equalsIgnoreCase("y") ? true : false;
		}
		
	}

	public String getPatientCareAddOnBenefitsFlag() {
		return patientCareAddOnBenefitsFlag;
	}

	public void setPatientCareAddOnBenefitsFlag(String patientCareAddOnBenefitsFlag) {
		this.patientCareAddOnBenefitsFlag = patientCareAddOnBenefitsFlag;
		if(this.patientCareAddOnBenefitsFlag != null) {
			this.patientCareAddOnBenefits = this.patientCareAddOnBenefitsFlag.toLowerCase().equalsIgnoreCase("y") ? true : false;
		}
	}

	public DocAcknowledgement getDocAckknowledgement() {
		return docAckknowledgement;
	}

	public void setDocAckknowledgement(DocAcknowledgement docAckknowledgement) {
		this.docAckknowledgement = docAckknowledgement;
	}

	public SelectValue getRoomCategory() {
		return roomCategory;
	}

	public void setRoomCategory(SelectValue roomCategory) {
		this.roomCategory = roomCategory;
	}

	public SelectValue getTreatmentType() {
		return treatmentType;
	}

	public void setTreatmentType(SelectValue treatmentType) {
		this.treatmentType = treatmentType;
	}

	public SelectValue getPatientStatus() {
		return patientStatus;
	}

	public void setPatientStatus(SelectValue patientStatus) {
		this.patientStatus = patientStatus;
	}
	
	public Double getPreauthReqAmt() {
		return preauthReqAmt;
	}

	public void setPreauthReqAmt(Double preauthReqAmt) {
		this.preauthReqAmt = preauthReqAmt;
	}

	public List<SpecialityDTO> getSpecialityList() {
		return specialityList;
	}

	public void setSpecialityList(List<SpecialityDTO> specialityList) {
		this.specialityList = specialityList;
	}

	public String getEnhancementReqAmt() {
		return enhancementReqAmt;
	}

	public void setEnhancementReqAmt(String enhancementReqAmt) {
		this.enhancementReqAmt = enhancementReqAmt;
	}

	public String getTreatmentRemarks() {
		return treatmentRemarks;
	}

	public void setTreatmentRemarks(String treatmentRemarks) {
		this.treatmentRemarks = treatmentRemarks;
	}

	public SelectValue getSublimitSpecify() {
		return sublimitSpecify;
	}

	public void setSublimitSpecify(SelectValue sublimitSpecify) {
		this.sublimitSpecify = sublimitSpecify;
	}

	public SelectValue getSumInsured() {
		return sumInsured;
	}

	public void setSumInsured(SelectValue sumInsured) {
		this.sumInsured = sumInsured;
	}

	public List<DiagnosisDetailsTableDTO> getDiagnosisTableList() {
		return diagnosisTableList;
	}

	public void setDiagnosisTableList(List<DiagnosisDetailsTableDTO> diagnosisTableList) {
		this.diagnosisTableList = diagnosisTableList;
	}

	public SelectValue getSublimitSumInsured() {
		return sublimitSumInsured;
	}

	public void setSublimitSumInsured(SelectValue sublimitSumInsured) {
		this.sublimitSumInsured = sublimitSumInsured;
	}
	
	public Date getDischargeDate() {
		return dischargeDate;
	}

	public void setDischargeDate(Date dischargeDate) {
		this.dischargeDate = dischargeDate;
	}
	
	public String getPreauthApprAmt() {
		return preauthApprAmt;
	}

	public void setPreauthApprAmt(String preauthApprAmt) {
		this.preauthApprAmt = preauthApprAmt;
	}

	public Date getDeathDate() {
		return deathDate;
	}

	public void setDeathDate(Date deathDate) {
		this.deathDate = deathDate;
	}

	public List<ProcedureDTO> getProcedureList() {
		return procedureList;
	}

	public void setProcedureList(List<ProcedureDTO> procedureList) {
		this.procedureList = procedureList;
	}

	public List<ProcedureDTO> getNewProcedureList() {
		return newProcedureList;
	}

	public void setNewProcedureList(List<ProcedureDTO> newProcedureList) {
		this.newProcedureList = newProcedureList;
	}

	public String getReasonForDeath() {
		return reasonForDeath;
	}

	public void setReasonForDeath(String reasonForDeath) {
		this.reasonForDeath = reasonForDeath;
	}

	public SelectValue getTerminateCover() {
		return terminateCover;
	}

	public void setTerminateCover(SelectValue terminateCover) {
		this.terminateCover = terminateCover;
		this.terminateCoverFlag = this.terminateCover != null && this.terminateCover.getValue().toString().toLowerCase().contains("yes") ? "Y" : "N";
	}

	public String getAutoRestoration() {
		return autoRestoration;
	}

	public void setAutoRestoration(String autoRestoration) {
		this.autoRestoration = autoRestoration;
	}

	public Long getCriticalIllnessFlag() {
		return criticalIllnessFlag;
	}

	public void setCriticalIllnessFlag(Long criticalIllnessFlag) {
		this.criticalIllnessFlag = criticalIllnessFlag;
		if(this.criticalIllnessFlag != null && this.criticalIllnessFlag == 1) {
			this.criticalIllness = true;
		}
	}

	public Long getCorporateBufferFlag() {
		return corporateBufferFlag;
	}

	public void setCorporateBufferFlag(Long corporateBufferFlag) {
		this.corporateBufferFlag = corporateBufferFlag;
		if(this.corporateBufferFlag != null && this.corporateBufferFlag == 1) {
			this.corpBuffer = true;
		}
	}

	public String getTerminateCoverFlag() {
		return terminateCoverFlag;
	}

	public void setTerminateCoverFlag(String terminateCoverFlag) {
		this.terminateCoverFlag = terminateCoverFlag;
		this.terminateCover = new SelectValue();
		this.terminateCover.setId((this.terminateCoverFlag != null && this.terminateCoverFlag.toLowerCase().equalsIgnoreCase("y")) ? 178l : ReferenceTable.COMMONMASTER_NO);
	}

	public String getInterimOrFinalEnhancementFlag() {
		return interimOrFinalEnhancementFlag;
	}

	public void setInterimOrFinalEnhancementFlag(
			String interimOrFinalEnhancementFlag) {

		if(interimOrFinalEnhancementFlag != null) {
			this.interimOrFinalEnhancement = interimOrFinalEnhancementFlag.toLowerCase().equalsIgnoreCase("i") ? true : false;
		}
		this.interimOrFinalEnhancementFlag = interimOrFinalEnhancementFlag;
	}

	public Boolean getInterimOrFinalEnhancement() {
		return interimOrFinalEnhancement;
	}

	public void setInterimOrFinalEnhancement(Boolean interimOrFinalEnhancement) {
		this.interimOrFinalEnhancementFlag = interimOrFinalEnhancement  ? "I" : "F";
		this.interimOrFinalEnhancement = interimOrFinalEnhancement;
	}

	public String getChangeOfDOA() {
		return changeOfDOA;
	}

	public void setChangeOfDOA(String changeOfDOA) {
		this.changeOfDOA = changeOfDOA;
	}

	public String getApprovedAmount() {
		return approvedAmount;
	}

	public void setApprovedAmount(String approvedAmount) {
		this.approvedAmount = approvedAmount;
	}


	public String getEnhancementTreatmentRemarks() {
		return enhancementTreatmentRemarks;
	}

	public void setEnhancementTreatmentRemarks(String enhancementTreatmentRemarks) {
		this.enhancementTreatmentRemarks = enhancementTreatmentRemarks;
	}

	public Double getTotalApprAmt() {
		return totalApprAmt;
	}

	public SelectValue getHospitalisationDueTo() {
		return hospitalisationDueTo;
	}

	public void setHospitalisationDueTo(SelectValue hospitalisationDueTo) {
		this.hospitalisationDueTo = hospitalisationDueTo;
	}

	public String getSystemOfMedicine() {
		return systemOfMedicine;
	}

	public void setSystemOfMedicine(String systemOfMedicine) {
		this.systemOfMedicine = systemOfMedicine;
	}

	public void setTotalApprAmt(Double totalApprAmt) {
		this.totalApprAmt = totalApprAmt;
	}

	public Date getInjuryDate() {
		return injuryDate;
	}

	public void setInjuryDate(Date injuryDate) {
		this.injuryDate = injuryDate;
	}

	public SelectValue getCauseOfInjury() {
		return causeOfInjury;
	}

	public void setCauseOfInjury(SelectValue causeOfInjury) {
		this.causeOfInjury = causeOfInjury;
	}

	public String getMedicalLegalCaseFlag() {
		return medicalLegalCaseFlag;
	}

	public void setMedicalLegalCaseFlag(String medicalLegalCaseFlag) {
		this.medicalLegalCaseFlag = medicalLegalCaseFlag;
		
		if(this.medicalLegalCaseFlag != null) {
			this.medicalLegalCase = this.medicalLegalCaseFlag.toLowerCase().equalsIgnoreCase("y") ? true : false;
		}
	}

	public Boolean getMedicalLegalCase() {
		return medicalLegalCase;
	}

	public void setMedicalLegalCase(Boolean medicalLegalCase) {
		this.medicalLegalCase = medicalLegalCase;
		this.setMedicalLegalCaseFlag((this.medicalLegalCase != null && this.medicalLegalCase) ? "Y" : "N" );
	}

	public Boolean getReportedToPolice() {
		return reportedToPolice;
	}

	public void setReportedToPolice(Boolean reportedToPolice) {
		this.reportedToPolice = reportedToPolice;
		this.setReportedToPoliceFlag((this.reportedToPolice != null && this.reportedToPolice) ? "Y" : "N" );
	}

	public String getReportedToPoliceFlag() {
		return reportedToPoliceFlag;
	}

	public void setReportedToPoliceFlag(String reportedToPoliceFlag) {
		this.reportedToPoliceFlag = reportedToPoliceFlag;
		if(this.reportedToPoliceFlag != null) {
			this.reportedToPolice = this.reportedToPoliceFlag.toLowerCase().equalsIgnoreCase("y") ? true : false;
		}
	}

	public Date getDiseaseFirstDetectedDate() {
		return diseaseFirstDetectedDate;
	}

	public void setDiseaseFirstDetectedDate(Date diseaseFirstDetectedDate) {
		this.diseaseFirstDetectedDate = diseaseFirstDetectedDate;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public Boolean getCoveredPreviousClaim() {
		return coveredPreviousClaim;
	}

	public void setCoveredPreviousClaim(Boolean coveredPreviousClaim) {
		this.coveredPreviousClaim = coveredPreviousClaim;
		this.setCoveredPreviousClaimFlag((this.coveredPreviousClaim != null && this.coveredPreviousClaim) ? "Y" : "N" );
	}

	public String getCoveredPreviousClaimFlag() {
		return coveredPreviousClaimFlag;
	}

	public void setCoveredPreviousClaimFlag(String coveredPreviousClaimFlag) {
		this.coveredPreviousClaimFlag = coveredPreviousClaimFlag;
		if(this.coveredPreviousClaimFlag != null) {
			this.coveredPreviousClaim = this.coveredPreviousClaimFlag.toLowerCase().equalsIgnoreCase("y") ? true : false;
		}
	}

	public String getPreHospitalisationPeriod() {
		return preHospitalisationPeriod;
	}

	public void setPreHospitalisationPeriod(String preHospitalisationPeriod) {
		this.preHospitalisationPeriod = preHospitalisationPeriod;
	}

	public String getPostHospitalisationPeriod() {
		return postHospitalisationPeriod;
	}

	public void setPostHospitalisationPeriod(String postHospitalisationPeriod) {
		this.postHospitalisationPeriod = postHospitalisationPeriod;
	}

	public Boolean getDomicillaryHospitalisation() {
		return domicillaryHospitalisation;
	}

	public void setDomicillaryHospitalisation(Boolean domicillaryHospitalisation) {
		this.domicillaryHospitalisation = domicillaryHospitalisation;
		this.setDomicillaryHospitalisationFlag((this.domicillaryHospitalisation != null && this.domicillaryHospitalisation) ? "Y" : "N" );
	}

	public String getDomicillaryHospitalisationFlag() {
		return domicillaryHospitalisationFlag;
	}

	public void setDomicillaryHospitalisationFlag(
			String domicillaryHospitalisationFlag) {
		this.domicillaryHospitalisationFlag = domicillaryHospitalisationFlag;
		if(this.domicillaryHospitalisationFlag != null) {
			this.domicillaryHospitalisation = this.domicillaryHospitalisationFlag.toLowerCase().equalsIgnoreCase("y") ? true : false;
		}
	}

	public List<OtherClaimDiagnosisDTO> getOtherClaimDetailsList() {
		return otherClaimDetailsList;
	}

	public void setOtherClaimDetailsList(
			List<OtherClaimDiagnosisDTO> otherClaimDetailsList) {
		this.otherClaimDetailsList = otherClaimDetailsList;
	}

	public ZonalReviewUpdateHospitalDetailsDTO getUpdateHospitalDetails() {
		return updateHospitalDetails;
	}

	public void setUpdateHospitalDetails(
			ZonalReviewUpdateHospitalDetailsDTO updateHospitalDetails) {
		this.updateHospitalDetails = updateHospitalDetails;
	}

	public String getRelapseIllness() {
		return relapseIllness;
	}

	public void setRelapseIllness(String relapseIllness) {
		this.relapseIllness = relapseIllness;
	}

	public String getStatusValue() {
		return statusValue;
	}

	public void setStatusValue(String statusValue) {
		this.statusValue = statusValue;
	}
	
	public void setNullValuesToField() {
		setNoOfDays(null);
		setAdmissionDate(null);
		setCauseOfInjury(null);
		setChangeOfDOA(null);
		setCorpBuffer(null);
		setCorporateBufferFlag(null);
		setCoveredPreviousClaim(null);
		setCoveredPreviousClaimFlag(null);
		setCriticalIllness(null);
		setCriticalIllnessFlag(null);
		setDeathDate(null);
		setDeliveryDate(null);
		setDocAckknowledgement(null);
		setDischargeDate(null);
		setDiseaseFirstDetectedDate(null);
		setDomicillaryHospitalisation(null);
		setDomicillaryHospitalisationFlag(null);
		setHospitalisationDueTo(null);
		setFirstConsultantDate(null);
		setIllness(null);
		setInjuryDate(null);
		setMedicalLegalCase(null);
		setMedicalLegalCaseFlag(null);
		setPatientStatus(null);
		setTreatmentType(null);
		setTreatmentRemarks(null);
		setPostHospitalisationPeriod(null);
		setPreHospitalisationPeriod(null);
		setReasonForAdmission(null);
		setReasonForDeath(null);
		setRelapseIllness(null);
		setReportedToPolice(null);
		setReportedToPoliceFlag(null);
		setRoomCategory(null);
		setVentilatorSupport(null);
		setVentilatorSupportFlag(null);
		setSumInsured(null);
		setTerminateCover(null);
		setTerminateCoverFlag(null);
		setSystemOfMedicine(null);
		setPtcaCabg(null);
		setNotAdheringToANHReport(null);
		setNotAdheringToANHReportFlag(null);
		procedureList = new ArrayList<ProcedureDTO>();
		  newProcedureList = new ArrayList<ProcedureDTO>();
		  specialityList = new ArrayList<SpecialityDTO>();
		  claimedDetailsList = new ArrayList<NoOfDaysCell>();
		  diagnosisTableList = new ArrayList<DiagnosisDetailsTableDTO>();
		  updateHospitalDetails = new ZonalReviewUpdateHospitalDetailsDTO();
		  otherClaimDetails = new OtherClaimDetailsDTO();
		  otherClaimDetailsList = new ArrayList<OtherClaimDiagnosisDTO>();
	}

	public String getChangeInReasonDOA() {
		return changeInReasonDOA;
	}

	public void setChangeInReasonDOA(String changeInReasonDOA) {
		this.changeInReasonDOA = changeInReasonDOA;
	}

	public Boolean getPoliceReportAttached() {
		return policeReportAttached;
	}
	

	public void setPoliceReportAttached(Boolean policeReportAttached) {
		this.policeReportAttached = policeReportAttached;
		this.setPoliceReportAttachedFlag((this.policeReportAttached != null && this.policeReportAttached) ? "Y" : "N" );
	}

	public String getPoliceReportAttachedFlag() {
		return policeReportAttachedFlag;
	}

	public void setPoliceReportAttachedFlag(String policeReportAttachedFlag) {
		this.policeReportAttachedFlag = policeReportAttachedFlag;
		if(this.policeReportAttachedFlag != null) {
			this.policeReportAttached = this.policeReportAttachedFlag.toLowerCase().equalsIgnoreCase("y") ? true : false;
		}
	}

	public String getFirNumber() {
		return firNumber;
	}

	public void setFirNumber(String firNumber) {
		this.firNumber = firNumber;
	}

	public String getAmountInWords() {
		return amountInWords;
	}

	public void setAmountInWords(String amountInWords) {
		this.amountInWords = amountInWords;
	}

	public String getAdmissionDateStr() {
		return admissionDateStr;
	}

	public void setAdmissionDateStr(String admissionDateStr) {
		this.admissionDateStr = admissionDateStr;
	}

	public String getDischargeDateStr() {
		return dischargeDateStr;
	}

	public void setDischargeDateStr(String dischargeDateStr) {
		this.dischargeDateStr = dischargeDateStr;
	}

	public String getChangeInReasonDOD() {
		return changeInReasonDOD;
	}

	public void setChangeInReasonDOD(String changeInReasonDOD) {
		this.changeInReasonDOD = changeInReasonDOD;
	}

	public String getBillingRemarks() {
		return billingRemarks;
	}

	public void setBillingRemarks(String billingRemarks) {
		this.billingRemarks = billingRemarks;
	}

	public String getMedicalRemarks() {
		return medicalRemarks;
	}

	public void setMedicalRemarks(String medicalRemarks) {
		this.medicalRemarks = medicalRemarks;
	}

	public SelectValue getReconsiderationRequest() {
		return reconsiderationRequest;
	}

	public void setReconsiderationRequest(SelectValue reconsiderationRequest) {
		this.reconsiderationRequest = reconsiderationRequest;
	}

	public SelectValue getReasonForReconsideration() {
		return reasonForReconsideration;
	}

	public void setReasonForReconsideration(SelectValue reasonForReconsideration) {
		this.reasonForReconsideration = reasonForReconsideration;
	}

	public SelectValue getSection() {
		return section;
	}

	public void setSection(SelectValue section) {
		this.section = section;
	}

	public Date getTreatmentStartDate() {
		return treatmentStartDate;
	}

	public void setTreatmentStartDate(Date treatmentStartDate) {
		this.treatmentStartDate = treatmentStartDate;
	}

	public Date getTreatmentEndDate() {
		return treatmentEndDate;
	}

	public void setTreatmentEndDate(Date treatmentEndDate) {
		this.treatmentEndDate = treatmentEndDate;
	}


	public String getHospitalizationNoOfDays() {
		return hospitalizationNoOfDays;
	}

	public void setHospitalizationNoOfDays(String hospitalizationNoOfDays) {
		this.hospitalizationNoOfDays = hospitalizationNoOfDays;
	}

	public SelectValue getTypeOfDelivery() {
		return typeOfDelivery;
	}

	public void setTypeOfDelivery(SelectValue typeOfDelivery) {
		this.typeOfDelivery = typeOfDelivery;
	}

	public Long getBankId() {
		return bankId;
	}

	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}

	public Double getPreauthTotalApprAmt() {
		return preauthTotalApprAmt;
	}

	public void setPreauthTotalApprAmt(Double preauthTotalApprAmt) {
		this.preauthTotalApprAmt = preauthTotalApprAmt;
	}

	public Boolean getChangeInDiagnosis() {
		return changeInDiagnosis;
	}

	public void setChangeInDiagnosis(Boolean changeInDiagnosis) {
		this.changeInDiagnosis = changeInDiagnosis;
	}
	

	public Boolean getVerifiedPolicySchedule() {
		return verifiedPolicySchedule;
	}

	public void setVerifiedPolicySchedule(Boolean verifiedPolicySchedule) {
		this.verifiedPolicySchedule = verifiedPolicySchedule;
		this.setVerifiedPolicyScheduleFlag((this.verifiedPolicySchedule != null && this.verifiedPolicySchedule) ? "Y" : "N" );
		
	}

	public String getVerifiedPolicyScheduleFlag() {
		return verifiedPolicyScheduleFlag;
	}

	public void setVerifiedPolicyScheduleFlag(String verifiedPolicyScheduleFlag) {
		this.verifiedPolicyScheduleFlag = verifiedPolicyScheduleFlag;
		if(this.verifiedPolicyScheduleFlag != null) {
			this.verifiedPolicySchedule = this.verifiedPolicyScheduleFlag.toLowerCase().equalsIgnoreCase("y") ? true : false;
		}
		
	}

	public Double getApprovedAmountAftDeduction() {
		return approvedAmountAftDeduction;
	}

	public void setApprovedAmountAftDeduction(Double approvedAmountAftDeduction) {
		this.approvedAmountAftDeduction = approvedAmountAftDeduction;
	} 
	public SectionDetailsTableDTO getSectionDetailsDTO() {
		return sectionDetailsDTO;
	}

	public void setSectionDetailsDTO(SectionDetailsTableDTO sectionDetailsDTO) {
		this.sectionDetailsDTO = sectionDetailsDTO;
	}

	public String getBillingMedicalReason() {
		return billingMedicalReason;
	}

	public void setBillingMedicalReason(String billingMedicalReason) {
		this.billingMedicalReason = billingMedicalReason;
	}

	public String getBillingMedicalRemarks() {
		return billingMedicalRemarks;
	}

	public void setBillingMedicalRemarks(String billingMedicalRemarks) {
		this.billingMedicalRemarks = billingMedicalRemarks;
	}

	public String getClaimApprovalMedicalReason() {
		return claimApprovalMedicalReason;
	}

	public void setClaimApprovalMedicalReason(String claimApprovalMedicalReason) {
		this.claimApprovalMedicalReason = claimApprovalMedicalReason;
	}

	public String getClaimApprovalMedicalRemarks() {
		return claimApprovalMedicalRemarks;
	}

	public void setClaimApprovalMedicalRemarks(String claimApprovalMedicalRemarks) {
		this.claimApprovalMedicalRemarks = claimApprovalMedicalRemarks;
	}

	public String getFinancialApprovalMedicalReason() {
		return financialApprovalMedicalReason;
	}

	public void setFinancialApprovalMedicalReason(
			String financialApprovalMedicalReason) {
		this.financialApprovalMedicalReason = financialApprovalMedicalReason;
	}

	public String getFinancialApprovalMedicalRemarks() {
		return financialApprovalMedicalRemarks;
	}

	public void setFinancialApprovalMedicalRemarks(
			String financialApprovalMedicalRemarks) {
		this.financialApprovalMedicalRemarks = financialApprovalMedicalRemarks;
	}
	
	public SelectValue getPaBenefits() {
		return paBenefits;
	}



	public void setPaBenefits(SelectValue paBenefits) {
		this.paBenefits = paBenefits;
	}

	public String getInvestigatorName() {
		return investigatorName;
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

	public Boolean getInvestigationReportReviewed() {
		return investigationReportReviewed;
	}

	public void setInvestigationReportReviewed(Boolean investigationReportReviewed) {
		this.investigationReportReviewed = investigationReportReviewed;
	}

	public String getInvestigationReportReviewedFlag() {
		return investigationReportReviewedFlag;
	}

	public void setInvestigationReportReviewedFlag(
			String investigationReportReviewedFlag) {
		this.investigationReportReviewedFlag = investigationReportReviewedFlag;
	}

	public List<PABenefitsDTO> getPaBenefitsList() {
		return paBenefitsList;
	}

	public void setPaBenefitsList(List<PABenefitsDTO> paBenefitsList) {
		this.paBenefitsList = paBenefitsList;
	}

	public List<AddOnCoversTableDTO> getAddOnCoversTableList() {
		return addOnCoversTableList;
	}

	public void setAddOnCoversTableList(
			List<AddOnCoversTableDTO> addOnCoversTableList) {
		this.addOnCoversTableList = addOnCoversTableList;
	}

	public List<AddOnCoversTableDTO> getOptionalCoversTableList() {
		return optionalCoversTableList;
	}

	public void setOptionalCoversTableList(
			List<AddOnCoversTableDTO> optionalCoversTableList) {
		this.optionalCoversTableList = optionalCoversTableList;
	}

	public Date getAccidentDeathDate() {
		return accidentDeathDate;
	}

	public void setAccidentDeathDate(Date accidentDeathDate) {
		this.accidentDeathDate = accidentDeathDate;
	}

	public Boolean getAccidentOrDeath() {
		return accidentOrDeath;
	}

	public void setAccidentOrDeath(Boolean accidentOrDeath) {
		this.accidentOrDeath = accidentOrDeath;
		this.accidentOrDeathFlag = null != this.accidentOrDeath && accidentOrDeath ? SHAConstants.ACCIDENT_FLAG : SHAConstants.DEATH_FLAG; 
	}

	public String getAccidentOrDeathFlag() {
		return accidentOrDeathFlag;
	}

	public void setAccidentOrDeathFlag(String accidentOrDeathFlag) {
		this.accidentOrDeathFlag = accidentOrDeathFlag;
		this.accidentOrDeath = null != accidentOrDeathFlag && SHAConstants.ACCIDENT_FLAG.equalsIgnoreCase(accidentOrDeathFlag) ? true : false;
	}

	public SelectValue getCauseOfAccident() {
		return causeOfAccident;
	}

	public void setCauseOfAccident(SelectValue causeOfAccident) {
		this.causeOfAccident = causeOfAccident;
	}

	public Date getDftDate() {
		return dftDate;
	}

	public void setDftDate(Date dftDate) {
		this.dftDate = dftDate;
	}

	public Boolean getPreExistingDisabilities() {
		return preExistingDisabilities;
	}

	public void setPreExistingDisabilities(Boolean preExistingDisabilities) {
		this.preExistingDisabilities = preExistingDisabilities;
		this.preExistingDisablitiesFlag = null != this.preExistingDisabilities && preExistingDisabilities ? SHAConstants.YES_FLAG : SHAConstants.N_FLAG; 
	}

	public String getPreExistingDisablitiesFlag() {
		return preExistingDisablitiesFlag;
	}

	public void setPreExistingDisablitiesFlag(String preExistingDisablitiesFlag) {
		this.preExistingDisablitiesFlag = preExistingDisablitiesFlag;
		this.preExistingDisabilities = null != preExistingDisablitiesFlag && SHAConstants.YES_FLAG.equalsIgnoreCase(preExistingDisablitiesFlag) ? true : false;
	}

	public String getDisabilitesRemarks() {
		return disabilitesRemarks;
	}

	public void setDisabilitesRemarks(String disabilitesRemarks) {
		this.disabilitesRemarks = disabilitesRemarks;
	}

	public String getBenefitsValue() {
		return benefitsValue;
	}

	public void setBenefitsValue(String benefitsValue) {
		this.benefitsValue = benefitsValue;
	}

	public List<AddOnCoversTableDTO> getAddOnCoversTableDeletedList() {
		return addOnCoversTableDeletedList;
	}

	public void setAddOnCoversTableDeletedList(
			List<AddOnCoversTableDTO> addOnCoversTableDeletedList) {
		this.addOnCoversTableDeletedList = addOnCoversTableDeletedList;
	}

	public List<AddOnCoversTableDTO> getOptionalCoversTableDeletedList() {
		return optionalCoversTableDeletedList;
	}

	public void setOptionalCoversTableDeletedList(
			List<AddOnCoversTableDTO> optionalCoversTableDeletedList) {
		this.optionalCoversTableDeletedList = optionalCoversTableDeletedList;
	}

	public Boolean getAccOrDeath() {
		return accOrDeath;
	}

	public void setAccOrDeath(Boolean accOrDeath) {
		this.accOrDeath = accOrDeath;
	}

	public Date getDateOfDeathAcc() {
		return dateOfDeathAcc;
	}

	public void setDateOfDeathAcc(Date dateOfDeathAcc) {
		this.dateOfDeathAcc = dateOfDeathAcc;
	}

	public String getDateOfDeathAccStr() {
		return dateOfDeathAccStr;
	}

	public void setDateOfDeathAccStr(String dateOfDeathAccStr) {
		this.dateOfDeathAccStr = dateOfDeathAccStr;
	}
	public List<SelectValue> getCoverListContainer() {
		return coverListContainer;
	}

	public void setCoverListContainer(List<SelectValue> coverListContainer) {
		this.coverListContainer = coverListContainer;
	}

	public List<TableBenefitsDTO> getBenefitDTOList() {
		return benefitDTOList;
	}

	public void setBenefitDTOList(List<TableBenefitsDTO> benefitDTOList) {
		this.benefitDTOList = benefitDTOList;
	}

	public List<SelectValue> getOptionalCoverListContainer() {
		return optionalCoverListContainer;
	}

	public void setOptionalCoverListContainer(
			List<SelectValue> optionalCoverListContainer) {
		this.optionalCoverListContainer = optionalCoverListContainer;
	}	
	public Date getDischargeDateForPa() {
		return dischargeDateForPa;
	}

	public void setDischargeDateForPa(Date dischargeDateForPa) {
		this.dischargeDateForPa = dischargeDateForPa;
	}

	public SelectValue getTreatmentTypeForPA() {
		return treatmentTypeForPA;
	}

	public void setTreatmentTypeForPA(SelectValue treatmentTypeForPA) {
		this.treatmentTypeForPA = treatmentTypeForPA;
	}

	public List<OptionalCoversDTO> getOptionalCoversTableListBilling() {
		return optionalCoversTableListBilling;
	}

	public void setOptionalCoversTableListBilling(
			List<OptionalCoversDTO> optionalCoversTableListBilling) {
		this.optionalCoversTableListBilling = optionalCoversTableListBilling;
	}

	public List<SelectValue> getAddOnCoverListContainer() {
		return addOnCoverListContainer;
	}

	public void setAddOnCoverListContainer(List<SelectValue> addOnCoverListContainer) {
		this.addOnCoverListContainer = addOnCoverListContainer;
	}

	public List<com.shaic.paclaim.billing.processclaimbilling.page.billreview.AddOnCoversTableDTO> getAddOnCoversTableListBilling() {
		return addOnCoversTableListBilling;
	}

	public void setAddOnCoversTableListBilling(
			List<com.shaic.paclaim.billing.processclaimbilling.page.billreview.AddOnCoversTableDTO> addOnCoversTableListBilling) {
		this.addOnCoversTableListBilling = addOnCoversTableListBilling;
	}

	public List<PABillingConsolidatedDTO> getBillingConsolidatedDTOList() {
		return billingConsolidatedDTOList;
	}

	public void setBillingConsolidatedDTOList(
			List<PABillingConsolidatedDTO> billingConsolidatedDTOList) {
		this.billingConsolidatedDTOList = billingConsolidatedDTOList;
	}

	public List<com.shaic.paclaim.billing.processclaimbilling.page.billreview.AddOnCoversTableDTO> getAddOnAllCoversTableListBilling() {
		return addOnAllCoversTableListBilling;
	}

	public void setAddOnAllCoversTableListBilling(
			List<com.shaic.paclaim.billing.processclaimbilling.page.billreview.AddOnCoversTableDTO> addOnAllCoversTableListBilling) {
		this.addOnAllCoversTableListBilling = addOnAllCoversTableListBilling;
	}

	

	public List<PABillingConsolidatedDTO> getPaBillingConsolidatedDTOList() {
		return paBillingConsolidatedDTOList;
	}

	public void setPaBillingConsolidatedDTOList(
			List<PABillingConsolidatedDTO> paBillingConsolidatedDTOList) {
		this.paBillingConsolidatedDTOList = paBillingConsolidatedDTOList;
	}

	public Long getOnloadBenefitId() {
		return onloadBenefitId;
	}

	public void setOnloadBenefitId(Long onloadBenefitId) {
		this.onloadBenefitId = onloadBenefitId;
	}

	public Double getConsolidatedPayableAmt() {
		return consolidatedPayableAmt;
	}

	public void setConsolidatedPayableAmt(Double consolidatedPayableAmt) {
		this.consolidatedPayableAmt = consolidatedPayableAmt;
	}

	public Double getAlreadyPaidAmt() {
		return alreadyPaidAmt;
	}

	public void setAlreadyPaidAmt(Double alreadyPaidAmt) {
		this.alreadyPaidAmt = alreadyPaidAmt;
	}

	public Double getNetPayableAmt() {
		return netPayableAmt;
	}

	public void setNetPayableAmt(Double netPayableAmt) {
		this.netPayableAmt = netPayableAmt;
	}
	
	public List<PABenefitsDTO> getDeletedPaBenefitsList() {
		return deletedPaBenefitsList;
	}

	public void setDeletedPaBenefitsList(List<PABenefitsDTO> deletedPaBenefitsList) {
		this.deletedPaBenefitsList = deletedPaBenefitsList;
	}

	public Double getPayableToInsured() {
		return payableToInsured;
	}

	public void setPayableToInsured(Double payableToInsured) {
		this.payableToInsured = payableToInsured;
	}

	public List<NoOfDaysCell> getClaimedDetailsListForBenefitSheet() {
		return claimedDetailsListForBenefitSheet;
	}

	public void setClaimedDetailsListForBenefitSheet(
			List<NoOfDaysCell> claimedDetailsListForBenefitSheet) {
		this.claimedDetailsListForBenefitSheet = claimedDetailsListForBenefitSheet;

	}

	public Boolean getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(Boolean paymentMode) {
		this.paymentMode = paymentMode;	
		this.paymentModeFlag = null != this.paymentMode && paymentMode ? ReferenceTable.PAYMENT_MODE_CHEQUE_DD : ReferenceTable.PAYMENT_MODE_BANK_TRANSFER; 
	}

	public String getCaReasonForRefferingToBilling() {
		return caReasonForRefferingToBilling;
	}

	public void setCaReasonForRefferingToBilling(
			String caReasonForRefferingToBilling) {
		this.caReasonForRefferingToBilling = caReasonForRefferingToBilling;
	}

	public String getCaApproverRemarks() {
		return caApproverRemarks;
	}

	public void setCaApproverRemarks(String caApproverRemarks) {
		this.caApproverRemarks = caApproverRemarks;
	}

	public String getFaReasonForRefferingToBilling() {
		return faReasonForRefferingToBilling;
	}

	public void setFaReasonForRefferingToBilling(
			String faReasonForRefferingToBilling) {
		this.faReasonForRefferingToBilling = faReasonForRefferingToBilling;
	}

	public String getFaApproverRemarks() {
		return faApproverRemarks;
	}

	public void setFaApproverRemarks(String faApproverRemarks) {
		this.faApproverRemarks = faApproverRemarks;
	}

	public List<AddOnCoverOnLoadDTO> getAddOnCoverNameList() {
		return addOnCoverNameList;
	}

	public void setAddOnCoverNameList(List<AddOnCoverOnLoadDTO> addOnCoverNameList) {
		this.addOnCoverNameList = addOnCoverNameList;
	}

	public SelectValue getInvestigationNameVal() {
		return investigationNameVal;
	}

	public void setInvestigationNameVal(SelectValue investigationNameVal) {
		this.investigationNameVal = investigationNameVal;
	}

	public String getOrganisationName() {
		return organisationName;
	}

	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}

	public Double getPaSumInsured() {
		return paSumInsured;
	}

	public void setPaSumInsured(Double paSumInsured) {
		this.paSumInsured = paSumInsured;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getDateOfBirthValue() {
		return dateOfBirthValue;
	}

	public void setDateOfBirthValue(String dateOfBirthValue) {
		this.dateOfBirthValue = dateOfBirthValue;
	}

	public String getRiskName() {
		return riskName;
	}

	public void setRiskName(String riskName) {
		this.riskName = riskName;
	}

	public Double getAge() {
		return age;
	}

	public void setAge(Double age) {
		this.age = age;
	}

	public String getMaReasonForRefferingToBilling() {
		return maReasonForRefferingToBilling;
	}

	public void setMaReasonForRefferingToBilling(
			String maReasonForRefferingToBilling) {
		this.maReasonForRefferingToBilling = maReasonForRefferingToBilling;
	}

	public String getMaApproverRemarks() {
		return maApproverRemarks;
	}

	public void setMaApproverRemarks(String maApproverRemarks) {
		this.maApproverRemarks = maApproverRemarks;
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

	public Boolean getWorkOrNonWorkPlace() {
		return workOrNonWorkPlace;
	}

	public void setWorkOrNonWorkPlace(Boolean workOrNonWorkPlace) {
		this.workOrNonWorkPlace = workOrNonWorkPlace;
		this.workOrNonWorkPlaceFlag = null != this.workOrNonWorkPlace && workOrNonWorkPlace ? SHAConstants.YES_FLAG : SHAConstants.N_FLAG; 
	}

	public String getWorkOrNonWorkPlaceFlag() {
		return workOrNonWorkPlaceFlag;
	}

	public void setWorkOrNonWorkPlaceFlag(String workOrNonWorkPlaceFlag) {
		this.workOrNonWorkPlaceFlag = workOrNonWorkPlaceFlag;
		this.workOrNonWorkPlace = null != workOrNonWorkPlaceFlag && SHAConstants.YES_FLAG.equalsIgnoreCase(workOrNonWorkPlaceFlag) ? true : false;
	}
	public SelectValue getPtcaCabg() {
		return ptcaCabg;
	}

	public void setPtcaCabg(SelectValue ptcaCabg) {
		this.ptcaCabg = ptcaCabg;
	}

	public Integer getCorpBufferAllocatedClaim() {
		return corpBufferAllocatedClaim;
	}

	public void setCorpBufferAllocatedClaim(Integer corpBufferAllocatedClaim) {
		this.corpBufferAllocatedClaim = corpBufferAllocatedClaim;
	}

	public Double getCorpBufferSI() {
		return corpBufferSI;
	}

	public void setCorpBufferSI(Double corpBufferSI) {
		this.corpBufferSI = corpBufferSI;
	}

	public Double getCorpBufferUtilisedAmt() {
		return corpBufferUtilisedAmt;
	}

	public void setCorpBufferUtilisedAmt(Double corpBufferUtilisedAmt) {
		this.corpBufferUtilisedAmt = corpBufferUtilisedAmt;
	}

	public Double getCorpBufferLimit() {
		return corpBufferLimit;
	}

	public void setCorpBufferLimit(Double corpBufferLimit) {
		this.corpBufferLimit = corpBufferLimit;
	}

	public Double getCorpBufferAvalBal() {
		return corpBufferAvalBal;
	}

	public void setCorpBufferAvalBal(Double corpBufferAvalBal) {
		this.corpBufferAvalBal = corpBufferAvalBal;
	}

	public Boolean getOtherBenfitOpt() {
		return otherBenfitOpt;
	}

	public void setOtherBenfitOpt(Boolean otherBenfitOpt) {
		this.otherBenfitOpt = otherBenfitOpt;
		this.otherBenfitFlag = ((this.otherBenfitOpt != null && this.otherBenfitOpt) ? "Y" : "N" );
	}

	public String getOtherBenfitFlag() {
		return otherBenfitFlag;
	}

	public void setOtherBenfitFlag(String otherBenfitFlag) {
		this.otherBenfitFlag = otherBenfitFlag;
		if(this.otherBenfitFlag != null) {
			this.otherBenfitOpt = this.otherBenfitFlag.equalsIgnoreCase("y") ? true : false;
		}
	}

	public List<OtherBenefitsTableDto> getOtherBenefitsList() {
		return otherBenefitsList;                                                    
	}

	public void setOtherBenefitsList(List<OtherBenefitsTableDto> otherBenefitsList) {
		this.otherBenefitsList = otherBenefitsList;
	}

	public List<SectionDetailsTableDTO> getSectionDetailsListDto() {
		return sectionDetailsListDto;
	}

	public void setSectionDetailsListDto(
			List<SectionDetailsTableDTO> sectionDetailsListDto) {
		this.sectionDetailsListDto = sectionDetailsListDto;
	}

	public Double getTotalOtherBenefitsApprovedAmt() {
		return totalOtherBenefitsApprovedAmt;
	}

	public void setTotalOtherBenefitsApprovedAmt(
			Double totalOtherBenefitsApprovedAmt) {
		this.totalOtherBenefitsApprovedAmt = totalOtherBenefitsApprovedAmt;
	}

	public int getRestorationCount() {
		return restorationCount;
	}

	public void setRestorationCount(int restorationCount) {
		this.restorationCount = restorationCount;
	}

	public Boolean getIsRTAButtonEnable() {
		return isRTAButtonEnable;
	}

	public void setIsRTAButtonEnable(Boolean isRTAButtonEnable) {
		this.isRTAButtonEnable = isRTAButtonEnable;
	}

	public int getOtherBeneitApplicableFlag() {
		return otherBeneitApplicableFlag;
	}

	public void setOtherBeneitApplicableFlag(int otherBeneitApplicableFlag) {
		this.otherBeneitApplicableFlag = otherBeneitApplicableFlag;
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
	
	public String getGpaSection() {
		return gpaSection;
	}

	public void setGpaSection(String gpaSection) {
		this.gpaSection = gpaSection;
	}

	public SelectValue getGpaCategory() {
		return gpaCategory;
	}

	public void setGpaCategory(SelectValue gpaCategory) {
		this.gpaCategory = gpaCategory;
	}

	public String getPayModeChangeReason() {
		return payModeChangeReason;
	}

	public void setPayModeChangeReason(String payModeChangeReason) {
		this.payModeChangeReason = payModeChangeReason;
	}

	public Integer getHospitalizationClaimedAmount() {
		return hospitalizationClaimedAmount;
	}

	public void setHospitalizationClaimedAmount(Integer hospitalizationClaimedAmount) {
		this.hospitalizationClaimedAmount = hospitalizationClaimedAmount;
	}

	public Integer getPreHospitalizationClaimedAmount() {
		return preHospitalizationClaimedAmount;
	}

	public void setPreHospitalizationClaimedAmount(
			Integer preHospitalizationClaimedAmount) {
		this.preHospitalizationClaimedAmount = preHospitalizationClaimedAmount;
	}

	public Integer getPostHospitalizationClaimedAmount() {
		return postHospitalizationClaimedAmount;
	}

	public void setPostHospitalizationClaimedAmount(
			Integer postHospitalizationClaimedAmount) {
		this.postHospitalizationClaimedAmount = postHospitalizationClaimedAmount;
	}

	public Integer getOtherBenefitclaimedAmount() {
		return otherBenefitclaimedAmount;
	}

	public void setOtherBenefitclaimedAmount(Integer otherBenefitclaimedAmount) {
		this.otherBenefitclaimedAmount = otherBenefitclaimedAmount;
	}

	public Integer getHospClaimedAmountDocRec() {
		return hospClaimedAmountDocRec;
	}

	public void setHospClaimedAmountDocRec(Integer hospClaimedAmountDocRec) {
		this.hospClaimedAmountDocRec = hospClaimedAmountDocRec;
	}

	public Integer getPreHospClaimedAmountDocRec() {
		return preHospClaimedAmountDocRec;
	}

	public void setPreHospClaimedAmountDocRec(Integer preHospClaimedAmountDocRec) {
		this.preHospClaimedAmountDocRec = preHospClaimedAmountDocRec;
	}

	public Integer getPostHospClaimedAmountDocRec() {
		return postHospClaimedAmountDocRec;
	}

	public void setPostHospClaimedAmountDocRec(Integer postHospClaimedAmountDocRec) {
		this.postHospClaimedAmountDocRec = postHospClaimedAmountDocRec;
	}

	public Integer getOtherBenefitsAmountDocRec() {
		return otherBenefitsAmountDocRec;
	}

	public void setOtherBenefitsAmountDocRec(Integer otherBenefitsAmountDocRec) {
		this.otherBenefitsAmountDocRec = otherBenefitsAmountDocRec;
	}

	public String getDocumentVerificationFlag() {
		return documentVerificationFlag;
	}

	public void setDocumentVerificationFlag(String documentVerificationFlag) {
		this.documentVerificationFlag = documentVerificationFlag;
	}

	public Boolean getDocumentVerification() {
		return documentVerification;
	}

	public void setDocumentVerification(Boolean documentVerification) {
		this.documentVerification = documentVerification;
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
	public Boolean getIsFvrOrInvsInitiated() {
		return isFvrOrInvsInitiated;
	}

	public void setIsFvrOrInvsInitiated(Boolean isFvrOrInvsInitiated) {
		this.isFvrOrInvsInitiated = isFvrOrInvsInitiated;
	}

	public Boolean getIsFvrInitiated() {
		return isFvrInitiated;
	}

	public void setIsFvrInitiated(Boolean isFvrInitiated) {
		this.isFvrInitiated = isFvrInitiated;
	}

	public Boolean getIsInvsInitiated() {
		return isInvsInitiated;
	}

	public void setIsInvsInitiated(Boolean isInvsInitiated) {
		this.isInvsInitiated = isInvsInitiated;
	}

	public String getReconsiderationFlag() {
		return reconsiderationFlag;
	}

	public void setReconsiderationFlag(String reconsiderationFlag) {
		this.reconsiderationFlag = reconsiderationFlag;
	}

	public Integer getInvestigatorsCount() {
		return investigatorsCount;
	}

	public void setInvestigatorsCount(Integer investigatorsCount) {
		this.investigatorsCount = investigatorsCount;
	}

	public Integer getInvsReplyReceivedCount() {
		return invsReplyReceivedCount;
	}

	public void setInvsReplyReceivedCount(Integer invsReplyReceivedCount) {
		this.invsReplyReceivedCount = invsReplyReceivedCount;
	}

	public Boolean getIsMultipleInvsAssigned() {
		return isMultipleInvsAssigned;
	}

	public void setIsMultipleInvsAssigned(Boolean isMultipleInvsAssigned) {
		this.isMultipleInvsAssigned = isMultipleInvsAssigned;
	}
	
	public String getAmtClaimed() {
		return amtClaimed;
	}

	public void setAmtClaimed(String amtClaimed) {
		this.amtClaimed = amtClaimed;
	}

	public String getDisCntHospBill() {
		return disCntHospBill;
	}

	public void setDisCntHospBill(String disCntHospBill) {
		this.disCntHospBill = disCntHospBill;
	}

	public String getNetAmt() {
		return netAmt;
	}

	public void setNetAmt(String netAmt) {
		this.netAmt = netAmt;
	}

	public Boolean getNotAdheringToANHReport() {
		return notAdheringToANHReport;
	}

	public void setNotAdheringToANHReport(Boolean notAdheringToANHReport) {
		this.notAdheringToANHReport = notAdheringToANHReport;
		this.notAdheringToANHReportFlag = this.notAdheringToANHReport != null && notAdheringToANHReport ? "Y" : "N" ;
	}

	public String getNotAdheringToANHReportFlag() {
		return notAdheringToANHReportFlag;
	}

	public void setNotAdheringToANHReportFlag(String notAdheringToANHReportFlag) {
		this.notAdheringToANHReportFlag = notAdheringToANHReportFlag;
		if(this.notAdheringToANHReportFlag !=null && this.notAdheringToANHReportFlag.equalsIgnoreCase("Y")){
			this.notAdheringToANHReport=true;
		}
	}
	public Boolean getIsNegotiationInitiated() {
		return isNegotiationInitiated;
	}

	public void setIsNegotiationInitiated(Boolean isNegotiationInitiated) {
		this.isNegotiationInitiated = isNegotiationInitiated;
	}

	public SelectValue getPreAuthType() {
		return preAuthType;
	}

	public void setPreAuthType(SelectValue preAuthType) {
		this.preAuthType = preAuthType;
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

	public String getAccountPref() {
		return accountPref;
	}

	public void setAccountPref(String accountPref) {
		this.accountPref = accountPref;
	}

	public String getAccType() {
		return accType;
	}

	public void setAccType(String accType) {
		this.accType = accType;
	}

	public String getNameAsPerBank() {
		return nameAsPerBank;
	}

	public void setNameAsPerBank(String nameAsPerBank) {
		this.nameAsPerBank = nameAsPerBank;
	}

	public String getPayeeRelationship() {
		return payeeRelationship;
	}

	public void setPayeeRelationship(String payeeRelationship) {
		this.payeeRelationship = payeeRelationship;
	}		
	
	public List<TreatingDoctorDTO> getTreatingDoctorDTOs() {
		return treatingDoctorDTOs;
	}

	public void setTreatingDoctorDTOs(List<TreatingDoctorDTO> treatingDoctorDTOs) {
		this.treatingDoctorDTOs = treatingDoctorDTOs;
	}

	public SelectValue getCategory() {
		return category;
	}

	public void setCategory(SelectValue category) {
		this.category = category;
	}
	
	public String getEscalatePccRemarksvalue() {
		return escalatePccRemarksvalue;
	}

	public void setEscalatePccRemarksvalue(String escalatePccRemarksvalue) {
		this.escalatePccRemarksvalue = escalatePccRemarksvalue;
	}

	public Boolean getPreAuthOrPreaAuthFinalBill() {
		return preAuthOrPreaAuthFinalBill;
	}

	public void setPreAuthOrPreaAuthFinalBill(Boolean preAuthOrPreaAuthFinalBill) {
		this.preAuthOrPreaAuthFinalBill = preAuthOrPreaAuthFinalBill;
	}

	public List<ImplantDetailsDTO> getImplantDetailsDTOs() {
		return implantDetailsDTOs;
	}

	public void setImplantDetailsDTOs(List<ImplantDetailsDTO> implantDetailsDTOs) {
		this.implantDetailsDTOs = implantDetailsDTOs;
	}

	public Boolean getImplantApplicable() {
		return implantApplicable;
	}

	public void setImplantApplicable(Boolean implantApplicable) {
		this.implantApplicable = implantApplicable;
	}

	public Boolean getIsStarGrpCorApproveBtn() {
		return isStarGrpCorApproveBtn;
	}

	public void setIsStarGrpCorApproveBtn(Boolean isStarGrpCorApproveBtn) {
		this.isStarGrpCorApproveBtn = isStarGrpCorApproveBtn;
	}
	
	public SelectValue getHomeCareTreatment() {
		return homeCareTreatment;
	}

	public void setHomeCareTreatment(SelectValue homeCareTreatment) {
		this.homeCareTreatment = homeCareTreatment;
	}

	public SelectValue getAdmissionType() {
		return admissionType;
	}

	public void setAdmissionType(SelectValue admissionType) {
		this.admissionType = admissionType;
	}

	public Boolean getIsHomeIsolationExist() {
		return isHomeIsolationExist;
	}

	public void setIsHomeIsolationExist(Boolean isHomeIsolationExist) {
		this.isHomeIsolationExist = isHomeIsolationExist;
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

	public Boolean getVentilatorSupport() {
		return ventilatorSupport;
	}

	public void setVentilatorSupport(Boolean ventilatorSupport) {
		this.ventilatorSupport = ventilatorSupport;
		this.ventilatorSupportFlag = ((this.ventilatorSupport != null && this.ventilatorSupport) ? "Y" : "N" );
	}

	public String getVentilatorSupportFlag() {
		return ventilatorSupportFlag;
	}

	public void setVentilatorSupportFlag(String ventilatorSupportFlag) {
		this.ventilatorSupportFlag = ventilatorSupportFlag;
		if(this.ventilatorSupportFlag != null) {
			this.ventilatorSupport = this.ventilatorSupportFlag.equalsIgnoreCase("y") ? true : false;
		}
	}

	public SelectValue getHospitalCashDueTo() {
		return hospitalCashDueTo;
	}

	public void setHospitalCashDueTo(SelectValue hospitalCashDueTo) {
		this.hospitalCashDueTo = hospitalCashDueTo;
	}

	public Boolean getPatientDayCare() {
		return patientDayCare;
	}

	public void setPatientDayCare(Boolean patientDayCare) {
		this.patientDayCare = patientDayCare;
	}

	public SelectValue getPatientDayCareDueTo() {
		return patientDayCareDueTo;
	}

	public void setPatientDayCareDueTo(SelectValue patientDayCareDueTo) {
		this.patientDayCareDueTo = patientDayCareDueTo;
	}

	public Boolean getEmergency() {
		return emergency;
	}

	public void setEmergency(Boolean emergency) {
		this.emergency = emergency;
		this.emergenyFlag = this.emergency != null && emergency ? "Y" : "N" ;
	}

	public String getEmergenyFlag() {
		return emergenyFlag;
	}

	public void setEmergenyFlag(String emergenyFlag) {
		this.emergenyFlag = emergenyFlag;
		if(this.emergenyFlag != null && this.emergenyFlag == "Y") {
			this.emergency = true;
		}
	}

	
	public String getBufferType() {
		return bufferType;
	}

	public void setBufferType(String bufferType) {
		this.bufferType = bufferType;
	}

	public Integer getCorpBufferUtilizedAmt() {
		return corpBufferUtilizedAmt;
	}

	public void setCorpBufferUtilizedAmt(Integer corpBufferUtilizedAmt) {
		this.corpBufferUtilizedAmt = corpBufferUtilizedAmt;
	}

	public String getBufferTypeValue() {
		return bufferTypeValue;
	}

	public void setBufferTypeValue(String bufferTypeValue) {
		this.bufferTypeValue = bufferTypeValue;
	}

	public Integer getNonPayableAmt() {
		return nonPayableAmt;
	}

	public void setNonPayableAmt(Integer nonPayableAmt) {
		this.nonPayableAmt = nonPayableAmt;
	}

	public Integer getDeductibleAmt() {
		return deductibleAmt;
	}

	public void setDeductibleAmt(Integer deductibleAmt) {
		this.deductibleAmt = deductibleAmt;
	}

	public Integer getNacBufferUtilizedAmount() {
		return nacBufferUtilizedAmount;
	}

	public void setNacBufferUtilizedAmount(Integer nacBufferUtilizedAmount) {
		this.nacBufferUtilizedAmount = nacBufferUtilizedAmount;
	}

	public Boolean getIsDiscreationaryAplicable() {
		return isDiscreationaryAplicable;
	}

	public void setIsDiscreationaryAplicable(Boolean isDiscreationaryAplicable) {
		this.isDiscreationaryAplicable = isDiscreationaryAplicable;
	}

	public Boolean getIsWintageApplicable() {
		return isWintageApplicable;
	}

	public void setIsWintageApplicable(Boolean isWintageApplicable) {
		this.isWintageApplicable = isWintageApplicable;
	}

	public Boolean getIsNacbApplicable() {
		return isNacbApplicable;
	}

	public void setIsNacbApplicable(Boolean isNacbApplicable) {
		this.isNacbApplicable = isNacbApplicable;
	}

	public Boolean getIsDisBufUtlzed() {
		return isDisBufUtlzed;
	}

	public void setIsDisBufUtlzed(Boolean isDisBufUtlzed) {
		this.isDisBufUtlzed = isDisBufUtlzed;
	}

	public Boolean getIsWinBufUtlzed() {
		return isWinBufUtlzed;
	}

	public void setIsWinBufUtlzed(Boolean isWinBufUtlzed) {
		this.isWinBufUtlzed = isWinBufUtlzed;
	}

	public Boolean getIsNacbufUtilzed() {
		return isNacbufUtilzed;
	}

	public void setIsNacbufUtilzed(Boolean isNacbufUtilzed) {
		this.isNacbufUtilzed = isNacbufUtilzed;
	}
	
	public Integer getEmpBufferAvlBal() {
		return empBufferAvlBal;
	}

	public void setEmpBufferAvlBal(Integer empBufferAvlBal) {
		this.empBufferAvlBal = empBufferAvlBal;
	}

	public Integer getWinBufferAvlBal() {
		return winBufferAvlBal;
	}

	public void setWinBufferAvlBal(Integer winBufferAvlBal) {
		this.winBufferAvlBal = winBufferAvlBal;
	}

	public Integer getNacBufferAvlBal() {
		return NacBufferAvlBal;
	}

	public void setNacBufferAvlBal(Integer nacBufferAvlBal) {
		NacBufferAvlBal = nacBufferAvlBal;
	}

	public String getRemarksBillEntry() {
		return remarksBillEntry;
	}

	public void setRemarksBillEntry(String remarksBillEntry) {
		this.remarksBillEntry = remarksBillEntry;
	}

	public SelectValue getCovid19Variant() {
		return covid19Variant;
	}

	public void setCovid19Variant(SelectValue covid19Variant) {
		this.covid19Variant = covid19Variant;
	}

	public String getCocktailDrugFlag() {
		return cocktailDrugFlag;
	}

	public void setCocktailDrugFlag(String cocktailDrugFlag) {
		this.cocktailDrugFlag = cocktailDrugFlag;
		/*this.cocktailDrug = new SelectValue();
		this.cocktailDrug.setId((this.cocktailDrugFlag != null && this.cocktailDrugFlag.equalsIgnoreCase("Y")) ? ReferenceTable.COMMONMASTER_YES : (this.cocktailDrugFlag != null && this.cocktailDrugFlag.equalsIgnoreCase("N")) ? ReferenceTable.COMMONMASTER_NO : 0l);*/
	}
	
	public SelectValue getCocktailDrug() {
		return cocktailDrug;
	}

	public void setCocktailDrug(SelectValue cocktailDrug) {
		this.cocktailDrug = cocktailDrug;
		//this.cocktailDrugFlag = this.cocktailDrug != null && null != this.cocktailDrug.getValue() && this.cocktailDrug.getValue().toString().toLowerCase().contains("yes") ? "Y" : "N";
	}
	
	
	
}

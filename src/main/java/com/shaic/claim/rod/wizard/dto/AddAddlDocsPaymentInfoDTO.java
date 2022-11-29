package com.shaic.claim.rod.wizard.dto;

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

		public class AddAddlDocsPaymentInfoDTO {
			
			
			@NotNull(message="Please select documents received from")
			private SelectValue documentsReceivedFrom;
			
			private String acknowledgmentContactNumber;
			
			@NotNull(message="Please select Documents Received Date")
			private Date documentsReceivedDate;
			
			private String emailId;
			
			@NotNull(message="Please select mode of receipt")
			private SelectValue modeOfReceipt;
			
			private SelectValue reconsiderationRequest;
			
			private String reconsiderationRequestFlag;
			
			//Variables declared for bill classification section
			
			private Boolean hospitalization ;
			
			//private Long hospitalizationFlag;
			private String hospitalizationFlag;
			
			private Boolean preHospitalization;
			
			private String preHospitalizationFlag;
			
			//private Long preHospitalizationFlag;
			
			private Boolean postHospitalization;
			
			//private Long postHospitalizationFlag;
			private String postHospitalizationFlag;
			
			private Boolean partialHospitalization ;
			
			//private Long partialHospitalizationFlag;
			private String partialHospitalizationFlag;
			
			private Boolean hospitalizationRepeat ;
			
			private String hospitalizationRepeatFlag;
			
			private Boolean lumpSumAmount;
			
			//private Long lumSumAmountFlag;
			private String lumpSumAmountFlag;
			
			private Boolean addOnBenefitsHospitalCash;
			
			//private Long addOnBenefitsHospitalCashFlag;
			
			private String addOnBenefitsHospitalCashFlag;
			
			private Boolean addOnBenefitsPatientCare;
			
			//private Long addOnBenefitsPatientCareFlag;
			private String addOnBenefitsPatientCareFlag;
			
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
			
			private Boolean paHospitalisation;
			
			private Boolean paPartialHospitalisation;
			
			private String organisationName;
			
			private Double paSumInsured;
			
			private String parentName;
			
			private Date dateOfBirth;
			
			private String dateOfBirthValue;
			
			private String riskName;
			
			private Double age;	
			
			private Date dateOfDeath;
			
			private Date dateOfAccident;
			
			private Date dateOfDisablement;
			
			private Boolean workOrNonWorkPlace = false;
			
			private String workOrNonWorkPlaceFlag;
			
			private Boolean otherBenefits;
			
			private String otherBenefitsFlag;
			
			private Boolean emergencyMedicalEvaluation;
			
			private String emergencyMedicalEvaluationFlag;
			
			private Boolean compassionateTravel;
			
			private String compassionateTravelFlag;
			
			private Boolean repatriationOfMortalRemains;
			
			private String repatriationOfMortalRemainsFlag;
			
			private Boolean preferredNetworkHospital;
			
			private String preferredNetworkHospitalFlag;
			
			private Boolean sharedAccomodation;
			
			private String sharedAccomodationFlag;
			
			
			private String additionalRemarks;
			
			private List<DocumentCheckListDTO> documentCheckList;
			
			//Added for create rod screen. Apart from above fields, below given were extra fields present here.
			private Date dateOfAdmission;
			private Date dateOfDischarge;
			
			private String hospitalName;
			
			private String hospitalPayableAt;
			
			private SelectValue insuredPatientName;
			
			private String modeOfReceiptValue;
			
			private String documentReceivedFromValue;
			
			private String reconsiderationRequestValue;
			
			private Boolean paymentMode;
			
			private Long paymentModeFlag;
			
			private SelectValue payeeName;
			
			private String reasonForChange;
			
			private String panNo;
			
			private String legalFirstName;
			
			private String legalMiddleName;
			
			private String legalLastName;
			
			private SectionDetailsTableDTO sectionDetailsDTO;

			@NotNull(message = "Please Enter PayableAt")
			@Size(min = 1 , message = "Please Enter PayableAt")
			private String payableAt;
			
			private Long docAcknowledgementKey;
			
			@NotNull(message = "Please Enter Account No")
			@Size(min = 1 , message = "Please Enter Account No")
			private String accountNo;
			
			@NotNull(message = "Please Enter Ifsc code")
			@Size(min = 1 , message = "Please Enter Ifsc code")
			private String ifscCode;
			
			private String branch;
			
			private String bankName;
			
			private String city;
			
			//@NotNull(message = "Please Enter reason for change in DOA")
			@Size(min = 1 , message = "Please Enter reason for change in DOA")
			private String changeInReasonDOA;
			
			private Long rodKey;
			
			private String acknowledgementNumber;
			
			private String acknowledgmentCreateOn;
			
			private String acknowledgmentCreatedId;
			
			private String acknowledgmentCreatedName;
			
			private Long key;
			
			private String hospitalizationClaimedAmount;
			private String preHospitalizationClaimedAmount;
			private String postHospitalizationClaimedAmount;
			private String otherBenefitclaimedAmount;
			
			private String rodNumber;
			
			private Long bankId;
			
			
			private String paymentModSelectFlag;
			
//			private SelectRODtoAddAdditionalDocumentsDTO selectRODtoAddAdditionalDocumentsDTO;
			
			private SelectValue reasonForReconsideration;
			
			private String reasonForReconsiderationRequestValue;
			
			
			@NotNull(message = "Please select reason for cancelling acknowledgement")
			private SelectValue cancelAcknowledgementReason;
			
			private String cancelAcknowledgementRemarks;
			
			private Boolean isLetterGenerated;
			
			//Added for cancel acknowledgement status 
			private Long statusId;
			
			//Added for reconsideration request and payment cancellation.
			private Boolean paymentCancellationNeeded;
			
			private String paymentCancellationNeededFlag;
			
			private SelectValue cancelReason;
			
			private String cancelRemarks;
			
			/**
			 * Added for benefits screen. 
			 * */
			
			@NotNull(message = "Please Select Type of Coordinator Request.")
			private SelectValue typeOfCoordinatorRequest;
			
			@NotNull(message = "Please Enter Reason for Refering.")
			@Size(min=1,message = "Please Enter Reason for Refering.")
			private String reasonForRefering;
			
			@NotNull(message = "Please Select Reason for Cancellation")
			private SelectValue cancellationReason;
			
			@NotNull(message = "Please Enter Billing Remarks.")
			@Size(min=1,message = "Please Enter Billing Remarks.")
			private String billingRemarks;
			
			
			private Double initialTotalApprovedAmt;
			
			@NotNull(message = "Please Enter Reason for Referring to Billing.")
			@Size(min=1,message = "Please Enter Reason for Referring to Billing.")
			private String reasonForReferringToBilling;

			@NotNull(message = "Please Enter Financial Approver Remarks.")
			@Size(min=1,message = "Please Enter Financial Approver Remarks.")
			private String financialApproverRemarks;
			
			
			@NotNull(message = "Please Enter Query Remarks.")
			@Size(min=1 , message ="Please Enter Query Remarks.")
			private String queryRemarks;
			
			
			@NotNull(message = "Please Enter Financial Remarks.")
			@Size(min=1,message = "Please Enter Financial Remarks.")
			private String financialRemarks;
			
//			@AssertTrue(message = "Please Select Verified the amount claimed and documents based on the bills received Option.")
//			private boolean originalBillsReceived;
			
			private String approvalRemarks;
			
			@NotNull(message = "Please Enter Rejection Remarks.")
			@Size(min=1,message = "Please Enter Rejection Remarks.")
			private String rejectionRemarks;
			
			private Boolean sentToCPUFlag;
			
			private Integer sentToCPU;
			
			
			private Long reimbursementQueryKey;
			
			private String reasonForReconsiderationValue;
			
			//Added for refer to bill entry recod.
			private String referToBillEntryBillingRemarks;
			private String referToBillEntryFAApproverRemarks;
			
			@NotNull(message = "Please Select Accident Or Death Option.")
			private Boolean accidentOrDeath;
			
			private Date accidentOrDeathDate;
			
			@NotNull(message = "Please Select Document Type")
			private SelectValue documentType;
			
			private String documentTypeValue;
			
			private Long documentTypeId;
			
			private String benifitFlag;
			
//			private BeanItemContainer<SelectValue> additionalCovers;	
//			
//			private BeanItemContainer<SelectValue> optionalCovers;	
			
			private List<AddOnCoversTableDTO> addOnCoversList;
			
			
			private List<AddOnCoversTableDTO> optionalCoversList;
			
			private List<AddOnCoversTableDTO> optionalCoversDeletedList;
			
			private List<AddOnCoversTableDTO> addOnCoversDeletedList;
			
			private List<DocumentCheckListDTO> defaultDocumentCheckList;
			
			private Long ackKey;
			
			private String benifitClaimedAmount;
			
			private Double benefitClaimedAmt;
			
			private Date gpaRiskDOB;
			
			private Double gpaRiskAge;
			
			private SelectValue gpaCategory;
			
			private String gpaSection;
			
			private String payModeChangeReason;
			
			private Long paymentModeChangeFlag;
			
			private Boolean sourceOfDocument;
			
			private String sourceOfDocumentValue;
			private String documentVerificationFlag;
			
			private Boolean documentVerification;
			
			private Double hospClaimedAmountDocRec;
			
			private Double preHospClaimedAmountDocRec;
			
			private Double postHospClaimedAmountDocRec;
			
			private Double otherBenefitsAmountDocRec;
			
			private Boolean isOtherBenefitApplicableInPreauth = false;
			
			private Boolean isEmergencyMedicalEvacuation = false;
			
			private Boolean isRepatriationOfMortal = false;
			
			private String documentReceivedDate;
			
			private Boolean isEditable;


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

			public String getEmailId() {
				return emailId;
			}

			public void setEmailId(String emailId) {
				this.emailId = emailId;
			}

			public SelectValue getModeOfReceipt() {
				return modeOfReceipt;
			}

			public void setModeOfReceipt(SelectValue modeOfReceipt) {
				this.modeOfReceipt = modeOfReceipt;
			}

			public SelectValue getReconsiderationRequest() {
				return reconsiderationRequest;
			}

			public void setReconsiderationRequest(SelectValue reconsiderationRequest) {
				this.reconsiderationRequest = reconsiderationRequest;
				this.reconsiderationRequestFlag = this.reconsiderationRequest != null && this.reconsiderationRequest.getValue().toString().toLowerCase().contains("yes") ? "Y" : "N";
			}

			public Boolean getHospitalization() {
				return hospitalization;
			}

			public void setHospitalization(Boolean hospitalization) {
				this.hospitalization = hospitalization;
				this.hospitalizationFlag = this.hospitalization != null && hospitalization ? "Y" : "N" ;

			}

			/*public Long getHospitalizationFlag() {
				return hospitalizationFlag;
			}*/
			public String getHospitalizationFlag() {
				return hospitalizationFlag;
			}

			/*public void setHospitalizationFlag(Long hospitalizationFlag) {
				this.hospitalizationFlag = hospitalizationFlag;
				if(this.hospitalizationFlag != null && this.hospitalizationFlag == 1) {
					this.hospitalization = true;
				}
			}*/
			
			public void setHospitalizationFlag(String hospitalizationFlag) {
				this.hospitalizationFlag = hospitalizationFlag;
				if(this.hospitalizationFlag != null && this.hospitalizationFlag.equalsIgnoreCase("Y")) {
					this.hospitalization = true;
				}
			}
			
			
			/**
			 * @param paymentMode the paymentMode to set
			 */
			public void setPaymentMode(Boolean paymentMode) {
				
				this.paymentMode = paymentMode;
				//this.paymentModSelectFlag = this.paymentMode != null && paymentMode ? "Y" : "N" ;
				this.paymentModeFlag = null != this.paymentMode && paymentMode ? ReferenceTable.PAYMENT_MODE_CHEQUE_DD : ReferenceTable.PAYMENT_MODE_BANK_TRANSFER; 
			}

			/**
			 * @return the paymentModeFlag
			 */
			public Long getPaymentModeFlag() {
				return paymentModeFlag;
			}
			
			public void setPaymentModeFlag(Long paymentModeFlag) {
				this.paymentModeFlag = paymentModeFlag;
			}

			
			
			/*public String getPaymentModSelectFlag() {
				return paymentModSelectFlag;
			}

			public void setPaymentModSelectFlag(String paymentModSelectFlag) {
				
				this.paymentModSelectFlag = paymentModSelectFlag;
				if(this.paymentModSelectFlag != null && this.paymentModSelectFlag.equalsIgnoreCase("Y")) {
					this.paymentMode = true;
				}
				
				
			}*/

			

			public Boolean getPreHospitalization() {
				return preHospitalization;
			}

			public void setPreHospitalization(Boolean preHospitalization) {
				this.preHospitalization = preHospitalization;
				this.preHospitalizationFlag = this.preHospitalization != null && preHospitalization ? "Y" : "N" ;
			}

			/*public Long getPreHospitalizationFlag() {
				return preHospitalizationFlag;
			}*/
			
			public String getPreHospitalizationFlag() {
				return preHospitalizationFlag;
			}

			/*public void setPreHospitalizationFlag(Long preHospitalizationFlag) {
				this.preHospitalizationFlag = preHospitalizationFlag;
				if(this.preHospitalizationFlag != null && this.preHospitalizationFlag == 1) {
					this.preHospitalization = true;
				}
			}*/
			
			public void setPreHospitalizationFlag(String preHospitalizationFlag) {
				this.preHospitalizationFlag = preHospitalizationFlag;
				if(this.preHospitalizationFlag != null && this.preHospitalizationFlag.equalsIgnoreCase("Y") ) {
					this.preHospitalization = true;
				}
			}

			public Boolean getPostHospitalization() {
				return postHospitalization;
			}

			public void setPostHospitalization(Boolean postHospitalization) {
				this.postHospitalization = postHospitalization;
				this.postHospitalizationFlag = this.postHospitalization != null && postHospitalization ? "Y" : "N" ;
			}

			public String getPostHospitalizationFlag() {
				return postHospitalizationFlag;
			}

			public void setPostHospitalizationFlag(String postHospitalizationFlag) {
				this.postHospitalizationFlag = postHospitalizationFlag;
				if(this.postHospitalizationFlag != null && this.postHospitalizationFlag.equalsIgnoreCase("Y")) {
					this.postHospitalization = true;
				}
			}

			public Boolean getPartialHospitalization() {
				return partialHospitalization;
			}

			public void setPartialHospitalization(Boolean partialHospitalization) {
				this.partialHospitalization = partialHospitalization;
				this.partialHospitalizationFlag = this.partialHospitalization != null && partialHospitalization ? "Y" : "N" ;
				
			}

			public String getPartialHospitalizationFlag() {
				return partialHospitalizationFlag;
			}

			public void setPartialHospitalizationFlag(String partialHospitalizationFlag) {
				this.partialHospitalizationFlag = partialHospitalizationFlag;
				if(this.partialHospitalizationFlag != null && this.partialHospitalizationFlag.equalsIgnoreCase("Y")) {
					this.partialHospitalization = true;
				}
			}
			
			
			public String getHospitalizationRepeatFlag() {
				return hospitalizationRepeatFlag;
			}

			public void setHospitalizationRepeatFlag(String hospitalizationRepeatFlag) {
				this.hospitalizationRepeatFlag = hospitalizationRepeatFlag;
				if(this.hospitalizationRepeatFlag != null && this.hospitalizationRepeatFlag.equalsIgnoreCase("Y")) {
					this.hospitalizationRepeat = true;
				}
			}
			
			public Boolean getHospitalizationRepeat() {
				return hospitalizationRepeat;
			}

			public void setHospitalizationRepeat(Boolean hospitalizationRepeat) {
				this.hospitalizationRepeat = hospitalizationRepeat;
				this.hospitalizationRepeatFlag = this.hospitalizationRepeat != null && hospitalizationRepeat ? "Y" : "N" ;
			}


			public Boolean getLumpSumAmount() {
				return lumpSumAmount;
			}

			public void setLumpSumAmount(Boolean lumpSumAmount) {
				this.lumpSumAmount = lumpSumAmount;
				this.lumpSumAmountFlag = this.lumpSumAmount != null && lumpSumAmount ? "Y" : "N" ;
			}

			public String getLumpSumAmountFlag() {
				return lumpSumAmountFlag;
			}

			public void setLumpSumAmountFlag(String lumpSumAmountFlag) {
				this.lumpSumAmountFlag = lumpSumAmountFlag;
				if(this.lumpSumAmountFlag != null && this.lumpSumAmountFlag.equalsIgnoreCase("Y")) {
					this.lumpSumAmount = true;
				}
			}

			public Boolean getAddOnBenefitsHospitalCash() {
				return addOnBenefitsHospitalCash;
			}

			public void setAddOnBenefitsHospitalCash(Boolean addOnBenefitsHospitalCash) {
				this.addOnBenefitsHospitalCash = addOnBenefitsHospitalCash;
				this.addOnBenefitsHospitalCashFlag = this.addOnBenefitsHospitalCash != null && addOnBenefitsHospitalCash ? "Y" : "N" ;
			}

			public String getAddOnBenefitsHospitalCashFlag() {
				return addOnBenefitsHospitalCashFlag;
			}

			public void setAddOnBenefitsHospitalCashFlag(String addOnBenefitsHospitalCashFlag) {
				this.addOnBenefitsHospitalCashFlag = addOnBenefitsHospitalCashFlag;
				if(this.addOnBenefitsHospitalCashFlag != null && this.addOnBenefitsHospitalCashFlag.equalsIgnoreCase("Y")) {
					this.addOnBenefitsHospitalCash = true;
				}
			}

			public Boolean getAddOnBenefitsPatientCare() {
				return addOnBenefitsPatientCare;
			}

			public void setAddOnBenefitsPatientCare(Boolean addOnBenefitsPatientCare) {
				this.addOnBenefitsPatientCare = addOnBenefitsPatientCare;
				this.addOnBenefitsPatientCareFlag = this.addOnBenefitsPatientCare != null && addOnBenefitsPatientCare ? "Y" : "N" ;
			}

			public String getAddOnBenefitsPatientCareFlag() {
				return addOnBenefitsPatientCareFlag;
			}

			public void setAddOnBenefitsPatientCareFlag(String addOnBenefitsPatientCareFlag) {
				this.addOnBenefitsPatientCareFlag = addOnBenefitsPatientCareFlag;
				if(this.addOnBenefitsPatientCareFlag != null && this.addOnBenefitsPatientCareFlag.equalsIgnoreCase("Y")) {
					this.addOnBenefitsPatientCare = true;
				}
			}

			public String getAdditionalRemarks() {
				return additionalRemarks;
			}

			public void setAdditionalRemarks(String additionalRemarks) {
				this.additionalRemarks = additionalRemarks;
			}

			/**
			 * @return the documentCheckList
			 */
			public List<DocumentCheckListDTO> getDocumentCheckList() {
				return documentCheckList;
			}

			/**
			 * @param documentCheckList the documentCheckList to set
			 */
			public void setDocumentCheckList(List<DocumentCheckListDTO> documentCheckList) {
				this.documentCheckList = documentCheckList;
			}

			/**
			 * @param addOnBenefitsPatientCareFlag the addOnBenefitsPatientCareFlag to set
			 *//*
			public void setAddOnBenefitsPatientCareFlag(Long addOnBenefitsPatientCareFlag) {
				this.addOnBenefitsPatientCareFlag = addOnBenefitsPatientCareFlag;
			}
		*/
			
			/**
			 * @return the reconsiderationRequestFlag
			 */
			public String getReconsiderationRequestFlag() {
				return reconsiderationRequestFlag;
			}

			/**
			 * @param reconsiderationRequestFlag the reconsiderationRequestFlag to set
			 */
			public void setReconsiderationRequestFlag(String reconsiderationRequestFlag) {
				this.reconsiderationRequestFlag = reconsiderationRequestFlag;
				this.reconsiderationRequest = new SelectValue();
				this.reconsiderationRequest.setId((this.reconsiderationRequestFlag != null && this.reconsiderationRequestFlag.equalsIgnoreCase("Y")) ? ReferenceTable.COMMONMASTER_YES : ReferenceTable.COMMONMASTER_NO);
				
			}

			/**
			 * @return the dateOfAdmission
			 */
			public Date getDateOfAdmission() {
				return dateOfAdmission;
			}

			/**
			 * @param dateOfAdmission the dateOfAdmission to set
			 */
			public void setDateOfAdmission(Date dateOfAdmission) {
				this.dateOfAdmission = dateOfAdmission;
			}

			/**
			 * @return the hospitalName
			 */
			public String getHospitalName() {
				return hospitalName;
			}

			/**
			 * @param hospitalName the hospitalName to set
			 */
			public void setHospitalName(String hospitalName) {
				this.hospitalName = hospitalName;
			}

			/**
			 * @return the insuredPatientName
			 */
			public SelectValue getInsuredPatientName() {
				return insuredPatientName;
			}

			/**
			 * @param insuredPatientName the insuredPatientName to set
			 */
			public void setInsuredPatientName(SelectValue insuredPatientName) {
				this.insuredPatientName = insuredPatientName;
			}

			/**
			 * @return the modeOfReceiptValue
			 */
			public String getModeOfReceiptValue() {
				return modeOfReceiptValue;
			}

			/**
			 * @param modeOfReceiptValue the modeOfReceiptValue to set
			 */
			public void setModeOfReceiptValue(String modeOfReceiptValue) {
				this.modeOfReceiptValue = modeOfReceiptValue;
			}

			/**
			 * @return the documentReceivedFromValue
			 */
			public String getDocumentReceivedFromValue() {
				return documentReceivedFromValue;
			}

			/**
			 * @param documentReceivedFromValue the documentReceivedFromValue to set
			 */
			public void setDocumentReceivedFromValue(String documentReceivedFromValue) {
				this.documentReceivedFromValue = documentReceivedFromValue;
			}

			/**
			 * @return the reconsiderationRequestValue
			 */
			public String getReconsiderationRequestValue() {
				return reconsiderationRequestValue;
			}

			/**
			 * @param reconsiderationRequestValue the reconsiderationRequestValue to set
			 */
			public void setReconsiderationRequestValue(String reconsiderationRequestValue) {
				this.reconsiderationRequestValue = reconsiderationRequestValue;
			}

			/**
			 * @return the payeeName
			 */
			public SelectValue getPayeeName() {
				return payeeName;
			}

			/**
			 * @param payeeName the payeeName to set
			 */
			public void setPayeeName(SelectValue payeeName) {
				this.payeeName = payeeName;
			}

			/**
			 * @return the reasonForChange
			 */
			public String getReasonForChange() {
				return reasonForChange;
			}

			/**
			 * @param reasonForChange the reasonForChange to set
			 */
			public void setReasonForChange(String reasonForChange) {
				this.reasonForChange = reasonForChange;
			}

			/**
			 * @return the panNo
			 */
			public String getPanNo() {
				return panNo;
			}

			/**
			 * @param panNo the panNo to set
			 */
			public void setPanNo(String panNo) {
				this.panNo = panNo;
			}

			/**
			 * @return the legalFirstName
			 */
			public String getLegalFirstName() {
				return legalFirstName;
			}

			/**
			 * @param legalFirstName the legalFirstName to set
			 */
			public void setLegalFirstName(String legalFirstName) {
				this.legalFirstName = legalFirstName;
			}

			/**
			 * @return the legalMiddleName
			 */
			public String getLegalMiddleName() {
				return legalMiddleName;
			}

			/**
			 * @param legalMiddleName the legalMiddleName to set
			 */
			public void setLegalMiddleName(String legalMiddleName) {
				this.legalMiddleName = legalMiddleName;
			}

			/**
			 * @return the legalLastName
			 */
			public String getLegalLastName() {
				return legalLastName;
			}

			/**
			 * @param legalLastName the legalLastName to set
			 */
			public void setLegalLastName(String legalLastName) {
				this.legalLastName = legalLastName;
			}

			/**
			 * @return the payableAt
			 */
			public String getPayableAt() {
				return payableAt;
			}

			/**
			 * @param payableAt the payableAt to set
			 */
			public void setPayableAt(String payableAt) {
				this.payableAt = payableAt;
			}

			/**
			 * @return the paymentMode
			 */
			public Boolean getPaymentMode() {
				return paymentMode;
			}


			


			/**
			 * @param paymentModeFlag the paymentModeFlag to set
			 */
			
			/**
			 * @return the docAcknowledgementKey
			 */
			public Long getDocAcknowledgementKey() {
				return docAcknowledgementKey;
			}

			/**
			 * @param docAcknowledgementKey the docAcknowledgementKey to set
			 */
			public void setDocAcknowledgementKey(Long docAcknowledgementKey) {
				this.docAcknowledgementKey = docAcknowledgementKey;
			}

			/**
			 * @return the accountNo
			 */
			public String getAccountNo() {
				return accountNo;
			}

			/**
			 * @param accountNo the accountNo to set
			 */
			public void setAccountNo(String accountNo) {
				this.accountNo = accountNo;
			}

			/**
			 * @return the ifscCode
			 */
			public String getIfscCode() {
				return ifscCode;
			}

			/**
			 * @param ifscCode the ifscCode to set
			 */
			public void setIfscCode(String ifscCode) {
				this.ifscCode = ifscCode;
			}

			/**
			 * @return the branch
			 */
			public String getBranch() {
				return branch;
			}

			/**
			 * @param branch the branch to set
			 */
			public void setBranch(String branch) {
				this.branch = branch;
			}

			/**
			 * @return the bankName
			 */
			public String getBankName() {
				return bankName;
			}

			/**
			 * @param bankName the bankName to set
			 */
			public void setBankName(String bankName) {
				this.bankName = bankName;
			}

			/**
			 * @return the city
			 */
			public String getCity() {
				return city;
			}

			/**
			 * @param city the city to set
			 */
			public void setCity(String city) {
				this.city = city;
			}


			

			/**
			 * @return the key
			 */
			public Long getKey() {
				return key;
			}

			/**
			 * @param key the key to set
			 */
			public void setKey(Long key) {
				this.key = key;
			}

			/**
			 * @return the changeInReasonDOA
			 */
			public String getChangeInReasonDOA() {
				return changeInReasonDOA;
			}

			/**
			 * @param changeInReasonDOA the changeInReasonDOA to set
			 */
			public void setChangeInReasonDOA(String changeInReasonDOA) {
				this.changeInReasonDOA = changeInReasonDOA;
			}

			/**
			 * @return the rodKey
			 */
			public Long getRodKey() {
				return rodKey;
			}

			/**
			 * @param rodKey the rodKey to set
			 */
			public void setRodKey(Long rodKey) {
				this.rodKey = rodKey;
			}

			/**
			 * @return the acknowledgementNumber
			 */
			public String getAcknowledgementNumber() {
				return acknowledgementNumber;
			}

			/**
			 * @param acknowledgementNumber the acknowledgementNumber to set
			 */
			public void setAcknowledgementNumber(String acknowledgementNumber) {
				this.acknowledgementNumber = acknowledgementNumber;
			}

			public String getAcknowledgmentCreateOn() {
				return acknowledgmentCreateOn;
			}

			public void setAcknowledgmentCreateOn(String acknowledgmentCreateOn) {
				this.acknowledgmentCreateOn = acknowledgmentCreateOn;
			}

			public String getAcknowledgmentCreatedId() {
				return acknowledgmentCreatedId;
			}

			public void setAcknowledgmentCreatedId(String acknowledgmentCreatedId) {
				this.acknowledgmentCreatedId = acknowledgmentCreatedId;
			}

			public String getAcknowledgmentCreatedName() {
				return acknowledgmentCreatedName;
			}

			public void setAcknowledgmentCreatedName(String acknowledgmentCreatedName) {
				this.acknowledgmentCreatedName = acknowledgmentCreatedName;
			}

			public String getHospitalizationClaimedAmount() {
				return hospitalizationClaimedAmount;
			}

			public void setHospitalizationClaimedAmount(String hospitalizationClaimedAmount) {
				this.hospitalizationClaimedAmount = hospitalizationClaimedAmount;
			}

			public String getPreHospitalizationClaimedAmount() {
				return preHospitalizationClaimedAmount;
			}

			public void setPreHospitalizationClaimedAmount(
					String preHospitalizationClaimedAmount) {
				this.preHospitalizationClaimedAmount = preHospitalizationClaimedAmount;
			}

			public String getPostHospitalizationClaimedAmount() {
				return postHospitalizationClaimedAmount;
			}

			public void setPostHospitalizationClaimedAmount(
					String postHospitalizationClaimedAmount) {
				this.postHospitalizationClaimedAmount = postHospitalizationClaimedAmount;
			}

			public String getRodNumber() {
				return rodNumber;
			}

			public void setRodNumber(String rodNumber) {
				this.rodNumber = rodNumber;
			}

			public String getAcknowledgmentContactNumber() {
				return acknowledgmentContactNumber;
			}

			public void setAcknowledgmentContactNumber(String acknowledgmentContactNumber) {
				this.acknowledgmentContactNumber = acknowledgmentContactNumber;
			}

			public Long getBankId() {
				return bankId;
			}

			public void setBankId(Long bankId) {
				this.bankId = bankId;
			}

			public String getPaymentModSelectFlag() {
				return paymentModSelectFlag;
			}

			public void setPaymentModSelectFlag(String paymentModSelectFlag) {
				this.paymentModSelectFlag = paymentModSelectFlag;
			}


//			public SelectRODtoAddAdditionalDocumentsDTO getSelectRODtoAddAdditionalDocumentsDTO() {
//				return selectRODtoAddAdditionalDocumentsDTO;
//			}
//
//			public void setSelectRODtoAddAdditionalDocumentsDTO(
//					SelectRODtoAddAdditionalDocumentsDTO selectRODtoAddAdditionalDocumentsDTO) {
//				this.selectRODtoAddAdditionalDocumentsDTO = selectRODtoAddAdditionalDocumentsDTO;
//			}

			public SelectValue getReasonForReconsideration() {
				return reasonForReconsideration;
			}

			public void setReasonForReconsideration(SelectValue reasonForReconsideration) {
				this.reasonForReconsideration = reasonForReconsideration;
			}

			public String getReasonForReconsiderationRequestValue() {
				return reasonForReconsiderationRequestValue;
			}

			public void setReasonForReconsiderationRequestValue(
					String reasonForReconsiderationRequestValue) {
				this.reasonForReconsiderationRequestValue = reasonForReconsiderationRequestValue;
			}

			public SelectValue getCancelAcknowledgementReason() {
				return cancelAcknowledgementReason;
			}

			public void setCancelAcknowledgementReason(
					SelectValue cancelAcknowledgementReason) {
				this.cancelAcknowledgementReason = cancelAcknowledgementReason;
			}

			public String getCancelAcknowledgementRemarks() {
				return cancelAcknowledgementRemarks;
			}

			public void setCancelAcknowledgementRemarks(String cancelAcknowledgementRemarks) {
				this.cancelAcknowledgementRemarks = cancelAcknowledgementRemarks;
			}

			public Boolean getIsLetterGenerated() {
				return isLetterGenerated;
			}

			public void setIsLetterGenerated(Boolean isLetterGenerated) {
				this.isLetterGenerated = isLetterGenerated;
			}

			public Date getDateOfDischarge() {
				return dateOfDischarge;
			}

			public void setDateOfDischarge(Date dateOfDischarge) {
				this.dateOfDischarge = dateOfDischarge;
			}

			public Long getStatusId() {
				return statusId;
			}

			public void setStatusId(Long statusId) {
				this.statusId = statusId;
			}

			public Boolean getPaymentCancellationNeeded() {
				return paymentCancellationNeeded;
			}

			public void setPaymentCancellationNeeded(Boolean paymentCancellationNeeded) {
				this.paymentCancellationNeeded = paymentCancellationNeeded;
				this.paymentCancellationNeededFlag = null != this.paymentCancellationNeeded && paymentCancellationNeeded ? "Y" : "N"; 
			}


			public String getPaymentCancellationNeededFlag() {
				return paymentCancellationNeededFlag;
			}


			public void setPaymentCancellationNeededFlag(
					String paymentCancellationNeededFlag) {
				this.paymentCancellationNeededFlag = paymentCancellationNeededFlag;
			}


			public SelectValue getCancelReason() {
				return cancelReason;
			}


			public void setCancelReason(SelectValue cancelReason) {
				this.cancelReason = cancelReason;
			}


			public String getCancelRemarks() {
				return cancelRemarks;
			}


			public void setCancelRemarks(String cancelRemarks) {
				this.cancelRemarks = cancelRemarks;
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


			public void setReasonForRefering(String reasonForRefering) {
				this.reasonForRefering = reasonForRefering;
			}
			


			
			public void setReasonForReferring(String reasonForRefering) {
				this.reasonForRefering = reasonForRefering;
			}


			public SelectValue getCancellationReason() {
				return cancellationReason;
			}


			public void setCancellationReason(SelectValue cancellationReason) {
				this.cancellationReason = cancellationReason;
			}


			public String getBillingRemarks() {
				return billingRemarks;
			}


			public void setBillingRemarks(String billingRemarks) {
				this.billingRemarks = billingRemarks;
			}


			public Double getInitialTotalApprovedAmt() {
				return initialTotalApprovedAmt;
			}


			public void setInitialTotalApprovedAmt(Double initialTotalApprovedAmt) {
				this.initialTotalApprovedAmt = initialTotalApprovedAmt;
			}


			public String getReasonForReferringToBilling() {
				return reasonForReferringToBilling;
			}


			public void setReasonForReferringToBilling(String reasonForReferringToBilling) {
				this.reasonForReferringToBilling = reasonForReferringToBilling;
			}


			public String getFinancialApproverRemarks() {
				return financialApproverRemarks;
			}


			public void setFinancialApproverRemarks(String financialApproverRemarks) {
				this.financialApproverRemarks = financialApproverRemarks;
			}


			public String getQueryRemarks() {
				return queryRemarks;
			}


			public void setQueryRemarks(String queryRemarks) {
				this.queryRemarks = queryRemarks;
			}


			public String getFinancialRemarks() {
				return financialRemarks;
			}


			public void setFinancialRemarks(String financialRemarks) {
				this.financialRemarks = financialRemarks;
			}

//			public boolean getOriginalBillsReceived() {
//				return originalBillsReceived;
//			}
//
//			public void setOriginalBillsReceived(boolean originalBillsReceived) {
//				this.originalBillsReceived = originalBillsReceived;
//			}

			public String getApprovalRemarks() {
				return approvalRemarks;
			}


			public void setApprovalRemarks(String approvalRemarks) {
				this.approvalRemarks = approvalRemarks;
			}


			public String getRejectionRemarks() {
				return rejectionRemarks;
			}


			public void setRejectionRemarks(String rejectionRemarks) {
				this.rejectionRemarks = rejectionRemarks;
			}


			
			
			/**
			 * @return the paymentModeFlag
			 *//*
			public String getPaymentModeFlag() {
				return paymentModeFlag;
			}

			*//**
			 * @param paymentModeFlag the paymentModeFlag to set
			 *//*
			
			public void setPaymentModeFlag(String paymentModeFlag) {
				
				if(paymentModeFlag != null) {
					this.paymentMode = paymentModeFlag.toLowerCase().equalsIgnoreCase("cheque") ? true : false;
				}
				this.paymentModeFlag = paymentModeFlag;
			}
		*/
			
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


			public Long getReimbursementQueryKey() {
				return reimbursementQueryKey;
			}


			public void setReimbursementQueryKey(Long reimbursementQueryKey) {
				this.reimbursementQueryKey = reimbursementQueryKey;
			}


			public String getReasonForReconsiderationValue() {
				return reasonForReconsiderationValue;
			}


			public void setReasonForReconsiderationValue(
					String reasonForReconsiderationValue) {
				this.reasonForReconsiderationValue = reasonForReconsiderationValue;
			}


			public String getReferToBillEntryBillingRemarks() {
				return referToBillEntryBillingRemarks;
			}


			public void setReferToBillEntryBillingRemarks(
					String referToBillEntryBillingRemarks) {
				this.referToBillEntryBillingRemarks = referToBillEntryBillingRemarks;
			}


			public String getReferToBillEntryFAApproverRemarks() {
				return referToBillEntryFAApproverRemarks;
			}


			public void setReferToBillEntryFAApproverRemarks(
					String referToBillEntryFAApproverRemarks) {
				this.referToBillEntryFAApproverRemarks = referToBillEntryFAApproverRemarks;
			}

			public String getHospitalPayableAt() {
				return hospitalPayableAt;
			}


			public void setHospitalPayableAt(String hospitalPayableAt) {
				this.hospitalPayableAt = hospitalPayableAt;
			}

			public void setDeath(Boolean death) {
				this.death = death;		
			}


			public String getDeathFlag() {
				return deathFlag;
			}


			public void setDeathFlag(String deathFlag) {
				this.deathFlag = deathFlag;		
			}


			public Boolean getPermanentPartialDisability() {
				return permanentPartialDisability;
			}


			public void setPermanentPartialDisability(Boolean permanentPartialDisability) {
				this.permanentPartialDisability = permanentPartialDisability;
			}


			public String getPermanentPartialDisabilityflag() {
				return permanentPartialDisabilityflag;
			}


			public void setPermanentPartialDisabilityflag(String permanentPartialDisabilityflag) {
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


			public String getBenifitFlag() {
				return benifitFlag;
			}


			public void setBenifitFlag(String benifitFlag) {
				this.benifitFlag = benifitFlag;
			}


			public String getDocumentTypeValue() {
				return documentTypeValue;
			}


			public void setDocumentTypeValue(String documentTypeValue) {
				this.documentTypeValue = documentTypeValue;
			}


			public SectionDetailsTableDTO getSectionDetailsDTO() {
				return sectionDetailsDTO;
			}


			public void setSectionDetailsDTO(SectionDetailsTableDTO sectionDetailsDTO) {
				this.sectionDetailsDTO = sectionDetailsDTO;
			}

			public Long getDocumentTypeId() {
				return documentTypeId;

			}


			public void setDocumentTypeId(Long documentTypeId) {
				this.documentTypeId = documentTypeId;
			}


			public Long getAckKey() {
				return ackKey;
			}


			public void setAckKey(Long ackKey) {
				this.ackKey = ackKey;
			}


			public List<AddOnCoversTableDTO> getOptionalCoversDeletedList() {
				return optionalCoversDeletedList;
			}


			public void setOptionalCoversDeletedList(
					List<AddOnCoversTableDTO> optionalCoversDeletedList) {
				this.optionalCoversDeletedList = optionalCoversDeletedList;
			}


			public List<AddOnCoversTableDTO> getAddOnCoversDeletedList() {
				return addOnCoversDeletedList;
			}


			public void setAddOnCoversDeletedList(
					List<AddOnCoversTableDTO> addOnCoversDeletedList) {
				this.addOnCoversDeletedList = addOnCoversDeletedList;
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


			public List<DocumentCheckListDTO> getDefaultDocumentCheckList() {
				return defaultDocumentCheckList;
			}


			public void setDefaultDocumentCheckList(
					List<DocumentCheckListDTO> defaultDocumentCheckList) {
				this.defaultDocumentCheckList = defaultDocumentCheckList;
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


			public Boolean getOtherBenefits() {
				return otherBenefits;
			}


			public void setOtherBenefits(Boolean otherBenefits) {
				this.otherBenefits = otherBenefits;
				this.otherBenefitsFlag = this.otherBenefits != null && otherBenefits ? "Y" : "N" ;
			}


			public String getOtherBenefitsFlag() {
				return otherBenefitsFlag;
			}


			public void setOtherBenefitsFlag(String otherBenefitsFlag) {
				this.otherBenefitsFlag = otherBenefitsFlag;
				if(this.otherBenefitsFlag != null && this.otherBenefitsFlag.equalsIgnoreCase("Y")) {
					this.otherBenefits = true;
				}
			}
			
			
			public Boolean getEmergencyMedicalEvaluation() {
				return emergencyMedicalEvaluation;
			}


			public void setEmergencyMedicalEvaluation(Boolean emergencyMedicalEvaluation) {
				this.emergencyMedicalEvaluation = emergencyMedicalEvaluation;
				this.emergencyMedicalEvaluationFlag = this.emergencyMedicalEvaluation != null && emergencyMedicalEvaluation ? "Y" : "N" ;
			}
			
			public String getEmergencyMedicalEvaluationFlag() {
				return emergencyMedicalEvaluationFlag;
			}


			public void setEmergencyMedicalEvaluationFlag(
					String emergencyMedicalEvaluationFlag) {
				this.emergencyMedicalEvaluationFlag = emergencyMedicalEvaluationFlag;
				if(this.emergencyMedicalEvaluationFlag != null && this.emergencyMedicalEvaluationFlag.equalsIgnoreCase("Y")) {
					this.emergencyMedicalEvaluation = true;
				}
			}

			
			public Boolean getCompassionateTravel() {
				return compassionateTravel;
			}


			public void setCompassionateTravel(Boolean compassionateTravel) {
				this.compassionateTravel = compassionateTravel;
				this.compassionateTravelFlag = this.compassionateTravel != null && compassionateTravel ? "Y" : "N" ;
			}


			public String getCompassionateTravelFlag() {
				return compassionateTravelFlag;
			}


			public void setCompassionateTravelFlag(String compassionateTravelFlag) {
				this.compassionateTravelFlag = compassionateTravelFlag;
				if(this.compassionateTravelFlag != null && this.compassionateTravelFlag.equalsIgnoreCase("Y")) {
					this.compassionateTravel = true;
				}
			}


			public Boolean getRepatriationOfMortalRemains() {
				return repatriationOfMortalRemains;
			}


			public void setRepatriationOfMortalRemains(Boolean repatriationOfMortalRemains) {
				this.repatriationOfMortalRemains = repatriationOfMortalRemains;
				this.repatriationOfMortalRemainsFlag = this.repatriationOfMortalRemains != null && repatriationOfMortalRemains ? "Y" : "N" ;
			}


			public String getRepatriationOfMortalRemainsFlag() {
				return repatriationOfMortalRemainsFlag;
			}


			public void setRepatriationOfMortalRemainsFlag(
					String repatriationOfMortalRemainsFlag) {
				this.repatriationOfMortalRemainsFlag = repatriationOfMortalRemainsFlag;
				if(this.repatriationOfMortalRemainsFlag != null && this.repatriationOfMortalRemainsFlag.equalsIgnoreCase("Y")) {
					this.repatriationOfMortalRemains = true;
				}
			}


			public Boolean getPreferredNetworkHospital() {
				return preferredNetworkHospital;
			}


			public void setPreferredNetworkHospital(Boolean preferredNetworkHospital) {
				this.preferredNetworkHospital = preferredNetworkHospital;
				this.preferredNetworkHospitalFlag = this.preferredNetworkHospital != null && preferredNetworkHospital ? "Y" : "N" ;
			}


			public String getPreferredNetworkHospitalFlag() {
				return preferredNetworkHospitalFlag;
			}


			public void setPreferredNetworkHospitalFlag(String preferredNetworkHospitalFlag) {
				this.preferredNetworkHospitalFlag = preferredNetworkHospitalFlag;
				if(this.preferredNetworkHospitalFlag != null && this.preferredNetworkHospitalFlag.equalsIgnoreCase("Y")) {
					this.preferredNetworkHospital = true;
				}
			}


			public Boolean getSharedAccomodation() {
				return sharedAccomodation;
			}


			public void setSharedAccomodation(Boolean sharedAccomodation) {
				this.sharedAccomodation = sharedAccomodation;
				this.sharedAccomodationFlag = this.sharedAccomodation != null && sharedAccomodation ? "Y" : "N" ;
			}


			public String getSharedAccomodationFlag() {
				return sharedAccomodationFlag;
			}


			public void setSharedAccomodationFlag(String sharedAccomodationFlag) {
				this.sharedAccomodationFlag = sharedAccomodationFlag;
				if(this.sharedAccomodationFlag != null && this.sharedAccomodationFlag.equalsIgnoreCase("Y")) {
					this.sharedAccomodation = true;
				}
			}


			public String getOtherBenefitclaimedAmount() {
				return otherBenefitclaimedAmount;
			}


			public void setOtherBenefitclaimedAmount(String otherBenefitclaimedAmount) {
				this.otherBenefitclaimedAmount = otherBenefitclaimedAmount;
			}


			public Boolean getPaHospitalisation() {
				return paHospitalisation;
			}


			public void setPaHospitalisation(Boolean paHospitalisation) {
				this.paHospitalisation = paHospitalisation;
			}


			public Boolean getPaPartialHospitalisation() {
				return paPartialHospitalisation;
			}


			public void setPaPartialHospitalisation(Boolean paPartialHospitalisation) {
				this.paPartialHospitalisation = paPartialHospitalisation;
			}


			public Boolean getAccidentOrDeath() {
				return accidentOrDeath;
			}


			public void setAccidentOrDeath(Boolean accidentOrDeath) {
				this.accidentOrDeath = accidentOrDeath;
			}


			public Date getAccidentOrDeathDate() {
				return accidentOrDeathDate;
			}


			public void setAccidentOrDeathDate(Date accidentOrDeathDate) {
				this.accidentOrDeathDate = accidentOrDeathDate;
			}


			public SelectValue getDocumentType() {
				return documentType;
			}


			public void setDocumentType(SelectValue documentType) {
				this.documentType = documentType;
			}


//			public BeanItemContainer<SelectValue> getAdditionalCovers() {
//				return additionalCovers;
//			}
//
//
//			public void setAdditionalCovers(BeanItemContainer<SelectValue> additionalCovers) {
//				this.additionalCovers = additionalCovers;
//			}
//
//
//			public BeanItemContainer<SelectValue> getOptionalCovers() {
//				return optionalCovers;
//			}
//
//
//			public void setOptionalCovers(BeanItemContainer<SelectValue> optionalCovers) {
//				this.optionalCovers = optionalCovers;
//			}


			public List<AddOnCoversTableDTO> getAddOnCoversList() {
				return addOnCoversList;
			}


			public void setAddOnCoversList(List<AddOnCoversTableDTO> addOnCoversList) {
				this.addOnCoversList = addOnCoversList;
			}


			public List<AddOnCoversTableDTO> getOptionalCoversList() {
				return optionalCoversList;
			}


			public void setOptionalCoversList(List<AddOnCoversTableDTO> optionalCoversList) {
				this.optionalCoversList = optionalCoversList;
			}


			public Double getBenefitClaimedAmt() {
				return benefitClaimedAmt;
			}


			public void setBenefitClaimedAmt(Double benefitClaimedAmt) {
				this.benefitClaimedAmt = benefitClaimedAmt;
			}


			public Boolean getDeath() {
				return death;
			}


			public String getBenifitClaimedAmount() {
				return benifitClaimedAmount;
			}


			public void setBenifitClaimedAmount(String benifitClaimedAmount) {
				this.benifitClaimedAmount = benifitClaimedAmount;
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


			public SelectValue getGpaCategory() {
				return gpaCategory;
			}


			public void setGpaCategory(SelectValue gpaCategory) {
				this.gpaCategory = gpaCategory;
			}


			public String getGpaSection() {
				return gpaSection;
			}


			public void setGpaSection(String gpaSection) {
				this.gpaSection = gpaSection;
			}


			public String getPayModeChangeReason() {
				return payModeChangeReason;
			}


			public void setPayModeChangeReason(String payModeChangeReason) {
				this.payModeChangeReason = payModeChangeReason;
			}


			public Long getPaymentModeChangeFlag() {
				return paymentModeChangeFlag;
			}


			public void setPaymentModeChangeFlag(Long paymentModeChangeFlag) {
				this.paymentModeChangeFlag = paymentModeChangeFlag;
			}


			public Boolean getSourceOfDocument() {
				return sourceOfDocument;
			}


			public void setSourceOfDocument(Boolean sourceOfDocument) {
				this.sourceOfDocument = sourceOfDocument;
			}


			public String getSourceOfDocumentValue() {
				return sourceOfDocumentValue;
			}


			public void setSourceOfDocumentValue(String sourceOfDocumentValue) {
				this.sourceOfDocumentValue = sourceOfDocumentValue;
			}
			public String getDocumentVerificationFlag() {
				return documentVerificationFlag;
			}


			public void setDocumentVerificationFlag(String documentVerificationFlag) {
				this.documentVerificationFlag = documentVerificationFlag;
			}


			public Double getHospClaimedAmountDocRec() {
				return hospClaimedAmountDocRec;
			}


			public void setHospClaimedAmountDocRec(Double hospClaimedAmountDocRec) {
				this.hospClaimedAmountDocRec = hospClaimedAmountDocRec;
			}


			public Double getPreHospClaimedAmountDocRec() {
				return preHospClaimedAmountDocRec;
			}


			public void setPreHospClaimedAmountDocRec(Double preHospClaimedAmountDocRec) {
				this.preHospClaimedAmountDocRec = preHospClaimedAmountDocRec;
			}


			public Double getPostHospClaimedAmountDocRec() {
				return postHospClaimedAmountDocRec;
			}


			public void setPostHospClaimedAmountDocRec(Double postHospClaimedAmountDocRec) {
				this.postHospClaimedAmountDocRec = postHospClaimedAmountDocRec;
			}


			public Double getOtherBenefitsAmountDocRec() {
				return otherBenefitsAmountDocRec;
			}


			public void setOtherBenefitsAmountDocRec(Double otherBenefitsAmountDocRec) {
				this.otherBenefitsAmountDocRec = otherBenefitsAmountDocRec;
			}


			public Boolean getDocumentVerification() {
				return documentVerification;
			}


			public void setDocumentVerification(Boolean documentVerification) {
				this.documentVerification = documentVerification;
			}


			public Boolean getIsOtherBenefitApplicableInPreauth() {
				return isOtherBenefitApplicableInPreauth;
			}


			public void setIsOtherBenefitApplicableInPreauth(
					Boolean isOtherBenefitApplicableInPreauth) {
				this.isOtherBenefitApplicableInPreauth = isOtherBenefitApplicableInPreauth;
			}


			public Boolean getIsEmergencyMedicalEvacuation() {
				return isEmergencyMedicalEvacuation;
			}


			public void setIsEmergencyMedicalEvacuation(Boolean isEmergencyMedicalEvacuation) {
				this.isEmergencyMedicalEvacuation = isEmergencyMedicalEvacuation;
			}


			public Boolean getIsRepatriationOfMortal() {
				return isRepatriationOfMortal;
			}


			public void setIsRepatriationOfMortal(Boolean isRepatriationOfMortal) {
				this.isRepatriationOfMortal = isRepatriationOfMortal;
			}


			public String getDocumentReceivedDate() {
				return documentReceivedDate;
			}


			public void setDocumentReceivedDate(String documentReceivedDate) {
				this.documentReceivedDate = documentReceivedDate;
			}


			public Boolean getIsEditable() {
				return isEditable;
			}


			public void setIsEditable(Boolean isEditable) {
				this.isEditable = isEditable;
			}
				
		}




package com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.mapper;

import java.io.Serializable;
import java.util.List;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.impl.generator.EclipseJdtCompilerStrategy;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.preauth.wizard.dto.CoordinatorDTO;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.ImplantDetailsDTO;
import com.shaic.claim.preauth.wizard.dto.NewProcedureTableDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureTableDTO;
import com.shaic.claim.preauth.wizard.dto.SpecialityDTO;
import com.shaic.claim.preauth.wizard.dto.TreatingDoctorDTO;
import com.shaic.claim.preauth.wizard.dto.UpdateOtherClaimDetailDTO;
import com.shaic.claim.premedical.dto.OtherBenefitsTableDto;
import com.shaic.claim.reimbursement.dto.EmployeeMasterDTO;
import com.shaic.claim.reimbursement.dto.ExtraEmployeeEffortDTO;
import com.shaic.claim.reimbursement.dto.OtherClaimDetailsDTO;
import com.shaic.claim.reimbursement.dto.OtherClaimDiagnosisDTO;
import com.shaic.claim.reimbursement.dto.QuantumReductionDetailsDTO;
import com.shaic.claim.reimbursement.dto.ZonalReviewUpdateHospitalDetailsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.domain.ClaimPayment;
import com.shaic.domain.PreviousClaimedHistory;
import com.shaic.domain.PreviousClaimedHospitalization;
import com.shaic.domain.RRCDetails;
import com.shaic.domain.RRCRequest;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.TmpEmployee;
import com.shaic.domain.UpdateHospital;
import com.shaic.domain.preauth.BenefitAmountDetails;
import com.shaic.domain.preauth.Coordinator;
import com.shaic.domain.preauth.DiagnosisPED;
import com.shaic.domain.preauth.ImplantDetails;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Procedure;
import com.shaic.domain.preauth.RodBillSummary;
import com.shaic.domain.preauth.Speciality;
import com.shaic.domain.preauth.TreatingDoctorDetails;
import com.shaic.domain.preauth.UpdateOtherClaimDetails;

public class ZonalMedicalReviewMapper implements Serializable {

	private static final long serialVersionUID = -5809070666366947161L;
	
	static MapperFactory mapperFactory = new DefaultMapperFactory.Builder().compilerStrategy(new EclipseJdtCompilerStrategy()).build();
	
	static ZonalMedicalReviewMapper myObj;
	
	private static BoundMapperFacade<Reimbursement, PreauthDTO> reimbursementMapper;
	private static BoundMapperFacade<Speciality, SpecialityDTO> specialityIndMap;
	private static BoundMapperFacade<PreviousClaimedHospitalization, OtherClaimDiagnosisDTO> otherClaimsDiagnosisIndMap;
	private static BoundMapperFacade<ProcedureDTO, ProcedureTableDTO> oldProcedureIndMap;
	private static BoundMapperFacade<ProcedureDTO, NewProcedureTableDTO> newProcedureIndMap;
	private static BoundMapperFacade<Procedure, ProcedureDTO> procedureIndMap;
	private static BoundMapperFacade<PedValidation, DiagnosisDetailsTableDTO> pedValidationIndMap;
	private static BoundMapperFacade<Coordinator, CoordinatorDTO> coordinatorMapper;
	private static BoundMapperFacade<UpdateHospital, ZonalReviewUpdateHospitalDetailsDTO> updateHospitalMapper;
	private static BoundMapperFacade<PreviousClaimedHistory, OtherClaimDetailsDTO> claimedHistoryMapper;
	private static BoundMapperFacade<RodBillSummary, UploadDocumentDTO> rodBillSummaryMapper;
	
	//Added for lot creation enhancement
	private static BoundMapperFacade<ClaimPayment, PreauthDTO> claimPaymentMapper;
	
	
	private static BoundMapperFacade<DiagnosisPED, PedDetailsTableDTO> diagnosiPEDIndMap;
	
	//Added for RRC Request.
	
	private static BoundMapperFacade<RRCRequest, QuantumReductionDetailsDTO> rrcRequestDataMapper;
	private static BoundMapperFacade<RRCDetails, ExtraEmployeeEffortDTO> rrcDetailsDataMapper; 
	private static BoundMapperFacade<TreatingDoctorDetails, TreatingDoctorDTO> treatingDoctorMap;
	private static BoundMapperFacade<ImplantDetails, ImplantDetailsDTO> implantDetailsMap;
	
	private static MapperFacade specialityMapper;
	private static MapperFacade newProcedureMapper;
	private static MapperFacade procedureMapper;
	private static MapperFacade procedureListMapper;
	private static MapperFacade oldProcedureMapper;
	private static MapperFacade newProcedureListMapper;
	private static MapperFacade finalProcedureMapper;
	private static MapperFacade finalPedvalidation;
	private static MapperFacade diagnosisPedMapper;
	private static MapperFacade otherClaimsDiagnosisMapper;
	private static MapperFacade employeeDetailsMapper;	
	private static MapperFacade rodBillSummaryDetailsMapper;
	private static MapperFacade otherBenefitMapper;
	private static MapperFacade treatingDoctorMapper;
	private static MapperFacade implantMapper;
	
	
	/*private static ClassMapBuilder<Reimbursement, PreauthDTO> reimbursementMap = mapperFactory.classMap(Reimbursement.class,PreauthDTO.class);
	//private static ClassMapBuilder<Reimbursement, PreauthDTO> otherReimbursmentMap = mapperFactory.classMap(Reimbursement.class,PreauthDTO.class);
	private static ClassMapBuilder<UpdateHospital, ZonalReviewUpdateHospitalDetailsDTO> updateHospitalMap = mapperFactory.classMap(UpdateHospital.class,ZonalReviewUpdateHospitalDetailsDTO.class);
	private static ClassMapBuilder<PreviousClaimedHistory, OtherClaimDetailsDTO> claimedHistoryMap = mapperFactory.classMap(PreviousClaimedHistory.class,OtherClaimDetailsDTO.class);
	private static ClassMapBuilder<Coordinator, CoordinatorDTO> coordinatorMap = mapperFactory.classMap(Coordinator.class,CoordinatorDTO.class);
	private static ClassMapBuilder<Speciality, SpecialityDTO> specialityMap = mapperFactory.classMap(Speciality.class, SpecialityDTO.class);
	private static ClassMapBuilder<DiagnosisDetailsTableDTO,DiagnosisDetailsTableDTO> pedvalidationClassMap = mapperFactory.classMap(DiagnosisDetailsTableDTO.class,DiagnosisDetailsTableDTO.class);
	private static ClassMapBuilder<Procedure,ProcedureDTO> finalProcedureMap=mapperFactory.classMap(Procedure.class, ProcedureDTO.class);
	private static ClassMapBuilder<PedValidation,DiagnosisDetailsTableDTO> finalPedValidationMap = mapperFactory.classMap(PedValidation.class, DiagnosisDetailsTableDTO.class);
	private static ClassMapBuilder<DiagnosisPED, PedDetailsTableDTO> diagnosisPEDMap = mapperFactory.classMap(DiagnosisPED.class, PedDetailsTableDTO.class);
	private static ClassMapBuilder<PreviousClaimedHospitalization, OtherClaimDiagnosisDTO> otherClaimsDiagnosisMap = mapperFactory.classMap(PreviousClaimedHospitalization.class, OtherClaimDiagnosisDTO.class);
	private static ClassMapBuilder<TmpEmployee, EmployeeMasterDTO> employeeDetailsMap = mapperFactory.classMap(TmpEmployee.class, EmployeeMasterDTO.class);
	private static ClassMapBuilder<RRCRequest, QuantumReductionDetailsDTO> rrcRequestDataMap  = mapperFactory.classMap(RRCRequest.class, QuantumReductionDetailsDTO.class);
	private static ClassMapBuilder<RRCDetails, ExtraEmployeeEffortDTO> rrcDetailsDataMap  = mapperFactory.classMap(RRCDetails.class, ExtraEmployeeEffortDTO.class);
	private static ClassMapBuilder<RodBillSummary, UploadDocumentDTO> rodBillDetailsMap  = mapperFactory.classMap(RodBillSummary.class, UploadDocumentDTO.class);
	private static ClassMapBuilder<ClaimPayment, PreauthDTO> claimPaymentMap  = mapperFactory.classMap(ClaimPayment.class, PreauthDTO.class);*/
	
	private static ClassMapBuilder<Reimbursement, PreauthDTO> reimbursementMap = null;
	//private static ClassMapBuilder<Reimbursement, PreauthDTO> otherReimbursmentMap = mapperFactory.classMap(Reimbursement.class,PreauthDTO.class);
	private static ClassMapBuilder<UpdateHospital, ZonalReviewUpdateHospitalDetailsDTO> updateHospitalMap = null;
	private static ClassMapBuilder<PreviousClaimedHistory, OtherClaimDetailsDTO> claimedHistoryMap = null;
	private static ClassMapBuilder<Coordinator, CoordinatorDTO> coordinatorMap = null;
	private static ClassMapBuilder<Speciality, SpecialityDTO> specialityMap = null;
	private static ClassMapBuilder<DiagnosisDetailsTableDTO,DiagnosisDetailsTableDTO> pedvalidationClassMap = null;
	private static ClassMapBuilder<Procedure,ProcedureDTO> finalProcedureMap = null;
	private static ClassMapBuilder<PedValidation,DiagnosisDetailsTableDTO> finalPedValidationMap = null;
	private static ClassMapBuilder<DiagnosisPED, PedDetailsTableDTO> diagnosisPEDMap = null;
	private static ClassMapBuilder<PreviousClaimedHospitalization, OtherClaimDiagnosisDTO> otherClaimsDiagnosisMap = null;
	private static ClassMapBuilder<TmpEmployee, EmployeeMasterDTO> employeeDetailsMap = null;
	private static ClassMapBuilder<RRCRequest, QuantumReductionDetailsDTO> rrcRequestDataMap  = null;
	private static ClassMapBuilder<RRCDetails, ExtraEmployeeEffortDTO> rrcDetailsDataMap  = null;
	private static ClassMapBuilder<RodBillSummary, UploadDocumentDTO> rodBillDetailsMap  = null;
	private static ClassMapBuilder<ClaimPayment, PreauthDTO> claimPaymentMap  = null;
	private static ClassMapBuilder<BenefitAmountDetails, OtherBenefitsTableDto> otherBenefitMap  = null;
	private static ClassMapBuilder<UpdateOtherClaimDetails , UpdateOtherClaimDetailDTO> otherInsurerMap = null;
	private static ClassMapBuilder<TreatingDoctorDetails , TreatingDoctorDTO> treatingDoctorDetailMap = null;
	private static ClassMapBuilder<ImplantDetails, ImplantDetailsDTO> implantMap = null;
	
	public static void getAllMapValues() {
		
		reimbursementMap = mapperFactory.classMap(Reimbursement.class,PreauthDTO.class);
		//private static ClassMapBuilder<Reimbursement, PreauthDTO> otherReimbursmentMap = mapperFactory.classMap(Reimbursement.class,PreauthDTO.class);
		updateHospitalMap = mapperFactory.classMap(UpdateHospital.class,ZonalReviewUpdateHospitalDetailsDTO.class);
		claimedHistoryMap = mapperFactory.classMap(PreviousClaimedHistory.class,OtherClaimDetailsDTO.class);
		coordinatorMap = mapperFactory.classMap(Coordinator.class,CoordinatorDTO.class);
		specialityMap = mapperFactory.classMap(Speciality.class, SpecialityDTO.class);
		pedvalidationClassMap = mapperFactory.classMap(DiagnosisDetailsTableDTO.class,DiagnosisDetailsTableDTO.class);
		finalProcedureMap=mapperFactory.classMap(Procedure.class, ProcedureDTO.class);
		finalPedValidationMap = mapperFactory.classMap(PedValidation.class, DiagnosisDetailsTableDTO.class);
		diagnosisPEDMap = mapperFactory.classMap(DiagnosisPED.class, PedDetailsTableDTO.class);
		otherClaimsDiagnosisMap = mapperFactory.classMap(PreviousClaimedHospitalization.class, OtherClaimDiagnosisDTO.class);
		employeeDetailsMap = mapperFactory.classMap(TmpEmployee.class, EmployeeMasterDTO.class);
		rrcRequestDataMap  = mapperFactory.classMap(RRCRequest.class, QuantumReductionDetailsDTO.class);
		rrcDetailsDataMap  = mapperFactory.classMap(RRCDetails.class, ExtraEmployeeEffortDTO.class);
		rodBillDetailsMap  = mapperFactory.classMap(RodBillSummary.class, UploadDocumentDTO.class);
		claimPaymentMap  = mapperFactory.classMap(ClaimPayment.class, PreauthDTO.class);
		otherBenefitMap = mapperFactory.classMap(BenefitAmountDetails.class, OtherBenefitsTableDto.class);
		otherInsurerMap = mapperFactory.classMap(UpdateOtherClaimDetails.class, UpdateOtherClaimDetailDTO.class);
		treatingDoctorDetailMap = mapperFactory.classMap(TreatingDoctorDetails.class, TreatingDoctorDTO.class);
		implantMap = mapperFactory.classMap(ImplantDetails.class, ImplantDetailsDTO.class);
		
		reimbursementMap.field("key","key");
		reimbursementMap.field("claim.key","claimKey");
		reimbursementMap.field("dateOfAdmission","preauthDataExtractionDetails.admissionDate");
		reimbursementMap.field("status.processValue", "statusValue");
		reimbursementMap.field("status.key", "statusKey");
		reimbursementMap.field("stage.key", "stageKey");
		reimbursementMap.field("rodNumber","rodNumber");
		reimbursementMap.field("createdDate", "createDate");
		reimbursementMap.field("createdBy","createdBy");
		
		/**
		 * new column added
		 */
		reimbursementMap.field("copayRemarks", "copayRemarks");
		
		
		reimbursementMap.field("docAcknowLedgement","preauthDataExtractionDetails.docAckknowledgement");
		reimbursementMap.field("dateOfDeath","preauthDataExtractionDetails.deathDate");
		reimbursementMap.field("deathReason","preauthDataExtractionDetails.reasonForDeath");
//		reimbursementMap.field("terminatorCover","preauthDataExtractionDetails.terminateCoverFlag");
		reimbursementMap.field("numberOfDays","preauthDataExtractionDetails.noOfDays");
		reimbursementMap.field("natureOfTreatment.key","preauthDataExtractionDetails.natureOfTreatment.id");
		reimbursementMap.field("natureOfTreatment.value","preauthDataExtractionDetails.natureOfTreatment.value");
		reimbursementMap.field("consultationDate", "preauthDataExtractionDetails.firstConsultantDate");
		reimbursementMap.field("criticalIllnessFlag","preauthDataExtractionDetails.criticalIllnessFlag");
		reimbursementMap.field("corporateBufferFlag","preauthDataExtractionDetails.corporateBufferFlag");
		reimbursementMap.field("criticalIllness.key","preauthDataExtractionDetails.specifyIllness.id");
		reimbursementMap.field("criticalIllness.value","preauthDataExtractionDetails.specifyIllness.value");
		reimbursementMap.field("roomCategory.key","preauthDataExtractionDetails.roomCategory.id");
		reimbursementMap.field("roomCategory.value","preauthDataExtractionDetails.roomCategory.value");
		reimbursementMap.field("ventilatorSupport","preauthDataExtractionDetails.ventilatorSupportFlag");
		reimbursementMap.field("treatmentType.key","preauthDataExtractionDetails.treatmentType.id");
		reimbursementMap.field("treatmentType.value","preauthDataExtractionDetails.treatmentType.value");
		reimbursementMap.field("treatmentRemarks","preauthDataExtractionDetails.treatmentRemarks");
		reimbursementMap.field("automaticRestoration","preauthDataExtractionDetails.autoRestoration");
		reimbursementMap.field("illness.key","preauthDataExtractionDetails.illness.id");
		reimbursementMap.field("illness.value","preauthDataExtractionDetails.illness.value");
		reimbursementMap.field("patientStatus.key","preauthDataExtractionDetails.patientStatus.id");
		reimbursementMap.field("patientStatus.value","preauthDataExtractionDetails.patientStatus.value");
		reimbursementMap.field("relapseFlag","preauthPreviousClaimsDetails.relapseFlag");
		reimbursementMap.field("relapseRemarks","preauthPreviousClaimsDetails.relapseRemarks");
		reimbursementMap.field("coordinatorFlag","coordinatorDetails.refertoCoordinatorFlag");
		reimbursementMap.field("currentProvisionAmt","currentProvisionAmt");
		reimbursementMap.field("otherInsurerHospAmt","otherInsurerAmount");
		reimbursementMap.field("otherInsurerPreHospAmt","preauthMedicalDecisionDetails.otherInsurerPreHospAmountClaimed");
		reimbursementMap.field("prorataPercentage","prorataPercentage");
		reimbursementMap.field("packageAvailableFlag","packageAvailableFlag");
		reimbursementMap.field("prorataDeductionFlag","prorataDeductionFlag");
		reimbursementMap.field("otherInsurerPostHospAmt","preauthMedicalDecisionDetails.otherInsurerPostHospAmountClaimed");
		reimbursementMap.field("bankId","bankId");
		reimbursementMap.field("paymentModeId","paymentModeId");
		reimbursementMap.field("payeeName","payeeName");
		reimbursementMap.field("nameAsPerBankAccount","nameAsPerBankAccount");
		reimbursementMap.field("payeeEmailId","payeeEmailId");
		reimbursementMap.field("payModeChangeReason", "payModeChangeReason");
		reimbursementMap.field("panNumber","panNumber");
		reimbursementMap.field("payableAt","payableAt");
		reimbursementMap.field("accountNumber","accountNumber");
		reimbursementMap.field("modifiedBy", "modifiedBy");
		reimbursementMap.field("insuredKey.key","insuredKey");
		reimbursementMap.field("hospitalId","hospitalId");
		reimbursementMap.field("zonalDate", "zonalDate");
		reimbursementMap.field("claim.incidenceDate","preauthDataExtractionDetails.accidentDeathDate");
		reimbursementMap.field("claim.incidenceFlag","preauthDataExtractionDetails.accidentOrDeathFlag");
		reimbursementMap.field("benefitsId.key","preauthDataExtractionDetails.paBenefits.id");
		reimbursementMap.field("benefitsId.value","preauthDataExtractionDetails.paBenefits.value");
		reimbursementMap.field("accidentCauseId.key","preauthDataExtractionDetails.causeOfAccident.id");
		reimbursementMap.field("accidentCauseId.value","preauthDataExtractionDetails.causeOfAccident.value");
		reimbursementMap.field("pedFlag","preauthDataExtractionDetails.preExistingDisablitiesFlag");
		reimbursementMap.field("pedDisablitiyDetails","preauthDataExtractionDetails.disabilitesRemarks");
		reimbursementMap.field("otherBenefitApprovedAmt", "preauthDataExtractionDetails.totalOtherBenefitsApprovedAmt");
		reimbursementMap.field("reconsiderationRequest", "preauthDataExtractionDetails.reconsiderationFlag");
		
		reimbursementMap.field("decisionChangeReason.key","preauthMedicalDecisionDetails.decisionChangeReason.id");
		reimbursementMap.field("decisionChangeReason.value","preauthMedicalDecisionDetails.decisionChangeReason.value");
		reimbursementMap.field("decisionChangeRemarks", "preauthMedicalDecisionDetails.decisionChangeRemarks");

		
		reimbursementMap.field("claim.accidentDate","preauthDataExtractionDetails.dateOfAccident");
		reimbursementMap.field("claim.deathDate","preauthDataExtractionDetails.dateOfDeath");
		reimbursementMap.field("claim.disablementDate","preauthDataExtractionDetails.dateOfDisablement");
		reimbursementMap.field("unNamedKey", "unNamedKey");
		reimbursementMap.field("skipZmrFlag", "skipZmrFlag");
		
		reimbursementMap.field("doaChangeReason","preauthDataExtractionDetails.changeInReasonDOA");
//		reimbursementMap.field("reasonForChange", "preauthDataExtractionDetails.changeInReasonDOA");
		reimbursementMap.field("paymentModeId","preauthDataExtractionDetails.paymentModeFlag");
		reimbursementMap.field("payeeName", "preauthDataExtractionDetails.payeeName.value");
		reimbursementMap.field("payeeEmailId", "preauthDataExtractionDetails.emailId");
		reimbursementMap.field("payModeChangeReason", "preauthDataExtractionDetails.payModeChangeReason");
		reimbursementMap.field("reasonForChange", "preauthDataExtractionDetails.reasonForChange");
		reimbursementMap.field("panNumber", "preauthDataExtractionDetails.panNo");
		reimbursementMap.field("legalHeirFirstName", "preauthDataExtractionDetails.legalFirstName");
		reimbursementMap.field("legalHeirMiddleName", "preauthDataExtractionDetails.legalMiddleName");
		reimbursementMap.field("legalHeirLastName", "preauthDataExtractionDetails.legalLastName");
		reimbursementMap.field("accountNumber", "preauthDataExtractionDetails.accountNo");
		reimbursementMap.field("accountPreference", "preauthDataExtractionDetails.accountPref");
		reimbursementMap.field("nameAsPerBankAccount", "preauthDataExtractionDetails.nameAsPerBank");
		reimbursementMap.field("accountType", "preauthDataExtractionDetails.accType");
		reimbursementMap.field("financialApprovedAmount", "preauthMedicalDecisionDetails.financialApprovedAmt");
		reimbursementMap.field("billingApprovedAmount", "preauthMedicalDecisionDetails.billingApprovedAmt");
		reimbursementMap.field("approvedAmount", "preauthMedicalDecisionDetails.approvedAmount");
		reimbursementMap.field("approvedAmount","preauthDataExtractionDetails.totalApprAmt");
		reimbursementMap.field("claimApprovalAmount","preauthDataExtractionDetails.payableToInsured");
		reimbursementMap.field("addOnCoversApprovedAmount","hospitalCashAmt");
		reimbursementMap.field("optionalApprovedAmount","patientCareAmt");
		reimbursementMap.field("doaChangeReason", "preauthDataExtractionDetails.changeOfDOA");
		reimbursementMap.field("dateOfDischarge", "preauthDataExtractionDetails.dischargeDate");
		
		// Reimbursement Fields
		reimbursementMap.field("systemOfMedicine","preauthDataExtractionDetails.systemOfMedicine");
		reimbursementMap.field("hopsitaliztionDueto.key","preauthDataExtractionDetails.hospitalisationDueTo.id");
		reimbursementMap.field("hopsitaliztionDueto.value","preauthDataExtractionDetails.hospitalisationDueTo.value");
		
		reimbursementMap.field("hopsitaliztionDueto.key","preauthDataExtractionDetails.hospitalisationDueTo.id");
		reimbursementMap.field("hopsitaliztionDueto.value","preauthDataExtractionDetails.hospitalisationDueTo.value");
		reimbursementMap.field("injuryCauseId","preauthDataExtractionDetails.causeOfInjury.id");
		reimbursementMap.field("injuryDate","preauthDataExtractionDetails.injuryDate");
		reimbursementMap.field("medicoLeagalCare","preauthDataExtractionDetails.medicalLegalCaseFlag");
		reimbursementMap.field("reportedToPolice","preauthDataExtractionDetails.reportedToPoliceFlag");
		reimbursementMap.field("firstDiseaseDetectedDate","preauthDataExtractionDetails.diseaseFirstDetectedDate");
		reimbursementMap.field("dateOfDelivery","preauthDataExtractionDetails.deliveryDate");
		reimbursementMap.field("rejectionRemarks","preauthMedicalProcessingDetails.rejectionRemarks");
		//reimbursementMap.field("rejectionRemarks2","preauthMedicalDecisionDetails.rejectionRemarks2");
		reimbursementMap.field("approvalRemarks","preauthMedicalProcessingDetails.approvalRemarks");
		reimbursementMap.field("billingRemarks","preauthMedicalDecisionDetails.billingRemarks");
		reimbursementMap.field("billingRemarks","preauthDataExtractionDetails.billingRemarks");
		reimbursementMap.field("financialApprovalRemarks","preauthMedicalDecisionDetails.financialRemarks");
		reimbursementMap.field("preHospitalizationDays","preauthDataExtractionDetails.preHospitalisationPeriod");
		reimbursementMap.field("postHospitalizationDays","preauthDataExtractionDetails.postHospitalisationPeriod");
		reimbursementMap.field("domicillary","preauthDataExtractionDetails.domicillaryHospitalisationFlag");
		reimbursementMap.field("billingRemarks","preauthMedicalDecisionDetails.billingRemarks");
		reimbursementMap.field("billingCompletedDate", "billingDate");
		reimbursementMap.field("medicalRemarks","preauthMedicalDecisionDetails.medicalRemarks");
		reimbursementMap.field("doctorNote","preauthMedicalDecisionDetails.doctorNote");
		reimbursementMap.field("firNumber","preauthDataExtractionDetails.firNumber");
		reimbursementMap.field("attachedPoliceReport","preauthDataExtractionDetails.policeReportAttachedFlag");
		reimbursementMap.field("investigatorRemarks","preauthMedicalDecisionDetails.investigationReviewRemarks");
		reimbursementMap.field("investigatorCode", "preauthMedicalDecisionDetails.investigatorCode");
		reimbursementMap.field("investigationReportReview", "preauthMedicalDecisionDetails.investigationReportReviewedFlag");
		reimbursementMap.field("corporateUtilizedAmt", "preauthMedicalDecisionDetails.corporateBufferUtilizedAmt");
		reimbursementMap.field("billingInternalRemarks","preauthDataExtractionDetails.billingInternalRemarks");
		reimbursementMap.field("faInternalRemarks","preauthDataExtractionDetails.faInternalRemarks");
		
		
		reimbursementMap.field("medicalCompletedDate", "medicalCompletedDate");
		reimbursementMap.field("financialCompletedDate", "financialCompletedDate");
		
		reimbursementMap.field("sectionCategory","preauthDataExtractionDetails.section.id");
		reimbursementMap.field("typeOfDelivery","preauthDataExtractionDetails.typeOfDelivery.id");
		reimbursementMap.field("treatmentStartDate","preauthDataExtractionDetails.treatmentStartDate");
		reimbursementMap.field("treatmentEndDate","preauthDataExtractionDetails.treatmentEndDate");
		reimbursementMap.field("domicillaryNumberOfDays","preauthDataExtractionDetails.hospitalizationNoOfDays");
		
		reimbursementMap.field("amtConsCopayPercentage","amountConsCopayPercentage");
		reimbursementMap.field("balanceSICopayPercentage","balanceSICopayPercentage");
		reimbursementMap.field("amtConsAftCopayAmount","amountConsAftCopayAmt");
		reimbursementMap.field("balanceSIAftCopayAmt","balanceSIAftCopayAmt");
		reimbursementMap.field("otherInsurerApplicableFlag", "preauthMedicalDecisionDetails.otherInsurerApplicableFlag");
		//Added for refer to bill entry remarks
		reimbursementMap.field("billEntryRemarks","preauthMedicalDecisionDetails.referToBillEntryBillingRemarks");
		reimbursementMap.field("zonalDate", "zonalDate");
		reimbursementMap.field("parentKey", "rodParentKey");
		reimbursementMap.field("version", "version");
		
		reimbursementMap.field("fvrNotRequiredRemarks.key","preauthMedicalDecisionDetails.fvrNotRequiredRemarks.id");
		reimbursementMap.field("fvrNotRequiredRemarks.value","preauthMedicalDecisionDetails.fvrNotRequiredRemarks.value");
		reimbursementMap.field("fvrAlertFlag","fvrAlertFlag");
		reimbursementMap.field("amountClaimedFromHospital","preauthMedicalDecisionDetails.amountClaimedFrmHosp");
		reimbursementMap.field("scoringFlag", "hospitalScoreFlag");
		reimbursementMap.field("icacFlag", "icacProcessFlag");
		 //GLX2020076
		reimbursementMap.field("reasonForIcdExclusion", "preauthMedicalDecisionDetails.icdExclusionReason");
		
		reimbursementMap.field("covidTreatmentId.key","preauthDataExtractionDetails.homeCareTreatment.id");
		reimbursementMap.field("covidTreatmentId.value","preauthDataExtractionDetails.homeCareTreatment.value");
		reimbursementMap.field("typeOfAdmission","preauthDataExtractionDetails.admissionType.id");
		
		reimbursementMap.field("nomineeFlag","preauthDataExtractionDetails.nomineeDeceasedFlag");
		
		reimbursementMap.field("remarksBillEntry","preauthDataExtractionDetails.remarksBillEntry");
		reimbursementMap.field("grievanceRepresentation","grievanceRepresentation");
		
		updateHospitalMap.field("key","key");
		updateHospitalMap.field("hospitalName","hospitalName");
		updateHospitalMap.field("phoneNumber","hospitalPhoneNo");
		updateHospitalMap.field("pincode","hospitalPincode");
		updateHospitalMap.field("cityId","hospitalCityId");
		updateHospitalMap.field("stateId","hospitalStateId");
		updateHospitalMap.field("hospitalId","hospitalId");
		updateHospitalMap.field("hospitalTypeId","hospitalTypeId");
		updateHospitalMap.field("registrationNumber","hopitalRegNumber");
		updateHospitalMap.field("address","hospitalAddress1");
		updateHospitalMap.field("otFacilityFlag","otFacilityFlag");
		updateHospitalMap.field("icuFacilityFlag","icuFacilityFlag");
		updateHospitalMap.field("inpatientBeds","inpatientBeds");
		
		claimedHistoryMap.field("key","key");
		claimedHistoryMap.field("companyName","otherClaimCompanyName");
		claimedHistoryMap.field("commencementDate","otherClaimCommencementDate");
		claimedHistoryMap.field("policyNumber","otherClaimPolicyNumber");
		claimedHistoryMap.field("sumInsured","otherClaimSumInsured");
		claimedHistoryMap.field("hospitalizationFlag","last4YearsHospitalisationFlag");
		
	
		finalPedValidationMap.field("diagnosisId","diagnosisId");
		finalPedValidationMap.field("diagnosisId","diagnosisName.id");
		finalPedValidationMap.field("key", "key");
//		finalPedValidationMap.field("pedName","pedName");
		finalPedValidationMap.field("policyAging","policyAgeing");
//		finalPedValidationMap.field("diagnosisRemarks","remarks");
		finalPedValidationMap.field("icdChpterId","icdChapter.id");
		finalPedValidationMap.field("icdBlockId","icdBlock.id");
		finalPedValidationMap.field("icdCodeId","icdCode.id");
		finalPedValidationMap.field("subLimitApplicable","sublimitApplicableFlag");
		finalPedValidationMap.field("sublimitId","sublimitName.limitId");
		finalPedValidationMap.field("considerForPayment","considerForPaymentFlag");
		
		//R20181300
		finalPedValidationMap.field("pedImpactId.key","pedImpactOnDiagnosis.id");
		finalPedValidationMap.field("pedImpactId.value","pedImpactOnDiagnosis.value");
		finalPedValidationMap.field("notPayingReason.key","reasonForNotPaying.id");
		finalPedValidationMap.field("notPayingReason.value","reasonForNotPaying.value");		
		//R20181300 
		
		finalPedValidationMap.field("sumInsuredRestrictionId","sumInsuredRestriction.id");
		finalPedValidationMap.field("approveAmount","oldApprovedAmount");
		finalPedValidationMap.field("approveAmount","approvedAmount");
		finalPedValidationMap.field("netApprovedAmount","oldApprovedAmount");
		finalPedValidationMap.field("netApprovedAmount","netApprovedAmount");
		finalPedValidationMap.field("diffAmount","oldDiffAmount");
		finalPedValidationMap.field("diffAmount","diffAmount");
		finalPedValidationMap.field("approvedRemarks","approveRemarks");
		finalPedValidationMap.field("amountConsideredAmount","amountConsideredAmount");
		finalPedValidationMap.field("minimumAmount","minimumAmount");
		finalPedValidationMap.field("copayPercentage","copayPercentage");
		finalPedValidationMap.field("copayAmount","copayAmount");
		finalPedValidationMap.field("netAmount","netAmount");
		finalPedValidationMap.field("copayAmount","copayAmount");
		finalPedValidationMap.field("createdBy", "createdBy");
		finalPedValidationMap.field("createdDate", "createDate");
		finalPedValidationMap.field("coPayTypeId.key","coPayTypeId.id");
		finalPedValidationMap.field("coPayTypeId.value","coPayTypeId.value");
		
		finalPedValidationMap.field("ambulanceChargeFlag", "isAmbChargeFlag");
		finalPedValidationMap.field("ambulanceCharges", "ambulanceCharge");
		finalPedValidationMap.field("ambulanceChargeWithAmount", "amtWithAmbulanceCharge");
		
		
		finalPedValidationMap.field("recTypeFlag","recTypeFlag");
		finalPedValidationMap.field("sittingsInput","sittingsInput");
		finalPedValidationMap.field("oldIcdCode","oldIcdCode.id");
		finalPedValidationMap.field("oldDiagnosisId","oldDiagnosisId.id");
		finalPedValidationMap.field("icdFlag","icdFlag");
		finalPedValidationMap.field("diagnosisFlag","diagnosisFlag");
		finalPedValidationMap.field("primaryDiagnosis","primaryDiagnosisFlag");

		
		diagnosisPEDMap.field("key","key");
		diagnosisPEDMap.field("pedName","pedName");
		diagnosisPEDMap.field("pedCode","pedCode");
		diagnosisPEDMap.field("diagnosisRemarks","remarks");
		diagnosisPEDMap.field("exclusionDetails.exclusion","exclusionDetails.value");
		diagnosisPEDMap.field("exclusionDetails.key","exclusionDetails.id");
		diagnosisPEDMap.field("diagonsisImpact.key","pedExclusionImpactOnDiagnosis.id");
		diagnosisPEDMap.field("diagonsisImpact.value","pedExclusionImpactOnDiagnosis.value");
		
		specialityMap.field("key", "key");
//		specialityMap.field("preauth.key","preAuthKey");
		specialityMap.field("specialityType.key","specialityType.id");
		specialityMap.field("specialityType.value","specialityType.value");
		specialityMap.field("procedure.key", "procedure.id");
		specialityMap.field("procedure.procedureName", "procedure.value");
		specialityMap.field("remarks","remarks");
		specialityMap.field("createdBy", "createdBy");
		specialityMap.field("createdDate", "createDate");
		specialityMap.field("oldspecialityType.key","oldspecialityType.id");
		specialityMap.field("oldspecialityType.value","oldspecialityType.value");
		specialityMap.field("splFlag","splFlag");
		
		otherClaimsDiagnosisMap.field("key", "key");
		otherClaimsDiagnosisMap.field("hospitalizationDate","hospitalizationDate");
		otherClaimsDiagnosisMap.field("diagnosis","diagnosis");
		
		coordinatorMap.field("key","key");
		coordinatorMap.field("policy.key","policyKey");
		coordinatorMap.field("intimation.key","intimationKey");
//		coordinatorMap.field("preauth.key","preauthKey");
		coordinatorMap.field("coordinatorRequestType.key","typeofCoordinatorRequest.id");
		coordinatorMap.field("coordinatorRequestType.value","typeofCoordinatorRequest.value");
		coordinatorMap.field("requestorRemarks","reasonForRefering");
		coordinatorMap.field("coordinatorRemarks","coordinatorRemarks");
		
		finalProcedureMap.field("procedureName","procedureNameValue");
		finalProcedureMap.field("procedureID","procedureName.id");
		finalProcedureMap.field("procedureName","procedureName.value");
		finalProcedureMap.field("procedureID","procedureCode.id");
		finalProcedureMap.field("procedureCode","procedureCode.value");
		finalProcedureMap.field("procedureCode","procedureCodeValue");
		
		finalProcedureMap.field("key", "key");
		finalProcedureMap.field("newProcedureFlag", "newProcedureFlag");
		finalProcedureMap.field("packageRate","packageRate");
		finalProcedureMap.field("dayCareProcedure","dayCareProcedureFlag");
		finalProcedureMap.field("considerForDayCare","considerForDayFlag");
		finalProcedureMap.field("sublimitNameId","sublimitName.limitId");
		finalProcedureMap.field("subLimitApplicable","sublimitApplicableFlag");
		finalProcedureMap.field("considerForPayment","considerForPaymentFlag");
		finalProcedureMap.field("procedureRemarks","remarks");
		finalProcedureMap.field("procedureStatus.key","procedureStatus.id");
		finalProcedureMap.field("procedureStatus.value","procedureStatus.value");
		finalProcedureMap.field("exculsionDetails.key","exclusionDetails.id");
		finalProcedureMap.field("exculsionDetails.value","exclusionDetails.value");
		
		finalProcedureMap.field("approvedAmount","oldApprovedAmount");
		finalProcedureMap.field("approvedAmount","approvedAmount");
		finalProcedureMap.field("netApprovedAmount","oldApprovedAmount");
		finalProcedureMap.field("netApprovedAmount","netApprovedAmount");
		finalProcedureMap.field("diffAmount","oldDiffAmount");
		finalProcedureMap.field("diffAmount","diffAmount");
		finalProcedureMap.field("approvedRemarks","approvedRemarks");
		finalProcedureMap.field("amountConsideredAmount","amountConsideredAmount");
		finalProcedureMap.field("minimumAmount","minimumAmount");
		finalProcedureMap.field("copayPercentage","copayPercentage");
		finalProcedureMap.field("copayAmount","copayAmount");
		finalProcedureMap.field("netAmount","netAmount");
		finalProcedureMap.field("copayAmount","copayAmount");
		finalProcedureMap.field("createdBy", "createdBy");
		finalProcedureMap.field("createdDate", "createDate");
		
		finalProcedureMap.field("recTypeFlag","recTypeFlag");
		finalProcedureMap.field("sittingsInput","sittingsInput");
		
		finalProcedureMap.field("ambulanceChargeFlag", "isAmbChargeFlag");
		finalProcedureMap.field("ambulanceCharges", "ambulanceCharge");
		finalProcedureMap.field("ambulanceChargeWithAmount", "amtWithAmbulanceCharge");
		finalProcedureMap.field("coPayTypeId.key","coPayTypeId.id");
		finalProcedureMap.field("coPayTypeId.value","coPayTypeId.value");
		finalProcedureMap.field("recTypeFlag","recTypeFlag");
		finalProcedureMap.field("oldprocedureID","oldprocedureID");
		finalProcedureMap.field("procedureFlag","procedureFlag");
		
		//Added for RRC request view.
		employeeDetailsMap.field("key", "employeeTblkey");
		employeeDetailsMap.field("empId", "employeeId");
		employeeDetailsMap.field("loginId", "loginId");
		//employeeDetailsMap.field("organizationUnitId", "organizationUnitId");
		employeeDetailsMap.field("empFirstName", "employeeFirstName");
		employeeDetailsMap.field("empMiddleName","employeeMiddleName");
		employeeDetailsMap.field("empLastName","employeeLastName");
		employeeDetailsMap.field("empCPUId","empCpuId");
		
		//rrcRequestDataMap.field("rrcRequestNumber","requestNo");
		rrcRequestDataMap.field("preAuthAmount","preAuthAmount");
		rrcRequestDataMap.field("finalBillAmount","finalBillAmount");
		rrcRequestDataMap.field("settlementAmount","settlementAmount");
		rrcRequestDataMap.field("anhAmount","anhAmount");
		rrcRequestDataMap.field("savedAmount","savedAmount");
		rrcRequestDataMap.field("diagnosis","diagnosis");
		rrcRequestDataMap.field("management","management");
		rrcRequestDataMap.field("anh","anh.value");
		
		rrcDetailsDataMap.field("employeeId","selEmployeeId.value");
		rrcDetailsDataMap.field("employeeName","selEmployeeName.value");
		rrcDetailsDataMap.field("score","score");
		rrcDetailsDataMap.field("remarks","remarks");
		
		
		
		rodBillDetailsMap.field("key","rodBillSummaryKey");
		rodBillDetailsMap.field("fileName","fileName");
		rodBillDetailsMap.field("remarks","billingWorkSheetRemarks");
		rodBillDetailsMap.field("documentToken","dmsDocToken");
		rodBillDetailsMap.field("createdBy","createdBy");
		rodBillDetailsMap.field("createdDate","createdDate");
		rodBillDetailsMap.field("reimbursement.rodNumber","rodNo");
		
		claimPaymentMap.field("rodNumber", "rodNumber");
		claimPaymentMap.field("pioCode","newIntimationDTO.policy.homeOfficeCode"); //Policy issuing office code
//		claimPaymentMap.field("paymentCpuCode", ""); --> to be discussed.
		claimPaymentMap.field("productCode", "newIntimationDTO.policy.product.code");  
		claimPaymentMap.field("claimType", "claimDTO.claimType.value");
		claimPaymentMap.field("intimationNumber","newIntimationDTO.intimationId");
		claimPaymentMap.field("claimNumber","claimDTO.claimId");
		claimPaymentMap.field("policyNumber","newIntimationDTO.policy.policyNumber");
		claimPaymentMap.field("policySysId","newIntimationDTO.policy.policySystemId");
		claimPaymentMap.field("riskId","newIntimationDTO.insuredPatient.insuredId");
		claimPaymentMap.field("insuredName","newIntimationDTO.insuredPatient.insuredName");
		
		//claimPaymentMap.field("paymentType","");
		claimPaymentMap.field("approvedAmount","preauthMedicalDecisionDetails.initialTotalApprovedAmt");
		claimPaymentMap.field("cpuCode","newIntimationDTO.cpuCode");
		claimPaymentMap.field("ifscCode","preauthDataExtractionDetails.ifscCode");
		claimPaymentMap.field("accountNumber","preauthDataExtractionDetails.accountNo");
		claimPaymentMap.field("branchName","preauthDataExtractionDetails.branch");
		claimPaymentMap.field("payeeName","preauthDataExtractionDetails.payeeName.value");
		claimPaymentMap.field("panNumber", "preauthDataExtractionDetails.panNo");
		claimPaymentMap.field("hospitalCode", "newIntimationDTO.hospitalDto.hospitalCode");
		claimPaymentMap.field("emailId", "preauthDataExtractionDetails.emailId");
		claimPaymentMap.field("proposerCode", "newIntimationDTO.policy.proposerCode");
		claimPaymentMap.field("proposerName", "newIntimationDTO.policy.proposerFirstName");
		claimPaymentMap.field("bankName","preauthDataExtractionDetails.bankName");
		claimPaymentMap.field("payModeChangeReason","preauthDataExtractionDetails.payModeChangeReason");
		//claimPaymentMap.field("rodkey","key");
		
		
	//	claimPaymentMap.field("netAmount", ""); --> from premia.
		
		//rrcDetailsDataMap.field("employeeZone","");
		//Code for credit type to be implemented
		otherBenefitMap.field("key", "benefitKey");
		otherBenefitMap.field("benefit.key", "benefitObjId");
		otherBenefitMap.field("benefit.limitName", "benefitName");
		otherBenefitMap.field("numberOfDays", "noOfDays");
		otherBenefitMap.field("claimedAmt", "amtClaimed");
		otherBenefitMap.field("nonPayableAmt", "nonPayable");
		otherBenefitMap.field("netAmt", "netPayable");
		otherBenefitMap.field("eligibleAmt", "eligibleAmt");
		otherBenefitMap.field("payableAmt", "approvedAmt");
		otherBenefitMap.field("amountPayableToInsured", "amtPaybableToInsured");
		otherBenefitMap.field("amtAlreadyPaid", "amtAlreadyPaid");
		otherBenefitMap.field("balancePayable", "balancePayable");
		otherBenefitMap.field("remarks", "remarks");
		
		otherInsurerMap.field("key", "key");
		otherInsurerMap.field("intimationKey","intimationKey");
		otherInsurerMap.field("intimationId","intimationNo");
		otherInsurerMap.field("claimKey","claimKey");
		otherInsurerMap.field("cashlessKey","cashlessKey");
		otherInsurerMap.field("claimType","claimType");
		otherInsurerMap.field("insurerName","insurerName");
		otherInsurerMap.field("primaryDiagnosiProcedure","primaryProcedure");
		otherInsurerMap.field("icdChapter","icdChaper");
		otherInsurerMap.field("icdBlock","icdBlock");
		otherInsurerMap.field("icdCode","icdCode");
		otherInsurerMap.field("claimedAmount","claimAmount");
		otherInsurerMap.field("deductibility","deductibles");
		otherInsurerMap.field("admissibleAmount","admissibleAmount");
		otherInsurerMap.field("remarks","remarks");
		otherInsurerMap.field("status.key","statusKey");
		otherInsurerMap.field("stage.key","stageKey");
		otherInsurerMap.field("editFlag", "editFlagValue");
		
		treatingDoctorDetailMap.field("key", "key");
		treatingDoctorDetailMap.field("claimKey","claimKey");
		treatingDoctorDetailMap.field("transactionKey","transactionKey");
		treatingDoctorDetailMap.field("doctorName","treatingDoctorName");
		treatingDoctorDetailMap.field("doctorQualification","qualification");
		treatingDoctorDetailMap.field("createdBy","createdBy");
		treatingDoctorDetailMap.field("createdDate","createDate");
		treatingDoctorDetailMap.field("activeStatus","activeStatus");
		treatingDoctorDetailMap.field("oldDoctorName","oldDoctorName");
		treatingDoctorDetailMap.field("oldQualification","oldQualification");
		treatingDoctorDetailMap.field("dcDoctorFlag","dcDoctorFlag");
		
		implantMap.field("key", "key");
		implantMap.field("claimKey","claimKey");
		implantMap.field("transactionKey","transactionKey");
		implantMap.field("implantName","implantName");
		implantMap.field("implantCost","implantCost");
		implantMap.field("implantType","implantType");
		implantMap.field("createdBy","createdBy");
		implantMap.field("createDate","createDate");
		implantMap.field("activeStatus","activeStatus");
		implantMap.field("oldImplantName","oldImplantName");
		implantMap.field("oldImplantCost","oldImplantCost");
		implantMap.field("oldImplantType","oldImplantType");
		implantMap.field("dcImplantFlag","dcImplantFlag");

		reimbursementMap.register();
		updateHospitalMap.register();
		coordinatorMap.register();
		specialityMap.register();
		otherClaimsDiagnosisMap.register();
		pedvalidationClassMap.register();
		finalProcedureMap.register();
		finalPedValidationMap.register();
		claimedHistoryMap.register();
		employeeDetailsMap.register();
		rrcRequestDataMap.register();
		rrcDetailsDataMap.register();
		rodBillDetailsMap.register();
		claimPaymentMap.register();
		otherBenefitMap.register();
		otherInsurerMap.register();
		treatingDoctorDetailMap.register();
		implantMap.register();
		
		reimbursementMapper = mapperFactory.getMapperFacade(Reimbursement.class, PreauthDTO.class);
		updateHospitalMapper = mapperFactory.getMapperFacade(UpdateHospital.class, ZonalReviewUpdateHospitalDetailsDTO.class);
		coordinatorMapper = mapperFactory.getMapperFacade(Coordinator.class, CoordinatorDTO.class);
		specialityIndMap = mapperFactory.getMapperFacade(Speciality.class, SpecialityDTO.class);
		pedValidationIndMap = mapperFactory.getMapperFacade(PedValidation.class, DiagnosisDetailsTableDTO.class);
		diagnosiPEDIndMap = mapperFactory.getMapperFacade(DiagnosisPED.class, PedDetailsTableDTO.class);
		claimedHistoryMapper = mapperFactory.getMapperFacade(PreviousClaimedHistory.class, OtherClaimDetailsDTO.class);
		otherClaimsDiagnosisIndMap = mapperFactory.getMapperFacade(PreviousClaimedHospitalization.class, OtherClaimDiagnosisDTO.class);
		rrcRequestDataMapper = mapperFactory.getMapperFacade(RRCRequest.class, QuantumReductionDetailsDTO.class);
		rrcDetailsDataMapper = mapperFactory.getMapperFacade(RRCDetails.class, ExtraEmployeeEffortDTO.class);
		rodBillSummaryMapper = mapperFactory.getMapperFacade(RodBillSummary.class, UploadDocumentDTO.class);
		implantDetailsMap = mapperFactory.getMapperFacade(ImplantDetails.class, ImplantDetailsDTO.class);
		
		specialityMapper = mapperFactory.getMapperFacade();
		finalPedvalidation = mapperFactory.getMapperFacade();
		otherClaimsDiagnosisMapper = mapperFactory.getMapperFacade();
		otherBenefitMapper = mapperFactory.getMapperFacade();
		
		procedureIndMap = mapperFactory.getMapperFacade(Procedure.class, ProcedureDTO.class);
		
		newProcedureMapper = mapperFactory.getMapperFacade();
		procedureMapper = mapperFactory.getMapperFacade();
		
		oldProcedureMapper= mapperFactory.getMapperFacade();
		newProcedureListMapper= mapperFactory.getMapperFacade();
		finalProcedureMapper = mapperFactory.getMapperFacade();
		
		finalPedvalidation = mapperFactory.getMapperFacade();
		diagnosisPedMapper = mapperFactory.getMapperFacade();
		procedureListMapper = mapperFactory.getMapperFacade();
		pedValidationIndMap = mapperFactory.getMapperFacade(PedValidation.class, DiagnosisDetailsTableDTO.class);
		employeeDetailsMapper = mapperFactory.getMapperFacade();
		rodBillSummaryDetailsMapper = mapperFactory.getMapperFacade();
		claimPaymentMapper = mapperFactory.getMapperFacade(ClaimPayment.class, PreauthDTO.class);
		treatingDoctorMap = mapperFactory.getMapperFacade(TreatingDoctorDetails.class, TreatingDoctorDTO.class);
		treatingDoctorMapper = mapperFactory.getMapperFacade();
		implantMapper =  mapperFactory.getMapperFacade();
	}
	
	
	/**************************** UPDATE HOSPITAL *********************************/
	
	public ZonalReviewUpdateHospitalDetailsDTO getUpdateHospitalDTO(UpdateHospital reimbursement) {
		ZonalReviewUpdateHospitalDetailsDTO dest = updateHospitalMapper.map(reimbursement);
		return dest;
	}
	
	public UpdateHospital getUpdateHospital(ZonalReviewUpdateHospitalDetailsDTO updateHospitalDTO) {
		UpdateHospital dest = updateHospitalMapper.mapReverse(updateHospitalDTO);
		return dest;
	}
	
	/******************************************** UPDATE HOSPITAL END ************************/
	
/**************************** CLAIMED HISTORY *********************************/
	
	public OtherClaimDetailsDTO getClaimedHistoryDTO(PreviousClaimedHistory claimedHistory) {
		OtherClaimDetailsDTO dest = claimedHistoryMapper.map(claimedHistory);
		return dest;
	}
	
	public PreviousClaimedHistory getClaimedHistory(OtherClaimDetailsDTO claimedDTO) {
		PreviousClaimedHistory dest = claimedHistoryMapper.mapReverse(claimedDTO);
		return dest;
	}
	
	/******************************************** CLAIMED HISTORY END ************************/
	
	

	/**************************** PREAUTH *********************************/
	
	public PreauthDTO getReimbursementDTO(Reimbursement reimbursement) {
		PreauthDTO dest = reimbursementMapper.map(reimbursement);
		return dest;
	}
	
	public Reimbursement getReimbursement(PreauthDTO preauthDTO) {
		Reimbursement dest = reimbursementMapper.mapReverse(preauthDTO);
		return dest;
	}
	
	/******************************************** PREAUTH END ************************/
	
	
	/**************************** COORDINATOR **********************************/
	
	public CoordinatorDTO getCoordinatorDTO(Coordinator coordinator) {
		CoordinatorDTO dest = coordinatorMapper.map(coordinator);
		return dest;
	}
	
	public Coordinator getCoordinator(CoordinatorDTO coordinatorDTO) {
		Coordinator dest = coordinatorMapper.mapReverse(coordinatorDTO);
		return dest;
	}
	
	/***************************** COORDINATOR END ****************************/
	
	/**************************** SPECIALITY **********************************/
	
	public List<Speciality> getSpecialityList(List<SpecialityDTO> specialityDTOList) {
		List<Speciality> mapAsList = specialityMapper.mapAsList(specialityDTOList, Speciality.class);
		return mapAsList;
	}
	
	public SpecialityDTO getSpecialityDTO(Speciality speciality) {
		SpecialityDTO dest = specialityIndMap.map(speciality);
		return dest;
	}
	
	public Speciality getSpeciality(SpecialityDTO specialityDTO) {
		Speciality dest = specialityIndMap.mapReverse(specialityDTO);
		return dest;
	}
	
	public List<SpecialityDTO> getSpecialityDTOList(List<Speciality> specialityList) {
		List<SpecialityDTO> mapAsList = specialityMapper.mapAsList(specialityList, SpecialityDTO.class);
		return mapAsList;
	}
	
	/**************************** SPECIALITY END **********************************/
	
	
	
/**************************** OtherClaims Diagnosis **********************************/
	
	public List<PreviousClaimedHospitalization> getPreviousHospitalizationList(List<OtherClaimDiagnosisDTO> specialityDTOList) {
		List<PreviousClaimedHospitalization> mapAsList = otherClaimsDiagnosisMapper.mapAsList(specialityDTOList, PreviousClaimedHospitalization.class);
		return mapAsList;
	}
	
	public OtherClaimDiagnosisDTO getOtherClaimsDiagnosisDTO(PreviousClaimedHospitalization otherClaims) {
		OtherClaimDiagnosisDTO dest = otherClaimsDiagnosisIndMap.map(otherClaims);
		return dest;
	}
	
	public PreviousClaimedHospitalization getClaimedHospitalization(OtherClaimDiagnosisDTO otherClaimsDiagnosisDTO) {
		PreviousClaimedHospitalization dest = otherClaimsDiagnosisIndMap.mapReverse(otherClaimsDiagnosisDTO);
		return dest;
	}
	
	public List<OtherClaimDiagnosisDTO> getOtherClaimDiagnosisDTOList(List<PreviousClaimedHospitalization> hospitalizationList) {
		List<OtherClaimDiagnosisDTO> mapAsList = otherClaimsDiagnosisMapper.mapAsList(hospitalizationList, OtherClaimDiagnosisDTO.class);
		return mapAsList;
	}
	
	/****************************  OtherClaims Diagnosis  END **********************************/
	
	/**************************** EXISITING PROCEDURE **********************************/
	
	
	public ProcedureDTO getProcedureDTO(ProcedureTableDTO procedureTableDTO) {
		ProcedureDTO dest = oldProcedureIndMap.mapReverse(procedureTableDTO);
		return dest;
	}
	
	public ProcedureTableDTO getProcedureTableDTO(ProcedureDTO procedureDTO) {
		ProcedureTableDTO dest = oldProcedureIndMap.map(procedureDTO);
		return dest;
	}
	
	public List<Procedure> getProcedureList(List<ProcedureTableDTO> procedureDTOList) {
		List<Procedure> mapAsList = procedureMapper.mapAsList(procedureDTOList, Procedure.class);
		return mapAsList;
	}
	
	public List<ProcedureTableDTO> getProcedureDTOList(List<Procedure> procedureList) {
		List<ProcedureTableDTO> mapAsList = procedureMapper.mapAsList(procedureList, ProcedureTableDTO.class);
		return mapAsList;
	}
	
	public List<ProcedureDTO> getProcedureDto(List<ProcedureTableDTO> procedureList){
		List<ProcedureDTO> mapAsList = oldProcedureMapper.mapAsList(procedureList, ProcedureDTO.class);
		return mapAsList;
	}
	
	/********************** EXISTING PROCEDURE ********************************/
	
	/**************************** NEW PROCEDURE **********************************/
	
	public List<Procedure> getNewProcedureList(List<NewProcedureTableDTO> newProcedureDTOList) {
		List<Procedure> mapAsList = newProcedureMapper.mapAsList(newProcedureDTOList, Procedure.class);
		return mapAsList;
	}
	
	public List<NewProcedureTableDTO> getNewProcedureDTOList(List<Procedure> procedureList) {
		List<NewProcedureTableDTO> mapAsList = newProcedureMapper.mapAsList(procedureList, NewProcedureTableDTO.class);
		return mapAsList;
	}
	
	public List<ProcedureDTO> getNewProcedureDto(List<NewProcedureTableDTO> procedureList){
		List<ProcedureDTO> mapAsList = newProcedureListMapper.mapAsList(procedureList, ProcedureDTO.class);
		return mapAsList;
	}
	
	public List<Procedure> getNewProcedureListDto(List<ProcedureDTO> procedureList){
		List<Procedure> mapAsList = finalProcedureMapper.mapAsList(procedureList, Procedure.class);
		return mapAsList;
	}
	
	public NewProcedureTableDTO getNewProcedureTableDTO(ProcedureDTO procedureDto) {
		NewProcedureTableDTO dest = newProcedureIndMap.map(procedureDto);
		return dest;
	}
	
	public ProcedureDTO getProcedureDTO(NewProcedureTableDTO newProcedureDTO) {
		ProcedureDTO dest = newProcedureIndMap.mapReverse(newProcedureDTO);
		return dest;
	}
	
	
	/************************************ COMMON PROCEDURE ******************************/
	public Procedure getProcedure(ProcedureDTO procedureDTO) {
		Procedure dest = procedureIndMap.mapReverse(procedureDTO);
		return dest;
	}
	
	public ProcedureDTO getProcedureDTO(Procedure procedure) {
		ProcedureDTO dest = procedureIndMap.map(procedure);
		return dest;
	}
	
	public List<Procedure> getProcedureMainList(List<ProcedureDTO> procedureDTO) {
		 List<Procedure> dest = procedureListMapper.mapAsList(procedureDTO, Procedure.class);
		return dest;
	}
	
	public List<ProcedureDTO> getProcedureMainDTOList(List<Procedure> procedure) {
		List<ProcedureDTO> dest = procedureListMapper.mapAsList(procedure, ProcedureDTO.class);
		return dest;
	}
	
	/************************************ COMMON PROCEDURE END ******************************/
	
	/**************************** NEW PROCEDURE END**********************************/
	
	
	
	/**************************** PED VALIDATION**********************************/
	
	public PedValidation getPedValidation(DiagnosisDetailsTableDTO pedValidationDTO){
		PedValidation dest = pedValidationIndMap.mapReverse(pedValidationDTO);
		return dest;
	}
	
	public DiagnosisDetailsTableDTO getNewPedValidationDto(PedValidation pedValidation){
		DiagnosisDetailsTableDTO dest = pedValidationIndMap.map(pedValidation);
		return dest;
	}
	
	
	public List<PedValidation> getNewPedValidationListDto(List<DiagnosisDetailsTableDTO> procedureList){
		List<PedValidation> mapAsList = finalPedvalidation.mapAsList(procedureList, PedValidation.class);
		return mapAsList;
	}
	
	public List<DiagnosisDetailsTableDTO> getNewPedValidationTableListDto(List<PedValidation> pedValidationList){
		List<DiagnosisDetailsTableDTO> mapAsList = finalPedvalidation.mapAsList(pedValidationList, DiagnosisDetailsTableDTO.class);
		return mapAsList;
	}
		
	
	/**************************** PED VALIDATION END**********************************/
	
	
	
/**************************** DIAGNOSIS PED**********************************/
	
	public DiagnosisPED getDiagnosisPED(PedDetailsTableDTO pedDetailsDTO){
		DiagnosisPED dest = diagnosiPEDIndMap.mapReverse(pedDetailsDTO);
		return dest;
	}
	
	public PedDetailsTableDTO getPEDDetailsDTO(DiagnosisPED diagnosisPED){
		PedDetailsTableDTO dest = diagnosiPEDIndMap.map(diagnosisPED);
		return dest;
	}
	
	
	public List<DiagnosisPED> getDiagnosisPEDList(List<PedDetailsTableDTO> pedDetailsList){
		List<DiagnosisPED> mapAsList = diagnosisPedMapper.mapAsList(pedDetailsList, DiagnosisPED.class);
		return mapAsList;
	}
	
	public List<PedDetailsTableDTO> getDiagnosisPEDListDto(List<DiagnosisPED> diagnosisPEDList){
		List<PedDetailsTableDTO> mapAsList = diagnosisPedMapper.mapAsList(diagnosisPEDList, PedDetailsTableDTO.class);
		return mapAsList;
	}
		
	
	/**************************** DIAGNOSIS PED END**********************************/
	
	/*****************************EMPLOYEE MASTER DATA--FOR RRC REQUEST -- STARTS*************/
	public  List<EmployeeMasterDTO> getEmployeeMasterDTO(List<TmpEmployee> tmpEmployee){
		List<EmployeeMasterDTO> mapAsList = 
				employeeDetailsMapper.mapAsList(tmpEmployee, EmployeeMasterDTO.class);
		return mapAsList;
		
	}
	/*****************************EMPLOYEE MASTER DATA--FOR RRC REQUEST -- ENDS *************/
	
	/***************************** RRC REQUEST DATA -- STARTS*************/
	
	public RRCRequest getQuantumReductionDetails(QuantumReductionDetailsDTO quantumReductionDetailsDTO) {
		RRCRequest rrcRequest = rrcRequestDataMapper.mapReverse(quantumReductionDetailsDTO);
		return rrcRequest;
	}
	/***************************** RRC REQUEST DATA -- ENDS*************/
	
	/***************************** RRC DETAILS DATA -- STARTS*************/
	
	public RRCDetails getEmployeeDetails(ExtraEmployeeEffortDTO extraEffortEmployeeDTO) {
		RRCDetails rrcDetails = rrcDetailsDataMapper.mapReverse(extraEffortEmployeeDTO);
		return rrcDetails;
	}
	
	/***************************** RRC REQUEST DATA -- ENDS*************/
	
/***************************** ROD BILL SUMMARY -- STARTS*************/
	
	public RodBillSummary getRodBillSummary(UploadDocumentDTO uploadDocDTO) {
		RodBillSummary rodBillSummary = rodBillSummaryMapper.mapReverse(uploadDocDTO);
		return rodBillSummary;
	}
	
	public List<UploadDocumentDTO> getUploadDocListForBillSummary(List<RodBillSummary> billSummaryList)
	{
		List<UploadDocumentDTO> uploadDocList = rodBillSummaryDetailsMapper.mapAsList(billSummaryList,UploadDocumentDTO.class);
		return uploadDocList;
	}
	
	public List<BenefitAmountDetails> getOtherBenefitsAmountDetails(List<OtherBenefitsTableDto> otherBenefitDTO) {
		List<BenefitAmountDetails> mapAsList = otherBenefitMapper.mapAsList(otherBenefitDTO, BenefitAmountDetails.class);
		return mapAsList;
	}
	/***************************** ROD BILL SUMMARY -- ENDS*************/
	
	public ClaimPayment getClaimPaymentObject (PreauthDTO preauthDTO)
	{
		ClaimPayment objClaimPayment = claimPaymentMapper.mapReverse(preauthDTO);
		return objClaimPayment;
	}
	
	public static ZonalMedicalReviewMapper getInstance(){
        if(myObj == null){
            myObj = new ZonalMedicalReviewMapper();	
            getAllMapValues();
        }
        return myObj;
	 }
	
/**************************** TreatingDoctor **********************************/
	
	public List<TreatingDoctorDetails> gettreatingDoctorList(List<TreatingDoctorDTO> treatingDoctorDTOs) {
		List<TreatingDoctorDetails> mapAsList = treatingDoctorMapper.mapAsList(treatingDoctorDTOs, TreatingDoctorDetails.class);
		return mapAsList;
	}
	
	public TreatingDoctorDTO gettreatingDoctorDTO(TreatingDoctorDetails treatingDoctorDetails) {
		TreatingDoctorDTO dest = treatingDoctorMap.map(treatingDoctorDetails);
		return dest;
	}
	
	public TreatingDoctorDetails gettreatingDoctor(TreatingDoctorDTO treatingDoctorDTO) {
		TreatingDoctorDetails dest = treatingDoctorMap.mapReverse(treatingDoctorDTO);
		return dest;
	}
	
	public List<TreatingDoctorDTO> gettreatingDoctorDTOList(List<TreatingDoctorDetails> treatingDoctorDetails) {
		List<TreatingDoctorDTO> mapAsList = treatingDoctorMapper.mapAsList(treatingDoctorDetails, TreatingDoctorDTO.class);
		return mapAsList;
	}
	
	/**************************** TreatingDoctor END **********************************/
	
/**************************** ImplantDetails **********************************/
	
	public List<ImplantDetails> getimplantDetailsList(List<ImplantDetailsDTO> implantDetailsDTOs) {
		List<ImplantDetails> mapAsList = implantMapper.mapAsList(implantDetailsDTOs, ImplantDetails.class);
		return mapAsList;
	}
	
	public ImplantDetailsDTO getimplantDetailsDTO(ImplantDetails implantDetails) {
		ImplantDetailsDTO dest = implantDetailsMap.map(implantDetails);
		return dest;
	}
	
	public ImplantDetails getimplantDetails(ImplantDetailsDTO implantDetailsDTO) {
		ImplantDetails dest = implantDetailsMap.mapReverse(implantDetailsDTO);
		return dest;
	}
	
	public List<ImplantDetailsDTO> getimplantDetailsDTOList(List<ImplantDetails> implantDetails) {
		List<ImplantDetailsDTO> mapAsList = implantMapper.mapAsList(implantDetails, ImplantDetailsDTO.class);
		return mapAsList;
	}
	
	/**************************** ImplantDetails END **********************************/
}

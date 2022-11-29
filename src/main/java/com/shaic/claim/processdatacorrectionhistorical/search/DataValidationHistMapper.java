package com.shaic.claim.processdatacorrectionhistorical.search;

import java.util.List;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.impl.generator.EclipseJdtCompilerStrategy;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.processdatacorrectionhistorical.bean.ClaimHist;
import com.shaic.claim.processdatacorrectionhistorical.bean.DiagnosisHist;
import com.shaic.claim.processdatacorrectionhistorical.bean.HospitalScoringHist;
import com.shaic.claim.processdatacorrectionhistorical.bean.PPCodingHist;
import com.shaic.claim.processdatacorrectionhistorical.bean.ProcedureHist;
import com.shaic.claim.processdatacorrectionhistorical.bean.ReimbursementHist;
import com.shaic.claim.processdatacorrectionhistorical.bean.SpecialityHist;
import com.shaic.claim.processdatacorrectionhistorical.bean.TreatingDoctorDetailsHist;
import com.shaic.claim.scoring.ppcoding.PPCoding;
import com.shaic.domain.Claim;
import com.shaic.domain.HospitalScoring;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Procedure;
import com.shaic.domain.preauth.Speciality;
import com.shaic.domain.preauth.TreatingDoctorDetails;

public class DataValidationHistMapper {

	static MapperFactory mapperFactory = new DefaultMapperFactory.Builder().compilerStrategy(new EclipseJdtCompilerStrategy()).build();
	static DataValidationHistMapper myObj;

	private static BoundMapperFacade<Speciality, SpecialityHist> specialityIndMap;
	private static BoundMapperFacade<Procedure, ProcedureHist> procedureIndMap;
	private static BoundMapperFacade<PedValidation, DiagnosisHist> pedValidationIndMap;
	private static BoundMapperFacade<TreatingDoctorDetails, TreatingDoctorDetailsHist> treatingDoctorMap;
	private static BoundMapperFacade<Claim, ClaimHist> claimHistMap;
	private static BoundMapperFacade<Reimbursement, ReimbursementHist> reimbursementHistMap;
	private static BoundMapperFacade<HospitalScoring, HospitalScoringHist> hospitalScoringHistMap;
	private static BoundMapperFacade<PPCoding, PPCodingHist> ppcodingHistMap;

	private static MapperFacade specialityMapper;
	private static MapperFacade procedureMapper;
	private static MapperFacade finalProcedureMapper;
	private static MapperFacade finalPedvalidation;
	private static MapperFacade treatingDoctorMapper;
	private static MapperFacade claimHistMapper;
	private static MapperFacade reimbursementMapper;
	private static MapperFacade hospitalScoringHistMapper;

	private static ClassMapBuilder<Speciality, SpecialityHist> specialityMap = null;
	private static ClassMapBuilder<Procedure,ProcedureHist> finalProcedureMap = null;
	private static ClassMapBuilder<PedValidation,DiagnosisHist> finalPedValidationMap = null;
	private static ClassMapBuilder<TreatingDoctorDetails , TreatingDoctorDetailsHist> treatingDoctorDetailMap = null;
	private static ClassMapBuilder<Claim, ClaimHist> claimMap = null;
	private static ClassMapBuilder<Reimbursement, ReimbursementHist> reimbursementMap = null;
	private static ClassMapBuilder<HospitalScoring, HospitalScoringHist> hospitalScoringMap = null;
	private static ClassMapBuilder<PPCoding, PPCodingHist> ppcodingMap = null;

	public static void  getAllMapValues(){

		specialityMap = mapperFactory.classMap(Speciality.class, SpecialityHist.class);
		finalProcedureMap = mapperFactory.classMap(Procedure.class, ProcedureHist.class);
		finalPedValidationMap = mapperFactory.classMap(PedValidation.class, DiagnosisHist.class);
		treatingDoctorDetailMap = mapperFactory.classMap(TreatingDoctorDetails.class, TreatingDoctorDetailsHist.class);
		claimMap = mapperFactory.classMap(Claim.class, ClaimHist.class);
		reimbursementMap = mapperFactory.classMap(Reimbursement.class, ReimbursementHist.class);
		hospitalScoringMap = mapperFactory.classMap(HospitalScoring.class, HospitalScoringHist.class);
		ppcodingMap = mapperFactory.classMap(PPCoding.class, PPCodingHist.class);

		specialityMap.field("key", "dmCode");
		specialityMap.field("activeStatus","activeStatus");
		specialityMap.field("createdBy","createdBy");
		specialityMap.field("createdDate","createdDate");
		specialityMap.field("claim","claim");
		specialityMap.field("officeCode","officeCode");
		specialityMap.field("procedure","procedure");
		specialityMap.field("status","status");
		specialityMap.field("transactionKey","transactionKey");
		specialityMap.field("transactionFlag","transactionFlag");
		specialityMap.field("remarks","remarks");
		specialityMap.field("specialityType","specialityType");
		specialityMap.field("oldspecialityType","oldspecialityType");
		specialityMap.field("splFlag","splFlag");

		finalPedValidationMap.field("key", "key");
		finalPedValidationMap.field("activeStatus","activeStatus");
		finalPedValidationMap.field("approveAmount","approveAmount");
		finalPedValidationMap.field("netApprovedAmount","netApprovedAmount");
		finalPedValidationMap.field("createdBy","createdBy");
		finalPedValidationMap.field("createdDate","createdDate");
		finalPedValidationMap.field("deleteFlag","deleteFlag");
		finalPedValidationMap.field("diagnosisId","diagnosisId");
		finalPedValidationMap.field("icdChpterId","icdChpterId");
		finalPedValidationMap.field("icdBlockId","icdBlockId");
		finalPedValidationMap.field("icdCodeId","icdCodeId");
		finalPedValidationMap.field("intimation.key","intimation");		
		finalPedValidationMap.field("policy.key","policy");
		finalPedValidationMap.field("transactionKey","transactionKey");
		finalPedValidationMap.field("sublimitId","sublimitId");
		finalPedValidationMap.field("sumInsuredRestrictionId","sumInsuredRestrictionId");
		finalPedValidationMap.field("diffAmount","diffAmount");
		finalPedValidationMap.field("subLimitApplicable","subLimitApplicable");
		finalPedValidationMap.field("processFlag","processFlag");
		finalPedValidationMap.field("action","action");
		finalPedValidationMap.field("considerForPayment", "considerForPayment");
		finalPedValidationMap.field("recTypeFlag", "recTypeFlag");
		finalPedValidationMap.field("officeCode", "officeCode");
		finalPedValidationMap.field("policyAging","policyAging");
		finalPedValidationMap.field("approvedRemarks","approvedRemarks");
		finalPedValidationMap.field("diagnosisRemarks","diagnosisRemarks");
		finalPedValidationMap.field("status","status");
		finalPedValidationMap.field("stage","stage");
		finalPedValidationMap.field("amountConsideredAmount", "amountConsideredAmount");
		finalPedValidationMap.field("modifiedBy", "modifiedBy");
		finalPedValidationMap.field("modifiedDate","modifiedDate");
		finalPedValidationMap.field("minimumAmount","minimumAmount");
		finalPedValidationMap.field("copayPercentage", "copayPercentage");
		finalPedValidationMap.field("copayAmount", "copayAmount");
		finalPedValidationMap.field("netAmount", "netAmount");
		finalPedValidationMap.field("sittingsInput","sittingsInput");
		finalPedValidationMap.field("ambulanceCharges","ambulanceCharges");
		finalPedValidationMap.field("ambulanceChargeFlag","ambulanceChargeFlag");
		finalPedValidationMap.field("ambulanceChargeWithAmount","ambulanceChargeWithAmount");
		finalPedValidationMap.field("coPayTypeId","coPayTypeId");
		finalPedValidationMap.field("pedImpactId", "pedImpactId");
		finalPedValidationMap.field("notPayingReason", "notPayingReason");
		finalPedValidationMap.field("sublimitUpdateRemarks","sublimitUpdateRemarks");
		finalPedValidationMap.field("oldIcdCode","oldIcdCode");
		finalPedValidationMap.field("oldDiagnosisId", "oldDiagnosisId");
		finalPedValidationMap.field("icdFlag","icdFlag");
		finalPedValidationMap.field("diagnosisFlag","diagnosisFlag");

		reimbursementMap.field("key","key");
		reimbursementMap.field("docAcknowLedgement.key","docAcknowLedgement");
		reimbursementMap.field("claim.key","claim");
		reimbursementMap.field("rodNumber","rodNumber");
		reimbursementMap.field("bankId","bankId");
		reimbursementMap.field("paymentModeId","paymentModeId");
		reimbursementMap.field("payeeName","payeeName");
		reimbursementMap.field("nameAsPerBankAccount","nameAsPerBankAccount");
		reimbursementMap.field("payeeEmailId","payeeEmailId");
		reimbursementMap.field("panNumber","panNumber");
		reimbursementMap.field("reasonForChange","reasonForChange");
		reimbursementMap.field("legalHeirFirstName","legalHeirFirstName");
		reimbursementMap.field("legalHeirMiddleName","legalHeirMiddleName");
		reimbursementMap.field("legalHeirLastName","legalHeirLastName");
		reimbursementMap.field("payableAt","payableAt");
		reimbursementMap.field("accountNumber","accountNumber");
		reimbursementMap.field("accountType","accountType");
		reimbursementMap.field("dateOfAdmission","dateOfAdmission");
		reimbursementMap.field("medicalCompletedDate","medicalCompletedDate");
		reimbursementMap.field("doaChangeReason","doaChangeReason");
		reimbursementMap.field("roomCategory","roomCategory");
		reimbursementMap.field("numberOfDays","numberOfDays");
		reimbursementMap.field("natureOfTreatment","natureOfTreatment");
		reimbursementMap.field("consultationDate","consultationDate");
		reimbursementMap.field("criticalIllnessFlag","criticalIllnessFlag");
		reimbursementMap.field("criticalIllness","criticalIllness");
		reimbursementMap.field("dateOfDischarge","dateOfDischarge");
		reimbursementMap.field("corporateBufferFlag","corporateBufferFlag");
		reimbursementMap.field("illness","illness");
		reimbursementMap.field("automaticRestoration","automaticRestoration");
		reimbursementMap.field("systemOfMedicine","systemOfMedicine");
		reimbursementMap.field("hopsitaliztionDueto","hopsitaliztionDueto");
		reimbursementMap.field("injuryCauseId","injuryCauseId");
		reimbursementMap.field("injuryDate","injuryDate");
		reimbursementMap.field("medicoLeagalCare","medicoLeagalCare");
		reimbursementMap.field("reportedToPolice","reportedToPolice");
		reimbursementMap.field("attachedPoliceReport","attachedPoliceReport");
		reimbursementMap.field("firNumber","firNumber");
		reimbursementMap.field("firstDiseaseDetectedDate","firstDiseaseDetectedDate");
		reimbursementMap.field("dateOfDelivery","dateOfDelivery");
		reimbursementMap.field("treatmentType","treatmentType");
		reimbursementMap.field("treatmentRemarks","treatmentRemarks");
		reimbursementMap.field("patientStatus","patientStatus");
		reimbursementMap.field("dateOfDeath","dateOfDeath");
		reimbursementMap.field("deathReason","deathReason");
		reimbursementMap.field("rejectionCategoryId","rejectionCategoryId");
		reimbursementMap.field("rejSubCategoryId","rejSubCategoryId");
		reimbursementMap.field("rejectionRemarks","rejectionRemarks");
		reimbursementMap.field("approvalRemarks","approvalRemarks");
		reimbursementMap.field("billingRemarks","billingRemarks");
		reimbursementMap.field("preHospitalizationDays","preHospitalizationDays");
		reimbursementMap.field("postHospitalizationDays","postHospitalizationDays");
		reimbursementMap.field("domicillary","domicillary");
		reimbursementMap.field("billingApprovedAmount","billingApprovedAmount");
		reimbursementMap.field("financialApprovedAmount","financialApprovedAmount");
		reimbursementMap.field("approvedAmount","approvedAmount");
		reimbursementMap.field("processClaimType","processClaimType");
		reimbursementMap.field("faDocumentVerifiedFlag","faDocumentVerifiedFlag");
		reimbursementMap.field("investigationReportReview","investigationReportReview");
		reimbursementMap.field("skipZmrFlag","skipZmrFlag");
		reimbursementMap.field("causeOfLoss","causeOfLoss");
		reimbursementMap.field("natureOfLoss","natureOfLoss");
		reimbursementMap.field("catastrophicLoss","catastrophicLoss");
		reimbursementMap.field("oldRoomCategory","oldRoomCategory");
		reimbursementMap.field("rcFlag","rcFlag");
		reimbursementMap.field("transactionNumber","transactionNumber");
		reimbursementMap.field("claimTypeForPayment","claimTypeForPayment");
		reimbursementMap.field("claimPaymentMode","claimPaymentMode");
		reimbursementMap.field("financialSetteledDate","financialSetteledDate");
		reimbursementMap.field("transactionDate","transactionDate");
		reimbursementMap.field("investigatorCode","investigatorCode");
		reimbursementMap.field("investigatorRemarks","investigatorRemarks");
		reimbursementMap.field("financialApprovalRemarks","financialApprovalRemarks");
		reimbursementMap.field("activeStatus","activeStatus");
		reimbursementMap.field("officeCode","officeCode");
		reimbursementMap.field("medicalRemarks","medicalRemarks");
		reimbursementMap.field("doctorNote","doctorNote");
		reimbursementMap.field("stage","stage");
		reimbursementMap.field("status","status");
		reimbursementMap.field("createdBy","createdBy");
		reimbursementMap.field("createdDate","createdDate");
		reimbursementMap.field("relapseFlag","relapseFlag");
		reimbursementMap.field("relapseRemarks","relapseRemarks");
		reimbursementMap.field("coordinatorFlag","coordinatorFlag");
		reimbursementMap.field("otherInsurerHospAmt","otherInsurerHospAmt");
		reimbursementMap.field("otherInsurerPostHospAmt","otherInsurerPostHospAmt");
		reimbursementMap.field("otherInsurerPreHospAmt","otherInsurerPreHospAmt");
		reimbursementMap.field("verifiedPolicyScheduleFlag","verifiedPolicyScheduleFlag");
		reimbursementMap.field("billingCompletedDate","billingCompletedDate");
		reimbursementMap.field("financialCompletedDate","financialCompletedDate");
		reimbursementMap.field("modifiedBy","modifiedBy");
		reimbursementMap.field("modifiedDate","modifiedDate");
		reimbursementMap.field("currentProvisionAmt","currentProvisionAmt");
		reimbursementMap.field("prorataPercentage","prorataPercentage");
		reimbursementMap.field("prorataDeductionFlag","prorataDeductionFlag");
		reimbursementMap.field("packageAvailableFlag","packageAvailableFlag");
		reimbursementMap.field("cancellationReasonId","cancellationReasonId");
		reimbursementMap.field("cancellationRemarks","cancellationRemarks");
		reimbursementMap.field("insuredKey","insuredKey");
		reimbursementMap.field("hospitalId","hospitalId");
		reimbursementMap.field("sectionCategory","sectionCategory");
		reimbursementMap.field("typeOfDelivery","typeOfDelivery");
		reimbursementMap.field("treatmentStartDate","treatmentStartDate");
		reimbursementMap.field("treatmentEndDate","treatmentEndDate");
		reimbursementMap.field("domicillaryNumberOfDays","domicillaryNumberOfDays");
		reimbursementMap.field("amtConsCopayPercentage","amtConsCopayPercentage");
		reimbursementMap.field("balanceSICopayPercentage","balanceSICopayPercentage");
		reimbursementMap.field("amtConsAftCopayAmount","amtConsAftCopayAmount");
		reimbursementMap.field("balanceSIAftCopayAmt","balanceSIAftCopayAmt");
		reimbursementMap.field("billEntryDate","billEntryDate");
		reimbursementMap.field("zonalDate","zonalDate");
		reimbursementMap.field("otherBenefitApprovedAmt","otherBenefitApprovedAmt");
		reimbursementMap.field("otherInsurerApplicableFlag","otherInsurerApplicableFlag");
		reimbursementMap.field("billEntryRemarks","billEntryRemarks");
		reimbursementMap.field("billEntryAmt","billEntryAmt");
		reimbursementMap.field("copayRemarks","copayRemarks");
		reimbursementMap.field("premiumAmt","premiumAmt");
		reimbursementMap.field("corporateUtilizedAmt","corporateUtilizedAmt");
		reimbursementMap.field("accidentCauseId","accidentCauseId");
		reimbursementMap.field("pedFlag","pedFlag");
		reimbursementMap.field("pedDisablitiyDetails","pedDisablitiyDetails");
		reimbursementMap.field("benefitsId","benefitsId");
		reimbursementMap.field("addOnCoversApprovedAmount","addOnCoversApprovedAmount");
		reimbursementMap.field("optionalApprovedAmount","optionalApprovedAmount");
		reimbursementMap.field("claimApprovalAmount","claimApprovalAmount");
		reimbursementMap.field("claimApprovalDate","claimApprovalDate");
		reimbursementMap.field("benApprovedAmt","benApprovedAmt");
		reimbursementMap.field("nomineeName","nomineeName");
		reimbursementMap.field("nomineeAddr","nomineeAddr");
		reimbursementMap.field("claimApprovalRemarks","claimApprovalRemarks");
		reimbursementMap.field("workPlace","workPlace");
		reimbursementMap.field("unNamedKey","unNamedKey");
		reimbursementMap.field("payModeChangeReason","payModeChangeReason");
		reimbursementMap.field("fvrNotRequiredRemarks","fvrNotRequiredRemarks");
		reimbursementMap.field("updatePaymentDtlsFlag","updatePaymentDtlsFlag");
		reimbursementMap.field("paymentCpuCode","paymentCpuCode");
		reimbursementMap.field("faInternalRemarks","faInternalRemarks");
		reimbursementMap.field("billingInternalRemarks","billingInternalRemarks");
		reimbursementMap.field("parentKey","parentKey");
		reimbursementMap.field("version","version");
		reimbursementMap.field("reconsiderationRequest","reconsiderationRequest");
		reimbursementMap.field("fvrAlertFlag","fvrAlertFlag");
		reimbursementMap.field("fvrNotRequiredOthersRemarks","fvrNotRequiredOthersRemarks");
		reimbursementMap.field("scoringFlag","scoringFlag");
		reimbursementMap.field("amountClaimedFromHospital","amountClaimedFromHospital");
		reimbursementMap.field("cvcAuditFlag","cvcAuditFlag");
		reimbursementMap.field("cvcLockFlag","cvcLockFlag");
		reimbursementMap.field("nonAllopathicDiffAmt","nonAllopathicDiffAmt");
		reimbursementMap.field("nonAllopathicApprAmt","nonAllopathicApprAmt");
		reimbursementMap.field("prodBenefitAmount","prodBenefitAmount");
		reimbursementMap.field("accountPreference","accountPreference");
		reimbursementMap.field("prodDiagnosisID","prodDiagnosisID");
		reimbursementMap.field("prodBenefitDueToID","prodBenefitDueToID");
		reimbursementMap.field("prodBenefitFlag","prodBenefitFlag");
		reimbursementMap.field("hospitalDiscountFlag","hospitalDiscountFlag");
		reimbursementMap.field("phcDayCareFlag","phcDayCareFlag");
		reimbursementMap.field("phcDayCareID","phcDayCareID");

		treatingDoctorDetailMap.field("key","dmCode");
		treatingDoctorDetailMap.field("claimKey","claimKey");
		treatingDoctorDetailMap.field("transactionKey","transactionKey");
		treatingDoctorDetailMap.field("doctorName","doctorName");
		treatingDoctorDetailMap.field("doctorQualification","doctorQualification");
		treatingDoctorDetailMap.field("activeStatus","activeStatus");
		treatingDoctorDetailMap.field("createdDate","createdDate");
		treatingDoctorDetailMap.field("createdBy","createdBy");
		treatingDoctorDetailMap.field("modifiedDate","modifiedDate");
		treatingDoctorDetailMap.field("modifiedBy","modifiedBy");
		treatingDoctorDetailMap.field("oldDoctorName","oldDoctorName");
		treatingDoctorDetailMap.field("oldQualification","oldQualification");
		treatingDoctorDetailMap.field("dcDoctorFlag","dcDoctorFlag");


		finalProcedureMap.field("key","key");
		finalProcedureMap.field("activeStatus","activeStatus");
		finalProcedureMap.field("approvedAmount","approvedAmount");
		finalProcedureMap.field("netApprovedAmount","netApprovedAmount");
		finalProcedureMap.field("diffAmount","diffAmount");
		finalProcedureMap.field("dayCareProcedure","dayCareProcedure");
		finalProcedureMap.field("considerForDayCare","considerForDayCare");
		finalProcedureMap.field("considerForPayment","considerForPayment");
		finalProcedureMap.field("createdBy","createdBy");
		finalProcedureMap.field("createdDate","createdDate");
		finalProcedureMap.field("exculsionDetails","exculsionDetails");
		finalProcedureMap.field("stage","stage");
		finalProcedureMap.field("transactionKey","transactionKey");
		finalProcedureMap.field("modifiedBy","modifiedBy");
		finalProcedureMap.field("modifiedDate","modifiedDate");
		finalProcedureMap.field("deleteFlag","deleteFlag");
		finalProcedureMap.field("officeCode","officeCode");
		finalProcedureMap.field("packageRate","packageRate");
		finalProcedureMap.field("policyAgeing","policyAgeing");
		finalProcedureMap.field("procedureID","procedureID");
		finalProcedureMap.field("procedureCode","procedureCode");
		finalProcedureMap.field("procedureName","procedureName");
		finalProcedureMap.field("procedureStatus","procedureStatus");
		finalProcedureMap.field("newProcedureFlag","newProcedureFlag");
		finalProcedureMap.field("processFlag","processFlag");
		finalProcedureMap.field("action","action");
		finalProcedureMap.field("recTypeFlag","recTypeFlag");
		finalProcedureMap.field("status","status");
		finalProcedureMap.field("subLimitApplicable","subLimitApplicable");
		finalProcedureMap.field("sublimitNameId","sublimitNameId");
		finalProcedureMap.field("validationRemarks","validationRemarks");
		finalProcedureMap.field("procedureRemarks","procedureRemarks");
		finalProcedureMap.field("approvedRemarks","approvedRemarks");
		finalProcedureMap.field("amountConsideredAmount","amountConsideredAmount");
		finalProcedureMap.field("minimumAmount","minimumAmount");
		finalProcedureMap.field("copayPercentage","copayPercentage");
		finalProcedureMap.field("copayAmount","copayAmount");
		finalProcedureMap.field("netAmount","netAmount");
		finalProcedureMap.field("sittingsInput","sittingsInput");
		finalProcedureMap.field("ambulanceChargeFlag","ambulanceChargeFlag");
		finalProcedureMap.field("ambulanceCharges","ambulanceCharges");
		finalProcedureMap.field("ambulanceChargeWithAmount","ambulanceChargeWithAmount");
		finalProcedureMap.field("coPayTypeId","coPayTypeId");
		finalProcedureMap.field("oldprocedureID","oldprocedureID");
		finalProcedureMap.field("procedureFlag","procedureFlag");

		hospitalScoringMap.field("key","key");
		hospitalScoringMap.field("intimationKey","intimationKey");
		hospitalScoringMap.field("claimKey","claimKey");
		hospitalScoringMap.field("hospitalCode","hospitalCode");
		hospitalScoringMap.field("hospitalKey","hospitalKey");
		hospitalScoringMap.field("subCategoryKey","subCategoryKey");
		hospitalScoringMap.field("gradeScore","gradeScore");
		hospitalScoringMap.field("createdBy","createdBy");
		hospitalScoringMap.field("createdDate","createdDate");
		hospitalScoringMap.field("modifiedBy","modifiedBy");
		hospitalScoringMap.field("modifiedDate","modifiedDate");
		hospitalScoringMap.field("scoringVersion","scoringVersion");
		hospitalScoringMap.field("deleteFlag","deleteFlag");
		hospitalScoringMap.field("oldGradeScore","oldGradeScore");

		claimMap.field("key","key");
		claimMap.field("activeStatus","activeStatus");
		claimMap.field("claimId","claimId");
		claimMap.field("claimLink","claimLink");
		claimMap.field("claimType","claimType");
		claimMap.field("claimedAmount","claimedAmount");
		claimMap.field("claimedHomeAmount","claimedHomeAmount");
		claimMap.field("currentProvisionAmount","currentProvisionAmount");
		claimMap.field("claimedamountCurrencyId","claimedamountCurrencyId");
		claimMap.field("claimedhomeamountCurrencyId","claimedhomeamountCurrencyId");
		claimMap.field("conversionFlag","conversionFlag");
		claimMap.field("conversionLetter","conversionLetter");
		claimMap.field("conversionReason","conversionReason");
		claimMap.field("createdBy","createdBy");
		claimMap.field("latestPreauthKey","latestPreauthKey");
		claimMap.field("createdDate","createdDate");
		claimMap.field("intimation","intimation");
		claimMap.field("isVipCustomer","isVipCustomer");
		claimMap.field("modifiedBy","modifiedBy");
		claimMap.field("modifiedDate","modifiedDate");
		claimMap.field("firstReminderDate","firstReminderDate");
		claimMap.field("secondReminderDate","secondReminderDate");
		claimMap.field("thirdReminderDate","thirdReminderDate");
		claimMap.field("reminderCount","reminderCount");
		claimMap.field("conversionDate","conversionDate");
		claimMap.field("officeCode","officeCode");
		claimMap.field("proamountCurrencyId","proamountCurrencyId");
		claimMap.field("prohomeamountCurrencyId","prohomeamountCurrencyId");
		claimMap.field("provisionAmount","provisionAmount");
		claimMap.field("provisionHomeAmount","provisionHomeAmount");
		claimMap.field("rejectionLetterflag","rejectionLetterflag");
		claimMap.field("status","status");
		claimMap.field("stage","stage");
		claimMap.field("currencyId","currencyId");
		claimMap.field("rejectionCategoryId","rejectionCategoryId");
		claimMap.field("registrationRemarks","registrationRemarks");
		claimMap.field("suggestedRejectionRemarks","suggestedRejectionRemarks");
		claimMap.field("rejectionRemarks","rejectionRemarks");
		claimMap.field("waiverRemarks","waiverRemarks");
		claimMap.field("dataOfAdmission","dataOfAdmission");
		claimMap.field("dataOfDischarge","dataOfDischarge");
		claimMap.field("claimRegisteredDate","claimRegisteredDate");
		claimMap.field("closeDate","closeDate");
		claimMap.field("reopenDate","reopenDate");
		claimMap.field("legalFlag","legalFlag");
		claimMap.field("autoRestroationFlag","autoRestroationFlag");
		claimMap.field("normalClaimFlag","normalClaimFlag");
		claimMap.field("intimationCreatedDate","intimationCreatedDate");
		claimMap.field("documentReceivedDate","documentReceivedDate");
		claimMap.field("revisedProvisionAmount","revisedProvisionAmount");
		claimMap.field("medicalRemarks","medicalRemarks");
		claimMap.field("doctorNote","doctorNote");
		claimMap.field("claimSectionCode","claimSectionCode");
		claimMap.field("claimCoverCode","claimCoverCode");
		claimMap.field("claimSubCoverCode","claimSubCoverCode");
		claimMap.field("gmcCorpBufferLmt","gmcCorpBufferLmt");
		claimMap.field("gmcCorpBufferFlag","gmcCorpBufferFlag");
		claimMap.field("incidenceFlag","incidenceFlag");
		claimMap.field("incidenceDate","incidenceDate");
		claimMap.field("lobId","lobId");
		claimMap.field("processClaimType","processClaimType");
		claimMap.field("hospReqFlag","hospReqFlag");
		claimMap.field("injuryRemarks","injuryRemarks");
		claimMap.field("accidentDate","accidentDate");
		claimMap.field("deathDate","deathDate");
		claimMap.field("disablementDate","disablementDate");
		claimMap.field("paHospExpenseAmt","paHospExpenseAmt");
		claimMap.field("gpaParentName","gpaParentName");
		claimMap.field("gpaParentDOB","gpaParentDOB");
		claimMap.field("gpaParentAge","gpaParentAge");
		claimMap.field("gpaRiskName","gpaRiskName");
		claimMap.field("gpaRiskDOB","gpaRiskDOB");
		claimMap.field("gpaRiskAge","gpaRiskAge");
		claimMap.field("gpaCategory","gpaCategory");
		claimMap.field("gpaSection","gpaSection");
		claimMap.field("originalCpuCode","originalCpuCode");
		claimMap.field("crcFlag","crcFlag");
		claimMap.field("crcFlaggedReason","crcFlaggedReason");
		claimMap.field("crcFlaggedRemark","crcFlaggedRemark");
		claimMap.field("crcFlaggedDate","crcFlaggedDate");
		claimMap.field("internalNotes","internalNotes");
		claimMap.field("crcPriorityCode","crcPriorityCode");
		claimMap.field("crcPriorityDesc","crcPriorityDesc");
		claimMap.field("ghiAllowUser","ghiAllowUser");
		claimMap.field("ghiAllowFlag","ghiAllowFlag");
		claimMap.field("insuredKey","insuredKey");
		claimMap.field("sfxCpuCode","sfxCpuCode");
		claimMap.field("sfxProcessingCpuCode","sfxProcessingCpuCode");
		claimMap.field("coadingFlag","coadingFlag");
		claimMap.field("coadingUser","coadingUser");
		claimMap.field("coadingDate","coadingDate");
		claimMap.field("coadingRemark","coadingRemark");
		claimMap.field("dcCoadingFlag","dcCoadingFlag");
		claimMap.field("ppFlag","ppFlag");
		claimMap.field("ppProtected","ppProtected");
		claimMap.field("dcppFlag","dcppFlag");
		claimMap.field("oldppProtected","oldppProtected");
		
		ppcodingMap.field("key","oldppCodingKey");
		ppcodingMap.field("intimationKey","intimationKey");
		ppcodingMap.field("claimKey","claimKey");
		ppcodingMap.field("ppCode","ppCode");
		ppcodingMap.field("hospitalCode","hospitalCode");
		ppcodingMap.field("hospitalKey","hospitalKey");
		ppcodingMap.field("hospitalType","hospitalType");
		ppcodingMap.field("ppScore","ppScore");
		ppcodingMap.field("ppStage","ppStage");
		ppcodingMap.field("createdBy","createdBy");
		ppcodingMap.field("createdDate","createdDate");
		ppcodingMap.field("modifiedBy","modifiedBy");
		ppcodingMap.field("modifiedDate","modifiedDate");
		ppcodingMap.field("ppVersion","ppVersion");
		ppcodingMap.field("deleteFlag","deleteFlag");
		ppcodingMap.field("oldppScore","oldppScore");

		specialityMap.register();
		finalProcedureMap.register();
		finalPedValidationMap.register();
		treatingDoctorDetailMap.register();
		claimMap.register();
		reimbursementMap.register();
		hospitalScoringMap.register();
		ppcodingMap.register();

		specialityIndMap = mapperFactory.getMapperFacade(Speciality.class, SpecialityHist.class);
		pedValidationIndMap = mapperFactory.getMapperFacade(PedValidation.class, DiagnosisHist.class);
		treatingDoctorMap = mapperFactory.getMapperFacade(TreatingDoctorDetails.class, TreatingDoctorDetailsHist.class);
		procedureIndMap = mapperFactory.getMapperFacade(Procedure.class, ProcedureHist.class);
		claimHistMap = mapperFactory.getMapperFacade(Claim.class, ClaimHist.class);
		reimbursementHistMap = mapperFactory.getMapperFacade(Reimbursement.class, ReimbursementHist.class);
		hospitalScoringHistMap = mapperFactory.getMapperFacade(HospitalScoring.class, HospitalScoringHist.class);
		ppcodingHistMap = mapperFactory.getMapperFacade(PPCoding.class, PPCodingHist.class);


		specialityMapper = mapperFactory.getMapperFacade();
		finalPedvalidation = mapperFactory.getMapperFacade();
		procedureMapper = mapperFactory.getMapperFacade();
		finalProcedureMapper=mapperFactory.getMapperFacade();
		treatingDoctorMapper = mapperFactory.getMapperFacade();
		claimHistMapper=mapperFactory.getMapperFacade();
		reimbursementMapper = mapperFactory.getMapperFacade();
		hospitalScoringHistMapper = mapperFactory.getMapperFacade();

	}

	public static DataValidationHistMapper getInstance(){
		if(myObj == null){
			myObj = new DataValidationHistMapper();
			getAllMapValues();
		}
		return myObj;
	}
	/**************************** SPECIALITY **********************************/

	public SpecialityHist getSpecialityHist(Speciality speciality) {
		SpecialityHist dest = specialityIndMap.map(speciality);
		return dest;
	}

	public Speciality getSpeciality(SpecialityHist specialityDTO) {
		Speciality dest = specialityIndMap.mapReverse(specialityDTO);
		return dest;
	}

	public List<SpecialityHist> getSpecialityDTOList(List<Speciality> specialityList) {
		List<SpecialityHist> mapAsList = specialityMapper.mapAsList(specialityList, SpecialityHist.class);
		return mapAsList;
	}

	/**************************** SPECIALITY END **********************************/

	/**************************** EXISITING PROCEDURE **********************************/

	public Procedure getProcedure(ProcedureHist procedureHist) {
		Procedure dest = procedureIndMap.mapReverse(procedureHist);
		return dest;
	}

	public ProcedureHist getProcedureHist(Procedure procedure) {
		ProcedureHist dest = procedureIndMap.map(procedure);
		return dest;
	}

	/**************************** PED VALIDATION**********************************/

	public PedValidation getPedValidation(DiagnosisHist diagnosisHist){
		PedValidation dest = pedValidationIndMap.mapReverse(diagnosisHist);
		return dest;
	}

	public DiagnosisHist getDiagnosisHist(PedValidation pedValidation){
		DiagnosisHist dest = pedValidationIndMap.map(pedValidation);
		return dest;
	}


	/**************************** PED VALIDATION END**********************************/	

	/**************************** TreatingDoctor **********************************/

	public TreatingDoctorDetailsHist getTreatingDoctorDetailsHist(TreatingDoctorDetails treatingDoctorDetails) {
		TreatingDoctorDetailsHist dest = treatingDoctorMap.map(treatingDoctorDetails);
		return dest;
	}

	public TreatingDoctorDetails gettreatingDoctor(TreatingDoctorDetailsHist doctorDetailsHist) {
		TreatingDoctorDetails dest = treatingDoctorMap.mapReverse(doctorDetailsHist);
		return dest;
	}

	/**************************** TreatingDoctor END **********************************/

	/**************************** claimHist **********************************/

	public ClaimHist getClaim(Claim claim) {
		ClaimHist dest = claimHistMap.map(claim);
		return dest;
	}

	public Claim getClaimHist(ClaimHist claimHist) {
		Claim dest = claimHistMap.mapReverse(claimHist);
		return dest;
	}

	/**************************** claimHist END **********************************/

	/**************************** ReimbursementHist **********************************/

	public ReimbursementHist getReimbursementHist(Reimbursement rod) {
		ReimbursementHist dest = reimbursementHistMap.map(rod);
		return dest;
	}

	public Reimbursement getReimbursement(ReimbursementHist reimbursementHist) {
		Reimbursement dest = reimbursementHistMap.mapReverse(reimbursementHist);
		return dest;
	}

	/**************************** ReimbursementHist END **********************************/

	/**************************** HospitalScoring **********************************/

	public HospitalScoringHist getHospitalScoringHist(HospitalScoring hospitalScoring) {
		HospitalScoringHist dest = hospitalScoringHistMap.map(hospitalScoring);
		return dest;
	}

	public HospitalScoring getReimbursement(HospitalScoringHist hospitalScoringHist) {
		HospitalScoring dest = hospitalScoringHistMap.mapReverse(hospitalScoringHist);
		return dest;
	}

	/**************************** HospitalScoring END **********************************/
	
	/**************************** PPCodingHist **********************************/

	public PPCodingHist getPpCodingHist(PPCoding ppCoding) {
		PPCodingHist dest = ppcodingHistMap.map(ppCoding);
		return dest;
	}

	public PPCoding getPpCoding(PPCodingHist ppCodingHist) {
		PPCoding dest = ppcodingHistMap.mapReverse(ppCodingHist);
		return dest;
	}

	/**************************** PPCodingHist END **********************************/

}

package com.shaic.claim.premedical.mapper;

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
import com.shaic.claim.premedical.dto.NoOfDaysCell;
import com.shaic.domain.preauth.ClaimAmountDetails;
import com.shaic.domain.preauth.Coordinator;
import com.shaic.domain.preauth.DiagnosisPED;
import com.shaic.domain.preauth.ImplantDetails;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.Procedure;
import com.shaic.domain.preauth.Speciality;
import com.shaic.domain.preauth.TreatingDoctorDetails;
import com.shaic.domain.preauth.UpdateOtherClaimDetails;


public class PreMedicalMapper {
	
	static MapperFactory mapperFactory = new DefaultMapperFactory.Builder().compilerStrategy(new EclipseJdtCompilerStrategy()).build();
	
	static PreMedicalMapper myObj;
	
	private static BoundMapperFacade<Preauth, PreauthDTO> preauthMapper;
	private static BoundMapperFacade<Speciality, SpecialityDTO> specialityIndMap;
	private static BoundMapperFacade<ProcedureDTO, ProcedureTableDTO> oldProcedureIndMap;
	private static BoundMapperFacade<ProcedureDTO, NewProcedureTableDTO> newProcedureIndMap;
	private static BoundMapperFacade<Procedure, ProcedureDTO> procedureIndMap;
	private static BoundMapperFacade<PedValidation, DiagnosisDetailsTableDTO> pedValidationIndMap;
	private static BoundMapperFacade<Coordinator, CoordinatorDTO> coordinatorMapper;
	private static BoundMapperFacade<ClaimAmountDetails, NoOfDaysCell> claimAmountIndMap;
	private static BoundMapperFacade<DiagnosisPED, PedDetailsTableDTO> diagnosiPEDIndMap;
	private static BoundMapperFacade<TreatingDoctorDetails, TreatingDoctorDTO> treatingDoctorMap;
	private static BoundMapperFacade<ImplantDetails, ImplantDetailsDTO> implantDetailsMap;
	
	private static MapperFacade specialityMapper;
	private static MapperFacade newProcedureMapper;
	private static MapperFacade procedureMapper;
	private static MapperFacade procedureListMapper;
	//private static MapperFacade pedvalidationTableMapper;
	private static MapperFacade claimedAmountTableMapper;
	private static MapperFacade oldProcedureMapper;
	private static MapperFacade newProcedureListMapper;
	private static MapperFacade finalProcedureMapper;
	private static MapperFacade finalPedvalidation;
	private static MapperFacade diagnosisPedMapper;
	private static MapperFacade otherInsurerMapper;
	private static MapperFacade treatingDoctorMapper;
	private static MapperFacade implantMapper;
	
	/*private static ClassMapBuilder<Preauth, PreauthDTO> preauthMap = mapperFactory.classMap(Preauth.class,PreauthDTO.class);
	private static ClassMapBuilder<Coordinator, CoordinatorDTO> coordinatorMap = mapperFactory.classMap(Coordinator.class,CoordinatorDTO.class);
	private static ClassMapBuilder<Speciality, SpecialityDTO> specialityMap = mapperFactory.classMap(Speciality.class, SpecialityDTO.class);
	private static ClassMapBuilder<DiagnosisDetailsTableDTO,DiagnosisDetailsTableDTO> pedvalidationClassMap = mapperFactory.classMap(DiagnosisDetailsTableDTO.class,DiagnosisDetailsTableDTO.class);
	private static ClassMapBuilder<NoOfDaysCell,ClaimAmountDetails> claimAmountClassMap = mapperFactory.classMap(NoOfDaysCell.class,ClaimAmountDetails.class);
	private static ClassMapBuilder<Procedure,ProcedureDTO> finalProcedureMap=mapperFactory.classMap(Procedure.class, ProcedureDTO.class);
	private static ClassMapBuilder<PedValidation,DiagnosisDetailsTableDTO> finalPedValidationMap = mapperFactory.classMap(PedValidation.class, DiagnosisDetailsTableDTO.class);
<<<<<<< HEAD
	private static ClassMapBuilder<DiagnosisPED, PedDetailsTableDTO> diagnosisPEDMap = mapperFactory.classMap(DiagnosisPED.class, PedDetailsTableDTO.class);
	private static ClassMapBuilder<UpdateOtherClaimDetails , UpdateOtherClaimDetailDTO> otherInsurerMap = mapperFactory.classMap(UpdateOtherClaimDetails.class, UpdateOtherClaimDetailDTO.class);
=======
	private static ClassMapBuilder<DiagnosisPED, PedDetailsTableDTO> diagnosisPEDMap = mapperFactory.classMap(DiagnosisPED.class, PedDetailsTableDTO.class);*/
	
	private static ClassMapBuilder<Preauth, PreauthDTO> preauthMap = null;
	private static ClassMapBuilder<Coordinator, CoordinatorDTO> coordinatorMap = null;
	private static ClassMapBuilder<Speciality, SpecialityDTO> specialityMap = null;
	private static ClassMapBuilder<DiagnosisDetailsTableDTO,DiagnosisDetailsTableDTO> pedvalidationClassMap = null;
	private static ClassMapBuilder<NoOfDaysCell,ClaimAmountDetails> claimAmountClassMap = null;
	private static ClassMapBuilder<Procedure,ProcedureDTO> finalProcedureMap = null;
	private static ClassMapBuilder<PedValidation,DiagnosisDetailsTableDTO> finalPedValidationMap = null;
	private static ClassMapBuilder<DiagnosisPED, PedDetailsTableDTO> diagnosisPEDMap = null;
	private static ClassMapBuilder<UpdateOtherClaimDetails , UpdateOtherClaimDetailDTO> otherInsurerMap = null;
	private static ClassMapBuilder<TreatingDoctorDetails , TreatingDoctorDTO> treatingDoctorDetailMap = null;
	private static ClassMapBuilder<ImplantDetails, ImplantDetailsDTO> implantMap = null;
	
    public static void getAllMapValues()  {
    	
    	preauthMap = mapperFactory.classMap(Preauth.class,PreauthDTO.class);
    	coordinatorMap = mapperFactory.classMap(Coordinator.class,CoordinatorDTO.class);
    	specialityMap = mapperFactory.classMap(Speciality.class, SpecialityDTO.class);
    	pedvalidationClassMap = mapperFactory.classMap(DiagnosisDetailsTableDTO.class,DiagnosisDetailsTableDTO.class);
    	claimAmountClassMap = mapperFactory.classMap(NoOfDaysCell.class,ClaimAmountDetails.class);
    	finalProcedureMap=mapperFactory.classMap(Procedure.class, ProcedureDTO.class);
    	finalPedValidationMap = mapperFactory.classMap(PedValidation.class, DiagnosisDetailsTableDTO.class);
    	diagnosisPEDMap = mapperFactory.classMap(DiagnosisPED.class, PedDetailsTableDTO.class);
    	otherInsurerMap = mapperFactory.classMap(UpdateOtherClaimDetails.class, UpdateOtherClaimDetailDTO.class);
    	treatingDoctorDetailMap = mapperFactory.classMap(TreatingDoctorDetails.class, TreatingDoctorDTO.class);
    	implantMap = mapperFactory.classMap(ImplantDetails.class, ImplantDetailsDTO.class);
    	
		preauthMap.field("key","key");
		preauthMap.field("policy.key","policyKey");
		preauthMap.field("intimation.key","intimationKey");
		preauthMap.field("claim.key","claimKey");
		preauthMap.field("unNamedKey","unNamedKey");
		preauthMap.field("dataOfAdmission","preauthDataExtractionDetails.admissionDate");
		preauthMap.field("otherBenefitApprovedAmt","preauthDataExtractionDetails.totalOtherBenefitsApprovedAmt");
		preauthMap.field("otherBenefitFlag","preauthDataExtractionDetails.otherBenfitFlag");
		preauthMap.field("status.processValue", "statusValue");
		preauthMap.field("status.portalStatus", "portalStatusVal");
		preauthMap.field("status.websiteStatus", "websiteStatusVal");
		preauthMap.field("status.key", "statusKey");
		preauthMap.field("stage.key", "stageKey");
		preauthMap.field("doctorNote", "doctorNote");
//		preauthMap.field("medicalRemarks", "medicalRemarks");
		preauthMap.field("withdrawReason.key", "withdrawReason.id");
		preauthMap.field("withdrawReason.value", "withdrawReason.value");
		
		//preauthMap.field("preauthId", "referenceType");
		//preauthMap.field("activeStatus","");
		
		//preauthMap.field("doaChangeReason","preauthDataExtractionDetails.reasonForAdmission");
		/**
		 * Reason for admission will be available in intimation table. 
		 * Hence to set it to DTO, the above code is commented and below code is added.
		 * change in DOA will be available in preauth, which is mapped to a separated feild.
		 * */
		preauthMap.field("intimation.admissionReason","reasonForAdmission");
		preauthMap.field("dateOfDeath","preauthDataExtractionDetails.deathDate");
		preauthMap.field("deathReason","preauthDataExtractionDetails.reasonForDeath");
		preauthMap.field("terminatorCover","preauthDataExtractionDetails.terminateCoverFlag");
		preauthMap.field("preauthId","preauthDataExtractionDetails.referenceNo");
		preauthMap.field("numberOfDays","preauthDataExtractionDetails.noOfDays");
		preauthMap.field("createdDate", "createDate");
		preauthMap.field("createdBy","createdBy");
		preauthMap.field("dateOfDischarge","preauthDataExtractionDetails.dischargeDate");
		preauthMap.field("natureOfTreatment.key","preauthDataExtractionDetails.natureOfTreatment.id");
		preauthMap.field("natureOfTreatment.value","preauthDataExtractionDetails.natureOfTreatment.value");
		preauthMap.field("consultationDate", "preauthDataExtractionDetails.firstConsultantDate");
		preauthMap.field("criticalIllnessFlag","preauthDataExtractionDetails.criticalIllnessFlag");
		preauthMap.field("corporateBufferFlag","preauthDataExtractionDetails.corporateBufferFlag");
		preauthMap.field("criticalIllness.key","preauthDataExtractionDetails.specifyIllness.id");
		preauthMap.field("criticalIllness.value","preauthDataExtractionDetails.specifyIllness.value");
		preauthMap.field("roomCategory.key","preauthDataExtractionDetails.roomCategory.id");
		//CR2019169
		preauthMap.field("catastrophicLoss","preauthDataExtractionDetails.catastrophicLoss.id");
		preauthMap.field("natureOfLoss","preauthDataExtractionDetails.natureOfLoss.id");
		preauthMap.field("causeOfLoss","preauthDataExtractionDetails.causeOfLoss.id");
		
		preauthMap.field("roomCategory.value","preauthDataExtractionDetails.roomCategory.value");
		preauthMap.field("ventilatorSupport","preauthDataExtractionDetails.ventilatorSupportFlag");
		preauthMap.field("treatmentType.key","preauthDataExtractionDetails.treatmentType.id");
		preauthMap.field("treatmentType.value","preauthDataExtractionDetails.treatmentType.value");
		preauthMap.field("treatmentRemarks","preauthDataExtractionDetails.treatmentRemarks");
		preauthMap.field("autoRestoration","preauthDataExtractionDetails.autoRestoration");
		preauthMap.field("illness.key","preauthDataExtractionDetails.illness.id");
		preauthMap.field("illness.value","preauthDataExtractionDetails.illness.value");
		preauthMap.field("patientStatus.key","preauthDataExtractionDetails.patientStatus.id");
		preauthMap.field("patientStatus.value","preauthDataExtractionDetails.patientStatus.value");
		preauthMap.field("relapseFlag","preauthPreviousClaimsDetails.relapseFlag");
		preauthMap.field("relapseRemarks","preauthPreviousClaimsDetails.relapseRemarks");
		preauthMap.field("coordinatorFlag","coordinatorDetails.refertoCoordinatorFlag");
		preauthMap.field("medicalCategoryId","preauthMedicalProcessingDetails.category.id");
		preauthMap.field("medicalRemarks", "preauthMedicalProcessingDetails.medicalRemarks");
		preauthMap.field("reviewRemarks","preauthMedicalDecisionDetails.investigationReviewRemarks");
		preauthMap.field("investigatorName", "preauthMedicalDecisionDetails.investigatorCode");
		preauthMap.field("doctorNote","preauthMedicalDecisionDetails.doctorNote");
		
		//preauthMap.field("approvedAmount","preauthDataExtractionDetails.approvedAmount");
		preauthMap.field("totalApprovalAmount","preauthDataExtractionDetails.totalApprAmt");
		
		preauthMap.field("enhancementType", "preauthDataExtractionDetails.interimOrFinalEnhancementFlag");
		preauthMap.field("doaChangeReason", "preauthDataExtractionDetails.changeInReasonDOA");
		preauthMap.field("changePreauth", "preauthMedicalProcessingDetails.changeInPreauthFlag");
		preauthMap.field("createdBy", "createdBy");
		
		preauthMap.field("hopsitaliztionDueto.key","preauthDataExtractionDetails.hospitalisationDueTo.id");
		preauthMap.field("hopsitaliztionDueto.value","preauthDataExtractionDetails.hospitalisationDueTo.value");
		preauthMap.field("injuryCauseId","preauthDataExtractionDetails.causeOfInjury.id");
		preauthMap.field("injuryDate","preauthDataExtractionDetails.injuryDate");
		preauthMap.field("medicoLeagalCare","preauthDataExtractionDetails.medicalLegalCaseFlag");
		preauthMap.field("reportedToPolice","preauthDataExtractionDetails.reportedToPoliceFlag");
		preauthMap.field("firstDiseaseDetectedDate","preauthDataExtractionDetails.diseaseFirstDetectedDate");
		preauthMap.field("dateOfDelivery","preauthDataExtractionDetails.deliveryDate");
		preauthMap.field("sectionCategory","preauthDataExtractionDetails.section.id");
		preauthMap.field("typeOfDelivery","preauthDataExtractionDetails.typeOfDelivery.id");
		preauthMap.field("firNumber","preauthDataExtractionDetails.firNumber");
		preauthMap.field("attachedPoliceReport","preauthDataExtractionDetails.policeReportAttachedFlag");
		
		// Copay percentage value and aft copay amoutn will be saved for all medical screens..
		preauthMap.field("amtConsCopayPercentage","amountConsCopayPercentage");
		preauthMap.field("balanceSICopayPercentage","balanceSICopayPercentage");
		preauthMap.field("amtConsAftCopayAmount","amountConsAftCopayAmt");
		preauthMap.field("balanceSIAftCopayAmt","balanceSIAftCopayAmt");
		
		preauthMap.field("referToFLPRemarks", "preauthMedicalDecisionDetails.referToFLPremarks");
		
		preauthMap.field("premiumAmt", "preauthMedicalDecisionDetails.uniquePremiumAmt");
		preauthMap.field("approvedAmtAftPremium", "preauthMedicalDecisionDetails.amountToHospAftPremium");
		preauthMap.field("pedDisabilityFlag", "preauthMedicalProcessingDetails.pedDisabilityFlag");
		preauthMap.field("pedDisabilityDetails", "preauthMedicalProcessingDetails.pedDisabilityDetails");
		//preauthMap.field("workPlace", "preauthDataExtractionDetails.workOrNonWorkPlace");
		preauthMap.field("stent", "preauthDataExtractionDetails.ptcaCabg.id");
		preauthMap.field("corporateUtilizedAmt", "preauthMedicalDecisionDetails.corporateBufferUtilizedAmt");
		preauthMap.field("cpuAllocationSuggestionId.key","preauthMedicalDecisionDetails.cpuSuggestionCategory.id");
		preauthMap.field("cpuAllocationSuggestionId.value","preauthMedicalDecisionDetails.cpuSuggestionCategory.value");
		preauthMap.field("cpuAllocationReferringRemarks","preauthMedicalDecisionDetails.cpuRemarks");
		preauthMap.field("cpuSuggestedAmount","preauthMedicalDecisionDetails.cpuAmountSuggested");
		preauthMap.field("fvrAlertFlag","fvrAlertFlag");
		preauthMap.field("initiateFvr","preauthMedicalDecisionDetails.initiateFieldVisitRequest");
		preauthMap.field("fvrNotRequiredRemarks.key","preauthMedicalDecisionDetails.fvrNotRequiredRemarks.id");
		preauthMap.field("fvrNotRequiredRemarks.value","preauthMedicalDecisionDetails.fvrNotRequiredRemarks.value");

		preauthMap.field("behaviourOfHospCmb.key","preauthMedicalDecisionDetails.behaviourHospValue.id");
		preauthMap.field("behaviourOfHospCmb.value","preauthMedicalDecisionDetails.behaviourHospValue.value");

		preauthMap.field("mbAgrdDisNtAplyChkBx","preauthMedicalDecisionDetails.mbAgrdDisNtAplyChkBx");
		preauthMap.field("hospCollectOvrAmtChkBx","preauthMedicalDecisionDetails.hospCollectOvrAmtChkBx");
		
		//R1118
		preauthMap.field("fvrOtherRmrks", "preauthMedicalDecisionDetails.fvrNotRequiredOthersRemarks");

		//R1006
		preauthMap.field("claimedAmt", "preauthDataExtractionDetails.amtClaimed");
		preauthMap.field("discntHospBill", "preauthDataExtractionDetails.disCntHospBill");
		preauthMap.field("netAmount", "preauthDataExtractionDetails.netAmt");
		
		preauthMap.field("covidTreatmentId.key","preauthDataExtractionDetails.homeCareTreatment.id");
		preauthMap.field("covidTreatmentId.value","preauthDataExtractionDetails.homeCareTreatment.value");
		
		preauthMap.field("icacFlag", "icacProcessFlag");
		
		
		//R1192
		//preauthMap.field("scoringFlag", "hospitalScoreFlag");
		 
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
		
		finalPedValidationMap.field("recTypeFlag","recTypeFlag");
		finalPedValidationMap.field("sittingsInput","sittingsInput");
		
		finalPedValidationMap.field("ambulanceChargeFlag", "isAmbChargeFlag");
		finalPedValidationMap.field("ambulanceCharges", "ambulanceCharge");
		finalPedValidationMap.field("ambulanceChargeWithAmount", "amtWithAmbulanceCharge");
		finalPedValidationMap.field("coPayTypeId.key","coPayTypeId.id");
		finalPedValidationMap.field("coPayTypeId.value","coPayTypeId.value");
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
		specialityMap.field("procedure.key","procedure.id");
		specialityMap.field("procedure.procedureName","procedure.value");
		specialityMap.field("remarks","remarks");
		specialityMap.field("oldspecialityType.key","oldspecialityType.id");
		specialityMap.field("oldspecialityType.value","oldspecialityType.value");
		specialityMap.field("splFlag","splFlag");
		specialityMap.field("oldProcedure.key","oldProcedure.id");
		specialityMap.field("oldProcedure.procedureName","oldProcedure.value");
		specialityMap.field("oldRemarks","oldRemarks");

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
		finalProcedureMap.field("sittingsInput","sittingsInput");
		
		finalProcedureMap.field("ambulanceChargeFlag", "isAmbChargeFlag");
		finalProcedureMap.field("ambulanceCharges", "ambulanceCharge");
		finalProcedureMap.field("ambulanceChargeWithAmount", "amtWithAmbulanceCharge");
		finalProcedureMap.field("coPayTypeId.key","coPayTypeId.id");
		finalProcedureMap.field("coPayTypeId.value","coPayTypeId.value");
		
		finalProcedureMap.field("recTypeFlag","recTypeFlag");
		finalProcedureMap.field("oldprocedureID","oldprocedureID");
		finalProcedureMap.field("procedureFlag","procedureFlag");
		finalProcedureMap.field("speciality.key","speciality.id");
		finalProcedureMap.field("speciality.value","speciality.value");
		finalProcedureMap.field("speciality.value","specialityCodeValue");
		finalProcedureMap.field("newProceudreName","newProcedureName");
		finalProcedureMap.field("oldSpecialityKey","oldSpecialityKey");
		finalProcedureMap.field("oldNewProceudreName","oldNewProcedureName");
		
//		classMap.field("relapseFlag","preauthPreviousClaimsDetails.relapseOfIllness");
//		classMap.field("relapseRemarks","preauthPreviousClaimsDetails.relapseRemarks");
//		classMap.field("patientStatus","preauthMedicalDecisionDetails.patientStatus");
//		classMap.field("dateOfDeath","preauthMedicalDecisionDetails.deathDate");
//		classMap.field("deathReason","preauthMedicalDecisionDetails.reasonForDeath");
//		classMap.field("terminatorCover","preauthMedicalDecisionDetails.terminateCover.value");
//		classMap.field("specialistOpinionTaken","preauthMedicalDecisionDetails.specialistOpinionTaken");
//		classMap.field("specialistType","preauthMedicalDecisionDetails.specialistType");
//		classMap.field("specialistConsulted","preauthMedicalDecisionDetails.specialistConsulted");
//		classMap.field("specialistRemarks","preauthMedicalDecisionDetails.remarksBySpecialist");
//		classMap.field("initiateFvr","preauthMedicalDecisionDetails.initiateFieldVisitRequest");
//		classMap.field("fvrNotRequiredRemarks","preauthMedicalDecisionDetails.fvrNotRequiredRemarks");
//		classMap.field("approvedAmount","preauthMedicalDecisionDetails.initialApprovedAmt");
//		classMap.field("coPay","preauthMedicalDecisionDetails.selectedCopay");
//		classMap.field("totalApprovalAmount","preauthMedicalDecisionDetails.initialTotalApprovedAmt");
//		classMap.field("remarks","preauthMedicalDecisionDetails.approvalRemarks");
//		classMap.field("remarks","preauthMedicalDecisionDetails.denialRemarks");
//		classMap.field("remarks","preauthMedicalDecisionDetails.rejectionRemarks");
//		classMap.field("categoryReason.key","preauthMedicalDecisionDetails.reasonForDenial.id");	
//		classMap.field("categoryReason.value","preauthMedicalDecisionDetails.reasonForDenial.value");
//		classMap.field("categoryReason.key","preauthMedicalDecisionDetails.rejectionCategory.id");
//		classMap.field("categoryReason.value","preauthMedicalDecisionDetails.rejectionCategory.value");
		
		
		
//		pedvalidationClassMap.field("key", "key");
//		pedvalidationClassMap.field("preauthKey","preauthKey");
//		pedvalidationClassMap.field("diagnosis.value","diagnosis");
//		pedvalidationClassMap.field("diagnosis.key","diagnosisName.id");
//		pedvalidationClassMap.field("diagnosis.value", "diagnosisName.value");
//		pedvalidationClassMap.field("icdChapter.id","icdChapterKey");
//		pedvalidationClassMap.field("icdBlock.id","icdBlockKey");
//		pedvalidationClassMap.field("icdCode.id","icdCodeKey");
//		pedvalidationClassMap.field("diagnosis.id", "diagnosisId");
//		pedvalidationClassMap.field("sublimitApplicable.value","sublimitApplicable");
//		pedvalidationClassMap.field("sublimitName.id","sublimtNameId");
//		pedvalidationClassMap.field("sublimitAmt","subLimitAmount");
//		pedvalidationClassMap.field("considerForPayment.value","considerForPayment");
//		pedvalidationClassMap.field("sumInsuredRestriction.id","sumInsuredRestrictionId");
		
		claimAmountClassMap.field("key","key");
		claimAmountClassMap.field("preauthKey","preauth.key");
		claimAmountClassMap.field("benefitId","benefitId");
		claimAmountClassMap.field("stageId","stage.key");
		claimAmountClassMap.field("statusId","status.key");	
		claimAmountClassMap.field("restrictToFlag","restrictTo");
		claimAmountClassMap.field("overridePackageDeductionFlag","overridePackageDeduction");
		claimAmountClassMap.field("totalBillingDays","totalBillingDays");
		claimAmountClassMap.field("billingPerDayAmount", "billDayPayment");
		claimAmountClassMap.field("claimedBillAmount","claimedBillAmount");
		claimAmountClassMap.field("deductibleAmount","deductibleAmount");
		claimAmountClassMap.field("netAmount","netAmount");
		claimAmountClassMap.field("totalDaysForPolicy","totalDaysForPolicy");
		claimAmountClassMap.field("policyPerDayPayment", "policyDayPayment");
		claimAmountClassMap.field("policyMaxAmount","policyMaxAmount");
		claimAmountClassMap.field("paybleAmount","paybleAmount");
		claimAmountClassMap.field("nonPayableAmount","nonPayableAmount");		
//		claimAmountClassMap.field("restrictTo","restrictTo");
//		claimAmountClassMap.field("overridePackageDeduction","overridePackageDeduction");
		claimAmountClassMap.field("nonPayableReason","nonPayableReason");
		claimAmountClassMap.field("considerPerDayAmt","perDayPayable");
		
		otherInsurerMap.field("key","key");
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
		
		otherInsurerMap.register();
		preauthMap.register();
		coordinatorMap.register();
		specialityMap.register();
		pedvalidationClassMap.register();
		claimAmountClassMap.register();
		finalProcedureMap.register();
		finalPedValidationMap.register();
		treatingDoctorDetailMap.register();
		implantMap.register();
		
		
		preauthMapper = mapperFactory.getMapperFacade(Preauth.class, PreauthDTO.class);
		coordinatorMapper = mapperFactory.getMapperFacade(Coordinator.class, CoordinatorDTO.class);
		specialityIndMap = mapperFactory.getMapperFacade(Speciality.class, SpecialityDTO.class);
		pedValidationIndMap = mapperFactory.getMapperFacade(PedValidation.class, DiagnosisDetailsTableDTO.class);
		diagnosiPEDIndMap = mapperFactory.getMapperFacade(DiagnosisPED.class, PedDetailsTableDTO.class);
		claimAmountIndMap = mapperFactory.getMapperFacade(ClaimAmountDetails.class, NoOfDaysCell.class);
		implantDetailsMap = mapperFactory.getMapperFacade(ImplantDetails.class, ImplantDetailsDTO.class);
		
		specialityMapper = mapperFactory.getMapperFacade();
		finalPedvalidation = mapperFactory.getMapperFacade();
		claimedAmountTableMapper = mapperFactory.getMapperFacade();
		otherInsurerMapper = mapperFactory.getMapperFacade();
		treatingDoctorMapper = mapperFactory.getMapperFacade();
		implantMapper =  mapperFactory.getMapperFacade();
		
		
		
		procedureIndMap = mapperFactory.getMapperFacade(Procedure.class, ProcedureDTO.class);
		
		newProcedureMapper = mapperFactory.getMapperFacade();
		procedureMapper = mapperFactory.getMapperFacade();
		//pedvalidationTableMapper = mapperFactory.getMapperFacade();
		
		oldProcedureMapper= mapperFactory.getMapperFacade();
		newProcedureListMapper= mapperFactory.getMapperFacade();
		finalProcedureMapper = mapperFactory.getMapperFacade();
		
		finalPedvalidation = mapperFactory.getMapperFacade();
		diagnosisPedMapper = mapperFactory.getMapperFacade();
		procedureListMapper = mapperFactory.getMapperFacade();
		pedValidationIndMap = mapperFactory.getMapperFacade(PedValidation.class, DiagnosisDetailsTableDTO.class);
		treatingDoctorMap = mapperFactory.getMapperFacade(TreatingDoctorDetails.class, TreatingDoctorDTO.class);
	}
	
	
	
	/**************************** PREAUTH *********************************/
	
	public PreauthDTO getPreauthDTO(Preauth preauth) {
		PreauthDTO dest = preauthMapper.map(preauth);
		return dest;
	}
	
	public Preauth getPreauth(PreauthDTO preauthDTO) {
		Preauth dest = preauthMapper.mapReverse(preauthDTO);
		
		if(preauthDTO.getPreauthDataExtractionDetails().getCatastrophicLoss() != null) {
			dest.setCatastrophicLoss(preauthDTO.getPreauthDataExtractionDetails().getCatastrophicLoss().getId());
		}
		
		if(preauthDTO.getPreauthDataExtractionDetails().getNatureOfLoss() != null) {
			dest.setNatureOfLoss(preauthDTO.getPreauthDataExtractionDetails().getNatureOfLoss().getId());
		}
		
		if(preauthDTO.getPreauthDataExtractionDetails().getCauseOfLoss() != null) {
			dest.setCauseOfLoss(preauthDTO.getPreauthDataExtractionDetails().getCauseOfLoss().getId());
		}
		
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
	
	
	
	/**************************** CLAIM AMOUNT DETAILS**********************************/
	
	public List<ClaimAmountDetails> getClaimedAmountDetailsList(List<NoOfDaysCell> noOfDaysCellTableDtoList)
	{
		List<ClaimAmountDetails> mapAsList = claimedAmountTableMapper.mapAsList(noOfDaysCellTableDtoList, ClaimAmountDetails.class);
		return mapAsList;
	}
	
	public  List<NoOfDaysCell> getClaimedAmountDetailsDTOList( List<ClaimAmountDetails> noOfDaysCellTableDtoList)
	{
		List<NoOfDaysCell> mapAsList = claimedAmountTableMapper.mapAsList(noOfDaysCellTableDtoList, NoOfDaysCell.class);
		return mapAsList;
	}
	
	public ClaimAmountDetails getClaimedAmountDetails(NoOfDaysCell noOfDaysCellTableDto)
	{
		ClaimAmountDetails dest = claimAmountIndMap.mapReverse(noOfDaysCellTableDto);
		return dest;
	}
	
	public NoOfDaysCell getClaimedAmountDetailsList(ClaimAmountDetails claimAmountDetails)
	{
		NoOfDaysCell dest = claimAmountIndMap.map(claimAmountDetails);
		return dest;
	}
	
	public List<UpdateOtherClaimDetails> getUpdateOtherClaimDetails(List<UpdateOtherClaimDetailDTO> updateOtherClaimDetailsList) {
		List<UpdateOtherClaimDetails> mapAsList = otherInsurerMapper.mapAsList(updateOtherClaimDetailsList, UpdateOtherClaimDetails.class);
		return mapAsList;
	}
	
	public static PreMedicalMapper getInstance(){
        if(myObj == null){
            myObj = new PreMedicalMapper();
            getAllMapValues();
        }
        return myObj;
	 }
	

	
	/**************************** CLAIM AMOUNT DETAILS END**********************************/
	
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

package com.shaic.newcode.wizard.domain;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthEscalateDTO;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.PreauthEscalate;

public class PreauthMapper {
	
	private BoundMapperFacade<Preauth, PreauthDTO> boundMapper;
	
	private BoundMapperFacade<PreauthEscalate, PreauthEscalateDTO> boundEscalateMapper;

	public PreauthMapper()
	{
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Preauth, PreauthDTO> classMap = mapperFactory.classMap(Preauth.class,PreauthDTO.class);
		ClassMapBuilder<PreauthEscalate, PreauthEscalateDTO> classEscalateMap = mapperFactory.classMap(PreauthEscalate.class,PreauthEscalateDTO.class);
		
		classMap.field("key","key");
		classMap.field("policy.key","policyKey");
		classMap.field("intimation.key","intimationKey");
		classMap.field("claim.key","claimKey");
//		classMap.field("diagnosis","preauthDataExtractionDetails.diagnosis");
		classMap.field("dataOfAdmission","preauthDataExtractionDetails.admissionDate");
		classMap.field("preauthId","preauthDataExtractionDetails.referenceNo");
		classMap.field("numberOfDays","preauthDataExtractionDetails.noOfDays");
		classMap.field("natureOfTreatment","preauthDataExtractionDetails.natureOfTreatment");
		classMap.field("consultationDate","preauthDataExtractionDetails.firstConsultantDate");
		classMap.field("subLimitFlag","preauthDataExtractionDetails.subLimit");
		classMap.field("corporateBufferFlag","preauthDataExtractionDetails.corpBuffer");
		classMap.field("sumInsured","preauthDataExtractionDetails.sumInsured");
		classMap.field("subLimitSi","preauthDataExtractionDetails.sublimitSumInsured");
//		classMap.field("sublimitSpecify","preauthDataExtractionDetails.sublimitSpecify.value");
		classMap.field("criticalIllnessFlag","preauthDataExtractionDetails.criticalIllness");
		classMap.field("criticalIllnessSpecify","preauthDataExtractionDetails.specifyIllness");
		classMap.field("roomCategory","preauthDataExtractionDetails.roomCategory");
		classMap.field("ventilatorSupport","preauthDataExtractionDetails.ventilatorSupportFlag");
		classMap.field("treatmentType","preauthDataExtractionDetails.treatmentType");
		classMap.field("requestedAmount","preauthDataExtractionDetails.preauthReqAmt");
		classMap.field("treatmentRemarks","preauthDataExtractionDetails.treatmentRemarks");
		classMap.field("coordinatorFlag","coordinatorDetails.");
		
		
		classMap.field("relapseFlag","preauthPreviousClaimsDetails.relapseFlag");
		classMap.field("relapseRemarks","preauthPreviousClaimsDetails.relapseRemarks");
		classMap.field("patientStatus","preauthMedicalDecisionDetails.patientStatus");
		classMap.field("dateOfDeath","preauthMedicalDecisionDetails.deathDate");
		classMap.field("deathReason","preauthMedicalDecisionDetails.reasonForDeath");
		classMap.field("terminatorCover","preauthMedicalDecisionDetails.terminateCover.value");
		classMap.field("specialistOpinionTaken","preauthMedicalDecisionDetails.specialistOpinionTaken");
		classMap.field("specialistType","preauthMedicalDecisionDetails.specialistType");
		classMap.field("specialistConsulted","preauthMedicalDecisionDetails.specialistConsulted");
		classMap.field("specialistRemarks","preauthMedicalDecisionDetails.remarksBySpecialist");
		classMap.field("initiateFvr","preauthMedicalDecisionDetails.initiateFieldVisitRequest");
		classMap.field("fvrNotRequiredRemarks","preauthMedicalDecisionDetails.fvrNotRequiredRemarks");
		classMap.field("approvedAmount","preauthMedicalDecisionDetails.initialApprovedAmt");
		classMap.field("coPay","preauthMedicalDecisionDetails.selectedCopay");
		classMap.field("totalApprovalAmount","preauthMedicalDecisionDetails.initialTotalApprovedAmt");
		classMap.field("remarks","preauthMedicalDecisionDetails.approvalRemarks");
		classMap.field("remarks","preauthMedicalDecisionDetails.denialRemarks");
		classMap.field("remarks","preauthMedicalDecisionDetails.rejectionRemarks");
//		classMap.field("categoryReason.key","preauthMedicalDecisionDetails.reasonForDenial.id");	
//		classMap.field("categoryReason.value","preauthMedicalDecisionDetails.reasonForDenial.value");
//		classMap.field("categoryReason.key","preauthMedicalDecisionDetails.rejectionCategory.id");
//		classMap.field("categoryReason.value","preauthMedicalDecisionDetails.rejectionCategory.value");
		classEscalateMap.field("escalateTo.key","escalateTo.id");
		
		classMap.register();
		classEscalateMap.register();
		
		this.boundMapper = mapperFactory.getMapperFacade(Preauth.class, PreauthDTO.class);
		
		this.boundEscalateMapper = mapperFactory.getMapperFacade(PreauthEscalate.class, PreauthEscalateDTO.class);
		
	}
	

	public PreauthDTO getPreauthDTO(Preauth preauth) {
		PreauthDTO dest = boundMapper.map(preauth);
		return dest;
	}
	
	public PreauthEscalateDTO getPreautEscalatehDTO(PreauthEscalate preauthEscalate) {
		PreauthEscalateDTO dest = boundEscalateMapper.map(preauthEscalate);
		return dest;
	}
	
	public Preauth getPreauth(PreauthDTO preauthDTO) {
		Preauth dest = boundMapper.mapReverse(preauthDTO);
		return dest;
	}
	
}

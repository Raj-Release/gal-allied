package com.shaic.newcode.wizard.domain;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.domain.preauth.PedValidation;

public class PedValidationMapper
{
	private MapperFacade mapper;

    public PedValidationMapper()
    {
    	
    	MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<PedValidation, DiagnosisDetailsTableDTO> classMap = mapperFactory.classMap(PedValidation.class,DiagnosisDetailsTableDTO.class);
    
		classMap.field("key","key");
		classMap.field("intimation.key","intimationKey");
		classMap.field("policy.key","policyKey");
		classMap.field("preauth.key","preauthKey");
		classMap.field("approveAmount","approvedAmount");
		classMap.field("diagnosis","diagnosis");
		classMap.field("diagonsisImpact.key","pedExclusionImpactOnDiagnosis.id");
		classMap.field("diagonsisImpact.value","pedExclusionImpactOnDiagnosis.value");
		classMap.field("exclusionDetails.key","exclusionDetails.id");
		classMap.field("exclusionDetails.value","exclusionDetails.value");
		classMap.field("downsizedAmount","downsizedAmount");
		classMap.field("approveAmount","approvedAmount");
		classMap.field("pedName","pedName");
		classMap.field("policyAging","policyAgeing");
		classMap.field("remarks","remarks");
		classMap.field("subLimitAmount","subLimitAmount");   
    
		classMap.register();
		
		this.mapper = mapperFactory.getMapperFacade();
    
    }

	public PedValidation getPedValidation(DiagnosisDetailsTableDTO pedValidationTableDTO) {
		PedValidation result = mapper.map(pedValidationTableDTO, PedValidation.class);
		return result;
	}
	
	public DiagnosisDetailsTableDTO getPEDValidationTableDTO(PedValidation pedValidation) {
		DiagnosisDetailsTableDTO result = mapper.map(pedValidation, DiagnosisDetailsTableDTO.class);
		return result;
	}
    
}

package com.shaic.newcode.wizard.domain;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.preauth.wizard.dto.OldPedEndorsementDTO;
import com.shaic.domain.preauth.OldInitiatePedEndorsement;

public class OldPedEndorsementMapper
{
	private MapperFacade mapper;

	public OldPedEndorsementMapper()
	{
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<OldInitiatePedEndorsement, OldPedEndorsementDTO> classMap = mapperFactory.classMap(OldInitiatePedEndorsement.class,OldPedEndorsementDTO.class);
		
		classMap.field("key","key");
		classMap.field("approvalRemarks","approvalRemarks");
		classMap.field("escalateRemarks","escalateRemarks");
		classMap.field("intimation.key","intimationKey");
		classMap.field("policy.key","policyKey");
		classMap.field("preauth.key","preauthKey");
		classMap.field("officeCode","private String ");
		classMap.field("pedInitiateId","pedInitiateId");
		classMap.field("pedName","pedName");
		classMap.field("pedSuggestion.key","pedSuggestion.Id");
		classMap.field("pedSuggestion.value","pedSuggestion.value");
		classMap.field("rejectionRemarks","rejectionRemarks");
		classMap.field("remarks","remarks");
		classMap.field("repudiationLetterDate","repudiationLetterDate");
		classMap.field("status","status");
		classMap.field("statusDate","statusDate");
		classMap.field("statusRemarks","statusRemarks");
		
		classMap.register();
		
		this.mapper = mapperFactory.getMapperFacade();
		
	}
	

	public OldPedEndorsementDTO getOldPedEndorsementDTO(OldInitiatePedEndorsement oldInitiatePedEndorsement) {
		OldPedEndorsementDTO dest = mapper.map(oldInitiatePedEndorsement,OldPedEndorsementDTO.class);
		return dest;
	}
	
	public OldInitiatePedEndorsement getOldInitiatePedEndorsement(OldPedEndorsementDTO oldPedEndorsementDTO) {
		OldInitiatePedEndorsement dest = mapper.map(oldPedEndorsementDTO,OldInitiatePedEndorsement.class);
		return dest;
	}		
}

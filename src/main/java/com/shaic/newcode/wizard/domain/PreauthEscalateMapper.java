package com.shaic.newcode.wizard.domain;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.preauth.wizard.dto.PreauthEscalateDTO;
import com.shaic.domain.preauth.PreauthEscalate;

public class PreauthEscalateMapper 
{
	private MapperFacade mapper;

	public PreauthEscalateMapper()
	{
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<PreauthEscalate, PreauthEscalateDTO> classMap = mapperFactory.classMap(PreauthEscalate.class,PreauthEscalateDTO.class);
		
		classMap.field("key","key");
		classMap.field("escalateRemarks","escalateRemarks");
		classMap.field("escalateTo.key","escalateTo.id");
//		classMap.field("fileUpload"
		classMap.field("preauth.key","preauth.key");
		classMap.field("officeCode","officeCode");
		classMap.field("specialistType.key","specialistType.id");
		classMap.field("status","status");
		classMap.field("statusDate","statusDate");
		
		classMap.register();
		
		this.mapper = mapperFactory.getMapperFacade();
		
	}
	
	public PreauthEscalate getPreauthEscalate(PreauthEscalateDTO preauthEscalateDTO) {
		PreauthEscalate dest = mapper.map(preauthEscalateDTO, PreauthEscalate.class);
		return dest;
	}
	
	public PreauthEscalateDTO getPreauthEscalteDTO(PreauthEscalate preauthEscalate) {
		PreauthEscalateDTO dest = mapper.map(preauthEscalate, PreauthEscalateDTO.class);
		return dest;
	}		
		
}

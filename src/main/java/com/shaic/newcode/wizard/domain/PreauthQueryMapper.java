package com.shaic.newcode.wizard.domain;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.preauth.wizard.dto.PreauthQueryDTO;
import com.shaic.domain.preauth.PreauthQuery;

public class PreauthQueryMapper 
{
private MapperFacade mapper;
	
	public PreauthQueryMapper()
	{
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<PreauthQuery, PreauthQueryDTO> classMap = mapperFactory.classMap(PreauthQuery.class,PreauthQueryDTO.class);	
		
		classMap.field("key","key"); 
		classMap.field("preauth.key","preauthKey");
		classMap.field("activeStatus","activeStatus");
		classMap.field("activeStatusDate","activeStatusDate");
		classMap.field("createdBy","createdBy");
		classMap.field("createdDate","createdDate");
		classMap.field("officeCode","officeCode");				
		classMap.field("queryRemarks","queryRemarks");								
		classMap.field("status","status");		
		classMap.field("statusDate","statusDate");
										
		classMap.register();
		
		this.mapper = mapperFactory.getMapperFacade();
		
	}
	
	public PreauthQuery getPreauthQuery(PreauthQueryDTO preauthQueryDTO) {
		PreauthQuery dest = mapper.map(preauthQueryDTO, PreauthQuery.class);
		return dest;
	}
	
	public PreauthQueryDTO getPreauthQueryDTO(PreauthQuery preauthQuery) {
		PreauthQueryDTO dest = mapper.map(preauthQuery, PreauthQueryDTO.class);
		return dest;
	}	
}

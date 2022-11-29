package com.shaic.newcode.wizard.domain;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.preauth.wizard.dto.NewInitiatePedEndorsementDTO;
import com.shaic.domain.preauth.NewInitiatePedEndorsement;

public class NewPedEndorsementMapper
{
	private MapperFacade mapper;

	public NewPedEndorsementMapper()
	{
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<NewInitiatePedEndorsement, NewInitiatePedEndorsementDTO> classMap = mapperFactory.classMap(NewInitiatePedEndorsement.class,NewInitiatePedEndorsementDTO.class);
		
		classMap.field("key","key");
		classMap.field("description","description");
		classMap.field("doctorRemarks","doctorRemarks");
		classMap.field("oldInitiatePedEndorsement.key","oldPedEndorsementKey");
		classMap.field("icdBlock.key","ICDBlock.id");
		classMap.field("icdBlock.value","ICDBlock.value");
		classMap.field("icdChapter.key","ICDChapter.id");
		classMap.field("icdChapter.value","ICDChapter.value");
		classMap.field("icdCode.key","ICDCode.id");
		classMap.field("icdCode.value","ICDCode.value");
		classMap.field("officeCode","officeCode");
		classMap.field("othesSpecify","othesSpecify");
		classMap.field("pedCode","pedCode");
		classMap.field("source.key","source.id");
		classMap.field("source.value","source.value");
		
		classMap.register();
		
		this.mapper = mapperFactory.getMapperFacade();
		
	}
	
	public NewInitiatePedEndorsement getNewInitiatePedEndorsement(NewInitiatePedEndorsementDTO newPedEndorsementDTO) {
		NewInitiatePedEndorsement dest = mapper.map(newPedEndorsementDTO, NewInitiatePedEndorsement.class);
		return dest;
	}
	
	public NewInitiatePedEndorsementDTO getNewInitiatePedEndorsementDTO(NewInitiatePedEndorsement newInitiatePedEndorsement) {
		NewInitiatePedEndorsementDTO dest = mapper.map(newInitiatePedEndorsement, NewInitiatePedEndorsementDTO.class);
		return dest;
	}
}
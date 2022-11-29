package com.shaic.newcode.wizard.domain;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.NewBabyIntimation;
import com.shaic.newcode.wizard.dto.NewBabyIntimationDto;

public class NewBabyIntimationMapper {
	
private MapperFacade mapper;
	
	public NewBabyIntimationMapper() 
	{
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<NewBabyIntimation, NewBabyIntimationDto> classMap = mapperFactory.classMap(NewBabyIntimation.class, NewBabyIntimationDto.class);
		classMap.field("key","key");
		classMap.field("intimation.key","intimationKey");
		classMap.field("officeCode", "officeCode");
		classMap.field("babyRelationship.key", "babyRelationship.id");
		classMap.field("babyRelationship.value", "babyRelationship.value");
		classMap.field("name", "babyName");
		 
		 classMap.register();
		 
		 this.mapper = mapperFactory.getMapperFacade();
	}
	
	public NewBabyIntimation getNewBabyIntimation(NewBabyIntimationDto newBabyIntimationDto) {
		NewBabyIntimation dest = mapper.map(newBabyIntimationDto, NewBabyIntimation.class);
		return dest;
	}
	
	public NewBabyIntimationDto getNewBabyIntimationDto(NewBabyIntimation newBabyIntimation) {
		NewBabyIntimationDto dest = mapper.map(newBabyIntimation, NewBabyIntimationDto.class);
		return dest;
	}

}

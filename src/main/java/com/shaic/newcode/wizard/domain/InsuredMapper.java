package com.shaic.newcode.wizard.domain;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Insured;
import com.shaic.domain.InsuredDto;

public class InsuredMapper {
	
	private MapperFacade mapper;
	
	public InsuredMapper(){
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Insured, InsuredDto> classMap = mapperFactory.classMap(
				Insured.class, InsuredDto.class);
		
		classMap.field("key", "key");
		classMap.field("insuredName", "insuredName");
		classMap.field("insuredEmployeeId", "insuredEmployeeId");
		classMap.field("insuredGender.key", "insuredGender.id");
		classMap.field("insuredGender.value", "insuredGender.value");
		classMap.field("insuredAge", "insuredAge");
		classMap.field("insuredDateOfBirth", "insuredDateOfBirth");
		classMap.field("relationshipwithInsuredId.key", "relationshipwithInsuredId.id");
		classMap.field("relationshipwithInsuredId.value", "relationshipwithInsuredId.value");
		classMap.field("healthCardNumber", "healthCardNumber");
		classMap.field("registerdMobileNumber", "registerdMobileNumber");
		classMap.field("insuredSumInsured", "insuredSumInsured");
		classMap.field("insuredRechargedSI", "insuredRechargedSI");
		classMap.field("insuredRestoredSI", "insuredRestoredSI");
		classMap.field("cummulativeBonus", "cummulativeBonus");
		classMap.field("lopFlag", "lobFlag");
		
		this.mapper = mapperFactory.getMapperFacade();
	}
	
	public Insured getInsured(InsuredDto insuredDto) {
		Insured dest = mapper.map(insuredDto, Insured.class);
		return dest;
	}

	public InsuredDto getInsuredDto(Insured insured) {
		InsuredDto dest = mapper.map(insured, InsuredDto.class);
		return dest;
	}

}

package com.shaic.claim.policy.updateHospital.ui;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.UpdateHospital;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class UpdateHospitalMapper {
private MapperFacade mapper;
	
	public UpdateHospitalMapper() 
	{
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<UpdateHospital, UpdateHospitalDTO> classMap = mapperFactory.classMap(UpdateHospital.class, UpdateHospitalDTO.class);
		classMap.field("address","address");
		classMap.field("key", "key");
		classMap.field("stateId", "stateId");
		classMap.field("hospitalName", "hospitalName");
//		classMap.field("localityId", "localityId");
		classMap.field("pincode", "pincode");
		classMap.field("cityId", "cityId");
		classMap.field("phoneNumber", "phoneNumber");
		classMap.field("faxNumber", "faxNumber");
		classMap.field("mobileNumber", "mobileNumber");
		classMap.field("emailId", "emailId");
		classMap.field("officeCode", "hospitalCode");
		classMap.field("hospitalId", "hospitalCodeIrda");
		classMap.register();
		 
		this.mapper = mapperFactory.getMapperFacade();
	}
	
	public UpdateHospital getUpdateHospital(UpdateHospitalDTO updateHospitalDto) {
		System.out.println("-------------------------!!!!!!!!!!!!!-------------"+updateHospitalDto.getFaxNumber());
		UpdateHospital dest = mapper.map(updateHospitalDto, UpdateHospital.class);
		return dest;
	}
	
	public UpdateHospitalDTO getUpdateHospitalDto(UpdateHospital updateHospital) {
		UpdateHospitalDTO dest = mapper.map(updateHospital, UpdateHospitalDTO.class);
		return dest;
	}
}


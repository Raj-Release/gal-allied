package com.shaic.claim.selecthospital.ui;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.intimation.create.dto.HospitalDto;
import com.shaic.domain.Hospitals;
import com.shaic.domain.UnFreezHospitals;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class HospitalMapper {

	private BoundMapperFacade<Hospitals, HospitalDto> mapper;
	private BoundMapperFacade<UnFreezHospitals, HospitalDto> mapper1;
	MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
	ClassMapBuilder<Hospitals, HospitalDto> classMap = mapperFactory.classMap(Hospitals.class, HospitalDto.class);
	
	ClassMapBuilder<UnFreezHospitals, HospitalDto> classMap1 = mapperFactory.classMap(UnFreezHospitals.class, HospitalDto.class);

	public HospitalMapper() {
		classMap.field("key", "key");
		classMap.field("hospitalIrdaCode", "hospitalIrdaCode");
		classMap.field("hospitalCode", "hospitalCode");
		classMap.field("name", "name");
		classMap.field("city", "city");
		classMap.field("state", "state");
		classMap.field("address", "address");
		classMap.field("pincode", "pincode");
		classMap.field("fax", "fax");
		classMap.field("cpuId", "cpuId");
		classMap.field("cityId", "cityId");
		classMap.field("stateId", "stateId");
		classMap.field("hospitalType", "hospitalType");
		classMap.field("hospitalType.key", "hospitalType.value");
		classMap.register();
		this.mapper = mapperFactory.getMapperFacade(Hospitals.class, HospitalDto.class);
		
		classMap1.field("key", "key");
		classMap1.field("hospitalIrdaCode", "hospitalIrdaCode");
		classMap1.field("hospitalCode", "hospitalCode");
		classMap1.field("name", "name");
		classMap1.field("city", "city");
		classMap1.field("state", "state");
		classMap1.field("address", "address");
		classMap1.field("pincode", "pincode");
		classMap1.field("fax", "fax");
		classMap1.field("cpuId", "cpuId");
		classMap1.field("cityId", "cityId");
		classMap1.field("stateId", "stateId");
		classMap1.field("hospitalType", "hospitalType");
		classMap1.field("hospitalType.key", "hospitalType.value");
		classMap1.field("discount", "discount");
		classMap1.field("discountRemark", "discountRemark");
		classMap1.register();
		this.mapper1 = mapperFactory.getMapperFacade(UnFreezHospitals.class, HospitalDto.class);
	}

	public Hospitals getHospital(HospitalDto hospitalDetailsDTO) {
		Hospitals dest = mapper.mapReverse(hospitalDetailsDTO);
		return dest;
	}

	public HospitalDto getHospitalDTO(Hospitals hospitals) {
		HospitalDto dest = mapper.map(hospitals);
		return dest;
	}
	
	
	public HospitalDto getUnFreezHospitalDTO(UnFreezHospitals hospitals) {
		HospitalDto dest = mapper1.map(hospitals);
		return dest;
	}
	

}

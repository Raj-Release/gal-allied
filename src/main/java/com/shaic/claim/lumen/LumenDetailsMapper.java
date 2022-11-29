package com.shaic.claim.lumen;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.LumenDetails;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class LumenDetailsMapper {

	private static MapperFacade tableMapper;

	static LumenDetailsMapper  myObj;

	public static void getAllMapValues()  {
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<LumenDetails, LumenDetailsDTO> classMap = mapperFactory.classMap(LumenDetails.class, LumenDetailsDTO.class);
		classMap.field("lumenRequest", "lumenRequest");
		classMap.field("participantType", "participantType");
		classMap.field("employeeId", "employeeId");
		classMap.field("employeeName", "employeeName");
		classMap.field("employeeZone", "employeeZone");
		classMap.field("employeeDept","employeeDept");
		classMap.field("status", "status");
		classMap.field("stage", "stage");
		classMap.field("createdBy", "createdBy");
		classMap.field("createdDate", "createdDate");
		classMap.register();
		tableMapper = mapperFactory.getMapperFacade();
	}

	public static List<LumenDetailsDTO> getLumenDetailsData(List<LumenDetails> lumenRequestList) {
		List<LumenDetailsDTO> mapAsList = tableMapper.mapAsList(lumenRequestList, LumenDetailsDTO.class);
		return mapAsList;
	}

	public static LumenDetailsMapper getInstance(){
		if(myObj == null){
			myObj = new LumenDetailsMapper();
			getAllMapValues();
		}
		return myObj;
	}
}

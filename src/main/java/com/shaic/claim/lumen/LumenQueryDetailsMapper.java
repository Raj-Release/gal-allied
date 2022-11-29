package com.shaic.claim.lumen;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.LumenQueryDetails;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class LumenQueryDetailsMapper {

	private static MapperFacade tableMapper;

	static LumenQueryDetailsMapper  myObj;

	public static void getAllMapValues()  {
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<LumenQueryDetails, LumenQueryDetailsDTO> classMap = mapperFactory.classMap(LumenQueryDetails.class, LumenQueryDetailsDTO.class);
		classMap.field("lumenRequest", "lumenRequest");
		classMap.field("lumenQuery", "lumenQuery");
		classMap.field("queryRemarks", "queryRemarks");
		classMap.field("replyRemarks", "replyRemarks");
		classMap.field("repliedBy", "repliedBy");
		classMap.field("repliedDate","repliedDate");
		classMap.field("status", "status");
		classMap.field("stage", "stage");
//		classMap.field("createdBy", "createdBy");
//		classMap.field("createdDate", "createdDate");	
		classMap.field("key", "queryDetailsKey");
		classMap.field("repliedRole", "replyRaisedRole");
		classMap.register();
		tableMapper = mapperFactory.getMapperFacade();
	}

	public static List<LumenQueryDetailsDTO> getLumenQueryDetails(List<LumenQueryDetails> lumenRequestList) {
		List<LumenQueryDetailsDTO> mapAsList = tableMapper.mapAsList(lumenRequestList, LumenQueryDetailsDTO.class);
		return mapAsList;
	}

	public static LumenQueryDetailsMapper getInstance(){
		if(myObj == null){
			myObj = new LumenQueryDetailsMapper();
			getAllMapValues();
		}
		return myObj;
	}
}

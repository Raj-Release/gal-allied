package com.shaic.claim.lumen;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.LumenQuery;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class LumenQueryMapper {

	private static MapperFacade tableMapper;

	static LumenQueryMapper  myObj;

	public static void getAllMapValues()  {
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<LumenQuery, LumenQueryDTO> classMap = mapperFactory.classMap(LumenQuery.class, LumenQueryDTO.class);
		classMap.field("lumenRequest", "lumenRequest");
		classMap.field("queryType", "queryType");
		classMap.field("queryRaisedBy", "queryRaisedBy");
		classMap.field("query", "query");
		classMap.field("status", "status");
		classMap.field("stage", "stage");
		classMap.field("queryRaisedDate", "queryRaisedDate");
		classMap.field("key", "queryKey");
		classMap.field("queryRaisedRole", "queryRaisedRole");
		classMap.field("repliedDate", "repliedDate");		
		classMap.register();
		tableMapper = mapperFactory.getMapperFacade();
	}

	public static List<LumenQueryDTO> getLumenQueryDetails(List<LumenQuery> lumenRequestList) {
		List<LumenQueryDTO> mapAsList = tableMapper.mapAsList(lumenRequestList, LumenQueryDTO.class);
		return mapAsList;
	}

	public static LumenQueryMapper getInstance(){
		if(myObj == null){
			myObj = new LumenQueryMapper();
			getAllMapValues();
		}
		return myObj;
	}
}

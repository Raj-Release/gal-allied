package com.shaic.claim.lumen;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.lumen.components.MISSubDTO;
import com.shaic.domain.LumenQueryDetails;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class MISSubQueryDetailsMapper {

	private static MapperFacade tableMapper;

	static MISSubQueryDetailsMapper  myObj;

	public static void getAllMapValues()  {
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<LumenQueryDetails, MISSubDTO> classMap = mapperFactory.classMap(LumenQueryDetails.class, MISSubDTO.class);
		classMap.field("lumenRequest", "lumenRequest");
		classMap.field("lumenQuery", "lumenQuery");
		classMap.field("queryRemarks", "queryRemarks");
		classMap.field("replyRemarks", "replyRemarks");
		
		classMap.field("key", "lumenQueryDetailsKey");
		classMap.register();
		tableMapper = mapperFactory.getMapperFacade();
	}

	public static List<MISSubDTO> getLumenQueryDetails(List<LumenQueryDetails> lumenRequestList) {
		List<MISSubDTO> mapAsList = tableMapper.mapAsList(lumenRequestList, MISSubDTO.class);
		return mapAsList;
	}

	public static MISSubQueryDetailsMapper getInstance(){
		if(myObj == null){
			myObj = new MISSubQueryDetailsMapper();
			getAllMapValues();
		}
		return myObj;
	}
}

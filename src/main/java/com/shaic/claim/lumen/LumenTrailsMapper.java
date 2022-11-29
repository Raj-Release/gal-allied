package com.shaic.claim.lumen;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.LumenTrials;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class LumenTrailsMapper {

	private static MapperFacade tableMapper;

	static LumenTrailsMapper  myObj;

	public static void getAllMapValues()  {
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<LumenTrials, LumenTrialsDTO> classMap = mapperFactory.classMap(LumenTrials.class, LumenTrialsDTO.class);
		classMap.field("lumenRequest", "lumenRequest");
		classMap.field("intimation", "intimation");
		classMap.field("trialstatus", "trialstatus");
		classMap.field("statusRemarks", "statusRemarks");
		classMap.field("stage", "initiatedScreen");
		classMap.field("createdBy", "initiatedBy");
		classMap.field("createdDate", "initiatedDate");
		classMap.field("trialstatus", "response");
		classMap.field("statusRemarks", "comments");
		
		classMap.register();
		tableMapper = mapperFactory.getMapperFacade();
	}

	public static List<LumenTrialsDTO> getLumenTrialDetails(List<LumenTrials> lumenRequestList) {
		List<LumenTrialsDTO> mapAsList = tableMapper.mapAsList(lumenRequestList, LumenTrialsDTO.class);
		return mapAsList;
	}

	public static LumenTrailsMapper getInstance(){
		if(myObj == null){
			myObj = new LumenTrailsMapper();
			getAllMapValues();
		}
		return myObj;
	}
}

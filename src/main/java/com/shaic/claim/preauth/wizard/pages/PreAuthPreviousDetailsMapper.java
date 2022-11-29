package com.shaic.claim.preauth.wizard.pages;

import java.util.ArrayList;
import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.preauth.PreauthQuery;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class PreAuthPreviousDetailsMapper {
	
	private static MapperFacade tableMapper;
	
	static PreAuthPreviousDetailsMapper myObj;

	 public static void getAllMapValues()   {
		 
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<PreauthQuery, PreAuthPreviousQueryDetailsTableDTO> classMap = mapperFactory
				.classMap(PreauthQuery.class,
						PreAuthPreviousQueryDetailsTableDTO.class);
		classMap.field("key", "key");
		classMap.field("preauth.intimation.hospital", "hospitalName");
		classMap.field("preauth.intimation.hospital", "hospitalCity");
		classMap.field("preauth.preauthId", "diagnosis");
		classMap.field("queryRemarks", "queryRemarks");
		classMap.field("preauth", "preAuth");
		classMap.field("createdDate", "queryRaisedDate");
		classMap.field("createdBy", "queryRaisedRole");
		classMap.field("queryType.value", "opnQryTyp");
		
		classMap.field("status.processValue", "queryStatus");
		classMap.register();
		tableMapper = mapperFactory.getMapperFacade();
	}

	public static List<PreAuthPreviousQueryDetailsTableDTO> getSearchPEDQueryTableDTOTableDTO(
			List<PreauthQuery> previousQueryDetailsList) {
		if(previousQueryDetailsList != null && !previousQueryDetailsList.isEmpty()) {
			List<PreAuthPreviousQueryDetailsTableDTO> mapAsList = tableMapper.mapAsList(
					previousQueryDetailsList, PreAuthPreviousQueryDetailsTableDTO.class);
			return mapAsList;
		}
		return new ArrayList<PreAuthPreviousQueryDetailsTableDTO>();
	}
	
	
	public static PreAuthPreviousDetailsMapper getInstance(){
        if(myObj == null){
            myObj = new PreAuthPreviousDetailsMapper();
            getAllMapValues();
        }
        return myObj;
	 }

}

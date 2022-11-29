package com.shaic.claim.processtranslation.search;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.preauth.Preauth;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class SearchProcessTranslationMapper {
	
	private static MapperFacade tableMapper;
	
	static SearchProcessTranslationMapper  myObj;


	public static void getAllMapValues()  {
 
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Preauth, SearchProcessTranslationTableDTO> classMap = mapperFactory
				.classMap(Preauth.class,
						SearchProcessTranslationTableDTO.class);
		classMap.field("key", "key");
		classMap.field("intimation.intimationId", "intimationNo");
		classMap.field("claim.claimId", "claimNo");
		classMap.field("policy.policyNumber", "policyNo");
		classMap.field("intimation.insuredPatientName", "insuredPatientName");
		classMap.field("policy.lobId", "lob");
		classMap.field("policy.product.code","productCode");
		classMap.field("policy.product.value","productName");
		classMap.field("stage.key", "stageId");

		classMap.register();
		
		tableMapper = mapperFactory.getMapperFacade();
	}

	public static List<SearchProcessTranslationTableDTO> getSearchProcessTranslationTableDTO(
			List<Preauth> pedRequestQueryList) {
		List<SearchProcessTranslationTableDTO> mapAsList = tableMapper
				.mapAsList(pedRequestQueryList,
						SearchProcessTranslationTableDTO.class);
		return mapAsList;
	}
	
	
	public static SearchProcessTranslationMapper getInstance(){
        if(myObj == null){
            myObj = new SearchProcessTranslationMapper();
            getAllMapValues();
        }
        return myObj;
	 }

	
}

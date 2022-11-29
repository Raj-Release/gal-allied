package com.shaic.claim.processtranslation.search;
import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Claim;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class SearchProcessTranslationMapperR3 {
	
	private static MapperFacade tableMapper;
	
	static SearchProcessTranslationMapperR3  myObj;

	public static void getAllMapValues()  {
 
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Claim, SearchProcessTranslationTableDTO> classMap = mapperFactory
				.classMap(Claim.class,
						SearchProcessTranslationTableDTO.class);
		
		classMap.field("intimation.intimationId", "intimationNo");
		classMap.field("intimation.cpuCode.cpuCode", "cpuCode");
		classMap.field("intimation.cpuCode.description", "cpuCodeName");
		classMap.field("claimId", "claimNo");
		classMap.field("intimation.policy.policyNumber", "policyNo");
		classMap.field("intimation.insuredPatientName", "insuredPatientName");
		classMap.field("intimation.policy.lobId", "LOBID");
		classMap.field("intimation.policy.product.code","productCode");
		classMap.field("intimation.policy.product.value","productName");
		classMap.field("stage.key", "stageId");

		classMap.register();
		
		tableMapper = mapperFactory.getMapperFacade();
	}

	public static List<SearchProcessTranslationTableDTO> getSearchProcessTranslationTableDTO(
			List<Claim> pedRequestQueryList) {
		List<SearchProcessTranslationTableDTO> mapAsList = tableMapper
				.mapAsList(pedRequestQueryList,
						SearchProcessTranslationTableDTO.class);
		return mapAsList;
	}
	
	public static SearchProcessTranslationMapperR3 getInstance(){
        if(myObj == null){
            myObj = new SearchProcessTranslationMapperR3();
            getAllMapValues();
        }
        return myObj;
	 }

	
}

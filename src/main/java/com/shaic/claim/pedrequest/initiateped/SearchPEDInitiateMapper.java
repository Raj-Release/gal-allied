package com.shaic.claim.pedrequest.initiateped;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Claim;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class SearchPEDInitiateMapper {

	private static MapperFacade tableMapper;
	
	static SearchPEDInitiateMapper myObj;


	public static void getAllMapValues() {
		
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Claim, SearchPEDInitiateTableDTO> classMap = mapperFactory
				.classMap(Claim.class,
						SearchPEDInitiateTableDTO.class);
		classMap.field("key", "key");
		classMap.field("intimation.policy.policyNumber", "policyNo");
		classMap.field("intimation.policy.proposerCode", "proposerCode");
		classMap.field("intimation.policy.proposerFirstName", "proposerName");
		classMap.field("intimation.policy.productName", "productName");
		classMap.field("intimation.policy.totalPremium", "premium");
		classMap.field("intimation.policy.key", "policyKey");
		classMap.field("intimation.policy.homeOfficeCode", "policyIssuingOfficeName");
		
		classMap.register();
		tableMapper = mapperFactory.getMapperFacade();
	}

	public static List<SearchPEDInitiateTableDTO> getSearchPEDRequestProcessTableDTO(
			List<Claim> pedRequestQueryList) {
		List<SearchPEDInitiateTableDTO> mapAsList = tableMapper
				.mapAsList(pedRequestQueryList,
						SearchPEDInitiateTableDTO.class);
		return mapAsList;
	}
	
	public static SearchPEDInitiateMapper getInstance(){
        if(myObj == null){
            myObj = new SearchPEDInitiateMapper();
            getAllMapValues();
        }
        return myObj;
	 }


	
}

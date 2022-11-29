package com.shaic.claim.legal.processconsumerforum.page.consumerforum;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Claim;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class SearchConsumerForumMapper {
	
	private static MapperFacade tableMapper;
	
	static SearchConsumerForumMapper  myObj;


	public static void getAllMapValues()  {
 
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Claim, SearchProcessConsumerForumTableDTO> classMap = mapperFactory
				.classMap(Claim.class, SearchProcessConsumerForumTableDTO.class);		
		
		classMap.field("intimation.intimationId", "intimationNumber");
		classMap.field("claimType.value", "claimType");
		classMap.field("intimation.cpuCode.cpuCode", "cpuName");
		classMap.field("intimation.policy.policyNumber", "policyNumber");
		classMap.field("intimation.policy.homeOfficeCode", "policyIssuingOffice");
		classMap.field("intimation.policy.productName", "productName");
		classMap.field("intimation.policy.product.productType", "productType");
		classMap.field("intimation.lobId.value", "classOfBussiness");
		classMap.register();
		
		tableMapper = mapperFactory.getMapperFacade();
	}

	public static List<SearchProcessConsumerForumTableDTO> getViewClaimHistoryDTO(
			List<Claim> claim ) {
		List<SearchProcessConsumerForumTableDTO> mapAsList = tableMapper.mapAsList(
				claim, SearchProcessConsumerForumTableDTO.class);
		return mapAsList;
	}
	
	public static SearchConsumerForumMapper getInstance(){
        if(myObj == null){
            myObj = new SearchConsumerForumMapper();
            getAllMapValues();
        }
        return myObj;
	 }


}

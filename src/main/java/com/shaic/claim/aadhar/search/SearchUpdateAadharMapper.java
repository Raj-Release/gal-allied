package com.shaic.claim.aadhar.search;

import java.util.List;

import com.shaic.domain.Claim;
import com.shaic.newcode.wizard.domain.MappingUtil;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

public class SearchUpdateAadharMapper {
	
	private static MapperFacade tableMapper;
	
	static SearchUpdateAadharMapper myObj;
	
	public static void getAllMapValues()  {
		 
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Claim, SearchUpdateAadharTableDTO> intimationClassMap = mapperFactor.classMap(Claim.class, SearchUpdateAadharTableDTO.class);
		
		intimationClassMap.field("intimation.key", "key");
		intimationClassMap.field("intimation.intimationId", "intimationNo");
		intimationClassMap.field("intimation.policy.policyNumber", "policyNo");
		intimationClassMap.field("intimation.insured.key", "insuredKey");
		intimationClassMap.field("intimation.insured.insuredName", "insuredName");
		intimationClassMap.field("intimation.policy.policyFromDate", "policyStartDate");
		intimationClassMap.field("intimation.policy.policyToDate", "policyEndDate");
		intimationClassMap.field("intimation.insured.aadharNo", "aadharRefNo");
		intimationClassMap.register();
		tableMapper = mapperFactor.getMapperFacade();
	}
	
	public static List<SearchUpdateAadharTableDTO> getIntimationDTO(List<Claim> claimData){
		List<SearchUpdateAadharTableDTO> mapAsList = 
										tableMapper.mapAsList(claimData, SearchUpdateAadharTableDTO.class);
		return mapAsList;
		
	}
	
	public static SearchUpdateAadharMapper getInstance(){
        if(myObj == null){
            myObj = new SearchUpdateAadharMapper();
            getAllMapValues();
        }
        return myObj;
	 }

}

package com.shaic.claim.pancard.search.pages;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Claim;
import com.shaic.newcode.wizard.domain.MappingUtil;


/**
 * @author ntv.narenj
 *
 */
public class SearchUploadPanCardMapper {
	private static MapperFacade tableMapper;
	
	static SearchUploadPanCardMapper  myObj;

	public static void getAllMapValues()  {
 
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Claim, SearchUploadPanCardTableDTO> intimationClassMap = mapperFactor.classMap(Claim.class, SearchUploadPanCardTableDTO.class);
		
		intimationClassMap.field("intimation.key", "key");
		intimationClassMap.field("intimation.intimationId", "intimationNo");
		intimationClassMap.field("intimation.policy.policyNumber", "policyNo");
		intimationClassMap.field("intimation.policy.proposerFirstName", "proposerName");
		intimationClassMap.field("intimation.policy.policyFromDate", "policyStartDate");
		intimationClassMap.field("intimation.policy.policyToDate", "policyEndDate");
		intimationClassMap.register();
		tableMapper = mapperFactor.getMapperFacade();
	}
	
	//public static List<SearchUploadPanCardTableDTO> getIntimationDTO(List<Intimation> intimationData){
	public static List<SearchUploadPanCardTableDTO> getIntimationDTO(List<Claim> claimData){
		List<SearchUploadPanCardTableDTO> mapAsList = 
										tableMapper.mapAsList(claimData, SearchUploadPanCardTableDTO.class);
		return mapAsList;
		
	}
	
	public static SearchUploadPanCardMapper getInstance(){
        if(myObj == null){
            myObj = new SearchUploadPanCardMapper();
            getAllMapValues();
        }
        return myObj;
	 }
	
	
}

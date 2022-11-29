package com.shaic.claim.OMPprocessrejection.search;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.processrejection.search.SearchProcessRejectionTableDTO;
import com.shaic.domain.OMPClaim;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class SearchOMPProcessRejectionMapper {
	
private static MapperFacade tableMapper;

static SearchOMPProcessRejectionMapper  myObj;

	
public static void getAllMapValues()  {
	
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
//		ClassMapBuilder<OMPIntimation, SearchProcessRejectionTableDTO> classMapForIntimation = mapperFactory.classMap(OMPIntimation.class, SearchProcessRejectionTableDTO.class);
		ClassMapBuilder<OMPClaim, SearchProcessRejectionTableDTO> classMapForClaim = mapperFactory.classMap(OMPClaim.class, SearchProcessRejectionTableDTO.class);
		
		classMapForClaim.field("key", "key");
		classMapForClaim.field("intimation.intimationId", "intimationNo");
		classMapForClaim.field("intimation.policy.policyNumber", "policyNo");
		classMapForClaim.field("ailmentLoss","ailmentLoss");
		classMapForClaim.field("intimation.createdDate","intimationDate");
		classMapForClaim.field("intimation.admissionDate","claimDto.admissionDate");
		classMapForClaim.field("intimation.insured.insuredName","patientName");
		classMapForClaim.field("event.eventCode","eventCodeDescription");
		
//		classMapForIntimation.field("key", "key");
//		classMapForIntimation.field("intimationId","intimationNo");
//		classMapForIntimation.field("createdDate","intimationDate");
//		classMapForIntimation.field("hospitalType","hospitalType");
//		classMapForIntimation.field("status.processValue","status");
//		classMapForIntimation.register();
		
		classMapForClaim.register();
		tableMapper = mapperFactory.getMapperFacade();
	}

//	@SuppressWarnings("unused")
//	public static  List<SearchProcessRejectionTableDTO> getSearchProcessRejectionTableDTOForIntimation(List<OMPIntimation> searchProcessRejectionListForIntimation)
//	{
//		List<SearchProcessRejectionTableDTO> list = new ArrayList<SearchProcessRejectionTableDTO>();
//		List<SearchProcessRejectionTableDTO> tableDTO = new ArrayList<SearchProcessRejectionTableDTO>();
//		List<SearchProcessRejectionTableDTO> mapAsList = tableMapper.mapAsList(searchProcessRejectionListForIntimation, SearchProcessRejectionTableDTO.class);
//		return mapAsList;
//	}
	
	public List<SearchProcessRejectionTableDTO> getSearchProcessRejectionTableDTOForClaim(List<OMPClaim> searchProcessRejectionListForClaim)
	{
		List<SearchProcessRejectionTableDTO> mapAsList = tableMapper.mapAsList(searchProcessRejectionListForClaim, SearchProcessRejectionTableDTO.class);

		return mapAsList;
	}
	
	public static SearchOMPProcessRejectionMapper getInstance(){
        if(myObj == null){
            myObj = new SearchOMPProcessRejectionMapper();
            getAllMapValues();
        }
        return myObj;
	 }
	
}

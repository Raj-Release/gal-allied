package com.shaic.claim.fieldVisitPage;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.preauth.TmpFvR;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class SearchRepresentativeMapper {
	
	private static MapperFacade tableMapper;
	
	static SearchRepresentativeMapper  myObj;


	public static void getAllMapValues()  {
 
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<TmpFvR, SearchRepresentativeTableDTO> classMap = mapperFactory
				.classMap(TmpFvR.class, SearchRepresentativeTableDTO.class);		
		
		classMap.field("representiveName", "representativeName");
		classMap.field("phoneNumber", "representativeContactNo");
		classMap.field("mobileNumber", "repsentativeMobileNo");		
		classMap.field("allocationTo.value", "representativeCategory");
		classMap.field("totalAllocation", "totalAllocation");
		classMap.field("allocated", "allocated");
		classMap.field("canBeAllocated", "canByAllocated");
		classMap.field("representiveCode", "representativeCode");
		classMap.register();
		
		tableMapper = mapperFactory.getMapperFacade();
	}

	public static List<SearchRepresentativeTableDTO> getViewClaimHistoryDTO(
			List<TmpFvR> tmpFvRList) {
		List<SearchRepresentativeTableDTO> mapAsList = tableMapper.mapAsList(
				tmpFvRList, SearchRepresentativeTableDTO.class);
		return mapAsList;
	}
	
	public static SearchRepresentativeMapper getInstance(){
        if(myObj == null){
            myObj = new SearchRepresentativeMapper();
            getAllMapValues();
        }
        return myObj;
	 }


}

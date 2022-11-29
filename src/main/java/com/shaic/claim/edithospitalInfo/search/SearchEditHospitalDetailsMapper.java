package com.shaic.claim.edithospitalInfo.search;

import java.util.ArrayList;
import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Intimation;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class SearchEditHospitalDetailsMapper {
	
private static MapperFacade tableMapper;

static SearchEditHospitalDetailsMapper myObj;
	
	
public static void getAllMapValues()  {

	MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Intimation, SearchEditHospitalDetailsTableDTO> classMap = mapperFactory.classMap(Intimation.class, SearchEditHospitalDetailsTableDTO.class);
		classMap.field("key", "key");
		classMap.field("intimationId","intimationNo");
		classMap.field("createdDate","intimationDate");
		classMap.field("hospitalType","hospitalType");
		classMap.field("policyNumber","policyNo");
		classMap.field("status.processValue","status");
		classMap.register();
		tableMapper = mapperFactory.getMapperFacade();
	}

	@SuppressWarnings("unused")
	public static  List<SearchEditHospitalDetailsTableDTO> getSearchEditHospitalDetailsTableDTO(List<Intimation> searchEditHospitalInformationList)
	{
		List<SearchEditHospitalDetailsTableDTO> tableDTO = new ArrayList<SearchEditHospitalDetailsTableDTO>();
		List<SearchEditHospitalDetailsTableDTO> mapAsList = tableMapper.mapAsList(searchEditHospitalInformationList, SearchEditHospitalDetailsTableDTO.class);
		return mapAsList;
	}
	
	public static SearchEditHospitalDetailsMapper getInstance(){
        if(myObj == null){
            myObj = new SearchEditHospitalDetailsMapper();
            getAllMapValues();
        }
        return myObj;
	 }
	
}

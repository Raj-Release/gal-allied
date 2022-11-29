package com.shaic.claim.adviseonped.search;

import java.util.ArrayList;
import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Hospitals;
import com.shaic.domain.preauth.OldInitiatePedEndorsement;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class SearchAdviseOnPEDMapper {
	private static MapperFacade tableMapper;
	
	static SearchAdviseOnPEDMapper myObj;

	 public static void getAllMapValues()    {
		 
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<OldInitiatePedEndorsement, SearchAdviseOnPEDTableDTO> classMap = mapperFactory
				.classMap(OldInitiatePedEndorsement.class,
						SearchAdviseOnPEDTableDTO.class);
		ClassMapBuilder<Hospitals, SearchAdviseOnPEDTableDTO> classHospMap = mapperFactory.classMap(Hospitals.class, SearchAdviseOnPEDTableDTO.class);
		classMap.field("key", "key");
		classMap.field("intimation.intimationId", "intimationNo");
		classMap.field("policy.policyNumber", "policyNo");
		//classMap.field("policy.insuredFirstName", "insuredPatientName");
		classMap.field("intimation.insuredPatientName", "insuredPatientName");
		classMap.field("intimation.cpuCode.cpuCode", "cpuCode");
		classMap.field("pedSuggestion", "pedSuggestion");
		classMap.field("status.processValue","pedStatus");
		classMap.field("claim.claimId", "claimNo");
		//classMap.field("intimation.hospitalType.key", "hospitalTypeId");
		classMap.field("intimation.hospital", "hospitalTypeId");
		//Hospital info map -- starts
		classHospMap.field("name","hospitalName");
		classHospMap.field("city","hospitalCity");
		classHospMap.field("key", "key");
		classHospMap.field("address", "hospitalAddress");
		
/*		classHospMap.field("doorApartmentNumber","doorApartmentNumber");
		classHospMap.field("plotGateNumber","plotGateNumber");
		classHospMap.field("buildingName","buildingName");
		classHospMap.field("streetName","streetName");
		classHospMap.field("locality","locality");
	
		classHospMap.field("pincode","pinCode");
		classHospMap.field("state","state");
		classHospMap.field("country","country");*/
		//Hospital info map -- ends
		
		
		classMap.register();
		classHospMap.register();
		tableMapper = mapperFactory.getMapperFacade();
	}

	public static List<SearchAdviseOnPEDTableDTO> getSearchAdviseOnPEDTableDTO(
			List<OldInitiatePedEndorsement> adviseOnPedList) {
		List<SearchAdviseOnPEDTableDTO> mapAsList = tableMapper
				.mapAsList(adviseOnPedList,
						SearchAdviseOnPEDTableDTO.class);
		return mapAsList;
	}
	

	@SuppressWarnings("unused")
	public static  List<SearchAdviseOnPEDTableDTO> getHospitalInfoList(List<Hospitals> hospitalInfoList)
	{
		List<SearchAdviseOnPEDTableDTO> tableDTO = new ArrayList<SearchAdviseOnPEDTableDTO>();
		List<SearchAdviseOnPEDTableDTO> mapAsList = tableMapper.mapAsList(hospitalInfoList, SearchAdviseOnPEDTableDTO.class);
		return mapAsList;
	}
	
	public static SearchAdviseOnPEDMapper getInstance(){
        if(myObj == null){
            myObj = new SearchAdviseOnPEDMapper();
            getAllMapValues();
        }
        return myObj;
	 }
}

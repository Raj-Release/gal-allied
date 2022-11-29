package com.shaic.claim.pedrequest.process.search;

import java.util.ArrayList;
import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Hospitals;
import com.shaic.domain.preauth.OldInitiatePedEndorsement;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class SearchPEDRequestProcessMapper {

	private static MapperFacade tableMapper;
	
	static SearchPEDRequestProcessMapper myObj;


	public static void getAllMapValues()  {
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<OldInitiatePedEndorsement, SearchPEDRequestProcessTableDTO> classMap = mapperFactory
				.classMap(OldInitiatePedEndorsement.class,
						SearchPEDRequestProcessTableDTO.class);
		ClassMapBuilder<Hospitals, SearchPEDRequestProcessTableDTO> classHospMap = mapperFactory.classMap(Hospitals.class, SearchPEDRequestProcessTableDTO.class);
		classMap.field("key", "key");
		classMap.field("intimation.intimationId", "intimationNo");
		classMap.field("policy.policyNumber", "policyNo");
		//classMap.field("policy.insuredFirstName", "insuredPatientName");
		classMap.field("intimation.insuredPatientName", "insuredPatientName");
		classMap.field("intimation.cpuCode.cpuCode", "cpuCode");
		classMap.field("pedSuggestion", "pedSuggestion");
		classMap.field("status.processValue","pedStatus");
		classMap.field("claim.claimId", "claimNo");
		classMap.field("createdDate", "pedInitiated");
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

	public static List<SearchPEDRequestProcessTableDTO> getSearchPEDRequestProcessTableDTO(
			List<OldInitiatePedEndorsement> pedRequestQueryList) {
		List<SearchPEDRequestProcessTableDTO> mapAsList = tableMapper
				.mapAsList(pedRequestQueryList,
						SearchPEDRequestProcessTableDTO.class);
		return mapAsList;
	}
	
	
	@SuppressWarnings("unused")
	public static  List<SearchPEDRequestProcessTableDTO> getHospitalInfoList(List<Hospitals> hospitalInfoList)
	{
		List<SearchPEDRequestProcessTableDTO> tableDTO = new ArrayList<SearchPEDRequestProcessTableDTO>();
		List<SearchPEDRequestProcessTableDTO> mapAsList = tableMapper.mapAsList(hospitalInfoList, SearchPEDRequestProcessTableDTO.class);
		return mapAsList;
	}
	
	public static SearchPEDRequestProcessMapper getInstance(){
        if(myObj == null){
            myObj = new SearchPEDRequestProcessMapper();
            getAllMapValues();
        }
        return myObj;
	 }

	
}

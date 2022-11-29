package com.shaic.claim.pedrequest.approve.search;

import java.util.ArrayList;
import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Hospitals;
import com.shaic.domain.preauth.OldInitiatePedEndorsement;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class SearchPEDRequestApproveMapper {
	private static MapperFacade tableMapper;
	
	static SearchPEDRequestApproveMapper myObj;


	public static void getAllMapValues() {
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<OldInitiatePedEndorsement, SearchPEDRequestApproveTableDTO> classMap = mapperFactory
				.classMap(OldInitiatePedEndorsement.class,
						SearchPEDRequestApproveTableDTO.class);
		ClassMapBuilder<Hospitals, SearchPEDRequestApproveTableDTO> classHospMap = mapperFactory.classMap(Hospitals.class, SearchPEDRequestApproveTableDTO.class);
		classMap.field("key", "key");
		classMap.field("intimation.intimationId", "intimationNo");
		classMap.field("policy.policyNumber", "policyNo");
		//classMap.field("policy.insuredFirstName", "insuredPatientName");
		classMap.field("intimation.insuredPatientName", "insuredPatientName");
		classMap.field("intimation.cpuCode.cpuCode", "cpuCode");
		classMap.field("pedSuggestion", "pedSuggestion");
		classMap.field("createdDate", "pedInitiated");
		classMap.field("status.processValue","pedStatus");
		classMap.field("claim.claimId", "claimNo");
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

	public static List<SearchPEDRequestApproveTableDTO> getSearchPEDRequestApproveTableDTO(
			List<OldInitiatePedEndorsement> pedRequestQueryList) {
		List<SearchPEDRequestApproveTableDTO> mapAsList = tableMapper
				.mapAsList(pedRequestQueryList,
						SearchPEDRequestApproveTableDTO.class);
		return mapAsList;
	}
	
	@SuppressWarnings("unused")
	public static  List<SearchPEDRequestApproveTableDTO> getHospitalInfoList(List<Hospitals> hospitalInfoList)
	{
		List<SearchPEDRequestApproveTableDTO> tableDTO = new ArrayList<SearchPEDRequestApproveTableDTO>();
		List<SearchPEDRequestApproveTableDTO> mapAsList = tableMapper.mapAsList(hospitalInfoList, SearchPEDRequestApproveTableDTO.class);
		return mapAsList;
	}
	
	public static SearchPEDRequestApproveMapper getInstance(){
        if(myObj == null){
            myObj = new SearchPEDRequestApproveMapper();
            getAllMapValues();
        }
        return myObj;
	 }

	
}

package com.shaic.claim.pedrequest.approve.bancspedQuery;

import java.util.ArrayList;
import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Hospitals;
import com.shaic.domain.preauth.OldInitiatePedEndorsement;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class BancsSearchPEDRequestApproveMapper {
	private static MapperFacade tableMapper;
	
	static BancsSearchPEDRequestApproveMapper myObj;


	public static void getAllMapValues() {
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<OldInitiatePedEndorsement, BancsSearchPEDRequestApproveTableDTO> classMap = mapperFactory
				.classMap(OldInitiatePedEndorsement.class,
						BancsSearchPEDRequestApproveTableDTO.class);
		ClassMapBuilder<Hospitals, BancsSearchPEDRequestApproveTableDTO> classHospMap = mapperFactory.classMap(Hospitals.class, BancsSearchPEDRequestApproveTableDTO.class);
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

	public static List<BancsSearchPEDRequestApproveTableDTO> getSearchPEDRequestApproveTableDTO(
			List<OldInitiatePedEndorsement> pedRequestQueryList) {
		List<BancsSearchPEDRequestApproveTableDTO> mapAsList = tableMapper
				.mapAsList(pedRequestQueryList,
						BancsSearchPEDRequestApproveTableDTO.class);
		return mapAsList;
	}
	
	@SuppressWarnings("unused")
	public static  List<BancsSearchPEDRequestApproveTableDTO> getHospitalInfoList(List<Hospitals> hospitalInfoList)
	{
		List<BancsSearchPEDRequestApproveTableDTO> tableDTO = new ArrayList<BancsSearchPEDRequestApproveTableDTO>();
		List<BancsSearchPEDRequestApproveTableDTO> mapAsList = tableMapper.mapAsList(hospitalInfoList, BancsSearchPEDRequestApproveTableDTO.class);
		return mapAsList;
	}
	
	public static BancsSearchPEDRequestApproveMapper getInstance(){
        if(myObj == null){
            myObj = new BancsSearchPEDRequestApproveMapper();
            getAllMapValues();
        }
        return myObj;
	 }

	
}

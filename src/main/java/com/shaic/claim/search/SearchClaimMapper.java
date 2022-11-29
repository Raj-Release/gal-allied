package com.shaic.claim.search;

import java.util.ArrayList;
import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Hospitals;
import com.shaic.domain.ViewTmpClaim;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class SearchClaimMapper {
	
	private static MapperFacade tableMapper;
	
	static SearchClaimMapper myObj;


	public static void getAllMapValues()  {
 
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<ViewTmpClaim, SearchClaimTableDTO> classMap = mapperFactory
				.classMap(ViewTmpClaim.class,
						SearchClaimTableDTO.class);
		ClassMapBuilder<Hospitals, SearchClaimTableDTO> classHospMap = mapperFactory.classMap(Hospitals.class, SearchClaimTableDTO.class);
		classMap.field("key", "key");
		classMap.field("intimation.intimationId", "intimationNo");
		classMap.field("claimId", "claimNo");
		classMap.field("intimation.policy.policyNumber", "policyNo");
		//classMap.field("policy.insuredFirstName", "insuredPatientName");
		classMap.field("intimation.insuredPatientName", "insuredPatientName");
		classMap.field("intimation.cpuCode.cpuCode", "cpuCode");
		classMap.field("intimation.admissionReason", "reasonforAdmission");
		classMap.field("status.processValue","claimStatus");
		classMap.field("claimType.key", "claimTypeId");
		
		//classMap.field("intimation.hospitalType.key", "hospitalTypeId");
		classMap.field("intimation.hospital", "hospitalTypeId");
		
		//Hospital info map -- starts
		classHospMap.field("name","hospitalName");
		classHospMap.field("city","hospitalCity");
		classHospMap.field("key", "key");
		
		classMap.register();
		classHospMap.register();
		tableMapper = mapperFactory.getMapperFacade();
	}

	public static List<SearchClaimTableDTO> getSearchClaimDTO(
			List<ViewTmpClaim> searchClaimTableList) {
		List<SearchClaimTableDTO> mapAsList = tableMapper
				.mapAsList(searchClaimTableList,
						SearchClaimTableDTO.class);
		return mapAsList;
	}
	

	@SuppressWarnings("unused")
	public static  List<SearchClaimTableDTO> getHospitalInfoList(List<Hospitals> hospitalInfoList)
	{
		List<SearchClaimTableDTO> tableDTO = new ArrayList<SearchClaimTableDTO>();
		List<SearchClaimTableDTO> mapAsList = tableMapper.mapAsList(hospitalInfoList, SearchClaimTableDTO.class);
		return mapAsList;
	}
	
	public static SearchClaimMapper getInstance(){
        if(myObj == null){
            myObj = new SearchClaimMapper();
            getAllMapValues();
        }
        return myObj;
	 }

	
}

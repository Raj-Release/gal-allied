package com.shaic.claim.registration.ackhoscomm.search;

import java.util.ArrayList;
import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Hospitals;
import com.shaic.domain.preauth.Preauth;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class SearchAcknowledgeHospitalCommunicationMapper {

	private static MapperFacade tableMapper;
	
	static SearchAcknowledgeHospitalCommunicationMapper myObj;

	 public static void getAllMapValues()   {
		 
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Preauth, SearchAcknowledgeHospitalCommunicationTableDTO> classMap = mapperFactory.classMap(Preauth.class,SearchAcknowledgeHospitalCommunicationTableDTO.class);
		ClassMapBuilder<Hospitals, SearchAcknowledgeHospitalCommunicationTableDTO> classHospMap = mapperFactory.classMap(Hospitals.class, SearchAcknowledgeHospitalCommunicationTableDTO.class);

		classMap.field("key", "key");
		classMap.field("policy.lobId", "lob");
		classMap.field("claim.claimType.value", "claimType");
		classMap.field("intimation.cpuCode.cpuCode", "cpuCode");
		classMap.field("claim.claimId", "claimNo");
		classMap.field("intimation.insuredPatientName", "insuredPatientName");
		//classMap.field("", "hospitalName");
		classMap.field("intimation.admissionDate", "dateofAdmission");
		classMap.field("claim.status.processValue", "claimStatus");
		classMap.field("remarks", "remarks");
		classMap.field("intimation.hospital", "hospitalTypeId");		
		classHospMap.field("name", "hospitalName");
		classHospMap.field("key", "key");
		
		classMap.register();
		classHospMap.register();
		tableMapper = mapperFactory.getMapperFacade();
	}

	public static List<SearchAcknowledgeHospitalCommunicationTableDTO> getSearchAcknowledgeHospitalCommunicationTableDTO(
			List<Preauth> hospitalAcknowledgeList) {
		List<SearchAcknowledgeHospitalCommunicationTableDTO> mapAsList = tableMapper
				.mapAsList(hospitalAcknowledgeList,
						SearchAcknowledgeHospitalCommunicationTableDTO.class);
		return mapAsList;
	}
	
	@SuppressWarnings("unused")
	public static  List<SearchAcknowledgeHospitalCommunicationTableDTO> getHospitalInfoList(List<Hospitals> hospitalInfoList)
	{
		List<SearchAcknowledgeHospitalCommunicationTableDTO> tableDTO = new ArrayList<SearchAcknowledgeHospitalCommunicationTableDTO>();
		List<SearchAcknowledgeHospitalCommunicationTableDTO> mapAsList = tableMapper.mapAsList(hospitalInfoList, SearchAcknowledgeHospitalCommunicationTableDTO.class);
		return mapAsList;
	}
	
	
	public static SearchAcknowledgeHospitalCommunicationMapper getInstance(){
        if(myObj == null){
            myObj = new SearchAcknowledgeHospitalCommunicationMapper();
            getAllMapValues();
        }
        return myObj;
	 }

	
}

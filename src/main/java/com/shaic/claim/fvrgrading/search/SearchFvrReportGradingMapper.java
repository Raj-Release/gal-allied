package com.shaic.claim.fvrgrading.search;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class SearchFvrReportGradingMapper {

	private static MapperFacade tableMapper;

	static SearchFvrReportGradingMapper myObj;

	public static void getAllMapValues() {

		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<FieldVisitRequest, SearchFvrReportGradingTableDto> classMap = mapperFactory
				.classMap(FieldVisitRequest.class, SearchFvrReportGradingTableDto.class);
		/*ClassMapBuilder<Hospitals, SearchFvrReportGradingTableDto> classHospMap = mapperFactory
				.classMap(Hospitals.class, SearchFvrReportGradingTableDto.class);*/
		classMap.field("key", "key");
		classMap.field("policy.policyNumber","policyNo");
		classMap.field("intimation.intimationId","intimationNo");
		classMap.field("policy.lobId", "lob");
		classMap.field("intimation.cpuCode.cpuCode", "cpuCode");
		classMap.field("intimation.insuredPatientName", "insuredPatientName");
		classMap.field("intimation.admissionReason", "admissionReason");
		classMap.field("policy.productName", "product");
		classMap.field("intimation.hospital", "hospitalId");
		classMap.field("transactionKey", "rodKey");
		classMap.register();
		
		/*classHospMap.field("name", "hospitalName");
		classHospMap.field("city","hospCity");
		classHospMap.field("key", "key");
		classHospMap.register();*/
		tableMapper = mapperFactory.getMapperFacade();
	}

	public static List<SearchFvrReportGradingTableDto> getSearchFvrReportGradingTableDTO(
			List<FieldVisitRequest> claimInfoList) {
		List<SearchFvrReportGradingTableDto> mapAsList = tableMapper.mapAsList(
				claimInfoList, SearchFvrReportGradingTableDto.class);
		return mapAsList;
	}

	/*@SuppressWarnings("unused")
	public static List<SearchFvrReportGradingTableDto> getHospitalInfoList(
			List<Hospitals> hospitalInfoList) {
		List<SearchFvrReportGradingTableDto> tableDTO = new ArrayList<SearchFvrReportGradingTableDto>();
		List<SearchFvrReportGradingTableDto> mapAsList = tableMapper.mapAsList(
				hospitalInfoList, SearchFvrReportGradingTableDto.class);
		return mapAsList;
	}*/

	public static SearchFvrReportGradingMapper getInstance() {
		if (myObj == null) {
			myObj = new SearchFvrReportGradingMapper();
			getAllMapValues();
		}
		return myObj;
	}

}

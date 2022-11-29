package com.shaic.claim.registration.convertClaim.search;

import java.util.ArrayList;
import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Claim;
import com.shaic.domain.Hospitals;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class SearchConvertClaimMapper {

	private static MapperFacade tableMapper;

	static SearchConvertClaimMapper myObj;

	public static void getAllMapValues() {

		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Claim, SearchConvertClaimTableDto> classMap = mapperFactory
				.classMap(Claim.class, SearchConvertClaimTableDto.class);
		ClassMapBuilder<Hospitals, SearchConvertClaimTableDto> classHospMap = mapperFactory
				.classMap(Hospitals.class, SearchConvertClaimTableDto.class);
		classMap.field("key", "key");
		classMap.field("intimation.policy.policyNumber","policyNo");
		classMap.field("intimation.intimationId","intimationNo");
		classMap.field("intimation.key","intimationKey");
		classMap.field("intimation.policy.lobId", "lob");
		classMap.field("claimType.value", "claimType");
		classMap.field("intimation.cpuCode.cpuCode", "cpuCode");
		classMap.field("claimId", "claimNumber");
		classMap.field("intimation.insuredPatientName", "insuredPatientName");
		classMap.field("intimation.hospital", "hospitalTypeId");
		classMap.field("intimation.key", "intimationkey");
		// classMap.field("","hospitalName");
		classMap.field("intimation.admissionDate", "dateOfAdmission1");
		classMap.field("status.processValue", "claimStatus");
		classMap.field("incidenceFlag","accDeath"); 
		classHospMap.field("name", "hospitalName");
		classHospMap.field("hospitalType.value","hospitalType");
		classHospMap.field("key", "key");
		classMap.register();
		classHospMap.register();
		tableMapper = mapperFactory.getMapperFacade();
	}

	public static List<SearchConvertClaimTableDto> getSearchConvertClaimTableDTO(
			List<Claim> claimInfoList) {
		List<SearchConvertClaimTableDto> mapAsList = tableMapper.mapAsList(
				claimInfoList, SearchConvertClaimTableDto.class);
		return mapAsList;
	}

	@SuppressWarnings("unused")
	public static List<SearchConvertClaimTableDto> getHospitalInfoList(
			List<Hospitals> hospitalInfoList) {
		List<SearchConvertClaimTableDto> tableDTO = new ArrayList<SearchConvertClaimTableDto>();
		List<SearchConvertClaimTableDto> mapAsList = tableMapper.mapAsList(
				hospitalInfoList, SearchConvertClaimTableDto.class);
		return mapAsList;
	}

	public static SearchConvertClaimMapper getInstance() {
		if (myObj == null) {
			myObj = new SearchConvertClaimMapper();
			getAllMapValues();
		}
		return myObj;
	}

}

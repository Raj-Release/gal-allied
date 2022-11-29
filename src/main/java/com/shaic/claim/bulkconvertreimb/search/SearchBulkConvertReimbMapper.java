package com.shaic.claim.bulkconvertreimb.search;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Claim;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class SearchBulkConvertReimbMapper {

	private static MapperFacade tableMapper;

	static SearchBulkConvertReimbMapper myObj;

	public static void getAllMapValues() {

		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Claim, SearchBulkConvertReimbTableDto> classMap = mapperFactory
				.classMap(Claim.class, SearchBulkConvertReimbTableDto.class);
		classMap.field("key", "key");
		classMap.field("key", "claimKey");
		classMap.field("claimType.value", "claimType");
		classMap.field("intimation.cpuCode.cpuCode", "cpuCode");
		classMap.field("intimation.intimationId","intimationNumber");
		classMap.field("intimation.createdDate","intimatedDate");
		classMap.field("intimation.key","intimationKey");		
		classMap.field("claimId", "claimNumber");
		classMap.field("status.processValue", "claimStatus");
		classMap.register();
		tableMapper = mapperFactory.getMapperFacade();
	}

	public static List<SearchBulkConvertReimbTableDto> getSearchConvertClaimTableDTO(
			List<Claim> claimInfoList) {
		List<SearchBulkConvertReimbTableDto> mapAsList = tableMapper.mapAsList(
				claimInfoList, SearchBulkConvertReimbTableDto.class);
		return mapAsList;
	}

	public static SearchBulkConvertReimbMapper getInstance() {
		if (myObj == null) {
			myObj = new SearchBulkConvertReimbMapper();
			getAllMapValues();
		}
		return myObj;
	}

}

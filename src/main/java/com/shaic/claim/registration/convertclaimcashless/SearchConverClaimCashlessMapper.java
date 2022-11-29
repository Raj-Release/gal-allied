package com.shaic.claim.registration.convertclaimcashless;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Claim;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class SearchConverClaimCashlessMapper {

	private static MapperFacade tableMapper;
	
	static SearchConverClaimCashlessMapper myObj;

	public static void getAllMapValues()  {

		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Claim, SearchConverClaimCashlessTableDTO> classMap = mapperFactory
				.classMap(Claim.class,
						SearchConverClaimCashlessTableDTO.class);
		classMap.field("key", "key");
		classMap.field("intimation.intimationId", "intimationNumber");
		classMap.field("intimation.policy.policyNumber", "policyNo");
		classMap.field("claimType", "claimType");
		classMap.field("intimation.cpuCode.cpuCode", "cpuCode");
		classMap.field("intimation.insured.insuredName", "insuredPatientName");
		classMap.field("intimation.hospitalType", "hospitalType");
		classMap.field("intimation.createdDate", "intimationDate");
		classMap.field("status.processValue", "claimStatus");
		classMap.field("intimation.hospitalType.key", "hospitalTypeKey");
		classMap.field("intimation.hospital", "hospitalNameIds");
		
		classMap.register();
		tableMapper = mapperFactory.getMapperFacade();
	}

	public static List<SearchConverClaimCashlessTableDTO> getSearchconverclaimcashlessTableDTO(
			List<Claim> pedRequestQueryList) {
		List<SearchConverClaimCashlessTableDTO> mapAsList = tableMapper
				.mapAsList(pedRequestQueryList,
						SearchConverClaimCashlessTableDTO.class);
		return mapAsList;
	}
	
	public static SearchConverClaimCashlessMapper getInstance(){
        if(myObj == null){
            myObj = new SearchConverClaimCashlessMapper();
            getAllMapValues();
        }
        return myObj;
	 }


	
}

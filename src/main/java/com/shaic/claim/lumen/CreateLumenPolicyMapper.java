package com.shaic.claim.lumen;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.impl.generator.EclipseJdtCompilerStrategy;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.lumen.create.LumenPolicySearchResultTableDTO;
import com.shaic.domain.Policy;

public class CreateLumenPolicyMapper {

	private static MapperFactory mapperFactory =  new DefaultMapperFactory.Builder().compilerStrategy(new EclipseJdtCompilerStrategy()).build();

	private static CreateLumenPolicyMapper mapperObj;

	private static MapperFacade mapperFacade;

	private static ClassMapBuilder<Policy, LumenPolicySearchResultTableDTO> lumenMapper = mapperFactory.classMap(Policy.class, LumenPolicySearchResultTableDTO.class);

	public static  List<LumenPolicySearchResultTableDTO> getDetails(List<Policy> policyResultList){
		List<LumenPolicySearchResultTableDTO> mapAsList = mapperFacade.mapAsList(policyResultList, LumenPolicySearchResultTableDTO.class);
		return mapAsList;
	}

	public static CreateLumenPolicyMapper getInstance(){
		if(mapperObj == null){
			mapperObj = new CreateLumenPolicyMapper();
			getAllMapValues();
		}
		return mapperObj;
	}

	public static void  getAllMapValues(){
		lumenMapper.field("key", "policyKey");
		lumenMapper.field("policyNumber", "policyNumber");
		lumenMapper.field("proposerCode", "proposerCode");
		lumenMapper.field("proposerFirstName", "proposerName");
		lumenMapper.field("productName", "productName");
		lumenMapper.field("policyFromDate", "policyStartDate");
		lumenMapper.field("policyToDate", "policyEndDate");
		lumenMapper.field("homeOfficeCode", "homeOfficeCode");
		lumenMapper.field("policyType", "selectPolicyType");
		lumenMapper.field("productType", "selectProductType");
		lumenMapper.register();
		mapperFacade = mapperFactory.getMapperFacade();
	}

}

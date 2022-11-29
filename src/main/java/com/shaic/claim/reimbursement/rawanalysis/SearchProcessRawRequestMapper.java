package com.shaic.claim.reimbursement.rawanalysis;

import java.util.List;

import com.shaic.domain.Claim;
import com.shaic.domain.Intimation;
import com.shaic.newcode.wizard.domain.MappingUtil;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

public class SearchProcessRawRequestMapper {

	private static MapperFacade tableMapper;
	
	static SearchProcessRawRequestMapper myObj;
	
	private static BoundMapperFacade<Intimation, SearchProcessRawRequestTableDto> processRawRequestMapper;
	
	public  static void getAllMapValues(){
		
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Intimation, SearchProcessRawRequestTableDto> claimClassMap = mapperFactor.classMap(Intimation.class, SearchProcessRawRequestTableDto.class);
		
		
		claimClassMap.field("key", "intimationKey");
		claimClassMap.field("intimationId", "intimationNo");
		claimClassMap.field("policy.policyNumber", "policyNo");
		claimClassMap.field("insured.insuredName", "insuredPatientName");
		claimClassMap.field("hospital", "hospitalId");
		claimClassMap.field("policy.productName", "productName");
		claimClassMap.field("cpuCode.cpuCode", "cpuCode");
	/*	claimClassMap.field("claimType.value", "claimType");
		claimClassMap.field("key", "claimKey");*/
		
		
		claimClassMap.register();
		
		tableMapper = mapperFactor.getMapperFacade();
		
		processRawRequestMapper = mapperFactor.getMapperFacade(Intimation.class, SearchProcessRawRequestTableDto.class);
		
	}
	
	public static List<SearchProcessRawRequestTableDto> getClaimDTO(List<Intimation> claimData){
		List<SearchProcessRawRequestTableDto> mapAsList = 
										tableMapper.mapAsList(claimData, SearchProcessRawRequestTableDto.class);
		return mapAsList;
		
	}
	
	   public static SearchProcessRawRequestMapper getInstance(){
	       if(myObj == null){
	           myObj = new SearchProcessRawRequestMapper();
	           getAllMapValues();
	       }
	       return myObj;
		 }
	
	
	
	
}

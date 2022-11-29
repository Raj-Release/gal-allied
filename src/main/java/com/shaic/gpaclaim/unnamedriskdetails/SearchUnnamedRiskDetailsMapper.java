package com.shaic.gpaclaim.unnamedriskdetails;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Intimation;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class SearchUnnamedRiskDetailsMapper {
	

	private static MapperFacade tableMapper;
	
	static SearchUnnamedRiskDetailsMapper myObj;
	
	private static MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
	private static ClassMapBuilder<Intimation, SearchUnnamedRiskDetailsTableDTO> classMapForUnnamedRiskDetails = mapperFactory.classMap(Intimation.class, SearchUnnamedRiskDetailsTableDTO.class);
	
	public static void getAllMapValues()  {
		
		classMapForUnnamedRiskDetails.field("intimationId","intimationNo");
		classMapForUnnamedRiskDetails.field("policyNumber","policyNo");		
		classMapForUnnamedRiskDetails.field("policy.product.value","productName");
		classMapForUnnamedRiskDetails.field("insured","insuredName");
		
						
		classMapForUnnamedRiskDetails.register();
		tableMapper = mapperFactory.getMapperFacade();
		
	}
	public static List<SearchUnnamedRiskDetailsTableDTO> getUnnamedRiskDetailsTableObjects(List<Intimation> intimationtList)
    {
	List<SearchUnnamedRiskDetailsTableDTO> unnamedRiskDetailsTableObjectList = tableMapper.mapAsList(intimationtList, SearchUnnamedRiskDetailsTableDTO.class);
	return unnamedRiskDetailsTableObjectList;
    }
	
	public static SearchUnnamedRiskDetailsMapper getInstance(){
        if(myObj == null){
            myObj = new SearchUnnamedRiskDetailsMapper();
            getAllMapValues();
        }
        return myObj;
	 }
	


}

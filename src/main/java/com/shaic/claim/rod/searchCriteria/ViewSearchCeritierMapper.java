package com.shaic.claim.rod.searchCriteria;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.BankMaster;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class ViewSearchCeritierMapper {
	private static MapperFacade tableMapper;
	
	static ViewSearchCeritierMapper  myObj;

	
	public static void getAllMapValues()  {
 
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<BankMaster, ViewSearchCriteriaTableDTO> bankMasterClassMap = mapperFactor.classMap(BankMaster.class, ViewSearchCriteriaTableDTO.class);
		
		bankMasterClassMap.field("bankName", "bankName");
		bankMasterClassMap.field("key", "bankId");
		bankMasterClassMap.field("branchName", "branchName");
		bankMasterClassMap.field("address", "address");
		bankMasterClassMap.field("ifscCode", "ifscCode");
		bankMasterClassMap.field("city", "city");
		bankMasterClassMap.register();
		
		tableMapper = mapperFactor.getMapperFacade();
	}
	
	public static List<ViewSearchCriteriaTableDTO> getBankMasterDTO(List<BankMaster> bankMasterData){
		List<ViewSearchCriteriaTableDTO> mapAsList = 
										tableMapper.mapAsList(bankMasterData, ViewSearchCriteriaTableDTO.class);
		return mapAsList;
		
	}
	
	public static ViewSearchCeritierMapper getInstance(){
        if(myObj == null){
            myObj = new ViewSearchCeritierMapper();
            getAllMapValues();
        }
        return myObj;
	 }
}

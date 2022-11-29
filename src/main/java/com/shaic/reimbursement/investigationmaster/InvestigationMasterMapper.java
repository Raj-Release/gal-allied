package com.shaic.reimbursement.investigationmaster;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Intimation;
import com.shaic.domain.TmpInvestigation;
import com.shaic.domain.preauth.MasPrivateInvestigator;
import com.shaic.newcode.wizard.domain.MappingUtil;
import com.shaic.reimbursement.investigation.assigninvestigation.search.SearchAssignInvestigationMapper;
import com.shaic.reimbursement.investigation.assigninvestigation.search.SearchAssignInvestigationTableDTO;

public class InvestigationMasterMapper {
	static InvestigationMasterMapper myObj;

	private static MapperFacade tableMapper;
	
	public static void getAllMapValues()   {
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<TmpInvestigation, InvestigationMasterTableDTO> intimationClassMap = mapperFactor.classMap(TmpInvestigation.class, InvestigationMasterTableDTO.class);
		ClassMapBuilder<MasPrivateInvestigator, InvestigationMasterTableDTO> privateInvestigatorClassMap = mapperFactor.classMap(MasPrivateInvestigator.class, InvestigationMasterTableDTO.class);
		
		intimationClassMap.field("key", "key");
		intimationClassMap.field("investigatorCode", "investigatorId");
		intimationClassMap.field("investigatorName", "investigatorName");
		intimationClassMap.field("allocationTo.value", "investigatorType");
		intimationClassMap.field("mobileNumber", "mobileNo");
		intimationClassMap.field("state.value", "state");
		intimationClassMap.field("cityTownVillage.value", "city");
		intimationClassMap.field("activeStatus", "status");
		
		privateInvestigatorClassMap.field("privateInvestigationKey", "key");
		privateInvestigatorClassMap.field("privateInvestigationKey", "investigatorId");
		privateInvestigatorClassMap.field("investigatorName", "investigatorName");
		//privateInvestigatorClassMap.field("allocationTo.value", "investigatorType");
		privateInvestigatorClassMap.field("mobileNumberOne", "mobileNo");
		privateInvestigatorClassMap.field("activeStatus", "statusToDisplay");
		/*privateInvestigatorClassMap.field("state.value", "state");
		privateInvestigatorClassMap.field("cityTownVillage.value", "city");*/
		
		intimationClassMap.register();
		privateInvestigatorClassMap.register();
		
		tableMapper = mapperFactor.getMapperFacade();
	}
	
	public static List<InvestigationMasterTableDTO> getIvestigationMasterDetails(List<TmpInvestigation> investigatorData){
		List<InvestigationMasterTableDTO> mapAsList = 
										tableMapper.mapAsList(investigatorData, InvestigationMasterTableDTO.class);
		return mapAsList;
		
	}
	
	public static List<InvestigationMasterTableDTO> getPrivateIvestigationMasterDetails(List<MasPrivateInvestigator> privateInvestigatorData){
		List<InvestigationMasterTableDTO> mapAsList = 
										tableMapper.mapAsList(privateInvestigatorData, InvestigationMasterTableDTO.class);
		return mapAsList;
		
	}
	public static InvestigationMasterMapper getInstance(){
        if(myObj == null){
            myObj = new InvestigationMasterMapper();
            getAllMapValues();
        }
        return myObj;
	 }
	
}


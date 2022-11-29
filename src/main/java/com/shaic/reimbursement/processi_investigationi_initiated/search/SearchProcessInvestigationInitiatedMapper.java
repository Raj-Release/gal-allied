package com.shaic.reimbursement.processi_investigationi_initiated.search;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Intimation;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class SearchProcessInvestigationInitiatedMapper {

	private static MapperFacade tableMapper;
	
	static SearchProcessInvestigationInitiatedMapper  myObj;

	public static void getAllMapValues()  {
 
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Intimation, SearchProcessInvestigationInitiatedTableDTO> intimationClassMap = mapperFactor.classMap(Intimation.class, SearchProcessInvestigationInitiatedTableDTO.class);
		
		intimationClassMap.field("key", "key");
		intimationClassMap.field("intimationId", "intimationNo");
		intimationClassMap.field("policy.policyNumber", "policyNo");
		intimationClassMap.field("policy.lobId", "lOBId");
		intimationClassMap.field("cpuCode.cpuCode", "cpuCode");
		intimationClassMap.field("admissionReason", "reasonForAdmission");
		intimationClassMap.field("insured.insuredName", "insuredPatientName");
		intimationClassMap.field("hospital", "hospitalNameID");
		intimationClassMap.field("policy.productName", "productName");
		
		intimationClassMap.register();
		
		tableMapper = mapperFactor.getMapperFacade();
	}
	
	public static List<SearchProcessInvestigationInitiatedTableDTO> getIntimationDTO(List<Intimation> intimationData){
		List<SearchProcessInvestigationInitiatedTableDTO> mapAsList = 
										tableMapper.mapAsList(intimationData, SearchProcessInvestigationInitiatedTableDTO.class);
		return mapAsList;
		
	}
	
	public static SearchProcessInvestigationInitiatedMapper getInstance(){
        if(myObj == null){
            myObj = new SearchProcessInvestigationInitiatedMapper();
            getAllMapValues();
        }
        return myObj;
	 }

	
}

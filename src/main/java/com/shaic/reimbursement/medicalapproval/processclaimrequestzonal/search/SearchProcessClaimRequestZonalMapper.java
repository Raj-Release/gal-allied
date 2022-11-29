package com.shaic.reimbursement.medicalapproval.processclaimrequestzonal.search;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Intimation;
import com.shaic.newcode.wizard.domain.MappingUtil;


/**
 * @author ntv.narenj
 *
 */
public class SearchProcessClaimRequestZonalMapper  {

	static SearchProcessClaimRequestZonalMapper myObj;
	
	private static MapperFacade tableMapper;
	
	public static void getAllMapValues()   {
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Intimation, SearchProcessClaimRequestZonalTableDTO> intimationClassMap = mapperFactor.classMap(Intimation.class, SearchProcessClaimRequestZonalTableDTO.class);
		
		intimationClassMap.field("key", "intimationKey");
		intimationClassMap.field("intimationId", "intimationNo");
		intimationClassMap.field("policy.key", "policyKey");
		intimationClassMap.field("policy.policyNumber", "policyNumber");
		intimationClassMap.field("policy.totalSumInsured", "sumInsured");
		intimationClassMap.field("cpuCode.description", "cpuName");
		intimationClassMap.field("insured.insuredName", "insuredPatientName");
		intimationClassMap.field("hospital", "hospitalNameID");
		intimationClassMap.field("policy.productName", "productName");
		intimationClassMap.field("insured.key","insuredKey");
		intimationClassMap.field("intimationSource.value", "intimationSource");
		intimationClassMap.field("hospitalType.value", "hospitalType");
		
		intimationClassMap.register();
		
		tableMapper = mapperFactor.getMapperFacade();
	}
	
	public static List<SearchProcessClaimRequestZonalTableDTO> getIntimationDTO(List<Intimation> intimationData){
		List<SearchProcessClaimRequestZonalTableDTO> mapAsList = 
										tableMapper.mapAsList(intimationData, SearchProcessClaimRequestZonalTableDTO.class);
		return mapAsList;
		
	}
	
	public static SearchProcessClaimRequestZonalMapper  getInstance(){
        if(myObj == null){
            myObj = new SearchProcessClaimRequestZonalMapper ();
            getAllMapValues();
        }
        return myObj;
	 }
}

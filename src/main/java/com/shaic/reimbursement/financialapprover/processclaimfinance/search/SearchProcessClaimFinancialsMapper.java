package com.shaic.reimbursement.financialapprover.processclaimfinance.search;

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
public class SearchProcessClaimFinancialsMapper {
	
	private static MapperFacade tableMapper;
	
	static SearchProcessClaimFinancialsMapper myObj;

	public static void getAllMapValues()   {
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Intimation, SearchProcessClaimFinancialsTableDTO> intimationClassMap = mapperFactor.classMap(Intimation.class, SearchProcessClaimFinancialsTableDTO.class);
		
		intimationClassMap.field("key", "key");
		intimationClassMap.field("intimationId", "intimationNo");
		intimationClassMap.field("policy.lobId", "lOBId");
		intimationClassMap.field("admissionReason", "reasonForAdmission");
		intimationClassMap.field("insured.insuredName", "insuredPatientName");
		intimationClassMap.field("hospital", "hospitalNameID");
		intimationClassMap.field("cpuCode.cpuCode", "cpuCode");
		intimationClassMap.field("cpuCode.description", "cpuName");
		intimationClassMap.field("policy.policyNumber", "policyNo");
		intimationClassMap.field("policy.productName", "productName");
		intimationClassMap.field("policy.product.key", "productKey");
		
		intimationClassMap.register();
		
		tableMapper = mapperFactor.getMapperFacade();
	}
	
	public static List<SearchProcessClaimFinancialsTableDTO> getIntimationDTO(List<Intimation> intimationData){
		List<SearchProcessClaimFinancialsTableDTO> mapAsList = 
										tableMapper.mapAsList(intimationData, SearchProcessClaimFinancialsTableDTO.class);
		return mapAsList;
		
	}
	
	public static SearchProcessClaimFinancialsMapper getInstance(){
        if(myObj == null){
            myObj = new SearchProcessClaimFinancialsMapper();
            getAllMapValues();
        }
        return myObj;
	 }
	
}

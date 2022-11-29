package com.shaic.paclaim.financial.nonhospprocessclaimfinancial.search;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Intimation;
import com.shaic.newcode.wizard.domain.MappingUtil;
import com.shaic.reimbursement.financialapprover.processclaimfinance.search.SearchProcessClaimFinancialsTableDTO;

/**
 *
 *
 */
public class PASearchProcessClaimFinancialsNonHospMapper {
	
	private static MapperFacade tableMapper;
	
	static PASearchProcessClaimFinancialsNonHospMapper myObj;

	public static void getAllMapValues()   {
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Intimation, SearchProcessClaimFinancialsTableDTO> intimationClassMap = mapperFactor.classMap(Intimation.class, SearchProcessClaimFinancialsTableDTO.class);
		
		intimationClassMap.field("key", "key");
		intimationClassMap.field("intimationId", "intimationNo");
		intimationClassMap.field("policy.lobId", "lOBId");
		intimationClassMap.field("admissionReason", "reasonForAdmission");
		intimationClassMap.field("insured.insuredName", "insuredPatientName");
		intimationClassMap.field("paPatientName", "paPatientName");
		intimationClassMap.field("policy.product.key", "productKey");
		intimationClassMap.field("hospital", "hospitalNameID");
		intimationClassMap.field("cpuCode.cpuCode", "cpuCode");
		intimationClassMap.field("cpuCode.description", "cpuName");
		intimationClassMap.field("policy.policyNumber", "policyNo");
		intimationClassMap.field("policy.productName", "productName");
		
		intimationClassMap.register();
		
		tableMapper = mapperFactor.getMapperFacade();
	}
	
	public static List<SearchProcessClaimFinancialsTableDTO> getIntimationDTO(List<Intimation> intimationData){
		List<SearchProcessClaimFinancialsTableDTO> mapAsList = 
										tableMapper.mapAsList(intimationData, SearchProcessClaimFinancialsTableDTO.class);
		return mapAsList;
		
	}
	
	public static PASearchProcessClaimFinancialsNonHospMapper getInstance(){
        if(myObj == null){
            myObj = new PASearchProcessClaimFinancialsNonHospMapper();
            getAllMapValues();
        }
        return myObj;
	 }
	
}

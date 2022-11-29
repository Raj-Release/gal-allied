package com.shaic.reimbursement.billing.processclaimbilling.search;

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
public class SearchProcessClaimBillingMapper{
	
	static SearchProcessClaimBillingMapper myObj;

	
	private static MapperFacade tableMapper;
	
	public static void getAllMapValues() {
		
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Intimation, SearchProcessClaimBillingTableDTO> intimationClassMap = mapperFactor.classMap(Intimation.class, SearchProcessClaimBillingTableDTO.class);
		
		intimationClassMap.field("key", "key");
		intimationClassMap.field("intimationId", "intimationNo");
		intimationClassMap.field("policy.policyNumber", "policyNo");
		intimationClassMap.field("admissionReason", "reasonForAdmission");
		intimationClassMap.field("insured.insuredName", "insuredPatientName");
		intimationClassMap.field("paPatientName", "paPatientName");
		intimationClassMap.field("policy.product.key", "productKey");
		intimationClassMap.field("hospital", "hospitalNameID");
		intimationClassMap.field("cpuCode.cpuCode", "cpuCode");
		intimationClassMap.field("cpuCode.description", "cpuName");
		intimationClassMap.field("hospitalType.value", "hospitalType");
		intimationClassMap.field("admissionDate", "dateOfAdmission1");
		intimationClassMap.field("policy.productName", "productName");
		
		intimationClassMap.register();
		
		tableMapper = mapperFactor.getMapperFacade();
	}
	
	public static List<SearchProcessClaimBillingTableDTO> getIntimationDTO(List<Intimation> intimationData){
		List<SearchProcessClaimBillingTableDTO> mapAsList = 
										tableMapper.mapAsList(intimationData, SearchProcessClaimBillingTableDTO.class);
		return mapAsList;
		
	}
	
	public static SearchProcessClaimBillingMapper getInstance(){
        if(myObj == null){
            myObj = new SearchProcessClaimBillingMapper();
            getAllMapValues();
        }
        return myObj;
	 }

}

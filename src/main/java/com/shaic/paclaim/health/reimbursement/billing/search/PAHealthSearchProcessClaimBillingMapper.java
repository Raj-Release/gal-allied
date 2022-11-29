package com.shaic.paclaim.health.reimbursement.billing.search;

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
public class PAHealthSearchProcessClaimBillingMapper{
	
	static PAHealthSearchProcessClaimBillingMapper myObj;

	
	private static MapperFacade tableMapper;
	
	public static void getAllMapValues() {
		
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Intimation, PAHealthSearchProcessClaimBillingTableDTO> intimationClassMap = mapperFactor.classMap(Intimation.class, PAHealthSearchProcessClaimBillingTableDTO.class);
		
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
	
	public static List<PAHealthSearchProcessClaimBillingTableDTO> getIntimationDTO(List<Intimation> intimationData){
		List<PAHealthSearchProcessClaimBillingTableDTO> mapAsList = 
										tableMapper.mapAsList(intimationData, PAHealthSearchProcessClaimBillingTableDTO.class);
		return mapAsList;
		
	}
	
	public static PAHealthSearchProcessClaimBillingMapper getInstance(){
        if(myObj == null){
            myObj = new PAHealthSearchProcessClaimBillingMapper();
            getAllMapValues();
        }
        return myObj;
	 }

}

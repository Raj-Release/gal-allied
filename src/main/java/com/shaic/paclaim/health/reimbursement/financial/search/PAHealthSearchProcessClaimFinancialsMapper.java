package com.shaic.paclaim.health.reimbursement.financial.search;

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
public class PAHealthSearchProcessClaimFinancialsMapper {
	
	private static MapperFacade tableMapper;
	
	static PAHealthSearchProcessClaimFinancialsMapper myObj;

	public static void getAllMapValues()   {
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Intimation, PAHealthSearchProcessClaimFinancialsTableDTO> intimationClassMap = mapperFactor.classMap(Intimation.class, PAHealthSearchProcessClaimFinancialsTableDTO.class);
		
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
	
	public static List<PAHealthSearchProcessClaimFinancialsTableDTO> getIntimationDTO(List<Intimation> intimationData){
		List<PAHealthSearchProcessClaimFinancialsTableDTO> mapAsList = 
										tableMapper.mapAsList(intimationData, PAHealthSearchProcessClaimFinancialsTableDTO.class);
		return mapAsList;
		
	}
	
	public static PAHealthSearchProcessClaimFinancialsMapper getInstance(){
        if(myObj == null){
            myObj = new PAHealthSearchProcessClaimFinancialsMapper();
            getAllMapValues();
        }
        return myObj;
	 }
	
}

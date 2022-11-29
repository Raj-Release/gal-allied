package com.shaic.paclaim.health.reimbursement.medicalapproval.search;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Claim;
import com.shaic.newcode.wizard.domain.MappingUtil;

/**
 * @author ntv.narenj
 *
 */
public class PAHealthSearchProcessClaimRequestMapper {
	private static MapperFacade tableMapper;
	
	static PAHealthSearchProcessClaimRequestMapper myObj;

	public static void getAllMapValues()  {
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Claim, PAHealthSearchProcessClaimRequestTableDTO> intimationClassMap = mapperFactor.classMap(Claim.class, PAHealthSearchProcessClaimRequestTableDTO.class);
		
		intimationClassMap.field("key", "claimKey");
		intimationClassMap.field("intimation.key", "intimationKey");
		intimationClassMap.field("intimation.intimationId", "intimationNo");
		intimationClassMap.field("intimation.policy.key", "policyKey");
		intimationClassMap.field("intimation.policy.totalSumInsured", "sumInsured");
		intimationClassMap.field("intimation.insured.insuredName", "insuredPatientName");
		intimationClassMap.field("intimation.paPatientName", "paPatientName");
		intimationClassMap.field("intimation.policy.product.key", "productKey");
		intimationClassMap.field("intimation.hospital", "hospitalNameID");
		intimationClassMap.field("intimation.policy.productName", "productName");
		intimationClassMap.field("intimation.policy.policyNumber", "policyNumber");
		intimationClassMap.field("intimation.insured.key","insuredKey");
		intimationClassMap.field("intimation.cpuCode.description", "cpuName");
		intimationClassMap.field("intimation.intimationSource.value", "intimationSource");
		intimationClassMap.field("intimation.hospitalType.value", "hospitalType");
		intimationClassMap.field("intimation.admissionReason", "reasonForAdmission");
		intimationClassMap.field("claimType.value", "claimType");
		
		intimationClassMap.register();
		
		tableMapper = mapperFactor.getMapperFacade();
	}
	
	public static List<PAHealthSearchProcessClaimRequestTableDTO> getIntimationDTO(List<Claim> intimationData){
		List<PAHealthSearchProcessClaimRequestTableDTO> mapAsList = 
										tableMapper.mapAsList(intimationData, PAHealthSearchProcessClaimRequestTableDTO.class);
		return mapAsList;
		
	}
	
	public static PAHealthSearchProcessClaimRequestMapper getInstance(){
        if(myObj == null){
            myObj = new PAHealthSearchProcessClaimRequestMapper();
            getAllMapValues();
        }
        return myObj;
	 }

}

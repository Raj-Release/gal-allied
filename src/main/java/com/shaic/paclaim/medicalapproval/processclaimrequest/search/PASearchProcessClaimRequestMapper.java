package com.shaic.paclaim.medicalapproval.processclaimrequest.search;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Claim;
import com.shaic.newcode.wizard.domain.MappingUtil;
import com.shaic.reimbursement.medicalapproval.processclaimrequest.search.SearchProcessClaimRequestTableDTO;

/**
 * @author ntv.narenj
 *
 */
public class PASearchProcessClaimRequestMapper {
	private static MapperFacade tableMapper;
	
	static PASearchProcessClaimRequestMapper myObj;

	public static void getAllMapValues()  {
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Claim, SearchProcessClaimRequestTableDTO> intimationClassMap = mapperFactor.classMap(Claim.class, SearchProcessClaimRequestTableDTO.class);
		
		intimationClassMap.field("key", "claimKey");
		intimationClassMap.field("intimation.key", "intimationKey");
		intimationClassMap.field("intimation.intimationId", "intimationNo");
		intimationClassMap.field("intimation.policy.key", "policyKey");
		intimationClassMap.field("intimation.policy.totalSumInsured", "sumInsured");
		intimationClassMap.field("intimation.insured.insuredName", "insuredPatientName");
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
	
	public static List<SearchProcessClaimRequestTableDTO> getIntimationDTO(List<Claim> intimationData){
		List<SearchProcessClaimRequestTableDTO> mapAsList = 
										tableMapper.mapAsList(intimationData, SearchProcessClaimRequestTableDTO.class);
		return mapAsList;
		
	}
	
	public static PASearchProcessClaimRequestMapper getInstance(){
        if(myObj == null){
            myObj = new PASearchProcessClaimRequestMapper();
            getAllMapValues();
        }
        return myObj;
	 }

}

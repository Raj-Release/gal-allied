package com.shaic.reimbursement.billing.processClaimRequestBenefits;

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
public class SearchProcessClaimRequestBenefitsMapper {
	private static MapperFacade tableMapper;
	
	static SearchProcessClaimRequestBenefitsMapper myObj;

	public static void getAllMapValues()   {
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
																																
		ClassMapBuilder<Claim, SearchProcessClaimRequestBenefitsTableDTO> claimClassMap = mapperFactor.classMap(Claim.class, SearchProcessClaimRequestBenefitsTableDTO.class);
		
		claimClassMap.field("key", "claimKey");
		claimClassMap.field("intimation.key", "intimationKey");
		claimClassMap.field("intimation.intimationId", "intimationNo");
		claimClassMap.field("intimation.policy.key", "policyKey");
		claimClassMap.field("intimation.policy.totalSumInsured", "sumInsured");
		claimClassMap.field("intimation.insured.insuredName", "insuredPatientName");
		claimClassMap.field("intimation.hospital", "hospitalNameID");
		claimClassMap.field("intimation.policy.productName", "productName");
		claimClassMap.field("intimation.insured.key","insuredKey");
		claimClassMap.field("intimation.intimationSource.value", "intimationSource");
		claimClassMap.field("intimation.managementType.value", "hospitalType");
		claimClassMap.field("intimation.admissionReason", "reasonForAdmission");
		claimClassMap.field("claimType.value", "claimType");
		
		claimClassMap.register();
		
		tableMapper = mapperFactor.getMapperFacade();
	}
	
	public static List<SearchProcessClaimRequestBenefitsTableDTO> getClaimDTO(List<Claim> claimData){
		List<SearchProcessClaimRequestBenefitsTableDTO> mapAsList = 
										tableMapper.mapAsList(claimData, SearchProcessClaimRequestBenefitsTableDTO.class);
		return mapAsList;
		
	}
	
	public static SearchProcessClaimRequestBenefitsMapper getInstance(){
        if(myObj == null){
            myObj = new SearchProcessClaimRequestBenefitsMapper();
            getAllMapValues();
        }
        return myObj;
	 }
}

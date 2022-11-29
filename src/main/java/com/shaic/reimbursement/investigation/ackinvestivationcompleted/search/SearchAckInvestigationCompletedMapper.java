package com.shaic.reimbursement.investigation.ackinvestivationcompleted.search;

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
public class SearchAckInvestigationCompletedMapper {

	private static MapperFacade tableMapper;
	
	static SearchAckInvestigationCompletedMapper myObj;
	
	 public static void getAllMapValues()   {
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Claim, SearchAckInvestigationCompletedTableDTO> claimClassMap = mapperFactor.classMap(Claim.class, SearchAckInvestigationCompletedTableDTO.class);
		
		claimClassMap.field("claimId", "claimNo");
		claimClassMap.field("key", "claimKey");
		claimClassMap.field("intimation.key", "intimationkey");
		claimClassMap.field("intimation.intimationId", "intimationNo");
		claimClassMap.field("intimation.policy.lobId", "lOBId");
		claimClassMap.field("intimation.admissionReason", "reasonForAdmission");
		claimClassMap.field("intimation.insured.insuredName", "insuredPatientName");
		claimClassMap.field("intimation.hospital", "hospitalNameID");
		claimClassMap.field("intimation.policy.productName", "productName");
		
		claimClassMap.register();
		
		tableMapper = mapperFactor.getMapperFacade();
	}
	
	public static List<SearchAckInvestigationCompletedTableDTO> getClaimDTO(List<Claim> claimData){
		List<SearchAckInvestigationCompletedTableDTO> mapAsList = 
										tableMapper.mapAsList(claimData, SearchAckInvestigationCompletedTableDTO.class);
		return mapAsList;
		
	}
	public static SearchAckInvestigationCompletedMapper getInstance(){
        if(myObj == null){
            myObj = new SearchAckInvestigationCompletedMapper();
            getAllMapValues();
        }
        return myObj;
	 }
	
	
}

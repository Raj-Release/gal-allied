package com.shaic.reimbursement.queryrejection.draftrejection.search;

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
public class SearchDraftRejectionLetterMapper {
	private static MapperFacade tableMapper;
	
	static SearchDraftRejectionLetterMapper myObj;

		
	public static void getAllMapValues()   {
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Claim, SearchDraftRejectionLetterTableDTO> claimClassMap = mapperFactor.classMap(Claim.class, SearchDraftRejectionLetterTableDTO.class);
		claimClassMap.field("claimId", "claimNo");
		claimClassMap.field("key", "claimKey");
		claimClassMap.field("intimation.key", "intimationkey");
		claimClassMap.field("intimation.intimationId", "intimationNo");
		claimClassMap.field("intimation.policy.policyNumber", "policyNo");
		claimClassMap.field("intimation.admissionReason", "reasonForAdmission");
		claimClassMap.field("intimation.cpuCode.cpuCode", "cpuId");
		claimClassMap.field("intimation.insured.insuredName", "insuredPatientName");
		claimClassMap.field("intimation.hospital", "hospitalNameID");
		claimClassMap.field("status.processValue", "claimStatus");
		claimClassMap.register();
		
		tableMapper = mapperFactor.getMapperFacade();
	}
	
	public static List<SearchDraftRejectionLetterTableDTO> getClaimDTO(List<Claim> claimData){
		List<SearchDraftRejectionLetterTableDTO> mapAsList = 
										tableMapper.mapAsList(claimData, SearchDraftRejectionLetterTableDTO.class);
		return mapAsList;
		
	}
	
	public static SearchDraftRejectionLetterMapper getInstance(){
        if(myObj == null){
            myObj = new SearchDraftRejectionLetterMapper();
            getAllMapValues();
        }
        return myObj;
	 }
}
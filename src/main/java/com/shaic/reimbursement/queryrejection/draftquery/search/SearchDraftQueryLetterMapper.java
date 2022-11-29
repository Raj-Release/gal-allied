package com.shaic.reimbursement.queryrejection.draftquery.search;

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
public class SearchDraftQueryLetterMapper {

	private static MapperFacade tableMapper;
	
	static SearchDraftQueryLetterMapper myObj;

	
	public static void getAllMapValues()  {
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Claim, SearchDraftQueryLetterTableDTO> claimClassMap = mapperFactor.classMap(Claim.class, SearchDraftQueryLetterTableDTO.class);
		claimClassMap.field("claimId", "claimNo");
		claimClassMap.field("key", "claimKey");
		claimClassMap.field("intimation.key", "intimationkey");
		claimClassMap.field("intimation.intimationId", "intimationNo");
		claimClassMap.field("intimation.policy.policyNumber", "policyNo");
		claimClassMap.field("intimation.admissionReason", "reasonForAdmission");
		claimClassMap.field("intimation.insured.insuredName", "insuredPatientName");
		claimClassMap.field("intimation.hospital", "hospitalNameID");
		claimClassMap.field("status.processValue", "claimStatus");
		claimClassMap.field("intimation.cpuCode.cpuCode", "cpuId");
		claimClassMap.register();
		
		tableMapper = mapperFactor.getMapperFacade();
	}
	
	public static List<SearchDraftQueryLetterTableDTO> getClaimDTO(List<Claim> claimData){
		List<SearchDraftQueryLetterTableDTO> mapAsList = 
										tableMapper.mapAsList(claimData, SearchDraftQueryLetterTableDTO.class);
		return mapAsList;
		
	}
	
	public static SearchDraftQueryLetterMapper getInstance(){
        if(myObj == null){
            myObj = new SearchDraftQueryLetterMapper();
            getAllMapValues();
        }
        return myObj;
	 }
	
	
}

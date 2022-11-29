package com.shaic.reimbursement.manageclaim.reopenclaim.sarchClaimLevel;

import java.util.List;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Claim;
import com.shaic.newcode.wizard.domain.MappingUtil;

/**
 * 
 *
 */
public class SearchReOpenClaimMapper {

	private static MapperFacade tableMapper;
	
	static SearchReOpenClaimMapper  myObj;

	
	private static BoundMapperFacade<Claim, SearchReOpenClaimTableDTO> reOpenClaimMapper;
	
	public static void getAllMapValues()  {
 
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Claim, SearchReOpenClaimTableDTO> claimClassMap = mapperFactor.classMap(Claim.class, SearchReOpenClaimTableDTO.class);
		
		claimClassMap.field("key", "claimKey");
		claimClassMap.field("intimation.key", "intimationkey");
		claimClassMap.field("intimation.intimationId", "intimationNo");
		claimClassMap.field("intimation.policy.policyNumber", "policyNo");
		claimClassMap.field("intimation.insured.insuredName", "insuredPatientName");
		claimClassMap.field("intimation.hospital", "hospitalNameID");
		claimClassMap.field("status.processValue", "claimStatus");
		claimClassMap.field("claimType.value", "claimType");
		claimClassMap.field("intimation.admissionDate", "dateOfAdmission");
		
		claimClassMap.register();
		
		tableMapper = mapperFactor.getMapperFacade();
		
		reOpenClaimMapper = mapperFactor.getMapperFacade(Claim.class, SearchReOpenClaimTableDTO.class);
	}
	
	public static List<SearchReOpenClaimTableDTO> getClaimDTO(List<Claim> claimData){
		List<SearchReOpenClaimTableDTO> mapAsList = 
										tableMapper.mapAsList(claimData, SearchReOpenClaimTableDTO.class);
		return mapAsList;
		
	}
	
    public static SearchReOpenClaimTableDTO getSearchClaimDTO(Claim claim){
		
		SearchReOpenClaimTableDTO dto = reOpenClaimMapper.map(claim);
		
		return dto;
	}
    
    public static SearchReOpenClaimMapper getInstance(){
        if(myObj == null){
            myObj = new SearchReOpenClaimMapper();
            getAllMapValues();
        }
        return myObj;
	 }

	
}
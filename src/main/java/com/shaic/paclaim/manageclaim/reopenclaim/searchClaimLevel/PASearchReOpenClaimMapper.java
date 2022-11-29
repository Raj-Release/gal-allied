package com.shaic.paclaim.manageclaim.reopenclaim.searchClaimLevel;

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
public class PASearchReOpenClaimMapper {

	private static MapperFacade tableMapper;
	
	static PASearchReOpenClaimMapper  myObj;

	
	private static BoundMapperFacade<Claim, PASearchReOpenClaimTableDTO> reOpenClaimMapper;
	
	public static void getAllMapValues()  {
 
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Claim, PASearchReOpenClaimTableDTO> claimClassMap = mapperFactor.classMap(Claim.class, PASearchReOpenClaimTableDTO.class);
		
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
		
		reOpenClaimMapper = mapperFactor.getMapperFacade(Claim.class, PASearchReOpenClaimTableDTO.class);
	}
	
	public static List<PASearchReOpenClaimTableDTO> getClaimDTO(List<Claim> claimData){
		List<PASearchReOpenClaimTableDTO> mapAsList = 
										tableMapper.mapAsList(claimData, PASearchReOpenClaimTableDTO.class);
		return mapAsList;
		
	}
	
    public static PASearchReOpenClaimTableDTO getSearchClaimDTO(Claim claim){
		
		PASearchReOpenClaimTableDTO dto = reOpenClaimMapper.map(claim);
		
		return dto;
	}
    
    public static PASearchReOpenClaimMapper getInstance(){
        if(myObj == null){
            myObj = new PASearchReOpenClaimMapper();
            getAllMapValues();
        }
        return myObj;
	 }

	
}
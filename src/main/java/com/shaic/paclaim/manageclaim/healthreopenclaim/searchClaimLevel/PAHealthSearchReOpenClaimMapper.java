package com.shaic.paclaim.manageclaim.healthreopenclaim.searchClaimLevel;

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
public class PAHealthSearchReOpenClaimMapper {

	private static MapperFacade tableMapper;
	
	static PAHealthSearchReOpenClaimMapper  myObj;

	
	private static BoundMapperFacade<Claim, PAHealthSearchReOpenClaimTableDTO> reOpenClaimMapper;
	
	public static void getAllMapValues()  {
 
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Claim, PAHealthSearchReOpenClaimTableDTO> claimClassMap = mapperFactor.classMap(Claim.class, PAHealthSearchReOpenClaimTableDTO.class);
		
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
		
		reOpenClaimMapper = mapperFactor.getMapperFacade(Claim.class, PAHealthSearchReOpenClaimTableDTO.class);
	}
	
	public static List<PAHealthSearchReOpenClaimTableDTO> getClaimDTO(List<Claim> claimData){
		List<PAHealthSearchReOpenClaimTableDTO> mapAsList = 
										tableMapper.mapAsList(claimData, PAHealthSearchReOpenClaimTableDTO.class);
		return mapAsList;
		
	}
	
    public static PAHealthSearchReOpenClaimTableDTO getSearchClaimDTO(Claim claim){
		
		PAHealthSearchReOpenClaimTableDTO dto = reOpenClaimMapper.map(claim);
		
		return dto;
	}
    
    public static PAHealthSearchReOpenClaimMapper getInstance(){
        if(myObj == null){
            myObj = new PAHealthSearchReOpenClaimMapper();
            getAllMapValues();
        }
        return myObj;
	 }

	
}
package com.shaic.paclaim.manageclaim.reopenclaim.searchRodLevel;

import java.util.List;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Claim;
import com.shaic.domain.preauth.CloseClaim;
import com.shaic.newcode.wizard.domain.MappingUtil;
import com.shaic.paclaim.manageclaim.reopenclaim.pageRodLevel.PAReOpenRodLevelClaimDTO;

/**
 * @author ntv.narenj
 *
 */
public class PASearchReOpenClaimRODLevelMapper {

	private static MapperFacade tableMapper;
	
	static PASearchReOpenClaimRODLevelMapper  myObj;

	
	private static BoundMapperFacade<Claim, PASearchReOpenClaimRodLevelTableDTO> reOpenClaimMapper;
	private static BoundMapperFacade<CloseClaim, PAReOpenRodLevelClaimDTO> reOpenMapper;
	
	public static void getAllMapValues()  {
 
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Claim, PASearchReOpenClaimRodLevelTableDTO> claimClassMap = mapperFactor.classMap(Claim.class, PASearchReOpenClaimRodLevelTableDTO.class);
		ClassMapBuilder<CloseClaim, PAReOpenRodLevelClaimDTO> reOpenMap = mapperFactor.classMap(CloseClaim.class, PAReOpenRodLevelClaimDTO.class);
		
		claimClassMap.field("claimId", "claimNo");
		claimClassMap.field("key", "claimKey");
		claimClassMap.field("intimation.key", "intimationkey");
		claimClassMap.field("intimation.intimationId", "intimationNo");
		claimClassMap.field("intimation.policy.policyNumber", "policyNo");
		claimClassMap.field("intimation.admissionReason", "reasonForAdmission");
		claimClassMap.field("intimation.insured.insuredName", "insuredPatientName");
		claimClassMap.field("intimation.hospital", "hospitalNameID");
		claimClassMap.field("status.processValue", "claimStatus");
		claimClassMap.field("intimation.insured.healthCardNumber", "healthCardNo");
		claimClassMap.field("intimation.admissionDate", "dateOfAdmission");
		
		reOpenMap.field("key", "closeClaimKey");
		reOpenMap.field("status.processValue", "claimStatus");
		reOpenMap.field("createdDate", "closedDate");
		reOpenMap.field("closingRemarks", "closedRemarks");
		
		claimClassMap.register();
		reOpenMap.register();
		
		tableMapper = mapperFactor.getMapperFacade();
		
		reOpenClaimMapper = mapperFactor.getMapperFacade(Claim.class, PASearchReOpenClaimRodLevelTableDTO.class);
		reOpenMapper = mapperFactor.getMapperFacade(CloseClaim.class, PAReOpenRodLevelClaimDTO.class);
	}
	
	public static List<PASearchReOpenClaimRodLevelTableDTO> getClaimDTO(List<Claim> claimData){
		List<PASearchReOpenClaimRodLevelTableDTO> mapAsList = 
										tableMapper.mapAsList(claimData, PASearchReOpenClaimRodLevelTableDTO.class);
		return mapAsList;
		
	}
	
    public static PASearchReOpenClaimRodLevelTableDTO getSearchClaimDTO(Claim claim){
		
		PASearchReOpenClaimRodLevelTableDTO dto = reOpenClaimMapper.map(claim);
		
		return dto;
	}
    
   public static PAReOpenRodLevelClaimDTO getReOpenClaimDTO(CloseClaim result){
		
	   PAReOpenRodLevelClaimDTO dto = reOpenMapper.map(result);
		
	   return dto;
	}
   
   public static PASearchReOpenClaimRODLevelMapper getInstance(){
       if(myObj == null){
           myObj = new PASearchReOpenClaimRODLevelMapper();
           getAllMapValues();
       }
       return myObj;
	 }

	
	
}


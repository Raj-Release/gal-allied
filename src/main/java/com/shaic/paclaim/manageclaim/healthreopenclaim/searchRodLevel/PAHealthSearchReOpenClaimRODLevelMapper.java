package com.shaic.paclaim.manageclaim.healthreopenclaim.searchRodLevel;

import java.util.List;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Claim;
import com.shaic.domain.preauth.CloseClaim;
import com.shaic.newcode.wizard.domain.MappingUtil;
import com.shaic.paclaim.manageclaim.healthreopenclaim.pageRodLevel.PAHealthReOpenRodLevelClaimDTO;

/**
 * @author ntv.narenj
 *
 */
public class PAHealthSearchReOpenClaimRODLevelMapper {

	private static MapperFacade tableMapper;
	
	static PAHealthSearchReOpenClaimRODLevelMapper  myObj;

	
	private static BoundMapperFacade<Claim, PAHealthSearchReOpenClaimRodLevelTableDTO> reOpenClaimMapper;
	private static BoundMapperFacade<CloseClaim, PAHealthReOpenRodLevelClaimDTO> reOpenMapper;
	
	public static void getAllMapValues()  {
 
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Claim, PAHealthSearchReOpenClaimRodLevelTableDTO> claimClassMap = mapperFactor.classMap(Claim.class, PAHealthSearchReOpenClaimRodLevelTableDTO.class);
		ClassMapBuilder<CloseClaim, PAHealthReOpenRodLevelClaimDTO> reOpenMap = mapperFactor.classMap(CloseClaim.class, PAHealthReOpenRodLevelClaimDTO.class);
		
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
		
		reOpenClaimMapper = mapperFactor.getMapperFacade(Claim.class, PAHealthSearchReOpenClaimRodLevelTableDTO.class);
		reOpenMapper = mapperFactor.getMapperFacade(CloseClaim.class, PAHealthReOpenRodLevelClaimDTO.class);
	}
	
	public static List<PAHealthSearchReOpenClaimRodLevelTableDTO> getClaimDTO(List<Claim> claimData){
		List<PAHealthSearchReOpenClaimRodLevelTableDTO> mapAsList = 
										tableMapper.mapAsList(claimData, PAHealthSearchReOpenClaimRodLevelTableDTO.class);
		return mapAsList;
		
	}
	
    public static PAHealthSearchReOpenClaimRodLevelTableDTO getSearchClaimDTO(Claim claim){
		
		PAHealthSearchReOpenClaimRodLevelTableDTO dto = reOpenClaimMapper.map(claim);
		
		return dto;
	}
    
   public static PAHealthReOpenRodLevelClaimDTO getReOpenClaimDTO(CloseClaim result){
		
	   PAHealthReOpenRodLevelClaimDTO dto = reOpenMapper.map(result);
		
	   return dto;
	}
   
   public static PAHealthSearchReOpenClaimRODLevelMapper getInstance(){
       if(myObj == null){
           myObj = new PAHealthSearchReOpenClaimRODLevelMapper();
           getAllMapValues();
       }
       return myObj;
	 }

	
	
}


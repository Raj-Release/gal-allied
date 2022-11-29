package com.shaic.reimbursement.manageclaim.reopenclaim.searchRodLevel;

import java.util.List;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Claim;
import com.shaic.domain.preauth.CloseClaim;
import com.shaic.newcode.wizard.domain.MappingUtil;
import com.shaic.reimbursement.manageclaim.reopenclaim.pageRodLevel.ReOpenRodLevelClaimDTO;

/**
 * @author ntv.narenj
 *
 */
public class SearchReOpenClaimRODLevelMapper {

	private static MapperFacade tableMapper;
	
	static SearchReOpenClaimRODLevelMapper  myObj;

	
	private static BoundMapperFacade<Claim, SearchReOpenClaimRodLevelTableDTO> reOpenClaimMapper;
	private static BoundMapperFacade<CloseClaim, ReOpenRodLevelClaimDTO> reOpenMapper;
	
	public static void getAllMapValues()  {
 
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Claim, SearchReOpenClaimRodLevelTableDTO> claimClassMap = mapperFactor.classMap(Claim.class, SearchReOpenClaimRodLevelTableDTO.class);
		ClassMapBuilder<CloseClaim, ReOpenRodLevelClaimDTO> reOpenMap = mapperFactor.classMap(CloseClaim.class, ReOpenRodLevelClaimDTO.class);
		
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
		
		reOpenClaimMapper = mapperFactor.getMapperFacade(Claim.class, SearchReOpenClaimRodLevelTableDTO.class);
		reOpenMapper = mapperFactor.getMapperFacade(CloseClaim.class, ReOpenRodLevelClaimDTO.class);
	}
	
	public static List<SearchReOpenClaimRodLevelTableDTO> getClaimDTO(List<Claim> claimData){
		List<SearchReOpenClaimRodLevelTableDTO> mapAsList = 
										tableMapper.mapAsList(claimData, SearchReOpenClaimRodLevelTableDTO.class);
		return mapAsList;
		
	}
	
    public static SearchReOpenClaimRodLevelTableDTO getSearchClaimDTO(Claim claim){
		
		SearchReOpenClaimRodLevelTableDTO dto = reOpenClaimMapper.map(claim);
		
		return dto;
	}
    
   public static ReOpenRodLevelClaimDTO getReOpenClaimDTO(CloseClaim result){
		
	   ReOpenRodLevelClaimDTO dto = reOpenMapper.map(result);
		
	   return dto;
	}
   
   public static SearchReOpenClaimRODLevelMapper getInstance(){
       if(myObj == null){
           myObj = new SearchReOpenClaimRODLevelMapper();
           getAllMapValues();
       }
       return myObj;
	 }

	
	
}


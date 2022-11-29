package com.shaic.claim.cashlessprocess.flagreconsiderationrequest.search;

import java.util.List;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Claim;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.ReimbursementRejection;
import com.shaic.newcode.wizard.domain.MappingUtil;
import com.shaic.reimbursement.manageclaim.reopenclaim.sarchClaimLevel.SearchReOpenClaimMapper;
import com.shaic.reimbursement.manageclaim.reopenclaim.sarchClaimLevel.SearchReOpenClaimTableDTO;

public class FlagReconsiderationReqMapper {
	
private static MapperFacade tableMapper;
	
	static FlagReconsiderationReqMapper  myObj;
	
	public static BoundMapperFacade<Reimbursement,SearchFlagReconsiderationReqTableDTO> flagreconsiderationMapper;
	
	public static void getAllMapValues() {
 
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Reimbursement, SearchFlagReconsiderationReqTableDTO> claimClassMap = mapperFactor.classMap(Reimbursement.class, SearchFlagReconsiderationReqTableDTO.class);
		
		claimClassMap.field("key", "rodKey");
		claimClassMap.field("rodNumber", "rodNumber");
		claimClassMap.field("approvedAmount", "approvedAmount");
		claimClassMap.field("status.key", "statusKey");
		claimClassMap.field("status.processValue", "rodStatus");
		claimClassMap.field("docAcknowLedgement.key", "docAcknowLedgementKey");
		claimClassMap.field("claim.intimation.intimationId", "intimationNumber");
		claimClassMap.field("reconsiderationFlagReq", "flagStatus");
		claimClassMap.field("reconsiderationFlagRemark", "rejectRemarks");
		
		
		claimClassMap.register();
		
		tableMapper = mapperFactor.getMapperFacade();
		
		flagreconsiderationMapper = mapperFactor.getMapperFacade(Reimbursement.class, SearchFlagReconsiderationReqTableDTO.class);
	}
	
	public static List<SearchFlagReconsiderationReqTableDTO> getRejectionDTO(List<Reimbursement> claimData){
		List<SearchFlagReconsiderationReqTableDTO> mapAsList = 
										tableMapper.mapAsList(claimData, SearchFlagReconsiderationReqTableDTO.class);
		return mapAsList;
		
	}
	
	public static FlagReconsiderationReqMapper getInstance(){
        if(myObj == null){
            myObj = new FlagReconsiderationReqMapper();
            getAllMapValues();
        }
        return myObj;
	 }

}

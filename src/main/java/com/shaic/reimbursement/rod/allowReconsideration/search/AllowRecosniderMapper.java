package com.shaic.reimbursement.rod.allowReconsideration.search;

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

public class AllowRecosniderMapper {
	
private static MapperFacade tableMapper;
	
	static AllowRecosniderMapper  myObj;
	
	public static BoundMapperFacade<Reimbursement,SearchAllowReconsiderationTableDTO> allowreconsiderMapper;
	
	public static void getAllMapValues() {
 
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Reimbursement, SearchAllowReconsiderationTableDTO> claimClassMap = mapperFactor.classMap(Reimbursement.class, SearchAllowReconsiderationTableDTO.class);
		
		claimClassMap.field("claim.intimation.intimationId", "intimationNo");
		claimClassMap.field("claim.claimType.value", "claimType");
		claimClassMap.field("rodNumber", "rodNo");
		claimClassMap.field("claim.intimation.policy.policyNumber", "policyNo");
		claimClassMap.field("claim.intimation.cpuCode.cpuCode", "cpuId");
		claimClassMap.field("claim.intimation.cpuCode.description", "description");
		claimClassMap.field("key", "rodKey");
		claimClassMap.field("status.key", "statusKey");
		
		claimClassMap.register();
		
		tableMapper = mapperFactor.getMapperFacade();
		
		allowreconsiderMapper = mapperFactor.getMapperFacade(Reimbursement.class, SearchAllowReconsiderationTableDTO.class);
	}
	
	public static List<SearchAllowReconsiderationTableDTO> getRejectionDTO(List<Reimbursement> claimData){
		List<SearchAllowReconsiderationTableDTO> mapAsList = 
										tableMapper.mapAsList(claimData, SearchAllowReconsiderationTableDTO.class);
		return mapAsList;
		
	}
	
	public static AllowRecosniderMapper getInstance(){
        if(myObj == null){
            myObj = new AllowRecosniderMapper();
            getAllMapValues();
        }
        return myObj;
	 }

}

package com.shaic.claim.OMPReopenClaimRODLevel.SearchBased.search;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.OMPClaim;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class OMPReopenClaimRODLevelSearchBasedMapper {
	
public static MapperFacade tableMapper;
	
	static OMPReopenClaimRODLevelSearchBasedMapper myObj;
	
	public static void getAllMapValues(){
		
		MapperFactory mapperFactory =  MappingUtil.getMapperFactory(true);
		ClassMapBuilder<OMPClaim,OMPReopenClaimRODLevelSearchBasedTableDTO> reOpenRODClassMap = mapperFactory.classMap(OMPClaim.class, OMPReopenClaimRODLevelSearchBasedTableDTO.class);
		
		reOpenRODClassMap.field("intimation.key", "serialNo");
		reOpenRODClassMap.field("intimation.intimationId","intimationNo");
		reOpenRODClassMap.field("claim.claimId", "claimno");
		reOpenRODClassMap.field("policy.policyNumber", "policyno");
		reOpenRODClassMap.field("intimation.insuredName", "insuredName");
		reOpenRODClassMap.field("hospitalType.hospitalName", "hospitalname");
		reOpenRODClassMap.field("intimation.eventCodeId", "eventCode");
		reOpenRODClassMap.field("", "classification");
		reOpenRODClassMap.field("", "subclassification");
		reOpenRODClassMap.field("", "dateofloss");
		
		
		reOpenRODClassMap.register();
		
		tableMapper = mapperFactory.getMapperFacade();
		
	}

	public static List<OMPReopenClaimRODLevelSearchBasedTableDTO> getIntimationDTO(List<OMPClaim> intimationData){
		List<OMPReopenClaimRODLevelSearchBasedTableDTO> mapAsList = tableMapper.mapAsList(intimationData, OMPReopenClaimRODLevelSearchBasedTableDTO.class);
		return mapAsList;
	}
	
	public static OMPReopenClaimRODLevelSearchBasedMapper getInstance(){
		if(myObj == null){
			myObj = new OMPReopenClaimRODLevelSearchBasedMapper();
		}
		return myObj;
	}

}


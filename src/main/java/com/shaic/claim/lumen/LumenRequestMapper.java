package com.shaic.claim.lumen;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.lumen.create.LumenRequestDTO;
import com.shaic.domain.LumenRequest;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class LumenRequestMapper {

	private static MapperFacade tableMapper;

	static LumenRequestMapper  myObj;

	public static void getAllMapValues()  {
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<LumenRequest, LumenRequestDTO> classMap = mapperFactory.classMap(LumenRequest.class, LumenRequestDTO.class);
		classMap.field("lumenRemarks", "comments");
		classMap.field("lumenInitiatedDate", "initiatedDate");
		classMap.field("createdBy", "loginId");
		classMap.field("requestedStageId.key", "requestIntiatedId");
		
		classMap.field("claim", "claim");
		classMap.field("claim.intimation.cpuCode.cpuCode","cpuCode");
		classMap.field("claim.intimation.cpuCode.description","cpuDesc");
		classMap.field("claim.intimation", "intimation");
		classMap.field("claim.intimation.policy", "policy");
		classMap.field("status", "status");
		classMap.field("stage", "stage");
		
		// Search Lumen table mapping	
		classMap.field("claim.intimation.intimationId", "intimationNumber");
		classMap.field("claim.intimation.policy.policyNumber", "policyNumber");
		classMap.field("claim.intimation.policy.productName", "productName");
		classMap.field("claim.intimation.policy.proposerFirstName", "insuredPatientName");
		classMap.field("claim.claimType.value", "claimType");
		classMap.field("status.userStatus", "lumenStatus");
		
		classMap.field("requestedStageId.stageName", "initiatedScreen");
		classMap.field("createdBy", "initiatedBy");

		//For Common Carousel
		classMap.field("lumenRefNumber", "caseReferenceNumber");
		
		//lumen functional screens
		classMap.field("remarks", "remarks");
		classMap.field("level1ApprovalRemarks", "level1ApprovalRemarks");
		classMap.field("level1RejectRemarks", "level1RejectRemarks");
		classMap.field("level1ReplyRemarks", "level1ReplyRemarks");		
		classMap.field("level1CloseRemarks", "level1CloseRemarks");
		classMap.field("coordApprovalRemarks", "coordApprovalRemarks");
		classMap.field("coordReplyRemarks", "coordReplyRemarks");
		classMap.field("generateLetter", "generateLetter");		
		classMap.field("level2ApprovalRemarks", "level2ApprovalRemarks");	
		classMap.field("level2RejectRemarks", "level2RejectRemarks");	
		classMap.field("level2ReplyRemarks", "level2ReplyRemarks");		
		classMap.field("level2CloseRemarks", "level2CloseRemarks");
		classMap.field("key", "lumenRequestKey");
		classMap.field("lumenType", "lumenTypeId");
		classMap.field("hospitalErrorType", "errorTypeId");
		
		classMap.register();
		tableMapper = mapperFactory.getMapperFacade();
	}

	public static List<LumenRequestDTO> getLumenDetails(List<LumenRequest> lumenRequestList) {
		List<LumenRequestDTO> mapAsList = tableMapper.mapAsList(lumenRequestList, LumenRequestDTO.class);
		return mapAsList;
	}

	public static LumenRequestMapper getInstance(){
		if(myObj == null){
			myObj = new LumenRequestMapper();
			getAllMapValues();
		}
		return myObj;
	}
}

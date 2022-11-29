package com.shaic.claim.enhacement.table;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.ViewTmpPreauth;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class PreviousPreAuthMapper {
	
	private static MapperFacade tableMapper;
	
	static PreviousPreAuthMapper myObj;

	 public static void getAllMapValues()  {
		 
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Preauth, PreviousPreAuthTableDTO> classMap = mapperFactory
				.classMap(Preauth.class, PreviousPreAuthTableDTO.class);
		
		ClassMapBuilder<ViewTmpPreauth, PreviousPreAuthTableDTO> classMap1 = mapperFactory
				.classMap(ViewTmpPreauth.class, PreviousPreAuthTableDTO.class);
		
		classMap.field("key", "key");
		classMap.field("preauthId", "referenceNo");
		classMap.field("enhancementType", "referenceType");
		classMap.field("treatmentType.value", "treatementType");
		classMap.field("totalApprovalAmount", "approvedAmt");
		classMap.field("approvedAmtAftPremium", "approvedAmtAftPremiumDeduction");
		classMap.field("intimation", "intimation");
		classMap.field("diffAmount", "diffAmount");
		classMap.field("processType", "processFlag");
		classMap.field("claim.key", "claimKey");
		classMap.field("createdDate", "createdDate");
		classMap.field("modifiedDate", "modifiedDate");
		classMap.field("status.key", "statusKey");
		classMap.field("status.processValue", "status");
		classMap1.field("key", "key");
		classMap1.field("preauthId", "referenceNo");
		classMap1.field("enhancementType", "referenceType");
		classMap1.field("treatmentType.value", "treatementType");
		classMap1.field("totalApprovalAmount", "approvedAmt");
		classMap1.field("intimation", "intimation");
		classMap1.field("diffAmount", "diffAmount");
		classMap1.field("processType", "processFlag");
		classMap1.field("claim.key", "claimKey");
		classMap1.field("createdDate", "createdDate");
		classMap1.field("status.key", "statusKey");
		classMap1.field("status.processValue", "status");
		classMap1.field("treatmentRemarks", "treatmentRemarks");
		classMap1.field("remarks", "remarks");
		classMap1.field("medicalRemarks", "medicalRemarks");
		classMap1.field("status.portalStatus", "portalStatusVal");
		classMap1.field("status.websiteStatus", "websiteStatusVal");
		//classMap1.field("modifiedDate", "modifiedDate");
		classMap1.register();
		
		classMap.register();
		tableMapper = mapperFactory.getMapperFacade();
	}

	public static List<PreviousPreAuthTableDTO> getProcessEnhacementTableDTO(
			List<Preauth> processEnhacementTableList) {
		List<PreviousPreAuthTableDTO> mapAsList = tableMapper.mapAsList(
				processEnhacementTableList, PreviousPreAuthTableDTO.class);
		return mapAsList;
	}
	
	public static List<PreviousPreAuthTableDTO> getProcessEnhacementTableDTOFromTmpPreauth(
			List<ViewTmpPreauth> processEnhacementTableList) {
		List<PreviousPreAuthTableDTO> mapAsList = tableMapper.mapAsList(
				processEnhacementTableList, PreviousPreAuthTableDTO.class);
		return mapAsList;
	}
	
	public static PreviousPreAuthMapper getInstance(){
        if(myObj == null){
            myObj = new PreviousPreAuthMapper();
            getAllMapValues();
        }
        return myObj;
	 }

}

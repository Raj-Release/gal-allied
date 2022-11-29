package com.shaic.claim.OMPProcessOmpClaimProcessor.search;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.OMPClaim;
import com.shaic.domain.OMPIntimation;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class OMPProcessOmpClaimProcessorMapper {
	
private static MapperFacade tableMapper;

private static MapperFactory mapperFactory =  MappingUtil.getMapperFactory(false);

static OMPProcessOmpClaimProcessorMapper  myObj;

private static ClassMapBuilder<OMPClaim, OMPProcessOmpClaimProcessorTableDTO> classMapForIntimation = mapperFactory.classMap(OMPClaim.class, OMPProcessOmpClaimProcessorTableDTO.class);
	
public static void getAllMapValues()  {

				
			classMapForIntimation.field("key","key");
			classMapForIntimation.field("intimation.intimationId","intimationNo");
			classMapForIntimation.field("intimation.policy.policyNumber", "policyno");
			classMapForIntimation.field("intimation.insured.insuredName", "insuredName");
			classMapForIntimation.field("lossDateTime", "lossdate");
			classMapForIntimation.field("event.eventCode", "eventcode");
			classMapForIntimation.field("ailmentLoss", "ailment");
			classMapForIntimation.field("claimId", "claimno");
			classMapForIntimation.field("event.eventDescription", "eventDesc");
			classMapForIntimation.field("lossDetails", "lossDetail");
			classMapForIntimation.field("nonHospitalisationFlag", "nonHospitalizationFlag");
			
			
			
			classMapForIntimation.register();
			tableMapper = mapperFactory.getMapperFacade();
		
	}

	@SuppressWarnings("unused")
	public static  List<OMPProcessOmpClaimProcessorTableDTO> getOMPProcessOmpClaimProcessorTableDTOForIntimation(List<OMPClaim> searchProcessRejectionListForIntimation)
	{
		@SuppressWarnings("unchecked")
		List<OMPProcessOmpClaimProcessorTableDTO> mapAsList = tableMapper.mapAsList(searchProcessRejectionListForIntimation, OMPProcessOmpClaimProcessorTableDTO.class);
		//List<OMPProcessOmpClaimProcessorTableDTO> mapAsList = tableMapper.mapAsList(searchProcessRejectionListForIntimation, OMPProcessOmpClaimProcessorTableDTO.class);
		return mapAsList;
	}
	
	
	
	public static OMPProcessOmpClaimProcessorMapper getInstance(){
        if(myObj == null){
            myObj = new OMPProcessOmpClaimProcessorMapper();
            getAllMapValues();
        }
        return myObj;
	 }

	
	
	
}

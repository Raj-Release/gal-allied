package com.shaic.claim.OMPProcessOmpClaimApprover.search;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.OMPClaim;
import com.shaic.domain.OMPIntimation;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class OMPProcessOmpClaimApproverMapper {
	private static MapperFacade tableMapper;

	private static MapperFactory mapperFactory =  MappingUtil.getMapperFactory(false);

	static OMPProcessOmpClaimApproverMapper  myObj;
	
	
	private static ClassMapBuilder<OMPClaim, OMPProcessOmpClaimApproverTableDTO> classMapForIntimation = mapperFactory.classMap(OMPClaim.class, OMPProcessOmpClaimApproverTableDTO.class);
	
	public static void getAllMapValues()  {

				classMapForIntimation.field("key","key");
				classMapForIntimation.field("intimation.intimationId","intimationNo");
				classMapForIntimation.field("intimation.policy.policyNumber", "policyno");
				classMapForIntimation.field("intimation.insured.insuredName", "insuredName");
				classMapForIntimation.field("ailmentLoss", "ailment");
				classMapForIntimation.field("claimId", "claimno");
				classMapForIntimation.field("event.eventCode","eventcode");
				classMapForIntimation.field("event.eventDescription", "eventDesc");
				/*classMapForIntimation.field("intimation.intimationId", "intimationNo");
				classMapForIntimation.field("intimation.policy.policyNumber", "policyno");
				classMapForIntimation.field("intimation.policy.proposerFirstName", "insuredName");
//				classMapForIntimation.field("intimation.lossDateTime", "lossDate");
				classMapForIntimation.field("intimation.ailmentLoss", "ailment");*/
				classMapForIntimation.field("lossDetails", "lossDetail");
				classMapForIntimation.field("nonHospitalisationFlag", "nonHospitalizationFlag");
				
				classMapForIntimation.register();
				tableMapper = mapperFactory.getMapperFacade();
			
		}
	
	@SuppressWarnings("unused")
	public static  List<OMPProcessOmpClaimApproverTableDTO> getOMPProcessOmpClaimApproverTableDTOForIntimation(List<OMPIntimation> searchProcessRejectionListForIntimation)
	{
		@SuppressWarnings("unchecked")
		List<OMPProcessOmpClaimApproverTableDTO> mapAsList = tableMapper.mapAsList(searchProcessRejectionListForIntimation, OMPProcessOmpClaimApproverTableDTO.class);
		//List<OMPProcessOmpClaimProcessorTableDTO> mapAsList = tableMapper.mapAsList(searchProcessRejectionListForIntimation, OMPProcessOmpClaimProcessorTableDTO.class);
		return mapAsList;
	}
	
	public static List<OMPProcessOmpClaimApproverTableDTO> getIntimationDTO(List<OMPClaim> intimationData){
		List<OMPProcessOmpClaimApproverTableDTO> mapAsList = 
										tableMapper.mapAsList(intimationData, OMPProcessOmpClaimApproverTableDTO.class);
		return mapAsList;
		
	}
	
	public static OMPProcessOmpClaimApproverMapper getInstance(){
        if(myObj == null){
            myObj = new OMPProcessOmpClaimApproverMapper();
            getAllMapValues();
        }
        return myObj;
	 }
}

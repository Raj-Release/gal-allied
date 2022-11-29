package com.shaic.claim.OMPProcessOmpAcknowledgementDocuments.Upload;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.OMPProcessOmpAcknowledgementDocuments.Upload.OMPProcessOmpAcknowledgementDocumentsTableDTO;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.OMPIntimation;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class OMPProcessOmpAcknowledgementDocumentsMapper {
	
private static MapperFacade tableMapper;

private static MapperFactory mapperFactory =  MappingUtil.getMapperFactory(false);

static OMPProcessOmpAcknowledgementDocumentsMapper  myObj;

private static ClassMapBuilder<OMPClaim, OMPProcessOmpAcknowledgementDocumentsTableDTO> classMapForAckIntimation = mapperFactory.classMap(OMPClaim.class, OMPProcessOmpAcknowledgementDocumentsTableDTO.class);
	
	public static OMPProcessOmpAcknowledgementDocumentsMapper getAckInstance(){
        if(myObj == null){
            myObj = new OMPProcessOmpAcknowledgementDocumentsMapper();
            getAckAllMapValues();
        }
        return myObj;
	 }

	@SuppressWarnings("unused")
	public static  List<OMPProcessOmpAcknowledgementDocumentsTableDTO> getOMPProcessOmpAcknowledgementDocumentsTableDTOForIntimation(List<OMPClaim> searchProcessRejectionListForIntimation)
	{
		@SuppressWarnings("unchecked")
		List<OMPProcessOmpAcknowledgementDocumentsTableDTO> mapAsList = tableMapper.mapAsList(searchProcessRejectionListForIntimation, OMPProcessOmpAcknowledgementDocumentsTableDTO.class);
		//List<OMPProcessOmpClaimProcessorTableDTO> mapAsList = tableMapper.mapAsList(searchProcessRejectionListForIntimation, OMPProcessOmpClaimProcessorTableDTO.class);
		return mapAsList;
	}

	public static void getAckAllMapValues()  {


		classMapForAckIntimation.field("key","key");
		classMapForAckIntimation.field("intimation.intimationId","intimationNo");
		classMapForAckIntimation.field("intimation.policy.policyNumber", "policyno");
		classMapForAckIntimation.field("intimation.insured.insuredName", "insuredName");
		classMapForAckIntimation.field("lossDateTime", "lossdate");
		classMapForAckIntimation.field("event.eventCode", "eventcode");
		classMapForAckIntimation.field("ailmentLoss", "ailment");
		classMapForAckIntimation.field("claimId", "claimno");
		classMapForAckIntimation.field("event.eventDescription", "eventDesc");
		classMapForAckIntimation.field("lossDetails", "lossDetail");
		classMapForAckIntimation.field("nonHospitalisationFlag", "nonHospitalizationFlag");



		classMapForAckIntimation.register();
		tableMapper = mapperFactory.getMapperFacade();

	}
}

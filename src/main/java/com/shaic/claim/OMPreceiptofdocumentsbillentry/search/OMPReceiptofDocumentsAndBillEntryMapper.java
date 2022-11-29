package com.shaic.claim.OMPreceiptofdocumentsbillentry.search;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Intimation;
import com.shaic.domain.OMPClaim;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class OMPReceiptofDocumentsAndBillEntryMapper {
	
private static MapperFacade tableMapper;

private static MapperFactory mapperFactory =  MappingUtil.getMapperFactory(false);

static OMPReceiptofDocumentsAndBillEntryMapper  myObj;

private static ClassMapBuilder<OMPClaim, OMPReceiptofDocumentsAndBillEntryTableDTO> classMapForIntimation = mapperFactory.classMap(OMPClaim.class, OMPReceiptofDocumentsAndBillEntryTableDTO.class);
	
public static void getAllMapValues()  {

				
			//classMapForIntimation.field("key","intimationKey");
			classMapForIntimation.field("intimation.key", "intimationNo");
			classMapForIntimation.field("intimation.intimationId", "intimationNo");
			classMapForIntimation.field("intimation.policy.policyNumber", "policyno");
			classMapForIntimation.field("claimId", "claimno");
			classMapForIntimation.field("intimation.policy.proposerFirstName", "insuredPatientName");
			classMapForIntimation.field("intimation.lossDateTime", "lossDate");
			classMapForIntimation.field("ailmentLoss", "ailment");
			classMapForIntimation.field("event.eventDescription", "eventCode");
			classMapForIntimation.register();
			tableMapper = mapperFactory.getMapperFacade();
		
	}

	@SuppressWarnings("unused")
	public static  List<OMPReceiptofDocumentsAndBillEntryTableDTO> getOMPProcessReceiptofDocumentsAndBillTableDTOForIntimation(List<Intimation> searchProcessRejectionListForIntimation)
	{
		@SuppressWarnings("unchecked")
		List<OMPReceiptofDocumentsAndBillEntryTableDTO> mapAsList = tableMapper.mapAsList(searchProcessRejectionListForIntimation, OMPReceiptofDocumentsAndBillEntryTableDTO.class);
		//List<OMPProcessOmpClaimProcessorTableDTO> mapAsList = tableMapper.mapAsList(searchProcessRejectionListForIntimation, OMPProcessOmpClaimProcessorTableDTO.class);
		return mapAsList;
	}
	
	public static List<OMPReceiptofDocumentsAndBillEntryTableDTO> getIntimationDTO(List<OMPClaim> intimationData){
		List<OMPReceiptofDocumentsAndBillEntryTableDTO> mapAsList = 
										tableMapper.mapAsList(intimationData, OMPReceiptofDocumentsAndBillEntryTableDTO.class);
		return mapAsList;
		
	}
	
	
	public static OMPReceiptofDocumentsAndBillEntryMapper getInstance(){
        if(myObj == null){
            myObj = new OMPReceiptofDocumentsAndBillEntryMapper();
            getAllMapValues();
        }
        return myObj;
	 }

	
	
	
}

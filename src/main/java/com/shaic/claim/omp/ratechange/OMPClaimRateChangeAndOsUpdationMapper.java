package com.shaic.claim.omp.ratechange;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.OMPReimbursement;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class OMPClaimRateChangeAndOsUpdationMapper {
	
	
	
	private static MapperFacade tableMapper;

	private static MapperFactory mapperFactory =  MappingUtil.getMapperFactory(false);

	static OMPClaimRateChangeAndOsUpdationMapper  myObj;
	
	private static ClassMapBuilder<OMPReimbursement, OMPClaimRateChangeAndOsUpdationTableDTO> classMapForReimbursement = mapperFactory.classMap(OMPReimbursement.class, OMPClaimRateChangeAndOsUpdationTableDTO.class);
	
	public static void getAllMapValues()  {

				classMapForReimbursement.field("key","key");
				classMapForReimbursement.field("claim.intimation.intimationId","intimationNo");
				classMapForReimbursement.field("rodNumber", "rodNo");
				classMapForReimbursement.field("eventCodeId.eventDescription", "eventCode.value");
				classMapForReimbursement.field("classificationId", "classification");
				classMapForReimbursement.field("finalApprovedAmtUsd", "outstandingAmount");
				classMapForReimbursement.field("claim.intimation.intimationDate","intimationDate");
				classMapForReimbursement.field("inrConversionRate", "conversionRate");
				classMapForReimbursement.register();
				tableMapper = mapperFactory.getMapperFacade();
			
		}
	
	@SuppressWarnings("unused")
	public static  List<OMPClaimRateChangeAndOsUpdationTableDTO> getOMPClaimRateChangeAndOsUpdationTableDTOForIntimation(List<OMPReimbursement> searchProcessRejectionListForIntimation)
	{
		@SuppressWarnings("unchecked")
		List<OMPClaimRateChangeAndOsUpdationTableDTO> mapAsList = tableMapper.mapAsList(searchProcessRejectionListForIntimation, OMPClaimRateChangeAndOsUpdationTableDTO.class);
		//List<OMPProcessOmpClaimProcessorTableDTO> mapAsList = tableMapper.mapAsList(searchProcessRejectionListForIntimation, OMPProcessOmpClaimProcessorTableDTO.class);
		return mapAsList;
	}
	
	
	
	public static OMPClaimRateChangeAndOsUpdationMapper getInstance(){
        if(myObj == null){
            myObj = new OMPClaimRateChangeAndOsUpdationMapper();
        }
        getAllMapValues();
        return myObj;
	 }

}

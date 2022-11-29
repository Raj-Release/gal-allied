package com.shaic.claim.OMPreceiptofdocumentsbillentry.page;


import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimProcessorDTO;
import com.shaic.domain.OMPReimbursement;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class OMPRODBillEntryPageMapper {
	
	private static MapperFacade tableMapper;
	
	static OMPRODBillEntryPageMapper myObj;
	private static BoundMapperFacade<OMPReimbursement, OMPClaimProcessorDTO> rodBillEntryRecMapper;
	
	
	public static void getAllMapValues() {
		 
		//Added for Acknowledge doc received screen
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<OMPReimbursement,OMPClaimProcessorDTO> rodBillEntryMapper = mapperFactory.classMap(OMPReimbursement.class,OMPClaimProcessorDTO.class);	
		
		rodBillEntryMapper.field("key","key");
		rodBillEntryMapper.field("lossDateTime","lossDate");
		rodBillEntryMapper.field("hospitalName","hospName");
		rodBillEntryMapper.field("ailmentLoss","ailmentLoss");
		rodBillEntryMapper.field("cityName","hospCity");
		rodBillEntryMapper.field("placeVisit","placeOfVisit");
			
		
		rodBillEntryMapper.register();
		rodBillEntryRecMapper = mapperFactory.getMapperFacade(OMPReimbursement.class, OMPClaimProcessorDTO.class);
		tableMapper = mapperFactory.getMapperFacade();

}
	
	public   OMPReimbursement getRodBillEntryDetails(OMPClaimProcessorDTO ompclaimProcessorDto) {
		OMPReimbursement reimbursement = rodBillEntryRecMapper.mapReverse(ompclaimProcessorDto);
		return reimbursement;
	}
	
	public static OMPRODBillEntryPageMapper getInstance(){
        if(myObj == null){
            myObj = new OMPRODBillEntryPageMapper();
            getAllMapValues();
        }
        return myObj;
	 }

}
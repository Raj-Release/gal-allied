package com.shaic.claim.OMPProcessNegotiation.search;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.OMPIntimation;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class OMPProcessNegotiationMapper {
	
	
	private static MapperFacade tableMapper;

	private static MapperFactory mapperFactory =  MappingUtil.getMapperFactory(false);

	static OMPProcessNegotiationMapper  myObj;

	private static ClassMapBuilder<OMPIntimation, OMPProcessNegotiationTableDTO> classMapForIntimation = mapperFactory.classMap(OMPIntimation.class, OMPProcessNegotiationTableDTO.class);
		
	public static void getAllMapValues()  {

				classMapForIntimation.field("key","key");
//				classMapForIntimation.field("event","eventcode");
				classMapForIntimation.field("intimationId","intimationNo");
				classMapForIntimation.field("policy.policyNumber", "policyno");
				classMapForIntimation.field("insured.insuredName", "insuredName");
				classMapForIntimation.field("ailmentLoss", "ailment");
				classMapForIntimation.register();
				tableMapper = mapperFactory.getMapperFacade();
			
		}
	
	@SuppressWarnings("unused")
	public static  List<OMPProcessNegotiationTableDTO> getOMPProcessNegotiationTableDTOForIntimation(List<OMPIntimation> searchProcessRejectionListForIntimation)
	{
		@SuppressWarnings("unchecked")
		List<OMPProcessNegotiationTableDTO> mapAsList = tableMapper.mapAsList(searchProcessRejectionListForIntimation, OMPProcessNegotiationTableDTO.class);
		//List<OMPProcessOmpClaimProcessorTableDTO> mapAsList = tableMapper.mapAsList(searchProcessRejectionListForIntimation, OMPProcessOmpClaimProcessorTableDTO.class);
		return mapAsList;
	}
	
	
	
	public static OMPProcessNegotiationMapper getInstance(){
        if(myObj == null){
            myObj = new OMPProcessNegotiationMapper();
            getAllMapValues();
        }
        return myObj;
	 }

}

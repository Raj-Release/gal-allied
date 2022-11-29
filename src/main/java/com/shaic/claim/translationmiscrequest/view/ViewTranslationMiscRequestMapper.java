package com.shaic.claim.translationmiscrequest.view;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.preauth.Coordinator;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class ViewTranslationMiscRequestMapper {
	
	private static MapperFacade tableMapper;
	
	static ViewTranslationMiscRequestMapper  myObj;


	public static void getAllMapValues()  {
 
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Coordinator, ViewTranslationMiscRequestTableDTO> classMap = mapperFactory
				.classMap(Coordinator.class,
						ViewTranslationMiscRequestTableDTO.class);	
		classMap.field("key", "key");
		classMap.field("coordinatorRequestType.value", "requestType");
		classMap.field("requestorRemarks", "requestorRemarks");
//		classMap.field("fileUpload", "viewFile");
		classMap.field("modifiedBy", "coordinatorRepliedID");
		classMap.field("createdBy", "requestorRole");
		classMap.field("createdDate", "requestedDate");
		classMap.field("coordinatorReplyDate", "repliedDate");
		//classMap.field("fileUpload", "viewFile");
		classMap.field("createdBy", "requestorNameID");
		classMap.field("coordinatorRemarks", "coordinatorRemarks");			
		classMap.register();
		tableMapper = mapperFactory.getMapperFacade();
	}

	public static List<ViewTranslationMiscRequestTableDTO> getViewCoOrdinatorDTO(
			List<Coordinator> coordinatorList) {
		List<ViewTranslationMiscRequestTableDTO> mapAsList = tableMapper.mapAsList(
				coordinatorList, ViewTranslationMiscRequestTableDTO.class);
		return mapAsList;
	}
	
	public static ViewTranslationMiscRequestMapper getInstance(){
        if(myObj == null){
            myObj = new ViewTranslationMiscRequestMapper();
            getAllMapValues();
        }
        return myObj;
	 }

}

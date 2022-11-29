package com.shaic.claim.coordinator.view;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.preauth.Coordinator;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class ViewCoOrdinatorReplyMapper {
	
	private static MapperFacade tableMapper;
	
	static ViewCoOrdinatorReplyMapper  myObj;


	public static void getAllMapValues()  {
 
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Coordinator, ViewCoOrdinatorDTO> classMap = mapperFactory
				.classMap(Coordinator.class,
						ViewCoOrdinatorDTO.class);	
		classMap.field("key", "key");
		classMap.field("coordinatorRequestType.value", "requestType");
		classMap.field("requestorRemarks", "requestorRemarks");
//		classMap.field("fileName", "viewFile");
		classMap.field("modifiedBy", "coOrdinatorRepliedId");
		classMap.field("createdBy", "requestorRole");
		classMap.field("coordinatorReplyDate", "repliedDate");
		classMap.field("coordinatorRemarks", "coOrdinatorRemarks");
		classMap.field("createdDate", "requestedDate");
		classMap.field("createdBy", "requestroNameId");
		classMap.register();
		tableMapper = mapperFactory.getMapperFacade();
	}

	public static List<ViewCoOrdinatorDTO> getViewCoOrdinatorDTO(
			List<Coordinator> coordinatorList) {
		List<ViewCoOrdinatorDTO> mapAsList = tableMapper.mapAsList(
				coordinatorList, ViewCoOrdinatorDTO.class);
		return mapAsList;
	}
	
	public static ViewCoOrdinatorReplyMapper getInstance(){
        if(myObj == null){
            myObj = new ViewCoOrdinatorReplyMapper();
            getAllMapValues();
        }
        return myObj;
	 }

}

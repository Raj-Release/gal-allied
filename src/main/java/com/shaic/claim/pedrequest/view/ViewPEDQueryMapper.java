package com.shaic.claim.pedrequest.view;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.preauth.wizard.dto.OldPedEndorsementDTO;
import com.shaic.domain.preauth.OldInitiatePedEndorsement;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class ViewPEDQueryMapper {
	
	private static MapperFacade tableMapper;
	
	static ViewPEDQueryMapper  myObj;


	public static void getAllMapValues()  {
 
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<OldInitiatePedEndorsement, OldPedEndorsementDTO> classMap = mapperFactory
				.classMap(OldInitiatePedEndorsement.class,
						OldPedEndorsementDTO.class);
		classMap.field("key", "key");
		classMap.field("pedSuggestion.value", "pedSuggestionName");
		//classMap.field("preExistingDisease.value", "pedName");
		classMap.field("pedName", "pedName");
		classMap.field("repudiationLetterDate", "repudiationLetterDate");
		classMap.field("remarks", "remarks");
		classMap.field("createdBy", "requestorId");
		classMap.field("createdDate", "requestedDate");
		classMap.field("status.processValue", "requestStatus");
		classMap.field("status.key","statusKey");
		classMap.field("intimation.intimationId","intimationNo");
		classMap.field("pedEffectiveFromDate","pedEffectiveFromDate");
		classMap.register();
		tableMapper = mapperFactory.getMapperFacade();
	}

	public static List<OldPedEndorsementDTO> getOldPedEndorsementDTO(
			List<OldInitiatePedEndorsement> oldPedEndorsementList) {
		List<OldPedEndorsementDTO> mapAsList = tableMapper.mapAsList(
				oldPedEndorsementList, OldPedEndorsementDTO.class);
		return mapAsList;
	}
	
	public static ViewPEDQueryMapper getInstance(){
        if(myObj == null){
            myObj = new ViewPEDQueryMapper();
            getAllMapValues();
        }
        return myObj;
	 }

}

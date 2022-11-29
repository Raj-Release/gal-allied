package com.shaic.claim.viewPedEndorsement;

import java.util.List;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.pedquery.PEDQueryDTO;
import com.shaic.claim.pedrequest.view.ViewPEDTableDTO;
import com.shaic.domain.preauth.NewInitiatePedEndorsement;
import com.shaic.domain.preauth.OldInitiatePedEndorsement;
import com.shaic.newcode.wizard.domain.MappingUtil;


public class PedEndorsementMapper {
	
	static MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
	
	private static MapperFacade newInitiateMapper;
	private static BoundMapperFacade<OldInitiatePedEndorsement, PEDQueryDTO> oldInitiateMapper;
	
	static PedEndorsementMapper myObj;
	
	
	private static ClassMapBuilder<NewInitiatePedEndorsement, ViewPEDTableDTO> newInitiateMap = mapperFactory.classMap(NewInitiatePedEndorsement.class, ViewPEDTableDTO.class);
	private static ClassMapBuilder<OldInitiatePedEndorsement, PEDQueryDTO> oldInitiateMap = mapperFactory.classMap(OldInitiatePedEndorsement.class, PEDQueryDTO.class);
	
	 public static void getAllMapValues()  {
	
		newInitiateMap.field("key", "key");
		newInitiateMap.field("pedCode", "pedCode.id");
		newInitiateMap.field("description", "description");
		newInitiateMap.field("icdChapterId", "icdChapter.id");
		newInitiateMap.field("icdBlockId", "icdBlock.id");
		newInitiateMap.field("icdCodeId", "icdCode.id");
		newInitiateMap.field("source.key", "source.id");
		newInitiateMap.field("source.value", "source.value");
		newInitiateMap.field("othesSpecify", "othersSpecify");
		newInitiateMap.field("doctorRemarks", "doctorRemarks");
		newInitiateMap.field("deletedFlag", "deletedFlag");
		newInitiateMap.field("pedExclusionFlag", "permanentExclusionFlag");
		
		oldInitiateMap.field("pedSuggestion.key", "pedSuggestion.id");
		oldInitiateMap.field("pedSuggestion.value", "pedSuggestion.value");
//		oldInitiateMap.field("preExistingDisease.key", "pedName.id");
//		oldInitiateMap.field("preExistingDisease.value", "pedName.value");
		oldInitiateMap.field("pedName","pedName");
		oldInitiateMap.field("repudiationLetterDate", "repudiationLetterDate");
		oldInitiateMap.field("remarks", "remarks");
		
		oldInitiateMap.field("addWatchListFlag", "addWatchListFlag");
		oldInitiateMap.field("watchListRmrks", "watchlistRemarks");
		
		oldInitiateMap.field("uwTlFlag", "discussedFlag");
		oldInitiateMap.field("uwSuggestion", "discussRemarks");
		oldInitiateMap.field("uwDiscussWith.key", "discussWith.id");
		oldInitiateMap.field("uwDiscussWith.value", "discussWith.value");

		newInitiateMap.register();
		oldInitiateMap.register();
		
		newInitiateMapper = mapperFactory.getMapperFacade();
		oldInitiateMapper = mapperFactory.getMapperFacade(OldInitiatePedEndorsement.class, PEDQueryDTO.class);
		
		
	}
	public List<NewInitiatePedEndorsement> getNewInitiatePedEndorsementList(List<ViewPEDTableDTO> bean) {
		List<NewInitiatePedEndorsement> mapAsList = newInitiateMapper.mapAsList(bean, NewInitiatePedEndorsement.class);
		return mapAsList;
	}
	
	public List<ViewPEDTableDTO> getPedInitiateDetailsDTOList(List<NewInitiatePedEndorsement> newInitiatePedEndorsement){
		List<ViewPEDTableDTO> mapAsList = newInitiateMapper.mapAsList(newInitiatePedEndorsement, ViewPEDTableDTO.class);
		return mapAsList;
	}
	
	public OldInitiatePedEndorsement getOldInitiatePedEndorsement(PEDQueryDTO initiateDTO) {
		OldInitiatePedEndorsement dest = oldInitiateMapper.mapReverse(initiateDTO);
		return dest;
	}
	
	public static PedEndorsementMapper getInstance(){
        if(myObj == null){
            myObj = new PedEndorsementMapper();
            getAllMapValues();
        }
        return myObj;
	 }
}

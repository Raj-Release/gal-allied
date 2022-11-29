package com.shaic.claim.pedquery.viewPedDetails;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.preauth.NewInitiatePedEndorsement;
import com.shaic.domain.preauth.PedEndorsementDetailsHistory;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class ViewPEDEndoresementDetailsMapper {
	
	private static MapperFacade tableMapper;
	
	static ViewPEDEndoresementDetailsMapper  myObj;


	public static void getAllMapValues()  {
 
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<NewInitiatePedEndorsement, ViewPEDEndoresementDetailsDTO> classMap = mapperFactory
				.classMap(NewInitiatePedEndorsement.class,
						ViewPEDEndoresementDetailsDTO.class);
		
		
		ClassMapBuilder<PedEndorsementDetailsHistory, ViewPEDEndoresementDetailsDTO> classMap1 = mapperFactory
				.classMap(PedEndorsementDetailsHistory.class,
						ViewPEDEndoresementDetailsDTO.class);
		
		classMap.field("key", "key");
		classMap.field("pedCode", "pedCode");
		classMap.field("description", "description");
		classMap.field("icdChapterId", "icdChapter");
		classMap.field("icdBlockId", "icdBlock");		
		classMap.field("icdCodeId", "icdCode");
		classMap.field("source.value","source");
		classMap.field("othesSpecify", "othersSpecify");
		classMap.field("doctorRemarks", "doctorRemarks");
		
		
		classMap1.field("key", "key");
		classMap1.field("pedCode", "pedCode");
		classMap1.field("description", "description");
		classMap1.field("icdChapterId", "icdChapter");
		classMap1.field("icdBlockId", "icdBlock");		
		classMap1.field("icdCodeId", "icdCode");
		classMap1.field("source.value","source");
		classMap1.field("othesSpecify", "othersSpecify");
		classMap1.field("doctorRemarks", "doctorRemarks");
		
		classMap.register();
		tableMapper = mapperFactory.getMapperFacade();
	}

	public static List<ViewPEDEndoresementDetailsDTO> getSearchPEDQueryTableDTOTableDTO(
			List<NewInitiatePedEndorsement> pedEndoresementDetailsList) {
		List<ViewPEDEndoresementDetailsDTO> mapAsList = tableMapper.mapAsList(
				pedEndoresementDetailsList, ViewPEDEndoresementDetailsDTO.class);
		return mapAsList;
	}
	
	public static List<ViewPEDEndoresementDetailsDTO> getPedEndorsmentHistoryDetails(
			List<PedEndorsementDetailsHistory> pedEndoresementDetailsList) {
		List<ViewPEDEndoresementDetailsDTO> mapAsList = tableMapper.mapAsList(
				pedEndoresementDetailsList, ViewPEDEndoresementDetailsDTO.class);
		return mapAsList;
	}
	
	public static NewInitiatePedEndorsement getNewInitiatePedEndorsement(ViewPEDEndoresementDetailsDTO viewPEDEndoresementDetailsDTO) {
		NewInitiatePedEndorsement dest = tableMapper.map(viewPEDEndoresementDetailsDTO, NewInitiatePedEndorsement.class);
		return dest;
	}

	public static ViewPEDEndoresementDetailsDTO getViewPEDEndoresementDetailsDTO(NewInitiatePedEndorsement pedEndoresementDetailsList) {
		ViewPEDEndoresementDetailsDTO dest = tableMapper.map(pedEndoresementDetailsList, ViewPEDEndoresementDetailsDTO.class);
		return dest;
	}
	
	public static ViewPEDEndoresementDetailsMapper getInstance(){
        if(myObj == null){
            myObj = new ViewPEDEndoresementDetailsMapper();
            getAllMapValues();
        }
        return myObj;
	 }

}

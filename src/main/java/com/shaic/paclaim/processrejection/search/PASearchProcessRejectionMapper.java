package com.shaic.paclaim.processrejection.search;

import java.util.ArrayList;
import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.processrejection.search.SearchProcessRejectionTableDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.Intimation;
import com.shaic.domain.preauth.Preauth;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class PASearchProcessRejectionMapper {
	
private static MapperFacade tableMapper;

static PASearchProcessRejectionMapper  myObj;

	
public static void getAllMapValues()  {

	
		/*MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Preauth, SearchProcessRejectionTableDTO> classMap = mapperFactory.classMap(Preauth.class, SearchProcessRejectionTableDTO.class);*/
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Intimation, SearchProcessRejectionTableDTO> classMapForIntimation = mapperFactory.classMap(Intimation.class, SearchProcessRejectionTableDTO.class);
		ClassMapBuilder<Preauth, SearchProcessRejectionTableDTO> classMapForPreauth = mapperFactory.classMap(Preauth.class, SearchProcessRejectionTableDTO.class);
		ClassMapBuilder<Claim, SearchProcessRejectionTableDTO> classMapForClaim = mapperFactory.classMap(Claim.class, SearchProcessRejectionTableDTO.class);
		

	/*	classMapForPreauth.field("key", "key");
		classMapForPreauth.field("intimation.intimationId","intimationNo");
		classMapForPreauth.field("intimation.createdDate","intimationDate");
		classMapForPreauth.field("intimation.hospitalType","hospitalType");
		classMapForPreauth.field("intimation.status","status");*/
		classMapForPreauth.field("intimation.key", "key");
		classMapForPreauth.field("intimation.intimationId", "intimationNo");
		classMapForPreauth.field("status.processValue","preauthStatus");
		classMapForPreauth.field("intimation.createdDate","intimationDate");
		classMapForPreauth.field("intimation.hospitalType","hospitalType");
		classMapForPreauth.field("claim.suggestedRejectionRemarks","processRejectionDTO.rejectionRemarks");
		classMapForPreauth.field("claim.incidenceDate","processRejectionDTO.accDeathDate");
		classMapForPreauth.field("claim.incidenceFlag","accidentDeath");
		classMapForPreauth.field("claim.injuryRemarks","processRejectionDTO.injuryLossDetails");
		classMapForPreauth.field("status.processValue","status");
		
		classMapForClaim.field("intimation.key", "key");
		classMapForClaim.field("intimation.intimationId", "intimationNo");
		classMapForClaim.field("status.processValue","preauthStatus");
		classMapForClaim.field("intimation.createdDate","intimationDate");
		classMapForClaim.field("intimation.hospitalType","hospitalType");
		classMapForClaim.field("status.processValue","status");
		classMapForClaim.field("incidenceFlag", "accidentDeath");
		classMapForClaim.field("incidenceDate","processRejectionDTO.accDeathDate");
		classMapForClaim.field("injuryRemarks","processRejectionDTO.injuryLossDetails");
		classMapForClaim.field("suggestedRejectionRemarks","processRejectionDTO.rejectionRemarks");
		
		classMapForIntimation.field("key", "key");
		classMapForIntimation.field("intimationId","intimationNo");
		classMapForIntimation.field("createdDate","intimationDate");
		classMapForIntimation.field("hospitalType","hospitalType");
		classMapForIntimation.field("status.processValue","status");
		classMapForIntimation.field("incidenceFlag","accidentDeath");		
	/*	classMapForIntimation.field("status","preauthStatus");*/
		
		classMapForIntimation.register();
		classMapForPreauth.register();
		classMapForClaim.register();
		tableMapper = mapperFactory.getMapperFacade();
	}

	/*@SuppressWarnings("unused")
	public static  List<SearchProcessRejectionTableDTO> getSearchProcessRejectionTableDTO(List<Preauth> searchProcessRejectionList)
	{
		List<SearchProcessRejectionTableDTO> list = new ArrayList<SearchProcessRejectionTableDTO>();
		for(int i =1; i <=10; i++)
		{
			SearchProcessRejectionTableDTO item = new SearchProcessRejectionTableDTO();
			item.setHospitalType("Hospital Type" + i);
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			item.setIntimationDate(date);
			item.setIntimationNo("intimationNo" +i);
			item.setPreauthStatus("preauthStatus");
			item.setStatus("status");
			list.add(item);
		}
		return list;
		List<SearchProcessRejectionTableDTO> tableDTO = new ArrayList<SearchProcessRejectionTableDTO>();
		List<SearchProcessRejectionTableDTO> mapAsList = tableMapper.mapAsList(searchProcessRejectionList, SearchProcessRejectionTableDTO.class);
		return mapAsList;
	}*/
	@SuppressWarnings("unused")
	public static  List<SearchProcessRejectionTableDTO> getSearchProcessRejectionTableDTOForIntimation(List<Intimation> searchProcessRejectionListForIntimation)
	{
		List<SearchProcessRejectionTableDTO> list = new ArrayList<SearchProcessRejectionTableDTO>();
		List<SearchProcessRejectionTableDTO> tableDTO = new ArrayList<SearchProcessRejectionTableDTO>();
		List<SearchProcessRejectionTableDTO> mapAsList = tableMapper.mapAsList(searchProcessRejectionListForIntimation, SearchProcessRejectionTableDTO.class);
		return mapAsList;
	}
	
	public static  List<SearchProcessRejectionTableDTO> getSearchProcessRejectionTableDTOForPreauth(List<Preauth> searchProcessRejectionListForPreauth)
	{
		List<SearchProcessRejectionTableDTO> list = new ArrayList<SearchProcessRejectionTableDTO>();
		List<SearchProcessRejectionTableDTO> tableDTO = new ArrayList<SearchProcessRejectionTableDTO>();
		List<SearchProcessRejectionTableDTO> mapAsList = tableMapper.mapAsList(searchProcessRejectionListForPreauth, SearchProcessRejectionTableDTO.class);
		return mapAsList;
	}
	
	public static  List<SearchProcessRejectionTableDTO> getSearchProcessRejectionTableDTOForClaim(List<Claim> searchProcessRejectionListForPreauth)
	{
		List<SearchProcessRejectionTableDTO> list = new ArrayList<SearchProcessRejectionTableDTO>();
		List<SearchProcessRejectionTableDTO> tableDTO = new ArrayList<SearchProcessRejectionTableDTO>();
		List<SearchProcessRejectionTableDTO> mapAsList = tableMapper.mapAsList(searchProcessRejectionListForPreauth, SearchProcessRejectionTableDTO.class);
		return mapAsList;
	}
	
	
	
	public static PASearchProcessRejectionMapper getInstance(){
        if(myObj == null){
            myObj = new PASearchProcessRejectionMapper();
            getAllMapValues();
        }
        return myObj;
	 }
	
	
	
}

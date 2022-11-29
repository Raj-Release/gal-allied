package com.shaic.claim.preauth.procedurelist.view;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.preauth.Procedure;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class ProcedureListMapper {
	
	private static MapperFacade tableMapper;
	
	static ProcedureListMapper myObj;

	 public static void getAllMapValues()  { 
		 
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Procedure, ProcedureListTableDTO> classMap = mapperFactory
				.classMap(Procedure.class,
						ProcedureListTableDTO.class);
		classMap.field("procedureCode", "procedureCode");
		classMap.field("procedureName", "procedureName");
		classMap.field("packageRate", "packageRate");
		classMap.field("approvedAmount", "approvedRate");	
		classMap.field("newProceudreName", "newProceudreName");		
		classMap.field("speciality.value", "specialityValue");	
		classMap.register();
		tableMapper = mapperFactory.getMapperFacade();
	}

	public static List<ProcedureListTableDTO> getSearchPEDQueryTableDTOTableDTO(
			List<Procedure> pedSearchQueryList) {
		List<ProcedureListTableDTO> mapAsList = tableMapper.mapAsList(
				pedSearchQueryList, ProcedureListTableDTO.class);
		return mapAsList;
	}
	
	public static ProcedureListMapper getInstance(){
        if(myObj == null){
            myObj = new ProcedureListMapper();
            getAllMapValues();
        }
        return myObj;
	 }

}

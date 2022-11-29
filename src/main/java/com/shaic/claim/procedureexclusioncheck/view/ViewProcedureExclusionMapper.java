package com.shaic.claim.procedureexclusioncheck.view;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.preauth.Procedure;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class ViewProcedureExclusionMapper {
	
private static MapperFacade tableMapper;

static ViewProcedureExclusionMapper  myObj;

	
public static void getAllMapValues()  {

	
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Procedure, ViewProcedureExclusionCheckDTO> classMap = mapperFactory
				.classMap(Procedure.class,
						ViewProcedureExclusionCheckDTO.class);
		classMap.field("procedureName", "procedure");
		//classMap.field("diagnosisId", "waitingPeriod");
		classMap.field("policyAgeing", "policyAgeing");
		classMap.field("sublimitNameId", "subLimits");
		classMap.field("approvedAmount", "approvedAmt");
		classMap.register();
		tableMapper = mapperFactory.getMapperFacade();
	}
	
	public static List<ViewProcedureExclusionCheckDTO> getViewProcedureExclusionCheckDTO(
			List<Procedure> procudreExclusionList) {
		List<ViewProcedureExclusionCheckDTO> mapAsList = tableMapper.mapAsList(
				procudreExclusionList, ViewProcedureExclusionCheckDTO.class);
		return mapAsList;
	}
	
	public static ViewProcedureExclusionMapper getInstance(){
        if(myObj == null){
            myObj = new ViewProcedureExclusionMapper();
            getAllMapValues();
        }
        return myObj;
	 }

}

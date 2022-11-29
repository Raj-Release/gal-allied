package com.shaic.claim.procedureexclusioncheck.view;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.ViewTmpProcedure;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class ViewProcedureExclusionCheckMapper {
	
private static MapperFacade tableMapper;

static ViewProcedureExclusionCheckMapper  myObj;

	
public static void getAllMapValues()  {

		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<ViewTmpProcedure, ViewProcedureExclusionCheckDTO> classMap = mapperFactory
				.classMap(ViewTmpProcedure.class,
						ViewProcedureExclusionCheckDTO.class);
		classMap.field("procedureName", "procedure");		
		classMap.field("policyAgeing", "policyAgeing");		
		classMap.field("sublimitNameId", "subLimits");
		classMap.field("approvedAmount", "approvedAmt");
		classMap.field("procedureCode", "procedureCode");
		classMap.field("exculsionDetails.value", "exclusion");
		classMap.field("packageRate", "procedurePackageRate");
		classMap.field("dayCareProcedure", "dayCareProcedure");
		classMap.field("considerForDayCare", "considerForDayCare");
		classMap.field("subLimitApplicable", "subLimitApplicable");
		classMap.field("considerForPayment", "considerForPayment");
		classMap.field("procedureRemarks", "remarks");
		classMap.field("agreedPkgRate", "procedureAgreedPackageRate");
		classMap.field("reasonPkgRateChg", "reasonPkgRateChange");
		classMap.field("newProceudreName", "newProcedureName");	
		classMap.field("speciality.value", "speciality");	
		classMap.register();
		tableMapper = mapperFactory.getMapperFacade();
	}
	
	public static List<ViewProcedureExclusionCheckDTO> getViewProcedureExclusionCheckDTO(
			List<ViewTmpProcedure> procudreExclusionList) {
		List<ViewProcedureExclusionCheckDTO> mapAsList = tableMapper.mapAsList(
				procudreExclusionList, ViewProcedureExclusionCheckDTO.class);
		return mapAsList;
	}

	
	public static ViewProcedureExclusionCheckMapper getInstance(){
        if(myObj == null){
            myObj = new ViewProcedureExclusionCheckMapper();
            getAllMapValues();
        }
        return myObj;
	 }
}

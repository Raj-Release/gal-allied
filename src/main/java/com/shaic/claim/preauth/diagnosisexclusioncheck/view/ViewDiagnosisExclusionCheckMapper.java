package com.shaic.claim.preauth.diagnosisexclusioncheck.view;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.preauth.PedValidation;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class ViewDiagnosisExclusionCheckMapper {
	
	private static MapperFacade tableMapper;
	
	static ViewDiagnosisExclusionCheckMapper  myObj;


	public static void getAllMapValues()  {
 
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<PedValidation, ViewDiagnosisExclusionCheckDTO> classMap = mapperFactory
				.classMap(PedValidation.class,
						ViewDiagnosisExclusionCheckDTO.class);
		classMap.field("key", "key");
		classMap.field("diagnosisId", "diagnosis");
		classMap.field("sublimitId", "subLimit");
		classMap.field("policyAging", "policyAgeing");
		classMap.field("approveAmount", "approvedAmt");
		classMap.register();
		tableMapper = mapperFactory.getMapperFacade();
	}

	public static List<ViewDiagnosisExclusionCheckDTO> getViewDiagnosisExclusionCheckDTO(
			List<PedValidation> viewDiagnosisExclusinCheckList) {
		List<ViewDiagnosisExclusionCheckDTO> mapAsList = tableMapper.mapAsList(
				viewDiagnosisExclusinCheckList, ViewDiagnosisExclusionCheckDTO.class);
		return mapAsList;
	}
	
	public static ViewDiagnosisExclusionCheckMapper getInstance(){
        if(myObj == null){
            myObj = new ViewDiagnosisExclusionCheckMapper();
            getAllMapValues();
        }
        return myObj;
	 }


}

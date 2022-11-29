package com.shaic.claim.preauth.view;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.preauth.Preauth;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class PreAuthMapper {

	private static MapperFacade tableMapper;
	
	static PreAuthMapper myObj;

	 public static void getAllMapValues()  { 
		 
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Preauth, ViewPreAuthDetailsDTO> classMap = mapperFactory
				.classMap(Preauth.class, ViewPreAuthDetailsDTO.class);
		classMap.field("dataOfAdmission", "dateOfAdmission");
		classMap.field("intimation.admissionReason", "reasonForAdmission");
		classMap.field("totalApprovalAmount", "preAuthRequestedAmt");
		classMap.field("roomCategory.value", "roomCategory");
		classMap.field("treatmentType.value", "treatmentType");
		classMap.field("numberOfDays", "noOfDays");
		classMap.field("natureOfTreatment.value", "natureOfTreatement");
		classMap.field("consultationDate", "firstConsultationDate");
		classMap.field("relapseFlag", "replaceOfIlleness");
		classMap.field("relapseRemarks", "remarks");
		classMap.register();
		tableMapper = mapperFactory.getMapperFacade();
	}

	public static List<ViewPreAuthDetailsDTO> getViewPreAuthDetailsDTODTO(
			List<Preauth> preAuthList) {
		List<ViewPreAuthDetailsDTO> mapAsList = tableMapper.mapAsList(
				preAuthList, ViewPreAuthDetailsDTO.class);
		return mapAsList;
	}
	
	public static PreAuthMapper getInstance(){
        if(myObj == null){
            myObj = new PreAuthMapper();
            getAllMapValues();
        }
        return myObj;
	 }

}

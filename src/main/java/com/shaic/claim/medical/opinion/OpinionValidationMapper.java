package com.shaic.claim.medical.opinion;

import java.util.List;

import com.shaic.domain.OpinionValidation;
import com.shaic.newcode.wizard.domain.MappingUtil;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

public class OpinionValidationMapper {
	
	private static MapperFacade tableMapper;
	static OpinionValidationMapper myObj;
	private static MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
	private static ClassMapBuilder<OpinionValidation, OpinionValidationTableDTO> classMapForOpinionValidation = null;
	
	public static void getAllMapValues()  {
		classMapForOpinionValidation = mapperFactory.classMap(OpinionValidation.class, OpinionValidationTableDTO.class);
		classMapForOpinionValidation.field("key","validationkey");
		classMapForOpinionValidation.field("intimationNumber","intimationNumber");
		classMapForOpinionValidation.field("updatedBy","updatedBy");
		classMapForOpinionValidation.field("createdBy","createdBy");
		classMapForOpinionValidation.field("createdDate","createdDate");
		classMapForOpinionValidation.field("updatedDateTime","updatedDateTime");
		classMapForOpinionValidation.field("updatedRemarks","updatedRemarks");
		classMapForOpinionValidation.field("opinionStatus","opinionStatus");
		classMapForOpinionValidation.field("modifiedDateTime","modifiedDateTime");
		classMapForOpinionValidation.field("approveRejectRemarks","approveRejectRemarks");
		classMapForOpinionValidation.field("assignedRoleBy","assignedRoleBy");
		classMapForOpinionValidation.field("assignedDocName","assignedDocName");
		
		classMapForOpinionValidation.register();
		tableMapper = mapperFactory.getMapperFacade();
	}
	
	public static List<OpinionValidationTableDTO> getOpinionValidationTableObjects(List<OpinionValidation> opinionValidationList) {
		List<OpinionValidationTableDTO> opinionValidationTableObjectList = tableMapper.mapAsList(opinionValidationList, OpinionValidationTableDTO.class);
		return opinionValidationTableObjectList;
	} 
	
	public static OpinionValidationMapper getInstance() {
		if (myObj == null) {
			myObj = new OpinionValidationMapper();
			getAllMapValues();
		} 
		return myObj;
	}

}

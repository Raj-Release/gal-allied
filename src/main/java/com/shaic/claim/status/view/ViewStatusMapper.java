package com.shaic.claim.status.view;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.Procedure;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class ViewStatusMapper {

	private static MapperFacade tableMapper;

	static ViewStatusMapper myObj;

	public static void getAllMapValues() {

		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<PedValidation, ViewStatusDTO> classMapDiagnosis = mapperFactory
				.classMap(PedValidation.class, ViewStatusDTO.class);
		ClassMapBuilder<Preauth, ViewStatusDTO> classMapPreAuth = mapperFactory
				.classMap(Preauth.class, ViewStatusDTO.class);
		ClassMapBuilder<Procedure, ViewStatusDTO> classMapProcedure = mapperFactory
				.classMap(Procedure.class, ViewStatusDTO.class);
		classMapPreAuth.field("preauthId", "referenceNo");
		classMapDiagnosis.field("diagnosisId", "diagnosis");
		classMapProcedure.field("procedureName", "procedure");
		classMapPreAuth.field("claim.claimedAmount", "reqAmt");
		classMapPreAuth.field("status.processValue", "status");
		classMapPreAuth.field("totalApprovalAmount", "approvedAmt");
		classMapPreAuth.field("remarks", "approvalRemarks");
		classMapPreAuth.register();
		classMapDiagnosis.register();
		classMapProcedure.register();
		tableMapper = mapperFactory.getMapperFacade();
	}

	public static List<ViewStatusDTO> getViewStatusOfPedValidationDTO(
			List<PedValidation> procudreExclusionList) {
		List<ViewStatusDTO> mapAsList = tableMapper.mapAsList(
				procudreExclusionList, ViewStatusDTO.class);
		return mapAsList;
	}

	public static List<ViewStatusDTO> getViewStatuOfPreAuthsDTO(
			List<Preauth> procudreExclusionList) {
		List<ViewStatusDTO> mapAsList = tableMapper.mapAsList(
				procudreExclusionList, ViewStatusDTO.class);
		return mapAsList;
	}

	public static List<ViewStatusDTO> getViewStatuOfProcedureDTO(
			List<Procedure> procudreExclusionList) {
		List<ViewStatusDTO> mapAsList = tableMapper.mapAsList(
				procudreExclusionList, ViewStatusDTO.class);
		return mapAsList;
	}
	
	public static ViewStatusMapper getInstance(){
        if(myObj == null){
            myObj = new ViewStatusMapper();
            getAllMapValues();
        }
        return myObj;
	 }

}

package com.shaic.claim.preauth.view;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.preauth.PedValidation;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class DiagnosisMapper {

	private static MapperFacade tableMapper;
	
	static DiagnosisMapper myObj;

	 public static void getAllMapValues() {
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<PedValidation, DiagnosisDTO> classMap = mapperFactory
				.classMap(PedValidation.class, DiagnosisDTO.class);
		classMap.field("diagnosisId", "diagnosisId");		classMap.register();
		tableMapper = mapperFactory.getMapperFacade();
	}

	public static List<DiagnosisDTO> getDiagnosisDTO(
			List<PedValidation> diagnosisList) {
		List<DiagnosisDTO> mapAsList = tableMapper.mapAsList(diagnosisList,
				DiagnosisDTO.class);
		return mapAsList;
	}

	
	public static DiagnosisMapper getInstance(){
        if(myObj == null){
            myObj = new DiagnosisMapper();
            getAllMapValues();
        }
        return myObj;
	 }
}

package com.shaic.claim.medical.opinion;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Intimation;
import com.shaic.domain.OPIntimation;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class SearchRecordMarkEscMapperOP {
	private static MapperFacade tableMapper;
	
	static SearchRecordMarkEscMapperOP myObj;


	public static void getAllMapValues() {
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<OPIntimation, SearchRecordMarkEscTableDTO> classMap = mapperFactory.classMap(OPIntimation.class,SearchRecordMarkEscTableDTO.class);
		classMap.field("key", "key");
		//classMap.field("crcFlag", "crmFlagged");
		classMap.field("intimationId", "intimationNo");
		classMap.field("policy.policyNumber", "policyNo");
		classMap.field("insuredPatientName", "insuredPatientName");
		classMap.field("cpuCode.cpuCode", "cpuCode");
		classMap.field("lobId.key", "lobId");
		classMap.field("policy.productName", "productName");
		classMap.field("admissionReason", "reasonForAdmission");
		classMap.field("hospital", "hospitalKey");
		
		classMap.register();
		tableMapper = mapperFactory.getMapperFacade();
	}

	public static List<SearchRecordMarkEscTableDTO> getSearchRecordMarkEscTableDTO(List<OPIntimation> listIntimation) {
		List<SearchRecordMarkEscTableDTO> mapAsList = tableMapper.mapAsList(listIntimation,SearchRecordMarkEscTableDTO.class);
		return mapAsList;
	}
	
	public static SearchRecordMarkEscMapperOP getInstance(){
        if(myObj == null){
            myObj = new SearchRecordMarkEscMapperOP();
            getAllMapValues();
        }
        return myObj;
	 }

	
}

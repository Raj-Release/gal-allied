package com.shaic.claim.pcc.hrmp;

import java.util.List;



import com.shaic.claim.pcc.hrmprocessing.HRMProcessing;
import com.shaic.newcode.wizard.domain.MappingUtil;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;



public class SearchHRMPMapper {
	
private static MapperFacade tableMapper;
	
	static SearchHRMPMapper myObj;
	
public static void getAllMapValues(){

		
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<HRMProcessing, SearchHRMPTableDTO> claimClassMap = mapperFactor.classMap(HRMProcessing.class, SearchHRMPTableDTO.class);
		
		claimClassMap.field("key", "key");
		
		claimClassMap.field("claim.intimation.intimationId", "intimationNo");
		claimClassMap.field("claim.intimation.admissionDate", "dateOfAdmission");
		claimClassMap.field("claim.intimation.insured.insuredName", "insuredPatientName");
		claimClassMap.field("claim.intimation.policy.productName", "productName");
		claimClassMap.field("claim.intimation.policy.lobId", "lobId");
		claimClassMap.field("claim.intimation.hospital", "hospitalNameId");
		
		claimClassMap.register();
		
		tableMapper = mapperFactor.getMapperFacade();
	
	}
	
	public static List<SearchHRMPTableDTO> getIntimationDTO(List<HRMProcessing> intimationData){
		List<SearchHRMPTableDTO> mapAsList = 
										tableMapper.mapAsList(intimationData, SearchHRMPTableDTO.class);
		return mapAsList;
		
	}
	
	public static SearchHRMPMapper getInstance(){
        if(myObj == null){
            myObj = new SearchHRMPMapper();
            getAllMapValues();
        }
        return myObj;
	 }

}

package com.shaic.claim.bedphoto;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Claim;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class SearchUploadBedPhotoMapper {
	
	private static MapperFacade tableMapper;
	
	static SearchUploadBedPhotoMapper myObj;
	
	public static void getAllMapValues()  {
		 
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Claim, SearchBedPhotoTableDTO> intimationClassMap = mapperFactor.classMap(Claim.class, SearchBedPhotoTableDTO.class);
		
		intimationClassMap.field("intimation.key", "key");
		intimationClassMap.field("intimation.intimationId", "intimationNo");
		intimationClassMap.field("intimation.policy.policyNumber", "policyNo");
		intimationClassMap.field("intimation.insured.key", "insuredKey");
		intimationClassMap.field("intimation.insured.insuredName", "insuredPatientName");
		intimationClassMap.field("dataOfAdmission", "dateOfAdmission");
		intimationClassMap.field("dataOfDischarge", "dateOfDischarge");
		intimationClassMap.field("intimation.hospital", "hospitalNameKey");
		intimationClassMap.register();
		tableMapper = mapperFactor.getMapperFacade();
	}
	
	public static List<SearchBedPhotoTableDTO> getIntimationDTO(List<Claim> claimData){
		List<SearchBedPhotoTableDTO> mapAsList = 
										tableMapper.mapAsList(claimData, SearchBedPhotoTableDTO.class);
		return mapAsList;
		
	}
	
	public static SearchUploadBedPhotoMapper getInstance(){
        if(myObj == null){
            myObj = new SearchUploadBedPhotoMapper();
            getAllMapValues();
        }
        return myObj;
	 }

}

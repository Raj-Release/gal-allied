package com.shaic.claim.search.specialist.search;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.submitSpecialist.SubmitSpecialistDTO;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.PreauthEscalate;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class SubmitSpecialistMapper {
	private static MapperFacade tableMapper;
	
	private static MapperFacade specialistMapper;
	
	static SubmitSpecialistMapper  myObj;


	public static void getAllMapValues()  {
 
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Preauth, SubmitSpecialistTableDTO> classMap = mapperFactory
				.classMap(Preauth.class,
						SubmitSpecialistTableDTO.class);
		
		ClassMapBuilder<PreauthEscalate,SubmitSpecialistDTO> specialistMap=mapperFactory.classMap(PreauthEscalate.class,SubmitSpecialistDTO.class);
		
		classMap.field("key", "key");
		classMap.field("intimation.intimationId", "intimationNo");
		classMap.field("claim.claimId", "claimNo");
		classMap.field("claim.key", "claimKey");
		classMap.field("status.processValue", "claimStatus");
		classMap.field("intimation.insuredPatientName", "insuredPatientName");
		classMap.field("intimation.cpuCode.cpuCode", "cpuId");
		classMap.field("policy.lobId", "lob");
		classMap.field("policy.policyNumber", "policyNo");
		classMap.field("intimation.hospital","hospitalkey");
		classMap.field("intimation.admissionReason", "reasonForAdmission");
		classMap.field("policy.product.value","productName");
		classMap.field("createdBy","referredBy");
		classMap.field("intimation.key", "intimationkey");
		//Referred by mapping with sathish sir.
		// classMap.field("","hospitalName");
		
		specialistMap.field("key", "key");
		specialistMap.field("createdDate", "requestedDate");
		//specialistMap.field("", "viewFile");                                 //need to implement file view
		specialistMap.field("escalateRemarks", "requestorRemarks");
		specialistMap.field("specialistRemarks", "specialistRemarks");
		
		//specialistMap.field("", "fileUpload");	                           //need to implement file upload
		
        specialistMap.register();
		classMap.register();
		tableMapper = mapperFactory.getMapperFacade();
		specialistMapper=mapperFactory.getMapperFacade();
	}

	public static List<SubmitSpecialistTableDTO> getSearchAdviseOnPEDTableDTO(
			List<Preauth> submitSpecialistAdvise) {
		List<SubmitSpecialistTableDTO> mapAsList = specialistMapper.mapAsList( submitSpecialistAdvise, SubmitSpecialistTableDTO.class);
		return mapAsList;
	}
	
	public static List<SubmitSpecialistDTO> getSubmitSpecialistDTO(
			List<PreauthEscalate> submitSpecialistAdvise) {
		List<SubmitSpecialistDTO> mapAsList = specialistMapper.mapAsList( submitSpecialistAdvise, SubmitSpecialistDTO.class);
		return mapAsList;
	}
	
	public static SubmitSpecialistMapper getInstance(){
        if(myObj == null){
            myObj = new SubmitSpecialistMapper();
            getAllMapValues();
        }
        return myObj;
	 }

	
	
	
}

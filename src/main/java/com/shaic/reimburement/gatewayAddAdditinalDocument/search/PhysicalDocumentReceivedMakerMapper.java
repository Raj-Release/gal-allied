package com.shaic.reimburement.gatewayAddAdditinalDocument.search;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.PhysicalDocumentVerification;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class PhysicalDocumentReceivedMakerMapper {

	private static MapperFacade tableMapper;
	
	static PhysicalDocumentReceivedMakerMapper myObj;
	
	public static void getAllMapValues() {
		
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<PhysicalDocumentVerification, PhysicalDocumentReceivedMakerTableDTO> claimClassMap = mapperFactor.classMap(PhysicalDocumentVerification.class, PhysicalDocumentReceivedMakerTableDTO.class);
		
		claimClassMap.field("reimbursement.key", "key");
		claimClassMap.field("claim.key", "claimKey");
		claimClassMap.field("claim.claimId", "claimNo");
		claimClassMap.field("claim.claimType.value", "claimType");
		
		claimClassMap.field("claim.intimation.intimationId", "intimationNo");
		claimClassMap.field("claim.intimation.policy.policyNumber", "policyNo");
		claimClassMap.field("claim.intimation.admissionDate", "dateOfAdmission");
		claimClassMap.field("claim.intimation.admissionReason", "reasonForAdmission");
		claimClassMap.field("claim.intimation.insured.insuredName", "insuredPatientName");
		claimClassMap.field("claim.intimation.hospital", "hospitalNameID");
		
		claimClassMap.register();
		
		tableMapper = mapperFactor.getMapperFacade();
	}
	
	public static List<PhysicalDocumentReceivedMakerTableDTO> getIntimationDTO(List<PhysicalDocumentVerification> intimationData){
		List<PhysicalDocumentReceivedMakerTableDTO> mapAsList = 
										tableMapper.mapAsList(intimationData, PhysicalDocumentReceivedMakerTableDTO.class);
		return mapAsList;
		
	}
	
	public static PhysicalDocumentReceivedMakerMapper getInstance(){
        if(myObj == null){
            myObj = new PhysicalDocumentReceivedMakerMapper();
            getAllMapValues();
        }
        return myObj;
	 }
	
	


}

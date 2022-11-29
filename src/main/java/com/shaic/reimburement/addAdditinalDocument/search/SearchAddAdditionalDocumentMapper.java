package com.shaic.reimburement.addAdditinalDocument.search;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Reimbursement;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class SearchAddAdditionalDocumentMapper {
	private static MapperFacade tableMapper;
	
	static SearchAddAdditionalDocumentMapper myObj;
	
	public static void getAllMapValues() {
		
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Reimbursement, SearchAddAdditionalDocumentTableDTO> claimClassMap = mapperFactor.classMap(Reimbursement.class, SearchAddAdditionalDocumentTableDTO.class);
		
		claimClassMap.field("key", "key");
		claimClassMap.field("claim.key", "claimKey");
		claimClassMap.field("claim.claimId", "claimNo");
		claimClassMap.field("claim.claimType.value", "claimType");
		
		claimClassMap.field("claim.intimation.intimationId", "intimationNo");
		claimClassMap.field("claim.intimation.policy.policyNumber", "policyNo");
		claimClassMap.field("claim.intimation.admissionDate", "dateOfAdmission");
		claimClassMap.field("claim.intimation.admissionReason", "reasonForAdmission");
		claimClassMap.field("claim.intimation.insured.insuredName", "insuredPatientName");
		claimClassMap.field("claim.intimation.paPatientName", "paPatientName");
		claimClassMap.field("claim.intimation.policy.product.key", "productKey");

		claimClassMap.field("claim.intimation.hospital", "hospitalNameID");
		
		claimClassMap.register();
		
		tableMapper = mapperFactor.getMapperFacade();
	}
	
	public static List<SearchAddAdditionalDocumentTableDTO> getIntimationDTO(List<Reimbursement> intimationData){
		List<SearchAddAdditionalDocumentTableDTO> mapAsList = 
										tableMapper.mapAsList(intimationData, SearchAddAdditionalDocumentTableDTO.class);
		return mapAsList;
		
	}
	
	public static SearchAddAdditionalDocumentMapper getInstance(){
        if(myObj == null){
            myObj = new SearchAddAdditionalDocumentMapper();
            getAllMapValues();
        }
        return myObj;
	 }
	
	
}

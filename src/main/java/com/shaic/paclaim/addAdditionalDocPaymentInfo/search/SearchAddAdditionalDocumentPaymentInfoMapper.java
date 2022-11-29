package com.shaic.paclaim.addAdditionalDocPaymentInfo.search;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Reimbursement;
import com.shaic.newcode.wizard.domain.MappingUtil;


public class SearchAddAdditionalDocumentPaymentInfoMapper {

	private static MapperFacade tableMapper;
	
	static SearchAddAdditionalDocumentPaymentInfoMapper myObj;
	
	public static void getAllMapValues() {
		
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Reimbursement, SearchAddAdditionalDocumentPaymentInfoTableDTO> claimClassMap = mapperFactor.classMap(Reimbursement.class, SearchAddAdditionalDocumentPaymentInfoTableDTO.class);
		
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
	
	public static List<SearchAddAdditionalDocumentPaymentInfoTableDTO> getIntimationDTO(List<Reimbursement> intimationData){
		List<SearchAddAdditionalDocumentPaymentInfoTableDTO> mapAsList = 
										tableMapper.mapAsList(intimationData, SearchAddAdditionalDocumentPaymentInfoTableDTO.class);
		return mapAsList;
		
	}
	
	public static SearchAddAdditionalDocumentPaymentInfoMapper getInstance(){
        if(myObj == null){
            myObj = new SearchAddAdditionalDocumentPaymentInfoMapper();
            getAllMapValues();
        }
        return myObj;
	 }
	
	


}

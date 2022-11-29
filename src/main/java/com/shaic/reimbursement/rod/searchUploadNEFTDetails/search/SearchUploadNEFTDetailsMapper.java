package com.shaic.reimbursement.rod.searchUploadNEFTDetails.search;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.NEFTQueryDetails;
import com.shaic.domain.Reimbursement;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class SearchUploadNEFTDetailsMapper {

	private static MapperFacade tableMapper;
	
	static SearchUploadNEFTDetailsMapper myObj;
	
	public static void getAllMapValues() {
		
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<NEFTQueryDetails, SearchUploadNEFTDetailsTableDTO> claimClassMap = mapperFactor.classMap(NEFTQueryDetails.class, SearchUploadNEFTDetailsTableDTO.class);
		
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
		claimClassMap.field("reimbursement.key", "rodKey");
		
		claimClassMap.register();
		
		tableMapper = mapperFactor.getMapperFacade();
	}
	
	public static List<SearchUploadNEFTDetailsTableDTO> getIntimationDTO(List<NEFTQueryDetails> intimationData){
		List<SearchUploadNEFTDetailsTableDTO> mapAsList = 
										tableMapper.mapAsList(intimationData, SearchUploadNEFTDetailsTableDTO.class);
		return mapAsList;
		
	}
	
	public static SearchUploadNEFTDetailsMapper getInstance(){
        if(myObj == null){
            myObj = new SearchUploadNEFTDetailsMapper();
            getAllMapValues();
        }
        return myObj;
	 }
	
	



}

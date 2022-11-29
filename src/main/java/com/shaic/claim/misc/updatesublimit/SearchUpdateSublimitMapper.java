package com.shaic.claim.misc.updatesublimit;

import java.util.List;

import com.shaic.domain.Reimbursement;
import com.shaic.newcode.wizard.domain.MappingUtil;



import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

public class SearchUpdateSublimitMapper {
	
	private static MapperFacade tableMapper;
	
	static SearchUpdateSublimitMapper myObj;
	
	public static void getAllMapValues(){

		
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Reimbursement, SearchUpdateSublimitTableDTO> claimClassMap = mapperFactor.classMap(Reimbursement.class, SearchUpdateSublimitTableDTO.class);
		
		claimClassMap.field("key", "reimbursementKey");
		claimClassMap.field("billEntryAmt", "claimedAmtAsPerBill");
		claimClassMap.field("status.processValue", "type");
		claimClassMap.field("docAcknowLedgement.hospitalisationFlag", "hospitalizationFlag");
		claimClassMap.field("docAcknowLedgement.documentReceivedFromId.value", "documentRecvdFrm");
		claimClassMap.field("claim.key", "claimKey");
		claimClassMap.field("claim.claimType.value", "claimType");
		
		claimClassMap.field("claim.intimation.intimationId", "intimationNo");
		claimClassMap.field("claim.intimation.policy.policyNumber", "policyNo");
		claimClassMap.field("claim.intimation.admissionReason", "reasonForAdmission");
		claimClassMap.field("claim.intimation.insured.insuredName", "insuredPatientName");
		claimClassMap.field("claim.intimation.policy.productName", "productName");
		claimClassMap.field("claim.intimation.policy.lobId", "lobId");
		claimClassMap.field("claim.intimation.hospital", "hospitalNameId");
		
		claimClassMap.register();
		
		tableMapper = mapperFactor.getMapperFacade();
	
	}
	
	public static List<SearchUpdateSublimitTableDTO> getIntimationDTO(List<Reimbursement> intimationData){
		List<SearchUpdateSublimitTableDTO> mapAsList = 
										tableMapper.mapAsList(intimationData, SearchUpdateSublimitTableDTO.class);
		return mapAsList;
		
	}
	
	public static SearchUpdateSublimitMapper getInstance(){
        if(myObj == null){
            myObj = new SearchUpdateSublimitMapper();
            getAllMapValues();
        }
        return myObj;
	 }

}

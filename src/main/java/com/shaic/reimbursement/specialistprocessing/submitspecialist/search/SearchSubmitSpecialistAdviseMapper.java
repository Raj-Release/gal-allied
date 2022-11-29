package com.shaic.reimbursement.specialistprocessing.submitspecialist.search;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.search.specialist.search.SubmitSpecialistTableDTO;
import com.shaic.domain.Claim;
import com.shaic.newcode.wizard.domain.MappingUtil;

/**
 * @author ntv.narenj
 *
 */
public class SearchSubmitSpecialistAdviseMapper {
	private static MapperFacade tableMapper;
	
	static SearchSubmitSpecialistAdviseMapper  myObj;

	public static void getAllMapValues()  {
 
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Claim, SubmitSpecialistTableDTO> claimClassMap = mapperFactor.classMap(Claim.class, SubmitSpecialistTableDTO.class);
		claimClassMap.field("claimId", "claimNo");
		claimClassMap.field("key", "claimKey");
		claimClassMap.field("intimation.key", "intimationkey");
		claimClassMap.field("intimation.intimationId", "intimationNo");
		claimClassMap.field("intimation.policy.policyNumber", "policyNo");
		claimClassMap.field("intimation.policy.lobId", "lOBId");
		claimClassMap.field("intimation.policy.productName", "productName");
		claimClassMap.field("intimation.insured.insuredName", "insuredPatientName");
		claimClassMap.field("intimation.createdBy","referredBy");
		claimClassMap.field("stage.key", "stageId");
		claimClassMap.field("intimation.admissionReason","reasonForAdmission");
		claimClassMap.field("status.processValue", "claimStatus");
		claimClassMap.field("intimation.cpuCode.cpuCode","cpuId");
		claimClassMap.field("intimation.hospital","hospitalkey");
		claimClassMap.register();
		
		tableMapper = mapperFactor.getMapperFacade();
	}
	
	public static List<SubmitSpecialistTableDTO> getClaimDTO(List<Claim> claimData){
		List<SubmitSpecialistTableDTO> mapAsList = 
										tableMapper.mapAsList(claimData, SubmitSpecialistTableDTO.class);
		return mapAsList;
		
	}
	
	public static SearchSubmitSpecialistAdviseMapper getInstance(){
        if(myObj == null){
            myObj = new SearchSubmitSpecialistAdviseMapper();
            getAllMapValues();
        }
        return myObj;
	 }
}

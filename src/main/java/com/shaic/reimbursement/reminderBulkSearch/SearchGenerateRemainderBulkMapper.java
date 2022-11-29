package com.shaic.reimbursement.reminderBulkSearch;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Claim;
import com.shaic.newcode.wizard.domain.MappingUtil;

/**
 * 
 *
 */
public class SearchGenerateRemainderBulkMapper {
	private static MapperFacade tableMapper;
	static {
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Claim, SearchGenerateReminderBulkTableDTO> claimClassMap = mapperFactor.classMap(Claim.class, SearchGenerateReminderBulkTableDTO.class);
		claimClassMap.field("claimId", "claimNo");
		claimClassMap.field("key", "claimKey");
		claimClassMap.field("intimation.key", "intimationkey");
		claimClassMap.field("intimation.intimationId", "intimationNo");
		claimClassMap.field("intimation.policy.policyNumber", "policyNo");
		claimClassMap.field("intimation.admissionReason", "reasonForAdmission");
		claimClassMap.field("intimation.insured.insuredName", "insuredPatientName");
		claimClassMap.field("intimation.hospital", "hospitalNameID");
		claimClassMap.field("status.processValue", "claimStatus");
		claimClassMap.register();
		
		tableMapper = mapperFactor.getMapperFacade();
	}
	
	public static List<SearchGenerateReminderBulkTableDTO> getClaimDTO(List<Claim> claimData){
		List<SearchGenerateReminderBulkTableDTO> mapAsList = 
										tableMapper.mapAsList(claimData, SearchGenerateReminderBulkTableDTO.class);
		return mapAsList;
		
	}
}

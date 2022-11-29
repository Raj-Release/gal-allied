package com.shaic.reimbursement.queryrejection.processdraftquery.search;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Claim;
import com.shaic.newcode.wizard.domain.MappingUtil;


/**
 * @author ntv.narenj
 *
 */
public class SearchProcessDraftQueryMapper {

	private static MapperFacade tableMapper;
	
	static SearchProcessDraftQueryMapper  myObj;
	
	public static void getAllMapValues()   {
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Claim, SearchProcessDraftQueryTableDTO> claimClassMap = mapperFactor.classMap(Claim.class, SearchProcessDraftQueryTableDTO.class);
		claimClassMap.field("claimId", "claimNo");
		claimClassMap.field("key", "claimKey");
		claimClassMap.field("intimation.key", "intimationkey");
		claimClassMap.field("intimation.intimationId", "intimationNo");
		claimClassMap.field("intimation.policy.policyNumber", "policyNo");
		claimClassMap.field("intimation.admissionReason", "reasonForAdmission");
		claimClassMap.field("intimation.insured.insuredName", "insuredPatientName");
		claimClassMap.field("intimation.hospital", "hospitalNameID");
		claimClassMap.field("intimation.cpuCode.cpuCode", "cpuId");
		claimClassMap.field("status.processValue", "claimStatus");
		claimClassMap.register();
		
		tableMapper = mapperFactor.getMapperFacade();
	}
	
	public static List<SearchProcessDraftQueryTableDTO> getClaimDTO(List<Claim> claimData){
		List<SearchProcessDraftQueryTableDTO> mapAsList = 
										tableMapper.mapAsList(claimData, SearchProcessDraftQueryTableDTO.class);
		return mapAsList;
		
	}
	
	public static SearchProcessDraftQueryMapper getInstance(){
        if(myObj == null){
            myObj = new SearchProcessDraftQueryMapper();
            getAllMapValues();
        }
        return myObj;
	 }
}

package com.shaic.reimbursement.manageclaim.searchClaimwiseApproval;

import java.util.List;

import com.shaic.domain.Claim;
import com.shaic.newcode.wizard.domain.MappingUtil;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

public class SearchClaimWiseApprovalMapper {
	
private static MapperFacade tableMapper;
	
	static SearchClaimWiseApprovalMapper  myObj;
	
	private static BoundMapperFacade<Claim, SearchClaimWiseAllowApprovalDto> claimWiseApprovalMapper;
	
	public  static void getAllMapValues(){
		
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Claim, SearchClaimWiseAllowApprovalDto> claimClassMap = mapperFactor.classMap(Claim.class, SearchClaimWiseAllowApprovalDto.class);
		
		claimClassMap.field("claimId", "claimNo");
		claimClassMap.field("intimation.key", "intimationkey");
		claimClassMap.field("intimation.intimationId", "intimationNo");
		claimClassMap.field("intimation.policy.policyNumber", "policyNo");
		claimClassMap.field("intimation.insured.insuredName", "insuredPatientName");
		claimClassMap.field("intimation.hospital", "hospitalNameID");
		claimClassMap.field("intimation.admissionDate", "dateOfAdmission");
		claimClassMap.field("claimType.value", "claimType");
		claimClassMap.field("key", "claimKey");
		
		claimClassMap.register();
		
		tableMapper = mapperFactor.getMapperFacade();
		
		claimWiseApprovalMapper = mapperFactor.getMapperFacade(Claim.class, SearchClaimWiseAllowApprovalDto.class);
		
	}
	
	public static List<SearchClaimWiseAllowApprovalDto> getClaimDTO(List<Claim> claimData){
		List<SearchClaimWiseAllowApprovalDto> mapAsList = 
										tableMapper.mapAsList(claimData, SearchClaimWiseAllowApprovalDto.class);
		return mapAsList;
		
	}
	
	   public static SearchClaimWiseApprovalMapper getInstance(){
	       if(myObj == null){
	           myObj = new SearchClaimWiseApprovalMapper();
	           getAllMapValues();
	       }
	       return myObj;
		 }

}

package com.shaic.claim.claimhistory.view.ompView;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.omp.registration.OMPPreviousClaimTableDTO;
import com.shaic.domain.OMPClaim;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class OMPPreviousClaimMapper {

	private static MapperFacade mapper;
	static MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
	private static MapperFacade claimsMapper;
	private static ClassMapBuilder<OMPClaim, OMPPreviousClaimTableDTO> claimMap = mapperFactory.classMap(OMPClaim.class, OMPPreviousClaimTableDTO.class);
	static OMPPreviousClaimMapper myObj;
	public  static void getAllMapValues()  {
		
		claimMap.field("key","key"); 
		claimMap.field("claimId","claimNumber"); 
		claimMap.field("intimation.insured.insuredId","customerID");
		claimMap.field("intimation.insured.insuredName","insuredPatientName");
		claimMap.field("intimation.insured.insuredName", "insuredname");
		claimMap.field("intimation.insured.insuredName", "patientName");
		claimMap.field("intimation.admissionDate","admissiondate");
		claimMap.field("intimation.policy.policyYear", "policyYear");
		claimMap.field("status.processValue","claimStatus");
		claimMap.field("claimedAmount","claimAmount");	
		claimMap.field("intimation.policy.policyNumber","policyno");
		claimMap.field("intimation.intimationId","intimationno");
		claimMap.field("provisionAmount","provAmt");
		claimMap.field("claimType.value","claimtype");	
		claimMap.field("claimType.key", "claimTypeKey");
		claimMap.field("intimation.hospitalName", "hospitalname");
		claimMap.field("event.eventDescription", "eventcode");
		
		claimMap.register();
		mapper = mapperFactory.getMapperFacade();
		claimsMapper = mapperFactory.getMapperFacade();;
	
	}
	
	public List<OMPPreviousClaimTableDTO> getPreviousClaimDTOList(List<OMPClaim> claimList) {
		List<OMPPreviousClaimTableDTO> mapAsList = claimsMapper.mapAsList(claimList, OMPPreviousClaimTableDTO.class);
		return mapAsList;
	}
	
	public static OMPPreviousClaimMapper getInstance(){
        if(myObj == null){
            myObj = new OMPPreviousClaimMapper();
            getAllMapValues();
        }
        return myObj;
	 }
	
}

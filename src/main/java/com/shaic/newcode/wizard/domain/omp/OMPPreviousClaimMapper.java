package com.shaic.newcode.wizard.domain.omp;

import java.text.SimpleDateFormat;
import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.impl.generator.EclipseJdtCompilerStrategy;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.OMPClaim;

public class OMPPreviousClaimMapper {

	private static MapperFacade mapper;
	static MapperFactory mapperFactory = new DefaultMapperFactory.Builder().compilerStrategy(new EclipseJdtCompilerStrategy()).build();
	private static MapperFacade claimsMapper;
	private static ClassMapBuilder<OMPClaim, PreviousClaimsTableDTO> claimMap = mapperFactory.classMap(OMPClaim.class, PreviousClaimsTableDTO.class);
	
	static OMPPreviousClaimMapper myObj;
	
	public  static void getAllMapValues()  {
		
		claimMap.field("key","key"); 
		claimMap.field("intimation.key","intimationKey"); 
		claimMap.field("claimId","claimNumber"); 
		claimMap.field("intimation.insured.insuredId","customerID");
		claimMap.field("intimation.insured.insuredName","insuredPatientName");
		claimMap.field("intimation.insured.insuredName", "insuredName");
		claimMap.field("intimation.insured.insuredName", "patientName");
		claimMap.field("intimation.admissionDate","admissionDate");
		claimMap.field("status.processValue","claimStatus");
		claimMap.field("claimedAmount","claimAmount");	
		claimMap.field("intimation.policy.policyYear", "policyYear");
//		claimMap.field("provisionAmount", "claimAmount");
		claimMap.field("intimation.policy.policyNumber","policyNumber");
		claimMap.field("intimation.intimationId","intimationNumber");
		claimMap.field("provisionAmount","provisionAmount");
		claimMap.field("claimType.value","claimType");
		claimMap.field("claimType.key", "claimTypeKey");
//		claimMap.field("recordFlag", "recordFlag");
//		claimMap.field("settledAmount", "settledAmountForPreviousClaim");
		claimMap.field("hospitalName", "hospitalName");
		claimMap.field("claimType.key", "claimTypeKey");
		claimMap.field("ailmentLoss", "ailmentLoss");
		claimMap.field("lossDetails", "lossDetails");
		
		ClassMapBuilder<OMPClaim, PreviousClaimsTableDTO> classMap = mapperFactory.classMap(OMPClaim.class,PreviousClaimsTableDTO.class);	
		classMap.field("key","key"); 
		classMap.field("claimId","claimNumber"); 
		classMap.field("intimation.insured.insuredId","customerID");
		classMap.field("intimation.insured.insuredName","insuredPatientName");
		classMap.field("intimation.insured.insuredName", "insuredName");
		classMap.field("intimation.insured.insuredName", "patientName");
		classMap.field("intimation.admissionDate","admissionDate");
		classMap.field("intimation.policy.policyYear", "policyYear");
		classMap.field("status.processValue","claimStatus");
		classMap.field("claimedAmount","claimAmount");	
		classMap.field("intimation.policy.policyNumber","policyNumber");
		classMap.field("intimation.intimationId","intimationNumber");
		classMap.field("provisionAmount","provisionAmount");
		classMap.field("claimType.value","claimType");	
		classMap.field("claimType.key", "claimTypeKey");
		classMap.field("hospitalName", "hospitalName");
										
		claimMap.register();
		mapper = mapperFactory.getMapperFacade();
		claimsMapper = mapperFactory.getMapperFacade();
		
	}
	
	public Claim getClaim(PreviousClaimsTableDTO previousClaimsTableDTO) {
		Claim dest = mapper.map(previousClaimsTableDTO, Claim.class);
		return dest;
	}
	
	public PreviousClaimsTableDTO getPreviousClaimsTableDTO(OMPClaim claim) {
		PreviousClaimsTableDTO dest = mapper.map(claim, PreviousClaimsTableDTO.class);
		if(claim.getIntimation().getAdmissionDate() != null) {
			dest.setAdmissionDate(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a").format(claim.getIntimation().getAdmissionDate()));
		}
		
		return dest;
	}	
	
//	public List<OMPClaim> getClaimList(List<PreviousClaimsTableDTO> previousClaimsDTOList) {
//		List<OMPClaim> mapAsList = claimsMapper.mapAsList(previousClaimsDTOList, OMPClaim.class);
//		return mapAsList;
//	}
	
	public List<PreviousClaimsTableDTO> getPreviousClaimDTOList(List<OMPClaim> claimList) {
		List<PreviousClaimsTableDTO> mapAsList = claimsMapper.mapAsList(claimList, PreviousClaimsTableDTO.class);
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

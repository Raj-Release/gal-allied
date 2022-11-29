package com.shaic.newcode.wizard.domain;

import java.text.SimpleDateFormat;
import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.ViewTmpClaim;

public class PreviousClaimMapper {

	private static MapperFacade mapper;
	static MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
	private static MapperFacade claimsMapper;
	private static ClassMapBuilder<ViewTmpClaim, PreviousClaimsTableDTO> claimMap = mapperFactory.classMap(ViewTmpClaim.class, PreviousClaimsTableDTO.class);
	
	static PreviousClaimMapper myObj;
	
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
		claimMap.field("intimation.policyYear", "policyYear");
		claimMap.field("intimation.policyNumber","policyNumber");
		claimMap.field("intimation.intimationId","intimationNumber");
		claimMap.field("currentProvisionAmount","provisionAmount");
		claimMap.field("claimType.value","claimType");
		claimMap.field("claimType.key", "claimTypeKey");
		claimMap.field("recordFlag", "recordFlag");
		claimMap.field("settledAmount", "settledAmountForPreviousClaim");
		claimMap.field("settledAmount", "settledAmount");
		claimMap.field("diagnosis", "diagnosisForPreviousClaim");
		claimMap.field("claimType.key", "claimTypeKey");
		
		ClassMapBuilder<Claim, PreviousClaimsTableDTO> classMap = mapperFactory.classMap(Claim.class,PreviousClaimsTableDTO.class);	
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
		classMap.field("currentProvisionAmount","provisionAmount");
		classMap.field("claimType.value","claimType");	
		

										
		claimMap.register();
		mapper = mapperFactory.getMapperFacade();
		claimsMapper = mapperFactory.getMapperFacade();
		
	}
	
	public Claim getClaim(PreviousClaimsTableDTO previousClaimsTableDTO) {
		Claim dest = mapper.map(previousClaimsTableDTO, Claim.class);
		return dest;
	}
	
	public PreviousClaimsTableDTO getPreviousClaimsTableDTO(Claim claim) {
		PreviousClaimsTableDTO dest = mapper.map(claim, PreviousClaimsTableDTO.class);
		if(claim.getIntimation().getAdmissionDate() != null) {
			dest.setAdmissionDate(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a").format(claim.getIntimation().getAdmissionDate()));
		}
		
		return dest;
	}	
	
	public PreviousClaimsTableDTO getPreviousClaimsTableDTO(ViewTmpClaim claim) {
		PreviousClaimsTableDTO dest = mapper.map(claim, PreviousClaimsTableDTO.class);
		if(claim.getIntimation().getAdmissionDate() != null) {
			dest.setAdmissionDate(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a").format(claim.getIntimation().getAdmissionDate()));
		}
		
		return dest;
	}	
	
	public List<Claim> getClaimList(List<PreviousClaimsTableDTO> previousClaimsDTOList) {
		List<Claim> mapAsList = claimsMapper.mapAsList(previousClaimsDTOList, Claim.class);
		return mapAsList;
	}
	
	public List<PreviousClaimsTableDTO> getPreviousClaimDTOList(List<ViewTmpClaim> claimList) {
		List<PreviousClaimsTableDTO> mapAsList = claimsMapper.mapAsList(claimList, PreviousClaimsTableDTO.class);
		
		for(ViewTmpClaim tempClm : claimList){
		
		for (PreviousClaimsTableDTO previousClaimsTableDTO : mapAsList) {
			if(tempClm.getKey().equals(previousClaimsTableDTO.getKey())){
				if(tempClm.getIncidenceFlag() != null){
					previousClaimsTableDTO.setProvisionAmount(tempClm.getCurrentProvisionAmount());
				}
			}
			break;
		}
		}
		
		return mapAsList;
	}
	
	public List<PreviousClaimsTableDTO> getPreviousClaimDTOListOP(List<Claim> claimList) {
		List<PreviousClaimsTableDTO> mapAsList = claimsMapper.mapAsList(claimList, PreviousClaimsTableDTO.class);
		return mapAsList;
	}
	
	public static PreviousClaimMapper getInstance(){
        if(myObj == null){
            myObj = new PreviousClaimMapper();
            getAllMapValues();
        }
        return myObj;
	 }
}

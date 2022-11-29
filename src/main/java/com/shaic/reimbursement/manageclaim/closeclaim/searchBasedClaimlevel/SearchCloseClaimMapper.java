package com.shaic.reimbursement.manageclaim.closeclaim.searchBasedClaimlevel;

import java.util.List;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Claim;
import com.shaic.newcode.wizard.domain.MappingUtil;


/**
 * 
 *
 */
public class SearchCloseClaimMapper {
	
	static SearchCloseClaimMapper myObj;
	
	private static MapperFacade tableMapper;
	
	
	private static BoundMapperFacade<Claim, SearchCloseClaimTableDTO> closeClaimMapper;
	
	
	
//	static MapperFactory mapperFactory1 = new DefaultMapperFactory.Builder().compilerStrategy(new EclipseJdtCompilerStrategy()).build();
	
	public static void getAllMapValues()   {
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		
		ClassMapBuilder<Claim, SearchCloseClaimTableDTO> claimClassMap = mapperFactor.classMap(Claim.class, SearchCloseClaimTableDTO.class);
		
		claimClassMap.field("key", "claimKey");
		claimClassMap.field("intimation.key", "intimationkey");
		claimClassMap.field("intimation.intimationId", "intimationNo");
		claimClassMap.field("intimation.policy.policyNumber", "policyNo");
		claimClassMap.field("claimType.value","claimType");
		claimClassMap.field("intimation.insured.insuredName", "insuredPatientName");
		claimClassMap.field("intimation.hospital", "hospitalNameID");
		claimClassMap.field("status.processValue", "claimStatus");
		claimClassMap.field("intimation.admissionDate", "dateOfAdmission");
		
		claimClassMap.register();
		
		tableMapper = mapperFactor.getMapperFacade();
		
		closeClaimMapper = mapperFactor.getMapperFacade(Claim.class, SearchCloseClaimTableDTO.class);
		
		
	}
	
	public static List<SearchCloseClaimTableDTO> getClaimDTO(List<Claim> claimData){
		List<SearchCloseClaimTableDTO> mapAsList = 
										tableMapper.mapAsList(claimData, SearchCloseClaimTableDTO.class);
		return mapAsList;
		
	}
	
	public static SearchCloseClaimTableDTO getSearchClaimDTO(Claim claim){
		
		SearchCloseClaimTableDTO dto = closeClaimMapper.map(claim);
		
		return dto;
	}
	
	
	public static SearchCloseClaimMapper getInstance(){
        if(myObj == null){
            myObj = new SearchCloseClaimMapper();
            getAllMapValues();
        }
        return myObj;
	 }

	
	
	
}


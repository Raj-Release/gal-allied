package com.shaic.paclaim.manageclaim.closeclaim.searchBasedClaimlevel;

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
public class PASearchCloseClaimMapper {
	
	static PASearchCloseClaimMapper myObj;
	
	private static MapperFacade tableMapper;
	
	
	private static BoundMapperFacade<Claim, PASearchCloseClaimTableDTO> closeClaimMapper;
	
	
	
//	static MapperFactory mapperFactory1 = new DefaultMapperFactory.Builder().compilerStrategy(new EclipseJdtCompilerStrategy()).build();
	
	public static void getAllMapValues()   {
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		
		ClassMapBuilder<Claim, PASearchCloseClaimTableDTO> claimClassMap = mapperFactor.classMap(Claim.class, PASearchCloseClaimTableDTO.class);
		
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
		
		closeClaimMapper = mapperFactor.getMapperFacade(Claim.class, PASearchCloseClaimTableDTO.class);
		
		
	}
	
	public static List<PASearchCloseClaimTableDTO> getClaimDTO(List<Claim> claimData){
		List<PASearchCloseClaimTableDTO> mapAsList = 
										tableMapper.mapAsList(claimData, PASearchCloseClaimTableDTO.class);
		return mapAsList;
		
	}
	
	public static PASearchCloseClaimTableDTO getSearchClaimDTO(Claim claim){
		
		PASearchCloseClaimTableDTO dto = closeClaimMapper.map(claim);
		
		return dto;
	}
	
	
	public static PASearchCloseClaimMapper getInstance(){
        if(myObj == null){
            myObj = new PASearchCloseClaimMapper();
            getAllMapValues();
        }
        return myObj;
	 }

	
	
	
}


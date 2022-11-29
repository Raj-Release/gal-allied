package com.shaic.paclaim.manageclaim.closeclaim.searchRodLevel;

import java.util.List;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Claim;
import com.shaic.newcode.wizard.domain.MappingUtil;


/**
 * @author ntv.narenj
 *
 */
public class PASearchCloseClaimMapperRODLevel {
	
	private static MapperFacade tableMapper;
	
	
	static PASearchCloseClaimMapperRODLevel myObj;

	
	private static BoundMapperFacade<Claim, PASearchCloseClaimTableDTORODLevel> closeClaimMapper;
	
	
	
//	static MapperFactory mapperFactory1 = new DefaultMapperFactory.Builder().compilerStrategy(new EclipseJdtCompilerStrategy()).build();
	
	public static void getAllMapValues()  {

		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		
		ClassMapBuilder<Claim, PASearchCloseClaimTableDTORODLevel> claimClassMap = mapperFactor.classMap(Claim.class, PASearchCloseClaimTableDTORODLevel.class);
		
		claimClassMap.field("claimId", "claimNo");
		claimClassMap.field("key", "claimKey");
		claimClassMap.field("intimation.key", "intimationkey");
		claimClassMap.field("intimation.intimationId", "intimationNo");
		claimClassMap.field("intimation.policy.policyNumber", "policyNo");
		claimClassMap.field("intimation.admissionReason", "reasonForAdmission");
		claimClassMap.field("intimation.insured.insuredName", "insuredPatientName");
		claimClassMap.field("intimation.hospital", "hospitalNameID");
		claimClassMap.field("status.processValue", "claimStatus");
		claimClassMap.field("intimation.insured.healthCardNumber", "healthCardNo");
		claimClassMap.field("intimation.admissionDate", "dateOfAdmission");
		
		claimClassMap.register();
		
		tableMapper = mapperFactor.getMapperFacade();
		
		closeClaimMapper = mapperFactor.getMapperFacade(Claim.class, PASearchCloseClaimTableDTORODLevel.class);
		
		
	}
	
	public static List<PASearchCloseClaimTableDTORODLevel> getClaimDTO(List<Claim> claimData){
		List<PASearchCloseClaimTableDTORODLevel> mapAsList = 
										tableMapper.mapAsList(claimData, PASearchCloseClaimTableDTORODLevel.class);
		return mapAsList;
		
	}
	
	public static PASearchCloseClaimTableDTORODLevel getSearchClaimDTO(Claim claim){
		
		PASearchCloseClaimTableDTORODLevel dto = closeClaimMapper.map(claim);
		
		return dto;
	}
	
	public static PASearchCloseClaimMapperRODLevel getInstance(){
        if(myObj == null){
            myObj = new PASearchCloseClaimMapperRODLevel();
            getAllMapValues();
        }
        return myObj;
	 }

	
	
	
	
}


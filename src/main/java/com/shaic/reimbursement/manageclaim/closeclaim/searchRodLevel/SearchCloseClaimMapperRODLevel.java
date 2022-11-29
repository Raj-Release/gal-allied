package com.shaic.reimbursement.manageclaim.closeclaim.searchRodLevel;

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
public class SearchCloseClaimMapperRODLevel {
	
	private static MapperFacade tableMapper;
	
	
	static SearchCloseClaimMapperRODLevel myObj;

	
	private static BoundMapperFacade<Claim, SearchCloseClaimTableDTORODLevel> closeClaimMapper;
	
	
	
//	static MapperFactory mapperFactory1 = new DefaultMapperFactory.Builder().compilerStrategy(new EclipseJdtCompilerStrategy()).build();
	
	public static void getAllMapValues()  {

		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		
		ClassMapBuilder<Claim, SearchCloseClaimTableDTORODLevel> claimClassMap = mapperFactor.classMap(Claim.class, SearchCloseClaimTableDTORODLevel.class);
		
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
		
		closeClaimMapper = mapperFactor.getMapperFacade(Claim.class, SearchCloseClaimTableDTORODLevel.class);
		
		
	}
	
	public static List<SearchCloseClaimTableDTORODLevel> getClaimDTO(List<Claim> claimData){
		List<SearchCloseClaimTableDTORODLevel> mapAsList = 
										tableMapper.mapAsList(claimData, SearchCloseClaimTableDTORODLevel.class);
		return mapAsList;
		
	}
	
	public static SearchCloseClaimTableDTORODLevel getSearchClaimDTO(Claim claim){
		
		SearchCloseClaimTableDTORODLevel dto = closeClaimMapper.map(claim);
		
		return dto;
	}
	
	public static SearchCloseClaimMapperRODLevel getInstance(){
        if(myObj == null){
            myObj = new SearchCloseClaimMapperRODLevel();
            getAllMapValues();
        }
        return myObj;
	 }

	
	
	
	
}


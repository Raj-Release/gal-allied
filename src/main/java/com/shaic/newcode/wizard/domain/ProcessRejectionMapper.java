package com.shaic.newcode.wizard.domain;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Claim;
import com.shaic.newcode.wizard.dto.ProcessRejectionDTO;

public class ProcessRejectionMapper {
	
	static MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
	
	static ProcessRejectionMapper myObj;
	
	private static BoundMapperFacade<Claim,ProcessRejectionDTO> claimMapper;
	
	private static ClassMapBuilder<Claim, ProcessRejectionDTO> claimMap = mapperFactory.classMap(Claim.class,ProcessRejectionDTO.class);
	
	public static void getAllMapValues()  { 
	
		claimMap.field("key", "key");
		claimMap.field("currencyId.value", "currencyName");
		claimMap.field("provisionAmount", "provisionAmt");
		claimMap.field("claimId", "claimNumber");
		claimMap.field("registrationRemarks", "registerRemarks");
		claimMap.field("incidenceFlag","accDeathValue");
		claimMap.field("incidenceDate","accDeathDate");
		claimMap.field("injuryRemarks","injuryLossDetails");
		//claimMap.field("suggestRejection.value", "suggestion");
		claimMap.field("suggestedRejectionRemarks", "rejectionRemarks");
		claimMap.field("rejectionCategoryId.key", "rejectionCategory.id");
		claimMap.field("rejectionRemarks", "confirmRemarks");
		claimMap.field("medicalRemarks", "medicalRemarks");
		claimMap.field("doctorNote", "doctorNote");
		claimMap.field("accidentDate", "dateOfAccident");
		claimMap.field("deathDate", "dateOfDeath");
		claimMap.field("disablementDate", "dateOfDisablement");
		
		claimMap.register();
		
		claimMapper=mapperFactory.getMapperFacade(Claim.class, ProcessRejectionDTO.class);
	}
	
	public Claim getClaimForRejection(ProcessRejectionDTO processRejectionDto) {
		Claim dest = claimMapper.mapReverse(processRejectionDto);
		return dest;
	}
	
	public ProcessRejectionDTO getProcessRejectionDTO(Claim claim) {
		ProcessRejectionDTO dest = claimMapper.map(claim);
		return dest;
	}
	
	public static ProcessRejectionMapper getInstance(){
        if(myObj == null){
            myObj = new ProcessRejectionMapper();
            getAllMapValues();
        }
        return myObj;
	 }

}

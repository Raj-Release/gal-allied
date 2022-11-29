package com.shaic.newcode.wizard.domain.omp;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.OMPClaim;
import com.shaic.newcode.wizard.domain.MappingUtil;
import com.shaic.newcode.wizard.dto.ProcessRejectionDTO;

public class OMPProcessRejectionMapper {
	

	
	static MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
	
	static OMPProcessRejectionMapper myObj;
	
	private static BoundMapperFacade<OMPClaim,ProcessRejectionDTO> claimMapper;
	
	private static ClassMapBuilder<OMPClaim, ProcessRejectionDTO> claimMap = mapperFactory.classMap(OMPClaim.class,ProcessRejectionDTO.class);
	
	public static void getAllMapValues()  { 
	
		claimMap.field("key", "key");
		claimMap.field("currencyId.value", "currencyName");
		claimMap.field("provisionAmount", "provisionAmt");
		claimMap.field("claimId", "claimNumber");
		claimMap.field("registrationRemarks", "registerRemarks");
		claimMap.field("suggestedRejectionRemarks", "rejectionRemarks");
		claimMap.field("rejectionCategoryId.key", "rejectionCategory.id");
		claimMap.field("rejectionRemarks", "confirmRemarks");
		claimMap.field("medicalRemarks", "medicalRemarks");
		claimMap.field("doctorNote", "doctorNote");
		
		claimMap.register();
		
		claimMapper=mapperFactory.getMapperFacade(OMPClaim.class, ProcessRejectionDTO.class);
	}
	
	public OMPClaim getClaimForRejection(ProcessRejectionDTO processRejectionDto) {
		OMPClaim dest = claimMapper.mapReverse(processRejectionDto);
		return dest;
	}
	
	public ProcessRejectionDTO getProcessRejectionDTO(OMPClaim claim) {
		ProcessRejectionDTO dest = claimMapper.map(claim);
		return dest;
	}
	
	public static OMPProcessRejectionMapper getInstance(){
        if(myObj == null){
            myObj = new OMPProcessRejectionMapper();
            getAllMapValues();
        }
        return myObj;
	 }



}

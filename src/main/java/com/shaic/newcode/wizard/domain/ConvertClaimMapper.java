package com.shaic.newcode.wizard.domain;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Claim;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.Preauth;
import com.shaic.newcode.wizard.dto.ConvertClaimDTO;

public class ConvertClaimMapper {
	
	static MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
	
	static ConvertClaimMapper myObj;
	
	private static BoundMapperFacade<Claim,ConvertClaimDTO> claimMapper;
	private static BoundMapperFacade<Preauth, ConvertClaimDTO>preauthMapper;
	private static BoundMapperFacade<Status, ConvertClaimDTO>statusMapper;
	
	private static ClassMapBuilder<Claim, ConvertClaimDTO> claimMap = mapperFactory.classMap(Claim.class,ConvertClaimDTO.class);
	private static ClassMapBuilder<Preauth,ConvertClaimDTO>preauthMap=mapperFactory.classMap(Preauth.class, ConvertClaimDTO.class);
	private static ClassMapBuilder<Status, ConvertClaimDTO>statusMap=mapperFactory.classMap(Status.class,ConvertClaimDTO.class);
	
	public static void getAllMapValues()  {
		
		claimMap.field("key", "key");
		claimMap.field("claimedAmount", "claimedAmount");
		claimMap.field("provisionAmount", "provisionAmount");
//		claimMap.field("conversionReason.key", "conversionReason.id");
		claimMap.field("claimType.value", "claimType");
		claimMap.field("incidenceDate","accDeathDate");
		claimMap.field("injuryRemarks","injuryLossDetails");
		claimMap.field("intimation.cpuCode.cpuCode","currentCPUCode");
		claimMap.field("intimation.cpuCode.cpuCode","afterConvCPUCode");
		//statusMap.field("processValue", "claimStatus");
		preauthMap.field("status.processValue", "claimStatus");
		preauthMap.field("remarks", "remarks");		
//		claimMap.field("", "");
		
		claimMap.register();
		preauthMap.register();
		statusMap.register();
		
		claimMapper=mapperFactory.getMapperFacade(Claim.class, ConvertClaimDTO.class);
		preauthMapper=mapperFactory.getMapperFacade(Preauth.class,ConvertClaimDTO.class);
		statusMapper=mapperFactory.getMapperFacade(Status.class,ConvertClaimDTO.class);
		
	}
	
	public Claim getClaim(ConvertClaimDTO convertClaimDto) {
		Claim dest = claimMapper.mapReverse(convertClaimDto);
		return dest;
	}
	
	public ConvertClaimDTO getClaimDTO(Claim claim) {
		ConvertClaimDTO dest = claimMapper.map(claim);
		return dest;
	}
	
	public Preauth getPreauth(ConvertClaimDTO convertClaimDto) {
		Preauth dest = preauthMapper.mapReverse(convertClaimDto);
		return dest;
	}
	
	public ConvertClaimDTO getPreauthDTO(Preauth preauth) {
		ConvertClaimDTO dest = preauthMapper.map(preauth);
		return dest;
	}
	
	public Status getStatus(ConvertClaimDTO convertClaimDto) {
		Status dest = statusMapper.mapReverse(convertClaimDto);
		return dest;
	}
	
	public ConvertClaimDTO getStatusDTO(Status status) {
		ConvertClaimDTO dest = statusMapper.map(status);
		return dest;
	}
	
	public static ConvertClaimMapper getInstance(){
        if(myObj == null){
            myObj = new ConvertClaimMapper();
            getAllMapValues();
        }
        return myObj;
	}
	

}

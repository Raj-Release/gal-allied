package com.shaic.claim.reimbursement.rrc.services;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Claim;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class InitiateRRCRequestMapper {
	
	static InitiateRRCRequestMapper myObj;
	
	private static MapperFacade tableMapper;
	private static MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
	//private static ClassMapBuilder<Claim, InitiateRRCRequestTableDTO> classMapForInitiateRRCRequest = mapperFactory.classMap(Claim.class, InitiateRRCRequestTableDTO.class);
	private static ClassMapBuilder<Claim, InitiateRRCRequestTableDTO> classMapForInitiateRRCRequest = null;
	
	 public static void getAllMapValues()   {
		 
		 classMapForInitiateRRCRequest = mapperFactory.classMap(Claim.class, InitiateRRCRequestTableDTO.class);
		
		classMapForInitiateRRCRequest.field("intimation.hospital","hospitalNameId");
		classMapForInitiateRRCRequest.field("claimId","claimNo");
		classMapForInitiateRRCRequest.field("intimation.intimationId","intimationNo");
		classMapForInitiateRRCRequest.field("intimation.policy.policyNumber","policyNo");
		classMapForInitiateRRCRequest.field("intimation.insured.insuredName","insuredPatientName"); 
		classMapForInitiateRRCRequest.field("intimation.policy.product.value","productName");
		classMapForInitiateRRCRequest.field("intimation.cpuCode.cpuCode","cpuCode");
		classMapForInitiateRRCRequest.field("key","claimKey");
	
		
		classMapForInitiateRRCRequest.register();
		tableMapper = mapperFactory.getMapperFacade();
		
	}
	
	public static List<InitiateRRCRequestTableDTO> getInitiateRRCTableObjects(List<Claim> initiateRRCList)
    {
	List<InitiateRRCRequestTableDTO> InitiateRRCObjectList = tableMapper.mapAsList(initiateRRCList, InitiateRRCRequestTableDTO.class);
	return InitiateRRCObjectList;
    }
	
	
	public static InitiateRRCRequestMapper getInstance(){
        if(myObj == null){
            myObj = new InitiateRRCRequestMapper();
            getAllMapValues();
        }
        return myObj;
	 }
}

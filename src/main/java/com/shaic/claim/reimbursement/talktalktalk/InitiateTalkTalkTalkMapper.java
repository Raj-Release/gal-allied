package com.shaic.claim.reimbursement.talktalktalk;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Claim;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.OPClaim;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class InitiateTalkTalkTalkMapper {

static InitiateTalkTalkTalkMapper myObj;
	
	private static MapperFacade tableMapper;
	private static MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
	//private static ClassMapBuilder<Claim, InitiateRRCRequestTableDTO> classMapForInitiateRRCRequest = mapperFactory.classMap(Claim.class, InitiateRRCRequestTableDTO.class);
	private static ClassMapBuilder<Claim, InitiateTalkTalkTalkTableDTO> classMapForInitiateRRCRequest = null;
	private static ClassMapBuilder<OMPClaim, InitiateTalkTalkTalkTableDTO> classMapForTALKOMPRequest = null;
	private static ClassMapBuilder<OPClaim, InitiateTalkTalkTalkTableDTO> classMapForTALKOPRequest = null;
	
	 public static void getAllMapValues()   {
		 
		 classMapForInitiateRRCRequest = mapperFactory.classMap(Claim.class, InitiateTalkTalkTalkTableDTO.class);
		 classMapForTALKOMPRequest = mapperFactory.classMap(OMPClaim.class, InitiateTalkTalkTalkTableDTO.class);
		 classMapForTALKOPRequest = mapperFactory.classMap(OPClaim.class, InitiateTalkTalkTalkTableDTO.class);
		
		classMapForInitiateRRCRequest.field("intimation.hospital","hospitalNameId");
		classMapForInitiateRRCRequest.field("claimId","claimNo");
		classMapForInitiateRRCRequest.field("intimation.intimationId","intimationNo");
		classMapForInitiateRRCRequest.field("intimation.key","intimationKey");
		classMapForInitiateRRCRequest.field("intimation.policy.policyNumber","policyNo");
		classMapForInitiateRRCRequest.field("intimation.insured.insuredName","insuredPatientName"); 
		classMapForInitiateRRCRequest.field("intimation.policy.product.value","productName");
		classMapForInitiateRRCRequest.field("intimation.cpuCode.cpuCode","cpuCode");
		classMapForInitiateRRCRequest.field("key","claimKey");
		
		classMapForTALKOMPRequest.field("intimation.hospital","hospitalNameId");
		classMapForTALKOMPRequest.field("claimId","claimNo");
		classMapForTALKOMPRequest.field("intimation.intimationId","intimationNo");
		classMapForTALKOMPRequest.field("intimation.key","intimationKey");
		classMapForTALKOMPRequest.field("intimation.policy.policyNumber","policyNo");
		classMapForTALKOMPRequest.field("intimation.insured.insuredName","insuredPatientName"); 
		classMapForTALKOMPRequest.field("intimation.policy.product.value","productName");
		classMapForTALKOMPRequest.field("intimation.cpuCode.cpuCode","cpuCode");
		classMapForTALKOMPRequest.field("key","claimKey");
		
		classMapForTALKOPRequest.field("intimation.hospital","hospitalNameId");
		classMapForTALKOPRequest.field("claimId","claimNo");
		classMapForTALKOPRequest.field("intimation.intimationId","intimationNo");
		classMapForTALKOPRequest.field("intimation.key","intimationKey");
		classMapForTALKOPRequest.field("intimation.policy.policyNumber","policyNo");
		classMapForTALKOPRequest.field("intimation.insured.insuredName","insuredPatientName"); 
		classMapForTALKOPRequest.field("intimation.policy.product.value","productName");
		classMapForTALKOPRequest.field("intimation.cpuCode.cpuCode","cpuCode");
		classMapForTALKOPRequest.field("key","claimKey");
	
		
		classMapForInitiateRRCRequest.register();
		classMapForTALKOMPRequest.register();
		classMapForTALKOPRequest.register();
		tableMapper = mapperFactory.getMapperFacade();
		
	}
	
	public static List<InitiateTalkTalkTalkTableDTO> getInitiateTalkTalkTalkTableObjects(List<Claim> initiateRRCList)
    {
	List<InitiateTalkTalkTalkTableDTO> InitiateTalkTalkTalkObjectList = tableMapper.mapAsList(initiateRRCList, InitiateTalkTalkTalkTableDTO.class);
	return InitiateTalkTalkTalkObjectList;
    }
	
	public static List<InitiateTalkTalkTalkTableDTO> getInitiateTalkTalkTalkTableOMPObjects(List<OMPClaim> initiateRRCList)
    {
	List<InitiateTalkTalkTalkTableDTO> InitiateTalkTalkTalkObjectList = tableMapper.mapAsList(initiateRRCList, InitiateTalkTalkTalkTableDTO.class);
	return InitiateTalkTalkTalkObjectList;
    }
	
	public static List<InitiateTalkTalkTalkTableDTO> getInitiateTalkTalkTalkTableOPObjects(List<OPClaim> initiateRRCList)
    {
	List<InitiateTalkTalkTalkTableDTO> InitiateTalkTalkTalkObjectList = tableMapper.mapAsList(initiateRRCList, InitiateTalkTalkTalkTableDTO.class);
	return InitiateTalkTalkTalkObjectList;
    }
	
	
	
	public static InitiateTalkTalkTalkMapper getInstance(){
        if(myObj == null){
            myObj = new InitiateTalkTalkTalkMapper();
            getAllMapValues();
        }
        return myObj;
	 }
}

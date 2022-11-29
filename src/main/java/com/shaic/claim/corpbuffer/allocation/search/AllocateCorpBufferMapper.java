package com.shaic.claim.corpbuffer.allocation.search;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Claim;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class AllocateCorpBufferMapper {
	
	private static MapperFacade tableMapper;
	
	static AllocateCorpBufferMapper myObj;
	
	private static MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
	private static ClassMapBuilder<Claim, AllocateCorpBufferTableDTO> classMap = null;
	
	public static void getAllMapValues()  {
		
		classMap = mapperFactory.classMap(Claim.class, AllocateCorpBufferTableDTO.class);
			
		classMap.field("key", "key");
		classMap.field("intimation.intimationId","intimationNo");
		classMap.field("intimation.cpuCode.description","cpuCode");
		classMap.field("intimation.policy.policyNumber", "policyNo");
        classMap.field("intimation.insuredPatientName", "insuredPatientName");
        classMap.field("claimId", "claimNo");
        classMap.field("claimType.value","claimType");
        classMap.field("intimation.admissionDate","dateOfAdmission");
        classMap.field("intimation.policy.product.key", "productKey");
        classMap.field("intimation.hospital", "hospitalNameID");
        classMap.field("intimation.policy.key","policyKey");
        classMap.field("intimation.insured.insuredId", "insuredId");
        classMap.field("intimation.insured.key", "insuredKey");
        classMap.field("intimation.key","intimationKey");
        classMap.register();
		tableMapper = mapperFactory.getMapperFacade();
	} 
	
	public static List<AllocateCorpBufferTableDTO> getClaimDTO(List<Claim> claims) {
		List<AllocateCorpBufferTableDTO> allocateCorpBufferList = tableMapper.mapAsList(claims, AllocateCorpBufferTableDTO.class);
		return allocateCorpBufferList;
	} 
	
	public static AllocateCorpBufferMapper getInstance() {
		if (myObj == null) {
			myObj = new AllocateCorpBufferMapper();
			getAllMapValues();
		}
		return myObj;
	}

}

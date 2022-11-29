package com.shaic.claim.fieldvisit.search;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class SearchFieldVisitMapper {
	
	private static MapperFacade tableMapper;
	
	static SearchFieldVisitMapper myObj;


	public static void getAllMapValues()   {
		
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<FieldVisitRequest, SearchFieldVisitTableDTO> classMap = mapperFactory
				.classMap(FieldVisitRequest.class,
						SearchFieldVisitTableDTO.class);
		classMap.field("key", "key");
		classMap.field("intimation.intimationId", "intimationNo");
		classMap.field("policy.policyNumber", "policyNo");
		classMap.field("intimation.admissionDate", "dateOfAdmission");
		//classMap.field("claim.claimId", "claimNo");
		classMap.field("policy.lobId", "lob");
		classMap.field("intimation.insuredPatientName", "insuredPatiendName");
		classMap.field("intimation.intimaterName", "intimaterName");
		classMap.field("intimation.hospital", "hospitalNameID");
		classMap.field("intimation.intimationMode.value", "intimationMode");
		classMap.field("intimation.intimatedBy.value", "intimatedBy");
		classMap.field("intimation.admissionType", "admissionType");
		classMap.field("intimation.createdDate", "intimationDate");
		classMap.field("intimation.callerLandlineNumber", "callerMobileNumber");
		classMap.field("intimation.cpuCode.cpuCode", "cpucode");
		classMap.field("policy.product.value", "productname");
		classMap.field("intimation.admissionReason", "reasonForAdmission");
		classMap.field("fvrCpuId", "fvrCpuCode");
		//classMap.field("", "receivedate");
		//classMap.field("", "receiveddateandtime");
		//classMap.field("", "reasonforadmission"); 
		classMap.register();
		tableMapper = mapperFactory.getMapperFacade();
	}

	public static List<SearchFieldVisitTableDTO> getSearchPEDRequestApproveTableDTO(
			List<FieldVisitRequest> fieldVisitQueryList) {
		List<SearchFieldVisitTableDTO> mapAsList = tableMapper.mapAsList(
				fieldVisitQueryList, SearchFieldVisitTableDTO.class);
		return mapAsList;
	}
	
	public static SearchFieldVisitMapper getInstance(){
        if(myObj == null){
            myObj = new SearchFieldVisitMapper();
            getAllMapValues();
        }
        return myObj;
	 }

}

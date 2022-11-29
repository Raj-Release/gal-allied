package com.shaic.reimbursement.processfieldvisit.search;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.fieldvisit.search.SearchFieldVisitTableDTO;
import com.shaic.domain.Intimation;
import com.shaic.newcode.wizard.domain.MappingUtil;

/**
 * @author ntv.narenj
 *
 */
public class SearchProcessFieldVisitMapper {
	private static MapperFacade tableMapper;
	
	static SearchProcessFieldVisitMapper  myObj;
	
	public static void getAllMapValues()  {
 
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Intimation, SearchFieldVisitTableDTO> intimationClassMap = mapperFactor.classMap(Intimation.class, SearchFieldVisitTableDTO.class);
		
		intimationClassMap.field("key", "key");
		intimationClassMap.field("intimationId", "intimationNo");
		intimationClassMap.field("policy.policyNumber", "policyNo");
		intimationClassMap.field("admissionReason", "reasonForAdmission");
		intimationClassMap.field("insured.insuredName", "insuredPatiendName");
		intimationClassMap.field("intimaterName", "intimaterName");
		intimationClassMap.field("intimationMode.value", "intimationMode");
		intimationClassMap.field("intimatedBy.value", "intimatedBy");
		intimationClassMap.field("admissionDate", "dateOfAdmission");
		intimationClassMap.field("admissionType", "admissionType");
		intimationClassMap.field("modifiedDate", "intimationDate");
		intimationClassMap.field("callerLandlineNumber", "callerMobileNumber");
		intimationClassMap.field("cpuCode.cpuCode", "cpuId");
		intimationClassMap.field("hospital", "hospitalNameID");
		intimationClassMap.field("hospitalType.value", "hospitalType");
		
		intimationClassMap.register();
		
		tableMapper = mapperFactor.getMapperFacade();
	}
	
	public static List<SearchFieldVisitTableDTO> getIntimationDTO(List<Intimation> intimationData){
		List<SearchFieldVisitTableDTO> mapAsList = 
										tableMapper.mapAsList(intimationData, SearchFieldVisitTableDTO.class);
		return mapAsList;
		
	}
	
	public static SearchProcessFieldVisitMapper getInstance(){
        if(myObj == null){
            myObj = new SearchProcessFieldVisitMapper();
            getAllMapValues();
        }
        return myObj;
	 }

	
}

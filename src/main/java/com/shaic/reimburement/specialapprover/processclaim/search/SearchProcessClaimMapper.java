/**
 * 
 */
package com.shaic.reimburement.specialapprover.processclaim.search;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Intimation;
import com.shaic.newcode.wizard.domain.MappingUtil;

/**
 * @author ntv.narenj
 *
 */
public class SearchProcessClaimMapper {
	private static MapperFacade tableMapper;
	
	static SearchProcessClaimMapper myObj;

	public static void getAllMapValues()  {
 
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Intimation, SearchProcessClaimTableDTO> intimationClassMap = mapperFactor.classMap(Intimation.class, SearchProcessClaimTableDTO.class);
		
		intimationClassMap.field("key", "key");
		intimationClassMap.field("intimationId", "intimationNo");
		intimationClassMap.field("policy.policyNumber", "policyNo");
		intimationClassMap.field("admissionDate", "dateOfAdmission");
		intimationClassMap.field("admissionReason", "reasonForAdmission");
		intimationClassMap.field("insured.insuredName", "insuredPatientName");
		intimationClassMap.field("hospital", "hospitalNameID");
		intimationClassMap.field("hospitalType.value", "hospitalType");
		
		intimationClassMap.register();
		
		tableMapper = mapperFactor.getMapperFacade();
	}
	
	public static List<SearchProcessClaimTableDTO> getIntimationDTO(List<Intimation> intimationData){
		List<SearchProcessClaimTableDTO> mapAsList = 
										tableMapper.mapAsList(intimationData, SearchProcessClaimTableDTO.class);
		return mapAsList;
		
	}
	
	public static SearchProcessClaimMapper getInstance(){
        if(myObj == null){
            myObj = new SearchProcessClaimMapper();
            getAllMapValues();
        }
        return myObj;
	 }
	
}

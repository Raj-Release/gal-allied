/**
 * 
 */
package com.shaic.reimburement.specialapprover.approveclaim.search;

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
public class SearchApproveClaimMapper {
	private static MapperFacade tableMapper;
	
	static SearchApproveClaimMapper myObj;
		
	public static void getAllMapValues()  {

		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Intimation, SearchApproveClaimTableDTO> intimationClassMap = mapperFactor.classMap(Intimation.class, SearchApproveClaimTableDTO.class);
		
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
	
	public static List<SearchApproveClaimTableDTO> getIntimationDTO(List<Intimation> intimationData){
		List<SearchApproveClaimTableDTO> mapAsList = 
										tableMapper.mapAsList(intimationData, SearchApproveClaimTableDTO.class);
		return mapAsList;
		
	}
	
	public static SearchApproveClaimMapper getInstance(){
        if(myObj == null){
            myObj = new SearchApproveClaimMapper();
            getAllMapValues();
        }
        return myObj;
	 }

	
}

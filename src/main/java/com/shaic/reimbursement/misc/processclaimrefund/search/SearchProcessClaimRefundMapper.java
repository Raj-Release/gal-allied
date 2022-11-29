package com.shaic.reimbursement.misc.processclaimrefund.search;

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
public class SearchProcessClaimRefundMapper {

	private static MapperFacade tableMapper;
	
	static SearchProcessClaimRefundMapper myObj;

	public static void getAllMapValues()  {
 
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Intimation, SearchProcessClaimRefundTableDTO> intimationClassMap = mapperFactor.classMap(Intimation.class, SearchProcessClaimRefundTableDTO.class);
		
		intimationClassMap.field("key", "key");
		intimationClassMap.field("intimationId", "intimationNo");
		intimationClassMap.field("policy.policyNumber", "policyNo");
		intimationClassMap.field("admissionDate", "dateOfAdmission");
		intimationClassMap.field("admissionReason", "reasonForAdmission");
		intimationClassMap.field("insured.insuredName", "insuredPatientName");
		intimationClassMap.field("insured.healthCardNumber", "healthCardNo");
		intimationClassMap.field("hospital", "hospitalNameID");
		intimationClassMap.field("hospitalType.value", "hospitalType");
		
		intimationClassMap.register();
		
		tableMapper = mapperFactor.getMapperFacade();
	}
	
	public static List<SearchProcessClaimRefundTableDTO> getIntimationDTO(List<Intimation> intimationData){
		List<SearchProcessClaimRefundTableDTO> mapAsList = 
										tableMapper.mapAsList(intimationData, SearchProcessClaimRefundTableDTO.class);
		return mapAsList;
		
	}
	
	public static SearchProcessClaimRefundMapper getInstance(){
        if(myObj == null){
            myObj = new SearchProcessClaimRefundMapper();
            getAllMapValues();
        }
        return myObj;
	 }

	
	
}



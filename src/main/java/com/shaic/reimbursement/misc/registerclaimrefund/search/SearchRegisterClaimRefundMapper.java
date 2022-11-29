package com.shaic.reimbursement.misc.registerclaimrefund.search;

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
public class SearchRegisterClaimRefundMapper {

	private static MapperFacade tableMapper;
	
	static SearchRegisterClaimRefundMapper  myObj;

	public static void getAllMapValues()  {
 
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Intimation, SearchRegisterClaimRefundTableDTO> intimationClassMap = mapperFactor.classMap(Intimation.class, SearchRegisterClaimRefundTableDTO.class);
		
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
	
	public static List<SearchRegisterClaimRefundTableDTO> getIntimationDTO(List<Intimation> intimationData){
		List<SearchRegisterClaimRefundTableDTO> mapAsList = 
										tableMapper.mapAsList(intimationData, SearchRegisterClaimRefundTableDTO.class);
		return mapAsList;
		
	}
	
	public static SearchRegisterClaimRefundMapper getInstance(){
        if(myObj == null){
            myObj = new SearchRegisterClaimRefundMapper();
            getAllMapValues();
        }
        return myObj;
	 }
	
	
}



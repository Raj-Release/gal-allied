package com.shaic.reimbursement.rod.createrod.search;

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
public class SearchCreateRODMapper {
	
	
	static SearchCreateRODMapper myObj;

	
	private static MapperFacade tableMapper;
	public static void getAllMapValues()   {
		
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Intimation, SearchCreateRODTableDTO> intimationClassMap = mapperFactor.classMap(Intimation.class, SearchCreateRODTableDTO.class);
		
		intimationClassMap.field("key", "key");
		intimationClassMap.field("intimationId", "intimationNo");
		intimationClassMap.field("policy.policyNumber", "policyNo");
		intimationClassMap.field("insured.healthCardNumber", "healthCardIDNumber");
		intimationClassMap.field("cpuCode.cpuCode", "cpuCode");
		intimationClassMap.field("admissionReason", "reasonForAdmission");
		intimationClassMap.field("insured.insuredName", "insuredPatientName");
		intimationClassMap.field("policy.product.key", "productKey");
		intimationClassMap.field("paPatientName", "paPatientName");
		intimationClassMap.field("hospital", "hospitalNameID");
		intimationClassMap.field("hospitalType.value", "hospitalType");
		intimationClassMap.field("admissionDate", "dateOfAdmission1");
		
		intimationClassMap.register();
		
		tableMapper = mapperFactor.getMapperFacade();
	}
	
	public static List<SearchCreateRODTableDTO> getIntimationDTO(List<Intimation> intimationData){
		List<SearchCreateRODTableDTO> mapAsList = 
										tableMapper.mapAsList(intimationData, SearchCreateRODTableDTO.class);
		return mapAsList;
		
	}
	
	public static SearchCreateRODMapper getInstance(){
        if(myObj == null){
            myObj = new SearchCreateRODMapper();
            getAllMapValues();
        }
        return myObj;
	 }
	
}


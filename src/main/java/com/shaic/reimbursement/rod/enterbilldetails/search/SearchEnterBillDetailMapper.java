package com.shaic.reimbursement.rod.enterbilldetails.search;

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
public class SearchEnterBillDetailMapper {
	
	private static MapperFacade tableMapper;

	static SearchEnterBillDetailMapper myObj;

	public static void getAllMapValues()  {
 
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Intimation, SearchEnterBillDetailTableDTO> intimationClassMap = mapperFactor.classMap(Intimation.class, SearchEnterBillDetailTableDTO.class);
		
		intimationClassMap.field("key", "key");
		intimationClassMap.field("intimationId", "intimationNo");
		intimationClassMap.field("policy.policyNumber", "policyNo");
		intimationClassMap.field("admissionReason", "reasonForAdmission");
		intimationClassMap.field("insured.insuredName", "insuredPatientName");
		intimationClassMap.field("paPatientName", "paPatientName");
		intimationClassMap.field("policy.product.key", "productKey");
		intimationClassMap.field("cpuCode.cpuCode", "cpuCode");
		intimationClassMap.field("hospital", "hospitalNameID");
		intimationClassMap.field("hospitalType.value", "hospitalType");
		intimationClassMap.field("admissionDate", "dateOfAdmission1");
		
		intimationClassMap.register();
		
		tableMapper = mapperFactor.getMapperFacade();
	}
	
	public static List<SearchEnterBillDetailTableDTO> getIntimationDTO(List<Intimation> intimationData){
		List<SearchEnterBillDetailTableDTO> mapAsList = 
										tableMapper.mapAsList(intimationData, SearchEnterBillDetailTableDTO.class);
		return mapAsList;
		
	}
	
	public static SearchEnterBillDetailMapper getInstance(){
        if(myObj == null){
            myObj = new SearchEnterBillDetailMapper();
            getAllMapValues();
        }
        return myObj;
	 }
}

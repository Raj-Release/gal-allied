package com.shaic.reimbursement.manageclaim.SearchUpdateRodDetails;

import java.util.List;

import com.shaic.domain.Intimation;
import com.shaic.newcode.wizard.domain.MappingUtil;
import com.shaic.reimbursement.rod.enterbilldetails.search.SearchEnterBillDetailMapper;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

public class SearchUpdateRodDetailsMapper {
	
	private static MapperFacade tableMapper;
	
	static SearchUpdateRodDetailsMapper myObj;
	
	public static void getAllMapValues() {
		
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Intimation, SearchUpdateRodDetailsTableDTO> intimationMap = mapperFactor.classMap(Intimation.class, SearchUpdateRodDetailsTableDTO.class);
		
		intimationMap.field("key","key");
		intimationMap.field("intimationId","intimationNo");
		intimationMap.field("policy.policyNumber", "policyNo");
		intimationMap.field("admissionReason", "reasonForAdmission");
		intimationMap.field("insured.insuredName", "insuredPatientName");
		intimationMap.field("paPatientName", "paPatientName");
		intimationMap.field("policy.product.key", "productKey");
		intimationMap.field("cpuCode.cpuCode", "cpuCode");
		intimationMap.field("hospital", "hospitalNameID");
		intimationMap.field("hospitalType.value", "hospitalType");
		intimationMap.field("admissionDate", "dateOfAdmission1");
		
		intimationMap.register();
		
		tableMapper = mapperFactor.getMapperFacade();
		
		
	}

	public static List<SearchUpdateRodDetailsTableDTO> getIntimationDtls(List<Intimation> intimationData){
		List<SearchUpdateRodDetailsTableDTO> mapAsList = tableMapper.mapAsList(intimationData, SearchUpdateRodDetailsTableDTO.class);
		return mapAsList;
	}
	
	public static SearchUpdateRodDetailsMapper getInstance(){
        if(myObj == null){
            myObj = new SearchUpdateRodDetailsMapper();
            getAllMapValues();
        }
        return myObj;
	 }
}

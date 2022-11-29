package com.shaic.reimbursement.investigation.draftinvestigation.search;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Intimation;
import com.shaic.domain.Investigation;
import com.shaic.newcode.wizard.domain.MappingUtil;


/**
 * @author ntv.narenj
 *
 */
public class SearchDraftInvestigationMapper {

	
	static SearchDraftInvestigationMapper myObj;

	private static MapperFacade tableMapper;
	
	public static void getAllMapValues()   {
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Intimation, SearchDraftInvestigationTableDTO> intimationClassMap = mapperFactor.classMap(Intimation.class, SearchDraftInvestigationTableDTO.class);
		
		intimationClassMap.field("key", "key");
		intimationClassMap.field("intimationId", "intimationNo");
		intimationClassMap.field("policy.policyNumber", "policyNo");
		intimationClassMap.field("policy.lobId", "lOBId");
		intimationClassMap.field("cpuCode.cpuCode", "cpuCode");
		intimationClassMap.field("admissionReason", "reasonForAdmission");
		intimationClassMap.field("insured.insuredName", "insuredPatientName");
		intimationClassMap.field("hospital", "hospitalNameID");
		intimationClassMap.field("policy.productName", "productName");
		
		intimationClassMap.register();
		
		tableMapper = mapperFactor.getMapperFacade();
	}
	
	public static List<SearchDraftInvestigationTableDTO> getIntimationDTO(List<Intimation> intimationData){
		List<SearchDraftInvestigationTableDTO> mapAsList = 
										tableMapper.mapAsList(intimationData, SearchDraftInvestigationTableDTO.class);
		return mapAsList;
		
	}
	public static SearchDraftInvestigationMapper getInstance(){
        if(myObj == null){
            myObj = new SearchDraftInvestigationMapper();
            getAllMapValues();
        }
        return myObj;
	 }
	
}

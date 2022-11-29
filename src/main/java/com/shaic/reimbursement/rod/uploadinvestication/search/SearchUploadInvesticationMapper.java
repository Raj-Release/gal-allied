package com.shaic.reimbursement.rod.uploadinvestication.search;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Claim;
import com.shaic.newcode.wizard.domain.MappingUtil;


/**
 * @author ntv.narenj
 *
 */
public class SearchUploadInvesticationMapper {
	private static MapperFacade tableMapper;
	
	static SearchUploadInvesticationMapper  myObj;

	public static void getAllMapValues()  {
 
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Claim, SearchUploadInvesticationTableDTO> intimationClassMap = mapperFactor.classMap(Claim.class, SearchUploadInvesticationTableDTO.class);
		
		intimationClassMap.field("intimation.key", "key");
		intimationClassMap.field("intimation.intimationId", "intimationNo");
		intimationClassMap.field("intimation.policy.policyNumber", "policyNo");
		intimationClassMap.field("intimation.admissionReason", "reasonForAdmission");
		intimationClassMap.field("intimation.insured.insuredName", "insuredPatientName");
		intimationClassMap.field("intimation.hospital", "hospitalNameID");
		intimationClassMap.field("intimation.hospitalType.value", "hospitalType");
		intimationClassMap.field("claimType.value", "claimType");
		
		intimationClassMap.register();
		tableMapper = mapperFactor.getMapperFacade();
	}
	
	//public static List<SearchUploadPanCardTableDTO> getIntimationDTO(List<Intimation> intimationData){
	public static List<SearchUploadInvesticationTableDTO> getIntimationDTO(List<Claim> claimData){
		List<SearchUploadInvesticationTableDTO> mapAsList = 
										tableMapper.mapAsList(claimData, SearchUploadInvesticationTableDTO.class);
		return mapAsList;
		
	}
	
	public static SearchUploadInvesticationMapper getInstance(){
        if(myObj == null){
            myObj = new SearchUploadInvesticationMapper();
            getAllMapValues();
        }
        return myObj;
	 }
	
	
}

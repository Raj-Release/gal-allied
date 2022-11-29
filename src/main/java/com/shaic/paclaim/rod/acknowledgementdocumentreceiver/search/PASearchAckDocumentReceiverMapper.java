package com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Claim;
import com.shaic.newcode.wizard.domain.MappingUtil;
import com.shaic.reimbursement.rod.acknowledgementdocumentreceiver.search.SearchAcknowledgementDocumentReceiverTableDTO;

public class PASearchAckDocumentReceiverMapper {

	
	private static MapperFacade tableMapper;
	
	static PASearchAckDocumentReceiverMapper myObj;
		
	public static void getAllMapValues() {
		
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Claim, SearchAcknowledgementDocumentReceiverTableDTO> intimationClassMap = mapperFactor.classMap(Claim.class, SearchAcknowledgementDocumentReceiverTableDTO.class);
		
		intimationClassMap.field("intimation.key", "key");
		intimationClassMap.field("intimation.intimationId", "intimationNo");
		intimationClassMap.field("intimation.policy.policyNumber", "policyNo");
		intimationClassMap.field("intimation.admissionDate", "dateOfAdmission1");
		intimationClassMap.field("intimation.admissionReason", "reasonForAdmission");
		intimationClassMap.field("intimation.cpuCode.cpuCode", "cpuCode");
		intimationClassMap.field("intimation.insured.insuredName", "insuredPatientName");
		intimationClassMap.field("intimation.insured.healthCardNumber", "healthCardNo");
		intimationClassMap.field("intimation.hospital", "hospitalNameID");
		intimationClassMap.field("intimation.hospitalType.value", "hospitalType");
		intimationClassMap.field("claimId", "claimNo");
		intimationClassMap.field("key", "claimKey");
		
		intimationClassMap.register();
		
		tableMapper = mapperFactor.getMapperFacade();
	}
	
	public static List<SearchAcknowledgementDocumentReceiverTableDTO> getIntimationDTO(List<Claim> intimationData){
		List<SearchAcknowledgementDocumentReceiverTableDTO> mapAsList = 
										tableMapper.mapAsList(intimationData, SearchAcknowledgementDocumentReceiverTableDTO.class);
		return mapAsList;
		
	}
	
	public static PASearchAckDocumentReceiverMapper getInstance(){
        if(myObj == null){
            myObj = new PASearchAckDocumentReceiverMapper();
            getAllMapValues();
        }
        return myObj;
	 }
}

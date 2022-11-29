package com.shaic.reimbursement.rod.acknowledgementdocumentreceiver.search;

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
public class SearchAcknowledgementDocumentReceiverMapper {

	private static MapperFacade tableMapper;
	
	static SearchAcknowledgementDocumentReceiverMapper myObj;
	
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
		intimationClassMap.field("intimation.policy.product.key", "productKey");
		intimationClassMap.field("intimation.paPatientName", "paPatientName");
		intimationClassMap.field("intimation.insured.healthCardNumber", "healthCardNo");
		intimationClassMap.field("intimation.hospital", "hospitalNameID");
		intimationClassMap.field("intimation.hospitalType.value", "hospitalType");
		intimationClassMap.field("claimId", "claimNo");
		intimationClassMap.field("key", "claimKey");
		intimationClassMap.field("incidenceFlag", "incidenceFlag");
		
		intimationClassMap.register();
		
		tableMapper = mapperFactor.getMapperFacade();
	}
	
	public static List<SearchAcknowledgementDocumentReceiverTableDTO> getIntimationDTO(List<Claim> intimationData){
		List<SearchAcknowledgementDocumentReceiverTableDTO> mapAsList = 
										tableMapper.mapAsList(intimationData, SearchAcknowledgementDocumentReceiverTableDTO.class);
		return mapAsList;
		
	}
	
	public static SearchAcknowledgementDocumentReceiverMapper getInstance(){
        if(myObj == null){
            myObj = new SearchAcknowledgementDocumentReceiverMapper();
            getAllMapValues();
        }
        return myObj;
	 }
	
	
}

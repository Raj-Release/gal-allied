/**
 * 
 */
package com.shaic.claim.reimbursement.rrc.services;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.RRCRequest;
import com.shaic.newcode.wizard.domain.MappingUtil;

/**
 * @author ntv.vijayar
 *
 */
public class SearchRRCStatusMapper {
	
	
	private static MapperFacade tableMapper;
	
	static SearchRRCStatusMapper  myObj;

	
	public static void getAllMapValues()  {

	
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<RRCRequest, SearchRRCStatusTableDTO> rrcRequestMap = mapperFactory.classMap(RRCRequest.class, SearchRRCStatusTableDTO.class);
		
		rrcRequestMap.field("rrcRequestKey", "key");
		rrcRequestMap.field("reimbursement.key", "rodKey");
		rrcRequestMap.field("claim.key", "claimKey");
		rrcRequestMap.field("claim.intimation.cpuCode.key", "cpuCode");
		rrcRequestMap.field("claim.intimation.cpuCode.description", "cpuName");
		
		rrcRequestMap.field("claim.intimation.intimationId", "intimationNo");
		rrcRequestMap.field("rrcRequestNumber", "rrcRequestNo");
		rrcRequestMap.field("rrcInitiatedDate", "dateOfRequest");
		rrcRequestMap.field("requestorID", "requestorId");
		rrcRequestMap.field("requestedTypeId.value", "rrcRequestType");
		rrcRequestMap.field("eligiblityTypeId.value","eligibilityValue");
		
		// added for excel sheet.
		rrcRequestMap.field("claim.intimation.cpuCode.description", "cpuCode");
		rrcRequestMap.field("claim.intimation.policy.product.value", "productName");
		rrcRequestMap.field("claim.intimation.policy.product.code", "productCode");
		
		rrcRequestMap.field("claim.claimType.value","claimType");
		rrcRequestMap.field("diagnosis","diag");
		rrcRequestMap.field("management","management");
		rrcRequestMap.field("claim.intimation.insured.insuredName","patientName");
		rrcRequestMap.field("claim.intimation.hospital","hospitalId");
		rrcRequestMap.field("preAuthAmount","amountClaimed");
		rrcRequestMap.field("settlementAmount","settledAmount");
		rrcRequestMap.field("savedAmount","amountSaved");
		rrcRequestMap.field("eligiblityTypeId.value", "status");
		rrcRequestMap.field("status.key", "rrcStatusId");
		rrcRequestMap.field("claim.intimation.policy.totalSumInsured", "presentSumInsured");
		rrcRequestMap.field("claim.intimation.insured.healthCardNumber", "healthCardNo");
		rrcRequestMap.field("claim.intimation.cpuCode.cpuCode", "cpuDivString");
		rrcRequestMap.field("claim.intimation.insured.key","insuredKey");
		rrcRequestMap.field("requestRemarks", "initiateRRCRemarks");
		rrcRequestMap.field("eligibiltyRemarks", "processRRCRemarks");
		rrcRequestMap.field("requestorID","requestorName");
		rrcRequestMap.field("processedDate","rrcProcessedDate");
		rrcRequestMap.field("reviewedDate","rrcReviewedDate");
		rrcRequestMap.field("modifiedDate","rrcModifedDate");
		rrcRequestMap.field("status.key","statusKey");
		rrcRequestMap.field("rrcType","rrcType");
		rrcRequestMap.field("requestorSavedAmount","requestorSavedAmount");

		rrcRequestMap.field("modifierSavedAmount","modifierSavedAmout");

		


		
		
		
		
		rrcRequestMap.register();
		
		tableMapper = mapperFactory.getMapperFacade();

	
	}


	@SuppressWarnings("unused")
	public  List<SearchRRCStatusTableDTO> getRRCRequestList(
			List<RRCRequest> rrcRequestList) {
		List<SearchRRCStatusTableDTO> mapAsList = tableMapper
				.mapAsList(rrcRequestList,
						SearchRRCStatusTableDTO.class);
		return mapAsList;
	}
	
	public static SearchRRCStatusMapper getInstance(){
        if(myObj == null){
            myObj = new SearchRRCStatusMapper();
            getAllMapValues();
        }
        return myObj;
	 }

}
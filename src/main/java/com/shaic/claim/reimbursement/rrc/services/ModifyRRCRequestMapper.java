/**
 * 
 */
package com.shaic.claim.reimbursement.rrc.services;

import java.util.List;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.domain.RRCRequest;
import com.shaic.newcode.wizard.domain.MappingUtil;

/**
 * @author ntv.vijayar
 *
 */
public class ModifyRRCRequestMapper {
	
	static ModifyRRCRequestMapper myObj;
	private static MapperFacade tableMapper;
	private static BoundMapperFacade<RRCRequest, RRCDTO> rrcReviewDataMapper;
	
	 public static void getAllMapValues() {
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<RRCRequest, SearchModifyRRCRequestTableDTO> rrcRequestMap = mapperFactory.classMap(RRCRequest.class, SearchModifyRRCRequestTableDTO.class);
		ClassMapBuilder<RRCRequest, RRCDTO> rrcReviewRRCRequestDataMap  = mapperFactory.classMap(RRCRequest.class, RRCDTO.class);
		
		rrcRequestMap.field("rrcRequestKey", "key");
		rrcRequestMap.field("reimbursement.key", "rodKey");
		rrcRequestMap.field("claim.key", "claimKey");
		rrcRequestMap.field("claim.intimation.intimationId", "intimationNo");
		rrcRequestMap.field("rrcRequestNumber", "rrcRequestNo");
		rrcRequestMap.field("rrcInitiatedDate", "dateOfRequest");
		rrcRequestMap.field("requestorID", "requestorId");
		rrcRequestMap.field("requestedTypeId.value", "rrcRequestType");
		rrcRequestMap.field("eligiblityTypeId.value","eligibilityValue");
		
		rrcReviewRRCRequestDataMap.field("rrcRequestNumber","quantumReductionDetailsDTO.requestNo");
		rrcReviewRRCRequestDataMap.field("preAuthAmount","quantumReductionDetailsDTO.preAuthAmount");
		rrcReviewRRCRequestDataMap.field("finalBillAmount","quantumReductionDetailsDTO.finalBillAmount");
		rrcReviewRRCRequestDataMap.field("settlementAmount","quantumReductionDetailsDTO.settlementAmount");
		rrcReviewRRCRequestDataMap.field("anhAmount","quantumReductionDetailsDTO.anhAmount");
		rrcReviewRRCRequestDataMap.field("savedAmount","quantumReductionDetailsDTO.savedAmount");
		rrcReviewRRCRequestDataMap.field("diagnosis","quantumReductionDetailsDTO.diagnosis");
		rrcReviewRRCRequestDataMap.field("management","quantumReductionDetailsDTO.management");
		rrcReviewRRCRequestDataMap.field("significantClinicalId.key","significantClinicalInformation.id");
		rrcReviewRRCRequestDataMap.field("requestRemarks","remarks");
		rrcReviewRRCRequestDataMap.field("anh","quantumReductionDetailsDTO.anhFlag");
		rrcReviewRRCRequestDataMap.field("eligiblityTypeId.key","rrcRequestRRCEligiblityId");
		rrcReviewRRCRequestDataMap.field("eligiblityTypeId.value","requestRRCElgilibilityValue");	
		rrcReviewRRCRequestDataMap.field("reviewerEligibilityTypeId.key","eligibility.id");
		rrcReviewRRCRequestDataMap.field("reviewerEligibilityTypeId.value","eligibility.value");	
		rrcReviewRRCRequestDataMap.field("modifierEligibilityTypeId.key","rrcModifiedEligbility.id");
		rrcReviewRRCRequestDataMap.field("modifierEligibilityTypeId.value","rrcModifiedEligbility.value");
		rrcReviewRRCRequestDataMap.field("eligibiltyRemarks","requestEligbilityRRCRemarks");
		rrcReviewRRCRequestDataMap.field("reviewedBy","strUserName");
		rrcReviewRRCRequestDataMap.field("createdBy","strUserName");
		rrcReviewRRCRequestDataMap.field("reviewerSavedAmount", "savedAmount");
		rrcReviewRRCRequestDataMap.field("requestorSavedAmount","requestRRCSavedAmount");	
		rrcReviewRRCRequestDataMap.field("modifierSavedAmount","rrcModifiedSavedAmount");
		rrcReviewRRCRequestDataMap.field("modifyRemarks","rrcModifiedRemarks");		
		
		/**
		 * The amount entered in quantum reduction details table will be updated by saved amount entered
		 * in review stage.
		 * */
		
		rrcReviewRRCRequestDataMap.field("savedAmount","savedAmount");
		rrcReviewRRCRequestDataMap.field("reviewRemarks","eligibilityRemarks");
		
		
		rrcRequestMap.register();
		rrcReviewRRCRequestDataMap.register();
		
		tableMapper = mapperFactory.getMapperFacade();
		rrcReviewDataMapper = mapperFactory.getMapperFacade(RRCRequest.class, RRCDTO.class);

	
	}
	
	public static RRCRequest getRRCRequestData(RRCDTO rrcDTO) {
		RRCRequest rrcRequest = rrcReviewDataMapper.mapReverse(rrcDTO);
		return rrcRequest;
	}


	@SuppressWarnings("unused")
	public  List<SearchModifyRRCRequestTableDTO> getRRCRequestList(
			List<RRCRequest> rrcRequestList) {
		List<SearchModifyRRCRequestTableDTO> mapAsList = tableMapper
				.mapAsList(rrcRequestList,
						SearchModifyRRCRequestTableDTO.class);
		return mapAsList;
	}
	
	public static ModifyRRCRequestMapper getInstance(){
        if(myObj == null){
            myObj = new ModifyRRCRequestMapper();
            getAllMapValues();
        }
        return myObj;
	 }

}
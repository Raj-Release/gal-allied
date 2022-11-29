/**
 * 
 */
package com.shaic.claim.reimbursement.rrc.services;

/**
 * @author ntv.vijayar
 *
 */

import java.util.List;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.reimbursement.dto.ExtraEmployeeEffortDTO;
import com.shaic.claim.reimbursement.dto.QuantumReductionDetailsDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.domain.RRCCategory;
import com.shaic.domain.RRCDetails;
import com.shaic.domain.RRCRequest;
import com.shaic.newcode.wizard.domain.MappingUtil;


/**
 * @author ntv.vijayar
 *
 */
public class ReviewRRCRequestMapper {
	
	
	static ReviewRRCRequestMapper myObj;
	
	private static MapperFacade tableMapper;
	
	private static MapperFacade rrcDetailsMapper;
	private static MapperFacade rrcRequestDataMapper;
	
	private static BoundMapperFacade<RRCDetails, ExtraEmployeeEffortDTO> rrcEmployeeDetailsMapper;
	
	private static BoundMapperFacade<RRCCategory, ExtraEmployeeEffortDTO> rrcCategoryDetailsMapper;
	
	private static BoundMapperFacade<RRCRequest, RRCDTO> rrcReviewDataMapper;
	
	
	public static void getAllMapValues()  {

		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		//This map is used by search process rrc request service, to map rrc request with table dto.
		ClassMapBuilder<RRCRequest, SearchReviewRRCRequestTableDTO> rrcRequestMap = mapperFactory.classMap(RRCRequest.class, SearchReviewRRCRequestTableDTO.class);
		ClassMapBuilder<RRCRequest, RRCDTO> rrcReviewRRCRequestDataMap  = mapperFactory.classMap(RRCRequest.class, RRCDTO.class);
		ClassMapBuilder<RRCDetails, ExtraEmployeeEffortDTO> rrcEmployeeDetailsMap = mapperFactory.classMap(RRCDetails.class,ExtraEmployeeEffortDTO.class);
		ClassMapBuilder<RRCCategory, ExtraEmployeeEffortDTO> rrcCategoryDetailsMap = mapperFactory.classMap(RRCCategory.class,ExtraEmployeeEffortDTO.class);
		
		
		// rrcRequestMap.field(<DO>,<DTO>);
		
		rrcRequestMap.field("rrcRequestKey", "key");
		rrcRequestMap.field("reimbursement.key", "rodKey");
		rrcRequestMap.field("claim.key", "claimKey");
		rrcRequestMap.field("claim.intimation.intimationId", "intimationNo");
		rrcRequestMap.field("claim.intimation.key", "intimationKey");
		rrcRequestMap.field("rrcRequestNumber", "rrcRequestNo");
		rrcRequestMap.field("rrcInitiatedDate", "dateOfRequest");
		rrcRequestMap.field("requestorID", "requestorId");
		rrcRequestMap.field("requestedTypeId.value", "rrcRequestType");
		rrcRequestMap.field("eligiblityTypeId.value","eligibilityValue");
		
		
		rrcEmployeeDetailsMap.field("rrcRequest","rrcRequestKey");
		rrcEmployeeDetailsMap.field("employeeId","employeeId");
		rrcEmployeeDetailsMap.field("employeeName","selEmployeeName.value");
		rrcEmployeeDetailsMap.field("creditTypeId.key","creditType.id");
		rrcEmployeeDetailsMap.field("creditTypeId.value","creditType.value");
		rrcEmployeeDetailsMap.field("score","score");
		rrcEmployeeDetailsMap.field("remarks","remarks");
		rrcEmployeeDetailsMap.field("rrcDetailsKey","rrcDetailsKey");
		rrcEmployeeDetailsMap.field("contributorTypeId.key","typeOfContributor.id");

		
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
		
		rrcReviewRRCRequestDataMap.field("eligiblityTypeId.key","eligibility.id");
		rrcReviewRRCRequestDataMap.field("eligiblityTypeId.value","eligibility.value");
		
		rrcReviewRRCRequestDataMap.field("reviewerEligibilityTypeId.key","reviewEligiblity.id");
		rrcReviewRRCRequestDataMap.field("reviewerEligibilityTypeId.value","reviewEligiblity.value");
		
		rrcReviewRRCRequestDataMap.field("eligibiltyRemarks","requestEligbilityRRCRemarks");
		rrcReviewRRCRequestDataMap.field("reviewedBy","strUserName");
		rrcReviewRRCRequestDataMap.field("createdBy","strUserName");
		rrcReviewRRCRequestDataMap.field("reviewerSavedAmount", "savedAmount");
		rrcReviewRRCRequestDataMap.field("requestorSavedAmount","requestRRCSavedAmount");
		rrcReviewRRCRequestDataMap.field("modifierEligibilityTypeId.key","rrcModifiedEligbility.id");
		rrcReviewRRCRequestDataMap.field("modifierEligibilityTypeId.value","rrcModifiedEligbility.value");
		rrcReviewRRCRequestDataMap.field("modifierSavedAmount","rrcModifiedSavedAmount");
		rrcReviewRRCRequestDataMap.field("modifyRemarks","rrcModifiedRemarks");	
		rrcReviewRRCRequestDataMap.field("anh","quantumReductionDetailsDTO.anhFlag");
		
		/**
		 * The amount entered in quantum reduction details table will be updated by saved amount entered
		 * in review stage.
		 * */
		
		rrcReviewRRCRequestDataMap.field("savedAmount","savedAmount");
		rrcReviewRRCRequestDataMap.field("reviewRemarks","eligibilityRemarks");
		
		
		
		rrcCategoryDetailsMap.field("rrcCategoryKey", "rrcCategoryKey");
		rrcCategoryDetailsMap.field("categoryId.key", "category.id");
		rrcCategoryDetailsMap.field("subCategorykey", "subCategory.id");
		rrcCategoryDetailsMap.field("sourcekey", "sourceOfIdentification.id");
		rrcCategoryDetailsMap.field("talkSpokento", "talkSpokento");
		rrcCategoryDetailsMap.field("talkSpokenDate", "talkSpokenDate");
		rrcCategoryDetailsMap.field("talkMobto", "talkMobto");
		
		rrcRequestMap.register();
		rrcReviewRRCRequestDataMap.register();
		rrcEmployeeDetailsMap.register();
		rrcCategoryDetailsMap.register();
	
		tableMapper = mapperFactory.getMapperFacade();
		rrcDetailsMapper = mapperFactory.getMapperFacade();
		rrcRequestDataMapper = mapperFactory.getMapperFacade();
		rrcEmployeeDetailsMapper = mapperFactory.getMapperFacade(RRCDetails.class, ExtraEmployeeEffortDTO.class);
		rrcCategoryDetailsMapper = mapperFactory.getMapperFacade(RRCCategory.class, ExtraEmployeeEffortDTO.class);
		rrcReviewDataMapper = mapperFactory.getMapperFacade(RRCRequest.class, RRCDTO.class);
	}
	
	@SuppressWarnings("unused")
	public  List<SearchReviewRRCRequestTableDTO> getRRCRequestList(
			List<RRCRequest> rrcRequestList) {
		List<SearchReviewRRCRequestTableDTO> mapAsList = tableMapper
				.mapAsList(rrcRequestList,
						SearchReviewRRCRequestTableDTO.class);
		return mapAsList;
	}
	
	public List<ExtraEmployeeEffortDTO> getEmployeeListenerTableData(List<RRCDetails> rrcDetailsList)
	{
		List<ExtraEmployeeEffortDTO> mapAsList = rrcDetailsMapper
				.mapAsList(rrcDetailsList,
						ExtraEmployeeEffortDTO.class);
		return mapAsList;
	}
	
	public List<QuantumReductionDetailsDTO> getQuantumReductionDetailsList(List<RRCRequest> rrcDetailsList)
	{
		List<QuantumReductionDetailsDTO> mapAsList = rrcRequestDataMapper
				.mapAsList(rrcDetailsList,
						QuantumReductionDetailsDTO.class);
		return mapAsList;
	}
	
	public RRCDetails getRRCDetailsForEmployee(ExtraEmployeeEffortDTO extraEmployeeEffortDTO)
	{
		RRCDetails rrcDetails = rrcEmployeeDetailsMapper.mapReverse(extraEmployeeEffortDTO);
		return rrcDetails;
	}
	
	public RRCCategory getRRCCategoryDetails(ExtraEmployeeEffortDTO extraEmployeeEffortDTO)
	{
		RRCCategory rrcCategory = rrcCategoryDetailsMapper.mapReverse(extraEmployeeEffortDTO);
		return rrcCategory;
		
	}
	
	public RRCRequest getRRCRequestData(RRCDTO rrcDTO) {
		RRCRequest rrcRequest = rrcReviewDataMapper.mapReverse(rrcDTO);
		return rrcRequest;
	}
	
	public static ReviewRRCRequestMapper getInstance(){
        if(myObj == null){
            myObj = new ReviewRRCRequestMapper();
            getAllMapValues();
        }
        return myObj;
	 }


}

package com.shaic.newcode.wizard.domain;

import java.util.List;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.pedrequest.approve.PEDRequestDetailsApproveDTO;
import com.shaic.claim.preauth.wizard.dto.NewInitiatePedEndorsementDTO;
import com.shaic.claim.preauth.wizard.dto.OldPedEndorsementDTO;
import com.shaic.domain.preauth.NewInitiatePedEndorsement;
import com.shaic.domain.preauth.OldInitiatePedEndorsement;
import com.shaic.domain.preauth.PedQuery;

public class PEDQueryMapper {
	
	static PEDQueryMapper myObj;
	
	static MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
	
	private static MapperFacade pedDetailsMapper;
	
	private static MapperFacade pedApprovalMapper;
	
	private static MapperFacade newInitiateMapper;
	
	private static BoundMapperFacade<OldInitiatePedEndorsement, OldPedEndorsementDTO> pedqueryMapper;
	
	private static BoundMapperFacade<PedQuery, OldPedEndorsementDTO> pedqueryRemarksMapper;
	
	private static BoundMapperFacade<OldInitiatePedEndorsement,PEDRequestDetailsApproveDTO>pedApproveDetailsMapper;

	
	private static ClassMapBuilder<OldInitiatePedEndorsement, OldPedEndorsementDTO> pedDetailsMap = mapperFactory.classMap(OldInitiatePedEndorsement.class,OldPedEndorsementDTO.class);
	private static ClassMapBuilder<OldInitiatePedEndorsement, OldPedEndorsementDTO> pedqueryMap = mapperFactory.classMap(OldInitiatePedEndorsement.class,OldPedEndorsementDTO.class);
	private static ClassMapBuilder<PedQuery, OldPedEndorsementDTO> pedqueryRemarksMap = mapperFactory.classMap(PedQuery.class,OldPedEndorsementDTO.class);
	private static ClassMapBuilder<OldInitiatePedEndorsement, PEDRequestDetailsApproveDTO> pedApproval = mapperFactory.classMap(OldInitiatePedEndorsement.class,PEDRequestDetailsApproveDTO.class);
	private static ClassMapBuilder<OldInitiatePedEndorsement, PEDRequestDetailsApproveDTO> pedApprovalDetails = mapperFactory.classMap(OldInitiatePedEndorsement.class,PEDRequestDetailsApproveDTO.class);
	private static ClassMapBuilder<NewInitiatePedEndorsement, NewInitiatePedEndorsementDTO> newInitiateMap=mapperFactory.classMap(NewInitiatePedEndorsement.class,NewInitiatePedEndorsementDTO.class);
	
	 public static void getAllMapValues()  { 
		 
		pedDetailsMap.field("key", "key");
//		pedDetailsMap.field("preExistingDisease.value", "pedSuggestionName");
		pedDetailsMap.field("pedName", "pedName");
		pedDetailsMap.field("pedSuggestion.key", "pedSuggestion.id");
		pedDetailsMap.field("pedSuggestion.value", "pedSuggestion.value");
		pedDetailsMap.field("remarks", "remarks");
		pedDetailsMap.field("escalateRemarks", "escalateRemarks");
		pedDetailsMap.field("repudiationLetterDate", "repudiationLetterDate");
		
		pedDetailsMap.field("createdBy", "requestorId");
		pedDetailsMap.field("createdDate", "requestedDate");
		pedDetailsMap.field("status.processValue", "requestStatus");
		
		
		pedqueryMap.field("key", "key");
		//pedqueryMap.field("preExistingDisease.value", "pedName");
//		pedqueryMap.field("preExistingDisease.key","");
//		pedqueryMap.field("preExistingDisease.value","");
		pedqueryMap.field("remarks", "remarks");
		pedqueryMap.field("escalateRemarks", "escalateRemarks");
		pedqueryMap.field("repudiationLetterDate", "repudiationLetterDate");
		pedqueryMap.field("pedSuggestion.key", "pedSuggestion.id");
		pedqueryMap.field("pedSuggestion.value", "pedSuggestion.value");
		pedqueryMap.field("pedName", "pedName");		
		
		pedqueryRemarksMap.field("queryRemarks", "reasonforReferring");
		pedqueryRemarksMap.field("replyRemarks", "replyRemarks");
		
		pedApproval.field("key", "key");
//		pedDetailsMap.field("preExistingDisease.value", "pedSuggestionName");
		pedApproval.field("pedName", "pedApprovalName");
		pedApproval.field("pedSuggestion.key", "pedSuggestion.id");
		pedApproval.field("pedSuggestion.value", "pedSuggestion.value");
		pedApproval.field("remarks", "remarks");
		pedApproval.field("escalateRemarks", "escalateRemarks");
		pedApproval.field("repudiationLetterDate", "repudiationLetterDate");
		pedApproval.field("createdBy", "requestorId");
		pedApproval.field("createdDate", "requestedDate");
		pedApproval.field("status.processValue", "requestStatus");
		
		pedApprovalDetails.field("key", "key");
		pedApprovalDetails.field("pedName", "pedApprovalName");
		pedApprovalDetails.field("remarks", "remarks");
		pedApprovalDetails.field("repudiationLetterDate", "repudiationLetterDate");
		pedApprovalDetails.field("pedSuggestion.key", "pedSuggestion.id");
		pedApprovalDetails.field("pedSuggestion.value", "pedSuggestion.value");
		pedApprovalDetails.field("processorRemarks", "processorRemarks");
		pedApprovalDetails.field("escalateRemarks", "escalateRemarks");
		
		newInitiateMap.field("description", "description");
		newInitiateMap.field("key","key");
		newInitiateMap.field("icdChapterId", "ICDChapterId");
		newInitiateMap.field("icdBlockId", "ICDBlockId");
		newInitiateMap.field("icdCodeId", "ICDCodeId");
		newInitiateMap.field("pedCode", "pedCodeId");
		newInitiateMap.field("source.key", "source.id");
		
		newInitiateMap.field("source.value", "source.value");
		newInitiateMap.field("deletedFlag", "deletedFlag");

		newInitiateMap.field("othesSpecify", "othersSpecify");
		newInitiateMap.field("doctorRemarks", "doctorRemarks");
		
//		newInitiateMap.field("", "ICDBlock");
//		newInitiateMap.field("", "ICDCode");
//		newInitiateMap.field("", "source.id");
//		newInitiateMap.field("", "source.value");
//		newInitiateMap.field("", "othersSpecify");
//		
		
		
		pedDetailsMapper = mapperFactory.getMapperFacade();	
		newInitiateMapper=mapperFactory.getMapperFacade();
		pedqueryMapper=mapperFactory.getMapperFacade(OldInitiatePedEndorsement.class, OldPedEndorsementDTO.class);
		pedqueryRemarksMapper=mapperFactory.getMapperFacade(PedQuery.class, OldPedEndorsementDTO.class);
		pedApprovalMapper= mapperFactory.getMapperFacade();
		pedApproveDetailsMapper=mapperFactory.getMapperFacade(OldInitiatePedEndorsement.class, PEDRequestDetailsApproveDTO.class);
	}
		
		
	public static List<OldPedEndorsementDTO> getPEDQueryTableDTO(List<OldInitiatePedEndorsement> resultList) {
		pedDetailsMap.register();
		List<OldPedEndorsementDTO> mapAsList = pedDetailsMapper.mapAsList(resultList, OldPedEndorsementDTO.class);
			return mapAsList;
		
	}

	public static OldPedEndorsementDTO getPedDetailsDto(OldInitiatePedEndorsement resultList) {
		pedqueryMap.register();
		
		OldPedEndorsementDTO dest = pedqueryMapper.map(resultList);
		return dest;
	
	}
	
	public static OldInitiatePedEndorsement getOldInitiatePedEndorsement(OldPedEndorsementDTO bean) {
		OldInitiatePedEndorsement dest=pedqueryMapper.mapReverse(bean);
		return dest;
	}
	
	public static PEDRequestDetailsApproveDTO getPedApproveDto(OldInitiatePedEndorsement resultList) {
		pedApprovalDetails.register();
		
		PEDRequestDetailsApproveDTO dest = pedApproveDetailsMapper.map(resultList);
		return dest;
	
	}
	
	public static OldInitiatePedEndorsement getPedApprove(PEDRequestDetailsApproveDTO bean) {
		OldInitiatePedEndorsement dest=pedApproveDetailsMapper.mapReverse(bean);
		return dest;
	}

	public static OldPedEndorsementDTO getpedQueryRemarks(PedQuery results) {
		pedqueryRemarksMap.register();
		OldPedEndorsementDTO dest = pedqueryRemarksMapper.map(results);
		return dest;
	}
	public static List<PEDRequestDetailsApproveDTO> getPEDQueryApprovalTableDTO(List<OldInitiatePedEndorsement> resultList) {
		pedApproval.register();
		List<PEDRequestDetailsApproveDTO> mapAsList = pedApprovalMapper.mapAsList(resultList, PEDRequestDetailsApproveDTO.class);
			return mapAsList;
	}
	
	public static List<NewInitiatePedEndorsementDTO> getinitiatePed(List<NewInitiatePedEndorsement> bean){
		newInitiateMap.register();
		List<NewInitiatePedEndorsementDTO> mapAsList=newInitiateMapper.mapAsList(bean, NewInitiatePedEndorsementDTO.class);
		return mapAsList;
	}
	
	public static List<NewInitiatePedEndorsement> getPedInitiateDetailsList(List<NewInitiatePedEndorsementDTO> bean){
		newInitiateMap.register();
		List<NewInitiatePedEndorsement> mapAsList=newInitiateMapper.mapAsList(bean, NewInitiatePedEndorsement.class);
		return mapAsList;
	}
	
	public static PEDQueryMapper getInstance(){
        if(myObj == null){
            myObj = new PEDQueryMapper();
            getAllMapValues();
        }
        return myObj;
	 }
	

	
}

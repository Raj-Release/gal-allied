/**
 * 
 */
package com.shaic.claim.rod.wizard.mapper;

import java.util.List;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.DocumentCheckListMaster;
import com.shaic.domain.RODDocumentCheckList;
import com.shaic.domain.Reimbursement;
import com.shaic.newcode.wizard.domain.MappingUtil;

/**
 * @author ntv.vijayar
 *
 */
public class AcknowledgeDocumentReceivedMapper {
	
	
	private static MapperFacade dtoMapper;
	private static MapperFacade tableMapper;
	private static MapperFacade reconsiderRODReqMapper;
	private static MapperFacade rodMapper;
	private static MapperFacade docChkListMapper;
	private static BoundMapperFacade<DocAcknowledgement, ReceiptOfDocumentsDTO> docAcknowledgeRecMapper;
	private static BoundMapperFacade<RODDocumentCheckList, DocumentCheckListDTO> rodDocCheckListMapper;
	private static BoundMapperFacade<ReconsiderRODRequestTableDTO, DocAcknowledgement> reconsiderDocAckList;
	private static BoundMapperFacade<ReconsiderRODRequestTableDTO, Reimbursement> reconsiderReimbList;
	
	
	//Added for create ROD Screen
	
	static AcknowledgeDocumentReceivedMapper myObj;
	
	 
	 public static void getAllMapValues() {
		 
		//Added for Acknowledge doc received screen
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<DocumentCheckListMaster, DocumentCheckListDTO> docCheckListMapper = mapperFactory.classMap(DocumentCheckListMaster.class,DocumentCheckListDTO.class);		
		ClassMapBuilder<DocAcknowledgement, ReceiptOfDocumentsDTO> docAckRecMapper = mapperFactory.classMap(DocAcknowledgement.class,ReceiptOfDocumentsDTO.class);
		ClassMapBuilder<RODDocumentCheckList, DocumentCheckListDTO> rodDocChkListMapper = mapperFactory.classMap(RODDocumentCheckList.class,DocumentCheckListDTO.class);
		ClassMapBuilder<DocAcknowledgement, ReconsiderRODRequestTableDTO> reconsiderRODRequestMapper = mapperFactory.classMap(DocAcknowledgement.class,ReconsiderRODRequestTableDTO.class);
		ClassMapBuilder<Reimbursement, ReconsiderRODRequestTableDTO> createRodMapper = mapperFactory.classMap(Reimbursement.class,ReconsiderRODRequestTableDTO.class);
		
	//	ClassMapBuilder<RODDocumentCheckList, DocumentCheckListDTO> cummlativeMapper = mapperFactory.classMap(RODDocumentCheckList.class,DocumentCheckListDTO.class);
		
		
	
		docCheckListMapper.field("key","key");
		docCheckListMapper.field("sequenceNumber","sequenceNumber");
		docCheckListMapper.field("value","value");
		docCheckListMapper.field("value","particulars.value");
		docCheckListMapper.field("value","particularsValue");
		docCheckListMapper.field("key","particulars.id");
		docCheckListMapper.field("mandatoryDocFlag","mandatoryDocFlag");
		docCheckListMapper.field("requiredDocType","requiredDocType");
		docCheckListMapper.field("activeStatus","activeStatus");
		docCheckListMapper.field("createdBy","createdBy");
		docCheckListMapper.field("createdDate","createdDate");
		docCheckListMapper.field("modifiedBy","modifiedBy");
		docCheckListMapper.field("modifiedDate","modifiedDate");
		
		
		//docAckRecMapper.field("key","key");
		
		docAckRecMapper.field("documentReceivedFromId.key", "documentDetails.documentsReceivedFrom.id");
		docAckRecMapper.field("documentReceivedFromId.value", "documentDetails.documentsReceivedFrom.value");
		
		docAckRecMapper.field("insuredContactNumber", "documentDetails.acknowledgmentContactNumber");
		docAckRecMapper.field("insuredEmailId", "documentDetails.emailId");
		docAckRecMapper.field("documentReceivedDate", "documentDetails.documentsReceivedDate");
		
		docAckRecMapper.field("modeOfReceiptId.key", "documentDetails.modeOfReceipt.id");
		docAckRecMapper.field("modeOfReceiptId.value", "documentDetails.modeOfReceipt.value");
		

		/*docAckRecMapper.field("documentTypeId.key", "documentDetails.documentTypeId.id");
		docAckRecMapper.field("documentTypeId.value", "documentDetails.documentTypeId.value");
		*/
		docAckRecMapper.field("reconsiderationRequest", "documentDetails.reconsiderationRequestFlag");
		
		docAckRecMapper.field("hospitalisationFlag", "documentDetails.hospitalizationFlag");
		docAckRecMapper.field("preHospitalisationFlag", "documentDetails.preHospitalizationFlag");
		docAckRecMapper.field("postHospitalisationFlag", "documentDetails.postHospitalizationFlag");
		docAckRecMapper.field("partialHospitalisationFlag", "documentDetails.partialHospitalizationFlag");
		docAckRecMapper.field("partialHospitalisationFlag", "documentDetails.partialHospitalizationFlag");
		docAckRecMapper.field("hospitalizationRepeatFlag", "documentDetails.hospitalizationRepeatFlag");
		docAckRecMapper.field("lumpsumAmountFlag", "documentDetails.lumpSumAmountFlag");
		docAckRecMapper.field("hospitalCashFlag", "documentDetails.addOnBenefitsHospitalCashFlag");
		docAckRecMapper.field("patientCareFlag", "documentDetails.addOnBenefitsPatientCareFlag");
		docAckRecMapper.field("otherBenefitsFlag", "documentDetails.otherBenefitsFlag");
		docAckRecMapper.field("emergencyMedicalEvaluation", "documentDetails.emergencyMedicalEvaluationFlag");
		docAckRecMapper.field("compassionateTravel", "documentDetails.compassionateTravelFlag");
		docAckRecMapper.field("repatriationOfMortalRemain", "documentDetails.repatriationOfMortalRemainsFlag");
		docAckRecMapper.field("preferredNetworkHospita", "documentDetails.preferredNetworkHospitalFlag");
		docAckRecMapper.field("sharedAccomodation", "documentDetails.sharedAccomodationFlag");		
		docAckRecMapper.field("additionalRemarks", "documentDetails.additionalRemarks");
		docAckRecMapper.field("status.processValue", "statusValue");
		docAckRecMapper.field("status.key", "statusKey");
		docAckRecMapper.field("stage.key", "stageKey");

		
		
		docAckRecMapper.field("benifitFlag", "documentDetails.benifitFlag");
		docAckRecMapper.field("prodHospBenefitFlag", "documentDetails.hospitalCashFlag");
		
		
		
		
		rodDocChkListMapper.field("documentTypeId", "particulars.id");
		rodDocChkListMapper.field("docAcknowledgement.key", "docAckTableKey");
		rodDocChkListMapper.field("noOfDocuments", "noOfDocuments");
		rodDocChkListMapper.field("receivedStatusId.key", "receivedStatus.id");
		rodDocChkListMapper.field("receivedStatusId.value", "receivedStatus.value");
		//rodDocChkListMapper.field("noOfDocuments", "noOfDocuments");
		rodDocChkListMapper.field("remarks", "remarks");
		
		
		/*cummlativeMapper.field("documentTypeId", "docTypeId");
		cummlativeMapper.field("key", "docChkLstKey");
		//rodDocChkListMapper.field("docAcknowledgement.key", "docAckTableKey");
		cummlativeMapper.field("noOfDocuments", "noOfDocuments");
		cummlativeMapper.field("receivedStatusId.key", "receivedStatus.id");
		cummlativeMapper.field("receivedStatusId.value", "receivedStatus.value");
		cummlativeMapper.field("noOfDocuments", "noOfDocuments");
		cummlativeMapper.field("remarks", "remarks");*/
		
		
		reconsiderRODRequestMapper.field("rodKey", "rodKey");
		reconsiderRODRequestMapper.field("acknowledgeNumber", "acknowledgementNo");
		reconsiderRODRequestMapper.field("documentReceivedFromId.value", "documentReceivedFrom");
		reconsiderRODRequestMapper.field("documentReceivedDate", "documentReceivedDate");
		reconsiderRODRequestMapper.field("modeOfReceiptId.value", "modeOfReceipt");
		reconsiderRODRequestMapper.field("hospitalisationFlag", "hospitalizationFlag");
		reconsiderRODRequestMapper.field("preHospitalisationFlag", "preHospitalizationFlag");
		reconsiderRODRequestMapper.field("postHospitalisationFlag", "postHospitalizationFlag");
		reconsiderRODRequestMapper.field("partialHospitalisationFlag", "partialHospitalizationFlag");
		reconsiderRODRequestMapper.field("lumpsumAmountFlag", "lumpSumAmountFlag");
		reconsiderRODRequestMapper.field("hospitalCashFlag", "addOnBenefitsHospitalCashFlag");
		reconsiderRODRequestMapper.field("patientCareFlag", "addOnBenefitsPatientCareFlag");
		reconsiderRODRequestMapper.field("hospitalizationRepeatFlag" , "hospitalizationRepeatFlag");
		reconsiderRODRequestMapper.field("otherBenefitsFlag", "otherBenefitFlag");
		reconsiderRODRequestMapper.field("emergencyMedicalEvaluation" , "emergencyMedicalEvaluationFlag");
		reconsiderRODRequestMapper.field("compassionateTravel" , "compassionateTravelFlag");
		reconsiderRODRequestMapper.field("repatriationOfMortalRemain" , "repatriationOfMortalRemainsFlag");
		reconsiderRODRequestMapper.field("preferredNetworkHospita" , "preferredNetworkHospitalFlag");
		reconsiderRODRequestMapper.field("sharedAccomodation" , "sharedAccomodationFlag");		
		reconsiderRODRequestMapper.field("hospitalizationClaimedAmount","hospitalizationClaimedAmt");
		reconsiderRODRequestMapper.field("preHospitalizationClaimedAmount","preHospClaimedAmt");
		reconsiderRODRequestMapper.field("postHospitalizationClaimedAmount","postHospClaimedAmt");
		reconsiderRODRequestMapper.field("otherBenefitsClaimedAmount" , "otherBenefitClaimedAmnt");
		reconsiderRODRequestMapper.field("claim.intimation.intimationId","intimationNo");
		
		
		createRodMapper.field("key","key");
		createRodMapper.field("rodNumber", "rodNo");
		createRodMapper.field("status.processValue","rodStatus");
		createRodMapper.field("status.key","statusId");
		createRodMapper.field("approvedAmount","approvedAmt");
		createRodMapper.field("billingApprovedAmount","billingApprovedAmount");
		createRodMapper.field("financialApprovedAmount","financialApprovedAmount");
		createRodMapper.field("docAcknowLedgement.key","docAcknowledgementKey");		
		
		createRodMapper.field("benApprovedAmt","benefitsApprovedAmt");
		createRodMapper.field("addOnCoversApprovedAmount","addOnApprovedAmt");
		createRodMapper.field("optionalApprovedAmount","optionalApprovedAmt");
		createRodMapper.field("processClaimType","processClmType");
		createRodMapper.field("reconsiderationRequest", "reconsiderationFlag");

		createRodMapper.field("stage.key","stageId");
		
		
		
		createRodMapper.register();
		reconsiderRODRequestMapper.register();
		docCheckListMapper.register();
		docAckRecMapper.register();
		rodDocChkListMapper.register();
		//cummlativeMapper.register();
		
		dtoMapper = mapperFactory.getMapperFacade();
		tableMapper = mapperFactory.getMapperFacade();
		reconsiderRODReqMapper = mapperFactory.getMapperFacade();
		rodMapper =  mapperFactory.getMapperFacade();
		docChkListMapper = mapperFactory.getMapperFacade();
		docAcknowledgeRecMapper = mapperFactory.getMapperFacade(DocAcknowledgement.class, ReceiptOfDocumentsDTO.class);
		rodDocCheckListMapper  = mapperFactory.getMapperFacade(RODDocumentCheckList.class, DocumentCheckListDTO.class);
		reconsiderDocAckList = mapperFactory.getMapperFacade(ReconsiderRODRequestTableDTO.class, DocAcknowledgement.class);
		reconsiderReimbList = mapperFactory.getMapperFacade(ReconsiderRODRequestTableDTO.class, Reimbursement.class);
		
	}
	
	@SuppressWarnings("unused")
	public static  List<DocumentCheckListDTO> getMasDocumentCheckList(List<DocumentCheckListMaster> masterDocumentCheckList)
	{
		//List<DocumentCheckListDTO> list = new ArrayList<DocumentCheckListDTO>();
		//List<DocumentCheckListDTO> tableDTO = new ArrayList<DocumentCheckListDTO>();
		List<DocumentCheckListDTO> mapAsList = tableMapper.mapAsList(masterDocumentCheckList, DocumentCheckListDTO.class);
		return mapAsList;
	}
	

	
	public   DocAcknowledgement getDocAckRecDetails(ReceiptOfDocumentsDTO documentDetailsDTO) {
		DocAcknowledgement docAckRec = docAcknowledgeRecMapper.mapReverse(documentDetailsDTO);
		return docAckRec;
	}
	
	
	public  RODDocumentCheckList getRODDocumentCheckList (DocumentCheckListDTO docCheckListDTO)
	{
		RODDocumentCheckList documentChkList = rodDocCheckListMapper.mapReverse(docCheckListDTO);
		return documentChkList;
	}
	
	public static List<DocumentCheckListDTO> getRODDocumentCheckList (List<RODDocumentCheckList> docCheckList)
	{
		List<DocumentCheckListDTO> documentChkList = docChkListMapper.mapAsList(docCheckList, DocumentCheckListDTO.class);
		return documentChkList;
	}
	
	
	public static List<ReconsiderRODRequestTableDTO> getDocAcknowledgeList(List<DocAcknowledgement> docAckValues)
	{
		List<ReconsiderRODRequestTableDTO> mapAsList = reconsiderRODReqMapper.mapAsList(docAckValues, ReconsiderRODRequestTableDTO.class);
		return mapAsList;
	}
	
	
	public static List<ReconsiderRODRequestTableDTO> getReimbursementDetails(List<Reimbursement> reimbursementDetails)
	{
		List<ReconsiderRODRequestTableDTO> mapAsList = rodMapper.mapAsList(reimbursementDetails, ReconsiderRODRequestTableDTO.class);
		return mapAsList;
	}
	
	public static  ReconsiderRODRequestTableDTO getDocAckRecDetails(DocAcknowledgement reconsiderList) {
		ReconsiderRODRequestTableDTO docAckRec = reconsiderDocAckList.mapReverse(reconsiderList);
		return docAckRec;
	}
	
	public static ReconsiderRODRequestTableDTO getReimbursementDetails(Reimbursement reimbursementDetails)
	{
		ReconsiderRODRequestTableDTO mapAsList = reconsiderReimbList.mapReverse(reimbursementDetails);
		return mapAsList;
	}
	
	public static List<ReceiptOfDocumentsDTO> getReceiptOfDocumentsDTOs( List<DocAcknowledgement> docAcknowledgementList ) {
		List<ReceiptOfDocumentsDTO> mapAsList = dtoMapper.mapAsList(docAcknowledgementList, ReceiptOfDocumentsDTO.class);
		return mapAsList;
	}
	
	/*@SuppressWarnings("unused")
	public static  List<DocAcknowledgement> getDocumentDetails(List<DocumentDetailsDTO> documentDetailsDTO)
	{
		//List<DocAcknowledgement> list = new ArrayList<DocAcknowledgement>();
		//List<DocAcknowledgement> tableDTO = new ArrayList<DocAcknowledgement>();
		List<DocumentCheckListDTO> mapAsList = tableMapper.mapAsList(documentDetailsDTO, DocAcknowledgement.class);
		return mapAsList;
	}*/
	
	public static AcknowledgeDocumentReceivedMapper getInstance(){
        if(myObj == null){
            myObj = new AcknowledgeDocumentReceivedMapper();
            getAllMapValues();
        }
        return myObj;
	 }

}

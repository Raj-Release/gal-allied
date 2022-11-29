/**
 * 
 */
package com.shaic.claim.claimhistory.view.ompView;

import java.util.List;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.impl.generator.EclipseJdtCompilerStrategy;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.ompviewroddetails.OMPViewClaimStatusDto;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.shaic.domain.DocumentCheckListMaster;
import com.shaic.domain.OMPDocAcknowledgement;
import com.shaic.domain.OMPIntimation;
import com.shaic.domain.OMPRODDocumentCheckList;
import com.shaic.domain.OMPReimbursement;

/**
 * @author ntv.vijayar
 *
 */
public class OMPAckDocReceivedMapper {
	
	
	
	private static MapperFacade tableMapper;
	private static MapperFacade reconsiderRODReqMapper;
	private static MapperFacade rodMapper;
	private static MapperFacade docChkListMapper;
	private static BoundMapperFacade<OMPDocAcknowledgement, ReceiptOfDocumentsDTO> docAcknowledgeRecMapper;
	private static BoundMapperFacade<OMPRODDocumentCheckList, DocumentCheckListDTO> rodDocCheckListMapper;
	private static BoundMapperFacade<ReconsiderRODRequestTableDTO, OMPDocAcknowledgement> reconsiderDocAckList;
	private static BoundMapperFacade<ReconsiderRODRequestTableDTO, OMPReimbursement> reconsiderReimbList;
	private static BoundMapperFacade<OMPIntimation, OMPViewClaimStatusDto> ompViewintimationList;
	
	
	//Added for create ROD Screen
	
	static OMPAckDocReceivedMapper myObj;
	
	 
	 public static void getAllMapValues() {
		 
		//Added for Acknowledge doc received screen
		//MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		MapperFactory mapperFactory = new DefaultMapperFactory.Builder().compilerStrategy(new EclipseJdtCompilerStrategy()).build();
		ClassMapBuilder<DocumentCheckListMaster, DocumentCheckListDTO> docCheckListMapper = mapperFactory.classMap(DocumentCheckListMaster.class,DocumentCheckListDTO.class);		
		ClassMapBuilder<OMPDocAcknowledgement, ReceiptOfDocumentsDTO> docAckRecMapper = mapperFactory.classMap(OMPDocAcknowledgement.class,ReceiptOfDocumentsDTO.class);
		ClassMapBuilder<OMPRODDocumentCheckList, DocumentCheckListDTO> rodDocChkListMapper = mapperFactory.classMap(OMPRODDocumentCheckList.class,DocumentCheckListDTO.class);
		ClassMapBuilder<OMPDocAcknowledgement, ReconsiderRODRequestTableDTO> reconsiderRODRequestMapper = mapperFactory.classMap(OMPDocAcknowledgement.class,ReconsiderRODRequestTableDTO.class);
		ClassMapBuilder<OMPReimbursement, ReconsiderRODRequestTableDTO> createRodMapper = mapperFactory.classMap(OMPReimbursement.class,ReconsiderRODRequestTableDTO.class);
		ClassMapBuilder<OMPIntimation, OMPViewClaimStatusDto> viewIntimationMapper = mapperFactory.classMap(OMPIntimation.class,OMPViewClaimStatusDto.class);
	//	ClassMapBuilder<OMPRODDocumentCheckList, DocumentCheckListDTO> cummlativeMapper = mapperFactory.classMap(OMPRODDocumentCheckList.class,DocumentCheckListDTO.class);
		
		
	
		docCheckListMapper.field("key","key");
		docCheckListMapper.field("sequenceNumber","sequenceNumber");
		docCheckListMapper.field("value","value");
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
		docAckRecMapper.field("additionalRemarks", "documentDetails.additionalRemarks");
		docAckRecMapper.field("status.processValue", "statusValue");
		docAckRecMapper.field("status.key", "statusKey");
		docAckRecMapper.field("stage.key", "stageKey");

		
		rodDocChkListMapper.field("documentTypeId", "key");
		rodDocChkListMapper.field("docAcknowledgement.key", "docAckTableKey");
		rodDocChkListMapper.field("noOfDocuments", "noOfDocuments");
		rodDocChkListMapper.field("receivedStatusId.key", "receivedStatus.id");
		rodDocChkListMapper.field("receivedStatusId.value", "receivedStatus.value");
		//rodDocChkListMapper.field("noOfDocuments", "noOfDocuments");
		rodDocChkListMapper.field("remarks", "remarks");
		
		
		/*cummlativeMapper.field("documentTypeId", "docTypeId");
		cummlativeMapper.field("key", "docChkLstKey");
		//rodDocChkListMapper.field("OMPDocAcknowledgement.key", "docAckTableKey");
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
		reconsiderRODRequestMapper.field("hospitalizationClaimedAmount","hospitalizationClaimedAmt");
		reconsiderRODRequestMapper.field("preHospitalizationClaimedAmount","preHospClaimedAmt");
		reconsiderRODRequestMapper.field("postHospitalizationClaimedAmount","postHospClaimedAmt");
		reconsiderRODRequestMapper.field("claim.intimation.intimationId","intimationNo");
		
		
		createRodMapper.field("key","key");
		createRodMapper.field("rodNumber", "rodNo");
		createRodMapper.field("status.processValue","rodStatus");
		createRodMapper.field("status.key","statusId");
		createRodMapper.field("approvedAmount","approvedAmt");
		createRodMapper.field("billingApprovedAmount","billingApprovedAmount");
		createRodMapper.field("financialApprovedAmount","financialApprovedAmount");
		createRodMapper.field("docAcknowLedgement.key","docAcknowledgementKey");
		createRodMapper.field("stage.key","stageId");
		
		viewIntimationMapper.field("key", "key");
		viewIntimationMapper.field("intimationId", "intimationId");
//		viewIntimationMapper.field("key", "intimationDate");
		viewIntimationMapper.field("lossDateTime", "lossDate");
		viewIntimationMapper.field("tpaIntimationNumber", "tpaIntimationNo");
		viewIntimationMapper.field("insured.insuredName", "insuredName");
		viewIntimationMapper.field("ailmentLoss", "ailmentOrLoss");
		viewIntimationMapper.field("event.eventCode", "eventCode");
		viewIntimationMapper.field("placeVisit", "placeofVisit");
		viewIntimationMapper.field("intimationMode", "modeOfIntimation");
		viewIntimationMapper.field("intimatedBy", "intimatedBy");
		viewIntimationMapper.field("intimaterName", "intimatorName");
		viewIntimationMapper.field("callerMobileNumber", "callerContactNo");
		viewIntimationMapper.field("dollarInitProvisionAmt", "intialProvisionAmt");
		viewIntimationMapper.field("inrConversionRate", "inrConversionRate");
		viewIntimationMapper.field("inrTotalAmount", "inrValue");
		viewIntimationMapper.field("policy.policyNumber", "policyNo");
		viewIntimationMapper.field("policy.homeOfficeCode", "policyIssuingOffice");
		viewIntimationMapper.field("policy.product.value", "productName");
//		viewIntimationMapper.field("key", "state");
		viewIntimationMapper.field("cityName", "city");
		viewIntimationMapper.field("admissionDate", "admissionDate");
//		viewIntimationMapper.field("key", "hospitalCode");
		viewIntimationMapper.field("cityName", "hospitalCity");
//		viewIntimationMapper.field("key", "country");
		viewIntimationMapper.field("remarks", "remarks");
		viewIntimationMapper.field("policy.smCode", "smCode");
		viewIntimationMapper.field("policy.smName", "smName");
		viewIntimationMapper.field("policy.agentCode", "agentOrBrokerCode");
		viewIntimationMapper.field("policy.agentName", "agentOrBrokerName");
		viewIntimationMapper.field("ailmentLoss", "ailment");
		viewIntimationMapper.field("claimType.value", "claimTypeValue.value");
		viewIntimationMapper.field("hospitalizationFlag", "hospitalisationFlag");
		viewIntimationMapper.field("nonHospitalizationFlag", "nonHospitalisationFlag");
		
		
		
		
		createRodMapper.register();
		reconsiderRODRequestMapper.register();
		docCheckListMapper.register();
		docAckRecMapper.register();
		rodDocChkListMapper.register();
		viewIntimationMapper.register();
		//cummlativeMapper.register();
		
		tableMapper = mapperFactory.getMapperFacade();
		reconsiderRODReqMapper = mapperFactory.getMapperFacade();
		rodMapper =  mapperFactory.getMapperFacade();
		docChkListMapper = mapperFactory.getMapperFacade();
		docAcknowledgeRecMapper = mapperFactory.getMapperFacade(OMPDocAcknowledgement.class, ReceiptOfDocumentsDTO.class);
		rodDocCheckListMapper  = mapperFactory.getMapperFacade(OMPRODDocumentCheckList.class, DocumentCheckListDTO.class);
		reconsiderDocAckList = mapperFactory.getMapperFacade(ReconsiderRODRequestTableDTO.class, OMPDocAcknowledgement.class);
		reconsiderReimbList = mapperFactory.getMapperFacade(ReconsiderRODRequestTableDTO.class, OMPReimbursement.class);
		ompViewintimationList = mapperFactory.getMapperFacade(OMPIntimation.class, OMPViewClaimStatusDto.class);
		
	}
	
	@SuppressWarnings("unused")
	public static  List<DocumentCheckListDTO> getMasDocumentCheckList(List<DocumentCheckListMaster> masterDocumentCheckList)
	{
		//List<DocumentCheckListDTO> list = new ArrayList<DocumentCheckListDTO>();
		//List<DocumentCheckListDTO> tableDTO = new ArrayList<DocumentCheckListDTO>();
		List<DocumentCheckListDTO> mapAsList = tableMapper.mapAsList(masterDocumentCheckList, DocumentCheckListDTO.class);
		return mapAsList;
	}
	

	
	public   OMPDocAcknowledgement getDocAckRecDetails(ReceiptOfDocumentsDTO documentDetailsDTO) {
		OMPDocAcknowledgement docAckRec = docAcknowledgeRecMapper.mapReverse(documentDetailsDTO);
		return docAckRec;
	}
	
	
	public  OMPRODDocumentCheckList getRODDocumentCheckList (DocumentCheckListDTO docCheckListDTO)
	{
		OMPRODDocumentCheckList documentChkList = rodDocCheckListMapper.mapReverse(docCheckListDTO);
		return documentChkList;
	}
	
	public static List<DocumentCheckListDTO> getRODDocumentCheckList (List<OMPRODDocumentCheckList> docCheckList)
	{
		List<DocumentCheckListDTO> documentChkList = docChkListMapper.mapAsList(docCheckList, DocumentCheckListDTO.class);
		return documentChkList;
	}
	
	
	public static List<ReconsiderRODRequestTableDTO> getDocAcknowledgeList(List<OMPDocAcknowledgement> docAckValues)
	{
		List<ReconsiderRODRequestTableDTO> mapAsList = reconsiderRODReqMapper.mapAsList(docAckValues, ReconsiderRODRequestTableDTO.class);
		return mapAsList;
	}
	
	
	public static List<ReconsiderRODRequestTableDTO> getReimbursementDetails(List<OMPReimbursement> reimbursementDetails)
	{
		List<ReconsiderRODRequestTableDTO> mapAsList = rodMapper.mapAsList(reimbursementDetails, ReconsiderRODRequestTableDTO.class);
		return mapAsList;
	}
	
	public static  ReconsiderRODRequestTableDTO getDocAckRecDetails(OMPDocAcknowledgement reconsiderList) {
		ReconsiderRODRequestTableDTO docAckRec = reconsiderDocAckList.mapReverse(reconsiderList);
		return docAckRec;
	}
	
	public static ReconsiderRODRequestTableDTO getReimbursementDetails(OMPReimbursement reimbursementDetails)
	{
		ReconsiderRODRequestTableDTO mapAsList = reconsiderReimbList.mapReverse(reimbursementDetails);
		return mapAsList;
	}
	
	public static OMPViewClaimStatusDto getViewClaimStatusDto(OMPIntimation intimation){
//		viewIntimationMapper.register();
		OMPViewClaimStatusDto map =  ompViewintimationList.map(intimation);
		return map;
	}
	
	/*@SuppressWarnings("unused")
	public static  List<OMPDocAcknowledgement> getDocumentDetails(List<DocumentDetailsDTO> documentDetailsDTO)
	{
		//List<OMPDocAcknowledgement> list = new ArrayList<OMPDocAcknowledgement>();
		//List<OMPDocAcknowledgement> tableDTO = new ArrayList<OMPDocAcknowledgement>();
		List<DocumentCheckListDTO> mapAsList = tableMapper.mapAsList(documentDetailsDTO, OMPDocAcknowledgement.class);
		return mapAsList;
	}*/
	
	public static OMPAckDocReceivedMapper getInstance(){
        if(myObj == null){
            myObj = new OMPAckDocReceivedMapper();
           
            getAllMapValues();
        }
        return myObj;
	 }

}

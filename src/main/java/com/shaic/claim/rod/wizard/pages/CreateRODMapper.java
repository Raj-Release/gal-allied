/**
 * 
 */
package com.shaic.claim.rod.wizard.pages;

import java.util.List;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.DMSDocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.domain.ClaimPayment;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.DocumentDetails;
import com.shaic.domain.OMPDocumentDetails;
import com.shaic.domain.RODBillDetails;
import com.shaic.domain.RODDocumentCheckList;
import com.shaic.domain.RODDocumentSummary;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.outpatient.OPDocumentSummary;
import com.shaic.newcode.wizard.domain.MappingUtil;

/**
 * @author ntv.vijayar
 *
 */
public class CreateRODMapper {
	
	
	private static MapperFacade rodDocTableMapper;
	private static MapperFacade uploadedDocumentMapper;
	
	// Added for document details token view enhancement -- starts
	private static MapperFacade documentDetailsMapper;
	// Added for document details token view enhancement -- ends
	private static MapperFacade previousAccountDetailsMapper;
	
	static CreateRODMapper myObj;
	
	private static BoundMapperFacade<Reimbursement, ReceiptOfDocumentsDTO> rodMapper;
	private static BoundMapperFacade<RODDocumentCheckList, DocumentCheckListDTO> rodDocCheckListValidationMapper;
	private static BoundMapperFacade<RODDocumentSummary,UploadDocumentDTO> uploadDocumentsMapper;
	private static BoundMapperFacade<DocAcknowledgement,ReceiptOfDocumentsDTO> docAcknowledgementMapper ;
	private static BoundMapperFacade<RODBillDetails, BillEntryDetailsDTO>  billEntryDetailsMapper;
	//private static BoundMapperFacade<ClaimPayment, PreviousAccountDetailsDTO>  previousAccountDetailsMapper;
	//private static BoundMapperFacade<ReimbursementBenefits , UploadDocumentDTO> benefitsDetailsMapper;


	 public static void getAllMapValues()  {
		
		//Added for create rod screen.
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<RODDocumentCheckList, DocumentCheckListDTO> rodDocCheckListValMapper  = mapperFactory.classMap(RODDocumentCheckList.class,DocumentCheckListDTO.class);
		ClassMapBuilder<Reimbursement, ReceiptOfDocumentsDTO> createRodMapper = mapperFactory.classMap(Reimbursement.class,ReceiptOfDocumentsDTO.class);
		ClassMapBuilder<RODDocumentSummary, UploadDocumentDTO> uploadDocsMapper = mapperFactory.classMap(RODDocumentSummary.class,UploadDocumentDTO.class);
		ClassMapBuilder<RODDocumentSummary, UploadDocumentDTO> uploadedDocsMapper = mapperFactory.classMap(RODDocumentSummary.class,UploadDocumentDTO.class);
		ClassMapBuilder<DocAcknowledgement, ReceiptOfDocumentsDTO> docAckMapper = mapperFactory.classMap(DocAcknowledgement.class,ReceiptOfDocumentsDTO.class);
		ClassMapBuilder<RODBillDetails, BillEntryDetailsDTO> billEntryMapper = mapperFactory.classMap(RODBillDetails.class,BillEntryDetailsDTO.class);
		ClassMapBuilder<DocumentDetails, DMSDocumentDetailsDTO> documentDetailsViewMapper = mapperFactory.classMap(DocumentDetails.class, DMSDocumentDetailsDTO.class);
		ClassMapBuilder<ClaimPayment, PreviousAccountDetailsDTO> previousAccntDetailsMapper = mapperFactory.classMap(ClaimPayment.class, PreviousAccountDetailsDTO.class);
		ClassMapBuilder<OMPDocumentDetails, DMSDocumentDetailsDTO> ompDocumentDetailsViewMapper = mapperFactory.classMap(OMPDocumentDetails.class, DMSDocumentDetailsDTO.class);
		ClassMapBuilder<OPDocumentSummary, DMSDocumentDetailsDTO> opDocumentDetailsViewMapper = mapperFactory.classMap(OPDocumentSummary.class, DMSDocumentDetailsDTO.class);
	//	ClassMapBuilder<ReimbursementBenefits, UploadDocumentDTO> benefitDetailsMap = mapperFactory.classMap(ReimbursementBenefits.class,UploadDocumentDTO.class);
//		ClassMapBuilder<RODDocumentCheckList, DocumentCheckListDTO> updateRodDocChkListValMapper = mapperFactory.classMap(RODDocumentCheckList.class,DocumentCheckListDTO.class);

		
		
		//Added for create ROD screen.
		
		rodDocCheckListValMapper.field("key","key");
		rodDocCheckListValMapper.field("documentTypeId", "docTypeId");
		rodDocCheckListValMapper.field("docAcknowledgement.key","docAckTableKey");
		rodDocCheckListValMapper.field("receivedStatusId.key", "receivedStatus.id");
		rodDocCheckListValMapper.field("receivedStatusId.value", "receivedStatus.value");
		rodDocCheckListValMapper.field("receivedStatusId.value", "ackReceivedStatus");
		rodDocCheckListValMapper.field("noOfDocuments", "noOfDocuments");
		rodDocCheckListValMapper.field("remarks", "remarks");
		rodDocCheckListValMapper.field("rodReceivedStatus", "rodReceivedStatusFlag");
		rodDocCheckListValMapper.field("rodRemarks", "rodRemarks");
		
		createRodMapper.field("dateOfAdmission", "documentDetails.dateOfAdmission");
		createRodMapper.field("dateOfDischarge", "documentDetails.dateOfDischarge");
		createRodMapper.field("reasonForChange","documentDetails.changeInReasonDOA");
		createRodMapper.field("paymentModeId","documentDetails.paymentModeFlag");
		createRodMapper.field("payeeName", "documentDetails.payeeName.value");
		createRodMapper.field("payeeEmailId", "documentDetails.emailId");
		
		createRodMapper.field("reasonForChange", "documentDetails.reasonForChange");
		createRodMapper.field("doaChangeReason","documentDetails.changeInReasonDOA");
		createRodMapper.field("panNumber", "documentDetails.panNo");
		createRodMapper.field("legalHeirFirstName", "documentDetails.legalFirstName");
		createRodMapper.field("accountNumber", "documentDetails.accountNo");
		createRodMapper.field("bankId", "documentDetails.bankId");
		createRodMapper.field("payableAt", "documentDetails.payableAt");
		
		
		createRodMapper.field("legalHeirFirstName", "documentDetails.legalFirstName");
		createRodMapper.field("legalHeirMiddleName", "documentDetails.legalMiddleName");
		createRodMapper.field("legalHeirLastName", "documentDetails.legalLastName");
		
		createRodMapper.field("accountNumber", "documentDetails.accountNo");
		//bank account no.
		//ifsc code
		//city
		//branch
		createRodMapper.field("roomCategory.key","claimDTO.newIntimationDto.roomCategory.id");
		createRodMapper.field("status.processValue", "statusValue");
		createRodMapper.field("status.key", "statusKey");
		createRodMapper.field("stage.key", "stageKey");
		
		uploadDocsMapper.field("reimbursement.rodNumber","rodNo");
		uploadDocsMapper.field("fileType.key","fileType.id");
		uploadDocsMapper.field("fileType.value","fileType.value");
		uploadDocsMapper.field("fileName", "fileName");
		uploadDocsMapper.field("billNumber","billNo");
		uploadDocsMapper.field("billDate", "billDate");
		uploadDocsMapper.field("noOfItems", "noOfItems");
		uploadDocsMapper.field("billAmount", "billValue");
		uploadDocsMapper.field("key","docSummaryKey");
		uploadDocsMapper.field("documentToken","dmsDocToken");
		uploadDocsMapper.field("corporateRemarks", "corporateRemarks");
		uploadDocsMapper.field("zonalRemarks", "zonalRemarks");
		uploadDocsMapper.field("billingRemarks", "billingRemarks");
		uploadDocsMapper.field("billNetAmount", "netAmount");
		uploadDocsMapper.field("createdDate", "createdDate");
		uploadDocsMapper.field("createdBy", "createdBy");
		uploadDocsMapper.field("rodVersion", "rodVersion");
		uploadDocsMapper.field("reimbursement.key","rodKey");
		
		uploadedDocsMapper.field("corporateRemarks", "corporateRemarks");
		uploadedDocsMapper.field("zonalRemarks", "zonalRemarks");
		uploadedDocsMapper.field("billingRemarks", "billingRemarks");
		uploadedDocsMapper.field("reimbursement.rodNumber","rodNo");
		uploadedDocsMapper.field("fileType.key","fileType.id");
		uploadedDocsMapper.field("fileType.value","fileType.value");
		uploadedDocsMapper.field("fileName", "fileName");
		uploadedDocsMapper.field("billNumber","billNo");
		uploadedDocsMapper.field("billDate", "billDate");
		uploadedDocsMapper.field("noOfItems", "noOfItems");
		uploadedDocsMapper.field("billAmount", "billValue");
		uploadedDocsMapper.field("key","docSummaryKey");
		uploadedDocsMapper.field("rodVersion", "rodVersion");

		uploadedDocsMapper.field("documentToken","dmsDocToken");
		uploadedDocsMapper.field("corporateRemarks", "corporateRemarks");
		uploadedDocsMapper.field("zonalRemarks", "zonalRemarks");
		uploadedDocsMapper.field("billingRemarks", "billingRemarks");
		uploadedDocsMapper.field("reimbursement.key","rodKey");
		

		
		docAckMapper.field("documentReceivedFromId.key", "documentDetails.documentsReceivedFrom.id");
		docAckMapper.field("documentReceivedFromId.value", "documentDetails.documentsReceivedFrom.value");
		docAckMapper.field("acknowledgeNumber", "documentDetails.acknowledgementNumber");

		docAckMapper.field("insuredContactNumber", "documentDetails.acknowledgmentContactNumber");
		docAckMapper.field("insuredEmailId", "documentDetails.emailId");
		docAckMapper.field("documentReceivedDate", "documentDetails.documentsReceivedDate");
		
		docAckMapper.field("modeOfReceiptId.key", "documentDetails.modeOfReceipt.id");
		docAckMapper.field("modeOfReceiptId.value", "documentDetails.modeOfReceipt.value");
		
		docAckMapper.field("reconsiderationRequest", "documentDetails.reconsiderationRequestFlag");
		
		docAckMapper.field("rodKey","documentDetails.rodKey");
		docAckMapper.field("key","documentDetails.docAcknowledgementKey");
		docAckMapper.field("hospitalisationFlag", "documentDetails.hospitalizationFlag");
		docAckMapper.field("preHospitalisationFlag", "documentDetails.preHospitalizationFlag");
		docAckMapper.field("postHospitalisationFlag", "documentDetails.postHospitalizationFlag");
		docAckMapper.field("partialHospitalisationFlag", "documentDetails.partialHospitalizationFlag");
		docAckMapper.field("hospitalizationRepeatFlag", "documentDetails.hospitalizationRepeatFlag");
		docAckMapper.field("lumpsumAmountFlag", "documentDetails.lumpSumAmountFlag");
		docAckMapper.field("hospitalCashFlag", "documentDetails.addOnBenefitsHospitalCashFlag");
		docAckMapper.field("patientCareFlag", "documentDetails.addOnBenefitsPatientCareFlag");
		docAckMapper.field("otherBenefitsFlag", "documentDetails.otherBenefitsFlag");
		docAckMapper.field("emergencyMedicalEvaluation", "documentDetails.emergencyMedicalEvaluationFlag");
		docAckMapper.field("compassionateTravel", "documentDetails.compassionateTravelFlag");
		docAckMapper.field("repatriationOfMortalRemain", "documentDetails.repatriationOfMortalRemainsFlag");
		docAckMapper.field("preferredNetworkHospita", "documentDetails.preferredNetworkHospitalFlag");
		docAckMapper.field("sharedAccomodation", "documentDetails.sharedAccomodationFlag");
		
		docAckMapper.field("additionalRemarks", "documentDetails.additionalRemarks");
		docAckMapper.field("acknowledgeNumber", "documentDetails.acknowledgementNumber");
		docAckMapper.field("createdBy", "createdBy");
		docAckMapper.field("modifiedBy","modifiedBy");
		docAckMapper.field("documentTypeId", "documentDetails.documentTypeId");
		
		billEntryMapper.field("key","key");
		billEntryMapper.field("itemName","itemName");
		billEntryMapper.field("billClassification.key","classification.id");
		billEntryMapper.field("billClassification.value","classification.value");
		billEntryMapper.field("billCategory.key","category.id");
		billEntryMapper.field("billCategory.value","category.value");
		billEntryMapper.field("itemNumber","itemNo");
		billEntryMapper.field("noOfDaysBills","noOfDays");
		billEntryMapper.field("perDayAmountBills","perDayAmt");
//		billEntryMapper.field("totalAmount","itemValue");             //need to implements
		billEntryMapper.field("claimedAmountBills", "itemValue");
		
		/**
		 * Below fields are added for populating the data
		 * which are entered in process claim request zonal review screen
		 * */
		
		billEntryMapper.field("noOfDaysPolicy","noOfDaysAllowed");
		billEntryMapper.field("perDayAmountPolicy","perDayAmtProductBased");
		billEntryMapper.field("totalAmount","amountAllowableAmount");
		billEntryMapper.field("nonPayableAmountProduct","nonPayableProductBased");
		billEntryMapper.field("nonPayableAmount","nonPayable");
		billEntryMapper.field("deductibleAmount", "reasonableDeduction");
		billEntryMapper.field("payableAmount", "totalDisallowances");
		billEntryMapper.field("netAmount", "netPayableAmount");
		billEntryMapper.field("nonPayableReason", "deductibleOrNonPayableReason");
		billEntryMapper.field("medicalRemarks", "medicalRemarks");
		billEntryMapper.field("irdaLevel1Id", "irdaLevel1.id");
		billEntryMapper.field("irdaLevel2Id", "irdaLevel2.id");
		billEntryMapper.field("irdaLevel3Id", "irdaLevel3.id");

		
		
		
		//benefitDetailsMap.field("", "");
		
		/**
		 * The below line will be added , once the file  upload
		 * functionality is implemented.
		 * */
		//uploadDocsMapper.field("documents", "fileUpload");
		
		/*updateRodDocChkListValMapper.field("key","key");
		updateRodDocChkListValMapper.field("documentTypeId", "key");
		updateRodDocChkListValMapper.field("docAcknowledgement.key","docAckTableKey");
		updateRodDocChkListValMapper.field("noOfDocuments", "noOfDocuments");
		updateRodDocChkListValMapper.field("receivedStatusId.key", "receivedStatus.id");
		updateRodDocChkListValMapper.field("receivedStatusId.value", "receivedStatus.value");
		updateRodDocChkListValMapper.field("remarks", "remarks");
		updateRodDocChkListValMapper.field("rodReceivedStatus", "rodReceivedStatusFlag");
		updateRodDocChkListValMapper.field("rodRemarks", "rodRemarks");*/
		//rodDocChkListValMapper.field("remarks", "remarks");

		
		
		documentDetailsViewMapper.field("intimationNumber","intimationNo");
		documentDetailsViewMapper.field("claimNumber","claimNo");
		documentDetailsViewMapper.field("documentType","documentType");		
		documentDetailsViewMapper.field("sfFileName","fileName");
		documentDetailsViewMapper.field("documentSource","documentSource");
		documentDetailsViewMapper.field("documentToken","dmsDocToken");
		documentDetailsViewMapper.field("fileName","galaxyFileName");
		documentDetailsViewMapper.field("createdDate","documentCreatedDate");
		documentDetailsViewMapper.field("reimbursementNumber", "reimbursementNumber");
		documentDetailsViewMapper.field("cashlessNumber", "cashlessNumber");
		documentDetailsViewMapper.field("docVersion", "docVersion");
		
		
		previousAccntDetailsMapper.field("claimNumber","policyClaimNo");
		previousAccntDetailsMapper.field("paymentDate","receiptDate");
		previousAccntDetailsMapper.field("accountNumber","bankAccountNo");
		previousAccntDetailsMapper.field("ifscCode","ifsccode");
		previousAccntDetailsMapper.field("panNumber","panNo");
		previousAccntDetailsMapper.field("emailId","emailId");
		previousAccntDetailsMapper.field("documentReceivedFrom","docReceivedFrom");

		ompDocumentDetailsViewMapper.field("intimationNumber","intimationNo");
		ompDocumentDetailsViewMapper.field("claimNumber","claimNo");
		ompDocumentDetailsViewMapper.field("documentType","documentType");		
		ompDocumentDetailsViewMapper.field("sfFileName","fileName");
		ompDocumentDetailsViewMapper.field("documentSource","documentSource");
		ompDocumentDetailsViewMapper.field("documentToken","dmsDocToken");
		ompDocumentDetailsViewMapper.field("fileName","galaxyFileName");
		ompDocumentDetailsViewMapper.field("createdDate","documentCreatedDate");
		ompDocumentDetailsViewMapper.field("reimbursementNumber", "reimbursementNumber");
		ompDocumentDetailsViewMapper.field("cashlessNumber", "cashlessNumber");
		
//		opDocumentDetailsViewMapper.field("intimationNumber","intimationNo");
//		opDocumentDetailsViewMapper.field("claimNumber","claimNo");
//		opDocumentDetailsViewMapper.field("documentType","documentType");		
		opDocumentDetailsViewMapper.field("fileName","fileName");
//		opDocumentDetailsViewMapper.field("documentSource","documentSource");
		opDocumentDetailsViewMapper.field("dmsDocToken","dmsDocToken");
		opDocumentDetailsViewMapper.field("fileName","galaxyFileName");
		opDocumentDetailsViewMapper.field("createdDate","documentCreatedDate");
//		opDocumentDetailsViewMapper.field("reimbursementNumber", "reimbursementNumber");
//		opDocumentDetailsViewMapper.field("cashlessNumber", "cashlessNumber");
		
		
		createRodMapper.register();
		rodDocCheckListValMapper.register();
		uploadDocsMapper.register();
		docAckMapper.register();
		billEntryMapper.register();
		documentDetailsViewMapper.register();
		previousAccntDetailsMapper.register();
		ompDocumentDetailsViewMapper.register();
		opDocumentDetailsViewMapper.register();
		rodDocTableMapper = mapperFactory.getMapperFacade();
		uploadedDocumentMapper = mapperFactory.getMapperFacade();
		rodMapper = mapperFactory.getMapperFacade(Reimbursement.class, ReceiptOfDocumentsDTO.class);
		rodDocCheckListValidationMapper = mapperFactory.getMapperFacade(RODDocumentCheckList.class, DocumentCheckListDTO.class);
		uploadDocumentsMapper = mapperFactory.getMapperFacade(RODDocumentSummary.class, UploadDocumentDTO.class);
		docAcknowledgementMapper = mapperFactory.getMapperFacade(DocAcknowledgement.class, ReceiptOfDocumentsDTO.class);
		billEntryDetailsMapper = mapperFactory.getMapperFacade(RODBillDetails.class, BillEntryDetailsDTO.class);
		documentDetailsMapper = mapperFactory.getMapperFacade();
		previousAccountDetailsMapper = mapperFactory.getMapperFacade();

	}
	
	
	public  Reimbursement getReimbursementDetails(ReceiptOfDocumentsDTO documentDetailsDTO) {
		Reimbursement reimbursement = rodMapper.mapReverse(documentDetailsDTO);
		return reimbursement;
	}
	
	public static List<UploadDocumentDTO> getUploadDocumentDTO (List<RODDocumentSummary> docSummary)
	{
		List<UploadDocumentDTO> uploadDocs = uploadedDocumentMapper.mapAsList(docSummary, UploadDocumentDTO.class);
		return uploadDocs;
	}
	
	
	public static List<DocumentCheckListDTO> getRODDocumentCheckList(List<RODDocumentCheckList> rodDocCheckListValues)
	{
		List<DocumentCheckListDTO> mapAsList = rodDocTableMapper.mapAsList(rodDocCheckListValues, DocumentCheckListDTO.class);
		return mapAsList;
	}
	
	
	public static RODDocumentSummary getDocumentSummary (UploadDocumentDTO uploadDocsDTO)
	{
		RODDocumentSummary rodDocSummary = uploadDocumentsMapper.mapReverse(uploadDocsDTO);
		return rodDocSummary;
	}
	
	

	public  RODDocumentCheckList getRODCheckListForUpdation (DocumentCheckListDTO docCheckListDTO)
	{
		RODDocumentCheckList documentChkList = rodDocCheckListValidationMapper.mapReverse(docCheckListDTO);
		//RODDocumentCheckList documentChkList = rodMapper.mapReverse(docCheckListDTO);
		return documentChkList;
	}
	
	public DocAcknowledgement getAcknowledgeDocumentList(ReceiptOfDocumentsDTO rodDTO)
	{
		DocAcknowledgement docAcknowledgement = docAcknowledgementMapper.mapReverse(rodDTO);
		return docAcknowledgement;
	}
	
	public static RODBillDetails getRODBillDetails (BillEntryDetailsDTO billEntryDetailsDTO)
	{
		RODBillDetails rodBillDetails = billEntryDetailsMapper.mapReverse(billEntryDetailsDTO);
		return rodBillDetails;
	}

	public static List<DMSDocumentDetailsDTO> getDMSDocumentDetailsDTO(List<DocumentDetails> documentDetailsList)
	{
		List<DMSDocumentDetailsDTO> dmsDocumentDetailsDTOList = documentDetailsMapper.mapAsList(documentDetailsList, DMSDocumentDetailsDTO.class);
		return dmsDocumentDetailsDTOList;
	}
	
	
	public static List<DMSDocumentDetailsDTO> getDMSDocumentDetailsDTOOMP(List<OMPDocumentDetails> documentDetailsList)
	{
		List<DMSDocumentDetailsDTO> dmsDocumentDetailsDTOList = documentDetailsMapper.mapAsList(documentDetailsList, DMSDocumentDetailsDTO.class);
		return dmsDocumentDetailsDTOList;
	}
	
	public static List<DMSDocumentDetailsDTO> getOPDMSDocumentDetails(List<OPDocumentSummary> documentDetailsList)
	{
		List<DMSDocumentDetailsDTO> dmsDocumentDetailsDTOList = documentDetailsMapper.mapAsList(documentDetailsList, DMSDocumentDetailsDTO.class);
		return dmsDocumentDetailsDTOList;
	}
	
	
	public static List<PreviousAccountDetailsDTO> getPreviousAccountDetails(List<ClaimPayment> claimPaymentList)
	{
		List<PreviousAccountDetailsDTO> previousAccntDetailsList = previousAccountDetailsMapper.mapAsList(claimPaymentList, PreviousAccountDetailsDTO.class);
		return previousAccntDetailsList;
	}
	
	public static CreateRODMapper getInstance(){
        if(myObj == null){
            myObj = new CreateRODMapper();
            getAllMapValues();
        }
        return myObj;
	 }

}

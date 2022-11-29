package com.shaic.claim.outpatient.registerclaim.mapper;

import java.util.List;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.outpatient.registerclaim.dto.InsuredDetailsDTO;
import com.shaic.claim.outpatient.registerclaim.dto.OPBillDetailsDTO;
import com.shaic.claim.outpatient.registerclaim.dto.OutPatientDTO;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.domain.outpatient.OPDocumentBillEntry;
import com.shaic.domain.outpatient.OPDocumentList;
import com.shaic.domain.outpatient.OPDocumentSummary;
import com.shaic.domain.outpatient.OPHCDetails;
import com.shaic.domain.outpatient.OPHealthCheckup;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class OutpatientMapper {

	static MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
	
	private static BoundMapperFacade<OPHealthCheckup, OutPatientDTO> documentDetailsMapper;
	private static BoundMapperFacade<OPDocumentList, DocumentCheckListDTO> documentCheckListMapper;
	private static BoundMapperFacade<OPHCDetails, InsuredDetailsDTO> insuredDetailsMapper;
	private static BoundMapperFacade<OPDocumentSummary,UploadDocumentDTO> uploadDocumentsMapper;
	private static BoundMapperFacade<OPDocumentBillEntry, OPBillDetailsDTO> billDetatilsMapper;
//	private static BoundMapperFacade<OPPayment,OPPaymentDTO> opPaymentMapper;
	
	private static MapperFacade docCheckListMapper;
	private static MapperFacade insDetailsMapper;
	private static MapperFacade uploadedDocumentMapper;
	
	
	private static ClassMapBuilder<OPHealthCheckup, OutPatientDTO> documentDetailsMap = mapperFactory.classMap(OPHealthCheckup.class,OutPatientDTO.class);
	private static ClassMapBuilder<OPDocumentList, DocumentCheckListDTO> documentCheckListMap = mapperFactory.classMap(OPDocumentList.class,DocumentCheckListDTO.class);
	private static ClassMapBuilder<OPHCDetails, InsuredDetailsDTO> insuredDetailsMap = mapperFactory.classMap(OPHCDetails.class,InsuredDetailsDTO.class);
	private static ClassMapBuilder<OPDocumentSummary, UploadDocumentDTO> uploadedDocsMapper = mapperFactory.classMap(OPDocumentSummary.class,UploadDocumentDTO.class);
	private static ClassMapBuilder<OPDocumentBillEntry, OPBillDetailsDTO> billDetailsMap = mapperFactory.classMap(OPDocumentBillEntry.class,OPBillDetailsDTO.class);
//	private static ClassMapBuilder<OPPayment,OPPaymentDTO> opPaymentMap = mapperFactory.classMap(OPPayment.class, OPPaymentDTO.class);
	
	{
//		documentDetailsMap.field("key","key");
//		documentDetailsMap.field("status.key","statusKey");
//		documentDetailsMap.field("stage.key","stageKey");
//		documentDetailsMap.field("documentReceivedFromId.key","documentDetails.documentReceivedFrom.id");
//		documentDetailsMap.field("documentReceivedFromId.value","documentDetails.documentReceivedFrom.value");
//		documentDetailsMap.field("documentReceivedDate","documentDetails.documentReceivedDate");
//		documentDetailsMap.field("modeOfReceipt.key","documentDetails.modeOfReceipt.id");
//		documentDetailsMap.field("modeOfReceipt.value","documentDetails.modeOfReceipt.value");
//		documentDetailsMap.field("personContactNumber","documentDetails.acknowledgementContactNumber");
//		documentDetailsMap.field("personEmailId","documentDetails.emailID");
//		documentDetailsMap.field("additionalRemarks","documentDetails.additionalRemarks");
//		documentDetailsMap.field("amountEligible","documentDetails.amountEligible");
//		documentDetailsMap.field("availableSI","documentDetails.balanceSI");
//		documentDetailsMap.field("amountPayable","documentDetails.amountPayable");
//		documentDetailsMap.field("rejectionRemarks","documentDetails.rejectionRemarks");
//		documentDetailsMap.field("approvalRemarks","documentDetails.approvalRemarks");
//		documentDetailsMap.field("payeeEmailId","documentDetails.payeeEmailId");
//		documentDetailsMap.field("panNumber","documentDetails.panNo");
//		documentDetailsMap.field("payableAt","documentDetails.payableAt");
//		documentDetailsMap.field("createdDate", "createDate");
//		documentDetailsMap.field("createdBy","createdBy");
//		documentDetailsMap.field("accountNumber","documentDetails.accountNo");
//		documentDetailsMap.field("payeeName","documentDetails.payeeName.value");
		
//		private SelectValue claimType;	
//		private Insured insuredPatientName;	
//		private Date OPCheckupDate;
//		private Date billReceivedDate;	
//		private String amountClaimed;	
//		private String provisionAmt;	
//		private SelectValue reasonForOPVisit;	
//		private String remarksForOpVisit;
		
		documentDetailsMap.field("claim.claimType.key", "documentDetails.claimType.id");
		documentDetailsMap.field("claim.claimType.value", "documentDetails.claimType.value");
		documentDetailsMap.field("claim.intimation.insured", "documentDetails.insuredPatientName");
		documentDetailsMap.field("claim.intimation.modeOfReceiptId.key", "documentDetails.modeOfReceipt.id");
		documentDetailsMap.field("claim.intimation.modeOfReceiptId.value", "documentDetails.modeOfReceipt.value");
		documentDetailsMap.field("claim.intimation.treatmentTypeId.key", "documentDetails.treatmentType.id");
		documentDetailsMap.field("claim.intimation.treatmentTypeId.value", "documentDetails.treatmentType.value");
		documentDetailsMap.field("opHealthCheckupDate", "documentDetails.OPCheckupDate");
		documentDetailsMap.field("billReceivedDate", "documentDetails.billReceivedDate");
		documentDetailsMap.field("claim.claimedAmount", "documentDetails.amountClaimed");
		documentDetailsMap.field("claim.provisionAmount", "documentDetails.provisionAmt");
		documentDetailsMap.field("opReason.key", "documentDetails.reasonForOPVisit.id");
		documentDetailsMap.field("opReason.value", "documentDetails.reasonForOPVisit.value");
		documentDetailsMap.field("opHealthRemarks", "documentDetails.remarksForOpVisit");
		
		
		documentCheckListMap.field("key","key");
		documentCheckListMap.field("documentTypeId", "docTypeId");
		documentCheckListMap.field("receivedStatusId.key", "receivedStatus.id");
		documentCheckListMap.field("receivedStatusId.value", "receivedStatus.value");
		documentCheckListMap.field("numberOfDocuments", "noOfDocuments");
		documentCheckListMap.field("remarks", "remarks");
		
		insuredDetailsMap.field("key","key");
		insuredDetailsMap.field("treatmentDate", "checkupDate");
		insuredDetailsMap.field("reasonForVisit", "reasonForCheckup");
		
		
		uploadedDocsMapper.field("fileType.key","fileType.id");
		uploadedDocsMapper.field("fileType.value","fileType.value");
		uploadedDocsMapper.field("billNumber","billNo");
		uploadedDocsMapper.field("billDate", "billDate");
		uploadedDocsMapper.field("noOfItems", "noOfItems");
		uploadedDocsMapper.field("billAmount", "billValue");
		
		billDetailsMap.field("key","key");
		billDetailsMap.field("billTypeId","masterId");
		billDetailsMap.field("billDate","billDate");
		billDetailsMap.field("billNumber", "billNumber");
		billDetailsMap.field("claimedAmount", "claimedAmount");
		billDetailsMap.field("nonPayableAmount", "nonPayableAmt");
//		billDetailsMap.field("billDate", "payableAmt");
		billDetailsMap.field("nonPayableReason", "nonPayableReason");
		
		
//		opPaymentMap.field( "key","");
	//	opPaymentMap.field( "lotNo","");
	//	opPaymentMap.field( "rodNo","");
//		opPaymentMap.field( "intimationNo","newIntimationDTO.intimationId");
//		opPaymentMap.field( "claimNo","newIntimationDTO.claimNumber");
//		opPaymentMap.field( "riskId","newIntimationDTO.insuredPatient.insuredId");
//		opPaymentMap.field( "policyNo","newIntimationDTO.policy.policyNumber");
//		opPaymentMap.field( "claimType","claimDTO.claimType.value");
	//	opPaymentMap.field( "legalHeirName","");
	//	opPaymentMap.field( "paymentCpuCode","");
	//	opPaymentMap.field( "cpuZone","");
	//	opPaymentMap.field( "paymentType","");
	// 	opPaymentMap.field( "approvedAmount","");
//		opPaymentMap.field( "cpuCode","newIntimationDTO.cpuCode");
	//	opPaymentMap.field( "ifscCode","");
	//	opPaymentMap.field( "accountNumber","");
	//	opPaymentMap.field( "branchName","");
	//	opPaymentMap.field( "payeeName","");
	//	opPaymentMap.field( "payableAt","");
	//	opPaymentMap.field( "panNumber","");
//		opPaymentMap.field( "hospitalCode","newIntimationDTO.hospitalDto.hospitalCode");
	//	opPaymentMap.field( "emailId","");
	//	opPaymentMap.field( "bankCode","");
	//	opPaymentMap.field( "chequeDDDate","");
	//	opPaymentMap.field( "chequeDDNumber","");
	//	opPaymentMap.field( "bankName","");
	//	opPaymentMap.field("netAmount","");
	//	opPaymentMap.field( "faApprovalDate","");
	//	opPaymentMap.field( "reasonForChange","");
	//	opPaymentMap.field( "stageId","");
	//	opPaymentMap.field( "statusId","");
	//	opPaymentMap.field( "createdDate","");
	//	opPaymentMap.field( "createdBy","");
	//	opPaymentMap.field( "modifiedBy","");
	//	opPaymentMap.field( "modifiedDate","");
		
		
		
		
		documentCheckListMap.register();
		documentDetailsMap.register();
		insuredDetailsMap.register();
		uploadedDocsMapper.register();
		billDetailsMap.register();
//		opPaymentMap.register();
		documentDetailsMapper = mapperFactory.getMapperFacade(OPHealthCheckup.class, OutPatientDTO.class);
		documentCheckListMapper = mapperFactory.getMapperFacade(OPDocumentList.class, DocumentCheckListDTO.class);
		insuredDetailsMapper = mapperFactory.getMapperFacade(OPHCDetails.class, InsuredDetailsDTO.class);
		billDetatilsMapper = mapperFactory.getMapperFacade(OPDocumentBillEntry.class, OPBillDetailsDTO.class);
		uploadDocumentsMapper = mapperFactory.getMapperFacade(OPDocumentSummary.class, UploadDocumentDTO.class);
//		opPaymentMapper = mapperFactory.getMapperFacade(OPPayment.class,OPPaymentDTO.class);
		docCheckListMapper = mapperFactory.getMapperFacade();
		insDetailsMapper = mapperFactory.getMapperFacade();
		uploadedDocumentMapper = mapperFactory.getMapperFacade();
		
	}
	
//	public OPPayment getOPPaymentList(OPPaymentDTO opPayment)
//	{
//		OPPayment opPaymentList = opPaymentMapper.mapReverse(opPayment);
//		return opPaymentList;
//	}
	
	
	public OPDocumentList getOPDocumentList(DocumentCheckListDTO docCheckListDTO)
	{
		OPDocumentList documentChkList = documentCheckListMapper.mapReverse(docCheckListDTO);
		return documentChkList;
	}
	
	public OPBillDetailsDTO getOPBillEntryDTO(OPDocumentBillEntry billEntry)
	{
		OPBillDetailsDTO billEntryDTO = billDetatilsMapper.map(billEntry);
		return billEntryDTO;
	}
	
	public OPDocumentBillEntry getOPBillEntry(OPBillDetailsDTO billEntryDTO)
	{
		OPDocumentBillEntry documentChkList = billDetatilsMapper.mapReverse(billEntryDTO);
		return documentChkList;
	}
	
	public DocumentCheckListDTO getDocumentCheckListDTO(OPDocumentList documentChkList)
	{
		DocumentCheckListDTO checkListDTO = documentCheckListMapper.map(documentChkList);
		return checkListDTO;
	}
	
	public OPHealthCheckup getOPHealthCheckup(OutPatientDTO outPatientDTO)
	{
		OPHealthCheckup opHealthCheckup = documentDetailsMapper.mapReverse(outPatientDTO);
		return opHealthCheckup;
	}
	
	public OutPatientDTO getOutpatientDTO(OPHealthCheckup opHealthCheckup)
	{
		OutPatientDTO outpatientDTO = documentDetailsMapper.map(opHealthCheckup);
		return outpatientDTO;
	}
	
	public List<OPDocumentList> getOPDocumentCheckList(List<DocumentCheckListDTO> documentCheckList) {
		List<OPDocumentList> mapAsList = docCheckListMapper.mapAsList(documentCheckList, OPDocumentList.class);
		return mapAsList;
	}
	
	public List<DocumentCheckListDTO> getDocumentCheckList(List<OPDocumentList> opDocChkList) {
		List<DocumentCheckListDTO> mapAsList = docCheckListMapper.mapAsList(opDocChkList, DocumentCheckListDTO.class);
		return mapAsList;
	}
	
	
	public List<OPHCDetails> getOPHCDetailsList(List<InsuredDetailsDTO> insuredDetailsList) {
		List<OPHCDetails> mapAsList = insDetailsMapper.mapAsList(insuredDetailsList, OPHCDetails.class);
		return mapAsList;
	}
	
	public List<InsuredDetailsDTO> getOPInsuredDetailsList(List<OPHCDetails> opDocChkList) {
		List<InsuredDetailsDTO> mapAsList = insDetailsMapper.mapAsList(opDocChkList, InsuredDetailsDTO.class);
		return mapAsList;
	}
	
	public static List<UploadDocumentDTO> getUploadDocumentDTO (List<OPDocumentSummary> docSummary)
	{
		List<UploadDocumentDTO> uploadDocs = uploadedDocumentMapper.mapAsList(docSummary, UploadDocumentDTO.class);
		return uploadDocs;
	}
	
	public static OPDocumentSummary getDocumentSummary(UploadDocumentDTO uploadDocsDTO)
	{
		OPDocumentSummary rodDocSummary = uploadDocumentsMapper.mapReverse(uploadDocsDTO);
		return rodDocSummary;
	}
	
	
	public OPHCDetails getOPHCDetails(InsuredDetailsDTO insuredDetailsList) {
		OPHCDetails mapAsList = insuredDetailsMapper.mapReverse(insuredDetailsList);
		return mapAsList;
	}
	
	public InsuredDetailsDTO getOPInsuredDetails(OPHCDetails opDocChkList) {
		InsuredDetailsDTO mapAsList = insuredDetailsMapper.map(opDocChkList);
		return mapAsList;
	}
	
}

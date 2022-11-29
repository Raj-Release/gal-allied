package com.shaic.claim.OMPDocumentRelatedService;



public class OMPEarlierRodMapper {
//	
//	static MapperFactory mapperFactory = new DefaultMapperFactory.Builder().compilerStrategy(new EclipseJdtCompilerStrategy()).build();
//	
//	static OMPEarlierRodMapper myObj;
//	
//	private static MapperFacade documentDetailsMapper;
//	private static MapperFacade queryDetailsMapper;
//	private static MapperFacade queryMapper;
//	private static MapperFacade portablityMapper;
//	private static BoundMapperFacade<ViewTmpIntimation, ViewClaimStatusDTO> intimationDetailsMapper;
//	private static BoundMapperFacade<OMPIntimation, ViewClaimStatusDTO> viewIntimationDetailsMapper;
//	private static BoundMapperFacade<Hospitals, ViewClaimStatusDTO> hospitalDetailMapper;
//	private static BoundMapperFacade<ViewTmpClaim,ClaimStatusRegistrationDTO> registrationDetailsMapper;
//	
////	private static BoundMapperFacade<ReimbursementQuery, ViewQueryDTO> viewQueryMapper;
//	private static BoundMapperFacade<OMPDocAcknowledgement, DocumentDetailsDTO> documentAcknowledgementMapper;
//	
////	private static BoundMapperFacade<ReimbursementRejection, ViewRejectionDTO> rejectionMapper;
//	
//	private static BoundMapperFacade<OMPDocAcknowledgement, ViewDocumentDetailsDTO> documentMapper;
//	
//	
//	private static ClassMapBuilder<DocAcknowledgement, ViewDocumentDetailsDTO> documentDetailsMap = mapperFactory.classMap(DocAcknowledgement.class,ViewDocumentDetailsDTO.class);
//	private static ClassMapBuilder<DocAcknowledgement, DocumentDetailsDTO> acknowledgeDetailsMap = mapperFactory.classMap(DocAcknowledgement.class, DocumentDetailsDTO.class);
//	private static ClassMapBuilder<ReimbursementQuery, RODQueryDetailsDTO> acknowledgeQueryMap = mapperFactory.classMap(ReimbursementQuery.class, RODQueryDetailsDTO.class);
//	
//	private static ClassMapBuilder<Reimbursement, ViewDocumentDetailsDTO> reimbursementMap = mapperFactory.classMap(Reimbursement.class,ViewDocumentDetailsDTO.class);
//	
//	private static ClassMapBuilder<ReimbursementQuery, ViewQueryDTO> queryMap = mapperFactory.classMap(ReimbursementQuery.class,ViewQueryDTO.class);
//	private static ClassMapBuilder<Intimation, ViewQueryDTO> viewQueryMap = mapperFactory.classMap(Intimation.class,ViewQueryDTO.class);
//	private static ClassMapBuilder<ViewTmpIntimation, ViewClaimStatusDTO> intimationDetailsMap = mapperFactory.classMap(ViewTmpIntimation.class, ViewClaimStatusDTO.class);
//	private static ClassMapBuilder<Intimation, ViewClaimStatusDTO> viewIntimationDetailsMap = mapperFactory.classMap(Intimation.class, ViewClaimStatusDTO.class);
//	private static ClassMapBuilder<Hospitals, ViewClaimStatusDTO> hospitlaDetailsMap = mapperFactory.classMap(Hospitals.class, ViewClaimStatusDTO.class);
//	private static ClassMapBuilder<ViewTmpClaim, ClaimStatusRegistrationDTO> registrationDetailsMap = mapperFactory.classMap(ViewTmpClaim.class, ClaimStatusRegistrationDTO.class);
//
////	private static ClassMapBuilder<ReimbursementRejection, ViewRejectionDTO> rejectionMap = mapperFactory.classMap(ReimbursementRejection.class, ViewRejectionDTO.class);
//
//	private static ClassMapBuilder<PortablityPolicy, PortablitiyPolicyDTO> portablityMap = mapperFactory.classMap(PortablityPolicy.class, PortablitiyPolicyDTO.class);
//	
//	
//	public static void getAllMapValues()  {
//		
//		documentDetailsMap.field("key", "key");
//		documentDetailsMap.field("acknowledgeNumber", "acknowledgeNumber");
//		documentDetailsMap.field("documentReceivedFromId.value", "receivedFromValue");
//		documentDetailsMap.field("documentReceivedDate", "documentReceivedDate");
//		documentDetailsMap.field("modeOfReceiptId.key", "modeOfReceipt.id");
//		documentDetailsMap.field("modeOfReceiptId.value", "modeOfReceipt.value");
//		documentDetailsMap.field("status.processValue", "status");
//		documentDetailsMap.field("rodKey", "rodKey");
//		documentDetailsMap.field("claim", "claim");
//		documentDetailsMap.field("claim.intimation.intimationId", "intimationNumber");
//		
//		reimbursementMap.field("docAcknowLedgement.key", "key");
//		reimbursementMap.field("docAcknowLedgement.acknowledgeNumber", "acknowledgeNumber");
//		reimbursementMap.field("docAcknowLedgement.documentReceivedFromId.value", "receivedFromValue");
//		reimbursementMap.field("docAcknowLedgement.documentReceivedDate", "documentReceivedDate");
//		reimbursementMap.field("docAcknowLedgement.modeOfReceiptId.key", "modeOfReceipt.id");
//		reimbursementMap.field("docAcknowLedgement.modeOfReceiptId.value", "modeOfReceipt.value");
//		reimbursementMap.field("docAcknowLedgement.status.processValue", "status");
//		reimbursementMap.field("docAcknowLedgement.rodKey", "rodKey");
//		reimbursementMap.field("claim", "claim");
//		reimbursementMap.field("claim.intimation.intimationId", "intimationNumber");
//		reimbursementMap.field("rodNumber", "rodNumber");
//		reimbursementMap.field("key", "reimbursementKey");
//		reimbursementMap.field("status.processValue", "status");
//		reimbursementMap.field("medicalCompletedDate", "medicalCompleteDate");
//		reimbursementMap.field("currentProvisionAmt", "approvedAmount");
//		reimbursementMap.field("docAcknowLedgement", "docAcknowledgement");
//		
//		
//		acknowledgeDetailsMap.field("key", "key");
//		acknowledgeDetailsMap.field("acknowledgeNumber", "acknowledgementNumber");
//		acknowledgeDetailsMap.field("createdDate", "acknowledgmentCreateOn");
//		acknowledgeDetailsMap.field("documentReceivedFromId.value", "documentReceivedFromValue");
//		acknowledgeDetailsMap.field("documentReceivedDate", "documentsReceivedDate");
//		acknowledgeDetailsMap.field("modeOfReceiptId.value", "modeOfReceiptValue");
//		acknowledgeDetailsMap.field("rodKey", "rodKey");
//		acknowledgeDetailsMap.field("insuredContactNumber", "acknowledgmentContactNumber");
//		acknowledgeDetailsMap.field("insuredEmailId", "emailId");
//		acknowledgeDetailsMap.field("hospitalizationClaimedAmount", "hospitalizationClaimedAmount");
//		acknowledgeDetailsMap.field("preHospitalizationClaimedAmount", "preHospitalizationClaimedAmount");
//		acknowledgeDetailsMap.field("postHospitalizationClaimedAmount", "postHospitalizationClaimedAmount");
//		
//		acknowledgeQueryMap.field("reimbursement.docAcknowLedgement.acknowledgeNumber", "acknowledgementNo");
//		acknowledgeQueryMap.field("reimbursement.rodNumber", "rodNo");
//		acknowledgeQueryMap.field("reimbursement.docAcknowLedgement.documentReceivedFromId.value", "documentReceivedFrom");
//		acknowledgeQueryMap.field("reimbursement.docAcknowLedgement.documentReceivedDate", "documentReceivedDate");
//		//acknowledgeQueryMap.field("", "diagnosis");
//		acknowledgeQueryMap.field("key", "reimbursementQueryKey");
//		acknowledgeQueryMap.field("createdBy", "queryRaisedRole");
//		acknowledgeQueryMap.field("reimbursement.key", "reimbursementKey");
//		acknowledgeQueryMap.field("createdDate", "queryRaisedDate");
//		acknowledgeQueryMap.field("queryReply", "replyStatus");
//		acknowledgeQueryMap.field("status.processValue", "queryStatus");
//		acknowledgeQueryMap.field("reimbursement", "reimbursement");
//		acknowledgeQueryMap.field("reimbursement.docAcknowLedgement", "docAcknowledgment");
//		
///*		queryMap.field("key", "key");
//		queryMap.field("queryRemarks", "queryRemarks");
//		queryMap.field("createdBy", "queryRaised");
//		queryMap.field("createdBy", "designation");
//		queryMap.field("createdDate", "queryRaisedDate");
//		queryMap.field("status.processValue", "queryStatus");
//		queryMap.field("draftedDate", "queryDraftedDate");
//		queryMap.field("queryLetterRemarks", "queryLetterRemarks");
//		queryMap.field("approvedRejectionDate", "approvedRejectedDate");
//		queryMap.field("rejectionRemarks", "rejectedRemarks");
//		queryMap.field("redraftRemarks", "redraftRemarks");
//		queryMap.field("reimbursement.docAcknowLedgement.acknowledgeNumber", "acknowledgementNo");
//		queryMap.field("reimbursement.rodNumber", "rodNumber");
//		queryMap.field("reimbursement.docAcknowLedgement.documentReceivedFromId.value", "receivedFrom");
//		queryMap.field("reimbursement.claim.claimType.value", "claimType");
//		queryMap.field("reimbursement.claim.claimId", "claimNo");
//		queryMap.field("reimbursement.claim.intimation.intimationId", "intimationNo");
//		queryMap.field("reimbursement.claim.intimation.insuredPatientName", "insuredPatientName");
//		queryMap.field("reimbursement.claim.intimation.policy.policyNumber", "policyNo");
//		queryMap.field("reimbursement.claim.intimation.policy.productName", "productName");
//		queryMap.field("reimbursement.claim.intimation.admissionDate", "admissionDate");
//		
//		rejectionMap.field("key", "key");
//		rejectionMap.field("rejectionRemarks", "rejectionRemarks");
//		rejectionMap.field("createdBy", "rejectedName");
//		rejectionMap.field("createdDate", "rejectedDate");
//		rejectionMap.field("reimbursement.status.processValue", "rejectionStatus");
//		rejectionMap.field("rejectionDraftDate", "draftedDate"); 
//		rejectionMap.field("rejectionLetterRemarks", "letterRemarks");
//		rejectionMap.field("redraftDate", "reDraftedDate");
//		rejectionMap.field("redraftRemarks", "reDraftRemarks");
//		rejectionMap.field("reimbursement.claim.claimType.value", "claimType");
//		rejectionMap.field("reimbursement.claim.claimId", "claimNo");
//		rejectionMap.field("reimbursement.claim", "claim");
//		rejectionMap.field("reimbursement.claim.intimation.intimationId", "intimationNo");
//		rejectionMap.field("reimbursement.claim.intimation.insuredPatientName", "insuredPatientName");
//		rejectionMap.field("reimbursement.claim.intimation.policy.policyNumber", "policyNo");
//		rejectionMap.field("reimbursement.claim.intimation.policy.productName", "productName");
//		rejectionMap.field("reimbursement.claim.intimation.admissionDate", "admissionDate");
//		rejectionMap.field("disapprovedRemarks","disApprovalRemarks");  */
//		
//		
//		intimationDetailsMap.field("key","intimationKey");
//		intimationDetailsMap.field("intimationId", "intimationId");
//		intimationDetailsMap.field("createdDate", "dateOfIntimation");
//		intimationDetailsMap.field("cpuCode.cpuCode", "cpuId");
//		intimationDetailsMap.field("intimationMode.value", "intimationMode");
//		intimationDetailsMap.field("intimatedBy.value", "intimatedBy");
//		intimationDetailsMap.field("insured.insuredName", "insuredPatientName");
//		intimationDetailsMap.field("admissionDate", "admissionDate");
//		intimationDetailsMap.field("admissionType.value", "admissionType");
//		intimationDetailsMap.field("admissionReason", "reasonForAdmission");
//		intimationDetailsMap.field("inpatientNumber", "inpatientNumber");
//		intimationDetailsMap.field("lateIntimationReason", "lateIntimationReason");	
//		intimationDetailsMap.field("policy.policyNumber", "policyNumber");
//		intimationDetailsMap.field("policy.homeOfficeCode", "policyIssuing");
//		intimationDetailsMap.field("policy.product.value", "productName");
//		intimationDetailsMap.field("hospitalComments", "comments");
//		intimationDetailsMap.field("policy.smCode", "smCode");
//		intimationDetailsMap.field("policy.smName", "smName");
//		intimationDetailsMap.field("policy.agentCode", "agentBrokerCode");
//		intimationDetailsMap.field("policy.agentName", "agentBrokerName");
//		intimationDetailsMap.field("insured.healthCardNumber", "healthCardNo");
//		intimationDetailsMap.field("policy.proposerFirstName", "patientName");
//		
//		viewIntimationDetailsMap.field("key","intimationKey");
//		viewIntimationDetailsMap.field("intimationId", "intimationId");
//		viewIntimationDetailsMap.field("createdDate", "dateOfIntimation");
//		viewIntimationDetailsMap.field("cpuCode.cpuCode", "cpuId");
//		viewIntimationDetailsMap.field("intimationMode.value", "intimationMode");
//		viewIntimationDetailsMap.field("intimatedBy.value", "intimatedBy");
//		viewIntimationDetailsMap.field("insured.insuredName", "insuredPatientName");
//		viewIntimationDetailsMap.field("admissionDate", "admissionDate");
//		viewIntimationDetailsMap.field("admissionType.value", "admissionType");
//		viewIntimationDetailsMap.field("admissionReason", "reasonForAdmission");
//		viewIntimationDetailsMap.field("inpatientNumber", "inpatientNumber");
//		viewIntimationDetailsMap.field("lateIntimationReason", "lateIntimationReason");	
//		viewIntimationDetailsMap.field("policy.policyNumber", "policyNumber");
//		viewIntimationDetailsMap.field("policy.homeOfficeCode", "policyIssuing");
//		viewIntimationDetailsMap.field("policy.product.value", "productName");
//		viewIntimationDetailsMap.field("hospitalComments", "comments");
//		viewIntimationDetailsMap.field("policy.smCode", "smCode");
//		viewIntimationDetailsMap.field("policy.smName", "smName");
//		viewIntimationDetailsMap.field("policy.agentCode", "agentBrokerCode");
//		viewIntimationDetailsMap.field("policy.agentName", "agentBrokerName");
//		viewIntimationDetailsMap.field("insured.healthCardNumber", "healthCardNo");
//		viewIntimationDetailsMap.field("insuredPatientName", "patientName");
//		
//		hospitlaDetailsMap.field("state", "state");
//		hospitlaDetailsMap.field("city", "city");
//		hospitlaDetailsMap.field("name", "hospitalName");
//		hospitlaDetailsMap.field("hospitalCode", "hospitalInternalCode");
//		hospitlaDetailsMap.field("hospitalIrdaCode", "hospitalIrdaCode");
//		hospitlaDetailsMap.field("hospitalType.value", "hospitalTypeValue");
//		
//		registrationDetailsMap.field("claimId", "claimNo");
//		registrationDetailsMap.field("registrationRemarks", "registrationStatus");
//		registrationDetailsMap.field("currencyId.value", "currency");
//		registrationDetailsMap.field("provisionAmount", "provisionAmt");
//		registrationDetailsMap.field("claimType.value", "claimType");
//		registrationDetailsMap.field("registrationRemarks", "registrationRemarks");
//		
//		portablityMap.field("policyNumber", "policyNo");
//		portablityMap.field("insurerName", "insuredName");
////		portablityMap.field("insurerCode", "insurerCode");
////		portablityMap.field("productId", "productId");
//		portablityMap.field("productDescription", "productName");
//		portablityMap.field("policyType", "policyType");
//		portablityMap.field("tbaCode", "tbaCode");
//		portablityMap.field("periodElapsed", "periodElapsed");
//		portablityMap.field("policyTerm", "policyTerm");
////		portablityMap.field("", "dateOfBirth");
//		portablityMap.field("pedDeclared", "pedDeclared");
//		portablityMap.field("pedIcdCode", "pedIcdCode");
//		portablityMap.field("pedDescription", "pedDescription");
//		portablityMap.field("familySize", "familySize");
//		portablityMap.field("remarks", "remarks");
//		portablityMap.field("requestId", "requestId");
//		portablityMap.field("siFist", "siFist");
//		portablityMap.field("siSecond", "siSecond");
//		portablityMap.field("siThird", "siThird");
//		portablityMap.field("siFourth", "siFourth");
//		portablityMap.field("siFirstFloat", "siFirstFloat");
//		portablityMap.field("siSecondFloat", "siSecondFloat");
//		portablityMap.field("siThirdFloat", "siThirdFloat");
//		portablityMap.field("siFourthFloat", "siFourthFloat");
//		portablityMap.field("siFirstChange", "siFirstChange");
//		portablityMap.field("siSecondChange", "siSecondChange");
//		portablityMap.field("siThirdChange", "siThirdChange");
//		portablityMap.field("siFourthChange", "siFourthChange");
//		portablityMap.field("policyStartDate", "dateOfBirth");
//		portablityMap.field("dateOfBirth", "dateOfBirth");
//		portablityMap.field("memberEntryDate", "memberEntryDate");
//		
//		documentDetailsMapper = mapperFactory.getMapperFacade();	
//		queryDetailsMapper = mapperFactory.getMapperFacade();
//		queryMapper = mapperFactory.getMapperFacade();
//		portablityMapper = mapperFactory.getMapperFacade();
//		
//		intimationDetailsMapper = mapperFactory.getMapperFacade(ViewTmpIntimation.class, ViewClaimStatusDTO.class);
//		viewIntimationDetailsMapper = mapperFactory.getMapperFacade(OMPIntimation.class, ViewClaimStatusDTO.class);
//		hospitalDetailMapper = mapperFactory.getMapperFacade(Hospitals.class, ViewClaimStatusDTO.class);
//		registrationDetailsMapper = mapperFactory.getMapperFacade(ViewTmpClaim.class, ClaimStatusRegistrationDTO.class);
//		
////		viewQueryMapper = mapperFactory.getMapperFacade(ReimbursementQuery.class, ViewQueryDTO.class);
////		rejectionMapper = mapperFactory.getMapperFacade(ReimbursementRejection.class, ViewRejectionDTO.class);
//		
//		documentAcknowledgementMapper = mapperFactory.getMapperFacade(OMPDocAcknowledgement.class, DocumentDetailsDTO.class);		
//		documentMapper = mapperFactory.getMapperFacade(OMPDocAcknowledgement.class, ViewDocumentDetailsDTO.class);
//	}
//	
//	public static List<ViewDocumentDetailsDTO> getDocumentDetailsTableDTO(List<DocAcknowledgement> resultList) {
//		documentDetailsMap.register();
//		List<ViewDocumentDetailsDTO> mapAsList = documentDetailsMapper.mapAsList(resultList, ViewDocumentDetailsDTO.class);
//			return mapAsList;
//		
//	}
//	
//	public static List<ViewDocumentDetailsDTO> getDocumentDetailsTableReimbursemntWise(List<Reimbursement> resultList) {
//		reimbursementMap.register();
//		List<ViewDocumentDetailsDTO> mapAsList = documentDetailsMapper.mapAsList(resultList, ViewDocumentDetailsDTO.class);
//		return mapAsList;
//		
//	}
//	
//	public static ViewDocumentDetailsDTO getDocumentDetails(OMPDocAcknowledgement result){
//		
//		documentDetailsMap.register();
//		ViewDocumentDetailsDTO dto = documentMapper.map(result);
//		return dto;
//		
//	}
//	
////	public static List<ViewQueryDTO> getViewQueryDTO(List<ReimbursementQuery> resultList) {
////		queryMap.register();
////		List<ViewQueryDTO> mapAsList = queryDetailsMapper.mapAsList(resultList, ViewQueryDTO.class);
////			return mapAsList;
////		
////	}
////	
////	public static List<RODQueryDetailsDTO> getAckQueryDTO(List<ReimbursementQuery> resultList){
////		acknowledgeQueryMap.register();
////		List<RODQueryDetailsDTO> mapAsList = queryMapper.mapAsList(resultList, RODQueryDetailsDTO.class);
////		return mapAsList;
////	}
////	
////	public static ViewQueryDTO getviewDetailsQuery(ReimbursementQuery result){
////		queryMap.register();
////		ViewQueryDTO map = viewQueryMapper.map(result);
////		return map;
////	}
//	
//	public static  List<PortablitiyPolicyDTO> getPortablityDetails(List<PortablityPolicy> portablityPolicy)
//	{
//		portablityMap.register();
//		List<PortablitiyPolicyDTO> mapAsList = portablityMapper.mapAsList(portablityPolicy, PortablitiyPolicyDTO.class);
//		return mapAsList;
//	}
//	
//	
////	public static ViewRejectionDTO getRejectionDTO(ReimbursementRejection result){
////		rejectionMap.register();
////		ViewRejectionDTO map = rejectionMapper.map(result);
////		return map;
////	}
//	
//	public static DocumentDetailsDTO getAcknowledgementDetail(OMPDocAcknowledgement result){
//		acknowledgeDetailsMap.register();
//		DocumentDetailsDTO map = documentAcknowledgementMapper.map(result);
//		return map;
//	}
//	
//	public static ViewClaimStatusDTO getViewClaimStatusDto(ViewTmpIntimation intimation){
//		intimationDetailsMap.register();
//		ViewClaimStatusDTO map =  intimationDetailsMapper.map(intimation);
//		return map;
//	}
//	
//	public static ViewClaimStatusDTO getViewClaimStatusDto(OMPIntimation intimation){
//		viewIntimationDetailsMap.register();
//		ViewClaimStatusDTO map =  viewIntimationDetailsMapper.map(intimation);
//		return map;
//	}
//	
//	public static ViewClaimStatusDTO gethospitalDetails(Hospitals hospitals){
//		hospitlaDetailsMap.register();
//		ViewClaimStatusDTO map = hospitalDetailMapper.map(hospitals);
//		return map;
//	}
//	
//	public static ClaimStatusRegistrationDTO getRegistrationDetails(ViewTmpClaim claim){
//		registrationDetailsMap.register();
//		ClaimStatusRegistrationDTO map = registrationDetailsMapper.map(claim);
//		return map;
//	}
//	
//	public static OMPEarlierRodMapper getInstance(){
//        if(myObj == null){
//            myObj = new OMPEarlierRodMapper();
//            getAllMapValues();
//        }
//        return myObj;
//	 }
//	
}

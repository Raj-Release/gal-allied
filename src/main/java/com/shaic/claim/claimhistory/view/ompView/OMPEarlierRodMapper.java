package com.shaic.claim.claimhistory.view.ompView;


import java.util.List;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.impl.generator.EclipseJdtCompilerStrategy;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.OMPClaimPayment;
import com.shaic.claim.omp.createintimation.OMPCreateIntimationTableDTO;
import com.shaic.claim.omp.ratechange.OMPClaimRateChangeTableDto;
import com.shaic.claim.omp.searchregistration.OMPSearchClaimRegistrationTableDTO;
import com.shaic.claim.ompviewroddetails.OMPPaymentDetailsTableDTO;
import com.shaic.claim.ompviewroddetails.OMPRodAndBillEntryDetailTableDTO;
import com.shaic.claim.productbenefit.view.PortablitiyPolicyDTO;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.viewEarlierRodDetails.ClaimStatusRegistrationDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewClaimStatusDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewQueryDTO;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.OMPClaimRateChange;
import com.shaic.domain.OMPCurrencyHistory;
import com.shaic.domain.OMPDocAcknowledgement;
import com.shaic.domain.OMPHospitals;
import com.shaic.domain.OMPIntimation;
import com.shaic.domain.OMPOtherCurrencyRateHistoryDto;
import com.shaic.domain.OMPReimbursement;
import com.shaic.domain.preauth.PortablityPolicy;

public class OMPEarlierRodMapper {
	
	static MapperFactory mapperFactory = new DefaultMapperFactory.Builder().compilerStrategy(new EclipseJdtCompilerStrategy()).build();
	
	static OMPEarlierRodMapper myObj;
	
	private static MapperFacade documentDetailsMapper;
	private static MapperFacade queryDetailsMapper;
	private static MapperFacade queryMapper;
	private static MapperFacade portablityMapper;
	private static MapperFacade viewClaimRateMapper;
	private static MapperFacade viewClaimIntimationMapper;
	private static MapperFacade ompcurrencyMapper;
	private static MapperFacade ompRodMapper;
	private static MapperFacade viewPaymentMapper;
	private static BoundMapperFacade<OMPIntimation, ViewClaimStatusDTO> intimationDetailsMapper;
	private static BoundMapperFacade<OMPIntimation, ViewClaimStatusDTO> viewIntimationDetailsMapper;
	private static BoundMapperFacade<OMPHospitals, ViewClaimStatusDTO> hospitalDetailMapper;
	private static BoundMapperFacade<OMPClaim,ClaimStatusRegistrationDTO> registrationDetailsMapper;
	
	
	private static BoundMapperFacade<OMPDocAcknowledgement, DocumentDetailsDTO> documentAcknowledgementMapper;
	
//	private static BoundMapperFacade<ReimbursementRejection, ViewRejectionDTO> rejectionMapper;
	
	private static BoundMapperFacade<OMPDocAcknowledgement, ViewDocumentDetailsDTO> documentMapper;
//	private static BoundMapperFacade<OMPClaimRateChange, OMPClaimRateChangeTableDto> viewClaimRateMapper;
	
	private static ClassMapBuilder<OMPDocAcknowledgement, ViewDocumentDetailsDTO> documentDetailsMap = mapperFactory.classMap(OMPDocAcknowledgement.class,ViewDocumentDetailsDTO.class);
	private static ClassMapBuilder<OMPDocAcknowledgement, DocumentDetailsDTO> acknowledgeDetailsMap = mapperFactory.classMap(OMPDocAcknowledgement.class, DocumentDetailsDTO.class);
	
//	private static ClassMapBuilder<ReimbursementQuery, RODQueryDetailsDTO> acknowledgeQueryMap = mapperFactory.classMap(ReimbursementQuery.class, RODQueryDetailsDTO.class);
	
	private static ClassMapBuilder<OMPReimbursement, ViewDocumentDetailsDTO> reimbursementMap = mapperFactory.classMap(OMPReimbursement.class,ViewDocumentDetailsDTO.class);
	
//	private static ClassMapBuilder<ReimbursementQuery, ViewQueryDTO> queryMap = mapperFactory.classMap(ReimbursementQuery.class,ViewQueryDTO.class);
	
	private static ClassMapBuilder<OMPIntimation, ViewQueryDTO> viewQueryMap = mapperFactory.classMap(OMPIntimation.class,ViewQueryDTO.class);
	private static ClassMapBuilder<OMPIntimation, ViewClaimStatusDTO> viewIntimationDetailsMap = mapperFactory.classMap(OMPIntimation.class, ViewClaimStatusDTO.class);
	private static ClassMapBuilder<OMPHospitals, ViewClaimStatusDTO> hospitlaDetailsMap = mapperFactory.classMap(OMPHospitals.class, ViewClaimStatusDTO.class);
	private static ClassMapBuilder<OMPClaim, ClaimStatusRegistrationDTO> registrationDetailsMap = mapperFactory.classMap(OMPClaim.class, ClaimStatusRegistrationDTO.class);
	
//	private static ClassMapBuilder<ReimbursementRejection, ViewRejectionDTO> rejectionMap = mapperFactory.classMap(ReimbursementRejection.class, ViewRejectionDTO.class);
	
	private static ClassMapBuilder<PortablityPolicy, PortablitiyPolicyDTO> portablityMap = mapperFactory.classMap(PortablityPolicy.class, PortablitiyPolicyDTO.class);
	private static ClassMapBuilder<OMPClaimRateChange, OMPClaimRateChangeTableDto> claimRateDetailsMap = mapperFactory.classMap(OMPClaimRateChange.class, OMPClaimRateChangeTableDto.class);
	private static ClassMapBuilder<OMPCreateIntimationTableDTO, OMPSearchClaimRegistrationTableDTO> intimationRegDetailsMap = mapperFactory.classMap(OMPCreateIntimationTableDTO.class, OMPSearchClaimRegistrationTableDTO.class);	
	private static ClassMapBuilder<OMPCurrencyHistory, OMPOtherCurrencyRateHistoryDto> currencyRateDetailsMap = mapperFactory.classMap(OMPCurrencyHistory.class, OMPOtherCurrencyRateHistoryDto.class);
	private static ClassMapBuilder<OMPReimbursement, OMPRodAndBillEntryDetailTableDTO> ompreimbursementMap = mapperFactory.classMap(OMPReimbursement.class,OMPRodAndBillEntryDetailTableDTO.class);
	private static ClassMapBuilder<OMPClaimPayment, OMPPaymentDetailsTableDTO> claimPayment = mapperFactory.classMap(OMPClaimPayment.class, OMPPaymentDetailsTableDTO.class);
	public static void getAllMapValues()  {
		
		documentDetailsMap.field("key", "key");
		documentDetailsMap.field("acknowledgeNumber", "acknowledgeNumber");
		documentDetailsMap.field("documentReceivedFromId.value", "receivedFromValue");
		documentDetailsMap.field("documentReceivedDate", "documentReceivedDate");
		documentDetailsMap.field("modeOfReceiptId.key", "modeOfReceipt.id");
		documentDetailsMap.field("modeOfReceiptId.value", "modeOfReceipt.value");
		documentDetailsMap.field("status.processValue", "status");
		documentDetailsMap.field("rodKey", "rodKey");
		documentDetailsMap.field("claim", "claim");
		documentDetailsMap.field("claim.intimation.intimationId", "intimationNumber");
		
		reimbursementMap.field("docAcknowLedgement.key", "key");
		reimbursementMap.field("classiDocumentRecivedFmId.value", "receivedFromValue");
		reimbursementMap.field("documentRecivedDate", "documentReceivedDate");
		reimbursementMap.field("classiModeReceiptId.value", "modeOfReceipt.value");
		reimbursementMap.field("claim.key", "claim.key");
		reimbursementMap.field("claim.key", "claimDto.key");
		reimbursementMap.field("claim.intimation.intimationId", "intimationNumber");
		reimbursementMap.field("claim.intimation.key","intimation.key");
		reimbursementMap.field("claim.intimation.intimationId", "intimation.intimationId");		
		reimbursementMap.field("rodNumber", "rodNumber");
		reimbursementMap.field("key", "reimbursementKey");
		reimbursementMap.field("eventCodeId.eventCode","eventCode");
		reimbursementMap.field("classificationId.value", "classification");
		reimbursementMap.field("subClassificationId.value", "subclassification");
		reimbursementMap.field("status.processValue", "status");
		reimbursementMap.field("currentProvisionAmt", "approvedAmount");
		
		
		acknowledgeDetailsMap.field("key", "key");
		acknowledgeDetailsMap.field("acknowledgeNumber", "acknowledgementNumber");
		acknowledgeDetailsMap.field("createdDate", "acknowledgmentCreateOn");
		acknowledgeDetailsMap.field("documentReceivedFromId.value", "documentReceivedFromValue");
		acknowledgeDetailsMap.field("documentReceivedDate", "documentsReceivedDate");
		acknowledgeDetailsMap.field("modeOfReceiptId.value", "modeOfReceiptValue");
		acknowledgeDetailsMap.field("rodKey", "rodKey");
		acknowledgeDetailsMap.field("insuredContactNumber", "acknowledgmentContactNumber");
		acknowledgeDetailsMap.field("insuredEmailId", "emailId");
		acknowledgeDetailsMap.field("hospitalizationClaimedAmount", "hospitalizationClaimedAmount");
		acknowledgeDetailsMap.field("preHospitalizationClaimedAmount", "preHospitalizationClaimedAmount");
		acknowledgeDetailsMap.field("postHospitalizationClaimedAmount", "postHospitalizationClaimedAmount");
		
		
		
		viewIntimationDetailsMap.field("key","intimationKey");
		viewIntimationDetailsMap.field("intimationId", "intimationId");
		viewIntimationDetailsMap.field("createdDate", "dateOfIntimation");
		viewIntimationDetailsMap.field("cpuCode.cpuCode", "cpuId");
		viewIntimationDetailsMap.field("intimationMode.value", "intimationMode");
		viewIntimationDetailsMap.field("intimatedBy.value", "intimatedBy");
		viewIntimationDetailsMap.field("insured.insuredName", "insuredPatientName");
		viewIntimationDetailsMap.field("admissionDate", "admissionDate");
//		viewIntimationDetailsMap.field("admissionType.value", "admissionType");
//		viewIntimationDetailsMap.field("admissionReason", "reasonForAdmission");
//		viewIntimationDetailsMap.field("inpatientNumber", "inpatientNumber");
//		viewIntimationDetailsMap.field("lateIntimationReason", "lateIntimationReason");	
		viewIntimationDetailsMap.field("policy.policyNumber", "policyNumber");
		viewIntimationDetailsMap.field("policy.homeOfficeCode", "policyIssuing");
		viewIntimationDetailsMap.field("policy.product.value", "productName");
		viewIntimationDetailsMap.field("hospitalComments", "comments");
		viewIntimationDetailsMap.field("policy.smCode", "smCode");
		viewIntimationDetailsMap.field("policy.smName", "smName");
		viewIntimationDetailsMap.field("policy.agentCode", "agentBrokerCode");
		viewIntimationDetailsMap.field("policy.agentName", "agentBrokerName");
//		viewIntimationDetailsMap.field("insured.healthCardNumber", "healthCardNo");
//		viewIntimationDetailsMap.field("insuredPatientName", "patientName");
		
		
		
//		hospitlaDetailsMap.field("locality", "area");
		hospitlaDetailsMap.field("name", "hospitalName");
		hospitlaDetailsMap.field("hospitalCode", "hospitalInternalCode");
		hospitlaDetailsMap.field("hospitalCode", "hospitalIrdaCode");
		hospitlaDetailsMap.field("address","hospitalAddress");
		hospitlaDetailsMap.field("city", "city");
		hospitlaDetailsMap.field("state", "state");
		hospitlaDetailsMap.field("country","area");
		
		registrationDetailsMap.field("claimId", "claimNo");
		registrationDetailsMap.field("registrationRemarks", "registrationStatus");
//		registrationDetailsMap.field("currencyId.value", "currency");
		registrationDetailsMap.field("provisionAmount", "provisionAmt");
//		registrationDetailsMap.field("claimType.value", "claimType");
		registrationDetailsMap.field("registrationRemarks", "registrationRemarks");
		
		portablityMap.field("policyNumber", "policyNo");
		portablityMap.field("insurerName", "insuredName");
//		portablityMap.field("insurerCode", "insurerCode");
//		portablityMap.field("productId", "productId");
		portablityMap.field("productDescription", "productName");
		portablityMap.field("policyType", "policyType");
		portablityMap.field("tbaCode", "tbaCode");
		portablityMap.field("periodElapsed", "periodElapsed");
		portablityMap.field("policyTerm", "policyTerm");
//		portablityMap.field("", "dateOfBirth");
		portablityMap.field("pedDeclared", "pedDeclared");
		portablityMap.field("pedIcdCode", "pedIcdCode");
		portablityMap.field("pedDescription", "pedDescription");
		portablityMap.field("familySize", "familySize");
		portablityMap.field("remarks", "remarks");
		portablityMap.field("requestId", "requestId");
		portablityMap.field("siFist", "siFist");
		portablityMap.field("siSecond", "siSecond");
		portablityMap.field("siThird", "siThird");
		portablityMap.field("siFourth", "siFourth");
		portablityMap.field("siFirstFloat", "siFirstFloat");
		portablityMap.field("siSecondFloat", "siSecondFloat");
		portablityMap.field("siThirdFloat", "siThirdFloat");
		portablityMap.field("siFourthFloat", "siFourthFloat");
		portablityMap.field("siFirstChange", "siFirstChange");
		portablityMap.field("siSecondChange", "siSecondChange");
		portablityMap.field("siThirdChange", "siThirdChange");
		portablityMap.field("siFourthChange", "siFourthChange");
		portablityMap.field("policyStartDate", "dateOfBirth");
		portablityMap.field("dateOfBirth", "dateOfBirth");
		portablityMap.field("memberEntryDate", "memberEntryDate");
//		
		
		claimRateDetailsMap.field("key","key");
		claimRateDetailsMap.field("conversionRate", "conversionValue");
		claimRateDetailsMap.field("userId", "modifyby");
		claimRateDetailsMap.field("modifiedDate", "dateofModification");
		claimRateDetailsMap.field("stage.stageName", "processingStage");
//		claimRateDetailsMap.field("intimatedBy.value", "intimatedBy");
		
		
		intimationRegDetailsMap.field("intimationKey","intimationKey");
		intimationRegDetailsMap.field("intimationno", "intimationNo");
		intimationRegDetailsMap.field("policyNo", "policyno");
		intimationRegDetailsMap.field("productCodeOrName", "productcode");
		intimationRegDetailsMap.field("intimationdate", "intimationDate");
		intimationRegDetailsMap.field("admissionDate", "admissiondate");
		
		
		currencyRateDetailsMap.field("currency.currencyCode", "otherurrencyCode");
		currencyRateDetailsMap.field("currency.currencyName", "currencyName");
		currencyRateDetailsMap.field("currency.country", "country");
		currencyRateDetailsMap.field("currencyRate", "currencyRate");
		currencyRateDetailsMap.field("modifiedDate", "date");
		currencyRateDetailsMap.field("userId", "userName");
		currencyRateDetailsMap.field("stage.stageName", "processingStage");
		currencyRateDetailsMap.field("userName", "userId");
		
		ompreimbursementMap.field("rodNumber", "rodNo");
		ompreimbursementMap.field("claimTypeFlag", "strClaimType");
		ompreimbursementMap.field("eventCodeId.eventCode", "eventCode");
		ompreimbursementMap.field("classificationId.value", "classification");
		ompreimbursementMap.field("classiDocumentRecivedFmId.value", "documentReceivedFrom");
		ompreimbursementMap.field("documentTypeId.value", "rodType");
		ompreimbursementMap.field("documentRecivedDate", "documentReceivedDate");
		ompreimbursementMap.field("documentRecivedDate","lastDocumentReceivedDate");
		ompreimbursementMap.field("classiModeReceiptId.value", "modeofReceipt");		
		ompreimbursementMap.field("currentProvisionAmt", "approvedAmount");
		ompreimbursementMap.field("claim.intimation.intimationId", "intimationNo");
		ompreimbursementMap.field("claim.claimId","claimNo");
		ompreimbursementMap.field("claim.intimation.policyNumber", "policyNo");
		ompreimbursementMap.field("subClassificationId.value", "subClasification");
		ompreimbursementMap.field("status.processValue", "status");
		ompreimbursementMap.field("claim.productName", "productName");
		ompreimbursementMap.field("classificationId.value", "clasification");
		ompreimbursementMap.field("claim.intimation.insured.insuredName", "insuredPatientName");
		ompreimbursementMap.field("ailmentLoss", "ailment");
		ompreimbursementMap.field("currentProvisionAmt", "claimedAmount");
		
		claimPayment.field("paymentKey","key");
		claimPayment.field("rodNumber", "rodNo");
//		claimPayment.field("ClaimType", "claimType");
		claimPayment.field("clmCvrCode", "eventCode");
//		claimPayment.field("stage.stageName", "classification");
		claimPayment.field("docReceicedFrom", "documentReceivedFrom");
//		claimPayment.field("stageid.stageName", "rodType");
		claimPayment.field("totApprovedAmt", "amount");
		claimPayment.field("paymentType.value", "paymentType");
		claimPayment.field("bankName", "bankName");
		claimPayment.field("chequeDdNumber", "chequeOrTransactionno");
		claimPayment.field("chequeDdDate", "chequeOrTransactionDate");
		claimPayment.field("accountNo", "accountno");
		claimPayment.field("ifscCode", "ifscCode");
		claimPayment.field("statusId.processValue", "paymentStatus");
		claimPayment.field("createdDate", "ompProcessApprovedDate");
		claimPayment.field("remarks", "remarks");
//		claimPayment.field("branchName", "branchName");
//		claimPayment.field("branchName", "branchName");
		
		documentDetailsMapper = mapperFactory.getMapperFacade();
		
//		queryDetailsMapper = mapperFactory.getMapperFacade();
//		queryMapper = mapperFactory.getMapperFacade();
		
		portablityMapper = mapperFactory.getMapperFacade();
		
		intimationDetailsMapper = mapperFactory.getMapperFacade(OMPIntimation.class, ViewClaimStatusDTO.class);
		viewIntimationDetailsMapper = mapperFactory.getMapperFacade(OMPIntimation.class, ViewClaimStatusDTO.class);
		hospitalDetailMapper = mapperFactory.getMapperFacade(OMPHospitals.class, ViewClaimStatusDTO.class);
		registrationDetailsMapper = mapperFactory.getMapperFacade(OMPClaim.class, ClaimStatusRegistrationDTO.class);
		
//		viewQueryMapper = mapperFactory.getMapperFacade(ReimbursementQuery.class, ViewQueryDTO.class);
		
		documentAcknowledgementMapper = mapperFactory.getMapperFacade(OMPDocAcknowledgement.class, DocumentDetailsDTO.class);
		
//		rejectionMapper = mapperFactory.getMapperFacade(ReimbursementRejection.class, ViewRejectionDTO.class);
		
		documentMapper = mapperFactory.getMapperFacade(OMPDocAcknowledgement.class, ViewDocumentDetailsDTO.class);
		viewClaimRateMapper =   mapperFactory.getMapperFacade();
		viewClaimIntimationMapper = mapperFactory.getMapperFacade();
		ompcurrencyMapper = mapperFactory.getMapperFacade();
		ompRodMapper = mapperFactory.getMapperFacade();
		viewPaymentMapper = mapperFactory.getMapperFacade();
//				mapperFactory.getMapperFacade(OMPClaimRateChange.class, OMPClaimRateChangeTableDto.class);
	}
	
	public static List<ViewDocumentDetailsDTO> getDocumentDetailsTableDTO(List<OMPDocAcknowledgement> resultList) {
		documentDetailsMap.register();
		List<ViewDocumentDetailsDTO> mapAsList = documentDetailsMapper.mapAsList(resultList, ViewDocumentDetailsDTO.class);
			return mapAsList;
		
	}
	
	public static List<ViewDocumentDetailsDTO> getDocumentDetailsTableReimbursemntWise(List<OMPReimbursement> resultList) {
		reimbursementMap.register();
		List<ViewDocumentDetailsDTO> mapAsList = documentDetailsMapper.mapAsList(resultList, ViewDocumentDetailsDTO.class);
		return mapAsList;
		
	}
	
	public static ViewDocumentDetailsDTO getDocumentDetails(OMPDocAcknowledgement result){
		
		documentDetailsMap.register();
		ViewDocumentDetailsDTO dto = documentMapper.map(result);
		return dto;
		
	}
	
//	public static List<ViewQueryDTO> getViewQueryDTO(List<ReimbursementQuery> resultList) {
//		queryMap.register();
//		List<ViewQueryDTO> mapAsList = queryDetailsMapper.mapAsList(resultList, ViewQueryDTO.class);
//			return mapAsList;
//		
//	}
//	
//	public static List<RODQueryDetailsDTO> getAckQueryDTO(List<ReimbursementQuery> resultList){
//		acknowledgeQueryMap.register();
//		List<RODQueryDetailsDTO> mapAsList = queryMapper.mapAsList(resultList, RODQueryDetailsDTO.class);
//		return mapAsList;
//	}
//	
//	public static ViewQueryDTO getviewDetailsQuery(ReimbursementQuery result){
//		queryMap.register();
//		ViewQueryDTO map = viewQueryMapper.map(result);
//		return map;
//	}
	
	public static  List<PortablitiyPolicyDTO> getPortablityDetails(List<PortablityPolicy> portablityPolicy)
	{
		portablityMap.register();
		List<PortablitiyPolicyDTO> mapAsList = portablityMapper.mapAsList(portablityPolicy, PortablitiyPolicyDTO.class);
		return mapAsList;
//		return null;
	}
	
//	public static ViewRejectionDTO getRejectionDTO(ReimbursementRejection result){
//		rejectionMap.register();
//		ViewRejectionDTO map = rejectionMapper.map(result);
//		return map;
//	}
	
	public static DocumentDetailsDTO getAcknowledgementDetail(OMPDocAcknowledgement result){
		acknowledgeDetailsMap.register();
		DocumentDetailsDTO map = documentAcknowledgementMapper.map(result);
		return map;
	}
	
		
	public static ViewClaimStatusDTO getViewClaimStatusDto(OMPIntimation intimation){
		viewIntimationDetailsMap.register();
		ViewClaimStatusDTO map =  viewIntimationDetailsMapper.map(intimation);
		return map;
	}
	
	public static ViewClaimStatusDTO gethospitalDetails(OMPHospitals hospitals){
		hospitlaDetailsMap.register();
		ViewClaimStatusDTO map = hospitalDetailMapper.map(hospitals);
		return map;
	}
	
	public static ClaimStatusRegistrationDTO getRegistrationDetails(OMPClaim claim){
		registrationDetailsMap.register();
		ClaimStatusRegistrationDTO map = registrationDetailsMapper.map(claim);
		return map;
	}
	
	public static List<OMPClaimRateChangeTableDto> getClaimRateChangeDetails(List<OMPClaimRateChange> resultList) {
		claimRateDetailsMap.register();
		List<OMPClaimRateChangeTableDto> mapAsList =  viewClaimRateMapper.mapAsList(resultList, OMPClaimRateChangeTableDto.class);
		return mapAsList;
		
	}
	
	public static OMPSearchClaimRegistrationTableDTO getintimationChangeDetails(OMPCreateIntimationTableDTO resultList) {
		intimationRegDetailsMap.register();
		OMPSearchClaimRegistrationTableDTO map =  viewClaimIntimationMapper.map(resultList, OMPSearchClaimRegistrationTableDTO.class);
		return map;
		
	}
	
	public static List<OMPOtherCurrencyRateHistoryDto> getOMPCurrencyHistoryDetails(List<OMPCurrencyHistory> resultList) {
		currencyRateDetailsMap.register();
		List<OMPOtherCurrencyRateHistoryDto> mapAsList =  ompcurrencyMapper.mapAsList(resultList, OMPOtherCurrencyRateHistoryDto.class);
		return mapAsList;
		
	}
	
	public static List<OMPRodAndBillEntryDetailTableDTO> getOMPRodAndBillEntryDetailTableDTOList(List<OMPReimbursement> resultList) {
		ompreimbursementMap.register();
		List<OMPRodAndBillEntryDetailTableDTO> mapAsList = ompRodMapper.mapAsList(resultList, OMPRodAndBillEntryDetailTableDTO.class);
		return mapAsList;
		
	}
	
	public static List<OMPPaymentDetailsTableDTO> getOMPClaimPaymentDetails(List<OMPClaimPayment> resultList) {
		claimPayment.register();
		List<OMPPaymentDetailsTableDTO> mapAsList =  viewPaymentMapper.mapAsList(resultList, OMPPaymentDetailsTableDTO.class);
		return mapAsList;
		
	}
	
	public static OMPEarlierRodMapper getInstance(){
        if(myObj == null){
            myObj = new OMPEarlierRodMapper();
        }
        getAllMapValues();
        return myObj;
	 }	

}

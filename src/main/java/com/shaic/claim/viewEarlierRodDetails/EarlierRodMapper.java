package com.shaic.claim.viewEarlierRodDetails;


import java.util.List;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.impl.generator.EclipseJdtCompilerStrategy;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.productbenefit.view.ContinuityBenefitDTO;
import com.shaic.claim.productbenefit.view.PortablitiyPolicyDTO;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.RODQueryDetailsDTO;
import com.shaic.claim.viewEarlierRodDetails.dto.ViewRejectionDTO;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.GalaxyIntimation;
import com.shaic.domain.GmcContinuityBenefit;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.OPIntimation;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.ReimbursementQuery;
import com.shaic.domain.ReimbursementRejection;
import com.shaic.domain.ViewTmpClaim;
import com.shaic.domain.ViewTmpIntimation;
import com.shaic.domain.ViewTmpOPClaim;
import com.shaic.domain.preauth.PortabilityPreviousPolicy;
import com.shaic.domain.preauth.PortablityPolicy;

public class EarlierRodMapper {
	
	static MapperFactory mapperFactory = new DefaultMapperFactory.Builder().compilerStrategy(new EclipseJdtCompilerStrategy()).build();
	
	static EarlierRodMapper myObj;
	
	private static MapperFacade documentDetailsMapper;
	private static MapperFacade queryDetailsMapper;
	private static MapperFacade queryMapper;
	private static MapperFacade portablityMapper;
	private static MapperFacade continuityBenefitMapper;
	/**
	 * Below Line of code was Added as part of CR R1080
	 */
	private static MapperFacade portabilityPrevPolicyMapper;
	private static BoundMapperFacade<Intimation, ViewClaimStatusDTO> intimationDetailsMapper;
	private static BoundMapperFacade<Intimation, ViewClaimStatusDTO> viewIntimationDetailsMapper;
	private static BoundMapperFacade<OPIntimation, ViewClaimStatusDTO> viewOPIntimationDetailsMapper;
	private static BoundMapperFacade<GalaxyIntimation, ViewClaimStatusDTO> viewIntimationDetailsMapper1;
	private static BoundMapperFacade<Hospitals, ViewClaimStatusDTO> hospitalDetailMapper;
	private static BoundMapperFacade<ViewTmpClaim,ClaimStatusRegistrationDTO> registrationDetailsMapper;
	private static BoundMapperFacade<ViewTmpOPClaim,ClaimStatusRegistrationDTO> registrationDetailsOPMapper;
	private static BoundMapperFacade<ReimbursementQuery, ViewQueryDTO> viewQueryMapper;
	private static BoundMapperFacade<DocAcknowledgement, DocumentDetailsDTO> documentAcknowledgementMapper;
	private static BoundMapperFacade<ReimbursementRejection, ViewRejectionDTO> rejectionMapper;
	private static BoundMapperFacade<DocAcknowledgement, ViewDocumentDetailsDTO> documentMapper;
	
	
	/*private static ClassMapBuilder<DocAcknowledgement, ViewDocumentDetailsDTO> documentDetailsMap = mapperFactory.classMap(DocAcknowledgement.class,ViewDocumentDetailsDTO.class);
	private static ClassMapBuilder<DocAcknowledgement, DocumentDetailsDTO> acknowledgeDetailsMap = mapperFactory.classMap(DocAcknowledgement.class, DocumentDetailsDTO.class);
	private static ClassMapBuilder<ReimbursementQuery, RODQueryDetailsDTO> acknowledgeQueryMap = mapperFactory.classMap(ReimbursementQuery.class, RODQueryDetailsDTO.class);
	private static ClassMapBuilder<Reimbursement, ViewDocumentDetailsDTO> reimbursementMap = mapperFactory.classMap(Reimbursement.class,ViewDocumentDetailsDTO.class);
	private static ClassMapBuilder<ReimbursementQuery, ViewQueryDTO> queryMap = mapperFactory.classMap(ReimbursementQuery.class,ViewQueryDTO.class);
	private static ClassMapBuilder<Intimation, ViewQueryDTO> viewQueryMap = mapperFactory.classMap(Intimation.class,ViewQueryDTO.class);
	private static ClassMapBuilder<ViewTmpIntimation, ViewClaimStatusDTO> intimationDetailsMap = mapperFactory.classMap(ViewTmpIntimation.class, ViewClaimStatusDTO.class);
	private static ClassMapBuilder<Intimation, ViewClaimStatusDTO> viewIntimationDetailsMap = mapperFactory.classMap(Intimation.class, ViewClaimStatusDTO.class);
	private static ClassMapBuilder<Hospitals, ViewClaimStatusDTO> hospitlaDetailsMap = mapperFactory.classMap(Hospitals.class, ViewClaimStatusDTO.class);
	private static ClassMapBuilder<ViewTmpClaim, ClaimStatusRegistrationDTO> registrationDetailsMap = mapperFactory.classMap(ViewTmpClaim.class, ClaimStatusRegistrationDTO.class);
	private static ClassMapBuilder<ReimbursementRejection, ViewRejectionDTO> rejectionMap = mapperFactory.classMap(ReimbursementRejection.class, ViewRejectionDTO.class);
	private static ClassMapBuilder<PortablityPolicy, PortablitiyPolicyDTO> portablityMap = mapperFactory.classMap(PortablityPolicy.class, PortablitiyPolicyDTO.class);*/
	
	private static ClassMapBuilder<DocAcknowledgement, ViewDocumentDetailsDTO> documentDetailsMap = null;
	private static ClassMapBuilder<DocAcknowledgement, DocumentDetailsDTO> acknowledgeDetailsMap = null;
	private static ClassMapBuilder<ReimbursementQuery, RODQueryDetailsDTO> acknowledgeQueryMap = null;
	private static ClassMapBuilder<Reimbursement, ViewDocumentDetailsDTO> reimbursementMap = null;
	private static ClassMapBuilder<ReimbursementQuery, ViewQueryDTO> queryMap = null;
	private static ClassMapBuilder<Intimation, ViewQueryDTO> viewQueryMap = null;
	private static ClassMapBuilder<ViewTmpIntimation, ViewClaimStatusDTO> intimationDetailsMap = null;
	private static ClassMapBuilder<Intimation, ViewClaimStatusDTO> viewIntimationDetailsMap = null;
	private static ClassMapBuilder<OPIntimation, ViewClaimStatusDTO> viewOPIntimationDetailsMap = null;
	private static ClassMapBuilder<GalaxyIntimation, ViewClaimStatusDTO> viewIntimationDetailsMap1 = null;
	private static ClassMapBuilder<Hospitals, ViewClaimStatusDTO> hospitlaDetailsMap = null;
	private static ClassMapBuilder<ViewTmpClaim, ClaimStatusRegistrationDTO> registrationDetailsMap = null;
	private static ClassMapBuilder<ViewTmpOPClaim, ClaimStatusRegistrationDTO> registrationDetailsOPMap = null;
	private static ClassMapBuilder<ReimbursementRejection, ViewRejectionDTO> rejectionMap = null;
	private static ClassMapBuilder<PortablityPolicy, PortablitiyPolicyDTO> portablityMap = null;
	private static ClassMapBuilder<GmcContinuityBenefit, ContinuityBenefitDTO> continuityBenefitMap = null;
	
	/**
	 * Below line of code was Added as part of CR R1080
	 */
	private static ClassMapBuilder<PortabilityPreviousPolicy, PortablitiyPolicyDTO>	portabilityPrevPolicyMap = null;
	
	
	
	public static void getAllMapValues()  {
		
		documentDetailsMap = mapperFactory.classMap(DocAcknowledgement.class,ViewDocumentDetailsDTO.class);
		acknowledgeDetailsMap = mapperFactory.classMap(DocAcknowledgement.class, DocumentDetailsDTO.class);
		acknowledgeQueryMap = mapperFactory.classMap(ReimbursementQuery.class, RODQueryDetailsDTO.class);
		reimbursementMap = mapperFactory.classMap(Reimbursement.class,ViewDocumentDetailsDTO.class);
		queryMap = mapperFactory.classMap(ReimbursementQuery.class,ViewQueryDTO.class);
		viewQueryMap = mapperFactory.classMap(Intimation.class,ViewQueryDTO.class);
		intimationDetailsMap = mapperFactory.classMap(ViewTmpIntimation.class, ViewClaimStatusDTO.class);
		viewIntimationDetailsMap = mapperFactory.classMap(Intimation.class, ViewClaimStatusDTO.class);
		viewOPIntimationDetailsMap = mapperFactory.classMap(OPIntimation.class, ViewClaimStatusDTO.class);
		viewIntimationDetailsMap1 = mapperFactory.classMap(GalaxyIntimation.class, ViewClaimStatusDTO.class);
		hospitlaDetailsMap = mapperFactory.classMap(Hospitals.class, ViewClaimStatusDTO.class);
		registrationDetailsMap = mapperFactory.classMap(ViewTmpClaim.class, ClaimStatusRegistrationDTO.class);
		registrationDetailsOPMap = mapperFactory.classMap(ViewTmpOPClaim.class, ClaimStatusRegistrationDTO.class);
		rejectionMap = mapperFactory.classMap(ReimbursementRejection.class, ViewRejectionDTO.class);
		portablityMap = mapperFactory.classMap(PortablityPolicy.class, PortablitiyPolicyDTO.class);
		continuityBenefitMap = mapperFactory.classMap(GmcContinuityBenefit.class, ContinuityBenefitDTO.class);
		/**
		 * Below line of code was Added as part of CR R1080
		 */
		portabilityPrevPolicyMap = mapperFactory.classMap(PortabilityPreviousPolicy.class, PortablitiyPolicyDTO.class);
		
		documentDetailsMap.field("key", "key");
		documentDetailsMap.field("acknowledgeNumber", "acknowledgeNumber");
		documentDetailsMap.field("documentReceivedFromId.value", "receivedFromValue");
		documentDetailsMap.field("documentReceivedDate", "documentReceivedDate");
		documentDetailsMap.field("modeOfReceiptId.key", "modeOfReceipt.id");
		documentDetailsMap.field("modeOfReceiptId.value", "modeOfReceipt.value");
		documentDetailsMap.field("status.processValue", "status");
		documentDetailsMap.field("status.key", "statusKey");
		documentDetailsMap.field("rodKey", "rodKey");
		documentDetailsMap.field("claim", "claim");
		documentDetailsMap.field("claim.intimation.intimationId", "intimationNumber");
		documentDetailsMap.field("status.portalStatus", "portalStatusVal");
		documentDetailsMap.field("status.websiteStatus", "websiteStatusVal");
		
		
		
		reimbursementMap.field("docAcknowLedgement.key", "key");
		reimbursementMap.field("reconsiderationFlagReq", "crmFlagged");
		reimbursementMap.field("reconsiderationFlagRemark", "reconsiderationRejectionFlagRemarks");
		reimbursementMap.field("docAcknowLedgement.acknowledgeNumber", "acknowledgeNumber");
		reimbursementMap.field("docAcknowLedgement.documentReceivedFromId.value", "receivedFromValue");
		reimbursementMap.field("docAcknowLedgement.documentReceivedDate", "documentReceivedDate");
		reimbursementMap.field("docAcknowLedgement.modeOfReceiptId.key", "modeOfReceipt.id");
		reimbursementMap.field("docAcknowLedgement.modeOfReceiptId.value", "modeOfReceipt.value");
		reimbursementMap.field("docAcknowLedgement.status.processValue", "status");
		reimbursementMap.field("docAcknowLedgement.rodKey", "rodKey");
		reimbursementMap.field("claim", "claim");
		reimbursementMap.field("claim.intimation.intimationId", "intimationNumber");
		reimbursementMap.field("rodNumber", "rodNumber");
		reimbursementMap.field("key", "reimbursementKey");
		reimbursementMap.field("status.processValue", "status");
		reimbursementMap.field("medicalCompletedDate", "medicalCompleteDate");
		reimbursementMap.field("approvedAmount", "approvedAmount");
		reimbursementMap.field("docAcknowLedgement", "docAcknowledgement");
		reimbursementMap.field("benefitsId.value", "benefits");
		reimbursementMap.field("docAcknowLedgement.benifitClaimedAmount", "claimedAmount");
		reimbursementMap.field("version", "rodVersion");
		reimbursementMap.field("reconsiderationRequest", "reconsiderationFlag");
		reimbursementMap.field("status.portalStatus", "portalStatusVal");
		reimbursementMap.field("status.websiteStatus", "websiteStatusVal");
		reimbursementMap.field("grievanceRepresentation", "grievanceRepresentationFlag");
		
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
		acknowledgeDetailsMap.field("benifitClaimedAmount", "benefitClaimedAmt");
		
		acknowledgeQueryMap.field("reimbursement.docAcknowLedgement.acknowledgeNumber", "acknowledgementNo");
		acknowledgeQueryMap.field("reimbursement.rodNumber", "rodNo");
		acknowledgeQueryMap.field("reimbursement.docAcknowLedgement.documentReceivedFromId.value", "documentReceivedFrom");
		acknowledgeQueryMap.field("reimbursement.docAcknowLedgement.documentReceivedDate", "documentReceivedDate");
		//acknowledgeQueryMap.field("", "diagnosis");
		acknowledgeQueryMap.field("key", "reimbursementQueryKey");
		acknowledgeQueryMap.field("createdBy", "queryRaisedRole");
		acknowledgeQueryMap.field("reimbursement.key", "reimbursementKey");
		acknowledgeQueryMap.field("createdDate", "queryRaisedDate");
		acknowledgeQueryMap.field("queryReply", "replyStatus");
		acknowledgeQueryMap.field("status.processValue", "queryStatus");
		acknowledgeQueryMap.field("reimbursement", "reimbursement");
		acknowledgeQueryMap.field("reimbursement.docAcknowLedgement", "docAcknowledgment");
		
		queryMap.field("key", "key");
		queryMap.field("queryRemarks", "queryRemarks");
		queryMap.field("createdBy", "queryRaised");
		queryMap.field("createdBy", "designation");
		queryMap.field("createdDate", "queryRaisedDate");
		queryMap.field("status.processValue", "queryStatus");
		queryMap.field("draftedDate", "queryDraftedDate");
		queryMap.field("queryLetterRemarks", "queryLetterRemarks");
		queryMap.field("approvedRejectionDate", "approvedRejectedDate");
		queryMap.field("rejectionRemarks", "rejectedRemarks");
		queryMap.field("redraftRemarks", "redraftRemarks");
		queryMap.field("reimbursement.docAcknowLedgement.acknowledgeNumber", "acknowledgementNo");
		queryMap.field("reimbursement.rodNumber", "rodNumber");
		queryMap.field("reimbursement.docAcknowLedgement.documentReceivedFromId.value", "receivedFrom");
		queryMap.field("reimbursement.claim.claimType.value", "claimType");
		queryMap.field("reimbursement.claim.claimId", "claimNo");
		queryMap.field("reimbursement.claim.intimation.intimationId", "intimationNo");
		queryMap.field("reimbursement.claim.intimation.insuredPatientName", "insuredPatientName");
		queryMap.field("reimbursement.claim.intimation.policy.policyNumber", "policyNo");
		queryMap.field("reimbursement.claim.intimation.policy.productName", "productName");
		queryMap.field("reimbursement.claim.intimation.admissionDate", "admissionDate");
		queryMap.field("status.key", "statusId");
		queryMap.field("status.portalStatus", "portalStatusVal");
		queryMap.field("status.websiteStatus", "websiteStatusVal");
		
		rejectionMap.field("key", "key");
		rejectionMap.field("rejectionRemarks", "rejectionRemarks");
		rejectionMap.field("rejectionRemarks2", "rejectionRemarks2");
		rejectionMap.field("createdBy", "rejectedName");
		rejectionMap.field("createdDate", "rejectedDate");
		rejectionMap.field("reimbursement.status.processValue", "rejectionStatus");
		rejectionMap.field("rejectionDraftDate", "draftedDate"); 
		rejectionMap.field("rejectionLetterRemarks", "letterRemarks");
		rejectionMap.field("rejectionLetterRemarks2", "letterRemarks2");
		rejectionMap.field("rejectionCategory.value", "rejCategValue");
		rejectionMap.field("redraftDate", "reDraftedDate");
		rejectionMap.field("redraftRemarks", "reDraftRemarks");
		rejectionMap.field("reimbursement.claim.claimType.value", "claimType");
		rejectionMap.field("reimbursement.claim.claimId", "claimNo");
		rejectionMap.field("reimbursement.claim", "claim");
		rejectionMap.field("reimbursement.claim.intimation.intimationId", "intimationNo");
		rejectionMap.field("reimbursement.claim.intimation.insuredPatientName", "insuredPatientName");
		rejectionMap.field("reimbursement.claim.intimation.policy.policyNumber", "policyNo");
		rejectionMap.field("reimbursement.claim.intimation.policy.productName", "productName");
		rejectionMap.field("reimbursement.claim.intimation.admissionDate", "admissionDate");
		rejectionMap.field("disapprovedRemarks","disApprovalRemarks");
		
		
		intimationDetailsMap.field("key","intimationKey");
		intimationDetailsMap.field("intimationId", "intimationId");
		intimationDetailsMap.field("createdDate", "dateOfIntimation");
		intimationDetailsMap.field("cpuCode.cpuCode", "cpuId");
		intimationDetailsMap.field("intimationMode.value", "intimationMode");
		intimationDetailsMap.field("intimatedBy.value", "intimatedBy");
		intimationDetailsMap.field("insured.insuredName", "insuredPatientName");
		intimationDetailsMap.field("admissionDate", "admissionDate");
		intimationDetailsMap.field("admissionType.value", "admissionType");
		intimationDetailsMap.field("admissionReason", "reasonForAdmission");
		intimationDetailsMap.field("inpatientNumber", "inpatientNumber");
		intimationDetailsMap.field("lateIntimationReason", "lateIntimationReason");	
		//intimationDetailsMap.field("policy.policyNumber", "policyNumber");
		//intimationDetailsMap.field("policy.homeOfficeCode", "policyIssuing");
		//intimationDetailsMap.field("policy.product.value", "productName");
		intimationDetailsMap.field("hospitalComments", "comments");
		//intimationDetailsMap.field("policy.smCode", "smCode");
		//intimationDetailsMap.field("policy.smName", "smName");
		//intimationDetailsMap.field("policy.agentCode", "agentBrokerCode");
		//intimationDetailsMap.field("policy.agentName", "agentBrokerName");
		intimationDetailsMap.field("insured.healthCardNumber", "healthCardNo");
		//intimationDetailsMap.field("policy.proposerFirstName", "patientName");
		intimationDetailsMap.field("processClaimType", "claimProcessType");
		
		viewIntimationDetailsMap.field("key","intimationKey");
		viewIntimationDetailsMap.field("intimationId", "intimationId");
		viewIntimationDetailsMap.field("createdDate", "dateOfIntimation");
		viewIntimationDetailsMap.field("cpuCode.cpuCode", "cpuId");
		viewIntimationDetailsMap.field("intimationMode.value", "intimationMode");
		viewIntimationDetailsMap.field("intimatedBy.value", "intimatedBy");
		viewIntimationDetailsMap.field("insured.insuredName", "insuredPatientName");
		viewIntimationDetailsMap.field("admissionDate", "admissionDate");
		viewIntimationDetailsMap.field("admissionType.value", "admissionType");
		viewIntimationDetailsMap.field("admissionReason", "reasonForAdmission");
		viewIntimationDetailsMap.field("inpatientNumber", "inpatientNumber");
		viewIntimationDetailsMap.field("lateIntimationReason", "lateIntimationReason");	
		viewIntimationDetailsMap.field("policy.policyNumber", "policyNumber");
		viewIntimationDetailsMap.field("policy.homeOfficeCode", "policyIssuing");
		viewIntimationDetailsMap.field("policy.product.value", "productName");
		viewIntimationDetailsMap.field("hospitalComments", "comments");
		viewIntimationDetailsMap.field("policy.smCode", "smCode");
		viewIntimationDetailsMap.field("policy.smName", "smName");
		viewIntimationDetailsMap.field("policy.agentCode", "agentBrokerCode");
		viewIntimationDetailsMap.field("policy.agentName", "agentBrokerName");
		viewIntimationDetailsMap.field("insured.healthCardNumber", "healthCardNo");
		viewIntimationDetailsMap.field("policy.proposerFirstName", "patientName");
		viewIntimationDetailsMap.field("processClaimType", "claimProcessType");
		viewIntimationDetailsMap.field("intimaterName", "intimatorName");
		viewIntimationDetailsMap.field("callerMobileNumber", "callerMobileNumber");
		viewIntimationDetailsMap.field("callerAddress", "callerAddress");
		viewIntimationDetailsMap.field("callerEmail", "callerEmail");
		viewIntimationDetailsMap.field("doctorName", "hospitalDoctorName");
		viewIntimationDetailsMap.field("roomCategory.value", "roomCategory");
		
		viewOPIntimationDetailsMap.field("key","intimationKey");
		viewOPIntimationDetailsMap.field("intimationId", "intimationId");
		viewOPIntimationDetailsMap.field("createdDate", "dateOfIntimation");
		viewOPIntimationDetailsMap.field("cpuCode.cpuCode", "cpuId");
		viewOPIntimationDetailsMap.field("intimationMode.value", "intimationMode");
		viewOPIntimationDetailsMap.field("intimatedBy.value", "intimatedBy");
		viewOPIntimationDetailsMap.field("insured.insuredName", "insuredPatientName");
		viewOPIntimationDetailsMap.field("admissionDate", "admissionDate");
		viewOPIntimationDetailsMap.field("admissionType.value", "admissionType");
		viewOPIntimationDetailsMap.field("admissionReason", "reasonForAdmission");
		viewOPIntimationDetailsMap.field("inpatientNumber", "inpatientNumber");
		viewOPIntimationDetailsMap.field("lateIntimationReason", "lateIntimationReason");	
		viewOPIntimationDetailsMap.field("policy.policyNumber", "policyNumber");
		viewOPIntimationDetailsMap.field("policy.homeOfficeCode", "policyIssuing");
		viewOPIntimationDetailsMap.field("policy.product.value", "productName");
		viewOPIntimationDetailsMap.field("hospitalComments", "comments");
		viewOPIntimationDetailsMap.field("policy.smCode", "smCode");
		viewOPIntimationDetailsMap.field("policy.smName", "smName");
		viewOPIntimationDetailsMap.field("policy.agentCode", "agentBrokerCode");
		viewOPIntimationDetailsMap.field("policy.agentName", "agentBrokerName");
		viewOPIntimationDetailsMap.field("insured.healthCardNumber", "healthCardNo");
		viewOPIntimationDetailsMap.field("policy.proposerFirstName", "patientName");
		viewOPIntimationDetailsMap.field("processClaimType", "claimProcessType");
		viewOPIntimationDetailsMap.field("intimaterName", "intimatorName");
		viewOPIntimationDetailsMap.field("callerMobileNumber", "callerMobileNumber");
		viewOPIntimationDetailsMap.field("callerAddress", "callerAddress");
		viewOPIntimationDetailsMap.field("callerEmail", "callerEmail");
		viewOPIntimationDetailsMap.field("doctorName", "hospitalDoctorName");
		viewOPIntimationDetailsMap.field("roomCategory.value", "roomCategory");
		
		viewIntimationDetailsMap1.field("key","intimationKey");
		viewIntimationDetailsMap1.field("intimationId", "intimationId");
		viewIntimationDetailsMap1.field("createdDate", "dateOfIntimation");
		viewIntimationDetailsMap1.field("cpuCode.cpuCode", "cpuId");
		viewIntimationDetailsMap1.field("intimationMode.value", "intimationMode");
		viewIntimationDetailsMap1.field("intimatedBy.value", "intimatedBy");
		viewIntimationDetailsMap1.field("insured.insuredName", "insuredPatientName");
		viewIntimationDetailsMap1.field("admissionDate", "admissionDate");
		viewIntimationDetailsMap1.field("admissionType.value", "admissionType");
		viewIntimationDetailsMap1.field("admissionReason", "reasonForAdmission");
		viewIntimationDetailsMap1.field("inpatientNumber", "inpatientNumber");
		viewIntimationDetailsMap1.field("lateIntimationReason", "lateIntimationReason");	
		viewIntimationDetailsMap1.field("policy.policyNumber", "policyNumber");
		viewIntimationDetailsMap1.field("policy.homeOfficeCode", "policyIssuing");
		viewIntimationDetailsMap1.field("policy.product.value", "productName");
		viewIntimationDetailsMap1.field("hospitalComments", "comments");
		viewIntimationDetailsMap1.field("policy.smCode", "smCode");
		viewIntimationDetailsMap1.field("policy.smName", "smName");
		viewIntimationDetailsMap1.field("policy.agentCode", "agentBrokerCode");
		viewIntimationDetailsMap1.field("policy.agentName", "agentBrokerName");
		viewIntimationDetailsMap1.field("insured.healthCardNumber", "healthCardNo");
		viewIntimationDetailsMap1.field("policy.proposerFirstName", "patientName");
		viewIntimationDetailsMap1.field("processClaimType", "claimProcessType");
		viewIntimationDetailsMap1.field("intimaterName", "intimatorName");
		viewIntimationDetailsMap1.field("callerMobileNumber", "callerMobileNumber");
		viewIntimationDetailsMap1.field("callerAddress", "callerAddress");
		viewIntimationDetailsMap1.field("callerEmail", "callerEmail");
		viewIntimationDetailsMap1.field("doctorName", "hospitalDoctorName");
		viewIntimationDetailsMap1.field("roomCategory.value", "roomCategory");
		
		hospitlaDetailsMap.field("state", "state");
		hospitlaDetailsMap.field("city", "city");
//		hospitlaDetailsMap.field("locality", "area");
		hospitlaDetailsMap.field("name", "hospitalName");
		hospitlaDetailsMap.field("hospitalCode", "hospitalInternalCode");
		hospitlaDetailsMap.field("hospitalIrdaCode", "hospitalIrdaCode");
		hospitlaDetailsMap.field("hospitalType.value", "hospitalTypeValue");
		hospitlaDetailsMap.field("phoneNumber", "hospitalPhoneNo");
		hospitlaDetailsMap.field("fax", "hospitalFaxNo");
		hospitlaDetailsMap.field("clmPrcsInstruction", "suspiciousReason");
		
		registrationDetailsMap.field("claimId", "claimNo");
		registrationDetailsMap.field("registrationRemarks", "registrationStatus");
		registrationDetailsMap.field("currencyId.value", "currency");
		registrationDetailsMap.field("provisionAmount", "provisionAmt");
		registrationDetailsMap.field("claimType.value", "claimType");
		registrationDetailsMap.field("registrationRemarks", "registrationRemarks");
		registrationDetailsMap.field("incidenceFlag", "incidence");
		
		registrationDetailsOPMap.field("claimId", "claimNo");
		registrationDetailsOPMap.field("registrationRemarks", "registrationStatus");
		registrationDetailsOPMap.field("currencyId.value", "currency");
		registrationDetailsOPMap.field("provisionAmount", "provisionAmt");
		registrationDetailsOPMap.field("claimType.value", "claimType");
		registrationDetailsOPMap.field("registrationRemarks", "registrationRemarks");
		registrationDetailsOPMap.field("incidenceFlag", "incidence");
		
		portablityMap.field("policyNumber", "policyNo");
		portablityMap.field("insurerName", "insurerName");
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
		
		//ContinuityBenefit
		continuityBenefitMap.field("policyYr", "policyYr");
		continuityBenefitMap.field("insuredName", "insuredName");
		continuityBenefitMap.field("policyNo", "policyNo");
		continuityBenefitMap.field("policyFromDate", "policyFrmDate");
		continuityBenefitMap.field("policyToDate", "policyToDate");
		continuityBenefitMap.field("pedWaiver", "PEDwaiver");
		continuityBenefitMap.field("waiver30Days", "waiver30Days");
		continuityBenefitMap.field("exclusion1Yr", "exclusionYear1");
		continuityBenefitMap.field("exclusion2Yr", "exclusionYear2");
		continuityBenefitMap.field("createdBy", "createdBy");
		continuityBenefitMap.field("createdDate", "createdDate");
		continuityBenefitMap.field("inceptionDate", "inceptionDate");
		
		/**
		 * Below code was Added as part of CR R1080
		 */		
		portabilityPrevPolicyMap.field("amount", "amount");
		portabilityPrevPolicyMap.field("cummulativeBonus", "cumulativeBonus");
		portabilityPrevPolicyMap.field("customerId", "custId");
		portabilityPrevPolicyMap.field("exclusion_1stYr", "exclusionYear1");
		portabilityPrevPolicyMap.field("exclusion_2ndYr", "exclusionYear2");
		portabilityPrevPolicyMap.field("insurerName", "insurerName");
		portabilityPrevPolicyMap.field("natureofIllness", "natureOfIllnessClaimPaid");
		portabilityPrevPolicyMap.field("noofClaims", "noofClaims");
		portabilityPrevPolicyMap.field("pedDetails", "detailPEDAny");
		portabilityPrevPolicyMap.field("pedWaiver", "PEDwaiver");
		portabilityPrevPolicyMap.field("policyFmDt", "policyFrmDate");
		portabilityPrevPolicyMap.field("policyNumber", "policyNo");
		portabilityPrevPolicyMap.field("policyToDt", "policyToDate");
		portabilityPrevPolicyMap.field("policyType", "policyType");
		portabilityPrevPolicyMap.field("productName", "productName");
		portabilityPrevPolicyMap.field("sumInsured", "sumInsured");
		portabilityPrevPolicyMap.field("uwYear", "underwritingYear");
		portabilityPrevPolicyMap.field("waiver30Days", "waiver30Days");
		portabilityPrevPolicyMap.field("year", "year");
		portabilityPrevPolicyMap.field("activeStatus", "activeStatus");
		portabilityPrevPolicyMap.field("insuredName", "insuredName");
		portabilityPrevPolicyMap.field("currentPolicyNumber", "currentPolicyNo");
		
		documentDetailsMap.register();
		reimbursementMap.register();
		documentDetailsMap.register();
		queryMap.register();
		acknowledgeQueryMap.register();
		portablityMap.register();
		continuityBenefitMap.register();
		portabilityPrevPolicyMap.register();
		rejectionMap.register();
		acknowledgeDetailsMap.register();
		intimationDetailsMap.register();
		viewIntimationDetailsMap.register();
		viewOPIntimationDetailsMap.register();
		viewIntimationDetailsMap1.register();
		hospitlaDetailsMap.register();
		registrationDetailsMap.register();
		registrationDetailsOPMap.register();

		
		documentDetailsMapper = mapperFactory.getMapperFacade();	
		queryDetailsMapper = mapperFactory.getMapperFacade();
		queryMapper = mapperFactory.getMapperFacade();
		portablityMapper = mapperFactory.getMapperFacade();
		continuityBenefitMapper = mapperFactory.getMapperFacade();
		/**
		 * Below Line of code was Added as part of CR R1080
		 */
		portabilityPrevPolicyMapper = mapperFactory.getMapperFacade();
		
		intimationDetailsMapper = mapperFactory.getMapperFacade(Intimation.class, ViewClaimStatusDTO.class);
		viewIntimationDetailsMapper = mapperFactory.getMapperFacade(Intimation.class, ViewClaimStatusDTO.class);
		viewOPIntimationDetailsMapper = mapperFactory.getMapperFacade(OPIntimation.class, ViewClaimStatusDTO.class);
		viewIntimationDetailsMapper1 = mapperFactory.getMapperFacade(GalaxyIntimation.class, ViewClaimStatusDTO.class);
		hospitalDetailMapper = mapperFactory.getMapperFacade(Hospitals.class, ViewClaimStatusDTO.class);
		registrationDetailsMapper = mapperFactory.getMapperFacade(ViewTmpClaim.class, ClaimStatusRegistrationDTO.class);
		registrationDetailsOPMapper = mapperFactory.getMapperFacade(ViewTmpOPClaim.class, ClaimStatusRegistrationDTO.class);
		viewQueryMapper = mapperFactory.getMapperFacade(ReimbursementQuery.class, ViewQueryDTO.class);
		documentAcknowledgementMapper = mapperFactory.getMapperFacade(DocAcknowledgement.class, DocumentDetailsDTO.class);
		rejectionMapper = mapperFactory.getMapperFacade(ReimbursementRejection.class, ViewRejectionDTO.class);
		documentMapper = mapperFactory.getMapperFacade(DocAcknowledgement.class, ViewDocumentDetailsDTO.class);
	}
	
	public static List<ViewDocumentDetailsDTO> getDocumentDetailsTableDTO(List<DocAcknowledgement> resultList) {
		//documentDetailsMap.register();
		List<ViewDocumentDetailsDTO> mapAsList = documentDetailsMapper.mapAsList(resultList, ViewDocumentDetailsDTO.class);
		return mapAsList;
		
	}
	
	public static List<ViewDocumentDetailsDTO> getDocumentDetailsTableReimbursemntWise(List<Reimbursement> resultList) {
		//reimbursementMap.register();
		List<ViewDocumentDetailsDTO> mapAsList = documentDetailsMapper.mapAsList(resultList, ViewDocumentDetailsDTO.class);
		return mapAsList;
		
	}
	
	public static ViewDocumentDetailsDTO getDocumentDetails(DocAcknowledgement result){
		
		//documentDetailsMap.register();
		ViewDocumentDetailsDTO dto = documentMapper.map(result);
		return dto;
		
	}
	
	public static List<ViewQueryDTO> getViewQueryDTO(List<ReimbursementQuery> resultList) {
		//queryMap.register();
		List<ViewQueryDTO> mapAsList = queryDetailsMapper.mapAsList(resultList, ViewQueryDTO.class);
			return mapAsList;
		
	}
	
	public static List<RODQueryDetailsDTO> getAckQueryDTO(List<ReimbursementQuery> resultList){
		//acknowledgeQueryMap.register();
		List<RODQueryDetailsDTO> mapAsList = queryMapper.mapAsList(resultList, RODQueryDetailsDTO.class);
		return mapAsList;
	}
	
	public static ViewQueryDTO getviewDetailsQuery(ReimbursementQuery result){
		//queryMap.register();
		ViewQueryDTO map = viewQueryMapper.map(result);
		return map;
	}
	
	public static  List<PortablitiyPolicyDTO> getPortablityDetails(List<PortablityPolicy> portablityPolicy)
	{
		//portablityMap.register();
		List<PortablitiyPolicyDTO> mapAsList = portablityMapper.mapAsList(portablityPolicy, PortablitiyPolicyDTO.class);
		return mapAsList;
//		return null;
	}
	
	//for continuity benefit
	public static  List<ContinuityBenefitDTO> getContinuityBenefitDetails(List<GmcContinuityBenefit> portablityPolicy)
	{
		//continuityBenefitMap.register();
		List<ContinuityBenefitDTO> mapAsList = continuityBenefitMapper.mapAsList(portablityPolicy, ContinuityBenefitDTO.class);
		return mapAsList;
//		return null;
	}
	
	/**
	 * Below code was Added as part of CR R1080
	 */
	public static  List<PortablitiyPolicyDTO> getPortablityPrevPolicyDetails(List<PortabilityPreviousPolicy> portablityPrevPolicyList)
	{
		//portabilityPrevPolicyMap.register();
		List<PortablitiyPolicyDTO> mapAsList = portabilityPrevPolicyMapper.mapAsList(portablityPrevPolicyList, PortablitiyPolicyDTO.class);
		return mapAsList;
	}	
	
	public static ViewRejectionDTO getRejectionDTO(ReimbursementRejection result){
		//rejectionMap.register();
		ViewRejectionDTO map = rejectionMapper.map(result);
		return map;
	}
	
	public static DocumentDetailsDTO getAcknowledgementDetail(DocAcknowledgement result){
		//acknowledgeDetailsMap.register();
		DocumentDetailsDTO map = documentAcknowledgementMapper.map(result);
		return map;
	}
	
	public static ViewClaimStatusDTO getViewClaimStatusDtoForDetails(Intimation intimation){
		//ntimationDetailsMap.register();
		ViewClaimStatusDTO map =  intimationDetailsMapper.map(intimation);
		return map;
	}
	
	public static ViewClaimStatusDTO getViewClaimStatusDto(Intimation intimation){
		//viewIntimationDetailsMap.register();
		ViewClaimStatusDTO map =  viewIntimationDetailsMapper.map(intimation);
		return map;
	}
	
	public static ViewClaimStatusDTO getViewClaimStatusDto(OPIntimation intimation){
		//viewOPIntimationDetailsMap.register();
		ViewClaimStatusDTO map =  viewOPIntimationDetailsMapper.map(intimation);
		return map;
	}
	
	public static ViewClaimStatusDTO getViewGalaxyIntimationStatusDto(GalaxyIntimation intimation){
		//viewIntimationDetailsMap1.register();
		ViewClaimStatusDTO map =  viewIntimationDetailsMapper1.map(intimation);
		return map;
	}
	
	public static ViewClaimStatusDTO gethospitalDetails(Hospitals hospitals){
		//hospitlaDetailsMap.register();
		ViewClaimStatusDTO map = hospitalDetailMapper.map(hospitals);
		return map;
	}
	
	public static ClaimStatusRegistrationDTO getRegistrationDetails(ViewTmpClaim claim){
		//registrationDetailsMap.register();
		ClaimStatusRegistrationDTO map = registrationDetailsMapper.map(claim);
		return map;
	}
	
	public static ClaimStatusRegistrationDTO getRegistrationDetails(ViewTmpOPClaim claim){
		//registrationDetailsOPMap.register();
		ClaimStatusRegistrationDTO map = registrationDetailsOPMapper.map(claim);
		return map;
	}
	
	public static EarlierRodMapper getInstance(){
        if(myObj == null){
            myObj = new EarlierRodMapper();
            getAllMapValues();
        }
        return myObj;
	 }
	
	public static void invalidate(EarlierRodMapper earlierRodMapper){
		earlierRodMapper= null;
	 }

}

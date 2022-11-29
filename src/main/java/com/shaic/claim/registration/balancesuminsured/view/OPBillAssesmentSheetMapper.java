package com.shaic.claim.registration.balancesuminsured.view;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Insured;
import com.shaic.domain.Policy;
import com.shaic.domain.outpatient.OPDocumentBillEntry;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class OPBillAssesmentSheetMapper {
	
	
	static OPBillAssesmentSheetMapper  myObj;
	
	static MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
	private static MapperFacade tableMapper;

	private static BoundMapperFacade<Policy, OPBillAssesmentSheetDTO> policyBoundMapperFacade;
	private static BoundMapperFacade<Insured, OPBillAssesmentSheetDTO> insuredBoundMapperFacade;
	private static BoundMapperFacade<OPDocumentBillEntry, OPBillAssesmentSheetDTO> oPDocumentBillEntryMapperFacade;
	private static ClassMapBuilder<Policy, OPBillAssesmentSheetDTO> policyMapper = mapperFactory
			.classMap(Policy.class, OPBillAssesmentSheetDTO.class);
	private static ClassMapBuilder<Insured, OPBillAssesmentSheetDTO> insuredMapper = mapperFactory
			.classMap(Insured.class, OPBillAssesmentSheetDTO.class);
	private static ClassMapBuilder<OPDocumentBillEntry, OPBillAssesmentSheetDTO> oPDocumentBillEntryMapper = mapperFactory
			.classMap(OPDocumentBillEntry.class, OPBillAssesmentSheetDTO.class);

	public static void getAllMapValues() {
		policyMapper.field("policyNumber", "policyNo");
		policyMapper.field("policyType.value", "policyType");
		policyMapper.field("policyFromDate", "coveragePeriodFrom");
		policyMapper.field("policyToDate", "coveragePeriodTo");
		// policyMapper.field("insured.insuredAge", "age");
		// policyMapper.field("insured.relationshipwithInsuredId",
		// "relationship");

		insuredMapper.field("policy.policyNumber", "policyNo");
		insuredMapper.field("insuredName", "insuredName");
		insuredMapper.field("policy.policyType.value", "policyType");
		insuredMapper.field("policy.policyFromDate", "coveragePeriodFrom");
		insuredMapper.field("policy.policyToDate", "coveragePeriodTo");
		insuredMapper.field("insuredAge", "age");
		insuredMapper.field("relationshipwithInsuredId", "relationship");
		insuredMapper.field("policy.policyNumber", "policyNo");
		insuredMapper.field("policy.policyNumber", "policyNo");

		oPDocumentBillEntryMapper.field("opHealthCheckup.claim.claimId",
				"claimNo");
		oPDocumentBillEntryMapper.field(
				"opHealthCheckup.claim.intimation.insured.insuredId", "idNo");
		oPDocumentBillEntryMapper.field(
				"opHealthCheckup.claim.intimation.policy.policyNumber",
				"policyNo");
		oPDocumentBillEntryMapper.field(
				"opHealthCheckup.claim.intimation.policy.policyType.value",
				"policyType");
		oPDocumentBillEntryMapper.field(
				"opHealthCheckup.claim.intimation.policy.policyFromDate",
				"coveragePeriodFrom");
		oPDocumentBillEntryMapper.field(
				"opHealthCheckup.claim.intimation.policy.policyToDate",
				"coveragePeriodTo");
		oPDocumentBillEntryMapper.field(
				"opHealthCheckup.claim.intimation.policy.proposerFirstName",
				"insuredName");
		oPDocumentBillEntryMapper.field(
				"opHealthCheckup.claim.intimation.insured.insuredAge", "age");
		oPDocumentBillEntryMapper
				.field("opHealthCheckup.claim.intimation.insured.relationshipwithInsuredId",
						"relationship");
		oPDocumentBillEntryMapper.field("opHealthCheckup.payeeName",
				"payeeName");
		oPDocumentBillEntryMapper.field("opHealthCheckup.availableSI",
				"balanceSIAvailable");
		oPDocumentBillEntryMapper.field("claimedAmount", "amountClaimed");
		oPDocumentBillEntryMapper.field("opHealthCheckup.documentReceivedDate",
				"billReceivedDate");

		policyMapper.register();
		insuredMapper.register();
		oPDocumentBillEntryMapper.register();

		tableMapper = mapperFactory.getMapperFacade();
		policyBoundMapperFacade = mapperFactory.getMapperFacade(Policy.class,
				OPBillAssesmentSheetDTO.class);
		insuredBoundMapperFacade = mapperFactory.getMapperFacade(Insured.class,
				OPBillAssesmentSheetDTO.class);
		oPDocumentBillEntryMapperFacade = mapperFactory.getMapperFacade(
				OPDocumentBillEntry.class, OPBillAssesmentSheetDTO.class);
	}

	public static OPBillAssesmentSheetDTO getpolicyMapper(Policy policy) {

		OPBillAssesmentSheetDTO uploadDocs = policyBoundMapperFacade
				.map(policy);
		return uploadDocs;
	}

	public static OPBillAssesmentSheetDTO getInsuredMapper(Insured insured) {

		OPBillAssesmentSheetDTO uploadDocs = insuredBoundMapperFacade
				.map(insured);
		return uploadDocs;
	}

	public static OPBillAssesmentSheetDTO getOPDocumentBillEntryMapper(
			OPDocumentBillEntry oPDocumentBillEntr) {

		OPBillAssesmentSheetDTO uploadDocs = oPDocumentBillEntryMapperFacade
				.map(oPDocumentBillEntr);
		return uploadDocs;
	}
	/*
	 * public static List<OPBillAssesmentSheetDTO>
	 * getInsuredMapper(List<Insured> premPreviousPolicyDetails) {
	 * List<OPBillAssesmentSheetDTO> previousPolicy =
	 * tableMapper.mapAsList(premPreviousPolicyDetails
	 * ,OPBillAssesmentSheetDTO.class); return previousPolicy; }
	 */
	
	public static OPBillAssesmentSheetMapper getInstance(){
        if(myObj == null){
            myObj = new OPBillAssesmentSheetMapper();
            getAllMapValues();
        }
        return myObj;
	 }

}

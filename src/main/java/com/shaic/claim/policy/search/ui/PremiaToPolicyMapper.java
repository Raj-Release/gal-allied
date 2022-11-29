package com.shaic.claim.policy.search.ui;

import java.util.List;

import javax.persistence.Column;

import org.codehaus.jackson.annotate.JsonProperty;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.ClassMapBuilder;
import ma.glasnost.orika.metadata.Type;

import com.shaic.claim.productbenefit.view.PortablitiyPolicyDTO;
import com.shaic.domain.GmcContinuityBenefit;
import com.shaic.domain.GmcCoorporateBufferLimit;
import com.shaic.domain.GpaBenefitDetails;
import com.shaic.domain.Insured;
import com.shaic.domain.InsuredPedDetails;
import com.shaic.domain.MasAilmentLimit;
import com.shaic.domain.MasCopayLimit;
import com.shaic.domain.MasDeliveryExpLimit;
import com.shaic.domain.MasPrePostHospLimit;
import com.shaic.domain.MasRoomRentLimit;
import com.shaic.domain.NomineeDetails;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyBankDetails;
import com.shaic.domain.PolicyEndorsementDetails;
import com.shaic.domain.PolicyNominee;
import com.shaic.domain.PreviousPolicy;
import com.shaic.domain.preauth.PortabilityPreviousPolicy;
import com.shaic.domain.preauth.PortablityPolicy;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class PremiaToPolicyMapper {
	
	private static BoundMapperFacade<PremPolicyDetails, Policy> policyMapper;
	private static BoundMapperFacade<PremInsuredOMPDetails, Insured> insuredMapper;
	private static BoundMapperFacade<PremEndorsementDetails, PolicyEndorsementDetails> endorsementMapper;
	//private static BoundMapperFacade<DocumentDetailsApi, DocumentDetails> documentDetailsMapper;
	private static MapperFacade tableMapper;
	
	static PremiaToPolicyMapper myObj;
	
	public static void getAllMapValues()  {
	
	
	MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);

	ClassMapBuilder<PremPolicyDetails, Policy> policyMap = mapperFactory.classMap(PremPolicyDetails.class, Policy.class);
	ClassMapBuilder<PremInsuredDetails, Insured> insuredMap = mapperFactory.classMap(PremInsuredDetails.class, Insured.class);
	ClassMapBuilder<PremInsuredOMPDetails, Insured> insuredOMPMap = mapperFactory.classMap(PremInsuredOMPDetails.class, Insured.class);
	ClassMapBuilder<PremPEDDetails, InsuredPedDetails> insuredPedMap = mapperFactory.classMap(PremPEDDetails.class, InsuredPedDetails.class);
	ClassMapBuilder<PremEndorsementDetails, PolicyEndorsementDetails> endorsementMap = mapperFactory.classMap(PremEndorsementDetails.class, PolicyEndorsementDetails.class);
	ClassMapBuilder<PremInsuredNomineeDetails, NomineeDetails> nomineeMap = mapperFactory.classMap(PremInsuredNomineeDetails.class, NomineeDetails.class);
	ClassMapBuilder<PremPreviousPolicyDetails, PreviousPolicy> previusPolicyMap = mapperFactory.classMap(PremPreviousPolicyDetails.class, PreviousPolicy.class);
	ClassMapBuilder<PremPortability, PortablityPolicy> portablityMap = mapperFactory.classMap(PremPortability.class, PortablityPolicy.class);
	ClassMapBuilder<PremBankDetails, PolicyBankDetails> bankDetailsMap = mapperFactory.classMap(PremBankDetails.class, PolicyBankDetails.class);
	ClassMapBuilder<PremiaInsuredPA, Insured> insuredMapPA = mapperFactory.classMap(PremiaInsuredPA.class, Insured.class);

	/**
	 *  Below code was Added as part of CR R1080
	 */
	ClassMapBuilder<PremPortabilityPrevPolicyDetails, PortabilityPreviousPolicy> portablityPrevPolicyMap = mapperFactory.classMap(PremPortabilityPrevPolicyDetails.class, PortabilityPreviousPolicy.class);

	
	//ClassMapBuilder<DocumentDetailsApi, DocumentDetails> documentDetailsMap = mapperFactory.classMap(DocumentDetailsApi.class, DocumentDetails.class);
	ClassMapBuilder<PremGmcAilmentLimit, MasAilmentLimit> ailmentLimit = mapperFactory.classMap(PremGmcAilmentLimit.class, MasAilmentLimit.class);
	ClassMapBuilder<PremGmcCopayLimit, MasCopayLimit> coPayLimit = mapperFactory.classMap(PremGmcCopayLimit.class, MasCopayLimit.class);
	ClassMapBuilder<PremDeliveryExpLimit, MasDeliveryExpLimit> deliveryLimit = mapperFactory.classMap(PremDeliveryExpLimit.class, MasDeliveryExpLimit.class);
	ClassMapBuilder<PremGmcPrePostLimit, MasPrePostHospLimit> prePostLimit = mapperFactory.classMap(PremGmcPrePostLimit.class, MasPrePostHospLimit.class);
	ClassMapBuilder<PremGmcRoomRentLimit, MasRoomRentLimit> roomRentLimit = mapperFactory.classMap(PremGmcRoomRentLimit.class, MasRoomRentLimit.class);
	ClassMapBuilder<PremDependentInsuredDetails, Insured> dependentInsuredMap = mapperFactory.classMap(PremDependentInsuredDetails.class, Insured.class);
	
	ClassMapBuilder<PremGpaBenefitDetails, GpaBenefitDetails> benefitMap = mapperFactory.classMap(PremGpaBenefitDetails.class, GpaBenefitDetails.class);
	
	ClassMapBuilder<PremInsuredNomineeDetails, PolicyNominee> insuredNomineeMap = mapperFactory.classMap(PremInsuredNomineeDetails.class, PolicyNominee.class);
	
	ClassMapBuilder<PremGMCContinuityBenefits, GmcContinuityBenefit> gmcContBenMap = mapperFactory.classMap(PremGMCContinuityBenefits.class, GmcContinuityBenefit.class);
	
	ClassMapBuilder<PremGmcCorpBufferLimit, GmcCoorporateBufferLimit> corpBufferLimit = mapperFactory.classMap(PremGmcCorpBufferLimit.class, GmcCoorporateBufferLimit.class);
	
		policyMap.field("policyNo", "policyNumber");
		policyMap.field("proposerCode", "proposerCode");
		policyMap.field("receiptNo", "receiptNumber");
		policyMap.field("productName", "productName");
		policyMap.field("policyEndNo", "endorsementNumber");
		policyMap.field("proposerName", "proposerFirstName");
	///	policyMap.field("proposerDOB", "proposerDob");
		policyMap.field("proposerMobileNo", "registeredMobileNumber");
		
		policyMap.field("proposerAddress1", "proposerAddress1");
		policyMap.field("proposerAddress2", "proposerAddress2");
		policyMap.field("proposerAddress3", "proposerAddress3");
		
		policyMap.field("proposerOfficeAddress1", "polOfficeAddr1");
		policyMap.field("proposerOfficeAddress2", "polOfficeAddr2");
		policyMap.field("proposerOfficeAddress3", "polOfficeAddr3");
		
	///	policyMap.field("policyStartDate", "policyFromDate");
	///	policyMap.field("policyEndDate", "policyToDate");
		policyMap.field("proposerTelNo", "polTelephoneNumber");
		policyMap.field("proposerFaxNo", "polFaxnumber");
		policyMap.field("proposerEmail", "polEmailId");
		
		policyMap.field("grossPremium", "grossPremium");
		//policyMap.field("polPremTax", "premiumTax");
		policyMap.field("renewalPolicyNo", "renewalPolicyNumber");
		policyMap.field("officeCode", "homeOfficeCode");
//		policyMap.field("proposerOfficeAddress1", "polOfficeAddr1");
//		policyMap.field("proposerOfficeTelNo", "polTelephoneNumber");
		//policyMap.field("proposerOfficeEmail", "polEmailId");
		policyMap.field("proposerOfficeFaxNo", "polFaxnumber");
	///	policyMap.field("receiptDate", "receiptDate");
		policyMap.field("agentCode", "agentCode");
		policyMap.field("agentName", "agentName");
		//policyMap.field("polStampDuty", "stampDuty");
		//policyMap.field("polTotalAmt", "totalPremium");
		policyMap.field("smCode", "smCode");
		policyMap.field("smName", "smName");
		policyMap.field("policySumInsured", "totalSumInsured");
		policyMap.field("policyStatus", "policyStatus");
		policyMap.field("cumulativeBonus", "cummulativeBonus");
		policyMap.field("coPay", "copay");
		
		policyMap.field("city", "proposerCity");
		policyMap.field("state", "proposerState");
		policyMap.field("district", "proposerDist");
		policyMap.field("pinCode", "proposerPinCode");
		policyMap.field("pml", "pml");
		policyMap.field("proposerTitle", "proposerTitle");
		policyMap.field("paymentMode", "paymentMade");
		policyMap.field("sectionCode","sectionCode");
		policyMap.field("sectionDescription","sectionDescription");
		
		policyMap.field("stopLossPercentage", "stopLossPercentage");
		policyMap.field("corporateBuffer", "corporateBuffer");
		policyMap.field("innerLimit", "innerLimit");
		policyMap.field("sectionCode", "sectionCode");
		policyMap.field("sectionDescription", "sectionDescription");
		
		//Jet privillage product
		policyMap.field("masterPolicyNumber", "masterPolicyNumber");
		policyMap.field("linkPolicyNo", "linkPolicyNumber");
		
		//R0974
		policyMap.field("communicationType", "communicationType");
		policyMap.field("paymentParty", "paymentParty");
		policyMap.field("gmcPolicyType", "gmcPolicyType");

	//	policyMap.field("sumInsuredII", "sumInsuredII");
		//policyMap.field("restoredSI", "restoredSI");
		//policyMap.field("rechargedSI", "rechargeSI");
	//	policyMap.field("juniorMarketingCode", "juniorMarketingCode");
	//	policyMap.field("juniorMarketingName", "juniorMarketingName");
		policyMap.register();
		
		
		
		endorsementMap.field("endNo", "endorsementNumber");
		//endorsementMap.field("endoresementDate", "endoresementDate");
	///	endorsementMap.field("endEffFmDt", "effectiveFromDate");
	///	endorsementMap.field("endEffToDt", "effectiveToDate");
		endorsementMap.field("endType", "endorsementType");
		endorsementMap.field("endText", "endorsementText");
		endorsementMap.field("endSumInsured", "sumInsured");
		endorsementMap.field("endRevisedSumInsured", "revisedSumInsured");
		endorsementMap.field("endPremium", "premium");
		endorsementMap.register();
		
		nomineeMap.field("nomineeAge","nomineeAge");
		nomineeMap.field("nomineeName","nomineeName");
		nomineeMap.field("nomineeRelation","relationshipCode");
		nomineeMap.register();

		insuredMap.field("riskSysId","insuredId");
	///	insuredMap.field("gender","insuredGender.value");
		insuredMap.field("insuredName","insuredName");
		//insuredMap.field("insuredAge","insuredAge");
	///	insuredMap.field("dob","insuredDateOfBirth");
	///	insuredMap.field("relation","relationshipwithInsuredId.value");
		insuredMap.field("relation", "relationshipCode");
		insuredMap.field("healthCardNo","healthCardNumber");
		insuredMap.field("sumInsured","insuredSumInsured");
	//	insuredMap.field("registerdMobileNumber","registerdMobileNumber");
		insuredMap.field("cumulativeBonus","cummulativeBonus");
		insuredMap.field("addBenefitsHospCash", "hospitalCashBenefits");
		insuredMap.field("addBenefitsPatientCare", "patientCareBenefits");
	//	insuredMap.field("restoredSI","insuredRestoredSI");
	//	insuredMap.field("rechargedSI","insuredRechargedSI");
	    insuredMap.field("employeeId","insuredEmployeeId");	
		insuredMap.field("section2SI", "section2SI");
		
		//GPA insured Details
		insuredMap.field("effectiveFromDate", "strEffectivedFromDate");
		insuredMap.field("effectiveToDate", "strEffectiveToDate");
		insuredMap.field("workPlaceAccident", "workPlaceAccident");
		//insuredMap.field("memberID", "memId");
		insuredMap.field("monthlyIncome", "monthlyIncome");
		insuredMap.field("numberOfPerson", "numberOfPerson");
		insuredMap.field("occupation", "occupation");
		insuredMap.field("recType", "recType");
		insuredMap.field("categoryDescription", "categoryDescription");
		insuredMap.field("category", "category");
		insuredMap.field("earningParentSI", "earningParentSI");
		insuredMap.field("nomineeRelation", "nomineeRelation");
		insuredMap.field("nomineeName", "nomineeName");
		insuredMap.field("nomineeSharePercent", "nomineeSharePercent");
		insuredMap.field("outPatient", "outPatient");
		insuredMap.field("table1","table1");
		insuredMap.field("table2","table2");
		insuredMap.field("table3","table3");
		insuredMap.field("table4","table4");
		insuredMap.field("hospitalExpension", "hospitalExpensive");
		insuredMap.field("inPatient", "inPatient");
		insuredMap.field("medicalExtensionOtherPaClaim", "medicalExtensionOtherPaClaim");
		insuredMap.field("medicalExtension", "medicalExtension");
		insuredMap.field("transMortRems", "transMortRem");
		insuredMap.field("tutionFees", "tutionFees");
		insuredMap.field("innerLimt", "innerLimit");
		insuredMap.field("pedCoPay", "pedCoPay");
		insuredMap.field("voluntaryCoPay", "voluntaryCopay");
		insuredMap.field("compCopay", "compCopay");
		insuredMap.field("dependentFloaterSI", "gmcFloaterSI");
		
		//Jet Privillage Product
		//insuredMap.field("jetUniqueId", "insuredEmployeeId");
		insuredMap.field("mailId", "insuredEmailId");
		insuredMap.field("mainMember", "mainMember");
		insuredMap.field("mobileNumber", "registerdMobileNumber");
		insuredMap.field("insuredAge", "strInsuredAge");
		insuredMap.field("dob", "strDateOfBirth");
		insuredMap.field("contactNumber", "insuredContactNumber");
		insuredMap.field("linkEmailId", "linkMailId");
		insuredMap.field("linkEmpName", "linkEmpName");
		insuredMap.field("linkEmpNo", "linkEmpNumber");
		insuredMap.field("linkMobileNo", "linkEmpMobileNo");
		insuredMap.field("section1_SI", "sumInsured1");
		insuredMap.field("section2_SI", "sumInsured2");
		insuredMap.field("section3_SI", "sumInsured3");
		insuredMap.field("gender","strGender");
		insuredMap.field("entryAge", "entryAge");
		insuredMap.field("policyYear", "policyYear");
		insuredMap.field("address1", "address1");
		insuredMap.field("address2", "address2");
		insuredMap.field("address3", "address3");
		insuredMap.field("city", "city");
		insuredMap.field("pinCode", "insuredPinCode");
		insuredMap.field("state", "insuredState");
		
		//R0974
		insuredMap.field("accountNo", "accountNumber");
		insuredMap.field("bankName", "bankName");
		insuredMap.field("branchName", "branchName");
		insuredMap.field("ifscCode", "ifscCode");
		insuredMap.field("nameOfAccountHolder", "nameOfAccountHolder");
		
		insuredMap.field("dependantSIFlag", "dependentSIFlag");
		insuredMap.field("certificateNo", "certificateNo");
		
		//TOP-UP POLICY
		insuredMap.field("topUpPolicyNo", "topUppolicyNo");
		insuredMap.field("topUpPolicyRiskId", "topUpInsuredId");
		// OP
		insuredMap.field("policyPlan","plan");
		
		
		insuredMap.register();
		
		//insuredOMPMap.field("riskSysId","insuredId");
		insuredOMPMap.field("insuredAge","strInsuredAge");
		insuredOMPMap.field("dob","strDateOfBirth");
		insuredOMPMap.field("gender","strGender");
		insuredOMPMap.field("idCardNo","healthCardNumber");
		insuredOMPMap.field("insuredName", "insuredName");
		insuredOMPMap.field("passportNo","passportNo");
		insuredOMPMap.field("typeofVisa","visaType");
		insuredOMPMap.field("placeofTravel1", "placeOfvisit");
		insuredOMPMap.field("plan","plan");
		insuredOMPMap.field("purposeofVisit","purposeOfvisit");
		insuredOMPMap.field("assigneename", "assigneeName");
		insuredOMPMap.field("specialExclusions", "compulsoryExclusions");
		insuredOMPMap.register();
		
		dependentInsuredMap.field("riskSysId","insuredId");
		///	insuredMap.field("gender","insuredGender.value");
		dependentInsuredMap.field("insuredName","insuredName");
		dependentInsuredMap.field("insuredAge","strInsuredAge");
		///	insuredMap.field("dob","insuredDateOfBirth");
		///	insuredMap.field("relation","relationshipwithInsuredId.value");
		dependentInsuredMap.field("relation", "relationshipCode");
		dependentInsuredMap.field("healthCardNo","healthCardNumber");
		dependentInsuredMap.field("sumInsured","insuredSumInsured");
		//	insuredMap.field("registerdMobileNumber","registerdMobileNumber");
		dependentInsuredMap.field("cumulativeBonus","cummulativeBonus");
		dependentInsuredMap.field("addBenefitsHospCash", "hospitalCashBenefits");
		dependentInsuredMap.field("addBenefitsPatientCare", "patientCareBenefits");
		//	insuredMap.field("restoredSI","insuredRestoredSI");
		//	insuredMap.field("rechargedSI","insuredRechargedSI");
		//  insuredMap.field("employeeId","insuredEmployeeId");	
		dependentInsuredMap.field("section2SI", "section2SI");
			
		dependentInsuredMap.field("innerLimt", "innerLimit");
		dependentInsuredMap.field("pedCoPay", "pedCoPay");
		dependentInsuredMap.field("voluntaryCoPay", "voluntaryCopay");
		dependentInsuredMap.field("compCopay", "compCopay");
		
		dependentInsuredMap.field("effectiveFromDate", "strEffectivedFromDate");
		dependentInsuredMap.field("effectiveToDate", "strEffectiveToDate");
		dependentInsuredMap.field("gender","strGender");
		
		//TOP-UP POLICY
		dependentInsuredMap.field("topUpPolicyNo", "topUppolicyNo");
		dependentInsuredMap.field("topUpPolicyRiskId", "topUpInsuredId");
		
		dependentInsuredMap.register();
		
		insuredMapPA.field("age","strInsuredAge");
		insuredMapPA.field("dateOfBirth","strDateOfBirth");
		insuredMapPA.field("gender","strGender");
		insuredMapPA.field("idCardNumber","healthCardNumber");
		insuredMapPA.field("insuredCummulativeBonus","cummulativeBonus");
		insuredMapPA.field("insuredName", "insuredName");
		insuredMapPA.field("relation","relationshipCode");
		insuredMapPA.field("riskSysId","insuredId");
		insuredMapPA.field("majorDisabilities", "majorDisablities");
		insuredMapPA.field("riskGroup", "riskGroup");
		insuredMapPA.field("sumInsured","insuredSumInsured");
		insuredMapPA.register();

		insuredPedMap.field("pedCode", "pedCode");
		insuredPedMap.field("pedDescription", "pedDescription");
		insuredPedMap.field("pedEffectiveFromDate", "strEffectivedFromDate");
		insuredPedMap.field("pedEffectiveFmDate", "strEffectivedFromDate");
		insuredPedMap.field("pedEffectiveToDate", "strEffectiveToDate");
		insuredPedMap.field("pedType", "pedType");
		//insuredPedMap.field("key", "insuredKey");
		insuredPedMap.register();
		
		previusPolicyMap.field("proposerCode", "proposerCode");
	///	previusPolicyMap.field("policyStartDate", "policyFrmDate");
	///	previusPolicyMap.field("policyEndDate", "policyToDate");
		previusPolicyMap.field("productCode", "productCode");
		previusPolicyMap.field("productName", "productName");
		previusPolicyMap.field("sumInsured", "sumInsured");
		previusPolicyMap.field("premium", "premium");
		previusPolicyMap.field("officeCode", "issuingOfficeCode");
		previusPolicyMap.field("previousInsurerName", "insuredName");
		previusPolicyMap.field("preExistingDisease", "preExistingDisease");
		previusPolicyMap.field("proposerAddress", "proposerAddress");
		previusPolicyMap.field("proposerTelNo", "proposerTelephoneNo");
		previusPolicyMap.field("proposerOfficeEmail", "proposerEmailId");
		previusPolicyMap.field("proposerOfficeFaxNo", "proposerFaxNo");
		previusPolicyMap.field("previousInsurerName", "previousInsurerName");
	///	previusPolicyMap.field("policyFromDate", "policyFrmDate");
		previusPolicyMap.field("policyNo", "policyNumber");
	///	previusPolicyMap.field("policyToDate", "policyToDate");
		previusPolicyMap.field("policyUWYear", "underWritingYear");
	///	previusPolicyMap.field("proposerEmail", "proposerEmailId");
	///	previusPolicyMap.field("proposerFaxNo", "proposerFaxNo");
		previusPolicyMap.field("proposerName", "proposerName");
		previusPolicyMap.register();
		
		portablityMap.field("policyNumber", "policyNumber");
		portablityMap.field("insurerName", "insurerName");
		portablityMap.field("insurerCode", "insurerCode");
		portablityMap.field("productId", "productId");
		portablityMap.field("productDesc", "productDescription");
		portablityMap.field("policyType", "policyType");
		portablityMap.field("tbaCode", "tbaCode");
		portablityMap.field("periodElapsed", "periodElapsed");
		portablityMap.field("policyTerm", "policyTerm");
//		portablityMap.field("", "dateOfBirth");
		portablityMap.field("pedDeclared", "pedDeclared");
		portablityMap.field("pedIcdCode", "pedIcdCode");
		portablityMap.field("pedDesc", "pedDescription");
		portablityMap.field("familySize", "familySize");
		portablityMap.field("remarks", "remarks");
		portablityMap.field("requestId", "requestId");
		portablityMap.field("siFirst", "siFist");
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
		portablityMap.field("policyStartDate", "policyStrStartDate");
		portablityMap.field("dob", "strDateOfBirth");
		portablityMap.field("memEntryDate", "strMemberEntryDate");
		portablityMap.register();

		
		/**
		 *
		 * Below code was Added as part of CR R1080
		 * Portability Previous Policy Details
		 *  
		 */
		portablityPrevPolicyMap.field("amount", "amount");
		portablityPrevPolicyMap.field("cummulativeBonus", "cummulativeBonus");
		portablityPrevPolicyMap.field("customerId", "customerId");
		portablityPrevPolicyMap.field("exclusion_1stYr", "exclusion_1stYr");
		portablityPrevPolicyMap.field("exclusion_2ndYr", "exclusion_2ndYr");
		portablityPrevPolicyMap.field("insurerName", "insurerName");
		portablityPrevPolicyMap.field("natureofIllness", "natureofIllness");
		portablityPrevPolicyMap.field("noofClaims", "noofClaims");
		portablityPrevPolicyMap.field("pedDetails", "pedDetails");
		portablityPrevPolicyMap.field("pedWaiver", "pedWaiver");
		portablityPrevPolicyMap.field("policyFmDt", "policyStrFmDt");
		portablityPrevPolicyMap.field("policyNumber", "policyNumber");
		portablityPrevPolicyMap.field("policyToDt", "policyStrToDt");
		portablityPrevPolicyMap.field("pedWaiver", "pedWaiver");
		portablityPrevPolicyMap.field("policyFmDt", "policyStrFmDt");   // ""policyFmDt");
		portablityPrevPolicyMap.field("policyNumber", "policyNumber");
		portablityPrevPolicyMap.field("policyToDt", "policyStrToDt");  //"policyToDt");
		portablityPrevPolicyMap.field("policyType", "policyType");
		portablityPrevPolicyMap.field("productName", "productName");
		portablityPrevPolicyMap.field("sumInsured", "sumInsured");
		portablityPrevPolicyMap.field("uwYear", "uwYear");
		portablityPrevPolicyMap.field("waiver30Days", "waiver30Days");
		portablityPrevPolicyMap.field("year", "year");
		portablityPrevPolicyMap.register();
		
		
		bankDetailsMap.field("nameAsPerBank", "customerName");
		bankDetailsMap.field("accountNumber", "accountNumber");
		bankDetailsMap.field("accountType", "accountType");
		bankDetailsMap.field("bankName", "bankName");
		bankDetailsMap.field("effectiveFrom", "strEffectiveFrom");
		bankDetailsMap.field("effectiveTo", "strEffectiveTo");
		bankDetailsMap.field("ifscCode", "ifscCode");
		bankDetailsMap.field("others", "othersDetails");
		bankDetailsMap.field("branchName", "branchName");
		bankDetailsMap.register();
		
		benefitMap.field("amountPerUnit", "amountPerUnit");
		benefitMap.field("benefitBasedOn", "benefitBasedOn");
		benefitMap.field("benefitCode", "benefitCode");
		benefitMap.field("benefitDesc", "benefitDescription");
		benefitMap.field("benefitLongDescription", "benefitLongDescription");
		benefitMap.field("claimMaximumAmount", "claimMaxAmount");
		benefitMap.field("claimPercentage", "claimPercentage");
		benefitMap.field("flatAmountPerClaim", "flatAmountPerClaim");
		benefitMap.field("flatAmountPerPolicyPeriod", "flatAmountPerPolicyPeriod");
		benefitMap.field("noOfUnits", "numberOfUnits");
		benefitMap.field("remarks", "remarks");
		benefitMap.field("siMaximumAmount", "siMaxAmount");
		benefitMap.field("siPercentage", "siPercentage");
		benefitMap.field("serialNumber", "srNumber");
		benefitMap.field("subBenefitDescription", "subBenefitDescription");
		benefitMap.field("withInSI", "withInSi");
		benefitMap.register();
		
		ailmentLimit.field("ailment", "ailment");
		ailmentLimit.field("limitPerClaim", "limitPerClaim");
		ailmentLimit.field("limitPerEye", "limitPerEye");
		ailmentLimit.field("limitPerFamily", "limitPerFamily");
		ailmentLimit.field("limitPerPolicy", "limitPerPolicy");
		ailmentLimit.field("limitPerRisk", "limitPerRisk");
		ailmentLimit.field("sumInsuredFrom", "sumInsuredFrom");
		ailmentLimit.field("sumInsuredTo", "sumInsuredTo");
		ailmentLimit.register();
		
		coPayLimit.field("ageType", "ageType");
		coPayLimit.field("claimType", "claimType");
		coPayLimit.field("copayPercentage", "copayPercentage");
		coPayLimit.field("endNoIdx", "endNoIndex");
		coPayLimit.field("relationType", "relType");
		coPayLimit.field("sumInsuredFrom", "sumInsuredFrom");
		coPayLimit.field("sumInsuredTo", "sumInsuredTo");
		coPayLimit.field("ageFrom", "ageFrom");
		coPayLimit.field("ageTo", "ageTo");
		coPayLimit.register();
		
		deliveryLimit.field("deliveryType", "deliveryType");
		deliveryLimit.field("endNoIdx", "endNoIndex");
		deliveryLimit.field("limitAmount", "limitAmount");
		deliveryLimit.field("sumInsuredFrom", "sumInsuredFrom");
		deliveryLimit.field("sumInsuredTo", "sumInsuredTo");
		deliveryLimit.register();
		
		prePostLimit.field("hospType", "hospitalType");
		prePostLimit.field("limitAmount", "limitAmount");
		prePostLimit.field("limitPercentage", "limitPercentage");
		prePostLimit.field("noOfDays", "noOfDays");
		prePostLimit.field("sumInsuredFrom", "sumInsuredFrom");
		prePostLimit.field("sumInsuredTo", "sumInsuredTo");
		prePostLimit.register();
		
		roomRentLimit.field("limitAmount", "limitAmount");
		roomRentLimit.field("limitPercentage", "limitPercentage");
		roomRentLimit.field("nrLimit", "nrLimit");
		roomRentLimit.field("sumInsuredFrom", "sumInsuredFrom");
		roomRentLimit.field("sumInsuredTo", "sumInsuredTo");
		roomRentLimit.field("roomRentType", "roomType");
		roomRentLimit.field("proportionateFlag", "proportionateFlag");
		roomRentLimit.field("charges", "charges");
		roomRentLimit.register();
		
		/*documentDetailsMap.field("docName", "sfFileName");
		documentDetailsMap.field("urlId", "documentUrl");
		documentDetailsMap.field("description", "fileName");
		documentDetailsMap.field("ecmUrl", "documentUrl");
		documentDetailsMap.register();*/
		
		insuredNomineeMap.field("nomineeAge","nomineeAge");
		insuredNomineeMap.field("nomineeName","nomineeName");
		insuredNomineeMap.field("nomineeRelation","relationshipWithProposer");
		insuredNomineeMap.field("appointeeAge","appointeeAge");
		insuredNomineeMap.field("appointeeName","appointeeName");
		insuredNomineeMap.field("appointeeRelationship","appointeeRelationship");
		
		insuredNomineeMap.field("nomineePercentage","sharePercent");
		insuredNomineeMap.field("nomineeRelationship","relationshipWithProposer");
		insuredNomineeMap.field("nomineeDob","strNomineeDOB");	

		insuredNomineeMap.field("ifscCode","IFSCcode");
		insuredNomineeMap.field("accountNumber","accountNumber");
		insuredNomineeMap.field("beneficiaryName","nameAsPerBank");
		

		insuredNomineeMap.register();
		
		
		gmcContBenMap.field("year","policyYr");
		gmcContBenMap.field("insuredName","insuredName");
		gmcContBenMap.field("policyNo","policyNo");
		gmcContBenMap.field("polFromDate","policyFromDate");
		gmcContBenMap.field("polToDate","policyToDate");
		gmcContBenMap.field("inceptionDate","inceptionDate");
		gmcContBenMap.field("waiver30Days","waiver30Days");
		gmcContBenMap.field("exclusion1Yr","exclusion1Yr");
		gmcContBenMap.field("exclusion2Yr","exclusion2Yr");	
		gmcContBenMap.field("pedWaiver","pedWaiver");
		
		gmcContBenMap.register();
		
		corpBufferLimit.field("familySiType", "familySiType");
		corpBufferLimit.field("limitAmount", "limitAmount");
		corpBufferLimit.field("limitApplicable", "limitApplicable");
		corpBufferLimit.field("noSiType", "noSiType");
		corpBufferLimit.field("sumInsuredFrom", "sumInsuredFrom");
		corpBufferLimit.field("sumInsuredTo", "sumInsuredTo");
		corpBufferLimit.field("totalNoSI", "totalNoSi");
		corpBufferLimit.register();
		
		
		mapperFactory.getConverterFactory().registerConverter(new CustomConverter<String, Double>() {

			@Override
			public Double convert(String source, Type<? extends Double> destinationType,
					MappingContext arg2) {
				// TODO Auto-generated method stub
				source = source.equals("") || source.isEmpty() || source == "" || source == null ? "0.0" : source;
				//	source = source.replace("[a-zA-Z\\]", "");
				return new Double(source);
			}
		});

		mapperFactory.getConverterFactory().registerConverter(new CustomConverter<String, Long>() {
			@Override
			public Long convert(String source,
					Type<? extends Long> destinationType,
					MappingContext mappingContext) {
				source = source.equals("") || source.isEmpty() || source == "" || source == null ? "0" : source;
				//	source = source.replace("[a-zA-Z\\]", "");
				return new Long(source);
			}

			
		});
		
		mapperFactory.getConverterFactory().registerConverter(new CustomConverter<String, Integer>() {

			@Override
			public Integer convert(String source,
					Type<? extends Integer> destinationType,
					MappingContext mappingContext) {
				source = source.equals("") || source.isEmpty() || source == "" || source == null ? "0" : source;
				//	source = source.replace("[a-zA-Z\\]", "");
				return new Integer(source);
			}

			
		});
	
		tableMapper = mapperFactory.getMapperFacade();
		policyMapper = mapperFactory.getMapperFacade(PremPolicyDetails.class, Policy.class);
		endorsementMapper = mapperFactory.getMapperFacade(PremEndorsementDetails.class, PolicyEndorsementDetails.class);
		insuredMapper = mapperFactory.getMapperFacade(PremInsuredOMPDetails.class, Insured.class);
		
	}
	
	@SuppressWarnings("unused")
	public static  Policy getPolicyFromPremia(PremPolicyDetails premPolicyDetails)
	{
		System.out.println("POLICY MAPPER CREATED -------------------------------> " +policyMapper);
 		Policy policy = policyMapper.map(premPolicyDetails);	
		return policy;
	}
	
	@SuppressWarnings("unused")
	public static  List<Insured> getInsuredFromPremia(List<PremInsuredDetails> premInsuredDetails)
	{
		List<Insured> mapAsList = tableMapper.mapAsList(premInsuredDetails, Insured.class);
		return mapAsList;
	}
	
	@SuppressWarnings("unused")
	public static Insured getOMPInsuredFromPremia(PremInsuredOMPDetails premInsuredDetails)
	{
		Insured insured = insuredMapper.map(premInsuredDetails);
		return insured;
	}
	
	@SuppressWarnings("unused")
	public static  List<PortablitiyPolicyDTO> getPortablityDetails(List<PortablityPolicy> portablityPolicy)
	{
		List<PortablitiyPolicyDTO> mapAsList = tableMapper.mapAsList(portablityPolicy, PortablitiyPolicyDTO.class);
		return mapAsList;
	}
	
	/**
	 *
	 * Below code was Added as part of CR R1080
	 * Portability Previous Policy Details
	 *  
	 */
	public static  List<PortablitiyPolicyDTO> getPortablityPrevPolicyDetails(List<PortabilityPreviousPolicy> portablityPrevPolicy)
	{
		List<PortablitiyPolicyDTO> mapAsList = tableMapper.mapAsList(portablityPrevPolicy, PortablitiyPolicyDTO.class);
		return mapAsList;
	}
	
	
	public static  List<Insured> getInsuredPAFromPremia(List<PremiaInsuredPA> premInsuredDetails)
	{
		List<Insured> mapAsList = tableMapper.mapAsList(premInsuredDetails, Insured.class);
		return mapAsList;
	}
	
	@SuppressWarnings("unused")
	public static  List<PolicyBankDetails> getBankDetailsFromPremia(List<PremBankDetails> premInsuredDetails)
	{
		List<PolicyBankDetails> mapAsList = tableMapper.mapAsList(premInsuredDetails, PolicyBankDetails.class);
		return mapAsList;
	}
	
	@SuppressWarnings("unused")
	public static  List<InsuredPedDetails> getInsuredPedFromPremia(List<PremPEDDetails> premPedDetails)
	{
		List<InsuredPedDetails> mapAsList = tableMapper.mapAsList(premPedDetails, InsuredPedDetails.class);
		return mapAsList;
	}
	
	@SuppressWarnings("unused")
	public static  List<PortablityPolicy> getPortablityList(List<PremPortability> premPedDetails)
	{
		List<PortablityPolicy> mapAsList = tableMapper.mapAsList(premPedDetails, PortablityPolicy.class);
		return mapAsList;
	}
	
	/**
	 * Below code was Added as part of CR R1080
	 *  
	 */
	@SuppressWarnings("unused")
	public static  List<PortabilityPreviousPolicy> getPortablityPrevPolicyList(List<PremPortabilityPrevPolicyDetails> premPortabilityPrevPolDetails)
	{
		List<PortabilityPreviousPolicy> mapAsList = tableMapper.mapAsList(premPortabilityPrevPolDetails, PortabilityPreviousPolicy.class);
		return mapAsList;
	}
	
	public static List<PolicyEndorsementDetails> getPolicyEndorsementDetailsFromPremia(List<PremEndorsementDetails> premEndorsementDetails)
	{
		List<PolicyEndorsementDetails> policyEndorsementDetailsList = tableMapper.mapAsList(premEndorsementDetails,PolicyEndorsementDetails.class);
		return policyEndorsementDetailsList;
	}
	
	public static List<NomineeDetails> getInsuredNomineeDetails(List<PremInsuredNomineeDetails> premNomineeDetails)
	{
		List<NomineeDetails> nomineeList = tableMapper.mapAsList(premNomineeDetails,NomineeDetails.class);
		return nomineeList;
	}

    public static PolicyEndorsementDetails getPolicyEndorsementFromPremia(PremEndorsementDetails premEndorsementDetails){
    	PolicyEndorsementDetails endorsementDetails = endorsementMapper.map(premEndorsementDetails);
    	return endorsementDetails;
    }
	
	public static List<PreviousPolicy> getPreviousPolicyDetailsFromPremia(List<PremPreviousPolicyDetails> premPreviousPolicyDetails)
	{
		List<PreviousPolicy> previousPolicy = tableMapper.mapAsList(premPreviousPolicyDetails,PreviousPolicy.class);
		return previousPolicy;
	}
	
	@SuppressWarnings("unused")
	public static  List<GpaBenefitDetails> getGpaBenefitDetails(List<PremGpaBenefitDetails> premGpaBenefitDetails)
	{
		List<GpaBenefitDetails> mapAsList = tableMapper.mapAsList(premGpaBenefitDetails, GpaBenefitDetails.class);
		return mapAsList;
	}
	public static List<MasAilmentLimit> getAilmentLimit(List<PremGmcAilmentLimit> premAilmentLimit)
	{
		List<MasAilmentLimit> ailmentList = tableMapper.mapAsList(premAilmentLimit,MasAilmentLimit.class);
		return ailmentList;
	}
	
	public static List<MasCopayLimit> getCopayLimit(List<PremGmcCopayLimit> premCopayLimit)
	{
		List<MasCopayLimit> copayLimit = tableMapper.mapAsList(premCopayLimit,MasCopayLimit.class);
		return copayLimit;
	}
	
	public static List<MasDeliveryExpLimit> getDeliveryExpLimits(List<PremDeliveryExpLimit> premDeliveryLimit)
	{
		List<MasDeliveryExpLimit> deliveryLimit = tableMapper.mapAsList(premDeliveryLimit,MasDeliveryExpLimit.class);
		return deliveryLimit;
	}
	
	public static List<MasPrePostHospLimit> getPrepostLimit(List<PremGmcPrePostLimit> premPrePostLimit)
	{
		List<MasPrePostHospLimit> prePostLimit = tableMapper.mapAsList(premPrePostLimit,MasPrePostHospLimit.class);
		return prePostLimit;
	}
	
	public static List<MasRoomRentLimit> getRoomRentLimit(List<PremGmcRoomRentLimit> premRoomRentList)
	{
		List<MasRoomRentLimit> roomRentList = tableMapper.mapAsList(premRoomRentList,MasRoomRentLimit.class);
		return roomRentList;
	}
	
	
	@SuppressWarnings("unused")
	public static  List<Insured> getInsuredFromPremiaDepenedent(List<PremDependentInsuredDetails> premInsuredDetails)
	{
		List<Insured> mapAsList = tableMapper.mapAsList(premInsuredDetails, Insured.class);
		return mapAsList;
	}
	/*public static DocumentDetails getDocumnetDetailsApi(DocumentDetailsApi documentDetailsApi) {
		DocumentDetails docDetails = documentDetailsMapper.map(documentDetailsApi);
		return docDetails;
	}*/
	
	public static List<PolicyNominee> getProposerInsuredNomineeDetails(List<PremInsuredNomineeDetails> premNomineeDetails)
	{
		List<PolicyNominee> nomineeList = tableMapper.mapAsList(premNomineeDetails,PolicyNominee.class);
		return nomineeList;
	}
	
	public static List<GmcContinuityBenefit> getGMCInsuredContBenDetails(List<PremGMCContinuityBenefits> premContBenDetails)
	{
		List<GmcContinuityBenefit> contBenList = tableMapper.mapAsList(premContBenDetails,GmcContinuityBenefit.class);
		return contBenList;
	}
	
	public static List<GmcCoorporateBufferLimit> getCorpBufferLimit(List<PremGmcCorpBufferLimit> premCorpBufferList)
	{
		List<GmcCoorporateBufferLimit> corpBufferLimitList = tableMapper.mapAsList(premCorpBufferList,GmcCoorporateBufferLimit.class);
		return corpBufferLimitList;
	}
	
	public static PremiaToPolicyMapper getInstance(){
        if(myObj == null){
            myObj = new PremiaToPolicyMapper();
            getAllMapValues();
        }
        return myObj;
	 }

}

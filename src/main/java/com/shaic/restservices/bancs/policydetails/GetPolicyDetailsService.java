package com.shaic.restservices.bancs.policydetails;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.shaic.claim.policy.search.ui.PremInsuredDetails;
import com.shaic.claim.policy.search.ui.PremInsuredNomineeDetails;
import com.shaic.claim.policy.search.ui.PremPolicyCoverDetails;
import com.shaic.claim.policy.search.ui.PremPreviousPolicyDetails;
import com.shaic.domain.Insured;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyCoverDetails;
import com.shaic.domain.PolicyNominee;
import com.shaic.domain.PreviousPolicy;
import com.shaic.restservices.bancs.claimprovision.policydetail.PolicyDetailsRequest;
import com.shaic.restservices.bancs.claimprovision.policydetail.PolicyDetailsResponse;

@Path("/policy")
@Stateless
public class GetPolicyDetailsService {
	
	@Inject
	GetPolicyDetailsDBService DbService;

	static SimpleDateFormat df = new SimpleDateFormat("dd/mm/yyyy HH:mm:ss");
	private static final Logger log = LoggerFactory.getLogger(GetPolicyDetailsService.class);
	@POST
    @Path("/getPolicyDetails")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public  void getPolicyDetailsMethod(String policyNumber) {
		String responseString = "";
		policyNumber = "012HFHOSTD401386878000000";
		PolicyDetailsResponse response = null;
		try {

			String serviceURL =
					"http://10.20.2.10:8080/IIMS/rest/webservice/underwrite/";
			if(!serviceURL.endsWith("/")){ 
				serviceURL = serviceURL+"/"+"getPolicyDetails"; 
			}else{
				serviceURL = serviceURL+"getPolicyDetails"; 
			} 
			URL url = new URL(serviceURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST"); 
			conn.setRequestProperty("Content-Type","application/json"); 
			conn.setRequestProperty("Accept", "application/json");

			PolicyDetailsRequest request = new PolicyDetailsRequest();
			request.setBusinessChannel("LABPORTAL"); 
			request.setUserCode("T966297");
			request.setRoleCode("SUPERUSER");
			request.setPolicyNumber(policyNumber);

			Gson gson = new Gson(); 
			String requestString = gson.toJson(request);
			log.info("Request To Policy Details: "+requestString);
			conn.setDoOutput(true); 
			
			OutputStream os = conn.getOutputStream();
			os.write(requestString.toString().getBytes()); 
			os.flush(); 
			os.close(); 
			
			if(conn.getResponseCode() != 200) { 
				throw new RuntimeException("Request Failed : HTTP Error code : "+conn.getResponseCode()); 
			}else if (conn.getResponseCode() == 200) {
				InputStreamReader in = new InputStreamReader(conn.getInputStream());
				BufferedReader br = new BufferedReader(in);
				
				String output = "";
				while ((output = br.readLine()) != null) { 
					JSONObject jsonObject = new	JSONObject(output); 
					responseString = responseString+jsonObject.toString();
					log.info("Response :"+responseString);				
				} 
				
				Any policyDetailResponse = JsonIterator.deserialize(responseString);

				getPolicyDetails(policyDetailResponse);

				getPolicyCoverDetails(policyDetailResponse);

				getPolicyNomineeDetails(policyDetailResponse);

				getPreviousPolicyDetails(policyDetailResponse);

				getPolicyinsuredDetails(policyDetailResponse);
				
				conn.disconnect();

			}
		}catch (Exception exp) {
			log.error("Exception occurred while calling the provision service" + exp.getMessage());
			exp.printStackTrace();
		}
	}

	private static String readLineByLine(String filePath) {
		StringBuilder contentBuilder = new StringBuilder();
		try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
			stream.forEach(s -> contentBuilder.append(s).append("\n"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return contentBuilder.toString();
	}

	public  void getPolicyCoverDetails(Any policyDetailResponse) throws ParseException {

		List<Any> policycoverDetail = policyDetailResponse.get("policyDetails").get("policyProperty")
				.get("multiSetProperty").asList().stream()
				.filter(a -> a.get("multiSetName").toString().equalsIgnoreCase("Cover SI Details"))
				.collect(Collectors.toList());

		Iterator<Any> policycoverDetailIterator = policycoverDetail.get(0).get("multiSetDetail").iterator();
		System.out.println(policycoverDetail);
		System.out.println("****************************************************************");
		int l = 0;
		while (policycoverDetailIterator.hasNext()) {
			PremPolicyCoverDetails PremPolicyCoverDetails=new PremPolicyCoverDetails();
			Any any5 = (Any) policycoverDetailIterator.next();
			Map<String, Any> collect2 = any5.get("property").asList().stream().collect(Collectors.toMap(
					x -> (x.get("paramName").toString().replaceAll("[\\-\\+\\.\\^\\:\\,\\ \\(\\)]", "").toLowerCase()),
					x -> x.get("paramValue")));
			System.out.println(collect2.get("covercode"));
			System.out.println(collect2.get("coversuminsured"));
			PremPolicyCoverDetails.setCoverCode(collect2.get("covercode").toString());
//			PremPolicyCoverDetails.setCoverDescription(collect2.get("CoverDesc").toString());
//			PremPolicyCoverDetails.setRiskId(collect2.get("RiskID").toString());
			PremPolicyCoverDetails.setSumInsured(collect2.get("coversuminsured").toString());
			PolicyCoverDetails setPolicyCoverDetailsValue = DbService.SetPolicyCoverDetailsValue(PremPolicyCoverDetails);
		}
	}
	public void getPolicyinsuredDetails(Any policyDetailResponse) throws ParseException {
		System.out.println("****************************************************************");
		List<Any> policyInsuredDetail = policyDetailResponse.get("policyDetails").get("insuredDetails").asList().stream()
				.collect(Collectors.toList());
		Iterator<Any> policyInsuredDetaillist = policyInsuredDetail.iterator();
	    while (policyInsuredDetaillist.hasNext()) {
	    	 String ageinyears="";
			 String memberAgeAtEntry="";
			 String coPayPercentage="";
			 String deductible="";
			 String bonusforHealth="";
			 String relationshipwithProposer="";
			 String ped="";
			 String riskSumInsuredinBaseCurrency="";
			 String healthidcardno="";
			 String dateofBirth="";
			 String gender="";
	    	 String insuredName="";
	    	 String partyCode="";
	    	 PremInsuredDetails premInsuredDetails=new PremInsuredDetails();
	    Any policyInsuredDetailData=(Any) policyInsuredDetaillist.next();
		List<Any> policyriskDetail = policyInsuredDetailData.get("riskProperty").get("simpleProperty").asList().stream().collect(Collectors.toList());
       Iterator<Any> riskDetailIterator = policyriskDetail.iterator();
       Any insuredMemberDetails = policyInsuredDetailData.get("memberDetails");
       System.out.println("****************************************************************");
       if(insuredMemberDetails !=null)
       {
       	dateofBirth = insuredMemberDetails.get("dateOfBirth").toString();
       	System.out.println("dateOfBirth" +":"+dateofBirth);
        premInsuredDetails.setDob(dateofBirth);
       	gender = insuredMemberDetails.get("gender").toString();
       	System.out.println("gender" +":"+gender);
       	premInsuredDetails.setGender(gender);
       	 String firstName = insuredMemberDetails.get("firstName").toString();
       	 String middleName = insuredMemberDetails.get("middleName").toString();
       	 String lastName = insuredMemberDetails.get("lastName").toString();
      
       	 if(firstName != null && !firstName.isEmpty())
       	 {
       		 insuredName = insuredName + firstName +" ";
       	 }
       	 if(middleName != null && !middleName.isEmpty())
           {
           	insuredName = insuredName + middleName +" ";
           }
       	 if(lastName != null && !lastName.isEmpty())
       	 {
           	insuredName = insuredName + lastName +" "; 
       	 }
      	 System.out.println("InsuredName" +":"+insuredName);
      	 premInsuredDetails.setInsuredName(insuredName);
      	 partyCode = insuredMemberDetails.get("partyCode").toString();
      	 System.out.println("partyCode" +":"+partyCode);
      	premInsuredDetails.setRiskSysId(partyCode);
   	}
   	System.out.println("****************************************************************");
		while (riskDetailIterator.hasNext()) {
			Any any5 = (Any) riskDetailIterator.next();
			 String ParamName = any5.get("paramName").toString().replaceAll("[\\-\\+\\.\\^\\:\\,\\ \\(\\)]", "").toLowerCase();
			 switch(ParamName)
			 {
			 case "ageinyears":
				 ParamName = any5.get("paramName").toString();
				  ageinyears=any5.get("paramValue").toString();
				 System.out.println(ParamName +":"+ageinyears);
				 premInsuredDetails.setInsuredAge(ageinyears);
	            break;
			 case "memberageatentry":
				 ParamName = any5.get("paramName").toString();
				 memberAgeAtEntry=any5.get("paramValue").toString();
				 System.out.println(ParamName +":"+memberAgeAtEntry);
				 premInsuredDetails.setEntryAge(memberAgeAtEntry);
	            break;
			 case "copaypercentage":    
				 ParamName = any5.get("paramName").toString();
				  coPayPercentage=any5.get("paramValue").toString();
				 System.out.println(ParamName +":"+coPayPercentage);
				 premInsuredDetails.setCoPay(coPayPercentage);
				 break;
			 case "deductible":    
				 ParamName = any5.get("paramName").toString();
				  deductible=any5.get("paramValue").toString();
				 System.out.println(ParamName +":"+deductible);
				 premInsuredDetails.setDeductiableAmt(deductible);
				 break;
			 case "bonusforhealth":    
				 ParamName = any5.get("paramName").toString();
				  bonusforHealth=any5.get("paramValue").toString();
				 System.out.println(ParamName +":"+bonusforHealth);
				 premInsuredDetails.setCumulativeBonus(bonusforHealth);
				 break;		 
			 case "relationshipwithproposer":    
				 ParamName = any5.get("paramName").toString();
				  relationshipwithProposer=any5.get("paramValue").toString();
				 System.out.println(ParamName +":"+relationshipwithProposer);
				 premInsuredDetails.setRelation(relationshipwithProposer);
				 break;	
			 case "ped":
				 ParamName = any5.get("paramName").toString();
				  ped=any5.get("paramValue").toString();
				 System.out.println(ParamName +":"+ped);
				 premInsuredDetails.setPedCoPay(ped);
				 break;
			 case "risksuminsuredinbasecurrency":
				 ParamName = any5.get("paramName").toString();
				 riskSumInsuredinBaseCurrency=any5.get("paramValue").toString();
				 System.out.println(ParamName +":"+riskSumInsuredinBaseCurrency);
				 premInsuredDetails.setSumInsured(riskSumInsuredinBaseCurrency);
				 break;	
			 case "healthidcardno":
				 ParamName = any5.get("paramName").toString();
				 healthidcardno=any5.get("paramValue").toString();
				 System.out.println(ParamName +":"+healthidcardno);
				 premInsuredDetails.setHealthCardNo(healthidcardno);
				 break;		 
			 }
			 Insured setPolicyInsuredDetailsValue = DbService.SetPolicyInsuredDetailsValue(premInsuredDetails);
		}
	System.out.println("****************************************************************");
	}
	}
	
	public void getPolicyNomineeDetails(Any policyDetailResponse) throws ParseException {

		List<Any> policyPropertyMSDetail = policyDetailResponse.get("policyDetails").get("policyProperty")
				.get("multiSetProperty").asList().stream()
				.filter(a -> a.get("multiSetName").toString().equalsIgnoreCase("Nominee Detail"))
				.collect(Collectors.toList());

		Iterator<Any> nomineeDetailIterator = policyPropertyMSDetail.get(0).get("multiSetDetail").iterator();
		
		System.out.println("****************************************************************");

		int l = 0;
		while (nomineeDetailIterator.hasNext()) {
			Any any5 = (Any) nomineeDetailIterator.next();
			Map<String, Any> collect2 = any5.get("property").asList().stream().collect(Collectors.toMap(
					x -> (x.get("paramName").toString().replaceAll("[\\-\\+\\.\\^\\:\\,\\ \\(\\)]", "").toLowerCase()),
					x -> x.get("paramValue")));
			System.out.println(collect2);	
			PremInsuredNomineeDetails premInsuredNomineeDetails = new PremInsuredNomineeDetails();
			premInsuredNomineeDetails.setAppointeeAge(collect2.get("appointeeage").toString());
	
			premInsuredNomineeDetails.setAppointeeName(collect2.get("appointeename").toString());
			premInsuredNomineeDetails.setAppointeeRelationship(collect2.get("appointeerelationship").toString());
			premInsuredNomineeDetails.setNomineeAge(collect2.get("age").toString());
			premInsuredNomineeDetails.setNomineeDob(collect2.get("dateofbirth").toString());
			premInsuredNomineeDetails.setNomineeName(collect2.get("nomineename").toString());
			premInsuredNomineeDetails.setNomineePercentage(collect2.get("nominee%").toString());
			premInsuredNomineeDetails.setNomineeRelationship(collect2.get("relationshipwithproposer").toString());
			PolicyNominee setPolicyNomineeDetails = DbService.SetPolicyNomineeDetails(premInsuredNomineeDetails);

		}
//		Map<Any, Any> collect = policyDetailResponse.get("policyDetails").get("policyProperty").get("multiSetProperty").asList().stream().
//				filter(a->a.get("multiSetName").toString().equalsIgnoreCase("Nominee Detail")).collect(Collectors.toMap(a->(a.get("multiSetDetail",0).get("property").get("paramName")),(a->a.get("multiSetDetail",0).get("property").get("paramValue"))));
//		System.out.println(collect);
	}
	
	 public  void getPreviousPolicyDetails(Any policyDetailResponse) throws ParseException {
		 List<Any> policyPropertyMSDetail = policyDetailResponse.get("policyDetails").get("previousPolicyDetails").asList().stream().collect(Collectors.toList());
   
		 PremPreviousPolicyDetails premPreviousPolicyDetails = new PremPreviousPolicyDetails();	 
		 System.out.println(policyPropertyMSDetail);
		 System.out.println("****************************************************************");
		 String policyNumber = policyPropertyMSDetail.get(0).get("policyNumber").toString();
		 String productCode=policyPropertyMSDetail.get(0).get("productCode").toString();
		 String productName=policyPropertyMSDetail.get(0).get("productName").toString();
		 String policyFromDate=policyPropertyMSDetail.get(0).get("policyExpiryDate").toString();
		 String policyToDate=policyPropertyMSDetail.get(0).get("policyExpiryDate").toString();
		 String sumInsured=policyPropertyMSDetail.get(0).get("sumInsured").toString();
		 String totalPremium=policyPropertyMSDetail.get(0).get("totalPremium").toString();
		 System.out.println("policyNumber : "+policyNumber);
		 System.out.println("productCode : "+productCode);
		 System.out.println("productName : "+productName);
		 System.out.println("policyInceptionDate : "+policyFromDate);
		 System.out.println("policyExpiryDate : "+policyToDate);
		 System.out.println("sumInsured : "+sumInsured);
		 System.out.println("totalPremium :"+totalPremium);
		 premPreviousPolicyDetails.setPolicyNo(policyNumber);
		 premPreviousPolicyDetails.setProductCode(productCode);
		 premPreviousPolicyDetails.setProductName(productName);
		 premPreviousPolicyDetails.setPolicyFromDate(policyFromDate);
		 premPreviousPolicyDetails.setPolicyToDate(policyToDate);
		 premPreviousPolicyDetails.setSumInsured(sumInsured);
		 premPreviousPolicyDetails.setPremium(totalPremium);
		 PreviousPolicy setPreviousPolicyDetails = DbService.SetPreviousPolicyDetails(premPreviousPolicyDetails);
		 
		 }
	 
	 public void getPolicyDetails(Any policyDetailResponse) throws ParseException {
		 System.out.println("**************** IMS_CLS_POLICY **************");
		 Policy newPolicy = new Policy();
		 List<Any> agentDetails = policyDetailResponse.get("policyDetails").get("policyRelation").asList()
		 .stream()
		 .filter(j->j.get("stakeCode").toString().equalsIgnoreCase("AGENT"))
		 .collect(Collectors.toList());

		 System.out.println("Agent Code: "+agentDetails.get(0).get("partyCode").toString());
		 System.out.println("Agent Name: "+agentDetails.get(0).get("partyName").toString());


		 List<Any> bonusDetail = policyDetailResponse.get("policyDetails").get("policyProperty").get("simpleProperty").asList()
		 .stream()
		 .filter(c->c.get("paramName").toString().equalsIgnoreCase("Bonus"))
		 .collect(Collectors.toList());
		 System.out.println("CumulativeBonus: "+bonusDetail.get(0).get("paramValue").toString());

		 List<Any> officeDetails = policyDetailResponse.get("policyDetails").get("officeDetails").get("officeAddress").asList()
		 .stream()
		 .filter(a-> a.get("addressType").toString().equalsIgnoreCase("Permanent")).collect(Collectors.toList());
		 System.out.println("District : "+ officeDetails.get(0).get("district").toString());

		 double totalPremium = policyDetailResponse.get("policyDetails").get("totalPremium").toDouble();
		 System.out.println("Total Premium : "+ totalPremium);

		 System.out.println("OfficeCode : "+ policyDetailResponse.get("policyDetails").get("officeDetails").get("officeCode").toString());

		 System.out.println("Policy Expiry Date : "+policyDetailResponse.get("policyDetails").get("policyExpiryDate").toString());

		 System.out.println("Policy Number : "+policyDetailResponse.get("policyDetails").get("policyNumber").toString());

		 System.out.println("Policy Inception Date : "+policyDetailResponse.get("policyDetails").get("policyInceptionDate").toString());

		 System.out.println("Policy Status : "+policyDetailResponse.get("policyDetails").get("policyStatus").toString());
		 System.out.println("Policy Duration : "+policyDetailResponse.get("policyDetails").get("policyDuration").toString());
		 System.out.println("Product Code : "+policyDetailResponse.get("policyDetails").get("productCode").toString());
		 System.out.println("Product Name : "+policyDetailResponse.get("policyDetails").get("productName").toString());

		 Iterator<Any> policyPropertySP = policyDetailResponse.get("policyDetails").get("policyProperty").get("simpleProperty").asList().iterator();
		 String bonus="";
		 String floaterSumInsured="";
		 String planType="";
		 String pan="";
		 String familySize="";
		 String zone="";

		 while(policyPropertySP.hasNext()) {
		 Any policySingleProperty = (Any) policyPropertySP.next();
		 // System.out.println("*****"+any5.get("paramName").toString().toLowerCase());
		 switch(policySingleProperty.get("paramName").toString().replaceAll("[\\-\\+\\.\\^\\:\\,\\ \\(\\)]", "").toLowerCase()) {
		 case "bonus":
		 bonus=policySingleProperty.get("paramValue").toString();
		 System.out.println("Bonus: "+bonus);
		 break;
		 case "floatersuminsured":
		 floaterSumInsured=policySingleProperty.get("paramValue").toString();
		 System.out.println("Floater Sum Insured: "+floaterSumInsured);
		 break;
		 case "plantype":
		 planType=policySingleProperty.get("paramValue").toString();
		 System.out.println("Plan Type: "+planType);
		 break;
		 case "familysize":
		 familySize=policySingleProperty.get("paramValue").toString();
		 System.out.println("Family Size: "+familySize);
		 break;
		 case "zone":
		 zone=policySingleProperty.get("paramValue").toString();
		 System.out.println("Zone: "+zone);
		 break;
		 }
		 }

		 List<Any> proposerPermAddrDetails = policyDetailResponse.get("policyDetails").get("proposerDetails").get("partyAddress").asList()
		 .stream()
		 .filter(a-> a.get("addressType").toString().equalsIgnoreCase("Permanent")).collect(Collectors.toList());
		 String proposerName = policyDetailResponse.get("policyDetails").get("proposerDetails").get("firstName").toString()
		 +" "+ policyDetailResponse.get("policyDetails").get("proposerDetails").get("middleName").toString().replace("null", "")
		 +" "+ policyDetailResponse.get("policyDetails").get("proposerDetails").get("lastName").toString();
		 System.out.println("Proposer Address1: "+proposerPermAddrDetails.get(0).get("address1").toString());
		 System.out.println("Proposer Address2: "+proposerPermAddrDetails.get(0).get("address2").toString());
		 System.out.println("Proposer Address3: "+proposerPermAddrDetails.get(0).get("address3").toString());
		 System.out.println("Proposer Party Code: "+ policyDetailResponse.get("policyDetails").get("proposerDetails").get("partyCode").toString());
		 System.out.println("Proposer DOB: "+ policyDetailResponse.get("policyDetails").get("proposerDetails").get("dateOfBirth").toString());
		 System.out.println("Proposer Name: "+ proposerName);

		 System.out.println("Proposer Email ID: "+proposerPermAddrDetails.get(0).get("eMailId").toString());
		 System.out.println("Proposer Fax no: "+proposerPermAddrDetails.get(0).get("fax").toString());
		 System.out.println("Proposer Mobile No: "+proposerPermAddrDetails.get(0).get("mobileNo").toString());
		 System.out.println("State: "+proposerPermAddrDetails.get(0).get("state").toString());
		 System.out.println("SubDistrict: "+proposerPermAddrDetails.get(0).get("district").toString());

		 List<Any> proposerOfficeAddrDetails =
		 policyDetailResponse.get("policyDetails").get("proposerDetails").get(
		 "partyAddress").asList() .stream() .filter(a->
		 a.get("addressType").toString().equalsIgnoreCase("Office")).collect(
		 Collectors.toList());
		 if(proposerOfficeAddrDetails.size()>0) {
		 newPolicy.setPolOfficeAddr1(proposerOfficeAddrDetails.get
		 (0).get("address1").toString());
		 newPolicy.setPolOfficeAddr2(proposerOfficeAddrDetails.get
		 (0).get("address2").toString());
		 newPolicy.setPolOfficeAddr3(proposerOfficeAddrDetails.get
		 (0).get("address3").toString());
		 newPolicy.setPolEmailId(proposerOfficeAddrDetails.get
		 (0).get("address1").toString());
		 newPolicy.setPolFaxnumber(proposerOfficeAddrDetails.get
		 (0).get("fax").toString());
		 newPolicy.setOfficeTelephone(proposerOfficeAddrDetails.get
		 (0).get("phoneNo").toString());
		 System.out.println("Proposer Office Address1: "+proposerOfficeAddrDetails.get
		 (0).get("address1").toString());
		 System.out.println("Proposer Office Address2: "+proposerOfficeAddrDetails.get
		 (0).get("address2").toString());
		 System.out.println("Proposer Office Address3: "+proposerOfficeAddrDetails.get
		 (0).get("address3").toString());
		 System.out.println("Proposer Office Email ID: "+proposerOfficeAddrDetails.get
		 (0).get("eMailId").toString());
		 System.out.println("Proposer Office Fax no: "+proposerOfficeAddrDetails.get(0
		 ).get("fax").toString());
		 System.out.println("Proposer Office Tel No: "+proposerOfficeAddrDetails.get(0
		 ).get("phoneNo").toString());
		 System.out.println("Proposer Tel No: "+proposerOfficeAddrDetails.get(0).get(
		 "phoneNo").toString());
		 }
		 String proposerPanNo = policyDetailResponse.get("policyDetails").get("proposerDetails").get("partyProperty").get("simpleProperty").asList()
		 .stream().filter(x-> x.get("paramName").toString().equalsIgnoreCase("PAN")).collect(Collectors.toList()).get(0).get("paramValue").toString();
		 System.out.println("Proposer PAN No: "+ proposerPanNo);
		 System.out.println("Proposer Title: "+policyDetailResponse.get("policyDetails").get("proposerDetails").get("title").toString());
		 System.out.println("Receipt Generation Date: "+policyDetailResponse.get("policyDetails").get("policyPaymentInformation",0).get("receiptGenerationDate").toString());
		 System.out.println("Receipt Number: "+policyDetailResponse.get("policyDetails").get("policyPaymentInformation",0).get("receiptNumber").toString());

		 List<Any> smDetails = policyDetailResponse.get("policyDetails").get("policyRelation").asList()
		 .stream()
		 .filter(j->j.get("stakeCode").toString().equalsIgnoreCase("SM"))
		 .collect(Collectors.toList());

		 System.out.println("SM Code: "+smDetails.get(0).get("partyCode").toString());
		 System.out.println("SM Name: "+smDetails.get(0).get("partyName").toString());

		 Iterator<Any> endorserIterator = policyDetailResponse.get("policyDetails").get("endorsementDetails").asList().iterator();
		 while(endorserIterator.hasNext()) {
		 Any endorserDetail = (Any) endorserIterator.next();
		 System.out.println("Endorsement Effective Date: "+endorserDetail.get("endorsementEffectiveDate").toString());
		 System.out.println("Endorsement Number: "+endorserDetail.get("endorsementNumber").toString());
		 System.out.println("Endorsement Premium: "+endorserDetail.get("endorsementPremium").toString());
		 System.out.println("Endorsement Revised Sum Insured: "+endorserDetail.get("endorsementRevisedSumInsured").toString());
		 System.out.println("Endorsement Sum Insured: "+endorserDetail.get("endorsementSumInsured").toString());
		 System.out.println("Type of Endorsement: "+endorserDetail.get("typeofEndorsement").toString());
		 System.out.println("Remarks: "+endorserDetail.get("remarks").toString());
		 }



		 newPolicy.setAgentCode(agentDetails.get(0).get("partyCode").toString());
		 newPolicy.setAgentName(agentDetails.get(0).get("partyName").toString());
		 newPolicy.setCummulativeBonus(bonusDetail.get(0).get("paramValue").toDouble());
		 newPolicy.setGrossPremium(totalPremium);
//		 if(!isNullOrEmpty(policyDetailResponse.get("policyDetails").get("policyExpiryDate").toString()))
//		 {
//		// newPolicy.setPolicyToDate(df.parse(policyDetailResponse.get("policyDetails").get("policyExpiryDate").toString()));
//		 }
//		 else
//		 {
//			 newPolicy.setPolicyToDate(null);
//		 }
		 newPolicy.setPolicyNumber(policyDetailResponse.get("policyDetails").get("policyNumber").toString());
//		 if(!isNullOrEmpty(policyDetailResponse.get("policyDetails").get("policyInceptionDate").toString()))
//		 {
//		// newPolicy.setPolicyFromDate(df.parse(policyDetailResponse.get("policyDetails").get("policyInceptionDate").toString()));
//		 }
//		 else
//		 {
//			 newPolicy.setPolicyFromDate(null);
//		 }
		 newPolicy.setPolicyStatus(policyDetailResponse.get("policyDetails").get("policyStatus").toString());

		 newPolicy.setPolicyTerm(policyDetailResponse.get("policyDetails").get("policyDuration").toLong());
		 //newPolicy.setPolicyType(planType.toString());
		 newPolicy.setProposerAddress1(proposerPermAddrDetails.get(0).get("address1").toString());
		 newPolicy.setProposerAddress2(proposerPermAddrDetails.get(0).get("address2").toString());
		 newPolicy.setProposerAddress3(proposerPermAddrDetails.get(0).get("address3").toString());
		 newPolicy.setProposerCode(policyDetailResponse.get("policyDetails").get("proposerDetails").get("partyCode").toString());
////		 if(!isNullOrEmpty(policyDetailResponse.get("policyDetails").get("proposerDetails").get("dateOfBirth").toString()))
////		 {
////		 newPolicy.setProposerDob(df.parse(policyDetailResponse.get("policyDetails").get("proposerDetails").get("dateOfBirth").toString()));
////		 }
////		 else
////		 {
//			 newPolicy.setProposerDob(null);
//		 }
		 newPolicy.setPolEmailId(proposerPermAddrDetails.get(0).get("eMailId").toString());
		 newPolicy.setPolFaxnumber(proposerPermAddrDetails.get(0).get("fax").toString());
		 newPolicy.setRegisteredMobileNumber(proposerPermAddrDetails.get(0).get("mobileNo").toString());
		 newPolicy.setProposerFirstName(proposerName);

		 newPolicy.setProposedPanNumber(proposerPanNo);
		// newPolicy.setPolTelephoneNumber(proposerOfficeAddrDetails.get(0).get("phoneNo").toString());
		 newPolicy.setProposerTitle(policyDetailResponse.get("policyDetails").get("proposerDetails").get("title").toString());
//		 if(!isNullOrEmpty(policyDetailResponse.get("policyDetails").get("policyPaymentInformation",0).get("receiptGenerationDate").toString()))
//		 {
//		 newPolicy.setReceiptDate(df.parse(policyDetailResponse.get("policyDetails").get("policyPaymentInformation",0).get("receiptGenerationDate").toString()));
//		 }
//		 else
//		 {
//			 newPolicy.setReceiptDate(null);
//		 }
		 newPolicy.setReceiptNumber(policyDetailResponse.get("policyDetails").get("policyPaymentInformation",0).get("receiptNumber").toString());

		 newPolicy.setSmCode(smDetails.get(0).get("partyCode").toString());
		 newPolicy.setSmName(smDetails.get(0).get("partyName").toString());
		 newPolicy.setProposerState(proposerPermAddrDetails.get(0).get("state").toString());
		 newPolicy.setProposerSubDist(proposerPermAddrDetails.get(0).get("district").toString());
		 newPolicy.setPolicyZone(zone);
		 Policy setPolicyDetails = DbService.SetPolicyDetails(newPolicy);
		 }
	 
	 public static boolean isNullOrEmpty(String str) {
	        if(str != null && !str.isEmpty())
	        {
	            return false;
	        }
	        else
	        {
	        return true;
	        }
	 }
	
}
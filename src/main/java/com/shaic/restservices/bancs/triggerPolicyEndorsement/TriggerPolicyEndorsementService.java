package com.shaic.restservices.bancs.triggerPolicyEndorsement;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shaic.ims.bpm.claim.BPMClientContext;

public class TriggerPolicyEndorsementService {
	private static final Logger log = LoggerFactory.getLogger(TriggerPolicyEndorsementService.class);
	
	public String callTriggerEndorsement(){
	
		String responseString = "";
		TriggerPolicyEndorsementResponse response = null;
		ObjectMapper mapper = new ObjectMapper();
		
		try{
			
			String serviceURL = BPMClientContext.BANCS_WEB_SERVICE_UW_URL;
			if(!serviceURL.trim().endsWith("/")){
				serviceURL = serviceURL.trim()+"/"+"triggerPolicyEndorsement";
			}else{
				serviceURL = serviceURL.trim()+"triggerPolicyEndorsement";
			}
			System.out.println("Bancs Live - Url :"+serviceURL);
			URL url = new URL(serviceURL); 
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");


			
			/*ArrayList<TriggerPolicyEndorseNomineeDetails>listDetails=new ArrayList<TriggerPolicyEndorseNomineeDetails>();
			TriggerPolicyEndorseNomineeDetails nomineeDetails=new TriggerPolicyEndorseNomineeDetails();
			nomineeDetails.setOperationName("ADD");
			nomineeDetails.setSerialNo(1L);
			nomineeDetails.setUniqueNumber("");
			nomineeDetails.setUniqueNumber("");
			nomineeDetails.setRelationshipWithProposer("BIL");
			nomineeDetails.setDateOfBirth(null);
			nomineeDetails.setAge(1l);
			nomineeDetails.setNomineeShare(2.0);
			nomineeDetails.setAppointeeName("");
			nomineeDetails.setAppointeeAge(22l);
			nomineeDetails.setAppointeeRelationship("");
			nomineeDetails.setAadhaarNumberOfTheNominee(1l);
			nomineeDetails.setAccountHolderName("");
			nomineeDetails.setAccountNumber(null);
			nomineeDetails.setIfscCode("");
			nomineeDetails.setAccountType("");
			listDetails.add(nomineeDetails);
			
			
			ArrayList<TriggerPolicyEndorseNachMandates> listNachMandates=new ArrayList<TriggerPolicyEndorseNachMandates>();
			TriggerPolicyEndorseNachMandates nachMandates=new TriggerPolicyEndorseNachMandates();			
			nachMandates.setOperationName("");
			nachMandates.setMandateSiNo(null);
			nachMandates.setAcHolderName("");
			nachMandates.setBankName("");
			nachMandates.setBankBranchName("");
			nachMandates.setBankAccountNumber(null);
			nachMandates.setAccountType("");
			nachMandates.setNachLimitAmount(null);
			nachMandates.setMicrCode("");
			nachMandates.setIfscCode("");
			nachMandates.setMandateStartDate(null);
			nachMandates.setMandateEndDate(null);
			nachMandates.setApplicationNo("");
			nachMandates.setNachUrnNo("");
			nachMandates.setFrequency("");
			listNachMandates.add(nachMandates);
			
			ArrayList<TriggerPolicyEndorseJourneyDetails> listJourneyDetails=new ArrayList<TriggerPolicyEndorseJourneyDetails>();
			TriggerPolicyEndorseJourneyDetails journeyDetails=new TriggerPolicyEndorseJourneyDetails();
			journeyDetails.setOperationName("");
			journeyDetails.setSerialNo(null);
			journeyDetails.setDateOfJourney(null);
			journeyDetails.setNoOfTravelDays(null);
			journeyDetails.setEndDateOfJourney(null);
			journeyDetails.setPlaceOfTravel("");
			journeyDetails.setVisaType("");
			journeyDetails.setPurposeOfVisit("");
			journeyDetails.setCurrencyType("");
			journeyDetails.setCertificateNumber("");
			listJourneyDetails.add(journeyDetails);
			
			ArrayList<TriggerPolicyEndorsePreviousInsuranceDetails> listPreviousInsuranceDetails=new ArrayList<TriggerPolicyEndorsePreviousInsuranceDetails>();
			TriggerPolicyEndorsePreviousInsuranceDetails previousInsuranceDetails=new TriggerPolicyEndorsePreviousInsuranceDetails();
			previousInsuranceDetails.setOperationName("");
			previousInsuranceDetails.setSerialNo(null);
			previousInsuranceDetails.setUnderwritingYear(null);
			previousInsuranceDetails.setInsuranceCompanyCode(null);
			previousInsuranceDetails.setNameOfTheInsuranceCompany("");
			previousInsuranceDetails.setPolicyIssuingOfficeCode("");
			previousInsuranceDetails.setPolicyIssuingOfficeName("");
			previousInsuranceDetails.setNameOfTheProduct("");
			previousInsuranceDetails.setProductUinNo("");
			previousInsuranceDetails.setPolicyType("");
			previousInsuranceDetails.setPolicyNumber("");
			previousInsuranceDetails.setCustomerId("");
			previousInsuranceDetails.setRiskEffectiveDate(null);
			previousInsuranceDetails.setRiskExpiryDate(null);
			previousInsuranceDetails.setSumInsuredIndividual(null);
			previousInsuranceDetails.setCumulativeBonusIndividual(null);
			previousInsuranceDetails.setSumInsuredFloater(null);
			previousInsuranceDetails.setCumulativeBonusFloater(null);
			previousInsuranceDetails.setWhetherPedDeclared("");
			previousInsuranceDetails.setDetailsOfPedIcdCodes("");
			previousInsuranceDetails.setDetailsOfPed("");
			previousInsuranceDetails.setDaysCoverExclusion("");
			previousInsuranceDetails.setStYearExclusion("");
			previousInsuranceDetails.setNdYearExclusion("");
			previousInsuranceDetails.setPedExclusion("");
			previousInsuranceDetails.setNoOfClaims(null);
			previousInsuranceDetails.setTotalClaimAmountIncurred(null);
			previousInsuranceDetails.setNatureOfIllnessResultingTheClaim("");
			previousInsuranceDetails.setAnnualIncome(null);
			listPreviousInsuranceDetails.add(previousInsuranceDetails);
			
			
			TriggerPolicyEndorsementDTO triggerEndosmntrequest = new TriggerPolicyEndorsementDTO();			
			triggerEndosmntrequest.setBusinessChannel(SHAConstants.bChanel);
			triggerEndosmntrequest.setUserCode(SHAConstants.userCode);
			triggerEndosmntrequest.setRoleCode(SHAConstants.roleCode);
			triggerEndosmntrequest.setPolicyNumber("012HFHOSTD401386878000000");
			triggerEndosmntrequest.setTypeOfEndorsement("END082");
			triggerEndosmntrequest.setNomineeDetails(listDetails);
			triggerEndosmntrequest.setNachMandates(listNachMandates);
			triggerEndosmntrequest.setJourneyDetails(listJourneyDetails);
			triggerEndosmntrequest.setPreviousInsuranceDetails(listPreviousInsuranceDetails);
			

			
			String requestString = mapper.writeValueAsString(triggerEndosmntrequest);*/
			
			ArrayList<TriggerPolicyEndorseNomineeDetails>listDetails=new ArrayList<TriggerPolicyEndorseNomineeDetails>();
			TriggerPolicyEndorseNomineeDetails nomineeDetails=new TriggerPolicyEndorseNomineeDetails();
			/*nomineeDetails.setOperationName("ADD");
			nomineeDetails.setSerialNo(1L);*/
			
			nomineeDetails.setNomineeName("Aradhana");
			nomineeDetails.setRelationshipWithProposer("D");
			nomineeDetails.setDateOfBirth("29/01/1995");
			nomineeDetails.setAge("25");
			nomineeDetails.setNomineeShare("100");
			nomineeDetails.setAppointeeName("Rajini");
			nomineeDetails.setAppointeeAge("29");
			nomineeDetails.setAppointeeRelationship("Son");
			nomineeDetails.setAadhaarNumberOfTheNominee("416232703256");
			//listDetails.add(nomineeDetails);
			
			TriggerPolicyEndorsementDTO triggerEndosmntrequest = new TriggerPolicyEndorsementDTO();	
			triggerEndosmntrequest.setServiceTransactionId("1234");
			triggerEndosmntrequest.setBusinessChannel("GC"/*SHAConstants.bChanel*/);
			triggerEndosmntrequest.setUserCode("T1125041"/*SHAConstants.userCode*/);
			triggerEndosmntrequest.setRoleCode("UWRITER"/*SHAConstants.roleCode*/);
			triggerEndosmntrequest.setPolicyNumber("012HFHOSTD401386878000000");
			triggerEndosmntrequest.setTypeOfEndorsement("END054");
			//triggerEndosmntrequest.setNomineeDetails(listDetails);

			
			/*String serviceTransactionId="1234";
			String businessChannel="GC";
			String userCode="T1125041";
			String roleCode="UWRITER";
			String policyNumber="012HFHOSTD401388088000000";
			String typeOfEndorsement="END054";
			
			String sequenceNo="1";
			String operationCode="ADD";
			
			String nomineeName="Aradhana";
			String relationshipWithProposer="D";
			String dateofBirth="29/01/1995";
			String age="25";
			String nominee ="100";
			String appointeeName="Rajini";
			String appointeeAge="29";
			String appointeeRelationship="Son";
			String aadhaarNonominee="416232703256";*/
			
			String sequenceNo="1";
			String operationCode="ADD";

			//String requestString="{ \n\n   \"serviceTransactionId\":\""+serviceTransactionId+"\",\n\n   \"businessChannel\":\""+businessChannel+"\",\n\n   \"userCode\":\""+userCode+"\",\n\n   \"roleCode\":\""+roleCode+"\",\n\n   \"policyNumber\":\""+policyNumber+"\",\n\n   \"typeOfEndorsement\":\""+typeOfEndorsement+"\",\n\n   \"policyProperty\":{ \n\n      \"simpleProperty\":[ \n\n \n\n      ],\n\n      \"multiSetProperty\":[ \n\n         { \n\n            \"multiSetName\":\"Nominee Detail\",\n\n            \"multiSetDetail\":[ \n\n               { \n\n                  \"sequenceNo\":\""+sequenceNo+"\",\n\n                  \"operationCode\":\""+operationCode+"\",\n\n                  \"property\":[ \n\n                     { \n\n                        \"paramName\":\"Nominee Name\",\n\n                        \"paramValue\":\""+nomineeName+"\"\n\n                     },\n\n                     { \n\n                        \"paramName\":\"Relationship with Proposer\",\n\n                        \"paramValue\":\""+relationshipWithProposer+"\"\n\n                     },\n\n                     { \n\n                        \"paramName\":\"Date of Birth\",\n\n                        \"paramValue\":\""+dateofBirth+"\"\n\n                     },\n\n                     { \n\n                        \"paramName\":\"Age\",\n\n                        \"paramValue\":\""+age+"\"\n\n                     },\n\n                     { \n\n                        \"paramName\":\"Nominee %\",\n\n                        \"paramValue\":\""+nominee+"\"\n\n                     },\n\n                     { \n\n                        \"paramName\":\"Appointee Name\",\n\n                        \"paramValue\":\""+appointeeName+"\"\n\n                     },\n\n                     { \n\n                        \"paramName\":\"Appointee Age\",\n\n                        \"paramValue\":\""+appointeeAge+"\"\n\n                     },\n\n                     { \n\n                        \"paramName\":\"Appointee relationship\",\n\n                        \"paramValue\":\""+appointeeRelationship+"\"\n\n                     },\n\n                     { \n\n                        \"paramName\":\"Aadhaar No of the nominee\",\n\n                        \"paramValue\":\""+aadhaarNonominee+"\"\n\n                     }\n\n                  ]\n\n               }\n\n            ]\n\n         }\n\n      ]\n\n   },\n\n   \"riskDetails\":[]\n\n}";
			
			String requestString="{ \n\n   \"serviceTransactionId\":\""+triggerEndosmntrequest.getServiceTransactionId()+"\",\n\n   \"businessChannel\":\""+triggerEndosmntrequest.getBusinessChannel()+"\",\n\n   \"userCode\":\""+triggerEndosmntrequest.getUserCode()+"\",\n\n   \"roleCode\":\""+triggerEndosmntrequest.getRoleCode()+"\",\n\n   \"policyNumber\":\""+triggerEndosmntrequest.getPolicyNumber()+"\",\n\n   \"typeOfEndorsement\":\""+triggerEndosmntrequest.getTypeOfEndorsement()+"\",\n\n   \"policyProperty\":{ \n\n      \"simpleProperty\":[ \n\n \n\n      ],\n\n      \"multiSetProperty\":[ \n\n         { \n\n            \"multiSetName\":\"Nominee Detail\",\n\n            \"multiSetDetail\":[ \n\n               { \n\n                  \"sequenceNo\":\""+sequenceNo+"\",\n\n                  \"operationCode\":\""+operationCode+"\",\n\n                  \"property\":[ \n\n                     { \n\n                        \"paramName\":\"Nominee Name\",\n\n                        \"paramValue\":\""+nomineeDetails.getNomineeName()+"\"\n\n                     },\n\n                     { \n\n                        \"paramName\":\"Relationship with Proposer\",\n\n                        \"paramValue\":\""+nomineeDetails.getRelationshipWithProposer()+"\"\n\n                     },\n\n                     { \n\n                        \"paramName\":\"Date of Birth\",\n\n                        \"paramValue\":\""+nomineeDetails.getDateOfBirth()+"\"\n\n                     },\n\n                     { \n\n                        \"paramName\":\"Age\",\n\n                        \"paramValue\":\""+nomineeDetails.getAge()+"\"\n\n                     },\n\n                     { \n\n                        \"paramName\":\"Nominee %\",\n\n                        \"paramValue\":\""+nomineeDetails.getNomineeShare()+"\"\n\n                     },\n\n                     { \n\n                        \"paramName\":\"Appointee Name\",\n\n                        \"paramValue\":\""+nomineeDetails.getAppointeeName()+"\"\n\n                     },\n\n                     { \n\n                        \"paramName\":\"Appointee Age\",\n\n                        \"paramValue\":\""+nomineeDetails.getAppointeeAge()+"\"\n\n                     },\n\n                     { \n\n                        \"paramName\":\"Appointee relationship\",\n\n                        \"paramValue\":\""+nomineeDetails.getAppointeeRelationship()+"\"\n\n                     },\n\n                     { \n\n                        \"paramName\":\"Aadhaar No of the nominee\",\n\n                        \"paramValue\":\""+nomineeDetails.getAadhaarNumberOfTheNominee()+"\"\n\n                     }\n\n                  ]\n\n               }\n\n            ]\n\n         }\n\n      ]\n\n   },\n\n   \"riskDetails\":[]\n\n}";
			
			
			
			JSONObject json = new JSONObject(requestString);
			log.info(requestString);
			System.out.println("Bancs Live - Request:"+requestString);
			conn.setDoOutput(true);
			OutputStream os = conn.getOutputStream();
			os.write(json.toString().getBytes());
			os.flush();
			os.close();
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Request Failed : HTTP Error code : "+ conn.getResponseCode());
			}else if (conn.getResponseCode() == 200) {
				InputStreamReader in = new InputStreamReader(conn.getInputStream());
				BufferedReader br = new BufferedReader(in);
				String output = "";
				while ((output = br.readLine()) != null) {
					JSONObject jsonObject = new JSONObject(output);
					responseString = jsonObject.toString();
					
					mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					log.info("Response :"+responseString);
					response = mapper.readValue(responseString, TriggerPolicyEndorsementResponse.class);
					if(response.getErrorCode().equals("0")){
						System.out.println("Bancs Live - Response: Success");
					}else {
						System.out.println("Bancs Live - Response: Error");
					}
				}
			}
			conn.disconnect();
			
			
		}catch(Exception exp){
			log.error("Exception occurred while calling this service"+exp.getMessage());
			exp.printStackTrace();
		}
		
		return responseString;
	}
}

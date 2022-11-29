package com.shaic.restservices.bancs.triggerPartyEndorsement;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.policy.search.ui.BancsSevice;
import com.shaic.claim.policy.search.ui.IntegrationLogTable;
import com.shaic.claim.rod.wizard.forms.AddBanksDetailsTableDTO;
import com.shaic.domain.BancsHeaderDetails;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;

public class TriggerPartyEndorsementService {
	private static final Logger log = LoggerFactory.getLogger(TriggerPartyEndorsementService.class);
	
	private static final TriggerPartyEndorsementService instance = new TriggerPartyEndorsementService();
	
	
	public static TriggerPartyEndorsementService getInstance() {
		return instance;
	}

	public TriggerPartyEndorsementService() {
		
	}
	
	public TriggerPartyEndorsementResponse callTriggerEndorsement(AddBanksDetailsTableDTO bankDetails, String riskID,String operationCode, String seqNo){
	
		String responseString = "";
		TriggerPartyEndorsementResponse response = null;
		ObjectMapper mapper = new ObjectMapper();
		
		try{
			
			String serviceURL = BPMClientContext.BANCS_WEB_SERVICE_PARTY_URL;
			if(!serviceURL.endsWith("/")){
				serviceURL = serviceURL+"/"+"updateParty";
			}else{
				serviceURL = serviceURL+"updateParty";
			}
			System.out.println("Bancs Live - Url :"+serviceURL);
			URL url = new URL(serviceURL); 
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			Map<String, String> mapVal = getAuthAndRequestParam("ALL");
			conn.setRequestProperty("Authorization", mapVal.get("authkey"));


			/*TriggerPartyEndorsementDTO triggerEndosmntrequest = new TriggerPartyEndorsementDTO();
			triggerEndosmntrequest.setServiceTransactionId(SHAConstants.serviceTrancId);
			triggerEndosmntrequest.setBusinessChannel(SHAConstants.bChanel);
			triggerEndosmntrequest.setUserCode(SHAConstants.userCode);
			triggerEndosmntrequest.setRoleCode(SHAConstants.roleCode);
			triggerEndosmntrequest.setStakeCode("POLICY-HOL");
			triggerEndosmntrequest.setPartyCode("ssss");
			triggerEndosmntrequest.setTypeOfEndorsement("END15");
			
			
			
			ArrayList<TriggerPartyEndorsementDetails>endorsementList=new ArrayList<TriggerPartyEndorsementDetails>();
			
			TriggerPartyEndorsementDetails partyEndorsementDetails = new TriggerPartyEndorsementDetails();
			partyEndorsementDetails.setAddressProof("");
			partyEndorsementDetails.setDobProof("");
			partyEndorsementDetails.setEiaAvailable("");
			partyEndorsementDetails.setEiaCreditRequired("");
			partyEndorsementDetails.setEiaNumber("");
			partyEndorsementDetails.setEiaRequired("");
			partyEndorsementDetails.setEiaWhenLinked("");
			partyEndorsementDetails.setEpolicyRequired("");
			partyEndorsementDetails.setFatherName("");
			partyEndorsementDetails.setFirstName("");
			partyEndorsementDetails.setIdProof("");
			partyEndorsementDetails.setInsuranceRepositoryCode("");
			partyEndorsementDetails.setLastName("");
			partyEndorsementDetails.setMiddleName("");
			partyEndorsementDetails.setSpouseName("");
			
			endorsementList.add(partyEndorsementDetails);
			
			
			ArrayList<TriggerPartyEndorseBankDetails> banklist=new ArrayList<TriggerPartyEndorseBankDetails>();
			TriggerPartyEndorseBankDetails bankdetials=new TriggerPartyEndorseBankDetails();
			bankdetials.setOperationCode("ADD");
			bankdetials.setSlNo(1L);
			bankdetials.setPreference("Primary");
			bankdetials.setNameAsPerBankAC("Srikanta");
			bankdetials.setNameOfTheBank("");
			bankdetials.setBranchName("");
			bankdetials.setAccountType("Saving");
			bankdetials.setAccountNumber("1111");
			bankdetials.setMICRCode("");
			bankdetials.setIFSCCode("sbi00034");
			bankdetials.setVirtualPaymentAddress("");
			bankdetials.setEffectiveFromDate(SHAUtils.getFromDate("31-07-2019"));
			bankdetials.setEffectiveToDate(null);
			
			banklist.add(bankdetials);
			
			
			ArrayList<TriggerPartyEndorseAddressDetails> addresList=new ArrayList<TriggerPartyEndorseAddressDetails>();
			
			TriggerPartyEndorseAddressDetails adressDetails=new TriggerPartyEndorseAddressDetails();
			adressDetails.setAddressType("permanent");
			adressDetails.setAddress1("chennai");
			adressDetails.setAddress2("");
			adressDetails.setAddress3("");
			adressDetails.setCountry("");
			adressDetails.setState("");
			adressDetails.setCity("");
			adressDetails.setPinCode("600030");
			adressDetails.setStdCode("");
			adressDetails.setPhoneNo(" ");
			adressDetails.setMobileNo("");
			adressDetails.setFax("");
			adressDetails.setEMailId("");
			adressDetails.setSubDistrictName("");
			adressDetails.setDistrictName("");
			adressDetails.setLocationType("");
			adressDetails.setSocialSector("");
			adressDetails.setAlternateEmailId("");
			adressDetails.setAlternateContactNumber("");
			adressDetails.setContactPersonType("");
			adressDetails.setContactPerson("");
			adressDetails.setContactNumber("");
			adressDetails.setAlternateContactNumber("");
			adressDetails.setWhatsappNo("");
			adressDetails.setFacebook("");
			adressDetails.setTwitter("");
			adressDetails.setLinkedIn("");
			adressDetails.setWebsiteAddress("");
			
			addresList.add(adressDetails);
			
			partyEndorsementDetails.setBankDetails(banklist);
			partyEndorsementDetails.setAddressDetails(addresList);
			triggerEndosmntrequest.setEndorsementDetailsObject(endorsementList);*/
			

			String serviceTransactionId=mapVal.get("tranid");
			String businessChannel=mapVal.get("business");
			String userCode=mapVal.get("usercode");
			String roleCode=mapVal.get("rolecode");
			String stakeCode="MEMBER";
			String typeOfEndorsement="END15";
			String partyCode = riskID!=null?riskID:""; /*"PI19490574";*/ //String.valueOf(riskID/*"PI19490380"*/);
			String title="";
			String fName="";
			String mName="";
			String lName="";
			String gender="";
			String dateOfBirth="";
			String occupation="";
			String businessName="";
			String regtNo="";
			String regtDate="";
			String typeOfOrg="";
			String nationality="";
			
			String sequenceNo="";
			//String operationCode="UPDATE";
			
			/*String nameAsBank="Karthikeyan";
			String accountType="SAVINGS";
			String banckAccNo="11234567890";
			String pref="SECONDAY";
			String micrCode="5RT1234";
			String ifscCode="KVBL0001672";
			String startDate="21/08/2019 12:11:00";
			String endDate="";*/
			
			/*TriggerPartyEndorseBankDetails bankdetials=new TriggerPartyEndorseBankDetails();
			
			bankdetials.setPreference("SECONDAY");
			bankdetials.setNameAsPerBankAC("Karthikeyan");
			bankdetials.setAccountType("SAVINGS");
			bankdetials.setAccountNumber("11234567890");
			bankdetials.setMICRCode("5RT1234");
			bankdetials.setIFSCCode("KVBL0001672");
			bankdetials.setStarDate("21/08/2019 12:11:00");
			bankdetials.setEndDate("");*/
			
			
			
			//String postValue="";
			
			
			//String requestString = mapper.writeValueAsString(triggerEndosmntrequest);
			//String requestString = mapper.writeValueAsString({  \n   \"serviceTransactionId\":\"+serviceTransactionId+\",\n   \"businessChannel\":\"+businessChannel+\",\n   \"userCode\":\"+userCode+\",\n   \"roleCode\":\"+roleCode+\",\n   \"stakeCode\":\"+stakeCode+\",\n   \"typeOfEndorsement\":\"+typeOfEndorsement+\",\n   \"partyCode\":\"+partyCode+\",\n   \"title\":\"+title+\",\n   \"firstName\":\"+fName+\",\n   \"middleName\":\"+mName+\",\n   \"lastName\":\"+lName+\",\n   \"gender\":\"+gender+\",\n   \"dateOfBirth\":\"+dateOfBirth+\",\n   \"occupation\":\"+occupation+\",\n   \"businessName\":\"+businessName+\",\n   \"registrationNo\":\"+regtNo+\",\n   \"registrationDate\":\"+regtDate+\",\n   \"typeOfOrganization\":\"+typeOfOrg+\",\n   \"nationality\":\"+nationality+\",\n   \"partyProperty\":{  \n      \"simpleProperty\":[  \n\n      ],\n      \"multiSetProperty\":[  \n         {  \n            \"multiSetName\":\"Bank Detail\",\n            \"multiSetDetail\":[  \n               {  \n                  \"sequenceNo\":\"\",\n                  \"operationCode\":\"\",\n                  \"property\":[  \n                     {  \n                        \"paramName\":\"Name As Per Bank AC\",\n                        \"paramValue\":\"+nameAsBank+\"\n                     },\n                     {  \n                        \"paramName\":\"Account Type \",\n                        \"paramValue\":\"+accountType+\"\n                     },\n                     {  \n                        \"paramName\":\"Bank Account Number\",\n                        \"paramValue\":\"+banckAccNo+\"\n                     },\n                     {  \n                        \"paramName\":\"Preference\",\n                        \"paramValue\":\"+pref+\"\n                     },\n                     {  \n                        \"paramName\":\"MICR Code\",\n                        \"paramValue\":\"+micrCode+\"\n                     },\n                     {  \n                        \"paramName\":\"IFSC Code\",\n                        \"paramValue\":\"+ifscCode+\"\n                     },\n                     {  \n                        \"paramName\":\"Start Date\",\n                        \"paramValue\":\"+startDate+\"\n                     },\n                     {  \n                        \"paramName\":\"End Date\",\n                        \"paramValue\":\"+endDate+\"\n                     }\n                  ]\n               }\n            ]\n         }\n      ]\n   },\n   \"partyAddress\":[  \n\n   ]\n});
//			String requestString = "{  \n   \"serviceTransactionId\":\""+serviceTransactionId+"\",\n   \"businessChannel\":\"+businessChannel+\",\n   \"userCode\":\"+userCode+\",\n   \"roleCode\":\"+roleCode+\",\n   \"stakeCode\":\"+stakeCode+\",\n   \"typeOfEndorsement\":\"+typeOfEndorsement+\",\n   \"partyCode\":\"+partyCode+\",\n   \"title\":\"+title+\",\n   \"firstName\":\"+fName+\",\n   \"middleName\":\"+mName+\",\n   \"lastName\":\"+lName+\",\n   \"gender\":\"+gender+\",\n   \"dateOfBirth\":\"+dateOfBirth+\",\n   \"occupation\":\"+occupation+\",\n   \"businessName\":\"+businessName+\",\n   \"registrationNo\":\"+regtNo+\",\n   \"registrationDate\":\"+regtDate+\",\n   \"typeOfOrganization\":\"+typeOfOrg+\",\n   \"nationality\":\"+nationality+\",\n   \"partyProperty\":{  \n      \"simpleProperty\":[  \n\n      ],\n      \"multiSetProperty\":[  \n         {  \n            \"multiSetName\":\"Bank Detail\",\n            \"multiSetDetail\":[  \n               {  \n                  \"sequenceNo\":\"\",\n                  \"operationCode\":\"\",\n                  \"property\":[  \n                     {  \n                        \"paramName\":\"Name As Per Bank AC\",\n                        \"paramValue\":\"+nameAsBank+\"\n                     },\n                     {  \n                        \"paramName\":\"Account Type \",\n                        \"paramValue\":\"+accountType+\"\n                     },\n                     {  \n                        \"paramName\":\"Bank Account Number\",\n                        \"paramValue\":\"+banckAccNo+\"\n                     },\n                     {  \n                        \"paramName\":\"Preference\",\n                        \"paramValue\":\"+pref+\"\n                     },\n                     {  \n                        \"paramName\":\"MICR Code\",\n                        \"paramValue\":\"+micrCode+\"\n                     },\n                     {  \n                        \"paramName\":\"IFSC Code\",\n                        \"paramValue\":\"+ifscCode+\"\n                     },\n                     {  \n                        \"paramName\":\"Start Date\",\n                        \"paramValue\":\"+startDate+\"\n                     },\n                     {  \n                        \"paramName\":\"End Date\",\n                        \"paramValue\":\"+endDate+\"\n                     }\n                  ]\n               }\n            ]\n         }\n      ]\n   },\n   \"partyAddress\":[  \n\n   ]\n}";
			//String requestString = "{  \n   \"serviceTransactionId\":\""+serviceTransactionId+"\",\n   \"businessChannel\":\""+businessChannel+"\",\n   \"userCode\":\""+userCode+"\",\n   \"roleCode\":\""+roleCode+"\",\n   \"stakeCode\":\""+stakeCode+"\",\n   \"typeOfEndorsement\":\""+typeOfEndorsement+"\",\n   \"partyCode\":\""+partyCode+"\",\n   \"title\":\""+title+"\",\n   \"firstName\":\""+fName+"\",\n   \"middleName\":\""+mName+"\",\n   \"lastName\":\""+lName+"\",\n   \"gender\":\""+gender+"\",\n   \"dateOfBirth\":\""+dateOfBirth+"\",\n   \"occupation\":\""+occupation+"\",\n   \"businessName\":\""+businessName+"\",\n   \"registrationNo\":\""+regtNo+"\",\n   \"registrationDate\":\""+regtDate+"\",\n   \"typeOfOrganization\":\""+typeOfOrg+"\",\n   \"nationality\":\""+nationality+"\",\n   \"partyProperty\":{  \n      \"simpleProperty\":[  \n\n      ],\n      \"multiSetProperty\":[  \n         {  \n            \"multiSetName\":\"Bank Detail\",\n            \"multiSetDetail\":[  \n               {  \n                  \"sequenceNo\":\"\",\n                  \"operationCode\":\"\",\n                  \"property\":[  \n                     {  \n                        \"paramName\":\"Name As Per Bank AC\",\n                        \"paramValue\":\""+nameAsBank+"\"\n                     },\n                     {  \n                        \"paramName\":\"Account Type \",\n                        \"paramValue\":\""+accountType+"\"\n                     },\n                     {  \n                        \"paramName\":\"Bank Account Number\",\n                        \"paramValue\":\""+banckAccNo+"\"\n                     },\n                     {  \n                        \"paramName\":\"Preference\",\n                        \"paramValue\":\""+pref+"\"\n                     },\n                     {  \n                        \"paramName\":\"MICR Code\",\n                        \"paramValue\":\""+micrCode+"\"\n                     },\n                     {  \n                        \"paramName\":\"IFSC Code\",\n                        \"paramValue\":\""+ifscCode+"\"\n                     },\n                     {  \n                        \"paramName\":\"Start Date\",\n                        \"paramValue\":\""+startDate+"\"\n                     },\n                     {  \n                        \"paramName\":\"End Date\",\n                        \"paramValue\":\""+endDate+"\"\n                     }\n                  ]\n               }\n            ]\n         }\n      ]\n   },\n   \"partyAddress\":[  \n\n   ]\n}";
			//String requestString = "{  \n   \"serviceTransactionId\":\""+serviceTransactionId+"\",\n   \"businessChannel\":\""+businessChannel+"\",\n   \"userCode\":\""+userCode+"\",\n   \"roleCode\":\""+roleCode+"\",\n   \"stakeCode\":\""+stakeCode+"\",\n   \"typeOfEndorsement\":\""+typeOfEndorsement+"\",\n   \"partyCode\":\""+partyCode+"\",\n   \"title\":\""+title+"\",\n   \"firstName\":\""+fName+"\",\n   \"middleName\":\""+mName+"\",\n   \"lastName\":\""+lName+"\",\n   \"gender\":\""+gender+"\",\n   \"dateOfBirth\":\""+dateOfBirth+"\",\n   \"occupation\":\""+occupation+"\",\n   \"businessName\":\""+businessName+"\",\n   \"registrationNo\":\""+regtNo+"\",\n   \"registrationDate\":\""+regtDate+"\",\n   \"typeOfOrganization\":\""+typeOfOrg+"\",\n   \"nationality\":\""+nationality+"\",\n   \"partyProperty\":{  \n      \"simpleProperty\":[  \n\n      ],\n      \"multiSetProperty\":[  \n         {  \n            \"multiSetName\":\"Bank Detail\",\n            \"multiSetDetail\":[  \n               {  \n                  \"sequenceNo\":\""+sequenceNo+"\",\n                  \"operationCode\":\""+operationCode+"\",\n                  \"property\":[  \n                     {  \n                        \"paramName\":\"Name As Per Bank AC\",\n                        \"paramValue\":\""+nameAsBank+"\"\n                     },\n                     {  \n                        \"paramName\":\"Account Type \",\n                        \"paramValue\":\""+accountType+"\"\n                     },\n                     {  \n                        \"paramName\":\"Bank Account Number\",\n                        \"paramValue\":\""+banckAccNo+"\"\n                     },\n                     {  \n                        \"paramName\":\"Preference\",\n                        \"paramValue\":\""+pref+"\"\n                     },\n                     {  \n                        \"paramName\":\"MICR Code\",\n                        \"paramValue\":\""+micrCode+"\"\n                     },\n                     {  \n                        \"paramName\":\"IFSC Code\",\n                        \"paramValue\":\""+ifscCode+"\"\n                     },\n                     {  \n                        \"paramName\":\"Start Date\",\n                        \"paramValue\":\""+startDate+"\"\n                     },\n                     {  \n                        \"paramName\":\"End Date\",\n                        \"paramValue\":\""+endDate+"\"\n                     }\n                  ]\n               }\n            ]\n         }\n      ]\n   },\n   \"partyAddress\":[  \n\n   ]\n}";
			
			String requestString = "{  \n   \"serviceTransactionId\":\""+serviceTransactionId+"\",\n   \"businessChannel\":\""+businessChannel+"\",\n   \"userCode\":\""+userCode+"\",\n   \"roleCode\":\""+roleCode+"\",\n   \"stakeCode\":\""+stakeCode+"\",\n   \"typeOfEndorsement\":\""+typeOfEndorsement+"\",\n   \"partyCode\":\""+partyCode+"\",\n   \"title\":\""+title+"\",\n   \"firstName\":\""+fName+"\",\n   \"middleName\":\""+mName+"\",\n   \"lastName\":\""+lName+"\",\n   \"gender\":\""+gender+"\",\n   \"dateOfBirth\":\""+dateOfBirth+"\",\n   \"occupation\":\""+occupation+"\",\n   \"businessName\":\""+businessName+"\",\n   \"registrationNo\":\""+regtNo+"\",\n   \"registrationDate\":\""+regtDate+"\",\n   \"typeOfOrganization\":\""+typeOfOrg+"\",\n   \"nationality\":\""+nationality+"\",\n   \"partyProperty\":{  \n      \"simpleProperty\":[  \n\n      ],\n      \"multiSetProperty\":[  \n         {  \n            \"multiSetName\":\"Bank Detail\",\n            \"multiSetDetail\":[  \n               {  \n                  \"sequenceNo\":\""+seqNo+"\",\n                  \"operationCode\":\""+operationCode+"\",\n                  \"property\":[  \n                     {  \n                        \"paramName\":\"Name as per Bank AC\",\n                        \"paramValue\":\""+bankDetails.getNameAsPerBankAC()+"\"\n                     },\n                     {  \n                        \"paramName\":\"Account Type\",\n                        \"paramValue\":\""+bankDetails.getAccountType()+"\"\n                     },\n                     {  \n                        \"paramName\":\"Bank Account Number\",\n                        \"paramValue\":\""+bankDetails.getAccountNumber()+"\"\n                     },\n                     {  \n                        \"paramName\":\"Preference\",\n                        \"paramValue\":\""+bankDetails.getPreference()+"\"\n                     },\n                     {  \n                        \"paramName\":\"MICR Code\",\n                        \"paramValue\":\""+bankDetails.getMicrCode()+"\"\n                     },\n                     {  \n                        \"paramName\":\"IFSC Code\",\n                        \"paramValue\":\""+bankDetails.getIfscCode()+"\"\n                     },\n                     {  \n                        \"paramName\":\"Start Date\",\n                        \"paramValue\":\""+SHAUtils.formatDate(bankDetails.getEffectiveFromDate())+"\"\n                     },\n                     {  \n                        \"paramName\":\"End Date\",\n                        \"paramValue\":\""+SHAUtils.formatDate(bankDetails.getEffectiveToDate())+"\"\n                     }\n                  ]\n               }\n            ]\n         }\n      ]\n   },\n   \"partyAddress\":[  \n\n   ]\n}";
			
			JSONObject json = new JSONObject(requestString);
			System.out.println("Bancs Live - Request:"+requestString);
			log.info(requestString);
			conn.setDoOutput(true);
			OutputStream os = conn.getOutputStream();
			os.write(json.toString().getBytes());
			os.flush();
			os.close();
			IntegrationLogTable intLog = new IntegrationLogTable();
	        intLog.setOwner("Bancs");
	        intLog.setServiceName("updateParty");
	        intLog.setUrl(serviceURL);
	        intLog.setRequest(requestString);
	        intLog.setCreatedBy("GALAXY");
	        intLog.setCreatedDate(new Timestamp(System.currentTimeMillis()));
	        intLog.setActiveStatus(1l);
			if (conn.getResponseCode() != 200) {
				intLog.setStatus("Error");
				intLog.setResponse("Connection Error");
				intLog.setRemarks("Connection Error");
				throw new RuntimeException("Request Failed : HTTP Error code : "+ conn.getResponseCode());
			}else if (conn.getResponseCode() == 200) {
				InputStreamReader in = new InputStreamReader(conn.getInputStream());
				BufferedReader br = new BufferedReader(in);
				String output = "";
				while ((output = br.readLine()) != null) {
					JSONObject jsonObject = new JSONObject(output);
					responseString = jsonObject.toString();
					log.info("Response :"+responseString);
					String resDtls = output;
					if(resDtls != null && resDtls.length()>3000){
						intLog.setResponse(resDtls.substring(0, 2999));
					}else {
						intLog.setResponse(resDtls);
					}
					response = mapper.readValue(responseString, TriggerPartyEndorsementResponse.class);
					if(response.getErrorCode().intValue() == 0){
						intLog.setStatus("Success");
						System.out.println("Bancs Live - Response: Success");
					}else {
						intLog.setStatus("Error");
						System.out.println("Bancs Live - Response: Error");
					}
				}
			}
			DBCalculationService bancsService = new DBCalculationService();
			bancsService.insertIntegrationLogTable(intLog);
			conn.disconnect();
			
			
		}catch(Exception exp){
			log.error("Exception occurred while calling this service"+exp.getMessage());
			exp.printStackTrace();
		}
		
		return response;
	}
	
	
	public static Map<String, String> getAuthAndRequestParam(String argRequestType) {
		Map<String, String> requestContainer = new HashMap<String, String>();
		BancsSevice bancsService = BancsSevice.getInstance();
		BancsHeaderDetails reqDtlObj = bancsService.getRequestHeaderValues(argRequestType);
		if(reqDtlObj != null){
			byte[] val = (reqDtlObj.getWsUserName()+":"+reqDtlObj.getWsPassword()).getBytes();
			String authString = new String(Base64.encodeBase64(val));
			requestContainer.put("authkey", authString);
			requestContainer.put("business", reqDtlObj.getWebserviceBC());
			requestContainer.put("usercode", reqDtlObj.getWebserviceUserCode());
			requestContainer.put("rolecode", reqDtlObj.getWebserviceRoleCode());
			Long id = bancsService.getBancsServiceTransactionalId();
			if(id != null && id.intValue() > 0){
				requestContainer.put("tranid", String.valueOf(id));
			}else{
				requestContainer.put("tranid", String.valueOf(0));
			}
			return requestContainer;
		}else{
			log.info("Auth and request parameter going wrong configure bancs user dtls tbl");
			return null;
		}
	}
}
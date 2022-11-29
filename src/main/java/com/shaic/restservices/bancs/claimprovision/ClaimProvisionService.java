package com.shaic.restservices.bancs.claimprovision;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.shaic.claim.policy.search.ui.BancsSevice;
import com.shaic.claim.policy.search.ui.IntegrationLogTable;
import com.shaic.claim.policy.search.ui.premia.PremPolicy;
import com.shaic.claim.rod.wizard.forms.BankDetailsTableDTO;
import com.shaic.domain.BancsHeaderDetails;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;

public class ClaimProvisionService {
	
	private static final Logger log = LoggerFactory.getLogger(ClaimProvisionService.class);
	
	private static final ClaimProvisionService instance = new ClaimProvisionService();
	
	public static ClaimProvisionService getInstance() {
		return instance;
	}

	public ClaimProvisionService() {
		
	}

	public static String callProvisionService(String requestString){
		String responseString = "";
		BancsCommonResponse response = null;
		try{
			String serviceURL = BPMClientContext.BANCS_WEB_SERVICE_URL;
			if(!serviceURL.endsWith("/")){
				serviceURL = serviceURL+"/"+"claimProvision";
			}else{
				serviceURL = serviceURL+"claimProvision";
			}
			System.out.println("Bancs Live - Url :"+serviceURL);
			URL url = new URL(serviceURL); //http://10.20.2.10:8080/IIMS/rest/webservice/claim/claimProvision
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			Map<String, String> mapVal = instance.getAuthAndRequestParam("ALL");
			conn.setRequestProperty("Authorization", mapVal.get("authkey"));

			/*ClaimProvisionServiceRequest request = new ClaimProvisionServiceRequest();
			request.setRequestId(String.valueOf(ThreadLocalRandom.current().nextInt()));
			request.setClaimIntimationNumber("CLI/2020/151118/02/0300962");//CLI/2020/151118/0057478
			request.setClaimIntimationDate("04-07-2019");
			request.setLossDate("04-07-2019");
			request.setTransactionDate("08-07-2019");//Current Date
			request.setPolicyNumber("012HFHOSTD401386878000000");//P/151118/01/2020/001590
			request.setLob("11");
			request.setProvisionAmount(1053);
			request.setProvisionReversalAmount(200);
			String requestString = mapper.writeValueAsString(request);*/
			
			ObjectMapper mapper = new ObjectMapper();
			JSONObject json = new JSONObject(requestString);
			log.info("Request To Claim Provision : "+requestString);
			System.out.println("Bancs Live - Request:"+requestString);
			conn.setDoOutput(true);
			OutputStream os = conn.getOutputStream();
			os.write(json.toString().getBytes());
			os.flush();
			os.close();
			IntegrationLogTable intLog = new IntegrationLogTable();
	        intLog.setOwner("Bancs");
	        intLog.setServiceName("claimProvision");
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
					response = mapper.readValue(responseString, BancsCommonResponse.class);
					String resDtls = output;
					if(resDtls != null && resDtls.length()>3000){
						intLog.setResponse(resDtls.substring(0, 2999));
					}else {
						intLog.setResponse(resDtls);
					}
				}
			}
			DBCalculationService bancsService = new DBCalculationService();;
			bancsService.insertIntegrationLogTable(intLog);
			conn.disconnect();
					
		}catch(Exception exp){
			log.error("Exception occurred while calling the provision service"+exp.getMessage());
			exp.printStackTrace();
		}
		return responseString;
	}
	
	public static String callUniqueInstallmentAmountService(String policyNumber){
		String responseString = "";
		UniqueInstAmountResponse response = null;
		try{
			String serviceURL = BPMClientContext.BANCS_WEB_SERVICE_URL;
			if(!serviceURL.endsWith("/")){
				serviceURL = serviceURL+"/"+"getUniqueInstAmount";
			}else{
				serviceURL = serviceURL+"getUniqueInstAmount";
			}
			System.out.println("Bancs Live - Url :"+serviceURL);
			URL url = new URL(serviceURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			Map<String, String> mapVal = instance.getAuthAndRequestParam("ALL");
			conn.setRequestProperty("Authorization", mapVal.get("authkey"));
			
			UniqueInstallmentAmountRequest request = new UniqueInstallmentAmountRequest();

			request.setServiceTransactionId(mapVal.get("tranid"));
			request.setBusinessChannel(mapVal.get("business"));
			request.setUserCode(mapVal.get("usercode"));
			request.setRoleCode(mapVal.get("rolecode"));
			request.setPolicyNumber(policyNumber/*"012HFHOSTD401386878000000"*/);
			
			ObjectMapper mapper = new ObjectMapper();
			String requestString = mapper.writeValueAsString(request);
			
			JSONObject json = new JSONObject(requestString);
			log.info("Request To Unique Installment Amount : "+requestString);
			System.out.println("Bancs Live - Request:"+requestString);
			conn.setDoOutput(true);
			OutputStream os = conn.getOutputStream();
			os.write(json.toString().getBytes());
			os.flush();
			os.close();
			IntegrationLogTable intLog = new IntegrationLogTable();
	        intLog.setOwner("Bancs");
	        intLog.setServiceName("getUniqueInstAmount");
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
					response = mapper.readValue(responseString, UniqueInstAmountResponse.class);
					String resDtls = output;
					if(resDtls != null && resDtls.length()>3000){
						intLog.setResponse(resDtls.substring(0, 2999));
					}else {
						intLog.setResponse(resDtls);
					}
					if(response.getErrorCode().intValue() == 0){
						intLog.setStatus("Success");
						System.out.println("Bancs Live - Response: Success");
					} else {
						intLog.setStatus("Error");
						System.out.println("Bancs Live - Response: Error");
					}
				}
			}
			DBCalculationService bancsService = new DBCalculationService();;
			bancsService.insertIntegrationLogTable(intLog);
			
		}catch(Exception exp){
			log.error("Exception occurred while calling the Unique Inst Amount service"+exp.getMessage());
			exp.printStackTrace();
		}
		return responseString;
	}
	
	public static String callAdjustInstallmentAmountService(){
		String responseString = "";
		AdjustInstAmountResponse response = null;
		try{
			String serviceURL = BPMClientContext.BANCS_WEB_SERVICE_URL;
			if(!serviceURL.endsWith("/")){
				serviceURL = serviceURL+"/"+"adjustUniqueInstallment";
			}else{
				serviceURL = serviceURL+"adjustUniqueInstallment";
			}
			System.out.println("Bancs Live - Url :"+serviceURL);
			URL url = new URL(serviceURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			Map<String, String> mapVal = instance.getAuthAndRequestParam("ALL");
			conn.setRequestProperty("Authorization", mapVal.get("authkey"));
			
			AdjustInstallmentAmountRequest request = new AdjustInstallmentAmountRequest();
			request.setServiceTransactionId(mapVal.get("tranid"));
			request.setBusinessChannel(mapVal.get("business"));
			request.setUserCode(mapVal.get("usercode"));
			request.setRoleCode(mapVal.get("rolecode"));
			request.setPolicyNumber("012HFHOSTD401386878000000");
			request.setIntimationNo("CLI/2020/151118/02/0300962");
			request.setCpuCode(0);
			request.setAdjustmentAmount(0);
			request.setAdjustmentDate("09/07/2019");
			
			ObjectMapper mapper = new ObjectMapper();
			String requestString = mapper.writeValueAsString(request);
			
			JSONObject json = new JSONObject(requestString);
			log.info("Request To Adjust Unique Installment Amount : "+requestString);
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
					log.info("Response :"+responseString);
					response = mapper.readValue(responseString, AdjustInstAmountResponse.class);
				}
			}

		}catch(Exception exp){
			log.error("Exception occurred while calling the Adjust Unique Inst Amount service"+exp.getMessage());
			exp.printStackTrace();
		}
		return responseString;
	}
	
	
	public static List<PremPolicy> callPolicySummaryService(Map<String, Object> argObj){
		String responseString = "";
		PolicySummaryResponse response = null;
		List<PremPolicy> poilcySummaryDetails = null;
		try{
			String serviceURL = BPMClientContext.BANCS_WEB_SERVICE_URL;
			if(!serviceURL.endsWith("/")){
				serviceURL = serviceURL+"/"+"getPolicySummary";
			}else{
				serviceURL = serviceURL+"getPolicySummary";
			}
			System.out.println("Bancs Live - Url :"+serviceURL);
			URL url = new URL(serviceURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			Map<String, String> mapVal = instance.getAuthAndRequestParam("ALL");
			conn.setRequestProperty("Authorization", mapVal.get("authkey"));
			
			PolicySummaryRequest request = new PolicySummaryRequest();
			request.setServiceTransactionId(mapVal.get("tranid"));
			request.setBusinessChannel(mapVal.get("business"));
			request.setUserCode(mapVal.get("usercode"));
			request.setRoleCode(mapVal.get("rolecode"));
			request.setHealthCardNo(String.valueOf(argObj.get("healthCardnumber")!=null?argObj.get("healthCardnumber"):""));
			request.setInsuredDOB(String.valueOf(argObj.get("insuredDateOfBirth")!=null?argObj.get("insuredDateOfBirth"):""));
			request.setInsuredName(String.valueOf(argObj.get("insuredName")!=null?argObj.get("insuredName"):""));
			request.setMobileNo(String.valueOf(argObj.get("registerdMobileNumber")!=null?argObj.get("registerdMobileNumber"):""));
			request.setOfficeCode(null);
			request.setPolicyNo(String.valueOf(argObj.get("polNo")!=null?argObj.get("polNo"):""));
			request.setProductCode("");
			request.setProposerDOB(String.valueOf(argObj.get("insuredDateOfBirth")!=null?argObj.get("insuredDateOfBirth"):""));
			request.setProposerName(String.valueOf(argObj.get("polAssrName")!=null?argObj.get("polAssrName"):""));
			request.setReceiptNo(String.valueOf(argObj.get("polReceiptNo")!=null?argObj.get("polReceiptNo"):""));
			request.setPageNo(1);
			request.setPageSize(5);
			
			ObjectMapper mapper = new ObjectMapper();
			String requestString = mapper.writeValueAsString(request);
			
			JSONObject json = new JSONObject(requestString);
			log.info("Request To Policy Summary : "+requestString);
			System.out.println("Bancs Live - Request:"+requestString);
			conn.setDoOutput(true);
			OutputStream os = conn.getOutputStream();
			os.write(json.toString().getBytes());
			os.flush();
			os.close();
			IntegrationLogTable intLog = new IntegrationLogTable();
	        intLog.setOwner("Bancs");
	        intLog.setServiceName("getPolicySummary");
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
					Gson gson=new Gson();
					response=gson.fromJson(responseString,PolicySummaryResponse.class);
					poilcySummaryDetails = response.getPolicySummary();
					String resDtls = output;
					if(resDtls != null && resDtls.length()>3000){
						intLog.setResponse(resDtls.substring(0, 2999));
					}else {
						intLog.setResponse(resDtls);
					}
					if(response.getErrorCode().intValue() == 0){
						intLog.setStatus("Success");
						System.out.println("Bancs Live - Response: Success");
					} else {
						intLog.setStatus("Error");
						System.out.println("Bancs Live - Response: Error");
					}
				}
			}
			DBCalculationService bancsService = new DBCalculationService();;
			bancsService.insertIntegrationLogTable(intLog);
		}catch(Exception exp){
			log.error("Exception occurred while calling Policy Summary service"+exp.getMessage());
			exp.printStackTrace();
		}
		return poilcySummaryDetails;
	}
	
	public static BankDetailsResponse callBankDetailsService(String PolicyNo,String partyCode){
		String responseString = "";
		BankDetailsResponse bdr = null;
		try{
			String serviceURL = BPMClientContext.BANCS_WEB_SERVICE_URL;
			if(!serviceURL.endsWith("/")){
				serviceURL = serviceURL+"/"+"getBankDetails";
			}else{
				serviceURL = serviceURL+"getBankDetails";
			}
			System.out.println("Bancs Live - Url :"+serviceURL);
			URL url = new URL(serviceURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			Map<String, String> mapVal = instance.getAuthAndRequestParam("ALL");
			conn.setRequestProperty("Authorization", mapVal.get("authkey"));
			
			BankDetailsRequest request = new BankDetailsRequest();
			request.setServiceTransactionId(mapVal.get("tranid"));
			request.setBusinessChannel(mapVal.get("business"));
			request.setUserCode(mapVal.get("usercode"));
			request.setRoleCode(mapVal.get("rolecode"));
			request.setPartyCode(partyCode!=null?partyCode:""/*"PI19490574"*/);
			request.setPolicyNumber("");
			
			ObjectMapper mapper = new ObjectMapper();
			String requestString = mapper.writeValueAsString(request);
			
			JSONObject json = new JSONObject(requestString);
			log.info("Request To Bank Details : "+requestString);
			System.out.println("Bancs Live - Request:"+requestString);
			conn.setDoOutput(true);
			OutputStream os = conn.getOutputStream();
			os.write(json.toString().getBytes());
			os.flush();
			os.close();
			IntegrationLogTable intLog = new IntegrationLogTable();
	        intLog.setOwner("Bancs");
	        intLog.setServiceName("getBankDetails");
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
//					response = mapper.readValue(responseString, BankDetailsResponse.class);
					Any root = JsonIterator.deserialize(responseString);
				    bdr = new BankDetailsResponse();
					bdr.setErrorCode(root.get("errorCode").toInt());
					bdr.setErrorDescription(root.get("errorDescription").toString());
					
					if(bdr.getErrorCode().intValue() == 0){
						intLog.setStatus("Success");
						System.out.println("Bancs Live - Response: Success");
						bdr.setPolicyNumber(root.get("policyNumber").toString());
						List<PartyBankDetails> listOfPbd = new ArrayList<PartyBankDetails>();
						PartyBankDetails pbd = null;
						for (Any element : root.get("partyBankDetails")) {
							pbd = new PartyBankDetails();
							pbd.setPartyCode(element.get("partyCode").toString());
							List<BankDetailsTableDTO> listOfBd = new ArrayList<BankDetailsTableDTO>();

							
							if(element.get("multiSetProperty").size() >0){
							for (Any element2 : element.get("multiSetProperty")) {
								BankDetailsTableDTO bd =  null;
								Map<String, Object> temp = new HashMap<String, Object>();
								for (Any element3 : element2.get("multiSetDetail")) {
									for (Any element4 : element3.get("property")) {
										 String paramName = element4.get("paramName").toString();
										 String paramValue = element4.get("paramValue").toString();
										 temp.put(paramName.trim(), paramValue);
									}
									bd = new BankDetailsTableDTO();
									bd.setSerialNumber(element3.get("sequenceNo").toString());
									for (Map.Entry<String,Object> entry : temp.entrySet())  {
										if(entry.getKey().equals("Account Type")){
											bd.setAccountType(String.valueOf(temp.get("Account Type")));
										}
										if(entry.getKey().equals("Bank Account Number")){
											bd.setAccountNumber(String.valueOf(temp.get("Bank Account Number")));
										}
										if(entry.getKey().equals("End Date")){
											bd.setEffectiveToDate(String.valueOf(temp.get("End Date")));
										}
										if(entry.getKey().equals("Start Date")){
											bd.setEffectiveFromDate(String.valueOf(temp.get("Start Date")));
										}
										if(entry.getKey().equals("IFSC Code")){
											bd.setIfscCode(String.valueOf(temp.get("IFSC Code")));
										}
										if(entry.getKey().equals("MICR Code")){
											bd.setMicrCode(String.valueOf(temp.get("MICR Code")));
										}
										if(entry.getKey().equals("Name as per Bank AC")){
											bd.setNamePerBankAccnt(String.valueOf(temp.get("Name as per Bank AC")));
										}
										if(entry.getKey().equals("Preference")){
											bd.setPreference(String.valueOf(temp.get("Preference")));
										}
										if(entry.getKey().equals("Branch Name")){
											bd.setBranchName(String.valueOf(temp.get("Branch Name")));
										}
										if(entry.getKey().equals("Bank Name")){
											bd.setNameOfBank(String.valueOf(temp.get("Bank Name")));
										}
										
									}
									listOfBd.add(bd);
								}
							}
						}
							pbd.setBankDetails(listOfBd);
							listOfPbd.add(pbd);
							bdr.setPartyBankDetails(listOfPbd);
						}
						System.out.println(bdr.toString());
					}else{
						intLog.setStatus("Success");
						System.out.println("Bancs Live - Response: Error");
						// to do whatever need to be done.....
					}
					
				
				}
			}
			DBCalculationService bancsService = new DBCalculationService();;
			bancsService.insertIntegrationLogTable(intLog);
			
		}catch(Exception exp){
			log.error("Exception occurred while calling the Bank Details service"+exp.getMessage());
			exp.printStackTrace();
		}
		return bdr;
	}

	public Map<String, String> getAuthAndRequestParam(String argRequestType) {
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
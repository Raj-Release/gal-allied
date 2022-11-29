package com.shaic.restservices.bancs.checkendomnt64vbstatus;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.shaic.arch.SHAConstants;
import com.shaic.claim.policy.search.ui.BancsSevice;
import com.shaic.claim.policy.search.ui.IntegrationLogTable;
import com.shaic.claim.policy.search.ui.PremUnique64VBStatusDetails;
import com.shaic.domain.BancsHeaderDetails;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.restservices.bancs.claimprovision.PolicySummaryResponse;
import com.shaic.restservices.bancs.lockPolicy.PolicyLockUnLockResponse;

public class Check64VBService {

	private static final Logger log = LoggerFactory.getLogger(Check64VBService.class);
	
	private static final Check64VBService instance = new Check64VBService();

	
	public static Check64VBService getInstance() {
		return instance;
	}

	public Check64VBService() {
		
	}
	
	public String view64vbStatus(String policyNumber){
	
		String jsonInString = "";
		PolicyLockUnLockResponse response = null;
		Gson gson = new Gson();
		try{
			String serviceURL = BPMClientContext.BANCS_WEB_SERVICE_URL;
			if(!serviceURL.endsWith("/")){
				serviceURL = serviceURL+"/"+"check64VBStatus";
			}else{
				serviceURL = serviceURL+"check64VBStatus";
			}
			System.out.println("Bancs Live - Url :"+serviceURL);
			URL url=new URL(serviceURL);
			HttpURLConnection urlCon=(HttpURLConnection) url.openConnection();
			urlCon.setRequestMethod("POST");
			urlCon.setRequestProperty("Content-Type", "application/json");
			urlCon.setRequestProperty("Accept", "application/json");
			Map<String, String> mapVal = getAuthAndRequestParam("ALL");
			urlCon.setRequestProperty("Authorization", mapVal.get("authkey"));
			
			Check64VBStatusDTO check64vbStatus=new Check64VBStatusDTO();
			
			check64vbStatus.setServiceTransactionId(mapVal.get("tranid"));
			check64vbStatus.setBusinessChannel(mapVal.get("business"));
			check64vbStatus.setUserCode(mapVal.get("usercode"));
			check64vbStatus.setRoleCode(mapVal.get("rolecode"));
			check64vbStatus.setPolicyNo(policyNumber != null ? policyNumber :"");
			
			
			String POST_PARAMS = "{" 
					+ "\"serviceTransactionId\":"+ "\""+check64vbStatus.getServiceTransactionId()+"\"," 
					+ "\"businessChannel\":"+ "\""+check64vbStatus.getBusinessChannel()+"\"," 
					+ "\"userCode\":"+ "\""+check64vbStatus.getUserCode()+"\"," 
					+ "\"roleCode\":"+ "\""+check64vbStatus.getRoleCode()+"\"," 
					+ "\"policyNumber\":"+ "\""+check64vbStatus.getPolicyNo()+"\"" 
					+"}";
			
			JSONObject json=new JSONObject(POST_PARAMS);
			log.info(POST_PARAMS);
			System.out.println("Bancs Live - Request:"+POST_PARAMS);
			urlCon.setDoOutput(true);
			OutputStream os=urlCon.getOutputStream();
			os.write(json.toString().getBytes());
			os.flush();
			os.close();
			IntegrationLogTable intLog = new IntegrationLogTable();
	        intLog.setOwner("Bancs");
	        intLog.setServiceName("check64VBStatus");
	        intLog.setUrl(serviceURL);
	        intLog.setRequest(POST_PARAMS);
	        intLog.setCreatedBy("GALAXY");
	        intLog.setCreatedDate(new Timestamp(System.currentTimeMillis()));
	        intLog.setActiveStatus(1l);
			
			
	        boolean isErrorCodeReceived = false;

			
			if (urlCon.getResponseCode() != 200) {
				isErrorCodeReceived = true;
				intLog.setStatus("Error");
				intLog.setResponse("Connection Error");
				intLog.setRemarks("Connection Error");
			}else if (urlCon.getResponseCode() == 200) {
				InputStreamReader in = new InputStreamReader(urlCon.getInputStream());
				BufferedReader br = new BufferedReader(in);
				String output = "";
				while ((output = br.readLine()) != null) {
					JSONObject jsonObject = new JSONObject(output);
                    jsonInString = jsonObject.toString();
					System.out.println("Response :"+jsonInString);
					response = gson.fromJson(jsonInString,PolicyLockUnLockResponse.class);
					String resDtls = output;
					if(resDtls != null && resDtls.length()>3000){
						intLog.setResponse(resDtls.substring(0, 2999));
					}else {
						intLog.setResponse(resDtls);
					}
					if(response.getErrorCode().equals("0")){
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
			urlCon.disconnect();
			
		}catch(Exception exp){
			log.error("Exception occurred while calling this service"+exp.getMessage());
			exp.printStackTrace();
			
		}
		return response.getStatus();
	}
	
	
	
	
	public List<PremUnique64VBStatusDetails> viewGetUnique64VB(String policyNumber){


		String jsonInString = "";
		List<PremUnique64VBStatusDetails> queries = null;
		try{

			String serviceURL = BPMClientContext.BANCS_WEB_SERVICE_URL;
			if(!serviceURL.endsWith("/")){
				serviceURL = serviceURL+"/"+"getUnique64VBStatus";
			}else{
				serviceURL = serviceURL+"getUnique64VBStatus";
			}
			System.out.println("Bancs Live - Url :"+serviceURL);
			URL url=new URL(serviceURL);
			HttpURLConnection urlCon=(HttpURLConnection) url.openConnection();
			urlCon.setRequestMethod("POST");
			urlCon.setRequestProperty("Content-Type", "application/json");
			urlCon.setRequestProperty("Accept", "application/json");
			Map<String, String> mapVal = getAuthAndRequestParam("ALL");
			urlCon.setRequestProperty("Authorization", mapVal.get("authkey"));

			GetUnique64VBStatusDTO getUnique64vbStatus=new GetUnique64VBStatusDTO();

			getUnique64vbStatus.setServiceTransactionId(mapVal.get("tranid"));
			getUnique64vbStatus.setBusinessChannel(mapVal.get("business"));
			getUnique64vbStatus.setUserCode(mapVal.get("usercode"));
			getUnique64vbStatus.setRoleCode(mapVal.get("rolecode"));
			getUnique64vbStatus.setPolicyNo(policyNumber != null ? policyNumber :""); //"012HFHOSTD401386878000000"


			String POST_PARAMS = "{" 
					+ "\"serviceTransactionId\":"+ "\""+getUnique64vbStatus.getServiceTransactionId()+"\"," 
					+ "\"businessChannel\":"+ "\""+getUnique64vbStatus.getBusinessChannel()+"\"," 
					+ "\"userCode\":"+ "\""+getUnique64vbStatus.getUserCode()+"\"," 
					+ "\"roleCode\":"+ "\""+getUnique64vbStatus.getRoleCode()+"\"," 
					+ "\"policyNumber\":"+ "\""+getUnique64vbStatus.getPolicyNo()+"\"" 
					+"}";

			JSONObject json=new JSONObject(POST_PARAMS);
			log.info(POST_PARAMS);
			System.out.println("Bancs Live - Request:"+POST_PARAMS);
			urlCon.setDoOutput(true);
			OutputStream os=urlCon.getOutputStream();
			os.write(json.toString().getBytes());
			os.flush();
			os.close();

			IntegrationLogTable intLog = new IntegrationLogTable();
	        intLog.setOwner("Bancs");
	        intLog.setServiceName("getUnique64VBStatus");
	        intLog.setUrl(serviceURL);
	        intLog.setRequest(POST_PARAMS);
	        intLog.setCreatedBy("GALAXY");
	        intLog.setCreatedDate(new Timestamp(System.currentTimeMillis()));
	        intLog.setActiveStatus(1l);
			boolean isErrorCodeReceived = false;
			GetUnique64VBResponseStatus response=null;

			if (urlCon.getResponseCode() != 200) {
				isErrorCodeReceived = true;
				intLog.setStatus("Error");
				intLog.setResponse("Connection Error");
				intLog.setRemarks("Connection Error");
			}else if (urlCon.getResponseCode() == 200) {
				InputStreamReader in = new InputStreamReader(urlCon.getInputStream());
				BufferedReader br = new BufferedReader(in);
				String output = "";
				while ((output = br.readLine()) != null) {
					JSONObject jsonObject = new JSONObject(output);
					ObjectMapper mapper = new ObjectMapper();

					jsonInString = jsonObject.toString();
					log.info("Response :"+jsonInString);
					String resDtls = output;
					if(resDtls != null && resDtls.length()>3000){
						intLog.setResponse(resDtls.substring(0, 2999));
					}else {
						intLog.setResponse(resDtls);
					}
					//response = mapper.readValue(jsonInString, GetUnique64VBResponseStatus.class);
					Gson gson=new Gson();
					response=gson.fromJson(jsonInString,GetUnique64VBResponseStatus.class);
					queries = response.getQueries();
					if(response.getErrorCode() == 0){
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
			urlCon.disconnect();

		}catch(Exception exp){
			log.error("Exception occurred while calling this service"+exp.getMessage());
			exp.printStackTrace();
		}

		return queries;
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

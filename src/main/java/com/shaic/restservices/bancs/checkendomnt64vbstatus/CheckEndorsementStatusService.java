package com.shaic.restservices.bancs.checkendomnt64vbstatus;

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

import com.google.gson.Gson;
import com.shaic.arch.SHAConstants;
import com.shaic.claim.policy.search.ui.BancsSevice;
import com.shaic.claim.policy.search.ui.IntegrationLogTable;
import com.shaic.domain.BancsHeaderDetails;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.restservices.bancs.lockPolicy.PolicyLockUnLockResponse;

public class CheckEndorsementStatusService {
	private static final Logger log = LoggerFactory.getLogger(CheckEndorsementStatusService.class);
	
	private static final CheckEndorsementStatusService instance = new CheckEndorsementStatusService();

	
	public static CheckEndorsementStatusService getInstance() {
		return instance;
	}

	public CheckEndorsementStatusService() {
		
	}
	
	public PolicyLockUnLockResponse viewEndoresementStatus(String PolicyNumber){
	
		String jsonInString = "";
		PolicyLockUnLockResponse pedInitiateResponse = null;
		try{
			String serviceURL = BPMClientContext.BANCS_WEB_SERVICE_URL;
			if(!serviceURL.endsWith("/")){
				serviceURL = serviceURL+"/"+"checkEndorsementStatus";
			}else{
				serviceURL = serviceURL+"checkEndorsementStatus";
			}
			System.out.println("Bancs Live - Url :"+serviceURL);
			URL url=new URL(serviceURL);
			HttpURLConnection urlCon=(HttpURLConnection) url.openConnection();
			urlCon.setRequestMethod("POST");
			urlCon.setRequestProperty("Content-Type", "application/json");
			urlCon.setRequestProperty("Accept", "application/json");
			Map<String, String> mapVal = getAuthAndRequestParam("ALL");
			urlCon.setRequestProperty("Authorization", mapVal.get("authkey"));
			
			CheckEndorsementStatusDTO checkEndStatus=new CheckEndorsementStatusDTO();
			
			checkEndStatus.setServiceTransactionId(mapVal.get("tranid"));
			checkEndStatus.setBusinessChannel(mapVal.get("business"));
			checkEndStatus.setUserCode(mapVal.get("usercode"));
			checkEndStatus.setRoleCode(mapVal.get("rolecode"));
			checkEndStatus.setPolicyNo(PolicyNumber);
			
			
			String POST_PARAMS = "{" 
					+ "\"serviceTransactionId\":"+ "\""+checkEndStatus.getServiceTransactionId()+"\"," 
					+ "\"businessChannel\":"+ "\""+checkEndStatus.getBusinessChannel()+"\"," 
					+ "\"userCode\":"+ "\""+checkEndStatus.getUserCode()+"\"," 
					+ "\"roleCode\":"+ "\""+checkEndStatus.getRoleCode()+"\"," 
					+ "\"policyNumber\":"+ "\""+checkEndStatus.getPolicyNo()+"\"" 
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
	        intLog.setServiceName("checkEndorsementStatus");
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
					log.info("Response :"+jsonInString);
					String resDtls = output;
					if(resDtls != null && resDtls.length()>3000){
						intLog.setResponse(resDtls.substring(0, 2999));
					}else {
						intLog.setResponse(resDtls);
					}
					Gson gson=new Gson();
					pedInitiateResponse=gson.fromJson(jsonInString,PolicyLockUnLockResponse.class);
					if(pedInitiateResponse.getErrorCode().equals("0")){
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
		return pedInitiateResponse;
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

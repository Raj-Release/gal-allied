package com.shaic.restservices.bancs.tiggerEndorsement;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shaic.arch.SHAConstants;
import com.shaic.ims.bpm.claim.BPMClientContext;

public class TriggerEndorsementService {
	private static final Logger log = LoggerFactory.getLogger(TriggerEndorsementService.class);
	
	public String callTriggerEndorsement(){
	
		String responseString = "";
		TriggerEndorsementResponse response = null;
		ObjectMapper mapper = new ObjectMapper();
		
		try{
			
			String serviceURL = BPMClientContext.BANCS_WEB_SERVICE_URL;
			if(!serviceURL.endsWith("/")){
				serviceURL = serviceURL+"/"+"triggerEndorsement";
			}else{
				serviceURL = serviceURL+"triggerEndorsement";
			}
			System.out.println("Bancs Live - Url :"+serviceURL);
			URL url = new URL(serviceURL); 
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");

			TriggerEndorsementDTO triggerEndosmntrequest = new TriggerEndorsementDTO();
			triggerEndosmntrequest.setServiceTransactionId("");
			triggerEndosmntrequest.setBusinessChannel("");
			triggerEndosmntrequest.setUserCode("");
			triggerEndosmntrequest.setRoleCode("");
			triggerEndosmntrequest.setOperationName("Recharge");
			triggerEndosmntrequest.setPolicyNo("012HFHOSTD401386878000000");
			triggerEndosmntrequest.setMemberCode("asd");
			triggerEndosmntrequest.setAmount(2333);
			
			String requestString = mapper.writeValueAsString(triggerEndosmntrequest);
			
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
					log.info("Response :"+responseString);
					response = mapper.readValue(responseString, TriggerEndorsementResponse.class);
					if(response.getErroCode().intValue() == 0){
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

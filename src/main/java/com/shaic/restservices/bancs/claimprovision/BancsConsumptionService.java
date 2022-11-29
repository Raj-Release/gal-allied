package com.shaic.restservices.bancs.claimprovision;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ThreadLocalRandom;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.restservices.bancs.claimprovision.policydetail.PolicyDetailsRequest;
import com.shaic.restservices.bancs.claimprovision.policydetail.PolicyDetailsResponse;

public class BancsConsumptionService {
	
	private static final Logger log = LoggerFactory.getLogger(BancsConsumptionService.class);

	public static String callPolicyDetailsService(){
		String responseString = "";
		PolicyDetailsResponse response = null;
		try{
			String serviceURL = BPMClientContext.BANCS_WEB_SERVICE_URL;
			if(!serviceURL.endsWith("/")){
				serviceURL = serviceURL+"/"+"getPolicyDetails";
			}else{
				serviceURL = serviceURL+"getPolicyDetails";
			}
			System.out.println("Bancs Live - Url :"+serviceURL);
			URL url = new URL(serviceURL); //http://10.20.2.10:8080/IIMS/rest/webservice/claim/claimProvision
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");

			PolicyDetailsRequest request = new PolicyDetailsRequest();
			request.setBusinessChannel("LABPORTAL");
			request.setUserCode("T966297");
			request.setRoleCode("SUPERUSER");
			request.setPolicyNumber("012HFHOSTD401386878000000");//P/151118/01/2020/001590

			ObjectMapper mapper = new ObjectMapper();
			String requestString = mapper.writeValueAsString(request);

			JSONObject json = new JSONObject(requestString);
			log.info("Request To Policy Details: "+requestString);
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
					response = mapper.readValue(responseString, PolicyDetailsResponse.class);
				}
			}
			conn.disconnect();

		}catch(Exception exp){
			log.error("Exception occurred while calling the provision service"+exp.getMessage());
			exp.printStackTrace();
		}
		return responseString;
	}
}

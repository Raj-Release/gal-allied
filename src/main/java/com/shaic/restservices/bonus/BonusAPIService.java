package com.shaic.restservices.bonus;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shaic.ims.bpm.claim.BPMClientContext;

public class BonusAPIService {
	
	private static final Logger log = LoggerFactory.getLogger(BonusAPIService.class);

	public static BonusResponse getBonusDetails(String argPolicyNumber, String argHealthId, String argProductCode){
		BonusResponse readValue = null;
		try{
			String serviceURL = BPMClientContext.VALIDATE_SERVICE_URL;
			URL url = new URL(serviceURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");

			BonusRequest request = new BonusRequest();
			request.setPolicyNumber(argPolicyNumber);
			request.setHealthId(argHealthId);
			request.setProductCode(argProductCode);

			ObjectMapper mapper = new ObjectMapper();
			String requestString = mapper.writeValueAsString(request);

			JSONObject json = new JSONObject(requestString);
			log.info("Request To Validate API : "+requestString);
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
					String responseString = jsonObject.toString();
//					log.info("Response :"+responseString);
					readValue = mapper.readValue(responseString, BonusResponse.class);
					log.info("Response :"+readValue.toString());
				}
			}
			conn.disconnect();

		}catch(Exception exp){
			log.error("Exception occurred while calling the validate api service"+exp.getMessage());
			exp.printStackTrace();
		}

		return readValue;
	}
	
	public static void main(String[] args) {
		BonusAPIService.getBonusDetails("", "", "");
	}

}

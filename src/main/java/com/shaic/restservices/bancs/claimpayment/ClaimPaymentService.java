package com.shaic.restservices.bancs.claimpayment;

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


public class ClaimPaymentService {
	

	
	private static final Logger log = LoggerFactory.getLogger(ClaimPaymentService.class);

	
	public static String callClaimPaymenService(){
		String responseString = "";
		try{
			String serviceURL = BPMClientContext.BANCS_WEB_SERVICE_URL;
			if(!serviceURL.endsWith("/")){
				serviceURL = serviceURL+"/"+"claimPayment";
			}else{
				serviceURL = serviceURL+"claimPayment";
			}
			System.out.println("Bancs Live - Url :"+serviceURL);
			URL url = new URL(serviceURL); //http://10.20.2.10:8080/IIMS/rest/webservice/claim/claimPayment
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");

			ClaimPaymentDetails request = new ClaimPaymentDetails();
			request.setRequestId("");
			request.setUprID("");
			request.setInvoiceType("Normal");
			request.setReProcessType("Rejection");
			request.setPriorityFlag("Normal");
			request.setPartyCode("");
			request.setClaimNumber("");
			request.setPolicyNumber("");
			request.setLob("11");
			request.setCpu("");
			request.setPaymentCPUCode("");
			request.setPaymentType("Cashless");
			request.setPaymentMode("NEFT");
			request.setTransactionDate(null);
			request.setPaymentPartyCode("");
			request.setPaymentPartyStake("");
			request.setAccountPreferance("Primary");
			request.setApprovedAmount(1);
			request.setDdPayableAt("");
			request.setDdPayableName("");
			request.setLegalHeirName("");
			request.setAccountHolName("");
			request.setAccountNumber("");
			request.setIfscCode("");
			request.setAddress1("");
			request.setAddress2("");
			request.setPincode("");
			
			
			ObjectMapper mapper = new ObjectMapper();
			String requestString = mapper.writeValueAsString(request);
			
			JSONObject json = new JSONObject(requestString);
			log.info("Request To Claim Payment : "+requestString);
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
				}
			}
			conn.disconnect();
					
		}catch(Exception exp){
			log.error("Exception occurred while calling the claim payment service"+exp.getMessage());
			exp.printStackTrace();
		}
		return responseString;
	}
}

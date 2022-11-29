package com.shaic.restservices.bancs.receivepedquery;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shaic.domain.preauth.IcdBlock;
import com.shaic.domain.preauth.IcdChapter;
import com.shaic.domain.preauth.IcdCode;
import com.shaic.domain.preauth.NewInitiatePedEndorsement;
import com.shaic.domain.preauth.PreExistingDisease;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.restservices.bancs.initiateped.ComplexPedDetails;
import com.shaic.restservices.bancs.initiateped.InitiatePedDetails;

public class UpdatePEDQueryResponseService {
	

	
	private static final Logger log = LoggerFactory.getLogger(UpdatePEDQueryResponseService.class);

	public static String callQueryResponseService(){
		String responseString = "";
		try{
			String serviceURL = BPMClientContext.BANCS_WEB_SERVICE_URL;
			if(!serviceURL.endsWith("/")){
				serviceURL = serviceURL+"/"+"updatePedQueryResponse";
			}else{
				serviceURL = serviceURL+"updatePedQueryResponse";
			}
			System.out.println("Bancs Live - Url :"+serviceURL);
			URL url = new URL(serviceURL); //http://10.20.2.10:8080/IIMS/rest/webservice/claim/updatePEDQueryResponse
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");

			UpdatePEDQueryResponseDetails request = new UpdatePEDQueryResponseDetails();
			request.setBusinessChannel("LABPORTAL");
			 request.setServiceTransactionId("1234");
			 request.setUserCode("T966297");
			 request.setRoleCode("SUPERUSER");
			 request.setPolicyNumber("P/151118/01/2020/001590");
			 request.setMemberCode("PI19488325");
			 request.setClaimNumber("CLI/2020/151118/0300955");
			 request.setWorkItemID(1234l);
			 UpdatePEDQueryComplexQueryDetails complexPedDetails = new UpdatePEDQueryComplexQueryDetails();
			 List<UpdatePEDQueryComplexQueryDetails> complexPedDetailsList = new ArrayList<UpdatePEDQueryComplexQueryDetails>();
			 complexPedDetails.setQueryId(1232423l);
			 complexPedDetails.setRepliedRoleCode("Test PED");
			 complexPedDetails.setRaisedDate("14-05-2019 12:00:54 PM");
			 complexPedDetails.setReply("test");
			 complexPedDetails.setRepliedUserCode("234234");
			 complexPedDetailsList.add(complexPedDetails);	 
			 request.setQuery(complexPedDetailsList);
				
	
			
			ObjectMapper mapper = new ObjectMapper();
			String requestString = mapper.writeValueAsString(request);
			
			JSONObject json = new JSONObject(requestString);
			log.info("Request To Query Response : "+requestString);
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
			log.error("Exception occurred while calling the QueryResponse service"+exp.getMessage());
			exp.printStackTrace();
		}
		return responseString;
	}
}
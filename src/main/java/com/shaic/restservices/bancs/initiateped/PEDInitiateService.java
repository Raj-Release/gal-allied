package com.shaic.restservices.bancs.initiateped;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.shaic.claim.policy.search.ui.IntegrationLogTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.restservices.GLXDBService;
import com.shaic.restservices.WSConstants;
import com.shaic.restservices.bancs.sendped.PEDUpdateService;

@Path("/claims")
@Stateless
public class PEDInitiateService {
	
	@Inject
	GLXDBService dbService;
	
	PEDInitiateTransaction pEDInitiateTransaction;
	
	private final Logger log = LoggerFactory.getLogger(PEDUpdateService.class);
	
	@POST
    @Path("/initiateClaimPEDEndorsement")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    public String checkPolicyData(PEDInitiateRequest pedObject, @HeaderParam("authorization") String authString) throws JsonParseException, JsonMappingException, IOException, JSONException {
		JSONObject reqStatus = new JSONObject();		
		ObjectMapper mapper = new ObjectMapper();
		String responseString = "";
		
		String serviceURL = BPMClientContext.BANCS_WEB_SERVICE_URL;
		if(!serviceURL.endsWith("/")){
			serviceURL = serviceURL+"/"+"initiateClaimPedEndorsement";
		}else{
			serviceURL = serviceURL+"initiateClaimPedEndorsement";
		}
		System.out.println("Bancs Live - Url :"+serviceURL);
		URL url = new URL(serviceURL); //http://10.20.2.10:8080/IIMS/rest/webservice/claim/initiateClaimPedEndorsement
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Accept", "application/json");
		log.info("PED REQ : "+mapper.writeValueAsString(pedObject));
		System.out.println("Bancs Live - Request:"+mapper.writeValueAsString(pedObject));
		
		try{
			boolean validationErrorFlag = false;
			StringBuilder docErrorMsg = new StringBuilder();
			if(!StringUtils.isBlank(authString)){
				if(authString.trim().equals(BPMClientContext.BANCS_EXP_WS_AUTH_KEY)){
					if(pedObject.getPedInitiateKey().equals("") || pedObject.getPedInitiateKey().equalsIgnoreCase("null")){
						validationErrorFlag = true;
						reqStatus.put(WSConstants.ERROR_DESCRIPTION, "Error - PED initiate Key is Empty or null");
						reqStatus.put(WSConstants.ERROR_CODE, "600");
					}

					if(!validationErrorFlag && StringUtils.isBlank(docErrorMsg.toString())){
						InitiatePedDetails pedInitiateObject = dbService.getPEDDetails(Long.valueOf(pedObject.getPedInitiateKey()));
						//String requestFormat = dbService.getRequestFormat(pedInitiateObject);
						String requestString = mapper.writeValueAsString(pedInitiateObject);

						JSONObject json = new JSONObject(requestString);
						System.out.println("Request To Initiate PED : "+requestString);
						conn.setDoOutput(true);
						OutputStream os = conn.getOutputStream();
						os.write(json.toString().getBytes());
						os.flush();
						os.close();
						IntegrationLogTable intLog = new IntegrationLogTable();
				        intLog.setOwner("Bancs");
				        intLog.setServiceName("initiateClaimPedEndorsement");
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
								System.out.println("Response :"+responseString);
								String resDtls = output;
								if(resDtls != null && resDtls.length()>3000){
									intLog.setResponse(resDtls.substring(0, 2999));
								}else {
									intLog.setResponse(resDtls);
								}
								Gson gson=new Gson();
								PEDInitiateResponse pedInitiateResponse=gson.fromJson(responseString,PEDInitiateResponse.class);
								pEDInitiateTransaction.doPEDTrans(pedInitiateResponse,pedInitiateObject.getServiceTransactionId());
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
						conn.disconnect();
					}
				}else{
					reqStatus.put(WSConstants.ERROR_DESCRIPTION, "Error - Authentication Error, Invalid Authorization Key");
					reqStatus.put(WSConstants.ERROR_CODE, "600");
				}
			}else{
				reqStatus.put(WSConstants.ERROR_DESCRIPTION, "Error - Authentication Error, Invalid Authorization Key");
				reqStatus.put(WSConstants.ERROR_CODE, "600");
			}
		}catch(Exception exp){
			reqStatus.put(WSConstants.REQ_STATUS, "Error"+" - "+exp.getMessage());
			log.info("Exception occured in Initiate PED service"+exp.getMessage());
		}	
		return responseString.toString();
    }
}

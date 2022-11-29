package com.shaic.restservices;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/v1")
@Stateless
public class GalaxyQueryResponseService {

	private final Logger log = LoggerFactory.getLogger(GalaxyQueryResponseService.class);
	
	@Inject
	GatewayDBService dbService;
	
	@POST
	@Path("/claims/{claimNumber}/queries/{queryId}/response")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String searchClaimQuerys(@PathParam("claimNumber") String argClaimNumber, @PathParam("queryId") String argQueryId, GalaxyQueryResponseRequest searchObj, @HeaderParam("Authorization") String argParam) throws JSONException {
		String responseString = "";
		try{
			if(!StringUtils.isBlank(argParam) && argParam.contains("Basic")){
				String[] arrayOne = argParam.split(" ");
				String decodedValue = new String(Base64.decodeBase64(arrayOne[1].getBytes()));
				if(decodedValue.contains(":")){
					String[] arrayTwo = decodedValue.split(":");
					searchObj.setUsername(arrayTwo[0]);
					searchObj.setPassword(arrayTwo[1]);
					if(dbService.validateRequest(searchObj)){
						boolean validationErrorFlag = false;
						if((StringUtils.isBlank(argClaimNumber) || argClaimNumber.equals("null"))){
							validationErrorFlag = true;
							JSONObject errorMsg = new JSONObject();
							errorMsg.put("resultMsg", "Error Occurred");
							errorMsg.put("errorYN", "Y");
							JSONObject errObj = new JSONObject();
							JSONArray errArray = new JSONArray();
							errObj.put("error", "ClaimNumber is Mandatory");
							errArray.put(errObj);
							errorMsg.put("errors", errArray);
							responseString = errorMsg.toString();
						}
						if((StringUtils.isBlank(argQueryId) || argQueryId.equals("null"))){
							validationErrorFlag = true;
							JSONObject errorMsg = new JSONObject();
							errorMsg.put("resultMsg", "Error Occurred");
							errorMsg.put("errorYN", "Y");
							JSONObject errObj = new JSONObject();
							JSONArray errArray = new JSONArray();
							errObj.put("error", "QueryId is Mandatory");
							errArray.put(errObj);
							errorMsg.put("errors", errArray);
							responseString = errorMsg.toString();
						}
						if(!validationErrorFlag){
							System.out.println("ClaimNo "+argClaimNumber);
							searchObj.setClaimNumber(argClaimNumber);
							searchObj.setQueryId(argQueryId);
							responseString = dbService.doSubmitQueryresponse(searchObj);
						}
					}else{
						JSONObject errorMsg = new JSONObject();
						errorMsg.put("resultMsg", "Error Occurred");
						errorMsg.put("errorYN", "Y");
						JSONObject errObj = new JSONObject();
						JSONArray errArray = new JSONArray();
						errObj.put("error", "Invalid Username/Password");
						errArray.put(errObj);
						errorMsg.put("errors", errArray);
						responseString = errorMsg.toString();
					}
				}else{
					JSONObject errorMsg = new JSONObject();
					errorMsg.put("resultMsg", "Error Occurred");
					errorMsg.put("errorYN", "Y");
					JSONObject errObj = new JSONObject();
					JSONArray errArray = new JSONArray();
					errObj.put("error", "Invalid Authorization Parameters");
					errArray.put(errObj);
					errorMsg.put("errors", errArray);
					responseString = errorMsg.toString();
				}
			}else{
				JSONObject errorMsg = new JSONObject();
				errorMsg.put("resultMsg", "Error Occurred");
				errorMsg.put("errorYN", "Y");
				JSONObject errObj = new JSONObject();
				JSONArray errArray = new JSONArray();
				errObj.put("error", "Invalid Authorization Parameters");
				errArray.put(errObj);
				errorMsg.put("errors", errArray);
				responseString = errorMsg.toString();
			}
		}catch(Exception exp){
			log.info("Exception occurred while submitting response in galaxy service"+exp.getMessage());
			JSONObject errorMsg = new JSONObject();
			errorMsg.put("resultMsg", "Error Occurred");
			errorMsg.put("errorYN", "Y");
			JSONObject errObj = new JSONObject();
			JSONArray errArray = new JSONArray();
			errObj.put("error", exp.getMessage());
			errArray.put(errObj);
			errorMsg.put("errors", errArray);
			responseString = errorMsg.toString();
		}
		
		return responseString;
	}
}

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
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/v1")
@Stateless
public class GalaxyViewQueryService {

	private final Logger log = LoggerFactory.getLogger(GalaxyViewQueryService.class);
	
	@Inject
	GatewayDBService dbService;
	
	@POST
	@Path("/claims/{claimNumber}/queries")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String searchClaimQuerys(@PathParam("claimNumber") String argClaimNumber, @HeaderParam("Authorization") String argParam) {
		String responseString = "";
		try{
			GalaxyViewQueryRequest searchObj = new GalaxyViewQueryRequest();
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
						if(!validationErrorFlag){
							System.out.println("ClaimNo "+argClaimNumber);
							responseString = dbService.getClaimQuery(argClaimNumber);
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
			log.info("Exception occurred while creating ROD in galaxy service"+exp.getMessage());
		}
		
		return responseString;
	}
}

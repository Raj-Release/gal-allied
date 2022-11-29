package com.shaic.restservices;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
public class GalaxySearchService {

	private final Logger log = LoggerFactory.getLogger(GalaxySearchService.class);
	
	@Inject
	GatewayDBService dbService;
	
	@POST
    @Path("/claimsearchbyagent")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String searchGalaxyIntimation(GalaxySearchRequest searchObj, @HeaderParam("Authorization") String argParam) {
		System.out.println(argParam);
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
						if(searchObj != null){
							if(searchObj.getOffset() > 50){
								searchObj.setOffset(50);
							}
							responseString = dbService.performSearch(searchObj);
						}
					}else{
						JSONArray dataArr = new JSONArray();
						JSONObject errorMsg = new JSONObject();
						errorMsg.put("data", dataArr);
						errorMsg.put("message", "Invalid Username/Password");
						errorMsg.put("resCode", 401);
						responseString = errorMsg.toString();
					}
				}else{
					JSONArray dataArr = new JSONArray();
					JSONObject errorMsg = new JSONObject();
					errorMsg.put("data", dataArr);
					errorMsg.put("message", "Invalid Authorization Parameters");
					errorMsg.put("resCode", 401);
					responseString = errorMsg.toString();
				}
			}else{
				JSONArray dataArr = new JSONArray();
				JSONObject errorMsg = new JSONObject();
				errorMsg.put("data", dataArr);
				errorMsg.put("message", "Invalid Authorization Parameters");
				errorMsg.put("resCode", 401);
				responseString = errorMsg.toString();
			}
		}catch(Exception exp){
			log.info("Exception occurred while performing search "+exp.getMessage());
		}
		return responseString;
	}
}

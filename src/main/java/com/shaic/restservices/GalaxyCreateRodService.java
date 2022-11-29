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
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/v1")
@Stateless
public class GalaxyCreateRodService {
	
	private final Logger log = LoggerFactory.getLogger(GalaxyCreateRodService.class);

	@Inject
	GatewayDBService dbService;

	@POST
	@Path("/claims/createROD")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String searchClaim(GalaxyCreateRodRequest searchObj, @HeaderParam("Authorization") String argParam) throws JSONException {
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
						StringBuilder docErrorMsg = new StringBuilder();
						boolean validationErrorFlag = false;
						if((StringUtils.isBlank(searchObj.getClaimNumber()) || searchObj.getClaimNumber().equals("null"))){
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
						}else if(!validationErrorFlag && (searchObj.getClassifications() != null  && searchObj.getClassifications().isEmpty())){
							validationErrorFlag = true;
							JSONObject errorMsg = new JSONObject();
							errorMsg.put("resultMsg", "Error Occurred");
							errorMsg.put("errorYN", "Y");
							JSONObject errObj = new JSONObject();
							JSONArray errArray = new JSONArray();
							errObj.put("error", "Classifications should not be empty");
							errArray.put(errObj);
							errorMsg.put("errors", errArray);
							responseString = errorMsg.toString();
						}else {
							if(!validationErrorFlag && (searchObj.getDocuments() != null  && searchObj.getDocuments().size() == 0)){
								validationErrorFlag = true;
								JSONObject errorMsg = new JSONObject();
								errorMsg.put("resultMsg", "Error Occurred");
								errorMsg.put("errorYN", "Y");
								JSONObject errObj = new JSONObject();
								JSONArray errArray = new JSONArray();
								errObj.put("error", "Documents should not be empty");
								errArray.put(errObj);
								errorMsg.put("errors", errArray);
								responseString = errorMsg.toString();
							}else{
								if(!validationErrorFlag && (searchObj.getDocuments() != null && searchObj.getDocuments().size() > 0)){
									for(GalaxyDocuments reqDocDetails : searchObj.getDocuments()){
										if(StringUtils.isBlank(reqDocDetails.getDocumentName()) || reqDocDetails.getDocumentName().equals("null")){
											docErrorMsg.append(" Document Name is Empty or null ");
										}
										if(StringUtils.isBlank(reqDocDetails.getDocumentType()) || reqDocDetails.getDocumentType().equals("null")){
											docErrorMsg.append(" Document Type is Empty or null ");
										}
										if(StringUtils.isBlank(reqDocDetails.getDocumentContent()) || reqDocDetails.getDocumentContent().equals("null")){
											docErrorMsg.append(" Document Content is Empty or null ");
										}

										if(!StringUtils.isBlank(docErrorMsg.toString())){
											break;
										}else{
											continue;
										}
									}

									if(!StringUtils.isBlank(docErrorMsg.toString())){
										JSONObject errorMsg = new JSONObject();
										errorMsg.put("resultMsg", "Error Occurred");
										errorMsg.put("errorYN", "Y");
										JSONObject errObj = new JSONObject();
										JSONArray errArray = new JSONArray();
										errObj.put("error", docErrorMsg.toString());
										errArray.put(errObj);
										errorMsg.put("errors", errArray);
										responseString = errorMsg.toString();
									}
								}
							}
						}
						
						if(!validationErrorFlag && StringUtils.isBlank(docErrorMsg.toString())){
							responseString = dbService.createRod(searchObj);
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
		}catch(Exception ex){
			log.info("Exception occurred while creating ROD in galaxy service"+ex.getMessage());
			JSONObject errorMsg = new JSONObject();
			errorMsg.put("resultMsg", "Error Occurred");
			errorMsg.put("errorYN", "Y");
			JSONObject errObj = new JSONObject();
			JSONArray errArray = new JSONArray();
			errObj.put("error", ex.getMessage());
			errArray.put(errObj);
			errorMsg.put("errors", errArray);
			responseString = errorMsg.toString();
		}
		return responseString;
	}
}

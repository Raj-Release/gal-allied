package com.shaic.restservices;

import java.io.IOException;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shaic.ims.bpm.claim.BPMClientContext;



@Path("/glx-srms")
@Stateless
public class SRMSService{
	
	@Inject
	GLXDBService dbService;
	
	private final Logger log = LoggerFactory.getLogger(SRMSService.class);
	
	@POST
    @Path("/claim-info")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    public String checkPolicyData(SRMSRequest reqObj, @HeaderParam("authorization") String authString) throws JsonParseException, JsonMappingException, IOException, JSONException {
		JSONObject reqStatus = new JSONObject();		
		JSONArray dataArr = new JSONArray();
		ObjectMapper mapper = new ObjectMapper();
		log.info("SRMS REQ : "+mapper.writeValueAsString(reqObj));
		
		try{
			boolean validationErrorFlag = false;
			authString =  authString.replace("Bearer=", "");
			if(authString.trim().equals(BPMClientContext.SRMS_AUTH_KEY)){
				if(reqObj.getIntimationNo().equals("") || reqObj.getIntimationNo().equalsIgnoreCase("null")){
					validationErrorFlag = true;
					reqStatus.put(WSConstants.REQ_STATUS, "Error - Intimation Number is Empty or null");
				}

				JSONObject clmDetails = null;
				if(!validationErrorFlag){
					clmDetails = dbService.getIntimationDetailsToGrievance(reqObj);
					reqStatus.put(WSConstants.REQ_STATUS, "Success");
					if(clmDetails != null){
						dataArr.put(clmDetails);
					}
					reqStatus.put("data", dataArr);
				}
			}else{
				reqStatus.put(WSConstants.REQ_STATUS, "Error - Authentication Error, Invalid Authorization Key");
			}
		}catch(Exception exp){
			reqStatus.put(WSConstants.REQ_STATUS, "Error"+" - "+exp.getMessage());
			log.info("Exception occured in Grievances service "+exp.getMessage());
		}	
		return reqStatus.toString();
    }
}

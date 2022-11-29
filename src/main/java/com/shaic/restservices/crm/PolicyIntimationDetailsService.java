package com.shaic.restservices.crm;

import java.io.IOException;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shaic.arch.SHAConstants;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.restservices.WSConstants;

@Path("/crm")
public class PolicyIntimationDetailsService {

	private final Logger log = LoggerFactory.getLogger(PolicyIntimationDetailsService.class);
	private final String regEx = "[: =]";
	
	@EJB
	private CRMService dbService;
	
	@POST
	@Path("/getIntimationDetails")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String getIntimationDetails(IntimationDetailsRequest request, @HeaderParam("authorization") String authString) throws JsonGenerationException, JsonMappingException, IOException {
		JSONObject reqStatus = new JSONObject();	
		ObjectMapper mapper = new ObjectMapper();
		log.info("CRM INTI DETAILS REQ : "+mapper.writeValueAsString(request));
		String reponseVal = "";
		try{
			authString = authString.replaceAll("Bearer", "");
			authString = authString.replaceAll("bearer", "");
			authString = authString.replaceAll(regEx, "");

			if((authString != null) && (authString.equals(BPMClientContext.CRM_WS_LIST_INTIMATION_KEY))) {
				if(request.getPolicyNumber().equals("") || request.getPolicyNumber().equalsIgnoreCase("null")){
					reqStatus.put("resCode", 600);
					reqStatus.put(WSConstants.REQ_STATUS, "Error - Policy Number is Empty or null");
					log.info("Error - Policy Number is Empty or null");
					reponseVal = reqStatus.toString();
				}else{
					reponseVal = dbService.getDetailsForPolicy(request);
				}
			}else{
				reqStatus.put("resCode", 600);
				reqStatus.put(WSConstants.REQ_STATUS, "Error - Authentication Error, Invalid Authorization Key");
				log.info("Error - Authentication Error, Invalid Authorization Key");
				reponseVal = reqStatus.toString();
			}
		}catch(Exception exp){
			exp.printStackTrace();
			log.error("Error -"+exp.getMessage());
		}
		return reponseVal;
	}
}

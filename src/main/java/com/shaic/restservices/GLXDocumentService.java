package com.shaic.restservices;

import java.io.IOException;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
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



@Path("/entry-glx")
@Stateless
public class GLXDocumentService{
	
	@Inject
	GLXDBService dbService;
	
	private final Logger log = LoggerFactory.getLogger(GLXDocumentService.class);
	
	@POST
    @Path("/document-details")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    public String checkPolicyData(GLXDocRequest detailObj) throws JsonParseException, JsonMappingException, IOException, JSONException {
		JSONObject reqStatus = new JSONObject();		
		try{
			JSONArray listOfDocDetails = dbService.getDocumentDetailsForIntimation(detailObj);
			reqStatus.put(WSConstants.REQ_STATUS, WSConstants.STATUS_SUCCESS);
			reqStatus.put("resCode", "200");
			reqStatus.put(WSConstants.DOCUMENT_DETAILS, listOfDocDetails);
		}catch(Exception exp){
			reqStatus.put(WSConstants.REQ_STATUS, WSConstants.STATUS_FAILURE);
			reqStatus.put("resCode", "200");
			reqStatus.put(WSConstants.DOCUMENT_DETAILS, "");
			reqStatus.put(WSConstants.RES_ERROR, exp.getMessage());
			log.info("Exception occured in while fetching document details"+exp.getMessage());
		}	
		return reqStatus.toString();
    }
}

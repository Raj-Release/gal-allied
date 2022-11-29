package com.shaic.restservices;

import java.io.IOException;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shaic.domain.DocAcknowledgement;



@Path("/entry-query")
@Stateless
public class QueryReceiptService{
	
	@Inject
	GatewayDBService dbService;
	
	private final Logger log = LoggerFactory.getLogger(QueryReceiptService.class);
	
	@POST
    @Path("/query-receipt")
	@Consumes(MediaType.APPLICATION_JSON)
    public String checkPolicyData(RODData rodObject) throws JsonParseException, JsonMappingException, IOException, JSONException {
		JSONObject reqStatus = new JSONObject();
		try{
			DocAcknowledgement docAckObj = dbService.doProceedIntimationProcess("Query-Reply", rodObject);
			reqStatus.put(WSConstants.REQ_STATUS, WSConstants.STATUS_SUCCESS);
			reqStatus.put(WSConstants.RES_MESSAGE, docAckObj.getAcknowledgeNumber());
		}catch(Exception exp){
			reqStatus.put(WSConstants.REQ_STATUS, WSConstants.STATUS_FAILURE);
			reqStatus.put(WSConstants.RES_MESSAGE, "");
			reqStatus.put(WSConstants.RES_ERROR, exp.getMessage());
			log.info("Exception occurred in query receipt service"+ exp.getMessage());
		}	
		return reqStatus.toString();
    }	
}

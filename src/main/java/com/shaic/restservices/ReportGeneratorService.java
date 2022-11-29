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

@Path("/entry-rod")
@Stateless
public class ReportGeneratorService {
	
	@Inject
	GatewayDBService dbService;

	private final Logger log = LoggerFactory.getLogger(ReportGeneratorService.class);
	
	@POST
    @Path("/create-report")
	@Consumes(MediaType.APPLICATION_JSON)
    public String generateReport(ReportData reportData) throws JsonParseException, JsonMappingException, IOException, JSONException {
		JSONObject reqStatus = new JSONObject();
		try{
			DocAcknowledgement docAckObj = dbService.getDocAcknowledgmentByIntimation(reportData.getIntimationNumber());
			if(docAckObj != null){
				// call doc generation method.
				log.info("Letter Generation Request processing started...");
				dbService.generateParametersForAckReport(reportData.getIntimationNumber(), docAckObj);
				reqStatus.put(WSConstants.REQ_STATUS, WSConstants.STATUS_SUCCESS);
				reqStatus.put(WSConstants.RES_MESSAGE, "");
			}else{
				log.info("Letter Generation Request failed since docAck is null");
				reqStatus.put(WSConstants.REQ_STATUS, WSConstants.STATUS_FAILURE);
				reqStatus.put(WSConstants.RES_MESSAGE, "");
			}
		}catch(Exception exp){
			reqStatus.put(WSConstants.REQ_STATUS, WSConstants.STATUS_FAILURE);
			reqStatus.put(WSConstants.RES_MESSAGE, "");
			reqStatus.put(WSConstants.RES_ERROR, exp.getMessage());
			log.info("Exception occured in rod creation"+exp.getMessage());
		}	
		return reqStatus.toString();
	}
}

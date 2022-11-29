package com.shaic.restservices.rodletter;

import java.io.IOException;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shaic.domain.DocAcknowledgement;
import com.shaic.restservices.GatewayDBService;
import com.shaic.restservices.WSConstants;

@Path("/entry-rod")
@Stateless
public class RodSubmissionLetterService {
	
	@Inject
	GatewayDBService dbService;

	private final Logger log = LoggerFactory.getLogger(RodSubmissionLetterService.class);
	
	@POST
    @Path("/create-report-rod")
	@Consumes(MediaType.APPLICATION_JSON)
    public String generateReport(RODLetterRequest reportData) throws JsonParseException, JsonMappingException, IOException, JSONException {
		JSONObject reqStatus = new JSONObject();
		boolean isValidationStatus = false;
		try{
			if(StringUtils.isBlank(reportData.getIntimationNumber())){
				isValidationStatus = true;
				log.info("Letter Generation Request failed since intimation no is null");
				reqStatus.put(WSConstants.REQ_STATUS, WSConstants.STATUS_FAILURE);
				reqStatus.put(WSConstants.RES_MESSAGE, "IntimationNumber is Mandatory");
			}
			if(!isValidationStatus && StringUtils.isBlank(reportData.getRodNumber())){
				isValidationStatus = true;
				log.info("Letter Generation Request failed since rod no is null");
				reqStatus.put(WSConstants.REQ_STATUS, WSConstants.STATUS_FAILURE);
				reqStatus.put(WSConstants.RES_MESSAGE, "RodNumber is Mandatory");
			}
			DocAcknowledgement docAckObj = dbService.getDocAcknowledgmentByROD(reportData.getRodNumber());
			if(!isValidationStatus && docAckObj != null){
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

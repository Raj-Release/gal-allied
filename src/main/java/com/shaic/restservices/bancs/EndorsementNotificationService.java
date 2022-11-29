package com.shaic.restservices.bancs;

import java.io.IOException;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shaic.domain.PremiaEndorsementTable;
import com.shaic.domain.Product;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.restservices.WSConstants;


@Path("/claims")
@Stateless
public class EndorsementNotificationService {
	
	@Inject
	GLXWBService wbService;
	
	private final Logger log = LoggerFactory.getLogger(EndorsementNotificationService.class);
	
	@POST
    @Path("/sendEndorsementNotification")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String checkEndorsementData(EndorsementNotificationRequest detailObj , @HeaderParam("authorization") String authString) throws JsonParseException, JsonMappingException, IOException, JSONException {
		JSONObject reqStatus = new JSONObject();	
		ObjectMapper mapper = new ObjectMapper();
		log.info("SEND Endorsement REQ  : "+mapper.writeValueAsString(detailObj));
		try{
			boolean validationErrorFlag = false;
			StringBuilder docErrorMsg = new StringBuilder();
			if(authString.trim().equals(BPMClientContext.BANCS_EXP_WS_AUTH_KEY)){
			if(StringUtils.isBlank(detailObj.getPolicyId())){
				validationErrorFlag = true;
				reqStatus.put(WSConstants.ERROR_CODE, "600");
				reqStatus.put(WSConstants.ERROR_DESCRIPTION, "Error - PolicyId is Empty or null");
			}else if(!validationErrorFlag && (StringUtils.isBlank(detailObj.getPolicyNumber()))){
				validationErrorFlag = true;
				reqStatus.put(WSConstants.ERROR_CODE, "600");
				reqStatus.put(WSConstants.ERROR_DESCRIPTION, "Error - Policy Number is Empty or null");
			}else if(!validationErrorFlag && (detailObj.getPolicyEndorsementNumber().equals("") || detailObj.getPolicyEndorsementNumber().equals("null"))){
				validationErrorFlag = true;
				reqStatus.put(WSConstants.ERROR_CODE, "600");
				reqStatus.put(WSConstants.ERROR_DESCRIPTION, "Error - Policy Endosement Number is Empty or null");
			}else if(!validationErrorFlag && (StringUtils.isBlank(detailObj.getProductCode()))){
				validationErrorFlag = true;
				reqStatus.put(WSConstants.ERROR_CODE, "600");
				reqStatus.put(WSConstants.ERROR_DESCRIPTION, "Error - Product Code is Empty or null");
			}else if(!validationErrorFlag && (StringUtils.isBlank(detailObj.getEndDate()))){
				validationErrorFlag = true;
				reqStatus.put(WSConstants.ERROR_CODE, "600");
				reqStatus.put(WSConstants.ERROR_DESCRIPTION, "Error - End Date is Empty or null");
			}
			if(!validationErrorFlag && StringUtils.isBlank(docErrorMsg.toString())){
				Product endNotifyData = wbService.getProductByProductCode(detailObj.getProductCode());
				if(endNotifyData !=null){
				EndorsementNotificationTable endData = wbService.EndorsementNotificationData(detailObj,endNotifyData);
				if(endData != null){
					reqStatus.put(WSConstants.ERROR_CODE, "200");
					reqStatus.put(WSConstants.ERROR_DESCRIPTION, WSConstants.STATUS_SUCCESS);
				}else{
					reqStatus.put(WSConstants.ERROR_CODE, "600");
					reqStatus.put(WSConstants.ERROR_DESCRIPTION, "Error"+" - "+endData);
				}
				}
				else{
					reqStatus.put(WSConstants.ERROR_CODE, "600");
					reqStatus.put(WSConstants.ERROR_DESCRIPTION, "Error"+" - "+"This product is not available in Galaxy");
				}
			}
			}else{
				reqStatus.put(WSConstants.ERROR_CODE, "600");
				reqStatus.put(WSConstants.ERROR_DESCRIPTION, "Error - Authentication Error, Invalid Authorization Key");
			}
		}catch(Exception exp){
			reqStatus.put(WSConstants.ERROR_CODE, "600");
			reqStatus.put(WSConstants.ERROR_DESCRIPTION, exp.getMessage());
			log.error("Exception occured in while fetching Endorsement details"+exp.getMessage());
		}	
		log.info(reqStatus.toString());
		return reqStatus.toString();
    }
	

}
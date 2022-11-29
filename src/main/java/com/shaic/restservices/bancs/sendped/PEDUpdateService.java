package com.shaic.restservices.bancs.sendped;

import java.io.IOException;

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
import org.eclipse.jetty.util.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.restservices.GatewayDBService;
import com.shaic.restservices.WSConstants;

@Path("/claims")
@Stateless
public class PEDUpdateService {
	
	@Inject
	GatewayDBService dbService;
	
	private final Logger log = LoggerFactory.getLogger(PEDUpdateService.class);
	
	@POST
    @Path("/sendPEDStatus")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    public String checkPolicyData(PEDSendDetails pedObject, @HeaderParam("authorization") String authString) throws JsonParseException, JsonMappingException, IOException, JSONException {
		JSONObject reqStatus = new JSONObject();		
		ObjectMapper mapper = new ObjectMapper();
		log.info("PED REQ : "+mapper.writeValueAsString(pedObject));
		
		try{
			boolean validationErrorFlag = false;
			StringBuilder docErrorMsg = new StringBuilder();
			if(!StringUtils.isBlank(authString)){
				if(authString.trim().equals(BPMClientContext.BANCS_EXP_WS_AUTH_KEY)){
					if(StringUtils.isBlank(pedObject.getServiceTransactionId())){
						validationErrorFlag = true;
						reqStatus.put(WSConstants.ERROR_DESCRIPTION, "Error - ServiceTransactionId is Empty or null");
						reqStatus.put(WSConstants.ERROR_CODE, "600");
					}else if(!validationErrorFlag && StringUtils.isBlank(pedObject.getWorkItemId())){
						validationErrorFlag = true;
						reqStatus.put(WSConstants.ERROR_DESCRIPTION, "Error - WorkItemId is Empty or null");
						reqStatus.put(WSConstants.ERROR_CODE, "600");
					}else if(!validationErrorFlag && StringUtils.isBlank(pedObject.getPolicyNumber())){
						validationErrorFlag = true;
						reqStatus.put(WSConstants.ERROR_DESCRIPTION, "Error - PolicyNumber is Empty or null");
						reqStatus.put(WSConstants.ERROR_CODE, "600");
					}else if(!validationErrorFlag && StringUtils.isBlank(pedObject.getEndorsementStatus())){
						validationErrorFlag = true;
						reqStatus.put(WSConstants.ERROR_DESCRIPTION, "Error - EndorsementStatus is Empty or null");
						reqStatus.put(WSConstants.ERROR_CODE, "600");
					}else if(!validationErrorFlag && StringUtils.isBlank(pedObject.getGaeApprEndYN())){
						validationErrorFlag = true;
						reqStatus.put(WSConstants.ERROR_DESCRIPTION, "Error - GAE_END_APPR_YN is Empty or null");
						reqStatus.put(WSConstants.ERROR_CODE, "600");
					}else if(!validationErrorFlag && StringUtils.isBlank(pedObject.getEndosementEffectiveDate())){
						validationErrorFlag = true;
						reqStatus.put(WSConstants.ERROR_DESCRIPTION, "Error - EndosementEffectiveDate is Empty or null");
						reqStatus.put(WSConstants.ERROR_CODE, "600");
					}else if(!validationErrorFlag && StringUtils.isBlank(pedObject.getGaeApprUID())){
						validationErrorFlag = true;
						reqStatus.put(WSConstants.ERROR_DESCRIPTION, "Error - GAE_APPR_UID is Empty or null");
						reqStatus.put(WSConstants.ERROR_CODE, "600");
					}else if(!validationErrorFlag && StringUtils.isBlank(pedObject.getGaeApprName())){
						validationErrorFlag = true;
						reqStatus.put(WSConstants.ERROR_DESCRIPTION, "Error - GAE_APPR_NAME is Empty or null");
						reqStatus.put(WSConstants.ERROR_CODE, "600");
					}else if(!validationErrorFlag && StringUtils.isBlank(pedObject.getGaeApprDate())){
						validationErrorFlag = true;
						reqStatus.put(WSConstants.ERROR_DESCRIPTION, "Error - GAE_APPR_DT is Empty or null");
						reqStatus.put(WSConstants.ERROR_CODE, "600");
					}

					if(!validationErrorFlag && StringUtils.isBlank(docErrorMsg.toString())){
						String uploadStatus = dbService.updatePEDStatusService(pedObject);
						if(uploadStatus.equals("Successfully Updated")){
							reqStatus.put(WSConstants.ERROR_DESCRIPTION, WSConstants.STATUS_SUCCESS);
							reqStatus.put(WSConstants.ERROR_CODE, "200");
						}else{
							reqStatus.put(WSConstants.ERROR_DESCRIPTION, "Error"+" - "+uploadStatus);
							reqStatus.put(WSConstants.ERROR_CODE, "600");
						}
					}
				}else{
					reqStatus.put(WSConstants.ERROR_DESCRIPTION, "Error - Authentication Error, Invalid Authorization Key");
					reqStatus.put(WSConstants.ERROR_CODE, "600");
				}
			}else{
				reqStatus.put(WSConstants.ERROR_DESCRIPTION, "Error - Authentication Error, Invalid Authorization Key");
				reqStatus.put(WSConstants.ERROR_CODE, "600");
			}
		}catch(Exception exp){
			reqStatus.put(WSConstants.ERROR_DESCRIPTION, WSConstants.STATUS_FAILURE +"-"+exp.getMessage());
			reqStatus.put(WSConstants.ERROR_CODE, "600");
			log.info("Exception occured in Send PED Status Update service"+exp.getMessage());
		}	
		return reqStatus.toString();
    }
}